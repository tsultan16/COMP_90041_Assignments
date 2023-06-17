import java.util.Scanner;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

/**
 * A class for auditing decision history obtained from log file.
 *
 * @author Tanzid Sultan
 * ID# 1430660, Email: tanzids@student.unimelb.edu.au
 */
public class Auditor {

    /**
    *  instance variables 
    */
    private Scanner kb;
    private FileManager fManager;   
    private int humansSavedTotalUser;
    private int humansSavedTotalAlgorithm;
    private int humansSavedTotalAgeUser;
    private int humansSavedTotalAgeAlgorithm;
    private double savedAverageAgeUser;
    private double savedAverageAgeAlgorithm;
    private int userRuns;
    private int algorithmRuns;
    private ArrayList<String> allUserCharacteristics;
    private ArrayList<String> allAlgorithmCharacteristics;
    private ArrayList<String> savedUserCharacteristics;
    private ArrayList<String> savedAlgorithmCharacteristics;
    private ArrayList<String> sortedCharacteristicsUser;
    private ArrayList<String> sortedStatisticsUser;
    private ArrayList<String> sortedCharacteristicsAlgorithm;
    private ArrayList<String> sortedStatisticsAlgorithm;

    /**
	*  Class Constructor
    *
	* @param kb  Scanner object for reading keyboard input stream
	* @param fManager  File manager object
	*/
    public Auditor(Scanner kb, FileManager fManager) {
        this.kb = kb;
        this.fManager = fManager; 

        this.humansSavedTotalUser = 0;
        this.humansSavedTotalAlgorithm = 0;
        this.humansSavedTotalAgeUser = 0;
        this.humansSavedTotalAgeAlgorithm = 0;
        this.savedAverageAgeUser = 0.0;
        this.savedAverageAgeAlgorithm = 0.0;
        this.userRuns = 0;
        this.algorithmRuns = 0;

        this.allUserCharacteristics = null;
        this.allAlgorithmCharacteristics = null;
        this.savedUserCharacteristics = null;
        this.savedAlgorithmCharacteristics = null;

        this.sortedCharacteristicsUser = null;
        this.sortedStatisticsUser = null;
        this.sortedCharacteristicsAlgorithm = null;
        this.sortedStatisticsAlgorithm = null;

    }

    /**
    * Helper method for auditing judged scenarios from log file history
    */
    public void auditLogHistory() {
    
        ArrayList<String> logFileData = null;

        // import log file data
        try {
            logFileData = this.fManager.importLogFile();
            
            // parse the log file and extract auditing data
            this.parseData(logFileData);

            // compute and display audit statistics
            this.computeStats();
            
            System.out.println("That's all. Press Enter to return to main menu.");

        } catch (Exception e) {
            System.out.println("No history found. Press enter to return to main menu.");
        }

         // return to main menu        
        System.out.print("> ");
        this.kb.nextLine();
        this.kb.nextLine();    

    }

