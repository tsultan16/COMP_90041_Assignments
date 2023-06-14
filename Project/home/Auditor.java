import java.util.Scanner;
import java.util.Set;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;

public class Auditor {

    private Scanner kb;
    private FileManager fManager;   
    private int humansSavedTotalUser = 0;
    private int humansSavedTotalAlgorithm = 0;
    private int humansSavedTotalAgeUser = 0;
    private int humansSavedTotalAgeAlgorithm = 0;
    private ArrayList<String> allUserCharacteristics;
    private ArrayList<String> allSimulationCharacteristics;
    private ArrayList<String> savedUserCharacteristics;
    private ArrayList<String> savedSimulationCharacteristics;

    // default constructor
    public Auditor(Scanner kb, FileManager fManager) {
        this.kb = kb;
        this.fManager = fManager; 

        this.humansSavedTotalUser = 0;
        this.humansSavedTotalAlgorithm = 0;
        this.humansSavedTotalAgeUser = 0;
        this.humansSavedTotalAgeAlgorithm = 0;

        this.allUserCharacteristics = null;
        this.allSimulationCharacteristics = null;
        this.savedUserCharacteristics = null;
        this.savedSimulationCharacteristics = null;
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

            // compute statistics
            System.out.println("Computing global statistics");
            
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
        this.allSimulationCharacteristics = new ArrayList<String>(0);
        this.savedUserCharacteristics = new ArrayList<String>(0);
        this.savedSimulationCharacteristics  = new ArrayList<String>(0);

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
                } 

            // algorithm made the decision    
            } else {

                // get simulation decision
                int decision = Integer.parseInt(sp[5])-1;

                // collect characteristics
                for(int j = 0; j < scenarioCharacteristics.size(); j++) {
                    ArrayList<String> sc = scenarioCharacteristics.get(j);
                    for(String characteristic: sc) {
                        this.allSimulationCharacteristics.add(characteristic);
                        if(decision == j) {
                            this.savedSimulationCharacteristics.add(characteristic);
                        }
                    }
                } 

                this.humansSavedTotalAlgorithm += humansSaved[decision];
                this.humansSavedTotalAgeAlgorithm += humansSavedTotalAge[decision];
            }
            i++;
        }
        System.out.println("Done parsing log file data!");
        System.out.printf("USER -> Humans Saved: %d, Total Age: %d \n",this.humansSavedTotalUser, this.humansSavedTotalAgeUser);
        System.out.printf("ALGORITHM -> Humans Saved: %d, Total Age: %d \n",this.humansSavedTotalAlgorithm, this.humansSavedTotalAgeAlgorithm);

    }

    /*

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
        Set<String> distinctCharacteristics = new HashSet<String>(this.allScenariosCharacteristics);

        // compute frequency counts and statistic of each distinct characteristic across the saved characters and all characters 
        //System.out.println("------------------------");
        //System.out.println("Characteristic Counts: ");
        for(String c: distinctCharacteristics) {
            int countSavedCharacters = Collections.frequency(savedCharactersCharacteristics, c);
            int countAllCharacters = Collections.frequency(this.allScenariosCharacteristics, c);
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

 */

}