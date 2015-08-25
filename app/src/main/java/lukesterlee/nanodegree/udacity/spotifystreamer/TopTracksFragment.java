package lukesterlee.nanodegree.udacity.spotifystreamer;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
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
import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TopTracksFragment extends Fragment {


    private String mArtistId;
    private TopTrackAdapter mAdapter;
    private ListView mListView;
    private List<MyTrack> mTopTrackList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View result = inflater.inflate(R.layout.fragment_toptracks, container, false);

        mListView = (ListView) result.findViewById(R.id.listView_top_tracks);


        if (savedInstanceState == null) {
            mArtistId = getArguments().getString(Constants.BUNDLE_ARTIST_KEY);
            new AsyncLoading().execute();
        } else {
            mTopTrackList = savedInstanceState.getParcelable(Constants.TOP_TRACKS_PARCELABLE_KEY);
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
                    Intent intent = new Intent(getActivity(), PlayerDialogActivity.class);
                    Parcelable topTracksParcelable = Parcels.wrap(mTopTrackList);
                    intent.putExtra(Constants.TOP_TRACKS_PARCELABLE_KEY, topTracksParcelable);
                    intent.putExtra(Constants.BUNDLE_SELECTED_TRACK_KEY, position);
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
        Parcelable topTracksParcelable = Parcels.wrap(mTopTrackList);
        outState.putParcelable(Constants.TOP_TRACKS_PARCELABLE_KEY, topTracksParcelable);
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
