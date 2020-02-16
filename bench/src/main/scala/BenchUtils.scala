package bench

object BenchUtils {
  import java.nio.file.{ Files, Paths }

  val root  = "/tmp"
  val bench = "/tmp/bench"

  def createDir(dir: String): Unit = {
    val dest = root + "/" + dir
    val path = Paths.get(dest)

    if (!Files.exists(path))
      Files.createDirectory(path)

    println("Created dir: " + dest)
  }

  def createFile(file: String): Unit = {
    val dest = bench + "/" + file
    val path = Paths.get(dest)

    if (!Files.exists(path))
      Files.createFile(path)

    println("Created file: " + dest)
  }
}
