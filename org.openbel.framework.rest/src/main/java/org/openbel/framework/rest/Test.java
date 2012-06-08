package org.openbel.framework.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("test")
public class Test {

    // XXX not the right way to do switch result types, but it's just a test
    @GET
    @Path("text")
    @Produces({ MediaType.TEXT_PLAIN })
    public String text() {
        return "test";
    }
    
    /*
    @GET
    @Path("json")
    @Produces(MediaType.APPLICATION_JSON)
    public String json() {
        return "test";
    }
    */

}
