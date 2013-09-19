//Wrappers can be pretty useful

def time (fun: => Unit) = { //Take a fn with no params/return
	val startTime = System.currentTimeMillis
	fun
	val endTime = System.currentTimeMillis
	endTime-startTime
}

val t = time{
	//do stuff
  //any codeblock can be passed
}

//OpenGL matrix ops, as an example

//These are defined by the gl library, but to avoid compilation things...
def matrix(fun: => Unit){
	glPushMatrix
	fun
	glPopMatrix
}

matrix{
	translatef(1, 2, 3)
	matrix{
		rotatef(60,0,0,1)
		//draw something
	}
	matrix{
		rotatef(60,0,1,0)
		//draw something else
	}
}
