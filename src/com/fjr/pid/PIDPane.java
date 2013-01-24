package com.fjr.pid;

import java.util.Random;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.LabelBuilder;
import javafx.scene.control.SliderBuilder;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.AnchorPaneBuilder;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderPaneBuilder;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.VBoxBuilder;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.CubicCurveTo;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathElement;
import javafx.util.Pair;

public class PIDPane extends Tab {

	private double proportionalValue;
	private double differentialValue;
	private double integralValue;
	private double desiredValue;
	private double processVariable;

	private double initY;
	private double newY;
	AnchorPane pane;
	private double totalTime;
	private double samplingTime;
	private double feed1, feed2; 
	private double Kp, Ki, Kd; 
	NumberAxis xAxis , yAxis;
	private boolean includeNumberAxis = true;
	
	// HBox mainBox;
	BorderPane mainBorderPane;
	int numberOfSamples ;
	private double[] proportional ;
	private double[] derivative ;
	private double[] integral ;
	private double[] I ;
	private double[] PID ;
	private double[] error  ;
//	private double[] state1 ;
//	private double[] state2 ;
	private double[] STATE1 ;
	private double[] STATE2 ;
	private double[] feedback ;
	private double[] output ;
	
	//untuk setting ban berjalan
	private double oldError = 0;
	private double oldProportional= 0;
	private double oldDerivative = 0;
	private double oldIntegral = 0;
	private double oldSTATE1 = 0; 
	private double oldstate2  = 0 ;
	private double oldSTATE2 = 0;
	private double oldFeedback = 0;
	private double oldI; 
	private double sumOfIntegral = 0;
	private double oldPID = 0;
	private double sumOfPID = 0;
	private double sumOfstate2 = 0;
	private double oldoutput = 0;
	
	
	private XYChart<Number,Number> chart; 
	
	private XYChart.Data<Number, Number>[]  seriesData ;
	

	public PIDPane() 
	{
		AnchorPane mainPane = new AnchorPane();
		mainPane.getChildren().addAll(getChart(), addControl());
		new NodeTransition(pane);
		setText("PID");
		setContent(mainPane);
		System.out.println("Hai");
		
//		setUpParameter(); //first test
//		startPID();
	}
	
	/*
	private void setUpParameter() 
	{
		System.out.println("setup parameter:");
		samplingTime = 0.01;
		totalTime = 10;
		desiredValue = 12;
		feed1 = 1;
		feed2 = 1;
		Kp = 1;
		Ki = 0.01;
		Kd = 0.01;

		numberOfSamples = (int) Math.round(totalTime / samplingTime);
		System.out.println("Number of sample time  = "+numberOfSamples);
		
		proportional = new double[numberOfSamples];
		derivative = new double[numberOfSamples];
		integral = new double[numberOfSamples];
		I = new double[numberOfSamples];
		PID = new double[numberOfSamples];
		error = new double[numberOfSamples];
		state1 = new double[numberOfSamples];
		state2 = new double[numberOfSamples];
		STATE1 = new double[numberOfSamples];
		STATE2 = new double[numberOfSamples];
		feedback = new double[numberOfSamples];
		output = new double[numberOfSamples];
	}
	*/
	
	public void setKp(double kp){
		this.Kp  = kp;
	}
	
	public void setKi(double ki){
		this.Ki = ki;
	}
	
	public void setKd(double kd){
		this.Kd= kd;
	}
	
	public void setNumberOfSample(int number){
		this.numberOfSamples = number;
	}
	
	public void setSamplingTime(double dt){
		this.samplingTime = dt;
	}
	
	public void setTime(double time){
		this.totalTime = time;
	}

	//ini saya translate dari MATLAB file exchange
	
//	public void startPID() 
//	{
//		System.out.println("Start PID:");
//		
//		for(int i=0; i< numberOfSamples-1 ;i++)
//		{
//			error[i+1] = desiredValue - feedback[i];
//			proportional[i+1] = error[i+1];
//			derivative[i+1] = (error[i+1] - error[i])/samplingTime;
//			integral[i+1] = (error[i+1]+ error[i]) * samplingTime /2;
//			I[i+1] = sum(integral);
//			PID[i+1] = Kp*proportional[i] + Ki * I[i+1] + Kd * derivative[i];
//			
//			STATE1[i+1] = sum(PID);
//			state2[i+1] = (STATE1[i+1] + STATE1[i])*samplingTime /2;
//			STATE2[i+1] = sum(state2);			
//			output[i+1] = (STATE2[i+1] + STATE2[i]) * samplingTime/2; 
//			feedback[i+1]  = state2[i+1] *feed1 + output[i+1] *feed2;
//		}
//	}
	
