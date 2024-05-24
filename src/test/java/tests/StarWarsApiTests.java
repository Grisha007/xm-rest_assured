package tests;

import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import requests.GetSpecificPersonRequest;
import requests.ValidatePeopleJsonSchemaRequest;
import url.StarWarsApiUrl;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.get;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StarWarsApiTests {

    @Test
    @Order(1)
    public void vaderAnalysisTest() {

        Response response = GetSpecificPersonRequest.getSpecificPerson("Vader");
        String filmWithLeastPlanetsTitle = get(getFilmWithLeastPlanets(response)).jsonPath().get("title");

        System.out.println("Vader's film with least planets: " + filmWithLeastPlanetsTitle);
        System.out.println("Is Vader's starship in the film: " + isStarshipInFilm(response));

        Assertions.assertEquals(200, response.getStatusCode());
    }

    @Test
    @Order(2)
    public void oldestPersonInStarWarsFilmsTest() {
        Map<String, Object> oldestPerson = getOldestPersonInStarWarsFilms();
        System.out.println("Oldest person: " + oldestPerson.get("name") + " (" + oldestPerson.get("birth_year") + ")");
    }

    @Test
    @Order(3)
    public void validatePeopleJsonSchemaTest() {
        ValidatePeopleJsonSchemaRequest.validatePeopleJsonSchemaRequest();
    }

    private Map<String, Object> getOldestPersonInStarWarsFilms() {
        String peopleUrl = StarWarsApiUrl.getPeopleUrl();
        String oldestPersonUrl = null;
        double oldestYear = Double.MAX_VALUE;

        while (peopleUrl != null) {
            Response peopleResponse = get(peopleUrl);
            List<Map<String, Object>> people = peopleResponse.jsonPath().getList("results");
            for (Map<String, Object> person : people) {
                String birthYear = (String) person.get("birth_year");
                if (birthYear != null && !birthYear.equals("unknown")) {
                    double year = parseBirthYear(birthYear);
                    if (year < oldestYear) {
                        oldestYear = year;
                        oldestPersonUrl = (String) person.get("url");
                    }
                }
            }
            peopleUrl = peopleResponse.jsonPath().getString("next");
        }

        Map<String, Object> oldestPerson = get(oldestPersonUrl).jsonPath().getMap("");
        return oldestPerson;
    }

    private boolean isStarshipInFilm(Response response) {
        List<String> starships = response.jsonPath().getList("results[0].starships");
        List<String> filmStarships = get(getFilmWithLeastPlanets(response)).jsonPath().getList("starships");
        boolean isStarshipInFilm = starships.stream().anyMatch(filmStarships::contains);

        return isStarshipInFilm;
    }

    private static String getFilmWithLeastPlanets(Response response) {
        List<String> films = response.jsonPath().getList("results[0].films");

        String filmWithLeastPlanets = films.stream()
                .min(Comparator.comparingInt(film -> get(film).jsonPath().getList("planets").size()))
                .orElseThrow();

        return filmWithLeastPlanets;
    }

    private double parseBirthYear(String birthYear) {
        if (birthYear.endsWith("BBY")) {
            return -Double.parseDouble(birthYear.replace("BBY", "").trim());
        } else if (birthYear.endsWith("ABY")) {
            return Double.parseDouble(birthYear.replace("ABY", "").trim());
        } else {
            throw new IllegalArgumentException("Unknown birth year format: " + birthYear);
        }
    }
}
