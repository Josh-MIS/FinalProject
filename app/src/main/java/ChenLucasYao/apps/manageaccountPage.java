package ChenLucasYao.apps;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
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
public class manageaccountPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manageaccount_page);
    }
    @Extra
    String uuidString;

    @ViewById
    ImageView manageaccountImage;

    @ViewById
    TextView manageaccountUsername;

    @ViewById
    TextView manageaccountPassword;

    Realm user;

    @AfterViews
    public void init() {
        user = Realm.getDefaultInstance();
        Table object = user.where(Table.class).equalTo("uuid", uuidString).findFirst();

        manageaccountUsername.setText("Username : " + object.getName());
        manageaccountPassword.setText("Password : " + object.getPassword());

        File getImageDir = getExternalCacheDir();
        File savedImage = new File(getImageDir, uuidString+".jpeg");

        if (savedImage.exists()) {
            refreshImageView(manageaccountImage, savedImage);
        }
    }

    @Click
    public void manageaccountBackButton() {
        finish();
    }

    @Click
    public void manageaccountEditButton() {
        editPage_.intent(this).uuidString(uuidString).start();
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