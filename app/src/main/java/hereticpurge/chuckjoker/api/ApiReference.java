package hereticpurge.chuckjoker.api;

public class ApiReference {

    // Base url
    public static final String ICNDB_BASE_URL = "http://api.icndb.com";

    // Single joke url
    // http://api.icndb.com/jokes/#
    // Usage Example: http://api.icndb.com/jokes/117
    public static final String SINGLE_JOKE_URL = "/jokes/";

    // Random joke url
    // http://api.icndb.com/jokes/random
    // returns a single random joke json chosen at random
    public static final String RANDOM_JOKE_URL = "/jokes/random";

    // Total joke count url
    // http://api.icndb.com/jokes/count
    // Returns the total number of jokes in the database as a json
    public static final String ALL_JOKES_COUNT_URL = "/jokes/count";

    // Joke categories url
    // http://api.icndb.com/categories
    // returns a list of the categories of jokes as a json
    public static final String JOKE_CATEGORIES_URL = "/categories";
}
