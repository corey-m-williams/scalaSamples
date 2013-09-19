//Sequence views allow you to avoid making intermediate collections while chaining modifying functions
val v = 1 to 10 toList
//ex: This creates a mapped vector, which is then mapped again
v map (_ + 1) map (_ * 2)
//However, this will only generate the final list from a composition of the mapped functions
(v.view map (_+ 1) map(_ * 2)).force
//view converts to view(lazy seq?) and force converts back to base type, and realizes all calculations

//Another use for views is for taking a large amount of items from a seq
//  similar to infinite sequences
def isPalindrome(x: String) = x == x.reverse
def findPalindrom(s: Seq[String]) = s find isPalindrome

//findPalindrome(words take 1000000) // -- generates a list of 1 million elements, then calls findPalindrome on it
//findPalindrome(words.view take 1000000) // -- generates lazy seq, and calls findPalindrome on that

//Views also let you access just a segment of mutable sequences
val arr = 0 to 9 toArray
//This does not copy the elements into a slice, but returns a reference to them
val subarr = arr.view.slice(3, 6)
def negate(xs: collection.mutable.Seq[Int]) = for(i<-0 until xs.length) xs(i) = -xs(i)
//negate(subarr) // negates in place just the slice of the array
