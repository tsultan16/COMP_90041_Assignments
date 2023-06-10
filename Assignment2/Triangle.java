/**
 * A class for manipulating triangles that can be put on a drawing canvas
 *
 * @author: Tanzid Sultan
 */
public class Triangle {

  /**
   * constants
   */
  private static final int DEFAULT_ORIGIN      = 0; 
  private static final int DEFAULT_SIDE_LENGTH = 2; 
  private static final char DEFAULT_PRINTCHAR  = 'o'; 
  private static final char DEFAULT_BGCHAR     = '-'; 
  
  // we enumerate the 4 possible orientations by the "orientation number": 0,1,2,3
  // where 0 is the default orientation and 1,2,3 represent consecutive clockwise
  // rotations by 90 degrees (this rotation number will aid in triangle zooming operations)
  private static final int ORIENTATION_0   = 0; 
  private static final int ORIENTATION_90  = 1; 
  private static final int ORIENTATION_180 = 2; 
  private static final int ORIENTATION_270 = 3; 
  
  /**
   * instance variables
   */
  private int sideLength;
  private char printChar;
  private char bgChar;
  
  // Note: '(originX, originY)' here are defined as the canvas coordinates of top-left 
  // corner of triangle bounding box
  private int originX; 
  private int originY; 
  
  // this array represents all bitmap characters inside the triangles bounding box
  private char[][] bitmap;
  
  // the default orientation number of the triangle is denoted by 0
  // 90 degree clockwise/anticlockwise rotation will increase/dcrease the orientation 
  // number by 1 (modulo 4).
  private int orientation;

	/**
	 * Default constructor for initializing triangle object with no input specs.
	 */
  public Triangle () {
    this.sideLength  = DEFAULT_SIDE_LENGTH;
    this.printChar   = DEFAULT_PRINTCHAR;
    this.bgChar      = DEFAULT_BGCHAR;
    this.originX     = DEFAULT_ORIGIN;
    this.originY     = DEFAULT_ORIGIN;
    this.bitmap      = new char[DEFAULT_SIDE_LENGTH][DEFAULT_SIDE_LENGTH];
    this.orientation = ORIENTATION_0;
    this.bitmapDefault();
  }

	/**
	 * Constructor for initializing triangle object with input specs.
	 * @param sideLength side length of the triangle
	 * @param printChar  print character for the triangle
	 * @param bgChar     background character of the canvas on which traingle can be placed
	 */
  public Triangle (int sideLength, char printChar, char bgChar) {
    this.sideLength = sideLength;
    this.printChar  = printChar;
    this.bgChar     = bgChar;
    // initialize triangle origin coordinates and orientation to defaults
    this.originX     = DEFAULT_ORIGIN;
    this.originY     = DEFAULT_ORIGIN;
    this.bitmap      = new char[sideLength][sideLength];
    this.orientation = ORIENTATION_0;
    this.bitmapDefault();
  }

  /**
	 * Accessor method for obtaining triangle side length.
	 * @return the triangle side length
	 */
  public int getSideLength () {
    return this.sideLength;
  }

  /**
	 * Accessor method for obtaining the traingle print character
	 * @return the triangle print character
	 */
  public char getPrintChar () {
    return this.printChar;
  }

  /**
	 * Accessor method for obtaining the background print character
	 * @return the background print character
	 */
  public char getBgChar () {
    return this.bgChar;
  }

  /**
	 * Accessor method for obtaining x-coordinate of triangle bounding box topleft corner 
	 * @return x-coordinate of bounding box top left corner
	 */
  public int getOriginX () {
    return this.originX;
  }
 
  /**
	 * Accessor method for obtaining y-coordinate of triangle bounding box topleft corner 
	 * @return y-coordinate of bounding box top left corner
	 */
  public int getOriginY () {
    return this.originY;
  }

  /**
	 * Accessor method for obtaining triangle bounding box bitmap array 
	 * @return array containing bitmap
	 */
  public char[][] getBitmap () {
    // return a deep copy of the triangle bitmap
    char[][] bitmapCopy = new char[this.sideLength][this.sideLength];
    for (int i = 0; i < this.sideLength; i++) {
      for (int j = 0; j < this.sideLength; j++) { 
        bitmapCopy[i][j] = this.bitmap[i][j];
      }
    }
    return bitmapCopy;
  }

  /**
	 * Helper method for initializing bitmap array for triangle with default orientation. 
	 */
  private void bitmapDefault () {
    int count = this.sideLength;
    for (int i = 0; i < this.sideLength; i++) {
      for (int j = 0; j < this.sideLength; j++) { 
        if (j < count) {
          this.bitmap[i][j] = this.printChar;
        }
        else {
          this.bitmap[i][j] = this.bgChar;
        }  
      }
      count--;
    }
  }  

