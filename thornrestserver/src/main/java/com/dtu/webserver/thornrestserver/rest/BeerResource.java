package com.dtu.webserver.thornrestserver.rest;

import com.dtu.webserver.thornrestserver.Beer;

import javax.print.attribute.standard.Media;
import javax.ws.rs.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/beer")
public class BeerResource {

    Client c = ClientBuilder.newClient();
    WebTarget r = c.target("http://localhost:8080");

    private List<Beer> beerStorage = new ArrayList<Beer>();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    @Path("register")
    public void registerNewBeer(Beer b) {
        Beer nb = new Beer();
        nb.id = b.id;
        nb.name = b.name;
        beerStorage.add(nb);
        System.out.println("Successfully added beer: "  + b.name + " (id: " + b.id + ")");
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("{id}")
    public Beer getBeer(@PathParam("id") String id){
        return r.path("beer").path(id).request()
                .get(Beer.class);
    }

    @GET
    @Produces("text/plain")
    @Path("getAll")
    public String getAllBeers(){
        StringBuilder sb = new StringBuilder();
        for(Beer b : beerStorage){
            sb.append(b.name + " ");
        }
        System.out.println(sb.toString());
        return "Successfully fetched beer! Here it is:\n" + r.path("beer").toString();
    }



}
