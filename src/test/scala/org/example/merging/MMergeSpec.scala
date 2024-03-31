package org.example.merging

import org.json4s.*
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.matchers.should.Matchers.*
import org.json4s.native.JsonMethods.*

import scala.io.Source

class MMergeSpec extends AnyFunSuite {
  val examplesDirectory: String = "/merging/examples"
  val expectedDirectory: String = "/merging/expected"


  test("Merge two JObject instances") {
    val json1 = JObject("name" -> JString("John"), "age" -> JInt(30))
    val json2 = JObject("name" -> JString("Alice"), "city" -> JString("New York"))

    val merged = MMerge.merge(json1, json2)

    val expected = JObject("name" -> JArray(List(JString("John"), JString("Alice"))), "age" -> JInt(30), "city" -> JString("New York"))
    assert(merged === expected)
  }

  test("Merge two JArray instances") {
    val json1 = JArray(List(JString("John"), JInt(30)))
    val json2 = JArray(List(JString("Alice"), JString("New York")))

    val merged = MMerge.merge(json1, json2)

    val expected = JArray(List(JString("John"), JInt(30), JString("Alice"), JString("New York")))
    assert(merged === expected)
  }

  test("Merge JObject with JArray") {
    val json1 = JObject("name" -> JString("John"), "age" -> JInt(30))
    val json2 = JArray(List(JString("Alice"), JString("New York")))
    val merged = MMerge.merge(json1, json2)
    val expected = JArray(List(JObject("name" -> JString("John"), "age" -> JInt(30)), JString("Alice"), JString("New York")))
    assert(merged === expected)
  }

  test("Merge JArray with JObject") {
    val json1 = JArray(List(JString("Alice"), JString("New York")))
    val json2 = JObject("name" -> JString("John"), "age" -> JInt(30))

    val merged = MMerge.merge(json1, json2)

    val expected = JArray(List(JString("Alice"), JString("New York"), JObject("name" -> JString("John"), "age" -> JInt(30))))
    assert(merged === expected)
  }

  test("Merge two Atomic values") {
    val json1 = JString("John")
    val json2 = JString("Alice")

    val merged = MMerge.merge(json1, json2)

    val expected = JArray(List(JString("John"), JString("Alice")))
    assert(merged === expected)
  }

  test("Merge Atomic value with JObject") {
    val json1 = JString("John")
    val json2 = JObject("name" -> JString("Alice"), "age" -> JInt(30))
    val merged = MMerge.merge(json1, json2)
    val expected = JArray(List(JString("John"), JObject("name" -> JString("Alice"), "age" -> JInt(30))))
    assert(merged === expected)
  }

  test("Merge Atomic value with JArray") {
    val json1 = JString("John")
    val json2 = JArray(List(JString("Alice"), JString("New York")))

    val merged = MMerge.merge(json1, json2)

    val expected = JArray(List(JString("John"), JString("Alice"), JString("New York")))
    assert(merged === expected)
  }

  test("Merge JNothing with JObject") {
    val json1 = JNothing
    val json2 = JObject("name" -> JString("Alice"), "age" -> JInt(30))

    val merged = MMerge.merge(json1, json2)

    val expected = JObject("name" -> JString("Alice"), "age" -> JInt(30))
    assert(merged === expected)
  }

  test("Merge JObject with JNothing") {
    val json1 = JObject("name" -> JString("Alice"), "age" -> JInt(30))
    val json2 = JNothing

    val merged = MMerge.merge(json1, json2)

    val expected = JObject("name" -> JString("Alice"), "age" -> JInt(30))
    assert(merged === expected)
  }

  test("Merge complex JObject instances with nested structures") {
    val a = parse(Source.fromInputStream(getClass.getResourceAsStream(s"$examplesDirectory/example1/a.json")).mkString)
    val b = parse(Source.fromInputStream(getClass.getResourceAsStream(s"$examplesDirectory/example1/b.json")).mkString)
    val expected = parse(Source.fromInputStream(getClass.getResourceAsStream(s"$expectedDirectory/example1.json")).mkString)
    val merged = MMerge.merge(a, b)

    assert(merged === expected)
  }

  test("Merge complex JArray instances with nested structures") {
    val a = parse(Source.fromInputStream(getClass.getResourceAsStream(s"$examplesDirectory/example2/a.json")).mkString)
    val b = parse(Source.fromInputStream(getClass.getResourceAsStream(s"$examplesDirectory/example2/b.json")).mkString)
    val expected = parse(Source.fromInputStream(getClass.getResourceAsStream(s"$expectedDirectory/example2.json")).mkString)
    val merged = MMerge.merge(a, b)

    assert(merged === expected)
  }

  test("Merge complex JObject with JArray instances") {
    val a = parse(Source.fromInputStream(getClass.getResourceAsStream(s"$examplesDirectory/example3/a.json")).mkString)
    val b = parse(Source.fromInputStream(getClass.getResourceAsStream(s"$examplesDirectory/example3/b.json")).mkString)
    val expected = parse(Source.fromInputStream(getClass.getResourceAsStream(s"$expectedDirectory/example3.json")).mkString)
    val merged = MMerge.merge(a, b)

    assert(merged === expected)
  }

  test("Merge complex JArray with JObject instances") {
    val a = parse(Source.fromInputStream(getClass.getResourceAsStream(s"$examplesDirectory/example4/a.json")).mkString)
    val b = parse(Source.fromInputStream(getClass.getResourceAsStream(s"$examplesDirectory/example4/b.json")).mkString)
    val expected = parse(Source.fromInputStream(getClass.getResourceAsStream(s"$expectedDirectory/example4.json")).mkString)
    val merged = MMerge.merge(a, b)

    assert(merged === expected)
  }

  test("Merge complex JArray with JArray instances") {
    val a = parse(Source.fromInputStream(getClass.getResourceAsStream(s"$examplesDirectory/example5/a.json")).mkString)
    val b = parse(Source.fromInputStream(getClass.getResourceAsStream(s"$examplesDirectory/example5/b.json")).mkString)
    val expected = parse(Source.fromInputStream(getClass.getResourceAsStream(s"$expectedDirectory/example5.json")).mkString)
    val merged = MMerge.merge(a, b)

    assert(merged === expected)
  }

}