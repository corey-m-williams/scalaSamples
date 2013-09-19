import javax.swing.{JFrame, JLabel, JPanel}
import java.awt.{Graphics, Color, Dimension, Component}
import java.awt.image.{BufferedImage, WritableRaster}

def gradient1(i: Int) = {
	i % 256
}
def gradient2(i: Int) = {
	(2 * i) % 256
}

def fillImgData(img: Array[Int], size: Int, f: Int => Int) = {
	var i = 0
	val max = size * size
	while(i < max){
		img(i) = f(i)
		i += 1
	}
}

def paintCanvas(buf: Array[Int], size: Int, img: BufferedImage) = {
	val raster = img.getRaster()
	raster.setPixels(0, 0, size, size, buf);
}

def draw(buf: Array[Int], size: Int) = {
	val image = new BufferedImage(size, size, BufferedImage.TYPE_BYTE_GRAY)
	val canvas = new JPanel(){
		override def paint(g: Graphics) = {
			g.drawImage(image, 0, 0, this)
		}
	}
	val frame = new JFrame();
	paintCanvas(buf, size, image);
	frame.add(canvas)
	frame.setSize(new Dimension(size, size))
	frame.setVisible(true)
	(image, canvas, frame)
}

val imgData = new Array[Int](256 * 256)
fillImgData(imgData, 256, gradient1)

def gradientDynamic(delay: Int) = {
	(i: Int) => {
		math.abs((i - (System.currentTimeMillis / delay).toInt) % 256)
	}
}
