package scala_guideline


object 模式匹配_9_1 extends App {

    /*
    9.1 模式匹配综述 
Scala 的模式匹配非常灵活，可以匹配字面量和常量，以及使用通配符匹配任意的值、元
组和列表，甚至还可以根据类型以及判定守卫来进行匹配。接下来我们就来逐个探索一下这
些应用方式。
9.1.1 匹配字面量和常量 
在 Actor 之间传递的消息通常都是 String 字面量、数值或者元组。①如果你的消息是字
面量，对其进行模式匹配几乎没有什么工作量，只需要输入想要匹配的字面量就可以了。假
如我们需要确定一周中不同天的活动信息，并假设我们得到的关于某天是周几的输入是一个
String，而我们需要响应当天的活动信息。下面是一个示例，说明了我们如何对这些日期
进行模式匹配。
    */
    def activity(day: String): Unit = {
        day match {
            case "Sunday" => println("Eat, sleep, repeat... ")
            case "Saturday" => println("Hang out with friends... ")
            case "Monday" => println("...code for fun...")
            case "Friday" => println("...read a good book...")
        }
    }

    List("Monday", "Sunday", "Saturday").foreach {
        activity
    }
    println("------------------9.1.2 匹配通配符-----------------------")

    object DayOfWeek extends Enumeration {
        val SUNDAY: DayOfWeek.Value = Value("Sunday")
        val MONDAY: DayOfWeek.Value = Value("Monday")
        val TUESDAY: DayOfWeek.Value = Value("Tuesday")
        val WEDNESDAY: DayOfWeek.Value = Value("Wednesday")
        val THURSDAY: DayOfWeek.Value = Value("Thursday")
        val FRIDAY: DayOfWeek.Value = Value("Friday")
        val SATURDAY: DayOfWeek.Value = Value("Saturday")
    }

    def activity(day: DayOfWeek.Value): Unit = {
        day match {
            case DayOfWeek.SUNDAY => println("Eat, sleep, repeat...")
            case DayOfWeek.SATURDAY => println("Hang out with friends")
            case _ => println("...code for fun...")
        }
    }

    activity(DayOfWeek.SATURDAY)
    activity(DayOfWeek.MONDAY)

    /*
    我们为一周中的每一天都定义了一个枚举值。在我们的 activity()方法中，我们匹配
    了 SUNDAY 和 SATURDAY，并使用下划线（_）表示的通配符处理其他工作日。
     */
    println("---------------------9.1.3 匹配元组和列表---------------------")

    /*
    匹配字面量和枚举值很简单。但是消息通常都不是单个字面量—它们通常是一组以
    元组或者列表形式表现的值。元组和列表也可以使用 case 表达式来匹配。假设我们正在
    编写一个需要接收并处理地理坐标的服务。可以使用元组来表示坐标，并进行模式匹配，
    如下所示。
     */
    def processCoordinates(input: Any): Unit = {
        input match {
            case (lat, long) => printf("Processing (%d, %d)...", lat, long)
            case "done" => println("done")
            case _ => println("invalid input")
        }
    }

    processCoordinates((39, -104))
    processCoordinates("done")

    /*
    如果我们发送的参数不是具有两个元素的元组，也不能匹配"done"，那么通配符将会
    处理它。用于打印坐标的 printf()语句有一个隐藏的假设，即（所匹配到的）元组中的值
    都是整数。如果这些值不是整数，那么我们的代码将会在运行时失败。我们可以通过提供用
    于模式匹配的类型信息来避免这种情况，如我们在下一节中将看到的那样。
    你可以用匹配元组的方式来对 List 进行模式匹配，只需要提供你关心的元素即可，而
    对于剩下的元素可以使用数组展开（array explosion）标记（_*）
     */
    def processItems(items: List[String]): Unit = {
        items match {
            case List("apple", "ibm") => println("Apples and IBMs")
            case List("red", "blue", "white") => println("Stars and Stripes...")
            case List("red", "blue", _*) => println("colors red, blue,... ")
            case List("apple", "orange", otherFruits@_*) =>
                println("apples, oranges, and " + otherFruits)
        }
    }

