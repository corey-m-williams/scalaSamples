//A good way to return multiple things from an fn

def createAgent() = {
  ("James", "Bond")
}

//Returns a (java.lang.String, java.lang.String) tuple

var agent = createAgent()

println(agent._1 + " " + agent._2)

//You can also use pattern matching/destructuring with tuples:
val (agent_fname, agent_lname) = agent
println(agent_fname + " " + agent_lname)

//Foreach with tuples inside lists:
val ls = List(("a", 1), ("b", 2), ("c", 3))
ls.foreach{ 
	case (c, n) => 
		println(n+" "+c)
}

//Useful for methods like zipWithIndex which return (index, value) tuples
val ls2 = List("Mary", "had", "a", "little", "lamb")
ls.zipWithIndex.foreach{ case(e, i) => println(i+": "+e) }
