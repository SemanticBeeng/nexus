package nexus.op.properties

import nexus._
import nexus.algebra._
import nexus.algebra.syntax._
import nexus.op._
import nexus.prob._
import org.scalatest._

/**
 * @author Tongfei Chen
 */
class OpVSTests[T[_], R](gen: Stochastic[R])(implicit T: IsRealTensorK[T, R]) extends FunSuite {


  class Axis
  val len = 10

  class Prop(op: Op1[T[Axis], R], gen: Stochastic[T[Axis]]) extends ApproxProp[T[Axis], R](op, gen) {
    implicit val R = T.R

    def autoGrad(x: T[Axis]) = {
      val y = op.forward(x)
      op.backward(R.one, y, x)
    }

    def numGrad(x: T[Axis]) =
      T.tabulate(x.shape(0)) { i =>
        val δ = x(i) * relativeDiff
        val δx = T.tabulate[Axis](x.shape(0)) { j => if (j == i) δ else R.zero }
        R.div(op.forward(x + δx) - op.forward(x - δx), (δ * 2d))
      }

    def error(ag: T[Axis], ng: T[Axis]): R = L2Norm(ag - ng) / L2Norm(ag)
  }

  val ops = Seq(
    L1Norm.l1NormF[T, R, Axis],
    L2Norm.l2NormF[T, R, Axis]
  )

  for (op <- ops) {
    test(s"${op.name}'s automatic derivative is close to its numerical approximation on $T") {
      val prop = new Prop(op, Stochastic.from(T.tabulate(len)(_ => gen.sample)))
      assert(prop.passedCheck())
    }
  }

}
