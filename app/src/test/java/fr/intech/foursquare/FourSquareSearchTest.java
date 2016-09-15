package fr.intech.foursquare;

import org.junit.Test;

import static org.junit.Assert.*;
/**
 */
public class FourSquareSearchTest {
    private static final String ID = "JQYYQV1QHVVYOBFXL4ZJMHFSINZQNAAY04JZME34BRWCCDQK";
    private static final String SECRET = "XWRCFSX3NNU2N1MY5SCNOXHAVS3ASVRLSMYMCD3UZCXPLXFV";
    private static final String CLIENT_ID = "client_id";
    private static final String CLIENT_SECRET = "client_secret";
    private static final String COORDINATES = "ll";
    private static final String VERSION = "v";

    private static final String URI = "https://api.foursquare.com/v2/venues/search?";

    @Test
    public void query_isCorrect() throws Exception {

        String query = "https://api.foursquare.com/v2/venues/search" +
                "?ll=40.7,74" +
                "&client_id=JQYYQV1QHVVYOBFXL4ZJMHFSINZQNAAY04JZME34BRWCCDQK" +
                "&client_secret=XWRCFSX3NNU2N1MY5SCNOXHAVS3ASVRLSMYMCD3UZCXPLXFV" +
                "&v=20161915";
        String check = URI + COORDINATES + "=" + 40.7 + "," + 74
                + "&" + CLIENT_ID + "=" + ID
                + "&" + CLIENT_SECRET + "=" + SECRET
                + "&" + VERSION + "=" + "20160915";

        assertEquals(query,"https://api.foursquare.com/v2/venues/search" +
                "?ll=40.7,74" +
                "&client_id=JQYYQV1QHVVYOBFXL4ZJMHFSINZQNAAY04JZME34BRWCCDQK" +
                "&client_secret=XWRCFSX3NNU2N1MY5SCNOXHAVS3ASVRLSMYMCD3UZCXPLXFV" +
                "&v=20161915");
    }
}
