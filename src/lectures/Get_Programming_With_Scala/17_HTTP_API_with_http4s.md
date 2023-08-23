# 17 HTTP API with http4s
After reading this lesson, you will be able to

* Run an HTTP server using sbt
* Implement an API to handle GET requests

After learning about partial functions, you’ll use them as part of your implementation of an HTTP server. Building an HTTP server without the help of an external library would require lots of extra time and code. Thankfully, the Scala ecosystem offers a few external libraries to help you handle HTTP communication in an efficient and performant way. In this lesson, you’ll learn about http4s, a popular library to manage HTTP requests and responses. You’ll discover how to implement an HTTP server that replies to a GET /ping request with a response with status code 200 – Ok and the text "pong". Finally, you’ll see how to run it using sbt. In the capstone, you’ll use http4s to create an HTTP server and define its API.

Consider this

Suppose you are building an HTTP server that provides a REST API for retrieving and storing data. What are the main components of your server? How would you structure your code?

## 17.1 An overview of http4s
Typelevel is a nonprofit organization to promote purely functional open source Scala projects together with an inclusive and welcoming environment (typelevel.org). http4s (http4s.org) is a Typelevel project and one of the most popular Scala libraries to handle HTTP requests and responses. It is particularly accessible to newcomers to the language, thanks to its extensive documentation and numerous examples.

Even though http4s does offer a Giter8 template (see sidebar), you’ll see how to build an HTTP server from scratch in this lesson.

A Giter8 template for http4s

The library http4s offers a Giter8 template to generate a simple HTTP server that replies to an /hello/world endpoint, an ideal skeleton for any new project that deals with an HTTP API. To apply the template, type the following sbt command:
```
$ sbt new http4s/http4s.g8
```
The terminal prompt will ask you to provide some information about your project, such as its name, organization, and package; when in doubt, choose the provided default value.

After applying the template, navigate to the newly created folder and execute the command sbt run to start the HTTP server. You can now send HTTP requests to it:
```
 $ curl -i http://localhost:8080/hello/scala
HTTP/1.1 200 OK
Content-Type: application/json
Date: Tue, 29 Dec 2020 15:24:52 GMT
Content-Length: 26
 
{"message":"Hello, scala"}
```
Pick at its code; it should look reasonably similar to the one you’ll see in this lesson. It also provides examples of testing an HTTP application and deserializing from JSON—a topic you’ll master in unit 8.

