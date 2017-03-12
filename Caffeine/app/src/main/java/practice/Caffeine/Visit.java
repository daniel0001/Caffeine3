package practice.Caffeine;

/**
 * Created by Daniel on 11/03/2017.
 */

public class Visit {

    private int id;
    private Long date;
    private int shopID;

    public Visit() {
    }

    public Visit(Long date, int shopID) {
        super();
        this.date = date;
        this.shopID = shopID;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }

    @Override
    public String toString() {
        return "Visit{" +
                "id=" + id +
                ", date=" + date +
                ", shopID=" + shopID +
                '}';
    }
}


