package ChenLucasYao.apps;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class FoodPlaceAdapter extends RealmRecyclerViewAdapter<FoodPlace, FoodPlaceAdapter.ViewHolder> {
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageButton delete;
        ImageButton edit;
        ImageButton image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.foodplaceRowLayoutText);
            delete = itemView.findViewById(R.id.foodplaceRowLayoutDeleteButton);
            edit = itemView.findViewById(R.id.foodplaceRowLayoutEditButton);
            image = itemView.findViewById(R.id.foodplaceRowLayoutImage);
        }
    }
    homePage activity;

    public FoodPlaceAdapter(homePage activity, @Nullable OrderedRealmCollection<FoodPlace> data, boolean autoUpdate) {
        super(data, autoUpdate);

        // THIS IS TYPICALLY THE ACTIVITY YOUR RECYCLERVIEW IS IN
        this.activity = activity;
    }
    @NonNull
    @Override
    public FoodPlaceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View v = activity.getLayoutInflater().inflate(R.layout.foodplace_row_layout, parent, false);

        FoodPlaceAdapter.ViewHolder vh = new FoodPlaceAdapter.ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(@NonNull FoodPlaceAdapter.ViewHolder holder, int position) {

        FoodPlace u = getItem(position);

        holder.name.setText(u.getFoodPlaceName());

        holder.delete.setTag(u);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {activity.delete(u);
            }
        });
        holder.edit.setTag(u);
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {activity.edit(u);
            }
        });
        holder.image.setTag(u);
        holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {activity.image(u);
            }
        });

        File getImageDir = activity.getExternalCacheDir();  // this method is in the Activity class

        // just a sample, normally you have a diff image name each time
        File file = new File(getImageDir, u.getUuid()+".jpeg");

        if (file.exists()) {
            // this will put the image saved to the file system to the imageview
            Picasso.get()
                    .load(file)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .into(holder.image);
        }
        else {
            // use a default picture
            holder.image.setImageResource(R.mipmap.ic_launcher);
        }
    }
}
