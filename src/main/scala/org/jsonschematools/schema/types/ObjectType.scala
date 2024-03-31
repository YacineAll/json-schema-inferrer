package org.jsonschematools.schema.types

abstract class ObjectType extends JsonSchemaType

object ObjectType extends ObjectType {
  @Override protected val jsonType: Option[String] = Some("object")
  var properties: Map[String, Any] = Map.empty
}

