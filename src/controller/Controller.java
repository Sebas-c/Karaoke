package controller;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Queue;

import model.Library;
import model.Playlist;
import model.Song;

public class Controller {
	//VARIABLES
	private Library _lib;
	private Playlist _playlist;

	//CONSTRUCTORS
	public Controller() {
		this._lib = new Library();
		this._playlist = new Playlist();
	}
	
	
	//METHODS
	
	//Loads songs from specified file into Library. Returns -1 if an error occurred
	public Integer loadFile(String filePath) {
		try {
			_lib.populateLibrary(filePath);
			return 0;
		} catch (FileNotFoundException exception) {
			//Returns error code to GUI
			return -1;
		}
	}
	
	//Checks if the playlist is empty
	public boolean isPlaylistEmpty() {
		return _playlist.isEmpty();
	}
	
	//Returns the file to play as a String
	public String getFileToPlay() {
		return _playlist.getHead().get_fileName();
	}
	
	//Adds a song to the playlist
	public void addSong(Song song) {
		_playlist.addSong(song);
	}
	
	//Deletes song from the play-list
	public void deleteSong() {
		_playlist.removeHead();
	}
	
	//Deletes specific song
	public void removeAt(Song song) {
		_playlist.removeAt(song);
	}
	
	//Retrieves all songs from the play-list
	public Queue<Song> getAllPlaylist() {
		return _playlist.getPlaylist();
	}
	
	//Returns an array of Songs with titles matching the String passed as argument
	public Song[] search(String input) {
		return _lib.search(input);
	}
}
