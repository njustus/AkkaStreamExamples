lazy val root = (project in file(".")).
  settings(
  name := "StreamExamples",
  version := "0.1",
  scalaVersion := "2.11.8")

resourceDirectory in Compile := baseDirectory.value / "rsc"
scalaSource in Compile := baseDirectory.value / "src"

libraryDependencies ++= Seq("com.typesafe.akka" %% "akka-stream" % "2.4.8")
