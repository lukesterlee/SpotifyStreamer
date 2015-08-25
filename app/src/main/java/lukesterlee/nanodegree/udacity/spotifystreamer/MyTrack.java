package lukesterlee.nanodegree.udacity.spotifystreamer;


import org.parceler.Parcel;

@Parcel
public class MyTrack {

    private String track;
    private String album;
    private String artist;
    private String thumbnailUrl;
    private String preview_url;

    public MyTrack() {
    }

    public MyTrack(String track, String album, String artist, String thumbnailUrl, String preview_url) {
        this.track = track;
        this.album = album;
        this.artist = artist;
        this.thumbnailUrl = thumbnailUrl;
        this.preview_url = preview_url;
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

    public String getPreview_url() {
        return preview_url;
    }

    public void setPreview_url(String preview_url) {
        this.preview_url = preview_url;
    }

}