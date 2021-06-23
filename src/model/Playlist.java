package model;


import java.util.LinkedList;
import java.util.Queue;

public class Playlist {
	//VARIABLES
	private Queue<Song> _playlist = new LinkedList<Song>();
	
	//CONSTRUCTOR
	public Playlist(){
	}
	
	//METHODS
	//Adds a song to the playlist
	public void addSong(Song song) {
		_playlist.add(song);
	}
	
	//Returns the head of the Queue
	public Song getHead() {
		return _playlist.peek();
	}
	
	//Removes head of the Queue
	public void removeHead() {
		_playlist.remove();
	}
	
	//Removes specific index (not efficient by all means, shouldn't be used, but part of the required specs)
	//Copies the entire queue to a temporary one unless the element to copy matches the one to delete, then overwrites initial queue with copy.
	//Therefore, time complexity of O(n) (also known in the business as "yikes")
	//Ideally, a dedicated data structure would be build in order to avoid looping through the entire playlist
	//We can also assume that since those playlists are supposed to be hand-made by the users, they are not too likely to be huge, therefore the time complexity is
	//less crucial than when we sort the library.
	//Sorry for the long comment, I felt chatty. I hope you're having a good day by the way.
	public void removeAt(Song songToDelete) {
		Queue<Song> _tempplaylist = new LinkedList<Song>();
		for (Song song : _playlist) {
			if(songToDelete.get_title().compareToIgnoreCase(song.get_title()) != 0){
				_tempplaylist.add(song);
			}
			_playlist = _tempplaylist;
		}
	}
	
	//Returns the entire Queue
	public Queue<Song> getPlaylist() {
		return _playlist;
	}
	
	//Checks if the Queue is empty
	public boolean isEmpty() {
		return _playlist.isEmpty();
	}
}
