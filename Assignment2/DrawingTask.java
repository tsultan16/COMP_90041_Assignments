import java.util.Scanner;

/**
 * A class for managing the two different drawing task modes: challenge and freestyle
 *
 * @author: Tanzid Sultan
 */
public class DrawingTask {

  /**
   * constants
   */
  private static final int MENU_OPTION1 = 1;  
  private static final int MENU_OPTION2 = 2;  
  private static final int MENU_OPTION3 = 3;  
  private static final int MENU_OPTION4 = 4;  

  /**
   * instance variables 
   * (Note: each of the two drawing modes and the sample drawing have their own separate canvas object)
   */
  private DrawingCanvas previewCanvas;
  private DrawingCanvas challengeCanvas;
  private DrawingCanvas freestyleCanvas;
  private char[][] sampleBitmap;
  private Scanner keyboard;

	/**
	 * Class Constructor
	 * @param preHeiht     predefined challenge mode sample canvas height
	 * @param preWidth     predefined challenge mode sample canvas width
	 * @param preBgChar    predefined challenge mode sample canvas background character
	 * @param sampleBitmap predefined challenge mode sample bitmap array
	 * @param keyboard     Scanner object for reading keyboard input stream
	 */
  public DrawingTask (int preHeight, int preWidth, char preBgChar, char[][] sampleBitmap, Scanner keyboard) {
    // instantiate preview canvas and challengemode canvas objects using pre-defined specs from file
    this.previewCanvas   = new DrawingCanvas(preHeight, preWidth, preBgChar, keyboard);    
    this.challengeCanvas = new DrawingCanvas(preHeight, preWidth, preBgChar, keyboard);    
    // instantiate the freestyle canvas object using default specs 
    this.freestyleCanvas = new DrawingCanvas(keyboard);    
    // keep a reference to the sample drawing
    this.sampleBitmap = sampleBitmap;
    this.keyboard     = keyboard;
  }

	/**
	 * Helper method for navigating the predefined object task/challenge mode sub-menu.
	 */
  public void challengeMode () {
    // initiate sub-menu loop
    int userOption;
    do {
      // disply the sub-menu 
      System.out.println("Please select an option. Type 4 to go back to the main menu.");
      System.out.println("1. Preview the sample drawing"); 
      System.out.println("2. Start/edit the current canvas"); 
      System.out.println("3. Check result"); 
      System.out.println("4. Go back to the main menu");

      // get user option input
      userOption = this.keyboard.nextInt();

      switch (userOption) {
        case MENU_OPTION1:
          // preview the sample drawing
          this.previewSample();
          break;
 
        case MENU_OPTION2:
          // start editing the current canvas
          this.editCanvas(this.challengeCanvas);
          break;

        case MENU_OPTION3:
          // compare the current challenge mode drawing to sample drawing
          this.checkResult(this.challengeCanvas);
          break;

        case MENU_OPTION4:
          // remove all triangles from canvas and navigate back to main menu
          this.challengeCanvas.cleanCanvas();
          break;

        default:
          // invalid user option prompt
          System.out.println("Unsupported option. Please try again.");
      }
    } while (userOption != MENU_OPTION4);
  }

  /**
	 * Helper method for navigating into the freestyle mode sub-menu.
	 */
  public void freestyleMode () {
    // obtain canvas settings from user
    int width, height;
    char bgChar;
    System.out.print("Canvas width: ");
    width = keyboard.nextInt();
    System.out.print("Canvas height: ");
    height = keyboard.nextInt();
    System.out.print("Background character: ");
    bgChar = keyboard.next().charAt(0);

    // update freestyle canvas settings with user input
    this.freestyleCanvas.updateCanvas(height, width, bgChar);

    // initiate sub-menu loop
    int userOption;
    do {
      // disply the sub-menu 
      System.out.println("Please select an option. Type 3 to go back to the main menu.");
      System.out.println("1. Start/edit your current canvas"); 
      System.out.println("2. Share your current drawing"); 
      System.out.println("3. Go back to the main menu"); 

      // get user option input
      userOption = this.keyboard.nextInt();

      switch (userOption) {
        case MENU_OPTION1:
          // start editing the current canvas
          this.editCanvas(this.freestyleCanvas);
          break;
 
        case MENU_OPTION2:
          // share drawing
          this.shareDrawing(this.freestyleCanvas);
          break;

        case MENU_OPTION3:
          // remove all triangles from canvas and navigate back to main menu
          this.freestyleCanvas.cleanCanvas();
          break;

        default:
          // invalid user option prompt
          System.out.println("Unsupported option. Please try again.");
      }
    } while (userOption != MENU_OPTION3);
  }

  /**
	 * Helper method for previewing the sample drawing.
   */
  private void previewSample () {
    // draw the sample image onto the canvas
    System.out.println("This is your task. Just try to draw the same object. Enjoy!");
    this.previewCanvas.drawSampleBitmap(this.sampleBitmap);
  }  

