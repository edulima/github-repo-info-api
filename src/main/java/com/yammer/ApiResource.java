package com.yammer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Path("/repo/{username}")
public class ApiResource {

    @GET
    @Produces( MediaType.APPLICATION_JSON )
    public String getRepo(@PathParam("username") String username) throws IOException, URISyntaxException {
        String uri = "https://api.github.com/users/" + username + "/repos?sort=created";

        URL url = new URL(uri);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");
        connection.connect();

        BufferedReader br = new BufferedReader(
                new InputStreamReader((connection.getInputStream())));

        if (connection.getInputStream().available() > 2) {
            StringBuilder sb = new StringBuilder();
            String output;

            while ((output = br.readLine()) != null) {
                sb.append(output);
            }

            JSONArray jsonArr = new JSONArray(sb.toString());

            List<JSONObject> jsonValues = new ArrayList<JSONObject>();
            for (int i = 0; i < jsonArr.length(); i++) {
                jsonValues.add(jsonArr.getJSONObject(i));
            }

            connection.disconnect();

            return sortResponse(jsonValues);
        }
        return userNotFoundResponse(connection);
    }

    private String sortResponse(List<JSONObject> jsonValues) {
        JSONArray sortedJsonArray = new JSONArray();

        Collections.sort(jsonValues, new Comparator<JSONObject>() {
            public int compare(JSONObject objA, JSONObject objB) {
                int sizeA = 0;
                int sizeB= 0;
                try {
                    sizeA = objA.getInt("size");
                    sizeB = objB.getInt("size");
                } catch (JSONException e) {
                }
                return new Integer(sizeB).compareTo(sizeA);
            }
        });

        int counter = 0;

        while (counter < 5) {
            sortedJsonArray.put(jsonValues.get(counter));
            counter++;
        }

        return sortedJsonArray.toString();
    }

    private String userNotFoundResponse(HttpURLConnection connection) throws IOException {
        JSONObject jsonNotFoundResponse = new JSONObject();
        jsonNotFoundResponse.put("message", "No user found");
        jsonNotFoundResponse.put("code", connection.getResponseCode());
        return jsonNotFoundResponse.toString();
    }
}
