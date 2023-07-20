package ChenLucasYao.apps;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;

import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity
public class addFoodPlacePage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_food_place_page);
    }

    @ViewById
    TextView addFoodPlacePageName;

    @ViewById
    TextView addFoodPlacePageAddress;

    @ViewById
    TextView addFoodPlacePagePrice;

    @ViewById
    RatingBar addFoodPlacePageRating;

    @Click
    public void addFoodPlacePageAddButton(){

    }

    @Click
    public void addFoodPlacePageCancelButton(){
        finish();
    }
}