
import scalafx.Includes._
import scalafx.application.JFXApp
import scalafx.scene.layout.VBox
import scalafx.scene.layout.BorderPane

import scalafx.scene.Scene
import scalafx.scene.paint.Color._
import scalafx.scene.shape.Rectangle
import scalafx.geometry.Insets
import scalafx.scene.Scene
import scalafx.scene.effect.DropShadow
import scalafx.scene.layout.HBox
import scalafx.scene.layout.BorderPane
import scalafx.scene.control.TabPane
import scalafx.scene.control.SeparatorMenuItem

import scalafx.scene.paint.Color._
import scalafx.scene.paint.{Stops, LinearGradient}
import scalafx.scene.text.Text
import scalafx.scene.control.MenuBar
import scalafx.scene.control.Menu
import scalafx.scene.control.MenuItem
import scalafx.scene.control.TreeItem
import scalafx.scene.control.TextArea
import scalafx.scene.control.TextField

import scalafx.scene.control.Slider
import scalafx.scene.control.ScrollPane
import scalafx.scene.control.SplitPane
import scalafx.scene.control.Tab
import scalafx.scene.control.Label
import scalafx.scene.control.ChoiceDialog
import scalafx.scene.control.ColorPicker

import scalafx.scene.control.TreeView

import scalafx.geometry.Orientation
import scalafx.scene.canvas.Canvas
import scalafx.scene.canvas.GraphicsContext
//import java.awt.event.ActionEvent
//import javafx.event.ActionEvent
import scalafx.event.ActionEvent
import scalafx.scene.paint.Color
import scalafx.scene.Node


trait Drawable {
  def draw(gc: GraphicsContext): Unit
  def propertiesPanel: scalafx.scene.Node
  

}

class DrawRectangle (
   private var x: Double,
   private var y: Double,
   private var width: Double,
   private var height:Double,
   private var color: Color) extends Drawable {

   private var propPanel: Option[Node] = None


   def draw(gc: GraphicsContext): Unit={
     //
     gc.fill=color
     gc.fillRect(x,y,width,height)
   }

   def propertiesPanel: scalafx.scene.Node ={
    if (propPanel.isEmpty){
       val panel = new VBox
       
       val xField=PsiViewMain.labeledTextField("x",x.toString, s =>{
          x=s.toDouble
          //drawing.draw()
       })
      val yField=PsiViewMain.labeledTextField("y",y.toString, s =>{
          y=s.toDouble
          //drawing.draw()
       })
      val widthField=PsiViewMain.labeledTextField("width",width.toString, s =>{
          width=s.toDouble
          //drawing.draw()
       })
       val heightField=PsiViewMain.labeledTextField("height", height.toString, s =>{
          height=s.toDouble
          //drawing.draw()
       })
       val colorPicker = new ColorPicker(color)
       colorPicker.onAction=(ae: ActionEvent) =>{
          color= colorPicker.value()
          //drawing.draw()
       }
       //
       //move to another func as refactoring.
       //
       panel.children=List(xField,yField,widthField,heightField,colorPicker)
       propPanel = Some(panel)
       
    }
    propPanel.get
     //new Label("Rectangle")
   }
   override def toString: String = "Rectangle"
}
class DrawText (
  private var x: Double,
  private var y: Double,
  private var text: String,
  private var color: Color) 
  extends Drawable{
  def draw(gc: GraphicsContext): Unit={
    gc.fill=color
     gc.fillText(text,x,y)
   }
   def propertiesPanel: scalafx.scene.Node ={
     new Label("Text")
   }
   override def toString: String = "Text"

}

class DrawTransform extends Drawable{
   private var _children=List[Drawable]()
   def children = _children
   def addChild(d: Drawable): Unit = _children ::= d
   def draw(gc: GraphicsContext): Unit={
     //Todo
     children.foreach{_.draw(gc)}
   }
   def propertiesPanel: scalafx.scene.Node ={
     new Label("Transform")
   }
   override def toString: String = "Transform"


}


import scalafx.scene.control.TreeItem
import scalafx.scene.canvas.GraphicsContext

class Drawing {
  val root = new DrawTransform
  private var _gc = None:Option[GraphicsContext]
  
  def gc= _gc
  def gc_= (g: GraphicsContext): Unit = _gc = Some(g)


  def draw(): Unit = {
    
    gc.foreach{ g =>
      g.fill=Color.White
      g.fillRect(0,0,2000,2000)
      root.draw(g)
    }
    

  }
  def makeTree() : TreeItem[Drawable]={
     def helper(d: Drawable): TreeItem[Drawable]=d match{
        case dt: DrawTransform => 
           val item = new TreeItem(d)
           dt.children.foreach(c => item.children += helper(c) )
           item
        case _ => new TreeItem(d)
     }
     helper(root)
  }   

}


object PsiViewMain extends JFXApp {
  private var drawings = List[(Drawing, TreeView[Drawable])]()
  
