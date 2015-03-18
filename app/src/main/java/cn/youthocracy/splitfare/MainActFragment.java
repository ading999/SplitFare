package cn.youthocracy.splitfare;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainActFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MainActFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainActFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private ListView lv01;

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
     * @return A new instance of fragment MainActFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainActFragment newInstance(String param1, String param2) {
        MainActFragment fragment = new MainActFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public MainActFragment() {
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
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        TextView addCollection;
        lv01 = (ListView) rootView.findViewById(R.id.mainlistview);
        BillDataSource BData = new BillDataSource(getActivity());
        try{
            BData.open();
            List<FareCollection> c = BData.getAllCollections();
            if(c!=null){
                updateListView(c);}
        }
        catch(SQLException e){
            Log.d("overview sql exception", e.toString());
        }

        addCollection = (TextView) rootView.findViewById(R.id.add);
        addCollection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, new AddCollectionFragment())
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

  /*  @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }
*/
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

    public void updateListView(List<FareCollection> rs) {
        try {
            //actually update listview
            ArrayList<FareCollection> com = new ArrayList<>();
            //Initialize our array adapter notice how it references the listitems.xml layout
            CollectionAdapter mainadp = new CollectionAdapter(getActivity(), R.layout.collection_item, com);
            lv01.setAdapter(mainadp);
            for (FareCollection l : rs) {
                com.add(l);

            }
            mainadp.notifyDataSetChanged();
        } catch (java.lang.IndexOutOfBoundsException e) {
            //  Log.d("IndexOutOfBound", e.toString());
        }
    }
}
