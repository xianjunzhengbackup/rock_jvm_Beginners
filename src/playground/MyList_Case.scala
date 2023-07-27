package playground


object MyList_Case extends App {
  abstract class MyList{
  /*
  head = first element of the list
  tail = remainder of the list
  isEmpty = is this list empty
  add(int) => new list with this element added
  toString => a string representation of the list
   */
  def head:Int 
  def tail:MyList 
  def isEmpty:Boolean 
  def add(element:Int):MyList 
  def printElements:String 

  override def toString: String = "[" + printElements + "]"
}

case object Empty extends MyList{
  def head: Int = throw new  NoSuchElementException()

  def tail: MyList = throw new NoSuchElementException()

  def isEmpty: Boolean = true

  def add(element: Int): MyList = new Cons(element,Empty)

  override def printElements: String = ""

  def apply(element:Int):MyList = add(element)
}

case class Cons(h:Int,t:MyList) extends MyList{
  def head: Int = h

  def tail: MyList = t

  def isEmpty: Boolean = false

  def add(element: Int): MyList = new Cons(element,this)

  def apply(element:Int):MyList = new Cons(element,this)

  override def printElements: String =
    if(t.isEmpty) "" + h
    else h + " " + t.printElements
}  

  val list = Empty.add(1).add(2).add(3).add(4).add(5)
  println(list.toString)
  val anotherList=Empty(1)
  println(anotherList)
}
