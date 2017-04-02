package practice.Caffeine;

/**
 * Created by Daniel on 11/03/2017.
 */

public class Visit {

    private int id;
    private int visitID;
    private String date;
    private int shopID;

    public Visit() {
    }

    public Visit(int visitID, String date, int shopID) {
        super();
        this.visitID = visitID;
        this.date = date;
        this.shopID = shopID;
    }


    public int getID() {
        return id;
    }

    public void setID(int id) {
        this.id = id;
    }

    public int getVisitID() {
        return visitID;
    }

    public void setVisitID(int visitID) {
        this.visitID = visitID;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
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
                ", visitID=" + visitID +
                ", date=" + date +
                ", shopID=" + shopID +
                '}';
    }

}


