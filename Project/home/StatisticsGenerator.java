import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * A class for computing the statistics of relevant charactreistics for the saved characters
 * from judged scenarios.
 *
 * @author Tanzid Sultan
 */
public class StatisticsGenerator {


    /**
    *  instance variables 
    */
    private ArrayList<String> allScenariosCharacteristics;
    private ArrayList<Scenario> scenarios;
    private int lastScenarioJudged;
    private ArrayList<Integer> decisions;

     /**
	 *  Class Constructor
	 */
    public StatisticsGenerator() {
        this.allScenariosCharacteristics = null;
        this.scenarios = null;
        this.lastScenarioJudged = 0;
        this.decisions = null;
    }

    /**
    * Helper method for cleanup/resetting of the data used to compute the statistics
    */ 
    private void cleanUp() {
        this.allScenariosCharacteristics = null;
        this.scenarios = null;
        this.lastScenarioJudged = 0;
        this.decisions = null;
    }

    /**
    * Helper method for generating statistics for judged scenarios
    *
    * @param scenarios  array list of all available scenarios
    * @param lastScenarioJudged  the number of scenarios that have been judged, starting from the first
    * @param decisions  array list containing decision values for the judged scenarios
    */ 
    public void generateStatistics(ArrayList<Scenario> scenarios, int lastScenarioJudged, ArrayList<Integer> decisions) {
       
        this.scenarios = scenarios;
        this.lastScenarioJudged = lastScenarioJudged;
        this.decisions = decisions;

        ArrayList<String> sortedCharacteristics = new ArrayList<String>(0);
        ArrayList<String> sortedStatistics = new ArrayList<String>(0);

        // collect characteristics from all characters from every location in judged scenarios
        this.collectAllCharacteristic();

        // compute the statistics
        this.computeStats(sortedCharacteristics, sortedStatistics);

        // sort the statistics in descending order
        this.sortStats(sortedCharacteristics, sortedStatistics);

        // display to screen
        this.displayStatistics(sortedCharacteristics, sortedStatistics);

        // cleanup/reset the data
        this.cleanUp();
    }
    
    /**
    * Helper method for collecting all relevant characteristics across characters from all judged scenarios
    */ 
    private void collectAllCharacteristic() {

        this.allScenariosCharacteristics = new ArrayList<String>(0);

         // iterate over every scenario, collect characteristics from all characters
        for (int i = 0; i < this.lastScenarioJudged; i++) {
            Scenario sc = this.scenarios.get(i);
            // iterate over locations
            ArrayList<Location> locations = sc.getLocations();
            for (int j = 1; j <= locations.size(); j++) {
                Location loc = locations.get(j-1);
                // iterate over characters
                ArrayList<Character> characters = loc.getCharacters();
                for (Character ch: characters) {
                    String[] characteristics = ch.toString().split(" ");
                    for (String characteristic: characteristics) {
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
        for (int i = 0; i < this.lastScenarioJudged; i++){  
            Scenario sc = this.scenarios.get(i);
            Location loc = sc.getLocation(this.decisions.get(i));
            // show each character
            ArrayList<Character> characters = loc.getCharacters();
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
                    if (sortedCharacteristics.get(largestSoFar).compareTo(sortedCharacteristics.get(j)) > 0) {
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
        System.out.printf("- %% SAVED AFTER %d RUNS\n",this.lastScenarioJudged);
        int i = 0;
        for (i = 0; i < sortedCharacteristics.size(); i++) {
            System.out.printf("%s: %s\n", sortedCharacteristics.get(i), sortedStatistics.get(i));
        }
        System.out.printf("--\naverage age: %s\n",sortedStatistics.get(i));
    }






    

}