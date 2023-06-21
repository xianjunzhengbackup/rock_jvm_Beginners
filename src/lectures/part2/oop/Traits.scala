package lectures.part2.oop

object Traits extends App {
  /*
  Java 只允许单继承，这会强制建立一种线性的层次结构模型。但现实世界中充满了横切
  关注点（crosscutting concerns）—一种横切且影响多个抽象的概念，这些抽象并不同属于
  某个单一的类层次结构 ① 。在典型的企业级应用程序中，安全、日志记录、验证、事务以及
  资源管理都是这些横切关注点的应用场景。但是，因为我们受限于单一的类层次结构，所以
  实现这些横切关注点变得相当困难，往往需要代码上的重复或者引入重量级工具 ②。Scala 使
  用特质（trait）解决了这个问题。
  特质类似于带有部分实现的接口，提供了一种介于单继承和多继承的中间能力，因为可
  以将它们混入或包含到其他类中。通过这种能力，可以使用横切特性增强类或者实例。如我
  们将在本章中所学习到的，通过 Scala 的特质，可以将横切关注点应用于任意类，并且免去
  了从多个实现继承产生的痛苦。
  7.1 理解特质
  特质是一种可以混入或者同化到类层次结构中的行为。例如，要做一个关于朋友的抽象
  建模，我们可以将一个 Friend 特质混入任何的类中，如 Man、Woman、Dog 等，而又不必
  让所有这些类都继承同一个公共基类。
   */
  /*
  这个例子其实要做的事情是这样的，建构一个人的模型，然后继承出男人，女人。作为人，他有朋友的属性
  在这里朋友的属性由trait来实现，这个朋友的特质，里面有两个field，一个是name，这个在特质中没有实现，
  将由继承特质的类中同名的field传递来实现;另外一个是作为朋友的method---listen，这个有具体实现
  同时还建构了一个狗的模型，它的基类是Animal，同时具备了朋友的属性。由于特质friend中的name没有具体实现，
  它将接受来自继承特质的类中同名的field，所以这样就实现类与特质共享数据的通道。
  也就是人与狗共享了朋友的特质。但是狗的朋友特质中的listen method将在狗的类中被override，因为
  它是安静的倾听，与人不同。
   */
  class Animal

  /*
  class Human(val name: String) {
  def listen(): Unit = println(s"Your friend $name is listening")
  }
  class Man(override val name: String) extends Human(name)
  class Woman(override val name: String) extends Human(name)
  这段代码的一个缺点是“友好”这个特点并不凸显，而且被合并到了 Human 类中。此外，
  经过几个星期的开发，我们发现我们忘记了人类最好的朋友。狗是人类的好朋友—当我们
  需要减压时，它们会安静地倾听。但是，在当前的设计中，我们很难让狗成为一个“朋友”。
  因为我们不能为此让 Dog 继承 Human。
  这就是 Scala 的特质派上用场的地方了。特质类似于一个带有部分实现的接口。我们在
  特质中定义并初始化的 val 和 var 变量，将会在混入了该特质的类的内部被实现。任何已
  定义但未被初始化的 val 和 var 变量都被认为是抽象的，混入这些特质的类需要实现它们。
  我们可以将 Friend 这个概念重新实现为一个特质。
   */
  trait Friend {
    val name: String
    def listen(): Unit = println(s"Your friend $name is listening")
  }
  /*
  在这里，我们将 Friend 定义为一个特质。它有一个名为 name 的 val（不可变变量），
  并且它是抽象的。我们还有 listen()方法的实现。name 的实际定义或者实现则将由混入
  了这个特质的类或者对象提供。让我们来看一下如何混入该特质。
   */
  class Human(val name: String) extends Friend

  class Woman(override val name: String) extends Human(name)

  class Man(override val name: String) extends Human(name)
  /*
  Human 类混入了 Friend 特质。如果一个类没有扩展任何其他类，则使用 extends 关
  键字来混入特质。Human 类以及它的派生类 Man 和 Woman 简单地使用了由 Friend 特质提
  供的 listen()方法。你很快将会看到，我们可以按照自己意愿选择重写该方法的实现。
  我们可以混入任意数量的特质。如果要混入额外的特质，要使用 with 关键字。如果一个类已
  经扩展了另外一个类（如在下一个示例中的 Dog 类），那么我们也可以使用 with 关键字来混入第一
  个特质。除了混入该特质之外，我们还重写了 Dog 类中的 listen()方法（实际上继承自该特质）。
   */
  class Dog(val name: String) extends Animal with Friend {
    // 选择性重写方法
    override def listen(): Unit = println(s"$name's listening quietly")
  }
  /*
  我们可以在混入了某个特质的类实例上调用该特质的方法，同时也可以将指向这些类的
  引用视为指向该特质的引用。
   */
  val john = new Man("John")
  val sara = new Woman("Sara")
  val comet = new Dog("Comet")
  john.listen()
  sara.listen()
  comet.listen()
  val mansBestFriend: Friend = comet
  mansBestFriend.listen()

