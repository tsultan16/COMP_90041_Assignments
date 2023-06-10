/**
 * COMP90041, Sem1, 2023: Assignment 1
 * @author: Tanzid Sultan
 */
 
 import java.util.Scanner;

 public class KinderKit {
   public static void main(String[] args) {
 
     // instantiate a scanner object for reading input stream from keyboard
     Scanner keyboard = new Scanner(System.in);
 
     // store command line arguments
     int width = Integer.parseInt(args[0]);
     int height = Integer.parseInt(args[1]);
     String backgroundChar = args[2];
 
     // create and initialize the drawing board object
     DrawingCanvas canvas = new DrawingCanvas(width, height, backgroundChar, keyboard);
     
     // create triangle and rectange objects
     Triangle triangle = new Triangle(canvas, keyboard);
     Rectangle rectangle = new Rectangle(canvas, keyboard);
 
 
     // display welcome message on screen
     System.out.println("----DIGITAL KINDER KIT: LET'S PLAY & LEARN----");
     System.out.println("Current drawing canvas settings:");
     System.out.printf("- Width: %d\n", canvas.getWidth());
     System.out.printf("- Height: %d\n", canvas.getHeight());
     System.out.printf("- Background character: %s\n\n", canvas.getBackground());
 
     // initiate the main menu loop  
     int userOption;
     do{
       // display main menu
       System.out.println("Please select an option. Type 4 to exit.");
       System.out.println("1. Draw triangles");
       System.out.println("2. Draw rectangles");
       System.out.println("3. Update drawing canvas settings");
       System.out.println("4. Exit");
 
       // get option selected by user
       userOption = keyboard.nextInt();
 
       // execute user's option
       if(userOption == 1){
         String triangleOption;
         do{
             triangle.drawTriangles();
             boolean invalidOption;
             do{
                 System.out.println("Draw another triangle (Y/N)?");
                 triangleOption = keyboard.next().toLowerCase();
                 invalidOption = (!triangleOption.equals("y")) && (!triangleOption.equals("n"));
                 if(invalidOption){
                     System.out.println("Unsupported option. Please try again!");
                 }
             } while(invalidOption);
         } while(triangleOption.equals("y"));
       } else if(userOption == 2){
         String rectangleOption;
         do{
             rectangle.drawRectangles();
             boolean invalidOption;
             do{
                 System.out.println("Draw another rectangle (Y/N)?");
                 rectangleOption = keyboard.next().toLowerCase();
                 invalidOption = (!rectangleOption.equals("y")) && (!rectangleOption.equals("n"));
                 if(invalidOption){
                     System.out.println("Unsupported option. Please try again!");
                 }
             } while(invalidOption);
         } while(rectangleOption.equals("y"));     
       } else if(userOption == 3){
         canvas.updateSettings();    
       } else if(userOption == 4){
           System.out.println("Goodbye! We hope you had fun :)");    
       } else{
         System.out.println("Unsupported option. Please try again!");
       }
 
     } while(userOption != 4);
 
   }
 
 
 }