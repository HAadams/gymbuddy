package gymbuddy.project.capstone.gymbuddy.UI.MessagePage;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import gymbuddy.project.capstone.gymbuddy.Database.CurrentUser;
import gymbuddy.project.capstone.gymbuddy.Database.User;
import gymbuddy.project.capstone.gymbuddy.R;

public class MessageFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_PARAM1 = "param1";//id
    private static final String ARG_PARAM2 = "param2";//title

    // TODO: Customize parameters
    private int mColumnCount = 1;
    private int mID;
    private String mTitle;

    private OnListInteractionListener mListener;


    public MessageFragment() {
    }

    public static MessageFragment newInstance(int columnCount, int id, String title) {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putInt(ARG_PARAM1, id);
        args.putString(ARG_PARAM2, title);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
           mID = getArguments().getInt(ARG_PARAM1);
           mTitle = getArguments().getString(ARG_PARAM2);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_topic_list, container, false);

        List<User> users = new ArrayList<>();

        RecyclerView rView = view.findViewById(R.id.list);
        RecyclerView navRV = view.findViewById(R.id.recyclerView);

       //todo users = get users that you have already started talking to
        users.add(CurrentUser.getInstance());
        users.add(CurrentUser.getInstance());
        users.add(CurrentUser.getInstance());
        users.add(CurrentUser.getInstance());
        users.add(CurrentUser.getInstance());
        users.add(CurrentUser.getInstance());
        users.add(CurrentUser.getInstance());
        users.add(CurrentUser.getInstance());
        users.add(CurrentUser.getInstance());
        users.add(CurrentUser.getInstance());
        users.add(CurrentUser.getInstance());
        users.add(CurrentUser.getInstance());
        users.add(CurrentUser.getInstance());
        users.add(CurrentUser.getInstance());
        users.add(CurrentUser.getInstance());
        users.add(CurrentUser.getInstance());
        users.add(CurrentUser.getInstance());
        users.add(CurrentUser.getInstance());
        users.add(CurrentUser.getInstance());
        users.add(CurrentUser.getInstance());
        users.add(CurrentUser.getInstance());
        users.add(CurrentUser.getInstance());
        users.add(CurrentUser.getInstance());
        users.add(CurrentUser.getInstance());
        users.add(CurrentUser.getInstance());
        users.add(CurrentUser.getInstance());
        users.add(CurrentUser.getInstance());
        users.add(CurrentUser.getInstance());
        users.add(CurrentUser.getInstance());
        users.add(CurrentUser.getInstance());
        users.add(CurrentUser.getInstance());
        users.add(CurrentUser.getInstance());
        users.add(CurrentUser.getInstance());
        users.add(CurrentUser.getInstance());
        users.add(CurrentUser.getInstance());
        users.add(CurrentUser.getInstance());
        users.add(CurrentUser.getInstance());
        users.add(CurrentUser.getInstance());
        users.add(CurrentUser.getInstance());
        users.add(CurrentUser.getInstance());


            final Context context = view.getContext();

            RecyclerView recyclerView = (RecyclerView) rView;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                navRV.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, true));
                //setting horizontal linear layout and reversing the list so the recyclerview scrolls to the end
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }


           recyclerView.setAdapter(new MessageRecyclerViewAdapter(users, mListener));

            List<User> navTopics = new ArrayList<>();
            navTopics.add(CurrentUser.getInstance());

            //navRV.setAdapter(new MatchedRecyclerViewAdapter(mListener, navTopics));

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnListInteractionListener) {
//            mListener = (OnListInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnListFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnListFragmentInteractionListener {
        //click listener interface for the navigation List
        void onNavListFragmentInteraction(User item);
        //click listener interface for the main list
        void onListFragmentInteraction(User item);

    }
}
