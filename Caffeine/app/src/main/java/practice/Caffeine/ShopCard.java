package practice.Caffeine;

/**
 * Created by danie on 12/03/2017.
 */

public class ShopCard {
    private String name;
    private int numPoints;
    private int shopImage;
    private String shopAddress;
    private String shopPhone;
    private Double lat;
    private Double lng;
    private int shopID;

    public ShopCard() {
    }

    public ShopCard(String name, int numPoints, int shopImage, String shopAddress, String shopPhone, Double lat, Double lng, int shopID) {
        this.name = name;
        this.numPoints = numPoints;
        this.shopImage = shopImage;
        this.shopAddress = shopAddress;
        this.shopPhone = shopPhone;
        this.lat = lat;
        this.lng = lng;
        this.shopID = shopID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumPoints() {
        return numPoints;
    }

    public void setNumPoints(int numOfSongs) {
        this.numPoints = numOfSongs;
    }

    public int getShopImage() {
        return shopImage;
    }

    public void setShopImage(int shopImage) {
        this.shopImage = shopImage;
    }

    public String getShopAddress() {
        return shopAddress;
    }

    public void setShopAddress(String shopAddress) {
        this.shopAddress = shopAddress;
    }

    public String getShopPhone() {
        return shopPhone;
    }

    public void setShopPhone(String shopPhone) {
        this.shopPhone = shopPhone;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
        this.shopID = shopID;
    }
}
