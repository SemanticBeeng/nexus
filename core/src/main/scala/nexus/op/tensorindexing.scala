package nexus.op

import nexus._
import nexus.algebra._
import nexus.algebra.syntax._
import scala.annotation._

/**
 * Wraps a scalar to a 0-dim tensor.
 * @author Tongfei Chen
 * @since 0.1.0
 */
object ScalarToTensor0 extends PolyOp1 {
  implicit def scalar[T[_], R](implicit T: IsRealTensorK[T, R]): F[R, T[Unit]] =
    new F[R, T[Unit]] {
      def name = "ScalarToTensor0"
      def tag(tx: Type[R]) = T.ground[Unit]
      def forward(x: R) = T.wrapScalar(x)
      def backward(dy: T[Unit], y: T[Unit], x: R) = T.unwrapScalar(dy)
    }
}

/**
 * Unwraps a 0-dim tensor to a scalar.
 * @author Tongfei Chen
 * @since 0.1.0
 */
object Tensor0ToScalar extends PolyOp1 {

  implicit def scalar[T[_], R](implicit T: IsRealTensorK[T, R]): F[T[Unit], R] =
    new F[T[Unit], R] {
      def name = "Tensor0ToTensor"
      def tag(tx: Type[T[Unit]]) = T.R
      def forward(x: T[Unit]) = T.unwrapScalar(x)
      def backward(dy: R, y: R, x: T[Unit]) = T.wrapScalar(dy)
  }

}


/**
 * Transforms each
 * @author Tongfei Chen
 */
object OneHot extends ParameterizedPolyOp1

/**
 * Slices a tensor along a specific axis.
 * @example {{{ SliceAlong(Width -> 3) }}}
 * @author Tongfei Chen
 * @since 0.1.0
 */
object SliceAlong extends ParameterizedPolyOp1

object Select extends PolyOp2 {

  implicit def selectF[TX[_], X, TZ[_], Z, A](implicit TE: IsTensorK[TX, X], TZ: IsIntTensorK[TZ, Z]) =
    new F[TX[A], TZ[A], TX[A]] {
      def name = "Select"
      def tag(tx1: Type[TX[A]], tx2: Type[TZ[A]]) = ???
      def forward(x1: TX[A], x2: TZ[A]) = ???
      def backward1(dy: TX[A], y: TX[A], x1: TX[A], x2: TZ[A]) = ???
      def backward2(dy: TX[A], y: TX[A], x1: TX[A], x2: TZ[A]) = ???
    }

}

object Diag

object Trace
