package controller;

import java.util.Scanner;

/**
 * Interface defining operations for application controllers.
 */
public interface IController {

  /**
   * This method accepts inputs from user either through the keyboard or
   * in the form of a predefined script, parses the input and calls the
   * respective method in the model to perform an operation if the command
   * is valid, else it displays an invalid command message to user or throws an error.
   */
  void processCommand();

  /**
   * Starts the program and processes user commands.
   *
   * @param sc Scanner instance for reading commands.
   */
  void executeCommands(Scanner sc);
}