	/**
	 * Mutator method for performing triangle rotations (the input has to be a rotation angle of 
   * either +1 representing 90 degree clockwise or -1 representing 90 degree anti-clockwise)
   *
	 * @param rotationAngle the angle of rotation 
	 */
  public void rotate (int rotationAngle) {
    // make a temp array to hold the transformed triangle bitmap
    char[][] transformedBitmap = new char[this.sideLength][this.sideLength];

    if (rotationAngle == 1) {
      // apply 90 degree clockwise rotation
      // (Note: for +90 degree rotation, the first row of the original bitmap gets mapped to the last columm
      // of transformed bitmap, the secod row gets mapped to second to last column... and so on)
      for (int i = 0; i < this.sideLength; i++) {
        for (int j = 0; j < this.sideLength; j++) {
          transformedBitmap[j][this.sideLength-1-i] = this.bitmap[i][j];
        }
      }
    } else if (rotationAngle == -1) {
      // apply 90 degree anti-clockwise rotation
      // (Note: for -90 degree rotation, the first row of the original bitmap gets reversed then mapped to the first columm
      // of transformed bitmap, the secod row gets reversed then mapped to second column... and so on)
      for (int i = 0; i < this.sideLength; i++) {
        for (int j = this.sideLength-1; j >=0; j--) {
          transformedBitmap[this.sideLength-1-j][i] = this.bitmap[i][j];
        }
      }
    } 

    // update the orientation number of the triangle, i.e. add/subtract 1 (modulo 4) 
    this.orientation = ((this.orientation + rotationAngle)%4 + 4)%4;

    // now replace original bitmap with transformed bitmap
    this.bitmap = transformedBitmap;     
  } 

	/**
	 * Mutator method for magnifying the triangle, i.e. zooming in.
   * (Note: To magnify the triangle, we need to increase it's side length by one.
   * In this algorithm, we start by creating a new bitmap array containing a copy of the old bitmap
   * array in addition to one extra row and one extra column which are initially empty/background. 
   * Magnification can then be achieved by simply filling the extra row with the triangle  
   * print character, which is either top or bottom row depending on triangle orientation.)
	 */
  public void magnify () {
    char[][] transformedBitmap = new char[this.sideLength+1][this.sideLength+1];
    this.fillBitmap(transformedBitmap, this.bgChar);

    // determine which row of new bitmap to fill based on triangle orientation 
    // (We compute "offset" values which indicate the portion of the new bitmap array
    // into which we copy the old bitmap. We fill the top row for orientaion numbers 0 and 1
    // and the bottom row for the remaining two orientations.)
    int rowOffset = 0, colOffset = 0, emptyRow = 0;
    switch (this.orientation) {
      case ORIENTATION_0:
        rowOffset = 1;    
        colOffset = 0;
        emptyRow = 0;
        break;

      case ORIENTATION_90:
        rowOffset = colOffset = 1; 
        emptyRow = 0;   
        break;

      case ORIENTATION_180:
        rowOffset = 0;    
        colOffset = 1;
        emptyRow = this.sideLength;
        break;

      case ORIENTATION_270:
        rowOffset = colOffset = 0; 
        emptyRow = this.sideLength;   
        break;
    }  

    // copy the old bitmap into the new bitmap
    for (int i = 0; i < this.sideLength; i++) {
      for (int j = 0; j < this.sideLength; j++) {
        transformedBitmap[rowOffset+i][colOffset+j] = this.bitmap[i][j];
      }
    }

    // now fill the appropriate empty row
    for (int j = 0; j < this.sideLength+1; j++) {
      transformedBitmap[emptyRow][j] = this.printChar;
    } 

    // finally, update the side length and replace the original bitmap with the new transformed bitmap
    this.sideLength++;
    this.bitmap = transformedBitmap;
  }

	/**
	 * Mutator method for for shrinking the triangle, i.e. zooming out.
	 * (Note: To shrink the triangle, we need to decrease it's side length by one
   * which can be done by simply shaving off one row and one column from
   * the triangles bounding box, which row and column gets shaved off will
   * depend on triangle orientation.)
   */
  public void shrink () {
    char[][] transformedBitmap = new char[this.sideLength-1][this.sideLength-1];
    
    // first we determine which row and column to shave off based on triangle orientation
    // (we compute "offset" values which indicate the remaining portion of the triangle after shaving
    // which needs to be copied into the new array)
    int rowOffset = 0, colOffset = 0;
    switch (this.orientation) {
      case ORIENTATION_0:
        rowOffset = 1;    
        colOffset = 0;
        break;

      case ORIENTATION_90:
        rowOffset = colOffset = 1;    
        break;

      case ORIENTATION_180:
        rowOffset = 0;    
        colOffset = 1;
        break;

      case ORIENTATION_270:
        rowOffset = colOffset = 0;      
        break;
    } 

    // now we shave off a row and column from the triangle's bitmap to obtain a new bitmap for the shrunken triangle
    for (int i = 0; i < this.sideLength-1; i++) {
      for (int j = 0; j < this.sideLength-1; j++) {
        transformedBitmap[i][j] = this.bitmap[rowOffset+i][colOffset+j];
      }
    }
    
    // finally, update the side length and replace the original bitmap with the new transformed bitmap
    this.sideLength--;
    this.bitmap = transformedBitmap;
  }

	/**
	 * Mutator method for updating the triangle origin position.
	 */
  public void move (int delX, int delY) {
    this.originX += delX; 
    this.originY += delY; 
  }

	/**
	 * Helper method for filling a given bitmap array with a given background character.
   * @param bitmap array containing the bitmap
   * @param bgChar the background character
	 */
  private void fillBitmap (char[][] bitmap, char bgChar) {
    for (int i = 0; i < bitmap.length; i++) {
      for (int j = 0; j < bitmap.length; j++) {
        bitmap[i][j] = bgChar;
      }
    }  
  } 
}

