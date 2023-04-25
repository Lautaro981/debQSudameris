import java.net.InetSocketAddress

import play.PlayRunHook
import sbt._

object Grunt {
  def apply(base: File): PlayRunHook = {

    object GruntProcess extends PlayRunHook {

      var watchProcess: Option[Process] = None

      override def beforeStarted(): Unit = {}

      override def afterStarted(addr: InetSocketAddress): Unit = {
        if (System.getProperty("os.name").toUpperCase().indexOf("WIN") >= 0) {
          watchProcess = Some(Process("cmd /c \"grunt watch\"", base).run)
        } else {
          watchProcess = Some(Process("grunt watch", base).run)
        }
      }

      override def afterStopped(): Unit = {
        watchProcess.map(p => p.destroy())
        watchProcess = None
      }
    }

    GruntProcess
  }
}
