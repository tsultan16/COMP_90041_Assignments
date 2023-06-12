import java.util.ArrayList;

public class Scenario {
    

    private String descriptor;
    private ArrayList<Location> locations;


    public Scenario(String descriptor) {
        this.descriptor = descriptor;
        this.locations = new ArrayList<Location>(0);

    }


    public void addLocation(Location newLoc) {
        this.locations.add(newLoc);
    }

    public int getNumLocations() {
        return this.locations.size();
    }

}