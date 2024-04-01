package org.jsonschematools.schema.types

/**
 * An abstract class representing a JSON Schema array type.
 */
abstract class ArrayType extends JsonSchemaType

/**
 * The companion object for the `ArrayType` class, providing default values and utility functions.
 */
object ArrayType extends ArrayType {

  /**
   * The JSON type as a string (i.e., "array").
   */
  override protected val jsonType: Option[String] = Some("array")

  /**
   * The list of items in the array. This can be a list of JSON Schema types or JSON values.
   */
  var items: List[Any] = List.empty
}
