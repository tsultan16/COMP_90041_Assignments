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

}