# 16 Partial functions
### After reading this lesson, you will be able to
* Implement partial functions to abstract commonalities between functions
* Create new functions by composing partial functions
* Use a try-catch expression to handle exceptions

Now that you’ve learned about pattern matching, you’ll discover partial functions and how they relate to pattern matching in this lesson. Partial functions are functions that are defined only for some input. You’ll see how they can be useful to abstract commonalities between functions and how you can compose them to create more complex functionalities. Finally, you’ll see how you can use partial functions to catch and handle exceptions. In the capstone, you’ll use partial functions to define the routes of your HTTP server.
Consider this
Suppose you have two pattern matching constructs; they look the same except the last case clause. How would you refactor them so that you can avoid code repetition?
### 16.1 Partial functions
Suppose you need to compute operations on integers. In particular, you want to calculate the square root of an integer. But this operation is defined only for nonnegative numbers. When dealing with a negative integer, you need to either return the negative value or return zero, depending on your use case. You could use pattern matching and define two functions.
```
Listing 16.1 Square root of an integer functions
def sqrtOrZero(n: Int): Double = n match {
case x if x >= 0 => Math.sqrt(x)
case _ => 0 ❶
}
def sqrtOrValue(n: Int): Double = n match {
case x if x >= 0 => Math.sqrt(x)
case x => x ❶
} 
❶ The compiler converts the integer into a double because of the function's return type.
```
The functions sqrtOrZero and sqrtOrValue are very similar: their only difference is the last case clause of their pattern matching constructs. Code duplication makes code difficult to maintain and keep consistent. Let’s see how you can avoid repeating yourself using partial functions.
### 16.1.1 IMPLEMENTING A PARTIAL FUNCTION
A partial function is a function that you can define only for some instances of a type. You have already encountered examples of partial functions when discussing pattern matching: you can consider each case clause as a partial function. For example, you can define a partial function to compute the square root of a nonnegative integer.
```
Listing 16.2 The sqrt partial function
val sqrt: PartialFunction[Int, Double] = ❶
{ case x if x >= 0 => Math.sqrt(x) } 
❶ You can read PartialFunction[Int, Double] as “partial function from int to double.”
```
You can view partial functions as a particular anonymous function with one or more case clauses as their body. <span style="color:red;"> *The compiler cannot infer their type, so you need to specify their argument and return types. In Scala, the type PartialFunction[A, B] identifies a partial function of a parameter of type A that returns an instance of type B.* </span>
```
Listing 16.3 A toPrettyString function
val toPrettyString: PartialFunction[Any, String] = { ❶
case x: Int if x > 0 => s"positive number: $x"
case s: String => s
}
```
❶ Partial function for any type to string

After you define a partial function, you can perform function calls using the same syntax you learned for anonymous functions. But do not forget that your function is now partial; if your input doesn’t match any of its case clauses, you will receive a MatchError exception at runtime:
```
scala> val toPrettyString: PartialFunction[Any, String] = {
| case x: Int if x > 0 => s"positive number: $x"
| case s: String => s
| }
| 
val toPrettyString: PartialFunction[Any,String] = <function1>
scala> toPrettyString(1)
val res0: String = positive number: 1
scala> toPrettyString("hello")
val res1: String = hello
scala> toPrettyString(-1)
scala.MatchError: -1 (of class java.lang.Integer)
at scala.PartialFunction$$anon$1.apply(PartialFunction.scala:255)
at scala.PartialFunction$$anon$1.apply(PartialFunction.scala:253)
at $anonfun$1.applyOrElse(<pastie>:14)
at scala.runtime.AbstractPartialFunction.apply(AbstractPartialFunction.scala:34)
... 28 elided
```
### 16.1.2 FUNCTION COMPOSITION
When talking about the composition of functions, you usually refer to chaining two functions together by passing the result of the first function as the parameter to the second one. For example, consider the following two functions:
```
val f: String => Int = _.size
val g: Int => Boolean = _ > 2
You can create a new function by calling f and then g:
val gof: String => Boolean = { s => g(f(s)) } 
Alternatively, you can use the andThen function as follows:
val gof: String => Boolean = f.andThen(g) 
```
The concept of composition may have a different meaning in the context of partial functions.  <mark style="background-color:  #ff0000">Instead of chaining functions together, you may want to combine partial functions as fallbacks if the previous partial function couldn’t match the given input. In Scala, you can compose partial functions as fallbacks using the function orElse.</mark>

 Listing 16.4 shows you how you can refactor your square root functions to remove the code duplication.