	public void calculate()
	{
		double newError = desiredValue - oldFeedback;
		double newProportional  = newError;
		double newDerivative = (newError - oldError)/ samplingTime;
		double newIntegral = (newError + oldError)*samplingTime /2;
		sumOfIntegral+= newIntegral;
		double newI = sumOfIntegral;
		double newPID  = Kp* oldProportional + Ki * newIntegral + Kd * oldDerivative;
		
		sumOfPID += newPID;
		double newSTATE1 = sumOfPID;
		double newstate2  = (newSTATE1 + oldSTATE1) *samplingTime /2; 
		sumOfstate2 += newstate2;
		double newSTATE2 = sumOfstate2; 
		double newoutput  = (newSTATE2 + oldSTATE2) * samplingTime /2; 
		double newFeedback = newstate2 * feed1 + newoutput *feed2;
		
		//setting for nex iteration
		oldFeedback = newFeedback;
		oldError = newError;
		oldProportional  = newProportional;
		oldDerivative = newDerivative;
		oldI = newI;
		oldIntegral = newIntegral;
		oldSTATE1 = newSTATE1;
		oldSTATE2 = newSTATE2;
		
	}
	
	public void startPID(){
		
	}
	
	public void  printArray(double[] array){
		for(int i=0; i< array.length ;i++){
			System.out.print(array[i]+ " | ");
		}
		System.out.println();
	}
	
	public double  sum(double[] array){
		double result = 0; 
		for(int i=0; i< array.length ;i++){
			result+= array[i];
		}
		return result;
	}
	
	public void plot()
	{
		//get the highest value, Maybe not important and not effisien
		double highestValue = 0;
		for(int i=0;i< output.length; i++){
			if (output[i]> highestValue)
				highestValue = output[i];
		}
		
		xAxis = new NumberAxis(0, numberOfSamples, 100);
		yAxis = new NumberAxis(0, highestValue + 1, 1);
		chart = new AreaChart<Number, Number>(xAxis, yAxis);
		
		seriesData = new XYChart.Data[(int) numberOfSamples];
		XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
		for(int i=0; i< numberOfSamples ;i++){
			seriesData[i] = new XYChart.Data<Number,Number>(i,output[i]);
			series.getData().add(seriesData[i]);
		}
		chart.getData().add(series);
		chart.setLegendVisible(false);
		
//		xAxis = new NumberAxis(0, numberOfSamples, 100);
//		yAxis = new NumberAxis(0, highestValue + 1, 1);
//		chart = new AreaChart<Number, Number>(xAxis, yAxis);
//		seriesData = new XYChart.Data[(int) numberOfSamples];
//		XYChart.Series<Number, Number> series = new XYChart.Series<Number, Number>();
//		for(int i=0; i< numberOfSamples ;i++){
//			seriesData[i] = new XYChart.Data<Number,Number>(i,output[i]);
//			series.getData().add(seriesData[i]);
//		}
//		chart.getData().add(series);
//		chart.setLegendVisible(false);
	}

	public void setDesiredValue(double desiredValue) {
		this.desiredValue = desiredValue;
	}

	private AnchorPane getChart() {
//		setUpParameter(); 
//		startPID();
		plot();
		AnchorPane panechart = AnchorPaneBuilder.create()
				.translateX(190)
				.translateY(20)
				.style("-fx-background-color:  #e6e6fa;") // untuk ngetes aja
				.children(chart)
				.build();
		new NodeTransition(panechart);
		return panechart ;
	}
	
