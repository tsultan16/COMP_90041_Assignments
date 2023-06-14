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

public class RescueBot {

    private Scanner kb;
    private FileManager fManager;
    private UserJudgingEngine userJEngine;
    private SimulationJudgingEngine simulationJEngine;


    public RescueBot() {
   
        this.kb = new Scanner(System.in);
        this.fManager = new FileManager(kb);
        this.userJEngine = null; 
        this.simulationJEngine = null; 
    }




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

    private void helpMenu() {
        System.out.print("RescueBot - COMP90041 - Final Project\n\n");
        System.out.print("Usage: java RescueBot [arguments]\n\n");
        System.out.println("Arguments:");        
        System.out.println("-s or --scenarios    Optional: path to scenario file");  
        System.out.println("-h or --help        Optional: Print Help (this message) and exit");
        System.out.println("-l or --log        Optional: path to data log file");      
    }
 


    private boolean extractArgs(String[] args) {
        boolean lSet = false;
        boolean sSet = false;
        boolean hasLogPath = false;
        boolean hasScenarioPath = false; 
        boolean showHelp = true;

        // determine if user has provided valid set of command line arguments
        if(args.length == 0){
            return false;
        } 
        for (int i = 0; i < args.length; i++) {
            if((args[i].equals("--log")) || (args[i].equals("-l"))) {
                lSet = true;
                if((i+1) < args.length) {
                    if(args[i+1].charAt(0) != '-') {
                        hasLogPath = true;
                        this.fManager.setLogFile(args[i+1]);
                    }
                }
            } else if((args[i].equals("--scenarios")) || (args[i].equals("-s"))) {
                sSet = true;
                if((i+1) < args.length) {
                    if(args[i+1].charAt(0) != '-') {
                        hasScenarioPath = true;
                        this.fManager.setScenarioFile(args[i+1]);
                    }
                }
            }
        }

        // do not need to display help menu for appropriate set of command line arguments
        if((hasLogPath && lSet && (!sSet)) || (hasScenarioPath && sSet && (!lSet)) || (((hasScenarioPath && sSet) && (hasLogPath && lSet)))) {
            showHelp = false;
        }
        return showHelp;    
    }

    private void mainMenu() {
        String userInput;
        boolean done = false;
        do {
            System.out.println("Please enter one of the following commands to continue:");
            System.out.println("- judge scenarios: [judge] or [j]");
            System.out.println("- run simulations with the in-built decision algorithm: [run] or [r]");
            System.out.println("- show audit from history: [audit] or [a]");
            System.out.println("- quit the program: [quit] or [q]");
            System.out.print("> ");

            userInput = this.kb.next().toLowerCase();
            if (userInput.equals("judge") || userInput.equals("j")) {
                // collect user consent, then commence judging
                this.collectUserConsent();
                this.userJEngine.judgeScenarios();
                
            } else if (userInput.equals("run") || userInput.equals("r")) {
                // run simulation
                this.simulationJEngine.judgeScenarios();
            
            } else if (userInput.equals("audit") || userInput.equals("a")) {
                // option 3
            
            } else if (userInput.equals("quit") || userInput.equals("q")) {
                // option 4
                done = true;
            
            } else {
                System.out.print("Invalid command! ");
            }       

        } while (!done);
    }

    private void collectUserConsent() {
        boolean done = false;
        String userInput = "";
        System.out.println("Do you consent to have your decisions saved to a file? (yes/no)");
        while(!done) {
            System.out.print("> ");
            userInput = kb.next().toLowerCase();
            try{
                if((userInput.equals("yes")) || (userInput.equals("no"))) {
                    done = true;
                } else {
                    throw new InvalidInputException("Invalid response! Do you consent to have your decisions saved to a file? (yes/no)");
                }
            } catch (InvalidInputException e) { 
                System.out.println(e.getMessage());
            }
            
        } 
        if (userInput.equals("yes")) {
            this.fManager.setUserConsented();
        } 

    }

    /**
     * Program entry
     */
    public static void main(String[] args) {
        
        // instantiate a rescuebot objec
        RescueBot rb = new RescueBot();

        // extract command line arguments
        boolean showHelp = rb.extractArgs(args);
        if(showHelp) { 
            rb.helpMenu();
            System.exit(0);
        }

        // instantiate the user-based judging engine object
        rb.userJEngine = new UserJudgingEngine(rb.kb, rb.fManager);

        // instantiate the simulation-based judging engine object
        rb.simulationJEngine = new SimulationJudgingEngine(rb.kb, rb.fManager);

        // display welcome message to screen
        rb.welcomeMsg();

        // enter main menu loop
        rb.mainMenu();
    }
}
