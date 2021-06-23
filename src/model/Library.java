package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import model.Song;

public class Library {
	//VARIABLES
	private ArrayList<Song> _tempLibrary = new ArrayList<Song>(); //Stores all the songs temporarily
	private Song[] _library; //Stores all songs permanently (at least while the software runs ;) )
	private ArrayList<Token> _tempTokens = new ArrayList<Token>(); //Stores all the tokens temporarily
	private Token[] _tokens; //Stores all tokens objects (i.e. words that appear in titles and the index of said titles in _library)
	
	//Nomenclature note: in the following comments, we tried to use "token(s)" to refer to the object/class.
	//"word" refers to the variable stored in the token (return with the getToken() method)
	
	//CONSTRUCTOR
	public Library(){
		
	}
	
	//GETTER
	public Song[] getLibrary() {
		return _library;
	}
	
	
	//METHODS
	
	//Reads provided file and populates _library with it
	public void populateLibrary(String path) throws FileNotFoundException{
		
		File file = new File(path);
		Scanner sc = new Scanner(file); 
		Integer counter = 0;
		
		//Reading from Scanner and populating Song[]
		while (sc.hasNextLine()) {
			//Storing line as String
			String currentString = sc.nextLine();
			
			//Breaking down line into our four parameters (Title, Artist, Duration and file)
			String[] songDetails = currentString.split("\\t");
			
			//Creating a Song object with the details
			Song currentSong = new Song(songDetails[0], songDetails[1], Integer.parseInt(songDetails[2]), songDetails[3]);
							
			//Adding Song object to ListArray of Songs
			_tempLibrary.add(currentSong);
			
		}
		
		//Create the final array with the size of the temporary ArrayList we used before.
		_library = new Song[_tempLibrary.size()];
		
		//Populate the final array with the content of the temporary ArrayList
		for (Song song : _tempLibrary) {
			_library[counter] = song;
			_library[counter].trimTitle();
			counter++;
		}
		
		//Populate the token array
		populateTokens();
	}
	
	//Loops through the entire library and creates tokens for each word (or adds indexes to the existing ones)
	private void populateTokens() {
		String[] words;
		Token[] tokensTemp;
		
		//Loops through all songs
		for (int i = 0; i < _library.length; i++) {
			//Splits the title into words
			words = _library[i].get_title().split("\\s+");
			//If array of tokens is not empty
			if(_tempTokens.size() > 0) {
				//For each word in the title, binary search the array of tokens to search if it the word is already stored
				for (String word : words) {
					_tempTokens.add(new Token(word, i));
//					System.out.println(_tempTokens.get(i).getToken());
				}
			} //If array of tokens is empty, add the first token
			else {
				_tempTokens.add(new Token(words[0], i));
			}
		}
		
		
		//Copy the arraylist to an array
		_tokens = new Token[_tempTokens.size()];
		
		for (int i = 0; i < _tempTokens.size(); i++) {
			_tokens[i] = _tempTokens.get(i);
		}
		
		
		//quicksort the array
		quickSortTokens(_tokens, 0, _tokens.length - 1);
		
		

		//Loop through the array to look for duplicates token, merge the indexes and remove them
		tokensTemp = new Token[_tokens.length];
		tokensTemp = removeDuplicateTokens(_tokens, tokensTemp);
		_tokens = tokensTemp;
		_tokens = resizeArray(_tokens);
		
	}
	
	//Removes duplicate tokens
	private Token[] removeDuplicateTokens(Token[] original, Token[] temporary) {
		int j = 0;
		int length = original.length;
		
		for (int i = 0; i < original.length - 1; i++) {
			//If the original token's word and the next one in the array do not match, copy the token to the temporary array
			if (original[i].getToken().compareToIgnoreCase(original[i + 1].getToken()) != 0) {
				temporary[j] = original[i];
				j++;
			}else {//If they match, add indexes to the second one
				ArrayList<Integer> indexes = original[i].getIndexesInLibrary(); 
				for (Integer index : indexes) {
					original[i + 1].addIndex(index);
				}
//				System.out.println(indexes.toString());
			}
		}
		//Stores the last element of the array
		temporary[j++] = original[length - 1];
		return temporary;
	}
	
	//Resizes the array of tokens by removing all null elements
	private Token[] resizeArray(Token[] tokens) {
		Token[] newArray;
		Integer firstNullIndex = tokens.length - 1;
		boolean foundFirstNull = false;
		
		//If the array is not null, loop through it to find the first null element
		if(tokens != null) {
			for (int i = 0; i < tokens.length; i++) {
				if(!foundFirstNull && tokens[i] == null) {
					foundFirstNull = true;
					firstNullIndex = i;
				}
			}
			
			//If the original array had null elements, 
			if(foundFirstNull) {
				newArray = new Token[firstNullIndex];
				for (int i = 0; i < firstNullIndex; i++) {
					newArray[i] = tokens[i];
				}
				return newArray;
			}
			
			
		}
		
		return tokens;
	}
	
