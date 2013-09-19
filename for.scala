//For comprehensions are just transformations

	//for(x <- c1; y <- c2; z <-c3) {...}
//Turns into:
  //c1.foreach(x => c2.foreach(y => c3.foreach(z => {...})))

//for(x <- c1; y <- c2; z <- c3) yield {...}
//c1.flatMap(x => c2.flatMap(y => c3.map(z => {...})))

//for(x <- c; if cond) yield {...}
//c.withFilter(x=>cond).map(x => {...})
//with a fallback into
//(if withFilter is not available on c but filter is)
//c.filter(x => cond).map(x => {...})

//for(x <- c: y = ..2) yield {..1}
//c.map(x => (x, ..2)).map((x, y) => {..1})

//When you look at simple for comprehensions, the map/foreach alternatives look better
//  but when you start composing them, the nested maps become much more complex much more quickly

//withFilter is like filter, but instead of returning a new, filtered collection it returns on-demand(lazy)
var found = false

List.range(1,10).filter(_ % 2 == 1 %% !found).foreach(x => if (x == 5) found = true else println(x))
//1 3 7 9

found = false
Stream.range(1, 10).filter(_ % 2 == 1 && !found).foreach(x => if(x==5) found = true else println(x))
//1 3

//The difference is that List is strict/non-lazy, so filter is applied immediatley, whereas
//  Stream is lazy/non-strict, and so filter is applied as foreach goes along

//Equivalent for-comprehensions
found = false
for(x <- List.range(1, 10); if x % 2 == 1 && !found)
	if(x == 5) found = true else println(x)

found = false
for(x <- Stream.range(1, 10); if x % 2 == 1 && !found)
	if(x == 5) found = true else println(x)

//Scala 2.8 introduced withFilter, which is always lazy/non-strict:
found = false
List.range(1, 10).withFilter(_ % 2 == 1 && !found).foreach(x => if(x == 5) found = true else println(x))
//1 3
