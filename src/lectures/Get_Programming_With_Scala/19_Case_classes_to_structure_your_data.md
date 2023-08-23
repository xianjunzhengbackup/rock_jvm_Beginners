# 19 Case classes to structure your data
After reading this lesson, you’ll be able to

* Represent immutable structured data using case classes
* Decide when to use case objects rather than regular objects

Use case classes together with pattern matching
In the previous unit, you mastered how to create a simple HTTP server. In this lesson, you’ll discover an essential tool in your Scala’s toolbox called case class. When coding, dealing with data is a fundamental and recurring task. A case class provides a convenient and efficient way to represent your data in an immutable way that allows you to share data between multiple threads safely. Being able to express data efficiently and conveniently is essential to make sure our program works correctly. You’ll also learn about case objects and how they can be useful when serialization is involved. Finally, you’ll see how pattern matching provides dedicated support for case classes thanks to the unapply function. In the capstone, you’ll use case classes and case objects to represent the core elements of the game “Paper, Rock, Scissors, Lizard, Spock!”

Consider this

Think of the languages you have encountered in your coding experience. How do they represent data? Are there methods that you must implement? Do you create both setters and getters? Do you have support from either the language itself or your IDE to reduce any potential boilerplate?

## 19.1 Case class
Representing data is a crucial part of writing programs, but it is also often mechanical: you need to define your fields, setters, getters, and so on. When coding in languages a bit more verbose such as Java, you usually take advantage of tools, such as your IDE, to generate code automatically. What if the compiler could do this for you instead of relying on your IDE?

A case class is a class with an arbitrary number of parameters for which the compiler automatically adds ad hoc code. In Scala’s early versions, you couldn’t specify a case class with more than 22 parameters, but this limitation no longer exists since Scala 2.11. Case classes are the ideal data containers because they encourage the use of immutability. The implementation of a case class looks very similar to a regular class, but it has an additional case keyword.

Listing 19.1 The case class Person
```
case class Person(name: String, age: Int)
```
You can also think of a case class as a class that is characterized by its parameters. For example, you could identify an instance of person by its name and its age. For this reason, you can also refer to this as product type. ==As a result of the keyword case, the compiler automatically adds a few convenience methods to it==. Here’s an overview considering the following instance of the class Person:

`val personA = new Person("Tom", 25)`

**Getters For each parameter, the compiler adds a getter function so that you can easily access it**. For example, you can access the name and age of personA as follows:
```
personA.name // returns "Tom"
personA.age  // returns 25
```
copy function 

**You do not have setter functions for parameters** because a case class represents data in an immutable way. When changing one of the case class’s values, you should use the copy function to create a new data representation. Suppose you want to change the age of you instance personA to be 35:
```
val personB: Person = personA.copy(age = 35)
personA.age        // returns 25
personB.age        // returns 35 
```
You can also change more parameters at the same time. For example, if you want to change both its name and age you can just provide more parameters to the copy function:
```
val mark = personA.copy(name = "Mark", age = personA.age + 1)
mark.name // returns "Mark"
mark.age  // returns 26
```
==The copy function is effectively equivalent to initializing a new class. Using the copy function is particularly convenient when a case class has lots of fields, but you only need to change one==. Consider the following snippet of code:
```
case class Test(a: Int, b: Int, c: Int, d: Int)
val test = Test(1,2,3,4)
 
val testA = Test(a = 0, b = test.b, c = test.c, d = test.d)
val testB = test.copy(a = 0)
```
The instances testA and testB are equivalent: you initialized testA using the apply function, while you initialized testB using the copy function. Hopefully, you’ll agree that the copy function approach is more readable than using the apply one in this case.

toString, hashcode, equals functions 

Every class has the functions toString, hashCode, and equals; their implementation comes directly from the Java world. In Java, java.lang.Object is the superclass of all the classes, and it provides an implementation for several functions inherited by all the other classes. A case class redefines the implementation for some of these functions inherited from java.lang.Object. Let’s see how:

