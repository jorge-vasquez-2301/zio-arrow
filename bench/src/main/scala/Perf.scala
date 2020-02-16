package bench
import FileUtils._

import scala.jdk.CollectionConverters._

object App0 extends App {
  val file = "file0"

  newDir("bench")
  newFile(file)
  wrFile(file, "22")
  val res = rdFile(file).get.toInt
  println(res)

  delFile(file)
}
