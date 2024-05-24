package requests;

import io.restassured.response.Response;
import url.StarWarsApiUrl;

import static io.restassured.RestAssured.given;

public class GetSpecificPersonRequest {

    public static Response getSpecificPerson(String name) {

        return given()
                .queryParam("search", name)
                .when()
                .get(StarWarsApiUrl.getPeopleUrl());
    }
}
