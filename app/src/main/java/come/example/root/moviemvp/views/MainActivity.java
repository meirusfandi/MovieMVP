package come.example.root.moviemvp.views;import android.app.ProgressDialog;import android.os.Handler;import android.os.Parcelable;import android.os.PersistableBundle;import android.support.v4.widget.SwipeRefreshLayout;import android.support.v7.app.AppCompatActivity;import android.os.Bundle;import android.support.v7.widget.DividerItemDecoration;import android.support.v7.widget.LinearLayoutManager;import android.support.v7.widget.RecyclerView;import android.util.Log;import android.widget.Toast;import com.android.volley.Request;import com.android.volley.RequestQueue;import com.android.volley.Response;import com.android.volley.VolleyError;import com.android.volley.toolbox.JsonObjectRequest;import com.android.volley.toolbox.Volley;import com.google.gson.Gson;import org.json.JSONArray;import org.json.JSONException;import org.json.JSONObject;import java.util.ArrayList;import java.util.List;import butterknife.BindView;import come.example.root.moviemvp.R;import come.example.root.moviemvp.adapter.MyAdapter;import come.example.root.moviemvp.model.Movie;import come.example.root.moviemvp.presenter.MoviePresenter;public class MainActivity extends AppCompatActivity implements MainView, SwipeRefreshLayout.OnRefreshListener{//    @BindView(R.id.main_recycerview)    RecyclerView recyclerView;//    @BindView(R.id.refresh)    SwipeRefreshLayout refreshLayout;    private MyAdapter adapter;    private List<Movie> movies;    @Override    protected void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        setContentView(R.layout.activity_main);        recyclerView = (RecyclerView)findViewById(R.id.main_recycerview);        refreshLayout = (SwipeRefreshLayout)findViewById(R.id.refresh);        refreshLayout.setOnRefreshListener(this);        refreshLayout.setColorScheme(android.R.color.holo_blue_bright,                android.R.color.holo_green_light,                android.R.color.holo_orange_light,                android.R.color.holo_red_light);        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);        DividerItemDecoration itemDecoration = new DividerItemDecoration(getApplicationContext(), layoutManager.getOrientation());        recyclerView.setLayoutManager(layoutManager);        recyclerView.setHasFixedSize(true);        recyclerView.addItemDecoration(itemDecoration);        recyclerView.setAdapter(adapter);        movies = new ArrayList<>();        adapter = new MyAdapter(getApplicationContext(), movies);        adapter.notifyDataSetChanged();        MoviePresenter presenter = new MoviePresenter(this);        presenter.getNowPlayingMovies();        if (savedInstanceState == null){            getData();        } else {            List<Movie> movies;            movies = savedInstanceState.getParcelableArrayList("movie");            adapter.setMovies(movies);            recyclerView.setAdapter(adapter);        }    }    @Override    public void onSaveInstanceState(Bundle outState) {        super.onSaveInstanceState(outState);        outState.putParcelableArrayList("movie", new ArrayList<Parcelable>(adapter.getMovies()));    }    @Override    public void getMovies(List<Movie> movies) {        for (Movie movie: movies){            Log.i("id", String.valueOf(movie.getId()));            Log.i("title", movie.getTitle());            Log.i("release", movie.getRelease());            Log.i("overview", movie.getOverview());            Log.i("backdrop", movie.getBackdrop());            Log.i("poster", movie.getPoster());        }    }    @Override    public void onRefresh() {        new Handler().postDelayed(new Runnable() {            @Override            public void run() {                refreshLayout.setRefreshing(false);                getData();            }        }, 3000);    }    public void getData(){        final ProgressDialog dialog = new ProgressDialog(this);        dialog.setMessage("Sedang mencari Film");        dialog.show();        String API_KEY = "233b568bea8fb27357da8dd3df2fc37f";        String url = "https://api.themoviedb.org/3/movie/now_playing?api_key="+API_KEY+"&language=en-US";        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,                new Response.Listener<JSONObject>() {                    @Override                    public void onResponse(JSONObject response) {                        try {                            Gson gson = new Gson();                            JSONArray array = response.getJSONArray("results");                            for (int i=0; i<array.length(); i++){                                JSONObject object = array.getJSONObject(i);                                Movie movie = new Movie();                                movie.setId(object.getInt("id"));                                movie.setTitle(object.getString("title"));                                movie.setRelease(object.getString("release_date"));                                movie.setOverview(object.getString("overview"));                                movie.setPoster(object.getString("poster_path"));                                movie.setBackdrop(object.getString("backdrop_path"));                                movies.add(movie);                            }                            adapter = new MyAdapter(getApplicationContext(), movies);                            recyclerView.setAdapter(adapter);                        } catch (JSONException e) {                            e.printStackTrace();                            dialog.dismiss();                        }                        adapter.notifyDataSetChanged();                        dialog.dismiss();                    }                },                new Response.ErrorListener() {                    @Override                    public void onErrorResponse(VolleyError error) {                        Log.e("Error", error.toString());                        dialog.dismiss();                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();                    }                });        RequestQueue requestQueue = Volley.newRequestQueue(this);        requestQueue.add(request);    }}