  /**
	 * Helper method for allowing user to edit the drawing on a given canvas.
	 * @param cv the canvas object containing user drawing that can be edited.
	 */
  private void editCanvas (DrawingCanvas cv) {
    // initiate canvas edit sub-menu loop
    int userOption;
    do {
      // disply the current canvas 
      cv.display();

      // display sub-menu
      System.out.println("Please select an option. Type 4 to go back to the previous menu.");
      System.out.println("1. Add a new Triangle");
      System.out.println("2. Edit a triangle");
      System.out.println("3. Remove a triangle");
      System.out.println("4. Go back");
 
      // get user selected option 
      userOption = keyboard.nextInt();

      switch (userOption) {
        case MENU_OPTION1:
          // add new triangle to canvas
          this.addNewTriangle(cv);
          break;

        case MENU_OPTION2: 
          // edit existing triangle
          this.selectEditTriangle(cv);
          break;

        case MENU_OPTION3:
          // remove existing triangle
          this.selectRemoveTriangle(cv);
          break;

        case MENU_OPTION4:
          // navigate back to challenge mode sub-menu
          break;

        default:
          // invalid user option prompt
          System.out.println("Unsupported option. Please try again.");
      } 
    } while (userOption != MENU_OPTION4); 
  } 

  /**
	 * Helper method for comparing user drawing with sample drawing.
	 * @param cv the canvas object containing user drawing to be compared with sample drawing
	 */
  private void checkResult (DrawingCanvas cv) {
    boolean result = cv.compare(this.sampleBitmap); 
    if (result) {
      System.out.println("You successfully complete the drawing task. Congratulations!!");
    } else {
      System.out.println("Not quite! Please edit your canvas and check the result again.");
    }
    // display the sample drawing and user drawing
    System.out.println("This is the sample drawing:");
    this.previewCanvas.drawSampleBitmap(this.sampleBitmap);
    System.out.println("And this is your drawing:");
    cv.display();
  }

	/**
	 * Helper method for adding a new triangle to a canvas.
	 * @param cv the canvas object containing user drawing into which as triangle can be added
	 */ 
  private void addNewTriangle (DrawingCanvas cv) {
    // prompt user for triangle specs
    System.out.println("Side length:");
    int sideLength = keyboard.nextInt();
    System.out.println("Printing character:");
    char printChar = keyboard.next().charAt(0);
    cv.addTriangle(sideLength, printChar);   
  }

	/**
	 * Helper method for obtaining user selection of a triangle to edit.
	 * @param cv the canvas object containing user drawing from which triangles can be selected for editing
	 */ 
  private void selectEditTriangle (DrawingCanvas cv) {
    // first make sure there are triangles on the canvas
    int numTriangles = cv.getTrianglesSize();
    if (numTriangles > 0) {
      // prompt user to select a triangle to edit
      cv.listTrianglesInfo();
      System.out.println("Index of the shape:");
      // get user input
      int shapeIndex = keyboard.nextInt();
      // make sure the user input index is valid
      if (shapeIndex <= numTriangles) {
        // navigate to triangle editing options menu
        cv.editATriangle(shapeIndex-1);
      } else {
        System.out.println("The shape index cannot be larger than the number of shapes!");
      }

    } else {
      System.out.println("The current canvas is clean; there are no shapes to edit!");
    }
  } 

	/**
	 * Helper method for removing a triangle from the canvas.
	 * @param cv the canvas object containing user drawing from which triangles can be selected and removed
	 */ 
  private void selectRemoveTriangle (DrawingCanvas cv) {
    // first make sure there are triangles on the canvas
    int numTriangles = cv.getTrianglesSize();
    if (numTriangles > 0) {
      // prompt user to select a triangle to remove
      cv.listTrianglesInfo();
      System.out.println("Index of the shape to remove:");
      // get user input
      int shapeIndex = keyboard.nextInt();
      // make sure the user input index is valid
      if (shapeIndex <= numTriangles) {
        // remove the triangle 
        cv.removeTriangle(shapeIndex-1);
      } else {
        System.out.println("The shape index cannot be larger than the number of shapes!");
      }

    } else {
      System.out.println("The current canvas is clean; there are no shapes to remove!");
    }
  } 

	/**
	 * Helper method for sharing the details of the currrent canvas drawing .
	 * @param cv the canvas object containing user drawing that will be shared
	 */ 
  private void shareDrawing (DrawingCanvas cv) {
    System.out.println("This is the detail of your current drawing");
    System.out.printf("%d,%d,%c\n",cv.getHeight(), cv.getWidth(), cv.getBackground());

    // display comma separated bitmap array values
    char[][] bitmap = cv.getBitmap();
    for (int i = 0; i < cv.getHeight(); i++) {
      for (int j = 0; j < cv.getWidth(); j++) { 
        System.out.print(bitmap[i][j]);
        if (j < cv.getWidth()-1) {
          System.out.print(",");
        }
      }
      System.out.print("\n");
    }
  }
}

