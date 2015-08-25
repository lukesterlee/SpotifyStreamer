package lukesterlee.nanodegree.udacity.spotifystreamer;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PlayerDialogActivity extends AppCompatActivity {

    @Bind(R.id.textView_artist_name) TextView mTextViewArtist;
    @Bind(R.id.textView_album_name) TextView mTextViewAlbum;
    @Bind(R.id.textView_track_name) TextView mTextViewTrack;
    @Bind(R.id.imageView_album) ImageView mImageViewThumbnail;

    private MusicService mService;
    private Intent playIntent;

    private List<MyTrack> mList;
    private int position;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        ButterKnife.bind(this);
        if (getIntent() == null) {
            mList = savedInstanceState.getParcelable(Constants.TOP_TRACKS_PARCELABLE_KEY);
            position = savedInstanceState.getInt(Constants.BUNDLE_SELECTED_TRACK_KEY);
        } else {
            mList = getIntent().getParcelableExtra(Constants.TOP_TRACKS_PARCELABLE_KEY);
            position = getIntent().getIntExtra(Constants.BUNDLE_SELECTED_TRACK_KEY, 0);
        }

        fetchData();
        loadData();
    }

    private void fetchData() {
        mArtistName = mBundle.getString(Constants.BUNDLE_ARTIST_KEY);
        mAlbumName = mBundle.getString(Constants.BUNDLE_ALBUM_KEY);
        mTrackName = mBundle.getString(Constants.BUNDLE_TRACK_KEY);
        mThumbnailUrl = mBundle.getString(Constants.BUNDLE_URL_KEY);
        mPreviewUrl = mBundle.getString(Constants.BUNDLE_PREVIEW_KEY);
    }

    private void loadData() {
        Picasso.with(getApplicationContext()).load(mThumbnailUrl).into(mImageViewThumbnail);
        mTextViewArtist.setText(mArtistName);
        mTextViewAlbum.setText(mAlbumName);
        mTextViewTrack.setText(mTrackName);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Parcelable parcelable = Parcels.wrap(mList);
        outState.putParcelable(Constants.TOP_TRACKS_PARCELABLE_KEY, parcelable);
        outState.putInt(Constants.BUNDLE_SELECTED_TRACK_KEY, position);
    }
}