import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Dictionary {
	private final DNode head;

	public Dictionary() {
		this.head = new DNode();
	}

	/* Adds dictionary from file */ 
	public Dictionary(String fileName) {
		this.head = new DNode();
		addLibrary(fileName);
	}
	
	/* Recursive function to add a single word to the dictionary */  
	public void add(String word) {
		// Cleaning up words
		word = word.toLowerCase();
		word = word.replaceAll("\\W", "");

		// Adding words
		add(word, head);
	}

	private void add(String word, DNode node) {

		if (word.length() > 0) {
			char c = word.charAt(0);
			DNode cn = node.getCharNode(c);

			if (cn == null) {
				node.addChild(c);
				cn = node.getCharNode(c);
			}

			if (word.length() == 1) {
				cn.setWordMarker(true);
			} else {
				add(word.substring(1), node.getCharNode(c));
			}
		}
	}

	/* Searches for a specific word, or prefix.*/
	public boolean checkHelper(String word, boolean forPrefix) {
		DNode tmp = head;
		int i;

		for (i = 0; i < word.length(); i++) {
			tmp = tmp.getCharNode(word.charAt(i));
			if (tmp == null) {
				break;
			}
		}

		if (i == word.length() && tmp.wordMarker == !forPrefix) {
			return true;
		}
		return false;
	}

	public boolean check(String word) {
		return checkHelper(word, false);
	}

	public boolean checkPrefix(String prefix) {
		return checkHelper(prefix, true);
	}

	public void remove(String word) {
		remove(word, head);
	}
	
	/* Remove words and branches if needed */ 
	private void remove(String word, DNode node) {
		DNode tmp = node.getCharNode(word.charAt(0));

		if (tmp != null && word.length() > 1) {
			remove(word.substring(1), tmp);
		} else if (word.length() == 1 && tmp != null) {
			tmp.setWordMarker(false);
		}

		if (tmp != null) {
			boolean hasMore = false;

			for (DNode n : tmp.children) {
				if (n != null) {
					hasMore = true;
				}
			}

			if (!hasMore) {
				node.removeChild(word.charAt(0));
			}
		}

	}

	public void print() {
		print(head, "");
	}
	
	/* Print the contents of the dictionary */ 
	private void print(DNode node, String word) {
		for (DNode n : node.children) {
			if (n != null) {
				if (n.wordMarker) {
					System.out.println(word + n.value);
				}
				print(n, word + n.value);
			}
		}
	}
	
	/* This method finds all words from a group of letters. 
	 * For the Scrabble cheaters. :D 
	 */ 
	public ArrayList<String> findWordsWithLetters(char[] letters) { 
		boolean[] used = new boolean[letters.length]; 
		ArrayList<String> permutations = new ArrayList<String>(); 
		
		findWordsWithLetters(letters, used, permutations, "", 0);
		
		/* Check permutations for valid words */ 
		ArrayList<String> validWords = new ArrayList<String>(); 
		for(String s : permutations) { 
			if(check(s)) { 
				validWords.add(s); 
			}
		}
		
		return validWords;
	}
	
	private void findWordsWithLetters(char[] letters, boolean[] used,
			ArrayList<String> permutations, String builder, int depth) {

		permutations.add(builder.toString());
		if (builder.length() == letters.length) { 
			return; 
		}

		for (int i = 0; i < letters.length; i++) {
			if (!used[i]) { 
				builder += letters[i];  
				used[i] = true;
				findWordsWithLetters(letters, used, permutations, builder, depth + 1);
				used[i] = false;
				builder = builder.substring(0, builder.length() - 1); 
			}
		}
	}

	public void addLibrary(String fileName) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			String word;

			while ((word = reader.readLine()) != null) {
				if (!word.equals("")) {
					add(word); // Add each word to the dictionary.
				}
			}
			reader.close(); 
		} catch (FileNotFoundException e) {
			System.err.println("Error - Could not add library: " + fileName
					+ "(no such file)");
			System.exit(1);
		} catch (IOException ioe) {
			System.err
					.println("Error - Could not add library: Task was interrupted");
			System.exit(1);
		}
	}

	/* Node class */
	private class DNode {
		char value;
		DNode[] children;
		boolean wordMarker;

		public DNode(char c) {
			this.value = c;
			this.children = new DNode[26]; // 1 for each lowercase char.
		}

		// Constructor for head node
		public DNode() {
			this.children = new DNode[26]; // 1 for each lowercase char.
		}

		public DNode getCharNode(char c) {
			return children[(int) c % 97];
		}

		public void setWordMarker(boolean wordMarker) {
			this.wordMarker = wordMarker;
		}

		public void addChild(char c) {
			children[(int) c % 97] = new DNode(c);
		}

		public void removeChild(char c) {
			children[(int) c % 97] = null;
		}
	}
}
