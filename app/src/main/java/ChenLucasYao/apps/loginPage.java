package ChenLucasYao.apps;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import io.realm.Realm;

@EActivity
public class loginPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);
        Realm.init(this);

        SharedPreferences checker = getSharedPreferences("data", MODE_PRIVATE);
        String check = checker.getString("rememberMe", "");
        if (check.contentEquals("hi")) {
            String uuid = checker.getString("UUID", "");
            Table result = user.where(Table.class).equalTo("uuid", uuid).findFirst();

            if (result != null) {
                String username = result.getName();
                String password = result.getPassword();

                loginUsername.setText(username);
                loginPassword.setText(password);
                loginRememberMe.setChecked(true);
            }
            else {
                SharedPreferences account = getSharedPreferences("data", MODE_PRIVATE);
                SharedPreferences.Editor editor = account.edit();
                editor.remove("rememberMe");
                editor.apply();
            }
        }

    }
    @ViewById
    EditText loginUsername;

    @ViewById
    EditText loginPassword;

    @ViewById
    CheckBox loginRememberMe;

    Realm user;

    @AfterViews
    public void checkPermissions()
    {

        // REQUEST PERMISSIONS for Android 6+
        // THESE PERMISSIONS SHOULD MATCH THE ONES IN THE MANIFEST
        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA

                )

                .withListener(new BaseMultiplePermissionsListener()
                {
                    public void onPermissionsChecked(MultiplePermissionsReport report)
                    {
                        if (report.areAllPermissionsGranted())
                        {
                            // all permissions accepted proceed
                            init();
                        }
                        else
                        {
                            // notify about permissions
                            toastRequirePermissions();
                        }
                    }
                })
                .check();

    }

    public void toastRequirePermissions()
    {
        Toast.makeText(this, "You must provide permissions for app to run", Toast.LENGTH_LONG).show();
        finish();
    }
    public void init() {
        user = Realm.getDefaultInstance();
    }

    public void onDestroy() {
        super.onDestroy();

        if (!user.isClosed()) {
            user.close();
        }
    }

    @Click
    public void loginRegisterButton(){
        Intent register = new Intent(this, signupPage_.class);
        startActivity(register);
    }

    @Click
    public void loginLoginButton() {

        Table result = user.where(Table.class).equalTo("name", loginUsername.getText().toString()).findFirst();

        if (result != null) {
            if (loginUsername.getText().toString().equals("Administrator")) {
                if (loginPassword.getText().toString().equals("Password")) {
                    Intent admin = new Intent(this, adminPage_.class);
                    startActivity(admin);
                }
            }
            if (loginUsername.getText().toString().equals(result.getName())) {
                if (loginPassword.getText().toString().equals(result.getPassword())) {
                    if (loginRememberMe.isChecked()) {
                        SharedPreferences checker = getSharedPreferences("data", MODE_PRIVATE);
                        SharedPreferences.Editor editor = checker.edit();
                        editor.putString("rememberMe", "hi");
                        editor.putString("UUID", result.getUuid());
                        editor.apply();
                        Intent homepage = new Intent(this, homePage_.class);
                        startActivity(homepage);
                    } else {
                        SharedPreferences checker = getSharedPreferences("data", MODE_PRIVATE);
                        SharedPreferences.Editor editor = checker.edit();
                        editor.putString("UUID", result.getUuid());
                        editor.apply();
                        Intent homepage = new Intent(this, homePage_.class);
                        startActivity(homepage);
                    }
                } else {
                    Toast.makeText(this, "Incorrect Credentials", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Incorrect Credentials", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(this, "No User Found", Toast.LENGTH_LONG).show();
        }
    }

    @Click
    public void loginClearButton(){
        SharedPreferences account = getSharedPreferences("data", MODE_PRIVATE);
        SharedPreferences.Editor editor = account.edit();
        editor.remove("rememberMe");
        editor.remove("UUID");
        editor.apply();

        Toast.makeText(this,"Preferences Cleared", Toast.LENGTH_LONG).show();
    }
}