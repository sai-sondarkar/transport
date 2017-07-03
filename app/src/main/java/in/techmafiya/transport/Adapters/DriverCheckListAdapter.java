package in.techmafiya.transport.Adapters;

import android.app.Activity;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import in.techmafiya.transport.FireBaseInfo.FirebaseInfo;
import in.techmafiya.transport.Models.SingleLocalityDetails;
import in.techmafiya.transport.R;

/**
 * Created by saiso on 11-12-2016.
 */

public class DriverCheckListAdapter extends ArrayAdapter<SingleLocalityDetails> {

    private DatabaseReference mDatabase;
    public int x=1;
    private static final String TAG = "TripDetailedPanelForStreamingAdapter";
    private final Activity activity;
    List<SingleLocalityDetails> tripList = new ArrayList<SingleLocalityDetails>();
    String uid,s1;
    boolean once =true;
    List<String> userCreatedList = new ArrayList<String>();
    List<String> userSyncList = new ArrayList<String>();


    public DriverCheckListAdapter.ViewHolder holder;
    public DriverCheckListAdapter(Activity activity,
                                                List<SingleLocalityDetails> tripList) {
        super(activity, R.layout.single_item_for_stream, tripList);
        this.activity = activity;
        this.tripList = tripList;

        mDatabase = FirebaseDatabase.getInstance().getReference(FirebaseInfo.TRANSPORT);
    }



    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            view = inflater.inflate(R.layout.single_item_for_stream, null, true);
            holder = new DriverCheckListAdapter.ViewHolder();

            holder.nameTextView = (TextView) view.findViewById(R.id.stopname);
            holder.etaTextView = (TextView) view.findViewById(R.id.eta);

            view.setTag(holder);
        } else {
            holder = (DriverCheckListAdapter.ViewHolder) view.getTag();
        }

        holder.nameTextView.setText(tripList.get(position).getStopPoint());
        holder.etaTextView.setText(tripList.get(position).getEta());
        if(tripList.get(position).isPassed()){
//            holder.nameTextView.setBackgroundResource(R.color.bgBackSideColor);
            holder.nameTextView.setPaintFlags(holder.nameTextView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
        else
        {
            holder.nameTextView.setPaintFlags(holder.nameTextView.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
//            holder.nameTextView.setBackgroundResource(R.color.active);
        }

        return view;
    }

    static class ViewHolder {

        TextView nameTextView;
        TextView etaTextView;

    }
}

