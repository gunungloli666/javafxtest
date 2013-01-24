package com.fjr.pid;

import java.io.File;
import javafx.animation.SequentialTransition;
import javafx.animation.SequentialTransitionBuilder;
import javafx.animation.TranslateTransitionBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ButtonBuilder;
import javafx.scene.control.Label;
import javafx.scene.control.LabelBuilder;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SliderBuilder;
import javafx.scene.control.Tab;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.AnchorPaneBuilder;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.HBoxBuilder;
import javafx.scene.layout.StackPane;
import javafx.scene.media.AudioSpectrumListener;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;

public class FunnyPane extends Tab {

	private static AudioSpectrumListener audioSpectrumListener;
	private static String AUDIO_URI;
	private static MediaPlayer audioMediaPlayer;

	ObservableList<File> listMusic;

	Stage primaryStage;

	static boolean mainkan = false;
	static int different = 50;

	SequentialTransition transition;
	// static AnimationTimer timer ;
	static Integer a = 0, b = a + different;
	// add playlist
	static ListView<File> playList;

	private double initY;
	private double newY;

	private static boolean shuffle = true;
	private BorderPane mainPane;
	static private XYChart.Data<String, Number>[] series1Data;

	public FunnyPane(Stage primaryStage) 
	{
		StackPane mainStack = new StackPane();
		mainStack.setPrefSize(GeneralProperty.TAB_PANE_SIZE,
				GeneralProperty.GENERAL_HEIGHT - 50);
		mainStack.getChildren().add(createChart());
		mainStack.getChildren().add(addMusicController());
		this.primaryStage = primaryStage;
		setContent(mainStack);
		setText("Funny");
	}
	
	private  AnchorPane createChart() {
		final CategoryAxis xAxis = new CategoryAxis();
		xAxis.setVisible(false);
		final NumberAxis yAxis = new NumberAxis(0, 50, 10);
		final BarChart<String, Number> bc = new BarChart<String, Number>(xAxis,
				yAxis);
		yAxis.setTickLabelsVisible(false);
		yAxis.setTickMarkVisible(false);
		xAxis.setTickLabelsVisible(false);
		xAxis.setTickMarkVisible(false);
		bc.setPrefSize(400, 400);
		bc.setTranslateX(20);
		bc.setTranslateY(20);
		bc.getStylesheets().add(GeneralProperty.CHART_STYLE);
		bc.setLegendVisible(false);
		bc.setAnimated(false);
		bc.setBarGap(0);
		bc.setCategoryGap(1);
		bc.setVerticalGridLinesVisible(false);
		yAxis.setTickLabelFormatter(new NumberAxis.DefaultFormatter(yAxis,
				null, "dB"));
		XYChart.Series<String, Number> series1 = new XYChart.Series<String, Number>();
		series1.setName("Data Series 1");
		series1Data = new XYChart.Data[128];
		String[] categories = new String[128];
		for (int i = 0; i < series1Data.length; i++) {
			categories[i] = Integer.toString(i + 1);
			series1Data[i] = new XYChart.Data<String, Number>(categories[i], 50);
			series1Data[i].setYValue(0);
			series1.getData().add(series1Data[i]);
		}
		bc.getData().add(series1);
		bc.setLayoutX(20);
		bc.setLayoutY(20);
		
		return AnchorPaneBuilder.create()
				.children(bc)
				.build();
	}
	

