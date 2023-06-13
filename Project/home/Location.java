import java.util.ArrayList;

public class Location {

    private String latitude;
    private String longitude;
    private boolean trespassing;
    private ArrayList<Character> characters;



    public Location(String latitude, String longitude, String tp){
        this.latitude = latitude;
        this.longitude = longitude;
        this.trespassing = Boolean.parseBoolean(tp);
        this.characters = new ArrayList<Character>(0);
        
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

    public String getTrespassing() {
        String tresp;
        if(this.trespassing) {
            tresp = "yes";
        } else {
            tresp = "no";
        }
        return tresp;
    }

    public ArrayList<Character> getCharacters() {
        // return a deep copy of the characters array list
        ArrayList<Character> charactersCopy = new ArrayList<Character>(0);
        for(Character c: this.characters) {
            charactersCopy.add(c);
        }
        return charactersCopy;
    }

}