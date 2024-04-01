package org.jsonschematools.schema.types

/**
 * An abstract class representing a JSON Schema boolean type.
 */
abstract class BooleanType extends JsonSchemaType

/**
 * The companion object for the `BooleanType` class, providing default values and utility
 * functions.
 */
object BooleanType extends BooleanType {

  /**
   * The JSON type as a string (i.e., "boolean").
   */
  override protected val jsonType: Option[String] = Some("boolean")
}
