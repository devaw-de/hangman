import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    private static String FILE_PATH = "wordlist.txt";
    private static ArrayList<String> wordList;
    private static boolean won = false;
    private static int failures;
    private static int maxFailures = 4;
    private static String word;
    private static char[] solution;


    private static ArrayList<String> readWordList() throws IOException {
      ArrayList<String> data = new ArrayList<String>();
      ClassLoader classloader = Thread.currentThread().getContextClassLoader();
      InputStream inputStream = classloader.getResourceAsStream(FILE_PATH);
      if(inputStream != null) {
        InputStreamReader streamReader = new InputStreamReader(inputStream, Charset.defaultCharset());
        BufferedReader reader = new BufferedReader(streamReader);
        for (String line; (line = reader.readLine()) != null; ) {
          data.add(line);
        }
      }
      return data;
    }


    private static String getRandomWord() {
      int r = (int) Math.round(Math.random() * wordList.size());
      return wordList.get(r);
    }


    private static char readInput() {
      Scanner scanner = new Scanner(System.in);
      String guess = scanner.nextLine().toUpperCase();
      if(guess.length() != 1) {
        System.out.println("Please only one letter. Try again.");
        return readInput();
      }
      else if(!Character.isLetter(guess.charAt(0))) {
        System.out.println("Letters only, please. Try again.");
        return readInput();
      }
      else if(guess.charAt(0) < 65 || guess.charAt(0) > 122) {
        System.out.println("English letters only, please. Try again.");
        return readInput();
      }
      else {
        return guess.charAt(0);
      }
    }


    private static void checkGuess(char letter) {
      int hits = 0;
      for(int i=0; i<word.length(); i++) {
        if(word.charAt(i) == letter) {
          hits++;
          solution[i*2] = letter;
        }
      }
      if(hits == 0) failures++;
      else checkForWin();
    }


    private static void checkForWin() {
      for(int i=0; i<word.length(); i++) {
        if(solution[i*2] == '_') {
          return;
        }
      }
      won = true;
    }


    private static void display() {
      String display = new String(solution);
      display += "\t\t(" + failures + "/" + maxFailures + ")";
      System.out.println(display);
    }


    private static char[] createSolution() {
      String tmp = "";
      for(int i=0; i<word.length(); i++) {
        tmp += "_ ";
      }
      return tmp.toCharArray();
    }


    public static void main(String[] args) throws IOException {
      failures = 0;
      wordList = readWordList();
      word = getRandomWord().toUpperCase();
      solution = createSolution();
      System.out.println("Guess this word (" + word.length() + " letters):");

      while(!won && failures <= maxFailures) {
        display();
        checkGuess(readInput());
      }

      if(won) {
        System.out.println("Congratulations! The word was " + word);
      }
      else {
        System.out.println("Better luck next time.");
      }
     }
}
