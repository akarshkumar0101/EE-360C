import java.util.*;


public class HuffmanTree {
	/**
	 * The root node of this tree.
	 */
	TreeNode root;

	/**
	 * Mapping from a leaf tree node to the character it represents
 	 */
	private final Map<TreeNode, String> leafNodeToCharacter;
	// the actual encodings are found by traversing the tree (0 for left, 1 for right)
	// but you can also find the encoding by doing treeNode.getValue()
	// (after calling constructHuffman of course)

	/**
	 * Mapping from characters to their encodings
	 */
	private final Map<String, String> characterToEncoding;
	/**
	 * Mapping from encodings to their characters
	 */
	private final Map<String, String> encodingToCharacter;

	/**
	 * String representation of zero
	 */
	private static final String ZERO_STR = String.valueOf('0');
	/**
	 * String representation of one
	 */
	private static final String ONE_STR = String.valueOf('1');

	/**
	 * Default and only constructor. Call constructHuffmanTree immediately after.
	 */
	HuffmanTree() {
		root = null;
		leafNodeToCharacter = new HashMap<>();
		characterToEncoding = new HashMap<>();
		encodingToCharacter = new HashMap<>();
	}

	/**
	 * Constructs the tree with the given parameters. The two lists should be the same size and freq should
	 * represent the character frequency of the character in the same index in the characters list.
	 * @param characters
	 * @param freq
	 */
	public void constructHuffmanTree(ArrayList<String> characters, ArrayList<Integer> freq) {
		/*
		 * Priority queue is used in order to constantly get the minimum frequency nodes.
		 * (Huffman algorithm)
		 */
		PriorityQueue<TreeNode> nodesQueue = new PriorityQueue<>(new Comparator<TreeNode>() {
			// comparing two nodes and return -1 if the first is less frequent, 1 if the second is less frequent and 0 if same frequency.
			@Override
			public int compare(TreeNode node1, TreeNode node2) {
				//return negative if first is to be chosen
				//first is to be chosen if it is less frequent than second
				//return negative if first is less frequent than second
				int freqDiff = node1.getFrequency() - node2.getFrequency();
				return freqDiff;
			}
		});

		/*
		Make the leaf nodes and put their frequency in and add them all to the min queue.
		 */
		for(int i=0;i<characters.size();i++){
			TreeNode leafNode = new TreeNode();
			//get associated values
			String ch = characters.get(i);
			Integer fr = freq.get(i);

			if(fr==null){
				//invalid integer found
				throw new RuntimeException("Illegal Integer value in frequency list: null");
			}

			//set frequency to use in queue
			leafNode.setFrequency(fr);

			//add the map to access leafNodes later
			leafNodeToCharacter.put(leafNode, ch);

			//add to queue
			nodesQueue.add(leafNode);
		}

		while(nodesQueue.size()>1){//has two or more
			//combine two of them and put the combined one back into queue.
			//retrieve two
			TreeNode node1 = nodesQueue.remove();
			TreeNode node2 = nodesQueue.remove();;

			//combine them
			TreeNode combinedNode = new TreeNode();

			combinedNode.setLeftChild(node1);
			combinedNode.setRightChild(node2);
			node1.setParent(combinedNode);
			node2.setParent(combinedNode);

			//add frequencies
			combinedNode.setFrequency(node1.getFrequency() + node2.getFrequency());

			//add back to queue
			nodesQueue.add(combinedNode);
		}
		// if no characters were provided
		if(leafNodeToCharacter.keySet().isEmpty()){
			//then no nodes
			this.root = null;
		}
		else {
			//get the singular final node constructed.
			this.root = nodesQueue.remove();

			//if only one character showed up in message
			if(leafNodeToCharacter.keySet().size()==1){
				//construct a lonely root with encoding ZERO_STR
				constructMappingLoneRootNode(root);
			}
			else{
				//construct a full and valid encoding based on the huffman tree
				constructMapping(root, new StringBuilder());
			}

		}
	}

