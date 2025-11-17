package co.woloszyn.auth.usecase;

// CorsResponseFilter intentionally disabled. Quarkus built-in CORS handling is enabled via
// application.properties (quarkus.http.cors.*) and should be the single source of truth.
// The previous implementation added Access-Control headers and could conflict with
// Quarkus' CORS processing causing duplicate header values like:
//   Access-Control-Allow-Origin: http://localhost:8080, http://localhost:8080
// Keep this file as a reference; it's not registered as a JAX-RS provider.
public final class CorsResponseFilter {
    private CorsResponseFilter() {
        // utility class - intentionally disabled
    }
}
