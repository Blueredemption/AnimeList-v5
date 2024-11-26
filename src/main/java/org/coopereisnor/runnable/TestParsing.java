package org.coopereisnor.runnable;

import org.coopereisnor.malScrape.MalParser;

import java.io.IOException;
import java.util.Scanner;

public class TestParsing {
  public static void acceptUserInput() throws IOException {
    Scanner scanner = new Scanner(System.in);
    System.out.println("Enter links (type 'exit' to stop):");

    while (true) {
      String link = scanner.nextLine();
      if ("exit".equalsIgnoreCase(link)) {
        System.out.println("o/");
        break;
      }

      MalParser malParser = new MalParser(link);
      System.out.println(malParser.getNewOccurrence());
    }

    scanner.close();
  }

  public static void main(String[] args) throws IOException {
    acceptUserInput();
  }
}
