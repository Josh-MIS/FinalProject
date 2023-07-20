package ChenLucasYao.apps;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
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

    Realm user;
    @AfterViews
    public void init(){
        user = Realm.getDefaultInstance();
        SharedPreferences checker = getSharedPreferences("data", MODE_PRIVATE);
        String check = checker.getString("rememberMe", "");
        String uuid = checker.getString("UUID", "");

        Table result = user.where(Table.class).equalTo("uuid", uuid).findFirst();
    }
    public void onDestroy() {
        super.onDestroy();

        if (!user.isClosed()) {
            user.close();
        }
    }

    @Click
    public void homepageAddButton() {
        Intent add = new Intent(this, addFoodPlacePage_.class);
        startActivity(add);
    }

    @Click
    public void homepageManageAccountButton() {
        SharedPreferences checker = getSharedPreferences("data", MODE_PRIVATE);
        String uuid = checker.getString("UUID", "");

        manageaccountPage_.intent(this).uuidString(uuid).start();
    }

    @Click
    public void homepageLogoutButton() {
        finish();
    }
}