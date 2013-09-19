import scala.actors.Future
import scala.actors.Futures._
import scala.io.Source
import java.net.URL

val f: Future[String] = future { "foo" }
//Can just call respond, but it will block
while(!f.isSet){
	f.respond(println) // outputs 'foo'
}

val f2 = future {
	Source.fromURL(
		new URL("http://localhost"), java.nio.charset.StandardCharsets.UTF_8.name()
	).mkString
}
f.respond(println)
