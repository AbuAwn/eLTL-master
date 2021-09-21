//Adnan Bouaouda Arafa, Sept. 2021, UMA
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
     
     val config = new Configuration()
     val benv = ExecutionEnvironment.createLocalEnvironmentWithWebUI(config)
     val port =
          if (config.contains(RestOptions.BIND_PORT))
            config.getString(RestOptions.BIND_PORT)
          else if (config.contains(RestOptions.PORT)) {
            config.getInteger(RestOptions.PORT)
          } else RestOptions.PORT.defaultValue()
     val host = Option(config.getString(RestOptions.BIND_ADDRESS)).getOrElse("localhost")
     log.info(s"Enabled local Flink Web UI at $host:$port")
     config.setBoolean(ConfigConstants.LOCAL_START_WEBSERVER, true)
     config.setString(ConfigConstants.JOB_MANAGER_WEB_LOG_PATH_KEY, "file:///C:/Users/aba/git/flink/eltl/test/dummyLogFile.txt")
     // config.setInteger(RestOptions.PORT, 8081)
     // config.setString(RestOptions.BIND_ADDRESS, "0.0.0.0")

     
     // def env: StreamExecutionEnvironment  = {
     //      val config = new Configuration()
     //      // start the web dashboard
     //      config.setBoolean(ConfigConstants.LOCAL_START_WEBSERVER, true)
     //      // required to start the web dashboard
     //      config.setString(ConfigConstants.JOB_MANAGER_WEB_LOG_PATH_KEY, "./data/dummyLogFile.txt")

     //      // create a local stream execution environment
     //      new LocalStreamEnvironment(config)
     // }
     //val lista = benv.fromElements("0", "1", "2", "3", "4", "5p", "6r", "7", "8", "9", "10l")
     val _10 = benv.readCsvFile[T]("file:///C:/Users/aba/git/flink/eltl/test/10.csv", fieldDelimiter = ";")
     val _001in1M = benv.readCsvFile[T]("file:///C:/Users/aba/git/flink/eltl/test/001in1M.csv", fieldDelimiter = ";")
     val _010in1M = benv.readCsvFile[T]("file:///C:/Users/aba/git/flink/eltl/test/010in1M.csv", fieldDelimiter = ";")
     val _020in1M = benv.readCsvFile[T]("file:///C:/Users/aba/git/flink/eltl/test/020in1M.csv", fieldDelimiter = ";")
     val _030in1M = benv.readCsvFile[T]("file:///C:/Users/aba/git/flink/eltl/test/030in1M.csv", fieldDelimiter = ";")
     val _040in1M = benv.readCsvFile[T]("file:///C:/Users/aba/git/flink/eltl/test/040in1M.csv", fieldDelimiter = ";")
     val _050in1M = benv.readCsvFile[T]("file:///C:/Users/aba/git/flink/eltl/test/050in1M.csv", fieldDelimiter = ";")
     val _060in1M = benv.readCsvFile[T]("file:///C:/Users/aba/git/flink/eltl/test/060in1M.csv", fieldDelimiter = ";")
     val _070in1M = benv.readCsvFile[T]("file:///C:/Users/aba/git/flink/eltl/test/070in1M.csv", fieldDelimiter = ";")
     val _080in1M = benv.readCsvFile[T]("file:///C:/Users/aba/git/flink/eltl/test/080in1M.csv", fieldDelimiter = ";")
     val _090in1M = benv.readCsvFile[T]("file:///C:/Users/aba/git/flink/eltl/test/090in1M.csv", fieldDelimiter = ";")
     val _100in1M = benv.readCsvFile[T]("file:///C:/Users/aba/git/flink/eltl/test/100in1M.csv", fieldDelimiter = ";")
     //println(lista.collect)
     // type  = Int
     println("Ejemplo 1: A(Start, Stop) (E(Cond) (True))")
     def Start: (TT => Boolean) = (e: TT) => {e == 1}
     def Stop : (TT => Boolean) = (e: TT) => {e == 3}
     def Cond : (TT => Boolean) = (e: TT) => {e == 2}

     println(A(Start, Stop) (E(Cond) (True)) (_10, 0, Long.MaxValue))

     val time001 = measure {println(A2(Start, Stop) (E2(Cond) (True)) (_001in1M, 0, Long.MaxValue))}
     val time010 = measure {println(A2(Start, Stop) (E2(Cond) (True)) (_010in1M, 0, Long.MaxValue))}
     val time020 = measure {println(A2(Start, Stop) (E2(Cond) (True)) (_020in1M, 0, Long.MaxValue))}
     val time030 = measure {println(A2(Start, Stop) (E2(Cond) (True)) (_030in1M, 0, Long.MaxValue))}
     val time040 = measure {println(A2(Start, Stop) (E2(Cond) (True)) (_040in1M, 0, Long.MaxValue))}
     val time050 = measure {println(A2(Start, Stop) (E2(Cond) (True)) (_050in1M, 0, Long.MaxValue))}
     val time060 = measure {println(A2(Start, Stop) (E2(Cond) (True)) (_060in1M, 0, Long.MaxValue))}
     val time070 = measure {println(A2(Start, Stop) (E2(Cond) (True)) (_070in1M, 0, Long.MaxValue))}
     val time080 = measure {println(A2(Start, Stop) (E2(Cond) (True)) (_080in1M, 0, Long.MaxValue))}
     val time090 = measure {println(A2(Start, Stop) (E2(Cond) (True)) (_090in1M, 0, Long.MaxValue))}
     val time100 = measure {println(A2(Start, Stop) (E2(Cond) (True)) (_100in1M, 0, Long.MaxValue))}

     println(s"Total time 001in1M.csv: $time001")
     println(s"Total time 010in1M.csv: $time010")
     println(s"Total time 020in1M.csv: $time020")
     println(s"Total time 030in1M.csv: $time030")
     println(s"Total time 040in1M.csv: $time040")
     println(s"Total time 050in1M.csv: $time050")
     println(s"Total time 060in1M.csv: $time060")
     println(s"Total time 070in1M.csv: $time070")
     println(s"Total time 080in1M.csv: $time080")
     println(s"Total time 090in1M.csv: $time090")
     println(s"Total time 010in1M.csv: $time100")
     
}