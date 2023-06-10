import java.util.Scanner;

/**
 * A class for creating a drawing canvas
 *
 * @author: Tanzid Sultan
 */

public class DrawingCanvas {

    // instance variables
    private int width;
    private int height;
    private String background;
    private Scanner keyboard;

    // contructor for initializing the drawing canvas
    public DrawingCanvas(int width, int height, String background, Scanner keyboard){
      this.width = width; 
      this.height = height; 
      this.background = background;
      this.keyboard = keyboard;
    }

    // method for updating the canvas settings with user input
    public void updateSettings(){
      System.out.print("Canvas width: ");
      this.width = keyboard.nextInt();
      System.out.print("Canvas height: ");
      this.height = keyboard.nextInt();
      System.out.print("Background character: ");
      this.background = keyboard.next();
      
      System.out.print("Drawing canvas has been updated!\n\n");
      System.out.println("Current drawing canvas settings:");
      System.out.printf("- Width: %d\n", this.width);
      System.out.printf("- Height: %d\n", this.height);
      System.out.printf("- Background character: %s\n\n", this.background);
    }
  
    // getter methods
    public int getWidth(){
      return this.width;
    }

    public int getHeight(){
      return this.height;
    }
    
    public String getBackground(){
      return this.background;
    }
}