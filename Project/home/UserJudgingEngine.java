import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;
import java.util.Set;
import java.util.HashSet;

public class UserJudgingEngine implements JudgingEngine {


    private ArrayList <Scenario> scenarios;
    private int lastScenarioJudged;
    private int[] userDecisions;
    private Scanner kb;
    private ArrayList<String> allCharactersCharacteristics;
    private FileManager fManager;
    private RandomScenarioGenerator rsGenerator;

    public static final String DECISION_MAKER = "USER";


    public UserJudgingEngine(Scanner kb, FileManager fManager) {
        this.scenarios = null;
        this.lastScenarioJudged = 0;
        this.kb = kb;
        this.fManager = fManager;
        this.rsGenerator = new RandomScenarioGenerator();

        // load scenarios
        this.loadScenarios();

        this.userDecisions = new int[scenarios.size()];

        // store characteristics from all characters across all available scenarios and locations
        this.storeAllCharacteristic();
    }

    public void loadScenarios() {

        // import scenarios from file (if option provided), otrherwise generate randomly
        if(this.fManager.getScenarioProvided()){
            //System.out.println("Importing scenarios file: " + rb.scenarioFile);
            scenarios = this.fManager.importScenarios();
        } else {

            //generate random scenarios
            scenarios = this.rsGenerator.generateScenarios();
        }
    }

    public ArrayList<Scenario> getScenarios() {
        // return a deep copy of the scenarios array list
        ArrayList<Scenario> scenariosCopy = new ArrayList<Scenario>(0);
        for(Scenario sc: this.scenarios){
            scenariosCopy.add(new Scenario(sc));
        }
        return scenariosCopy;
    }

    public int[] getuserDecisions() {
        // return a deep copy of the array containing user decisions  
        int[] userDecisionsCopy = new int[scenarios.size()];
        for(int i = 0; i < scenarios.size(); i++) {
            userDecisionsCopy[i] = this.userDecisions[i];
        }
        return userDecisionsCopy;
    }

    public void judgeScenarios() {
        System.out.println("User judging in progress...");  

        boolean done = false;
        String userInput = "";

        while(!done) {
            // judge next three scenarios (get user decisions) 
            int initScenario = this.lastScenarioJudged;
            int finalScenario =  Math.min(this.lastScenarioJudged+3,this.scenarios.size());
            for(int i = initScenario; i < finalScenario; i++) {
                this.userDecisions[i] = this.decide(scenarios.get(i));
            } 
            
            this.lastScenarioJudged = finalScenario;

            // compute and display statistics
            this.generateStatistics();   

            // save judged scenarios to log file
            this.fManager.saveToLogFile(this.getScenarios(), initScenario, finalScenario, this.getuserDecisions(), DECISION_MAKER);

            // prompt user for rerun if more scenarios remaining, if user responds with no, reset the lastScenarioJudged to 0 and the userScenarioJudging[] 
            if(finalScenario == this.scenarios.size()) {
                System.out.println("That's all. Press Enter to return to main menu.");
                System.out.print("> ");
                this.kb.nextLine();
                this.kb.nextLine();
                done = true;
            } else {
                boolean validResponse = false;
                System.out.println("Would you like to continue? (yes/no)");
                while(!validResponse){
                    System.out.print("> ");
                    userInput = this.kb.next().toLowerCase();
                    try{
                        if((userInput.equals("yes")) || (userInput.equals("no"))) {
                            if(userInput.equals("no")) {
                                done = true;
                            }
                            validResponse = true;
                        } else {
                            throw new InvalidInputException("Invalid response! Would you like to continue? (yes/no)");
                        }
                    } catch (InvalidInputException e) { 
                        System.out.println(e.getMessage());
                    }
                }
                
            }
        }

        // reset judged scenarios
        this.lastScenarioJudged = 0;
        this.userDecisions = new int[scenarios.size()];

    }

