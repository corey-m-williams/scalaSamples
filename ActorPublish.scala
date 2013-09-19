import scala.actors._
import scala.actors.Actor._

//Marker trait for every subscriber to implement, make sure they're an actor
trait Subscriber[E] extends Actor

//Defining the subscribe/unsubscribe messages
sealed abstract class SubscriberRequest[E]
case class Subscribe[E](subscriber:Subscriber[E]) extends SubscriberRequest[E]
case class Unsubscribe[E](subscriber:Subscriber[E]) extends SubscriberRequest[E]

//Finally there is the publisher trait, which maintains the list of subscribers, and
//  provides a method for publishing messages
trait ActorPublisher[E] {
	private var subscribers: List[Subscriber[E]] = Nil

	//The react method accepts an argument of a PartialFunction[Any, Unit], so you can use an explicit
	//  partial function instead of an anonymous one
	protected val handleSubscribe : PartialFunction[Any, Unit] = {
		case m:Subscribe[E] =>
			if(!isSubscriber(m.subscriber)){
				println("adding subscriber")
				subscribers = m.subscriber :: subscribers
			}
		case m:Unsubscribe[E] =>
			println("Removing subscriber")
			subscribers = subscribers.filter(_ != m.subscriber)
	}
	
	def getSubscribers():List[Subscriber[E]] = 
		subscribers
	
	private def isSubscriber(subscriber: Subscriber[E]) =
		subscribers.exists(_==subscriber)
	
	protected def publish(message: E) = subscribers.foreach(_ ! message)
}


//Example usage class:

//Messages sent from playlist on add/remove item
sealed abstract class PlayListMessage
case class PlayListAddItem() extends PlayListMessage
case class PlayListRemoveItem() extends PlayListMessage

//Message to send to playlist to request add/remove
sealed abstract class PlayListRequest
case class PlayListRequestAdd() extends PlayListRequest
case class PlayListRequestRemove() extends PlayListRequest

class PlayListTracker extends Actor with ActorPublisher[PlayListMessage] {
	def act() {
		println("Acting")
		loop {
			//Use the orElse method in PartialFunction
			//Parital functions allow you to have multiple functions for different ranges/types/etc of input
			//  so if handleSubscribe can't processes the input, it passes it to handleOther
			react(handleSubscribe orElse handleOther)
		}
	}
	private val handleOther : PartialFunction[Any, Unit] = {
		case m:PlayListRequestAdd => 
			println("Add to playlist")
		  publish(new PlayListAddItem())
		case m:PlayListRequestRemove =>
			println("Remove from playlist")
		  publish(new PlayListRemoveItem())
	}
}

val tracker = new PlayListTracker
val listener = new Subscriber[PlayListMessage] {
	def act(){
		loop{
			react {
				case m:PlayListAddItem =>
					println("Item Added")
				case m:PlayListRemoveItem =>
					println("Item Removed")
				case _ =>
					println("Unsupported message...")
			}
		}
	}
}

tracker start
listener start

tracker ! Subscribe(listener)
tracker ! new PlayListRequestAdd()
tracker ! new PlayListRequestRemove()
