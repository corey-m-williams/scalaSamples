//Map -- run a function on elements of a list

val l = List(1, 2, 3, 4, 5)
l.map( x => x * 2) //List(2, 4, 6, 8, 10)

//There are some occasions when you want to return a sequence or list from a
//  function, for example, with Options
def f(x: Int) = if(x > 2) Some(x) else None
l.map(x=> f(x)) //List(None, None, Some(3), Some(4), Some(5))

//flatMap works by applying a function that returns a sequence for each element,
//  and flattening that list of lists
def g(v: Int) = List(v-1, v, v+1)
l.map(x => g(x)) //List(List(0,1,2), List(1, 2, 3), List(2,3,4), ...)
l.flatMap(x => g(x)) //List(0,1,2,1,2,3,2,3,4...)

//This can come in handy with option lists, because an option can
//  be considered either an empty list, or a list of one item:
l.flatMap(x => f(x)) // List(3,4,5)

//A Map can be seen as a list of tuples...
val m = Map(1 -> 2, 2 -> 4, 3 -> 6)

m.toList // List((1,2), (2,4), (3,6))

//You can do a map over a Map's values
m.mapValues(v => v*2) // Map(1->4, 2->8, 3->12)

m.mapValues(v => f(v)) // Map(1->None, 2->Some(4), 3->Some(12))

//If we want to filter it based on the new value...
def h(k: Int, v: Int) = if (v > 2) Some(k->v) else None

m.flatMap( e => h(e._1, e._2) ) // Map(2->4, 3->6)
//That's pretty ugly though, so we can use destructuring
//  In order to get unapply to be used, we need it executed in a partial function, which is usually
//    simplest using a case statement in a code block
m.flatMap{ case (k,v) => h(k, v) }
