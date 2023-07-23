package ChenLucasYao.apps;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import io.realm.Realm;
import io.realm.RealmResults;

@EActivity
public class homePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);
    }
    @ViewById
    RecyclerView homepageRecyclerView;

    Realm user;
    Realm food;
    @AfterViews
    public void init(){
        user = Realm.getDefaultInstance();
        SharedPreferences checker = getSharedPreferences("data", MODE_PRIVATE);
        String check = checker.getString("rememberMe", "");
        String uuid = checker.getString("UUID", "");

        Table result = user.where(Table.class).equalTo("uuid", uuid).findFirst();

        food = Realm.getDefaultInstance();


        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        homepageRecyclerView.setLayoutManager(mLayoutManager);

        user = Realm.getDefaultInstance();
        RealmResults<FoodPlace> list = user.where(FoodPlace.class).findAll();

        FoodPlaceAdapter adapter = new FoodPlaceAdapter(this, list, true);
        homepageRecyclerView.setAdapter(adapter);


    }
    public void onDestroy() {
        super.onDestroy();

        if (!user.isClosed()) {
            user.close();
        }
        if (!food.isClosed()) {
            food.close();
        }
    }

    public void delete(FoodPlace u)
    {
        if (u.isValid())
        {
            food.beginTransaction();
            u.deleteFromRealm();
            food.commitTransaction();
        }
    }
    public void edit(FoodPlace u)
    {
        String uuid = u.getUuid();

        editFoodPlace.intent(this).start();
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