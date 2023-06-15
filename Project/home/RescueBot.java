import java.lang.Math;

/**
 * COMP90041, Sem1, 2023: Final Project
 * @author: Tanzid Sultan
 * student id: 1430660
 * student email: tanzids@student.unimelb.edu.au
 */
import java.util.Scanner;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * This program implements a RescueBot decision engine that offers both user-based and algorithm-based
 * options for choosing which groups to save at different scenarios. The core components of the RescueBot
 * object consists of the following: a file manager, a user-based scenario judging engine, an algorithm-based 
 * scenario judging engine (i.e. simulation) and an auditor for analyzing decision history data.  
 * 
 * @author: Tanzid Sultan
 * ID# 1430660, Email: tanzids@student.unimelb.edu.au
 */
public class RescueBot {

    /**
     * instance variables
     */
    private Scanner kb;
    private FileManager fManager;
    private UserJudgingEngine userJEngine;
    private SimulationJudgingEngine simulationJEngine;
    private Auditor audit;

    /**
	 * Constructor for initializing a RescueBot object.
	 */
    public RescueBot() {
        this.kb = new Scanner(System.in);
        this.fManager = new FileManager(kb);
        this.userJEngine = null; 
        this.simulationJEngine = null; 
        this.audit = null;
    }

    /**
	 * Local main method for testing the RescueBot class (Program entry)
	 * @param args  command-line arguments for reading in any input flags
	 */
    public static void main(String[] args) {
        
        // instantiate a rescuebot objec
        RescueBot rb = new RescueBot();

        // extract command line arguments
        boolean showHelp = rb.parseArgs(args);
        if (showHelp) { 
            rb.helpMenu();
            System.exit(0);
        }
        
        // display welcome message to screen
        rb.welcomeMsg();

        // import data from the user specified scenarios file if provided
        if (rb.fManager.getScenarioProvided()) {
            rb.fManager.importScenarios();
        }

        // instantiate the user-based judging engine object
        rb.userJEngine = new UserJudgingEngine(rb.kb, rb.fManager);

        // instantiate the simulation-based judging engine object
        rb.simulationJEngine = new SimulationJudgingEngine(rb.kb, rb.fManager);

        // instantiate the auditor object
        rb.audit = new Auditor(rb.kb,rb.fManager);

        // enter main menu loop
        rb.mainMenu();
    }
    
    /**
	 * Helper method for reading welcome message from file and displaying to the screen.
	 */
    private void welcomeMsg() {
        // read welcome message from file and display to screen
        Scanner inStream = null;
        try {
            inStream = new Scanner(new FileInputStream("welcome.ascii"));
            while (inStream.hasNextLine()) {
                System.out.println(inStream.nextLine());    
            }

        } catch (FileNotFoundException e) {
            System.out.println("Welcome message file not found! Aborting.");
            System.exit(1);
        }
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
                this.userJEngine.judgeScenarios();
                
            // OPTION: navigate to algorithm-based scenario judging simulation option
            } else if (userInput.equals("run") || userInput.equals("r")) {
                this.simulationJEngine.judgeScenarios();
            
            // OPTION: navigate to judging history audit option
            } else if (userInput.equals("audit") || userInput.equals("a")) {
                // option 3
                this.audit.auditLogHistory();
            
            // OPTION: terminate the program    
            } else if (userInput.equals("quit") || userInput.equals("q")) {
                // option 4
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

}