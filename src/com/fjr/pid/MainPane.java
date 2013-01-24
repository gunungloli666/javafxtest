package com.fjr.pid;


import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.BubbleChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.AnchorPaneBuilder;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.scene.text.TextBuilder;
import javafx.stage.Stage;

public class MainPane extends Application {

	AnchorPane controlPane;
	PIDPane pidPane;
	FunnyPane funnyPane ;


	@Override
	public void start(Stage primaryStage)  {

		BorderPane mainPane = new BorderPane();
		pidPane = new PIDPane();
		funnyPane = new FunnyPane(primaryStage);
		//asembli pane-pane ke dalam sebuah TabPane
		TabPane tabPaneDisplay = new  TabPane();
		tabPaneDisplay.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		

		tabPaneDisplay.getTabs().add(pidPane);

		tabPaneDisplay.getTabs().add(funnyPane);
		
		
		StackPane  stackTab  = new StackPane();
		stackTab.setAlignment(Pos.TOP_LEFT);
		stackTab.getChildren().add(tabPaneDisplay);		 
		
		mainPane.setCenter(stackTab);
		

		Scene mainScene = new Scene(mainPane, GeneralProperty.GENERAL_WIDTH,
				GeneralProperty.GENERAL_HEIGHT);

		mainPane.getStylesheets().add(GeneralProperty.SCENE_STYLE);
		mainPane.getStyleClass().add("main");

		primaryStage.setScene(mainScene);
		primaryStage.setTitle("Control PID");
		primaryStage.show();
	}
	
	public StackPane credit() {
		StackPane pane = new StackPane();
		return pane;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
