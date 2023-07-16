package ChenLucasYao.apps;

import android.content.Intent;
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
public class adminPage extends AppCompatActivity {

    @ViewById
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_page);
    }

    @Click
    public void adminLogoutButton(){
        finish();
    }
    Realm user;
    @AfterViews
    public void init() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(mLayoutManager);

        user = Realm.getDefaultInstance();
        RealmResults<Table> list = user.where(Table.class).findAll();

        TableAdapter adapter = new TableAdapter(this, list, true);
        recyclerView.setAdapter(adapter);
    }
    public void onDestroy()
    {
        super.onDestroy();
        if (!user.isClosed())
        {
            user.close();
        }
    }
    public void delete(Table u)
    {
        if (u.isValid())
        {
            user.beginTransaction();
            u.deleteFromRealm();
            user.commitTransaction();
        }
    }
    public void edit(Table u)
    {
        String uuid = u.getUuid();

        editPage_.intent(this).uuidString(uuid).start();
    }

    @Click
    public void adminClearButton() {
        user = Realm.getDefaultInstance();
        user.beginTransaction();
        user.deleteAll();
        user.commitTransaction();
    }
}
