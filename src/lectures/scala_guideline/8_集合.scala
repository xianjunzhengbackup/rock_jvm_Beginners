object 集合_8 extends App{
  /*
   * Scala 标准库包含了一组丰富的集合类，以及用于组合、遍历和提取元素的强大操作。在
   * 创建 Scala 应用程序时，会经常用到这些集合。如果想要在使用 Scala 时更加具有生产力，彻
   * 底地学习这些集合是很有必要的。
   * 在本章中，我们将学习如何创建常见的 Scala 集合的实例，以及如何遍历它们。我们仍
   * 然可以使用 JDK 中的集合（如 ArrayList、HashSet 以及普通数组），但是在本章中，我
   * 们将重点讨论特定于 Scala 的集合，以及如何使用它们。
   */
  println("--------8.1 常见的 Scala 集合--------")
  /*Scala 有 3 种主要的集合类型：
   * • List—有序的对象集合；
   * • Set—无序的集合；
   * • Map—键值对字典。
   * Scala 推崇不可变集合，尽管也可以使用可变版本。如果想要修改集合，而且集合上所有
   * 的操作都在单线程中进行，那么就可以选择可变集合。但是，如果打算跨线程、跨 Actor 地
   * 使用集合，那么不可变集合将会是更好的选择。不可变集合是线程安全的，不受副作用影响，
   * 并且有助于程序的正确性。可以通过选择下列两个包之一来选择所使用的版本：
   * scala.collection.mutable 或者 scala.collection.immutable。
   * 如果不指定所使用的包名，那么，在默认情况下，Scala 会使用不可变集合。①下面是一
   * 个使用 Set 的例子—当然，是不可变的版本。*/
 val colors1 = Set("Blue", "Green", "Red") 
 println(s"colors1: $colors1") 
 val colors2 = colors1 + "Black" 
 println(s"colors2: $colors2") 
 println(s"colors1: $colors1")
 println(colors1 + "Red")
   /*
    我们从一个具有 3 种颜色的 Set 开始。当添加了"Black"时，我们并没有修改原始的集
   * 合。相反，我们得到了一个具有 4 个元素的新集合，正如我们在下面所看到的：
   * colors1: Set(Blue, Green, Red) 
   * colors2: Set(Blue, Green, Red, Black) 
   * colors1: Set(Blue, Green, Red) 
   * 在默认情况下，得到的是不可变集合。因为（被默认包含的）Predef 对象为 Set 和 Map
   * 提供了别名，指向的是不可变的实现。Set 和 Map 是 scala.collection 包中的特质，在
   * scala.collection.mutable 包中有其可变版本的实现，而在 scala.collection. 
   * immutable 包中有其不可变版本的实现。
   * 在前面的示例中，我们没有使用 new 关键字来创建 Set 的实例。在内部，Scala 创建了
   * 内部类 Set3 的一个实例，正如我们在下面的 REPL 交互中所看到的：
   * scala> val colors = Set("Blue", "Green", "Red") 
   * colors: scala.collection.immutable.Set[String] = Set(Blue, Green, Red) 
   * scala> colors.getClass 
   * res0: Class[_ <: scala.collection.immutable.Set[String]] = class 
   * scala.collection.immutable.Set$Set3 
   * scala> :quit 
   * Set3 是一个表示具有 3 个元素的集合的实现的类。因为 Set 是不可变的，并且必须要
   * 在构造时提供值①，所以 Scala 针对元素较少的 Set 优化了具体实现，并为大于 4 个元素的值
   * 创建 HashSet 的实现。②
   * 根据所提供的参数，Scala 发现我们需要的是一个 Set[String]。同样地，如果是
   * Set(1,2,3)，那么我们将会得到一个 Set[Int]。因为特殊的 apply()方法（也被称为工
   * 厂方法），所以才得以创建一个对象而又不用使用 new 关键字。类似于 X(...)这样的语句，
   * 其中 X 是一个类的名称或者一个实例的引用，将会被看作是 X.apply(...)。如果对应的
方法存在，Scala 会自动调用这个类的伴生对象上的 apply()方法。这种隐式调用 apply()
方法的能力也可以在 Map 和 List 上找到。*/
println("-----------8.2 使用 Set---------")
/*假设我们正在编写一个 RSS 流阅读器，我们希望经常更新 feed 流，但是并不关心更新的
顺序。那么我们可以将这些 feed 的 URL 存储到一个 Set 中。假设我们有下面这些 feed，它
们分别存储在两个 Set 中：*/
val feeds1 = Set("blog.toolshed.com", "pragdave.me", "blog.agiledeveloper.com") 
val feeds2 = Set("blog.toolshed.com", "martinfowler.com/bliki") 
/*如果我们只是想更新从 feeds1 中选择的 feed，如所有包含有“blog”这个词的，那么
我们可以使用 filter()方法来获取这些 feed。*/
val blogFeeds = feeds1 filter (_ contains "blog") 
println(s"blog feeds: ${blogFeeds.mkString(", ")}") 
/*我们将会得到如下输出：
blog feeds: blog.toolshed.com, blog.agiledeveloper.com 
mkString()方法为 Set 中的每一个元素都创建了一个对应的字符串表示形式，并将结
果与参数字符串（在这个例子中的逗号）拼接起来。
如果要合并两个 Set 的 feed 来创建一个新的 Set，那么我们可以使用++()方法：*/
val mergedFeeds = feeds1 ++ feeds2 
println(s"# of merged feeds: ${mergedFeeds.size}") 
/*正如我们在输出结果中所看到的，在合并过后的 Set 中，两个 Set 中相同的 feed 将只
会被存储一次，因为同一个元素将最多只会被 Set 存储一次：
# of merged feeds: 4 
如果要确定我们和某个朋友所订阅的多个 feed 中相同的部分，那么我们可以导入朋友的
feed，并执行求交集操作（即调用&()方法）：*/
val commonFeeds = feeds1 & feeds2 
println(s"common feeds: ${commonFeeds.mkString(", ")}") 
/*下面是在前面提到的两个 feed Set 上执行求交集操作之后的结果：
common feeds: blog.toolshed.com 
如果要在每个 feed 的前面加上一个“http://”字符串前缀，我们可以使用 map()方法。
这将对每个元素应用给定的函数值、将结果收集到一个 Set 中，并最终返回这个 Set：*/
val urls = feeds1 map ("http://" + _) 
println(s"One url: ${urls.head}")
println(s"One url tail: ${urls.tail}")
/*
我们将会看到下面的结果：
One url: http://blog.toolshed.com 
最后，当我们准备好循环遍历这些 feed 并一个一个地刷新它们的时候，我们可以使用内
置的迭代器 foreach()方法，如下所示：*/
println("Refresh Feeds:") 
feeds1 foreach { feed => println(s" Refreshing $feed...") } 
feeds1 foreach println
/*下面是输出结果：
Refresh Feeds: 
 Refreshing blog.toolshed.com... 
  Refreshing pragdave.me... 
   Refreshing blog.agiledeveloper.com... 
   这就是元素的无序集合。接下来，让我们探讨关联映射—Map。*/
println("s--------8.3 关联映射----------") 
/*   假设我们要将 feed 的作者的名字附加到 feed 上，我们可以将其以键值对的形式存储在
   Map 中。*/
val feeds = Map( 
  "Andy Hunt" -> "blog.toolshed.com", 
  "Dave Thomas" -> "pragdave.me", 
  "NFJS" -> "nofluffjuststuff.com/blog") 
/*   如果想要得到一个 feed 的 Map，其中 feed 的作者名开头都为“D”，那么我们可以使用
   filterKeys()方法。*/
val filterNameStartWithD = feeds filterKeys (_ startsWith "D") 
println(s"# of Filtered: ${filterNameStartWithD.size}") 
/*   下面是输出结果：
   # of Filtered: 1 
   另外，如果想要对这些值进行筛选，那么除对键进行操作之外，我们还可以使用 filter()
   方法。提供给 filter()方法的函数值接收一个（键，值）元组，我们可以像下面这样使
   用它：*/
val filterNameStartWithDAndPragprogInFeed = feeds filter { element => 
  val (key, value) = element 
  (key startsWith "D") && (value contains "pragdave") 
} 
print("# of feeds with auth name D* and pragdave in URL: ") 
println(filterNameStartWithDAndPragprogInFeed.size)
/*
 下面是输出结果：
 # of feeds with auth name D* and pragdave in URL: 1 
 如果需要获取一个人的 feed，只需要使用 get()方法。因为对于给定的键，其对应的值
 可能不存在，所以 get()方法的返回类型是 Option[T]（参见 5.2.3 节）。该方法的实际结
 果要么是一个 Some[T]要么是 None，其中 T 也是 Map 的值类型。*/
 println(s"Get Andy's Feed: ${feeds.get("Andy Hunt")}") 
 println(s"Get Bill's Feed: ${feeds.get("Bill Who")}") 
/*上述代码的输出结果如下：
 Get Andy's Feed: Some(blog.toolshed.com) 
 Get Bill's Feed: None 
 此外，我们可以使用 apply()方法来获取一个键的值。需要牢记的是，这是我们在类或
 者实例后加上圆括号时，Scala 调用的方法。但是，和 get()方法不同的是，apply()方法
 返回的不是 Option[T]，而是返回的值（即 T）。小心使用—确保将代码放置在一个
 try-catch 代码块中。①*/
 try { 
    println(s"Get Andy's Feed Using apply(): ${feeds("Andy Hunt")}") 
     print("Get Bill's Feed: ") 
      println(feeds("Bill Who")) 
 } catch { 
    case _: java.util.NoSuchElementException => println("Not found") 
 } 
/* 下面是使用 apply()方法的输出结果：
 Get Andy's Feed Using apply(): blog.toolshed.com 
 Get Bill's Feed: Not found 
 要添加 feed，请使用 updated()方法。因为我们使用的是不可变集合，所以 updated()
 方法不会影响原来的 Map。如同其方法名所提示的一样，它会返回一个携带着新元素的全新
 Map。*/
 val newFeeds1 = feeds.updated("Venkat Subramaniam", "blog.agiledeveloper.com") 
 println("Venkat's blog in original feeds: " + feeds.get("Venkat Subramaniam")) 
 println("Venkat's blog in new feed: " + newFeeds1("Venkat Subramaniam")) 
/* 让我们看一下调用 updated()方法之后的效果：
 Venkat's blog in original feeds: None 
 Venkat's blog in new feed: blog.agiledeveloper.com 
 除显式地调用 updated()方法之外，也可以利用另一个 Scala 小技巧。如果在赋值语句
 的左边的类或者实例上使用圆括号，那么 Scala 将自动调用 updated()方法。因此，X() = 
   b 等价于 X.updated(b)。如果 updated()接受多个参数，那么可以将除尾参数之外的所
   有参数都放置在括号内部。因此，X(a) = b 等价于 X.updated(a,b)。
   我们可以在不可变集合上使用该隐式调用，像这样：val newFeed = feeds("author") 
   = "blog"。但是，多重赋值语句这种形式，使其失去了语法上的优雅性，后一个等号用于
   调用 updated()方法，而前一个用于保存新创建的 Map。如果我们要从一个方法中返回新
   创建的 Map，那么隐式的 updated()方法使用起来就很优雅。然而，如果我们想要就地更
   新 Map，那么在可变集合上使用该隐式调用则更加具有意义。
   val mutableFeeds = scala.collection.mutable.Map( 
      "Scala Book Forum" -> "forums.pragprog.com/forums/87") 
   mutableFeeds("Groovy Book Forum") = "forums.pragprog.com/forums/246" 
   println(s"Number of forums: ${mutableFeeds.size}") 
   我们将得到如下输出结果：
   Number of forums: 2 
   既然已经学习了 Set 和 Map，那么我们就不能再忽略最常见的集合—List。*/
   println("-----------8.4 不可变列表-------------") 
  /* 通过使用 head 方法，Scala 使访问一个列表的第一个元素更加简单快速。使用 tail 方
   法，可以访问除第一个元素之外的所有元素。访问列表中的最后一个元素需要对列表进行遍
   历，因此相比访问列表的头部和尾部①，该操作更加昂贵。所以，列表上的大多数操作都是
   围绕着对头部和尾部的操作构造的。
   让我们继续使用上面的 feed 例子来学习 List。我们可以使用 List 来维护一个有序的
   feed 集合。*/
   val feedss = List("blog.toolshed.com", "pragdave.me", "blog.agiledeveloper.com") 
  /* 这创建了一个 List[String]的实例。我们可以使用从 0 到 list.length - 1 的索
   引来访问 List 中的元素。当调用 feeds(1)方法时，我们使用的是 List 的 apply()方法。
   也就是说，feeds(0)是 feeds.apply(0)的一个简单形式。要访问第一个元素，我们可以
   使用 feeds(0)或者 head()方法。*/
   println(s"First feed: ${feedss.head}") 
   println(s"Second feed: ${feedss(1)}") 
  /* 这段代码的输出结果如下：
   First feed: blog.toolshed.com
Second feed: pragdave.me
如果我们想要前插一个元素，即将一个元素放在当前 List 的前面，我们可以使用特殊
的::()方法。a :: list 读作“将 a 前插到 list”。虽然 list 跟在这个操作符之后，但
它是 list 上的一个方法。8.5 节会详细介绍其工作原理。*/
val prefixedList = "forums.pragprog.com/forums/87" :: feedss
println(s"First Feed In Prefixed: ${prefixedList.head}")
/*上述代码的输出结果如下：
First Feed In Prefixed: forums.pragprog.com/forums/87
假设我们想要追加一个列表到另外一个列表，例如，将 listA 追加到另外一个列表
list。那么我们可以使用:::()方法将 list 实际上前插到 listA。因此，代码应该是
list ::: listA，并读作“将 list 前插到 listA”。因为 List 是不可变的，所以我们
不会影响前面的任何一个列表。我们只是使用这两个列表中的元素创建了一个新列表。 ① 下
面是一个追加的例子：*/
val feedsWithForums =
feedss ::: List(
"forums.pragprog.com/forums/87",
"forums.pragprog.com/forums/246")
println(s"First feed in feeds with forum: ${feedsWithForums.head}")
println(s"Last feed in feeds with forum: ${feedsWithForums.last}")
/*下面是输出结果：
First feed in feeds with forum: blog.toolshed.com
Last feed in feeds with forum: forums.pragprog.com/forums/246
同样地，:::()方法是在操作符后面的列表上调用的。
要将一个元素追加到列表中，可以使用相同的:::()方法。将想要追加的元素添加到一
个列表中，然后将原始列表拼接到它的前面：*/
val appendedList = feedss ::: List("agilelearner.com")
println(s"Last Feed In Appended: ${appendedList.last}")
/*我们应该能看到下面这样的输出：
Last Feed In Appended: agilelearner.com
需要注意的是，将元素或者列表追加到另外一个列表中，实际上调用的是后者的前缀方
法。这样做的原因是，与遍历到列表的最后一个元素相比，访问列表的头部元素要快得多。
事半功倍。
① 实际上，这个新列表将会共享整个 listA。——译者注

如果想要只选择满足某些条件的 feed，应该使用 filter()方法。如果我们想要检查是
否所有的 feed 都满足某个特定的条件，则可以使用 forall()方法。另外，如果我们想要知
道是否有任意 feed 满足某一条件，那么 exists()方法可以帮到我们。*/
println(s"Feeds with blog: ${feedss.filter(_ contains "blog").mkString(", ")}")
println(s"All feeds have com: ${feedss.forall(_ contains "com")}")
println(s"All feeds have dave: ${feedss.forall(_ contains "dave")}")
println(s"Any feed has dave: ${feedss.exists(_ contains "dave")}")
println(s"Any feed has bill: ${feedss.exists(_ contains "bill")}")
/*我们将得到下面这样的结果：
Feeds with blog: blog.toolshed.com, blog.agiledeveloper.com
All feeds have com: false
All feeds have dave: false
Any feed has dave: true
Any feed has bill: false
如果想要知道我们需要显示的每个 feed 名称的字符数，那么我们可以使用 map()方法来
处理每个元素，并获得一个结果列表，如下所示：*/
println(s"Feed url lengths: ${feedss.map(_.length).mkString(", ")}")
/*下面是输出结果：
Feed url lengths: 17, 11, 23
如果我们对所有 feed 的字符总数感兴趣，那么我们可以使用 foldLeft()方法，如下
所示：*/
val total = feedss.foldLeft(0) { (total, feed) => total + feed.length }
println(s"Total length of feed urls: $total")
/*上述代码的输出结果如下：
Total length of feed urls: 51
需要注意的是，虽然前面的方法在执行求和操作，但是它并没有处理任何可变状态。这
是纯函数式风格。在不断地使用方法对列表中的元素进行处理的过程中，将会累计出一个新
的更新值，但这一切并没有改变任何的内容。
foldLeft()方法将从列表的左侧开始，为列表中的每个元素调用给定的函数值（代码
块）。它将两个参数传递给该函数值，第一个参数是使用（该列表中的）前一个元素执行该函
数值得到的部分结果，这就是为何其被称为“折叠”（folding）—好像列表经过这些计算折
叠出结果一样。第二个参数是列表中的一个元素。部分结果的初始值被作为该方法的参数提
供（在这个例子中是 0）。foldLeft()方法形成了一个元素链，并在该函数值中将计算得到
的部分结果值，从左边开始，从一个元素携带到下一个元素。类似地，foldRight()方法
也一样，但是它从右边开始。
为了使前面的方法更加简洁，Scala 提供了替代方法。/:()方法等价于 foldLeft()
方法，而\:()方法等价 于 foldRight()方法 。下面我们 使用/:()方法重写前 面的
例子：*/
val total2 = (0 /: feedss) { (total, feed) => total + feed.length }
println(s"Total length of feed urls: $total2")
/*上述代码的输出结果如下：
Total length of feed urls: 51
程序员们要么喜欢这样的简洁性，比如我，要么讨厌它；我不觉得有“骑墙派”。
现在我们可以使用 Scala 的多项约定，让代码甚至可以像下面这样更加简洁：*/
//val total3 = (0 /: feeds) { _ + _.length }
//println(s"Total length of feed urls: $total3")
/*下面是输出结果：
Total length of feed urls: 51
在本节中，我们看到了 List 的一些有趣方法。List 中还有其他一些方法，提供了额
外的能力。
这些方法名中的冒号在 Scala 中具有重大的意义，理解它是非常重要的。接下来，让我
们一起来探讨一下吧。*/
println("-----------8.5 方法名约定-----------")
/*本节中介绍的功能非常酷（我真的是这样认为的），但是理解起来也有一点儿难。如果你
在阅读下面的这几页时，身边有氧气面罩，那么在帮助你身边的程序员之前，请先带好自己
的面罩。 ①
在 3.9 节中，我们明白了 Scala 是如何支持操作符重载的，尽管它并没有（原生的）操作
符。操作符就是方法，只不过在实现上使用了取巧的方法命名约定。我们看到了方法名的第
一个字母决定了优先级。在这里，我们将看到它们名称的最后一个字母也有一个效果—它
决定了方法调用的目标。
约定：一开始可能令人感到惊讶，但是当你习惯了之后（或者当你“长出了一只 Scala
之眼”后），你便会发现它可以提高代码的流畅度。例如，如果要前插一个值到列表中，可以
编写 value :: list。即使它读起来好像是“将 value 前插到 list 中”，但是，该方法
的目标实际上是 list，而 value 作为参数，即 list.::(value)。
有些程序员会问，是否可以在调用过程中将冒号附加到现有的方法上。① 答案是不可以，因
为 Scala 并没有提供用于装饰现有方法名称的设施。该约定仅用于以此特殊符号结束的方法名。
如果方法名以冒号（:）结尾，那么调用的目标是该操作符后面的实例。Scala 不允许使用字
母作为操作符的名称，除非使用下划线对该操作符增加前缀。因此，一个名为 jumpOver:()
的方法是被拒绝的，但是 jumpOver_:()则会被接受。
在下面这个例子中，^()方法是一个定义在 Cow 类上的方法，而^:()方法是独立定义在
Moon 类上的一个方法。*/
class Cow {
  def ^(moon: Moon): Unit = println("Cow jumped over the moon")
}
class Moon {
  def ^:(cow: Cow): Unit = println("This cow jumped over the moon too")
}
//下面是使用这两个方法的一个示例。
val cow = new Cow
val moon = new Moon
cow ^ moon
cow ^: moon
/*对这两个方法的调用看起来几乎是完全一样的，cow 都在操作符的左边，而 moon 都在
操作符的右边。但是，第一个调用发生在 cow 上，而第二个调用发生在 moon 上，这一区别
相当微妙。对于 Scala 新人来说，这可能是相当令人沮丧的；但是，在 List 的操作中，这
种约定相当常见，所以我们最好还是习惯它。上述代码的输出结果如下：
Cow jumped over the moon
This cow jumped over the moon too
上面这个例子中的最后一行调用和下面的代码片段等价：
moon.^:(cow)
除了以:结尾的操作符之外，还有一组调用目标也是跟随它们之后的实例的操作符。这
些都是一元操作符，分别是+、-、!和～。其中一元+操作符被映射为对 unary_+()方法的
调用，而一元-操作符被映射为对 unary_-()方法的调用，以此类推。
下面是一个在 Sample 类上定义一元操作符的例子。
① 原作者的意思是将 a.fun(b)这种调用通过添加冒号的方式变成 a.fun:(b)，也就是 b fun: a。——译者注*/
class Sample {
  def unary_+(): Unit = println("Called unary +")
  def unary_-(): Unit = println("called unary -")
  def unary_!(): Unit = println("called unary !")
  def unary_~(): Unit = println("called unary ~")
}
val sample = new Sample
+sample
-sample
!sample
~sample
/*上述代码的输出结果如下：
Called unary +
called unary -
called unary !
called unary ~
在熟悉了 Scala 之后，你便会长出一只 Scala 之眼—很快，处理这些符号和约定将会变
成你的第二天性。*/
println("-----------8.6 for 表达式--------")
/*foreach()方法提供了集合上的内部迭代器—你不必控制循环，只需要提供在每次迭
代上下文中执行的代码片段即可。但是，如果希望同时控制循环或者处理多个集合，那么你
便可以使用外部迭代器，即 for 表达式。我们来看一个简单的循环。*/
for (_ <- 1 to 3) { print("ho ") }
//这段代码打印出了“ho ho ho”。它是下面表达式的一般语法的简洁形式：
//for([pattern <- generator; definition*]+; filter*)
//[yield] expression
/*for 表达式接受一个或者多个生成器作为参数，并带有 0 个或者多个定义以及 0 个或者
多个过滤器。这些都是由分号分隔的。yield 关键字是可选的，如果存在，则告诉表达式返
回一个值列表而不是一个 Unit。虽然有大量的细节，不过不必担心，因为我们将会用例子
来说明，所以你很快就会适应它了。
让我们先从 yield 关键字开始。假设我们想要获取一个区间内的值，并将每个值都乘以
2。下面是这样做的一个代码示例。*/
val result = for (i <- 1 to 10) yield i * 2
println(result)
/*上面的代码返回了一个值的集合，其中每个值分别是给定区间 1 到 10 中的每个值的两倍大小。
我们还可以使用 map()方法来完成前面的逻辑，像下面这样。
val result2 = (1 to 10).map(_ * 2)
在幕后，Scala 将根据表达式的复杂程度，把 for 表达式翻译为组合使用了类似 map()
和 withFilter()这样的方法的表达式。
现在，假设我们只想将区间内的偶数进行加倍，那么我们可以使用过滤器。*/
val doubleEven = for (i <- 1 to 10; if i % 2 == 0) yield i * 2
println(doubleEven)
/*前面的 for 表达式读作“返回一个 i * 2 的集合，其中 i 是一个给定区间的成员，且
i 是偶数”。因此，上面的表达式实际上就像是对一个值的集合进行 SQL 查询—这在函数
式编程中称为列表推导（list comprehension）。
如果觉得上述代码中的分号碍眼，也可以将它们替换成换行符，然后使用大括号，而不
是括号，就像下面这样：
for {
  i <- 1 to 10
  if i % 2 == 0
} yield i * 2
可以将定义和生成器放在一起。Scala 在每次迭代的过程中都会定义一个新的 val 值。
在下面这个例子中，我们循环遍历一个 Person 的集合，并打印出其姓氏。*/
class Person(val firstName: String, val lastName: String)
object Person {
def apply(firstName: String, lastName: String): Person =
new Person(firstName, lastName)
}
val friends = List(Person("Brian", "Sletten"), Person("Neal", "Ford"),
Person("Scott", "Davis"), Person("Stuart", "Halloway"))
val lastNames =
for (friend <- friends; lastName = friend.lastName) yield lastName
println(lastNames.mkString(", "))
/*这段代码的输出如下：
Sletten, Ford, Davis, Halloway
上面的代码也是 Scala 语法糖的一个例子，我们在新建一个 Person 的列表时实际上在
幕后调用的是 apply()方法—这样的代码简洁且易读。
如果在 for 表达式中提供了多个生成器，那么每个生成器都将形成一个内部循环。最右
边的生成器控制最里面的循环。下面是使用了两个生成器的例子。
for (i <- 1 to 3; j <- 4 to 6) {
print(s"[$i,$j] ")
}
上述代码的输出结果如下：
[1,4] [1,5] [1,6] [2,4] [2,5] [2,6] [3,4] [3,5] [3,6]
使用多个生成器，可以轻松地将这些值组合起来，以创建强大的组合
*/
}

