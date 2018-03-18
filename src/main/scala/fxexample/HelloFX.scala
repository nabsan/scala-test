import scalafx.application.JFXApp
import scalafx.application.JFXApp.PrimaryStage
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.control.Label
import scalafx.scene.layout.BorderPane

object HelloFX extends JFXApp {
  stage = new PrimaryStage {
    title = "Hello, FX-World!"
    scene = new Scene {
      root = new BorderPane {
        padding = Insets(25)
        center = new Label("Hello, World!")
        //fill = java.awt.Color.LIGHTGREEN
      }
      


    }
  }
}