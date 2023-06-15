import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.io.File;

/**
 * A class for managing all file IO tasks, such as reading from scenario file and
 * reading/writing into log file.
 *
 * @author: Tanzid Sultan
 * ID# 1430660, Email: tanzids@student.unimelb.edu.au
 */
public class FileManager {

    /**
     * constants
     */
    public static final String DEFAULT_LOGFILE = "rescuebot.log";

    /**
    *  instance variables 
    */
    private String logFile;
    private String scenarioFile;
    private boolean logProvided;
    private boolean scenarioProvided;
    private boolean userConsented;
    private Scanner kb;
    private ArrayList <Scenario> fileScenarios;

    /**
	 * Class Constructor
     *
	 * @param kb  Scanner object for reading keyboard input stream
	 */
    public FileManager(Scanner kb) {
        this.logFile = DEFAULT_LOGFILE;
        this.scenarioFile = "";
        this.logProvided = false;
        this.scenarioProvided = false;
        this.userConsented = false;
        this.kb = kb;
        this.fileScenarios = null;
    }

    /**
	 * Accessor method for retrieving imported scenarios data.
     * 
     * @return a copy of the array list containing imported scenarios
	 */
    public ArrayList <Scenario> getFileScenarios() {
        // return a deep copy of the scenarios array list
        ArrayList<Scenario> scenariosCopy = new ArrayList<Scenario>(0);
        for (Scenario sc: this.fileScenarios) {
            scenariosCopy.add(new Scenario(sc));
        }
        return scenariosCopy;
    }