	private AnchorPane addMusicController()
	{
		AnchorPane asembli = new AnchorPane();
		mainPane = new BorderPane();
		mainPane.setTranslateX(20);
		mainPane.setTranslateY(20);
		transition = new SequentialTransition();
		transition.getChildren().addAll(
				TranslateTransitionBuilder.create().
				duration(Duration.seconds(1)).fromY(30).toY(240).autoReverse(false).cycleCount(1).
				build(), 
				TranslateTransitionBuilder.create().
				duration(Duration.seconds(1)).fromY(240).toY(70).autoReverse(false).cycleCount(1).
				build(),
				TranslateTransitionBuilder.create().
				duration(Duration.seconds(1)).fromY(70).toY(240).autoReverse(false).cycleCount(1).
				build(),
				TranslateTransitionBuilder.create().
				duration(Duration.seconds(1)).fromY(240).toY(110).autoReverse(false).cycleCount(1).
				build(),
				TranslateTransitionBuilder.create().
				duration(Duration.seconds(1)).fromY(110).toY(240).autoReverse(false).cycleCount(1).
				build(),
				TranslateTransitionBuilder.create().
				duration(Duration.seconds(1)).fromY(240).toY(145).autoReverse(false).cycleCount(1).
				build(),
				TranslateTransitionBuilder.create().
				duration(Duration.seconds(1)).fromY(145).toY(240).autoReverse(false).cycleCount(1).
				build(), 
				TranslateTransitionBuilder.create().
				duration(Duration.seconds(1)).fromY(240).toY(180).autoReverse(false).cycleCount(1).
				build(),
				TranslateTransitionBuilder.create().
				duration(Duration.seconds(1)).fromY(180).toY(240).autoReverse(false).cycleCount(1).
				build(), 
				TranslateTransitionBuilder.create().
				duration(Duration.seconds(1)).fromY(240).toY(210).autoReverse(false).cycleCount(1).
				build(),
				TranslateTransitionBuilder.create().
				duration(Duration.seconds(1)).fromY(210).toY(240).autoReverse(false).cycleCount(1).
				build(),
				TranslateTransitionBuilder.create().
				duration(Duration.seconds(1)).fromY(240).toY(230).autoReverse(false).cycleCount(1).
				build(),
				TranslateTransitionBuilder.create().
				duration(Duration.seconds(1)).fromY(230).toY(240).autoReverse(false).cycleCount(1).
				build()
				);
				
		transition.setNode(mainPane);
//		transition.play();


		playList = new ListView<File>();
		playList.setOrientation(Orientation.VERTICAL);
		playList.setPrefSize(300, 300);

		playList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
		playList.setStyle("-fx-background-color:transparent;");

		playList.setCellFactory(new Callback<ListView<File>, ListCell<File>>() {
			@Override
			public ListCell<File> call(ListView<File> arg0) { 
				return new LabelCell();
			}
			
		});
		
		// add control button
		HBox boxControl = HBoxBuilder
				.create()
				.prefWidth(200)
				.prefHeight(20)
				.spacing(3)
				.style("-fx-background-color: #ffe4e1; -fx-padding: 3  3  3  3; ")
				.children(
						ButtonBuilder.create()
						.text("open")
								.onAction(new EventHandler<ActionEvent>() {
									@Override
									public void handle(ActionEvent arg0)
									{
										System.out.println("open file");
										chooseFile();
										if (listMusic!= null)
										{
											String[] name = new String[listMusic.size()];
											for(int  index=0 ; index < listMusic.size(); index++){
												name[index] = listMusic.get(index).getName();
											}
											playList.setItems(listMusic);
										}
									}
								})
								.maxWidth(Double.MAX_VALUE)
								.build(),
						ButtonBuilder
						.create()
						.text("start")
								.onAction(new EventHandler<ActionEvent>() {
									@Override
									public void handle(ActionEvent arg0) {
										play();
									}
								})
								.maxWidth(Double.MAX_VALUE)
								.build(),
						ButtonBuilder
						.create()
						.text("pause")
								.onAction(new EventHandler<ActionEvent>() {
									@Override
									public void handle(ActionEvent arg0) {
										pause();
									}
								})
									.maxWidth(Double.MAX_VALUE)
								.build(),
								
						ButtonBuilder.create().text("clear")
								.onAction(new EventHandler<ActionEvent>() {
									@Override
									public void handle(ActionEvent arg0) {
										playList.setItems(null);
										stop();
									}
								})
									.maxWidth(Double.MAX_VALUE)
								.build(), 
								
								SliderBuilder.
								create()
								.prefWidth(90)
								.maxWidth(90)
								.minWidth(90)
								.showTickMarks(false)
								.showTickLabels(false)
								.min(0)
								.max(100)
								.value(50)
								.build())
								.maxWidth(Double.MAX_VALUE)
								.build();
		
		HBox boxSlider = HBoxBuilder
				.create()
				.spacing(10)
				.prefWidth(300)
				.style("-fx-background-color: #ffe4e1; -fx-padding: 3  3  3  3;")
				.children(
						LabelBuilder.
						create()
						.text("Time")
						.build(), 
					    SliderBuilder
					    .create()
					    .prefWidth(200)
					    .showTickMarks(false)
					    .showTickLabels(false)
					    .min(0)
					    .max(100)
					    .value(0)
					    .build()
						).
				build();
		
		mainPane.setTop(boxSlider);
		mainPane.setCenter(playList);
		mainPane.setBottom(boxControl);
		asembli.getChildren().add(mainPane);
		
		new NodeTransition(mainPane);
		
		return asembli;
	}
	
	
	public static  class LabelCell extends ListCell<File>{
		
