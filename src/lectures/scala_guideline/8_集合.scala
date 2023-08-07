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
   既然已经学习了 Set 和 Map，那么我们就不能再忽略最常见的集合—List。
   8.4 不可变列表 
   通过使用 head 方法，Scala 使访问一个列表的第一个元素更加简单快速。使用 tail 方
   法，可以访问除第一个元素之外的所有元素。访问列表中的最后一个元素需要对列表进行遍
   历，因此相比访问列表的头部和尾部①，该操作更加昂贵。所以，列表上的大多数操作都是
   围绕着对头部和尾部的操作构造的。
   让我们继续使用上面的 feed 例子来学习 List。我们可以使用 List 来维护一个有序的
   feed 集合。
   val feeds = List("blog.toolshed.com", "pragdave.me", "blog.agiledeveloper.com") 
   这创建了一个 List[String]的实例。我们可以使用从 0 到 list.length - 1 的索
   引来访问 List 中的元素。当调用 feeds(1)方法时，我们使用的是 List 的 apply()方法。
   也就是说，feeds(0)是 feeds.apply(0)的一个简单形式。要访问第一个元素，我们可以
   使用 feeds(0)或者 head()方法。
   println(s"First feed: ${feeds.head}") 
   println(s"Second feed: ${feeds(1)}") 
   这段代码的输出结果如下：
   First feed: blog.toolshed.com