     /**
	 * Mutator method 
     * 
     * @param  filename  user provided scenario filepath
     * @return  whether user provided filepath is valid 
	 */
    public boolean setScenarioFile(String filename) {

        File f = new File(filename);
        try {
            if (f.exists()) { 
                this.scenarioFile = filename;
                this.scenarioProvided = true;        
            } else {
                throw new FileNotFoundException("java.io.FileNotFoundException: could not find scenarios file.");
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
         
        return this.scenarioProvided;
    }

    /**
	 * Mutator method 
     * 
     * @param filename  user provided log filepath
	 */
    public void setLogFile(String filename) {
        this.logFile = filename;
        // setting logProvided flag tells us that the user has provided a file (in case the user file has the same name as the default filename, this flag will distinguish this case)
        this.logProvided = true;
    }

    /**
	 * Accessor method 
     * 
     * @return  scenarios file path
	 */
    public String getScenarioFile() {
        return this.scenarioFile;
    }

     /**
	 * Accessor method 
     * 
     * @return  whether valid scenarios file path has been provided by user
	 */
    public boolean getScenarioProvided() {
        return this.scenarioProvided;
    }

    /**
	 * Helper method for prompting user's consent for saving decisions to log file 
	 */
    public void collectUserConsent() {
        boolean done = false;
        String userInput = "";
        System.out.println("Do you consent to have your decisions saved to a file? (yes/no)");
        while (!done) {
            System.out.print("> ");
            userInput = this.kb.next().toLowerCase();
            try {
                if ((userInput.equals("yes")) || (userInput.equals("no"))) {
                    done = true;
                } else {
                    throw new InvalidInputException("Invalid response! Do you consent to have your decisions saved to a file? (yes/no)");
                }
            } catch (InvalidInputException e) { 
                System.out.println(e.getMessage());
            }   
        } 
        if (userInput.equals("yes")) {
            this.userConsented = true;
        } 
    }

    /**
	 * Helper method for importing data from scenarios file provided by user
	 */
    public void importScenarios () {

        // create empty arraylist for storing scenarios imported from file 
        this.fileScenarios = new ArrayList<Scenario>(0);        

        // open and read from scenario file
        Scanner inStream = null;

        try {
            // open file input stream
            inStream = new Scanner(new FileInputStream(this.scenarioFile));

            // skip the first line (column headers)
            inStream.nextLine();
            
            ArrayList<String> fileLines = new ArrayList<String>(0);

            // read all the lines from the file
            while (inStream.hasNextLine()) {
                fileLines.add(inStream.nextLine());
            }
            // close file input stream
            inStream.close();

            // extract each scenario
            int i = 0;
            while (i <  fileLines.size()) {

                String line = fileLines.get(i);
                i++;
                try {
                    
                    String[] sp1 = line.split(",", -1);
                    if (sp1.length != 8) {
                        throw new InvalidDataFormatException("WARNING: invalid data format in scenarios file in line " + (i+1) + ": " + line);
                    }
                    String[] sp2 = sp1[0].split(":",-1); 

                    
                    if(sp2[0].equals("scenario")) {
                        String scDescriptor = sp2[1].toLowerCase(); 
                        // instantiate a scenario object   
                        Scenario scen = new Scenario(scDescriptor);
                        
                        // extract each location
                        boolean doneLocation = false;
                        while ((!doneLocation) && (i <  fileLines.size())) {

                            line =  fileLines.get(i);
                            i++;                        
                            try {
                                String[] sp3 = line.split(",",-1);
                                if (sp3.length != 8) {
                                    throw new InvalidDataFormatException("WARNING: invalid data format in scenarios file in line " + (i+1) + ": " + line);
                                }
                                String[] sp4 = sp3[0].split(":",-1);          
                            
                                if (sp4[0].equals("location")) {  

                                    String[] locDescriptor = sp4[1].split(";",-1);    
                                    String latitude = locDescriptor[0];
                                    String longitude = locDescriptor[1];
                                    String trespassing = locDescriptor[2].toLowerCase(); 
                                    
                                    // instantiate a location object
                                    Location loc = new Location(latitude, longitude, trespassing, i+1); 

                                    // extract each character
                                    boolean doneCharacter = false;
                                    while ((!doneCharacter) && (i <  fileLines.size())) {

                                        // extract each character
                                        line =  fileLines.get(i);
                                        i++;
                                        try {
                                            String[] sp5 = line.split(",",-1);
                                            if(sp5.length != 8) {
                                                throw new InvalidDataFormatException("WARNING: invalid data format in scenarios file in line " + (i+1) + ": " + line);
                                            }
                                            String[] sp6 = sp5[0].split(":",-1);     
                                                                                
                                            if ((sp6[0].equals("human")) || (sp6[0].equals("animal"))) {    
                                                int age;
                                                try {
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
                                                        // instantiate a human object
                                                        ch = new Human(gender, age, bodyType, profession, pregnant, i+1); 
                                                        break;

                                                    case "animal":
                                                        String species = sp5[6].toLowerCase();
                                                        String isPet = sp5[7].toLowerCase();
                                                        // instantiate an animal object
                                                        ch = new Animal(gender, age, bodyType, species, isPet, i+1); 
                                                        break;
                                                    default:
                                                        // invalid character type (neither huiman or animal)
                                                        System.out.println("Invalid character type, neither human nor animal..Aborting");
                                                        System.exit(1);
                                                }   
                                                loc.addCharacter(ch);                                                  
                                            } else {
                                                doneCharacter = true;
                                                i--;
                                            } 
                                        } catch (InvalidDataFormatException e) {
                                            System.out.println(e.getMessage());
                                        }                                       
                                    }
                                    scen.addLocation(loc);
                                } else {
                                    doneLocation = true;
                                    i--;
                                }   
                            } catch (InvalidDataFormatException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                        this.fileScenarios.add(scen);
                    }                 
                } catch (InvalidDataFormatException e) {
                    System.out.println(e.getMessage());
                }
            }
            System.out.printf("%d scenarios imported.\n", this.fileScenarios.size());
        } catch (FileNotFoundException e) {
            System.out.println("java.io.FileNotFoundException: could not find scenarios file.");
            // reset the scenarios file provided flag
            this.scenarioProvided = false;
        }
    }

    /**
	 * Helper method for saving judged decisions to log file 
     * 
     * @param scenarios  object containing data for all scenarios
     * @param initScenario  first judged scenario    
     * @param finalScenario  last judged scenario    
     * @param decisions  scenario judging decisions (i.e. location number for the group that will be saved by RescueBot)
     * @param decisionMaker  who made the decisions, i.e. user or algorithm
	 */
    public void saveToLogFile(ArrayList<Scenario> scenarios, int initScenario, int finalScenario, int[] decisions, String decisionMaker) {

        // open file stream and save the scenarios
        try {
            PrintWriter outStream = new PrintWriter(new FileOutputStream(this.logFile, true));         

            // save all relevant information pertaining to each scenario
            for (int s = initScenario; s < finalScenario; s++) {
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
                    if (this.userConsented) {
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

    /**
	 * Helper method for importing data from log file 
     * 
     * @throws Exception  exception thrown in case of file not found or file empty
     * @return  data imported from log file
	 */
    public ArrayList<String> importLogFile() throws Exception{

        // read and return all lines from the logfile
        Scanner inStream = null;
        ArrayList<String> fileLines = null;
        
        // open file input stream
        inStream = new Scanner(new FileInputStream(this.logFile));

        // make sure file is not empty
        if (!inStream.hasNextLine()) {
            throw new Exception();
        }
        
        // read all the lines from the file
        fileLines = new ArrayList<String>(0);
        while (inStream.hasNextLine()) {
            fileLines.add(inStream.nextLine());
        }
        // close file input stream
        inStream.close();

        return fileLines;
    }

}
