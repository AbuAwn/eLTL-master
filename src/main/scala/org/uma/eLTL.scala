package org.uma
//Adnan Bouaouda Arafa, Sept. 2021, UMA
import org.apache.flink.api.scala._
import org.apache.flink.api.scala.utils._
import org.apache.flink.util.Collector
import javax.script._
import scala.annotation.tailrec
import scalaz._
import Scalaz._
/**
 * Implements the "eLTL" logic
 */
trait eLTL {
    type T
    type batch = DataSet[T]
}
object eLTL {
    type T // use "override type" so specify the type in the user module
    def memoizeFnc[K, V](f: K => V): K => V = {
    val cache = collection.mutable.Map.empty[K, V]
        k =>
            cache.getOrElse(k, {
            cache update(k, f(k))
            cache(k)
            })
    }

    def apply[T](b: DataSet[(Long, T)], cond: T => Boolean): DataSet[(Long, Boolean)] 
            =  b.map{ e => (e._1, cond(e._2))}
    val mApply = Memo.immutableHashMapMemo{apply _ tupled}

    def parse[T](b: DataSet[(Long, T)], cond: T => Boolean): List[Long]
        = { lazy val lazy_b = apply(b, cond).filter{_._2}.map { pair => pair._1 }.collect.toList
            lazy_b
            //apply(b, cond).filter{_._2}.map { pair => pair._1 }
        }
                
    def intervalPQ(lp: List[Long], lq: List[Long],  ti: Long = 0, tf: Long = Long.MaxValue): List[(Long, Long)] = {
    try{    
        @tailrec def tIntervslPQ(Lp: List[Long], Lq: List[Long], acc: List[(Long, Long)] = List.empty[(Long, Long)]):List[(Long, Long)] = {
            val  Lqq = Lq.dropWhile(_ <= Lp.head)
            val  Lpp = Lp.dropWhile(_ <= Lqq.head)
            (Lpp, Lqq) match {
                case (List(), _) => acc ++ List((Lp.head, Lqq.head))
                case (_, List()) => acc ++ List((Lp.head, Lqq.head))
                case (_, _)   => tIntervslPQ(Lpp, Lqq.tail, acc ++ List((Lp.head, Lqq.head)))
            }
    }
        val  lpp = lp.sortWith(_ < _) filter (e => e >= ti  && e<= lq.max)
        val  lqq = lq.sortWith(_ < _) filter (e => e >= ti  && e<= tf)
        tIntervslPQ(lpp, lqq)
    }catch{case e: Exception => List.empty[(Long, Long)]}
    } 
    val mIntervalPQ = Memo.immutableHashMapMemo{intervalPQ _ tupled}
    def intervalP(lp: List[Long],  ti: Long = 0, tf: Long = Long.MaxValue): List[(Long, Long)]={
        val lpp = lp.sortWith(_ < _) filter (e => e >= ti  && e<= tf)
        lpp zip lpp}
    val mIntervalP  = Memo.immutableHashMapMemo{intervalP _ tupled}
    def intervals[T](b: DataSet[(Long, T)], p: T => Boolean, q: T => Boolean = null, ti: Long = 0, tf: Long = Long.MaxValue)
    =  q match{
            case null => mIntervalP(parse(b,p), ti, tf)
            case _    => mIntervalPQ(parse(b,p), parse(b,q), ti, tf)
        }
    def bPQ[T](b: DataSet[(Long, T)], i: List[(Long, Long)])
    = i.map(e => b.filter(x => (x._1 >= e._1) && (x._1 <= e._2)))
    def Phi[T](b: DataSet[(Long, T)], ti: Long = 0, tf: Long = Long.MaxValue): Boolean = ???
    def True[T](b: DataSet[(Long, T)], ti: Long = 0, tf: Long = Long.MaxValue): Boolean = true
    def PhiAll[T](p: T => Boolean)(b: DataSet[(Long, T)], ti: Long, tf: Long): Boolean = {
            val bb = b.filter(e => (e._1 >= ti) && (e._1 <= tf))
            bb.map(e => p(e._2)).collect.forall(_ == true)
    }
    def PhiK[T](K: Long)(p: T => Boolean, q: T => Boolean = null)
        = {(b: DataSet[(Long, T)], ti: Long, tf: Long) 
        => intervals(b, p, q, ti, tf).map(e => (e._2 - e._1 >= K)).
        reduceOption(_ || _).getOrElse(false)}
    def Neg[T](F: (DataSet[(Long, T)], Long, Long) => Boolean)
        ={(b: DataSet[(Long, T)], ti: Long, tf: Long) => !F(b, ti, tf)}
    def Or[T](F1: (DataSet[(Long, T)], Long, Long) => Boolean, 
            F2: (DataSet[(Long, T)], Long, Long) => Boolean)
        = {(b: DataSet[(Long, T)], ti: Long, tf: Long) 
        => F1(b, ti, tf) || F2(b, ti, tf)}  
    def U[T](p: T => Boolean, q: T => Boolean = null)
        (F1: (DataSet[(Long, T)], Long, Long) => Boolean,
        F2: (DataSet[(Long, T)], Long, Long) => Boolean)
        = {(b: DataSet[(Long, T)], ti: Long, tf: Long)
        => intervals(b, p, q, ti, tf).map(e => F1(b, ti, e._1) && F2(b, e._1, e._2)).
        reduceOption(_ || _).getOrElse(false)}                                 
    def E[T](p: T => Boolean, q: T => Boolean = null)
        (F: (DataSet[(Long, T)], Long, Long) => Boolean)
        = {(b: DataSet[(Long, T)], ti: Long, tf: Long) 
        =>  val I = intervals(b, p, q, ti, tf); I.map(e => F(b, e._1, e._2)).
        reduceOption(_ || _).getOrElse(false)}
    def E2[T](p: T => Boolean, q: T => Boolean = null)
            (F: (DataSet[(Long, T)], Long, Long) => Boolean)  = U(p, q)(True, F)
    def A[T](p: T => Boolean, q: T => Boolean = {(e: T) => true})
            (F: (DataSet[(Long, T)], Long, Long) => Boolean)
        = {(b: DataSet[(Long, T)], ti: Long, tf: Long)
        =>  val I = intervals(b, p, q, ti, tf); I.map(e => F(b, e._1, e._2)).
            reduceOption(_ && _).getOrElse(false)}
    def A2[T](p: T => Boolean, q: T => Boolean = null)
            (F: (DataSet[(Long, T)], Long, Long) => Boolean)  = Neg(E(p, q)(Neg(F)))
    val memInt = Memo.immutableHashMapMemo{intervals _ tupled}
    val intervals2 = memoizeFnc{intervals _ tupled}
}