    processItems(List("apple", "ibm"))
    processItems(List("red", "blue", "green"))
    processItems(List("red", "blue", "white"))
    processItems(List("apple", "orange", "grapes", "dates"))
    /*
    在前两行的 case 语句中，我们预期 List 中分别具有 2 个和 3 个指定的元素。在剩下
    的两行 case 语句中，我们预期 2 个或者更多的元素，但是开头的 2 个元素必须是我们所指
    定的。如果我们需要引用 List 中剩下的元素，可以在特殊的@符号①之前放置一个变量名（如
    otherFruits），就像在最后一行 case 语句中一样。
     */

    println("------------------9.1.4 匹配类型和守卫------------------------")
    /*
    有时候你可能会想要根据值的类型进行匹配。例如，你可能想要处理序列，如 Int 序列，
    而又不同于处理 Double 序列。Scala 使你可以要求 case 语句匹配对应的类型，如下面的例
    子所示。
     */

    def process(input: Any): Unit = {
        input match {
            case (_: Int, _: Int) => print("Processing (int, int)... ")
            case (_: Double, _: Double) => print("Processing (double, double)... ")
            case msg: Int if msg > 1000000 => println("Processing int > 1000000")
            case _: Int => print("Processing int... ")
            case _: String => println("Processing string... ")
            case _ => printf(s"Can't handle $input... ")
        }
    }

    process((34.2, -159.3))
    process(0)
    process(1000001)
    process(2.2)
    /*
    你将看到如何在 case 语句中指定单个值以及元组中元素的类型，此外，你还可以使用
    守卫来进一步约束模式匹配。除了匹配模式之外，有时还必须满足由 if 子句提供的守卫约
    束，才能对=>后面的表达式进行求值。这段代码的输出如下：
    Processing (double, double)... Processing int... Processing int > 1000000
    Can't handle 2.2...
    在编写多个 case 表达式时，它们的顺序很重要。Scala 将会自上而下地对 case 表达式
    进行求值。 ①如果我们交换代码中的第 5 行和第 6 行，就会导致一个警告，以及一个不一样
    的结果，因为带有守卫的 case 语句永远也不会被执行。
     */
    println("---------------------9.2 case 表达式中的模式变量和常量------------------------")

    /*
    你已经看到了如何为自己正在匹配的值定义占位符，如在匹配元组时的 lat 和 long。
    这些就是模式变量。然而，在定义它们的时候必须要小心。按照约定，Scala 期望模式变量名
    都以小写字母开头，而常量名则是大写字母。
    如果你使用大写字母的名称，Scala 将会在作用域范围内查找常量。但是，如果使用的是
    非大写字母的名称，它将只假设其是一个模式变量—在作用域范围内任何具有相同非大写字
    母的名称都将会被忽略。在下面的代码中，我们定义了一个和字段具有相同名称的模式变量，
    但是这段代码将不会给出我们想要的结果—模式变量隐藏了（Sample 类的）max 字段。
     */
    class Sample {
        val max = 100

        def process(input: Int): Unit = {
            input match {
                case max => println(s"You matched max $max")
            }
        }
    }

    val sample = new Sample
    try {
        sample.process(0)
    } catch {
        case ex: Throwable => println(ex)
    }
    sample.process(100)

