package view;

import javafx.scene.media.Media;
import javafx.scene.media.MediaException;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.Queue;

import controller.Controller;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Song;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class Player {
	//VARIABLES
	private Controller _cont;
	
	//CONSTRUCTORS
	public Player() {
		this._cont = new Controller();
	}
	
	
	//METHODS
	
	//Calls the method to load the library and shows error message if it does not work. Otherwise shows the GUI.
	public void loadLibrary(Stage stage, String filepath) {
		//If library is loaded successfully, show GUI, else show an error message.
		if (_cont.loadFile(filepath) == 0) {
			showGUI(stage);
		}else {
			//Setting the error screen on the GUI
	        stage.setTitle("Kara-Omar-Oke");
	        
	        //Creating a VBox
	        VBox vbox = new VBox();
	        vbox.setPadding(new Insets(10));
	        vbox.setSpacing(8);
	        vbox.setAlignment(Pos.CENTER);
	        
	        //Creating a Label
	        Label errorLabel = new Label("An error occured while loading the song list. Please ensure the file is in the correct folder and/or that you pased it as an argument properly.");
	        
	        //Creating a button to close the app.
	        Button btnClose = new Button("Close the app.");
	        
	        btnClose.setOnAction(eventClose ->
	        {
	        	stage.close();
	        });
	        
	        //Adding label and button to the VBox
	        vbox.getChildren().addAll(errorLabel, btnClose);
	        
	        Scene mainScene = new Scene(new StackPane(vbox), 1280, 720);
	        
	        stage.setScene(mainScene);
	        stage.show();
		}
	}
	
	
	//Loads the main GUI
	public void showGUI(Stage stage) {
		
//		_cont.printTokens();

		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(20));
		
		RowConstraints row1 = new RowConstraints();
		row1.setPercentHeight(75);
		gridPane.getRowConstraints().add(row1);
		
		//Creating buttons, styling them and adding them to the gridpane
		Button btnPlay = new Button("Play >");
		Button btnPause = new Button("Pause ||");
		Button btnStop = new Button("Stop []");
		Button btnNext = new Button("Next >|");
		
		btnPlay.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		btnPause.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		btnStop.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		btnNext.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		
		TilePane tileButtons = new TilePane(Orientation.HORIZONTAL);
		tileButtons.setPadding(new Insets(0, 0, 0, 70));
		tileButtons.setHgap(10.0);
		tileButtons.setVgap(8.0);
		tileButtons.getChildren().addAll(btnPlay, btnPause, btnStop, btnNext);
		
		gridPane.add(tileButtons, 1, 1, 6, 1);
		
		//Adding a table for the playlist
		TableView<Song> tableView = new TableView<Song>();
    	tableView.setEditable(false);
    	
    	//Columns
    	//First column: Title
    	TableColumn<Song, String> colTitle = new TableColumn<Song, String>("Title");
    	colTitle.setMinWidth(150);
    	colTitle.setCellValueFactory(new PropertyValueFactory<Song, String>("_title"));
    	
    	//Second column: Artist
    	TableColumn<Song, String> colArtist = new TableColumn<Song, String>("Artist");
    	colArtist.setMinWidth(150);
    	colArtist.setCellValueFactory(new PropertyValueFactory<Song, String>("_artist"));
    	
    	//Third column: Duration
    	TableColumn<Song, Integer> colDuration = new TableColumn<Song, Integer>("Duration");
    	colDuration.setMinWidth(150);
    	colDuration.setCellValueFactory(new PropertyValueFactory<Song, Integer>("_duration"));
    	
    	//Adding all columns to the table
    	tableView.getColumns().addAll(colTitle, colArtist, colDuration);
    	tableView.setMaxWidth(450);
    	
    	//Populating the table if the playlist is not empty
    	if(!_cont.isPlaylistEmpty()) {
    		Queue<Song> playlist = _cont.getAllPlaylist();
    		
    		for (Song song : playlist) {
	    		tableView.getItems().addAll(song);
			}
    	}else {
    		tableView.setPlaceholder(new Label("The songs you add to your playlist will show here."));
    	}
    	
    	gridPane.add(tableView, 7, 0, 3, 1);
		
		//Adding buttons for the playlist
		Button btnRemove = new Button("Remove song");
		Button btnSearch = new Button("Search songs");
		
		TilePane tableButtons = new TilePane(Orientation.HORIZONTAL);
		tableButtons.setPadding(new Insets(0, 0, 0, 120));
		tableButtons.setHgap(10.0);
		tableButtons.setVgap(8.0);
		tableButtons.getChildren().addAll(btnRemove, btnSearch);
		
		gridPane.add(tableButtons, 7, 1, 3, 1);
		
		//If the playlist is not empty, load first video
		if(!_cont.isPlaylistEmpty()) {
			String path = _cont.getFileToPlay();
			path = "res/videofiles/" + path;
			
			try {
				//Creates a media file containing the video to play		
				Media video = new Media(new File(path).toURI().toString());
				
				//Creates a mediaplayer to control the video
				MediaPlayer mediaplayer = new MediaPlayer(video);
				
				
				//Creates a mediaview to play the video
				MediaView mediaview = new MediaView(mediaplayer);
				
				//Adds mediaview to the gridpane with colIndex 0, rowIndex 0, colSpan 6, rowSpan 8.
				gridPane.add(mediaview, 0, 0, 6, 1);

				
				//Assigning an event to the buttons
		    	btnPlay.setOnAction(event ->
		        {
		        	mediaplayer.play();
		        });
		    	btnPause.setOnAction(event ->
		        {
		        	mediaplayer.pause();
		        });
		    	btnStop.setOnAction(event ->
		        {
		        	mediaplayer.stop();
		        });
		    	btnNext.setOnAction(event ->
		        {
		        	mediaplayer.stop();
		        	if(!_cont.isPlaylistEmpty()) {

			        	_cont.deleteSong();
		        	}
		        	//Not pretty but since the media player can only handle a single media at a time, we reload the GUI
		        	//This will prompt it to check once again if the playlist is empty and then get the new media & mediaplayer ready
		        	stage.close();
	        		showGUI(stage);
		        });
		    	btnRemove.setOnAction(event ->
		        {
		        	//Stores the object to remove
		        	if(tableView.getSelectionModel().getSelectedItem() != null) {
		        		Song songToRemove = tableView.getSelectionModel().getSelectedItem();
		        		//Pass it to the controller
			        	_cont.removeAt(songToRemove);
		        	}
		        	//If the playlist is not empty (i.e. we did not remove the only song in it), repopulate
		        	if(!_cont.isPlaylistEmpty()) {
		        		tableView.getItems().clear();
		        		Queue<Song> playlist = _cont.getAllPlaylist();
		        		
		        		for (Song song : playlist) {
		    	    		tableView.getItems().addAll(song);
		    			}
		        		tableView.refresh();
		        	}else { //Else, tell use to add songs
		        		tableView.getItems().clear();
		        		tableView.setPlaceholder(new Label("The songs you add to your playlist will show here."));
		        	}
		        	tableView.refresh();
		        });
			}catch (MediaException ex) {
				Label errorMessage = new Label("The media file is missing. Please ensure that the media is correctly located. \nIf you are unsure as to where videos should be stored, check the readme file.");
				gridPane.add(errorMessage, 0, 0, 6, 1);
			}
			
			
		}//Else load a rectangle instead of the MediaView
		else {
			Rectangle rectangle = new Rectangle();
			rectangle.setWidth(480);
			rectangle.setHeight(360);
			rectangle.setFill(Color.BLACK);
			gridPane.add(rectangle, 0, 0, 6, 1);
		}
		
		
    	btnSearch.setOnAction(event ->
        {
        	btnStop.fireEvent(event);
        	stage.close();
        	searchGUI(stage);
        });
		
        Scene mainScene = new Scene(new StackPane(gridPane), 1280, 720);
        stage.setTitle("Kara-Omar-Oke");
        
        
        stage.setScene(mainScene);
        stage.show();
	}
	
	private void searchGUI(Stage stage) {
		//Creating a VBox to contain all the elements of the GUI vertically
		VBox vBox = new VBox();
		vBox.setAlignment(Pos.CENTER);
		vBox.setSpacing(10);
		vBox.setMaxWidth(450);
		
		//Creating a TextFields for the search
		TextField textFieldSearch = new TextField();
		
		//Adding a table for the playlist
		TableView<Song> tableView = new TableView<Song>();
    	tableView.setEditable(false);
    	
    	//Columns
    	//First column: Title
    	TableColumn<Song, String> colTitle = new TableColumn<Song, String>("Title");
    	colTitle.setMinWidth(150);
    	colTitle.setCellValueFactory(new PropertyValueFactory<Song, String>("_title"));
    	
    	//Second column: Artist
    	TableColumn<Song, String> colArtist = new TableColumn<Song, String>("Artist");
    	colArtist.setMinWidth(150);
    	colArtist.setCellValueFactory(new PropertyValueFactory<Song, String>("_artist"));
    	
    	//Third column: Duration
    	TableColumn<Song, Integer> colDuration = new TableColumn<Song, Integer>("Duration");
    	colDuration.setMinWidth(150);
    	colDuration.setCellValueFactory(new PropertyValueFactory<Song, Integer>("_duration"));
    	
    	//Adding all columns to the table
    	tableView.getColumns().addAll(colTitle, colArtist, colDuration);
    	tableView.setMaxWidth(450);
    	
    	tableView.setPlaceholder(new Label("Type in the textfield and hit the search button. \n       You can try \"Kanye\" for example ;)"));
    	
    			
		//Creating buttons and their behaviour
		Button btnSearch = new Button("Search");
		Button btnAdd = new Button("Add selected song to the playlist");
		Button btnBack = new Button("Back to the Kara-Omar-Oke");
		
		//Performs a search based on user input and populates the tableView with the result
		btnSearch.setOnAction(event ->
        {
        	tableView.getItems().clear();
        	Song[] songsFound = _cont.search(textFieldSearch.getText()); //Searches through the library for 
        	//Populating the table if the search result is not empty
        	if(songsFound.length != 0) {
        		        		
        		for (Song song : songsFound) {
    	    		tableView.getItems().addAll(song);
    			}
        	}else {
        		tableView.setPlaceholder(new Label("No results found. Try another word :)"));
        	}
        	tableView.refresh();
        });
		
		//Adds the selected song to the playlist
		btnAdd.setOnAction(event ->
        {
        	if(tableView.getSelectionModel().getSelectedItem() != null) { //Checks if a song is selected (i.e. song object not null)
            	_cont.addSong(tableView.getSelectionModel().getSelectedItem());
        	}
        });
		
		//Shows main GUI
		btnBack.setOnAction(event ->
        {
        	stage.close();
        	showGUI(stage);
        });
		
		vBox.getChildren().addAll(textFieldSearch, tableView, btnSearch, btnAdd, btnBack);
		
		Scene mainScene = new Scene(new StackPane(vBox), 480, 650);
        stage.setTitle("Kara-Omar-Oke - Search for songs");
        
        stage.setOnCloseRequest(event -> {
            showGUI(stage);
        });
        
        stage.setScene(mainScene);
        stage.show();
	}
}
