package com.yammer;

import org.json.JSONObject;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.net.URISyntaxException;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class DefaultResource {

    @Path("/{default: .*}")
    @GET
    public String defaultMethod() throws URISyntaxException {
        JSONObject jsonNotFoundResponse = new JSONObject();
        jsonNotFoundResponse.put("message", "URL not found");
        jsonNotFoundResponse.put("code", "404");
        return jsonNotFoundResponse.toString();
    }
}
