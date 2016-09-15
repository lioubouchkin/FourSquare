package fr.intech.foursquare;

/**
 */
public class Location {
    private String street;
    private String city;
    private String state;
    private String postalCode;
    private String country;
    private int distance;

    public Location (String street, String city, String state, String postalCode, String country, int distance ) {
        this.city = city;
        this.country = country;
        this.street = street;
        this.distance = distance;
        this.postalCode = postalCode;
        this.state = state;
    }
}
