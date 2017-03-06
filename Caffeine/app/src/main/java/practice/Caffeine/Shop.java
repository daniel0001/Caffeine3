package practice.Caffeine;

/**
 * Created by Daniel on 06/03/2017.
 */

public class Shop {
    private String name;
    private int numOfVisits;
    private int thumbnail;

    public Shop() {
    }

    public Shop(String name, int numOfVisits, int thumbnail) {
        this.name = name;
        this.numOfVisits = numOfVisits;
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumOfVisits() {
        return numOfVisits;
    }

    public void setNumOfVisits(int numOfSongs) {
        this.numOfVisits = numOfSongs;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
