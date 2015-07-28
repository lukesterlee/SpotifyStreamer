package lukesterlee.nanodegree.udacity.spotifystreamer;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PlayerDialogFragment extends DialogFragment {

    @Bind(R.id.textView_artist_name) TextView mTextViewArtist;
    @Bind(R.id.textView_album_name) TextView mTextViewAlbum;
    @Bind(R.id.textView_track_name) TextView mTextViewTrack;
    @Bind(R.id.imageView_album) ImageView mImageViewThumbnail;

    private Bundle mBundle;
    private String mArtistName;
    private String mAlbumName;
    private String mTrackName;
    private String mThumbnailUrl;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.activity_player, null);

        ButterKnife.bind(getActivity(), dialogView);

        return new AlertDialog.Builder(getActivity()).setView(dialogView).create();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        if (getIntent() == null) {
            mBundle = savedInstanceState;
        } else {
            mBundle = getIntent().getBundleExtra(Constants.BUNDLE_SELECTED_TRACK_KEY);
        }

        fetchData();
        loadData();
    }

    private void fetchData() {
        mArtistName = mBundle.getString(Constants.BUNDLE_ARTIST_KEY);
        mAlbumName = mBundle.getString(Constants.BUNDLE_ALBUM_KEY);
        mTrackName = mBundle.getString(Constants.BUNDLE_TRACK_KEY);
        mThumbnailUrl = mBundle.getString(Constants.BUNDLE_URL_KEY);
    }

    private void loadData() {
        Picasso.with(getActivity()).load(mThumbnailUrl).into(mImageViewThumbnail);
        mTextViewArtist.setText(mArtistName);
        mTextViewAlbum.setText(mAlbumName);
        mTextViewTrack.setText(mTrackName);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Constants.BUNDLE_ARTIST_KEY, mArtistName);
        outState.putString(Constants.BUNDLE_ALBUM_KEY, mAlbumName);
        outState.putString(Constants.BUNDLE_TRACK_KEY, mTrackName);
        outState.putString(Constants.BUNDLE_URL_KEY, mThumbnailUrl);
    }
}