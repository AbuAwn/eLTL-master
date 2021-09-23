//Adnan Bouaouda Arafa, Sept. 2021, UMA
// Compara la duración de la ejecución de la misma fórmula eLTL con un número creciente de
// intervalos.
import org.apache.flink.api.scala._
import org.apache.flink.api.scala.utils._
import org.apache.flink.util.Collector
import org.apache.flink.configuration.{ConfigConstants, Configuration,RestOptions}
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.api.environment.LocalStreamEnvironment
import org.apache.flink.configuration.RestOptions
import org.uma._
import org.uma.eLTL._
import javax.script._
import org.scalameter._

object Rendimiento extends eLTL with App{
     override type T = (Long, Int)
     type TT = Int
     val path = os.pwd / "csv"

     val config = new Configuration()
     val benv = ExecutionEnvironment.createLocalEnvironmentWithWebUI(config)
     
     val _10      = benv.readCsvFile[T](path+"/10.csv", fieldDelimiter = ";")
     val _001in1M = benv.readCsvFile[T](path+"/001in1M.csv", fieldDelimiter = ";")
     val _010in1M = benv.readCsvFile[T](path+"/010in1M.csv", fieldDelimiter = ";")
     val _020in1M = benv.readCsvFile[T](path+"/020in1M.csv", fieldDelimiter = ";")
     val _030in1M = benv.readCsvFile[T](path+"/030in1M.csv", fieldDelimiter = ";")
     val _040in1M = benv.readCsvFile[T](path+"/040in1M.csv", fieldDelimiter = ";")
     val _050in1M = benv.readCsvFile[T](path+"/050in1M.csv", fieldDelimiter = ";")
     val _060in1M = benv.readCsvFile[T](path+"/060in1M.csv", fieldDelimiter = ";")
     val _070in1M = benv.readCsvFile[T](path+"/070in1M.csv", fieldDelimiter = ";")
     val _080in1M = benv.readCsvFile[T](path+"/080in1M.csv", fieldDelimiter = ";")
     val _090in1M = benv.readCsvFile[T](path+"/090in1M.csv", fieldDelimiter = ";")
     val _100in1M = benv.readCsvFile[T](path+"/100in1M.csv", fieldDelimiter = ";")
  
     println("Formula performance 1: A(Start, Stop) (E(Cond) (True))")
     println("Formula performance 2: A2(Start, Stop) (E2(Cond) (True))")
     println("Both formulae check if eventually exists number 2 in any interval (1, 3)")
     def Start: (TT => Boolean) = (e: TT) => {e == 1}
     def Stop : (TT => Boolean) = (e: TT) => {e == 3}
     def Cond : (TT => Boolean) = (e: TT) => {e == 2}
     println("The stream example contains:")
     println(_10.collect.map(e => e._2))
     println("We will increase the number of this interval from 10 to 100 and mesure the elapsed time.")
    
     val f = (d : org.apache.flink.api.scala.DataSet[(Long, Rendimiento.TT)]) => A(Start, Stop) (E(Cond) (True)) (d, 0, Long.MaxValue)
     val f2 = (d : org.apache.flink.api.scala.DataSet[(Long, Rendimiento.TT)]) => A2(Start, Stop) (E2(Cond) (True)) (d, 0, Long.MaxValue)

     for(i <- 1 to 1){

     val list = List(_001in1M, _010in1M, _020in1M, _030in1M, _040in1M, _050in1M,
                     _060in1M, _070in1M, _080in1M, _090in1M, _100in1M).map(b => measure(f(b)))
     val list2 = List(_001in1M, _010in1M, _020in1M, _030in1M, _040in1M, _050in1M,
                     _060in1M, _070in1M, _080in1M, _090in1M, _100in1M).map(b => measure(f2(b)))                     
     println(list)
     println(list2)
    }     
}