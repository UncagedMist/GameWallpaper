package tbc.uncagedmist.gamewallpaper.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import tbc.uncagedmist.gamewallpaper.Common.Common;
import tbc.uncagedmist.gamewallpaper.FavDB.Favourites;
import tbc.uncagedmist.gamewallpaper.Fragments.ViewWallpaperFragment;
import tbc.uncagedmist.gamewallpaper.Model.Wallpapers;
import tbc.uncagedmist.gamewallpaper.R;
import tbc.uncagedmist.gamewallpaper.ViewHolder.CategoryViewHolder;

public class MyFavAdapter extends RecyclerView.Adapter<CategoryViewHolder> {

    private Context context;
    private List<Favourites> favourites;

    private InterstitialAd mInterstitialAd;

    public MyFavAdapter(Context context, List<Favourites> favourites) {
        this.context = context;
        this.favourites = favourites;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_wallpaper_item,parent,false);

        int height = parent.getMeasuredHeight() / 2;
        itemView.setMinimumHeight(height);

        loadFullscreen();

        return new CategoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CategoryViewHolder holder, final int position) {

        if (mInterstitialAd != null) {
            mInterstitialAd.show((Activity) context);
        }
        else {
            holder.progressBar.setVisibility(View.VISIBLE);
            Picasso.get()
                    .load(favourites.get(position).getImageLink())
                    .into(holder.wallpaperImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            holder.progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

            holder.setItemClickListener((view, position1) -> {
                ViewWallpaperFragment viewWallpaperFragment = new ViewWallpaperFragment();
                FragmentTransaction transaction = ((AppCompatActivity)context)
                        .getSupportFragmentManager().beginTransaction();

                Wallpapers wallpapers = new Wallpapers();
                wallpapers.setImageLink(favourites.get(position1).getImageLink());
                Common.selected_background = wallpapers;
                Common.selected_background_key = favourites.get(position1).getKey();

                transaction.replace(R.id.main_frame,viewWallpaperFragment).commit();

            });
        }
    }

    @Override
    public int getItemCount() {
        return favourites.size();
    }

    private void loadFullscreen() {
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(
                context,
                context.getString(R.string.FULL_SCREEN),
                adRequest, new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        mInterstitialAd = interstitialAd;

                        mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback(){
                            @Override
                            public void onAdDismissedFullScreenContent() {
                                Log.d("TAG", "The ad was dismissed.");
                            }

                            @Override
                            public void onAdFailedToShowFullScreenContent(AdError adError) {
                                Log.d("TAG", "The ad failed to show.");
                            }

                            @Override
                            public void onAdShowedFullScreenContent() {
                                mInterstitialAd = null;
                                Log.d("TAG", "The ad was shown.");
                            }
                        });
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        mInterstitialAd = null;
                    }
                });
    }
}