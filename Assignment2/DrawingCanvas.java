import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 * A class for manipulating a drawing canvas
 *
 * @author: Tanzid Sultan
 * ID# 1430660, Email: tanzids@student.unimelb.edu.au
 */
public class DrawingCanvas {

  /**
   * constants
   */
  private static final int DEFAULT_HEIGHT  = 5; 
  private static final int DEFAULT_WIDTH   = 5; 
  private static final char DEFAULT_BGCHAR = '*'; 

  /**
   * instance variables
   */
  private int width;
  private int height;
  private char bgChar;
  private ArrayList<Triangle> triangles;
  private char[][] bitmap;
  private Scanner keyboard;

	/**
	 * Default constructor for initializing drawing canvas object with no input specs.
	 * @param keyboard Scanner object for reading keyboard input stream
	 */
  public DrawingCanvas (Scanner keyboard) {
    this.height    = DEFAULT_HEIGHT;
    this.width     = DEFAULT_WIDTH;
    this.bgChar    = DEFAULT_BGCHAR;
    this.triangles = new ArrayList<Triangle>(0);
    this.bitmap    = new char[DEFAULT_HEIGHT][DEFAULT_WIDTH];
    this.keyboard  = keyboard;
    // fill canvas with background characters
    this.fillBackground();
  }

	/**
	 * Constructor for initializing drawing canvas object with input specs.
	 * @param height   height of the canvas
	 * @param width    width of the canvas
	 * @param bgChar   background character for the canvas
	 * @param keyboard Scanner object for reading keyboard input stream
	 */
  public DrawingCanvas (int height, int width, char bgChar, Scanner keyboard) {
    this.height    = height;
    this.width     = width;
    this.bgChar    = bgChar;
    this.triangles = new ArrayList<Triangle>(0);
    this.bitmap    = new char[height][width];
    this.keyboard  = keyboard;
    // fill canvas with background characters
    this.fillBackground();
  }

  /**
	 * Accessor method for obtaining canvas width.
	 * @return the canvas width
	 */
  public int getWidth () {
    return this.width;
  }

  /**
	 * Accessor method for obtaining canvas height.
	 * @return the canvas height
	 */
  public int getHeight () {
    return this.height;
  }

  /**
	 * Accessor method for obtaining canvas background character.
	 * @return the canvas background character
	 */    
  public char getBackground () {
    return this.bgChar;
  }

  /**
	 * Accessor method for obtaining canvas bitmap.
	 * @return the canvas bitmap array
	 */
  public char[][] getBitmap () {
    // return a deep copy of the canvas bitmap 
    char[][] bitmapCopy = new char[this.height][this.width];
    for (int i = 0; i < this.height; i++) {
      for (int j = 0; j < this.width; j++) { 
        bitmapCopy[i][j] = this.bitmap[i][j];
      }
    }
    return bitmapCopy;
  }

  /**
	 * Accessor method for obtaining the number of triangles currently in the canvas.
	 * @return the number of triangles
	 */
  public int getTrianglesSize () {
    return this.triangles.size();
  }

  /**
	 * Helper method for listing out information about all triangles on the canvas.
   * and returning number of triangles in the list
	 */
  public void listTrianglesInfo () {
    int i = 1;
    for (Triangle tr: this.triangles) {
      System.out.printf("Shape #%d - Triangle: xPos = %d, yPos = %d, tChar = %c\n", 
                         i,tr.getOriginX(),tr.getOriginY(), tr.getPrintChar());
      i++;
    }
  }

	/**
	 * Mutator method for updating the canvas settings.
   * (Note: any existing triangles are also removed so that we start with a fresh canvas)
	 * @param newHeight new height for the updated canvas
	 * @param newWidth  new width for the updated canvas
	 * @param newBgChar new background character for the updated canvas
	 */
  public void updateCanvas (int newHeight, int newWidth, char newBgChar) {
    this.height = newHeight;
    this.width  = newWidth;
    this.bgChar = newBgChar;
    this.bitmap = new char[newHeight][newWidth];
    this.triangles.clear();
    // fill canvas with background characters
    this.fillBackground();
  }

	/**
	 * Mutator method for adding a new traingle.
	 * @param sideLength length of the new triangle
	 * @param printChar  print character of the new triangle
	 */
  public void addTriangle (int sideLength, char printChar) {
    // create a new triangle object and add it to the ArrayList
    Triangle newT = new Triangle(sideLength, printChar, this.bgChar);
    this.triangles.add(newT);
    
    // navigate to triangle editing options
    this.editATriangle(this.triangles.size()-1);
  }

	/**
	 * Mutator method for removing an existing traingle.
	 * @param triangleIndex list position index of the triangle that will be removed
	 */ 
  public void removeTriangle (int triangleIndex) {
    this.triangles.remove(triangleIndex); 
  }

