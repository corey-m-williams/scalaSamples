//Common Methods:
  //Many operators are just methods on a class
	List(1, 2) ++ List(3, 4)
	List(1, 2).++(List(3, 4)) // => List(1, 2, 3, 4)
	//:-postfixed methods bind to the right, so the class that it's called on is to the right
	//So to find a method with a semicolon on the right, look in the class on the right
	1 :: List(2, 3) // is equiv to:
	List(2, 3).::(1) // => List(1, 2, 3)

	1 +: List(2, 3) :+ 4
	List(2, 3).+:(1).:+(4)
