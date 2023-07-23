package ChenLucasYao.apps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

import io.realm.Realm;
import io.realm.RealmResults;

@EActivity
public class addFoodPlacePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_food_place_page);
        food = Realm.getDefaultInstance();
    }


    @ViewById
    TextView addFoodPlaceName;

    @ViewById
    TextView addFoodPlacePageAddress;

    @ViewById
    TextView addFoodPlacePagePrice;

    @ViewById
    RatingBar addFoodPlacePageRating;

    @ViewById
    ImageView addFoodPlacePageImageButton;

    Realm food;


    String foodPlaceUUID = "";

    @Click
    public void addFoodPlacePageSaveButton(){
        if (addFoodPlaceName.getText().toString().isEmpty()) {
            Toast.makeText(this, "Name Cannot Be Blank", Toast.LENGTH_LONG).show();
            return;
        }
        if (addFoodPlacePageAddress.getText().toString().isEmpty()) {
            Toast.makeText(this, "Address Cannot Be Blank", Toast.LENGTH_LONG).show();
            return;
        }
        if (addFoodPlacePagePrice.getText().toString().isEmpty()) {
            Toast.makeText(this, "Price Cannot Be Blank", Toast.LENGTH_LONG).show();
            return;
        }
        RealmResults<FoodPlace> r = food.where(FoodPlace.class).findAll();
        FoodPlace result = food.where(FoodPlace.class).equalTo("foodPlaceName", addFoodPlaceName.getText().toString()).findFirst();
        if (result != null) {
            if (addFoodPlaceName.getText().toString().equals(result.getFoodPlaceName())) {
                Toast.makeText(this, "User Already Exists", Toast.LENGTH_LONG).show();
            }
        }
        else {

            FoodPlace newItem = new FoodPlace();

            newItem.setUuid(foodPlaceUUID);
            newItem.setFoodPlaceName(addFoodPlaceName.getText().toString());
            newItem.setAddress(addFoodPlacePageAddress.getText().toString());
            newItem.setPrice(addFoodPlacePagePrice.getText().toString());

            long count = 0;

            try {
                food.beginTransaction();
                food.copyToRealmOrUpdate(newItem);
                food.commitTransaction();

                count = food.where(Table.class).count();

                Toast t = Toast.makeText(this, "New Food Place saved. Total: " + count, Toast.LENGTH_LONG);
                t.show();
                finish();
            } catch (Exception e) {
                Toast t = Toast.makeText(this, "Error saving", Toast.LENGTH_LONG);
                t.show();
                finish();
            }
        }
    }

    @Click
    public void addFoodPlacePageCancelButton(){
        finish();
    }

    public static int REQUEST_CODE_IMAGE_SCREEN = 0;
    @Click
    public void addFoodPlacePageImageButton(){
        ImageActivity_.intent(this).startForResult(REQUEST_CODE_IMAGE_SCREEN);
    }
    public void onActivityResult(int requestCode, int responseCode, Intent data)
    {
        super.onActivityResult(requestCode, responseCode, data);

        if (requestCode==REQUEST_CODE_IMAGE_SCREEN)
        {
            if (responseCode==ImageActivity.RESULT_CODE_IMAGE_TAKEN)
            {
                // receieve the raw JPEG data from ImageActivity
                // this can be saved to a file or save elsewhere like Realm or online
                byte[] jpeg = data.getByteArrayExtra("rawJpeg");

                try {
                    foodPlaceUUID = UUID.randomUUID().toString();
                    // save rawImage to file
                    File savedImage = saveFile(jpeg, foodPlaceUUID+".jpeg");

                    // load file to the image view via picasso
                    refreshImageView(addFoodPlacePageImageButton, savedImage);
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }

            }
        }
    }

    private File saveFile(byte[] jpeg, String filename) throws IOException
    {
        // this is the root directory for the images
        File getImageDir = getExternalCacheDir();

        // just a sample, normally you have a diff image name each time
        File savedImage = new File(getImageDir, filename);


        FileOutputStream fos = new FileOutputStream(savedImage);
        fos.write(jpeg);
        fos.close();
        return savedImage;
    }

    private void refreshImageView(ImageView imageView, File savedImage) {


        // this will put the image saved to the file system to the imageview
        Picasso.get()
                .load(savedImage)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .into(imageView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the Realm instance when the activity is destroyed
        if (food != null) {
            food.close();
        }
    }
}