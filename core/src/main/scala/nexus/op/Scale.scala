package nexus.op

import nexus._

/**
 * Scales a tensor by a scalar.
 * @author Tongfei Chen
 * @since 0.1.0
 */
object Scale extends PolyOp2[ScaleF]

trait ScaleF[X1, X2, Y] extends Op2[X1, X2, Y] {
  def name = "Scale"
}

object ScaleF {

  implicit def tensor[T[_ <: $$], D, A <: $$](implicit env: Env[T, D]) = new ScaleF[T[A], T[$], T[A]] {
    def forward(x1: T[A], x2: T[$]) = x1 :* x2
    def backward1(dy: T[A], y: T[A], x1: T[A], x2: T[$]) = dy :* x2
    def backward2(dy: T[A], y: T[A], x1: T[A], x2: T[$]) = dy ⋅ x1
  }

}
