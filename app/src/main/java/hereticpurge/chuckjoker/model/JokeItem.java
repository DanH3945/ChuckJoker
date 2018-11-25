package hereticpurge.chuckjoker.model;


public class JokeItem {

    private int id;
    private String joke;
    private String[] categories;

    public int getId() {
        return id;
    }

    public String getJoke() {
        return joke;
    }

    public String[] getCategories() {
        return categories;
    }

    protected void setId(int id) {
        this.id = id;
    }

    protected void setJoke(String joke) {
        this.joke = joke;
    }
}
