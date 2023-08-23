# 21 What is purity?
After reading this lesson, you will be able to

* Differentiate between pure and impure functions
* Provide code examples in which impure functions cause unpredicted code behavior

In this lesson, you’ll learn about purity, a fundamental principle of functional programming. In particular, you’ll see that a pure function is total and has no side effects. Distinguishing between pure and impure functions can help you identify and prevent bugs in your code. For example, consider the following scenario:

Suppose you are developing the software for a smart thermostat. Your business requirements dictate your thermostat never to reach temperatures below the freezing point of 0°C (equivalent to 32°F) because it could damage its mechanical parts. If this happens, your program should trigger an emergency recovery plan that changes the target temperature to a default value the user can configure. You could translate this with the following function:
```
def monitorTemperature(current: Double, recovery: Double): Double =
  if (current >= 0) current else recovery
```
This function monitorTemperature behaves in different ways depending on the purity of its parameters. Consider the following function invocations:
```
scala> monitorTemperature(current = 5, recovery = 10)
val res0: Double = 5.0
 
scala> monitorTemperature(
        | current = 5,
        | recovery = { println("EMERGENCY! Triggering recovery"); 10 }
        | )
 
EMERGENCY! triggering recovery
val res1: Double = 5.0
```
Both function calls are valid, but the second one behaves unpredictably: even when the current temperature is above the freezing threshold, an unexpected (and confusing!) message appears in the console.

Later in the book you’ll learn about lazy evaluation and how to handle these use cases in which you’d like to evaluate a given function parameter only if needed. For now, let’s discuss purity and how it differs from impurity. In the capstone, you will use pure functions to determine the winner of the game “Paper, Rock, Scissors, Lizard, Spock!”

Consider this

Can you think of another example in which your function may suffer from some unexpected behavior because of the possibly impure values you passed as parameters?

## 21.1 A definition of purity
A pure function is total and has no side effects. Let’s see what each of these terms mean.

TOTALITY A function is total if it is well-defined for every input: it must terminate for every parameter value and return an instance that matches its return type.Terminate在这里是收敛的意思。

Let’s consider the following functions:
```
def plus2(n: Int): Int = n + 2                       // total
 
def div(n: Int): Int = 42 / n                        // non-total
 
def rec(n: Int): Int = if (n > 0) n else rec(n - 1)  // non-total
```
The function plus2 is total because for every possible integer passed as its parameter, it terminates and returns an integer value as the result of its evaluation. The div function is not total because even if it always ends, it doesn’t always return a value of type Int. It throws an ArithmeticException, its parameter n equal to zero. A thrown exception is an unexpected value not represented in its return type. Finally, the rec function is not total because it never terminates for any integer less or equal to zero.
```
Quick Check 21.1 Which of the following functions are total? Why?

def opsA(n: Int): Int = if(n <= 0) n else n + 1

def opsB(n: Int): Int = if(n <= 0) n else opsB(n + 1)

def selectException(predicate: Boolean): Exception = if (predicate) new IllegalStateException("msg here")

    else new ArithmeticException("another msg here")

def anotherToString(obj: AnyRef): String = {

      Thread.sleep(1000) // measured in millis

      obj.toString

}

def validateDistance(dist: Double): Double =

    if (dist < 0) {

        throw new IllegalStateException("Distance cannot be negative")

} else dist
```
SIDE EFFECTS A side effect is an operation that has an observable interchange with elements outside its local scope. It affects (i.e., writes side effect) or is affected by (i.e., reads side effect) the state of your application by interacting with the outside world. Here are a few examples:
```
def negate(predicate: Boolean): Boolean = !predicate 
                                      // no side effect
 
class Counter {
  private var counter = 0
 
  def incr(): Unit = counter += 1      // (write) side effect
  def get(): Int = counter             // (read) side effect
 }
 
def hello(name: String): String = {
  val msg = s"Hello $name"
  println(msg)                         // (write) side effect
  msg
}
```
The function negate has no side effects; its only instruction acts on its parameter to produce a return value. The function Counter.incr contains a (write) side effect: every time you invoke the function, it changes the assignment for the variable counter, which is a code element that lives outside of its local scope. Counter.get also has a (read) side effect: given the same input, it returns a different integer depending on the variable counter’s current assignment. The function hello has a (write) side effect because its println instruction produces a message in the console, a component shared across your application that lives independently from its local scope.

