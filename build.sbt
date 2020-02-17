import sbt._
import Keys._

import BuildHelper._

inThisBuild(
  List(
    organization := "dev.zio",
    homepage := Some(url("https://zio.github.io/zio-arrow/")),
    licenses := List("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
    developers := List(
      Developer(
        "jdegoes",
        "John De Goes",
        "john@degoes.net",
        url("http://degoes.net")
      )
    ),
    pgpPassphrase := sys.env.get("PGP_PASSWORD").map(_.toArray),
    pgpPublicRing := file("/tmp/public.asc"),
    pgpSecretRing := file("/tmp/secret.asc"),
    scmInfo := Some(
      ScmInfo(url("https://github.com/zio/zio-arrow/"), "scm:git:git@github.com:zio/zio-arrow.git")
    )
  )
)

ThisBuild / publishTo := sonatypePublishToBundle.value

val zioVersion = "1.0.0-RC17"
libraryDependencies ++= Seq(
  "dev.zio" %% "zio"          % zioVersion,
  "dev.zio" %% "zio-test"     % zioVersion % "test",
  "dev.zio" %% "zio-test-sbt" % zioVersion % "test"
)

testFrameworks := Seq(new TestFramework("zio.test.sbt.ZTestFramework"))

lazy val root = (project in file("."))
  .settings(stdSettings("zio-arrow"))
  .settings(buildInfoSettings("zio-arrow"))
// .enablePlugins(BuildInfoPlugin)

lazy val bench = (project in file("bench"))
  .settings(stdSettings("bench"))
  .settings(scalacOptions --= Seq("-Ywarn-value-discard"))
  .enablePlugins(JmhPlugin)
  .dependsOn(root)

lazy val graphDeps = libraryDependencies ++= Seq(
  "org.scala-graph" %% "graph-core" % "1.13.1"
)

lazy val examples = (project in file("examples"))
  .settings(stdSettings("examples"))
  .settings(buildInfoSettings("examples"))
  .settings(graphDeps)
  .dependsOn(root)

lazy val docs = project
  .in(file("zio-arrow-docs"))
  .settings(
    skip.in(publish) := true,
    moduleName := "zio-arrow-docs",
    scalacOptions -= "-Yno-imports",
    scalacOptions -= "-Xfatal-warnings",
    libraryDependencies ++= Seq(
      "dev.zio" %% "zio" % zioVersion
    ),
    unidocProjectFilter in (ScalaUnidoc, unidoc) := inProjects(root),
    target in (ScalaUnidoc, unidoc) := (baseDirectory in LocalRootProject).value / "website" / "static" / "api",
    cleanFiles += (target in (ScalaUnidoc, unidoc)).value,
    docusaurusCreateSite := docusaurusCreateSite.dependsOn(unidoc in Compile).value,
    docusaurusPublishGhpages := docusaurusPublishGhpages.dependsOn(unidoc in Compile).value
  )
  .dependsOn(root)
  .enablePlugins(MdocPlugin, DocusaurusPlugin, ScalaUnidocPlugin)

addCommandAlias("com", "compile")
addCommandAlias("rel", "reload")
addCommandAlias("bench", "bench/jmh:run -i 1 -wi 1 -f1 -t1 ;.*BubbleSortBenchmark;.*ArrayFillBenchmark")
addCommandAlias("fmt", "all scalafmtSbt scalafmt test:scalafmt")
addCommandAlias("check", "all scalafmtSbtCheck scalafmtCheck test:scalafmtCheck")
