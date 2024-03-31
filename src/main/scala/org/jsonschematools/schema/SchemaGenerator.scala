package org.jsonschematools.schema

import org.jsonschematools.schema.types.{ArrayType, JsonSchemaType, ObjectType}

import org.json4s.*
import org.json4s.jackson.JsonMethods.*

/** Object responsible for generating JSON schema from a JSON string. */
object SchemaGenerator {

  /** Generates a JSON schema from a JSON string.
   *
   * @param json The JSON string to generate schema from.
   * @return The JSON schema as a JValue.
   */
  def apply(json: String): JValue = new SchemaGenerator(parse(json)).toJValue()
}

/** Class responsible for generating JSON schema from a JSON AST (Abstract Syntax Tree). */
class SchemaGenerator(val bObj: JValue) {

  /** Generates JSON schema from a given JSON AST.
   *
   * @param obj        The JSON AST to generate schema from.
   * @param objId      The ID of the current object.
   * @param firstLevel Indicates if it's the first level of recursion.
   * @param required   Indicates whether properties are required.
   * @param nullable   Indicates whether properties can be null.
   * @return The JSON schema as a JValue.
   */
  private def toJValue(
                        obj: JValue = null,
                        objId: String = null,
                        firstLevel: Boolean = true,
                        required: Boolean = true,
                        nullable: Boolean = false
                      ): JValue = {

    // Determine the base object for schema generation
    val baseObject = if (firstLevel) bObj else obj

    // Builder for schema fields and required properties
    val schema = Seq.newBuilder[JField]
    val requiredProperties = Set.newBuilder[String]

    // Add meta fields for the top-level schema
    if (firstLevel) {
      schema += JField("$schema", JString(JsonSchemaType.schemaVersion))
      //schema += JField("id", JString("#"))
    }

    // Add object ID field if provided
    if (objId != null) schema += JField("id", JString(objId))

    // Determine the schema type of the base object
    val schemaType: JsonSchemaType = JsonSchemaType.getSchemaTypeFor(baseObject)
    schema += JField("type", JString(schemaType.toString))

    // Handle object properties
    baseObject match {
      case JObject(properties) if schemaType.isInstanceOf[ObjectType] =>
        // Handle each property of the object
        schema += JField(
          "properties",
          JObject(
            properties.map {
              case (name, elem) =>
                val propertySchema = toJValue(elem, null, firstLevel = false, required, nullable)
                if (required) requiredProperties += name
                name -> propertySchema
            }
          )
        )

      // Handle array items
      case JArray(elems) if schemaType.isInstanceOf[ArrayType] && elems.nonEmpty =>
        val sameType = elems.forall(_.getClass == elems.head.getClass)
        val itemsSchema = if (sameType) JArray(List(toJValue(elems.head, null, firstLevel = false, required, nullable)))
        else JArray(elems.map(item => toJValue(item, null, firstLevel = false, required, nullable)))
        schema += JField("items", itemsSchema)

      case _ => // Do nothing
    }
    // Add required properties field if any
    if (requiredProperties.result().nonEmpty)
      schema += JField("required", JArray(requiredProperties.result().toList.sorted.map(JString.apply)))
    // Convert the schema builder to a JObject and return
    JObject(schema.result().toList)
  }
}

