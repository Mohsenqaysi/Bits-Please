import java.awt.Color;

import org.knowm.xchart.PieChart;
import org.knowm.xchart.PieChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.demo.charts.ExampleChart;

public class PieChart02 implements ExampleChart<PieChart> {
	
	int[] prefs = new int[10];
	 public  PieChart02(int[] pref) {
		prefs = pref;
	}
  @Override
  public PieChart getChart() {
 
    // Create Chart
	    PieChart chart = new PieChartBuilder().width(800).height(600).title("Students Satisfaction").build();

    // Series
	    for(int i=0; i < prefs.length; i++){
	    chart.addSeries("Preference "+(i+1),prefs[i]);
	    } 
    return chart;
  }
 
}