import java.util.ArrayList;

/**
 * A class for representing scenario objects.
 *
 * @author Tanzid Sultan
 * ID# 1430660, Email: tanzids@student.unimelb.edu.au
 */
public class Scenario {
    
    /**
    *  instance variables 
    */  
    private String descriptor;
    private ArrayList<Location> locations;

    /**
	*  Class default constructor
    *
    * @param descriptor  scenario description
	*/     
    public Scenario(String descriptor) {
        this.descriptor = descriptor;
        this.locations = new ArrayList<Location>(0);
    }

    /**
	* Overloaded class copy constructor
    *
    * @param other  scenario object to be copied
	*/     
    public Scenario(Scenario other) {
        this.descriptor = other.getDescriptor();
        this.locations = other.getLocations();
    }

	/**
	 * Mutator method for adding a new location to this scenario
     * 
	 * @param newLoc the new location object 
	 */
    public void addLocation(Location newLoc) {
        this.locations.add(newLoc);
    }

    /**
	 * Accessor method for obtaining total number of locations within the scenario 
	 *
     * @return number of locations
	 */
    public int getNumLocations() {
        return this.locations.size();
    }

    /**
	 * Accessor method for obtaining description of scenario 
	 *
     * @return scenario descriptor
	 */
    public String getDescriptor() {
        return this.descriptor;
    }

    /**
	 * Accessor method for obtaining all locations within the scenario 
	 *
     * @return array list of location objects
	 */
    public ArrayList<Location> getLocations() {
        // return a deep copy of the locations array list
        ArrayList<Location> locationsCopy = new ArrayList<Location>(0);
        for (Location l: this.locations) {
            locationsCopy.add(l);
        }
        return locationsCopy;
    }

     /**
	 * Accessor method for obtaining a specified location within the scenario 
	 *
	 * @param locationNumber  array index for the specified location object
     * @return location object
	 */
    public Location getLocation (int locationNumber) {
        // return a copy of requested location from this scenario
        Location locationCopy = new Location(this.locations.get(locationNumber));
        return locationCopy;
    }

}