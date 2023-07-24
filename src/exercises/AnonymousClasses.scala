package exercises

object AnonymousClasses extends App{
  /*
    1. Generic trait MyPredicate[-T] with a little method test(T) => Boolean
    2. Generic trait MyTransformer[-A,B] with a method transform(A) => B
    3. MyList:
      - map(transformer) => MyList
      - filter(predicate) => MyList
      - flatMap(transformer from A to MyList[B]) => MyList[B]

      class EvenPredicate extends MyPredicate[Int]
      class StringToIntTransformer extends MyTransformer[String,Int]

      [1,2,3].map(n * 2) =>[2,4,6]
      [1,2,3,4].filter(n%2)=>[2,4]
      [1,2,3].flatMap(n=>[n,n+1]) => [1,2,2,3,3,4]
     */
  trait MyTransformer[-A,B] {
    def transform(elem:A):B
  }
  trait MyPredicate[-T] {
    def test(elem:T):Boolean
  }


  abstract class MyList[+A] {
    /*
    head = first element of the list
    tail = remainder of the list
    isEmpty = is this list empty
    add(int) => new list with this element added
    toString => a string representation of the list
     */

    def head: A

    def tail: MyList[A]

    def isEmpty: Boolean

    def add[B >: A](element: B): MyList[B]

    def printElements: String

    override def toString: String = "[" + printElements + "]"
  }

  object Empty extends MyList[Nothing] {
    def head: Nothing = throw new NoSuchElementException()
    def tail: MyList[Nothing] = throw new NoSuchElementException()
    def isEmpty: Boolean = true
    def add[B >: Nothing](element: B): MyList[B] = new Cons(element, Empty)
    override def printElements: String = ""
  }

  //亦可写成class Cons[A](h: A, t: MyList[A]) extends MyList[A]
  class Cons[A](h: A, t: MyList[A]) extends MyList[A] {
    def head: A = h
    def tail: MyList[A] = t
    def isEmpty: Boolean = false
    def add[B >: A](element: B): MyList[B] = new Cons(element, this)
    override def printElements: String =
      if (t.isEmpty) "" + h
      else h + " " + t.printElements
  }



}