	//BinarySearches _tokens and returns all songs with a title matching passed argument or an empty array if no match is found
	public Song[] search(String input){
		int numberOfSongs;
		int foundIndex = tokensBinarySearch(_tokens, 0, _tokens.length - 1, input);
		ArrayList<Integer> songIndexes;
		Song[] songsMatching;
		
		//If a matching token has been found, create an array of Songs and return it
		if(foundIndex > -1) {
			//Retrieves the number of songs containing the token and initiate the array of songs with it
			numberOfSongs = _tokens[foundIndex].getIndexesInLibrary().size();
			songsMatching = new Song[numberOfSongs];
			
			//Stores all the indexes of the songs
			songIndexes = _tokens[foundIndex].getIndexesInLibrary();
			
			//Populates the array of songs from the library
			for(int i = 0; i < numberOfSongs; i++) {
				songsMatching[i] = _library[songIndexes.get(i)];
			}
			
			return songsMatching;
		}else { //No match found, returns empty array
			return new Song[0];
		}
	}
	
	//BinarySearches through _tokens.
	private int tokensBinarySearch(Token[] _tokens, int left, int right, String word) 
    { 
		//If we haven't searched all elements
        if (right >= left) { 
        	
        	//Get the index in the middle of the array
            int mid = left + (right - left) / 2;
            //If the token in the middle of the array matches, return its index
            if (_tokens[mid].getToken().compareToIgnoreCase(word) == 0) {
            	return mid;
            }//If the word is after, perform binary search on left side of the array
            else if (_tokens[mid].getToken().compareToIgnoreCase(word) > 0) {
            	return tokensBinarySearch(_tokens, left, mid - 1, word); 
            }//The word is before, perform binary search on the right side of the array
            else {
            	return tokensBinarySearch(_tokens, mid + 1, right, word); 
            }            
        } //Binary search can't find the word
        else {
        	return -1; 
        }
    }
	
	//Uses QuickSort to order the tokens
	public void quickSortTokens(Token[] _tokens, Integer low, Integer high){
		if (low < high) 
        { 
            int pi = partitionTokens(_tokens, low, high); 
  
            quickSortTokens(_tokens, low, pi-1); 
            quickSortTokens(_tokens, pi+1, high); 
        } 
	}
	
	//Used to quicksort the tokens
	private Integer partitionTokens(Token[] _tokens, Integer low, Integer high) {
		String pivot = _tokens[high].getToken();
		
        Integer i = (low-1); // index of smaller element 
        for (Integer j=low; j<high; j++) 
        { 
            // If current element is smaller than the pivot 
            if (_tokens[j].getToken().compareToIgnoreCase(pivot) < 0) 
            { 
                i++; 
  
                // swap _tokens[i] and _tokens[j] 
                Token temp = _tokens[i]; 
                _tokens[i] = _tokens[j]; 
                _tokens[j] = temp; 
            } 
        } 
  
        // swap _tokens[i+1] and _tokens[high] (or pivot) 
        Token temp = _tokens[i+1]; 
        _tokens[i+1] = _tokens[high]; 
        _tokens[high] = temp; 
  
        return i+1;
	}	

	
	//Uses QuickSort to order the library
	public void quickSortLibrary(Song[] _library, int low, int high){
		if (low < high) 
        { 
            int pi = partitionLibrary(_library, low, high); 
  
            quickSortLibrary(_library, low, pi-1); 
            quickSortLibrary(_library, pi+1, high); 
        } 
	}
	
	//Used to quicksort the library
	private Integer partitionLibrary(Song[] _library, Integer low, Integer high) {
		String pivot = _library[high].get_title();
		
        Integer i = (low-1); // index of smaller element 
        for (Integer j=low; j<high; j++) 
        { 
            // If current element is smaller than the pivot 
            if (_library[j].get_title().compareToIgnoreCase(pivot) < 0) 
            { 
                i++; 
  
                // swap _library[i] and _library[j] 
                Song temp = _library[i]; 
                _library[i] = _library[j]; 
                _library[j] = temp; 
            } 
        } 
  
        // swap _library[i+1] and _library[high] (or pivot) 
        Song temp = _library[i+1]; 
        _library[i+1] = _library[high]; 
        _library[high] = temp; 
  
        return i+1;
	}
}
