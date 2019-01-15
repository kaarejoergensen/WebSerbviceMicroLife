package test.com.dtu.webserver.thornrestserver;

import org.junit.Before;
import org.junit.jupiter.api.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import static org.junit.jupiter.api.Assertions.assertEquals;


class BreweryTest {

    Client client = ClientBuilder.newClient();
    WebTarget r = client.target("http://localhost:8080/beer");

    @Test
    void getName() {
        String result = r.path("name").request().get(String.class);
        assertEquals("Knekken Ale", result);
    }

    @Test
    void setName() {
        String expected = "Indian Fail Ale";
        r.path("name").request().put(Entity.entity(expected,
                 MediaType.TEXT_PLAIN));
        assertEquals(expected,r.request().get(String.class));
    }

    @Before
    void reset() {
        r.path("reset")
                .request()
                .put(Entity.entity("", MediaType.TEXT_PLAIN));
    }
}