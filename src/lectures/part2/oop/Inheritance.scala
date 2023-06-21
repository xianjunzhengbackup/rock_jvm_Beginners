package lectures.part2.oop

object Inheritance extends App{
  //single class inheritance
  class Animal{
    val creatureType="wild"
//    protected def eat=println("nomnom")
    def eat=println("nomnom")
  }
  class Cat extends Animal{
    def crunch={
      eat //this will be fine. protected method from superclass can be assessed from inside the inheritaed class
      println("crunch crunch")
    }
  }
  val cat = new Cat
  //cat.eat this will be illegal. protected method can only be assessed from Animal or inside the Cat
  //cat 只可以继承Animal中的None-private method
  cat.crunch

  //constructors
  class Person(name:String,age:Int){
    def this(name:String)=this(name,0)
  }
  //class Adult(name:String,age:Int,idCard:String) extends Person this will be illegal
  //因为scala不知道如何初始化父类
  //class Adult(name:String,age:Int,idCard:String) extends Person(name)
  //这样是可以的，因为父类已经有 this(name:String)的定义，scala知道如何初始化父类
  class Adult(name:String,age:Int,idCard:String) extends Person(name,age)

  //overriding
  class Dog(override val creatureType: String = "domestic") extends Animal{
    override def eat={
      super.eat //this is how you implement superclass's method
      println("Crunch,crunch")
    }

    //override val creatureType: String = "domestic"
    //或者将它放在constructor中来override
  }

  val dog = new Dog("K9")
  dog.eat
  println(dog.creatureType)

  //type substitution (broad: polymorphism)
  //明明定义的是Animal，eat调用的是Dog的method
  val unknownAnimal:Animal = new Dog("k9")
  unknownAnimal.eat

  //super

  //preventing overrides
  //1. final on memeber
  //2. use final on the entire class
  //3. sealed the class = extend classes in this file, prevent extension in other files
}
