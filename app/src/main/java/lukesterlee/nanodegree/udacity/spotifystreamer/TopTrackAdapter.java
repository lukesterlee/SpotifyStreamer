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

public class TopTrackAdapter extends BaseAdapter {
    private Context mContext;
    private List<MyTrack> mTopTrackList;
    private int mResourceLayout;
    private LayoutInflater mInflater;

    public TopTrackAdapter(Context context, int resource, List<MyTrack> mTopTrackList) {
        this.mContext = context;
        this.mTopTrackList = mTopTrackList;
        this.mResourceLayout = resource;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mTopTrackList.size();
    }

    @Override
    public MyTrack getItem(int position) {
        return mTopTrackList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mInflater.inflate(mResourceLayout, parent, false);
        }
        MyTrack item = getItem(position);
        ImageView albumCover = (ImageView) convertView.findViewById(R.id.imageView);
        TextView track = (TextView) convertView.findViewById(R.id.textView_song);
        TextView album = (TextView) convertView.findViewById(R.id.textView_album);
        track.setText(item.getTrack());
        album.setText(item.getAlbum());
        Picasso.with(mContext).load(item.getThumbnailUrl()).resize(200, 200).centerCrop().into(albumCover);
        return convertView;
    }
}