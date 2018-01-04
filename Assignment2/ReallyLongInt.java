//Written By: Alex Blackson
//Last Modified:  February 14, 2017

// CS 0445 Spring 2017
// This is a partial implementation of the ReallyLongInt class.  You need to
// complete the implementations of the remaining methods.  Also, for this class
// to work, you must complete the implementation of the LinkedListPlus class.
// See additional comments below.

public class ReallyLongInt 	extends LinkedListPlus<Integer> 
							implements Comparable<ReallyLongInt>
{
	// Instance variables are inherited.  You may not add any new instance variables
	
	// Default constructor
	private ReallyLongInt()
	{
		super();
	}

	// Note that we are adding the digits here in the FRONT. This is more efficient
	// (no traversal is necessary) and results in the LEAST significant digit first
	// in the list.  It is assumed that String s is a valid representation of an
	// unsigned integer with no leading zeros.
	public ReallyLongInt(String s)
	{
		super();
		char c;
		int digit;
		// Iterate through the String, getting each character and converting it into
		// an int.  Then make an Integer and add at the front of the list.  Note that
		// the add() method (from A2LList) does not need to traverse the list since
		// it is adding in position 1.  Note also the the author's linked list
		// uses index 1 for the front of the list.
		for (int i = 0; i < s.length(); i++)
		{
			c = s.charAt(i);
			if (('0' <= c) && (c <= '9'))
			{
				digit = c - '0';
				this.add(1, new Integer(digit));
			}
			else throw new NumberFormatException("Illegal digit " + c);
		}
	}

	// Simple call to super to copy the nodes from the argument ReallyLongInt
	// into a new one.
	public ReallyLongInt(ReallyLongInt rightOp)
	{
		super(rightOp);
	}
	
	// Method to put digits of number into a String.  Since the numbers are
	// stored "backward" (least significant digit first) we first reverse the
	// number, then traverse it to add the digits to a StringBuilder, then
	// reverse it again.  This seems like a lot of work, but given the
	// limitations of the super classes it is what we must do.
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		if (numberOfEntries > 0)
		{
			this.reverse();
			for (Node curr = firstNode; curr != null; curr = curr.next)
			{
				sb.append(curr.data);
			}
			this.reverse();
		}
		return sb.toString();
	}

	// You must implement the methods below.  See the descriptions in the
	// assignment sheet

	public ReallyLongInt add(ReallyLongInt rightOp)
	{
		int carry = 0;	//carry in variable
		int shortLen, bigLen; //lengths of longer and shorter ReallyLongInts
		int temp = 0, leftInt, rightInt; //temporary variables to be manipulated
		boolean longer; //longer is true if right number is longer and vice versa
		Integer left; // left and right are Integer objects to be pulled out of ReallyLongInts
		Integer right;
		ReallyLongInt sum = new ReallyLongInt(); //initializes return sum object
		
		// Initializes shortLen, bigLen, and longer to appropriate values
		if (this.getLength()<=rightOp.getLength()){
			shortLen = this.getLength();
			bigLen = rightOp.getLength();
			longer = true;
		}
		// Initializes shortLen, bigLen, and longer to appropriate values
		else{
			shortLen = rightOp.getLength();
			bigLen = this.getLength();
			longer = false;
		}
		
		//Loop to add in positions that the numbers have in common
		for (int i = 1; i <= shortLen; i++){
			//initializes left to Integers at appropriate positions
			left = this.remove(i);
			right = rightOp.remove(i);
			
			//converts left and right to int types
			leftInt = left.intValue();
			rightInt = right.intValue();
			
			//temp stores variable to be added to sum array 
			temp = leftInt + rightInt + carry;
			
			//if temp is bigger than 10, it generates a new carry and reduces temp by factor of 10
			if (temp >= 10){
				carry = 1;
				temp = temp % 10;
			}
			else{
				carry = 0;
			}
			
			//adds temp to the sum to be returned
			sum.add(new Integer(temp));
			
			//adds left and right to the ReallyLongInt to prevent 
			this.add(i, left);
			rightOp.add(i, right);
		}
		
		//Loop to handle remaining values
		for (int j = (shortLen + 1); j <= bigLen; j++){
			//pulls out value from longer ReallyLongInt and converts to int
			if (longer){
				left = rightOp.remove(j);
				
				leftInt = left.intValue();
				
				rightOp.add(j, left);
			}
			else{
				left = this.remove(j);
				
				leftInt = left.intValue();
				
				this.add(j, left);
			}
			
			//determines value to be added, handling carry in the process
			temp = leftInt + carry;
			
			//handles carry case as in above loop
			if (temp >= 10){
				carry = 1;
				temp = temp % 10;
			}
			else{
				carry = 0;
			}
			
			//adds temp to sum to be returned
			sum.add(new Integer(temp));
		}
		
		//If there is a final carry, it will be added to return value
		if (carry >= 1){
			sum.add(new Integer(carry));
		}
		
		//Makes sure sum does not contain leading zeros and returns sum
		sum.spliceZeros();
		return sum;
	}
	
	public ReallyLongInt subtract(ReallyLongInt rightOp)
	{	
		int borrow; //borrow controls manipulated value of larger ReallyLongInt
		// temporary variables to control length, individual values of ReallyLongInts
		int shortLen, bigLen, leftInt, rightInt, temp, j;
		Integer left; // left and right are Integer objects to be pulled out of ReallyLongInts
		Integer right;
		ReallyLongInt leftRLI = new ReallyLongInt(this); // creates new ReallyLongInt to prevent manipulation differences
		ReallyLongInt difference = new ReallyLongInt(); // initializes difference ReallyLongInt to be returned
		
		//initializes length variables
		if(leftRLI.getLength() >= rightOp.getLength()){
			shortLen = rightOp.getLength();
			bigLen = this.getLength();
		}
		//throws exception if rightOp is greater than leftRLI
		else{
			throw new ArithmeticException("Invalid Difference -- Negative Number");
		}
		if(leftRLI.compareTo(rightOp) < 0){
			throw new ArithmeticException("Invalid Difference -- Negative Number");
		}
		
		//loop to subtract each individual digit that both ReallyLongInts share
		for (int i = 1; i <= shortLen; i++){
			j = i; //j is used in borrow case
			
			//pulls out Integer objects to be subtracted from each other
			left = leftRLI.remove(i);
			right = rightOp.remove(i);
			
			//converts left and right to int values
			leftInt = left.intValue();
			rightInt = right.intValue();
			
			//adds leftInt and rightInt back into the two ReallyLongInts to prevent unintentional manipulation 
			leftRLI.add(i, leftInt);
			rightOp.add(i, rightInt);
			
			//In non-borrow case, difference digit to be added is just the difference in digits
			if (leftInt >= rightInt){
				temp = leftInt - rightInt;
			}
			//Borrow case to handle when the left digit is greater than right digit
			else{
				j++; //increments j to change the next digit
				leftInt = leftInt + 10; // left digit borrow 10 from next significant bit
				temp = leftInt - rightInt; // difference digit is difference two new values
				borrow = leftRLI.remove(j).intValue(); //initializes borrow to be next significant digit
				
				//handles borrow from zero case
				while (borrow == 0){
					borrow = 9; //if borrow zero, it will borrow from next digit to be 9
					leftRLI.add(j, new Integer(borrow)); //adds borrow back into next significant digit
					j++; //increments j in case another borrow must be performed
					borrow = leftRLI.remove(j).intValue(); //sets next borrow in case next digit is also zero
				}
				
				borrow--; //borrow goes down by one to donate to the following digit
				leftRLI.add(j, new Integer(borrow)); //adds borrow back into left number
			}
			
			difference.add(new Integer(temp)); //adds individual digits into differnce return value
		}
		//adds remaining values of left number to the difference
		for (int k = (shortLen+1); k <= bigLen; k++){
			temp = leftRLI.remove(k);
			difference.add(temp);
			leftRLI.add(k, temp);
		}
		
		//gets rid of leading zeros and returns result
		difference.spliceZeros();
		return difference;
	}

	public int compareTo(ReallyLongInt rOp)
	{
		//copies right and left ReallyLongInts to prevent manipulation error
		ReallyLongInt left = new ReallyLongInt(this);
		ReallyLongInt right = new ReallyLongInt(rOp);
		
		//If left is longer, it will be a bigger number
		if (left.getLength() > right.getLength()){
			return 1;
		}
		
		//If right is longer, it will be a bigger number 
		else if (right.getLength() > left.getLength()){
			return -1;
		}
		
		//If left and right are same length, this loop goes through digit by digit to test for bigger value
		else{
			int leftInt, rightInt; //temporary values to be pulled out of left and right ReallyLongInts
			
			//Reverses left and right to test for most significant digits
			left.reverse();
			right.reverse();
			
			int len = left.getLength(); //len stores length of numbers
			
			for (int i = 1; i <= len; i++){
				// removes ints to be compared
				leftInt = left.remove(1).intValue(); 
				rightInt = right.remove(1).intValue();
				
				//current most significant determines which ReallyLongInt is bigger
				if (leftInt > rightInt){
					return 1; 
				}
				if(rightInt > leftInt){
					return -1;
				}
			}
		}
		//if this point in method is reached, ReallyLongInts must be equal
		return 0;
	}

	public boolean equals(Object rightOp)
	{
		ReallyLongInt rightRLI = ((ReallyLongInt) rightOp); //converts parameter to ReallyLongInt
		Integer l; //l and r are the current Integers to be pulled out of list
		Integer r;
		
		// ReallyLongInts cannot be equal if they are of different lengths
		if (this.getLength() != rightRLI.getLength()){
			return false;
		}
		
		else{
			//loops through every value of each list to test for equality
			for (int i = 1; i <= this.getLength(); i++){
				//pulls out each value in each ReallyLongInt
				l = this.remove(i);
				r = rightRLI.remove(i);
				
				//adds Integers back into 
				this.add(i, l);
				rightRLI.add(i, r);
				
				//calls Integers equals() to test for Integer equality 
				if (!l.equals(r)){
					return false;
				}
			}
		}
		//if this point in method is program is reached, the ReallyLongInts will be equal
		return true;
	}

	public void multTenToThe(int num)
	{
		// Adds zeros to the least significant digit up until num
		for (int i = 1; i <= num; i++){
			this.add(1, new Integer(0));
		}
	}

	public void divTenToThe(int num)
	{
		//Removes least significant digits up until num
		for (int i = 1; i <= num; i++){
			this.remove(1);
		}
	}
	
	//added method to remove leading zeros
	public void spliceZeros(){
		this.reverse(); //reverses ReallyLongInt to loop through sequentially
		int temp; 
		int len = this.getLength();
		
		// loop through loop to test for leading zeros
		for (int i = 1; i <= len; i++){
			//removes least significant digit and converts to int
			temp = this.remove(1).intValue();
			
			//if temp is not zero, temp will be readded and ReallyLongInt will be reversed and method will be exited
			if (temp != 0){
				this.add(1, temp); 
				this.reverse();
				return;
			}
		}
	}
}