	private AnchorPane addControl() {
		
		return pane =
				AnchorPaneBuilder.create()
				.maxWidth(200).maxHeight(200)
				.translateX(20)
				.translateY(20)
				.children(
						BorderPaneBuilder.create()
						.center(
								HBoxBuilder.create()
								.spacing(10)
								.style("-fx-background-color:  #e6e6fa; -fx-padding: 3  3  3  3; ")
								.children(
										VBoxBuilder.create().alignment(Pos.CENTER)
											.children(
												SliderBuilder.create()
													.prefHeight(150)
													.orientation(Orientation.VERTICAL)
													.build(),
												LabelBuilder.create()
													.text("P")
													.alignment(Pos.CENTER)
													.build())
											.build(),

										VBoxBuilder.create()
											.alignment(Pos.CENTER)
											.children(
													SliderBuilder.create()
														.prefHeight(150)
														.orientation(Orientation.VERTICAL)
														.build(),
													LabelBuilder.create().text("I").alignment(Pos.CENTER)
														.build())
											.build(),
											VBoxBuilder.create().alignment(Pos.CENTER)
													.children(SliderBuilder.create()
															.prefHeight(150)
															.orientation(Orientation.VERTICAL).build(),
															LabelBuilder.create().text("D").alignment(Pos.CENTER).build())
													.build(),
											VBoxBuilder.create().alignment(Pos.CENTER)
												.children(
														SliderBuilder.create()
															.prefHeight(150)
															.orientation(Orientation.VERTICAL).build(),
														LabelBuilder.create().text("SP").build())
															.build(),
											AnchorPaneBuilder.create()
											.children(
														VBoxBuilder.create().spacing(5).alignment(Pos.CENTER).layoutY(2)
															.children(
																	ButtonBuilder.create().text("START")
																		.onAction(new EventHandler<ActionEvent>() 
																				{@Override public void handle(ActionEvent arg0) {
																							}
																				})
																		.build(),
																	ButtonBuilder.create().text("PAUSE")
																		.onAction(new EventHandler<ActionEvent>() 
																				{@Override public void handle(ActionEvent arg0) {
																			}
																		})
																		.build()
																		)
											.build())
										.build())

								.build()
								) // end center main borderpane 
//							.top(
//								BorderPaneBuilder.create().
//									left(CircleBuilder.create()
//											.radius(10.0f)
//											.build())
//									.build()
//								) // end center inner  border pane
						.build()
						).// end children anchor pane
				build();
	}
	
	
	 public static class CurveFittedAreaChart extends AreaChart<Number, Number> {

	        public CurveFittedAreaChart(NumberAxis xAxis, NumberAxis yAxis) {
	            super(xAxis, yAxis);
	        }

	        /**
	         * @inheritDoc
	         */
	        @Override
	        protected void layoutPlotChildren() {
	            super.layoutPlotChildren();
	            for (int seriesIndex = 0; seriesIndex < getDataSize(); seriesIndex++) {
	                final XYChart.Series<Number, Number> series = getData().get(seriesIndex);
	                final Path seriesLine = (Path) ((Group) series.getNode()).getChildren().get(1);
	                final Path fillPath = (Path) ((Group) series.getNode()).getChildren().get(0);
	                smooth(seriesLine.getElements(), fillPath.getElements());
	            }
	        }

	        private int getDataSize() {
	            final ObservableList<XYChart.Series<Number, Number>> data = getData();
	            return (data != null) ? data.size() : 0;
	        }

	        private static void smooth(ObservableList<PathElement> strokeElements, ObservableList<PathElement> fillElements) {
	            // as we do not have direct access to the data, first recreate the list of all the data points we have
	            final Point2D[] dataPoints = new Point2D[strokeElements.size()];
	            for (int i = 0; i < strokeElements.size(); i++) {
	                final PathElement element = strokeElements.get(i);
	                if (element instanceof MoveTo) {
	                    final MoveTo move = (MoveTo) element;
	                    dataPoints[i] = new Point2D(move.getX(), move.getY());
	                } else if (element instanceof LineTo) {
	                    final LineTo line = (LineTo) element;
	                    final double x = line.getX(), y = line.getY();
	                    dataPoints[i] = new Point2D(x, y);
	                }
	            }
	            // next we need to know the zero Y value
	            final double zeroY = ((MoveTo) fillElements.get(0)).getY();

	            // now clear and rebuild elements
	            strokeElements.clear();
	            fillElements.clear();
	            Pair<Point2D[], Point2D[]> result = calcCurveControlPoints(dataPoints);
	            Point2D[] firstControlPoints = result.getKey();
	            Point2D[] secondControlPoints = result.getValue();
	            // start both paths
	            strokeElements.add(new MoveTo(dataPoints[0].getX(), dataPoints[0].getY()));
	            fillElements.add(new MoveTo(dataPoints[0].getX(), zeroY));
	            fillElements.add(new LineTo(dataPoints[0].getX(), dataPoints[0].getY()));
	            // add curves
	            for (int i = 1; i < dataPoints.length; i++) {
	                final int ci = i - 1;
	                strokeElements.add(new CubicCurveTo(
	                        firstControlPoints[ci].getX(), firstControlPoints[ci].getY(),
	                        secondControlPoints[ci].getX(), secondControlPoints[ci].getY(),
	                        dataPoints[i].getX(), dataPoints[i].getY()));
	                fillElements.add(new CubicCurveTo(
	                        firstControlPoints[ci].getX(), firstControlPoints[ci].getY(),
	                        secondControlPoints[ci].getX(), secondControlPoints[ci].getY(),
	                        dataPoints[i].getX(), dataPoints[i].getY()));
	            }
	            // end the paths
	            fillElements.add(new LineTo(dataPoints[dataPoints.length - 1].getX(), zeroY));
	            fillElements.add(new ClosePath());
	        }