Second feed: pragdave.me
如果我们想要前插一个元素，即将一个元素放在当前 List 的前面，我们可以使用特殊
的::()方法。a :: list 读作“将 a 前插到 list”。虽然 list 跟在这个操作符之后，但
它是 list 上的一个方法。8.5 节会详细介绍其工作原理。
val prefixedList = "forums.pragprog.com/forums/87" :: feeds
println(s"First Feed In Prefixed: ${prefixedList.head}")
上述代码的输出结果如下：
First Feed In Prefixed: forums.pragprog.com/forums/87
假设我们想要追加一个列表到另外一个列表，例如，将 listA 追加到另外一个列表
list。那么我们可以使用:::()方法将 list 实际上前插到 listA。因此，代码应该是
list ::: listA，并读作“将 list 前插到 listA”。因为 List 是不可变的，所以我们
不会影响前面的任何一个列表。我们只是使用这两个列表中的元素创建了一个新列表。 ① 下
面是一个追加的例子：
val feedsWithForums =
feeds ::: List(
"forums.pragprog.com/forums/87",
"forums.pragprog.com/forums/246")
println(s"First feed in feeds with forum: ${feedsWithForums.head}")
println(s"Last feed in feeds with forum: ${feedsWithForums.last}")
下面是输出结果：
First feed in feeds with forum: blog.toolshed.com
Last feed in feeds with forum: forums.pragprog.com/forums/246
同样地，:::()方法是在操作符后面的列表上调用的。
要将一个元素追加到列表中，可以使用相同的:::()方法。将想要追加的元素添加到一
个列表中，然后将原始列表拼接到它的前面：
val appendedList = feeds ::: List("agilelearner.com")
println(s"Last Feed In Appended: ${appendedList.last}")
我们应该能看到下面这样的输出：
Last Feed In Appended: agilelearner.com
需要注意的是，将元素或者列表追加到另外一个列表中，实际上调用的是后者的前缀方
法。这样做的原因是，与遍历到列表的最后一个元素相比，访问列表的头部元素要快得多。
事半功倍。
① 实际上，这个新列表将会共享整个 listA。——译者注
异步社区会员 雄鹰1(13027310973) 专享 尊重版权
8.4 不可变列表·123
如果想要只选择满足某些条件的 feed，应该使用 filter()方法。如果我们想要检查是
否所有的 feed 都满足某个特定的条件，则可以使用 forall()方法。另外，如果我们想要知
道是否有任意 feed 满足某一条件，那么 exists()方法可以帮到我们。
println(s"Feeds with blog: ${feeds.filter(_ contains "blog").mkString(", ")}")
println(s"All feeds have com: ${feeds.forall(_ contains "com")}")
println(s"All feeds have dave: ${feeds.forall(_ contains "dave")}")
println(s"Any feed has dave: ${feeds.exists(_ contains "dave")}")
println(s"Any feed has bill: ${feeds.exists(_ contains "bill")}")
我们将得到下面这样的结果：
Feeds with blog: blog.toolshed.com, blog.agiledeveloper.com
All feeds have com: false
All feeds have dave: false
Any feed has dave: true
Any feed has bill: false
如果想要知道我们需要显示的每个 feed 名称的字符数，那么我们可以使用 map()方法来
处理每个元素，并获得一个结果列表，如下所示：
println(s"Feed url lengths: ${feeds.map(_.length).mkString(", ")}")
下面是输出结果：
Feed url lengths: 17, 11, 23
如果我们对所有 feed 的字符总数感兴趣，那么我们可以使用 foldLeft()方法，如下
所示：
val total = feeds.foldLeft(0) { (total, feed) => total + feed.length }
println(s"Total length of feed urls: $total")
上述代码的输出结果如下：
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
异步社区会员 雄鹰1(13027310973) 专享 尊重版权
124·第 8 章 集合
为了使前面的方法更加简洁，Scala 提供了替代方法。/:()方法等价于 foldLeft()
方法，而\:()方法等价 于 foldRight()方法 。下面我们 使用/:()方法重写前 面的
例子：
val total2 = (0 /: feeds) { (total, feed) => total + feed.length }
println(s"Total length of feed urls: $total2")
上述代码的输出结果如下：
Total length of feed urls: 51
程序员们要么喜欢这样的简洁性，比如我，要么讨厌它；我不觉得有“骑墙派”。
现在我们可以使用 Scala 的多项约定，让代码甚至可以像下面这样更加简洁：
val total3 = (0 /: feeds) { _ + _.length }
println(s"Total length of feed urls: $total3")
下面是输出结果：
Total length of feed urls: 51
在本节中，我们看到了 List 的一些有趣方法。List 中还有其他一些方法，提供了额
外的能力。
这些方法名中的冒号在 Scala 中具有重大的意义，理解它是非常重要的。接下来，让我
们一起来探讨一下吧。
8.5 方法名约定
本节中介绍的功能非常酷（我真的是这样认为的），但是理解起来也有一点儿难。如果你
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
① 这里是一种打趣的说法，类似的用语在坐飞机的时候，机上广播一定会说到的。——译者注
异步社区会员 雄鹰1(13027310973) 专享 尊重版权
8.5 方法名约定·125
有些程序员会问，是否可以在调用过程中将冒号附加到现有的方法上。① 答案是不可以，因
为 Scala 并没有提供用于装饰现有方法名称的设施。该约定仅用于以此特殊符号结束的方法名。
如果方法名以冒号（:）结尾，那么调用的目标是该操作符后面的实例。Scala 不允许使用字
母作为操作符的名称，除非使用下划线对该操作符增加前缀。因此，一个名为 jumpOver:()
的方法是被拒绝的，但是 jumpOver_:()则会被接受。
在下面这个例子中，^()方法是一个定义在 Cow 类上的方法，而^:()方法是独立定义在
Moon 类上的一个方法。
UsingCollections/Colon.scala
class Cow {
def ^(moon: Moon): Unit = println("Cow jumped over the moon")
}
class Moon {
def ^:(cow: Cow): Unit = println("This cow jumped over the moon too")
}
下面是使用这两个方法的一个示例。
UsingCollections/Colon.scala
val cow = new Cow
val moon = new Moon
cow ^ moon
cow ^: moon
对这两个方法的调用看起来几乎是完全一样的，cow 都在操作符的左边，而 moon 都在
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
① 原作者的意思是将 a.fun(b)这种调用通过添加冒号的方式变成 a.fun:(b)，也就是 b fun: a。——译者注
异步社区会员 雄鹰1(13027310973) 专享 尊重版权
126·第 8 章 集合
UsingCollections/Unary.scala
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
上述代码的输出结果如下：
Called unary +
called unary -
called unary !
called unary ~
在熟悉了 Scala 之后，你便会长出一只 Scala 之眼—很快，处理这些符号和约定将会变
成你的第二天性。
8.6 for 表达式
foreach()方法提供了集合上的内部迭代器—你不必控制循环，只需要提供在每次迭
代上下文中执行的代码片段即可。但是，如果希望同时控制循环或者处理多个集合，那么你
便可以使用外部迭代器，即 for 表达式。我们来看一个简单的循环。
UsingCollections/PowerOfFor.scala
for (_ <- 1 to 3) { print("ho ") }
这段代码打印出了“ho ho ho”。它是下面表达式的一般语法的简洁形式：
for([pattern <- generator; definition*]+; filter*)
[yield] expression
for 表达式接受一个或者多个生成器作为参数，并带有 0 个或者多个定义以及 0 个或者
多个过滤器。这些都是由分号分隔的。yield 关键字是可选的，如果存在，则告诉表达式返
回一个值列表而不是一个 Unit。虽然有大量的细节，不过不必担心，因为我们将会用例子
来说明，所以你很快就会适应它了。
让我们先从 yield 关键字开始。假设我们想要获取一个区间内的值，并将每个值都乘以
2。下面是这样做的一个代码示例。
异步社区会员 雄鹰1(13027310973) 专享 尊重版权
8.6 for 表达式·127
UsingCollections/PowerOfFor.scala
val result = for (i <- 1 to 10)
yield i * 2
上面的代码返回了一个值的集合，其中每个值分别是给定区间 1 到 10 中的每个值的两倍大小。
我们还可以使用 map()方法来完成前面的逻辑，像下面这样。
UsingCollections/PowerOfFor.scala
val result2 = (1 to 10).map(_ * 2)
在幕后，Scala 将根据表达式的复杂程度，把 for 表达式翻译为组合使用了类似 map()
和 withFilter()这样的方法的表达式。
现在，假设我们只想将区间内的偶数进行加倍，那么我们可以使用过滤器。
UsingCollections/PowerOfFor.scala
val doubleEven = for (i <- 1 to 10; if i % 2 == 0)
yield i * 2
前面的 for 表达式读作“返回一个 i * 2 的集合，其中 i 是一个给定区间的成员，且
i 是偶数”。因此，上面的表达式实际上就像是对一个值的集合进行 SQL 查询—这在函数
式编程中称为列表推导（list comprehension）。
如果觉得上述代码中的分号碍眼，也可以将它们替换成换行符，然后使用大括号，而不
是括号，就像下面这样：
for {
i <- 1 to 10
if i % 2 == 0
} yield i * 2
可以将定义和生成器放在一起。Scala 在每次迭代的过程中都会定义一个新的 val 值。
在下面这个例子中，我们循环遍历一个 Person 的集合，并打印出其姓氏。
UsingCollections/Friends.scala
class Person(val firstName: String, val lastName: String)
object Person {
def apply(firstName: String, lastName: String): Person =
new Person(firstName, lastName)
}
val friends = List(Person("Brian", "Sletten"), Person("Neal", "Ford"),
Person("Scott", "Davis"), Person("Stuart", "Halloway"))
val lastNames =
for (friend <- friends; lastName = friend.lastName) yield lastName
异步社区会员 雄鹰1(13027310973) 专享 尊重版权
128·第 8 章 集合
println(lastNames.mkString(", "))
这段代码的输出如下：
Sletten, Ford, Davis, Halloway
上面的代码也是 Scala 语法糖的一个例子，我们在新建一个 Person 的列表时实际上在
幕后调用的是 apply()方法—这样的代码简洁且易读。
如果在 for 表达式中提供了多个生成器，那么每个生成器都将形成一个内部循环。最右
边的生成器控制最里面的循环。下面是使用了两个生成器的例子。
UsingCollections/MultipleLoop.scala
for (i <- 1 to 3; j <- 4 to 6) {
print(s"[$i,$j] ")
}
上述代码的输出结果如下：
[1,4] [1,5] [1,6] [2,4] [2,5] [2,6] [3,4] [3,5] [3,6]
使用多个生成器，可以轻松地将这些值组合起来，以创建强大的组合
*/
}
