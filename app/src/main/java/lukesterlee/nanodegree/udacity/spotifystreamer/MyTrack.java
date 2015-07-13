package lukesterlee.nanodegree.udacity.spotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;

public class MyTrack implements Parcelable {

    public static final Creator<MyTrack> CREATOR = new Creator<MyTrack>() {
        @Override
        public MyTrack createFromParcel(Parcel parcel) {
            return new MyTrack(parcel);
        }

        @Override
        public MyTrack[] newArray(int size) {
            return new MyTrack[size];
        }
    };

    private String track;
    private String album;
    private String artist;
    private String thumbnailUrl;

    public MyTrack() {
        track = "";
        album = "";
        artist = "";
        thumbnailUrl = "";
    }

    public MyTrack(Parcel parcel) {
        track = parcel.readString();
        album = parcel.readString();
        artist = parcel.readString();
        thumbnailUrl = parcel.readString();
    }

    public MyTrack(String track, String album, String artist, String thumbnailUrl) {
        this.track = track;
        this.album = album;
        this.artist = artist;
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(track);
        parcel.writeString(album);
        parcel.writeString(artist);
        parcel.writeString(thumbnailUrl);
    }
}