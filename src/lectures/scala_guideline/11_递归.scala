object 递归_11 extends App{

  /*
   使用解决子问题的方案解决一个问题，也就是递归，这种想法十分诱人。许多算法和
   问题本质上都是递归的。一旦我们找到窍门，使用递归来设计解决方案就变得极富表现力
   且直观。
   一般来说，递归最大的问题是大规模的输入值会造成栈溢出。但幸运的是，在 Scala 中可以
   使用特殊构造的递归来规避这个问题。在本章中，我们将分别探讨强大的尾调用优化（tail call 
   optimization）技术以及Scala 标准库中的相关支持类。使用这些易于访问的工具，就可以在高度递归
   的算法实现中既可以处理大规模的输入值又能同时规避栈溢出（即触发 StackOverflowError）
   的风险。
   */
  println("----------11.1 一个简单的递归--------")
  /*
   递归在许多算法中广泛使用，如快速排序、动态规划、基于栈的操作等。递归极富表现
   力而且直观。有时候我们使用递归来避免可变（状态）。让我们看一个使用递归的场景。为了
   专注于递归问题本身，又不陷入问题或者领域本身的复杂性中，这里做了一些简化。*/ 
   def factorial(number: Int): BigInt = { 
     if (number == 0) 
      1 
     else 
      number * factorial(number - 1) 
    } 
   /*factorial()函数接收一个参数，如果参数值为 0，则返回值为 1；否则，它返回参数
   值与参数值减 1 的阶乘的乘积。
   在 Scala 中编写递归函数和编写其他函数唯一的区别在于：递归函数的返回值类型必须
   显式指定。这样设计的原因在于，因为函数至少在一条执行路径中调用自己，而 Scala 不想
   承担推导返回类型的负担。
   让我们用一个相对较小的输入值来运行 factorial()函数：*/
   println(factorial(5)) 
   /*这一调用会很快运行并产生期望的结果，表明 Scala 能很好地处理递归调用：
   120 
   仔细看看 factorial()函数中第 5 行的代码，最后一个操作是乘法（*）。在每次通过
   函数调用时，number 参数的值都会在栈中暂存，并等待接下来的调用结果。如果输入参数
   是 5，在递归结束前，调用最深可至 6 层。
   栈是有限的资源且无法无限增长。对于很大的输入值，简单的递归很快就会遇到麻烦。
   例如，尝试用一个比较大的值调用 factorial()函数，像这样。
   println(factorial(10000)) 
   这次调用注定会抛出：
   java.lang.StackOverflowError 
   概念上虽然强大而优雅，却无法胜任一些实际的需求，这种糟糕的命运可悲可叹。
   大多数支持递归的编程语言都在递归的使用上有限制。可喜的是，也有一些编程语言，
   如 Scala，提供了一些好用的特性规避这些问题，详见 11.2 节。*/
   println("-----------11.2 尾调用优化（TCO）---------") 
   /*虽然很多编程语言支持递归，但只有一些编译器在递归调用上做了进一步的优化。常见
   的方法是，将递归转化成迭代以避免栈溢出的问题。
   迭代不会遭受在递归中易出现的栈溢出问题，但却没有足够的表现力。经过优化后，我
   们可以写出极富表现力且直观的代码，让编译器在运行前将递归转化为更安全的迭代（可以
   参考 Abelson 和 Sussman 的 Structure and Interpretation of Computer Programs①[AS96]一书）。
   然而，并不是所有的递归都能够转化为迭代。只有具有特殊结构的递归—尾递归，才能享
   受这种特权。让我们深入探索这种特性。
   在 factorial()函数中，第 5 行上的最后一次调用是乘法。在尾递归中，最终的调用
   应该是调用这个函数本身。在那种情况下，这个函数调用被说成在尾部。我们将用尾递归重
   写 factorial()函数，但让我们先用另外一个例子来说明这样做的好处。*/
   println("------------11.2.1 常规递归并无优化----------") 
   /*Scala 并不会对常规递归进行优化，只会优化尾递归。让我们用一个例子来看一下这种差别。
   在下一个例子中，mad()函数在参数为 0 时会抛出一个异常。注意，递归的最后一个操
   作是乘法。 
   def mad(parameter: Int): Int = { 
      if (parameter == 0) 
         throw new RuntimeException("Error") 
        else 
           1 * mad(parameter - 1) 
   } 
   mad(5) */
   /*从这段代码的运行结果中摘录部分如下：
   java.lang.RuntimeException: Error 
    at Main$$anon$1.mad(mad.scala:3) 
     at Main$$anon$1.mad(mad.scala:5) 
      at Main$$anon$1.mad(mad.scala:5) 
       at Main$$anon$1.mad(mad.scala:5) 
        at Main$$anon$1.mad(mad.scala:5) 
         at Main$$anon$1.mad(mad.scala:5) 
          at Main$$anon$1.<init>(mad.scala:8) 
          所摘录的栈跟踪表明在异常抛出之前，我们调用了 6 次 mad()函数。这正是我们所期望
          的常规递归调用的方式。*/
         println("------------11.2.2 用尾调用优化来拯救-----------") 
          /*并不是所有支持递归的编程语言都支持尾调用优化。例如，Java 就不支持尾调用优化，所有
          的递归，不管是不是尾部调用，都注定会在输入大值时栈溢出。Scala 则很容易支持尾调用优化。
          我们改造一下 mad()函数，去除多余的乘 1 操作。这将使调用在尾部递归—对函数的
          调用在最后，即在尾部。
          def mad(parameter: Int): Int = { 
             if (parameter == 0) 
                throw new RuntimeException("Error") 
               else 
                  mad(parameter - 1) 
          } 
          mad(5)
  让我们看一下这个修改版的输出结果：
  java.lang.RuntimeException: Error 
   at Main$$anon$1.mad(mad2.scala:3) 
    at Main$$anon$1.<init>(mad2.scala:8) 
    两个版本中调用 mad()函数的次数是一样的。然而，修改版的栈跟踪表明，在抛出异常
    时，深度只有 1 层，而不是 6 层。这是因为 Scala 的尾递归优化做了一些改善工作。
    可以用 scala 命令的-save 选项拿到优化细节的第一手资料，像这样：scala -save 
    mad.scala。这会将字节码保存到一个名为 mad.jar 的文件中，然后运行 jar xf mad.jar
    以及 javap -e -private Main\$\$anon\$1.class，就可以展示 Scala 编译器生成的
    字节码。
    我们先看一下为作为常规递归编写的 mad()函数生成的字节码。
     private int mad(int); 
      Code: 
       0: iload_1 
        1: iconst_0 
         2: if_icmpne 15 
          5: new #14 // 类
          java/lang/RuntimeException 
           8: dup 
            9: ldc #16 // 字符串 Error 
             11: invokespecial #20 // 方法 
             java/lang/RuntimeException."<init>":(Ljava/lang/String;)V 
              14: athrow 
               15: iconst_1 
                16: aload_0 
                 17: iload_1 
                  18: iconst_1 
                   19: isub 
                    20: invokespecial #22 // 方法 mad:(I)I 
                     23: imul 
                      24: ireturn 
                      在 mad()方法的末尾，标记为 20 行的地方，有个名为 invokeSpecial 的字节码，表
                      明该调用是递归的。现在我们修改代码，使其变成尾递归，然后再看一下生成的字节码。
                       private int mad(int); 
                        Code: 
                         0: iload_1 
                          1: iconst_0 
                           2: if_icmpne 15 
                            5: new #14 // 类
                            java/lang/RuntimeException 
                             8: dup 
                              9: ldc #16 // 字符串 Error
  11: invokespecial #20 // 方法 
  java/lang/RuntimeException."<init>":(Ljava/lang/String;)V 
   14: athrow 
    15: iload_1 
     16: iconst_1 
      17: isub 
       18: istore_1 
        19: goto 0 
        我们看到的已经不是 invokeSpecial 而是 goto，goto 是一个简单的跳转，表明是
        简单的迭代而不是递归方法调用。从我们（使用）的角度看，这种机智的优化无须耗费太多
        精力。*/
        println("-----------11.2.3 确保尾调用优化------------") 
        /*编译器会将尾递归自动转化成迭代。这种隐性优化非常好，但也让人略感不安—没有
        直接可见的反馈可供辨别。为了推断是否是尾递归，我们需要检查字节码或者检查大的输入
        值是否会导致代码运行失败。这样做太麻烦了。
        还好 Scala 提供了一个注解，辅助尾递归的编写。可以用 tailrec 注解标记任何函数，
        Scala 会在编译时检查函数是否是尾递归的。如果不是，那么函数不能被优化，编译器会严格
        地报错。
        为了检查这个注解是否生效，可以在 factorial()函数上标注，像下面这样：
        @scala.annotation.tailrec 
        def factorial(number: Int): BigInt = { 
           if (number == 0) 
              1 
             else 
                number * factorial(number - 1) 
        } 
        println(factorial(10000)) 
        因为这个版本的 factorial()函数是常规递归，而不是尾递归，因此编译器会报一个
        恰当的错误：
        error: could not optimize @tailrec annotated method factorial: it contains 
        a recursive call not in tail position 
         number * factorial(number - 1) 
          ^ 
          error found 
          将一个常规递归改写成尾递归并不难。我们可以做预计算，将部分结果放置在参数中，
          而不是在递归调用方法返回的时候做乘法操作。下面是重构之后的代码：*/
          @scala.annotation.tailrec
          def factorial(fact: BigInt, number: Int): BigInt = { 
             if (number == 0) 
                fact 
               else 
                  factorial(fact * number, number - 1) 
          } 
          //println(factorial(1, 10000))
          /*
          factorial(1,10000) => factorial(1*10000,9999)=>factorial(1*10000*9999,9998)
            ....=>factorial(10000*9999*...*2 ,1)=>factorial(10000*9999*...*2*1,0)=>
              10000*9999*.....*2*1
           */
          /*修改后的 factorial()函数接收两个参数，其中第一个参数 fact 是已经计算出来的
          部分结果。对 factorial()函数的递归调用发生在尾部，符合函数头部的注解。在做了这
          样的更改之后，Scala 就不会报错，而会在调用中做优化。
          下面是这个版本的函数的运行结果：
          284625968091705451890641321211986889014805140170279923079417999427441134000 
          ... 
          只要是尾递归，Scala 都会做尾调用优化。注解是可选的，使用之后明晰了优化的意图。
          使用注解是一个好方法。它能够在代码重构中保证函数尾递归的性质，并让以后重构这段代
          码的程序员注意到这个细节。*/
          println("---------11.3 蹦床调用------------") 
          /*尽管 Scala 中的尾调用优化非常强大，但也有诸多限制。编译器只能够检测到直接的递
          归，也就是说函数调用自己。如果两个函数相互调用，也就是蹦床调用（trampoline call），
          那么 Scala 就无法检测到这种递归，也不会做优化。
          尽管 Scala 编译器并不支持蹦床调用的优化，但是我们可以用 TailRec 类来避免栈溢出
          的问题。
          我们先来看一个在比较大的输入值下会栈溢出的蹦床调用例子。*/
          import scala.io.Source._ 
          def explore(count: Int, words: List[String]): Int = 
             if (words.isEmpty) 
                count 
             else 
                countPalindrome(count, words)

          def countPalindrome(count: Int, words: List[String]): Int = { 
              val firstWord = words.head 
              if (firstWord.reverse == firstWord)
                explore(count + 1, words.tail) 
              else 
                explore(count, words.tail) 
          } 
          def callExplore(text: String): Unit = println(explore(0, text.split(" ").toList)) 
          callExplore("dad mom and racecar") 
          try { 
                val text = fromURL("https://en.wikipedia.org/wiki/Gettysburg_Address").mkString 
                callExplore(text) 
              } catch { 
                       case ex: Throwable => println(ex) 
                    } 
  /*explore()函数将部分结果 count 和单词列表作为参数。如果列表是空的，那么直接
  返回 count 的值；否则，会调用 countPalindrome()方法。countPalindrome()方法
  会依次检查列表中的第一个单词是否回文。如果是，则调用 explore()方法，其参数 count
  的值加 1；否则，就调用 explore()方法，参数 count 的值不变。在这两种情况下，传递
  给 explore()函数的列表都会将第一个元素移除。
  callExplore()函数将一串文本作为输入，以空格为分隔符分隔成单词数组，并将数
  组转化为列表，然后传递给 explore()函数，并最终输出计算结果。
  我们调用 callExplore()两次，第一次输入的是很短的字符串，第二次用从网络获取
  的大块文本作为参数。我们来看一下代码的执行结果：
      3 
      java.lang.StackOverflowError 
  对于短字符串，这段代码正确地识别出了回文字符串的数量。然而，对于长文本，它会
  陷入困难。
  用@scala.annotation.tailrec 去标记例子中的函数不会有效果—你将会得到
  错误提示，表明这些函数都不是递归的。Scala 编译器无法识别跨方法的递归。
  像这种函数间相互调用产生的递归，我们可以用 TailRec 类和 scala.util.control. 
  TailCalls 包中的可用函数解决。
  TailRec 的实例将会保存函数值（参见第 6 章）。TailRec 中的 result()函数是一个
  简单的迭代器或者说是循环。它会取出保存在 TailRec 中的内部函数，检查它是不是子类
  Call 或者 Done 的实例。如果是 Call 的实例，那么它会发信号通知调用继续执行，迭代会
  继续执行内部函数以便做进一步的处理。如果是 Done 的实例，那么它会发信号通知迭代终
  止，并将内部函数中留存的结果返回。
  */
  
   

}
