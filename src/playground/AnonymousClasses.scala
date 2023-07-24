package playground

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
    def transform(v:A):B
  }
  trait MyPredicate[-T] {
    def test(v:T):Boolean
  }

  class EvenPredicate extends MyPredicate[Int] {
    override def test(v: Int): Boolean = v % 2 == 0
  }

  class StringToIntTransformer extends MyTransformer[String, Int] {
    override def transform(v: String): Int = v.toInt
  }
  abstract class MyList[+A] {
    /*
    head = first element of the list
    tail = remainder of the list
    isEmpty = is this list empty
    add(int) => new list with this element added
    toString => a string representation of the list
     */
    def map[B](f:A=>B):MyList[B]
    def map[B](trans:MyTransformer[A,B]) :MyList[B]
    def filter[B>:A](f:B=>Boolean):MyList[B]
    def filter(pre:MyPredicate[A]):MyList[A]
    def flatMap[B>:A](f:B=>MyList[B]):MyList[B]

    def head: A

    def tail: MyList[A]

    def isEmpty: Boolean

    def add[B >: A](element: B): MyList[B]

    def +[B >:A](anotherList:MyList[B]):MyList[B]

    def printElements: String

    override def toString: String = "[" + printElements + "]"
  }

  object Empty extends MyList[Nothing] {
    def map[B](f:Nothing=>B) = throw new NoSuchElementException()
    def map[B](trans:MyTransformer[Nothing,B]) :MyList[B] = throw new NoSuchElementException()
    def filter[B>:Nothing](f:B=>Boolean) = this
    def flatMap[B>:Nothing](f:B=>MyList[B]):MyList[B] = this
    def head: Nothing = throw new NoSuchElementException()
    def tail: MyList[Nothing] = throw new NoSuchElementException()
    def isEmpty: Boolean = true
    def +[B>:Nothing](anotherList:MyList[B]):MyList[B] = anotherList
    def add[B >: Nothing](element: B): MyList[B] = new Cons(element, Empty)
    override def printElements: String = ""
  }

  //亦可写成class Cons[A](h: A, t: MyList[A]) extends MyList[A]
  class Cons[A](h: A, t: MyList[A]) extends MyList[A] {
    def map[B](f:A=>B):MyList[B] = {
      if (t.isEmpty) new Cons(f(h),Empty)
      else new Cons(f(h),t.map(f))
    }

    def map[B](trans:MyTransformer[A,B]) :MyList[B] ={
      if(t.isEmpty) new Cons(trans.transform(h),Empty)
      else new Cons(trans.transform(h),t.map(trans))
    }
    def filter[B>:A](f:B=>Boolean):MyList[B] = {
      if(isEmpty) Empty
      else if(f(h)) new Cons(h,t.filter(f))
      else t.filter(f)
    }

    def +[B>:A](anotherList:MyList[B]):MyList[B] = {
//      def addList[B>:A](anotherList:MyList[B]):MyList[B] = {
      if(anotherList.isEmpty) this
//      else new Cons(anotherList.head,( anotherList.tail + this))
      else new Cons(h, tail + anotherList)
    }
    def flatMap[B>:A](f:B=>MyList[B]):MyList[B] ={
      if(isEmpty) Empty
      else f(h) + tail.flatMap(f)
    }
    def head: A = h
    def tail: MyList[A] = t
    def isEmpty: Boolean = false
    def add[B >: A](element: B): MyList[B] = new Cons(element, this)
    override def printElements: String =
      if (t.isEmpty) "" + h
      else h + " " + t.printElements
  }

  val v = (new Cons[Int](3,Empty)).add(1).add(2).add(3)
  println(v)
  def Int_Int_M(v:Int):Int = v*3
  println(v.map(Int_Int_M _))
  val transformer = new MyTransformer[Int,Int] {
    override def transform(v: Int): Int = 3*v
  }
  println(v.map(transformer))

  println(v.filter(n=>n%3==0))
  val predicate = new MyPredicate[Int] {
    override def test(v: Int): Boolean = v%3==0
  }
  println(v.filter(predicate.test))

  println(v.flatMap(n=>new Cons(n,new Cons(n+1,Empty))))

  println("-----------------------------------------")

  val v_string = new Cons[String]("1",Empty).add("2").add("3").add("4")
  println(v_string.map(new StringToIntTransformer))

}
