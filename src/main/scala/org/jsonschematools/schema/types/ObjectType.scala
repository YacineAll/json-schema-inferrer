package org.jsonschematools.schema.types

/**
 * An abstract class representing a JSON Schema object type.
 */
abstract class ObjectType extends JsonSchemaType

/**
 * The companion object for the `ObjectType` class, providing default values and utility
 * functions.
 */
object ObjectType extends ObjectType {

  /**
   * The JSON type as a string (i.e., "object").
   */
  override protected val jsonType: Option[String] = Some("object")

  /**
   * The properties of the object, represented as a map from property names to their corresponding
   * JSON Schema types or JSON values.
   */
  var properties: Map[String, Any] = Map.empty
}
