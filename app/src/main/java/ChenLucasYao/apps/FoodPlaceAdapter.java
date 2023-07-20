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
        TextView username;
        TextView password;
        ImageButton delete;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.rowlayoutUsername);
            password = itemView.findViewById(R.id.rowlayoutPassword);
//            edit = itemView.findViewById(R.id.rowlayoutEditButton);
            delete = itemView.findViewById(R.id.rowlayoutDeleteButton);
            image = itemView.findViewById(R.id.rowlayoutImage);
        }
    }
    homePage activity;

    public FoodPlaceAdapter(homePage activity, @Nullable OrderedRealmCollection<Table> data, boolean autoUpdate) {
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

        holder.username.setText(u.getName());
        holder.password.setText(u.getPassword());

        holder.delete.setTag(u);
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {activity.delete(u);
            }
        });
//        holder.edit.setTag(u);
//        holder.edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {activity.edit(u);
//            }
//        });

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