	/**
	 * Mutator method for cleaning the canvas, i.e.removing all triangles.
	 */ 
  public void cleanCanvas () {
    this.triangles.clear();
  }

	/**
	 * Mutator method for editing a single given triangle, i.e zooming, moving and rotation.
	 * (Note: When editing a triangle, we temporarily swap it with the last element in the 
   * arraylist, if it is already not the last element. We do this because when displaying 
   * the triangles on the canvas using the display() method, the last triangle in the list   
   * appears on the foreground, which is what we want for the triangle being edited.)
   *
   * @param triangleIndex list position index of the triangle that will be edited
	 */ 
  public void editATriangle (int triangleIndex) {
    // initiate triangle edit loop
    Triangle tr = this.triangles.get(triangleIndex);
    // temporarily swap with the last element in triangles arraylist
    if (triangleIndex != this.triangles.size()-1) {
      Collections.swap(this.triangles, triangleIndex, this.triangles.size()-1); 
    }  
    boolean doneEditing = false;
    char userOption;
    do {
      // display the canvas
      this.display();

      // prompt user for edit option
      System.out.println("Type Z/M/R for zooming/moving/rotating. Use other keys to quit the Zooming/Moving/Rotating mode.");
      userOption = keyboard.next().toLowerCase().charAt(0);

      switch (userOption) {
        case 'z':
          // navigate to zoom menu
          this.zoom(tr);
          break;

        case 'm':
          // navigate to move menu
          this.move(tr);
          break;

        case 'r':
          // navigate to rotate menu
          this.rotate(tr);
          break;

        default:
          // exit menu
          doneEditing = true;
      }
    } while (!doneEditing);
    // now that we're done editing, swap back the last element in triangles arraylist
    if (triangleIndex != this.triangles.size()-1) {
      Collections.swap(this.triangles, triangleIndex, this.triangles.size()-1); 
    }  
  }

	/**
	 * Helper method for triangle zooming.
	 * @param tr the triangle object that we are manipulating
	 */ 
  private void zoom (Triangle tr) {
    // initiate triangle zoom loop
    boolean doneZooming = false;
    boolean willFit;
    char userOption;
    do {
      // display the canvas
      this.display();

      // prompt user for edit option
      System.out.println("Type I/O to zoom in/out. Use other keys to go back to the Zooming/Moving/Rotating menu.");
      userOption = keyboard.next().toLowerCase().charAt(0);

      switch (userOption) {
        case 'i':
          // zoom in, i.e. magnify the triangle
          willFit =  ((tr.getOriginX() + tr.getSideLength()) < this.width) &&
                     ((tr.getOriginY() + tr.getSideLength()) < this.height);
          if (willFit) {
            tr.magnify();
          } else {
            System.out.println("This triangle reaches its limit. You cannot make it bigger!");
          }
          break;

        case 'o':
          // zoom out, i.e. shrink the triangle
          willFit = tr.getSideLength() > 1;
          if (willFit) {
            tr.shrink();
          } else {
            System.out.println("This triangle reaches its limit. You cannot make it smaller!");  
          }
          break;

        default:
          // exit menu
          doneZooming = true;
      }
    } while (!doneZooming);
  }

	/**
	 * Helper method for triangle moving.
	 * @param tr the triangle object that we are manipulating
	 */ 
  private void move (Triangle tr) {
    // initiate triangle move loop
    boolean doneMoving = false;
    boolean willFit;
    char userOption;
    do {
      // display the canvas
      this.display();

      // prompt user for edit option
      System.out.println("Type A/S/W/Z to move left/right/up/down. Use other keys to go back to the Zooming/Moving/Rotating menu.");
      userOption = keyboard.next().toLowerCase().charAt(0);

      switch (userOption) {
        case 'a':
          // move one unit to the left
          willFit =  (tr.getOriginX() - 1) >= 0;
          if (willFit) {
            tr.move(-1,0);
          } else {
            System.out.println("You cannot move this triangle outside of the drawing canvas!");
          }
          break;

        case 's':
          // move one unit to the right
          willFit =  (tr.getOriginX() + tr.getSideLength()) < this.width;
          if (willFit) {
            tr.move(1,0);
          } else {
            System.out.println("You cannot move this triangle outside of the drawing canvas!");
          }
          break;

        case 'w':
          // move one unit up
          willFit =  (tr.getOriginY() - 1) >= 0;
          if (willFit) {
            tr.move(0,-1);
          } else {
            System.out.println("You cannot move this triangle outside of the drawing canvas!");
          }
          break;

        case 'z':
          // move one unit down
          willFit =  (tr.getOriginY() + tr.getSideLength()) < this.height;
          if (willFit) {
            tr.move(0,1);
          } else {
            System.out.println("You cannot move this triangle outside of the drawing canvas!");
          }
          break;

        default:
          // exit menu
          doneMoving = true;  
      }
    } while (!doneMoving);
  }

