package example

import scalax.chart.api._

import java.util.Calendar;
import java.util.Date;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.IntervalCategoryDataset;
import org.jfree.data.gantt.Task;
import org.jfree.data.gantt.TaskSeries;
import org.jfree.data.gantt.TaskSeriesCollection;
import org.jfree.data.time.SimpleTimePeriod;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import scala.swing._
import java.awt.BorderLayout

object Hello extends App {

    def date(day: Int,month: Int ,year: Int):Date =  {
       val cal = Calendar.getInstance()
       cal.set(year,month,day)
       return cal.getTime()
    }
    drawImage
    def drawImage(){
      //println(greeting)
        val data = for (i <- 1 to 5) yield (i,i)
        val chart = XYLineChart(data)
        chart.saveAsPNG("/tmp/chart.png")
        //val series = new XYSeries("f(x) = sin(x)")
        //val chart2 = XYLineChart(series)
        //chart2.show()
        //for (x <- -4.0 to 4 by 0.1) {
        //  swing.Swing onEDT {
        //   series.add(x,math.sin(x))
        //}
        //Thread.sleep(20)

        val s1: TaskSeries = new TaskSeries("Scheduled")
        s1.add(new Task("A", new SimpleTimePeriod(date(1,Calendar.APRIL,2001),date(5, Calendar.APRIL, 2001))))
        s1.add(new Task("B", new SimpleTimePeriod(date(9,Calendar.APRIL,2001),date(9, Calendar.APRIL, 2001))))
        s1.add(new Task("C", new SimpleTimePeriod(date(10,Calendar.APRIL,2001),date(5, Calendar.MAY, 2001))))
        s1.add(new Task("D", new SimpleTimePeriod(date(6,Calendar.MAY,2001),date(30, Calendar.MAY, 2001))))
      
        val s2: TaskSeries = new TaskSeries("Actual")
        
        
        s2.add(new Task("C", new SimpleTimePeriod(date(10,Calendar.APRIL,2001),date(15, Calendar.MAY, 2001))))
        s2.add(new Task("D", new SimpleTimePeriod(date(15,Calendar.MAY,2001),date(17, Calendar.JUNE, 2001))))
        s2.add(new Task("B", new SimpleTimePeriod(date(9,Calendar.APRIL,2001),date(9, Calendar.APRIL, 2001))))
        s2.add(new Task("A", new SimpleTimePeriod(date(1,Calendar.APRIL,2001),date(5, Calendar.APRIL, 2001))))
      
        val collection = new TaskSeriesCollection()
        collection.add(s1)
        collection.add(s2)

        println(collection.getColumnKeys)
        println(collection.getRowKeys)
        println(collection.getEndValue(collection.getRowKey(0),"D")) //これでいくらでも値が取れる。
        println(collection.getPercentComplete(collection.getRowKey(0),"D"))

        val dataset: IntervalCategoryDataset=collection
        val chart3: JFreeChart = ChartFactory.createGanttChart(
          "GChart",
          "Task",
          "Date",
          dataset, true, true, false)
        //chart3.show()
        val chartPanel: ChartPanel = new ChartPanel(chart3)
        chartPanel.setPreferredSize(new java.awt.Dimension(500,270))
        
        //def top=new MainFrame{
        //peer.setContentPane(chartPanel)
        //peer.setLocationRelativeTo(null)
        //}
        val chartFrame: ChartFrame = new ChartFrame("aaa",chart3) 
        chartFrame.pack()
        chartFrame.setVisible(true)
        //setContentPane(chartPanel)
        //chartPanel.show
        //chartPanel.setContentPane(chartPanel)
        Thread.sleep(2)
    }


  //   def main(args: Array[String]){
  //       if(args.size == 0) {
  //         //val strs = Array("Hello", "World")
  //         drawImage()
  //       } else {
  //             printf("Invalid args.")
  //       }
  // }



  

}

trait Greeting {
  lazy val greeting: String = "hello"
}


