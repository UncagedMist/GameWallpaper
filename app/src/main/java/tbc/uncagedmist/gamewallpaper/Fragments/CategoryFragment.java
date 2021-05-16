package tbc.uncagedmist.gamewallpaper.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import tbc.uncagedmist.gamewallpaper.Common.Common;
import tbc.uncagedmist.gamewallpaper.ListWallpaperActivity;
import tbc.uncagedmist.gamewallpaper.Model.CategoryItem;
import tbc.uncagedmist.gamewallpaper.R;
import tbc.uncagedmist.gamewallpaper.ViewHolder.CategoryViewHolder;

public class CategoryFragment extends Fragment {

    FirebaseDatabase database;
    DatabaseReference categoryBackground;

    FirebaseRecyclerOptions<CategoryItem> options;
    FirebaseRecyclerAdapter<CategoryItem, CategoryViewHolder> adapter;

    private static CategoryFragment INSTANCE = null;

    RecyclerView recyclerView;

    private InterstitialAd mInterstitialAd;

    public CategoryFragment() {
        database = FirebaseDatabase.getInstance();
        categoryBackground = database.getReference(Common.STR_CATEGORY_BACKGROUND);

        options = new FirebaseRecyclerOptions.Builder<CategoryItem>()
                .setQuery(categoryBackground,CategoryItem.class)
                .build();

        adapter = new FirebaseRecyclerAdapter<CategoryItem, CategoryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final CategoryViewHolder holder, int position, @NonNull final CategoryItem model) {

                Picasso.get()
                        .load(model.getImageLink())
                        .into(holder.background_image);

                holder.category_name.setText(model.getName());

                holder.setItemClickListener((view, position1) -> {

                    if (mInterstitialAd != null) {
                        mInterstitialAd.show(getActivity());
                    }
                    else {
                        Common.CATEGORY_ID_SELECTED = adapter.getRef(position1).getKey();
                        Common.CATEGORY_SELECTED = model.getName();
                        Common.Current_Description = model.getDesc();
                        startActivity(new Intent(getActivity(), ListWallpaperActivity.class));
                    }
                });

            }

            @NonNull
            @Override
            public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.layout_category_item,parent,false);

                AdRequest adRequest = new AdRequest.Builder().build();

                InterstitialAd.load(
                        getContext(),
                        getContext().getString(R.string.FULL_SCREEN),
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

                return new CategoryViewHolder(itemView);
            }
        };
    }

    public static CategoryFragment getInstance()    {

        if (INSTANCE == null)   {
            INSTANCE = new CategoryFragment();
        }
        return INSTANCE;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        recyclerView = view.findViewById(R.id.recycler_category);

        recyclerView.setHasFixedSize(true);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(gridLayoutManager);

        if (Common.isConnectedToInternet(getContext()))
            setCategory();
        else
            Toast.makeText(getContext(), "Please Connect to Internet...", Toast.LENGTH_SHORT).show();

        return view;
    }

    private void setCategory() {
        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (adapter != null)    {
            adapter.startListening();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (adapter != null)    {
            adapter.startListening();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (adapter != null)
            adapter.stopListening();
    }
}