package teamc.finalproject.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import teamc.finalproject.R;
import teamc.finalproject.RecyclerViewScores.DividerItemDecoration;
import teamc.finalproject.RecyclerViewScores.ScoresAdapter;
import teamc.finalproject.Score;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ScoreboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScoreboardFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private RecyclerView mScoresRecyclerView;
    private RecyclerView.Adapter mScoresAdapter;
    private RecyclerView.LayoutManager mScoresLayoutManager;
    private static int mGameId;

    private static ArrayList<User> mUsers;
    private static ArrayList<Score> mScores;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ScoreboardFragment() {
        // Required empty public constructor
    }

    private static DatabaseReference mGamesRef;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ScoreboardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScoreboardFragment newInstance(int gameID) {
        ScoreboardFragment fragment = new ScoreboardFragment();

        Bundle args = new Bundle();
        args.putInt("gameID", gameID);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mGameId = getArguments().getInt("gameID");;

            DatabaseReference db = FirebaseDatabase.getInstance().getReference();
            mGamesRef = db.child("games").child(Integer.toString(mGameId));

            mScores = new ArrayList<>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_scoreboard, container, false);

        mScoresRecyclerView = view.findViewById(R.id.recyclerView);
        mScoresLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), R.drawable.scores_divider);
        mScoresRecyclerView.addItemDecoration(dividerItemDecoration);
        mScoresRecyclerView.setLayoutManager(mScoresLayoutManager);
        mScoresAdapter = new ScoresAdapter(getScores(), 0, getContext());
        mScoresRecyclerView.setAdapter(mScoresAdapter);

        return view;
    }

    private ArrayList<Score> getScores() {

        mUsers = new ArrayList<User>();

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mUsers = new ArrayList<User>();
                for (DataSnapshot userSnap: dataSnapshot.getChildren()) {


                    String name = (String) userSnap.child("name").getValue();
                    if (name == null) {
                        continue;

                    }
                    String uid = (String) userSnap.getKey();
                    User newUser = new User(uid, name);
                    mUsers.add(newUser);

                }

                getScoresFromUsers();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return mScores;
    }
  
    private void getScoresFromUsers() {

        DatabaseReference wordsRef = mGamesRef.child("player_list");

        wordsRef.orderByChild("points").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                mScores = new ArrayList<Score>();

                for (DataSnapshot child : snapshot.getChildren()) {

                    String userID = (String) child.getKey();
                    String userName = (String) "unknown";
                    for (int i = 0; i < mUsers.size(); i++) {
                        User user = mUsers.get(i);
                        if (user.uid.equals(userID)) {
                            userName = user.userName;
                            break;
                        }
                    }

                    Long points = (Long) child.child("points").getValue();
                    int userPoints = 0;

                    if (points != null) {
                        userPoints = points.intValue(); // toIntExact() is not supported
                    }

                    Score newScore = new Score(userName, userPoints);

                    mScores.add(0, newScore);

                }
                ScoresAdapter adapt = (ScoresAdapter) mScoresAdapter;
                adapt.scoreList = mScores;
                mScoresAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

class User {
    public String userName;
    public String uid;
    public User(String uid, String name) {
        this.uid = uid;
        this.userName = name;
    }
}
