object Timer{
	//Function that takes a fn returning Unit(scala for void)
	def oncePerSecond(callback: () => Unit) {
		while(true) {callback(); Thread sleep 1000}
	}
	def timeFlies(){
		println("Time flies when you're having fun")
	}
	def useAnonymous(){
		//Anonymous function:
		//  (<arglist>) => [{] body [}] -- don't always need curly braces (one liners)
		oncePerSecond(() =>
      println("I'm anonymous"))
	}
}
