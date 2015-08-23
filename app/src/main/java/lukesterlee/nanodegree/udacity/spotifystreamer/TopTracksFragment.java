package lukesterlee.nanodegree.udacity.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class TopTracksFragment extends Fragment {

    private static final String TOP_TRACKS_PARCELABLE_KEY = "top_tracks";
    private String mArtistId;
    private TopTrackAdapter mAdapter;
    private ListView mListView;
    private ArrayList<MyTrack> mTopTrackList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View result = inflater.inflate(R.layout.fragment_toptracks, container, false);

        mListView = (ListView) result.findViewById(R.id.listView_top_tracks);


        if (savedInstanceState == null) {
            mArtistId = getArguments().getString(Constants.BUNDLE_ARTIST_KEY);
            new AsyncLoading().execute();
        } else {
            mTopTrackList = savedInstanceState.getParcelableArrayList(TOP_TRACKS_PARCELABLE_KEY);
            mAdapter = new TopTrackAdapter(getActivity(), mTopTrackList);
            mListView.setAdapter(mAdapter);
        }
        return result;
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpListener(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        setUpListener(false);
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
                    Intent intent = new Intent(getActivity(), PlayerDialogFragment.class);
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
                    Toast.makeText(getActivity(), "There is no track found.", Toast.LENGTH_SHORT).show();
                } else {
                    mAdapter = new TopTrackAdapter(getActivity(), mTopTrackList);
                    mListView.setAdapter(mAdapter);
                }
            }
        }
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(TOP_TRACKS_PARCELABLE_KEY, mTopTrackList);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                getActivity().onBackPressed();
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}
