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

public class ArtistSearchAdapter extends BaseAdapter {

    private Context mContext;
    private List<MyArtist> artistSearchList;
    private LayoutInflater mInflater;

    private ImageView mImageView;
    private TextView mTextView;
    private MyArtist mArtist;
    private String mThumbnailUrl;

    public ArtistSearchAdapter(Context mContext, List<MyArtist> artistSearchList) {
        this.mContext = mContext;
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
        mArtist = getItem(position);
        mImageView = (ImageView) convertView.findViewById(R.id.imageView_artist);
        mTextView = (TextView) convertView.findViewById(R.id.textView_artist);
        mTextView.setText(mArtist.getArtistName());
        mThumbnailUrl = mArtist.getThumbnailUrl();
        if (mThumbnailUrl.length() == 0) {
            Picasso.with(mContext).load(R.drawable.artist).resize(200, 200).centerCrop().into(mImageView);
        } else {
            Picasso.with(mContext).load(mThumbnailUrl).resize(200, 200).centerCrop().into(mImageView);
        }
        return convertView;
    }
}