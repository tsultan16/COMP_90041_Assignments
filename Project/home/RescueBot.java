/**
 * COMP90041, Sem1, 2023: Final Project
 * @author Tanzid Sultan
 * student id: 1430660
 * student email: tanzids@student.unimelb.edu.au
 */
import java.util.Scanner;
import java.util.ArrayList;

/**
 * This program implements a RescueBot decision engine that offers both user-based and algorithm-based
 * options for choosing which groups to save at different scenarios. It offers two different methods of 
 * judging scenarios:
 *     (1) a user-based method in which each scenarios is presented and the user is prompted to decide which
 *         location to save  
 *     (2) an algorithm-based approach in which an algorithm decides which locations to save within each 
 *         scenario based on characteristics criteria of the inhabitants of each location. 
 *  
 * The auxiliary components used the RescueBot consists of the following: a file manager, a random scenario 
 * generator, a statictics generator and an auditor for analyzing decision  *history data.  
 * 
 * @author Tanzid Sultan
 * ID# 1430660, Email: tanzids@student.unimelb.edu.au
 */
public class RescueBot {

    /* decision maker label for algorithm */  
    public static final String DECISION_MAKER_USER = "USER";
    /* decision maker label for user */  
    public static final String DECISION_MAKER_ALGORITHM = "ALGORITHM";

    /**
     * instance variables
     */
    private Scanner kb;
    private FileManager fManager;
    private RandomScenarioGenerator rsGenerator;
    private StatisticsGenerator stats;
    private Auditor audit;

    private ArrayList<Scenario> scenarios;
    private ArrayList<Integer> decisions;
    private int lastScenarioJudged;


    /**
	 * Constructor for initializing a RescueBot object.
	 */
    public RescueBot() {
        this.kb = new Scanner(System.in);
        this.fManager = new FileManager(kb);
        this.rsGenerator = new RandomScenarioGenerator();
        this.stats = new StatisticsGenerator();
        this.audit = null;

        this.scenarios = null;
        this.lastScenarioJudged = 0;
        this.decisions = null;
    }

    /**
	 * Local main method for testing the RescueBot class (Program entry)
	 * @param args  command-line arguments for reading in any input flags
	 */
    public static void main(String[] args) {
        
        // instantiate a rescuebot object
        RescueBot rb = new RescueBot();

        // extract command line arguments
        boolean showHelp = rb.parseArgs(args);
        if (showHelp) { 
            rb.helpMenu();
            System.exit(0);
        }
        
        // display welcome message to screen
        rb.fManager.welcomeMsg();

        // import data from the user specified scenarios file if provided
        if (rb.fManager.getScenarioProvided()) {
            rb.fManager.importScenarios();
        }

        // instantiate the auditor object
        rb.audit = new Auditor(rb.kb,rb.fManager);

        // enter main menu loop
        rb.mainMenu();
    }

    /**
	 * Helper method for executing the main menu prompt.
	 */
    private void mainMenu() {
        String userInput;
        boolean done = false;
        do {
            // display main menu to screen
            System.out.println("Please enter one of the following commands to continue:");
            System.out.println("- judge scenarios: [judge] or [j]");
            System.out.println("- run simulations with the in-built decision algorithm: [run] or [r]");
            System.out.println("- show audit from history: [audit] or [a]");
            System.out.println("- quit the program: [quit] or [q]");
            System.out.print("> ");

            // obtain user input for menu option
            userInput = this.kb.next().toLowerCase();

            // OPTION: navigate to user-based scenario judging option
            if (userInput.equals("judge") || userInput.equals("j")) {
                this.judgeScenariosUser();
                
            // OPTION: navigate to algorithm-based scenario judging simulation option
            } else if (userInput.equals("run") || userInput.equals("r")) {
                this.judgeScenariosAlgorithm();
            
            // OPTION: navigate to judging history audit option
            } else if (userInput.equals("audit") || userInput.equals("a")) {
                this.audit.auditLogHistory();
            
            // OPTION: terminate the program    
            } else if (userInput.equals("quit") || userInput.equals("q")) {
                done = true;
            
            } else {
                System.out.print("Invalid command! ");
            }       

        } while (!done);
    }

