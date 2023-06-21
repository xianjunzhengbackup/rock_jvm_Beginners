package lectures.part1.basics

object Expressions extends App{
  val x =  1 + 2 //EXPRESSION
  println(x)

  println(2+3*4)
  //+ - * / & | ^ << >> >>>(right shift with zero extension)

  println(1 == x)
  // == != > >= < <=

  println(!(1==x))
  // ! && ||

  var aVariable=2
  aVariable += 3 //also works with -= *= /=
  println(aVariable)

  //IF expression
  val aCondition = true
  val aCOnditionedValue = if(aCondition) 3 else 5 //IF EXPRESSION
  println(aCOnditionedValue)

  var i=0
  while(i<10){
    println(i)
    i += 1
  }
  //NEVER WRITE THIS AGAIN
  //EVERYTHING IN SCALA IS AN EXPRESSION

  val aWeirdValue=(aVariable=3) //Unit === void

  //code blocks
  val aCodeBlock={
    val y=2
    val z=y+1
    if(z>2) "hello" else "goodbye"
  }

}