  def helpAsFriend(friend: Friend): Unit = friend.listen()

  helpAsFriend(sara)
  helpAsFriend(comet)

  /*7.2 选择性混入
  特质看起来和类相似，但是也有一些显著的差异。首先，特质要求混入了它们的类去实
  现在特质中已经声明但尚未初始化的（抽象的）变量（val 和 var）。其次，特质的构造器
  不能接受任何参数 ①。特质连同对应的实现类被编译为 Java 中的接口，实现类中保存了特质
  中已经实现的所有方法②。
  多重继承通常会带来方法冲突，而特质则不会。通过延迟绑定类中由特质混入的方法，
  就可以避免方法冲突。就像你很快将会看到的，在一个特质中，对 super 的调用将被解析成
  对另一个特质或混入了该特质的类方法的调用。
   */

  println("---------------------------------------------------------------------------------------------------------")
  /*
  在前面的例子中，我们将 Friend 特质混入到了 Dog 类中。因此，任何 Dog 类的实例
  都可以被看作是一个 Friend，也就是说，所有的 Dog 都是 Friend。
  在某些场景下，这可能过于笼统了。如果我们想，我们也可以在实例级别有选择性地混
  入特质。在这种情况下，我们可以将某个类的特定实例视为某个特质的实例。让我们来看一
  个例子。
  class Cat(val name: String) extends Animal
  Cat 类没有混入 Friend 特质，因此我们不能将一个 Cat 类的实例看作是一个 Friend。
  如同我们在下面的代码中看到的，任何这样的尝试都会导致编译错误。
  def useFriend(friend: Friend): Unit = friend.listen()
  val alf = new Cat("Alf")
  val friend: Friend = alf // 编译错误
  useFriend(alf) // 编译错误

  然而，Scala 确实为爱猫人士提供了帮助，可以将我们的特殊宠物—Angel，看作是一
  位 Friend。在创建实例时，只需要使用 with 关键字对其进行标记即可。

   */

  class Cat(val name: String) extends Animal
  val angel = new Cat("Angel") with Friend
  angel.listen()
  helpAsFriend(angel)

  println("---------------------------------Traits as interfaces------------------------------------------------------------------------")
  /*
  Traits as interfaces
  Traits can be viewed as interfaces in other languages, for example,
  Java. However they, allow the developers to implement some or all
  of their methods. Whenever there is some code in a trait, the trait is
  called a mixin. Let's have a look at the following example:
   */
  trait Alarm {
    def trigger(): String
  }
  /*
  Here, Alarm is an interface. Its only method, trigger, does not have any
  implementation and if mixed in a non-abstract class, an
  implementation of the method will be required.
  Let's see another trait example:
   */
  trait Notifier {
    val notificationMessage: String

    def printNotification(): Unit = {
      System.out.println(notificationMessage)
    }

    def clear()
  }
  /*
  The Notifier interface shown previously has one of its methods
  implemented, and clear and the value of notificationMessage have to be
  handled by the classes that will mix with the Notifier interface.
  Moreover, the traits can require a class to have a specific variable
  inside it. This is somewhat similar to abstract classes in other
  languages.
   */
  /*
  Mixing in traits with variables
  As we just pointed out, traits might require a class to have a specific
  variable. An interesting use case would be when we pass a variable
  to the constructor of a class. This will cover the trait requirements:
   */
  class NotifierImpl(val notificationMessage: String) extends Notifier {
    override def clear(): Unit = System.out.println("cleared")
  }
  /*
  The only requirement here is for the variable to have the same
  name and to be preceded by the val keyword in the class definition.
  If we don't use val in front of the parameter in the preceding code,
  the compiler would still ask us to implement the trait. In this case,
  we would have to use a different name for the class parameter and
  would have an override val notificationMessage assignment in the class
  body. The reason for this behavior is simple: if we explicitly use val
  (or var), the compiler will create a field with a getter with the same
  scope as the parameter. If we just have the parameter, a field and
  internal getter will be created only if the parameter is used outside
  the constructor scope, for example, in a method. For completeness,
  case classes automatically have the val keyword prepended to
  parameters. After what we said it means that when using val, we
  actually have a field with the given name and the right scope, and it
  will automatically override whatever the trait requires us to do.
   */
  class AnotherNotifierImpl() extends Notifier {
    val notificationMessage: String ="blabla"
    override def clear(): Unit = System.out.println("cleared")
  }

