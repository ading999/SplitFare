package cn.youthocracy.splitfare;

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
 * {@link EditPersonsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditPersonsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditPersonsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TextView addBox;
    private int CollectionID;
    private Bundle arg;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView lv;
    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PersonsOverview.
     */
    // TODO: Rename and change types and number of parameters
    public static EditPersonsFragment newInstance(String param1, String param2) {
        EditPersonsFragment fragment = new EditPersonsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public EditPersonsFragment() {
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
        final View rootView = (View) inflater.inflate(R.layout.collection_editperson,container,false);
        arg = getArguments();
        CollectionID = arg.getInt("CollectionID");
       // Log.d("EditPersonsFrag collectionID",String.valueOf(CollectionID));
        TextView finishperson = (TextView) rootView.findViewById(R.id.finishperson);
        TextView backperson = (TextView) rootView.findViewById(R.id.backperson);
        lv = (ListView) rootView.findViewById(R.id.personslist);
        addBox = (TextView) rootView.findViewById(R.id.addbox);
        final BillDataSource BData = new BillDataSource(getActivity());
        try {
            BData.open();

        }
        catch(SQLException e){
          //  Log.d("Add person exception",e.toString());
        }
        List<Person> c = BData.getAllPersons(CollectionID);
        if(c!=null){
            updateListView(c);}
        final TextView personname = (TextView) rootView.findViewById(R.id.personname);
        finishperson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!personname.getText().toString().isEmpty()){
                    Person p = new Person();
                    p.setCollectionID(CollectionID);
                    p.setPersonName(personname.getText().toString());
                    BData.addPerson(p);
                }
                CollectionOverviewFragment collectionOverviewFrag = new CollectionOverviewFragment();
                collectionOverviewFrag.setArguments(arg);
            //    Log.d("Bundle collectionid editPersons",String.valueOf(CollectionID));
                InputMethodManager input = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                input.hideSoftInputFromWindow(rootView.getWindowToken(),0);
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, collectionOverviewFrag)
                        .commit();
            }
        });
        backperson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager input = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                input.hideSoftInputFromWindow(personname.getWindowToken(),0);
                CollectionOverviewFragment collectionOverviewFrag = new CollectionOverviewFragment();
                collectionOverviewFrag.setArguments(arg);
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, collectionOverviewFrag)
                        .commit();
            }
        });

        addBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!personname.getText().toString().isEmpty()){
                Person p = new Person();
                p.setCollectionID(CollectionID);
                p.setPersonName(personname.getText().toString());
                InputMethodManager input = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                input.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),0);
                BData.addPerson(p);
                EditPersonsFragment PersonsFrag = new EditPersonsFragment();
                PersonsFrag.setArguments(arg);
                getFragmentManager().beginTransaction()
                        .replace(R.id.container, PersonsFrag)
                        .commit();   }}
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

    public void updateListView(List<Person> rs) {
        try {
            //actually update listview
            ArrayList<Person> com = new ArrayList<>();
            //Initialize our array adapter notice how it references the listitems.xml layout
            PersonAdapter personadp = new PersonAdapter(getActivity(), R.layout.person_item, com);
            lv.setAdapter(personadp);
            for (Person l : rs) {
                com.add(l);

            }
            personadp.notifyDataSetChanged();
        } catch (java.lang.IndexOutOfBoundsException e) {
            //  Log.d("IndexOutOfBound", e.toString());
        }
    }
}
