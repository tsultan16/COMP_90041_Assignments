import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

public class FileManager {


    private String logFile;
    private String scenarioFile;
    private boolean logProvided;
    private boolean scenarioProvided;
    private boolean userConsented;
    private Scanner kb;

    public static final String DEFAULT_LOGFILE = "rescuebot.log";

    public FileManager(Scanner kb) {
        this.logFile = DEFAULT_LOGFILE;
        this.scenarioFile = "";
        this.logProvided = false;
        this.scenarioProvided = false;
        this.userConsented = false;
        this.kb = kb;
    }

    public void setScenarioFile(String filename) {
        this.scenarioFile = filename;
        this.scenarioProvided = true;
    }

    public void setLogFile(String filename) {
        this.logFile = filename;
        // setting logProvided flag tells us that the user has provided a file (in case the user file has the same name as the default filename, this flag will distinguish this case)
        this.logProvided = true;
    }

    public void setUserConsented() {
        this.userConsented = true;
    }

    public String getScenarioFile() {
        return this.scenarioFile;
    }

    public boolean getScenarioProvided() {
        return this.scenarioProvided;
    }

    public ArrayList<Scenario> importScenarios () {

        // create empty arraylist for storing scenarios imported from file 
        ArrayList<Scenario> scenarios = new ArrayList<Scenario>(0);        

        // open and read from scenario file
        Scanner inStream = null;

        try {
            // open file input stream
            inStream = new Scanner(new FileInputStream(this.scenarioFile));
            //System.out.println("Reading from scenario file: " + this.scenarioFile);

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

            // extract each scenario
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

                    
                    if(sp2[0].equals("scenario")) {
                        String scDescriptor = sp2[1].toLowerCase(); 

                        // System.out.println("**Found scenario: " + scDescriptor);

                        // instantiate a scenario object   
                        Scenario scen = new Scenario(scDescriptor);

                        // extract each location
                        boolean doneLocation = false;
                        while((!doneLocation) && (i <  fileLines.size())) {

                            line =  fileLines.get(i);
                            i++;
                            // System.out.println("Location loop line read: " + line);
                        
                            try {
                                // System.out.println(line);
                                String[] sp3 = line.split(",",-1);
                                if(sp3.length != 8) {
                                    throw new InvalidDataFormatException("WARNING: invalid data format in scenarios file in line " + (i+1) + ": " + line);
                                }
                                String[] sp4 = sp3[0].split(":",-1);          
                            
                                if(sp4[0].equals("location")) {  

                                    String[] locDescriptor = sp4[1].split(";",-1);    
                                    String latitude = locDescriptor[0];
                                    String longitude = locDescriptor[1];
                                    String trespassing = locDescriptor[2].toLowerCase(); 
                                    
                                    // System.out.printf("****Found location: %s, %s, %s\n",latitude, longitude, trespassing);

                                    // instantiate a location object
                                    Location loc = new Location(latitude, longitude, trespassing, i+1); 

                                    // extract each character
                                    boolean doneCharacter = false;
                                    while((!doneCharacter) && (i <  fileLines.size())) {

                                        // extract each character
                                        line =  fileLines.get(i);
                                        i++;

                                        // System.out.println("Character loop line read: " + line);

                                        try {
                                            String[] sp5 = line.split(",",-1);
                                            if(sp5.length != 8) {
                                                throw new InvalidDataFormatException("WARNING: invalid data format in scenarios file in line " + (i+1) + ": " + line);
                                            }
                                            String[] sp6 = sp5[0].split(":",-1);     
                                                                                
                                            if((sp6[0].equals("human")) || (sp6[0].equals("animal"))) {    
                                                
                                                int age;
                                                try{
                                                    age = Integer.parseInt(sp5[2]);
                                                } catch (NumberFormatException e) {
                                                    System.out.println("WARNING: invalid number format in scenarios file in line " + (i+1));
                                                    age = Character.DEFAULT_AGE;
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

                                                        // instantiate an animal object
                                                        ch = new Animal(gender, age, bodyType, species, isPet, i+1); 
                                                        break;
                                                    default:
                                                        // invalid character type (neither huiman or animal)
                                                        System.out.println("Invalid character type, neither human nor animal..Aborting");
                                                        System.exit(1);
                                                }   
                                                loc.addCharacter(ch);
                                                //i++;
                                                  

                                            } else{
                                                doneCharacter = true;
                                                // System.out.println("\nDone with this location\n");
                                                i--;
                                            } 

                                        } catch (InvalidDataFormatException e) {
                                            System.out.println(e.getMessage());
                                        }                                       
                                    }

                                    scen.addLocation(loc);
                                    
                                    // System.out.println("Number of characters in this location: " + loc.getNumCharacters());

                                } else {
                                    doneLocation = true;
                                    // System.out.println("\nDone with this scenario\n");
                                    i--;
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
            System.out.printf("%d scenarios imported.\n", scenarios.size());
        
        } catch (FileNotFoundException e) {
            System.out.println("java.io.FileNotFoundException: could not find scenarios file.");
            System.exit(1);
        }


        return scenarios;
    }

    public void saveToLogFile(ArrayList<Scenario> scenarios, int initScenario, int finalScenario, int[] decisions, String decisionMaker) {

        // open file stream and save the scenarios
        try{
            PrintWriter outStream = new PrintWriter(new FileOutputStream(this.logFile, true));         

            // save all relevant information pertaining to each scenario
            for(int s = initScenario; s < finalScenario; s++) {
                Scenario sc = scenarios.get(s);
                outStream.println("# Scenario: " + sc.getDescriptor());
                ArrayList<Location> locations = sc.getLocations();
                outStream.println(locations.size() +" locations");
                for (int i = 1; i <= locations.size(); i++) {
                    Location loc = locations.get(i-1);
                    outStream.printf("[%d] Location: %s, %s\n",i, loc.getLatitude(), loc.getLongitude());
                    outStream.println("Trespassing: " + loc.getTrespassing());
                    int numHumans = 0, numAnimals = 0, totalHumanAge = 0;
                    // show each character
                    ArrayList<Character> characters = loc.getCharacters();
                    outStream.printf("%d characters\n",characters.size());
                    for (Character ch: characters) {
                        String[] sp = ch.toString().split(" ");
                        if (sp[0].equals("human")) {
                            numHumans++;
                            totalHumanAge += ch.getAge();
                        } else {
                            numAnimals++;
                        }
                        outStream.println("- " + ch.getAge() + " " + ch);
                    }
                    outStream.println("Humans: " + numHumans + " TotalHumanAge: " +  totalHumanAge + " Animals: " + numAnimals);
                }

                // save user decisions if user has consented
                if (decisionMaker.equals("USER")) {
                    if(this.userConsented) {
                        outStream.println("** USER Decision Location Number: " + (decisions[s]+1));
                    } else {
                        outStream.println("** USER Decision Location Number: noConsent");
                    }
                } else {
                    outStream.println("** ALGORITHM Decision Location Number: " + (decisions[s]+1));
                }
                
            } 
   
            // close file output stream
            outStream.close();

        } catch(FileNotFoundException e) {
            System.out.println("ERROR: could not print results. Target directory does not exist.");
            System.exit(1);
        }
    }

    public ArrayList<String> importLogFile() throws Exception{

        // read and return all lines from the logfile
        // open and read from scenario file
        Scanner inStream = null;
        ArrayList<String> fileLines = null;
        
        // open file input stream
        inStream = new Scanner(new FileInputStream(this.logFile));

        // make sure file is not empty
        if (!inStream.hasNextLine()) {
            throw new Exception();
        }
        
        fileLines = new ArrayList<String>(0);

        // read all the lines from the file
        int j = 1;
        while(inStream.hasNextLine()) {
            fileLines.add(inStream.nextLine());
            System.out.printf("Line %d: %s \n",j,fileLines.get(fileLines.size()-1));
            j++;
        }
        // close file input stream
        inStream.close();

        System.out.println("Done reading from log file!");
        return fileLines;

    }


    private boolean isValidFileName(String filename, String type) {
        boolean valid = false; 
        String[] sp = filename.split("\\.");
        for(String s: sp) {
            System.out.println(s);
        }
        if(sp.length > 1) {
            if(sp[1].equals(type)) {
                valid = true;
            }
        }
        return valid;
    }

    public static void main(String[] args) {

        // testing
        //FileManager f = new FileManager();
        //ArrayList<Scenario> sc = f.importScenarios("scenarios.csv");

    }


}