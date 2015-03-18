package cn.youthocracy.splitfare;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
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
 * {@link CollectionOverviewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CollectionOverviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CollectionOverviewFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ListView lv01;
    private int CollectionID;
    private Bundle arg;
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
     * @return A new instance of fragment BlankFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CollectionOverviewFragment newInstance(String param1, String param2) {
        CollectionOverviewFragment fragment = new CollectionOverviewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public CollectionOverviewFragment() {
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
        View rootView = inflater.inflate(R.layout.collection_overview, container, false);
        final TextView title = (TextView) rootView.findViewById(R.id.collectionoverviewtitle);
        final TextView addpersons = (TextView) rootView.findViewById(R.id.addperson);
        final TextView addbill = (TextView) rootView.findViewById(R.id.addbill);
        final TextView backcollection = (TextView) rootView.findViewById(R.id.backcollection);
        final TextView total = (TextView) rootView.findViewById(R.id.collectiontotal);
               lv01 = (ListView) rootView.findViewById(R.id.overviewlist);
        InputMethodManager input = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        rootView.requestFocus();

        input.hideSoftInputFromWindow(rootView.getWindowToken(),0);
        BillDataSource BData = new BillDataSource(getActivity());
        try{
        BData.open();
            //need to setup interface between fragments
        List<Bill> c = BData.getAllBills(CollectionID);
        FareCollection f = BData.getCollection(CollectionID);
            title.setText(f.getCollectionName());
            if(c!=null){
                Log.d("collection overview c is empty","xxx");

                updateListView(c);}
        }
        catch(SQLException e){
            Log.d("overview sql exception",e.toString());
        }

        total.setText("Total: $"+BData.getCollectionTotal(CollectionID));

        addpersons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditPersonsFragment EditPersonsFrag  = new EditPersonsFragment();
                EditPersonsFrag.setArguments(arg);
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, EditPersonsFrag)
                        .commit();
            }
        });
        addbill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddbillFragment AddbillFrag  = new AddbillFragment();
                AddbillFrag.setArguments(arg);
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, AddbillFrag)
                        .commit();
            }
        });
        backcollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, new MainActFragment())
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

    public void updateListView(List<Bill> rs) {
        try {
            //actually update listview
            ArrayList<Bill> com = new ArrayList<>();
            //Initialize our array adapter notice how it references the listitems.xml layout
            BillAdapter comadp = new BillAdapter(getActivity(), R.layout.bill_item, com);
            lv01.setAdapter(comadp);
            for (Bill l : rs) {
                                   Log.d("Desc",l.getBillDescription());
                                    com.add(l);
            }

            comadp.notifyDataSetChanged();
        } catch (java.lang.IndexOutOfBoundsException e) {
            //  Log.d("IndexOutOfBound", e.toString());
        }
    }


}
