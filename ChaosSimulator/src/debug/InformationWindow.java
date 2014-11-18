package debug;

import java.awt.Color;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

public class InformationWindow extends JFrame{
	private static final long serialVersionUID = 1L;
	
	private int frameWidth = 200;
	private int frameHeight = 800;
	private ChartPanel chartPanel;
	
	private JFreeChart chart;
	private XYSeries totalMemoryUsedSeries;
	private XYSeriesCollection dataset;
	
	public InformationWindow(String windowName, int xVal, int yVal){
		super(windowName);
		
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setSize(frameWidth, frameHeight);
        this.setLocation(xVal, yVal);
        
        createDataset();
        JFreeChart chart = createChart(dataset);
        chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(chartPanel);
        this.pack();

		long time = System.currentTimeMillis();
	}
	
	private long time; 
	private int counter = 1;
	
	public void updateGraph(){
		if(System.currentTimeMillis() < time + 1000){
			return;
		}
		time = System.currentTimeMillis();
		
		int mb = 1024 * 1024; 
		 
		// get Runtime instance
		Runtime instance = Runtime.getRuntime();
 
		System.out.println("***** Heap utilization statistics [MB] *****\n");
 
		// available memory
		System.out.println("Total Memory: " + instance.totalMemory() / mb);
 
		// free memory
		System.out.println("Free Memory: " + instance.freeMemory() / mb);
 
		// used memory
		System.out.println("Used Memory: "
				+ (instance.totalMemory() - instance.freeMemory()) / mb);
 
		// Maximum available memory
		System.out.println("Max Memory: " + instance.maxMemory() / mb);
		
		
		totalMemoryUsedSeries.add(counter, (instance.totalMemory() - instance.freeMemory()) / mb);
        chartPanel.updateUI();
        counter++;
	}
	
	private void createDataset() {
        totalMemoryUsedSeries = new XYSeries("MySeries");
        
        dataset = new XYSeriesCollection();
        dataset.addSeries(totalMemoryUsedSeries);   
    }
	
	private JFreeChart createChart(final XYDataset dataset) {
    
	    // create the chart...
	    final JFreeChart chart = ChartFactory.createXYLineChart(
	        "Line Chart Demo 6",      // chart title
	        "X",                      // x axis label
	        "Y",                      // y axis label
	        dataset,                  // data
	        PlotOrientation.VERTICAL,
	        true,                     // include legend
	        true,                     // tooltips
	        false                     // urls
	    );
	
	    // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
	    chart.setBackgroundPaint(Color.white);
	
	//    final StandardLegend legend = (StandardLegend) chart.getLegend();
	//      legend.setDisplaySeriesShapes(true);
	    
	    // get a reference to the plot for further customisation...
	    final XYPlot plot = chart.getXYPlot();
	    plot.setBackgroundPaint(Color.lightGray);
	//    plot.setAxisOffset(new Spacer(Spacer.ABSOLUTE, 5.0, 5.0, 5.0, 5.0));
	    plot.setDomainGridlinePaint(Color.white);
	    plot.setRangeGridlinePaint(Color.white);
	    
	    final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
	    renderer.setSeriesLinesVisible(0, true);
	    renderer.setSeriesShapesVisible(0, false);
	    plot.setRenderer(renderer);
	
	    // change the auto tick unit selection to integer units only...
	    final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
	    rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
	    // OPTIONAL CUSTOMISATION COMPLETED.
	            
	    return chart;
	    
	}
}
