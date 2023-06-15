import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;
import java.util.Set;
import java.util.HashSet;

/**
 * A class implementating an algorithm-based judging engine.
 *
 * @author: Tanzid Sultan
 * ID# 1430660, Email: tanzids@student.unimelb.edu.au
 */
public class SimulationJudgingEngine implements JudgingEngine {

    /**
     * constants
     */
    public static final String DECISION_MAKER = "ALGORITHM";

    /**
    *  instance variables 
    */
    private ArrayList <Scenario> scenarios;
    private int[] simulationDecisions;
    private Scanner kb;
    private ArrayList<String> allScenariosCharacteristics;
    private FileManager fManager;
    private RandomScenarioGenerator rsGenerator;

     /**
	 *  Class Constructor
     *
	 * @param kb  Scanner object for reading keyboard input stream
	 * @param fManager  File manager object
	 */
    public SimulationJudgingEngine(Scanner kb, FileManager fManager) {
        this.scenarios = null;
        this.kb = kb;
        this.fManager = fManager;
        this.rsGenerator = new RandomScenarioGenerator();

        // load scenarios
        this.loadScenarios();

    }

    /**
    * Helper method for acquiring scenarios (either from file or randomly generated)
    */
    public void loadScenarios() {

        // import scenarios from file (if option provided), otrherwise generate randomly
        if (this.fManager.getScenarioProvided()) {
            scenarios = this.fManager.getFileScenarios();
        } 
            
    }

    /**
	 * Accessor method for retrieving scenarios data.
     * 
     * @return a copy of the array list containing scenarios
	 */
    public ArrayList<Scenario> getScenarios() {
        // return a deep copy of the scenarios array list
        ArrayList<Scenario> scenariosCopy = new ArrayList<Scenario>(0);
        for (Scenario sc: this.scenarios) {
            scenariosCopy.add(new Scenario(sc));
        }
        return scenariosCopy;
    }

    /**
	 * Accessor method for retrieving user decisions.
     * 
     * @return a copy of the array containing user decisions for judged scenarios
	 */    
    public int[] getSimulationDecisions() {
        // return a deep copy of the array containing user decisions  
        int[] simulationDecisionsCopy = new int[scenarios.size()];
        for (int i = 0; i < scenarios.size(); i++) {
            simulationDecisionsCopy[i] = this.simulationDecisions[i];
        }
        return simulationDecisionsCopy;
    }

    /**
    * Helper method for judging given scenarios
    */    
    public void judgeScenarios() {

        if (!this.fManager.getScenarioProvided()) {
            // prompt user for number of scenarios
            boolean done = false;
            int N = 0;
            System.out.println("How many scenarios should be run?");
            while (!done) {
                System.out.print("> ");
                try {
                    N = Integer.parseInt(kb.next());
                    // check for valid input (integer >= 1)
                    if(N < 1) {
                        throw new Exception();
                    } else {
                        done = true;
                    }
                } catch (Exception e) {
                    System.out.println("Invalid input! How many scenarios should be run?");
                }
            }
            //generate the specified number of random scenarios
            scenarios = this.rsGenerator.generateScenarios(N);          
        }

        this.simulationDecisions = new int[scenarios.size()];
        // store characteristics from all characters across all available scenarios and locations
        this.storeAllCharacteristic();

        // judge all scenarios (get algorithm decisions)
        for (int i = 0; i < this.scenarios.size(); i++) {
            this.simulationDecisions[i] = this.decide(scenarios.get(i));
        } 
        
        // compute and display statistics
        this.generateStatistics();   

        // save judged scenarios to log file
        this.fManager.saveToLogFile(this.getScenarios(), 0, this.scenarios.size(), this.getSimulationDecisions(), DECISION_MAKER);

        // reset judged scenarios
        this.simulationDecisions = new int[scenarios.size()];

        // return to main menu        
        System.out.println("That's all. Press Enter to return to main menu.");
        System.out.print("> ");
        this.kb.nextLine();
        this.kb.nextLine();
    }


    /**
    * Decision algorithm for choosing which group to save within the given scenario.
    * This algorithm first computes a "decision score" for each location using the characteristics of 
    * its inhabitants. Different characteristics are assigned different weights which contribute
    * to the overall score. Then the algorithm makes a decision by choosing the location which
    * has the highest decision score.
    *
    * @param sc  the scenario
    * @return  which group location to save
    */    
    public int decide(Scenario sc) {

        // show each location
        ArrayList<Location> locations = sc.getLocations();

        // a "decision score" will be computed for each location
        // the location with the highest score will get chosen to be saved
        double[] decisionScore = new double[locations.size()];

        // compute decision scores for each location 
        for (int i = 0; i < locations.size(); i++) {
            Location loc = locations.get(i);
            decisionScore[i] = this.computeDecisionScore(loc);
        }

        // find location with largest score        
        int decision = findMax(decisionScore);
        return decision;
    }

