ThisBuild / resolvers ++= Seq(
    "Apache Development Snapshot Repository" at "https://repository.apache.org/content/repositories/snapshots/",
    Resolver.mavenLocal
)

name := "eLTL" ////Adnan Bouaouda Arafa, Sept. 2020, UMA

version := "1.0-SNAPSHOT"

organization := "org.uma"

ThisBuild / scalaVersion := "2.11.12"

val flinkVersion = "1.11.1"

val flinkDependencies = Seq(
  "org.apache.flink" %% "flink-clients" % flinkVersion % "provided",
  "org.apache.flink" %% "flink-scala" % flinkVersion % "provided",
  "org.apache.flink" %% "flink-streaming-scala" % flinkVersion % "provided",
  "org.slf4j" % "slf4j-simple" % "1.7.30" % "test",
  "org.scalactic" %% "scalactic" % "3.0.0",
  "org.scalatest" %% "scalatest" % "3.0.4" % "test",
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.1.2",
  "org.scala-lang" % "scala-compiler" % "2.11.12",
  "org.scalaz" %% "scalaz-core" % "7.3.5",
  "org.apache.flink" %% "flink-runtime-web" % "1.13.2" % Test
)

lazy val root = (project in file(".")).
  settings(
    libraryDependencies ++= flinkDependencies
  )
assembly / mainClass := Some("org.uma.Job")

// make run command include the provided dependencies
Compile / run  := Defaults.runTask(Compile / fullClasspath,
                                   Compile / run / mainClass,
                                   Compile / run / runner
                                  ).evaluated

// stays inside the sbt console when we press "ctrl-c" while a Flink programme executes with "run" or "runMain"
Compile / run / fork := true
Global / cancelable := true

// exclude Scala library from assembly
assembly / assemblyOption  := (assembly / assemblyOption).value.copy(includeScala = false)

// scalameter
resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots"
libraryDependencies += "com.storm-enroute" %% "scalameter-core" % "0.18"
testFrameworks += new TestFramework(
  "org.scalameter.ScalaMeterFramework")
logBuffered := false
parallelExecution in Test := false
fork := true
outputStrategy := Some(StdoutOutput)
connectInput := true
