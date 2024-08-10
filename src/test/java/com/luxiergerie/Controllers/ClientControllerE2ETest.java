package com.luxiergerie.Controllers;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ClientControllerE2ETest {

    @LocalServerPort
    private int port = 8090;

    private String token;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
        token = obtainAccessToken("12345678", "password");
    }

    private String obtainAccessToken(String serialNumber, String password) {
        String authUrl = "/api/auth/login";

        // Effectue une requête pour obtenir le token
        Response response = given()
                .contentType(ContentType.JSON)
                .body("{\"serialNumber\":\"" + serialNumber + "\", \"password\":\"" + password + "\"}")
                .when()
                .post(authUrl)
                .andReturn();

        // Vérifie que le statut est 200 OK
        response.then().statusCode(200);

        // Extraire le token de la réponse
        return response.cookie("jwt-token");
    }

    @Test
    public void testCreateAndGetClient() {
        // Obtenez le nombre initial de clients
        int initialSize = given()
                .port(port)
                .auth().oauth2(token)
                .when()
                .get("/api/client")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath().getList("").size();

        // Prépare les données pour la création d'un client
        String clientPayload = "{\n" +
                "    \"firstName\": \"Jane\",\n" +
                "    \"lastName\": \"Doe\",\n" +
                "    \"phoneNumber\": \"0987654321\",\n" +
                "    \"email\": \"janedoe@example.com\",\n" +
                "    \"room\": null,\n" +
                "    \"sojourns\": null\n" +
                "}";

        // Crée un nouveau client
        given()
                .port(port)
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(clientPayload)
                .when()
                .post("/api/client")
                .then()
                .statusCode(201)
                .body("id", notNullValue());

        // Récupèrer le client créé
        String clientId = given()
                .port(port)
                .auth().oauth2(token)
                .when()
                .get("/api/client")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath().getString("id[0]");
    }

    @Test
    public void testUpdateClient() {
        // Prépare les données pour la création d'un client
        String clientPayload = "{\n" +
                "    \"firstName\": \"Jane\",\n" +
                "    \"lastName\": \"Doe\",\n" +
                "    \"phoneNumber\": \"0987654321\",\n" +
                "    \"email\": \"janedoe@example.com\",\n" +
                "    \"room\": null,\n" +
                "    \"sojourns\": null\n" +
                "}";

        // Crée un nouveau client
        String clientId = given()
                .port(port)
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(clientPayload)
                .when()
                .post("/api/client")
                .then()
                .statusCode(201)
                .extract().jsonPath().getString("id");

        // Prépare les données pour la mise à jour
        String updatePayload = "{\n" +
                "    \"firstName\": \"Janet\",\n" +
                "    \"lastName\": \"Doe\",\n" +
                "    \"phoneNumber\": \"0987654321\",\n" +
                "    \"email\": \"janedoeupdated@example.com\",\n" +
                "    \"room\": null,\n" +
                "    \"sojourns\": null\n" +
                "}";

        // Met à jour le client existant
        given()
                .port(port)
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(updatePayload)
                .when()
                .put("/api/client/" + clientId)
                .then()
                .statusCode(200);

        // Vérifie que la mise à jour a réussi
        given()
                .port(port)
                .auth().oauth2(token)
                .when()
                .get("/api/client/" + clientId)
                .then()
                .statusCode(200)
                .body("firstName", equalTo("Janet"))
                .body("email", equalTo("janedoeupdated@example.com"));
    }

    @Test
    public void testDeleteClient() {
        // Prépare les données pour la création d'un client
        String clientPayload = "{\n" +
                "    \"firstName\": \"Jane\",\n" +
                "    \"lastName\": \"Doe\",\n" +
                "    \"phoneNumber\": \"0987654321\",\n" +
                "    \"email\": \"janedoe@example.com\",\n" +
                "    \"room\": null,\n" +
                "    \"sojourns\": null\n" +
                "}";

        // Crée un nouveau client
        String clientId = given()
                .port(port)
                .auth().oauth2(token)
                .contentType(ContentType.JSON)
                .body(clientPayload)
                .when()
                .post("/api/client")
                .then()
                .statusCode(201)
                .extract().jsonPath().getString("id");

        // Supprime le client créé
        given()
                .port(port)
                .auth().oauth2(token)
                .when()
                .delete("/api/client/" + clientId)
                .then()
                .statusCode(204);

        // Vérifie que le client n'existe plus
        given()
                .port(port)
                .auth().oauth2(token)
                .when()
                .get("/api/client/" + clientId)
                .then()
                .statusCode(404);
    }

}