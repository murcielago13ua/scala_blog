val projectId = "back_end"

lazy val model = ProjectRef(file("../model"), "modelJVM")

lazy val backEnd = Project(id = projectId, base = file("."))
  .settings(
    version := "0.1",
    scalaVersion := "2.11.8",
    resolvers += "Maven" at "https://repo1.maven.org/maven2/",
    libraryDependencies ++= {
      val monocleV = "1.4.0"
      val macwireV = "2.3.0"
      val akkaV = "2.4.17"
      val akkaHttpV = "10.0.3"
      Seq(
        // akka
        "com.typesafe.akka" %% "akka-actor" % akkaV,
        "com.typesafe.akka" %% "akka-http-core" % akkaHttpV,
        "com.typesafe.akka" %% "akka-http" % akkaHttpV,
        "com.softwaremill.akka-http-session" %% "core" % "0.5.0",
        "com.typesafe.akka" %% "akka-slf4j" % akkaV,
        // scalaTest
        "org.scalatest" %% "scalatest" % "3.0.0" % "test",

        // server based on akka-http
        "org.julienrf" %% "endpoints-akka-http-server" % "0.2.0",
        "org.julienrf" %% "endpoints-akka-http-server-circe" % "0.2.0",
        "com.softwaremill.akka-http-session" %% "core" % "0.4.0",
        "de.heikoseeberger" %% "akka-http-circe" % "1.15.0",

        //DB
        "org.postgresql" % "postgresql" % "9.4.1212.jre7",
        "com.typesafe.slick" %% "slick" % "3.2.0-M2",
        "com.typesafe.slick" %% "slick-hikaricp" % "3.2.0",
        "com.github.tminglei" %% "slick-pg" % "0.15.0",
        "com.github.tminglei" %% "slick-pg_circe-json" % "0.15.0",

        //DI
        "com.softwaremill.macwire" %% "macros" % macwireV % "provided",
        "com.softwaremill.macwire" %% "util" % macwireV,

        //Optics
        "com.github.julien-truffaut" %%  "monocle-core"  % monocleV,
        "com.github.julien-truffaut" %%  "monocle-macro" % monocleV
      )
    }
  )
  .dependsOn(model)