		public LabelCell() {
			// TODO Auto-generated constructor stub
			setStyle("-fx-background-color:  transparent");
		}
		
		public void updateItem(File item, boolean empty)
		{
			super.updateItem(item, empty);
			final Label label;
			final  Text text;
			if(item!= null){
				final File f = item;
				label = new Label();
				label.setText(item.getName());
				setAlignment(Pos.CENTER_LEFT);
				label.setPrefWidth(290);
				label.minWidth(290);
				label.setMaxWidth(290);
				setGraphic(label);
				text = new Text();
				text.setText(item.getName());
				setGraphic(text);
				text.setOnMouseClicked(new EventHandler<MouseEvent>() {
					@Override
					public void handle(MouseEvent arg0)
					{
						AUDIO_URI = f.toURI().toString();
						playMusic();
//						if(FunnyPane.isPlaying()){
//							FunnyPane.stop();
//						}
//						FunnyPane.play();
						
//						if(FunnyPane.isPlaying()){
//							a = 0;
//							b = 0;
//						}
//						FunnyPane.setPlaying(true);
//						AnimationTimer timer ;
//						System.out.println(path);
//						final String tempString  = "                                                                                        ".concat(name).concat("                                                                                        ");
//					
//						timer = new AnimationTimer() {
//							@Override
//							public void handle(long as) {
//								as = 1L;
//								final String subStr = tempString.substring(a, a+different);
//								label.setText(subStr);
//								a++;
//								b++;
//								if(b.intValue() == tempString.length()){
//									a = 0;
//									b = a+different;
//								}
//							}
//						};
//						timer.start();
					}
				});
			
			}
		}
		
	}
	
	public static void setPLaying(boolean state){
		mainkan = state;
	}
	
	public static boolean isMainkan(){
		return mainkan;
	}
	
	public static void playMusic(){
		if(audioMediaPlayer != null){
			audioMediaPlayer.stop();
		}
		Media audioMedia = new Media(AUDIO_URI);
		audioMediaPlayer = new MediaPlayer(audioMedia);
		
		audioSpectrumListener = new AudioSpectrumListener() {
			@Override
			public void spectrumDataUpdate(double timestamp, double duration,
					float[] magnitudes, float[] phases) {
				for (int i = 0; i < series1Data.length; i++) {
					series1Data[i].setYValue(magnitudes[i] + 60);
				}
			}
		};
		audioMediaPlayer.setAudioSpectrumListener(audioSpectrumListener);
		audioMediaPlayer.play();
	}
	
	public static void play(){
		if(audioMediaPlayer!=  null){
			audioMediaPlayer.play();
		}else{
			playListMusic();
		}
	}
	
	public static void playListMusic(){
		if(!isShuffle()){
			for(File f: playList.getItems()){
				AUDIO_URI  = f.toURI().toString();
				playMusic();
			}
		}else{
			
		}
	}
	
	public static void normalisir(){
		for(int i=0; i< series1Data.length; i++){
			series1Data[i].setYValue(0);
		}
	}
	
	public static void setShuffle(boolean state){
		shuffle = state;
	}
	
	public static boolean isShuffle(){
		return shuffle;
	}
	
	public void playSchedule(File f){
		ObservableList<File> list = FXCollections.observableArrayList(playList.getItems());
		
	}
	
	
	public static MediaPlayer getMediaPlayer(){ return audioMediaPlayer;}
	
	private  void chooseFile()
	{	
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter(
				"MP3 Files (*.MP3)", "*.mp3");
		fileChooser.getExtensionFilters().add(filter);
		try{
			listMusic = 	
					FXCollections.observableArrayList(fileChooser
					.showOpenMultipleDialog(primaryStage));
		}catch(Exception e){System.out.println("No music selected");}
	}

	public static void pause() { if(audioMediaPlayer!= null){ audioMediaPlayer.pause();}}
	
	public static  void stop(){if(audioMediaPlayer!= null ){ normalisir(); audioMediaPlayer.stop();}}
	
	
	public static void  animateCurrentPLaying(){}
	
}
