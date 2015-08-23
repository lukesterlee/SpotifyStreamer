package lukesterlee.nanodegree.udacity.spotifystreamer;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

public class SearchFragment extends Fragment {

    private OnArtistSelectedListener mCallback;
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View result = inflater.inflate(R.layout.fragment_search, container, false);

        mEditText = (EditText) result.findViewById(R.id.editText_search_artist);
        mListView = (ListView) result.findViewById(R.id.listView_search_result);

        mToast = Toast.makeText(getActivity(), "There is no artist found.", Toast.LENGTH_SHORT);
        if (savedInstanceState != null) {
            mArtistSearchList = savedInstanceState.getParcelableArrayList(ARTIST_SEARCH_PARCELABLE_KEY);
            mAdapter = new ArtistSearchAdapter(getActivity(), mArtistSearchList);
            mListView.setAdapter(mAdapter);
        }

        return result;
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
                    mAdapter = new ArtistSearchAdapter(getActivity(), mArtistSearchList);
                    mListView.setAdapter(mAdapter);
                }
            }
        }
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
                    mCallback.onArtistSelected(artistId);
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpListeners(true);
    }

    @Override
    public void onPause() {
        super.onPause();
        setUpListeners(false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mCallback = (OnArtistSelectedListener) activity;
    }

    @Override
    public void onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu();
        mCallback = null;
    }
}