    /**
    * Helper method for parsing the log file data and extracting all relevant characteristics needed for the audit.
    * Note: Each scenario read from the log file is also accompanied by information about who made the judging 
    * decision, which can be either user or algorithm. Scenarios judged by user will further indicate whether
    * user provided consent for decision to be logged. 
    * @param data  log file data array
    */
    private void parseData(ArrayList<String> data) {

        this.allUserCharacteristics = new ArrayList<String>(0);
        this.allAlgorithmCharacteristics = new ArrayList<String>(0);
        this.savedUserCharacteristics = new ArrayList<String>(0);
        this.savedAlgorithmCharacteristics  = new ArrayList<String>(0);

        this.humansSavedTotalUser = 0;
        this.humansSavedTotalAlgorithm = 0;
        this.humansSavedTotalAgeUser = 0;
        this.humansSavedTotalAgeAlgorithm = 0;

        int i = 0;
        while (i < data.size()) {
            // read each scenario
            String[] sp = data.get(i).split(" ");
            // get number of locations
            i++; 
            sp = data.get(i).split(" ");
            int numLocs = Integer.parseInt(sp[0]);
            i++;
            ArrayList<ArrayList<String>> scenarioCharacteristics = new ArrayList<ArrayList<String>>(numLocs);
            int[] humansSaved = new int[numLocs];
            int[] humansSavedTotalAge = new int[numLocs];

            // read characters from each location
            for (int j = 0; j < numLocs; j++) {
                ArrayList<String> locationCharacteristics = new ArrayList<String>(0); 
                // read trespassing status
                i++;
                String trespassing = "";
                sp = data.get(i).split(" ");
                if (sp[1].equals("yes")) {
                    trespassing = "trespassing";
                } else {
                    trespassing = "legal";
                }

                // get number of characters
                i++;
                sp = data.get(i).split(" ");
                int numChars = Integer.parseInt(sp[0]);
                i++;

                // read each character
                for (int k = 0; k < numChars; k++) {
                    sp = data.get(i).split(" ");
                    // accumulate all characteristics from this character
                    locationCharacteristics.add(trespassing);
                    for(int c = 2; c < sp.length; c++) {
                        locationCharacteristics.add(sp[c]);
                    }
                    i++;
                }
                scenarioCharacteristics.add(locationCharacteristics);
                sp = data.get(i).split(" ");
                humansSaved[j] = Integer.parseInt(sp[1]);
                humansSavedTotalAge[j] = Integer.parseInt(sp[3]);
                i++;

            }

            // find out the type of decision maker (user or algorithm)
            sp = data.get(i).split(" ");
            if (!sp[0].equals("**")) {
                System.out.println("ERROR!! BAD LINE.");
                System.exit(1);
            }

            // collect all the relevant characteristics 
            String decisionMaker = sp[1];
            // user made the decision
            if (decisionMaker.equals("USER")) {
                // get user decision
                int decision = Integer.parseInt(sp[5])-1;
                // collect characteristics
                for (int j = 0; j < scenarioCharacteristics.size(); j++) {
                    ArrayList<String> sc = scenarioCharacteristics.get(j);
                    for (String characteristic: sc) {
                        this.allUserCharacteristics.add(characteristic);
                        if (decision == j) {
                            this.savedUserCharacteristics.add(characteristic);
                        }
                    }
                }
                this.humansSavedTotalUser += humansSaved[decision];
                this.humansSavedTotalAgeUser += humansSavedTotalAge[decision];
                this.userRuns++; 

            // algorithm made the decision    
            } else {
                // get simulation decision
                int decision = Integer.parseInt(sp[5])-1;
                // collect characteristics
                for (int j = 0; j < scenarioCharacteristics.size(); j++) {
                    ArrayList<String> sc = scenarioCharacteristics.get(j);
                    for (String characteristic: sc) {
                        this.allAlgorithmCharacteristics.add(characteristic);
                        if (decision == j) {
                            this.savedAlgorithmCharacteristics.add(characteristic);
                        }
                    }
                } 
                this.humansSavedTotalAlgorithm += humansSaved[decision];
                this.humansSavedTotalAgeAlgorithm += humansSavedTotalAge[decision];
                this.algorithmRuns++;
            }
            i++;
        }

        if (this.humansSavedTotalUser > 0) {
            this.savedAverageAgeUser = (double) this.humansSavedTotalAgeUser / (double) this.humansSavedTotalUser;
        }
    
        if (this.humansSavedTotalAlgorithm > 0) {
            this.savedAverageAgeAlgorithm = (double) this.humansSavedTotalAgeAlgorithm / (double) this.humansSavedTotalAlgorithm;
        }

    }

