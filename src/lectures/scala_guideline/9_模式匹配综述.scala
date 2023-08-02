package scala_guideline


object 模式匹配_9 extends App {

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

   // List("Monday", "Sunday", "Saturday").foreach {
   //     activity
   // }
   List("Monday","Sunday","Saturday") foreach activity
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
    println(Symbol())
    /*
    process()方法使用了尚未定义的 Symbol 提取器。如果该提取器认为输入的股票代码
    有效，那么它将返回 true；否则，返回 false。如果返回 true，那么将会执行和 case
    语句相关联的表达式，在这个例子中，我们只打印了一条消息来说明匹配成功；否则，模式
    匹配将继续尝试下一个 case 语句。现在，让我们来看一下缺失的部分，即提取器。
     */
    object Symbol {
        def apply() = this
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
    /*
    补充：关于unapply
    An extractor object is an object with an unapply method.
    Whereas the apply method is like a constructor which takes arguments and creates an object,
    the unapply takes an object and tries to give back the arguments.
    This is most often used in pattern matching and partial functions.
    import scala.util.Random

    object CustomerID {

      def apply(name: String) = s"$name--${Random.nextLong()}"

      def unapply(customerID: String): Option[String] = {
        val stringArray: Array[String] = customerID.split("--")
        if (stringArray.tail.nonEmpty) Some(stringArray.head) else None
      }
    }

    val customer1ID = CustomerID("Sukyoung")  // Sukyoung--23098234908
    customer1ID match {
      case CustomerID(name) => println(name)  // prints Sukyoung
      case _ => println("Could not extract a CustomerID")
    }
    The apply method creates a CustomerID string from a name.
    The unapply does the inverse to get the name back.
    When we call CustomerID("Sukyoung"), this is shorthand syntax for calling CustomerID.apply("Sukyoung").
    When we call case CustomerID(name) => println(name),
    we’re calling the unapply method with CustomerID.unapply(customer1ID).

    Since a value definition can use a pattern to introduce a new variable,
    an extractor can be used to initialize the variable,
    where the unapply method supplies the value.

    val customer2ID = CustomerID("Nico")
    val CustomerID(name) = customer2ID
    println(name)  // prints Nico
     */
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
    下面是修改过后的 process()方法，用来处理这
    项额外的任务。
     */
    object AnotherStockService {
        def process(input: String): Unit = {
            input match {
                case Symbol() => println(s"Look up price for valid symbol $input")
                case ReceiveStockPrice(symbol, price) =>
                    println(s"Received price $$$price for symbol $symbol")
                case _ => println(s"Invalid input $input")
            }
        }
    }
    /*
    我们添加了一个新的 case 语句，使用还未编写的提取器 ReceiveStockPrice。这个提
    取器不同于我们之前编写的 Symbol 提取器—它只是简单地返回一个 Boolean 结果。而
    ReceiveStockPrice 需要解析输入，并返回两个值，即 symbol 和 price。在 case 语句中。
    它们作为参数指定给了 ReceiveStockPrice；然而，它们并不会传入参数。它们是从提取器
    中传出的参数。因此，我们并没有发送 symbol 和 price。相反，我们正在接收它们。
    让我们看一下 ReceiveStockPrice 提取器。正如你所期望的，它有一个 unapply()
    方法，该方法将会根据字符：对输入进行切分，并返回一个股票代码和价格的元组。然而，
    还需要注意的一点是，输入可能不满足“SYMBOL:PRICE”这样的格式。所以为了处理这种可
    能的情况，这个方法的返回类型应该是 Option[(String,Double)]，在运行时，我们将接
    收到 Some((String,Double))或者 None（参见 5.2.3 节）。下面是 ReceiveStockPrice
    提取器的代码。
     */
    object ReceiveStockPrice {
        def unapply(input: String): Option[(String, Double)] = {
            try {
                if (input contains ":") {
                    val splitQuote = input split ":"
                    Some((splitQuote(0), splitQuote(1).toDouble))
                } else {
                    None
                }
            } catch {
                case _: NumberFormatException => None
            }
        }
    }

