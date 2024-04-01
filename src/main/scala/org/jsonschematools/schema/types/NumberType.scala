package org.jsonschematools.schema.types

/**
 * An abstract class representing a JSON Schema number type.
 */
abstract class NumberType extends JsonSchemaType

/**
 * The companion object for the `NumberType` class, providing default values and utility
 * functions.
 */
object NumberType extends NumberType {

  /**
   * The JSON type as a string (i.e., "number").
   */
  override protected val jsonType: Option[String] = Some("number")
}
