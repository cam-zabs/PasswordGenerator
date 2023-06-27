import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class xkcdpwgen {

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);

    // defines the arguments for different types of characters
    int words = 4;
    int caps = 0;
    int numbers = 0;
    int symbols = 0;

    for (int i = 0; i < args.length; i++) {
      if (args[i].equals("-w") && i + 1 < args.length) {
        words = Integer.parseInt(args[i + 1]);
      }
      if (args[i].equals("-c") && i + 1 < args.length) {
        caps = Integer.parseInt(args[i + 1]);
      }
      if (args[i].equals("-n") && i + 1 < args.length) {
        numbers = Integer.parseInt(args[i + 1]);
      }
      if (args[i].equals("-s") && i + 1 < args.length) {
        symbols = Integer.parseInt(args[i + 1]);
      }
    }

    // makes a list of words for the password
    List<String> wordsList = new ArrayList<String>();
    try {
      File file = new File("words.txt");
      Scanner sc = new Scanner(file);
      while (sc.hasNextLine()) {
        wordsList.add(sc.nextLine());
      }
      sc.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    // makes a list of symbols for the password
    List<String> symbolsList = new ArrayList<String>();
    try {
      File file = new File("symbols.txt");
      Scanner sc = new Scanner(file);
      while (sc.hasNextLine()) {
        symbolsList.add(sc.nextLine());
      }
      sc.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    // define constants for arguments
    int w = words;
    int c = caps;
    int n = numbers;
    int s = symbols;
    int replaceInt = 0;
    List<String> password = new ArrayList<String>();
    List<Integer> capitalized = new ArrayList<Integer>();

    // makes a string with required words
    for (int i = 0; i < w; i++) {
      if (!wordsList.isEmpty()) {
        password.add(wordsList.get(new Random().nextInt(wordsList.size())));
      }
    }

    // edit password based on arguments
    String passwordString = String.join("", password);
    if (c > passwordString.length()) {
      c = passwordString.length(); // limit the caps to the length of a pw
    }
    int count = 0;
    int availableSpace = passwordString.length() - w;
    if (availableSpace <= 0) {
      System.out.println("Error: not enough space for symbols and numbers");
      return;
    }
    // caps
    while (count < c) {
      replaceInt = new Random().nextInt(password.size());
      if (!capitalized.contains(replaceInt)) {
        String word = password.get(replaceInt);
        password.set(replaceInt, Character.toUpperCase(word.charAt(0)) + word.substring(1));
        capitalized.add(replaceInt);
        count++;
      }
    }

    int remaining = passwordString.length() - w;
    for (int i : capitalized) {
      remaining -= password.get(i).length();
      remaining += password.get(i).length();
    }

    // checks if there is enough space for symbols and numbers
    if (passwordString.length() - s - n < 1) {
      System.out.println("Error: not enough space for symbols and numbers");
      return;
    }

    // adjust number of symbols and numbers
    s = Math.min(s, remaining / 2);
    n = Math.min(n, remaining - s);

    // adds numbers and symbols
    count = 0;
    while (count < s) {
      replaceInt = new Random().nextInt(passwordString.length());
      passwordString = passwordString.substring(0, replaceInt)
          + symbolsList.get(new Random().nextInt(symbolsList.size()))
          + passwordString.substring(replaceInt);
      count++;
    }
    count = 0;
    while (count < n) {
      replaceInt = new Random().nextInt(passwordString.length() - s);
      passwordString = passwordString.substring(0, replaceInt) + new Random().nextInt(10)
          + passwordString.substring(replaceInt);
      count++;
    }
    System.out.println(passwordString);
  }
}