    AnotherStockService process "GOOG"
    AnotherStockService process "GOOG:310.84"
    AnotherStockService process "GOOG:BUY"
    AnotherStockService process "ERR:12.21"
    /*
    面的代码可以很好地处理前 3 个请求。它接收了有效的输入，并拒绝了无效的。然而，
    对于最后的请求，其处理并不顺利。即使输入了有效的格式，还是应该因为无效的股票代码
    ERR 而被拒绝。我们有两种方式来处理这种情况。一种是在 ReceiveStockPrice 中检查
    该股票代码是否有效。但是，这是一件导致重复的工作。另一种是在一个 case 语句中应用
    多个模式匹配。让我们修改 process()方法来做到这一点。
    case ReceiveStockPrice(symbol @ Symbol(), price) =>
    println(s"Received price $$$price for symbol $symbol")
    我们首先应用了 ReceiveStockPrice 提取器，如果成功，它将返回一个结果对。在
    第一个结果（symbol）上，我们进一步应用了 Symbol 提取器来验证股票代码。我们可以
    使用一个模式变量，然后在其后面跟上@符号，在该股票代码从一个提取器到另外一个提取
    器的过程中对股票代码进行拦截，如上面的代码所示。
    现在，如果重新运行这个修改后的服务，我们将会得到如下的输出结果：
    Look up price for valid symbol GOOG
    Received price $310.84 for symbol GOOG
    Invalid input GOOG:BUY
    Invalid input ERR:12.21
    你看到了提取器是多么的强大。它们使你几乎可以匹配任意模式。在 unapply()方法
    中，你几乎可以控制模式匹配的整个过程，并返回你想要的任意多的组成部分。
    如果输入的格式很复杂，你将可能极大地受益于提取器的能力。然而，如果格式相对简单，
    例如使用正则表达式就能非常容易表达的东西，你可能就不会想要自定义一个提取器了，而是
    想事半功倍。
     */
    println("--------9.4.2 正则表达式----------")
    /*
    Scala 通过 scala.util.matching 包中的类对正则表达式提供了支持。对正则表达式
    的详细讨论参阅 Jeffrey E. F. Friedl 的 Mastering Regular Expressions [Fri97]①一书。创建正则表
    达式时，使用的是该包中的 Regex 类的实例。让我们创建一个正则表达式，并用其来检查给
    定的字符串是否包含 Scala 或者 scala 这个词。
     */
    val pattern = "(S|s)cala".r
    val str = "Scala is scalable and cool"
    println(pattern findFirstIn str)
  /*
  我们创建了一个字符串，并调用它上面的 r 方法。Scala 隐式地将 String 转换为了
  StringOps②，并调用该方法以获取 Regex 类的实例。当然，如果我们的正则表达式需要转
  义字符，那么我们最好使用原始字符串而不是普通字符串。编写和阅读"""\d2:\d2:\
  d4"""比"\\d2:\\d2:\\d4"容易多了。
  要找到正则表达式的第一个匹配项，只需要调用 findFirstln()方法即可。在上面的
  例子中，该调用将会在文本中找到 Scala 这个词。
  如果不是只查找第一个匹配项，而是希望查找所匹配的单词的所有匹配项，那么我们便
  可以使用 findAllIn()方法。
   */
  println((pattern findAllIn str).mkString(", "))
  /*
  这将返回所有匹配的单词的集合。在这个例子中将是(Scala, scala)。最后，我们使
  用了 mkString()方法将生成的列表元素串联在一起。
  如果我们想要替换匹配到的文本，那么可以使用 replaceFirstIn()方法来替换第一
  个匹配项（如下例所示），或者使用 replaceAllIn()方法来替换所有匹配项。
   */
  println("cool".r replaceFirstIn (str, "awesome"))
  println("-----------9.4.3 正则表达式作为提取器------------")
  /*
  Scala 的正则表达式买一赠一。当你创建了一个正则表达式时，将免费得到一个提取器。
  Scala 的正则表达式是提取器，所以可以马上将其应用到 case 表达式中。Scala 将放置在括
  号中的每个匹配项看作是一个模式变量。因此，例如，一方面，"(Sls)cala".r 的 unapply()
  方法将会保存返回 Option[String]，另一方面，"(Sls)(cala)".r 的 unapply()方
  法将会返回 Option[(String,String)]。让我们用一个例子来探索这个特性。下面是使
  用正则表达式匹配“GOOG:price”并提取价格的一种方式。
   */
  def process(input: String): Unit = {
    val GoogStock = """^GOOG:(\d*\.\d+)""".r
    input match {
      case GoogStock(price) => println(s"Price of GOOG is $$$price")
      case _ => println(s"not processing $input")
    }
  }

