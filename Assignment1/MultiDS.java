// Created by Alex Blackson
// arb171@pitt.edu
// Peoplesoft ID: 3920894
// COE 0445: Data Structures with John Ramirez
// Last Edited: January 22, 2017

// Imports utilities needed to randomize objects
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class MultiDS<T> implements PrimQ<T>, Reorder {
	
	// Array of type T
	private T [] data;
	// count holds logical size while size holds physical size 
	private int count, size;
	
	//Constructor where user passes in max size of Object  
	public MultiDS(int s){
		// Creates array of type T and initializes physical/logical size values
		@SuppressWarnings("unchecked")
		T [] temp = (T[]) new Object[s];
		data = temp;
		count = 0;
		size = s;
	}
	
	
	public boolean addItem(T item){
		// Checks to see if logical size is less than physical so object can be added successfully
		if(count < size){
			// Adds item in place count and increment count (logical size)
			data[count] = item;
			count++;
			return true;
		}
		// If array is full, addItem will fail
		else{
			return false;
		}
	}
	
	// Removes oldest item in data (object in place 0)
	public T removeItem(){
		// Checks to see if there are objects to remove 
		if (!empty()){
			// Creates T object from position 0 in array 
			T item = (T) new Object();
			item = data[0];
			// Shifts remaining objects left one position
			for (int i = 0; i < (count - 1); i++){
				data [i] = data [i+1];
			}
			// Nullifies the object in last position and decrements logical size
			data[count-1]=null;
			count--;
			return item;
		}
		
		// If array is empty, will return null reference 
		else{
			return null;
		}
	}
	
	public boolean full(){
		for (int i = 0; i < size; i++){
			if (data[i] == null)
				return false;
		}
		return true;
	}
	
	// Returns whether or not data contains entirely null references
	public boolean empty(){ 
		if (count == 0)
			return true;
		return false;
	}
	
	// Returns logical size of instance
	public int size(){
		return count;
	}
	
	// Sets all items to null 
	public void clear(){
		for (int i = 0; i < size; i++){
			data[i] = null;
		}
		count = 0;
	}
	
	// Reverses the order of array
	public void reverse(){
		// rightPos is the position of the right object to be switched
		int rightPos = count - 1;
		for (int i = 0; i < (count/2); i++){
			// temp contains object in position rightPos
			T temp = data[rightPos];
			// left object stored in rightPos position data
			data[rightPos] = data[i];
			// rightPos object (stored in temp) goes to left position
			data[i] = temp;
			rightPos--;
		}
	}
	
	// Shifts all objects in array one position to the right
	public void shiftRight(){
		// last contains object in last position
		T last = data[count - 1];
		for (int i = (count - 1); i > 0; i--){
			data[i] = data[i-1];
		}
		// object in last position shifts right to the first
		data[0] = last;
	}
	
	// Shifts all objects in array one position to the left
	public void shiftLeft(){
		// first contains object in first position
		T first = data[0];
		for (int i = 0; i < (count -1); i++){
			data[i] = data[i+1];
		}
		// object in first position shifts left to the last
		data[count-1] = first;
	}
	
	// Randomizes the order of the array
	public void shuffle(){
		
		// Creates Random object
		Random rand = ThreadLocalRandom.current();
		for (int i = (count-1); i > 0; i--){
			// index contains random index between 0 and i
			int index = rand.nextInt(i+1);
			// temp contains value at index which is switched with the current last object
			T temp = data[index];
			data[index] = data[i];
			data[i] = temp;
		}
	}
	
	// Method to display the contents of data
	public String toString(){
		String out = "Contents:\n";
		for (int i = 0; i < count; i++){
			out += data[i] + " ";
		}
		return out;
	}
}
