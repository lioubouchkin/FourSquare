package fr.intech.foursquare;

import android.util.JsonReader;
import android.util.Log;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
/**
 */
public class JSonParserTest {

    static FourSquareSearch search = null;
    static BufferedReader in = null;

    @BeforeClass
    public static void setUpClass() {
        search = new FourSquareSearch("40.7", "74");
        search.buildQuery();
        search.readURL();

        in = search.getIn();
    }

    @AfterClass
    public static void tearDownClass() throws IOException {
        in.close();
    }

    @Test
    public void get_JSON_object_from_URL() throws IOException {
        String line = "";
        String result = "";

        while ((line = in.readLine()) != null) {
            result += line;
        }
        assertEquals(result, "test");
    }

    @Test
    public void parse_JSON() throws IOException {
        JsonReader reader = new JsonReader(in);
        String res = "";

        reader.beginArray();
//        while (reader.hasNext()) {
//            reader.beginObject();
//            while (reader.hasNext()) {
//                String item = reader.nextName();
//                if (item.equals("id")) {
//                    res += reader.nextString();
//                } else if (item.equals("name")) {
//                    res += reader.nextString();
//                }
//            }
//        }
        reader.endArray();

        assertEquals(res, "test");
    }
}
