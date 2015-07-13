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
    private ImageView mImageViewThumbNail;
    private Bundle mBundle;
    private String mArtistName;
    private String mAlbumName;
    private String mTrackName;
    private String mAlbumImageUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        if (getIntent() == null) {
            mBundle = savedInstanceState;
        } else {
            mBundle = getIntent().getBundleExtra("selected_track");
        }

        initializeViews();
        fetchData();
        loadData();


    }

    private void initializeViews() {
        mTextViewArtist = (TextView) findViewById(R.id.textView_artist_name);
        mTextViewAlbum = (TextView) findViewById(R.id.textView_album_name);
        mTextViewTrack = (TextView) findViewById(R.id.textView_track_name);
        mImageViewThumbNail = (ImageView) findViewById(R.id.imageView_album);
    }

    private void fetchData() {
        mArtistName = mBundle.getString("artist");
        mAlbumName = mBundle.getString("album");
        mTrackName = mBundle.getString("track");
        mAlbumImageUrl = mBundle.getString("url");
    }


    private void loadData() {
        Picasso.with(PlayerActivity.this).load(mAlbumImageUrl).into(mImageViewThumbNail);
        mTextViewArtist.setText(mArtistName);
        mTextViewAlbum.setText(mAlbumName);
        mTextViewTrack.setText(mTrackName);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("artist", mArtistName);
        outState.putString("album", mAlbumName);
        outState.putString("track", mTrackName);
        outState.putString("url", mAlbumImageUrl);

    }
}
