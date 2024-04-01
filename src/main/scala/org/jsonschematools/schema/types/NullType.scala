package org.jsonschematools.schema.types

abstract class NullType extends JsonSchemaType

object NullType extends NullType {
  @Override protected val jsonType: Option[String] = Some("null")
}