	        /**
	         * Calculate open-ended Bezier Spline Control Points.
	         *
	         * @param dataPoints Input data Bezier spline points.
	         */
	        public static Pair<Point2D[], Point2D[]> calcCurveControlPoints(Point2D[] dataPoints) {
	            Point2D[] firstControlPoints;
	            Point2D[] secondControlPoints;
	            int n = dataPoints.length - 1;
	            if (n == 1) { // Special case: Bezier curve should be a straight line.
	                firstControlPoints = new Point2D[1];
	                // 3P1 = 2P0 + P3
	                firstControlPoints[0] = new Point2D(
	                        (2 * dataPoints[0].getX() + dataPoints[1].getX()) / 3,
	                        (2 * dataPoints[0].getY() + dataPoints[1].getY()) / 3);

	                secondControlPoints = new Point2D[1];
	                // P2 = 2P1 – P0
	                secondControlPoints[0] = new Point2D(
	                        2 * firstControlPoints[0].getX() - dataPoints[0].getX(),
	                        2 * firstControlPoints[0].getY() - dataPoints[0].getY());
	                return new Pair<Point2D[], Point2D[]>(firstControlPoints, secondControlPoints);
	            }

	            // Calculate first Bezier control points
	            // Right hand side vector
	            double[] rhs = new double[n];

	            // Set right hand side X values
	            for (int i = 1; i < n - 1; ++i) {
	                rhs[i] = 4 * dataPoints[i].getX() + 2 * dataPoints[i + 1].getX();
	            }
	            rhs[0] = dataPoints[0].getX() + 2 * dataPoints[1].getX();
	            rhs[n - 1] = (8 * dataPoints[n - 1].getX() + dataPoints[n].getX()) / 2.0;
	            // Get first control points X-values
	            double[] x = GetFirstControlPoints(rhs);

	            // Set right hand side Y values
	            for (int i = 1; i < n - 1; ++i) {
	                rhs[i] = 4 * dataPoints[i].getY() + 2 * dataPoints[i + 1].getY();
	            }
	            rhs[0] = dataPoints[0].getY() + 2 * dataPoints[1].getY();
	            rhs[n - 1] = (8 * dataPoints[n - 1].getY() + dataPoints[n].getY()) / 2.0;
	            // Get first control points Y-values
	            double[] y = GetFirstControlPoints(rhs);

	            // Fill output arrays.
	            firstControlPoints = new Point2D[n];
	            secondControlPoints = new Point2D[n];
	            for (int i = 0; i < n; ++i) {
	                // First control point
	                firstControlPoints[i] = new Point2D(x[i], y[i]);
	                // Second control point
	                if (i < n - 1) {
	                    secondControlPoints[i] = new Point2D(2 * dataPoints[i + 1].getX() - x[i + 1], 2
	                            * dataPoints[i + 1].getY() - y[i + 1]);
	                } else {
	                    secondControlPoints[i] = new Point2D((dataPoints[n].getX() + x[n - 1]) / 2,
	                            (dataPoints[n].getY() + y[n - 1]) / 2);
	                }
	            }
	            return new Pair<Point2D[], Point2D[]>(firstControlPoints, secondControlPoints);
	        }

	        /**
	         * Solves a tridiagonal system for one of coordinates (x or y) of first
	         * Bezier control points.
	         *
	         * @param rhs Right hand side vector.
	         * @return Solution vector.
	         */
	        private static double[] GetFirstControlPoints(double[] rhs) {
	            int n = rhs.length;
	            double[] x = new double[n]; // Solution vector.
	            double[] tmp = new double[n]; // Temp workspace.
	            double b = 2.0;
	            x[0] = rhs[0] / b;
	            for (int i = 1; i < n; i++) {// Decomposition and forward substitution.
	                tmp[i] = 1 / b;
	                b = (i < n - 1 ? 4.0 : 3.5) - tmp[i];
	                x[i] = (rhs[i] - x[i - 1]) / b;
	            }
	            for (int i = 1; i < n; i++) {
	                x[n - i - 1] -= tmp[n - i] * x[n - i]; // Backsubstitution.
	            }
	            return x;
	        }
	    }

	public double getIntegral() {
		return integralValue;
	}

	public double getDifferential() {
		return differentialValue;
	}

	public double getProportional() {
		return proportionalValue;
	}
	
}



