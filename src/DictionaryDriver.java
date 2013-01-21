import java.util.ArrayList;

public class DictionaryDriver {
	public static void main(String[] args) {
		Dictionary d = new Dictionary("scrabbleLibrary");
		char[] c = {'d', 'g', 'o'}; 
		ArrayList<String> words = d.findWordsWithLetters(c); 
		
		int i = 0; 
		for(String s : words)  {
			System.out.printf("[%d] %s\n", ++i, s);
		}
	}
}
