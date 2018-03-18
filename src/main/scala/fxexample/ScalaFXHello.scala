
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.scene.Scene
import scalafx.scene.control.Button
import scalafx.scene.layout.VBox

object ScalaFxHello extends JFXApp {
  stage = new PrimaryStage {
    title = "Hello"
    scene = new Scene {
      root = new VBox {
        children = new Button("Hello Button") {
          //onMouseClicked = handle {
          onAction=handle{
            println("hello clicked..")
          }
        }
      }
    }
  }
}