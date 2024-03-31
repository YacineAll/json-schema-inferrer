package org.example.schema

import org.json4s._
import org.json4s.native.JsonMethods._

import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.prop.TableDrivenPropertyChecks

import java.nio.file.{Files, Paths}

class SchemaGeneratorSpec extends AnyFunSuite with TableDrivenPropertyChecks {
  // Define directories and files for testing
  private val examplesDir = Paths.get("src/test/resources/schema/examples")
  private val expectedDir = Paths.get("src/test/resources/schema/expected")
  private val files = examplesDir.toFile.listFiles().map(_.getName)

  // Test case for generating schema for an empty JSON object
  test("SchemaGenerator should generate schema for an empty JSON object") {
    val json = "{}"
    val schema = SchemaGenerator(json)
    val expected = JObject(
      List(("$schema", JString("http://json-schema.org/draft-04/schema#")), ("type", JString("object")),
        ("properties", JObject(List())))
    )
    assert(schema === expected)
  }

  // Parameterized test for generating schema for all examples
  test("SchemaGenerator should generate correct schema for all examples") {
    // Table-driven property check to iterate over all example files
    val testData = Table("fileName", files*)

    forAll(testData) { fileName =>
      val examplePath = examplesDir.resolve(fileName)
      val expectedPath = expectedDir.resolve(fileName)

      // Read example JSON and expected JSON from files
      val exampleJson: String = Files.readString(examplePath)
      val expectedJson: String = Files.readString(expectedPath)

      // Generate schema from example JSON and parse expected schema from expected JSON
      val generatedSchema = SchemaGenerator(exampleJson)
      val expectedSchema = parse(expectedJson)

      // Assert that generated schema matches expected schema
      assert(generatedSchema === expectedSchema)
    }
  }
}
