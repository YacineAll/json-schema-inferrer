package org.example.schema.types

abstract class ArrayType extends JsonSchemaType


object ArrayType extends ArrayType {
  @Override protected val jsonType: Option[String] = Some( "array")
  var items: List[Any] = List.empty
}

