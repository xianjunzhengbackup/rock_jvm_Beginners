package lectures.scala_guideline

object 函数值和闭包_6 extends App{
  /*
  在函数式编程中，函数是一等公民。函数可以作为参数值传入其他函数中，函数的返回值可
  以是函数，函数甚至可以嵌套函数。这些高阶函数在 Scala 中被称为函数值（function value）。闭
  包（closure）是函数值的特殊形式，会捕获或者绑定到在另一个作用域或上下文中定义的变量。
  因为 Scala 同时支持面向对象和函数式风格编程，所以除了对象分解（即面向对象，分
  而治之），也可以将函数值作为构件块来构筑应用程序。这有利于写出简洁、可复用的代码。
  在本章中，我们将学习如何在 Scala 中使用函数值和闭包。
   */
  println("------6.1 常规函数的局限性-----------")
  /*
  函数式编程的核心就是函数或者所谓的高阶函数。为了了解这些是什么，让我们从一个
  熟悉的函数开始。
  要算出从 1 到给定整数 number 区间内所有整数的总和，我们可能会编写代码如下：
  def sum(number: Int) = {
    var result = 0
    for (i <- 1 to number) {
      result += i
    }
    result
  }
  这段代码我们很熟悉，我们都使用不同的编程语言写过类似的代码超过百万次了。这就
  是所谓的命令式风格—不仅要说明做什么，还要说明怎么做。这就要求你在底层的细节上
  耗费心力。在 Scala 中，可以在需要时写这种命令式代码，但并不局限于此。
  虽然这段代码完成了工作，但它不可扩展。现在，如果额外需要计算给定区间内偶数的个数和
  奇数的个数，使用这段代码就会碰壁。我们会禁不住使用臭名昭著的代码复用模式—复制粘贴并
  修改。难堪吧！使用常规函数，我们做到这样也算是极致了，但这会导致代码重复、复用率低下。
  让我们另辟蹊径来解决手头的这个简单问题。我们可以用函数式风格而不是命令式风格
  来编程解决这个问题。我们可以传递一个匿名函数给遍历区间中整数的函数。换句话说，我
  们利用了一个中间层。我们传递的函数可以拥有不同的逻辑，以在迭代中完成不同的任务。
  让我们用函数式风格重写前面的代码。
   */
  println("---------6.2 可扩展性与高阶函数-------------")
  /*
  可以将其他函数作为参数的函数称为高阶函数。高阶函数能减少代码重复，提高代码复用性，
  简化代码。我们可以在函数中创建函数，将它们赋值给引用，并将它们传递给其他函数。Scala
  内部通过创建特殊类实例的方式处理这些所谓的函数值。①在 Scala 中，函数值实际上就是对象。
  让我们使用函数值重写上面的例子。有了这个新版本，我们就可以执行不同的操作，如
  对数值求和或者统计一个区间内偶数的个数。
  首先，通过循环遍历区间，将公共代码提取成一个名为 totalResultOverRange()
  的方法。
   */
  //def totalResultOverRange(number: Int, codeBlock: Int => Int) = {
  //  var result = 0
  //  for (i <- 1 to number) {
  //    result += codeBlock(i)
  //  }
  //  result
  //}
  def totalResultOverRange(number:Int, codeBlock: Int=>Int):Int = {
    if(number > 1) codeBlock(number) + totalResultOverRange(number-1,codeBlock)
    else codeBlock(1)
  }
  /*
  我们为方法 totalResultOverRange()定义了两个参数：第一个参数是 Int 类型的，
  表 示 要 遍 历 的 区 间 的 上 界 ； 第 二 个 参 数 有 些 特 殊 ， 它 是 一 个 函 数 值 ， 参 数 的 名 称 是
  codeBlock，它的类型是接受 Int 并返回 Int 的函数。方法 totalResultOverRange()
  的结果本身是一个 Int。
  在符号=>左边指定了函数的预期输入类型，在其右边指定了函数的预期输出类型。“输
  入=>输出”这种语法形式旨在帮助我们将函数的作用视为接收输入并转换为输出且不产生任
  何副作用的过程。
  在 totalResultOverRange()方法的主体中，我们遍历区间内的数值，并对每个元素调
  用由变量 codeBlock 引用的函数。这个给定的函数期望接收一个表示区间内元素的 Int，并返
  回 Int 作为该元素上的计算结果。计算或操作本身留给 totalResultOverRange()方法的调
  用者定义。我们对给定函数值的调用结果求和，并返回该总数。
  totalResultOverRange()方法中的代码移除了 6.1 节的例子中的重复代码。下面展
  示了我们如何调用该方法来获取区间内数值的总和：*/
  println(totalResultOverRange(11, i => i))
  /*
  我们将两个参数传递给该方法：第一个参数是所遍历的区间上限（11）；第二个参数实际
  上是一个匿名的即时函数（just-in-time function），即一个没有名称只有参数和实现的函数。
  在这个例子中，实现只是返回了给定的参数。在这个例子中，符号=>的左边是参数列表，右
  边是实现。Scala 能够从 totalResultOverRange()方法的参数列表中推断出参数 i 是 Int
  类型的。如果参数的类型或结果类型与预期的不匹配，Scala 会给出一个错误。
  对于一个简单的数值求和过程，与调用之前写的普通函数 sum()相比，调用 total
  ResultOverRange()方法需要一个数和一个函数作为参数就显得太笨重了。然而，新版本
  是可扩展的，我们可以用类似的方式调用它来完成其他操作。例如，如果我们想要对区间内
  的偶数求和而不是求总和，就可以像下面这样调用这个函数：*/
  println(totalResultOverRange(11, i => if (i % 2 == 0) i else 0))
  /*
  在这个例子中，如果输入是偶数，那么作为参数传入的函数值会返回输入本身；否则返
  回 0。因此，函数 totalResulOverRange()将只会对给定区间内的所有偶数求和。
  如果我们想要对奇数求和，就可以用如下方式调用这个函数：*/
  println(totalResultOverRange(11, i => if (i % 2 == 0) 0 else i))
  /*
  与 sum()函数不同，我们看到了如何扩展 totalResultOverRange()函数，从而在
  指定区间上使用不同的元素选取策略求和。
  这是使用高阶函数实现中间层的直接好处之一。
  函数和方法可以具有任意个数的函数值参数，它们可以是任何参数，而不仅仅是最后一
  个参数。
  使用函数值就很容易使代码符合 DRY（Don’t Repeat Yourself）原则（有关 DRY 原则的
  更多信息，参见 Andrew Hunt 和 David Thomas 的 The Pragmatic Programmer: From Journeyman
  to Master①[HT00]一书）。我们将公共代码收集到函数中，并将差异转化为方法调用的参数。
  接受函数值的函数和方法在 Scala 库中司空见惯，我们将在第 8 章中看到许多。Scala 可以轻
  松地将多个参数传递给函数值，如果需要，也可以定义参数的类型。
   */
  println("---------6.3 具有多个参数的函数值------------")
  /*
  在前面的示例中，函数值只接收一个参数。函数值其实可以接收零个或多个参数。我们
  看几个例子，来了解一下如何使用不同数量的参数定义函数值。
  在其最简单的形式中，函数值甚至可以不接收任何参数，只返回一个结果。这样的函数
  值就像一个工厂，它构造并返回一个对象。让我们看一个没有参数的函数值是怎样定义和使
  用的例子：
   */
  def printValue(generator: () => Int): Unit = {
    println(s"Generated value is ${generator()}")
  }