Before explaining how to implement an HTTP server using http4s, let’s go over its architecture. First, you need to link your routes to their business logic through instances of org.http4s.HttpRoutes. Each HttpRoutes uses partial functions to match an incoming HTTP request, and it produces an HTTP response together with a side effect (e.g., an IO read/write, a connection to a third party). At this point, you may not be familiar with the concept of side effects, but do not worry, as you’ll learn about them in the next unit. Most applications use cats.effect.IO as a generic representation of both synchronous and asynchronous side effects (in unit 8, you’ll learn about side effects and why this is crucial). Finally, you define a singleton object that extends cats.effect.IOApp and provide instructions on the port and host to bind and services to mount. Your executable object also uses Blaze (https://github.com/http4s/blaze) together with streams as the backend for network IO.

An HttpRoutes[IO] matches a request, and it produces a response wrapped around an IO instance to represent possible side effects. Your executable object extends IOApp and uses a BlazeServerBuilder to bind to a given port and host and to mount multiple instances of HttpRoutes[IO] to define the API of an HTTP server using an f2.Stream instance.

## 17.2 A ping server using http4s
Now that you have an overview of http4s, let’s see how you can use it to code an HTTP server. Your goal is to implement an HTTP server that runs on localhost:8000 and that replies to a GET /ping request with a response with status code 200 – OK and the string "pong"; look at figure 17.2 for a visual summary of your server’s requirements.



Figure 17.2 Functionalities of your HTTP server: when receiving a GET /ping request, it should reply with status code 200 - OK and body “pong.”

### 17.2.1 INITIAL SETUP
Let’s create an empty sbt project: use your IDE or a Giter8 template. Alternatively, you can create one manually as follows:

Create a build.sbt file in the directory of your project; it should contain the name and version of the project together with the Scala version you’d like to use:
```
// file build.sbt
 
name := "ping-app"
version := "0.1"
scalaVersion := "3.0.0"
Create a project directory and create a build.properties file that includes your sbt version:

// file project/build.properties
 
sbt.version = 1.5.2
Create the directories src/main/scala and src/main/resources to store your source code and static configuration files.
```
Next, you need to add the http4s modules you’ll use for your HTTP server to your build.sbt file. You are also going to add the logback library; http4s uses it as its logging engine.
```
// append to build.sbt
 
val Http4sVersion = "0.22.0"
 
libraryDependencies ++= List(
  "org.http4s"      %% "http4s-blaze-server" % Http4sVersion,
  "org.http4s"      %% "http4s-dsl"          % Http4sVersion,
  "ch.qos.logback"  %  "logback-classic"     % "1.2.3"
)
```
logback is a popular Java library to customize your logs’ behavior and appearance via a configuration file, usually called logback.xml. Let’s create one in the src/main/resources folder. (See https://logback.qos.ch for more information and how to configure it.)
```
<!-- file src/main/resources/logback.xml -->
 
<configuration>
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%msg %n</pattern>
        </encoder>
    </appender>
    <root level="INFO">
        <appender-ref ref="STDOUT" />
    </root>
</configuration>
```
Finally, let’s define a package that will include all the code for your HTTP ping route by creating the folder src/main/scala/org/example/ping.

### 17.2.2 IMPLEMENTING THE API
Let’s define a class called PingApi to define the routes of your API. PingApi should extend Http4sDsl, a trait that introduces a more intuitive DSL to match HTTP requests and produce HTTP responses.
```
Listing 17.1 The ping API

// file src/main/scala/org/example/ping/PingApi.scala
 
package org.example.ping
 
import cats.effect.IO
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl
 
class PingApi extends Http4sDsl[IO] {         ❶
 
  val routes = HttpRoutes.of[IO] {            ❷
    case GET -> Root / "ping" => Ok("pong")   ❸
  }
}
❶ org.http4s.dsl.Http4sDsl provides an intuitive DSL for matching HTTP requests.

❷ You need to provide a function from a request to a response wrapped in IO to define an instance of HttpRoutes[IO].

❸ It matches a GET request with path /ping and returns a response with status code 200 – OK and body “pong.” You do not need to wrap the response as an IO type; the compiler does it for you at compile time automatically.
```
  

Quick Check 17.1 Suppose you’d like your PingApi to match a request with path/ ping, but with any HTTP method (i.e., GET, POST, PUT, DELETE, PATCH). How would you change your code?

  

Quick Check 17.2 Add a new endpoint to your PingApi to match a GET request with a path /ping/<name>; it should return a string containing "pong" followed by the value passed in the path.

### 17.2.3 BUILDING A SERVER
The only missing component for your HTTP server is your executable application that extends IOApp. It defines your server, so you should instantiate it only once by declaring it as an object.
```
// file src/main/scala/org/example/ping/PingApp.scala
 
package org.example.ping
 
import cats.effect.{ExitCode, IO, IOApp}
import org.http4s.server.Router
 
import org.http4s.implicits._                           ❶
import org.http4s.blaze.server.BlazeServerBuilder
import scala.concurrent.ExecutionContext
 
object PingApp extends IOApp {                          ❷
 
  private val httpApp = Router(
    "/" -> new PingApi().routes
  ).orNotFound                                          ❶
 
  override def run(args: List[String]): IO[ExitCode] =
    stream(args).compile.drain.as(ExitCode.Success)
 
  private def stream(args: List[String]): fs2.Stream[IO, ExitCode] =
    BlazeServerBuilder[IO](ExecutionContext.global)     ❸
    .bindHttp(8000, "0.0.0.0")
    .withHttpApp(httpApp)
    .serve
}
❶ The org.http4s.implicits._ import adds the orNotFound function to your scope; it returns a 404 error code if a request doesn’t match any route.

❷ IOApp is an interface that provides requires to implement a function.

❸ BlazeServerBuilder defines an HTTP server with an address, port, and routes using a stream to process requests.
```
Execution.global indicates the thread pool to use while streaming; you’ll learn more about this when discussing concurrency and the type Future.

IOApp is an interface that requires you to define a run function. Use BlazeServerBuilder[IO] together with the function bindHttp to provide a port and host. The function withHttpApp attaches a group of routes with a prefix to your server. Finally, the function serve transforms your Blaze definition into a stream that represents your HTTP server.

Quick Check 17.3 Change your code not to provide a host or port for your server by invoking the function bindHttp without parameters. What happens when you start your application using the command sbt run?

### 17.2.4 LET’S TRY IT OUT!
The implementation of your HTTP ping server is now complete; it’s time to see it in action! Open your terminal, navigate to your project’s root directory, and execute the command sbt run. sbt will download the dependencies, compile your code, and start your application. After a few seconds, you should see a message similar to the following in your console:
```
$sbt run
[info] Running org.example.ping.PingApp 
Service bound to address /0:0:0:0:0:0:0:0:8000 
  _   _   _        _ _      
 | |_| |_| |_ _ __| | | ___ 
 | ' \  _|  _| '_ \_  _(_-< 
 |_||_\__|\__| .__/ |_|/__/ 
             |_| 
http4s v0.22.0 on blaze v0.15.1 started at http://[0:0:0:0:0:0:0:0]:8000/ 
Your HTTP server is now running and listening on localhost:8000.

If you send a GET /ping request, you will get back a response with status code 200 - Ok and body "pong":

$ curl -i localhost:8000/ping
HTTP/1.1 200 OK
Content-Type: text/plain; charset=UTF-8
Date: Wed, 30 Dec 2020 19:16:18 GMT
Content-Length: 4
 
pong
On the other hand, if you send a POST /ping request rather than a GET /ping request, the server will reply with a 404 – Not Found response:

$ curl -i -X POST localhost:8000/ping
HTTP/1.1 404 Not Found
Content-Type: text/plain; charset=UTF-8
Date: Wed, 30 Dec 2020 19:16:35 GMT
Content-Length: 9
 
Not found
```
## Summary
In this lesson, my objective was to teach you how to build a server that provides an HTTP API to handle GET requests.

You discovered http4s, a purely functional library to handle HTTP communication.

You reviewed its main components, and you applied them to implement an HTTP ping server.

Let’s see if you got this!

Try this Implement a server that exposes an API that successfully replies to any request with a response with status code 200 – Ok and its body with a message that provides the request method and path. For example, when receiving a request POST /this/is/an/example, it should reply with a response with status code 200 – Ok and a body method is POST; path is /this/is/an/example.

 Answers to quick checks
  

Quick Check 17.1 You can change your partial function to match any HTTP method of an incoming request by using the underscore symbol as follows:
```
case _ -> Root / "ping" => Ok("pong")
  
```
Quick Check 17.2 Implement an additional case clause for the partial function that defines your HttpRoutes instance:
```
case GET -> Root / "ping" / name => Ok(s"pong $name")
```  

Quick Check 17.3 The function bindHttp uses predefined defaults when invoked with no parameters. It binds the server to host 127.0.0.1 and port 8080:
```
[info] running org.example.ping.PingApp 
Service bound to address /127.0.0.1:8080 
 
  _   _   _        _ _
 | |_| |_| |_ _ __| | | ___
 | ' \  _|  _| '_ \_  _(_-<
 |_||_\__|\__| .__/ |_|/__/
             |_| 
http4s v0.22.0 on blaze v0.15.1 started at http://127.0.0.1:8080/

```
  |_| 
http4s v0.22.0 on blaze v0.15.1 started at http://127.0.0.1:8080/