    // user decides on which location to save
    public int decide(Scenario sc) {

        // display all relevant information pertaining to this scenario
        System.out.println("======================================");
        System.out.println("# Scenario: " + sc.getDescriptor());
        System.out.println("======================================");

        // show each location
        ArrayList<Location> locations = sc.getLocations();
        System.out.println("Number of locations: " +locations.size());
        for (int i = 1; i <= locations.size(); i++) {
            Location loc = locations.get(i-1);
            System.out.printf("[%d] Location: %s, %s\n",i, loc.getLatitude(), loc.getLongitude());
            System.out.println("Trespassing: " + loc.getTrespassing());
            // show each character
            ArrayList<Character> characters = loc.getCharacters();
            System.out.printf("%d Characters:\n",characters.size());
            for (Character ch: characters) {
                String characteristics = ch.toString();
                String[] sp = characteristics.split(" ");
                characteristics = sp[1];
                for(int j = 2; j < sp.length; j++) {
                    characteristics += " " + sp[j]; 
                }
                System.out.println("- " + characteristics);
            }
        }

        // prompt user to choose a location
        boolean done = false;
        int userOption = 0;
        System.out.println("To which location should RescueBot be deployed?");
        while(!done) {
            System.out.print("> ");
            try {
                userOption = Integer.parseInt(kb.next());
                if((userOption < 1) || (userOption > locations.size())) {
                    throw new Exception();
                } else {
                    done = true;
                }
            } catch (Exception e) {
                System.out.println("Invalid response! To which location should RescueBot be deployed?");
            }
        }
        int decision = userOption-1; 
        return decision;
    }

    /*
    // simulation algorithm decides on which location to save
    public int decide(Scenario sc) {
        // a very simple decision engine
        // TODO: take into account at least 5 characteristics
        
        // 50/50
        if(Math.random() > 0.5) {
            return scenario.getLocation(1);
        } else {
            return scenario.getLocation(2);
        }
    }
    */

    private void storeAllCharacteristic() {

        this.allCharactersCharacteristics = new ArrayList<String>(0);

         // iterate over every scenario and location, collect characteristic from all characters
        for(int i = 0; i < this.scenarios.size(); i++){
           
            Scenario sc = this.scenarios.get(i);
            
            // show each location
            ArrayList<Location> locations = sc.getLocations();
            //System.out.println("Number of locations: " +locations.size());
            for (int j = 1; j <= locations.size(); j++) {
                Location loc = locations.get(j-1);
                //System.out.printf("[%d] Location: %s, %s\n",j, loc.getLatitude(), loc.getLongitude());
                //System.out.println("Trespassing: " + loc.getTrespassing());
                // show each character
                ArrayList<Character> characters = loc.getCharacters();
                //System.out.printf("%d Characters:\n",characters.size());
                for (Character ch: characters) {
                    //System.out.println("- " + ch.toString());
                    String[] characteristics = ch.toString().split(" ");
                    for(String characteristic: characteristics) {
                        // exclude default values (i.e none, unknown, unspecified)
                        if((!characteristic.equals("none")) && (!characteristic.equals("unknown")) &&
                           (!characteristic.equals("none"))){
                            this.allCharactersCharacteristics.add(characteristic);
                        }
                    } 
                    // also add trespassing characteristic
                    if(loc.getTrespassing().equals("yes")) {
                        this.allCharactersCharacteristics.add("trespassing");
                    } else {
                        this.allCharactersCharacteristics.add("legal");
                    }
                }
            }
        }

        /* 

            // get all distinct characteristics
            Set<String> distinctCharacteristics = new HashSet<String>(this.allCharactersCharacteristics);

            // show frequency of occurance of each distinct characteristic 
            System.out.println("------------------------");
            System.out.println("Characteristic Counts: ");
            for(String c: distinctCharacteristics) {
                System.out.printf("%s : %d \n", c, Collections.frequency(this.allCharactersCharacteristics, c));
            }

        */ 

    }

     
    public void generateStatistics() {
       
        ArrayList<String> sortedCharacteristics = new ArrayList<String>(0);
        ArrayList<String> sortedStatistics = new ArrayList<String>(0);

        // compute the statistics
        this.computeStats(sortedCharacteristics, sortedStatistics);

        // sort the statistics in descending order
        this.sortStats(sortedCharacteristics, sortedStatistics);

        // display to screen
        this.displayStatistics(sortedCharacteristics, sortedStatistics);

    }

