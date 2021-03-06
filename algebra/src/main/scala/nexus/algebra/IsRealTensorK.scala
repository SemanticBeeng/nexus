package nexus.algebra

import nexus.algebra.typelevel._
import nexus.algebra.util._

import scala.annotation._

/**
 * Typeclass that describes the algebraic structures on tensors whose elements are real numbers.
 * @author Tongfei Chen
 * @since 0.1.0
 */
@implicitNotFound("Cannot prove that ${T} is a typed tensor of ${R}.")
trait IsRealTensorK[T[_], R] extends IsTensorK[T, R] with GradK[T] { self =>

  val H: IsUntypedRealTensor[H, R]

  implicit val R: IsReal[R]
  def elementType = R

  def newGaussianTensor[A](μ: Double, σ2: Double, shape: Array[Int]): T[A]

  def zeroBy[A](x: T[A]): T[A]

  def add[A](x1: T[A], x2: T[A]): T[A] =
    typeWith[A](H.add(untype(x1), untype(x2)))

  def addI[A](x1: T[A], x2: T[A]) =
    H.addI(untype(x1), untype(x2))

  def addS[A](x: T[A], u: R) =
    typeWith[A](H.addS(untype(x), u))

  def neg[A](x: T[A]) =
    typeWith[A](H.neg(untype(x)))

  def sub[A](x1: T[A], x2: T[A]): T[A] =
    typeWith[A](H.sub(untype(x1), untype(x2)))

  def subS[A](x: T[A], u: R) =
    typeWith[A](H.subS(untype(x), u))

  def eMul[A](x1: T[A], x2: T[A]): T[A] =
    typeWith[A](H.eMul(untype(x1), untype(x2)))

  def eDiv[A](x1: T[A], x2: T[A]): T[A] =
    typeWith[A](H.eDiv(untype(x1), untype(x2)))

  def scale[A](x: T[A], u: R): T[A] =
    typeWith[A](H.scale(untype(x), u))

  def eInv[A](x: T[A]): T[A] =
    typeWith[A](H.eInv(untype(x)))

  def eSqr[A](x: T[A]): T[A] =
    typeWith[A](H.eSqr(untype(x)))

  def eSqrt[A](x: T[A]): T[A] =
    typeWith[A](H.eSqrt(untype(x)))

  def transpose[A, B](x: T[(A, B)]): T[(B, A)] =
    typeWith[(B, A)](H.transpose(untype(x)))

  def eLog[A](x: T[A]): T[A] = typeWith[A](H.eLog(untype(x)))
  def eExp[A](x: T[A]): T[A] = typeWith[A](H.eExp(untype(x)))
  def eLog1p[A](x: T[A]): T[A] = typeWith[A](H.eLog1p(untype(x)))
  def eExpm1[A](x: T[A]): T[A] = typeWith[A](H.eExpm1(untype(x)))

  def eSin[A](x: T[A]): T[A] = typeWith[A](H.eSin(untype(x)))
  def eCos[A](x: T[A]): T[A] = typeWith[A](H.eCos(untype(x)))
  def eTan[A](x: T[A]): T[A] = typeWith[A](H.eTan(untype(x)))

  def sigmoid[A](x: T[A]): T[A] = typeWith[A](H.eSigmoid(untype(x)))

  def relu[A](x: T[A]): T[A] = typeWith[A](H.eReLU(untype(x)))

  def eAbs[A](x: T[A]): T[A] = typeWith[A](H.eAbs(untype(x)))
  def eSgn[A](x: T[A]): T[A] = typeWith[A](H.eSgn(untype(x)))

  def pos[A](x: T[A]): T[A] = typeWith[A](H.eIsPos(untype(x)))

  def sum(x: T[_]): R = H.sum(untype(x))

  def mmMul[A, B, C](x: T[(A, B)], y: T[(B, C)]): T[(A, C)] =
    typeWith[(A, C)](H.mmMul(untype(x), untype(y)))

  def mvMul[A, B](x: T[(A, B)], y: T[B]): T[A] =
    typeWith[A](H.mvMul(untype(x), untype(y)))

  def vvMul[A, B](x: T[A], y: T[B]): T[(A, B)] = typeWith[(A, B)](H.vvMul(untype(x), untype(y)))

  def dot[A](x: T[A], y: T[A]): R = H.dot(untype(x), untype(y))

  def contract[A, B, C](x: T[A], y: T[B])(implicit sd: SymDiff.Aux[A, B, C]): T[C] =
    typeWith[C](H.tMul(untype(x), untype(y), sd.matchedIndices))

  def ground[A]: IsRealTensor[T[A], R] =
    new IsRealTensor[T[A], R] {
      def elementType = self.elementType
      def mutable = true
      def zeroBy(x: T[A]) = self.zeroBy(x)
      def add(x1: T[A], x2: T[A]) = self.add(x1, x2)
      def addS(x1: T[A], x2: Double) = self.addS(x1, R.fromDouble(x2))
      def addI(x1: T[A], x2: T[A]): Unit = self.addI(x1, x2)
      def sub(x1: T[A], x2: T[A]) = self.sub(x1, x2)
      def neg(x: T[A]) = self.neg(x)
      def eMul(x1: T[A], x2: T[A]) = self.eMul(x1, x2)
      def eDiv(x1: T[A], x2: T[A]) = self.eDiv(x1, x2)
      def scale(x: T[A], k: Double) = self.scale(x, R.fromDouble(k))
      def eSqrt(x: T[A]) = self.eSqrt(x)
    }

}


trait IsRealTensor[T, R] extends IsTensor[T, R] with Grad[T] {
  def elementType: IsReal[R]
}