	/**
	 * Helper method for triangle rotation.
	 * @param tr the triangle object that we are manipulating
	 */ 
  private void rotate (Triangle tr) {
    // initiate triangle rotate loop
    boolean doneRotating = false;
    char userOption;
    do {
      // display the canvas
      this.display();

      // prompt user for edit option
      System.out.println("Type R/L to rotate clockwise/anti-clockwise. Use other keys to go back to the Zooming/Moving/Rotating menu.");
      userOption = keyboard.next().toLowerCase().charAt(0);

      switch (userOption) {
        case 'r':
          // rotate 90 degrees clockwise
          tr.rotate(1);
          break;

        case 'l':
          // rotate 90 degrees anti-clockwise
          tr.rotate(-1); 
          break;  
        
        default:
          // exit menu
          doneRotating = true;
      }
    } while (!doneRotating);
  }

	/**
	 * Helper method for drawing a sample image bitmap onto the canvas.
	 * @param inputBitmap the sample image bitmap array to be drawn
	 */ 
  public void drawSampleBitmap (char[][] inputBitmap) { 
    // first make sure the size of the input bitmap matches canvas size
    if ((inputBitmap.length == this.height) && 
        (inputBitmap[0].length == this.width)) {
      for (int i = 0; i < inputBitmap.length; i++) {
        for (int j = 0; j < inputBitmap[0].length; j++) {
          System.out.printf("%c",inputBitmap[i][j]);
        }
        System.out.print("\n");
      }        
    } else {
      System.out.println("Fatal error! Input bitmap incompatible with canvas.");
    }
  }

	/**
	 * Helper method for comparing current user drawing on the canvas with a given sample drawing.
	 * @param sampleDrawingBitmap the sample bitmap array that will be compared to the user drawing bitmap
   * @return the result of the comparison
	 */
  public boolean compare (char[][] sampleDrawingBitmap) {
    // make sure sample drawing is the same size as canvas drawing 
    if ((sampleDrawingBitmap.length == this.bitmap.length) && 
       (sampleDrawingBitmap[0].length == this.bitmap[0].length)) {
      // compare every pixel to see if there's a match 
      for (int i = 0; i < this.bitmap.length; i++) {
        for (int j = 0; j < this.bitmap[0].length; j++) {
          if (this.bitmap[i][j] != sampleDrawingBitmap[i][j]) {
            return false;
          }
        }
      }    
    } else {
      System.out.println("Error! Sample drawing size does not match canvas drawing size.");
      return false;
    }
    return true;   
  }

	/**
	 * Helper method for displaying the canvas onto the screen.
	 */
  public void display () {
    // start by filling the bitmap with background character
    this.fillBackground();

    // next, add in triangles one by one into the canvas bitmap,
    // (Note: when adding in a triangle, everything in existing bitmap is treated as background and the 
    // triangle print characters from its bounding box bitmap replaces existing content in the canvas 
    // bitmap. So if there are overlapping triangles, the triangle added last will be in the foreground.)
    int sideLength, originX, originY;
    char printChar;
    char[][] triangleBitmap;
    // iterate over each triangle in the indexed order
    for (Triangle tr: this.triangles) {
      // get triangle internal specs
      sideLength     = tr.getSideLength();
      originX        = tr.getOriginX(); 
      originY        = tr.getOriginY(); 
      printChar      = tr.getPrintChar(); 
      triangleBitmap = tr.getBitmap();

      // place triangle inside the canvas bitmap  
      for (int i = 0; i < sideLength; i++) {
        for (int j = 0; j < sideLength; j++) {
          // only copy triangle print characters from triangle bitmap
          if (triangleBitmap[i][j] == printChar) {
            this.bitmap[originY+i][originX+j] = triangleBitmap[i][j];
          }
        }
      }
    }

    // finally, display the canvas bitmap on screen
    for (int i = 0; i < this.bitmap.length; i++) {
      for (int j = 0; j < this.bitmap[0].length; j++) {
        System.out.printf("%c",this.bitmap[i][j]);
      }
    System.out.print("\n");
    }
  }

	/**
	 * Helper method for filling the canvas bitmap array with background characters.
	 */
  private void fillBackground () {
    for (int i = 0; i < this.bitmap.length; i++) {
      for (int j = 0; j < this.bitmap[0].length; j++) {
        this.bitmap[i][j] = this.bgChar;  
      }
    }
  }
}

