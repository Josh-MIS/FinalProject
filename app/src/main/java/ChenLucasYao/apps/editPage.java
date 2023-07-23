package ChenLucasYao.apps;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import io.realm.Realm;

@EActivity
public class editPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_page);
    }
    @ViewById
    TextView editUsername;

    @ViewById
    TextView editPassword;

    @ViewById
    TextView editConfirmPassword;

    @Extra
    String uuidString;

    @ViewById
    ImageView editImage;

    Realm user;

    @AfterViews
    public void init() {
        user = Realm.getDefaultInstance();
        Table object = user.where(Table.class).equalTo("uuid", uuidString).findFirst();

            String username = object.getName();
            String password = object.getPassword();

            editUsername.setText(username);
            editPassword.setText(password);
            editConfirmPassword.setText(password);

        File getImageDir = getExternalCacheDir();
        File savedImage = new File(getImageDir, uuidString+".jpeg");

        if (savedImage.exists()) {
            refreshImageView(editImage, savedImage);
        }

    }

    @Click
    public void editCancelButton() {
        manageaccountPage_.intent(this).uuidString(uuidString).start();
        finish();
    }

    @Click
    public void editImage()
    {
        ImageActivity_.intent(this).startForResult(REQUEST_CODE_IMAGE_SCREEN);
    }

    @Click
    public void editSaveButton() {
        if (editUsername.getText().toString().isEmpty()) {
            Toast.makeText(this, "Name Cannot Be Blank", Toast.LENGTH_LONG).show();
            return;
        }
        if (!editPassword.getText().toString().equals(editConfirmPassword.getText().toString())) {
            Toast.makeText(this, "Confirm Password Does Not Match", Toast.LENGTH_LONG).show();
            return;
        }

        user = Realm.getDefaultInstance();
        Table changeUser = user.where(Table.class).equalTo("uuid", uuidString).findFirst();
        user.beginTransaction();
        changeUser.setName(editUsername.getText().toString());
        changeUser.setPassword(editPassword.getText().toString());
        user.commitTransaction();

        Toast t = Toast.makeText(this, "Edited", Toast.LENGTH_LONG);
        t.show();
        manageaccountPage_.intent(this).uuidString(uuidString).start();
        finish();
    }

    public void onDestroy() {
        super.onDestroy();

        if (!user.isClosed()) {
            user.close();
        }
    }

    public static int REQUEST_CODE_IMAGE_SCREEN = 0;

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
                    // save rawImage to file
                    File savedImage = saveFile(jpeg,uuidString+".jpeg");

                    // load file to the image view via picasso
                    refreshImageView(editImage, savedImage);
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

}