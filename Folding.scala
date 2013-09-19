//Folding is like reduce
//Fn that takes an inital value of type B, and returns
//  a fn that takes a fn that takes types (B, A), and returns B, eventually returning one B
//def foldLeft[B](init: B)(f: (B, A) => B): B
val tmpList = 1 to 10 toList
val sum = tmpList.foldLeft(0)((x,y) => x+y)

//You can capture a reducer for a collection like so:
// This creates a reducer starting at 0, the final _ is to mark the fn as partially applied
val reducer = tmpList.foldLeft(0)_

//foldLeft goes from head to tail
//foldRight goes from tail to head
//since lists are singly-linked, foldRight is head-recursive, so it can blow the stack for large lists
//To avoid this, if you need foldRight, do the following:
val sum2 = tmpList.reverse.foldLeft(0)((x,y) => x+y)

//Also, a view seems to be able to not blow the stack as well
val sum3 = tmpList.view.foldRight(0)((x,y) => x+y)

//Easy way to do sum:
tmpList.foldLeft(0)(_+_)

//You can get a curried version
val tmpCur = tmpList.foldLeft(0)_
val sum4 = tmpCur(_+_)

//Fold is also available, with no guarantee of order of application
val sum5 = tmpList.fold(0)(_+_)
