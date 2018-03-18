import javafx.application.Application
import javafx.event.{ActionEvent, EventHandler}
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.layout.StackPane
import javafx.stage.Stage

object Main extends App {
  Application.launch(classOf[Main], args: _*)
}

class Main extends Application {
  override def start(primaryStage: Stage): Unit = {
    val btn = new Button()
    btn.setText("Say 'Hello World'")

    btn.setOnAction(new EventHandler[ActionEvent] {
      override def handle(event: ActionEvent): Unit = {
        println("Hello")
      }
    })

    val root = new StackPane()
    root.getChildren.add(btn)

    val scene = new Scene(root, 300, 250)
    primaryStage.setTitle("Hello World!!")
    primaryStage.setScene(scene)
    primaryStage.show()
  }
}