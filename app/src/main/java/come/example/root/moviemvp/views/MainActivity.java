package come.example.root.moviemvp.views;import android.app.ProgressDialog;import android.os.Handler;import android.os.Parcelable;import android.os.PersistableBundle;import android.support.v4.widget.SwipeRefreshLayout;import android.support.v7.app.AppCompatActivity;import android.os.Bundle;import android.support.v7.widget.DividerItemDecoration;import android.support.v7.widget.LinearLayoutManager;import android.support.v7.widget.RecyclerView;import java.util.ArrayList;import java.util.List;import butterknife.BindView;import come.example.root.moviemvp.R;import come.example.root.moviemvp.adapter.MyAdapter;import come.example.root.moviemvp.model.Movie;import come.example.root.moviemvp.presenter.MoviePresenter;public class MainActivity extends AppCompatActivity implements MainView, SwipeRefreshLayout.OnRefreshListener{//    @BindView(R.id.main_recycerview)    RecyclerView recyclerView;//    @BindView(R.id.refresh)    SwipeRefreshLayout refreshLayout;    private MyAdapter adapter;    private List<Movie> movie;    ProgressDialog dialog;    @Override    protected void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        setContentView(R.layout.activity_main);        recyclerView = (RecyclerView)findViewById(R.id.main_recycerview);        refreshLayout = (SwipeRefreshLayout)findViewById(R.id.refresh);        refreshLayout.setOnRefreshListener(this);        refreshLayout.setColorScheme(android.R.color.holo_blue_bright,                android.R.color.holo_green_light,                android.R.color.holo_orange_light,                android.R.color.holo_red_light);        movie = new ArrayList<>();        adapter = new MyAdapter(getApplicationContext(), movie);        adapter.notifyDataSetChanged();        dialog = new ProgressDialog(getApplicationContext());        dialog.setMessage("Movie Loading ....");        dialog.setCancelable(false);        if (savedInstanceState == null){            dialog.dismiss();            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());            layoutManager.setOrientation(LinearLayoutManager.VERTICAL);            DividerItemDecoration itemDecoration = new DividerItemDecoration(getApplicationContext(), layoutManager.getOrientation());            recyclerView.setLayoutManager(layoutManager);            recyclerView.setHasFixedSize(true);            recyclerView.addItemDecoration(itemDecoration);            recyclerView.setAdapter(adapter);        } else {            List<Movie> movies;            movies = savedInstanceState.getParcelableArrayList("movie");            adapter.setMovies(movies);            recyclerView.setAdapter(adapter);        }    }    @Override    public void onSaveInstanceState(Bundle outState) {        super.onSaveInstanceState(outState);        outState.putParcelableArrayList("movie", new ArrayList<Parcelable>(adapter.getMovies()));    }    @Override    public void getMovies(List<Movie> movies) {        movie.add((Movie) movies);        adapter = new MyAdapter(getApplicationContext(), movie);        adapter.notifyDataSetChanged();        recyclerView.setAdapter(adapter);    }    @Override    public void onRefresh() {        new Handler().postDelayed(new Runnable() {            @Override            public void run() {                refreshLayout.setRefreshing(false);            }        }, 3000);    }}