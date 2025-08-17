name := """Agenda"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.16"

libraryDependencies += guice

// Dependencias para base de datos
libraryDependencies += "mysql" % "mysql-connector-java" % "8.0.33"
libraryDependencies += "io.ebean" % "ebean" % "13.6.0"
libraryDependencies += "io.ebean" % "ebean-querybean" % "13.6.0"
libraryDependencies += "io.ebean" % "ebean-migration" % "13.6.0"



// Para desarrollo y testing
libraryDependencies += "com.h2database" % "h2" % "2.2.224" % Test
