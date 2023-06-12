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

public class RescueBot {

    private String logFile;
    private String scenarioFile;
    private Scanner kb;
    
    public RescueBot() {
        this.logFile = "";
        this.scenarioFile = "";
        this.kb = new Scanner(System.in);
    }

    /**
     * Decides whether to save the passengers or the pedestrians
     * @param Scenario scenario: the ethical dilemma
     * @return Decision: which group to save
     */

     /*
    public static Location decide(Scenario scenario) {
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
 
    private boolean isValidFileName(String filename, String type) {
        boolean valid = false; 
        String[] sp = filename.split("\\.");
        for(String s: sp) {
            System.out.println(s);
        }
        if(sp.length > 1) {
            if(sp[1].equals(type)) {
                valid = true;
            }
        }
        return valid;
    }

    private boolean validArgs(String[] args) {
        boolean lSet = false;
        boolean sSet = false;
        boolean hasLogPath = false;
        boolean hasScenarioPath = false; 
        boolean showHelp = false;

        if(args.length == 0){
            return true;
        } 

        if((args[0].equals("--help")) || (args[0].equals("-h"))) {
            showHelp = true;
        } else { 
            for (int i = 0; i < args.length; i++) {
                if((args[i].equals("--log")) || (args[i].equals("-l"))) {
                    lSet = true;
                    if(((i+1) < args.length) && (this.isValidFileName(args[i+1], "log"))) {
                        hasLogPath = true;
                        this.logFile = args[i+1];    
                    }
                } else if((args[i].equals("--scenario")) || (args[i].equals("-s"))) {
                    sSet = true;
                    if(((i+1) < args.length) && (this.isValidFileName(args[i+1], "csv"))) {
                        hasScenarioPath = true;
                        this.scenarioFile = args[i+1];    
                    }
                }
            }
            if(((!hasLogPath) && lSet) || ((!hasScenarioPath) && sSet)) {
                showHelp = true;
            }
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

            userInput = this.kb.next();
            if (userInput.equals("judge") || userInput.equals("j")) {
                // option 1
                
            } else if (userInput.equals("run") || userInput.equals("r")) {
                // option 2
            
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

    /**
     * Program entry
     */
    public static void main(String[] args) {
        
        // instantiate a rescuebot objec
        RescueBot rb = new RescueBot();

        // check for valid command line arguments
        boolean showHelp = rb.validArgs(args);
        if(showHelp) { 
            rb.helpMenu();
            System.exit(0);
        }

        // display welcome message to screen
        rb.welcomeMsg();

        // enter main menu loop
        rb.mainMenu();
    }
}
