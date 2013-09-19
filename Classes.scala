//The two params here are defaulted to vals, which are immutable
//  you can change this by putting var in front of them
class Complex(var real: Double, var imaginary: Double) {
	//Sort of overloaded constructor -- can only have one line: call to main or
  //  'other' constructor, all end up at main constructor
  def this() = this(1.5, 2.0)
	/* //You can declare these as fns, but they require parens to call later
  def re() = real
  def im() = imaginary
	*/
	//With no parens following def name, can use like a field
	def re = real
	//This defines <var>.re = ...
  //  re needs to be var/mutable for this to work
	def re_=(newRe: Double):Unit = real = newRe
  def im = imaginary
	def im_=(newIm: Double):Unit = imaginary = newIm
	
	//You must specify when overriding super class methods
  override def toString() =
    "" + re + (if (im < 0) "" else "+") + im + "i"
}

var comp = new Complex(1.5, 2.3)


class Stock(val name:String, val symbol:String, var price:Double, var change:Double){
	def this(name:String, symbol:String) = this(name, symbol, 0.0, 0.0)
}

//Factory pattern
//Objects are singletons
//Essentially all fields in 'object' are static
//Can have same name as a class
//Creates a type and instance with same name (Stock, in this case)
object Stock{
  def fromCSV(ser: String):Stock = {
		val params = ser.split(",")
    new Stock(params(0), params(1),
							java.lang.Double.parseDouble(params(2)),
							java.lang.Double.parseDouble(params(3)))
	}
}
//With Stock as a regular class, you have to instantiate with new, so the creation would be:
//class constructor
val apple = new Stock("Apple Computers", "AAPL", 99.77, -1.11)
//factory
val microsoft = Stock.fromCSV("Microsoft,MSFT,21.20,-0.10")

//Trees:
//Example -- calculator, using a tree where branches are operations, and vals are leaves
//In java, probably have abstract super-class for trees, and one sub-class per node
//  or leaf... scala uses case classes
abstract class Tree
case class Sum(l: Tree, r: Tree) extends Tree
case class Var(n: String) extends Tree
case class Const(v: Int) extends Tree
//Case classes:
//  -don't need new for creation, can just do Const(5) instead of new Const(5)
//  -getter fns are automatically defined for constructor params
//  -default definitions for equals/hashCode which work on structure of instances
//  -default toString, prints as a 'source form' ex: x+1 -> 'Sum(Var(x),Const(1))'
//  -instances can be decomposed through pattern matching

//Pattern matching
//we want to execute the calculations in environments, with vars getting values
//An environment can just be a function with the correct params, ex if we want x->5,
//  {case "x" => 5} //this takes a string, and if it's 'x' returns 5, else exception

type Environment = String => Int
def eval(t: Tree, env: Environment): Int = 
t match{ //match the tree against the following cases
	//It's a sum, bind l and r to local vars
	case Sum(l, r) => eval(l, env) + eval(r, env)
	//It's a var, bind n to local var
	case Var(n)    => env(n)
	case Const(v)  => v
}

def derive(t: Tree, v: String): Tree =
t match{
	case Sum(l, r) => Sum(derive(l, v), derive(r, v))
	case Var(n) if (v == n) => Const(1) // this case has a guard, (if statement)
	case _ => Const(0) // default
}

//We could use methods on the tree classes for eval, but
//  1) new ops would be easy to add
//  2) new actions on tree would have to be updated in every op
//With pattern matching
//  1) new ops would need to be added to all fns that deal with tree (eval as example)
//  2) new fns that deal with tree are easy to add
//So there's a tradeoff either way

val exp: Tree = Sum(Sum(Var("x"),Var("x")),Sum(Const(7),Var("y")))
val env: Environment = { case "x" => 5 case "y" => 7 }
