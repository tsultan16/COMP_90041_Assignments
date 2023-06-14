import java.util.ArrayList;
import java.util.Scanner;
import java.util.Collections;
import java.util.Set;
import java.util.HashSet;

public class SimulationJudgingEngine implements JudgingEngine {


    private ArrayList <Scenario> scenarios;
    private int[] simulationDecisions;
    private Scanner kb;
    private ArrayList<String> allCharactersCharacteristics;
    private FileManager fManager;
    private RandomScenarioGenerator rsGenerator;

    public static final String DECISION_MAKER = "ALGORITHM";


    public SimulationJudgingEngine(Scanner kb, FileManager fManager) {
        this.scenarios = null;
        this.kb = kb;
        this.fManager = fManager;
        this.rsGenerator = new RandomScenarioGenerator();

        // load scenarios
        this.loadScenarios();

    }

    public void loadScenarios() {

        // import scenarios from file (if option provided), otrherwise generate randomly
        if(this.fManager.getScenarioProvided()){
            //System.out.println("Importing scenarios file: " + rb.scenarioFile);
            scenarios = this.fManager.importScenarios();
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

    public int[] getSimulationDecisions() {
        // return a deep copy of the array containing user decisions  
        int[] simulationDecisionsCopy = new int[scenarios.size()];
        for(int i = 0; i < scenarios.size(); i++) {
            simulationDecisionsCopy[i] = this.simulationDecisions[i];
        }
        return simulationDecisionsCopy;
    }

    public void judgeScenarios() {
        System.out.println("Simulation judging in progress...");  

        if (!this.fManager.getScenarioProvided()) {
            // prompt user for number of scenarios
            boolean done = false;
            int N = 0;
            System.out.println("How many scenarios should be run?");
            while(!done) {
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
        for(int i = 0; i < this.scenarios.size(); i++) {
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

    // simulation algorithm decides on which location to save
    public int decide(Scenario sc) {

        //System.out.println("======================================");
        //System.out.println("# Scenario: " + sc.getDescriptor());
        //System.out.println("======================================");

        // show each location
        ArrayList<Location> locations = sc.getLocations();
        //System.out.println("Number of locations: " +locations.size());

        // a "decision score" will be computed for each location
        // the location with the highest score will get chosen to be saved
        double[] decisionScore = new double[locations.size()];

        // compute decision scores for each location 
        for (int i = 0; i < locations.size(); i++) {
            Location loc = locations.get(i);

            //System.out.printf("[%d] Location: %s, %s\n",i, loc.getLatitude(), loc.getLongitude());
            //System.out.println("Trespassing: " + loc.getTrespassing());
            decisionScore[i] = this.computeDecisionScore(loc);

            //System.out.printf("Location: %d, Decision_Score: %.2f \n",i,decisionScore[i]);
        }

        // find location with largest score        
        int decision = findMax(decisionScore);
        //System.out.println("decision: "+decision);

        return decision;
    }

    // method for computing the decision score for a given location
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
        //System.out.printf("%d Characters:\n",characters.size());
        
        // accumulate decision score weights for each characters
        for (Character ch: characters) {

            String characteristics = ch.toString();
            String[] sp = characteristics.split(" ");
            characteristics = sp[1];
            for(int j = 2; j < sp.length; j++) {
                characteristics += " " + sp[j]; 
            }
            //System.out.println("- " + characteristics);

            // check for decision characteristic 1: human or animal
            if(sp[0].equals("human")) {
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

    // helper method for finding the array index corresponding to the largest array element
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
        for(int i = 0; i < this.scenarios.size(); i++){
           
            Scenario sc = this.scenarios.get(i);
            Location loc = sc.getLocation(this.simulationDecisions[i]);


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
    }
    
    private void displayStatistics(ArrayList<String> sortedCharacteristics, ArrayList<String> sortedStatistics) {

        System.out.println("======================================");
        System.out.println("# Statistic");
        System.out.println("======================================");     
        System.out.printf("- %% SAVED AFTER %d RUNS\n",this.scenarios.size());
        int i = 0;
        for(i = 0; i < sortedCharacteristics.size(); i++){
            System.out.printf("%s: %s\n", sortedCharacteristics.get(i), sortedStatistics.get(i));
        }
        System.out.printf("--\naverage age: %s\n",sortedStatistics.get(i));
    }

}