package tbc.uncagedmist.gamewallpaper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import tbc.uncagedmist.gamewallpaper.Common.Common;
import tbc.uncagedmist.gamewallpaper.Interface.ItemClickListener;
import tbc.uncagedmist.gamewallpaper.Model.WallpaperItem;
import tbc.uncagedmist.gamewallpaper.ViewHolder.ListWallpaperViewHolder;

public class ListWallpaperActivity extends AppCompatActivity {

    AdView aboveBanner, bottomBanner;

    RecyclerView recyclerView;

    Query query;
    FirebaseRecyclerOptions<WallpaperItem> options;
    FirebaseRecyclerAdapter<WallpaperItem, ListWallpaperViewHolder> adapter;

    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        setContentView(R.layout.activity_list_wallpaper);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(Common.CATEGORY_SELECTED);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recycler_list_wallpaper);

        aboveBanner = findViewById(R.id.aboveBanner);
        bottomBanner = findViewById(R.id.bottomBanner);

        AdRequest adRequest = new AdRequest.Builder().build();

        aboveBanner.loadAd(adRequest);
        bottomBanner.loadAd(adRequest);

        recyclerView.setHasFixedSize(true);

        GridLayoutManager layoutManager = new GridLayoutManager(this,2);
        recyclerView.setLayoutManager(layoutManager);

        loadBackgroundList();

        adMethod();
    }

    private void adMethod() {
        aboveBanner.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });

        bottomBanner.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(LoadAdError adError) {
                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdClicked() {
                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when the user is about to return
                // to the app after tapping on an ad.
            }
        });
    }

    private void loadBackgroundList() {

        query = FirebaseDatabase.getInstance().getReference(Common.STR_WALLPAPER)
                .orderByChild("categoryId").equalTo(Common.CATEGORY_ID_SELECTED);

        options = new FirebaseRecyclerOptions.Builder<WallpaperItem>()
                .setQuery(query,WallpaperItem.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<WallpaperItem, ListWallpaperViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ListWallpaperViewHolder holder, int position, @NonNull final WallpaperItem model) {

                Picasso.get()
                        .load(model.getImageUrl())
                        .into(holder.wallpaper);

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {

                        if (mInterstitialAd != null) {
                            mInterstitialAd.show((ListWallpaperActivity.this));
                        }
                        else {
                            Intent intent = new Intent(ListWallpaperActivity.this,ViewWallpaperActivity.class);
                            Common.select_background = model;
                            Common.select_background_key = adapter.getRef(position).getKey();
                            startActivity(intent);
                        }
                    }
                });

            }

            @NonNull
            @Override
            public ListWallpaperViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_wallpaper_item,parent,false);

                int height = parent.getMeasuredHeight() / 2;
                itemView.setMinimumHeight(height);

                AdRequest adRequest = new AdRequest.Builder().build();

                InterstitialAd.load(
                        ListWallpaperActivity.this,
                        getString(R.string.FULL_SCREEN),
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

                return new ListWallpaperViewHolder(itemView);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (adapter != null)    {
            adapter.startListening();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (adapter != null)    {
            adapter.startListening();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapter != null)    {
            adapter.stopListening();
        }
    }
}