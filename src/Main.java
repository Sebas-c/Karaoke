//Importing required JavaFX components
import java.io.FileNotFoundException;
import java.util.List;

import javafx.application.Application;
import javafx.application.Application.Parameters;
import javafx.stage.Stage;

//Importing other classes
import model.Library;
import model.Song;
import view.Player;

public class Main extends Application {
	
	public static void main(String[] args) {
        launch(args);
        
    }
	
    @Override //Creates a Scene for the GUI
    public void start(Stage stage) {
    	String file = "res/songlist/sample_song_data";
    	
    	//If a parameter was passed, stores the parameter to pass as file path to load
    	Parameters param = getParameters();
    	List<String> args = param.getUnnamed();
    	if(args.size() > 0) {
    		file = "res/songlist/" + args.get(0);
    	}
    	
    	
    	//Create a Player object (main GUI)
		Player player = new Player();
		player.loadLibrary(stage, file);
    }
}