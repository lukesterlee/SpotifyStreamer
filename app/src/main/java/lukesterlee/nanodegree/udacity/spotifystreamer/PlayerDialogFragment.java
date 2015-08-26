package lukesterlee.nanodegree.udacity.spotifystreamer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PlayerDialogFragment extends DialogFragment {

    @Bind(R.id.textView_artist_name)
    TextView mTextViewArtist;
    @Bind(R.id.textView_album_name)
    TextView mTextViewAlbum;
    @Bind(R.id.textView_track_name)
    TextView mTextViewTrack;
    @Bind(R.id.imageView_album)
    ImageView mImageViewThumbnail;
    @Bind(R.id.button_pause)
    ImageButton mImageButtonPause;

    private MusicService mService;
    private Intent playIntent;
    private boolean musicBound = false;

    private List<MyTrack> mList;
    private int position;
    private boolean isPlaying;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View result = inflater.inflate(R.layout.activity_player, container, false);

        ButterKnife.bind(result);
        isPlaying = false;
        if (getIntent() == null) {
            mList = Parcels.unwrap(savedInstanceState.getParcelable(Constants.TOP_TRACKS_PARCELABLE_KEY));
            position = savedInstanceState.getInt(Constants.BUNDLE_SELECTED_TRACK_KEY);
        } else {
            mList = Parcels.unwrap(getIntent().getParcelableExtra(Constants.TOP_TRACKS_PARCELABLE_KEY));
            position = getIntent().getIntExtra(Constants.BUNDLE_SELECTED_TRACK_KEY, 0);
        }

        setData(position);


        return result;
    }

    private ServiceConnection musicConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            mService = binder.getService();
            mService.setList(mList);
            musicBound = true;
            mService.playSong(position);
            mImageButtonPause.setImageResource(R.drawable.ic_pause_black_48dp);
            isPlaying = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    private void setData(int position) {
        this.position = position;
        MyTrack track = mList.get(position);
        Picasso.with(getActivity()).load(track.getThumbnailUrl()).into(mImageViewThumbnail);
        mTextViewArtist.setText(track.getArtist());
        mTextViewAlbum.setText(track.getAlbum());
        mTextViewTrack.setText(track.getTrack());

    }

    @Override
    public void onStart() {
        super.onStart();
        if (playIntent == null) {
            playIntent = new Intent(getActivity(), MusicService.class);
            getActivity().bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
            getActivity().startService(playIntent);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Parcelable parcelable = Parcels.wrap(mList);
        outState.putParcelable(Constants.TOP_TRACKS_PARCELABLE_KEY, parcelable);
        outState.putInt(Constants.BUNDLE_SELECTED_TRACK_KEY, position);
    }

    @Override
    public void onDestroy() {
        getActivity().stopService(playIntent);
        mService = null;
        super.onDestroy();
    }

    @OnClick(R.id.button_back)
    public void onButtonBack(View view) {
        position--;
        if (position < 0)
            position = mList.size() - 1;
        setData(position);
        mService.playPrevious();
        mImageButtonPause.setImageResource(R.drawable.ic_pause_black_48dp);
        isPlaying = true;
    }

    @OnClick(R.id.button_pause)
    public void onButtonPause(View view) {
        if (isPlaying) {
            mService.pause();
            mImageButtonPause.setImageResource(R.drawable.ic_play_arrow_black_48dp);
            isPlaying = false;
        } else {
            mService.pause();
            mImageButtonPause.setImageResource(R.drawable.ic_pause_black_48dp);
            isPlaying = true;
        }
    }

    @OnClick(R.id.button_next)
    public void onButtonNext(View view) {
        position++;
        if (position >= mList.size())
            position = 0;
        setData(position);
        mService.playNext();
        mImageButtonPause.setImageResource(R.drawable.ic_pause_black_48dp);
        isPlaying = true;
    }
}
