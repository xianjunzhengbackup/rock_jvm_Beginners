package lectures.part1.basics

object ValuesVariablesTypes extends App{
  val x: Int = 42
  println(x)
  //VALS ARE IMMUTABLE
  //COMPILER CAN INFER TYPES

  val aString: String = "hello"

  val aBoolean: Boolean = false
  val aChar: Char = 'a'
  val anInt: Int = x
  val aShort: Short =  4612
  val aLong: Long = 55555555L
  val aFloat: Float = 2.888f
  val aDouble: Double = 3.14

  //variables
  var aVariable: Int = 4
  aVariable = 7

}
