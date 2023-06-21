package playground

abstract class MyList[+A] {
  /*
  head = first element of the list
  tail = remainder of the list
  isEmpty = is this list empty
  add(int) => new list with this element added
  toString => a string representation of the list
   */
  def head:A
  def tail:MyList[A]
  def isEmpty:Boolean
  def add[B >:A](element:B):MyList[B]
  def printElements:String

  override def toString: String = "[" + printElements + "]"
}

object Empty extends MyList[Nothing] {
  /*
  好玩的地方是这里
  当程序运行时，假如我要初始化两个对象
  val listOfInteger:MyList[Int] = Empty
  val listOfString:MyList[String]=Empty
  这两对象应该要能用同一个Empty object来初始化，也就是说这个Empty object要能初始化任意一种类型
  Nothing is substitue for any type
  it’s also a subtype of every other type in Scala — even the classes and traits we define.
   */
  def head: Nothing = throw new NoSuchElementException()

  def tail: MyList[Nothing] = throw new NoSuchElementException()

  def isEmpty: Boolean = true

  def add[B >: Nothing](element: B): MyList[B] = new Cons(element,Empty)

  override def printElements: String = ""
}

class Cons[+A](h:A,t:MyList[A]) extends MyList[A] {
  def head: A = h

  def tail: MyList[A] = t

  def isEmpty: Boolean = false

  def add[B >:A](element: B): MyList[B] = new Cons(element,this)

  override def printElements: String =
    if(t.isEmpty) "" + h
    else h + " " + t.printElements
}

object ListTest extends App{
  val listOfInteger : MyList[Int] = Empty
  println(listOfInteger.add(1).add(2).add(3).add(4).add(5))
  println("------------------------------------------------------------")
  def workWithPets(pets: MyList[Pet]): Unit = {}
  val DogList = new Cons[Dog](new Dog("dog1"),Empty).add(new Dog("dog2"))
  println(DogList)
  workWithPets(DogList)
}

class Pet(val name: String) {
  override def toString: String = name
}

class Dog(override val name: String) extends Pet(name)