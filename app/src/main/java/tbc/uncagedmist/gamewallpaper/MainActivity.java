package tbc.uncagedmist.gamewallpaper;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.android.play.core.tasks.Task;

import am.appwise.components.ni.NoInternetDialog;
import tbc.uncagedmist.gamewallpaper.Common.Common;

public class MainActivity extends AppCompatActivity {

    NoInternetDialog noInternetDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        checkAppUpdate();

        setContentView(R.layout.activity_main);

        noInternetDialog = new NoInternetDialog.Builder(MainActivity.this).build();

        new Handler().postDelayed(() -> {

            if (Common.isConnectedToInternet(MainActivity.this))    {
                startActivity(new Intent(MainActivity.this,HomeActivity.class));
                finish();
            }
            else    {
                Toast.makeText(MainActivity.this, "Please Check your Internet Connection...", Toast.LENGTH_SHORT).show();
            }
        },1000);
    }

    private void checkAppUpdate() {
        final AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(MainActivity.this);
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        appUpdateInfoTask.addOnSuccessListener(result -> {

            if (result.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                    result.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE))    {

                try {
                    appUpdateManager.startUpdateFlowForResult(
                            result,AppUpdateType.IMMEDIATE,
                            MainActivity.this,
                            51
                    );
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        noInternetDialog.onDestroy();
    }
}