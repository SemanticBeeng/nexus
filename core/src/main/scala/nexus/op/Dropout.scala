package nexus.op

import nexus._
import nexus.impl._

/**
 * Dropout.
 * @author Tongfei Chen
 * @since 0.1.0
 */
case class Dropout(parameter: Double) extends ParaPolyOp1[Double, DropoutF]

trait DropoutF[P, X, Y] extends (P => Op1[X, Y])

object DropoutF {

  implicit def tensor[T[_ <: $$], D, A <: $$](implicit ops: TypedMathOps[T, D]) = new DropoutF[Double, T[A], T[A]] {
    def apply(rate: Double) = new Op1[T[A], T[A]] {
      def name = s"Dropout[$rate]"
      def _ops = ops.ground[A]
      def forward(x: T[A]) = x // TODO: Not implemented!
      def backward(dy: T[A], y: T[A], x: T[A]) = dy // as if it doesn't exist
    }
  }

}
