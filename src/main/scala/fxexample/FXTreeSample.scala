
import scalafx.application.JFXApp
import scalafx.scene.Scene
import scalafx.scene.paint.Color
import scalafx.scene.control.{TreeItem, TreeView}
import scalafx.scene.layout.{VBox, Priority}

/**
 * Created with IntelliJ IDEA.
 * User: sakura
 * Date: 2013/10/21
 * Time: 18:34
 */
object TreeSample extends JFXApp {
  val tree = new TreeView[String]() {
    vgrow = Priority.ALWAYS
    root = new TreeItem[String]("root") {
      children = Seq(nodes("parent1"), nodes("parent2"))
    }
  }

  stage = new JFXApp.PrimaryStage {
    title = "Hello ScalaFX TreeView"
    scene = new Scene {
      fill = Color.LIGHTBLUE
      root = new VBox {
        hgrow = Priority.ALWAYS
        content = tree
      }
    }
  }

  def nodes(name: String) = new TreeItem[String](name) {
    children = (1 until 10).map { i =>
      new TreeItem[String]("child" + i)
    }
  }
}