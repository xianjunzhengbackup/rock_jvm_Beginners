package lectures.part2.oop

object 协变和逆变 extends App{
  /*
  缘起：
  通常来说，一个派生类型的集合不应该赋值给一个基
  类型的集合。然而，有时候我们需要放宽这一规则。在这些情况下，我们可以要求 Scala 允
  许在其他情况下无效的转换。
  在期望接收一个基类实例的集合的地方，能够使用一个子类实例的集合的能力叫作协变
  （covariance）。而在期望接收一个子类实例的集合的地方，能够使用一个超类实例的集合的能
  力叫作逆变（contravariance）。在默认的情况下，Scala 都不允许（即不变）。
   */
  println("----------------------------------协变------------------------------------------------------------------")
  /*
  虽然 Scala 的默认行为总的来说是好事，但是我们还是想要小心地将派生类型的集合，
  也就是 Dog 的集合，看作是其基类型的集合，也就是 Pet 的集合。考虑下面的例子。
   */
  class Pet(val name: String) {
    override def toString: String = name
  }

  class Dog(override val name: String) extends Pet(name)

  /*
  def workWithPets(pets: Array[Pet]): Unit = {}
  我们定义了两个类，其中 Dog 类扩展了 Pet 类。我们有一个方法 workWithPets，它
  接受一个 Pet 的数组，但是实际上什么也没做。现在让我们来创建一个 Dog 的数组。
  val dogs = Array(new Dog("Rover"), new Dog("Comet"))
  如果我们把 dogs 传递给前面的方法，我们将会得到一个编译错误：
  workWithPets(dogs) // 编译错误
  Scala 将会抱怨对 workWithPets()方法的调用—我们不能将一个包含 Dog 的数组发
  送给一个接受 Pet 的数组的方法。但是，这个方法是无害的。然而，Scala 并不知道这一点，
  所以它试图保护我们。我们必须要告诉 Scala，我们允许这样做。下面是一个我们如何能够做
  到这一点的例子①。
  */
  def playWithPets[T <: Pet](pets: Array[T]): Unit =
    println("Playing with pets: " + pets.mkString(", "))
  /*
  我们使用一种特殊语法定义了 playWithPets()方法。T <: Pet 表明由 T 表示的类
  派生自 Pet 类。这个语法用于定义一个上界（如果可视化这个类的层次结构，那么 Pet 将
  会是类型 T 的上界），T 可以是任何类型的 Pet，也可以是在该类型层次结构中低于 Pet 的
  类型。通过指定上界，我们告诉 Scala 数组参数的类型参数 T 必须至少是一个 Pet 的数组，
  但是也可以是任何派生自 Pet 类型的类的实例数组。因此，现在我们可以执行下面的调用了。
   */
  val dogs = Array(new Dog("Rover"), new Dog("Comet"))
  playWithPets(dogs)
  val pets = Array(new Pet("pet1"),new Pet("pet2"))
  playWithPets(pets)
  /*
  如果不用playWithPets的方法，也可以重新定义一个Array，让它变成协变的类*/
  abstract class MyList[+A] {
    def head:A
    def tail:MyList[A]
    def isEmpty:Boolean
    def add[B >:A](element:B):MyList[B]
    def printElements:String
    override def toString: String = "[" + printElements + "]"
  }

  object Empty extends MyList[Nothing] {
    def head: Nothing = throw new NoSuchElementException()
    def tail: MyList[Nothing] = throw new NoSuchElementException()
    def isEmpty: Boolean = true
    def add[B >: Nothing](element: B): MyList[B] = new Cons(element,Empty)
    override def printElements: String = ""
  }

  class Cons[A](h:A,t:MyList[A]) extends MyList[A] {
    def head: A = h
    def tail: MyList[A] = t
    def isEmpty: Boolean = false
    def add[B >:A](element: B): MyList[B] = new Cons(element,this)
    override def printElements: String =
      if(t.isEmpty) "" + h
      else h + " " + t.printElements
  }

  def workWithPets(pets: MyList[Pet]): Unit = {println(pets)}
  val DogList = new Cons[Dog](new Dog("dog1"), Empty).add(new Dog("dog2"))
  workWithPets(DogList)

  println("-----------------------------逆变------------------------------------------------")
  /*
  现在，假设我们想要将宠物从一个集合复制到另外一个集合，那么我们可以编写一个名
  为 copy()的方法，其接受两个类型为 Array[Pet]的参数。然而，这将不能帮助我们传递
  一个 Dog 的数组。此外，我们应该能够从一个 Dog 的数组复制到一个 Pet 的数组。换句话
  说，在这个场景下，接收数组中元素类型是源数组中元素类型的超类型也是可以的。这里我们需要的是一个下界①。
   */
  def copyPets[S, D >: S](fromPets: Array[S], toPets: Array[D]): Unit = { //...
  }
  copyPets(dogs, pets)
  /*
  我们限定了目标数组的参数化类型（D），将其限定为源数组的参数化类型（S）的一个
  超类型。换句话说，S（对于像 Dog 一样的源类型）设定了类型 D（像 Dog 或者 Pet 这样的
  目标类型）的下界—它可以是类型 S，也可以是它的超类型。
   */
  println("---------------------------定制集合的型变----------------------------------")
  /*
  在前面的两个例子中，我们控制了方法定义中方法的参数。如果你是一个集合类的作者，
  你也可以控制这一行为，也就是说，如果你假定派生类型的集合可以被看作是其基类型的集
  合。你可以通过将参数化类型标记为+T 而不是 T 来完成这项操作，如下所示。
  MakingUseOfTypes/MyList.scala
  class MyList[+T] //...
  var list1 = new MyList[Int]
  var list2: MyList[Any] = _
  list2 = list1 // 编译正确
  在这里，+T 告诉 Scala 允许协变 ②
  ；换句话说，在类型检查期间，它要求 Scala 接受一个
  类型或者该类型的派生类型。因此，你可以将一个 MyList[Int]的实例赋值给一个
  MyList[Any]的引用。需要记住的是，这不能是 Array[Int]。然而，这可以是 Scala 库
  中实现的 List—我们将会在第 8 章对其进行讨论。
  同样，通过使用参数化类型-T 而不是 T，我们可以要求 Scala 为自己的类型提供逆变支持。
  在默认情况下，Scala 编译器将会严格检查型变。我们也可以要求对协变或者逆变进行宽
  大处理。无论如何，Scala 都会检查型变是否可靠。
   */
}
