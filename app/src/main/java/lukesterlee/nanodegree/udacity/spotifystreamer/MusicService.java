package lukesterlee.nanodegree.udacity.spotifystreamer;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ProgressBar;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

public class MusicService extends Service implements MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {

    private MediaPlayer mPlayer;
    private List<MyTrack> mList;
    private int currentPosition;
    private final IBinder musicBind = new MusicBinder();

    @Override
    public void onCreate() {
        super.onCreate();
        currentPosition = 0;
        mPlayer = new MediaPlayer();
        init();
    }

    public void init() {
        mPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnCompletionListener(this);
        mPlayer.setOnErrorListener(this);
    }

    public class MusicBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    public void setList(List<MyTrack> tracks) {
        mList = tracks;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();

        Intent intent = new Intent(this, MusicService.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);
        MyTrack track = mList.get(currentPosition);

        builder.setContentIntent(pendingIntent)
                .setTicker(track.getTrack())
                .setOngoing(true);
        Notification notification = builder.build();
        startForeground(3, notification);


    }

    @Override
    public boolean onUnbind(Intent intent) {
        mPlayer.stop();
        mPlayer.release();
        return false;
    }

    public void playSong(int position) {
        mPlayer.reset();
        currentPosition = position;
        try {
            mPlayer.setDataSource(mList.get(position).getPreview_url());
        } catch (Exception e) {
            Log.e("MUSIC SERVICE", "Error setting data source", e);
        }
        mPlayer.prepareAsync();
    }

    public void playNext(){
        currentPosition++;
        if(currentPosition >= mList.size())
            currentPosition = 0;
        playSong(currentPosition);
    }

    public void playPrevious() {
        currentPosition--;
        if (currentPosition < 0)
            currentPosition = mList.size()-1;
        playSong(currentPosition);
    }

    public void pause() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
        } else {
            mPlayer.start();
        }
    }
}
