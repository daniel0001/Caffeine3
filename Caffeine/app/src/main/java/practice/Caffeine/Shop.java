package practice.Caffeine;

/**
 * Created by Daniel on 06/03/2017.
 */

public class Shop {
    private int id;
    private int shopID;
    private String name;
    private String address;
    private String website;
    private String lat;
    private String lng;
    private String placeID;
    private String wifiMAC;
    private String wifiSSID;
    private String phoneNum;

    public Shop() {
    }


    public Shop(int id, int shopID, String name, String address, String website, String lat, String lng, String placeID, String wifiMac, String wifiSSID, String phoneNum) {
        super();
        this.id = id;
        this.shopID = shopID;
        this.name = name;
        this.address = address;
        this.website = website;
        this.lat = lat;
        this.lng = lng;
        this.placeID = placeID;
        this.wifiMAC = wifiMac;
        this.wifiSSID = wifiSSID;
        this.phoneNum = phoneNum;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getShopID() {
        return shopID;
    }

    public void setShopID(int shopID) {
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

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
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

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    @Override
    public String toString() {
        return "Shop{" +
                "id=" + id + '\'' +
                "shopID=" + shopID + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", website='" + website + '\'' +
                ", lat=" + lat + '\'' +
                ", lng=" + lng + '\'' +
                ", placeID='" + placeID + '\'' +
                ", wifiMAC='" + wifiMAC + '\'' +
                ", wifiSSID='" + wifiSSID + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                '}';
    }


}
