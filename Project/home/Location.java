import java.util.ArrayList;

public class Location {

    private String latitude;
    private String longitude;
    private boolean trespassing;
    private ArrayList<Character> characters;

    public static final String DEFAULT_LATITUDE = "10.0000 N";
    public static final String DEFAULT_LONGITUDE = "10.0000 E";


    // default constructor, with exception handling
    public Location(String latitude, String longitude, String tp, int lineNum){
        this.latitude = this.validLatitude(latitude, lineNum);
        this.longitude = this.validLongitude(longitude, lineNum);
        if(tp.equals("legal")) {
            this.trespassing = false;
        } else {
            this.trespassing = true;
        }
        this.characters = new ArrayList<Character>(0);
        
    }

    // constructor without exception handling
    public Location(String latitude, String longitude, String tp){
        this.latitude = latitude;
        this.longitude = longitude;
        if(tp.equals("legal")) {
            this.trespassing = false;
        } else {
            this.trespassing = true;
        }
        this.characters = new ArrayList<Character>(0);
        
    }

    // copy costructor
    public Location(Location other) {
        this.latitude = other.getLatitude();
        this.longitude = other.getLongitude();
        this.trespassing = other.getTrespassingBool();
        this.characters = other.getCharacters();
    }    

    public void addCharacter(Character newChar) {
        this.characters.add(newChar);
    }

    public int getNumCharacters() {
        return this.characters.size();
    }

    public String getLatitude() {
        return this.latitude;
    }

    public String getLongitude() {
        return this.longitude;
    }

    public boolean getTrespassingBool () { 
        return this.trespassing;
    }

    public String getTrespassing() {
        String tresp;
        if (this.trespassing) {
            tresp = "yes";
        } else {
            tresp = "no";
        }
        return tresp;
    }

    public ArrayList<Character> getCharacters() {
        // return a deep copy of the characters array list
        ArrayList<Character> charactersCopy = new ArrayList<Character>(0);
        for (Character c: this.characters) {
            charactersCopy.add(c);
        }

        return charactersCopy;
    }


    private String validLatitude(String latitude, int lineNum) {
        String valid;
        String[] sp = latitude.split(" ");    
        try {
            // make sure latitude value contains both a degree and a N/S identifier
            if (sp.length == 2) {
                // make sure the degree value is numeric
                try {
                    double degree = Double.parseDouble(sp[0]);
                } catch (NumberFormatException e) {
                    throw new InvalidCharacteristicException("WARNING: invalid characteristic in scenarios file in line " + lineNum);
                }
                // make sure the degree identifier is either 'N' or 'S'
                if ((sp[1].equals("N")) || (sp[1].equals("S"))) {
                    valid = latitude;
                } else {
                    throw new InvalidCharacteristicException("WARNING: invalid characteristic in scenarios file in line " + lineNum);
                }
            } else {
                throw new InvalidCharacteristicException("WARNING: invalid characteristic in scenarios file in line " + lineNum);
            }
        } catch(InvalidCharacteristicException e) {
            System.out.println(e.getMessage());
            valid= DEFAULT_LATITUDE;
        }
        return valid;
    }

    private String validLongitude(String longitude, int lineNum) {
        String valid;
        String[] sp = longitude.split(" ");    
        try {
            // make sure longitude value contains both a degree and an E/W identifier
            if (sp.length == 2) {
                // make sure the degree value is numeric 
                try {
                    double degree = Double.parseDouble(sp[0]);
                } catch (NumberFormatException e) {
                    throw new InvalidCharacteristicException("WARNING: invalid characteristic in scenarios file in line " + lineNum);
                }
                // make sure the degree identifier is either 'E' or 'W'
                if ((sp[1].equals("E")) || (sp[1].equals("W"))) {
                    valid = longitude;
                } else {
                    throw new InvalidCharacteristicException("WARNING: invalid characteristic in scenarios file in line " + lineNum);
                }
            } else {
                throw new InvalidCharacteristicException("WARNING: invalid characteristic in scenarios file in line " + lineNum);
            }
        } catch(InvalidCharacteristicException e) {
            System.out.println(e.getMessage());
            valid= DEFAULT_LONGITUDE;
        }
        return valid;
    }

}