  stage = new JFXApp.PrimaryStage {
    title.value = "Hello Stage"
    width = 600
    height = 450


    scene = new Scene {
      val menuBar=new MenuBar
      val fileMenu = new Menu("File")
      val newItem  = new MenuItem("New")
      val openItem  = new MenuItem("Open")
      val saveItem  = new MenuItem("Save")
      val exitItem  = new MenuItem("Exit")
      fileMenu.items = List(newItem,openItem,saveItem,new SeparatorMenuItem,exitItem)
      val editMenu = new Menu("Edit")
      val addItem  = new MenuItem("Add Drawable")
      val copyItem  = new MenuItem("Copy")
      val cutItem  = new MenuItem("Cut")
      val pasteItem  = new MenuItem("Paste")
      editMenu.items=List(addItem,copyItem,cutItem,pasteItem)
      menuBar.menus=List(fileMenu,editMenu)

      val tabPane = new TabPane
      val firstDrawing= new Drawing
      //firstDrawing.root.addChild(new DrawRectangle)
      //firstDrawing.root.addChild(new DrawText)
      val (tab, tree) = makeDrawingTab(firstDrawing,"Intitled")
      drawings = drawings :+ firstDrawing -> tree
      //val (tab,tree) =  makeDrawingTab(firstDrawing, "untitled")
      tabPane += tab

      


      newItem.onAction = (ae: ActionEvent) => {
        val newDrawing = new Drawing
        val (tab, tree) = makeDrawingTab(firstDrawing,"Intitled")
        drawings = drawings :+ new Drawing -> tree
        //val newTab = makeDrawingTab(newDrawing, "untitled")
        
        tabPane += tab
      }
      addItem.onAction = (ae: ActionEvent) => {
        val current = tabPane.selectionModel().selectedIndex()
        if (current >= 0){
          val (drawing,treeView) = drawings(current)
          val dialog = new ChoiceDialog("Rectangle",List("Rectangle","Transform","Text"))
          dialog.showAndWait().foreach { s =>
            val d: Drawable = s match {
              case "Rectangle" => new DrawRectangle(0,0,100,100,Color.Black)
              case "Transform" => new DrawTransform
              case "Text"      => new DrawText(200,200,"The Text",Color.Black)
            }
            val treeSelect = treeView.selectionModel().selectedItem()
            def treeAdd(item: TreeItem[Drawable]): Unit = item.getValue match{
              case dt: DrawTransform => 
                 dt.addChild(d)
                 item.children += new TreeItem(d)
                 drawing.draw()
              case d => 
                 treeAdd(item.getParent)
            }
            if (treeSelect != null) treeAdd(treeSelect)
          }
        }
      }
      exitItem.onAction = (ae: ActionEvent) => {
        //Todo save all the tabls
        sys.exit(0)
      }

      val rootPane=new BorderPane
      rootPane.top = menuBar
      rootPane.center=tabPane
      root=rootPane


      
    }

    private def makeDrawingTab(drawing: Drawing, name: String): (Tab,TreeView[Drawable]) = {
      val drawingTree = new TreeView[Drawable]
      drawingTree.root = drawing.makeTree()
      val treeScroll = new ScrollPane
      drawingTree.prefWidth <==  treeScroll.width -10
      drawingTree.prefHeight <==  treeScroll.height -10

      
      treeScroll.content=drawingTree

      val propertyPane= new ScrollPane
      val leftSplit = new SplitPane
      leftSplit.orientation = Orientation.Vertical
      leftSplit.items ++= List(treeScroll, propertyPane)

      val topRightBorder = new BorderPane
      val slider = new Slider(0,1000,0)
      val canvas = new Canvas(2000,2000)

      var gc = canvas.graphicsContext2D
      drawing.gc = gc 
      //gc.fillRect(50,50,100,100)
      val scrollCanvas = new ScrollPane
      scrollCanvas.content=canvas

      topRightBorder.top=slider
      topRightBorder.center=scrollCanvas

      val bottomRightBorder = new BorderPane
      val commandField = new TextField
      val commandArea =  new TextArea
      commandArea.editable = false
      bottomRightBorder.top=commandField
      bottomRightBorder.center=commandArea



      val rightSplit= new SplitPane
      rightSplit.orientation = Orientation.Vertical
      rightSplit.items ++=List(topRightBorder,bottomRightBorder)
      rightSplit.dividerPositions=0.7
      
      val topSplit = new SplitPane
      topSplit.items ++= List(leftSplit, rightSplit)
      topSplit.dividerPositions = 0.3

      drawingTree.selectionModel.value.selectedItem.onChange {
        val selected = drawingTree.selectionModel().selectedItem()
        if (selected != null){
           propertyPane.content = selected.getValue.propertiesPanel
        }else {
          propertyPane.content = new Label("Nothing selected..")
        }
      }
      
      val tab = new Tab
      tab.text = name
      tab.content=topSplit
      tab -> drawingTree
    }
  
  }
    //private[drawing] def labeledTextField(labelString: String, initialText: String, action: String=> Unit): BorderPane = {
  private[drawings] def labeledTextField(labelString: String, initialText: String, action: String => Unit): BorderPane = {
      val borderPane = new BorderPane
      borderPane.left = new Label(labelString)
      val textField   = new TextField
      textField.text  = initialText
      borderPane.center = textField
      textField.onAction = (ae: ActionEvent) => action(textField.text.value)
      textField.focused.onChange(if(!textField.focused.value) action(textField.text.value) )
    
      borderPane
  }

  

}