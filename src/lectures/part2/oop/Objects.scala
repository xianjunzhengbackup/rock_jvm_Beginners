package lectures.part2.oop

object Objects extends App{
  //SCALA DOES NOT HAVE CLASS-LEVEL FUNCTIONALITY
  object Person{  //type + its only instance
    val N_EYES=2
    def canFly:Boolean=false

    //factory method
    //一般情况下，object里面都会有一个factory method来实例化一个companion类，比如下面的from
    def from(mother:Person,father:Person):Person=new Person("Bobbie")
    //更通常的写法是，针对这个要实例化companion class的factory method，我们通常愿意用apply method
    def apply(mother:Person,father:Person):Person=new Person("Bobbie")
  }
  //前面的class都需要new一个instance才能使用。对于静态的全局变量，我们希望直接拿来就能用，不需要初始化，所以出现了object。object不带parameter
  //object就是一个类型type，同时它也是该类型的唯一实例。 type+its only instance就是这意思

  println(Person.N_EYES)
  println(Person.canFly)

  //scala object = single instance就是说object就是single instance
  val Anothermary=Person
  val Anotherjohn=Person
  println(Anothermary==Anotherjohn)
  //mary 和 john都指向同一个object，因为object是single instance

  //COMPANION object一般与与它同名的class出现，同名的class就是它的companion
  class Person(val name:String){
    //instance-level functionality
  }

  val mary=new Person("Mary")
  val john=new Person("John")
  val AnotherBobbie=Person.from(mary,john)
  //调用apply method来直接实例化一个companion class
  val bobbie=Person(mary,john)

}
