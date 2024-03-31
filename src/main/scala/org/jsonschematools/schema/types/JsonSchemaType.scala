package org.jsonschematools.schema.types

import org.jsonschematools.schema.*
import org.json4s.{JArray, JBool, JDecimal, JDouble, JInt, JLong, JNothing, JNull, JNumber, JObject, JSet, JString, JValue}

abstract class JsonSchemaType {
  protected val jsonType: Option[String]

  @Override
  override def toString: String = jsonType.getOrElse("")
}

object JsonSchemaType extends JsonSchemaType {
  val schemaVersion: String = "http://json-schema.org/draft-04/schema#"
  var id: Option[String] = None
  var required: Boolean = false
  val jsonType: Option[String] = None


  def getSchemaTypeFor(element: JValue): JsonSchemaType =
    element match
      case JSet(set) => ArrayType
      case JObject(obj) => ObjectType
      case JNull => NullType
      case JLong(num) => NumberType
      case JInt(num) => NumberType
      case JArray(arr) => ArrayType
      case JDecimal(num) => NumberType
      case JBool(value) => BooleanType
      case number: JNumber => NumberType
      case JNothing => NullType
      case JString(s) => StringType
      case JDouble(_) => NumberType


}