object Scala_Collections extends App{
  println("This is from Hands on Scala Programming")
  
  println("--------4.1 Operations----------------")
  /*Scala collections provide many common operations for constructing them, querying them, or transforming
  them. These operations are present on the Array s we saw in Chapter 3: Basic Scala, but they also apply to
  all the collections we will cover in this chapter: Vectors (4.2.1), Sets (4.2.3), Maps (4.2.4), etc.
  4.1.1 Builders*/
  val b = Array.newBuilder[Int]
  //b: mutable.ArrayBuilder[Int] = ArrayBuilder.ofInt
  b += 1
  b += 2
  println(b.result())
  //res3: Array[Int] = Array(1, 2)
  //For some reason, above code can only see result in sbt console.
  /*4.2.scala
  Builders let you efficiently construct a collection of unknown length, "freezing" it into the collection you
  want at the end. This is most useful for constructing Arrays or immutable collections where you cannot add
  or remove elements once the collection has been constructed.
  4.1.2 Factory Methods*/
  Array.fill(5)("hello").foreach(println) // Array with "hello" repeated 5 times
  //res4: Array[String] = Array("hello", "hello", "hello", "hello", "hello")
  Array.tabulate(5)(n => s"hello $n").foreach(println) // Array with 5 items, each computed from its index
  //res5: Array[String] = Array("hello 0", "hello 1", "hello 2", "hello 3", "hello 4")
  println((Array(1, 2, 3) ++ Array(4, 5, 6)).mkString(",")) // Concatenating two Arrays into a larger one
  //res6: Array[Int] = Array(1, 2, 3, 4, 5, 6)
  /*4.3.scala
  Factory methods provide another way to instantiate collections: with every element the same, with each
  element constructed depending on the index, or from multiple smaller collections. This can be more
  convenient than using Builders (4.1.1) in many common use cases.
  See example 4.1 - BuildersFactories
  04.1.3 Transforms*/
 Array(1, 2, 3, 4, 5).map(i => i * 2).foreach(x=>print(s"$x,")) // Multiply every element by 2
  //res7: Array[Int] = Array(2, 4, 6, 8, 10)
  println()
  Array(1, 2, 3, 4, 5).filter(i => i % 2 == 1).foreach(x=>print(s"$x,"));println() // Keep only elements not divisible by 2
    //res8: Array[Int] = Array(1, 3, 5)
    Array(1, 2, 3, 4, 5).take(2).foreach(x=>print(s"$x,"));println() // Keep first two elements
  //res9: Array[Int] = Array(1, 2)
  Array(1, 2, 3, 4, 5).drop(2).foreach(x=>print(s"$x,"));println() // Discard first two elements
  //res10: Array[Int] = Array(3, 4, 5)
  Array(1, 2, 3, 4, 5).slice(1, 4).foreach(x=>print(s"$x,"));println() // Keep elements from index 1-4
  //res11: Array[Int] = Array(2, 3, 4)
  Array(1, 2, 3, 4, 5, 4, 3, 2, 1, 2, 3, 4, 5, 6, 7, 8).distinct.foreach(x=>print(s"$x,"));println() // Removes all duplicates
  //res12: Array[Int] = Array(1, 2, 3, 4, 5, 6, 7, 8)
  /*4.4.scala
  Transforms take an existing collection and create a new collection modified in some way. Note that these
  transformations create copies of the collection, and leave the original unchanged. That means if you are still
  using the original array, its contents will not be modified by the transform:
  @ val a = Array(1, 2, 3, 4, 5)
  a: Array[Int] = Array(1, 2, 3, 4, 5)
  @ val a2 = a.map(x => x + 10)
  a2: Array[Int] = Array(11, 12, 13, 14, 15)
  @ a(0) // Note that `a` is unchanged!
  res15: Int = 1
  @ a2(0)
  res16: Int = 11
  </> 4.5.scala
  The copying involved in these collection transformations does have some overhead, but in most cases that
  should not cause issues. If a piece of code does turn out to be a bottleneck that is slowing down your
  program, you can always convert your .map / .filter /etc. transformation code into mutating operations
  over raw Array s or In-Place Operations (4.3.4) over Mutable Collections (4.3) to optimize for performance.
  See example 4.2 - Transforms*/
  println("----------4.1.4 Queries--------------")
  Array(1, 2, 3, 4, 5, 6, 7).find(i => i % 2 == 0 && i > 4).foreach(x=>print(s"$x,"));println()
  //res17: Option[Int] = Some(6)
  Array(1, 2, 3, 4, 5, 6, 7).find(i => i % 2 == 0 && i > 10).foreach(x=>print(s"$x,"));println()
  //res18: Option[Int] = None
  println(Array(1, 2, 3, 4, 5, 6, 7).exists(x => x > 1)) // are any elements greater than 1?
  //res19: Boolean = true
  println(Array(1, 2, 3, 4, 5, 6, 7).exists(_ < 0))// same as a.exists(x => x < 0)
  //res20: Boolean = false
  /*4.6.scala
  Queries let you search for elements without your collection, returning either a Boolean indicating if a
  matching element exists, or an Option containing the element that was found. This can make it convenient
  to find things inside your collections without the verbosity of writing for-loops to inspect the elements one
  by one.*/
  println("-----------4.1.5 Aggregations------------")
  /*4.1.5.1 mkString
  Stringifies the elements in a collection and combines them into one long string, with the given separator.
  Optionally can take a start and end delimiter:
  Array(1, 2, 3, 4, 5, 6, 7).mkString(",")
  res21: String = "1,2,3,4,5,6,7"
  @ Array(1, 2, 3, 4, 5, 6, 7).mkString("[", ",", "]")
  res22: String = "[1,2,3,4,5,6,7]"
  </> 4.7.scala
  4.1.5.2 foldLeft
  Takes a starting value and a function that it uses to combine each element of your collection with the
  starting value, to produce a final result:
  @ Array(1, 2, 3, 4, 5, 6, 7).foldLeft(0)((x, y) => x + y) // sum of all elements
  res23: Int = 28
  @ Array(1, 2, 3, 4, 5, 6, 7).foldLeft(1)((x, y) => x * y) // product of all elements
  res24: Int = 5040
  @ Array(1, 2, 3, 4, 5, 6, 7).foldLeft(1)(_ * _) // same as above, shorthand syntax
  res25: Int = 5040
  
  In general, foldLeft is similar to a for -loop and accumulator var , and the above sum-of-all-elements
  foldLeft call can equivalently be written as:
  @ {
  var total = 0
  for (i <- Array(1, 2, 3, 4, 5, 6, 7)) total += i
  total
  }
  total: Int = 28
  </> 4.9.scala
  4.1.5.3 groupBy
  Groups your collection into a Map of smaller collections depending on a key:*/
  val grouped = Array(1, 2, 3, 4, 5, 6, 7).groupBy(_ % 2)
  //grouped: Map[Int, Array[Int]] = Map(0 -> Array(2, 4, 6), 1 -> Array(1, 3, 5, 7))
  grouped(0)
  //res26: Array[Int] = Array(2, 4, 6)
  grouped(1)
  //res27: Array[Int] = Array(1, 3, 5, 7)
  /*4.1.6 Combining Operations
  It is common to chain more than one operation together to achieve what you want. For example, here is a
  function that computes the standard deviation of an array of numbers:
  @ def stdDev(a: Array[Double]): Double = {
  val mean = a.foldLeft(0.0)(_ + _) / a.length
  val squareErrors = a.map(_ - mean).map(x => x * x)
  math.sqrt(squareErrors.foldLeft(0.0)(_ + _) / a.length)
  }
  @ stdDev(Array(1, 2, 3, 4, 5))
  res29: Double = 1.4142135623730951
  @ stdDev(Array(3, 3, 3))
  res30: Double = 0.0
  </> 4.11.scala
  Scala collections provide a convenient helper method .sum that is equivalent to .foldLeft(0.0)(_ + _) , so
  the above code can be simplified to:
  Chapter 4 Scala Collections
  63@ def stdDev(a: Array[Double]): Double = {
  val mean = a.sum / a.length
  val squareErrors = a.map(_ - mean).map(x => x * x)
  math.sqrt(squareErrors.sum / a.length)
  }
  </> 4.12.scala
  As another example, here is a function that uses .exists , .map and .distinct to check if an incoming grid
  of numbers is a valid Sudoku grid:
  @ def isValidSudoku(grid: Array[Array[Int]]): Boolean = {
  !Range(0, 9).exists{i =>
  val row = Range(0, 9).map(grid(i)(_))
  val col = Range(0, 9).map(grid(_)(i))
  val square = Range(0, 9).map(j => grid((i % 3) * 3 + j % 3)((i / 3) * 3 + j / 3))
  row.distinct.length != row.length ||
  col.distinct.length != col.length ||
  square.distinct.length != square.length
  }
  }
  </> 4.13.scala
  This implementation receives a Sudoku grid, represented as a 2-dimensional Array[Array[Int]] . For each i
  from 0 to 9 , we pick out a single row, column, and 3x3 square. It then checks that each such
  row/column/square has 9 unique numbers by calling .distinct to remove any duplicates, and then
  checking if the .length has changed as a result of that removal.
  We can test this on some example grids to verify that it works:
  @ isValidSudoku(Array(
  @ isValidSudoku(Array(
  Array(5, 3, 4,6, 7, 8,9, 1, 2),Array(5, 3, 4,6, 7, 8,9, 1, 2),
  Array(6, 7, 2,1, 9, 5,3, 4, 8),Array(6, 7, 2,1, 9, 5,3, 4, 8),
  Array(1, 9, 8,3, 4, 2,5, 6, 7),Array(1, 9, 8,3, 4, 2,5, 6, 7),
  Array(8, 5, 9,7, 6, 1,4, 2, 3),Array(8, 5, 9,7, 6, 1,4, 2, 3),
  Array(4, 2, 6,8, 5, 3,7, 9, 1),Array(4, 2, 6,8, 5, 3,7, 9, 1),
  Array(7, 1, 3,9, 2, 4,8, 5, 6),Array(7, 1, 3,9, 2, 4,8, 5, 6),
  Array(9, 6, 1,5, 3, 7,2, 8, 4),Array(9, 6, 1,5, 3, 7,2, 8, 4),
  Array(2, 8, 7,4, 1, 9,6, 3, 5),Array(2, 8, 7,4, 1, 9,6, 3, 5),
  Array(3, 4, 5,2, 8, 6,1, 7, 9)Array(3, 4, 5,2, 8, 6,1, 7, 8)
  ))
  res33: Boolean = true
  Chapter 4 Scala Collections
  )) // bottom right cell should be 9
  </> 4.14.scala
  res34: Boolean = false
  </> 4.15.scala
  64Chaining collection transformations in this manner will always have some overhead, but for most use cases
  the overhead is worth the convenience and simplicity that these transforms give you. If collection
  transforms do become a bottleneck, you can optimize the code using Views (4.1.8), In-Place Operations
  (4.3.4), or finally by looping over the raw Array s yourself.
  See example 4.4 - Combining
  4.1.7 Converters
  You can convert among Array s and other collections like Vector (4.2.1)s and Set (4.2.3) using the .to
  method:
  @ Array(1, 2, 3).to(Vector)
  res35: Vector[Int] = Vector(1, 2, 3)
  @ Vector(1, 2, 3).to(Array)
  res36: Array[Int] = Array(1, 2, 3)
  @ Array(1, 1, 2, 2, 3, 4).to(Set)
  res37: Set[Int] = Set(1, 2, 3, 4)
  </> 4.16.scala
  4.1.8 Views
  When you chain multiple transformations on a collection, we are creating many intermediate collections
  that are immediately thrown away. For example, in the following snippet:
  @ val myArray = Array(1, 2, 3, 4, 5, 6, 7, 8, 9)
  @ val myNewArray = myArray.map(x => x + 1).filter(x => x % 2 == 0).slice(1, 3)
  myNewArray: Array[Int] = Array(4, 6)
  </> 4.17.scala
  The chain of .map .filter .slice operations ends up traversing the collection three times, creating three
  new collections, but only the last collection ends up being stored in myNewArray and the others are
  discarded.
  Chapter 4 Scala Collections
  65myArray
  1
  2
  3
  4
  5
  6
  7
  8
  9
  map(x => x + 1)
  2
  3
  4
  5
  6
  7
  8
  9
  10
  filter(x => x % 2 == 0)
  2
  4
  6
  8
  10
  slice(1, 3)
  myNewArray
  4
  6
  This creation and traversal of intermediate collections is wasteful. In cases where you have long chains of
  collection transformations that are becoming a performance bottleneck, you can use the .view method
  together with .to to "fuse" the operations together:
  @ val myNewArray = myArray.view.map(_ + 1).filter(_ % 2 == 0).slice(1, 3).to(Array)
  myNewArray: Array[Int] = Array(4, 6)
  </> 4.18.scala
  Using .view before the map / filter / slice transformation operations defers the actual traversal and
  creation of a new collection until later, when we call .to to convert it back into a concrete collection type:
  myArray
  1
  2
  3
  4
  5
  6
  7
  8
  9
  view map filter slice to
  myNewArray
  4
  6
  This allows us to perform this chain of map / filter / slice transformations with only a single traversal, and
  only creating a single output collection. This reduces the amount of unnecessary processing and memory
  allocations.
  See example 4.5 - ConvertersViews
  4.2 Immutable Collections
  While Array s are the low-level primitive, most Scala applications are built upon its mutable and immutable
  collections: Vector s, List s, Set s, and Map s. Of these, immutable collections are by far the most common.
  Immutable collections rule out an entire class of bugs due to unexpected modifications, and are especially
  useful in multi-threaded scenarios where you can safely pass immutable collections between threads
  without worrying about thread-safety issues. Most immutable collections use Structural Sharing (4.2.2) to
  make creating updated copies cheap, allowing you to use them in all but the most performance critical
  code.
  Chapter 4 Scala Collections
  664.2.1 Immutable Vectors
  Vector s are fixed-size, immutable linear sequences. They are a good general-purpose sequence data
  structure, and provide efficient O(log n) performance for most operations.
  @ val v = Vector(1, 2, 3, 4, 5)@ val v = Vector[Int]()
  v: Vector[Int] = Vector(1, 2, 3, 4, 5)v: Vector[Int] = Vector()
  @ v(0)@ val v1 = v :+ 1
  res42: Int = 1v1: Vector[Int] = Vector(1)
  @ val v2 = v.updated(2, 10)@ val v2 = 4 +: v1
  v2: Vector[Int] = Vector(1, 2, 10, 4, 5)v2: Vector[Int] = Vector(4, 1)
  @ v2@ val v3 = v2.tail
  res44: Vector[Int] = Vector(1, 2, 10, 4, 5)v3: Vector[Int] = Vector(1)
  @ v // note that `v` did not change!
  res45: Vector[Int] = Vector(1, 2, 3, 4, 5)
  </> 4.19.scala
  </> 4.20.scala
  Unlike Array s where a(...) = ... mutates it in place, a Vector 's .updated method returns a new Vector
  with the modification while leaving the old Vector unchanged. Due to Structural Sharing (4.2.2), this is a
  reasonably-efficient O(log n) operation. Similarly, using :+ and +: to create a new Vector with additional
  elements on either side, or using .tail to create a new Vector with one element removed, are all O(log n)
  as well:
  Vector s support the same set of Operations (4.1) that Array s and other collections do: builders (4.1.1),
  factory methods (4.1.2), transforms (4.1.3), etc.
  In general, using Vector s is handy when you have a sequence you know will not change, but need flexibility
  in how you work with it. Their tree structure makes most operations reasonably efficient, although they will
  never be quite as fast as Array s for in-place updates or Immutable Lists (4.2.5) for adding and removing
  elements at the front.
  4.2.2 Structural Sharing
  Vector s implement their O(log n) copy-and-update operations by re-using portions of their tree structure.
  This avoids copying the whole tree, resulting in a "new" Vector that shares much of the old tree structure
  with only minor modifications.
  Consider a large Vector , v1 :
  @ val v1 = Vector(1, 2, 0, 9,
  Chapter 4 Scala Collections
  7, 2, 9, 6,
  ...,
  3, 2, 5, 5,
  4, 8, 4, 6)
  67This is represented in-memory as a tree structure, whose breadth and depth depend on the size of the
  Vector :
  v1
  1
  7
  2
  9
  2
  6
  0
  ...
  9
  ...
  3
  ...
  ...
  ...
  ...
  2
  ...
  5
  5
  ...
  ...
  484
  6
  .........
  This example is somewhat simplified - a Vector in Scala has 32 elements per tree node rather than the 4
  shown above - but it will serve us well enough to illustrate how the Vector data structure works.
  Let us consider what happens if we want to perform an update, e.g. replacing the fifth value 7 in the above
  Vector with the value 8 :
  @ val v2 = v1.updated(4, 8)
  @ v2
  res50: Vector[Int] = Vector(1, 2, 0, 9, 8, 2, 9, 6, ..., 3, 2, 5, 5, 4, 8, 4, 6)
  </> 4.21.scala
  This is done by making updated copies of the nodes in the tree that are in the direct path down to the value
  we wish to update, but re-using all other nodes unchanged:
  v2
  1
  8
  2
  9
  6
  7
  2
  2
  9
  0
  6
  v1
  9
  3
  ...
  ...
  ...
  ...
  ...
  ...
  ...
  ...
  2
  5
  ...
  5
  ...
  4
  ...
  8
  4
  6
  ...
  In this example Vector with 9 nodes, only 3 of the nodes end up needing to be copied. In a large Vector ,
  the number of nodes that need to be copied is proportional to the height of the tree, while other nodes can
  be re-used: this structural sharing is what allows updated copies of the Vector to be created in only O(log
  n) time. This is much less than the O(n) time it takes to make a full copy of a mutable Array or other data
  structure.
  Chapter 4 Scala Collections
  68Nevertheless, updating a Vector does always involve a certain amount of copying, and will never be as fast
  as updating mutable data structures in-place. In some cases where performance is important and you are
  updating a collection very frequently, you might consider using a mutable ArrayDeque (4.3.1) which has
  faster O(1) update/append/prepend operations, or raw Array s if you know the size of your collection in
  advance.
  A similar tree-shaped data structure is also used to implement Immutable Sets (4.2.3) and Immutable Maps
  (4.2.4).
  See example 4.6 - ImmutableVectors
  4.2.3 Immutable Sets
  Scala's immutable Set s are unordered collections of elements without duplicates, and provide an efficient
  O(log n) .contains method. Set s can be constructed via + and elements removed by - , or combined via
  ++ . Note that duplicates elements are discarded:
  @ val s = Set(1, 2, 3)@ Set(1, 2, 3) + 4 + 5
  s: Set[Int] = Set(1, 2, 3)res53: Set[Int] = HashSet(5, 1, 2, 3, 4)
  @ s.contains(2)@ Set(1, 2, 3) - 2
  res51: Boolean = trueres54: Set[Int] = Set(1, 3)
  @ s.contains(4)@ Set(1, 2, 3) ++ Set(2, 3, 4)
  res52: Boolean = false
  </> 4.22.scala
  res55: Set[Int] = Set(1, 2, 3, 4)
  </> 4.23.scala
  The uniqueness of items within a Set is also sometimes useful when you want to ensure that a collection
  does not contain any duplicates.
  You can iterate over Set s using for-loops, but the order of items is undefined and should not be relied upon:
  @ for (i <- Set(1, 2, 3, 4, 5)) println(i)
  5
  1
  2
  3
  4
  </> 4.24.scala
  Most immutable Set operations take time O(log n) in the size of the Set . This is fast enough for most
  purposes, but in cases where it isn't you can always fall back to Mutable Sets (4.3.2) for better performance.
  Set s also support the standard set of operations common to all collections.
  Chapter 4 Scala Collections
  69See example 4.7 - ImmutableSets
  4.2.4 Immutable Maps
  Immutable maps are unordered collections of keys and values, allowing efficient lookup by key:
  @ val m = Map("one" -> 1, "two" -> 2, "three" -> 3)
  m: Map[String, Int] = Map("one" -> 1, "two" -> 2, "three" -> 3)
  @ m.contains("two")
  res58: Boolean = true
  @ m("two")
  res59: Int = 2
  </> 4.25.scala
  You can also use .get if you're not sure whether a map contains a key or not. This returns Some(v) if the key
  is present, None if not:
  @ m.get("one")
  res60: Option[Int] = Some(1)
  @ m.get("four")
  res61: Option[Int] = None
  </> 4.26.scala
  While Map s support the same set of operations as other collections, they are treated as collections of tuples
  representing each key-value pair. Conversions via .to requires a collection of tuples to convert from, + adds
  tuples to the Map as key-value pairs, and for loops iterate over tuples:
  @ Vector(("one", 1), ("two", 2), ("three", 3)).to(Map)
  res62: Map[String, Int] = Map("one" -> 1, "two" -> 2, "three" -> 3)
  @ Map[String, Int]() + ("one" -> 1) + ("three" -> 3)
  res63: Map[String, Int] = Map("one" -> 1, "three" -> 3)
  @ for ((k, v) <- m) println(k + " " + v)
  one 1
  two 2
  three 3
  </> 4.27.scala
  Like Set s, the order of items when iterating over a Map is undefined and should not be relied upon, and
  most immutable Map operations take time O(log n) in the size of the Map .
  Chapter 4 Scala Collections
  70See example 4.8 - ImmutableMaps
  4.2.5 Immutable Lists
  @ val myList = List(1, 2, 3, 4, 5)
  myList: List[Int] = List(1, 2, 3, 4, 5)
  @ myList.head
  res66: Int = 1
  @ val myTail = myList.tail
  myTail: List[Int] = List(2, 3, 4, 5)
  @ val myOtherList = 0 :: myList
  myOtherList: List[Int] = List(0, 1, 2, 3, 4, 5)
  @ val myThirdList = -1 :: myList
  myThirdList: List[Int] = List(-1, 1, 2, 3, 4, 5)
  </> 4.28.scala
  Scala's immutable List s are a singly-linked list data structure. Each node in the list has a value and pointer
  to the next node, terminating in a Nil node. List s have a fast O(1) .head method to look up the first item
  in the list, a fast O(1) .tail method to create a list without the first element, and a fast O(1) :: operator to
  create a new List with one more element in front.
  .tail and :: are efficient because they can share much of the existing List : .tail returns a reference to
  the next node in the singly linked structure, while :: adds a new node in front. The fact that multiple lists
  can share nodes means that in the above example, myList , myTail , myOtherList and myThirdList are
  actually mostly the same data structure:
  myList
  myOtherList01
  myThirdList-1myTail
  2
  3
  4
  5
  Nil
  This can result in significant memory savings if you have a large number of collections that have identical
  elements on one side, e.g. paths on a filesystem which all share the same prefix. Rather than creating an
  updated copy of an Array in O(n) time, or an updated copy of a Vector in O(log n) time, pre-pending an
  item to a List is a fast O(1) operation.
  Chapter 4 Scala Collections
  71The downside of List s is that indexed lookup via myList(i) is a slow O(n) operation, since you need to
  traverse the list starting from the left to find the element you want. Appending/removing elements on the
  right hand side of the list is also a slow O(n), since it needs to make a copy of the entire list. For use cases
  where you want fast indexed lookup or fast appends/removes on the right, you should consider using
  Vectors (4.2.1) or mutable ArrayDeques (4.3.1) instead.
  See example 4.9 - ImmutableLists
  4.3 Mutable Collections
  Mutable collections are in general faster than their immutable counterparts when used for in-place
  operations. However, mutability comes at a cost: you need to be much more careful sharing them between
  different parts of your program. It is easy to create bugs where a shared mutable collection is updated
  unexpectedly, forcing you to hunt down which line in a large codebase is performing the unwanted update.
  A common approach is to use mutable collections locally within a function or private to a class where there
  is a performance bottleneck, but to use immutable collections elsewhere where speed is less of a concern.
  That gives you the high performance of mutable collections where it matters most, while not sacrificing the
  safety that immutable collections give you throughout the bulk of your application logic.
  4.3.1 Mutable ArrayDeques
  ArrayDeque s are general-purpose mutable, linear collections that provide efficient O(1) indexed lookups,
  O(1) indexed updates, and O(1) insertion and removal at both left and right ends:
  @ val myArrayDeque = collection.mutable.ArrayDeque(1, 2, 3, 4, 5)
  myArrayDeque: collection.mutable.ArrayDeque[Int] = ArrayDeque(1, 2, 3, 4, 5)
  @ myArrayDeque.removeHead()
  res71: Int = 1
  @ myArrayDeque.append(6)
  res72: collection.mutable.ArrayDeque[Int] = ArrayDeque(2, 3, 4, 5, 6)
  @ myArrayDeque.removeHead()
  res73: Int = 2
  @ myArrayDeque
  res74: collection.mutable.ArrayDeque[Int] = ArrayDeque(3, 4, 5, 6)
  </> 4.29.scala
  ArrayDeque s are implemented as a circular buffer, with pointers to the logical start and end of the
  collection within the buffer. The operations above can be visualized as follows, from left to right:
  Chapter 4 Scala Collections
  72myArrayDequeremoveHead()
  startendstart
  52
  1
  2
  3
  4
  3
  4
  append(6)
  endend
  56
  removeHead()
  start
  2
  3
  4
  end
  5
  6
  start
  3
  4
  5
  An ArrayDeque tries to re-use the same underlying Array as much as possible, only moving the start and
  end pointers around as elements get added or removed from either end. Only if the total number of
  elements grows beyond the current capacity does the underlying Array get re-allocated, and the size is
  increased by a fix multiple to keep the amortized cost of this re-allocation small.
  As a result, operations on an ArrayDeque are much faster than the equivalent operations on an immutable
  Vector , which has to allocate O(log n) new tree nodes for every operation you perform.
  ArrayDeque s have the standard suite of Operations (4.1). They can serve many roles:
  An Array that can grow: an Array.newBuilder does not allow indexed lookup or modification while
  the array is being built, and an Array does not allow adding more elements. An ArrayDeque allows
  both
  A faster, mutable alternative to immutable Vector s, if you find adding/removing items from either
  end using :+ / +: or .tail / .init is a bottleneck in your code. Appending and prepending to
  ArrayDeque s is much faster than the equivalent Vector operations
  A first-in-first-out Queue, by inserting items to the right via .append , and removing items via
  .removeHead
  A first-in-last-out Stack, by inserting items to the right via .append , and removing items via
  .removeLast
  If you want to "freeze" a mutable ArrayDeque into an immutable Vector , you can use .to(Vector) :
  @ myArrayDeque.to(Vector)
  res75: Vector[Int] = Vector(3, 4, 5, 6)
  </> 4.30.scala
  Note that this makes a copy of the entire collection.
  ArrayDeque s implement the abstract collection.mutable.Buffer interface, and can also be constructed
  via the collection.mutable.Buffer(...) syntax.
  See example 4.10 - MutableArrayDeques
  Chapter 4 Scala Collections
  734.3.2 Mutable Sets
  The Scala standard library provides mutable Set s as a counterpart to the immutable Set s we saw earlier.
  Mutable sets also provide efficient .contains checks (O(1)), but instead of constructing new copies of the
  Set via + and - , you instead add and remove elements from the Set via .add and .remove :
  @ val s = collection.mutable.Set(1, 2, 3)
  @ s.add(4)
  s: mutable.Set[Int] = HashSet(1, 2, 3)
  @ s.remove(1)
  @ s.contains(2)
  res77: Boolean = true
  @ s
  res81: mutable.Set[Int] = HashSet(2, 3, 4)
  @ s.contains(4)
  res78: Boolean = false
  </> 4.31.scala
  </> 4.32.scala
  You can "freeze" a mutable Set into an immutable Set by using .to(Set) , which makes a copy you cannot
  mutate using .add or .remove , and convert it back to a mutable Set the same way. Note that each such
  conversion makes a copy of the entire set.
  See example 4.11 - MutableSets
  4.3.3 Mutable Maps
  Mutable Map s are again just like immutable Map s, but allow you to mutate the Map by adding or removing
  key-value pairs:
  @ val m = collection.mutable.Map("one" -> 1, "two" -> 2, "three" -> 3)
  m: mutable.Map[String, Int] = HashMap("two" -> 2, "three" -> 3, "one" -> 1)
  @ m.remove("two")
  res83: Option[Int] = Some(2)
  @ m("five") = 5
  @ m
  res85: mutable.Map[String, Int] = HashMap("five" -> 5, "three" -> 3, "one" -> 1)
  </> 4.33.scala
  Mutable Map s have a convenient getOrElseUpdate function, that allows you to look up a value by key, and
  compute/store the value if there isn't one already present:
  Chapter 4 Scala Collections
  74@ val m = collection.mutable.Map("one" -> 1, "two" -> 2, "three" -> 3)
  @ m.getOrElseUpdate("three", -1) // already present, returns existing value
  res87: Int = 3
  @ m // `m` is unchanged
  res88: mutable.Map[String, Int] = HashMap("two" -> 2, "three" -> 3, "one" -> 1)
  @ m.getOrElseUpdate("four", -1) // not present, stores new value in map and returns it
  res89: Int = -1
  @ m // `m` now contains "four" -> -1
  res90: mutable.Map[String, Int] = HashMap(
  "two" -> 2,
  "three" -> 3,
  "four" -> -1,
  "one" -> 1
  )
  </> 4.34.scala
  .getOrElseUpdate
  makes it convenient to use a mutable Map as a cache: the second parameter to
  .getOrElseUpdate is a lazy "by-name" parameter, and is only evaluated when the key is not found in the
  Map . This provides the common "check if key present, if so return value, otherwise insert new value and
  return that" workflow built in. We will go into more detail how by-name parameters work in Chapter 5:
  Notable Scala Features.
  Mutable Map s are implemented as hash-tables, with m(...) lookups and m(...) =
  efficient O(1) operations.
  ...
  updates being
  See example 4.12 - MutableMaps
  Chapter 4 Scala Collections
  754.3.4 In-Place Operations
  All mutable collections, including Array s, have in-place versions of many common collection operations.
  These allow you to perform the operation on the mutable collection without having to make a transformed
  copy:
  @ val a = collection.mutable.ArrayDeque(1, 2, 3, 4)
  a: mutable.ArrayDeque[Int] = ArrayDeque(1, 2, 3, 4)
  @ a.mapInPlace(_ + 1)
  res92: mutable.ArrayDeque[Int] = ArrayDeque(2, 3, 4, 5)
  @ a.filterInPlace(_ % 2 == 0)
  res93: mutable.ArrayDeque[Int] = ArrayDeque(2, 4)
  @ a // `a` was modified in place
  res94: mutable.ArrayDeque[Int] = ArrayDeque(2, 4)
  </> 4.35.scala
  Apart from those shown above, there is also dropInPlace , sliceInPlace , sortInPlace , etc. Using in-place
  operations rather than normal transformations avoids the cost of allocating new transformed collections,
  and can help in performance-critical scenarios.
  See example 4.13 - InPlaceOperations
  4.4 Common Interfaces
  In many cases, a piece of code does not care exactly what collection it is working on. For example, code that
  just needs something that can be iterated over in order can take a Seq[T] :
  @ def iterateOverSomething[T](items: Seq[T]) = {
  for (i <- items) println(i)
  }
  @ iterateOverSomething(Vector(1, 2, 3))
  1
  2
  3
  @ iterateOverSomething(List(("one", 1), ("two", 2), ("three", 3)))
  (one,1)
  (two,2)
  (three,3)
  Chapter 4 Scala Collections
  </> 4.36.scala
  76Code that needs something which provides efficient indexed lookup doesn't care if it's an Array or Vector ,
  but cannot work with a List . In that case, your code can take an IndexedSeq[T] :
  @ def getIndexTwoAndFour[T](items: IndexedSeq[T]) = (items(2), items(4))
  @ getIndexTwoAndFour(Vector(1, 2, 3, 4, 5))
  res99: (Int, Int) = (3, 5)
  @ getIndexTwoAndFour(Array(2, 4, 6, 8, 10))
  res100: (Int, Int) = (6, 10)
  </> 4.37.scala
  The hierarchy of data types we have seen so far is as follows:
  Iterable
  collection.Set
  Set
  collection.Seq
  collection.Map
  mutable.SetSeqmutable.IndexedSeq
  ListIndexedSeqmutable.Buffer
  Vectormutable.ArrayDeque
  Map
  mutable.Map
  Array
  Depending on what you want your code to be able to accept, you can pick the relevant type in the
  hierarchy: Iterable , IndexedSeq , Seq , collection.Seq , etc. In general, most code defaults to using
  immutable Seq s, Set s and Map s. Mutable collections under the collection.mutable package are only
  used where necessary, and it is best to keep them local within a function or private to a class. collection.
  {Seq,Set,Map} serve as common interfaces to both mutable and immutable collections.
  See example 4.14 - CommonInterfaces
  4.5 Conclusion
  In this chapter, we have gone through the basic collections that underlie the Scala standard library: Array ,
  immutable Vector / Set / Map / List , and mutable ArrayDeque / Set / Map . We have seen how to construct
  collections, query them, convert one to another, and write functions that work with multiple possible
  collection types.
  This chapter should have given you a foundation for competently working with Scala's collections library,
  which is widely used throughout every Scala program. We will now go through some of the more unique
  features of the Scala language, to round off your introduction to Scala.
  Chapter 4 Scala Collections
  77Exercise: Modify the def isValidSudoku method we defined in this chapter to allow testing the
  validity of partially-filled Sudoku grids, with un-filled cells marked by the value 0 .
  @ isValidSudoku(Array(
  @ isValidSudoku(Array(
  Array(3, 1, 6,5, 7, 8,4, 9, 2),Array(3, 1, 6,5, 7, 8,4, 9, 3),
  Array(5, 2, 9,1, 3, 4,7, 6, 8),Array(5, 2, 9,1, 3, 4,7, 6, 8),
  Array(4, 8, 7,6, 2, 9,5, 3, 1),Array(4, 8, 7,6, 2, 9,5, 3, 1),
  Array(2, 6, 3,0, 1, 0,0, 8, 0),Array(2, 6, 3,0, 1, 0,0, 8, 0),
  Array(9, 7, 4,8, 6, 3,0, 0, 5),Array(9, 7, 4,8, 6, 3,0, 0, 5),
  Array(8, 5, 1,0, 9, 0,6, 0, 0),Array(8, 5, 1,0, 9, 0,6, 0, 0),
  Array(1, 3, 0,0, 0, 0,2, 5, 0),Array(1, 3, 0,0, 0, 0,2, 5, 0),
  Array(0, 0, 0,0, 0, 0,0, 7, 4),Array(0, 0, 0,0, 0, 0,0, 7, 4),
  Array(0, 0, 5,2, 0, 6,3, 0, 0)Array(0, 0, 5,2, 0, 6,3, 0, 0)
  ))
  res101: Boolean = true
  )) // top right cell should be 2
  </> 4.38.scala
  res102: Boolean = false
  </> 4.39.scala
  See example 4.15 - PartialValidSudoku
  Chapter 4 Scala Collections
  78Exercise: Write a def renderSudoku method that can be used to pretty-print a Sudoku grid as shown
  below: with the zeroes representing unfilled cells left out, and each 3x3 square surrounded by
  horizontal and vertical lines.
  @ renderSudoku(Array(
  res103: String = """
  Array(3, 1, 6,5, 7, 8,4, 9, 2),+-------+-------+-------+
  Array(5, 2, 9,1, 3, 4,7, 6, 8),| 3 1 6 | 5 7 8 | 4 9 2 |
  Array(4, 8, 7,6, 2, 9,5, 3, 1),| 5 2 9 | 1 3 4 | 7 6 8 |
  Array(2, 6, 3,0, 1, 0,0, 8, 0),+-------+-------+-------+
  Array(9, 7, 4,8, 6, 3,0, 0, 5),| 2 6 3 |
  Array(8, 5, 1,0, 9, 0,6, 0, 0),| 9 7 4 | 8 6 3 |
  | 4 8 7 | 6 2 9 | 5 3 1 |
  | 8 5 1 |
  1
  9
  |
  8
  |
  5 |
  | 6
  |
  Array(1, 3, 0,0, 0, 0,2, 5, 0),+-------+-------+-------+
  Array(0, 0, 0,0, 0, 0,0, 7, 4),| 1 3
  Array(0, 0, 5,2, 0, 6,3, 0, 0)|
  |
  |
  |
  5 | 2
  6 | 3
  ))
  |
  | 2 5
  |
  7 4 |
  |
  +-------+-------+-------+
  </> 4.40.scala
  """
  </> 4.41.output-scala
  You might find the Array.grouped operator useful for this, though you can also do without it:
  @ Array(3, 1, 6,
  5, 7, 8,
  4, 9, 2).grouped(3).toArray
  res104: Array[Array[Int]] = Array(Array(3, 1, 6), Array(5, 7, 8), Array(4, 9, 2))
  </> 4.42.scala
  See example 4.16 - RenderSudoku
  Discuss Chapter 4 online at https://www.handsonscala.com/discuss/4
   */
}
