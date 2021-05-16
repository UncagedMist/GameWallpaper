package tbc.uncagedmist.gamewallpaper.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

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
import tbc.uncagedmist.gamewallpaper.Database.DataSource.RecentsRepository;
import tbc.uncagedmist.gamewallpaper.Database.LocalDatabase.LocalDatabase;
import tbc.uncagedmist.gamewallpaper.Database.LocalDatabase.RecentsDataSource;
import tbc.uncagedmist.gamewallpaper.Database.Recents;
import tbc.uncagedmist.gamewallpaper.R;

@SuppressLint("ValidFragment")
public class RecentFragment extends Fragment {

    private static RecentFragment INSTANCE = null;

    RecyclerView recyclerView;

    List<Recents> recentsList;
    MyRecyclerAdapter adapter;

    Context context;

    CompositeDisposable compositeDisposable;
    RecentsRepository recentsRepository;

    @SuppressLint("ValidFragment")
    public RecentFragment(Context context) {
        this.context = context;

        compositeDisposable = new CompositeDisposable();
        LocalDatabase database = LocalDatabase.getInstance(context);
        recentsRepository = RecentsRepository.getInstance(RecentsDataSource.getInstance(database.recentDAO()));
    }

    public RecentFragment() {
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

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
        recyclerView.setLayoutManager(gridLayoutManager);

        recentsList = new ArrayList<>();

        adapter = new MyRecyclerAdapter(context,recentsList);
        recyclerView.setAdapter(adapter);

        if (Common.isConnectedToInternet(getContext()))
            loadRecents();
        else
            Toast.makeText(getContext(), "Please Connect to Internet...", Toast.LENGTH_SHORT).show();

        return view;
    }

    private void loadRecents() {
        Disposable disposable = recentsRepository.getAllRecents()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(recents ->
                        onGetAllRecentsSuccess(recents),
                        throwable ->
                        Log.d("ERROR", throwable.getMessage()));
        compositeDisposable.add(disposable);
    }

    private void onGetAllRecentsSuccess(List<Recents> recents) {
        recentsList.clear();
        recentsList.addAll(recents);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }
}