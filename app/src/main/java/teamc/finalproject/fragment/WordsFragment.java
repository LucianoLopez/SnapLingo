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
import java.util.Iterator;

import teamc.finalproject.R;
import teamc.finalproject.RecyclerViewWords.WordsAdapter;
import teamc.finalproject.VerticalSpaceItemDecoration;
import teamc.finalproject.Word;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WordsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WordsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WordsFragment extends Fragment {
    private RecyclerView mWordsRecyclerView;
    private RecyclerView.Adapter mWordsAdapter;
    private RecyclerView.LayoutManager mWordsLayoutManager;

    private RecyclerView mFoundWordsRecyclerView;
    private RecyclerView.Adapter mFoundWordsAdapter;
    private RecyclerView.LayoutManager mFoundWordsLayoutManager;

    private OnFragmentInteractionListener mListener;

    private static DatabaseReference mGamesRef;

    private static String mGameId;
    private static String mUserId;
    private static ArrayList<Word> mWords;
    private static ArrayList<Word> mFoundWords;

    public WordsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment WordsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WordsFragment newInstance() {
        WordsFragment fragment = new WordsFragment();
        mGameId = "1579";
        mUserId = "123456";

        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        mGamesRef = db.child("games").child(mGameId);

        mWords = new ArrayList<>();
        mFoundWords = new ArrayList<>();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_words, container, false);

        VerticalSpaceItemDecoration dividerItemDecoration = new VerticalSpaceItemDecoration(48);

        mWordsRecyclerView = view.findViewById(R.id.inProgressRecyclerView);
        mWordsRecyclerView.addItemDecoration(dividerItemDecoration);
        mWordsRecyclerView.setNestedScrollingEnabled(true);
        mWordsRecyclerView.setHasFixedSize(true);
        mWordsLayoutManager = new LinearLayoutManager(getContext());
        mWordsRecyclerView.setLayoutManager(mWordsLayoutManager);
        mWordsAdapter = new WordsAdapter(getWords(), 0, getContext());
        mWordsRecyclerView.setAdapter(mWordsAdapter);

        mFoundWordsRecyclerView = view.findViewById(R.id.foundRecyclerView);
        mFoundWordsRecyclerView.addItemDecoration(dividerItemDecoration);
        mFoundWordsRecyclerView.setNestedScrollingEnabled(true);
        mFoundWordsRecyclerView.setHasFixedSize(true);
        mFoundWordsLayoutManager = new LinearLayoutManager(getContext());
        mFoundWordsRecyclerView.setLayoutManager(mFoundWordsLayoutManager);
        mFoundWordsAdapter = new WordsAdapter(getFoundWords(), 1, getContext());
        mFoundWordsRecyclerView.setAdapter(mFoundWordsAdapter);

        return view;
    }

    private ArrayList<Word> getWords() {
        DatabaseReference wordsRef = mGamesRef.child("words");
        wordsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                for (DataSnapshot child : snapshot.getChildren()) {
                    mWords.add(child.getValue(Word.class));
                    mWordsAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return mWords;
    }

    private ArrayList<Word> getFoundWords() {
        DatabaseReference wordsFoundRef = mGamesRef.child("player_list").child(mUserId).child("words_found");
        wordsFoundRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for (DataSnapshot child : dataSnapshot.getChildren()){
                        Word foundWord = child.getValue(Word.class);
                        if (!mFoundWords.contains(foundWord)) {
                            mFoundWords.add(foundWord);
                            mFoundWordsAdapter.notifyDataSetChanged();
                        }
                        updateWords(foundWord);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return mFoundWords;
    }

    private void updateWords(Word foundWord) {
        for (Iterator<Word> iterator = mWords.iterator(); iterator.hasNext(); ) {
            Word word = iterator.next();
            if (word.getEnglishTranslation().equals(foundWord.getEnglishTranslation())) {
                iterator.remove();
            }
        }
        mWordsAdapter.notifyDataSetChanged();
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