    /*
    下面的输出结果表明：Scala 将变量 max 推断为模式变量，而不是 Sample 类中 max 字
    段的不可变变量值
     */
    /*
    可以使用显式的作用域指定（如果 ObjectName 是一个单例对象或者伴生对象，那么使
    用 ObjectName.filedName，如果 obj 是一个引用，则使用 obj.fieldName），以便在
    case 表达式中访问被隐藏的字段，如下所示：
    case this.max => println(s"You matched max $max")
    在这个版本中，Scala 知道我们指向的是一个字段：
    scala.MatchError: 0 (of class java.lang.Integer)
    You matched max 100
    除了使用点号表示法来解析作用域范围，也可以通过在变量名的两边加上反单引号（`）
    来给 Scala 提示：
    case `max` => println(s"You matched max $max")
    同样，在这个修改后的版本中，Scala 正确地解析了位于当前作用域范围内的不可变变量：
    scala.MatchError: 0 (of class java.lang.Integer)
    You matched max 100
    可以使用这两种方法的任何一种来指示 Scala 将非大写字母的名称看作是作用域范围内
    的预定义值，而不是模式变量。然而，最好避免这样做—请将大写字母的名称用于真正的
    常量，如下例所示
     */
    class AnotherSample {
        val MAX = 100

        def process(input: Int): Unit = {
            input match {
                case MAX => println("You matched max")
            }
        }
    }

    val anothersample = new AnotherSample
    try {
        anothersample.process(0)
    } catch {
        case ex: Throwable => println(ex)
    }
    anothersample.process(100)
    println("----------------------9.3 使用 case 类进行模式匹配---------------------")

    /*
    case 类是特殊的类，可以使用 case 表达式来进行模式匹配。case 类很简洁，并且容
    易创建，它将其构造参数都公开为值。可以使用 case 类来创建轻量级值对象，或者类名和
    属性名都富有意义的数据持有者。
    假设我们希望接收和处理股票交易信息。用于出售和购买股票的消息可能会伴随着股票
    的名称以及数量等信息。将这些信息存储在对象中很方便，但是如何对它们进行模式匹配呢？
    这就是 case 类的使用场景了。case 类是模式匹配容易识别和匹配的类。下面是几个 case
    类的示例。
     */
    trait Trade

    case class Sell(stockSymbol: String, quantity: Int) extends Trade

    case class Buy(stockSymbol: String, quantity: Int) extends Trade

    case class Hedge(stockSymbol: String, quantity: Int) extends Trade

    /*
    我们将 Trade 定义为特质，因为我们不期望它有任何直接的实例，这很像我们在 Java
    中定义接口。case 类 Sell、Buy 和 Hedge 都扩展自这个特质。这 3 个类接受股票的代码
    及其数量作为构造器参数。
    现在，我们已经可以在 case 语句中使用这些类了。
     */
    object TradeProcessor {
        def processTransaction(request: Trade): Unit = {
            request match {
                case Sell(stock, 1000) => println(s"Selling 1000-units of $stock")
                case Sell(stock, quantity) =>
                    println(s"Selling $quantity units of $stock")
                case Buy(stock, quantity) if quantity > 2000 =>
                    println(s"Buying $quantity (large) units of $stock")
                case Buy(stock, quantity) =>
                    println(s"Buying $quantity units of $stock")
            }
        }
    }
    /*
    我们把 request 和 Sell 以及 Buy 进行模式匹配。我们收到的股票代码和数量分别与
    模式变量 stock 和 quantity 对应。我们可以指定常量值，如将 quantity 指定为 1000，
    也可以进一步使用守卫进行模式匹配，例如检查 if quantity > 2000。下面是一个使用
    TradeProcessor 单例对象的例子
     */
    TradeProcessor.processTransaction(Sell("GOOG", 500))
    TradeProcessor.processTransaction(Buy("GOOG", 700))
    TradeProcessor.processTransaction(Sell("GOOG", 1000))
    TradeProcessor.processTransaction(Buy("GOOG", 3000))

    /*
    在上面的例子中，所有具体的 case 类都接受参数。如果你有一个不带参数的 case 类，
    那么请在类名之后加上一个空括号，以表明它接受的是空的参数列表，否则 Scala 编译器会
    产生一个警告。
    在处理不带参数的 case 类的时候，还有其他复杂的问题需要注意—当把它们作为消
    息传递时要格外小心。在下面的例子中，有不接受任何参数的 case 类。
     */
    case class Apple()

