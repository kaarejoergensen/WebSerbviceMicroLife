package com.dtu.webserver.thornrestserver.rest;


import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;


@Path("/brewery")
public class Brewery {

    private static String name = "GAHK Brewing Brothers";

	@GET
	@Produces("text/plain")
	@Path("name")
	public String getName() {
		return name;
	}

    @PUT
    @Produces("text/plain")
    @Path("name")
    public Response setName(String input) {
        name = input;
        return Response.ok("Successfully updated name to '" + input + "'").build();
    }

    @PUT
    @Path("reset")
	public void reset(){
	    name = "GAHK Brewing Brothers";
    }
}



