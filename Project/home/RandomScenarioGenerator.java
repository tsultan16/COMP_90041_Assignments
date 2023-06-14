import java.util.ArrayList;
import java.util.Random;

public class RandomScenarioGenerator {

    private Random rng;


    public static final long RNG_SEED = 12345;
    public static final int NUM_SCENARIOS = 10;
    public static final int MIN_NUM_LOCATIONS = 2;
    public static final int MAX_NUM_LOCATIONS = 5;
    public static final int MIN_NUM_CHARACTERS = 1;
    public static final int MAX_NUM_CHARACTERS = 8;


    public static final String[] SCENARIO_DESCRIPTORS = {"cyclone", "flood", "bushfire", "earthquake", "blizzard"};
    public static final String[] GENDERS = {"male", "female"};
    public static final String[] BODYTYPES = {"average", "athletic", "overweight"};
    public static final String[] PROFESSIONS = {"doctor", "ceo", "criminal", "homeless", "none", "student", "unemployed", "scientist"};
    public static final String[] SPECIES = {"cat", "ferret", "dog", "koala", "kangaroo", "wolf", "platypus", "snake", "possum", "dingo", "bear", "wallaby", "cockatoo", "boar"};


    // default constructor
    public RandomScenarioGenerator() {
        // create a random number generator object
        Random rng = new Random(RNG_SEED);        
    }


    public ArrayList<Scenario> generateScenarios() {

        ArrayList<Scenario> randomScenarios = new ArrayList<Scenario>(0);

        // generate scenarios
        for(int s = 0; s < NUM_SCENARIOS; s++) {

            // generate a random scenario descriptor
            int id = rng.nextInt(SCENARIO_DESCRIPTORS.length); 
            // instantiate scenario object
            Scenario scen = new Scenario(SCENARIO_DESCRIPTORS[id]);

            // generate a random number of locations for this scenario
            int numLoc = MIN_NUM_LOCATIONS + rng.nextInt(MAX_NUM_LOCATIONS - MIN_NUM_LOCATIONS + 1);
            for(int i = 0; i < numLoc; i++) {

                // generate random location co-ordinates
                String latitude = String.format("%.4f", rng.nextDouble() * 90.0);
                String longitude = String.format("%.4f", rng.nextDouble() * 180.0);
                id = rng.nextInt(2);
                if(id == 0) {
                    latitude += " N"; 
                } else {
                    latitude += " S";
                }
                id = rng.nextInt(2);
                if(id == 0) {
                    longitude += " E"; 
                } else {
                    longitude += " W";
                }

                // generate random trespassing state    
                id = rng.nextInt(2);
                String trespassing = "";
                if(id == 0) {
                    trespassing = "trespassing"; 
                } else {
                    trespassing = "legal" ;
                }
                // instantiate location object
                Location loc = new Location(latitude, longitude, trespassing); 

                // generate a random number of locations for this scenario
                int numchar = MIN_NUM_CHARACTERS + rng.nextInt(MAX_NUM_CHARACTERS - MIN_NUM_CHARACTERS + 1);
                for(int j = 0; j < numLoc; i++) {

                    // generate random age
                    id = rng.nextInt(GENDERS.length);
                    String gender = GENDERS[id];

                    // generate random body type
                    id = rng.nextInt(BODYTYPES.length);
                    String bodyType = BODYTYPES[id];

                    // generate random character type (human or animal)
                    Character ch = null;
                    id = rng.nextInt(2);
                    // human
                    if(id == 0) {
                        // generate random age
                        int age = 1 + rng.nextInt(99);  
                        
                        // generate random profession
                        id = rng.nextInt(PROFESSIONS.length);
                        String profession = PROFESSIONS[id];

                        // generate random pregnancy status 
                        boolean pregnant = false;

                        if((gender.equals("female")) && ((age >= 17) && (age <= 68))) {
                            id = rng.nextInt(2);
                            if(id == 0) {
                                pregnant = true;
                            } 
                        }

                        // instantiate a human object
                        ch = new Human(gender, age, bodyType, profession, pregnant); 

                    // animal    
                    } else {
                        // generate random age
                        int age = 1 + rng.nextInt(20);  

                        // generate a random species
                        id = rng.nextInt(SPECIES.length);
                        String species = SPECIES[id];

                        // generate a random pet status for appropriate species
                        boolean isPet = false;
                        if((species.equals("cat")) || (species.equals("dog")) || (species.equals("ferret"))) {
                            id = rng.nextInt(2);
                            if(id == 0) {
                                isPet = true;
                            } 
                        }
                        ch = new Animal(gender, age, bodyType, species, isPet);
                    }
                    loc.addCharacter(ch);
                }        
                scen.addLocation(loc);
            }
            randomScenarios.add(scen);
        }
        return randomScenarios;
    }


}