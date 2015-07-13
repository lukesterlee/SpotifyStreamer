package lukesterlee.nanodegree.udacity.spotifystreamer;

import android.os.Parcel;
import android.os.Parcelable;

public class MyArtist implements Parcelable {

    public static final Creator<MyArtist> CREATOR = new Creator<MyArtist>() {
        @Override
        public MyArtist createFromParcel(Parcel parcel) {
            return new MyArtist(parcel);
        }

        @Override
        public MyArtist[] newArray(int size) {
            return new MyArtist[size];
        }
    };

    private String artistId;
    private String artistName;
    private String thumbnailUrl;

    public MyArtist() {
        artistId = "";
        artistName = "";
        thumbnailUrl = "";
    }

    public MyArtist(Parcel parcel) {
        artistId = parcel.readString();
        artistName = parcel.readString();
        thumbnailUrl = parcel.readString();
    }

    public MyArtist(String artistId, String artistName, String thumbnailUrl) {
        this.artistId = artistId;
        this.artistName = artistName;
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(artistId);
        parcel.writeString(artistName);
        parcel.writeString(thumbnailUrl);
    }
}
