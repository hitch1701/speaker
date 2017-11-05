
name := "speaker"

organization := "creektiger"

version := "0.1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "de.dfki.mary" % "marytts-lang-de" % "5.2" % "provided"
)

enablePlugins(JavaAppPackaging)
