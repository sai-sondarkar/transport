package in.techmafiya.transport.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import in.techmafiya.transport.Activities.TripDetailedPanelForStream;
import in.techmafiya.transport.FireBaseInfo.FirebaseInfo;
import in.techmafiya.transport.Models.SingleLocalityDetails;
import in.techmafiya.transport.Models.StopModelForUser;
import in.techmafiya.transport.Models.TripFullDetails;
import in.techmafiya.transport.R;

/**
 * Created by saiso on 09-12-2016.
 */

public class ScheduledAdapter extends ArrayAdapter<StopModelForUser> {

    private DatabaseReference mDatabase;
    public int x=1;
    private static final String TAG = "TripDetailedPanelForStreamingAdapter";
    private final Activity activity;
    List<StopModelForUser> tripList = new ArrayList<StopModelForUser>();
    List<String> Keys = new ArrayList<String>();


    public ScheduledAdapter.ViewHolder holder;
    public ScheduledAdapter(Activity activity,
                                                List<StopModelForUser> tripList, List<String> Keys) {
        super(activity, R.layout.item_scheduled, tripList);
        this.activity = activity;
        this.tripList = tripList;
        this.Keys = Keys;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            view = inflater.inflate(R.layout.item_scheduled, null, true);
            holder = new ScheduledAdapter.ViewHolder();

            holder.relativeLayout =  (RelativeLayout) view.findViewById(R.id.r1);
            holder.nameTextView = (TextView) view.findViewById(R.id.stopname);
            holder.etaTextView = (TextView) view.findViewById(R.id.eta);
            holder.uidNameTextView = (TextView) view.findViewById(R.id.xyz);


            view.setTag(holder);
        } else {
            holder = (ScheduledAdapter.ViewHolder) view.getTag();
        }
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, TripDetailedPanelForStream.class);
                intent.putExtra("user_entered_id", Keys.get(position));
                activity.startActivity(intent);
            }
        });
        holder.nameTextView.setText(tripList.get(position).getStopName());
        holder.etaTextView.setText(tripList.get(position).getTime());
        holder.uidNameTextView.setText(Keys.get(position));

        return view;
    }

    static class ViewHolder {

        RelativeLayout relativeLayout;
        TextView nameTextView;
        TextView etaTextView;
        TextView uidNameTextView;

    }
}

