package org.jsonschematools.merging

import org.json4s._

import scala.annotation.tailrec

object MMerge {

  /**
   * A type alias for the basic JSON data types: JString, JInt, JDouble, and JBool. This alias
   * represents the atomic JSON values that cannot be further decomposed.
   */
  private type Atomic = Either[JString, Either[JInt, Either[JDouble, JBool]]]

  /**
   * This function takes two JSON values as input and returns a merged JSON value. It uses pattern
   * matching to handle different combinations of JSON values.
   *
   * @param val1
   *   the first JSON value to merge
   * @param val2
   *   the second JSON value to merge
   * @return
   *   the merged JSON value
   */
  def merge(val1: JValue, val2: JValue): JValue = (val1, val2) match {
    case (JObject(xs), JObject(ys)) => JObject(mergeFields(xs, ys))
    case (JArray(xs), JArray(ys)) => JArray(mergeVals(xs, ys))
    case (JObject(xs), JArray(ys)) => JArray(List(JObject(xs)) ++ ys)
    case (JArray(xs), JObject(ys)) => JArray(xs ++ List(JObject(ys)))
    case (JObject(xs), singleValue @ (_: JString | _: JInt | _: JDouble | _: JBool)) =>
      JArray(List(JObject(xs)) ++ List(singleValue))
    case (singleValue @ (_: JString | _: JInt | _: JDouble | _: JBool), JObject(xs)) =>
      JArray(List(singleValue) ++ List(JObject(xs)))
    case (JNothing, x) => x
    case (x, JNothing) => x
    case (JArray(xs), singleValue @ (_: JString | _: JInt | _: JDouble | _: JBool)) =>
      JArray(xs :+ singleValue)
    case (singleValue @ (_: JString | _: JInt | _: JDouble | _: JBool), JArray(xs)) =>
      JArray(singleValue +: xs)
    case (
          a @ (_: JString | _: JInt | _: JDouble | _: JBool),
          b @ (_: JString | _: JInt | _: JDouble | _: JBool)) =>
      if (a == b) a else JArray(List(a, b))
    case (_, y) => y
  }

  /**
   * This private function takes a list of JSON values and an atomic JSON value as input, and
   * returns a new JSON array containing the elements of the list and the atomic value appended to
   * it.
   *
   * @param array
   *   the list of JSON values
   * @param singleValue
   *   the atomic JSON value
   * @return
   *   the new JSON array containing both the list elements and the atomic value
   */
  private def mergeWithArray(array: List[JValue], singleValue: JValue): JArray = JArray(
    array :+ singleValue)

  /**
   * This private function takes two lists of JSON object fields as input and returns a merged
   * list of fields. It uses the `mergeRec` tail-recursive helper function to merge the fields
   * based on their names. If a field with the same name exists in both input lists, the function
   * merges their values using the `merge` function.
   *
   * @param vs1
   *   the first list of JSON object fields
   * @param vs2
   *   the second list of JSON object fields
   * @return
   *   the merged list of JSON object fields
   */
  private def mergeFields(vs1: List[JField], vs2: List[JField]): List[JField] = {

    /**
     * Tail-recursive helper function for merging fields.
     *
     * @param acc
     *   the accumulator list for storing merged fields
     * @param xleft
     *   the remaining fields from the first list
     * @param yleft
     *   the remaining fields from the second list
     * @return
     *   the merged list of JSON object fields
     */
    @tailrec def mergeRec(
        acc: List[JField],
        xleft: List[JField],
        yleft: List[JField]): List[JField] = xleft match {
      case Nil => acc ++ yleft
      case (xn, xv) :: xs =>
        yleft find (_._1 == xn) match {
          case Some(y @ (yn @ _, yv)) =>
            mergeRec(acc ++ List(JField(xn, merge(xv, yv))), xs, yleft filterNot (_ == y))
          case None => mergeRec(acc ++ List(JField(xn, xv)), xs, yleft)
        }
    }

    mergeRec(List(), vs1, vs2)
  }

  /**
   * This private function takes two lists of JSON values as input and returns a merged list of
   * values. It uses the `mergeRec` tail-recursive helper function to merge the values. If a value
   * exists in both input lists, the function merges them using the `merge` function.
   *
   * @param vs1
   *   the first list of JSON values
   * @param vs2
   *   the second list of JSON values
   * @return
   *   the merged list of JSON values
   */
  private def mergeVals(vs1: List[JValue], vs2: List[JValue]): List[JValue] = {

    /**
     * Tail-recursive helper function for merging values.
     *
     * @param acc
     *   the accumulator list for storing merged values
     * @param xleft
     *   the remaining values from the first list
     * @param yleft
     *   the remaining values from the second list
     * @return
     *   the merged list of JSON values
     */
    @tailrec def mergeRec(
        acc: List[JValue],
        xleft: List[JValue],
        yleft: List[JValue]): List[JValue] = xleft match {
      case Nil => acc ++ yleft
      case x :: xs =>
        yleft find (_ == x) match {
          case Some(y) => mergeRec(acc ++ List(merge(x, y)), xs, yleft filterNot (_ == y))
          case None => mergeRec(acc ++ List(x), xs, yleft)
        }
    }
    mergeRec(List(), vs1, vs2)
  }

}
