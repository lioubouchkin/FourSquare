package fr.intech.foursquare;

/**
 */
public class PointOfInterest {
    private String id = "-1" ;
    private String name = "";
    private Location location = null;

    public PointOfInterest(String id, String name, Location location ) {
        this.id = id;
        this.location = location;
        this.name = name;
    }
}
