package lectures.part2.oop

object AbstractDataTypes extends App{
  //abstract
  abstract class Animal{
    val creatureType:String
    def eat:Unit
  }
  //虚函数，只是用来定义一个接口。不可以实例化,也不用提供具体的实现

  class Dog extends Animal{
    override val creatureType: String = "Canine"

    def eat: Unit = println("crunch crunch")
  }
  //因为继承的是虚函数，所以加不加override都一样。虚函数就是用来继承的。

  //traits
  trait Carnivore{
    def eat(animal:Animal):Unit
  }

  trait ColdBlooded
  class Crocodile extends Animal with Carnivore with ColdBlooded {
    override def eat: Unit = println("nomnomnom")
    override val creatureType="croc"

    override def eat(animal: Animal): Unit = println(s"I'm a croc and I'm eating ${animal.creatureType}")

  }

  val dog=new Dog
  val croc=new Crocodile
  croc.eat(dog)

  //traits vs abstract classes
  //1 - traits do not have constructor parameters
  //2 - multiple traits may be inherited by the same class
  //3 - traits = behavior, abstract class = type of thing
}
