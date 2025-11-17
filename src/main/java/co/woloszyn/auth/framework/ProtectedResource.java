package co.woloszyn.auth.framework;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

@Path("protected")
public class ProtectedResource {

    @GET
    public Response hello(@Context SecurityContext sc) {
        String user = sc.getUserPrincipal() != null ? sc.getUserPrincipal().getName() : "anonymous";
        return Response.ok("hello " + user).build();
    }
}

