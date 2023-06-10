import java.util.Scanner;

/**
 * A class for drawing reactangles on a drawing canvas
 *
 * @author: Tanzid Sultan
 */

public class Rectangle {
   
  // instance variables
  private static final int DEFAULT_ORIGIN = 1;
  private DrawingCanvas canvas;   
  private Scanner keyboard;
  private int width;
  private int height;
  private String printChar;
  private int originX;
  private int originY;

  // constructor for initializing rectangle object
  public Rectangle(DrawingCanvas canvas, Scanner keyboard){
    this.canvas = canvas;
    // the origin refers to the co-ordinates of the top left corner of the rectangle
    this.originX = DEFAULT_ORIGIN;
    this.originY = DEFAULT_ORIGIN;
    this.keyboard = keyboard;
  }

  // this method lets the user navigate into the 'Draw rectangles' option from the main menu
  public void drawRectangles(){
 
    // reset rectangle origin coordinates (top left corner) to default
    this.originX = DEFAULT_ORIGIN;
    this.originY = DEFAULT_ORIGIN;

    // get rectangle width from user
    boolean tooLarge = true;
    while(tooLarge){
      System.out.println("width:");
      this.width = Math.abs(keyboard.nextInt()); // using abs to deal with negative input value
      tooLarge = this.width > this.canvas.getWidth();
      if(tooLarge){
        System.out.printf("Error! The width is too large (Current canvas size is %dx%d). Please try again.\n",this.canvas.getWidth(), this.canvas.getHeight());
      }
    }
    // get rectangle height from user
    tooLarge = true;
    while(tooLarge){
      System.out.println("height:");
      this.height = Math.abs(keyboard.nextInt()); // using abs to deal with negative input value
      tooLarge = this.height > this.canvas.getHeight();
      if(tooLarge){
        System.out.printf("Error! The height is too large (Current canvas size is %dx%d). Please try again.\n",this.canvas.getWidth(), this.canvas.getHeight());
      }
    }
    System.out.println("Printing character:");
    this.printChar = keyboard.next();

    // draw the rectangle
    draw(this.originX,this.originY, this.width, this.height);
   
    // give user options for zoom/move
    String userOption;
    do{
      System.out.println("Type Z/M for zooming/moving. Use other keys to quit the Zooming/Moving mode.");
      userOption = keyboard.next().toLowerCase();

      if(userOption.equals("z")){
        // draw the rectangle
        draw(this.originX,this.originY, this.width, this.height);
        // navigate to zoom menu
        zoom();
      } else if(userOption.equals("m")){
        // draw the rectangle
        draw(this.originX,this.originY, this.width, this.height);
        // navigate to move menu
        move(); 
      } 
   
    } while((userOption.equals("z")) || (userOption.equals("m")));
  }

  // method for zooming the rectangle view on the canvas
  private void zoom(){  
    String userOption;
    do{
      System.out.println("Type I/O to zoom in/out. Use other keys to go back to the Zooming/Moving menu.");
      userOption = keyboard.next().toLowerCase();
      if(userOption.equals("i")){
        // draw the zoomed-in rectangle
        this.width++;
        this.height++;
        if(!fitsInCanvas(this.originX,this.originY, this.width, this.height)){
          System.out.println("This rectangle reaches its limit. You cannot make it bigger!");
          this.width--;
          this.height--;
        }
       
      } else if(userOption.equals("o")){
        // draw the zoomed-out rectangle
        this.width--;
        this.height--;
        if((this.width < 1) || (this.height < 1)){
          System.out.println("This rectangle reaches its limit. You cannot make it smaller!");
          this.width++;
          this.height++;
        }
      }
      draw(this.originX,this.originY, this.width, this.height);   
   
    }  while((userOption.equals("i")) || (userOption.equals("o")));
  }

  // method for moving the rectangle across the canvas
  private void move(){
    String userOption;
    do{
      System.out.println("Type A/S/W/Z to move left/right/up/down. Use other keys to go back to the Zooming/Moving menu.");
      userOption = keyboard.next().toLowerCase();
      if(userOption.equals("a")){
        // move one pixel to the left
        this.originX--;
        if(this.originX < 1){
          System.out.println("You cannot move this rectangle outside of the drawing canvas!");
          this.originX++;
        }
      } else if(userOption.equals("s")){
        // move one pixel to the right
        this.originX++;
        if(!fitsInCanvas(this.originX,this.originY, this.width, this.height)){
          System.out.println("You cannot move this rectangle outside of the drawing canvas!");
          this.originX--;
        }
      } else if(userOption.equals("w")){
        // move one pixel up
        this.originY--;
        if(this.originY < 1){
          System.out.println("You cannot move this rectangle outside of the drawing canvas!");
          this.originY++;
        }
      } else if(userOption.equals("z")){
        // move one pixel down
        this.originY++;
        if(!fitsInCanvas(this.originX,this.originY, this.width, this.height)){
          System.out.println("You cannot move this rectangle outside of the drawing canvas!");
          this.originY--;
        }
      }
      draw(this.originX,this.originY, this.width, this.height);   
   
    }  while((userOption.equals("a")) || (userOption.equals("s")) || (userOption.equals ("w")) || (userOption.equals("z")));
  }

  // method for drawing a rectangle on the canvas
  private void draw(int originX, int originY, int width, int height){
    // before drawing the rectangle on the canvas, first make sure that it will fit
    if(fitsInCanvas(originX, originY, width, height)){
      String line;
      for(int i = 1; i <= this.canvas.getHeight(); i++){
        if((i >= originY) && (i <= (originY + height -1))){
            line = this.canvas.getBackground().repeat(originX-1); // background chars
            line += this.printChar.repeat(width); // rectangle chars
            line += this.canvas.getBackground().repeat(this.canvas.getWidth()-(width)-(originX-1)); // background chars
            System.out.printf("%s\n",line);
        } else{
          System.out.printf("%s\n", this.canvas.getBackground().repeat(this.canvas.getWidth()));
        }
      }
    } 
  }

  // method for checking if a rectangle fits inside the canvas 
  private boolean fitsInCanvas(int originX, int originY, int width, int height){
    if(((originX + width -1) <= this.canvas.getWidth()) &&
        ((originY + height -1) <= this.canvas.getHeight())){
      return true;
    } else {
      return false;
    }
  } 

}