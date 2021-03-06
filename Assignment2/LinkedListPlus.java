//Written By: Alex Blackson
//Last Modified:  February 14, 2017

// LinkedListPlus<T> class partial implementation

// See the commented methods below.  You must complete this class by
// filling in the method bodies for the remaining methods.  Note that you
// may NOT add any new instance variables, but you may use method variables
// as needed.

public class LinkedListPlus<T> extends A2LList<T>
{
	// Default constructor simply calls super()
	public LinkedListPlus()
	{
		super();
	}
	
	// Copy constructor.  This is a "deepish" copy so it will make new
	// Node objects for all of the nodes in the old list.  However, it
	// is not totally deep since it does NOT make copies of the objects
	// within the Nodes -- rather it just copies the references.
	public LinkedListPlus(LinkedListPlus<T> oldList)
	{
		super();
		if (oldList.getLength() > 0)
		{
			// Special case for first Node since we need to set the
			// firstNode instance variable.
			Node temp = oldList.firstNode;		// front of old list
			Node newNode = new Node(temp.data);	// copy the data
			firstNode = newNode;				// set front of new list
			
			// Now we traverse the old list, appending a new Node with
			// the correct data to the end of the new list for each Node
			// in the old list.  Note how the loop is done and how the
			// Nodes are linked.
			Node currNode = firstNode;
			temp = temp.next;
			while (temp != null)
			{
				currNode.next = new Node(temp.data);
				temp = temp.next;
				currNode = currNode.next;
			}
			numberOfEntries = oldList.numberOfEntries;
		}			
	}

	// Make a StringBuilder then traverse the nodes of the list, appending the
	// toString() of the data for each node to the end of the StringBuilder.
	// Finally, return the StringBuilder as a String.
	public String toString()
	{
		StringBuilder b = new StringBuilder();
		for (Node curr = firstNode; curr != null; curr = curr.next)
		{
			b.append(curr.data.toString());
			b.append(" ");
		}
		return b.toString();
	}

	// Shift left num positions.  This will shift out the first num Nodes
	// of the list, with Node num+1 now being at the front.
	public void leftShift(int num)
	{
		if (num >= this.getLength())	// If we shift more than the number of
		{								// Nodes, just initialize to empty
			firstNode = null;
			numberOfEntries = 0;
		}
		else if (num > 0)
		{
			Node temp = firstNode;		// Start at front
			for (int i = 0; i < num-1; i++)	 // Get to node BEFORE the one that 
			{								 // should be the new firstNode
				temp = temp.next;
			}
			firstNode = temp.next;		// Set firstNode to Node after temp
			temp.next = null;		    // Disconnect old prefix of list (just
										// to be extra cautious)
			numberOfEntries = numberOfEntries - num;	// Update logical size
		}
	}

	// Remove num items from the end of the list
	public void rightShift(int num)
	{
		//removes all entries if num is larger than length
		if (num >= this.getLength()){
			firstNode = null;
			numberOfEntries = 0;
		}
		else if (num > 0){
			int len = this.getLength();
			for (int i = 0; i < num; i++){
				//removes last elements of list
				super.remove(len-i);
			}
			
			//decrements numberOfEntries by number of removals
			numberOfEntries = numberOfEntries - num;
		}
	}

	// Remove from the front and add at the end.  Note that this method should
	// not create any new Node objects -- it is simply moving them.  If num
	// is negative it should still work, actually doing a right rotation.
	public void leftRotate(int num)
	{
		//prevents unnecessary number of rotations
		num = num % numberOfEntries;
		
		//negative rotate will rotate in other direction
		if (num < 0){
			rightRotate(-num);
		}
		else if (num > 0){
			//temporary nodes to allow for manipulation
			Node temp;
			Node lastNode;
			//performs a single loop for each rotation
			for (int i = 0; i < num; i++){
				//stores temp as firstNode to prevent it from being lost
				temp = firstNode;
				//firstNode is now the next node and previous firstNode will rotate to last
				firstNode = firstNode.next;
				//finds last Node
				if (firstNode.next != null){
					lastNode = firstNode.next;
				}
				//case where only one node in list
				else{
					lastNode = firstNode;
				}
				//finds last node
				while(lastNode.next != null){
					lastNode = lastNode.next;
				}
				//lastNode now points to first
				lastNode.next = temp;
				//previous firstNode now points to null, as the now lastNode
				temp.setNextNode(null);
			}
		}
	}

	// Remove from the end and add at the front.  Note that this method should
	// not create any new Node objects -- it is simply moving them.  If num
	// is negative it should still work, actually doing a left rotation.
	public void rightRotate(int num)
	{
		//prevents rotation to perform unneccesary amount of times
		num = num % numberOfEntries;
		//negative rotate will rotate in other direction
		if (num < 0){
			leftRotate(-num);
		}
		else if (num > 0){
			//temporary Nodes to allow for manipulation
			Node temp;
			Node current;
			for (int i = 0; i < num; i++){
				//current will point to firstNode initially
				current = firstNode;
				temp = null;
				
				//while loop will allow current to find last node and temp to be the previous node
				while (current.next != null){
					temp = current;
					current = current.next;
				}
				
				//as long as temp stores valid value, firstNode will now point to last element, allowing right rotation
				if(temp != null){
					temp.next = null;
					current.next = firstNode;
					firstNode = current;
				}
			}
		}
	}
	
	// Reverse the nodes in the list.  Note that this method should not create
	// any new Node objects -- it is simply moving them.
	public void reverse()
	{
		//Nothing to be reversed if there is 1 or 0 entries in list
		if (numberOfEntries <= 1){
			return;
		}
		//temporary Nodes to trace through list
		Node before = null;
		Node current = firstNode;
		Node temp;
		
		while (current != null){
			//temp stores what current points to so it isn't lost
			temp = current.next;
			
			//current will now point to previous node, reversing order
			current.next = before;
			
			//increments before and current nodes
			before = current;
			current = temp;
		}
		//sets firstNode to before so that data isn't lost
		firstNode = before;
	}				
}