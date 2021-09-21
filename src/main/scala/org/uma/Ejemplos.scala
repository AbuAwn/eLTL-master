//Adnan Bouaouda Arafa, Sept. 2021, UMA
import org.apache.flink.api.scala._
import org.apache.flink.api.scala.utils._
import org.apache.flink.util.Collector
import org.uma._
import org.uma.eLTL._
import javax.script._

object Ejemplos extends eLTL with App{
    override type T = (Long, String)
    val benv = ExecutionEnvironment.getExecutionEnvironment
    def s: (String => Boolean) = (e: String) => {e.contains("s")}
    def t: (String => Boolean) = (e: String) => {e.contains("t")}
    /////////////////////////////
    println("Ejemplo 1: A(vStart, vStop) (E(firstP) (True))")
    def vStart: (String => Boolean) = (e: String) => {e.contains("vStart")}
    def vStop : (String => Boolean) = (e: String) => {e.contains("vStop")}
    def firstP: (String => Boolean) = (e: String) => {e.contains("firstP")}
    def b1 = benv.fromElements((1L,"0"), (2L,"1vStart"), (3L,"2"), (4L,"3firstP"), 
               (5L,"4vStop"), (6L,"5p"), (7L,"6r"), (8L,"7vStart"), (9L,"8"), (10L,"9"), (11L,"10l"),
               (12L,"11"), (13L,"12vStart"), (14L,"13"), (15L,"14firstP"), (16L,"15"), (17L,"16"),
               (18L,"17vStop"), (19L,"19"), (20L,"20q"), (21L,"..."))
    val forumla1 = A(vStart, vStop) (E(firstP) (True)) (b1, 0, Long.MaxValue)
    println(forumla1)
    /////////////////////////////
    println("Ejemplo 2: A(vStart, vStop)(A(High, Low)(PhiAll(rssi)))(b2, 0, 21)")
    def b2 = benv.fromElements((1L,"0"), (2L,"1"), (3L,"2"), (4L,"3vStart"), (5L,"4"), (6L,"5p"), (7L,"6_High_rssi"),
                               (8L,"7rssi"), (9L,"8Low_rssi"), (10L,"9vStop"), (11L,"10l"), (12L,"11"),
                               (13L,"12"), (14L,"13vStart"),(15L,"14High_rssi"),(16L,"15rssi"),
                               (17L,"16rssi"), (18L,"17Low_rssi"), (19L,"19vStop"), (20L,"20q"), (21L,"..."))
    def High: (String => Boolean) = (e: String) => {e.contains("High")}
    def Low : (String => Boolean) = (e: String) => {e.contains("Low")}
    def rssi: (String => Boolean) = (e: String) => {e.contains("rssi")}
    val forumla2 = A(vStart, vStop)(A(High, Low)(PhiAll(rssi)))(b2, 0, 21)
    println(intervals(b2, vStart, vStop))
    println(intervals(b2, High, Low))
    println(forumla2)
    /////////////////////////////
    println("Ejemplo 3: A(p, q)(Phi10 -> E(r, l) Phi3) == A(p,q)(Or(Neg(Phi10), E(r,l)(Phi3)))")
    def p: (String => Boolean) = (e: String) => {e.contains("p")}
    def q: (String => Boolean) = (e: String) => {e.contains("q")}
    def r: (String => Boolean) = (e: String) => {e.contains("r")}
    def l: (String => Boolean) = (e: String) => {e.contains("l")}
    def b3 = benv.fromElements((1L,"0"), (2L,"1"), (3L,"2"), (4L,"3"), (5L,"4"), (6L,"5p"), (7L,"6r"),
          (8L,"7"), (9L,"8"), (10L,"9"), (11L,"10l"), (12L,"11"), (13L,"12"), (14L,"13"), (15L,"14"), (16L,"15"), 
          (17L,"16"), (18L,"17"), (19L,"19"), (20L,"20q"), (21L,"..."))
    val forumla3 = A(p, q)(Or(Neg(PhiK(10)(p,q)), E(r, l)(PhiK(3)(r,l))))(b3, 0, 21)
    println(forumla3)
}