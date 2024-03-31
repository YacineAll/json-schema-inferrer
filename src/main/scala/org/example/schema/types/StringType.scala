package org.example.schema.types

abstract class StringType extends JsonSchemaType

object StringType extends StringType {
  @Override protected val jsonType: Option[String] = Some("string")
}