	/**
	 * Construct a lonely root mapping (one and only one node in the tree).
	 * It assigns that node/character the ZERO_STR for encoding.
	 * @param root
	 */
	private void constructMappingLoneRootNode(TreeNode root){
		characterToEncoding.put(leafNodeToCharacter.get(root), ZERO_STR);
		encodingToCharacter.put(ZERO_STR, leafNodeToCharacter.get(root));
		root.setValue(ZERO_STR);
	}

	/**
	 * Construct a full mapping by traversing the tree and assigning leaf nodes the appropriate encodings.
	 * ZERO_STR for traversing left, and ONE_STR for traversing right. Initially call with an empty encoding StringBuilder.
	 * @param node
	 * @param encoding
	 */
	private void constructMapping(TreeNode node, StringBuilder encoding){
		if(node==null){
			return;
		}
		if(node.getLeftChild()==null && node.getRightChild()==null){
			//found leaf node
			String decodedChar = leafNodeToCharacter.get(node);

			//use the encoding and assign values in the map
			String encodingString = encoding.toString();
			characterToEncoding.put(decodedChar, encodingString);
			encodingToCharacter.put(encodingString, decodedChar);
			node.setValue(encodingString);
		}
		else{
			node.setValue(encoding.toString());
			//traverse left edge
			encoding.append(ZERO_STR);
			constructMapping(node.getLeftChild(), encoding);
			encoding.delete(encoding.length()-1, encoding.length());

			//traverse right edge
			encoding.append(ONE_STR);
			constructMapping(node.getRightChild(), encoding);
			encoding.delete(encoding.length()-1, encoding.length());
		}
	}

	/**
	 * Encode the human message.
	 * @param humanMessage
	 * @return the encoded message
	 */
	public String encode(String humanMessage) {
		StringBuilder encodedMessage = new StringBuilder();
		for(int i=0;i<humanMessage.length();i++){
			char c = humanMessage.charAt(i);
			//encode that char and append to encodedMessage
			encodedMessage.append(characterToEncoding.get(String.valueOf(c)));
		}
	    return encodedMessage.toString();
	}

	/**
	 * Decode the encoded message.
	 * @param encodedMessage must have been written using this huffman tree.
	 * @return the decoded message
	 */
	public String decode(String encodedMessage) {
		StringBuilder humanMessage = new StringBuilder();

		StringBuilder encodedCharacter = new StringBuilder();

		for(int i=0;i<encodedMessage.length();i++){
			char c = encodedMessage.charAt(i);
			encodedCharacter.append(c);

			String encodedCharacterString = encodedCharacter.toString();
			// if the current encoding is valid then we found something to decode
			if(encodingToCharacter.containsKey(encodedCharacterString)){
				//found a full character
				//decode it and append to humanMessage
				String humanCharacter = encodingToCharacter.get(encodedCharacterString);
				humanMessage.append(humanCharacter);
				//reset the encoding to restart.
				encodedCharacter.setLength(0);
			}
		}
		//make sure no left over characters
		if(encodedCharacter.length()!=0){
			throw new RuntimeException("Invalid character encoding found. This tree was not used to encode this message.");
		}

		return humanMessage.toString();
	}

	/**
	 * @return map associated with turning characters into their encodings.
	 */
	public Map<String,String> getCharacterToEncoding(){
		return characterToEncoding;
	}

	/**
	 * @return map associated with turning encodings into their characters.
	 */
	public Map<String,String> getEncodingToCharacter(){
		return encodingToCharacter;
	}

	/**
	 * Visual representation of the tree as a string. Useful for debugging.
	 * @return
	 */
	@Override
	public String toString(){
		return toString(root);
	}

	/**
	 * A useful toString for the TreeNode class, because modify TreeNode is not modifiable.
	 * @param node
	 * @return the toString for the given node. Useful for debugging.
	 */
	private String toString(TreeNode node){
		if(node==null){
			return "{EMPTY_TREE}";
		}
		String str = "";

		str +="{";

		if(leafNodeToCharacter.containsKey(node)){
			str+=leafNodeToCharacter.get(node);
		}
		else{
			str+='-';
			str+=" left: "+toString(node.getLeftChild());
			str+=" right: "+toString(node.getRightChild());
		}

		str +="}";
		return str;
	}
}