    /**
    * Helper method for computing the decision score for a given location
    * @return  decision score
    */ 
    private double computeDecisionScore(Location loc) {

        // the following 6 characteristics are assigned weights which will contribute to the decision scores
        
        // decision characteristic 1: human or animal
        // (higher weight for humans)
        double isHumanWeight = 10.0;
        double isAnimalWeight = 2.0;

        // decision characteristic 2: age category 
        // (higher weight for younger)
        double isBabyWeight = 20.0;
        double isChildWeight = 15.0;
        double isAdultWeight = 10.0;

        // decision characteristic 3: pregnant
        // (higher weight for pregnant)
        double isPregnantWeight = 20.0;
        
        // decision characteristic 4: pet
        // (higher weights for pets)
        double isPetWeight = 5.0;

        // decision characteristic 5: profession
        // (only professions scientists, doctors and students receive weight, all other professions receive zero weight)
        double isScientistWeight = 10.0;
        double isDoctorWeight = 8.0;
        double isStudent = 5.0;

        // decision characteristic 6: endangered species
        // (endangered species receive higher weight)
        double endangeredSpeciesWeight = 5.0;

        double score = 0.0;
        ArrayList<Character> characters = loc.getCharacters();
        
        // accumulate decision score weights for each characters
        for (Character ch: characters) {

            String characteristics = ch.toString();
            String[] sp = characteristics.split(" ");
            characteristics = sp[1];
            for (int j = 2; j < sp.length; j++) {
                characteristics += " " + sp[j]; 
            }

            // check for decision characteristic 1: human or animal
            if (sp[0].equals("human")) {
                score += isHumanWeight;
                // check for decision characteristic 2: age category
                if (sp[2].equals("baby")) {
                    score += isBabyWeight;
                } else if (sp[2].equals("child")) {       
                    score += isChildWeight;
                } else if (sp[2].equals("adult")) {        
                    score += isAdultWeight;
                }
                // check for decision characteristic 3: pregnant
                if (sp.length == 6) {
                    score += isPregnantWeight;
                }
                // check for decision characteristic 5: profession
                if (sp[3].equals("scientist")) {
                    score += isScientistWeight;
                } else if (sp[3].equals("doctor")) {       
                    score += isDoctorWeight;
                } else if (sp[3].equals("student")) {        
                    score += isStudent;
                }

            } else {
                score += isAnimalWeight;
                // check for decision characteristic 4: pet
                if (sp.length == 3) {
                    score += isPetWeight;
                }
                // check for decision characteristic 6: endangered species
                if((sp[1].equals("wolf")) || (sp[1].equals("panda")) ||(sp[1].equals("orangutan"))){
                    score += endangeredSpeciesWeight;
                }
            }
        }
        return score;
    }

    /**
    * Helper method for finding the array index corresponding to the largest array element
    * @return  index of the largest element
    */ 
    private int findMax(double[] values) {
        double max = 0.0;
        int ix = 0;
        for (int i = 0; i < values.length; i++) {
            if(values[i] > max) {
                max = values[i]; 
                ix = i;         
            }
        }
        return ix;
    }

