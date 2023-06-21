package lectures.part2.oop

object Generics extends App{
  class MyList[+A]{
    //use the type A
    /*
    def add(element:A):MyList[A] =  ???
    这样写会报错
    covariant type A occurs in contravariant position in type A of value element
        def add(element:A):MyList[A] =  ???


    class CovariantList[+A]
    val animal:Animal = new Cat
    val animalList: CovariantList[Animal] = new CovariantList[Cat]
    假如按照上面的写法
    现在 animalList.add(new Dog) 那么返回的是CovariantList[Dog]
    猫群加狗，应该变成动物群
    正确的写法如下：
     */
    def add[B >: A](element:B):MyList[B] = ???
  }

  class MyMap[Key,Value]
  val listOfIntegers = new MyList[Int]
  val listOfStrings = new MyList[String]

  //generic methods
  object MyList{
    def empty[A] : MyList[A] = new MyList[A]
  }
  val emptyListOfIntergers = MyList.empty[Int]

  //variance problem
  class Animal
  class Cat extends Animal
  class Dog extends Animal

  //If B extends A, should List[B] extend List[A]
  // 1. yes List[Cat] extends List[Animal] = COVARIANCE
  class CovariantList[+A]
  val animal:Animal = new Cat
  val animalList: CovariantList[Animal] = new CovariantList[Cat]
  // animalList.add(new Dog) ???

  //2. No = INVARIANCE
  class InvariantList[A]
  val invariantAnimalList:InvariantList[Animal] = new InvariantList[Animal]
  // Illegal --- val invariantAnimalList:InvariantList[Animal] = new InvariantList[Cat]

  // 3. Hell, no!CONTRAVARIANCE
  class Trainer[-A]
  val trainer:Trainer[Cat] = new Trainer[Animal]

  // bounded types
  //A <: Animal解读成这样：A的边界被限制在Animal的子类中
  class Cage[A <: Animal](animal: A)
  val cage = new Cage(new Dog)

  //expand MyList to be generic

}

object MyListExercise extends App{
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

    def add[B >: Nothing](element: B): MyList[B] = new Cons(element, Empty)
    //Empty继承自MyList[Nothing],根据MyList[+A]的定义，那么MyList[Nothing]也是任意MyList[A]的子类，所以这里new Cons(element, Empty)是合理的

    override def printElements: String = ""
  }
  //亦可写成class Cons[A](h: A, t: MyList[A]) extends MyList[A]
  class Cons[+A](h: A, t: MyList[A]) extends MyList[A] {
    def head: A = h

    def tail: MyList[A] = t

    def isEmpty: Boolean = false

    def add[B >: A](element: B): MyList[B] = new Cons(element, this)

    override def printElements: String =
      if (t.isEmpty) "" + h
      else h + " " + t.printElements
  }

  val listOfInteger: MyList[Int] = Empty
  println(listOfInteger.add(1).add(2).add(3).add(4).add(5))
  val p1:Cons[Int] = new Cons(1,Empty)
  println(p1)

}