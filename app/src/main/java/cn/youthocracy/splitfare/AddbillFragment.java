package cn.youthocracy.splitfare;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddbillFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddbillFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddbillFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private int CollectionID;
    private Bundle arg;
    private int idOfPayer;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddbillFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddbillFragment newInstance(String param1, String param2) {
        AddbillFragment fragment = new AddbillFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AddbillFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        arg = getArguments();
        CollectionID = arg.getInt("CollectionID");
       // Log.d("AddBillFrag",String.valueOf(CollectionID));
        View rootView = (View) inflater.inflate(R.layout.collection_addbill,container,false);
        TextView finishadd = (TextView) rootView.findViewById(R.id.finishbill);
        TextView backbill = (TextView) rootView.findViewById(R.id.backbill);
        final EditText payerID = (EditText) rootView.findViewById(R.id.payer);
        final EditText amount = (EditText) rootView.findViewById(R.id.billamount);
        final EditText description = (EditText) rootView.findViewById(R.id.billname);
        description.requestFocus();
        final Bill b = new Bill();

        final BillDataSource BData = new BillDataSource(getActivity());
        try{
        BData.open();}
        catch(SQLException e){
         //   Log.d("BData exception", e.toString());
        }

payerID.setOnFocusChangeListener(new View.OnFocusChangeListener() {
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if(hasFocus){
            final CharSequence names[] = BData.getPersonNames(CollectionID);
            final int[] ids = BData.getPersonIds(CollectionID);

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Choose a payer");
            builder.setItems(names, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // the user clicked on colors[which]
                idOfPayer = ids[which];
                payerID.setText(names[which]);
                }
            });
            builder.show();
        }
    }
});

        finishadd.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                if(!description.getText().toString().isEmpty()){
                    if(isNumeric(amount.getText().toString())){
                        if(!payerID.getText().toString().isEmpty()){
               b.setAmount(Double.parseDouble(amount.getText().toString()));
               b.setBillDescription(description.getText().toString());
               b.setPayerID(idOfPayer);
             //  Log.d("add bill fragment payerID",String.valueOf(idOfPayer));
               b.setCollectionID(CollectionID);
               BData.addBill(b);
               BData.close();
               InputMethodManager input = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
               input.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),0);
               CollectionOverviewFragment OverviewFrag = new CollectionOverviewFragment();
               OverviewFrag.setArguments(arg);
              getFragmentManager().beginTransaction()
                      .replace(R.id.container,OverviewFrag)
                       .commit();}else{
                            alert("Please select a payer");
                        }

                    }
                    else{
                        alert("Please enter a bill amount");
                    }
                }else{
                alert("Please enter a bill description");
                }

           }
       });

        backbill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                        BData.close();
                CollectionOverviewFragment OverviewFrag = new CollectionOverviewFragment();
                OverviewFrag.setArguments(arg);
                getFragmentManager().beginTransaction()
                        .replace(R.id.container,OverviewFrag)
                        .commit();

            }
        });


        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
/*
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }


    public static boolean isNumeric(String str)
    {
        try
        {
            double d = Double.parseDouble(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }

    private void alert(String message){
        new AlertDialog.Builder(getActivity())
                .setTitle("Error:")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        // continue with delete
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
