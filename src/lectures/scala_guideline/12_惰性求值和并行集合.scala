object 惰性求值和并行集合_12 extends App{
  println("---------第 12 章 惰性求值和并行集合-----------")
  /*一个值前拼接到各自对应的集合或者流上。然而，Stream 上的#::函数是惰性的，它只会
  在需要的时候进行连接，并在最终结果被请求之前推迟执行。
  因此，不同于常规递归，程序将不会急切地“扑向”对该函数的调用上。相反，它将懒
  洋洋地推迟对函数的调用。你一定很好奇调用这个函数的结果。我们将通过调用该函数并打
  印该调用的结果来解解馋。*/
  //Stream(25, ?)
  /*上面的输出结果告诉我们：我们有一个初始值为 25 的流，后面跟着一个尚未计算的值。
  这看起来像是该流提出了一项挑战：“如果你想要知道下一个值，来，主动获取它。”如果你
  不在该流上进行任何的调用，那么它将不会进行任何实际的工作，也不会为元素占用任何的
  空间。①
  只有一种方法可以使流生成值并执行一些操作：你必须要从中强制得到一个结果。为此，
  你可以调用 force()方法，但是需要注意的是，不要在无限流上调用这个方法，否则，将会
  最终耗尽内存，哪怕你使用的是云服务器。或者，你也可以调用另一种方法，这种方法会强
  制返回非流或者非惰性的结果，如调用 toList()方法。重申一次，一定要确保只在有限流
  上调用这样的方法。
  这就引出了另一个问题，我们如何将无限流转换为有限流呢？take()方法可以帮助我
  们。这个方法的返回值也是一个流，但是不同于原始的流，该结果流的大小是有限的。让我
  们来看一下代码，并从我们已经创建的流中获取一些数据。*/
  //println(generate(25).take(10).force)
  //println(generate(25).take(10).toList)
  /*这段代码同时展示了 force()方法和 toList()方法的用例。第一个方法将会强制流生
  成值。第二个方法也会做相同的事情，此外，还将结果转换为了一个严格集合—一个列表。
  让我们来看一下输出结果。
  Stream(25, 26, 27, 28, 29, 30, 31, 32, 33, 34)
  List(25, 26, 27, 28, 29, 30, 31, 32, 33, 34)
  force()方法返回的结果依然是流，但是大小受 take()方法的限制。而 toList()方
  法可以帮助将其转换为我们熟悉的列表。
  take()方法有助于将流中的元素个数限定为有限个。你也可以强制流不停地产生元素—
  通过调用 force()方法，当然，也包括在某些条件不满足之前一直产生。让我们要求流在到
  达某个数字之前（如 40）一直产生值。
  ① 实际上，是指不会为所有的元素占有存储空间，即是按需计算的。—译者注
  异步社区会员 雄鹰1(13027310973) 专享 尊重版权
  12.3 终极惰性流·169
  Parallel/NumberGenerator.scala
  println(generate(25).takeWhile { _ < 40 }.force)
  我们使用 takeWhile()代替了 take()方法，其接受一个函数值作为参数。只要该函
  数值中的表达式一直返回 true，那么 force()方法就会持续地生成值。一旦该函数值返回
  了 false，那么值生成的过程也就终止了。我们可以从输出结果中看到这一点：
  Stream(25, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35, 36, 37, 38, 39)
  我们创造了一个无限序列，并限制了它的大小。该序列并不一定要保持连续。让我们看
  另外一个例子，它展示了如何创建一系列的质数。
  Parallel/Primes.scala
  def isDivisibleBy(number: Int, divisor: Int) = number % divisor == 0
  def isPrime(number: Int) =
  number > 1 && !(2 until number).exists { isDivisibleBy(number, _) }
  def primes(starting: Int): Stream[Int] = {
  println(s"computing for $starting")
  if (isPrime(starting))
  starting #:: primes(starting + 1)
  else
  primes(starting + 1)
  }
  前两个函数一目了然。有意思的部分在 primes()函数中。在这个函数中，我们首先打印了
  一条消息，用来展示调用的入参值。如果指定的数是质数，那么将返回该数，并请求惰性地获取
  接下来的所有质数。如果给定的数不是质数，那么将立即开始搜索紧随着该数的质数。
  这个例子和数值生成器并没有太多的不同，只是生成过程不是连续的。我们将使用这个
  示例来了解流的另外一个特性：它们记住（memoize）它们已经生成的值。这其实也没什么，
  只不过是缓存（caching）而已，但是在我们的（计算机）领域，我们就喜欢给熟知的概念取
  奇怪的名字，以便看起来很有“深度”。①当流按需产生了一个新值时，它将会在返回该值之
  前缓存它—我的意思是记住它。然后，如果再次请求相同的值，就没有必要进行重复计算
  了。我们将通过从 primes()函数创建的流上进行两次调用，来展示这个特性。
  Parallel/Primes.scala
  val primesFrom100 = primes(100)
  println(primesFrom100.take(3).toList)
  ① 在计算机科学中，memoization 是一种加速计算的优化技术：将耗时的函数调用结果缓存起来，然后在同样的输入再次
  产生时，直接返回已经缓存的调用结果。—译白维基百科
  异步社区会员 雄鹰1(13027310973) 专享 尊重版权
  170·第 12 章 惰性求值和并行集合
  println("Let's ask for more...")
  println(primesFrom100.take(4).toList)
  我们将调用 primes()方法返回的流保存在变量 primesFrom100 中。我们两次使用了
  这个变量：第一次用于得到前三个值，第二次用于得到前四个值。第一次调用 take()方法
  时，我们使用 3 作为参数值，创建了一个由原始的无限流支撑的有限流。对 toList()方法
  的调用触发了实际的计算，并将结果保存在一个列表中。第二次对 take()方法的调用也在
  原始的流（primesFrom100）上进行，但是这一次，我们需要 4 个值。它将会给我们 3 个
  已经生成的值，还将产生 1 个新值。这是因为，这个流之前产生的值已经全部被安全地记住
  了，并且可以安全地复用，所以正如我们在输出中所看到的那样—只产生了新值。
  computing for 100
  computing for 101
  computing for 102
  computing for 103
  computing for 104
  computing for 105
  computing for 106
  computing for 107
  List(101, 103, 107)
  Let's ask for more...
  computing for 108
  computing for 109
  List(101, 103, 107, 109)
  流是 Scala 标准库中最迷人的特性之一。使用它们可以非常方便地实现各种算法，其中
  我们可以将问题归一化为可以按需惰性生成并执行的序列。在 11.3 节中我们已经看过一个这
  样的例子。尾递归可以看作是一个无限序列的问题。一次递归的执行可能会产生另一次递归，
  它可以被惰性求值，也可以被终止并产生结果值。理解了无限序列之后，你很快就能知道流
  是否适合解决自己所遇到的问题。
  12.4 并行集合
  如果惰性是提高效率之道路，那么并行性则可以被认为是提高效率之航线。如果两个或者
  多个任务可以按任意顺序序列执行，而又不会对结果的正确性产生任何影响，那么这些任务就
  可以并行执行。Scala 为此提供了多种方式，其中最简单的方式是并行地处理集合中的元素。
  我们一直都在处理数据的集合。我们可能需要检查几个产品的价格，并根据订单状态更
  新库存，或者汇总最近的交易流水。当我们处理数据的集合时，我们通常都会使用内部的迭
  代器，如 map()、filter()和 foldLeft()（在第 8 章中用到过一些），来执行必要的操
  作，并产生预期的结果。
  如果对象或者元素的数量很大，并且/或者处理它们的时间很长，那么产生结果的总体响
  应时间可能会非常长。并行地在多个线程上运行这些任务，并利用多核 CPU，则可以极大地
  异步社区会员 雄鹰1(13027310973) 专享 尊重版权
  12.4 并行集合·171
  提高速度。 ① 但是，使用低级别的线程构造和锁将导致额外的复杂性，并导致并发相关的错
  误，让程序员的生活一团糟。幸运的是，在 Scala 中你不必受这个罪，因为在数据的集合上
  进行并行操作非常简单。
  下面，我们会实现一个示例程序，顺序地处理数据的集合，然后进行并行化处理，从而
  提高处理速度。
  12.4.1 从顺序集合入手
  让我们看一个例子，首先按照顺序处理的方式实现它，然后再对其进行重构，使其运行得
  更快。我们将使用一个示例来收集并显示天气数据—环球观光旅行家们密切地关注着他们所
  要前往的城市的天气。让我们来创建一个将会报告所选择城市的温度以及天气状况的小程序。
  我们将从一组城市的名称开始，获取它们当前天气的状况，并按照城市名的顺序给出详细报
  告。对天气服务 API 的 Web 请求可以为我们提供不同格式的数据。我们在这里将会使用 XML
  格式，因为在 Scala 中可以很容易地对其进行解析。我们还将显示创建这份报告所耗费的时间。
  因为我们需要一个函数来发起 Web 服务请求，并获取给定城市的天气数据，所以我们先
  来实现这一功能。
  Parallel/Weather.scala
  import scala.io.Source
  import scala.xml._
  def getWeatherData(city: String) = {
  val response = Source.fromURL(
  s"https://raw.githubusercontent.com/ReactivePlatform/" +
  s"Pragmatic-Scala-StaticResources/master/src/main/resources/" +
  s"weathers/$city.xml")
  val xmlResponse = XML.loadString(response.mkString)
  val cityName = (xmlResponse \\ "city" \ "@name").text
  val temperature = (xmlResponse \\ "temperature" \ "@value").text
  val condition = (xmlResponse \\ "weather" \ "@value").text
  (cityName, temperature, condition)
  }
  getWeatherData()方法接受一个城市名作为其参数。在这个方法中，我们首先向相
  应的 URL 发送一个请求，以获取天气服务 API 的 Web 服务。因为我们选择使用的是 XML
  格式，所以来自该服务的响应也将采用该格式。然后，我们使用 XML 类的 loadString()
  方法来解析该 XML 响应（我们将会在第 15 章中对这个类进行仔细研究）。最后，我们使用
  XPath 查询来从 XML 响应中提取我们想要的数据。这个方法的返回值是一个由 3 个字符串组
  成的元组，其中包括城市名称、当前温度以及天气状况，按此顺序排列。
  ① 这里指缩短响应时间。—译者注
  异步社区会员 雄鹰1(13027310973) 专享 尊重版权
  172·第 12 章 惰性求值和并行集合
  接下来，我们将创建一个辅助函数来打印该天气数据。
  Parallel/Weather.scala
  def printWeatherData(weatherData: (String, String, String)): Unit = {
  val (cityName, temperature, condition) = weatherData
  println(f"$cityName%-15s $temperature%-6s $condition")
  }
  在 printWeatherData()方法中，我们接受一个带有天气详细信息的元组，并使用 f
  字符串插值器对数据进行格式化，以便打印到控制台上。我们仅剩一步之遥：一组样本数据，
  以及一种测量耗时的方法。现在我们就创建这个函数。
  Parallel/Weather.scala
  def timeSample(getData: List[String] => List[(String, String, String)]): Unit = {
  val cities = List("Houston,us", "Chicago,us", "Boston,us", "Minneapolis,us",
  "Oslo,norway", "Tromso,norway", "Sydney,australia", "Berlin,germany",
  "London,uk", "Krakow,poland", "Rome,italy", "Stockholm,sweden",
  "Bangalore,india", "Brussels,belgium", "Reykjavik,iceland")
  val start = System.nanoTime
  getData(cities) sortBy { _._1 } foreach printWeatherData
  val end = System.nanoTime
  println(s"Time taken: ${(end - start) / 1.0e9} sec")
  }
  timeSample()方法接受一个函数值作为其参数。其思路是让 timeSample()方法的
  调用者来指定一个函数，该函数将会接受一个城市的列表，并返回一个带有天气数据的元组
  列表。在 timeSample()函数中，我们创建了一个世界各地的城市列表。然后，我们测量使
  用（所传入的）函数值参数来获取天气数据所需要花费的时间，并按照城市名称对结果进行
  排序，并最终打印出每个城市的结果。
  我们已经完全准备好使用我们创建的函数了。让我们对该 Web 服务进行顺序调用，以获
  取天气数据。
  Parallel/Weather.scala
  timeSample { cities => cities map getWeatherData }
  我们调用了 timeSample()函数，并传递一个函数值作为其参数。该函数值接受了在
  timeSample()函数中传入的城市列表。随后，它为列表中的每个城市都调用 getWeatherData()
  函数，一次调用一个城市。map()操作返回的结果是一组由 getWeatherData()调用返回
  的数据—天气数据的元组列表。
  让我们来运行这段代码，看一看对应的输出结果以及执行时间。
  异步社区会员 雄鹰1(13027310973) 专享 尊重版权
  12.4 并行集合·173
  Parallel/output/Weather.output
  Bangalore 88.57 few clouds
  Berlin 48.2 mist
  Boston 45.93 mist
  Brussels 49.21 clear sky
  Chicago 31.59 overcast clouds
  Houston 61 clear sky
  Krakow 55.4 broken clouds
  London 50 broken clouds
  Minneapolis 29.55 clear sky
  Oslo 42.8 fog
  Reykjavik 48.06 overcast clouds
  Rome 54.88 few clouds
  Stockholm 38.97 clear sky
  Sydney 68 broken clouds
  Tromso 35.6 clear sky
  Time taken: 2.369008456 sec
  城市按照城市名的顺序列出，与其一起列出的还有温度信息以及请求时的天气状况。代
  码运行了大约 2 s，你观察到的执行时间将取决于你的网络速度以及拥塞情况。接下来，我们
  将看到如何通过最小的改变从而更快地获取到结果。
  12.4.2 使用并行集合加速
  前面的例子有两个部分：慢的部分—对于每个城市，我们都通过网络获取并收集天气
  信息，快的部分—我们对数据进行排序，并显示它们。非常简单，因为慢的部分被封装到
  了作为参数传递给 timeSample()函数的函数值中。因此，我们只需要更换那部分代码来提
  高速度即可，而其余的部分则可以保持不变。
  在 这 个 例 子 中 ， 在 城 市 列 表 上 调 用 的 map()方 法 ， 将 会 为 每 个 城 市 调 用 传 入 的
  getWeatherData()函数，一次一个。这是顺序集合上的方法行为：它们为它们的集合中
  的每个元素顺序地执行它们的操作。但是，我们传递给 map()函数的操作可以并行地执行，
  因为获取一个城市的数据与获取另外一个城市的数据相互独立。值得庆幸的是，让 map()
  方法为每个城市并行地执行操作并不需要太多工作。我们只需要将该集合转换为并行版本
  就可以了。
  对于许多顺序集合，Scala 都拥有其并行版本。①例如，ParArray 是 Array 对应的并
  行版本，同样的，ParHashMap、ParHashSet 和 ParVector 分别对应于 HashMap、
  HashSet 和 Vector。我们可以使用 par()和 seq()方法来在顺序集合及其并行版本之间
  进行相互转换。
  ① 从 Scala 2.13.x 开始，并行集合将以单独的模块提供。—译者注
  异步社区会员 雄鹰1(13027310973) 专享 尊重版权
  174·第 12 章 惰性求值和并行集合
  让我们使用 par()方法将城市列表转换为其并行版本。现在，map()方法将并行地执行
  它的操作。在完成之后，我们将使用 toList()方法来将所生成的并行集合转换为顺序集合，
  即（作为参数传入的）函数值的结果类型。下面我们使用并行集合而不是顺序集合，来重写
  对 timeSample()方法的调用。
  Parallel/Weather.scala
  timeSample { cities => (cities.par map getWeatherData).toList }
  具体的改动非常小，完全包含在了函数值之内。其余代码的结构在顺序版本和其并行版
  本之间完全一样。事实上，我们在顺序版本和并行版本两个版本之间复用了其余的代码。让
  我们运行这个修改后的版本，看一下输出结果。
  Parallel/output/Weather.output
  Bangalore 88.57 few clouds
  Berlin 48.2 mist
  Boston 45.93 mist
  Brussels 49.21 clear sky
  Chicago 31.59 overcast clouds
  Houston 61 clear sky
  Krakow 55.4 broken clouds
  London 50 broken clouds
  Minneapolis 29.55 clear sky
  Oslo 42.8 fog
  Reykjavik 48.06 overcast clouds
  Rome 54.88 few clouds
  Stockholm 38.97 clear sky
  Sydney 68 broken clouds
  Tromso 35.6 clear sky
  Time taken: 0.447646174 sec
  输出结果展示了完全相同的天气条件。然而，这段代码所花费的时间差异却非常大—少
  太多了。我们利用了多个线程来为不同的城市并行地执行 getWeatherData()函数。
  从顺序版本转换为并行版本，几乎不费吹灰之力。鉴于此，一个合乎逻辑的问题便是：
  我们为什么不一直使用并行集合呢？简而言之，这和上下文有关。
  你不会开车去厨房的冰箱里取一瓶牛奶 ① ，但是你可能会开车去商店，将牛奶和其他物
  品一起带回来。同样，你也不会想在小型集合上使用并行集合，进而来执行本来就已经很快
  的操作。创建和调度线程的开销不应该大于执行这些任务所需要的时间。对于慢型任务或者
  大型集合来说，并行集合可能有所裨益，但是对于小型集合上的快速任务来说，则不太适合。
  除了计算速度和集合大小之外，还有其他几个因素决定了我们是否可以使用并行集合。
  ① 也就是，不要高射炮打蚊子。—译者注
  异步社区会员 雄鹰1(13027310973) 专享 尊重版权
  12.5 小结·175
  如果在集合上调用的操作会修改全局状态 ① ，那么整体的计算结果将是不可预知的—共享
  的可变性通常都是一个糟糕的主意。因此，如果所进行的操作具有副作用，那么就不要使用
  并行集合。此外，如果操作不满足结合律 ② ，也不要使用并行集合。因为，在并行集合中，
  操作的执行顺序是非确定性的。类似于加法这样的操作不关心以什么顺序累加出总数，但像
  减法这样的操作，就非常依赖执行的顺序了，并不适合并行化。
  在 Scala 中，应用并行集合轻松愉快，但我们必须要作出关键决定：并行化是否是正确
  的选择？能否确保我们在提高速度的同时，能够得到正确的结果？
  12.5 小结
  在本章中，我们学习了 Scala 中的一些技术和特性，这些特性可以使代码执行得更快、
  更高效。惰性变量将变量的绑定推迟到了变量首次被需要的最后时刻。我们还学习了如何从
  严格集合转换到其惰性视图，如何使用无限流、有限流，以及如何使用并行集合，同时得到
  了一些何时使用它们以及何时避免使用它们的指导。
  我们只触及了高效编程的表面，在下一章中，我们将会讨论并发编程。
   */

}
