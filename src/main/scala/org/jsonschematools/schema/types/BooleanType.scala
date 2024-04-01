package org.jsonschematools.schema.types

abstract class BooleanType extends JsonSchemaType

object BooleanType extends BooleanType {
  @Override protected val jsonType: Option[String] = Some("boolean")
}
