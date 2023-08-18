15 Anonymous functions
After reading this lesson, you will be able to

Implement anonymous functions Code using the concise notation for anonymous functions
In lesson 6, you learned the basics of functions in Scala. In this lesson, you’ll discover a new type of function: anonymous. Anonymous functions are functions that you can define quickly and concisely. At first, they may seem just an alternative to the standard Scala functions you have seen so far, but you’ll soon discover that they are particularly handy when combined with another type of function called higher order. The concept of an anonymous function is not unique to Scala; other languages, such as Java 8+ and Python, refer to it as lambda. In the capstone, you will use a particular kind of anonymous function called partial to define your HTTP server’s routes.

Consider this

Consider the following two functions to sum and subtract two integers:

    def sum(a: Int, b: Int): Int = a + b 
 
    def subtract(a: Int, b: Int): Int = a - b
Which parts do these two functions have in common? How are they different? Can you think of a more concise way of achieving the same implementation?

 15.1 Function vs. anonymous function
Suppose you implemented a calculator program to perform the standard operations on integers (i.e., sum, subtraction, multiplication, division) together with negation.

Listing 15.1 MyCalculator program

  object MyCalculator {
 
    def sum(a: Int, b: Int): Int = a + b
 
    def subtract(a: Int, b: Int): Int = a - b
 
    def multiply(a: Int, b: Int): Int = a * b
 
    def divide(a: Int, b: Int): Int = a / b
 
    def negate(a: Int): Int = subtract(0, a)    ❶
    
  }
❶ Alternatively, you can also multiply by minus one.

You can use your MyCalculator program as follows:

  import MyCalculator._
 
  sum(3, 5)         // returns 8
  subtract(4, 4)    // returns 0
  multiply(5, 3)    // returns 15
  divide(6, 2)      // returns 3
  negate(-5)        // returns 5
In Scala, every function has a type; you can represent this by combining its parameters with the arrow “=>” and its return type. For example, let’s look at listing 15.1 again. The function sum has the type (Int, Int) => Int, which reads “Int Int to Int” because it takes two integers as parameters and returns an integer. On the other hand, the function negate has type Int => Int, which reads “Int to Int” because it takes an integer as the parameter and returns an integer.

The type notation for functions reflects the syntax used to implement anonymous functions. For example, you can implement the equivalent anonymous functions for sum and negate as follows:

def sum(a: Int, b: Int): Int = a + b      // function for sum
{ (a: Int, b: Int) => a + b }             // anonymous function for sum
 
def negate(a: Int): Int = subtract(0, a)  // function for negate
{ (a: Int) => subtract(0, a) }            // anonymous function for negate
When implementing anonymous functions, the function name is no longer needed, while its parameters and body stay the same. Its return type also disappears because the compiler infers it from its implementation. Figure 15.1 shows a syntax summary of how to define anonymous functions.



Figure 15.1 Comparison between the syntax for a function and its corresponding anonymous function

Listing 15.2 shows how to reimplement your calculation program using anonymous functions.

Listing 15.2 MySecondCalculator program

  object MySecondCalculator {
 
    val sum = { (a: Int, b: Int) => a + b }
    val subtract = { (a: Int, b: Int) => a - b }
    
    val multiply = { (a: Int, b: Int) => a * b }
    
    val divide = { (a: Int, b: Int) => a / b }
    
    val negate = { (a: Int) => subtract(0, a) }  
 
  }
In the function negate, you can omit the parenthesis for the parameter a. You can do this when an anonymous function accepts only one parameter. Note that you reused the function names in listing 15.1 to create values that refer to the corresponding anonymous function; this is an optional step that allows you to call the function later on. You can use your MySecondCalculator program by calling the values you defined and by providing parameters as if they were regular functions:

  import MySecondCalculator._
 
  sum(3, 5)
  subtract(4, 4)
  multiply(5, 3)
  divide(6, 2)
  negate(-5)
Quick Check 15.1 What is the type for each of the values defined in listing 15.2? Use the REPL to validate your hypothesis.

  

Quick Check 15.2 Write an anonymous function equivalent to the following function:

    def hello(n: String): String = s"Hello, $n!" 
 15.2 Concise notation for anonymous functions
Scala offers a more concise notation for anonymous functions. Let’s see how it works.

When transforming a function to an anonymous function, its return type is no longer needed because the compiler infers its type, and you can do the same for the parameter type. If you provide the compiler an explicit type for your anonymous function, it will use it to infer the type of your parameters. For example, you can refactor the anonymous functions sum and negate shown in listing 15.2 as follows:

val sum = { (a: Int, b: Int) => a + b }           // before
val sum: (Int, Int) => Int = { (a, b) => a + b }  // after
 
