```scala
import scala.concurrent.{ExecutionContext, Future, Await}
import scala.concurrent.duration._
import scala.util.{Failure, Success}

class MyClass(implicit ec: ExecutionContext) {
  def myMethod(x: Int): Future[Int] = Future {
    if (x < 0) throw new IllegalArgumentException("x must be non-negative")
    x * 2
  }.recover {
    case e: IllegalArgumentException => 
      println(s"Error in myMethod: ${e.getMessage}")
      0 // Or some other default value
    case e: Exception => 
      println(s"Unexpected error in myMethod: ${e.getMessage}")
      0 // Or some other default value
  }
}

object Main extends App{
  implicit val ec = scala.concurrent.ExecutionContext.global
  val myClass = new MyClass()
  val result = myClass.myMethod(-1)
  result.onComplete{
    case Success(value) => println(s"Success: $value")
    case Failure(exception) => println(s"Failure: ${exception.getMessage}")
  }
  Await.result(result, 1.second)
}
```