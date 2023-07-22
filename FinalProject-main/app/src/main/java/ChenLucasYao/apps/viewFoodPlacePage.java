package ChenLucasYao.apps;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextClock;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.io.File;

import io.realm.Realm;

@EActivity
public class viewFoodPlacePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_food_place_page);
    }

    @Extra
    String foodUuidString;

    @ViewById
    ImageView viewFoodPlacePageImage;

    @ViewById
    TextView viewFoodPlacePageName;

    @ViewById
    TextView viewFoodPlacePageAddress;

    @ViewById
    TextView viewFoodPlacePagePrice;

    @ViewById
    RatingBar viewFoodPlacePageRating;

    Realm food;

    @AfterViews
    public void init(){
        food = Realm.getDefaultInstance();
        FoodPlace object = food.where(FoodPlace.class).equalTo("uuid", foodUuidString).findFirst();

        viewFoodPlacePageName.setText("Food Place Name : " + object.getFoodPlaceName());
        viewFoodPlacePageAddress.setText("Address : " + object.getAddress());
        viewFoodPlacePagePrice.setText("Price : PHP" + object.getPrice());

        File getImageDir = getExternalCacheDir();
        File savedImage = new File(getImageDir, foodUuidString+".jpeg");

        if (savedImage.exists()) {
            refreshImageView(viewFoodPlacePageImage, savedImage);
        }
    }

    @Click
    public void viewFoodPlacePageBackButton(){
        finish();
    }

    @Click
    public void viewFoodPlacePageCommentsButton(){

    }

    private void refreshImageView(ImageView imageView, File savedImage) {

        // this will put the image saved to the file system to the imageview
        Picasso.get()
                .load(savedImage)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(imageView);
    }
}