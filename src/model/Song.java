package model;

public class Song {
	//VARIABLES
	private String _title;
	private String _artist;
	private Integer _duration;
	private String _fileName;
	
	//CONSTRUCTOR
	public Song(String title, String artist, Integer duration, String fileName) {
		this._title = title;
		this._artist = artist;
		this._duration = duration;
		this._fileName = fileName;
	}
	
	//GETTERS
	public String get_title() {
		return _title;
	}
	
	public String get_artist() {
		return _artist;
	}
	
	public Integer get_duration() {
		return _duration;
	}
	
	public String get_fileName() {
		return _fileName;
	}
	
	//METHODS
	public void trimTitle() {
		_title = _title.trim();
	}
	
	public void upperTitle() {
		_title = _title.toUpperCase();
	}
}

