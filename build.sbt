name := """play-workshop"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.13.1"

libraryDependencies += guice
// https://mvnrepository.com/artifact/io.lettuce/lettuce-core
libraryDependencies += "io.lettuce" % "lettuce-core" % "5.3.5.RELEASE"
