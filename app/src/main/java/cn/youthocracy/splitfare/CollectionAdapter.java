package cn.youthocracy.splitfare;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by Justice on 2/15/15.
 */
public class CollectionAdapter extends ArrayAdapter<FareCollection> {
    int resource;

    //String BookID;
    //String CommentID;
    public CollectionAdapter(Context context, int resource, List<FareCollection> items) {
        super(context, resource, items);
        this.resource = resource;


    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final LinearLayout CommentView;
        //Get the current alert object
        final FareCollection al = getItem(position);
        // Log.d("position", Integer.toString(position));

        //Inflate the view
        if (convertView == null) {
            CommentView = new LinearLayout(getContext());
            String inflater = Context.LAYOUT_INFLATER_SERVICE;
            LayoutInflater vi;
            vi = (LayoutInflater) getContext().getSystemService(inflater);
            vi.inflate(resource, CommentView, true);
        } else {
            CommentView = (LinearLayout) convertView;
        }
        //Get the text boxes from the listitem.xml file
        TextView CollectionName = (TextView) CommentView.findViewById(R.id.main_collectionname);
        TextView ID = (TextView) CommentView.findViewById(R.id.main_collectionid);
        TextView Amount = (TextView) CommentView.findViewById(R.id.main_collectiontotal);
        //Assign the appropriate data from our alert object above
        CollectionName.setText(al.getCollectionName());
        View collectionClick = (View) CommentView.findViewById(R.id.collectionclick);
        TextView collectionRemove = (TextView) CommentView.findViewById(R.id.collection_item_remove);
        final BillDataSource BData = new BillDataSource(getContext());
        try{BData.open();}
        catch(SQLException e){
            Log.d("CollectionAdapter SQL Exception",e.toString());
        }
        Amount.setText("Total: $"+String.valueOf(al.getCollectionTotal()));

        //  Log.d("collection adapter error",String.valueOf(al.getCollectionID()));
        ID.setText(String.valueOf(al.getCollectionID()));
        collectionClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getContext()instanceof FragmentActivity){
                    CollectionOverviewFragment overviewFragment = new CollectionOverviewFragment();
                    Bundle arg = new Bundle();
                    arg.putInt("CollectionID",al.getCollectionID());
                    overviewFragment.setArguments(arg);
                FragmentActivity activity = (FragmentActivity)(getContext());
                    activity.getFragmentManager().beginTransaction().replace(R.id.container, overviewFragment)
                            .commit();

                }
            }
        });

        collectionRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BData.removeCollection(al.getCollectionID());

                Bundle arg = new Bundle();
                arg.putInt("CollectionID",al.getCollectionID());
                MainActFragment mainFrag = new MainActFragment();
                mainFrag.setArguments(arg);
                FragmentActivity activity = (FragmentActivity)(getContext());
                activity.getFragmentManager().beginTransaction().replace(R.id.container, mainFrag)
                        .commit();
            }
        });
        return CommentView;
    }


}