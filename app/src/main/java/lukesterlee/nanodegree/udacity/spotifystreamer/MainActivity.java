package lukesterlee.nanodegree.udacity.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private static final String ARTIST_SEARCH_PARCELABLE_KEY = "artist_search";
    private static final int INTERVAL = 400;
    private Toast mToast;
    private EditText mEditText;
    private ListView mListView;
    private ArtistSearchAdapter mAdapter;
    private ArrayList<MyArtist> mArtistSearchList;
    private Runnable mArtistSearchRunnable = new Runnable() {
        @Override
        public void run() {
            if (mEditText.getText().length() != 0) {
                new ArtistSearchTask().execute(mEditText.getText().toString());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToast = Toast.makeText(MainActivity.this, "There is no artist found.", Toast.LENGTH_SHORT);
        initializeViews();
        if (savedInstanceState != null) {
            mArtistSearchList = savedInstanceState.getParcelableArrayList(ARTIST_SEARCH_PARCELABLE_KEY);
            mAdapter = new ArtistSearchAdapter(MainActivity.this, mArtistSearchList);
            mListView.setAdapter(mAdapter);
        }
    }

    private void initializeViews() {
        mEditText = (EditText) findViewById(R.id.editText_search_artist);
        mListView = (ListView) findViewById(R.id.listView_search_result);
    }

    private void setUpListeners(boolean isResumed) {
        if (!isResumed) {
            mEditText.addTextChangedListener(null);
            mListView.setOnItemClickListener(null);
        } else {
            mEditText.addTextChangedListener(new ArtistSearchWatcher());
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String artistId = mAdapter.getArtistId(position);
                    Intent intent = new Intent(MainActivity.this, TopTracksActivity.class);
                    intent.putExtra(Constant.BUNDLE_ARTIST_KEY, artistId);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpListeners(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        setUpListeners(false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(ARTIST_SEARCH_PARCELABLE_KEY, mArtistSearchList);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class ArtistSearchTask extends AsyncTask<String, Void, ArrayList<MyArtist>> {

        @Override
        protected ArrayList<MyArtist> doInBackground(String... params) {
            try {
                return new ResultGetter(params[0]).getArtistSearchList();
            } catch (JSONException e) {
                Log.e("JSON", "couldn't get Json image.");
            } catch (IOException e) {
                Log.e("JSON", "couldn't get Json string.");
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<MyArtist> artistSearchList) {
            mArtistSearchList = artistSearchList;
            if (artistSearchList != null) {
                if (artistSearchList.size() == 0) {
                    mToast.show();
                } else {
                    mToast.cancel();
                    mAdapter = new ArtistSearchAdapter(MainActivity.this, mArtistSearchList);
                    mListView.setAdapter(mAdapter);
                }
            }
        }
    }

    private class ArtistSearchWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        }
        @Override
        public void onTextChanged(CharSequence charSequence, int position, int i2, int i3) {
            if (charSequence.length() != 0) {
                Handler handler = new Handler();
                handler.removeCallbacks(mArtistSearchRunnable);
                handler.postDelayed(mArtistSearchRunnable, INTERVAL);
            }
        }
        @Override
        public void afterTextChanged(Editable editable) {
        }
    }
}