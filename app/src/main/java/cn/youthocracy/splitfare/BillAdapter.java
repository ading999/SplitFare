package cn.youthocracy.splitfare;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Justice on 2/15/15.
 */
public class BillAdapter extends ArrayAdapter<Bill> {
    int resource;

    //String BookID;
    //String CommentID;
    public BillAdapter(Context context, int resource, List<Bill> items) {
        super(context, resource, items);
        this.resource = resource;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout CommentView;
        //Get the current alert object
        final Bill al = getItem(position);

        Log.d("position", String.valueOf(al.getCollectionID()));

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
        TextView Description = (TextView) CommentView.findViewById(R.id.billitemdescription);
        final TextView Amount = (TextView) CommentView.findViewById(R.id.billitemamount);
        TextView ID = (TextView) CommentView.findViewById(R.id.billitemid);
        final TextView Payer = (TextView) CommentView.findViewById(R.id.billitempayer);
        TextView billRemove = (TextView) CommentView.findViewById(R.id.bill_item_remove);
        //Assign the appropriate data from our alert object above
        Description.setText(al.getBillDescription());
        ID.setText(String.valueOf(position+1));
        //ID.setText(String.valueOf(al.getBillID()));
        final BillDataSource BData = new BillDataSource(getContext());
        try{
            BData.open();

        }catch(SQLException e){
            Log.d("BillAdapter sql error",e.toString());
        }
        Payer.setText("Paid by: "+BData.getPayerName(al.getPayerID()));

        Amount.setText("Amount: $"+String.valueOf(al.getAmount()));
       // Payer.setText(al.getPayerID());
        billRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                BData.removeBill(al.getBillID(),al.getCollectionID());
                Bundle arg = new Bundle();
                arg.putInt("CollectionID",al.getCollectionID());
                Log.d("Bill adapter collectionid",String.valueOf(al.getCollectionID()));
                CollectionOverviewFragment overviewFragment = new CollectionOverviewFragment();
                overviewFragment.setArguments(arg);
                FragmentActivity activity = (FragmentActivity)(getContext());
                activity.getFragmentManager().beginTransaction().replace(R.id.container, overviewFragment)
                        .commit();
            }
        });

        return CommentView;
    }


}