package org.jsonschematools.schema.types

/**
 * An abstract class representing a JSON Schema string type.
 */
abstract class StringType extends JsonSchemaType

/**
 * The companion object for the `StringType` class, providing default values and utility
 * functions.
 */
object StringType extends StringType {

  /**
   * The JSON type as a string (i.e., "string").
   */
  override protected val jsonType: Option[String] = Some("string")
}
