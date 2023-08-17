
sealed trait List[+A] {
  def head():A
  def tail():List[A]
  def isEmpty:Boolean
  def printElement:String
}
case object Nil extends List[Nothing] {
  override def head():Nothing = throw new NoSuchElementException()
  override def tail():List[Nothing] = throw new NoSuchElementException()  
  override def isEmpty:Boolean = true
  override def toString:String = "[" + printElement +"]"
  override def printElement:String = " "
}
case class Cons[+A](heads:A,tails:List[A]) extends List[A] {
  override def head():A = heads
  override def tail():List[A] = tails
  override def isEmpty:Boolean = false
  override def toString:String = "[ " + printElement + "]"
  override def printElement():String = heads + " " + tails.printElement
}

object List{
  def sum(ints:List[Int]):Int = ints match{
    case Nil => 0
    case Cons(x,xs) => x+sum(xs)
  }

  def produce(ds:List[Double]):Double = ds match{
    case Nil => 1.0
    case Cons(0,_) => 0
    case Cons(x,xs) => x * produce(xs)
  }

  def apply[A](as: A*):List[A] =
    if(as.isEmpty) Nil
    else Cons(as.head,apply(as.tail: _*))
  
  def setHead[A](newHead:A,oldList:List[A]):List[A]=
    if(oldList.isEmpty) Cons(newHead,Nil)
    else Cons(newHead,oldList.tail)

  def drop[A](l:List[A],n:Int):List[A]=
    if(l.isEmpty) Nil
    else if(n==1) l.tail
    else drop(l.tail,n-1) 

  def dropWhile[A](l:List[A],f:A=>Boolean):List[A]=
    if(l.isEmpty) Nil
    else if(f(l.head)) dropWhile(l.tail,f)
    else l

  def append[A](a1:List[A],a2:List[A]):List[A]=
    a1 match{
      case Nil => a2
      case Cons(h,t) =>Cons(h,append(t,a2))
    }
  def init[A](l:List[A]):List[A]=
    l match{
      case Nil => Nil
      case Cons(h,Nil) => Nil
      case Cons(h,t) => Cons(h,init(t))
    }
}

object SinglyLinkedList extends App{
  println("Singly Linked List")
  val a = List(1,2,3,4,5,6,8)
  println(List.sum(a))
  println(a.tail)
  val b = List.setHead(99,a)
  println(b)
  println(List.drop(a,3))
  println(List.drop(a,11))
  println(List.drop(a,7))
  println("dropWhile test")
  println(List.dropWhile(a,{x:Int=>x<=4}))
  //function as parameter has to be declared like {}.Otherwise illegal start of declaration
  //println(List.dropWhile(a,x:Int=>{x<=4}))
  println("append test")
  println(List.append(List(3,4,5,6),a))
  println("init test")
  println(List.init(a))

}
