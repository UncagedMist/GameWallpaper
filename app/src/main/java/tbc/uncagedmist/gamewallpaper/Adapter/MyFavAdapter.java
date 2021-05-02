package tbc.uncagedmist.gamewallpaper.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.squareup.picasso.Picasso;

import java.util.List;

import tbc.uncagedmist.gamewallpaper.Common.Common;
import tbc.uncagedmist.gamewallpaper.Database.Recents;
import tbc.uncagedmist.gamewallpaper.FavDB.Favourites;
import tbc.uncagedmist.gamewallpaper.Model.WallpaperItem;
import tbc.uncagedmist.gamewallpaper.R;
import tbc.uncagedmist.gamewallpaper.ViewHolder.ListWallpaperViewHolder;
import tbc.uncagedmist.gamewallpaper.ViewWallpaperActivity;

public class MyFavAdapter extends RecyclerView.Adapter<ListWallpaperViewHolder> {

    private Context context;
    private List<Favourites> favourites;

    private InterstitialAd mInterstitialAd;

    public MyFavAdapter(Context context, List<Favourites> favourites) {
        this.context = context;
        this.favourites = favourites;
    }

    @NonNull
    @Override
    public ListWallpaperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_wallpaper_item,parent,false);

        int height = parent.getMeasuredHeight() / 2;
        itemView.setMinimumHeight(height);

        mInterstitialAd = new InterstitialAd(context);
        mInterstitialAd.setAdUnitId(context.getResources().getString(R.string.FULL_SCREEN));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });

        return new ListWallpaperViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListWallpaperViewHolder holder, final int position) {

        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
        else {
            Picasso.get()
                    .load(favourites.get(position).getImageLink())
                    .into(holder.wallpaper);

            holder.setItemClickListener((view, position1) -> {
                Intent intent = new Intent(context, ViewWallpaperActivity.class);
                WallpaperItem wallpaperItem = new WallpaperItem();
                wallpaperItem.setCategoryId(favourites.get(position1).getCategoryId());
                wallpaperItem.setImageUrl(favourites.get(position1).getImageLink());
                Common.select_background = wallpaperItem;
                Common.select_background_key = favourites.get(position1).getKey();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return favourites.size();
    }
}