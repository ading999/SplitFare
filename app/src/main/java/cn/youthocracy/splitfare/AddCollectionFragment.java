package cn.youthocracy.splitfare;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import java.io.InputStream;
import java.sql.SQLException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddCollectionFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddCollectionFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddCollectionFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

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
     * @return A new instance of fragment AddPersonFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AddCollectionFragment newInstance(String param1, String param2) {
        AddCollectionFragment fragment = new AddCollectionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AddCollectionFragment() {
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
        final View rootView = inflater.inflate(R.layout.collection_addcollection, container, false);
        final TextView finishadd = (TextView) rootView.findViewById(R.id.finishaddcollection);
        final TextView back = (TextView) rootView.findViewById(R.id.backaddcollection);
        final EditText collectionname = (EditText) rootView.findViewById(R.id.collectionname);
        collectionname.requestFocus();
        finishadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!collectionname.getText().toString().isEmpty()){

                    BillDataSource BData = new BillDataSource(getActivity());
                    try {
                        BData.open();

                    } catch (SQLException e) {
                        Log.d("add collection exception", e.toString());
                    }
                    FareCollection c = new FareCollection();
                    c.setCollectionName(collectionname.getText().toString());
                    int CollectionID = BData.addCollection(c);
                    EditPersonsFragment editPersonsFrag = new EditPersonsFragment();
                    Bundle arg = new Bundle();
                    arg.putInt("CollectionID", CollectionID);
                    Log.d("Bundle collectionid add collection", String.valueOf(CollectionID));
                    editPersonsFrag.setArguments(arg);
                    getFragmentManager().beginTransaction().replace(R.id.container, editPersonsFrag).commit();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager input = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                input.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),0);
                getFragmentManager().beginTransaction().replace(R.id.container, new MainActFragment()).commit();
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

   /* @Override
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
