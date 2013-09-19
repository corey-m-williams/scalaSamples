import scala.collection.mutable.PriorityQueue
import scala.math.Ordering

object NavNode{
	//In this case, we want the lowest score first, so negate it
	implicit val ord = Ordering.by[NavNode,Double](-_.score)
}
class NavNode(loc: (Int, Int), target: (Int, Int)){
	def score():Double = {
		val dx = target._1 - loc._1
		val dy = target._2 - loc._2
		val dist = math.sqrt((dx * dx) + (dy * dy))
		return dist
	}
	override def toString() = {
		"NavNode<%f:(%d,%d)->(%d,%d)>".format(score(), loc._1, loc._2, target._1, target._2)
	}
}

//Note the () before the argument, needed to take care of implicit argument
//val tmpQ = new PriorityQueue[NavNode]()(Ordering.by[NavNode,Double](_.score))
val tmpQ = new PriorityQueue[NavNode]
val tmp1 = new NavNode((0,0), (2,2))
val tmp2 = new NavNode((2,2), (2,2))
val tmp3 = new NavNode((1,1), (2,2))
val tmp4 = new NavNode((2,1), (2,2))
tmpQ.enqueue(tmp1, tmp2, tmp3, tmp4)
//The default string generation for a pqueue uses the heap's sorting order, not by priority for 
//  the output of the elemtns, so we clone and do dequeue(All) to get the ordered elements
println(tmpQ.clone().dequeueAll)
//Doesn't seem to enqueue properly

//------------Custom priority queue --------------
trait Scorable {
	def score (): Int
}

class ScorableInt(val num: Int) extends Scorable{
	override def score (): Int = { num }
}
implicit final def ScorableInt(n: Int): ScorableInt = new ScorableInt(n)
def convertInt(x: Int): Scorable = x

def addToPQueue (lst: List[Scorable], item: Scorable): List[Scorable] = {
	val parts = lst.partition(item.score > _.score)
	(parts._1 :+ item) ++ parts._2
}

val tmpList = List(1, 10, 20, 30, 60)
val tmpSortable = tmpList map convertInt
