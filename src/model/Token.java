package model;

import java.util.ArrayList;

public class Token {
	//VARIABLES
	private String _token;
	private ArrayList<Integer> _indexesInLibrary = new ArrayList<Integer>();
	
	//CONSTRUCTOR
	public Token(String _token, Integer _indexesInLibrary) {
		this._token = _token;
		this._indexesInLibrary.add(_indexesInLibrary);
	}
	
	//GETTERS
	public String getToken() {
		return _token;
	}

	public ArrayList<Integer> getIndexesInLibrary() {
		return _indexesInLibrary;
	}


	//METHODS
	//Adds an index to the existing token (Used to avoid duplicating tokens)
	public void addIndex(Integer indexToAdd) {
		Boolean indexExist = false;
		
		//Checks if the index already exists in the array (i.e. if a title has the same word twice)
		for (Integer existingIndex : _indexesInLibrary) {
			if (existingIndex == indexToAdd) {
				indexExist = true;
			}
		}
		
		if(!indexExist) {
			_indexesInLibrary.add(indexToAdd);
		}
	}
}
