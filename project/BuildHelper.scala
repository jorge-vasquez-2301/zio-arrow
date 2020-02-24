import sbt._
import sbt.Keys._
import sbtbuildinfo._
import BuildInfoKeys._
import dotty.tools.sbtplugin.DottyPlugin.autoImport._

object BuildHelper {
  private val Scala212        = "2.12.10"
  private val Scala213        = "2.13.1"
  private val SilencerVersion = "1.4.4"

  private val stdOptions = Seq(
    "-deprecation",
    "-encoding",
    "UTF-8",
    "-feature",
    "-unchecked"
  )

  private val std2xOptions = Seq(
    "-language:higherKinds",
    "-language:existentials",
    "-explaintypes",
    "-Yrangepos",
    "-Xlint:_,-missing-interpolator,-type-parameter-shadow",
    "-Ywarn-numeric-widen",
    "-Ywarn-value-discard"
  ) ++ customOptions

  private def propertyFlag(property: String, default: Boolean) =
    sys.props.get(property).map(_.toBoolean).getOrElse(default)

  private def customOptions =
    if (propertyFlag("fatal.warnings", false)) {
      Seq("-Xfatal-warnings")
    } else {
      Nil
    }

  def buildInfoSettings(packageName: String) =
    Seq(
      buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion, isSnapshot),
      buildInfoPackage := packageName,
      buildInfoObject := "BuildInfo"
    )

  val dottyVersion = "0.22.0-RC1"

  val dottySettings = Seq(
    // Keep this consistent with the version in .circleci/config.yml
    crossScalaVersions += dottyVersion,
    scalacOptions ++= {
      if (isDotty.value)
        Seq("-noindent")
      else
        Seq()
    },
    libraryDependencies := libraryDependencies.value.map(_.withDottyCompat(scalaVersion.value)),
    sources in (Compile, doc) := {
      val old = (Compile / doc / sources).value
      if (isDotty.value) {
        Nil
      } else {
        old
      }
    },
    parallelExecution in Test := {
      val old = (Test / parallelExecution).value
      if (isDotty.value) {
        false
      } else {
        old
      }
    }
  )

  def extraOptions(scalaVersion: String) =
    CrossVersion.partialVersion(scalaVersion) match {
      case Some((0, _)) =>
        Seq(
          "-language:implicitConversions",
          "-Xignore-scala2-macros"
        )
      case Some((2, 13)) =>
        Seq(
          "-Ywarn-unused:params,-implicits"
        ) ++ std2xOptions
      case Some((2, 12)) =>
        Seq(
          "-opt-warnings",
          "-Ywarn-extra-implicit",
          "-Ywarn-unused:_,imports",
          "-Ywarn-unused:imports",
          "-Ypartial-unification",
          "-Yno-adapted-args",
          "-Ywarn-inaccessible",
          "-Ywarn-infer-any",
          "-Ywarn-nullary-override",
          "-Ywarn-nullary-unit",
          "-Ywarn-unused:params,-implicits",
          "-Xfuture",
          "-Xsource:2.13",
          "-Xmax-classfile-name",
          "242"
        ) ++ std2xOptions
      case Some((2, 11)) =>
        Seq(
          "-Ypartial-unification",
          "-Yno-adapted-args",
          "-Ywarn-inaccessible",
          "-Ywarn-infer-any",
          "-Ywarn-nullary-override",
          "-Ywarn-nullary-unit",
          "-Xexperimental",
          "-Ywarn-unused-import",
          "-Xfuture",
          "-Xsource:2.13",
          "-Xmax-classfile-name",
          "242"
        ) ++ std2xOptions
      case _ => Seq.empty
    }

  def stdSettings(prjName: String) = Seq(
    name := s"$prjName",
    crossScalaVersions := Seq(Scala212, Scala213),
    scalaVersion in ThisBuild := Scala213,
    scalacOptions := stdOptions ++ extraOptions(scalaVersion.value),
    libraryDependencies ++= {
      if (isDotty.value)
        Seq("com.github.ghik" % "silencer-lib_2.13.1" % "1.6.0" % Provided)
      else
        Seq(
          "com.github.ghik" % "silencer-lib" % "1.4.4" % Provided cross CrossVersion.full,
          compilerPlugin("com.github.ghik" % "silencer-plugin" % "1.4.4" cross CrossVersion.full)
        )
    },
    incOptions ~= (_.withLogRecompileOnMacro(false))
  )
}
