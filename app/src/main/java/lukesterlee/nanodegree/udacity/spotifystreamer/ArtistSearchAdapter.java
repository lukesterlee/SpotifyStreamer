package lukesterlee.nanodegree.udacity.spotifystreamer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Luke on 6/11/2015.
 */
public class ArtistSearchAdapter extends BaseAdapter {

    private Context mContext;
    private List<MyArtist> artistSearchList;
    private int mResourceLayout;
    private LayoutInflater mInflater;


    public ArtistSearchAdapter(Context mContext, int mResourceLayout, List<MyArtist> artistSearchList) {
        this.mContext = mContext;
        this.mResourceLayout = mResourceLayout;
        this.artistSearchList = artistSearchList;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return artistSearchList.size();
    }

    @Override
    public MyArtist getItem(int position) {
        return artistSearchList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public String getArtistId(int position) {
        return getItem(position).getArtistId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_item_artist_search, parent, false);
        }
        MyArtist artist = getItem(position);
        ImageView artistCover = (ImageView) convertView.findViewById(R.id.imageView_artist);
        TextView artistName = (TextView) convertView.findViewById(R.id.textView_artist);
        artistName.setText(artist.getArtistName());
        String thumbnailUrl = artist.getThumbnailUrl();
        if (thumbnailUrl.length() == 0) {
            Picasso.with(mContext).load(R.drawable.artist).resize(200, 200).centerCrop().into(artistCover);
        } else {
            Picasso.with(mContext).load(thumbnailUrl).resize(200, 200).centerCrop().into(artistCover);
        }
        return convertView;
    }
}