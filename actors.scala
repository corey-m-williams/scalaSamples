import scala.actors.Actor
import scala.actors.Actor._

//http://www.scala-lang.org/node/242

//Actors are the main concurrency primitive in scala
//Essentially message passing

//As an example, we'll have an actor that sends ping to a 2nd actor, which responds with pong
//  until stop is sent

//We need to define the possible messages
//since we want to use pattern matching, they are case objects
case object Ping
case object Pong
case object Stop

class Ping(count: Int, pong: Actor) extends Actor {
	def act(){
		var pingsLeft = count - 1
		pong ! Ping // send Ping message to pong actor
		while(true){
			//Call the recieve method, blocks until a message shows up
			receive {
				case Pong =>
					if(pingsLeft % 1000 == 0)
						 Console.println("Ping: pong")
					if(pingsLeft > 0){
						//Send another Ping
						pong ! Ping
						pingsLeft -= 1
					}else{
						Console.println("Ping: stop")
						pong ! Stop
						exit()
					}
			}
		}
	}
}

class Pong extends Actor {
	def act(){
		var pongCount = 0
		while(true) {
			receive {
				case Ping =>{
					if (pongCount % 1000 == 0)
						Console.println("Pong: ping " + pongCount)
					//Reply to sender (set inside recive method?)
					sender ! Pong
					pongCount = pongCount + 1
				}
				case Stop =>{
					Console.println("Pong: stop")
					exit()
				}
			}
		}
	}
}

//By default, actors waiting on recieve take up a jvm thread.
//To avoid this, you can use react instead of recieve
//One difference is that react never returns, you generally have to call some function
//  that finishes the work
//Because it never returns, a while loop won't work. The loop method will loop reacts
//You can nest reacts, so you can respond to two messages in a specific sequence
//  react {
//  	case A => ...
//    case B => react {
//if we get b, we also want a C
//      case C => ...
//    }
//  }
class PongReact extends Actor {
	def act(){
		var pongCount = 0
		loop {
			react {
				case Ping =>{
					if (pongCount % 1000 == 0)
						Console.println("Pong: ping" + pongCount)
					//Reply to sender (set inside recive method?)
					sender ! Pong
					pongCount = pongCount + 1
				}
				case Stop =>{
					Console.println("Pong: stop")
					exit()
				}
			}
		}
	}
}

object pingpong extends App{
	val pong = new Pong
	val ping = new Ping(100000, pong)
	Console.println("Starting up")
	ping.start
	pong.start
}


//Producers

case object Next
//Stop defined above for first example

class Tree[T](val elem: T, val left: Tree[T], val right: Tree[T])

abstract class Producer[T] {
	protected def produceValues: Unit

	protected def produce(x: T) {
		coordinator ! Some(x)
		receive { case Next => }
	}
	
	//Using the actor function creates *and starts* an actor
	private val producer: Actor = actor {
		receive {
			//When we get a Next
			case Next =>
				//Use the abstract produceValues, which will call produce, and
				//  sends a sequence of Some[T] values to the coordinator
				produceValues
			  //signal to coordinator that we're done, using None
			  coordinator ! None
		}
	}
	
	private val coordinator: Actor = actor {
		loop {
			react {
				case Next =>
					producer ! Next
				  reply {
						receive { case x: Option[_] => x }
					}
				case Stop => exit('stop)
			}
		}
	}
	
  //We want to make producers usable as normal iterators
  def iterator = new Iterator[T] {
		private var current: Any = null
		private def lookAhead = {
			if ( current == null) current = coordinator !? Next
			current
		}
		def hasNext: Boolean = lookAhead match {
			case Some(x) => true
			case None => { coordinator ! Stop; false }
		}
		def next: T = lookAhead match {
			case Some(x) => current = null; x.asInstanceOf[T]
			case None => null.asInstanceOf[T]
		}
	}
}

//Example of using producer to get values from a tree
class PreOrder(n: Tree[Int]) extends Producer[Int] {
	def produceValues = traverse(n)
	def traverse(n: Tree[Int]){
		if (n != null) {
			produce(n.elem)
			traverse(n.left)
			traverse(n.right)
		}
	}
}

object treeTest extends App {
	val data: Tree[Int] = new Tree[Int](1, new Tree[Int](0, null, null), new Tree[Int](2, null, null))
	val prod: Producer[Int] = new PreOrder(data)
	while(prod.iterator.hasNext){
		Console.println(prod.iterator.next)
	}
}
