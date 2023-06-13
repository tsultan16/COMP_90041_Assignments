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

    public String getDescriptor() {
        return this.descriptor;
    }

    public ArrayList<Location> getLocations() {
        // return a deep copy of the locations array list
        ArrayList<Location> locationsCopy = new ArrayList<Location>(0);
        for(Location l: this.locations) {
            locationsCopy.add(l);
        }
        return locationsCopy;
    }

    public Location getLocation (int locationNumber) {
        // return a copy of requested location from this scenario
        Location locationCopy = new Location(this.locations.get(locationNumber));
        return locationCopy;
    }

}