    /**
	 * Helper method for displaying the help menu to screen.
	 */
    private void helpMenu() {
        System.out.print("RescueBot - COMP90041 - Final Project\n\n");
        System.out.print("Usage: java RescueBot [arguments]\n\n");
        System.out.println("Arguments:");        
        System.out.println("-s or --scenarios\tOptional: path to scenario file");  
        System.out.println("-h or --help\t\tOptional: Print Help (this message) and exit");
        System.out.println("-l or --log\t\tOptional: path to data log file");      
    }

    /**
	 * Helper method for parsing the command line arguments, determining whether valid input flags have 
     * been provided and deciding whether help menu needs to be displayed
     *  
	 * @param args  command-line arguments array
     * @return whether help menu needs to be shown
	 */
    private boolean parseArgs(String[] args) {
        boolean lSet = false;
        boolean sSet = false;
        boolean hasLogPath = false;
        boolean hasScenarioPath = false; 
        boolean showHelp = true;

        // determine if user has provided valid set of command line arguments
        if (args.length == 0) {
            return false;
        } 

        for (int i = 0; i < args.length; i++) {
            if ((args[i].equals("--log")) || (args[i].equals("-l"))) {
                lSet = true;
                if((i+1) < args.length) {
                    if (args[i+1].charAt(0) != '-') {
                        hasLogPath = true;
                        this.fManager.setLogFile(args[i+1]);
                    }
                }

            } else if ((args[i].equals("--scenarios")) || (args[i].equals("-s"))) {
                sSet = true;
                if ((i+1) < args.length) {
                    if(args[i+1].charAt(0) != '-') {
                        hasScenarioPath = this.fManager.setScenarioFile(args[i+1]);
                    }
                }
            }
        }

        // do not need to display help menu for appropriate set of command line arguments
        if ((hasLogPath && lSet && (!sSet)) || (hasScenarioPath && sSet && (!lSet)) || (((hasScenarioPath && sSet) && (hasLogPath && lSet)))) {
            showHelp = false;
        }
        return showHelp;    
    }


    /**
	 * Accessor method for retrieving scenarios data.
     * 
     * @return a copy of the array list containing scenarios
	 */
    public ArrayList<Scenario> getScenarios() {
        // return a deep copy of the scenarios array list
        ArrayList<Scenario> scenariosCopy = new ArrayList<Scenario>(0);
        for (Scenario sc: this.scenarios){
            scenariosCopy.add(new Scenario(sc));
        }
        return scenariosCopy;
    }

    /**
	 * Accessor method for retrieving judging decisions.
     * 
     * @return a copy of the array list containing user decisions for judged scenarios
	 */
    public ArrayList<Integer> getDecisions() {
        // return a deep copy of the array list containing user decisions  
        ArrayList<Integer> decisionsCopy = new ArrayList<Integer>(0);
        for (int i = 0; i < this.decisions.size(); i++) {
            decisionsCopy.add(this.decisions.get(i));
        }
        return decisionsCopy;
    }


    /**
    * Helper method for acquiring scenarios from file
    *
    * @return success or failure of loading scenarios from file 
    */
    private boolean loadScenariosfromFile() {

        // import scenarios from file (if option provided)
        if (this.fManager.getScenarioProvided()){
            this.scenarios = this.fManager.getFileScenarios();
            return true;
        } else {
            // if unable to import, then create an empty scenarios array list
            this.scenarios = new ArrayList<Scenario>(0);
            return false;
        }
    }

