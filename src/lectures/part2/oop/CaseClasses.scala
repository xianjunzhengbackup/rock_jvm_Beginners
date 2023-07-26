package lectures.part2.oop

object CaseClasses extends App {
    /*
    equals,hashCode,toString
    它自带这些method
     */
  case class Person(name:String,age:Int)

  // 1. Class parameter are field
  //就是说相当于是 val name:String,val age:Int
  val jim = new Person("Jim",34)
  println(jim.name)

  //2. sensible toString
  println(jim)
  println(jim.toString)
  //with such output "Person(Jim,34)"

  //3. equals and hasCode implemented
  val jim2 = new Person("Jim",34)
  println(jim == jim2)

  //4. case class have handy copy method
  val jim3 = jim.copy(age=45)
  println(jim3)

  //5. case have companion objects
  val thePerson = Person
  val mary = Person("Mary",23)

  //6. case class are serializable

  //7. case class have extractor pattern = case class can be used in pattern matching

  case object UnitedKingdom {
    def name:String = "The UK of GB amd NI"
  }

  /*
  Expand MyList - use case classes and case objects
   */
}
