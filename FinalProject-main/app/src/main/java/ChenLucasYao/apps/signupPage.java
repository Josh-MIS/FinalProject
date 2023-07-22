package ChenLucasYao.apps;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.listener.multi.BaseMultiplePermissionsListener;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
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
public class signupPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_page);
        Realm.init(this);
    }

    @ViewById
    TextView signupUsername;

    @ViewById
    TextView signupPassword;
    @ViewById
    TextView signupConfirmPassword;

    @ViewById
    ImageView signupImage;

    Realm user;

    String userUUID = "";

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
    public void signupCancelButton() {
        finish();
    }

    public static int REQUEST_CODE_IMAGE_SCREEN = 0;

    @Click
    public void signupImage()
    {
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
                    userUUID = UUID.randomUUID().toString();
                    // save rawImage to file
                    File savedImage = saveFile(jpeg, userUUID+".jpeg");

                    // load file to the image view via picasso
                    refreshImageView(signupImage, savedImage);
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

    @Click
    public void signupSaveButton() {

        if (signupUsername.getText().toString().isEmpty()) {
            Toast.makeText(this, "Name Cannot Be Blank", Toast.LENGTH_LONG).show();
            return;
        }
        if (!signupPassword.getText().toString().equals(signupConfirmPassword.getText().toString())) {
            Toast.makeText(this, "Confirm Password Does Not Match", Toast.LENGTH_LONG).show();
            return;
        }

        RealmResults<Table> r = user.where(Table.class).findAll();
        Table result = user.where(Table.class).equalTo("name", signupUsername.getText().toString()).findFirst();
        if (result != null) {
            if (signupUsername.getText().toString().equals(result.getName())) {
                Toast.makeText(this, "User Already Exists", Toast.LENGTH_LONG).show();
            }
        }
        else {

            Table newItem = new Table();

            newItem.setUuid(userUUID);
            newItem.setName(signupUsername.getText().toString());
            newItem.setPassword(signupPassword.getText().toString());

            long count = 0;

            try {
                user.beginTransaction();
                user.copyToRealmOrUpdate(newItem);
                user.commitTransaction();

                count = user.where(Table.class).count();

                Toast t = Toast.makeText(this, "New User saved. Total: " + count, Toast.LENGTH_LONG);
                t.show();
                finish();
            } catch (Exception e) {
                Toast t = Toast.makeText(this, "Error saving", Toast.LENGTH_LONG);
                t.show();
                finish();
            }
        }
    }
}