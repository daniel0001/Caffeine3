package practice.Caffeine;

/**
 * Created by Daniel on 06/03/2017.
 */

public class Shop {
    private int shopID;
    private String name;
    private String address;
    private String website;
    private Float lat;
    private Float lng;
    private String placeID;
    private String wifiMAC;
    private String wifiSSID;

    public Shop() {
    }


    public Shop(int shopID, String name, String address, String website, Float lat, Float lng, String placeID, String wifiMac, String wifiSSID) {
        super();
        this.shopID = shopID;
        this.name = name;
        this.address = address;
        this.website = website;
        this.lat = lat;
        this.lng = lng;
        this.placeID = placeID;
        this.wifiMAC = wifiMac;
        this.wifiSSID = wifiSSID;

    }

    public int getShopID() {
        return shopID;
    }

    public void setID(int ID) {
        this.shopID = shopID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Float getLat() {
        return lat;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    public Float getLng() {
        return lng;
    }

    public void setLng(Float lng) {
        this.lng = lng;
    }

    public String getPlaceID() {
        return placeID;
    }

    public void setPlaceID(String placeID) {
        this.placeID = placeID;
    }

    public String getWifiMAC() {
        return wifiMAC;
    }

    public void setWifiMAC(String wifiMAC) {
        this.wifiMAC = wifiMAC;
    }

    public String getWifiSSID() {
        return wifiSSID;
    }

    public void setWifiSSID(String wifiSSID) {
        this.wifiSSID = wifiSSID;
    }

    @Override
    public String toString() {
        return "Shop{" +
                "shopID=" + shopID +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", website='" + website + '\'' +
                ", lat=" + lat +
                ", lng=" + lng +
                ", placeID='" + placeID + '\'' +
                ", wifiMAC='" + wifiMAC + '\'' +
                ", wifiSSID='" + wifiSSID + '\'' +
                '}';
    }

}
