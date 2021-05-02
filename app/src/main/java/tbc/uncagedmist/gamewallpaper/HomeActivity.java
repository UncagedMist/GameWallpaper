package tbc.uncagedmist.gamewallpaper;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.Icon;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import java.util.Arrays;

import am.appwise.components.ni.NoInternetDialog;
import tbc.uncagedmist.gamewallpaper.Fragments.CategoryFragment;
import tbc.uncagedmist.gamewallpaper.Fragments.FavouriteFragment;
import tbc.uncagedmist.gamewallpaper.Fragments.PopularFragment;
import tbc.uncagedmist.gamewallpaper.Fragments.RecentFragment;
import tbc.uncagedmist.gamewallpaper.Fragments.SettingsFragment;
import tbc.uncagedmist.gamewallpaper.SlideRoot.DrawerAdapter;
import tbc.uncagedmist.gamewallpaper.SlideRoot.DrawerItem;
import tbc.uncagedmist.gamewallpaper.SlideRoot.SimpleItem;
import tbc.uncagedmist.gamewallpaper.SlideRoot.SpaceItem;
import tbc.uncagedmist.gamewallpaper.Utility.CurvedBottomNavigationView;

public class HomeActivity extends AppCompatActivity implements DrawerAdapter.OnItemSelectedListener {

    ReviewManager manager;
    ReviewInfo reviewInfo;

    NoInternetDialog noInternetDialog;

    private static final int PERMISSION_REQUEST_CODE = 51;

    private static final int POS_HOME = 0;
    private static final int POS_FAVOURITE = 1;
    private static final int POS_SETTINGS = 2;
    private static final int POS_SHARE = 3;
    private static final int POS_FEED = 4;
    public static final int POS_EXIT = 6;

    private String[] screenTitles;
    private Drawable[] screenIcons;

    private SlidingRootNav slidingRootNav;

    FloatingActionButton fab;

    CurvedBottomNavigationView navigationView;

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
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
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        setContentView(R.layout.activity_home);

        noInternetDialog = new NoInternetDialog.Builder(HomeActivity.this).build();
        manager = ReviewManagerFactory.create(HomeActivity.this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fab = findViewById(R.id.fab);
        navigationView = findViewById(R.id.customBottomBar);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)   {
            requestPermissions(new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, PERMISSION_REQUEST_CODE);
        }

        slidingRootNav = new SlidingRootNavBuilder(this)
                .withToolbarMenuToggle(toolbar)
                .withMenuOpened(false)
                .withContentClickableWhenMenuOpened(false)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();

        screenIcons = loadScreenIcons();
        screenTitles = loadScreenTitles();

        DrawerAdapter adapter = new DrawerAdapter(Arrays.asList(
                createItemFor(POS_HOME).setChecked(true),
                createItemFor(POS_FAVOURITE),
                createItemFor(POS_SETTINGS),
                createItemFor(POS_SHARE),
                createItemFor(POS_FEED),
                new SpaceItem(48),
                createItemFor(POS_EXIT)));
        adapter.setListener(this);

        RecyclerView list = findViewById(R.id.list);
        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        adapter.setSelected(POS_HOME);

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            Fragment fragment;

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.action_category) {
                    getSupportActionBar().setTitle(R.string.app_name);
                    fragment = new CategoryFragment();
                    fab.setImageResource(R.drawable.ic_baseline_category_24);
                }
                else if (menuItem.getItemId() == R.id.action_trending) {
                    getSupportActionBar().setTitle("Trending");
                    fragment = new PopularFragment();
                    fab.setImageResource(R.drawable.ic_baseline_stream_24);
                }
                else if (menuItem.getItemId() == R.id.action_recent) {
                    getSupportActionBar().setTitle("Image History");
                    fragment = new RecentFragment(getApplicationContext());
                    fab.setImageResource(R.drawable.ic_baseline_history_edu_24);
                }
                else if (menuItem.getItemId() == R.id.action_favourite) {
                    getSupportActionBar().setTitle("Favourites");
                    fragment = new FavouriteFragment(getApplicationContext());
                    fab.setImageResource(R.drawable.ic_baseline_favorite_border_24);
                }
                return loadFragment(fragment);
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)   {
            requestPermissions(new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onItemSelected(int position) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (position == POS_HOME)   {
            CategoryFragment categoryFragment = new CategoryFragment();
            transaction.replace(R.id.main_frame,categoryFragment);
            getSupportActionBar().setTitle(R.string.app_name);
            fab.setImageResource(R.drawable.ic_baseline_category_24);
        }
        else if (position == POS_FAVOURITE) {
            FavouriteFragment favoriteFragment = new FavouriteFragment(getApplicationContext());
            transaction.replace(R.id.main_frame,favoriteFragment);
            getSupportActionBar().setTitle("Favourites");
            fab.setImageResource(R.drawable.ic_baseline_favorite_border_24);
        }
        else if (position == POS_SETTINGS) {
            SettingsFragment settingsFragment = new SettingsFragment();
            transaction.replace(R.id.main_frame,settingsFragment);
            getSupportActionBar().setTitle("Settings");
            fab.setImageResource(R.drawable.ic_baseline_settings_suggest_24);
        }
        else if (position == POS_SHARE) {
            fab.setImageResource(R.drawable.ic_baseline_share_24);
            shareApp();
        }
        else if (position == POS_FEED) {
            fab.setImageResource(R.drawable.ic_baseline_feedback_24);
            feedback();
        }
        else if (position == POS_EXIT) {
            fab.setImageResource(R.drawable.ic_baseline_exit_to_app_24);
            exit();
        }
        slidingRootNav.closeMenu();
        transaction.addToBackStack(null);
        transaction.commit();
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
        String message = "Amazing Wallpaper in your way. Install Game Wallpaper App and Make your Display look Colourful! \n https://play.google.com/store/apps/details?id=tbc.uncagedmist.gamewallpaper";
        intent.putExtra(Intent.EXTRA_TEXT, message);
        startActivity(Intent.createChooser(intent, "Share Game Wallpaper App Using"));
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
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=tbc.uncagedmist.gamewallpaper"))))
                .OnNegativeClicked(() -> {
                    moveTaskToBack(true);
                    android.os.Process.killProcess(android.os.Process.myPid());
                    System.exit(1);
                })
                .build();
    }


    @SuppressWarnings("rawtypes")
    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenIcons[position], screenTitles[position])
                .withIconTint(color(R.color.white))
                .withTextTint(color(R.color.white))
                .withSelectedIconTint(color(R.color.teal_200))
                .withSelectedTextTint(color(R.color.teal_200));
    }

    private String[] loadScreenTitles() {
        return getResources().getStringArray(R.array.ld_activityScreenTitles);
    }

    private Drawable[] loadScreenIcons() {
        TypedArray ta = getResources().obtainTypedArray(R.array.ld_activityScreenIcons);
        Drawable[] icons = new Drawable[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            int id = ta.getResourceId(i, 0);
            if (id != 0) {
                icons[i] = ContextCompat.getDrawable(this, id);
            }
        }
        ta.recycle();
        return icons;
    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.main_frame, fragment);
            transaction.addToBackStack(null);
            transaction.commit();
            return true;
        }
        return false;
    }


    @Override
    public void onBackPressed() {
        exit();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }
}