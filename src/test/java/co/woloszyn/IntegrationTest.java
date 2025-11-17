package co.woloszyn;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.notNullValue;

@QuarkusTest
public class IntegrationTest {

    @Test
    public void testCreateLoginRefreshAndProtected() {
        String unique = String.valueOf(System.currentTimeMillis());
        String email = "test+" + unique + "@example.com";
        String password = "MyP@ssw0rd";

        String createBody = String.format("{\"name\":\"Test User\",\"email\":\"%s\",\"password\":\"%s\"}", email, password);

        // create customer
        given()
          .contentType(ContentType.JSON)
          .body(createBody)
        .when()
          .post("/customers")
        .then()
          .statusCode(200)
          .body("id", notNullValue())
          .body("email", containsString("@example.com"));

        // login
        String loginBody = String.format("{\"username\":\"%s\",\"password\":\"%s\"}", email, password);

        String token = given()
          .contentType(ContentType.JSON)
          .body(loginBody)
        .when()
          .post("/login")
        .then()
          .statusCode(200)
          .body("token", notNullValue())
          .extract().path("token");

        String refresh = given()
          .contentType(ContentType.JSON)
          .body(loginBody)
        .when()
          .post("/login")
        .then()
          .statusCode(200)
          .body("refreshToken", notNullValue())
          .extract().path("refreshToken");

        // access protected
        given()
          .header("Authorization", "Bearer " + token)
        .when()
          .get("/protected")
        .then()
          .statusCode(200)
          .body(containsString("hello "));

        // refresh
        String refreshBody = String.format("{\"refreshToken\":\"%s\"}", refresh);
        String newToken = given()
          .contentType(ContentType.JSON)
          .body(refreshBody)
        .when()
          .post("/refresh")
        .then()
          .statusCode(200)
          .body("token", notNullValue())
          .extract().path("token");

        // access protected with new token
        given()
          .header("Authorization", "Bearer " + newToken)
        .when()
          .get("/protected")
        .then()
          .statusCode(200)
          .body(containsString("hello "));
    }
}

