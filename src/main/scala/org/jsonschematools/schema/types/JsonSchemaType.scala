package org.jsonschematools.schema.types

import org.json4s.{JArray, JBool, JDecimal, JInt, JLong, JNothing, JNull, JNumber, JObject, JSet, JString, JValue}
import org.jsonschematools.schema.*

/**
 * An abstract class representing a JSON Schema type.
 */
abstract class JsonSchemaType {

  /**
   * The JSON type as a string (e.g., "string", "number", "object").
   */
  protected val jsonType: Option[String]

  /**
   * Converts the JSON Schema type to a string representation.
   *
   * @return
   *   the JSON type as a string
   */
  @Override
  override def toString: String = jsonType.getOrElse("")
}

/**
 * The companion object for the `JsonSchemaType` class, providing utility functions and default
 * values.
 */
object JsonSchemaType extends JsonSchemaType {

  /**
   * The JSON Schema version used.
   */
  val schemaVersion: String = "http://json-schema.org/draft-04/schema#"

  /**
   * The optional unique identifier for the JSON Schema.
   */
  var id: Option[String] = None

  /**
   * A flag indicating if the JSON Schema type is required.
   */
  var required: Boolean = false

  /**
   * The JSON type as a string (e.g., "string", "number", "object"). This is `None` for the base
   * `JsonSchemaType`.
   */
  override val jsonType: Option[String] = None

  /**
   * Determines the JSON Schema type for the given JSON value.
   *
   * @param element
   *   the JSON value to determine the schema type for
   * @return
   *   the corresponding JSON Schema type
   */
  def getSchemaTypeFor(element: JValue): JsonSchemaType =
    element match
      case JSet(set) => ArrayType
      case JObject(obj) => ObjectType
      case JNull => NullType
      case JLong(num) => NumberType
      case JInt(num) => NumberType
      case JArray(arr) => ArrayType
      case JDecimal(num) => NumberType
      case JBool(value) => BooleanType
      case number: JNumber => NumberType
      case JNothing => NullType
      case JString(s) => StringType

}