toString: By default, a toString function returns a string representing the class’s name, followed by the hexadecimal representation of the instance’s memory address (e.g., Person@1e04fa0a). A case class redefines this method to return a string that is descriptive of the data it contains:
```
personA.toString() // returns "Person(Tom,25)"
```
hashCode: 这玩意就是对应对象的索引。The hashCode function returns an integer that represents an instance of a class. The JVM uses this number in data structures and hash tables when storing objects in a more performant way. While a hash code of an instance usually considers both its internal structure and memory allocation, a case class overrides its hash code so that it considers only its internal structure. The compiler makes sure that two case classes with the same type and structure have the same hash code.
```
class C(x: Int)
new C(5).hashCode == new C(5).hashCode // returns false
 
case class A(x: Int)
new A(5).hashCode == new A(5).hashCode // returns true    
```
equals: According to the implementation of equals defined in java.lang .Object, equality holds if two instances are the same. In other words, they are equal if they point to the same memory allocation. When working with case classes, the compiler provides a different implementation for equals in which case classes that belong to the same type and structure are considered equal.
```
class C(x: Int)
new C(5).equals(new C(5)) // returns false
    
case class A(x: Int)
case class B(n: Int)
new A(5).equals(new B(5)) // returns false
new A(5).equals(new A(5)) // returns true
``` 
Scala 3: Strict equality

Scala 2 uses universal equality: you can always compare two instances for equality, even if they have different types. For examples, the following expression comparing a Boolean and a String is considered valid (with a compiler warning):
```
scala> true == "true" 
            ^
       warning: comparing values of types Boolean and String using 
`==` will always yield false
val res0: Boolean = false
// it compiles in Scala 2 with a warning
Scala 3 adopts strict equality: you can only compare instances that have the same type. The previous expression no longer compiles:

scala> "true" == true
1 |"true" == true
  |^^^^^^^^^^^^^^
  |Values of types String and Boolean cannot be compared 
with == or !=
    // it doesn’t compile in Scala 3
``` 
==Companion object: apply AND unapply FUNCTIONS== 

<u>When declaring a case class, the compiler generates its companion object with implementations for the apply and unapply functions.</u> You can use such methods to construct and deconstruct an instance of a case class, respectively. Let’s see how they work:

apply: thanks to the generated apply method, you can create an instance of your case class by providing parameters for it. You have already encountered the apply function when discussing singleton objects; let’s quickly recap how it works. For example, to create an instance of Person, you can use the apply method rather than directly invoking its constructor. All the following expressions are equivalent:
```
new Person("Tom", 25)        
Person.apply("Tom", 25)          
Person("Tom", 25)               
Person(age = 25, name = "Tom")   
```  
==unapply: you can use the unapply method to decompose a class.== In a case class, the compiler implements the unapply to return the class fields. For example, you can decompose a Person to obtain an optional grouping containing a name and an age:
```
Person.unapply(Person("Tom", 25)) 
// returns Some((Tom, 25))
// which has type Option[(String, Int)]
```
You may not be able to fully understand its return type and implementation yet. You’ll learn about the unapply method in detail when discussing optional values and tuples. In the next section, you’ll see how having an implementation for the unapply function allows you to pattern match over the fields of a case class.

Through the use of case classes, the compiler saves us from writing lots of potentially buggy code. Listing 19.2 shows the amount of boilerplate that you would have to define to implement a class that is equivalent to a case class.
```
Listing 19.2 Class vs. case class

  class Person(n: String, a: Int) {
    
    val name: String = n
    val age: Int = a
    def copy(name: String, age: Int) =
      new Person(name, age)
 
 
    override def toString(): String = s”Person($n,$a)”
 
    override def hashCode(): Int = ???             ❶
 
    override def equals(obj: Any): Boolean = ???   ❶
  }
  
  object Person {
 
    def apply(name: String, age: Int): Person = 
      new Person(name, age)
 
    def unapply(p: Person): Option[(String, Int)] =
      Some((p.name, p.age))
  }
❶ Implementation omitted
```
Figure 19.1 shows a summary of how to declare a case class and the functionalities the compiler generates for it.



