package teamc.finalproject;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

public class VerificationCorrectDialogFragment extends DialogFragment {
    private static int mPoints;

    public static VerificationCorrectDialogFragment newInstance(int points) {
        VerificationCorrectDialogFragment frag = new VerificationCorrectDialogFragment();
        mPoints = points;
        return frag;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.verification_correct)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        VerificationCorrectDialogFragment.this.getDialog().cancel();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }

    /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
    public interface VerificationCorrectDialogListener {
    }

    // Use this instance of the interface to deliver action events
    VerificationCorrectDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the VerificationCorrectDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the VerificationCorrectDialogListener so we can send events to the host
            mListener = (VerificationCorrectDialogListener) context;
        } catch (ClassCastException e) {
            // The context doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement VerificationCorrectDialogListener");
        }
    }
}