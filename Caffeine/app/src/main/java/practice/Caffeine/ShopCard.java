package practice.Caffeine;

/**
 * Created by danie on 12/03/2017.
 */

public class ShopCard {
    private String name;
    private int numOfVisits;
    private int shopImage;

    public ShopCard() {
    }

    public ShopCard(String name, int numOfVisits, int shopImage) {
        this.name = name;
        this.numOfVisits = numOfVisits;
        this.shopImage = shopImage;
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

    public int getShopImage() {
        return shopImage;
    }

    public void setShopImage(int shopImage) {
        this.shopImage = shopImage;
    }

}
