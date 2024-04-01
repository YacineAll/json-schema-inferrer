package org.jsonschematools.merging

import org.json4s._
import org.json4s.JNothing

import scala.annotation.tailrec

object MMerge {
  private type Atomic = JString | JInt | JDouble | JBool

  def merge(val1: JValue, val2: JValue): JValue = (val1, val2) match {
    case (JObject(xs), JObject(ys)) => JObject(mergeFields(xs, ys))
    case (JArray(xs), JArray(ys)) => JArray(mergeVals(xs, ys))
    case (JObject(xs), JArray(ys)) => JArray(List(JObject(xs)) ++ ys)
    case (JArray(xs), JObject(ys)) => JArray(xs ++ List(JObject(ys)))
    case (JObject(xs), singleValue: Atomic) => JArray(List(JObject(xs)) ++ List(singleValue))
    case (singleValue: Atomic, JObject(xs)) => JArray(List(singleValue) ++ List(JObject(xs)))
    case (JNothing, x) => x
    case (x, JNothing) => x
    case (JArray(xs), singleValue: Atomic) => JArray(xs :+ singleValue)
    case (singleValue: Atomic, JArray(xs)) => JArray(singleValue +: xs)
    case (a: Atomic, b: Atomic) => if (a == b) a else JArray(List(a, b))
    case (_, y) => y
  }

  private def mergeWithArray(array: List[JValue], singleValue: Atomic): JArray = JArray(
    array :+ singleValue)

  private def mergeFields(vs1: List[JField], vs2: List[JField]): List[JField] = {
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

  private def mergeVals(vs1: List[JValue], vs2: List[JValue]): List[JValue] = {
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
