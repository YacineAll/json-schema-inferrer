package org.jsonschematools.schema.types

/**
 * An abstract class representing a JSON Schema null type.
 */
abstract class NullType extends JsonSchemaType

/**
 * The companion object for the `NullType` class, providing default values and utility functions.
 */
object NullType extends NullType {

  /**
   * The JSON type as a string (i.e., "null").
   */
  override protected val jsonType: Option[String] = Some("null")
}
