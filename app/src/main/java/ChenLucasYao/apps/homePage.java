package ChenLucasYao.apps;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.io.File;

import io.realm.Realm;

@EActivity
public class homePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
    }
    @ViewById
    TextView homepageWelcome;

    @ViewById
    ImageView homepageImage;

    Realm user;
    @AfterViews
    public void init(){
        user = Realm.getDefaultInstance();
        SharedPreferences checker = getSharedPreferences("data", MODE_PRIVATE);
        String check = checker.getString("rememberMe", "");
        String uuid = checker.getString("UUID", "");

        Table result = user.where(Table.class).equalTo("uuid", uuid).findFirst();

        if (check.isEmpty()){
            homepageWelcome.setText("Welcome " + result.getName() + "!!!");
        }
        if (check.contentEquals("hi")){
            homepageWelcome.setText("Welcome " + result.getName() + "!!! You will be Remembered!!!");
        }

        File getImageDir = getExternalCacheDir();
        File savedImage = new File(getImageDir, result.getUuid()+".jpeg");

        if (savedImage.exists()) {
            refreshImageView(homepageImage, savedImage);
        }
    }
    public void onDestroy() {
        super.onDestroy();

        if (!user.isClosed()) {
            user.close();
        }
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