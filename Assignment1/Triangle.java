import java.util.Scanner;

/**
 * A class for drawing triangles on a drawing canvas
 *
 * @author: Tanzid Sultan
 */

public class Triangle {

  // instance variables
  private static final int DEFAULT_ORIGIN = 1; 
  private DrawingCanvas canvas;   
  private Scanner keyboard;
  private int sideLength;
  private String printChar;
  private int originX;
  private int originY;

  // contructor for initializing triangle object
  public Triangle(DrawingCanvas canvas, Scanner keyboard){
    this.canvas = canvas;
    // the origin refers to the co-ordinates of the top left corner of the triangle
    this.originX = DEFAULT_ORIGIN;
    this.originY = DEFAULT_ORIGIN;
    this.keyboard = keyboard;
  }

  // this method lets the user navigate into the 'Draw triangles' option from the main menu
  public void drawTriangles(){
  
    // set triangle origin coordinates (top left corner) to default
    this.originX = DEFAULT_ORIGIN;
    this.originY = DEFAULT_ORIGIN;

    // get triangle side length from user
    boolean tooLong = true;
    while(tooLong){
      System.out.println("Side length:");
      this.sideLength = keyboard.nextInt(); 
      tooLong = this.sideLength > Math.min(this.canvas.getWidth(), this.canvas.getHeight());
      if(tooLong){
        System.out.printf("Error! The side length is too long (Current canvas size is %dx%d). Please try again.\n",this.canvas.getWidth(), this.canvas.getHeight());
      }
    }
    System.out.println("Printing character:");
    this.printChar = keyboard.next();

    // draw the triangle
    draw(this.originX,this.originY, this.sideLength);
    
    // give user options for zoom/move
    String userOption;
    do{
      System.out.println("Type Z/M for zooming/moving. Use other keys to quit the Zooming/Moving mode.");
      userOption = keyboard.next().toLowerCase();
     
      if(userOption.equals("z")){ 
        // draw the triangle
        draw(this.originX,this.originY, this.sideLength);
        // navigate to zoom menu
        zoom();
      } else if(userOption.equals("m")){
        // draw the triangle
        draw(this.originX,this.originY, this.sideLength);
        // navigate to move menu
        move(); 
      } 
    
    } while((userOption.equals("z")) || (userOption.equals("m")));
  }

  // method for zooming the triangle view on the canvas
  private void zoom(){  
    String userOption;
    do{
      System.out.println("Type I/O to zoom in/out. Use other keys to go back to the Zooming/Moving menu.");
      userOption = keyboard.next().toLowerCase();
      if(userOption.equals("i")){
        // draw the zoomed-in triangle
        this.sideLength++;
        if(!fitsInCanvas(this.originX,this.originY, this.sideLength)){
          System.out.println("This triangle reaches its limit. You cannot make it bigger!");
          this.sideLength--;
        }
      } else if(userOption.equals("o")){
        // draw the zoomed-out triangle
        this.sideLength--;
        if(this.sideLength < 1){
          System.out.println("This triangle reaches its limit. You cannot make it smaller!");
          this.sideLength++;
        }
      }
      draw(this.originX,this.originY, this.sideLength);   
    
    }  while((userOption.equals("i")) || (userOption.equals("o")));
  }

  // method for moving the traingle across the canvas
  private void move(){
    String userOption;
    do{
      System.out.println("Type A/S/W/Z to move left/right/up/down. Use other keys to go back to the Zooming/Moving menu.");
      userOption = keyboard.next().toLowerCase();
      if(userOption.equals("a")){
        // move one pixel to the left
        this.originX--;
        if(this.originX < 1){
          System.out.println("You cannot move this triangle outside of the drawing canvas!");
          this.originX++;
        }
      } else if(userOption.equals("s")){
        // move one pixel to the right
        this.originX++;
        if(!fitsInCanvas(this.originX,this.originY, this.sideLength)){
          System.out.println("You cannot move this triangle outside of the drawing canvas!");
          this.originX--;
        }
      } else if(userOption.equals("w")){
        // move one pixel up
        this.originY--;
        if(this.originY < 1){
          System.out.println("You cannot move this triangle outside of the drawing canvas!");
          this.originY++;
        }
      } else if(userOption.equals("z")){
        // move one pixel down
        this.originY++;
        if(!fitsInCanvas(this.originX,this.originY, this.sideLength)){
          System.out.println("You cannot move this triangle outside of the drawing canvas!");
          this.originY--;
        }
      }
      draw(this.originX,this.originY, this.sideLength);   
    
    }  while((userOption.equals("a")) || (userOption.equals("s")) || (userOption.equals("w")) || (userOption.equals("z")));
  }

  // method for drawing a triangle on the canvas
  private void draw(int originX, int originY, int sideLength){
    // before drawing the triangle on the canvas, first make sure that it will fit
    if(fitsInCanvas(originX, originY, sideLength)){
      String line;
      int count = 0;
      for(int i = 1; i <= this.canvas.getHeight(); i++){
        if((i >= originY) && (i <= (originY + sideLength -1))){
            line = this.canvas.getBackground().repeat(originX-1); // backround chars
            line += this.printChar.repeat(sideLength-count); // triangle chars
            line += this.canvas.getBackground().repeat(this.canvas.getWidth()-(sideLength-count)-(originX-1)); // background chars
            count++;
            System.out.printf("%s\n",line);
        } else{
          System.out.printf("%s\n", this.canvas.getBackground().repeat(this.canvas.getWidth()));
        }
      }
    } 
  }

  // method for checking if a triangle fits inside the canvas 
  private boolean fitsInCanvas(int originX, int originY, int sideLength){
    if(((originX + sideLength -1) <= this.canvas.getWidth()) &&
        ((originY + sideLength -1) <= this.canvas.getHeight())){
      return true;
    } else {
      return false;
    }
     

  } 

}