  /*
  class ThirdNotifierImpl(notificationMessage: String) extends Notifier {
    override def clear(): Unit = System.out.println("cleared")
  }
  Error message:
  class ThirdNotifierImpl needs to be abstract.
  Missing implementation for member of trait Notifier:
    val notificationMessage: String = ???

    class ThirdNotifierImpl(notificationMessage: String) extends Notifier {

    在类的构造体中失去了val，notificationMessage就变成了parameter，只能在类的定义中使用，所以无法传递到特质中去。
   */
  println("------------------------Traits as classes-------------------------------------------------")
  /*
  Traits can also be seen from the perspective of classes. In this case,
  they have to implement all their methods and have only one
  constructor that does not accept any parameters. Consider the
  following:
   */
  trait Beeper {
    def beep(times: Int): Unit = {
      1 to times foreach (i => System.out.println(s"Beep number: $i"))
    }
  }
  /*
  Now, we can actually instantiate Beeper and call its method. The
  following is a console application that does just this
   */
  val TIMES = 10
  val beeper = new Beeper {}
  beeper.beep(TIMES)
  println("--------------------------Extending classes--------------------------------------------------")
  /*
  It is possible for traits to extend classes. Let's have a look at the
  following example:
   */
  abstract class Connector {
    def connect()

    def close()
  }

  trait ConnectorWithHelper extends Connector {
    def findDriver(): Unit = {
      System.out.println("Find driver called.")
    }
  }

  class PgSqlConnector extends ConnectorWithHelper {
    override def connect(): Unit = {
      System.out.println("Connected...")
    }

    override def close(): Unit = {
      System.out.println("Closed...")
    }
  }
  val pgsql = new PgSqlConnector
  pgsql.connect()
  pgsql.findDriver()
  pgsql.close()
  /*
  Here, as expected, PgSqlConnector will be obliged to implement the
  abstract class methods. As you can guess, we could have other traits
  that extend other classes and then we might want to mix them in.
  Scala, however, will put a limit in some cases, and we will see how it
  will affect us later in this chapter when we look at compositions.
   */
  println("----------------------------------Extending traits-------------------------------------------------------")
  /*
  Traits can also extend each other. Have a look at the following
  example:
   */
  trait Ping {
    def ping(): Unit = {
      System.out.println("ping")
    }
  }

  trait Pong {
    def pong(): Unit = {
      System.out.println("pong")
    }
  }

  trait PingPong extends Ping with Pong {
    def pingPong(): Unit = {
      ping()
      pong()
    }
  }
  val pingpong = new PingPong {}
  pingpong.pingPong()
  println("------------------------Composing-----------------------------------------------------------------")
  /*
  Composing at creation time gives us an opportunity to create
  anonymous classes without the need to explicitly define them. Also,
  if there are many different traits that we might want to combine,
  creating all the possibilities would involve too much work, so this
  helps make things easier for us.
   */
  //Composing simple traits
  //Let's see an example where we compose simple traits, which do not
  //extend other traits or classes:
  class Watch(brand: String, initialTime: Long) {
    def getTime(): Long = System.currentTimeMillis() - initialTime
  }

  val expensiveWatch = new Watch("expensive brand", 1000L) with Alarm with
    Notifier {
    override def trigger(): String = "The alarm was triggered."

    override def clear(): Unit = {
      System.out.println("Alarm cleared.")
    }

    override val notificationMessage: String = "Alarm is running!"
  }
  val cheapWatch = new Watch("cheap brand", 1000L) with Alarm {
    override def trigger(): String = "The alarm was triggered."
  }
  // show some watch usage.
  System.out.println(expensiveWatch.trigger())
  expensiveWatch.printNotification()
  System.out.println(s"The time is ${expensiveWatch.getTime()}.")
  expensiveWatch.clear()
  System.out.println(cheapWatch.trigger())
  System.out.println("Cheap watches cannot manually stop the alarm...")
  /*
  这个例子就写出composing的好处。首先有一个watch的基类，同时想实现两块手表，一块贵的，有alarm，也有notification
  另一快便宜，只有alarm。如果是放在其它语言，那么就必须在弄两个类。搁这里，不用新建类，就用基类watch带上不同的trait，就生成了
  不一样的类实例
   */
  //stop at page 137 of Scala-DesignPatterns-Second-Edition.pdf
}