val negate = { (a: Int) => subtract(0, a) }       // before
val negate: Int => Int = { a => subtract(0, a) }  // after
If your anonymous function has an an implementation that consists of a single instruction and your parameters are used in order of declaration, you can even go a step further by removing the parameters completely and replacing them with an underscore:

val sum = { (a: Int, b: Int) => a + b }            // before
val sum: (Int, Int) => Int = { (a, b) => a + b }   // first refactoring
val sum: (Int, Int) => Int = { _ + _ }             // second refactoring
 
val negate = { (a: Int) => subtract(0, a) }        // before
val negate: Int => Int = { a => subtract(0, a) }   // first refactoring
val negate: Int => Int = { subtract(0, _) }        // second refactoring
You can now refactor your calculator program using a more concise notation.

Listing 15.3 MyThirdCalculator program

  object MyThirdCalculator {
 
    val sum: (Int, Int) => Int = { _ + _ }        ❶
 
    val subtract: (Int, Int) => Int = { _ - _ }   ❶
 
    val multiply: (Int, Int) => Int = { _ * _ }   ❶
 
    val divide: (Int, Int) => Int = { _ / _ }     ❶
 
    val negate: Int => Int = subtract(0, _)       ❷
 
  }
❶ You could omit the curly brackets here (e.g., use “_ + _” instead of “{_ + _ }”).

❷ Curly brackets omitted

  

Quick Check 15.3 Are functions funcA and funcB equivalent? In other words, do they return the same output when receiving the same input? Why? Use the REPL to verify your hypotheses.

val funcA: (Int, Int) => Int = { (a, b) => b / a }
val funcB: (Int, Int) => Int = { _ / _ }  
  

Readability first!

In future lessons and units, you’ll discover how useful and expressive the concise notation for anonymous functions is, in particular when combined with higher order functions.

Depending on the context you are in, this notation can become quite cryptic and hard to read due to all the information removed and inferred at compile time instead. For example, you could argue that the expression “_ - _” is less expressive and more confusing than “{ (a, b) => a - b }”.

Do not compromise the readability of your code. When using the concise notation for anonymous functions, use it only when the omitted information is easy to infer for both the compiler and your fellow developers.

 Summary
In this lesson, my objective was to teach you about anonymous functions.

You can use them to create functions on the fly and without too much boilerplate; you are going to see their full potential when you learn about higher order functions.

You also discovered their concise notation to remove unnecessary information that the compiler infers from the function’s type.

Let’s see if you got this!

Try This Rewrite each of the following functions as anonymous functions; use your concise notation at your discretion.

def multiply(s: String, n: Int): Int = s.length * n

def toDouble(n: Int): Double = n.toDouble

def concat(s1: String, s2: String): String = s1 + s2

def inverseConcat(s1: String, s2: String): String = s2 + s1

def myLongFunc(s: String): String = {

      val length = s.length

      s.reverse * length

}

My Answer:
scala> val m:(String,n)=>Int ={ _.length * _}
<console>:11: error: not found: type n
       val m:(String,n)=>Int ={ _.length * _}
                     ^

scala> val m:(String,Int)=>Int ={ _.length * _}
m: (String, Int) => Int = $Lambda$5927/0x00000001011d8630@70761979

scala> m("kkkkk",3)
res0: Int = 15

scala> val tD:Int=>Double={_.toDouble}
tD: Int => Double = $Lambda$5942/0x00000001011e0428@23d4ea38

scala> val c:(String,String)=>String={_ + _}
c: (String, String) => String = $Lambda$5943/0x00000001011e1058@270cd5ea

scala> c("111"+"222")
<console>:13: error: not enough arguments for method apply: (v1: String, v2: String)String in trait Function2.
Unspecified value parameter v2.
       c("111"+"222")
        ^

scala> c("111","222")
res2: String = 111222

scala> val i={(s1:String,s2:String)=>s2+s1}
i: (String, String) => String = $Lambda$5969/0x00000001011e2680@1d735fc1

scala> i("111","222")
res3: String = 222111

 Answers to quick checks
  

Quick Check 15.1 The values sum, subtract, multiply, divide have the type (Int, Int) => Int, while the value negate has type Int => Int.

  

Quick Check 15.2 An implementation of an anonymous function equivalent to the function hello is the following:

    { (n: String) => s"Hello, $n!" }
  

Quick Check 15.3 Functions funcA and funcB are not equivalent because of the order of their parameters. Function funcA divides its second parameter called b by its first parameter called a. Function funcB does the inverse: it divides its first parameter by its second one because the compiler substitutes them by following their declaration order.


