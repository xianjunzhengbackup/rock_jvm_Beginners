package lectures.part2.oop
import scala.collection._

object Singleton extends App{
  /*
  单例指的是只有一个实例的
  类。用单例可以表示那种对某些操作集中访问的对象，如数据库操作、对象工厂等。
  单例模式易于理解，但在 Java 中其实很难实现，参考 Joshua Bloch 的 Effective Java[Blo08]
  一书中的讨论。幸运的是，在 Scala 中这个问题在编程语言层面就已经解决了。创建一个单
  例要使用关键字 object 而不是 class。因为不能实例化一个单例对象，所以不能传递参数
  给它的构造器。
  下面的例子中有一个名为 MarkerFactory 的单例和一个名为 Marker 的类。
   */
  class Marker(val color: String) {
    println(s"Creating ${this}")

    override def toString = s"marker color $color"
  }

  object MarkerFactory {
    private val markers = mutable.Map(
      "red" -> new Marker("red"),
      "blue" -> new Marker("blue"),
      "yellow" -> new Marker("yellow"))

    def getMarker(color: String): Marker =
      markers.getOrElseUpdate(color, new Marker(color))
  }

  println(MarkerFactory getMarker "blue")
  println(MarkerFactory getMarker "blue")
  println(MarkerFactory getMarker "red")
  println(MarkerFactory getMarker "red")
  println(MarkerFactory getMarker "green")
  /*
  在这个例子中，Marker 类表示一个带有初始颜色的颜色标记器。MarkerFactory 是
  一个能够帮助我们复用预先创建好的 Marker 实例的单例。
  可以直接用 MarkerFactory 这个名字访问这个单例—唯一的实例。一旦定义了一个
  单例，它的名字就代表了这个单例对象的唯一实例。
  然而，上面的代码中还有一个问题。我们不经过 MarkerFactory 就可以直接创建一个
  Marker 的实例。下面我们看一下如何在相应单例工厂中限制一个类的实例的创建。
   */

  //-----------------------------------------------------------------------------------------------------------------
  println("----------------------------------------------------------------------------------------------------------")
  /*
  前面提到的 MarkerFactory 是一个独立对象（stand-alone object）。它和任何类都没有
  自动的联系，尽管我们用它来管理 Marker 的实例。
  可以选择将一个单例关联到一个类。这样的单例，其名字和对应类的名字一致，因此被
  称为伴生对象（companion object）。相应的类被称为伴生类。我们在后面可以看到这种方式
  非常强大。
  在前面的例子中，我们想规范 Marker 实例的创建。🚩类与其伴生对象间没有边界—它
  们可以相互访问私有字段和方法。🚩一个类的构造器，包括主构造器，也可以标记为 private。
  我们可以结合这两个特性来解决前一节末尾特别提出的问题。下面是使用一个伴生对象对
  Marker 这个例子进行的重写。
   */
  class AnotherMarker private(val color: String) {
    println(s"Creating ${this}")

    override def toString = s"marker color $color"
  }

  object AnotherMarker {
    private val markers = mutable.Map(
      "red" -> new AnotherMarker("red"),
      "blue" -> new AnotherMarker("blue"),
      "yellow" -> new AnotherMarker("yellow"))

    def getMarker(color: String): AnotherMarker =
      markers.getOrElseUpdate(color, new AnotherMarker(color))
  }

  println(AnotherMarker getMarker "blue")
  println(AnotherMarker getMarker "blue")
  println(AnotherMarker getMarker "red")
  println(AnotherMarker getMarker "red")
  println(AnotherMarker getMarker "green")
  /*
  Marker 的构造器被声明为 private；然而，它的伴生对象可以访问它。因此，我们可
  以在伴生对象中创建 Marker 的实例。如果试着在类或者伴生对象之外创建 Marker 的实例，
  就会收到错误提示。
  每一个类都可以拥有伴生对象，伴生对象和相应的伴生类可以放在同一个文件中。在Scala 中，
  伴生对象非常常见，并且通常提供一些类层面的便利方法。伴生对象还能作为一种
  非常好的变通方案，弥补 Scala 中缺少 static 成员的事实
  上面的例子用两个private封印掉了外界直接调用AnotherMarker伴生类的可能
  */
}
