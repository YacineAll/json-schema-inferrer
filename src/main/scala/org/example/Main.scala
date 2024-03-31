package org.example

import org.example.schema.SchemaGenerator
import org.json4s.*
import org.json4s.native.JsonMethods.*

import scala.io.Source



object Main {
  def main(args: Array[String]): Unit =
    val input = Source.fromInputStream(getClass.getResourceAsStream("/example1.json")).mkString
    val schema: JValue =  SchemaGenerator(input)
    val expected: JValue = parse(getClass.getResourceAsStream("/expected1.json"))
    //val diff = schema.diff(expected)
    println(compact(render(schema)))
    println(compact(render(expected)))
    println(schema == expected)
}
