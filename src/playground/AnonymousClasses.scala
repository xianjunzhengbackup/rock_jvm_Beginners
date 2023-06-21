package playground

object AnonymousClasses extends App{
  /*
    1. Generic trait MyPredicate[-T] with a little method test(T) => Boolean
    2. Generic trait MyTransformer[-A,B] with a method transform(A) => B
    3. MyList:
      - map(transformer) => MyList
      - filter(predicate) => MyList
      - flatMap(transformer from A to MyList[B]) => MyList[B]

      class EvenPredicate extends MyPredicate[Int]
      class StringToIntTransformer extends MyTransformer[String,Int]

      [1,2,3].map(n * 2) =>[2,4,6]
      [1,2,3,4].filter(n%2)=>[2,4]
      [1,2,3].flatMap(n=>[n,n+1]) => [1,2,2,3,3,4]
     */
  trait MyPredicate[T] {
    val v : T
    def test[T]:Boolean
  }


}
