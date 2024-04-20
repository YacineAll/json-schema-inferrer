package org.jsonschematools.schema

import org.json4s._
import org.json4s.native.JsonMethods._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.prop.TableDrivenPropertyChecks

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}

class SchemaGeneratorSpec extends AnyFunSuite with TableDrivenPropertyChecks {
  // Define directories and files for testing
  private val examplesDir = Paths.get("src/test/resources/schema/examples")
  private val expectedDir = Paths.get("src/test/resources/schema/expected")
  private val files = examplesDir.toFile.listFiles().map(_.getName).toList

  // Test case for generating schema for an empty JSON object
  test("SchemaGenerator should generate schema for an empty JSON object") {
    val json = "{}"
    val schema = SchemaGenerator(json)
    val expected = JObject(
      List(
        ("$schema", JString("http://json-schema.org/draft-04/schema#")),
        ("type", JString("object")),
        ("properties", JObject(List()))))
    assert(schema === expected)
  }

  // Parameterized test for generating schema for all examples
  test("SchemaGenerator should generate correct schema for all examples") {
    // Table-driven property check to iterate over all jsonschematools files
    val testData = Table("fileName", files: _*)

    forAll(testData) { fileName =>
      val examplePath = examplesDir.resolve(Paths.get(fileName))
      val expectedPath = expectedDir.resolve(Paths.get(fileName))

      // Read jsonschematools JSON and expected JSON from files
      val exampleJson: String =
        String.join("\n", Files.readAllLines(examplePath, StandardCharsets.UTF_8))
      val expectedJson: String =
        String.join("\n", Files.readAllLines(expectedPath, StandardCharsets.UTF_8))

      // Generate schema from jsonschematools JSON and parse expected schema from expected JSON
      val generatedSchema = SchemaGenerator(exampleJson)
      val expectedSchema = parse(expectedJson)

      // Assert that generated schema matches expected schema
      assert(generatedSchema === expectedSchema)
    }
  }
}
