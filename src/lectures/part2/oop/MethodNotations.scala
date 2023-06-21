package lectures.part2.oop
import scala.language.postfixOps

object MethodNotations extends App{
  class Person(val name:String,favouriteMovie:String){
    def likes(movie:String)=movie==favouriteMovie
    def handOutWith(person:Person)=s"$name is hanging out with ${person.name}"
    def unary_! : String =s"Hello,what is the wreck"
    def isAlive=true
    def apply():String=s"Hi,my name is $name and I like $favouriteMovie"
  }

  val mary = new Person("Mary","Inception")
  println(mary.likes("Inception"))
  println(mary likes "Inception") //equivalent
  //infix notation = operator notation 这种写法只能用在单参数的method中

  //"operators" in scala
  val tom=new Person("Tom","Flight Club")
  println(mary handOutWith tom)
  //这样的Method handoutwith，弄的它像Operator，将handoutwith换成+，毫无违和

  println(1 + 2)
  println(1.+(2))
  //ALL OPERATORS ARE METHODS

  //prefix notation
  val x = -1  //equivalent 1.unary_-
  val y = 1.unary_-
  //unary_prefix only works with - + ~ !

  println(!tom)
  println(tom.unary_!)

  //postfix notation作用于没有参数的method
  println(mary.isAlive)
  println(mary isAlive)

  //apply
  println(mary.apply())
  println(mary()) //equivalent
}
