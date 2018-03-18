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
import java.awt.BasicStroke;
import java.awt.Color;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.DeviationRenderer;
import org.jfree.data.time.RegularTimePeriod;
import org.jfree.data.time.Week;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.YIntervalSeries;
import org.jfree.data.xy.YIntervalSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RectangleInsets;
import org.jfree.ui.RefineryUtilities;

object Hello2 extends App {

    def date(day: Int,month: Int ,year: Int):Date =  {
       val cal = Calendar.getInstance()
       cal.set(year,month,day)
       return cal.getTime()
    }
    drawImage
    def drawImage(){
        val series1=new YIntervalSeries("s1")
        val series2=new YIntervalSeries("s2")
        var t:RegularTimePeriod=new Week()
        var y1= 100.0
        var y2= 100.0
        var i=0
        for (i <- 0 to 52 ){
           val dev1: Double=(0.05 * i)
           series1.add(t.getFirstMillisecond(), y1 , y1 - dev1, y1 + dev1)
           y1 = y1 + Math.random() - 0.45

          val dev2: Double=(0.07 * i)
           series2.add(t.getFirstMillisecond(), y2 , y2 - dev2, y2 + dev2)
           y2 = y2 + Math.random() - 0.45
           t=t.next()
        }

        val collection = new YIntervalSeriesCollection()
        collection.addSeries(series1)
        collection.addSeries(series2)

        //println(collection.getColumnKeys)
        //println(collection.getRowKeys)
        //println(collection.getEndValue(collection.getRowKey(0),"D")) //これでいくらでも値が取れる。
        //println(collection.getPercentComplete(collection.getRowKey(0),"D"))

        val dataset: XYDataset=collection
        val chart3: JFreeChart = ChartFactory.createTimeSeriesChart(
           "Projected Values - Test",
           "Date",
           "Index projection",
           dataset,
           true,true,false
        )
        val plot: XYPlot = chart3.getPlot().asInstanceOf[XYPlot]
        plot.setDomainPannable(true)
        plot.setRangePannable(false)
        plot.setInsets(new RectangleInsets(5,5,5,20))

        val renderer: DeviationRenderer = new DeviationRenderer(true,false)
        renderer.setSeriesStroke(0, new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND))
        renderer.setSeriesStroke(0, new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND))
        renderer.setSeriesStroke(1, new BasicStroke(3.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND))
        renderer.setSeriesFillPaint(0, new Color(255,200,200))
        renderer.setSeriesFillPaint(0, new Color(200,200,255))
        plot.setRenderer(renderer)

        var yAxis: NumberAxis=plot.getRangeAxis().asInstanceOf[NumberAxis]
        yAxis.setAutoRangeIncludesZero(false)
        yAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits())



        val cp: ChartPanel = new ChartPanel(chart3)  
        
        //chart3.show()
        
        //cp.setPreferredSize(new java.awt.Dimension(500,270))
        
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




