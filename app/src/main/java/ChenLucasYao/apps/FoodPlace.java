package ChenLucasYao.apps;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class FoodPlace extends RealmObject {

    @PrimaryKey
    private String uuid;
    private String foodPlaceName;
    private String address;
    private String price;
    private String rating;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getFoodPlaceName() {
        return foodPlaceName;
    }

    public void setFoodPlaceName(String foodPlaceName) {
        this.foodPlaceName = foodPlaceName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "FoodPlace{" +
                "uuid='" + uuid + '\'' +
                ", foodPlaceName='" + foodPlaceName + '\'' +
                ", address='" + address + '\'' +
                ", price='" + price + '\'' +
                ", rating='" + rating + '\'' +
                '}';
    }
}
