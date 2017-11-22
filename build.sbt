
name := "speaker"

organization := "creektiger"

version := "0.1.0"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "de.dfki.mary" % "marytts-lang-de" % "5.2" % "provided"
)

enablePlugins(JavaAppPackaging)

scriptClasspath ~= (cp => Seq(
    "../../mary",
    "../../mary/lib",
    "../../mary/lib/marytts-lang-de-5.2.jar",
    "../../mary/lib/voice-dfki-pavoque-styles-5.2.jar",
    "../../mary/lib/marytts-runtime-5.2-jar-with-dependencies.jar"
) ++: cp)