/*
README
This is the submission for the graded lab assignment #1, which consists of implementation of a B-Tree, for the course CSD307, Advanced Data Management Systems, Shiv Nadar University. The B-Tree algorithm has been implemented in Java v1.7.

This implementation has been successfully tested for up to 1000000 elements. 
Inputs  : Choice of manual entry or randomized number insertion into the tree.
Outputs : Results of Traversal, Search.

AUTHORS
Atish Majumdar 	    : 1410110081
Prasanna Natarajan  : 1410110298
Vedant Chakravarthy : 1410110489

THANKS
The algorithm for the code was inspired from "Introduction to Algorithms" by Leiserson, Stein, Rivest and Cormen.

*/
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


public class BTree {
	static int DEBUG = 0;	// For debugging purposes
	static int order;	 	// Order of the B-Tree
	static boolean isExit = false; // To drive the main() 
	static int height=0; 	// Height of the B-Tree
	static Scanner scanner = new Scanner(System.in); // Scanner for inputs
	static int Uid=0; // Unique ID for each node of the B-Tree
	Node root; 		  // Root Node
	//String rootID;
	
	/* Constructor of the B-Tree Class 
	 *  Takes Argument: Order of the tree*/
	public BTree(int order){ 
		this.order = order;
		root = new Node(order,null);
		//rootID = "Node"+(Uid-1);
	}
	/* Main () : Driver Function */
	public static void main(String[] args){

		int option,n,o,internalOption;
		int limit = 999999;
		Random generator = new Random();
		BTree bt = null;
		boolean elementsExist = false;
		boolean treeExists = false;


		do{
			System.out.println("Choose one of the following options:");
			System.out.println("1.\tCreate tree");
			System.out.println("2.\tInsert element to tree");
			System.out.println("3.\tDisplay tree");
			System.out.println("4.\tFind element in tree");
			System.out.println("5.\tExit");
			option = scanner.nextInt();
			switch(option){
			case 1:
				if(treeExists){
					System.out.println("The tree already exists. Please choose another option.");
					break;
				}
				treeExists = true;
				System.out.println("Enter the order of the tree: ");
				o = scanner.nextInt();
				bt = new BTree(o);						
				break;
			case 2:	
				if(!treeExists){
					System.out.println("Please create the B tree before performing operations on it.");
					continue;
				}
				System.out.println("Choose one of the following options:");
				System.out.println("1.\tManually enter.");
				System.out.println("2.\tRandomized entry.");
				System.out.println("3.\tGo Back.");
				internalOption = scanner.nextInt();
				if(internalOption == 1){
					System.out.println("Enter the number of elements.");
					n = scanner.nextInt();
					for(int i=0;i<n;i++){
						bt.insert(bt, scanner.nextInt());
						if(DEBUG == 1){
							System.out.println("\n>>DEBUG PRINT");
							System.out.println(">>Inserting "+n+"\n>>***********************************************");
							Node node1 = null;
							// Trying to read the root
							try
							{
								FileInputStream fileIn = new FileInputStream(bt.root.uniqueID);
								ObjectInputStream in = new ObjectInputStream(fileIn);
								node1 = (Node) in.readObject();
								//System.out.println("root order from file"+n);
								in.close();
								fileIn.close();
							}catch(IOException e)
							{
								e.printStackTrace();
								return;
							}catch(ClassNotFoundException c)
							{
								System.out.println("BTree class not found");
								c.printStackTrace();
								return;
							}
							bt.print(node1);
							System.out.println("");
							System.out.println(">>***********************************************\n>>END OF DEBUG PRINT\n");
						}
					}
					
					if(n>0)
						elementsExist = true;
				}
				else if(internalOption == 2){
					System.out.println("Enter the number of elements.");
					n = scanner.nextInt();
					int x;
					for(int i=0;i<n;i++){
						x = generator.nextInt(limit);
						bt.insert(bt, x);
						if(DEBUG == 1){
							System.out.println("\n>>DEBUG PRINT");
							System.out.println(">>Inserting "+x+"\n>>***********************************************");
							Node node1 = null;
							// Trying to read the root
							try
							{
								FileInputStream fileIn = new FileInputStream(bt.root.uniqueID);
								ObjectInputStream in = new ObjectInputStream(fileIn);
								node1 = (Node) in.readObject();
								//System.out.println("root order from file"+n);
								in.close();
								fileIn.close();
							}catch(IOException e)
							{
								e.printStackTrace();
								return;
							}catch(ClassNotFoundException c)
							{
								System.out.println("BTree class not found");
								c.printStackTrace();
								return;
							}
							bt.print(node1);// Printing node1
							System.out.println("");
							System.out.println(">>***********************************************\n>>END OF DEBUG PRINT\n");
						}
					}
					if(n>0)
						elementsExist = true;
				}
				else
					break;
				break;

			case 3:
				if(!treeExists){
					System.out.println("Please create the B tree before performing operations on it.");
					continue;
				}
				if(!elementsExist){
					System.out.println("Please insert elements into the B tree before performing operations on it.");
					continue;
				}
				Node node1 = null;
				// Trying to read the root
				try
				{
					FileInputStream fileIn = new FileInputStream(bt.root.uniqueID);
					ObjectInputStream in = new ObjectInputStream(fileIn);
					node1 = (Node) in.readObject();
					in.close();
					fileIn.close();
				}catch(IOException e)
				{
					e.printStackTrace();
					return;
				}catch(ClassNotFoundException c)
				{
					System.out.println("BTree class not found");
					c.printStackTrace();
					return;
				}
				int pls;
				System.out.println("1. Print In Order");
				System.out.println("2. Print Pre Order");
				pls= scanner.nextInt();
				if (pls==1)
					bt.print_inorder(node1); // Printing the tree in order
				else{
					bt.print(node1); 		 // Printing the tree pre order
				}
				System.out.println("\n");
				
				break;

			case 4:
				if(!treeExists){
					System.out.println("Please create the B tree before performing operations on it.");
					continue;
				}
				if(!elementsExist){
					System.out.println("Please insert elements into the B tree before performing operations on it.");
					continue;
				}
				System.out.println("Enter number to search.");
				o = scanner.nextInt();
				Node node = null;
				// Trying to read the root
				try
				{
					FileInputStream fileIn = new FileInputStream(bt.root.uniqueID);
					ObjectInputStream in = new ObjectInputStream(fileIn);
					node = (Node) in.readObject();
					//System.out.println("root order from file"+n);
					in.close();
					fileIn.close();
				}catch(IOException e)
				{
					e.printStackTrace();
					return;
				}catch(ClassNotFoundException c)
				{
					System.out.println("BTree class not found");
					c.printStackTrace();
					return;
				}
				Node searchNode = bt.search(node, o); // Searching for o
				if(searchNode !=null){
					System.out.println("Element found at level = "+(height-1)+ "\n");
					height = 0;
				}else{
					System.out.println("Element could not be found.\n");
				}
				break;

			case 5:
				isExit = true;
				break;
			default:
				break;
			}
		}
		while(!isExit);
	}
	/* Search Function : 
	 * Inputs : The Root Node and the element to search for.
	 * Output : If found, the node containing the element, else, returns NULL. */
	public Node search(Node root, int elem){
		int i=0;  
		height++; // To calculate the height of the node being searched for.

		/* Find the first data element greater than or equal to elem */
		
		while((i<root.c) && (elem>root.data.get(i))) { 
			i++;
			//System.out.println("root.c = "+root.c+" i= "+i);
			//if(root.data.get(i)==null)return null;
		}
		/* If the found data is equal to elem, return this node */ 
		if(root.data.get(i)!=null)
			if(i<=root.c && elem == root.data.get(i)) {
				return root;
			}
		/* If data is not found, and this node is a leaf node, data does not exist within the tree, return NULL */
		if(root.isLeaf) {
			return null;
		}
		else {
			System.out.println("Traversal path is: ");
			Node node2 = null;
			/* Reading node's child */
			try
			{
				FileInputStream fileIn = new FileInputStream(root.link.get(i));
				ObjectInputStream in = new ObjectInputStream(fileIn);
				node2 = (Node) in.readObject();
				//System.out.println("root order from file"+n);
				in.close();
				fileIn.close();
			}catch(IOException e)
			{
				e.printStackTrace();
				return null;
			}catch(ClassNotFoundException c)
			{
				System.out.println("BTree class not found");
				c.printStackTrace();
				return null;
			}
			/* Traversing to the next child */
			for (int j = 0; j < node2.c; j++) {
				Node node1 = null;
				// Trying to read from node's (i)th child
				try
				{
					FileInputStream fileIn = new FileInputStream(root.link.get(i));
					ObjectInputStream in = new ObjectInputStream(fileIn);
					node1 = (Node) in.readObject();
					in.close();
					fileIn.close();
				}catch(IOException e)
				{
					e.printStackTrace();
					return null;
				}catch(ClassNotFoundException c)
				{
					System.out.println("BTree class not found");
					c.printStackTrace();
					return null;
				}
				System.out.print(" "+ node1.data.get(j)+" ");
			}
			System.out.println();
			Node node1 = null;
			// Trying to read from node's (i)th child
			try
			{
				FileInputStream fileIn = new FileInputStream(root.link.get(i));
				ObjectInputStream in = new ObjectInputStream(fileIn);
				node1 = (Node) in.readObject();
				//System.out.println("root order from file"+n);
				in.close();
				fileIn.close();
			}catch(IOException e)
			{
				e.printStackTrace();
				return null;
			}catch(ClassNotFoundException c)
			{
				System.out.println("BTree class not found");
				c.printStackTrace();
				return null;
			}
			/* Making a recursive search call after getting the appropriate node*/
			return search(node1,elem);		
		}
	}
/* Split Function : A utility function to split node2 (this must be done when node2 is full) 
 * Inputs : Two nodes and an integer which tells the position to be inserted.
 * Outputs : A new node is created, with appropriate values of node2 (function return type is void)
 */
	public void split(Node node1, int x, Node node2){
		Node node3 = new Node(order,null); // A node which is going to hold (order-1) data elements of node2
		node3.isLeaf = node2.isLeaf;
		node3.c = order-1;
		/* Copy the last (order-1) keys from node2 to node3 */
		for(int i=0;i<order-1;i++){
			node3.data.set(i,node2.data.get(i+order));
		}
		/* Copy the last (order) children of node2 to node3 */
		if(!node2.isLeaf){
			for(int j=0;j<order;j++){
				node3.link.set(j,node2.link.get(j+order));
			}
		}
		node2.c=order-1; // Reduce the count of data in node2
		/* Creating space for a new child */
		for(int i=node1.c;i>=x;i--){
			node1.link.set(i+1,node1.link.get(i));
		}
		node1.link.set(x+1,node3.uniqueID); // Link the new child to this node (node1)
		/* Shifting all greater data values one space ahead */
		for(int j=node1.c-1;j>=x;j--){
			node1.data.set(j+1,node1.data.get(j)); 
		}

		node1.data.set(x,node2.data.get(order-1)); //  Copy middle element of node2 to x position in node1
		node1.c++; 								   // Increment data count of node1
		FileOutputStream fileOut;
		// Trying to write into node1
		try {
			fileOut = new FileOutputStream(node1.uniqueID);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(node1);
	         out.close();
	         fileOut.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fileOut = null;
		// Trying to write into node2
		try {
			fileOut = new FileOutputStream(node2.uniqueID);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(node2);
	         out.close();
	         fileOut.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		fileOut= null;
		// Trying to write into node3
		try {
			fileOut = new FileOutputStream(node3.uniqueID);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(node3);
	         out.close();
	         fileOut.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	/* nonFullInsert function : Inserting new data into a node which is not full.
	 * Inputs : The node (not full) and the element to enter
	 * Output : The element is entered into the node (void function)*/
	public void nonFullInsert(Node node, int elem){
		int i = node.c-1;
		// Checking if node is a leaf node
		if(node.isLeaf){
			// To find the location of the new key to be inserted and move all greater keys to one place ahead
			while(i>=0 && elem < node.data.get(i)){
				node.data.set(i+1,node.data.get(i));
				i--;
			}

			node.data.set(i+1,elem); // Insert the new data into the found location
			node.c++;				 // Increment the count of data.
		}else{
			// If it is not a leaf node
			int j = node.c-1;
			// Find the child which is going to have new data
			while(j>=0 && node.data.get(j)>elem){
				j--;
			}
			Node node1 = null;
			// Trying to read from (j+1)th child of node
			try
			{
				FileInputStream fileIn = new FileInputStream(node.link.get(j+1));
				ObjectInputStream in = new ObjectInputStream(fileIn);
				node1 = (Node) in.readObject();
				//System.out.println("root order from file"+n);
				in.close();
				fileIn.close();
			}catch(IOException e)
			{
				e.printStackTrace();
				return;
			}catch(ClassNotFoundException c)
			{
				System.out.println("BTree class not found");
				c.printStackTrace();
				return;
			}
			// To see if the found child is full.
			if(node1!=null){				
				if(node1.c == ((order*2)-1)){
					Node n = null;
					// Trying to read from (j+1)th child of node
					try
					{
						FileInputStream fileIn = new FileInputStream(node.link.get(j+1));
						ObjectInputStream in = new ObjectInputStream(fileIn);
						n = (Node) in.readObject();
						//System.out.println("root order from file"+n);
						in.close();
						fileIn.close();
					}catch(IOException e)
					{
						e.printStackTrace();
						return;
					}catch(ClassNotFoundException c)
					{
						System.out.println("BTree class not found");
						c.printStackTrace();
						return;
					}
					// Split a child if it is full
					split(node,j+1,n);
					// To see where the new data is to be inserted
					if(elem > node.data.get(j+1)) j++;
				}	
			}
			Node nodej = null;
			// Trying to read from (j+1)th child of node
			try
			{
				FileInputStream fileIn = new FileInputStream(node.link.get(j+1));
				ObjectInputStream in = new ObjectInputStream(fileIn);
				nodej = (Node) in.readObject();
				//System.out.println("root order from file"+n);
				in.close();
				fileIn.close();
			}catch(IOException e)
			{
				e.printStackTrace();
				return;
			}catch(ClassNotFoundException c)
			{
				System.out.println("BTree class not found");
				c.printStackTrace();
				return;
			}
			nonFullInsert(nodej,elem); //Recursive call to nonFullInsert()
		}
		FileOutputStream fileOut;
		// Trying to write into the node
		try {
			fileOut = new FileOutputStream(node.uniqueID);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(node);
	         out.close();
	         fileOut.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/* Insert Function : Inserting an element into the B-Tree
	 * Inputs : The Btree and the element to insert
	 * Outputs : Element inserted into the appropriate node of the B-Tree (void function)
	 */
	public void insert(BTree bt, int elem){
		Node node1 = null;
		// Trying to read from the root of the B-Tree
		try
		{
			FileInputStream fileIn = new FileInputStream(bt.root.uniqueID);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			node1 = (Node) in.readObject();
			//System.out.println("root order from file"+n);
			in.close();
			fileIn.close();
		}catch(IOException i)
		{
			i.printStackTrace();
			return;
		}catch(ClassNotFoundException c)
		{
			System.out.println("BTree class not found");
			c.printStackTrace();
			return;
		}
		// If the node is full
		if(node1.c == ((2*order)-1)){
			
			Node node2 = new Node(order,null); // Create a new node to act as new root
			bt.root = node2;			
			node2.isLeaf = false;
			node2.c = 0;
			node2.link.set(0,node1.uniqueID);  // Set node1 as child of root   
			FileOutputStream fileOut;
			// Trying to write into root
			try {
				fileOut = new FileOutputStream(bt.root.uniqueID);
				ObjectOutputStream out = new ObjectOutputStream(fileOut);
				out.writeObject(node2);
		         out.close();
		         fileOut.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Split the old root and move one data to the new root
			split(node2,0,node1);
			// Function call to insert elem into the new root of the B-Tree
			nonFullInsert(node2,elem);
		}else{
			// Function call to insert elem into node1
			nonFullInsert(node1,elem);
		}
			}
	/* Print Function : Prints the B-Tree in pre-order
	 * Inputs : A node (mostly root)
	 * Output : Prints the B-Tree to the console (void function)
	 */
	public void print(Node node){
		//Printing the current node
		for(int i=0;i<node.c;i++)
			System.out.print(node.data.get(i)+" ");
		// If the current node is not a leaf node
		if(!node.isLeaf){
			// Traversing to get the current node's child
			for(int j=0;j<=node.c;j++){
				Node node1 = null;
				// Trying to read from node's (j)th child
				try
				{
					FileInputStream fileIn = new FileInputStream(node.link.get(j));
					ObjectInputStream in = new ObjectInputStream(fileIn);
					node1 = (Node) in.readObject();
					in.close();
					fileIn.close();
				}catch(IOException e)
				{
					e.printStackTrace();
					return;
				}catch(ClassNotFoundException c)
				{
					System.out.println("BTree class not found");
					c.printStackTrace();
					return;
				}
				if(node1!= null){
					System.out.println();
					
					print(node1); // Recursive call to print current node's child
				}
			}
		}
	}
	
	/* Print Function : Prints the B-Tree in in-order
	 * Inputs : A node (mostly root)
	 * Output : Prints the B-Tree to the console (void function)
	 */
	public void print_inorder(Node node){
		//Printing the current node
		int i;
		// Traversing the child from left to right of the current node
		for(i=0;i<node.c;i++) {
			if (node.isLeaf==false){
				Node node1 = null;
				// Trying to read from node's (i)th child
				try
				{
					FileInputStream fileIn = new FileInputStream(node.link.get(i));
					ObjectInputStream in = new ObjectInputStream(fileIn);
					node1 = (Node) in.readObject();
					in.close();
					fileIn.close();
				}catch(IOException e)
				{
					e.printStackTrace();
					return;
				}catch(ClassNotFoundException c)
				{
					System.out.println("BTree class not found");
					c.printStackTrace();
					return;
				}
				print_inorder(node1); // Recursive call to print inorder
			}
			System.out.println(" ");
			System.out.print(node.data.get(i));
		}
		// Printing the subtree rooted with last child
		if(node.isLeaf==false){
			Node node1 = null;
			// Trying to read from node's (i)th child
			try
			{
				FileInputStream fileIn = new FileInputStream(node.link.get(i));
				ObjectInputStream in = new ObjectInputStream(fileIn);
				node1 = (Node) in.readObject();
				in.close();
				fileIn.close();
			}catch(IOException e)
			{
				e.printStackTrace();
				return;
			}catch(ClassNotFoundException c)
			{
				System.out.println("BTree class not found");
				c.printStackTrace();
				return;
			}
			print_inorder(node1);
		}
	}
}

/* Class Node : Has the data structure for each node. Implements Serializable class so that each object can be easily stored in disk  */
class Node implements Serializable{
	static int order; // Order of the B-Tree 
	String uniqueID;  // Unique ID of the node
	ArrayList<Integer> data; // ArrayList of integers to contain data
	ArrayList<String>link;   // ArrayList of strings to contain child's information
	int c; // count the number of data in node
	boolean isLeaf; // If the node is a leaf node
	Node parent;    // Parent of this node
    // Constructor for Node to initialize 
	public Node(int orderInput,Node parent){
		order = orderInput;
		this.parent = parent;
		data = new ArrayList<Integer>(2*order -1);
		for(int i=0;i<(2*order) - 1;i++)
			data.add(0);
		link = new ArrayList<String>(2*order);
		for(int i=0;i<2*order;i++)
			link.add(null);
		isLeaf = true;
		uniqueID = "Node"+(BTree.Uid);
		BTree.Uid++; // Incrementing Uid of B-Tree to give unique name for a node
		FileOutputStream fileOut; 
		// Trying to write to current node's uniqueID
		try {
			fileOut = new FileOutputStream(uniqueID);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(this);
	         out.close();
	         fileOut.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}