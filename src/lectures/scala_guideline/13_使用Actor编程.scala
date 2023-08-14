object 使用Actor编程_13 extends App{
  println("--------Chapter 12---------")

  /*
  在编写复杂、耗时的应用程序时，我们经常会使用多线程以及并发来降低响应时间或者
  提高性能。可惜，传统的并发解决方案导致了一些问题，如线程安全、竞态条件、死锁、活
  锁以及不容易理解的、容易出错的代码。共享的可变性是罪魁祸首。
  避免共享的可变性，便已经规避了许多问题。但是如何避免呢？这就是 Actor 模型发挥
  作用的地方。Actor 帮助我们将共享的可变性转换为隔离的可变性（isolated mutability）。Actor
  是保证互斥访问的活动对象。没有两个线程会同时处理同一个 Actor。由于这种天然的互斥
  行为，所有存储在 Actor 中的数据都自动是线程安全的—不需要任何显式的同步。①
  如果能将一个任务有意义地分解为几个子任务，即分而治之，就可以使用 Actor 模型来
  解决这个问题，设计良好又清晰，并且避免了通常的并发问题。
  在本章中，我们将选择一个可以从并发中受益的问题，我们将带着这个问题来探索 Actor
  模型，并使用它来解决这个问题。
  13.1 一个顺序耗时问题
  一些应用程序可以受益于多核以及多线程：从多个 Web 服务获取大量的数据、查询股票
  的价格、分析地理数据等。为了不在复杂的领域和冗长代码中迷失自己，让我们先来处理一
  个相对较小的问题，它只需要非常少量的代码。这将帮助我们关注关键问题本身，并探索可
  能的解决方案。
  给定一个目录作为根目录，我们将使用程序来查找该目录下的子目录层次结构中的文件
  数量。下面是该程序的一个顺序实现。
  ① 这里有一个前提是，不要不安全地发布 Actor 的内部状态。——译者注
  import java.io.File
  def getChildren(file: File) = {
  val children = file.listFiles()
  if (children != null) children.toList else List()
  }
  val start = System.nanoTime
  val exploreFrom = new File(args(0))
  var count = 0L
  var filesToVisit = List(exploreFrom)
  while (filesToVisit.nonEmpty) {
  val head = filesToVisit.head
  filesToVisit = filesToVisit.tail
  val children = getChildren(head)
  count = count + children.count { !_.isDirectory }
  filesToVisit = filesToVisit ::: children.filter { _.isDirectory }
  }
  val end = System.nanoTime
  println(s"Number of files found: $count")
  println(s"Time taken: ${(end - start) / 1.0e9} seconds")
  getChildren()函数接受一个 File 作为其参数，如果不是文件夹或者文件夹中没有
  文件，则返回一个空列表，否则返回给定目录下的文件和子目录的列表。不可变变量
  exploreForm 指向作为命令行参数输入的目录名的 File 实例。我们创建了两个可变变量
  （第一感觉就是有问题），分别命名为 count 和 filesToVisit。这两个变量分别初始设置
  为 0 和只包含起始目录的列表。只要在 filesToVisit 列表中还有需要遍历的文件，那么
  while 循环就会继续迭代。在循环内，我们每次从需要遍历的文件列表中选取一个文件来进
  行遍历，（如果是文件夹）就获取该文件夹下的所有子文件，并将发现的文件数目添加到可变
  变量 count 中。我们还会把子文件中的文件夹添加到要访问的文件列表中，以便于进一步遍
  历这些文件夹。
  让我们运行这段代码，并测量其所耗费的时间：
  scala countFilesSequential.scala /Users/venkats/agility
  在运行之前，请将命令行参数替换为你电脑上的一个有效文件夹的完整路径—这里显
  示的示例使用了作者电脑中的 agility 目录。
  让我们运行这段代码。运行可能需要花费一段时间，取决于由命令行参数提供的起始目
  录下的文件数以及嵌套级别：
  异步社区会员 雄鹰1(13027310973) 专享 尊重版权
  178·第 13 章 使用 Actor 编程
  Number of files found: 479758
  Time taken: 66.524453436 seconds
  该程序报告了其所发现的文件数量，得到这个结果耗时超过 66 s。这真的是太慢了。如
  果这是一个 Web 或者移动应用程序，那么用户可能已经点击了无数次刷新按钮了，并且早已
  消失得无影无踪了。我们需要让它变快—应该是快得多。
  13.2 曲折的并发之路
  快速查看我们的系统上的活动监视器表明，当其中处理器一个核心异常忙碌时，其他核
  心可能正坐在一旁喝茶呢。因为该程序是 I/O 密集型的，所以如果我们将更多的线程用于该
  问题，从而利用其他核心，那么我们便可以获得更好的性能。
  将你的 Java 帽子戴上一分钟，然后想一想如何将这段代码变得更快。只想到要使用 JDK
  的并发库就非常伤脑筋了。启动多个线程并不是真正的困难之处，只是比较笨拙而已—你
  将会使用 Executors 来创建一个线程池。你可以将探索不同子文件夹的任务调度给线程池
  中的不同线程。但是，问题的根源在于那两个变量—共享的可变变量。当多个线程访问各
  个子目录时，我们不得不更新 count 和 filesToVisit 这两个变量。让我们看一下这为何
  会是一个问题。
  • 为了保护 count 变量不受并发更改的影响，我们可能使用 AtomicLong。这是有问
  题的，因为我们必须要保证对该变量的所有更改发生在该程序看到没有更多的文件需
  要访问并报告文件总数之前。换句话说，虽然原子性保证了单个值的线程安全性，但
  是其并不能保证跨多个值的原子性，因为这些值可能会同时发生变化。
  • 我们可能不得不使用一个线程安全的集合—同步列表或者并发列表，用来实现
  filesToVisit 列表。这也只能保护一个变量的原子性，但是并不能解决跨两个变
  量的原子性问题。
  • 我们可以将这两个变量封装到同一个类中，并提供 synchronized 方法来一次性
  地更新这两个值。这将确保对这两个变量的更改是原子的。然而，现在我们不得
  不确保这个同步操作实际发生在正确的位置、正确的时间上。如果我们忘记了同
  步，或者在错误的位置上进行了同步，那么 Java 编译器和运行时都不会给我们任
  何的警告。
  简而言之，将代码从顺序执行改为并发执行通常都会将代码变成“野兽”。可变的变量越
  多，它就变得越芜杂，也就越难证明代码的正确性。编写运行飞快但会产生不可预知的错误
  的代码是毫无意义的。
  手上的这个问题正是应用 Actor 模型的最佳候选。我们可以使用分而治之的方法，从
  而将问题拆解为多个子任务。可变变量将可以被隐藏在一个 Actor 中，从而防止多个线程
  异步社区会员 雄鹰1(13027310973) 专享 尊重版权
  13.3 创建 Actor·179
  并发地更新它们。①我们可以对更改请求进行排队，而不是让线程阻塞并相互等待。我们将
  很快使用 Actor 来实现这个程序，但是先让我们通过几个例子来学习一下 Actor，以及如何
  使用它们。
  13.3 创建 Actor
  通常都会创建一个对象，然后调用其方法。一个 Actor 也是一个对象，但是你从来都不
  会直接调用它的方法，而是通过发送消息，并且每个 Actor 都由一个消息队列支撑。如果一
  个 Actor 正忙于处理消息，那么到达的消息将会被插入消息队列中，而不会阻塞消息的发送
  者；它们发送并忘记（fire-and-forget）。在任意给定的时间，一个 Actor 将只会处理一条消息。
  Actor 模型具有与生俱来的线程安全性。
  让我们定义一个 Actor。
  ProgrammingActors/HollywoodActor.scala
  import akka.actor._
  class HollywoodActor() extends Actor {
  def receive: Receive = {
  case message => println(s"playing the role of $message")
  }
  }
  Scala 使用来自 Akka 的 Actor 模型支持—一个使用 Scala 编写的非常强大的反应式库。
  要创建一个 Actor，需要继承 Actor 特质 ② 并实现 receive()方法。receive()方法的主
  体部分看起来非常熟悉，它是去掉了 match 关键字的模式匹配语法。该匹配发生在一个隐式
  的消息对象上。该方法的主体是一个偏函数。
  在这个例子中，我们只简单地打印了接收到的消息，我们将很快为该 Actor 添加更多的
  逻辑。让我们使用刚刚定义的 Actor。
  ProgrammingActors/CreateActors.scala
  import akka.actor._
  import scala.concurrent.Await
  import scala.concurrent.duration.Duration
  object CreateActors extends App {
  val system = ActorSystem("sample")
  ① 一个 Actor 同时只会被一个线程调度运行。——译者注
  ② 如果使用 Java 8，那么建议继承抽象类 AbstractActor。——译者注
  异步社区会员 雄鹰1(13027310973) 专享 尊重版权
  180·第 13 章 使用 Actor 编程
  val depp = system.actorOf(Props[HollywoodActor])
  depp ! "Wonka"
  val terminateFuture = system.terminate()
  Await.ready(terminateFuture, Duration.Inf)
  }
  Akka 的 Actor 托管在一个 ActorSystem 中，它管理了线程、消息队列以及 Actor 的生命
  周期。相对于使用传统的 new 关键字来创建实例，我们使用了一种特殊的 actorOf 工厂方法
  来创建 Actor，并将其对应的 ActorRef 赋值给了名为 depp 的引用。此外，我们也没有使用
  传统的方法调用语法，而是发送了一个“Wonka”消息给 Actor—在这个例子中只传递了一
  个字符串—我们使用了名为!的方法，你可以使用一个名为 tell()的方法① ，而不是使用!()
  方法，但是那样就需要传递一个额外的 sender 参数。同时，如果你使用的方法名对阅读者来
  说是直观的，那么你的代码也就太简单了。说到直觉，它们真应该被称为 action()。
  Actor System 管理了一个线程池，只要系统保持活跃，这个线程池就会一直保持活跃。
  如果要使该程序在 main 代码块执行完成之后关闭，就必须要调用该 ActorSystem 的
  terminate()方法②，也就是说，退出它的线程。
  要编译这段代码，应输入下面的命令③：
  scalac -d classes HollywoodActor.scala CreateActors.scala
  因为 Scala 的安装中已经包含了 Akka 的 Actor 库④ ，所以要编译这段代码，我们不需要
  在 classpath 中包含任何其他内容，同样，要运行它，我们也不需要包含任何附加的库。
  下面是命令：
  scala -classpath classes CreateActors
  让我们看一下输出结果：
  playing the role of Wonka
  虽然我们并没有编写很多代码，但这依然是一个乏善可陈的输出结果。在代码中有许多内容，
  可是这些细节在输出结果中却完全丢失了。让我们更改一下这段代码，以获得更好的洞察。
  我们来修改一下 receive()方法：
  ① 当我们使用 Java API 的时候将会用到这个方法。——译者注
  ② 在旧版本中，有一个叫 shutdown()的阻塞方法，在本书中文版翻译的时候，我们全部更新到了最新的 API，即使用
  了 terminate()方法，不同的是，terminate()是异步的。——译者注
  ③ 这里专门改成了新版本的 API，同上面的代码。——译者注
  ④ 在现在的 Scala 版本中，已经不默认包含 Akka 的分发包了，可以通过 IDE、SBT 等来运行。——译者注
  异步社区会员 雄鹰1(13027310973) 专享 尊重版权
  13.4 Actor 和线程·181
  case message => println(s"$message - ${Thread.currentThread}")
  当接收到消息时，我们把执行线程的详细信息一起打印出来。让我们更改一下对应的调
  用代码，以便向多个 Actor 发送多条消息：
  val depp = system.actorOf(Props[HollywoodActor])
  val hanks = system.actorOf(Props[HollywoodActor])
  depp ! "Wonka"
  hanks ! "Gump"
  depp ! "Sparrow"
  hanks ! "Phillips"
  println(s"Calling from ${Thread.currentThread}")
  这将为我们提供一些更加有趣的细节。让我们运行这段代码并查看输出结果：
  Wonka - Thread[sample-akka.actor.default-dispatcher-2,5,main]
  Gump - Thread[sample-akka.actor.default-dispatcher-3,5,main]
  Calling from Thread[main,5,main]
  Phillips - Thread[sample-akka.actor.default-dispatcher-3,5,main]
  Sparrow - Thread[sample-akka.actor.default-dispatcher-2,5,main]
  我们给每个 Actor 都发送了两条消息：给 Actor depp 发送了“Wanka”和“Sparrow”，
  给 Actor hanks 发送了“Gump”和“Phillips”。这个输出结果展示了许多有趣的细节。
  • 一个可用的线程池，不必大惊小怪。
  • Actor 在不同的线程中运行，而不是调用代码的主线程。
  • 每个 Actor 一次只处理一条消息。
  • 多个 Actor 并发地运行，同时处理多条消息。
  • Actor 是异步的。
  • 不会阻塞调用者—main 方法（直接）运行了 println()方法，根本不会等待这
  些 Actor 的回复。
  虽然还有许多的领域需要覆盖，但是你已经可以看出这种方式的好处。这些好处来自我
  们根本没做的事情。我们并没有显式地创建一个线程池，也没有显式地调度任务。如果我们
  使用的是 JDK 的并发库，那么我们应该已经在使用 Executors，并且调用了类似 submit()
  之类的方法了—这为我们节省了编写大量代码的时间。相反，我们只是向 Actor 发送了一
  条消息，而 ActorSystem 则负责了所有剩下的事情。很酷，不是吗？
  13.4 Actor 和线程
  前面的例子很好地说明了使用 Actor 时的情况，但是同时也提出了一个问题。在前面的
  输出结果中我们看到，发送给 depp 的两条消息都是由同一个线程处理的，而发送给 hanks
  异步社区会员 雄鹰1(13027310973) 专享 尊重版权
  182·第 13 章 使用 Actor 编程
  的两条消息则都是由另一个线程处理的。这可能会给人们留下一个印象：Actor 将会持有它
  们自己的线程，但是这不是真的—事实上，在你的计算机上，你甚至可能会观察到 Actor
  切换线程的情况。
  线程之于 Actor 类似于客服经理之于消费者。当你拨打客户服务热线时，任何有空的客
  服经理都会接听你的热线。如果你挂掉之前的电话并重新拨通热线，此时上一位客服经理已
  经在处理别的客服电话了，那么另一位完全随机的客服经理现在将会回答你的疑问。只有在
  极端巧合下（在这两次热线电话的过程中），你才可能和同一位客户经理谈话。线程池中的线
  程对于 Actor 来说非常像客服经理。为了观察到这一点，我们稍微修改一下调用代码：
  depp ! "Wonka"
  hanks ! "Gump"
  Thread.sleep(100)
  depp ! "Sparrow"
  hanks ! "Phillips"
  在发送给 Actor 的两组消息之间，我们添加了一个小小的 100 ms 的延迟。让我们来看一
  下运行这段代码的输出结果：
  Wonka - Thread[sample-akka.actor.default-dispatcher-3,5,main]
  Gump - Thread[sample-akka.actor.default-dispatcher-4,5,main]
  Sparrow - Thread[sample-akka.actor.default-dispatcher-4,5,main]
  Phillips - Thread[sample-akka.actor.default-dispatcher-3,5,main]
  Calling from Thread[main,5,main]
  一旦两个线程帮助 Actor 处理了它们的第一组消息，它们便跑回 CPU 水冷器旁边休息去
  了，以便追上每日的八卦。但当下一组消息到达时，尽职尽责的线程便又会马上回归到它们
  的工作任务中。它们对曾经服务过的线程并没有什么亲和力。 ① 这也是在短暂的延迟之后，
  线程交换了它们所服务的 Actor 的原因。这是纯粹的试探法：每次运行代码时，你都可能会
  看到不同的线程与 Actor 之间的配对。实质上，这也表明了线程并不和 Actor 绑定—一个
  线程池服务于多个 Actor。
  Akka 提供了大量的工具来配置线程池的大小、消息队列的大小以及许多其他参数，包括
  与远程 Actor 进行交互。
  13.5 隔离可变性
  在可以将 Actor 模型应用到我们的文件遍历问题之前，最后还需要解决一个问题—共
  ① 在 Akka 2.5.x 版本中，已经添加了对 Actor 的线程亲和力支持，如有需要请使用 AffinityPool。如果需要将 Actor 绑定
  到某个线程，也可以使用 PinnedDispatcher。——译者注
  异步社区会员 雄鹰1(13027310973) 专享 尊重版权
  190·第 13 章 使用 Actor 编程
  保证我们不会在无意间修改共享状态，并最终导致并发问题。
  • 尽量避免使用 ask()。双向通信通常都不是一个好主意。“发送并忘记”模型要好得
  多，而且也更加不容易出错。
  13.8 小结
  在本章中，我们就诸如并发这样的复杂主题取得了一些不错的进展，学习了 Actor 模型
  和这种模型所解决的问题，以及如何创建和使用 Actor，还学习了线程池如何和 Actor 一起工
  作，如何在 Actor 之间进行通信，以及最重要的，隔离的可变性是如何拯救并发的复杂性的。
  我们还应用这些概念实现了一个实际的例子，这个例子表明，只用少量代码就能获得可观的
  速度改进。在接下来几章中，我们将学习如何综合应用在本书中学到的 Scala 的各种概念
   */

}
