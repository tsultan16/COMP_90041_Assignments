import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * COMP90041, Sem1, 2023: Assignment 2
 * @author: Tanzid Sultan
 */
public class KinderKit {
  
  /**
   * constants
   */
  private static final int CHALLENGE_MODE_OPTION = 1;
  private static final int FREESTYLE_MODE_OPTION = 2;
  private static final int EXIT_OPTION           = 3;

  /**
   * instance variables
   */
  private final Scanner keyboard;
  private DrawingTask task;

	/**
	 * Constructor for initializing a KinderKit object.
	 * @param rows   predefined challenge mode sample canvas height
	 * @param cols   predefined challenge mode sample canvas width
	 * @param bgChar predefined challenge mode sample canvas background character
	 * @param bitmap predefined challenge mode sample canvas bitmap array
	 */
  public KinderKit (int rows, int cols, char bgChar, char[][] bitmap) {
    // instantiate a Scanner object for reading in input stream from keyboard
    this.keyboard = new Scanner(System.in);
    // instantiate a drawing task object
    this.task = new DrawingTask(rows, cols, bgChar, bitmap, this.keyboard);
  }

  /**
	 * Local main method for testing the KinderKit class
	 * @param args command-line arguments for reading in an input file name
	 */
  public static void main(String[] args) {
    //DON'T TOUCH LINES 8 to 59
    //Check program arguments
    if (args.length != 1) {
      System.out.println("This program takes exactly one argument!");
      return;
    }

    //Read sample drawing from file
    Scanner inputStream = null;
    char[][] bitmap = null;
    int rows = 0, cols = 0;
    char bgChar;

    try {
      inputStream = new Scanner(new FileInputStream(args[0]));
      String line;
 
      //Read the first line
      if (inputStream.hasNextLine()) {
        line = inputStream.nextLine();
        String[] tmps = line.split(",");
        if (tmps.length != 3) {
          System.out.println("The given file is in wrong format!");
          return;
        } else {
          rows = Integer.parseInt(tmps[0]);
          cols = Integer.parseInt(tmps[1]);
          bgChar = tmps[2].charAt(0);
          bitmap = new char[rows][cols];
        }
      } else {
        System.out.println("The given file seems empty!");
        return;
      }

      //Read the remaining lines
      int rowIndex = 0;
      while (inputStream.hasNextLine()) {
        line = inputStream.nextLine();
        String[] tmps = line.split(",");
        for(int i = 0; i < tmps.length; i++) {
          bitmap[rowIndex][i] = tmps[i].charAt(0);
        }
        rowIndex++;
      }
      //Close the file input stream
      inputStream.close();
    } catch (FileNotFoundException e) {
      System.out.println("The given file is not found!");
      return;
    }

    //TODO: Write your code from here

    // instantiate a KinderKit object
    KinderKit myKit = new KinderKit(rows, cols, bgChar, bitmap);

    // initiate the KinderKit game
    myKit.startKinderKit();    
  }

  /**
	 * Helper method for initiating and running the KinderKit game.
	 */
  private void startKinderKit () {
    // display welcome message
    System.out.println("----DIGITAL KINDER KIT: LET'S PLAY & LEARN----");

    // initiate main menu loop
    this.mainMenuLoop();

    // terminate the program
    System.out.println("Goodbye! We hope you had fun :)"); 
  }

  /**
	 * Helper method for navigating the main menu loop.
	 */
  private void mainMenuLoop () {
    int userOption;
    do {
      // display the main menu
      this.displayMenu();

      // read in user option input
      userOption = this.keyboard.nextInt();

      // execute the option selected by user
      switch (userOption) {
        case CHALLENGE_MODE_OPTION:
          // navigate to challenge mode sub-menu
          this.task.challengeMode();
          break;

        case FREESTYLE_MODE_OPTION:
          // navigate to freestyle mode sub-menu
          this.task.freestyleMode();
          break;

        case EXIT_OPTION:
          // exit main menu
          break;
          
        default:
          // invalid user option prompt
          System.out.println("Unsupported option. Please try again!");
      } 
    } while (userOption != EXIT_OPTION);  
  }
    
  /**
	 * Helper method for displaying main menu to the screen .
	 */ 
  private void displayMenu () {
    System.out.println("Please select an option. Type 3 to exit.");
    System.out.println("1. Draw a predefined object");
    System.out.println("2. Freestyle Drawing");
    System.out.println("3. Exit");
  }
}

