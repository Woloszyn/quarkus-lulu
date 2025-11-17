package co.woloszyn.auth.usecase;

import jakarta.annotation.Priority;
import jakarta.inject.Inject;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

import java.security.Principal;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

@Provider
@Priority(Priorities.AUTHENTICATION)
public class JwtAuthenticationFilter implements ContainerRequestFilter {

    private static final Logger LOG = Logger.getLogger(JwtAuthenticationFilter.class);

    @Inject
    JwtUtil jwtUtil;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String path = requestContext.getUriInfo().getPath();
        if (path == null) path = "";
        String lower = path.toLowerCase(Locale.ROOT);

        if ( "OPTIONS".equals(requestContext.getMethod())) {
            return;
        }

        LOG.debugf("AuthFilter: method=%s path=%s", requestContext.getMethod(), path);

        // Split path into segments to support prefixes (e.g. api/login, auth/login)
        String[] segments = lower.split("/");
        List<String> segList = Arrays.asList(segments);

        // public endpoints (login, refresh, quarkus dev console)
        if (segList.contains("login") || segList.contains("refresh") || segList.contains("q")) {
            LOG.debugf("AuthFilter: allowing public endpoint (segment match) for path=%s", path);
            return; // allow
        }

        // Allow POST to any path whose segment is 'customers' (e.g. api/customers) to enable public registration
        if ("POST".equalsIgnoreCase(requestContext.getMethod()) && segList.contains("customers")) {
            LOG.debugf("AuthFilter: allowing POST to customers for path=%s", path);
            return;
        }

        String authHeader = requestContext.getHeaderString("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            LOG.debugf("AuthFilter: aborting - missing/invalid Authorization header for path=%s", path);
            abort(requestContext);
            return;
        }
        String token = authHeader.substring("Bearer ".length()).trim();
        String subject = jwtUtil.validateTokenAndGetSubject(token);
        if (subject == null) {
            LOG.debugf("AuthFilter: aborting - token validation failed for path=%s", path);
            abort(requestContext);
            return;
        }

        LOG.debugf("AuthFilter: token valid for subject=%s", subject);

        final SecurityContext currentSecurityContext = requestContext.getSecurityContext();
        SecurityContext sc = new SecurityContext() {
            @Override
            public Principal getUserPrincipal() {
                return () -> subject;
            }

            @Override
            public boolean isUserInRole(String role) {
                return false;
            }

            @Override
            public boolean isSecure() {
                return currentSecurityContext != null && currentSecurityContext.isSecure();
            }

            @Override
            public String getAuthenticationScheme() {
                return "Bearer";
            }
        };
        requestContext.setSecurityContext(sc);
    }

    private void abort(ContainerRequestContext requestContext) {
        // Keep response minimal - don't modify or validate CORS/request headers here.
        Response rb = Response.status(Response.Status.UNAUTHORIZED)
                .entity("Invalid or missing token")
                .build();
        requestContext.abortWith(rb);
    }
}