    /**
    * Helper method for acquiring default number of randomly generated scenarios
    */
    private void generateRandomScenarios() {
        // call random scenario generator (returns 3 randomly generated scenarios by default)
        ArrayList <Scenario> newRandomScenarios = this.rsGenerator.generateScenarios();
        // append these newly generated random scenarios to the scenarios array list
        for(Scenario sc: newRandomScenarios) {
            this.scenarios.add(sc);
        }
    }

    /**
    * @Overload Helper method for acquiring specified number of randomly generated scenarios
    *
    * @param  N number of scenarios to generate
    */
    private void generateRandomScenarios(int N) {
        // call random scenario generator (returns 3 randomly generated scenarios by default)
        ArrayList <Scenario> newRandomScenarios = this.rsGenerator.generateScenarios(N);
        // append these newly generated random scenarios to the scenarios array list
        for(Scenario sc: newRandomScenarios) {
            this.scenarios.add(sc);
        }
    }


    /**
    * Helper method for facilitating user-based judging of scenarios
    */
    public void judgeScenariosUser() {

        // prompt for user consent to save decisions to log file
        this.fManager.collectUserConsent();

        // attempt to load scenarios from file
        boolean scenariosLoadedFromFile = this.loadScenariosfromFile();
        this.decisions = new ArrayList<Integer>(0);

        boolean done = false;
        String userInput = "";
        while (!done) {
            // if ubable to load scenarios from file, then generate scenarios randomly 
            if(!scenariosLoadedFromFile) {
                this.generateRandomScenarios();
            }

            // judge next three (or however many less than 3 remaining) scenarios, get user decisions 
            int initScenario = this.lastScenarioJudged;
            int finalScenario =  Math.min(this.lastScenarioJudged+3,this.scenarios.size());
            for (int i = initScenario; i < finalScenario; i++) {
                this.decisions.add(this.decideUser(this.scenarios.get(i)));
            } 
            this.lastScenarioJudged = finalScenario;

            // compute and display statistics
            this.stats.generateStatistics(this.getScenarios(), this.lastScenarioJudged , this.getDecisions());   

            // save judged scenarios to log file if user consented
            if(this.fManager.getUserConsented()){
                this.fManager.saveToLogFile(this.getScenarios(), initScenario, this.lastScenarioJudged , this.getDecisions(), DECISION_MAKER_USER);
            }

            // prompt user for rerun if more scenarios remaining from all scenarios loaded from file
            if (scenariosLoadedFromFile && (finalScenario == this.scenarios.size())) {
                done = true;
            } else {
                boolean validResponse = false;
                System.out.println("Would you like to continue? (yes/no)");
                while (!validResponse){
                    System.out.print("> ");
                    userInput = this.kb.next().toLowerCase();
                    try {
                        if ((userInput.equals("yes")) || (userInput.equals("no"))) {
                            if (userInput.equals("no")) {
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

        // reset judged scenarios and return to main menu
        this.resetJudgedScenariosReturn(); 
    }

    /**
    * Decision maker for prompting user which group to save within the given scenario
    * @param sc  the scenario
    * @return  the integer location number of which group to save
    */
    public int decideUser(Scenario sc) {

        // display all relevant information pertaining to this scenario
        System.out.println("======================================");
        System.out.println("# Scenario: " + sc.getDescriptor());
        System.out.println("======================================");

        // show each location
        ArrayList<Location> locations = sc.getLocations();
        for (int i = 1; i <= locations.size(); i++) {
            Location loc = locations.get(i-1);
            System.out.printf("[%d] Location: %s, %s\n",i, loc.getLatitude(), loc.getLongitude());
            System.out.println("Trespassing: " + loc.getTrespassing());
            // show each character
            ArrayList<Character> characters = loc.getCharacters();
            System.out.printf("%d Characters: \n",characters.size());
            for (Character ch: characters) {
                String characteristics = ch.toString();
                String[] sp = characteristics.split(" ");
                characteristics = sp[1];
                for (int j = 2; j < sp.length; j++) {
                    if (sp[j].equals("pet")) {
                        sp[j] = "is pet";
                    }
                    characteristics += " " + sp[j]; 
                }
                System.out.println("- " + characteristics);
            }
        }

        // prompt user to choose a location
        boolean done = false;
        int userOption = 0;
        System.out.println("To which location should RescueBot be deployed?");
        while (!done) {
            System.out.print("> ");
            try {
                userOption = Integer.parseInt(this.kb.next());
                if ((userOption < 1) || (userOption > locations.size())) {
                    throw new Exception();
                } else {
                    done = true;
                }
            } catch (Exception e) {
                System.out.println("Invalid response! To which location should RescueBot be deployed?");
            }
        }

        // subtract 1 because were storing the array index of the chosen location
        int decision = userOption-1; 
        return decision;
    }


    /**
    * Helper method for facilitating algorithm-based judging of scenarios
    */  
    public void judgeScenariosAlgorithm() {

        // attempt to load scenarios from file
        boolean scenariosLoadedFromFile = this.loadScenariosfromFile();
        this.decisions = new ArrayList<Integer>(0);

        // if ubable to load scenarios from file, then generate scenarios randomly 
        if (!scenariosLoadedFromFile) {
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
            this.generateRandomScenarios(N);          
        }

        // judge all scenarios (get algorithm decisions)
        for (int i = 0; i < this.scenarios.size(); i++) {
            this.decisions.add(decide(this.scenarios.get(i)));
        } 
        this.lastScenarioJudged = this.scenarios.size();
        
        // compute and display statistics
        this.stats.generateStatistics(this.getScenarios(), this.lastScenarioJudged, this.getDecisions());   
 
        // save judged scenarios to log file
        this.fManager.saveToLogFile(this.getScenarios(), 0, this.lastScenarioJudged , this.getDecisions(), DECISION_MAKER_ALGORITHM);

        // reset judged scenarios and return to main menu
        this.resetJudgedScenariosReturn(); 
    }

    /**
    * Decision algorithm for selecting a group to save within a given scenario.
    * This algorithm first computes a "decision score" for each location using the characteristics of 
    * its inhabitants. Different characteristics are assigned different weights which contribute
    * to the overall score (these weights have been-hardcoded inside a helper method). Then the 
    * algorithm makes a decision by choosing the location which has the highest decision score.
    *
    * (Note: This is a public method instead of public static. Beacause this method requires lots of extra 
    *        computations, it is convenient to carry out these computations using other private helper methods 
    *        rather than doing them all inside this decision method or creating several other public staic 
    *        helper methods.)
    *
    * @param sc  the scenario
    * @return  the integer location number of which group to save
    *
    * (Note: the return variable is the integer location number, not a location object! This is because
    * it is simpler to just store all the decision location integers in an array, and use these numbers
    * to access the relevant locations directly from the existing scenarios object which already contains
    * an array of location objects, rather than having to re-create another separate array of location objects.)
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
    * Helper method for computing the decision score for a given location using hard-coded weights. The 
    * following 6 characteristics are used to determine the decision score: 
    *   (1) human or animal
    *   (2) age category (if human)
    *   (3) pregnancy status (if human)
    *   (4) pet status status (if animal)
    *   (5) profession (if human)
    *   (6) endangered species status (if animal)
    *
    * Weights are assigned to each of these characteristics. A decision score for a location is then computed 
    * by accumulating weights for each characteristic that is present among each character at the location.
    *
    * @param loc  location object for which the decision score will be computed
    * @return  decision score
    */ 
    private double computeDecisionScore(Location loc) {

        
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
    * Helper method for cleanup/resetting of the data for judged scenarios and returning to main menu
    */ 
    private void resetJudgedScenariosReturn() {
        this.scenarios = null;
        this.lastScenarioJudged = 0;
        this.decisions = null;

        // return to main menu
        System.out.println("That's all. Press Enter to return to main menu.");
        System.out.print("> ");
        this.kb.nextLine();
        this.kb.nextLine();   
    }

}
