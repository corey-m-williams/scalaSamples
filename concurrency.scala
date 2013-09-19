import java.net.{Socket, ServerSocket}
import java.util.concurrent.{Executors, ExecutorService}
import java.util.Date

class NetworkService(val port: Int, val poolSize: Int) extends Runnable{
	val serverSocket = new ServerSocket(port)
	val pool: ExecutorService = Executors.newFixedThreadPool(poolSize)
	
	def run(){
		try{
			while(true){
				val socket = serverSocket.accept()
				pool.execute(new Handler(socket))
			}
		}finally{
			pool.shutdown();
		}
	}
}

class Handler(val socket: Socket) extends Runnable{
	def message = (Thread.currentThread.getName() + "\n").getBytes;
	
	def run() {
		socket.getOutputStream.write(message)
		socket.getOutputStream.close()
	}
}

//(new NetworkService(2020, 2)).run

//Future represents an async computation.
/*val future = new FutureTask[String](new Callable[String]() {
	def call(): String = {
		searcher.search(target);
	}
});
*/
//executor.execute(future)

//Then you can block to get results using get
//val res = future.get();

//Thread safety:
//Synchronized blocks
class Person(var name: String){
	def setName(newName: String){
		//Synchronize on the current instance
		//synchronized is a method in the AnyRef class
		//  def synchronized[T0](arg0: => T0): T0
		this.synchronized {
			name = newName;
		}
	}
}
//Volatile variable is like auto-synchronized, and can be null
//  synchronized allows for more fine-grained access, as volatile syncs on all access
class Person2(@volatile var name: String){
	def setName(newName: String){
		name =newName;
	}
}

//Atomic references are also available
import java.util.concurrent.atomic.AtomicReference
class Person3(val name: AtomicReference[String]){
	def setName(newName: String){
		name.set(newName);
	}
}
//Generally, atomic is slower than volatile is (potentially) slower than synchronized
