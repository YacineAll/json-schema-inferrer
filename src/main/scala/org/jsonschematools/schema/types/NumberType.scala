package org.jsonschematools.schema.types

abstract class NumberType extends JsonSchemaType

object NumberType extends NumberType {
  @Override protected val jsonType: Option[String] = Some("number")
}
