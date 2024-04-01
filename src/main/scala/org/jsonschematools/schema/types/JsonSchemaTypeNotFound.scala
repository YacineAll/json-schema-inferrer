package org.jsonschematools.schema.types

/**
 * A custom exception class to represent cases where a JSON Schema type is not found.
 *
 * @param msg
 *   the error message describing the issue
 */
case class JsonSchemaTypeNotFound(msg: String) extends Exception(msg)