    /**
    * Helper method for computing the statistics for caracteristics of saved characters from all scenarios judged so far
    */
    private void computeStats() {
        
        this.sortedCharacteristicsUser = new ArrayList<String>(0);
        this.sortedStatisticsUser = new ArrayList<String>(0);
        this.sortedCharacteristicsAlgorithm = new ArrayList<String>(0);
        this.sortedStatisticsAlgorithm = new ArrayList<String>(0);

        boolean algorithmStatsPrinted = false;

         // get set of all distinct characteristics from all scenarios (for both user and algorithm runs)
        Set<String> distinctCharacteristicsUser = new HashSet<String>(this.allUserCharacteristics);
        Set<String> distinctCharacteristicsAlgorithm = new HashSet<String>(this.allAlgorithmCharacteristics);

        // compute frequency counts and statistic of each distinct characteristic for ALGORITHM runs
        if (this.savedAlgorithmCharacteristics.size() > 0) {
            this.statsFromCounts(distinctCharacteristicsAlgorithm, this.allAlgorithmCharacteristics, this.savedAlgorithmCharacteristics, this.sortedCharacteristicsAlgorithm, this.sortedStatisticsAlgorithm);

            // display the statistics
            this.displayStats( this.sortedCharacteristicsAlgorithm, this.sortedStatisticsAlgorithm, this.savedAverageAgeAlgorithm, "Algorithm",this.algorithmRuns);
            algorithmStatsPrinted = true;
        } 

        // compute frequency counts and statistic of each distinct characteristic for USER runs 
        if (this.savedUserCharacteristics.size() > 0) {
            this.statsFromCounts(distinctCharacteristicsUser, this.allUserCharacteristics, this.savedUserCharacteristics, this.sortedCharacteristicsUser, this.sortedStatisticsUser);

            // display the statistics
            if (algorithmStatsPrinted) {
                // print a blank line between Algorithm audit stats and User audit stats
                System.out.println();
            }
            this.displayStats( this.sortedCharacteristicsUser, this.sortedStatisticsUser, this.savedAverageAgeUser, "User", this.userRuns);
        }
   
    }

    /**
    * Helper method for counting the relevant characteristics frequencies and computing the statistic values
    *
    * @param distinctChars  array list containing all the distinct characteristics
    * @param allChars  array list containing statistic values for the relevant characteristics
    * @param allChars  array list containing all relevant characteristics for all locations
    * @param savedChars  array list containing all relevant characteristics for only saved location
    * @param sortedChars  array list containing all relevant characteristics for all locations sorted according to statistic value
    * @param sortedStats  array list containing sorted statistic values
    */  
    private void statsFromCounts(Set<String> distinctChars, ArrayList<String> allChars, ArrayList<String> savedChars, ArrayList<String> sortedChars, ArrayList<String> sortedStats) {

        // compute frequency counts and statistic of each distinct characteristic for USER runs 
        for (String c: distinctChars) {
            int countSavedCharacters = Collections.frequency(savedChars, c);
            int countAllCharacters = Collections.frequency(allChars, c);
            // compute the statistic value (round up the last digit)
            double stat = Math.ceil(100 * (double) countSavedCharacters / (double) countAllCharacters)/100; 
            sortedChars.add(c);
            sortedStats.add(String.format("%.2f",stat));
        }
        // sort the statistics
        this.sortStats(sortedChars, sortedStats);
    }


    /**
    * Helper method for performing selection sort on a given collection of statistic values
    *
    * @param sortedCharacteristics  array list containing relevant characteristics for which statistics have been computed
    * @param sortedStatistics  array list containing statistic values for the relevant characteristics
    */     
    private void sortStats(ArrayList<String> sortedCharacteristics, ArrayList<String> sortedStatistics) {

        // perform selection sort of statistic values in decreasing order
        for (int i = 0; i < sortedCharacteristics.size(); i++){
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
    * @param averageAge  average age across all saved humans
    * @param decisionMaker  who made the decisions, i.e. human or algorithm
    * @param numRuns total number of scenarios judged
    */ 
    private void displayStats(ArrayList<String> sortedCharacteristics, ArrayList<String> sortedStatistics, double averageAge, String decisionMaker, int numRuns) {

        System.out.println("======================================");
        System.out.printf("# %s Audit\n", decisionMaker);
        System.out.println("======================================");     
        System.out.printf("- %% SAVED AFTER %d RUNS\n", numRuns);
        int i = 0;
        for (i = 0; i < sortedCharacteristics.size(); i++) {
            System.out.printf("%s: %s\n", sortedCharacteristics.get(i), sortedStatistics.get(i));
        }
        System.out.printf("--\naverage age: %.2f\n",averageAge);
    }

}
