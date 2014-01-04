[![Build Status](https://travis-ci.org/JoshRosen/scala-type-providers.png?branch=master)](https://travis-ci.org/JoshRosen/scala-type-providers)

This repository contains experiments with [Scala type providers](http://docs.scala-lang.org/overviews/macros/typeproviders.html).

As a running example, I'll try to implement a DSL for declaring simple database table objects and capturing queries against them (imagine a lightweight version of [Slick's _lifted embedding_](http://slick.typesafe.com/doc/1.0.1/lifted-embedding.html)).  I'd like to be able to write something like

```
val users = new Table(Col[String]("name"), Col[Int]("age"))
```

to declare `users` table with `name` and `age` fields that contain column objects for use in the query DSL:

```
users.select(users.name == "Josh" && users.age >= 23)
```

The Table objects should have a canonical ordering of their columns.  For example, my `users` table should have a record type of `(String, Int)`.

The SBT project structure is based on [scalamacros/sbt-example](https://github.com/scalamacros/sbt-example).
