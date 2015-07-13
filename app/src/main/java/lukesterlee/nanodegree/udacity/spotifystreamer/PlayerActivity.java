package lukesterlee.nanodegree.udacity.spotifystreamer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

public class PlayerActivity extends ActionBarActivity {
    private TextView mTextViewArtist;
    private TextView mTextViewAlbum;
    private TextView mTextViewTrack;
    private ImageView mImageViewThumbnail;
    private Bundle mBundle;
    private String mArtistName;
    private String mAlbumName;
    private String mTrackName;
    private String mThumbnailUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        if (getIntent() == null) {
            mBundle = savedInstanceState;
        } else {
            mBundle = getIntent().getBundleExtra(Constant.BUNDLE_SELECTED_TRACK_KEY);
        }
        initializeViews();
        fetchData();
        loadData();
    }

    private void initializeViews() {
        mTextViewArtist = (TextView) findViewById(R.id.textView_artist_name);
        mTextViewAlbum = (TextView) findViewById(R.id.textView_album_name);
        mTextViewTrack = (TextView) findViewById(R.id.textView_track_name);
        mImageViewThumbnail = (ImageView) findViewById(R.id.imageView_album);
    }

    private void fetchData() {
        mArtistName = mBundle.getString(Constant.BUNDLE_ARTIST_KEY);
        mAlbumName = mBundle.getString(Constant.BUNDLE_ALBUM_KEY);
        mTrackName = mBundle.getString(Constant.BUNDLE_TRACK_KEY);
        mThumbnailUrl = mBundle.getString(Constant.BUNDLE_URL_KEY);
    }

    private void loadData() {
        Picasso.with(PlayerActivity.this).load(mThumbnailUrl).into(mImageViewThumbnail);
        mTextViewArtist.setText(mArtistName);
        mTextViewAlbum.setText(mAlbumName);
        mTextViewTrack.setText(mTrackName);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(Constant.BUNDLE_ARTIST_KEY, mArtistName);
        outState.putString(Constant.BUNDLE_ALBUM_KEY, mAlbumName);
        outState.putString(Constant.BUNDLE_TRACK_KEY, mTrackName);
        outState.putString(Constant.BUNDLE_URL_KEY, mThumbnailUrl);
    }
}