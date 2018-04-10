package com.android.githubintegration;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.githubintegration.Adapter.RecyclerAdapter;
import com.android.githubintegration.Service.GitHubService;
import com.android.githubintegration.model.User;
import com.android.githubintegration.model.UsersData;
import com.jakewharton.rxbinding2.widget.RxTextView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.search)
    AppCompatEditText searchView;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @BindView(R.id.emptyView)
    AppCompatTextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        final GitHubService service = GitHubService.Creator.getService();

        RxTextView.textChanges(searchView).skip(2)
                .debounce(150, TimeUnit.MILLISECONDS)
                .filter(new Predicate<CharSequence>() {
                    @Override
                    public boolean test(CharSequence text) throws Exception {
                        if (text!=null && !text.toString().isEmpty()) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                })
                .switchMap(new Function<CharSequence, ObservableSource<UsersData>>() {
                    @Override
                    public ObservableSource<UsersData> apply(CharSequence queryTextEvent) throws Exception {
                        return service.getUsers(queryTextEvent.toString(),"followers").subscribeOn(Schedulers.io());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<UsersData>() {

                    @Override
                    public void onNext(UsersData usersData) {
                        if(usersData!=null && usersData.getItems().size()>0) {
                            hideEmptyView();
                            populateData(usersData);
                        }
                        else {
                            showEmptyView();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        showEmptyView();
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    void populateData(UsersData usersData){
        recyclerView.setVisibility(View.VISIBLE);
        recyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        RecyclerAdapter recyclerAdapter = new RecyclerAdapter(new ArrayList<User>(usersData.getItems()));
        recyclerView.setAdapter(recyclerAdapter);
    }

    void showEmptyView(){
        emptyView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    void hideEmptyView(){
        emptyView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }
}