  printValue(() => 42)
  /*
  对于 printValue()这个函数，我们定义了一个名为 generator 的参数，这个参数是
  一个函数值，它用一对空的括号表示它不接受任何参数，并返回一个 Int。在这个函数中，
  我们像调用其他函数一样调用这个函数值，就像这样：generator()。在对 printValue()
  函数的调用中，我们创建了一个不接受任何参数并返回一个固定值 42 的函数值。这个函数值
  也可以返回一个随机值、新创建的值或者预缓存的值，而非一个固定值。
  我们已经知道如何传递零个或一个参数。要传递多个参数，我们需要在定义中提供逗号分
  隔的参数类型列表。让我们看一个例子，inject()函数将对 Int 数组中一个元素的操作结果
  传递给对下一个元素的操作。这是一种依次在每一个元素的操作上级联或累加结果的方式。
   */
  def inject(arr: Array[Int], initial: Int, operation: (Int, Int) => Int) = {
    var carryOver = initial
    arr.foreach(element => carryOver = operation(carryOver, element))
    carryOver
  }
  /*
  inject()方法有 3 个参数，即 Int 数组、注入 operation 中的初始 Int 值以及作为
  函数值的 operation 本身。在该方法中，我们将变量 carryOver 设置为初始值，并使用
  foreach()方法循环遍历给定数组中的元素。该方法接受一个函数值作为参数，它将数组中
  的每个元素作为参数值调用。在作为参数传递给 foreach()的函数中，我们使用两个参数
  （carryOver 和当前的元素）来调用给定的操作。我们将操作调用的结果保存到变量
  carryOver 中，以便在随后的操作调用中把这个值当作参数来传递。当我们为数组中每个
  元素都调用了一遍这个操作后，我们返回 carryOver 的最终值。
  我们来看几个使用 inject()方法的例子。下面演示了如何对数组中的元素进行求和：
   */
  val array = Array(2, 3, 5, 1, 6, 4)
  val sum = inject(array, 0, (carry, elem) => carry + elem)
  println(s"Sum of elements in array is $sum")
  /*
  inject()方法的第一个参数是一个数组，我们要对这个数组的元素求和。第二个参数是总
  和的初始值 0。第三个参数是一个用于实现对元素求和操作的函数，每次作用在一个元素上。如
  果不是求所有元素总和而是要找到所有元素中的最大值，我们同样也可以用 inject()方法：
   */
  val max = inject(array, Integer.MIN_VALUE, (carry, elem) => Math.max(carry, elem))
  println(s"Max of elements in array is $max")
  /*
  上面的例子帮助我们了解了如何传递多个参数。然而，为了遍历集合中的元素并执行操作，
  我们不必去实现自己的 inject()方法。Scala 标准库已经内置了这种方法。即 foldLeft()方
  法。下面是使用内置的 foldLeft()方法来获取数组中元素的总和和最大值的例子：*/
  val array1 = Array(2, 3, 5, 1, 6, 4)
  val sum1 = array1.foldLeft(0) { (sum, elem) => sum + elem }
  val max1 = array1.foldLeft(Integer.MIN_VALUE) { (large, elem) =>
  Math.max(large, elem)
  }
  println(s"Sum of elements in array is $sum1")
  println(s"Max of elements in array is $max1")
  /*
  为 了 使 代 码 更 加 简 洁 ， Scala 选 择了 一 些 方 法 并 为 它 们 定 义 了 一 些 简 称 和 记 号。
  foldLeft()方法有一个等效的/:操作符。我们可以用 foldLeft()或等效的/:操作符执
  行先前的操作。以冒号（:）结尾的方法在 Scala 中有特殊含义，8.5 节将介绍相关知识。让
  我们快速浏览一下如何使用该等效操作符而不是 foldLeft()：*/
  val sum2 = (0 /: array1) ((sum, elem) => sum + elem)
  val max2 = (Integer.MIN_VALUE /: array1) { (large, elem) => Math.max(large, elem) }
  println(s"Sum2 of elements in array1 is $sum2")
  println(s"max2 of elements in array1 is $max2")
  /*
  细心的读者可能已经注意到函数值被放到了大括号中，而不是和使用 foldLeft()方法
  时一样作为一个参数。这比将这些函数作为参数放在括号中好看多了。但是，如果在 inject()
  方法上尝试以下操作，我们将收到错误提示。
  FunctionValuesAndClosures/Inject3.scala
  val sum = inject(array, 0) { (carryOver, elem) => carryOver + elem }
  上面的代码将导致以下错误：
  Inject3.scala:9: error: not enough arguments for method inject: (arr:
  Array[Int], initial: Int, operation: (Int, Int) => Int)Int.
  Unspecified value parameter operation.
  val sum = inject(array, 0) {(carryOver, elem) => carryOver + elem}
  ^
  one error found
  这不是我们想要看到的。在享用那种和库方法一样的大括号效果之前，我们必须再学习
  一个概念—柯里化（currying）。*/
  println("--------------6.4 柯里化------------")
  /*
  Scala 中的柯里化（currying）会把接收多个参数的函数转化为接收多个参数列表的函数。
  如果你会用同样的一组参数多次调用一个函数，你就能用柯里化去除噪声并使代码更加有趣。
  我们来看一下 Scala 对柯里化做了怎样的支持。编写一个带有多个参数列表，每个参数
  列表只有一个参数的方法，而不要编写一个带有一个参数列表，含有多个参数的方法；在每
  个参数列表中，也可以接受多个参数。也就是说，要写成这样 
  def foo(a: Int)(b: Int)(c:Int) {}，
  而不是 def foo(a: Int, b: Int, c: Int) = {}。你可以这样调用，如
  foo(1)(2)(3)、foo(1){2}{3}，甚至可以是 foo{1}{2}{3}。
  我们来检验一下，在用多个参数列表定义一个方法时，到底发生了什么。看一下下面这
  个交互式 REPL 会话：
  scala> def foo(a: Int)(b: Int)(c:Int) = {}
  foo: (a: Int)(b: Int)(c: Int)Unit
  scala> foo _
  res0: Int => (Int => (Int => Unit)) = <function1>
  scala> :quit
  首先按照前面讨论过的，我们定义了函数 foo()。然后我们调用 foo _创建了一个部分
  应用函数（partially applied function）（就是含有一个或多个未绑定参数的函数）。部分应用函
  数在从其他函数创建可复用的临时便利函数时非常有用（我们将在 6.8 节中探讨更多细节）。
  我们本可以将创建好的部分应用函数赋值给一个变量，但在这个例子中这并不重要。我们专
  注于 REPL 中的信息。它展示了一系列（3 次）转换。链路中的每一个函数都接收一个 Int
  参数，并返回一个部分应用函数。然而最后一个是例外，它返回一个 Unit。
  在我们使用柯里化时部分应用函数的创建是 Scala 的内部逻辑。从实用的角度，柯里化
  帮助我们改善了传递函数值的语法。让我们用柯里化重写前一节中的 inject()方法。*/
  def Anotherinject(arr: Array[Int], initial: Int)(operation: (Int, Int) => Int): Int = {
    var carryOver = initial
    arr.foreach(element => carryOver = operation(carryOver, element))
    carryOver
  }
  /*
  两个版本的 inject()方法的唯一区别在于参数列表变成了多个。第一个参数列表接收
  两个参数，第二个只接收一个函数值。
  现在我们就没有必要再在括号中以逗号分隔的参数传递函数值了。我们可以用更美观的
  大括号来调用这个方法。*/
  val sum3: Int = Anotherinject(Array(1,2,3,4,5,6), 0) { (carryOver, elem) => carryOver + elem }
  println(s"sum3 for Array(1,2,3,4,5,6) is $sum")
  /*我们成功地使用柯里化将函数值从括号中移了出来。非常美观，但我们还可以更进一
  步—如果说函数值中的参数只使用一次，其本身可以更加简洁，且看 6.5 节。*/
  println("---------6.5 参数的占位符---------")
  /*
  Scala 用下划线（_）这个记号来表示一个函数值的参数。一开始下划线或许会让你觉得
  很隐晦，你一旦习惯了，就会发现这种写法能让代码变得简洁且容易修改。你可以用这个符
  号表示一个参数，但只有在你打算在这个函数值中只引用这个参数一次时可以这样做。你可
  以在一个函数值中多次使用下划线，但每个下划线都表示相继的不同参数。我们来看一个这
  个特性的例子。在下面的代码中，我们有一个带有两个参数的函数值。*/
  val arr3 = Array(1, 2, 3, 4, 5)
  val total3 = (0 /: arr3) { (sum, elem) => sum + elem }
  /*在这个例子中，方法/:用于计算变量 arr 表示的数组中元素的和。在这个函数值中，我
  们对 sum 和 elem 参数都只使用一次。我们可以用下划线来替代这两个名字，而不需要为这
  两个参数显式命名。*/
  val total33 = (0 /: arr3) { _ + _ }
  /*
  _的第一次出现代表第一个参数（sum），这个值在函数的调用中产生并传递到下一次调
  用。第二次出现代表第二个参数（elem），它是数组中的一个元素。
  让我们慢慢领会其中的含义—如果你觉得它表意隐晦，也很正常。一旦你很好地
  理解了其中的含义，那么它的可读性就会提升，也将会成为编写 Scala 代码时习以为常
  的细节。
  当显式定义参数时，除了提供参数名，还可以定义参数的类型。当使用下划线时，名字
  和类型都会被隐式指定。如果 Scala 无法断定类型，它就会报错。在那种情况下，可以给_指
  定类型，也可以使用带类型的参数名。
  有人或许会认为使用下划线代码太精简且难以阅读—sum 和 elem 这样的名字就非常
  有助于理解代码。这是一个正确的观点。但是，与此同时，尤其是在单个变量只出现一次时，
  给变量命名并马上使用这个参数就没有那么有用了。在这种情况下，你或许更想用_。要在
  合适的地方使用_，以使代码简洁且不失可读性，例如，下面这个例子。
  val negativeNumberExists1 = arr.exists { elem => elem < 0 }
  val negativeNumberExists2 = arr.exists { _ < 0 }*/
  println(Array(-1,-2,-3,4,5,6).exists{_<0})
  /*下划线替换了显式参数 elem，并减少了函数值中的噪声。我们还可以用其他手段进一
  步减少代码中的噪声，且看 6.6 节。*/
  println("---------6.6 参数路由---------")
  /*
  只要有意义，你有很多办法把自己见过的函数值变得更简洁。我们先创建一个在一组值
  中找最大值的例子，在其中使用 Math.max 方法来比较两个值（以获得其中较大者）。
  val largest =
  (Integer.MIN_VALUE /: arr) { (carry, elem) => Math.max(carry, elem) }
  在这个函数值中，我们把参数 carry 和 elem 传递给方法 max()方法以判定这两个值
  哪个更大。我们使用计算的结果，最终算出数组中最大的元素。应用在 6.5 节中所学到的知
  识，我们可以像下面这样使用_简化函数值并减少显式参数：
  val largest = (Integer.MIN_VALUE /: arr) { Math.max(_, _) }
  _不仅能表示单个参数，也能表示整个参数列表。因此我们可以将对 max()的调用改成
  如下形式：
  val largest = (Integer.MIN_VALUE /: arr) { Math.max _ }*/
  println((Integer.MIN_VALUE /: arr3){Math.max _})
  /*上面的代码中，_表示整个参数列表，也就是(参数 1, 参数 2)。如果只是为了按照同样
  的顺序将接收到的参数传递给底层的方法，我们甚至不需要_这种形式。我们可以进一步简
  化前面的代码：*/
  val largest = (Integer.MIN_VALUE /: arr3) { Math.max }
  println(largest)
  /*为了验证这段代码在语法上的正确性，Scala 编译器做了很多工作。首先，编译器会检查
  方法/:的函数签名，该签名决定了该方法接收两个参数列表—第一个参数列表接收一个对
  象，第二参数列表接收一个函数值。然后，编译器会要求这个函数值接收两个参数。一旦编
  译器推导出所接收的函数值的签名，那么它就会检查这个函数值是否接收两个参数。本例中
  的函数值没有用=>符号，我们只提供了一个实现，尽管我们没有指定 max()方法的参数，但
  是编译器也会知道这个方法接收两个参数。编译器让函数签名中的两个参数和 max()方法的
  两个参数对号入座，并最终执行正确的参数路由。
  在编译检查期间，如果其中任何一步的类型推断失败，编译器都会报错。例如，假设我
  们在函数值中调用了一个接收两个参数的方法，但是我们一个参数都没指定。在这种情况下，
  编译器就会报错说，即使算上隐式参数，目前也没有足够的参数传递给这个方法。
  调整 Scala 的简洁度到一个折中点，以达到你对可读性的要求。在利用 Scala 代码简洁性
  的同时，不要让代码变得含义模糊，一定要尽力保持在一个平衡点上。
  我们已经了解了定义函数值的不同方式。函数值很简洁，但是在不同的调用中重复同一
  个函数值就会导致代码冗余。我们来看一下去除这种冗余的各种方法。*/
  println("-------------6.7 复用函数值-------------")
  /*函数值能够帮助我们写出复用度更高的代码并消除代码冗余。但是，将一段代码作为参
  数嵌入方法中并不能做到代码复用。避免这种冗余很简单—可以创建对函数值的引用，然
  后复用它们。让我们看一个例子。
  我们来创建一个 Equipment 类，它接收一段计算逻辑用作模拟。可以将计算逻辑作为
  函数值传递给构造器。*/
  class Equipment(val routine: Int => Int) {
    def simulate(input: Int): Int = {
      print("Running simulation...")
      routine(input)
    }
  }
  /*在创建 Equipment 的实例时，我们可以将函数值作为一个参数传递给构造器，像下
  面这样。*/
    val equipment1 = new Equipment(
      { input => println(s"calc with $input"); input })
    val equipment2 = new Equipment(
      { input => println(s"calc with $input"); input })
    equipment1.simulate(4)
    equipment2.simulate(6)
  /*输出结果如下：
  Running simulation...calc with 4
  Running simulation...calc with 6
  在这段代码中，我们想在两个 Equipment 实例中使用相同的计算代码。遗憾的是，这
  段计算代码重复了。这段代码并不遵循 DRY 原则，如果想改变计算逻辑，我们就必须两个
  一起改。如果计算逻辑只写一次，然后复用，就非常好。我们可以把这个函数值赋值给一个
  val 变量，以便复用，如下所示。*/
  val calculator = { input: Int => println(s"calc with $input"); input }
  val equipment11 = new Equipment(calculator)
  val equipment22 = new Equipment(calculator)
  equipment11.simulate(4)
  equipment22.simulate(6)
  equipment22 simulate 6
  /*输出结果如下：
  Running simulation...calc with 4
  Running simulation...calc with 6
  我们把函数值存储在了一个名为 calculator 的引用中。在定义这个函数值的时候，需
  要在类型信息上做一些标注。在前一个例子中，Scala 根据调用的上下文推导出了参数 input
  的类型是 Int。但是，因为我们将这个函数值定义为一个独立的 val 变量，所以我们必须告
  诉 Scala 参数的类型是什么。然后我们将这个引用的名字作为一个参数传递给我们创建的两
  个实例的构造器中。
  在前面的例子中，我们给函数值创建了一个名为 calculator 的引用。因为我们已经习
  惯了在函数或者方法中定义引用或者变量，所以这样做会让我们觉得更加自然。然而，在 Scala
  中，我们可以在函数中定义完整的函数。因此，为了达到代码复用的目的，还有一种更加符
  合习惯的方法。Scala 的灵活性能够让我们做正确的事更加容易。我们可以在预期接收函数值
  的地方传入一个常规函数。*/
  def calculator1(input: Int) = { println(s"calc with $input"); input }
  val equipment111 = new Equipment(calculator1)
  val equipment222 = new Equipment(calculator1)
  equipment111.simulate(4)
  equipment222.simulate(6)
  /*我们将计算逻辑创建为一个函数，在创建这两个实例的时候，将函数名作为参数传递给
  构造器。在 Equipment 类中，Scala 很自然地将函数名视为函数值的引用。
  在使用 Scala 编程时，我们不需要在良好的设计原则和代码质量之间做折中。Scala 反而
  提倡良好的实践，我们在编码时应该利用 Scala 的特性努力做到这一点。
  没有必要把函数值赋值给变量，直接传递函数名就可以了，这是复用函数值的一种方式，
  6.8 节中将会介绍其他方式。
  6.8 部分应用函数
  调用一个函数，实际上是在一些参数上应用这个函数。如果传递了所有期望的参数，就
  是对这个函数的完整应用，就能得到这次应用或者调用的结果。然而，如果传递的参数比所
  要求的参数少，就会得到另外一个函数。这个函数被称为部分应用函数。部分应用函数使绑
  定部分参数并将剩下的参数留到以后填写变得很方便。下面是一个例子。
  FunctionValuesAndClosures/Log.scala
  import java.util.Date
  def log(date: Date, message: String): Unit = {
  //...
  println(s"$date ---- $message")
  }
  val date = new Date(1420095600000L)
  log(date, "message1")
  log(date, "message2")
  log(date, "message3")
  在这段代码中，log()方法接收两个参数，即 date 和 message。我们想多次调用这个
  方法，date 的值保持不变但 message 每次用不同的值。将 date 参数部分应用到 log()
  方法中，就可以去除每次调用都要传递同样的 date 参数这类语法噪声。
  在下面的代码样例中，我们首先把一个值绑定到了 date 参数上。我们使用_将第二个参
  数标记为未绑定。其结果是一个部分应用函数，然后我们将它存储到 logWithDateBound
  这个引用中。现在我们就可以只用未绑定的参数 message 调用这个新方法。
  FunctionValuesAndClosures/Log.scala
  val date = new Date(1420095600000L)
  val logWithDateBound = log(date, _: String)
  logWithDateBound("message1")
  logWithDateBound("message2")
  logWithDateBound("message3")
  我们引入 Scala REPL，以帮助我们更好地理解从 log()函数创建的部分应用函数：
  异步社区会员 雄鹰1(13027310973) 专享 尊重版权
  102·第 6 章 函数值和闭包
  scala> import java.util.Date
  import java.util.Date
  scala> def log(date: Date, message: String) = println(s"$date ----
  $message")
  log: (date: java.util.Date, message: String)Unit
  scala> val logWithDateBound = log(new Date, _ : String)
  logWithDateBound: String => Unit = <function1>
  scala> :quit
  从 REPL 显示的细节中我们可以知道，变量 logWithDateBound 是一个函数的引用，
  这个函数接收一个 String 作为参数并返回一个 Unit 作为结果。
  当创建一个部分应用函数的时候，Scala 在内部会创建一个带有特殊 apply()方法的新类。在
  调用部分应用函数的时候，实际上是在调用那个 apply()方法，apply()方法的更多细节可以参
  考 8.1 节。在 Actor 中接收消息进行模式匹配的时候，Scala 会大量应用偏函数①，详见第 13 章。
  接下来我们深入研究一下函数值的作用域。
  6.9 闭包
  在前面的例子中，在函数值或者代码块中使用的变量和值都是已经绑定的。你明确地知
  道它们所绑定的（实体），即本地变量或者参数。除此之外，你还可以创建带有未绑定变量的
  代码块。这样的话，你就必须在调用函数之前，为这些变量做绑定。但它们也可以绑定到或
  者捕获作用域和参数列表之外的变量。这也是这样的代码块被称之为闭包（closure）的原因。
  我们来看一下本章中见过的 totalResultOverRange()方法的一个变体。在本例中，
  方法 loopThrough()会遍历从 1 到一个给定的数 number 之间的元素。
  FunctionValuesAndClosures/Closure.scala
  def loopThrough(number: Int)(closure: Int => Unit): Unit = {
  for (i <- 1 to number) { closure(i) }
  }
  loopThrough()方法的第二个参数是一个代码块，对于从 1 到它的第一个参数间的每
  一个元素，它都会调用这个代码块。让我们来定义一个代码块，并传递给这个方法。
  FunctionValuesAndClosures/Closure.scala
  var result = 0
  val addIt = { value: Int => result += value }
  ① 偏函数和部分应用函数并不是一个概念，这里提及，是为了提醒读者二者的区别。 —译者注
  异步社区会员 雄鹰1(13027310973) 专享 尊重版权
  6.10 Execute Around Method 模式·103
  在上面的代码中，我们定义了一个代码块，并把它赋值给名为 addIt 的变量。在这个代
  码块中，变量 value 绑定到了参数上，但变量 result 在代码块或者参数列表中并没有定
  义，它实际上绑定到了代码块之外的变量 result。代码块中的变量延伸并绑定到了外部的
  变量。下面演示了如何在 loopThrough()方法的调用中使用这个代码块。
  FunctionValuesAndClosures/Closure.scala
  loopThrough(10) { elem => addIt(elem) }
  println(s"Total of values from 1 to 10 is $result")
  result = 0
  loopThrough(5) { addIt }
  println(s"Total of values from 1 to 5 is $result")
  当我们把闭包传递给 loopThrough()方法时，参数 value 绑定到了 loopThrough()传递
  过来的参数上，与此同时，result 绑定到了 loopThrough()的调用者所在的上下文中的变量。
  这种绑定并不会复制相应变量的当前值，实际上会绑定到变量本身。如果我们把 result 的
  值重置为 0，那么闭包也会受到这种改变的影响。并且，在闭包中给 result 赋值时，我们也能
  在主代码中看到相应的值。下面是另外一个例子，其中的闭包绑定到了另外一个变量 product。
  FunctionValuesAndClosures/Closure.scala
  var product = 1
  loopThrough(5) { product *= _ }
  println(s"Product of values from 1 to 5 is $product")
  在这个例子中，_指代 loopThrough()方法传递进来的参数，product 绑定到了
  loopThrough() 方 法 的 调 用 者 所 在 的 上 下 文 中 名 为 product 的 变 量 上 。 下 面 是 对
  loopThrough()进行 3 次调用的输出结果：
  Total of values from 1 to 10 is 55
  Total of values from 1 to 5 is 15
  Product of values from 1 to 5 is 120
  在本章中，我们已经取得很大进展了，学习了函数值以及如何使用它们。现在让我们运
  用一种设计模式在实战中使用函数值。
   */
  /*
  6.10 Execute Around Method 模式
  Java 程序员对同步代码块（synchronized block）比较熟悉。当我们进入一个同步代
  码块时，会在指定的对象上获得一个监视器（monitor），即锁（lock）。在我们离开这个代码
  块时，监视器会自动释放。即使代码块中抛出了一个没有被处理的异常，释放操作也还是会
  发生。这种确定性的行为不仅仅在这个特定的例子中，在别的很多场景中也非常有用。
  感谢函数值，你可以在 Scala 中非常简单地实现这种结构。我们来看一个例子。
  异步社区会员 雄鹰1(13027310973) 专享 尊重版权
  104·第 6 章 函数值和闭包
  我们有一个名为 Resource 的类，它需要自动启动某个事务，并在使用完对象之后立刻
  确定性地结束该事务。我们可以依赖构造器来正确地启动事务。具有挑战性的是结束部分。而
  这正好就是 Execute Around Method 模式（详见 Kent Beck 的 Smalltalk Best Practice Patterns
  [Bec96]）。我们想要在一个对象上的任意操作前后执行一对操作。
  在 Scala 中，我们可以用函数值实现这种模式。下面这段代码是 Resource 类和它的伴
  生对象。关于伴生对象的细节，参见 4.6.2 节。
  FunctionValuesAndClosures/Resource.scala
  class Resource private () {
  println("Starting transaction...")
  private def cleanUp(): Unit = { println("Ending transaction...") }
  def op1(): Unit = println("Operation 1")
  def op2(): Unit = println("Operation 2")
  def op3(): Unit = println("Operation 3")
  }
  object Resource {
  def use(codeBlock: Resource => Unit): Unit = {
  val resource = new Resource
  try {
  codeBlock(resource)
  } finally {
  resource.cleanUp()
  }
  }
  }
  我们把 Resource 类的构造器标记为 private。因此，我们不能在这个类和它的伴生
  对象之外创建它的实例。这种设计可以强制我们以某种方式使用对象，以保证自动的、确定
  性的行为。cleanUp()方法也声明为 private。其中的 println 语句只用来作为真实事
  务操作的占位符。事务在构造器被调用的时候开始，在 cleanUp()被隐式调用的时候结束。
  Resource 类中可用的实例方法有 op1()、op2()等。
  在伴生对象中，我们有一个名为 use()的方法，它接收函数值作为参数。在 use()方法
  中，我们创建了 Resource 的一个实例。在 try 和 finally 之间，我们把这个实例传递给
  了 给 定 的 函 数 值 。 在 finally 代 码 块 中 ， 我 们 调 用 了 Resource 的 私 有 实 例 方 法
  cleanUp()。相当简单，对吧？为了提供对一些必要操作的确定性调用，这样做就可以了。
  现在，我们来看一下如何使用 Resource 类。下面是一些示例代码。
  FunctionValuesAndClosures/Resource.scala
  Resource.use { resource =>
  resource.op1()
  异步社区会员 雄鹰1(13027310973) 专享 尊重版权
  6.10 Execute Around Method 模式·105
  resource.op2()
  resource.op3()
  resource.op1()
  }
  输出结果如下：
  Starting transaction...
  Operation 1
  Operation 2
  Operation 3
  Operation 1
  Ending transaction...
  我们调用了 Resource 伴生对象中的 use()方法，并传递了一个代码块作为参数。它
  会把 Resource 的一个实例传递给我们。在我们开始访问实例的同时，事务就被启动了。我
  们在 Resource 的实例上调用所需的方法，如 op1()和 op2()。调用结束之后，也就是我
  们离开代码块的时刻，use()方法将会自动地调用 Resource 中的 cleanUp()方法。
  前面提到的设计模式的一个变种是借贷模式（Loan pattern）。在你想确定性地处理非内
  存资源时，使用这种设计模式。（这种设计模式的核心思想是，）资源密集型对象可以被看作
  是借来的，（使用完毕后）我们应该用合理的方式归还。
  下面是使用这种模式的一个例子。
  FunctionValuesAndClosures/WriteToFile.scala
  import java.io._
  def writeToFile(fileName: String)(codeBlock: PrintWriter => Unit): Unit = {
  val writer = new PrintWriter(new File(fileName))
  try { codeBlock(writer) } finally { writer.close() }
  }
  现在我们可以使用 writeToFile()函数向一个文件写入一些内容。
  FunctionValuesAndClosures/WriteToFile.scala
  writeToFile("output/output.txt") { writer =>
  writer write "hello from Scala"
  }
  在运行这段代码后，文件 output.txt 中的内容如下：
  hello from Scala
  作为 writeToFile()方法的用户，我们不必再担心关闭文件了。在这段代码中，文件
  像是借给我们使用的一样。我们可以向给定的 PrintWriter 实例写入（内容），并且在从
  代码块返回的时候，文件会自动被该方法关闭
   */
}
