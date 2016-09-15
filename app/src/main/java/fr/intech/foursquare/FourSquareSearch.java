package fr.intech.foursquare;

import android.os.AsyncTask;
import android.os.Message;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * class allows build http query to
 */
public class FourSquareSearch {

    private static final String ID = "JQYYQV1QHVVYOBFXL4ZJMHFSINZQNAAY04JZME34BRWCCDQK";
    private static final String SECRET = "XWRCFSX3NNU2N1MY5SCNOXHAVS3ASVRLSMYMCD3UZCXPLXFV";
    private static final String CLIENT_ID = "client_id";
    private static final String CLIENT_SECRET = "client_secret";


    private static final String COORDINATES = "ll";
    private static final String VERSION = "v";

    private static final String PLACE_ID = "id";
    private static final String PLACE_NAME = "name";
    private static final String PLACE_LOCATION = "location";
    private static final String LOCATION_ADDRESS = "crossStreet";
    private static final String LOCATION_CITY = "city";
    private static final String LOCATION_STATE = "state";
    private static final String LOCATION_POSTAL_CODE = "postalCode";
    private static final String LOCATION_COUNTRY = "country";
    private static final String LOCATION_DISTANCE = "distance";

    private static final String URI = "https://api.foursquare.com/v2/venues/search?";

    private static final String LOG_TAG = "FourSquareSearch";

    private String query = "";
    private String longitude = "";
    private String latitude = "";
    private BufferedReader in;

    /**
     *
     * @param lon
     *      current position longitude
     * @param lat
     *      current position latitude
     */
    public FourSquareSearch (String lat, String lon) {
        this.latitude = lat;
        this.longitude = lon;
    }

    /**
     * query creation
     */
    public void buildQuery () {

        String date = getCurrentDate();

        this.query = URI + COORDINATES + "=" + this.latitude + "," + this.longitude
                + "&" + CLIENT_ID + "=" + ID
                + "&" + CLIENT_SECRET + "=" + SECRET
                + "&" + VERSION + "=" + date;

//        Log.d(LOG_TAG, "HTTP query builded : " +this.query);
    }

    private String getCurrentDate() {
        Date date = Calendar.getInstance().getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        return sdf.format(date);
    }

    public void readURL() {
        HttpURLConnection c = null;
        URL url = null;
        try {
            url = new URL(this.query);
            c = (HttpURLConnection) url.openConnection();
            c.connect();
            int status = c.getResponseCode();
            switch (status) {
                case 200:
                case 201:
                    this.in = new BufferedReader(
                            new InputStreamReader(url.openStream()));
            }
        } catch (MalformedURLException e) {
            e.getMessage();
        } catch (IOException e) {
            e.getMessage();
        }
    }

    public List<PointOfInterest> readJsonStream() throws IOException {
        Log.d(LOG_TAG, "enter readJsonStream() ");
        JsonReader reader = new JsonReader(this.in);
        List<PointOfInterest> messages = new ArrayList<PointOfInterest>();
        try {
            messages = readMessagesArray(reader);
        } finally {
            reader.close();
            return messages;
        }
    }

    private List<PointOfInterest> readMessagesArray(JsonReader reader) throws IOException {
        Log.d(LOG_TAG, "readMessagesArray(JsonReader reader)");
        List<PointOfInterest> messages = new ArrayList<PointOfInterest>();

        reader.beginArray();
        while (reader.hasNext()) {
            messages.add(readMessage(reader));
        }
        reader.endArray();
        return messages;
    }

    private PointOfInterest readMessage(JsonReader reader) throws IOException {
        Log.d(LOG_TAG, "readMessage(JsonReader reader)");
        String id = "-1" ;
        String name = "";
        Location location = null;

        reader.beginObject();
        while (reader.hasNext()) {
            Log.d(LOG_TAG, "readMessage(JsonReader reader) : read object");
            String item = reader.nextName();
            if (item.equals(PLACE_ID)) {
                Log.d(LOG_TAG, "object id : " + id);
                id = reader.nextString();
            } else if (item.equals(PLACE_NAME)) {
                name = reader.nextString();
                Log.d(LOG_TAG, "object name : " + name);
            } else if (item.equals(PLACE_LOCATION)) {
                location = readLocation(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new PointOfInterest(id, name, location);
    }

    private Location readLocation(JsonReader reader) throws IOException {
        Log.d(LOG_TAG, "readLocation(JsonReader reader)");
        String street = "";
        String city = "";
        String state ="";
        String postalCode ="";
        String country ="";
        int distance = -1;

        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            if (name.equals(LOCATION_ADDRESS)) {
                street = reader.nextString();
            } else if (name.equals(LOCATION_CITY)) {
                city = reader.nextString();
            }else if (name.equals(LOCATION_COUNTRY)) {
                country = reader.nextString();
            }else if (name.equals(LOCATION_DISTANCE)) {
                distance = reader.nextInt();
            }else if (name.equals(LOCATION_POSTAL_CODE)) {
                postalCode = reader.nextString();
            }else if (name.equals(LOCATION_STATE)) {
                state = reader.nextString();
            }else {
                reader.skipValue();
            }
        }
        reader.endObject();
        return new Location(street, city, state, postalCode, country, distance);
    }


    public void closeSources () {
        if (this.in != null) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public BufferedReader getIn() {
        return in;
    }

    public String getQuery() {
        return query;
    }

}