Quick Check 21.2 Which of the following functions have side effects? Why?
```
def div(a: Int, b: Int): Int = {

    if (b == 0) throw new Exception("Cannot divide by zero")

    else a / b

}

def getUserAge(id: Int): Int = {

    val user = getUser(id) // gets data for a database

    user.id

}

def powerOf2(d: Double): Double = Math.pow(2, d)

def anotherPowerOf2(d: Double): Double = {

    println(s"Computing 2^$d...")

    Math.pow(2, d)

}

def getCurrentTime(): Long = System.currentTimeMillis()
```
  

Quick Check 21.3 A pure function is total and has no side effects. Consider the code snippets provided in Quick Checks 21.1 and 21.2: which ones are pure?

## 21.2 Differentiating between pure and impure functions
In the previous section, you discovered that a function is pure if total and without side effects. You can describe this concept in a less formal way as follows: a function is pure if only its parameters determine its behavior, which its return type describes (figure 21.1).



Figure 21.1 A visual representation of the differences between pure and impure functions. Given an input, a pure function returns an output. An impure one produces additional effects not represented in its return value.

A pure function guarantees that it always returns the same output given the same input parameters. In other words, you can replace its invocation with its return value and obtain the same outcome: This concept is called referential transparency, and it has several practical implications.

Suppose you have the following two functions, called pureF and impureF, that take a string as their parameter and return another string as the result of some computation:
```
  def pureF(name: String): String = s"Hi $name!"
 
  def impureF(name: String): String = {
    println("...doing something here...")
    s"Hi $name!"
  }
```
The function pureF is pure, while the function impureF is impure.

You can substitute the function call pureF("Bob") with the string "Hi Bob!". However, swapping the function call impureF("Bob") with the string "Hi Bob!" would not produce the same result because the print instruction in the console would be missing.

Functions with no parameters: Parentheses or no parentheses?

When declaring a function with no parameters, you should omit the parentheses if the function is pure (i.e., def f = ???) and vice versa; specify them if the function is impure (i.e., def f() = ???).

This rule is a style suggestion rather than a law imposed by the compiler.

  

Quick Check 21.4 Which of the following statements are true?

Pure functions do more than just compute a value.

You can replace calls to impure functions with their return value without losing functionalities.

Pure functions are total.

A function that throws exceptions is pure.

A function with side effects is impure.

## Summary
In this lesson, my objective was to teach you about the functional concept of purity.

You learned that pure functions are total and have no side effects.

You also discovered referential transparency and how you can use it to differentiate between pure and impure functions.

Let’s see if you got this!

Try this Which of the following functions are pure? Which are impure?
```
def welcome(n: String): String = s"Welcome $n!"

def printWelcome(n: String): Unit =

    println(s"Welcome $n!")

def slowMultiplication(a: Int, b: Int): Int = {

    Thread.sleep(1000) // 1 second

    a * b

}

def saveUser(user: User): User = {

    insertUser(user) // inserts in a database

    user

}

def getUser(id: Int): User = {

    selectUser(id) // searches in a database

}
```
 Answers to quick checks
  

Quick Check 21.1

The function opsA is total because it always terminates and returns an integer for every integer passed as its parameter.

The function opsB is not total: it calls itself recursively and never terminates for positive integers.

The function selectException is total because it returns an exception: it computes a value that matches its return type for every input. The keyword throw is missing, so the function does not throw the exception, but it returns it as a class instance.

The function anotherToString is total: for every input, it eventually terminates after sleeping for 1 second (or 1,000 milliseconds) and returning a string. What if the function was to block for a much more extended period (e.g., 10 years); would you still consider it total?

The function validateDistance is not total because it throws an exception for any negative double number.

  

Quick Check 21.2

The function div has no side effects; throwing exceptions is not a side effect because it does not change the state of components external to the function.

The function getUserAge returns different results depending on which objects are in the database, which is a (read) side effect.

The function powerOf2 has no side effects as its return value depends entirely on its input.

The function anotherPowerOf2 has a (write) side effect: every time you call it, it produces a new message to the console, changing its state.

The function getCurrentTime returns a value that depends on your machine’s internal clock, which is a (read) side effect.

  

Quick Check 21.3 In Quick Check 21.1, the functions opsA, selectException, anotherToString are pure. In Quick Check 21.2, there is only one pure function: powerOf2.

  

Quick Check 21.4

False

False

True

False

True


