import java.util.Scanner;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class Auditor {

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

    // default constructor
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

    // audit judged scenarios from log file history
    public void auditLogHistory() {
    
        ArrayList<String> logFileData = null;

        // import log file data
        try {
            logFileData = this.fManager.importLogFile();
            
            // parse the log file data
            System.out.println("Parsing log file containing " + logFileData.size() + " lines");
            this.parseData(logFileData);

            // compute and display statistics
            System.out.println("Computing global statistics");
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

            System.out.printf("Reading line# %d: %s \n", (i+1), data.get(i));

            // read each scenario
            String[] sp = data.get(i).split(" ");
            
            // get number of locations
            i++; 
            sp = data.get(i).split(" ");
            int numLocs = Integer.parseInt(sp[0]);
            i++;

            System.out.println("Found scenario with " + numLocs + " locations");

            ArrayList<ArrayList<String>> scenarioCharacteristics = new ArrayList<ArrayList<String>>(numLocs);

            int[] humansSaved = new int[numLocs];
            int[] humansSavedTotalAge = new int[numLocs];

            // read characters from each location
            for(int j = 0; j < numLocs; j++) {
                ArrayList<String> locationCharacteristics = new ArrayList<String>(0); 

                // read trespassing status
                i++;
                String trespassing = "";
                sp = data.get(i).split(" ");
                if(sp[1].equals("yes")) {
                    trespassing = "trespassing";
                } else {
                    trespassing = "legal";
                }

                // get number of characters
                i++;
                sp = data.get(i).split(" ");
                int numChars = Integer.parseInt(sp[0]);
                i++;

                System.out.printf("Location# %d, Num characters: %d \n", j,numChars);

                // read each character
                for(int k = 0; k < numChars; k++) {
                    System.out.println(data.get(i));
                    sp = data.get(i).split(" ");
                    // accumulate all characteristics from this character
                    locationCharacteristics.add(trespassing);
                    for(int c = 1; c < sp.length; c++) {
                        locationCharacteristics.add(sp[c]);
                    }
                    i++;
                }
                scenarioCharacteristics.add(locationCharacteristics);
                System.out.println(data.get(i));
                sp = data.get(i).split(" ");
                humansSaved[j] = Integer.parseInt(sp[1]);
                humansSavedTotalAge[j] = Integer.parseInt(sp[3]);
                i++;

            }

            // collect all the userScenarioCharc

            // find out the type of decision maker (user or algorithm)
            sp = data.get(i).split(" ");
            if(!sp[0].equals("**")) {
                System.out.println("ERROR!! BAD LINE.");
                System.exit(1);
            }

            String decisionMaker = sp[1];

            System.out.println("Desicion maker: "+ decisionMaker + " , decision: " + sp[5]);

            // user made the decision
            if(decisionMaker.equals("USER")) {

                // ckeck if user consented
                if(!sp[5].equals("noConsent")) {
                    // get user decision
                    int decision = Integer.parseInt(sp[5])-1;

                    // collect characteristics
                    for(int j = 0; j < scenarioCharacteristics.size(); j++) {
                        ArrayList<String> sc = scenarioCharacteristics.get(j);
                        for(String characteristic: sc) {
                            this.allUserCharacteristics.add(characteristic);
                            if(decision == j) {
                                this.savedUserCharacteristics.add(characteristic);
                            }
                        }
                    }

                    this.humansSavedTotalUser += humansSaved[decision];
                    this.humansSavedTotalAgeUser += humansSavedTotalAge[decision];
                    this.userRuns++;
                } 

            // algorithm made the decision    
            } else {

                // get simulation decision
                int decision = Integer.parseInt(sp[5])-1;

                // collect characteristics
                for(int j = 0; j < scenarioCharacteristics.size(); j++) {
                    ArrayList<String> sc = scenarioCharacteristics.get(j);
                    for(String characteristic: sc) {
                        this.allAlgorithmCharacteristics.add(characteristic);
                        if(decision == j) {
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
        System.out.println("Done parsing log file data!");
        System.out.printf("USER -> Humans Saved: %d, Total Age: %d \n",this.humansSavedTotalUser, this.humansSavedTotalAgeUser);
        System.out.printf("ALGORITHM -> Humans Saved: %d, Total Age: %d \n",this.humansSavedTotalAlgorithm, this.humansSavedTotalAgeAlgorithm);

        if(this.humansSavedTotalUser > 0) {
            this.savedAverageAgeUser = (double) this.humansSavedTotalAgeUser / (double) this.humansSavedTotalUser;
        }
    
        if(this.humansSavedTotalAlgorithm > 0) {
            this.savedAverageAgeAlgorithm = (double) this.humansSavedTotalAgeAlgorithm / (double) this.humansSavedTotalAlgorithm;
        }


    }

    

    // computes the statistics for caracteristics of saved characters from all scenarios judged so far
    private void computeStats() {
        
        this.sortedCharacteristicsUser = new ArrayList<String>(0);
        this.sortedStatisticsUser = new ArrayList<String>(0);
        this.sortedCharacteristicsAlgorithm = new ArrayList<String>(0);
        this.sortedStatisticsAlgorithm = new ArrayList<String>(0);

         // get set of all distinct characteristics from all scenarios (for both user and algorithm runs)
        Set<String> distinctCharacteristicsUser = new HashSet<String>(this.allUserCharacteristics);
        Set<String> distinctCharacteristicsAlgorithm = new HashSet<String>(this.allAlgorithmCharacteristics);

        
        //System.out.println("------------------------");
        //System.out.println("Characteristic Counts: ");

        // compute frequency counts and statistic of each distinct characteristic for ALGORITHM runs
        if(this.savedAlgorithmCharacteristics.size() > 0){
            this.statsFromCounts(distinctCharacteristicsAlgorithm, this.allAlgorithmCharacteristics, this.savedAlgorithmCharacteristics, this.sortedCharacteristicsAlgorithm, this.sortedStatisticsAlgorithm);

            // display the statistics
            this.displayStats( this.sortedCharacteristicsAlgorithm, this.sortedStatisticsAlgorithm, this.savedAverageAgeAlgorithm, "Algorithm",this.algorithmRuns);
        } 

        // compute frequency counts and statistic of each distinct characteristic for USER runs 
        if(this.savedUserCharacteristics.size() > 0){
            this.statsFromCounts(distinctCharacteristicsUser, this.allUserCharacteristics, this.savedUserCharacteristics, this.sortedCharacteristicsUser, this.sortedStatisticsUser);

            // display the statistics
            this.displayStats( this.sortedCharacteristicsUser, this.sortedStatisticsUser, this.savedAverageAgeUser, "User", this.userRuns);

        }
        
   
    }


    private void statsFromCounts(Set<String> distinctChars, ArrayList<String> allChars, ArrayList<String> savedChars, ArrayList<String> sortedChars, ArrayList<String> sortedStats) {
        //double averageAge =  0.0;

        // compute frequency counts and statistic of each distinct characteristic for USER runs 
        for(String c: distinctChars) {
            int countSavedCharacters = Collections.frequency(savedChars, c);
            int countAllCharacters = Collections.frequency(allChars, c);
            double stat = (double) countSavedCharacters / (double) countAllCharacters; 
            //System.out.printf("Charcteristic: %s, Saved count: %d, All count: %d, Statistic: %.2f \n", c, countSavedCharacters, countAllCharacters, stat);
            sortedChars.add(c);
            sortedStats.add(String.format("%.2f",stat));
        }
        // compute average age and add it to statistics arraylist
        // averageAge = (double) this.humansSavedTotalAgeUser / (double) this.humansSavedTotalUser;
        // sortedStats.add(String.format("%.2f",averageAge));

        // sort the statistics
        this.sortStats(sortedChars, sortedStats);

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
    

    private void displayStats(ArrayList<String> sortedCharacteristics, ArrayList<String> sortedStatistics, double averageAge, String decisionMaker, int numRuns) {

        System.out.println("======================================");
        System.out.printf("# %s Audit\n", decisionMaker);
        System.out.println("======================================");     
        System.out.printf("- %% SAVED AFTER %d RUNS\n", numRuns);
        int i = 0;
        for(i = 0; i < sortedCharacteristics.size(); i++){
            System.out.printf("%s: %s\n", sortedCharacteristics.get(i), sortedStatistics.get(i));
        }
        System.out.printf("--\naverage age: %.2f\n",averageAge);
    }

}