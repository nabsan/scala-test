


import java.sql.{Connection, ResultSetMetaData, ResultSet, DriverManager}
import javafx.beans.value.{ObservableValue, ChangeListener}
import javafx.scene.control.{TreeItem => JFXTreeItem}
import scala.collection.mutable.ListBuffer
import scalafx.application.JFXApp
import scalafx.beans.property.StringProperty
import scalafx.collections.ObservableBuffer
import scalafx.scene.control._
import scalafx.scene.layout._
import scalafx.scene.paint.Color
import scalafx.scene.Scene

/**
 * Created with IntelliJ IDEA.
 * User: sakura
 * Date: 2013/10/10
 * Time: 18:55
 * 
 * DBへ接続して左にテーブル等の一覧、右に選択したテーブルの内容を表示するサンプル。
 * 実行時に接続文字列、ユーザ、パスワードを指定。
 * Class.forName()が必要な場合は--driver=org.sqlite.JDBCの様にオプションを追加する。
 */
object DBSample extends JFXApp {
  // DBの1レコード。サンプルなので全て文字列で扱う。
  type Record = Seq[String]

  // 検索結果表示用のView
  val table = new TableView[Record] {
    vgrow = Priority.ALWAYS
  }

  // テーブル一覧表示用のView
  val tree = new TreeView[String]() {
    vgrow = Priority.ALWAYS
    showRoot = false
  }

  // テーブル一覧でテーブルを選択した際のイベントハンドラ
  // 選択されたテーブルの全てのレコードを検索して表示する。
  // ここだけJavaFX2… ScalaFXにしろJavaFX8にしろもっと簡潔に記述できるはず…
  tree.selectionModel().selectedItemProperty().addListener(new ChangeListener[JFXTreeItem[String]] {
    def changed(prop: ObservableValue[_ <: JFXTreeItem[String]], oldValue: JFXTreeItem[String], newValue: JFXTreeItem[String]) {
      if (newValue.isLeaf) {
        // DB検索
        // 本来は別のスレッドで実行して結果だけJavaFX Application Threadで更新するべき
        val stmt = con.prepareStatement("select * from " + newValue.getValue)
        val rs = stmt.executeQuery()
        val md = rs.getMetaData
        val columns = toColumns(md)
        val records = toSeq(rs)
        stmt.close()

        table.columns.clear()
        table.columns ++= columns.map(_.delegate)
        table.items = ObservableBuffer(records)
      }
    }
  })

  val left = new VBox {
    //content = tree
  }
  val right = new VBox {
    //content = table
  }

  stage = new JFXApp.PrimaryStage {
    title = "DB Sample"
    scene = new Scene {
      fill = Color.LIGHTBLUE
      root = new SplitPane {
        hgrow = Priority.ALWAYS
        vgrow = Priority.ALWAYS
        items.addAll(left, right)
      }
    }
  }

  // DB接続用の情報
  val params = parameters.unnamed
  val url = params(0)
  val user = params(1)
  val password = params(2)

  // driver指定があればClass.forNameしておく
  val named = parameters.named
  named.get("driver").map(driver => Class.forName(driver))

  // サンプルなので一つの接続を使い回す。
  // まじめに作る時はdef withConnection[T](con: Connection): T の様な関数を作りましょう。
  // traitで作ってCake PatternにするのがScala流？
  val con = DriverManager.getConnection(url, user, password)
  tree.root = buildTablesTree(con)

  def buildTablesTree(con: Connection): TreeItem[String] = {
    val md = con.getMetaData
    // テーブルの一覧 Seq[Record]
    val tables = toSeq(md.getTables(null, user, null, null))
    // テーブル一覧をテーブルの種類別にグループ分けして、テーブル名だけ抜き出す。テーブル種類->Seq(テーブル名)なMap
    val groups = tables.groupBy(_(3)).map(pair => (pair._1, pair._2.map(_(2)))).toMap
    new TreeItem("root") {
      children = toTreeItem[String](groups)
    }
  }

  // 検索結果をSeq[Record]へ変換
  def toSeq(rs: ResultSet): Seq[Record] = {
    val result = ListBuffer[Record]()
    val md = rs.getMetaData
    val columnCount = md.getColumnCount
    while(rs.next()) {
      val columns = (1 to columnCount) map { i => rs.getString(i) }
      result += columns
    }
    result.toList
  }

  // Mapのキーを親ノード、valueを子ノードとして木を作る。
  def toTreeItem[T](map: Map[T, Seq[T]]): Seq[TreeItem[T]] = {
    (map map (e => {
      val parent = new TreeItem(e._1)
      parent.children = e._2.map (v => new TreeItem(v))
      parent
    })).toSeq
  }

  // メタデータからTableColumnを作成
  // JDBCはindexが1から始まる
  def toColumns(md: ResultSetMetaData): Seq[TableColumn[Record, String]] = {
    (1 to md.getColumnCount).map (i => {
      val column = new TableColumn[Record, String](md.getColumnName(i))
      column.cellValueFactory = f => new StringProperty(f.value, "name", f.value(i - 1))
      column
    })
  }

//  implicit class RichPair[T1, T2](val pair: Tuple2[T1, T2]) extends AnyVal {
//    def key = pair._1
//    def value = pair._2
//  }
}