    // computes the statistics for caracteristics of saved characters from all scenarios judged so far
    private void computeStats(ArrayList<String> sortedCharacteristics, ArrayList<String> sortedStatistics) {
        
        ArrayList<String> savedCharactersCharacteristics = new ArrayList<String>(0);
        Double averageAge = 0.0, totalAge = 0.0;
        int numHumans = 0;

        // iterate over judged scenario and locations, collect characteristics from all characters who have been saved
        for(int i = 0; i < this.lastScenarioJudged; i++){
           
            Scenario sc = this.scenarios.get(i);
            Location loc = sc.getLocation(this.userDecisions[i]);


            //System.out.printf("Location: %s, %s\n", loc.getLatitude(), loc.getLongitude());
            //System.out.println("Trespassing: " + loc.getTrespassing());
            // show each character
            ArrayList<Character> characters = loc.getCharacters();
            //System.out.printf("%d Characters:\n",characters.size());
            for (Character ch: characters) {
                //System.out.println("- " + ch);
                String[] characteristics = ch.toString().split(" ");
                for(String characteristic: characteristics) {
                    // exclude default values (i.e none, unknown, unspecified)
                    if((!characteristic.equals("none")) && (!characteristic.equals("unknown")) &&
                        (!characteristic.equals("none"))){
                        savedCharactersCharacteristics.add(characteristic);
                    }
                } 
                // also add trespassing characteristic
                if(loc.getTrespassing().equals("yes")) {
                    savedCharactersCharacteristics.add("trespassing");
                } else {
                    savedCharactersCharacteristics.add("legal");
                }
                // keep track of human age total and count
                if(characteristics[0].equals("human")) {
                    numHumans++;
                    totalAge += ch.getAge();
                }
            }
        }

        // now compute the compute the statistic value for each distinct characteristic across all saved characters
        // (the statistic value for a given characteristic is just the ratio of frequency counts for saved characters to all available characters)

        // get set of all distinct characteristics from all scenarios
        Set<String> distinctCharacteristics = new HashSet<String>(this.allCharactersCharacteristics);

        // compute frequency counts and statistic of each distinct characteristic across the saved characters and all characters 
        //System.out.println("------------------------");
        //System.out.println("Characteristic Counts: ");
        for(String c: distinctCharacteristics) {
            int countSavedCharacters = Collections.frequency(savedCharactersCharacteristics, c);
            int countAllCharacters = Collections.frequency(this.allCharactersCharacteristics, c);
            double stat = (double) countSavedCharacters / (double) countAllCharacters; 
            //System.out.printf("Charcteristic: %s, Saved count: %d, All count: %d, Statistic: %.2f \n", c, countSavedCharacters, countAllCharacters, stat);
            sortedCharacteristics.add(c);
            sortedStatistics.add(String.format("%.2f",stat));
        }

        // compute average age and add it to statistics arraylist
        averageAge = totalAge/numHumans;
        sortedStatistics.add(String.format("%.2f",averageAge));

    }

    // performs selection sort on a collection of statistic values
    private void sortStats(ArrayList<String> sortedCharacteristics, ArrayList<String> sortedStatistics) {

        // display unsorted statistics
        //System.out.println("\nUnsorted statistics:\n");
        //for(int i = 0; i < sortedCharacteristics.size(); i++){
        //    System.out.printf("%s  : %s \n", sortedCharacteristics.get(i), sortedStatistics.get(i));
        //}

        // perform selection sort of statistic values in decreasing order
        for(int i = 0; i < sortedCharacteristics.size(); i++){
            int largestSoFar = i;
            for(int j = i+1; j < sortedCharacteristics.size(); j++) {
                Double n1 = Double.parseDouble(sortedStatistics.get(largestSoFar));
                Double n2 = Double.parseDouble(sortedStatistics.get(j));
                if(n1.compareTo(n2) < 0) {
                    largestSoFar = j;
                } else if (n1.compareTo(n2) == 0) {
                    // resolve ties by sorting in alphabetical order
                    if(sortedCharacteristics.get(largestSoFar).compareTo(sortedCharacteristics.get(j)) > 0) {
                        largestSoFar = j;
                    }
                }
            }
            if(largestSoFar != i) {
                String tempStat = sortedStatistics.get(i);
                String tempChar = sortedCharacteristics.get(i);
                sortedStatistics.set(i, sortedStatistics.get(largestSoFar));
                sortedCharacteristics.set(i, sortedCharacteristics.get(largestSoFar));
                sortedStatistics.set(largestSoFar, tempStat);
                sortedCharacteristics.set(largestSoFar, tempChar);
            }
        }

        // display sorted statistics
        //System.out.println("\nSorted statistics:\n");
        //for(int i = 0; i < sortedCharacteristics.size(); i++){
        //    System.out.printf("%s  : %s \n", sortedCharacteristics.get(i), sortedStatistics.get(i));
        //}

    }
    
    private void displayStatistics(ArrayList<String> sortedCharacteristics, ArrayList<String> sortedStatistics) {

        System.out.println("======================================");
        System.out.println("# Statistic");
        System.out.println("======================================");     
        System.out.printf("- %% SAVED AFTER %d RUNS\n",this.lastScenarioJudged);
        int i = 0;
        for(i = 0; i < sortedCharacteristics.size(); i++){
            System.out.printf("%s: %s\n", sortedCharacteristics.get(i), sortedStatistics.get(i));
        }
        System.out.printf("--\naverage age: %s\n",sortedStatistics.get(i));
    }

}