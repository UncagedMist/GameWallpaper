package tbc.uncagedmist.gamewallpaper;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.database.annotations.NotNull;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.Icon;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import tbc.uncagedmist.gamewallpaper.Fragments.CategoryFragment;
import tbc.uncagedmist.gamewallpaper.Fragments.FavouriteFragment;
import tbc.uncagedmist.gamewallpaper.Fragments.PopularFragment;
import tbc.uncagedmist.gamewallpaper.Fragments.RecentFragment;
import tbc.uncagedmist.gamewallpaper.Fragments.SettingsFragment;
import tbc.uncagedmist.gamewallpaper.Utility.CurvedBottomNavigationView;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ReviewManager manager;
    ReviewInfo reviewInfo;

    private static final int PERMISSION_REQUEST_CODE = 31;

    CurvedBottomNavigationView curvedBottomNavigationView;

    FloatingActionButton fab;

    AdView aboveBanner, bottomBanner;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions,
                                           @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode)
        {
            case PERMISSION_REQUEST_CODE:
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)    {
                    Toast.makeText(this, "PERMISSION GRANTED..", Toast.LENGTH_SHORT).show();
                }
                else    {
                    Toast.makeText(this, "PERMISSION DENIED...", Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        manager = ReviewManagerFactory.create(HomeActivity.this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)   {
            requestPermissions(new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, PERMISSION_REQUEST_CODE);
        }

        curvedBottomNavigationView = findViewById(R.id.customBottomBar);
        fab = findViewById(R.id.fab);

        aboveBanner = findViewById(R.id.aboveBanner);
        bottomBanner = findViewById(R.id.bottomBanner);

        AdRequest adRequest = new AdRequest.Builder().build();

        aboveBanner.loadAd(adRequest);
        bottomBanner.loadAd(adRequest);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        curvedBottomNavigationView.setSelectedItemId(R.id.action_category);

        curvedBottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    Fragment fragment;

                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                        if (item.getItemId() == R.id.action_category) {
                            getSupportActionBar().setTitle(R.string.app_name);
                            fragment = new CategoryFragment();
                            fab.setImageResource(R.drawable.ic_baseline_legend_toggle_24);
                        }
                        else if (item.getItemId() == R.id.action_trending) {
                            getSupportActionBar().setTitle("Trending");
                            fragment = new PopularFragment();
                            fab.setImageResource(R.drawable.ic_baseline_stream_24);
                        }
                        else if (item.getItemId() == R.id.action_recent) {
                            getSupportActionBar().setTitle("Image History");
                            fragment = new RecentFragment(getApplicationContext());
                            fab.setImageResource(R.drawable.ic_baseline_history_edu_24);
                        }
                        else if (item.getItemId() == R.id.action_favourite) {
                            getSupportActionBar().setTitle("Favourites");
                            fragment = new FavouriteFragment(getApplicationContext());
                            fab.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                        }
                        return loadFragment(fragment);
                    }
                });

        loadFragment(CategoryFragment.getInstance());
        fab.setImageResource(R.drawable.ic_baseline_legend_toggle_24);

        adMethod();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)   {
            requestPermissions(new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            exit();
        }
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

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            CategoryFragment categoryFragment = new CategoryFragment();
            transaction.replace(R.id.main_frame,categoryFragment);
            getSupportActionBar().setTitle(R.string.app_name);
            fab.setImageResource(R.drawable.ic_baseline_legend_toggle_24);
        }
        else if (id == R.id.nav_fav)   {
            FavouriteFragment favoriteFragment = new FavouriteFragment(getApplicationContext());
            transaction.replace(R.id.main_frame,favoriteFragment);
            getSupportActionBar().setTitle("Favourites");
            fab.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        }
        else if (id == R.id.nav_settings) {
            SettingsFragment settingsFragment = new SettingsFragment();
            transaction.replace(R.id.main_frame,settingsFragment);
            getSupportActionBar().setTitle("Settings");
            fab.setImageResource(R.drawable.ic_baseline_settings_suggest_24);
        }
        else if (id == R.id.nav_share)   {
            fab.setImageResource(R.drawable.ic_baseline_share_24);
            shareApp();
        }
        else if (id == R.id.nav_feed) {
            fab.setImageResource(R.drawable.ic_baseline_feedback_24);
            feedback();
        }
        else if (id == R.id.nav_exit) {
            fab.setImageResource(R.drawable.ic_baseline_exit_to_app_24);
            exit();
        }
        transaction.addToBackStack(null);
        transaction.commit();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void feedback() {
        Task<ReviewInfo> request = manager.requestReviewFlow();

        request.addOnCompleteListener(task -> {
            if (task.isSuccessful())    {
                reviewInfo = task.getResult();

                Task<Void> flow = manager.launchReviewFlow(HomeActivity.this,reviewInfo);

                flow.addOnSuccessListener(result -> {
                });
            }
            else {
                Toast.makeText(HomeActivity.this, "ERROR...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void shareApp() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        String message = "Amazing Wallpaper in your way. Install Game Wallpaper App and Make your Display look Colourful! \n https://play.google.com/store/apps/details?id=tbc.uncagedmist.allgameswallpapers";
        intent.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(intent, "Share Game Wallpapers App Using"));
    }

    private void exit() {
        new FancyAlertDialog.Builder(HomeActivity.this)
                .setTitle("Good-Bye")
                .setBackgroundColor(Color.parseColor("#303F9F"))  //Don't pass R.color.colorvalue
                .setMessage("Do You Want to Step Out?")
                .setNegativeBtnText("Exit")
                .setPositiveBtnBackground(Color.parseColor("#FF4081"))  //Don't pass R.color.colorvalue
                .setPositiveBtnText("Rate US")
                .setNegativeBtnBackground(Color.parseColor("#FFA9A7A8"))  //Don't pass R.color.colorvalue
                .setAnimation(Animation.POP)
                .isCancellable(true)
                .setIcon(R.drawable.ic_star_border_black_24dp, Icon.Visible)
                .OnPositiveClicked(() ->
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=tbc.uncagedmist.allgameswallpapers"))))
                .OnNegativeClicked(() -> {
                    moveTaskToBack(true);
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                })
                .build();
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_frame, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}