Figure 19.1 A syntax diagram for a case class. A case class is equivalent to a regular class with its companion object that the compiler has enriched with a set of useful functions.

Quick Check 19.1 Define case classes to express the following relations:

A brewery has a name.

A beer has a name and a brewery.

## 19.2 Pattern matching and case classes
In the previous section, you discovered that the compiler provides an implementation for the unapply function for a case class. This function decomposes a case class into its parameters, and it enables pattern matching to analyze them. This process is entirely transparent; you do not need to invoke the unapply function explicitly. The compiler will look for an unapply function in the companion object for the class and the number of parameters you use in your pattern matching construct.

Consider again the case class Person in listing 19.1:
```
case class Person(name: String, age: Int)
```
Suppose that you want to write a function, called welcome, that returns a different message depending on the name and age of a person.
```
Listing 19.3 Pattern matching of a case class Person

  def welcome(person: Person): String = person match {
    case Person("Tom", _) => "Hello Mr Tom!"                        ❶
    case Person(name, age) if age > 18 => s"Good to see you $name"  ❷
    case p @ Person(_, 18) => s"${p.name}, you look older!"         ❸
    case Person(_, _) => "Hi bro!"                                  ❹
  }
❶ It matches a person with the name Tom.

❷ It matches a person with an age higher than 18.

❸ It matches a person with age 18, and it binds it to a value p.

❹ It matches a person with any name and any age.

You saw a new way of patterning using value binding in listing 19.3:

case p @ Person(_, 18) => s"${p.name}, you look older!"
When pattern matching a class, you can also bind the entire class instance to a value by providing a name and the symbol @.
```
Quick Check 19.2 What happens to the implementation of the function welcome if you declare Person as a regular class rather than a case class—that is, you remove the case keyword from its declaration?

## 19.3 Case object
Now that you understand what case classes are, you may wonder if an equivalent scenario exists for singleton objects: you refer to them as case objects. Let’s look at an example of a case object for currency.
```
Listing 19.4 USD currency

case object USD
```
A case object is a regular singleton object for which the compiler automatically overrides some useful methods; it redefines the implementation of toString to produce a human-readable string representation. For a regular object, toString returns its name followed by the hexadecimal encoding of its memory address. This looks similar to “USD@7b36aa0c.” When dealing with a case object, the compiler changes the definition for toString to return only the object name:
```
USD.toString     // returns "USD"
```
Figure 19.2 shows a summary of the syntax for case objects.



Figure 19.2 A syntax diagram for a case object in Scala. A case object is equivalent to an object with a redefined toString function.

Quick Check 19.3 Consider the code in listing 19.4. Modify the implementation of USD to define three more currencies using case objects: GBP, CAD, and EUR. Use a sealed trait to group them all as currency.

## Summary
In this lesson, my objective was to teach you about case classes, how they differ from regular classes, and why they are beneficial when representing data.

You learned how to use pattern matching with a case class and use the symbol @ to bid an entire instance to a value.

We also discussed case objects and why they are ideal for representing singleton objects using strings.

Let’s see if you got this!

try this Use case classes and case objects to represent the following data:

An author has a forename and a surname.

A genre has only three possible values: drama, horror, romantic.

A book has a title, an author, and a genre.

 Answers to quick checks
  

Quick Check 19.1 The following code describes the relations between brewery and beer:
```
case class Brewery(name: String)
case class Beer(name: String, brewery: Brewery)
```  

Quick Check 19.2 The function welcome no longer compiles. The compiler complains that it cannot find a value Person; a companion object with the name Person containing an implementation for the unapply function no longer exists because you declared Person as a regular class rather than a case class. Also, its fields name and age are no longer accessible.

  

Quick Check 19.3 A representation for currencies using case objects and a sealed trait is the following:
```
sealed trait Currency
case object USD extends Currency
case object GBP extends Currency
case object CAD extends Currency
case object EUR extends Currency
```

