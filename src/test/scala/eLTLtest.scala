//Adnan Bouaouda Arafa, Sept. 2021, UMA
import org.scalatest._
import org.apache.flink.api.scala._
import org.apache.flink.api.scala.utils._
import org.apache.flink.util.Collector
import org.uma._
import org.uma.eLTL._

class eLTLtest extends FunSuite with eLTL{
    override type T = (Long, String)
    val benv = ExecutionEnvironment.getExecutionEnvironment
    // val b = benv.fromElements(("ps", "ps", "srp", "ps", "pqs", "prs", "ps", "pps", "psr", "pqs", "psq", "pqs", "r", "t")
    val b = benv.fromElements((1L,"ps"), (2L,"ps") , (3L,"srp"), (4L,"ps"), (5L,"pqs"), (6L,"prs"), (7L,"ps"), 
                            (8L,"pps"), (9L,"psr"), (10L,"pqs"), (11L,"psq"), (12L,"pqs"), (13L,"r"), (14L,"t"))
    def p   : (String => Boolean) = (e: String) => {e.contains("p")}
    def q   : (String => Boolean) = (e: String) => {e.contains("q")}
    def r   : (String => Boolean) = (e: String) => {e.contains("r")}
    def s   : (String => Boolean) = (e: String) => {e.contains("s")}
    def t   : (String => Boolean) = (e: String) => {e.contains("t")}
    def l   : (String => Boolean) = (e: String) => {e.contains("l")}
    def TRUE: (String => Boolean) = (e: String) => true 
    test("TC 01: apply") {
        assert(apply(b, p).collect === apply(b, s).collect)
    }
    // test("TC 02: index function") {
    //     val bP = apply(b, p)
    //     val bS = apply(b, s)
    //     assert(index(bP).collect === index(bS).collect)
    // }
    test("TC 03: parse") {
        assert(parse(b, p) === parse(b, s))
    }
    test("TC 04: intervallPQ") {
        val l1: List[Long] = List(2, 3, 4, 10, 11)
        val l2: List[Long] = List(1, 2, 6, 12, 15)
        assert(intervalPQ(l1, l2) === List((2, 6), (10, 12)))
    }
    test("TC 05: Interval formula") {
        assert(PhiAll(p)(b, 0, 3) === true)
    }
    test("TC 06: UntilPQ") {
        def bU = benv.fromElements((1L,"r"), (2L,"prs"), (3L,"sr"), (4L,"sr"), (5L,"qrs"), (6L,"r"), (7L,"s"),
                                   (8L,"ps"), (9L,"sr"), (10L,"qrs"), (11L,"s"), (12L,"q"), (13L,"r"), (14L,"t"))
        assert(U(p,q)(PhiAll(r), PhiAll(s))(bU, 0, bU.count.toLong) === true)
    }
    test("TC 07: UntilP: " + intervals(benv.fromElements((1L,"s"), (2L,"srp"), (3L,"s")), p).toString) {
        def bU = benv.fromElements((1L,"s"), (2L,"srp"), (3L,"s"), (4L,"s"), (5L,"s"), (6L,"srp"),
         (7L,"s"), (8L,"s"), (9L,"sr"), (10L,"st"), (11L,"sq"), (12L,"rsp"), (13L,""), (14L,""))
        assert(U(p)(PhiAll(s), PhiAll(r))(bU, 0, Long.MaxValue) === true)
    }
    test("TC 08: EventuallyPQ, def. directa vs def. recursiva"){
        def bE = benv.fromElements((1L,"r"), (2L,"prs"), (3L,"sr"), (4L,"sr"), (5L,"qrs"), (6L,"s"), (7L,"s"), 
                                    (8L,"ps"), (9L,"sr"), (10L,"qrs"), (11L,"s"), (12L,"q"), (13L,"r"), (14L,"t"))
        assert(E(p,q)(PhiAll(r))(bE, 0, bE.count.toLong) === E2(p,q)(PhiAll(r))(bE, 0, bE.count.toLong))
    }
    test("TC 09: EventuallyP, def. directa vs def. recursiva") {
        def bE = benv.fromElements((1L,"r"), (2L,"prs"), (3L,"sr"), (4L,"sr"), (5L,"qrs"), (6L,"r"), (7L,"s"), 
                                    (8L,"ps"), (9L,"sr"), (10L,"qrs"), (11L,"s"), (12L,"q"), (13L,"r"), (14L,"t"))
        assert(E(p)(PhiAll(r))(bE, 0, bE.count.toLong) === E2(p)(PhiAll(r))(bE, 0, bE.count.toLong))
    }
        test("TC 10: AlwaysPQ, def. directa vs def. recursiva") {
        def bA = benv.fromElements((1L,"r"), (2L,"prs"), (3L,"sr"), (4L,"sr"), (5L,"qrs"), (6L,"r"), (7L,"s"), 
                                    (8L,"ps"), (9L,"sr"), (10L,"qrs"), (11L,"s"), (12L,"q"), (13L,"r"), (14L,"t"))
        assert(A(p,q)(PhiAll(r))(bA, 0, Long.MaxValue) === A2(p,q)(PhiAll(r))(bA, 0, Long.MaxValue))
    }
    test("TC 11: AlwaysP, def. directa vs def. recursiva") {
        def bA = benv.fromElements((1L,"r"), (2L,"prs"), (3L,"sr"), (4L,"sr"), (5L,"qrs"), (6L,"r"), (7L,"s"), 
                                    (8L,"ps"), (9L,"sr"), (10L,"qrs"), (11L,"s"), (12L,"q"), (13L,"r"), (14L,"t"))
        assert(A(p)(PhiAll(r))(bA, 0, Long.MaxValue) === A2(p)(PhiAll(r))(bA, 0, Long.MaxValue))
    }
}