    case class Orange()

    case class Book()

    object ThingsAcceptor {
        def acceptStuff(thing: Any): Unit = {
            thing match {
                case Apple() => println("Thanks for the Apple")
                case Orange() => println("Thanks for the Orange")
                case Book() => println("Thanks for the Book")
                case _ => println(s"Excuse me, why did you send me $thing")
            }
        }
    }

    ThingsAcceptor.acceptStuff(Apple())
    ThingsAcceptor.acceptStuff(Book())
    ThingsAcceptor.acceptStuff(Apple)
    /*
    当我们忘记了括号时，我们发送的将不是该 case 类的实例，而是其伴生对象。伴生对
    象混合了 scala.Function0 特质，意味着它可以被视为一个函数。因此，我们最终将会发
    送一个函数而不是 case 类的实例
     */

    println("----------------9.4 提取器和正则表达式---------------")
    /*
    Scala 强大的模式匹配并不止步于内置的模式匹配设施。我们可以使用提取器创建自定义
    的匹配模式，同时，Scala 也为我们提供了一些不同的选择。
    9.4.1 使用提取器进行模式匹配
    通过使用 Scala 的提取器来匹配任意模式，可以将模式匹配提升到下一个等级。顾名思
    义，提取器将从输入中提取匹配的部分。假设我们正在编写一个服务，它负责处理与股票相
    关的输入信息。那么我们当务之急便是接收一个股票代码，并返回该股票的价格。下面是一
    个我们可以预期的调用示例：
    StockService process "GOOG"
    StockService process "IBM"
    StockService process "ERR"
    process()方法需要验证给定的股票代码，如果有效，则返回它的价格。下面是对应的
    代码：
     */
    object StockService {
        def process(input: String): Unit = {
            input match {
                case Symbol() => println(s"Look up price for valid symbol $input")
                case _ => println(s"Invalid input $input")
            }
        }
    }
    /*
    process()方法使用了尚未定义的 Symbol 提取器。如果该提取器认为输入的股票代码
    有效，那么它将返回 true；否则，返回 false。如果返回 true，那么将会执行和 case
    语句相关联的表达式，在这个例子中，我们只打印了一条消息来说明匹配成功；否则，模式
    匹配将继续尝试下一个 case 语句。现在，让我们来看一下缺失的部分，即提取器。
     */
    object Symbol {
        def unapply(symbol: String): Boolean = {
            // 你查找了数据库，但是只识别了 GOOG 和 IBM
            symbol == "GOOG" || symbol == "IBM"
        }
    }
    /*
    该提取器具有一个名为 unapply()的方法，它接受我们想要匹配的值。当 case Symbol()
    =>被执行的时候，match 表达式将自动将 input 作为参数发送给 unapply()方法。当我
    们执行前面的 3 个代码片段时（请记住将对服务的示例调用放置到你代码文件的末尾，以保
    证能被调用到），我们将得到如下输出：*/
    StockService process "GOOG"
    StockService process "IBM"
    StockService process "ERR"
    /*
    Look up price for valid symbol GOOG
    Look up price for valid symbol IBM
    Invalid input ERR
    由于方法名奇怪，unapply()方法可能会让你感到吃惊。对于提取器，你可能会预期类
    似于 evaluate()这样的方法。提取器有这样的方法名的原因是：提取器也可能会有一个可
    选的 apply()方法。这两个方法，即 apply()和 unapply()，执行的是相反的操作。
    unapply()方法将对象分解成匹配模式的部分，而 apply()方法则倾向于将它们再次合并
    到一起。
    让我们进一步改进一下这个示例。现在，我们将能够请求股票报价，作为我们的服务的
    下一项任务。假设为此所到达的消息的格式是“SYMBOL:PRICE”。我们需要使用模式匹配
    来匹配这种格式，并采取进一步的动作
     */

}