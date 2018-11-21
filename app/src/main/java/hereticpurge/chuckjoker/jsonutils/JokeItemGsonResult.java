package hereticpurge.chuckjoker.jsonutils;

@Deprecated
public class JokeItemGsonResult {

    public String type;
    public JokeItemJsonObject value;

    private class JokeItemJsonObject {
        public String id;
        public String joke;
        public String[] categories;
    }

    public String getResultType() {
        return type;
    }

    public String getJokeId() {
        return value.id;
    }

    public String getJokeBody() {
        return value.joke;
    }

    public String[] getCategories() {
        return value.categories;
    }
}