  process("GOOG:310.84")
  process("GOOG:310")
  process("IBM:84.01")
  /*
  我们创建了一个正则表达式，用来匹配以“GOOG:”开头，后面跟着一个正的十进制数
  的字符串。我们将其存储到一个名为 GoogStock 的 val 中。在幕后，Scala 将会为这个提
  取器创建了一个 unapply()方法。它将返回与括号内的模式相匹配的值—price。
  Price of GOOG is $310.84
  not processing GOOG:310
  not processing IBM:84.01
  我们刚刚创建的提取器并不是真正可复用的。它查找股票代码“GOOG”，但是，如果我
  们想要匹配其他的股票代码，它就不是很有用了。不用做太多工作，我们就可以使其变得可
  复用。
   */
  def Anotherprocess(input: String): Unit = {
    val MatchStock = """^(.+):(\d*\.\d+)""".r
    input match {
      case MatchStock("GOOG", price) => println(s"We got GOOG at $$$price")
      case MatchStock("IBM", price) => println(s"IBM's trading at $$$price")
      case MatchStock(symbol, price) => println(s"Price of $symbol is $$$price")
      case _ => println(s"not processing $input")
    }
  }

  Anotherprocess("GOOG:310.84")
  Anotherprocess("IBM:84.01")
  Anotherprocess("GE:15.96")
  /*
  在这个例子中，我们的正则表达式匹配以任何字符或者数字开头，后面跟着一个冒号，
  然后是正的十进制数结束的任何字符串。所生成的 unapply()方法将会把:符号的前面和后
  面部分作为两个单独的模式变量返回。我们可以匹配特定的股票，如“GOOG”和“IBM”，
  也可以简单地接收发送给我们的任意股票代码。这段代码的输出结果如下：
  We got GOOG at $310.84
  IBM's trading at $84.01
  Price of GE is $15.96
   */
  /*
  9.5 无处不在的下划线字符
  _（下划线）这个字符在 Scala 中似乎无处不在，我们已经在这本书中看过它很多次了。
  到目前为止，它可能是 Scala 中使用最广泛的符号。如果知道了它在不同场景下使用的意义，
  那么在下一次遇到时，你就不会那么惊讶了。下面列出了这个符号在不同场景下的用途清单。
  • 作为包引入的通配符。例如，在 Scala 中 import java.util._等同于 Java 中的
  import java.util.*。
  • 作为元组索引的前缀。对于给定的一个元组 val names = ("Tom", "Jerry")，
  可以使用 names._1 和 names._2 来分别索引这两个值。
  • 作为函数值的隐式参数。代码片段 list.map { _ * 2 }和 list.map { e => e
  * 2 }是等价的。同样，代码片段 list.reduce { _ + _ }和 list.reduce { (a,
  b) => a + b }也是等价的。
  • 用于用默认值初始化变量。例如，var min : Int = _将使用 0 初始化变量 min，
  而 var msg : String = _将使用 null 初始化变量 msg。
  • 用于在函数名中混合操作符。你应该还记得，在 Scala 中，操作符被定义为方法。例
  如，用于将元素前插到一个列表中的::()方法。Scala 不允许直接使用字母和数字字
  符的操作符。例如，foo：是不允许的，但是可以通过使用下划线来绕过这个限制，
  如 foo_:。
  • 在进行模式匹配时作为通配符。case _将会匹配任意给定的值，而 case _:Int
  将匹配任何整数。此外，case <people>{_*}</people>将会匹配名为 people
  的 XML 元素，其具有 0 个或者多个子元素。
  • 在处理异常时，在 catch 代码块中和 case 联用。
  • 作为分解操作的一部分。例如，max(arg: _*)在将数组或者列表参数传递给接受
  可变长度参数的函数前，将其分解为离散的值。
  • 用于部分应用一个函数。例如，在代码片段 val square = Math.pow(_: Int,
  2)中，我们部分应用了 pow()方法来创建了一个 square()函数。
  _符号的目的是为了使代码更加简洁和富有表现力。开发人员应该根据自己的判断来决定
  何时使用该符号。只在代码真的变得更加简洁的时候才使用它，也就是说，代码是透明的，
  而且易于理解和维护。当你觉得代码变得生硬、难以理解或者晦涩时，就避免使用它
   */
}