    /**
    * Helper method for storing all relevant characteristics across characters from all judged scenarios
    */ 
    private void storeAllCharacteristic() {

        this.allScenariosCharacteristics = new ArrayList<String>(0);

         // iterate over every scenario and location, collect characteristic from all characters
        for (int i = 0; i < this.scenarios.size(); i++){
           
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
                    for (String characteristic: characteristics) {
                        // exclude default values (i.e none, unknown, unspecified)
                        if ((!characteristic.equals("none")) && (!characteristic.equals("unknown")) &&
                           (!characteristic.equals("unspecified"))){
                            this.allScenariosCharacteristics.add(characteristic);
                        }
                    } 
                    // also add trespassing characteristic
                    if (loc.getTrespassing().equals("yes")) {
                        this.allScenariosCharacteristics.add("trespassing");
                    } else {
                        this.allScenariosCharacteristics.add("legal");
                    }
                }
            }
        }
    }

    /**
    * Helper method for generating statistics for judged scenarios
    */ 
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


    /**
    * Helper method for computing the statistics for caracteristics of saved characters from all scenarios judged so far
    *
    * @param sortedCharacteristics  array list containing relevant characteristics for which statistics will be computed
    * @param sortedStatistics  array list containing statistic values for the relevant characteristics
    */     
    private void computeStats(ArrayList<String> sortedCharacteristics, ArrayList<String> sortedStatistics) {
        ArrayList<String> savedCharactersCharacteristics = new ArrayList<String>(0);
        Double averageAge = 0.0, totalAge = 0.0;
        int numHumans = 0;

        // iterate over judged scenario and locations, collect characteristics from all characters who have been saved
        for (int i = 0; i < this.scenarios.size(); i++){   
            Scenario sc = this.scenarios.get(i);
            Location loc = sc.getLocation(this.simulationDecisions[i]);

            // show each character
            ArrayList<Character> characters = loc.getCharacters();
            //System.out.printf("%d Characters:\n",characters.size());
            for (Character ch: characters) {
                String[] characteristics = ch.toString().split(" ");
                for (String characteristic: characteristics) {
                    // exclude default values (i.e none, unknown, unspecified)
                    if ((!characteristic.equals("none")) && (!characteristic.equals("unknown")) &&
                        (!characteristic.equals("none"))){
                        savedCharactersCharacteristics.add(characteristic);
                    }
                } 
                // also add trespassing characteristic
                if (loc.getTrespassing().equals("yes")) {
                    savedCharactersCharacteristics.add("trespassing");
                } else {
                    savedCharactersCharacteristics.add("legal");
                }
                // keep track of human age total and count
                if (characteristics[0].equals("human")) {
                    numHumans++;
                    totalAge += ch.getAge();
                }
            }
        }

        // now compute the compute the statistic value for each distinct characteristic across all saved characters
        // (the statistic value for a given characteristic is just the ratio of frequency counts for saved characters to all available characters)

        // get set of all distinct characteristics from all scenarios
        Set<String> distinctCharacteristics = new HashSet<String>(this.allScenariosCharacteristics);

        // compute frequency counts and statistic of each distinct characteristic across the saved characters and all characters 
        for (String c: distinctCharacteristics) {
            int countSavedCharacters = Collections.frequency(savedCharactersCharacteristics, c);
            int countAllCharacters = Collections.frequency(this.allScenariosCharacteristics, c);
            // compute the statistic value (round up the last digit)
            double stat = Math.ceil(100 * (double) countSavedCharacters / (double) countAllCharacters)/100; 
            sortedCharacteristics.add(c);
            sortedStatistics.add(String.format("%.2f",stat));
        }
        // compute average age and add it to statistics arraylist
        averageAge = totalAge/numHumans;
        sortedStatistics.add(String.format("%.2f",averageAge));
    }

    /**
    * Helper method for performing selection sort on a given collection of statistic values
    *
    * @param sortedCharacteristics  array list containing relevant characteristics for which statistics have been computed
    * @param sortedStatistics  array list containing statistic values for the relevant characteristics
    */     
    private void sortStats(ArrayList<String> sortedCharacteristics, ArrayList<String> sortedStatistics) {

        // perform selection sort of statistic values in decreasing order
        for (int i = 0; i < sortedCharacteristics.size(); i++) {
            int largestSoFar = i;
            for (int j = i+1; j < sortedCharacteristics.size(); j++) {
                Double n1 = Double.parseDouble(sortedStatistics.get(largestSoFar));
                Double n2 = Double.parseDouble(sortedStatistics.get(j));
                if (n1.compareTo(n2) < 0) {
                    largestSoFar = j;
                } else if (n1.compareTo(n2) == 0) {
                    // resolve ties by sorting in alphabetical order
                    if(sortedCharacteristics.get(largestSoFar).compareTo(sortedCharacteristics.get(j)) > 0) {
                        largestSoFar = j;
                    }
                }
            }
            if (largestSoFar != i) {
                String tempStat = sortedStatistics.get(i);
                String tempChar = sortedCharacteristics.get(i);
                sortedStatistics.set(i, sortedStatistics.get(largestSoFar));
                sortedCharacteristics.set(i, sortedCharacteristics.get(largestSoFar));
                sortedStatistics.set(largestSoFar, tempStat);
                sortedCharacteristics.set(largestSoFar, tempChar);
            }
        }
    }
    
    /**
    * Helper method for displaying statistics for the judged scenarios to the screen
    *
    * @param sortedCharacteristics  array list containing relevant characteristics for which statistics have been computed
    * @param sortedStatistics  array list containing statistic values for the relevant characteristics
    */ 
    private void displayStatistics(ArrayList<String> sortedCharacteristics, ArrayList<String> sortedStatistics) {
        System.out.println("======================================");
        System.out.println("# Statistic");
        System.out.println("======================================");     
        System.out.printf("- %% SAVED AFTER %d RUNS\n",this.scenarios.size());
        int i = 0;
        for (i = 0; i < sortedCharacteristics.size(); i++) {
            System.out.printf("%s: %s\n", sortedCharacteristics.get(i), sortedStatistics.get(i));
        }
        System.out.printf("--\naverage age: %s\n",sortedStatistics.get(i));
    }

}