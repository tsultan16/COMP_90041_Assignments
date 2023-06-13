import java.util.ArrayList;

public class JudgingEngine {


    private ArrayList <Scenario> scenarios;

    public JudgingEngine(ArrayList<Scenario> scenarios) {
        this.scenarios = scenarios;

    }

    public void startJudging() {
        System.out.println("Judging in progress...");  

        // judge each scenario one by one
        for(Scenario sc: scenarios) {
            this.judgeScenario(sc);
        } 

    }

    private void judgeScenario(Scenario sc) {

        // display all relevant information pertaining to this scenario
        System.out.println("======================================");
        System.out.println("# Scenario: " + sc.getDescriptor());
        System.out.println("======================================");

        // show each location
        ArrayList<Location> locations = sc.getLocations();
        System.out.println("Number of locations: " +locations.size());
        for (int i = 1; i <= locations.size(); i++) {
            Location loc = locations.get(i-1);
            System.out.printf("[%d] Location: %s, %s\n",i, loc.getLatitude(), loc.getLongitude());
            System.out.println("Trespassing: " + loc.getTrespassing());
            // show each character
            ArrayList<Character> characters = loc.getCharacters();
            System.out.printf("%d Characters:\n",characters.size());
            for (Character ch: characters) {
                System.out.println("- " + ch);
            }

        }

    }

}