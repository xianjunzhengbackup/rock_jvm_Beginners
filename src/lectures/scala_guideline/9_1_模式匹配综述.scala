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
            case "Sunday" => print("Eat, sleep, repeat... ") 
            case "Saturday" => print("Hang out with friends... ") 
            case "Monday" => print("...code for fun...") 
            case "Friday" => print("...read a good book...") 
        } 
    } 

    List("Monday", "Sunday", "Saturday").foreach { activity }
    
}
