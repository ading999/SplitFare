package cn.youthocracy.splitfare;

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
public class PersonAdapter extends ArrayAdapter<Person> {
    int resource;

    //String BookID;
    //String CommentID;
    public PersonAdapter(Context context, int resource, List<Person> items) {
        super(context, resource, items);
        this.resource = resource;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout CommentView;
        //Get the current alert object
        final Person al = getItem(position);
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
        TextView id = (TextView) CommentView.findViewById(R.id.personitem_id);
        TextView name = (TextView) CommentView.findViewById(R.id.personitem_name);
        TextView personRemove = (TextView) CommentView.findViewById(R.id.person_item_remove);
        //Assign the appropriate data from our alert object above
       id.setText(String.valueOf(position+1));
       // id.setText(String.valueOf(al.getPersonID()));

        name.setText(String.valueOf(al.getPersonName()));
        final Bundle arg = new Bundle();
        arg.putInt("CollectionID",al.getCollectionID());
        personRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getContext() instanceof FragmentActivity) {
                    BillDataSource BData = new BillDataSource(getContext());
                    try {
                        BData.open();
                    }catch(SQLException e){
                     //   Log.d("PersonAdapter SQL Exception", e.toString());
                    }
                    BData.removePerson(al.getPersonID(),al.getCollectionID());
                    FragmentActivity activity = (FragmentActivity) getContext();
                    EditPersonsFragment editFrag = new EditPersonsFragment();
                    editFrag.setArguments(arg);
                    activity.getFragmentManager().beginTransaction().replace(R.id.container, editFrag).commit();
                }
            }
        });
        return CommentView;
    }


}