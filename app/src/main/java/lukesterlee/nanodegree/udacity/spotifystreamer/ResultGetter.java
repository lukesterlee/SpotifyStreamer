package lukesterlee.nanodegree.udacity.spotifystreamer;

import org.json.JSONException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.AlbumSimple;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Pager;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;

public class ResultGetter {
    private static final String COUNTRY_KEY = "country";
    private static final String USA_CODE = "us";
    private String input;

    public ResultGetter(String input) {
        this.input = input;
    }

    public ArrayList<MyArtist> getArtistSearchList() throws JSONException, IOException {
        ArrayList<MyArtist> artistSearchList = new ArrayList<>();
        SpotifyApi api = new SpotifyApi();
        SpotifyService service = api.getService();
        ArtistsPager pager = service.searchArtists(input);
        Pager<Artist> artists = pager.artists;
        List<Artist> list = artists.items;
        if (list.size() > 30) {
            list = list.subList(0, 29);
        }
        for (Artist artist : list) {
            MyArtist newArtist = new MyArtist();
            List<Image> images = artist.images;
            if (images.size() != 0) {
                Image image = images.get(images.size() - 1);
                newArtist.setThumbnailUrl(image.url);
            }
            newArtist.setArtistName(artist.name);
            newArtist.setArtistId(artist.id);
            artistSearchList.add(newArtist);
        }
        return artistSearchList;
    }

    public ArrayList<MyTrack> getTopTracksList() throws JSONException, IOException {
        ArrayList<MyTrack> topTracksList = new ArrayList<>();
        SpotifyApi api = new SpotifyApi();
        SpotifyService service = api.getService();
        Map<String, Object> countryMap = new HashMap<>();
        countryMap.put(COUNTRY_KEY, USA_CODE);
        Tracks tracks = service.getArtistTopTrack(input, countryMap);
        List<Track> list = tracks.tracks;
        for (Track track : list) {
            MyTrack newTrack = new MyTrack();
            newTrack.setTrack(track.name);
            AlbumSimple albumSimple = track.album;
            newTrack.setAlbum(albumSimple.name);
            List<Image> images = albumSimple.images;
            Image image = images.get(0);
            newTrack.setThumbnailUrl(image.url);
            newTrack.setArtist(track.artists.get(0).name);
            topTracksList.add(newTrack);
        }
        return topTracksList;
    }
}