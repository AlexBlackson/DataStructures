public class HuffmanBinaryTree {

	private BinaryNode<Character> root;
	
	// default constructor if no data is based
	public HuffmanBinaryTree()
	{
		root = null;
	}	
	
	// creates a root node if nothing else is based
	public HuffmanBinaryTree(Character rootData)
	{
	    root = new BinaryNode<>(rootData);
	} 
	
	// constructor to create binary tree from a StringBuilder that's been interpretted from file
	public HuffmanBinaryTree(StringBuilder tree)
	{
		// calls addNode to initialize the tree and store root
		root = addNode(tree);
		System.out.println("The Huffman Tree has been restored");
	}
	
	// addNode recursively declares tree based off a StrinBuilder of leaf and interior nodes
	public BinaryNode<Character> addNode(StringBuilder currTree)
	{
		// base case, when the end of StringBuilder is reached. 
		if (currTree == null)
			return null;
		
		// makes sure the text file is formatted in given format
		assert (currTree.charAt(0) == 'I' || currTree.charAt(0) == 'L');
		
		int children; // current node's number of children 
		StringBuilder temp; // temp used to splice off nodes already traversed
		BinaryNode<Character> nodeToAdd; // node being added to tree
		
		// if interior node
		if (currTree.charAt(0) == 'I'){
			// declares nodeToAdd as dummy variable
			nodeToAdd = new BinaryNode<Character>('\0');
			// temp initialized as copy as remaining StringBuilder
			temp = new StringBuilder(currTree);
			// sets left child based on the current tree without the first character
			nodeToAdd.setLeftChild(addNode(currTree.deleteCharAt(0)));
			children = nodeToAdd.getNumberOfNodes(); 
			// splices off already used nodes and sets right child			
			for (int i = 0; i < children; i++){
				if (temp.charAt(0) == 'I'){
					temp.deleteCharAt(0);
				}
				else if (temp.charAt(0) == 'L'){
					temp.delete(0, 2);
				}
			}
			nodeToAdd.setRightChild(addNode(temp));
		}
		// leaf node - adds node character based off the character in the te
		else{
			nodeToAdd = new BinaryNode<Character>(new Character(currTree.charAt(1)));
		}
		// returns the child node
		return nodeToAdd;
	}
	
	// calls inOrder traversal from the BinaryNode class
	public void inOrder() {
		root.inOrder();
		System.out.println("");
	}
	
	// displays table based on the returned value from genTable() 
	public void dispTable(){
		// initializes the first line of table and table itself as blank and current node as root
		 StringBuilder tab = genTable(new StringBuilder(""), new StringBuilder(""), root);
		 System.out.println(tab);
	}
	
	// returns encoding table of tree
	private StringBuilder genTable(StringBuilder currLine, StringBuilder table, BinaryNode<Character> node){
		// adds 0 to current line if the node has a left child 
		if(node.hasLeftChild()){
			currLine.append("0");
			// recursively calls genTable for the next node
			table = genTable(currLine, table, node.getLeftChild());
			// removes the last character from the line to backtrack
			currLine.deleteCharAt(currLine.length()-1);
		}
		// adds 1 to current line if the ndoe has a right child
		if(node.hasRightChild()){
			currLine.append("1");
			// recursively calls genTable for the next node
			table = genTable(currLine, table, node.getRightChild());
			// removes the last character from the line to backtrack
			currLine.deleteCharAt(currLine.length()-1);
		}
		// when the leaf node is reached
		if (!node.hasLeftChild() && !node.hasLeftChild()){
			// updates the table with leaf data and formats 
			table.append(node.getData());
			table.append(": ");
			table.append(currLine);
			table.append("\n");
		}
		// returns most recently updated table
		return table;
	}
	
	// translates pattern of letters to a sequence of Huffman strings and displays
	public void encode(StringBuilder pattern){
		// calls genTable() and stores the table StringBuilder in table
		StringBuilder table = genTable(new StringBuilder(""), new StringBuilder(""), root);
		// temp stores the index of the Huffman strings
		int temp = 0;
		// p stores index of alphabetical characters to convert
		for (int p = 0; p < pattern.length(); p++){
			// t stores index of table to be analyzed 
			for (int t = 0; t < table.length(); t++){
				// finds character in table
				if (pattern.charAt(p) == table.charAt(t)){
					temp = t + 3;
					// prints out the Huffman strings associated with each character in pattern
					while (table.charAt(temp) != '\n'){
						System.out.print(table.charAt(temp));
						temp++;
					}
					System.out.println("");
				}
			}
		}
	}

	public void decode(StringBuilder pattern){
		decode(pattern, root, false);
	}
	
	private void decode(StringBuilder pattern, BinaryNode<Character> currNode, boolean done){
		// dir - direction to traverse
		char dir = ' '; 
		
		// escapes if the end of decoding has been reached 
		if (done){
			return;
		}
		
		if (!done && !pattern.toString().equals("")){
			dir = pattern.charAt(0);
		}
		
		// base case - end of pattern is reached
		if (pattern.toString().equals("")){
			done = true;
		}
		
		// traverse leftwards if the currNode has a left child and pattern states to go left
		if (dir == '0' && currNode.hasLeftChild() && !done){
			// updates path 
			pattern = new StringBuilder(pattern.substring(1, pattern.length()));
			decode(pattern, currNode.getLeftChild(),done);
		}
		
		// traverse rightwards if the currNode has a left child and pattern states to go right
		else if (dir == '1' && currNode.hasRightChild() && !done){
			// updates path
			pattern = new StringBuilder(pattern.substring(1, pattern.length()));
			decode(pattern, currNode.getRightChild(), done);
		}
		
		// when the lead node is reached
		if (!currNode.hasLeftChild() && !currNode.hasRightChild()){
			// outputs the leaf data at the end of the pathway
			System.out.print(currNode.getData());
			// calls decode again, starting again at the root
			decode(pattern, root, done);
			if(done)
				System.out.println("");
		}
	}
	
	// checkLetters checks that each inputted letter is a valid character in the tree
	public boolean checkLetters(StringBuilder letters){
		// contained - if all letters are in tree
		// found - if current letter is in tree
		boolean contained = true, found = false;
		// nodes stores all of the node values and then the spaces are spliced away
		StringBuilder nodes = root.inOrderToString();
		for (int i = nodes.length()-1; i >= 0; i--){
			if (nodes.charAt(i) == '\0'){
				nodes.deleteCharAt(i);
			}
		}
		
		// searches and checks that each letter is within the tree and exits if not contained
		for (int l = 0; (l < letters.length() && contained); l++){
			found = false;
			// searches for letter at letter[l] in all of the nodes 
			for (int n = 0; (n < nodes.length() && !found); n++){
				if (letters.charAt(l) == nodes.charAt(n)){
					// will exit when found is updated
					found = true;
				}
			}
			if (!found)
				contained = false;
		}
		return contained;
	}
	
	public boolean checkNumbers(StringBuilder numbers){
		// makes sure each character in numbers is a 0 or 1
		for (int check = 0; check < numbers.length(); check++){
			if (numbers.charAt(check) != '0' && numbers.charAt(check) != '1')
				return false;
		}
		
		// table will store encoding table
		StringBuilder table = genTable(new StringBuilder(""), new StringBuilder(""), root);
		int numOfNodes = 0;
		// counts number of nodes in the tree based off how many lines are in the table
		for (int i = 0; i < table.length(); i++){
			if (table.charAt(i) == '\n'){
				numOfNodes++;
			}
		}
		
		// paths will store all of the Huffman string of tree
		StringBuilder[] paths = new StringBuilder[numOfNodes];
		for (int i = 0; i < paths.length; i++){
			paths[i] = new StringBuilder("");
		}
		
		// adds all of the Huffman pathways to paths array
		int index = 0;
		for (int j = 0; j < numOfNodes; j++){
			index = index + 3;
			while (table.charAt(index) != '\n'){
				paths[j].append(table.charAt(index));
				index ++;
			}
			index++;
		}
		
		// searches for all of the patterns in paths
		boolean found = true;
		String currPath; // current path being analyzed
		int last; // last stores the last index of the substring of numbers currently being searched for in table
		for (int k = 0; (k < numbers.length() && found); k++){
			found = false;
			last = k + 1;
			while (!found && last <= numbers.length()){
				// currPath could be of varying length, so the length must be updated
				currPath = numbers.substring(k, last);
				// search for each substring until found
				for (int p = 0; (p < paths.length && !found); p++){
					if (paths[p].toString().equals(currPath))
						found = true;
				}
				last++;
			}
			// updates k based upon how long the substring found is
			k += (last - 2);
		}
		return found;
	}
	
	// returns data at root of tree
	public Character getRootData() 
	{
	    return root.getData();
	} 

	// returns if tree is empty
	public boolean isEmpty()
	{
	    return root == null;
	} 

	// clears tree by setting the root to null
	public void clear()
	{
	    root = null;
	}
	
}
