package url;

public abstract class StarWarsApiUrl {

    private static final String BASE_URL = "https://swapi.dev/api/";
    private static final String PEOPLE = "people/";

    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static String getPeopleUrl() {
        return getBaseUrl() + PEOPLE;
    }
}