```
Listing 16.4 Two square root functions
val sqrt: PartialFunction[Int, Double] = 
{ case x if x >= 0 => Math.sqrt(x) }
val zero: PartialFunction[Int, Double] = { case _ => 0 }
val value: PartialFunction[Int, Double] = { case x => x }
def sqrtOrZero(n: Int): Double = sqrt.orElse(zero)(n) ❶
def sqrtOrValue(n: Int): Double = sqrt.orElse(value)(n) ❶
❶ First, you create a new function using orElse. Then you apply the parameter to it.
```
Quick Check 16.2 Consider the partial functions sqrt and zero in listing 16.4. Is the partial function sqrt.orElse(zero) equivalent to zero.orElse(sqrt)?
### 16.2 Use case: Exception handling
You first encountered partial functions in lesson 14 when discussing pattern matching. You are now ready to discover another popular use of partial functions: the try-catch expression.

Scala’s exception handling comes directly from the Java world. In Scala, any class that extends java.lang.Exception is an exception. An exception interrupts your code’s execution flow: you need to intercept it or it will terminate your program.
The java.lang package offers a few types of exceptions ready for you to use. A few of them are RuntimeException, NullPointerException, IllegalStateException, IllegalArgumentException, and NumberFormatException. Alternatively, you can define a custom exception by extending the java.lang.Exception class:
```
scala> class MyException(msg: String) extends Exception(msg)
class MyException
You can use an instance of an exception to interrupt your program using the keyword throw:
scala> throw new MyException("BOOM!")
MyException: BOOM!
```
After raising an exception, you’ll need to catch it before it terminates your program. To do so, you can use a try-catch expression.
```
Listing 16.5 An exception handling example
def n(): Int =
try {
throw new Exception("BOOM!")
42
} catch {
case ex: Exception =>
println(s"Ignoring exception $ex. Returning zero instead")
0
}
When evaluating n, the console will display a message and return the integer zero:
scala> n()
Ignoring exception java.lang.Exception: BOOM!. Returning zero instead
val res0: Int = 0
```
The catch keyword follows partial functions that identify which exceptions it should handle: if an exception doesn’t match, it will not intercept it.

#### Think in Scala: Avoid exceptions
Scala reuses Java code that heavily uses exceptions. Some of its standard libraries and functions also throw them; knowing how to deal with them is crucial. However, you should avoid using exceptions in your code.

Exceptions are the equivalent of ticking bombs: they will explode and kill the whole program unless someone is ready to defuse them!

Exceptions are unpredictable. Identifying which exceptions a function could throw is particularly challenging. Its signature doesn’t give you this information. You could document and annotate which exceptions it could throw, but this is not controlled or enforced at compile time, so you have no guarantee that they are still accurate or correct. Often, your only option is to look at its implementation while hunting for exceptions, but this is not an easy task as they can hide in any of its inner function calls.

Exceptions are a drastic solution. If you do not catch an exception, it will terminate your program. Unless you are writing a simple script, your application’s crash is probably not the behavior you desire.

In future lessons, you’ll see how you can represent possible errors using types (i.e., the absence of a value using the type Option). In doing so, you will have specific information on the possible errors just by looking at your function signature. The compiler will also make sure that you handle them correctly by considering both the positive and negative case scenarios.

Partial functions can be dangerous: they will throw an exception if your input doesn’t match any case. You should convert all their possible inputs when composing them. When introducing Option, you’ll see how to rewrite any partial function as a total function returning an instance of Option and avoid the inconvenience of dealing with a MatchError exception.
```
Quick Check 16.3 The following expression throws an IllegalArgumentException exception:
val b = "hello".toBoolean
Write a try-catch expression to default any non-parsable value to false.
```
## Summary
In this lesson, my objective was to teach you about partial functions.

You discovered how you can implement partial functions and compose them to abstract commonalities between functions.

You also learned how to use partial functions in a try-catch expression to handle exceptions.
Let’s see if you got this!

Try This Implement a function to parse a string into an integer. If you cannot parse it, return its length instead. HINT: Use the toInt function on an instance of String.

Answers to quick checks

Quick Check 16.1 A possible implementation for the function transform is as follows:
```
val transform: PartialFunction[String, String] = {
case s if s.startsWith("a") => s.reverse
case s if s.startsWith("s") => s.toUpperCase
}
```
```
Quick Check 16.2 The two partial functions are not equivalent because of the different composition order: zero.orElse(sqrt) returns zero for any input.
sqrt.orElse(zero)(4) // returns 2.0
zero.orElse(sqrt)(4) // returns 0.0
```
```
Quick Check 16.3 You could change the expression val b = "hello".toBoolean as follows:
val b = try {
"hello".toBoolean
} catch { 
case _: IllegalArgumentException => false 
}
```
