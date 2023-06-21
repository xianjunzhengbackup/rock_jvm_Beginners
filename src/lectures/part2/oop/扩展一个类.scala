package lectures.part2.oop

object 扩展一个类 extends App {
  /*
  在 Scala 中扩展一个基类和 Java 中很像，只是多了两个非常好的限制：其一，方法的重
  载必须用 override 关键字；其二，只有主构造器能传递参数给基类的构造器。
  Java 5 开始引入了@Override 注解，但是在 Java 中该注解的使用是可选的。Scala 在重
  载一个方法的时候强制使用关键字 override① 。通过强制 override 关键字，Scala 就能帮
  助减少错误，如经常出现在方法名中的拼写错误，还可以避免无意识的方法重载或者想要重
  载一个基类方法却编写了一个新方法。
  在 Scala 中，辅助构造器必须调用主构造器或者其他辅助构造器。除此之外，只能在主构造器
  中传递参数给一个基类的构造器。一开始这看起来像是一个不必要的限制，但是 Scala 强制使用这
  条规则是有合理的理由的—它能够减少那些往往由多个构造器中的重复逻辑而引入的错误。
  本质上，主构造器在初始化一个类的实例时扮演了入口的角色，以初始化为目的并与基
  类的交互只能在这里控制。
  举个例子，我们来扩展一个类。
   */
  class Vehicle(val id: Int, val year: Int) {
    override def toString = s"ID: $id Year: $year"
  }

  class Car(override val id: Int, override val year: Int, var fuelLevel: Int)
    extends Vehicle(id, year) {
    override def toString = s"${super.toString} Fuel Level: $fuelLevel"
  }

  val car = new Car(1, 2015, 100)
  println(car)
  /*
  因为 Car 中的属性 id 和 year 派生自 Vehicle，我们通过在类 Car 的主构造器相应
  的参数前加上关键字 override 标明了这一点。看到这个关键字，Scala 编译器就不会为这
  两个属性生成字段，而是会将这些属性的访问器方法路由到基类的相应方法。如果忘了在这
  两个参数前写上 override 关键字，就会遇到编译错误。
  因为我们在 Vehicle 和 Car 中重载了 java.lang.Object 的 toString()方法，所
  以我们也必须在 toString()的定义前写上 override。
  在扩展一个类时，必须将派生类的参数传递给基类的某个构造器。因为只有主构造器才
  能调用一个基类的构造器，所以我们把这个调用直接放在 extends 声明之后的基类名后面。
   */
  /*
  类的继承中，主构造器给基类传递参数，这一点与trait有点像，只不过特质是通过主类中同名的field来传参。
   */

}
