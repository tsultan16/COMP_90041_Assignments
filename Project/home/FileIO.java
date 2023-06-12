import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class FileIO {


    public static final int DEFAULT_AGE = 15;

    public FileIO() {

    }

    public static ArrayList<Scenario> importScenarios (String filename) {

        // create empty arraylist for storing scenarios imported from file 
        ArrayList<Scenario> scenarios = new ArrayList<Scenario>(0);        

        // open and read from scenario file
        Scanner inStream = null;

        try {
            // open file input stream
            inStream = new Scanner(new FileInputStream(filename));
            
            // skip the first line (column headers)
            inStream.nextLine();
            
            ArrayList<String> fileLines = new ArrayList<String>(0);

            // read all the lines from the file
            // int j = 2;
            while(inStream.hasNextLine()) {
                fileLines.add(inStream.nextLine());
                // System.out.printf("Line %d: %s \n",j,fileLines.get(fileLines.size()-1));
                // j++;
            }
            // close file input stream
            inStream.close();

            // parse the file contents
            int i = 0;
            while(i <  fileLines.size()) {
                String line = fileLines.get(i);
                i++;
                try {
                    //System.out.println(line);
                    String[] sp1 = line.split(",", -1);

                    if(sp1.length != 8) {
                        throw new InvalidDataFormatException("WARNING: invalid data format in scenarios file in line " + (i+1) + ": " + line);
                    }

                    String[] sp2 = sp1[0].split(":",-1);
    
                    // extract each scenario
                    if(sp2[0].equals("scenario")) {
                        String scDescriptor = sp2[1].toLowerCase(); 

                        // instantiate a scenario object   
                        Scenario scen = new Scenario(scDescriptor);

                        boolean doneScenario = false;
                        while((!doneScenario) && (i <  fileLines.size())) {

                            line =  fileLines.get(i);
                            i++;
                            try {
                                //System.out.println(line);
                                String[] sp3 = line.split(",",-1);

                                if(sp3.length != 8) {
                                    throw new InvalidDataFormatException("WARNING: invalid data format in scenarios file in line " + (i+1) + ": " + line);
                                }

                                String[] sp4 = sp3[0].split(":",-1);
                                
                                // extract each location
                                if(sp4[0].equals("location")) {  

                                    String[] locDescriptor = sp4[1].split(";",-1);    
                                    String latitude = locDescriptor[0].toLowerCase();
                                    String longitude = locDescriptor[1].toLowerCase();
                                    String trespassing = locDescriptor[2].toLowerCase(); 
                                    
                                    // instantiate a location object
                                    Location loc = new Location(latitude, longitude, trespassing); 
                                    
                                    boolean doneLocation = false;
                                    while((!doneLocation) && (i <  fileLines.size())) {

                                        // extract each character
                                        line =  fileLines.get(i);
                                        i++;
                                        try {
                                            //System.out.println(line);
                                            String[] sp5 = line.split(",",-1);
                                            //System.out.println("sp5 length: " + sp5.length);

                                            if(sp5.length != 8) {
                                                throw new InvalidDataFormatException("WARNING: invalid data format in scenarios file in line " + (i+1) + ": " + line);
                                            }

                                            String[] sp6 = sp5[0].split(":",-1);
                                            // System.out.println("sp5[0] " + sp5[0]);
                                            // System.out.println("sp6[0] " + sp6[0]);    
                                            
                                            if((sp6[0].equals("human")) || (sp6[0].equals("animal"))) {    
                                                
                                                int age;
                                                try{
                                                    age = Integer.parseInt(sp5[2]);
                                                } catch (NumberFormatException e) {
                                                    System.out.println("WARNING: invalid number format in scenarios file in line " + (i+1));
                                                    age = DEFAULT_AGE;
                                                }
                                                String gender = sp5[1].toLowerCase();                                    
                                                String bodyType = sp5[3].toLowerCase();
                                                Character ch = null;

                                                switch (sp6[0]) {
                                                    case "human":
                                                        String profession = sp5[4].toLowerCase();
                                                        String pregnant = sp5[5].toLowerCase();
                                                        // System.out.printf("******Found human: %s,%s,%s,%s,%s\n",sp5[1],sp5[2],sp5[3],sp5[4],sp5[5]);
                                                        
                                                        // instantiate a human object
                                                        ch = new Human(gender, age, bodyType, profession, pregnant, i+1); 
                                                        break;

                                                    case "animal":
                                                        String species = sp5[6].toLowerCase();
                                                        String isPet = sp5[7].toLowerCase();
                                                        // System.out.printf("******Found animal: %s,%s,%s,%s,%s\n",sp5[1],sp5[2],sp5[3],sp5[6],sp5[7]);

                                                        // instantiate a human object
                                                        ch = new Animal(gender, age, bodyType, species, isPet, i+1); 
                                                        break;
                                                    default:
                                                        // invalid character type
                                                }   
                                                loc.addCharacter(ch);  

                                            } else {
                                                doneLocation = true;
                                            }   

                                        } catch (InvalidDataFormatException e) {
                                            System.out.println(e.getMessage());
                                        }                                       
                                        
                                    }
                                    scen.addLocation(loc);
                                    // System.out.println("Number of characters in this location: " + loc.getNumCharacters());

                                } else {
                                    doneScenario = true;
                                }

                            } catch(InvalidDataFormatException e) {
                                System.out.println(e.getMessage());
                            }

                            
                        }
                        scenarios.add(scen);
                        // System.out.println("Number of locations in this scenario: " + scen.getNumLocations());
                    }

                } catch(InvalidDataFormatException e) {
                    System.out.println(e.getMessage());
                }

                
            }
            System.out.printf("\n\n%d scenarios imported.\n", scenarios.size());
            

        } catch (FileNotFoundException e) {
            System.out.println("java.io.FileNotFoundException: could not find scenarios file.");
            System.exit(1);
        }


        return scenarios;
    }


    public static void main(String[] args) {

        // testing
        FileIO f = new FileIO();
        ArrayList<Scenario> sc = f.importScenarios("scenarios.csv");

    }


}