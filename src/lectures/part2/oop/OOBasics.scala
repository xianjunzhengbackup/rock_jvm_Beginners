package lectures.part2.oop

object OOBasics extends App {
  val person=new Person("John",26)
  println(person.age)
  person.greet("Jun")
  person.greet()

  val author=new Writer("Charles","Dickens",1812)
  val imposter=new Writer("Charles","Dickens",1812)
  val novel=new Novel("Great Expectations",1861,author)

  println(novel.authorAge)
  //因为是不同的instance，尽管内容一样，所以scala还是认为不一样
  println(novel.isWrittenBy(imposter))

  val c=new Counter(5)
  c.inc.inc.inc.print
  c.inc(5).print

}

//constructor
//没加val的name，就只是个parameter，而加了val的age就成了field。parameter无法被对象 access
//而field则可以被对象access
class Person(name:String,val age:Int){
  //body
  val x=2

  println(1+3)

  //method 这里用this.name就是为了区分greet里面的参数name,以及在constructor里的参数name
  def greet(name:String)=println(s"${this.name} says:Hi,$name")

  //overloading
  def greet()=println(s"Hi, I am $name")

  //multiple constructors
  def this(name:String)=this(name,0)  //this（name，0）调用的是上面的primary constructor
  def this()=this("Jun")

}

/*
  Novel and a Writer

  Writer: first name, surname, year
  -method fullname

  Novel: name,year of release,author
  -authorAge
  -isWrittenBy(author)
  -copy(new year of release) = new instance of Novel
 */
class Writer(firstName:String,surname:String,val year:Int){
  def fullName:String=firstName+" " +surname
}
class Novel(name:String,year:Int,author:Writer){
  def authorAge=year-author.year
  def isWrittenBy(authod:Writer)=authod==this.author
  def copy(newYear:Int):Novel=new Novel(name,newYear,author)
}

/*
  Counter class
  -receives an int value
  -method current count
  -method to inc/dec => new Counter
  -overload inc/dec to receive an amount

 */

class Counter(val count:Int){
  def inc = {
    println("incrementing")
    new Counter(count+1)
  }  //immutability scala里非常重要的概念}
  def dec = {
    println("decrementing")
    new Counter(count-1)}

  def inc(n:Int):Counter={
    if(n<=0) this
    else inc.inc(n-1)
  }

  def dec(n:Int):Counter={
    if(n<=0) this
    else dec.dec(n-1)
  }

  def print=println(count)
}