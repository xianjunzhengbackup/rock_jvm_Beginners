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
/*
我们将会看到下面的结果：
One url: http://blog.toolshed.com 
最后，当我们准备好循环遍历这些 feed 并一个一个地刷新它们的时候，我们可以使用内
置的迭代器 foreach()方法，如下所示：*/
println("Refresh Feeds:") 
feeds1 foreach { feed => println(s" Refreshing $feed...") } 
/*下面是输出结果：
Refresh Feeds: 
 Refreshing blog.toolshed.com... 
  Refreshing pragdave.me... 
   Refreshing blog.agiledeveloper.com... 
   这就是元素的无序集合。接下来，让我们探讨关联映射—Map。
   8.3 关联映射 
   假设我们要将 feed 的作者的名字附加到 feed 上，我们可以将其以键值对的形式存储在
   Map 中。
   val feeds = Map( 
      "Andy Hunt" -> "blog.toolshed.com", 
       "Dave Thomas" -> "pragdave.me", 
        "NFJS" -> "nofluffjuststuff.com/blog") 
   如果想要得到一个 feed 的 Map，其中 feed 的作者名开头都为“D”，那么我们可以使用
   filterKeys()方法。
   val filterNameStartWithD = feeds filterKeys (_ startsWith "D") 
   println(s"# of Filtered: ${filterNameStartWithD.size}") 
   下面是输出结果：
   # of Filtered: 1 
   另外，如果想要对这些值进行筛选，那么除对键进行操作之外，我们还可以使用 filter()
   方法。提供给 filter()方法的函数值接收一个（键，值）元组，我们可以像下面这样使
   用它：
   val filterNameStartWithDAndPragprogInFeed = feeds filter { element => 
      val (key, value) = element 
       (key startsWith "D") && (value contains "pragdave") 
   } 
   print("# of feeds with auth name D* and pragdave in URL: ") 
   println(filterNameStartWithDAndPragprogInFeed.size)
*/
}
