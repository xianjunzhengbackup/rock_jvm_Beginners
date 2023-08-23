# 20 Higher order functions
After reading this lesson, you will be able to

* Define functions that have functions as parameters
* Implement functions that return functions
* Build powerful abstractions that remove code duplication

Functions are first-class citizens in Scala: you can use them as parameters or return them as the result of some computation. Higher order functions are functions that accept other functions as parameters, return functions, or both. Their use allows you to create powerful abstractions that reduce duplication and increase the reusability of your code. In the capstone, you’ll use higher order functions to extract information on the winner of the game “Paper, Rock, Scissors, Lizard, Spock!”

Consider this

Suppose you want to compute some calculations and perform some input/output operation on them. For example, you may need to express two case scenarios. You should display the result of your computation into the console in the first, while you should append it to an existing file in the second. How would you design your functions to avoid code duplication and ensure their implementation is consistent everywhere in your program? How efficiently can you add support for other IO operations, such as querying a database?

## 20.1 Functions as parameters
Suppose you want to perform some simple statistics on a string to calculate its length and how many letters or digits it has.
```
Listing 20.1 Stats on a string

  def size(s: String): Int  = 
    s.length
 
  def countLetters(s: String): Int = 
    s.count(_.isLetter)               ❶
 
  def countDigits(s: String): Int = 
    s.count(_.isDigit)                ❶
❶ count is a function defined in the class String to calculate how many chars of a string respect a specific property.
```
The solution shown in listing 20.1 works, but it is not ideal. Adding new types of statistics to your program drastically increases the number of functions you need to write and maintain. The functions countLetters and countDigits are very similar; the only difference is the predicate used to determine if you should count a char. How can you design functions so that you can avoid code duplication? How can you also ensure you define them so that you can support new operations easily?

Let’s look again at the statistics you need to perform: compute its length, count its letters, and count its digits. In other words, you need to determine if you should consider a character. You should include each one when calculating the length of a string. At the same time, you should consider only those with a particular property (i.e., being a letter, being a digit, etc.) for the other two operations.
```
Listing 20.2 Stats on a string 2

  def stats(s: String, predicate: Char => Boolean): Int =   ❶
    s.count(predicate)
❶ predicate is a function that takes a char as its parameter and returns a Boolean.
```
After defining the function stats, you can compute the requested statistics needed as follows by removing any code duplication:
```
  def size(s: String): Int  = stats(s, _ => true)
 
  def countLetters(s: String): Int = stats(s, _.isLetter)
 
  def countDigits(s: String): Int = stats(s, _.isDigit)
```
You can also support custom statistics by calling the function stats directly. For example, you can count the number of uppercase letters or how many chars “x” it contains as follows:
```
val text = "This is my Text Example"
stats(text, _.isUpper)  // count of upper letters
stats(text, _ == 'x')   // count of chars equal to "x"
Quick Check 20.1 Write a function called foo that takes a function f of type Int => Double as its parameter and returns a Double; apply 42 to the function parameter and then add 2 to its result.
```
You can specify defaults for function parameters. For example, you can change the implementation of your stats function so that it counts all the chars by default—in other words, to be equivalent to the length of a string.
```
Listing 20.3 Stats on a string with default

def stats(s: String, 
     predicate: Char => Boolean = { _ => true } ): Int =    ❶
    s.count(predicate)
❶ predicate is the name of the function parameter. Char => Boolean is the type of the function parameter. { _ => true } is an anonymous function used as default value.
```
Once you have provided a default for the function parameter, you can call the function without providing the parameter predicate:
```
stats(s) // count of all the chars
Figure 20.1 shows a summary of the Scala syntax for function parameters.

```

Figure 20.1 A syntax diagram for function parameters in Scala and their use of defaults

Quick Check 20.2 In the quick check 20.1, you implemented a function called foo. Modify your implementation to add the function toDouble from the class Int as the default of its parameter f.

## 20.2 Functions as return values
In the previous section, you implemented a function called stats to perform statistics on a string (see listing 20.3). In particular, you represented each of the predefined options with three functions called size, countLetters, countDigits. This solution works for a small set of alternatives, but it will quickly become quite verbose once your program needs to provide more and more preferences. You will need to define a function with shape String => Int for each possible alternative. This approach can cause the number of functions in your program to explode. If you need to express 20 different alternatives, you will have 20 functions with a very similar structure. Another solution is to represent all the available selections with a set of well-defined values (or union types) and write a function to select the predicate filter to use for your stats function.
```
Listing 20.4 PredicateSelector function

  sealed trait Mode                                       ❶
  case object Length extends Mode
  case object Letters extends Mode
  case object Digits extends Mode
 
  def predicateSelector(mode: Mode): Char => Boolean =    ❷
    mode match {
      case Length => _ => true
      case Letters => _.isLetter 
      case Digits => _.isDigit
    }
❶ A finite set of values to define all the options

❷ It selects a predicate based on a given mode.
```
Now that you have defined the predicateSelector function, you can use it to call the stats function:
```
val text = "This is my Text Example"
stats(text, predicateSelector(Length))   // count of all chars
stats(text, predicateSelector(Letters))  // count of upper letters
```
Thanks to this solution, you can support additional predefined statistics by adding a case object for the trait Mode and a case clause in the predicateSelector function rather than defining a new function from scratch.

Figure 20.2 provides a summary of how to implement higher order functions that return functions in Scala.



Figure 20.2 A syntax diagram of higher order functions that return functions as result type

Quick Check 20.3 Change the code in listing 20.4 to support a new kind of statistics to count the whitespaces in a string; use the function isWhitespace defined in the class Char.

## Summary
In this lesson, my objective was to teach you about higher order functions and how to use them to build powerful abstractions.

You saw how to create functions that accept other functions as parameters.

You discovered how to implement functions that return functions.

Let’s see if you got this!

Try this Write a function, called operationWithFallback, that returns an Int and has three parameters:

n is an integer.

operation is a function from Int to Int.

fallback is an integer.

The function operationWithFallback should be implemented as follows: compute the value of operation applied to n and return it if more than zero; otherwise, compute the fallback. Make sure to evaluate fallback only if needed.

 Answers to quick checks
  

Quick Check 20.1 An implementation for the function foo is the following:

def foo(f: Int => Double): Double = f(42) + 2
  

Quick Check 20.2 The implementation is modified as follows:

def foo(f: Int => Double = _.toDouble): Double = f(42) + 2
  

Quick Check 20.3 The implementation shown in listing 20.4 should be modified as follows:

  sealed trait Mode 
  case object Length extends Mode
  case object Letters extends Mode
  case object Digits extends Mode
  case object Whitespaces extends Mode
 
 
  def predicateSelector(mode: Mode): Char => Boolean = 
      mode match {
          case Length => _ => true
          case Letters => _.isLetter 
          case Digits => _.isDigit
          case Whitespaces => _.isWhitespace
      }

