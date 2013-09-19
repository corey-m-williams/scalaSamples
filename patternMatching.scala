
val l = List(1, 2, 3)
val List(a, b, c) = l // a, b, and c are initialized with the contents of l
  //so a = 1, b = 2, c = 3

//val a:Any = [...]; //initialize with anything
val x:Any = List(1, 3, 4); //needs the cast to Any, so that you can match on it

x match{
	case 1 => 
		println("one")
	case 3 =>
		println("three") //this is like switch/case, except without break
	case x:Double => //matched to datatype
		println("A is a double")
	case x:Int if(x%2 == 1) =>
		println("a is and odd integer")
	case List(1, 2, 3) => //a is matched to a a list with exact content
		println("123")
	case List(1, x, 3) => //a is matched to 3 elt list starting/ending with 1/3, and binds x to middle
		println("Middle is: " + x)
	case 1 :: xs => //a is a list, and first elt is 1, xs becomes List(2, 3)
		println(xs)
	case _ => //wildcard, matches anything
		println("A is wierd.")
}
