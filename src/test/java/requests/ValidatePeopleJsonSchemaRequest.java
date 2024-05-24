package requests;

import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.ValidatableResponse;
import url.StarWarsApiUrl;

import static io.restassured.RestAssured.given;

public class ValidatePeopleJsonSchemaRequest {

    public static ValidatableResponse validatePeopleJsonSchemaRequest() {

        return given()
                .when()
                .get(StarWarsApiUrl.getPeopleUrl())
                .then()
                .assertThat()
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("swapi-people-schema.json"));
    }
}
