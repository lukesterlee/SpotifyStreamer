package lukesterlee.nanodegree.udacity.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class TopTracksActivity extends ActionBarActivity {

    private static final String TOP_TRACKS_PARCELABLE_KEY = "top_tracks";
    private String mArtistId;
    private TopTrackAdapter mAdapter;
    private ListView mListView;
    private ArrayList<MyTrack> mTopTrackList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_toptracks);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mListView = (ListView) findViewById(R.id.listView_top_tracks);
        if (savedInstanceState == null) {
            mArtistId = getIntent().getExtras().getString(Constants.BUNDLE_ARTIST_KEY);
            new AsyncLoading().execute();
        } else {
            mTopTrackList = savedInstanceState.getParcelableArrayList(TOP_TRACKS_PARCELABLE_KEY);
            mAdapter = new TopTrackAdapter(TopTracksActivity.this, mTopTrackList);
            mListView.setAdapter(mAdapter);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpListener(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        setUpListener(false);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(TOP_TRACKS_PARCELABLE_KEY, mTopTrackList);
    }

    private void setUpListener(boolean isResumed) {
        if (!isResumed) {
            mListView.setOnItemClickListener(null);
        } else {
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long l) {
                    MyTrack selectedTrack = mAdapter.getItem(position);
                    Bundle bundle = new Bundle();
                    bundle.putString(Constants.BUNDLE_TRACK_KEY, selectedTrack.getTrack());
                    bundle.putString(Constants.BUNDLE_ALBUM_KEY, selectedTrack.getAlbum());
                    bundle.putString(Constants.BUNDLE_URL_KEY, selectedTrack.getThumbnailUrl());
                    bundle.putString(Constants.BUNDLE_ARTIST_KEY, selectedTrack.getArtist());
                    Intent intent = new Intent(TopTracksActivity.this, PlayerDialogActivity.class);
                    intent.putExtra(Constants.BUNDLE_SELECTED_TRACK_KEY, bundle);
                    startActivity(intent);
                }
            });
        }
    }

    private class AsyncLoading extends AsyncTask<Void, Void, ArrayList<MyTrack>> {
        @Override
        protected ArrayList<MyTrack> doInBackground(Void... params) {
            try {
                return new ResultGetter(mArtistId).getTopTracksList();
            } catch (JSONException e) {
                Log.e("JSON", "couldn't get Json image.");
            } catch (IOException e) {
                Log.e("JSON", "couldn't get Json string.");
            }
            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<MyTrack> topTrackList) {
            mTopTrackList = topTrackList;
            if (topTrackList != null) {
                if (topTrackList.size() == 0) {
                    Toast.makeText(TopTracksActivity.this, "There is no track found.", Toast.LENGTH_SHORT).show();
                } else {
                    mAdapter = new TopTrackAdapter(TopTracksActivity.this, mTopTrackList);
                    mListView.setAdapter(mAdapter);
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}