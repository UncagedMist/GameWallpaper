package tbc.uncagedmist.gamewallpaper.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import tbc.uncagedmist.gamewallpaper.Adapter.MyRecyclerAdapter;
import tbc.uncagedmist.gamewallpaper.Common.Common;
import tbc.uncagedmist.gamewallpaper.R;
import tbc.uncagedmist.gamewallpaper.RecentDB.DB_Recent.RecentDataSource;
import tbc.uncagedmist.gamewallpaper.RecentDB.DB_Recent.RecentDatabase;
import tbc.uncagedmist.gamewallpaper.RecentDB.DataSource.RecentRepository;
import tbc.uncagedmist.gamewallpaper.RecentDB.Recent;

@SuppressLint("ValidFragment")
public class RecentFragment extends Fragment {

    @SuppressLint("StaticFieldLeak")
    private static RecentFragment INSTANCE = null;

    RecyclerView recyclerView;

    List<Recent> recentList;
    MyRecyclerAdapter adapter;

    Context context;

    CompositeDisposable compositeDisposable;
    RecentRepository recentRepository;

    @Override
    public void onAttach(@NonNull Activity activity) {
        context = activity;
        super.onAttach(activity);
    }

    @SuppressLint("ValidFragment")
    public RecentFragment(Context context) {
        this.context = context;

        compositeDisposable = new CompositeDisposable();
        RecentDatabase database = RecentDatabase.getInstance(context);
        recentRepository = RecentRepository.getInstance(RecentDataSource.getInstance(database.recentDAO()));
    }

    public static RecentFragment getInstance(Context context)    {

        if (INSTANCE == null)   {
            INSTANCE = new RecentFragment(context);
        }
        return INSTANCE;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recent, container, false);

        recyclerView = view.findViewById(R.id.recycler_recents);

        recyclerView.setHasFixedSize(true);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(context,2);
        recyclerView.setLayoutManager(gridLayoutManager);

        recentList = new ArrayList<>();

        adapter = new MyRecyclerAdapter(context,recentList);
        recyclerView.setAdapter(adapter);

        if (Common.isConnectedToInternet(context)) {
            loadRecent();
        }
        else
            Toast.makeText(getContext(), "Please Connect to Internet...", Toast.LENGTH_SHORT).show();

        return view;
    }

    private void loadRecent() {
        Disposable disposable = recentRepository.getAllRecent()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(recent ->
                                onGetAllRecentSuccess(recent),
                        throwable ->
                                Log.d("ERROR", throwable.getMessage()));
        compositeDisposable.add(disposable);
    }

    private void onGetAllRecentSuccess(List<Recent> recent) {
        recentList.clear();
        recentList.addAll(recent);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}