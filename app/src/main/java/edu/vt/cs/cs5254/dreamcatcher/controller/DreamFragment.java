package edu.vt.cs.cs5254.dreamcatcher.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.vt.cs.cs5254.dreamcatcher.R;
import edu.vt.cs.cs5254.dreamcatcher.model.Dream;
import edu.vt.cs.cs5254.dreamcatcher.model.DreamEntry;
import edu.vt.cs.cs5254.dreamcatcher.model.DreamEntryLab;
import edu.vt.cs.cs5254.dreamcatcher.model.DreamLab;
import edu.vt.cs.cs5254.dreamcatcher.view.EntryAdapter;

import static edu.vt.cs.cs5254.dreamcatcher.model.DreamEntryKind.REVEALED;

/* This class acts as the android fragment for the contents of a dream, this means entries and other
 * entities within a dream. You cannot update entries here, but they are created here.
 * @Aaron
 */
public class DreamFragment extends Fragment {

    //view field
    private RecyclerView mEntryRecyclerView;
    private EntryAdapter mEntryAdapter;
    private static final int REQUEST_COMMENT = 0;
    private static final int REQUEST_PHOTO = 2;
    private static final int REQUEST_IMAGE = 3;
    private static final String ARG_DREAM_ID = "dream_id";
    private static final String ARG_IMAGE = "ar_image";
    private static final String DIALOG_COMMENT = "DialogComment";
    private static final String DIALOG_ZOOM = "DialogZoom";

    // model fields
    private Dream mDream;
    private File mPhotoFile;

    // view fields
    private EditText mTitleField;
    private CheckBox mRealizedCheckBox;
    private CheckBox mDeferredCheckBox;
    private FloatingActionButton mAddCommentFAB;
    //project4
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;

    //tablet
    private Callbacks mCallbacks;

    /**
     * Required interface for hosting activities
     */
    public interface Callbacks {
        void onDreamUpdated(Dream dream);
        void onEntrySelected(DreamEntry entry);
    }

    //called whenever the activity needs to create a new fragment
    public static DreamFragment newInstance(UUID dreamId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DREAM_ID, dreamId);
        DreamFragment fragment = new DreamFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //retrieves the UID form the fragment arguments
        super.onCreate(savedInstanceState);
        UUID dreamId = (UUID) getArguments().getSerializable(ARG_DREAM_ID);
        mDream = DreamLab.getInstance(getActivity()).getDream(dreamId);
        //needed in order to call the method for the action bar up button
        setHasOptionsMenu(true);
        //project4
        mPhotoFile = DreamLab.getInstance(getActivity()).getPhotoFile(mDream);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dream, container, false);
        mEntryRecyclerView = view.findViewById(R.id.entry_recycler);
        mEntryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mPhotoView = view.findViewById(R.id.dream_photo);
        updatePhotoView();

        // initialize view fields
        mTitleField = view.findViewById(R.id.dream_title_hint);
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mDream.setTitle(charSequence.toString());
                updateDream();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // do nothing
            }
        });

        // set the listener on the Realized check box
        mRealizedCheckBox = view.findViewById(R.id.dream_realized);
        mRealizedCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                // add the DR message
                if (!mDream.isRealized()) mDream.addDreamRealized();
            } else {
                // remove the DR message
                if (mDream.isRealized()) mDream.removeDreamRealized();
            }
            mDream.setRealized(b);
            updateDream();
            refreshView();
        });

        // set the listener on the deferred box
        mDeferredCheckBox = view.findViewById(R.id.dream_deferred);
        mDeferredCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                // add the DR message
                if (!mDream.isDeferred()) mDream.addDreamDeferred();
            } else {
                // remove the DR message
                if (mDream.isDeferred()) mDream.removeDreamDeferred();
            }
            mDream.setDeferred(b);
            updateDream();
            refreshView();
        });


        // add a listener to the '+' button
        mAddCommentFAB = view.findViewById(R.id.myFAB);
        mAddCommentFAB.setOnClickListener(
                v -> {
                    FragmentManager manager =
                            DreamFragment.this.getFragmentManager();
                    DialogPickerFragment dialog = new DialogPickerFragment();
                    // callback for onActivityResult, second arg is resultCode
                    dialog.setTargetFragment(
                            DreamFragment.this, REQUEST_COMMENT);
                    dialog.show(manager, DIALOG_COMMENT);
                });

        //add listener to photo view
        mPhotoView.setOnClickListener(v -> {
            FragmentManager man =
                    DreamFragment.this.getFragmentManager();
            ZoomPhotoFragment dialog = ZoomPhotoFragment.newInstance(mDream.getId());
            //dialog.setPhotoFile(mPhotoFile);
            dialog.setTargetFragment(
                    DreamFragment.this, REQUEST_IMAGE);

            dialog.show(man, DIALOG_ZOOM);

        });

        refreshView();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode != Activity.RESULT_OK) { return;}
        // handle getting the text from the dialog, need to add logic for updating a comment
        if (requestCode == REQUEST_COMMENT) {
            String comment =
                    (String) intent.getSerializableExtra(DialogPickerFragment.EXTRA_TEXT);
            //update with a new comment, check if comment contains a UUID
            String regex = "\\p{XDigit}{8}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{4}-\\p{XDigit}{12}";
            Pattern pairRegex = Pattern.compile(regex);
            Matcher matcher = pairRegex.matcher(comment);
            boolean isUpdate = false;
            while (matcher.find()) {
                String uuid = matcher.group(0);
                mDream.updateEntryComment(comment.replaceAll(regex, ""), uuid);
                isUpdate = true;
                break;
            }
            if (!isUpdate) {
                mDream.addComment(comment);
            }
            updateDream();
            refreshEntryButtons();
        } //handle other dialogs here
        else if (requestCode == REQUEST_PHOTO) {
            Uri uri = FileProvider.getUriForFile(getActivity(),
                    "edu.vt.cs.cs5254.dreamcatcher.fileprovider",
                    mPhotoFile);
            getActivity().revokeUriPermission(uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            updateDream();
            updatePhotoView();
        }
    }

    // update a dream
    private void updateDream() {
        DreamLab.getInstance(getActivity()).updateDream(mDream);
        DreamEntryLab.getInstance(getActivity()).updateDreamEntries(mDream);
        // IMPORTANT, this method is only called for tablets
        mCallbacks.onDreamUpdated(mDream);
    }

    // not used, unless needed for a tablet
    private void updateEntry() { }

    private void refreshView() {
        if (mDream.getTitle() != null) {
            mTitleField.setText(mDream.getTitle());
        }
        mRealizedCheckBox.setChecked(mDream.isRealized());
        mDeferredCheckBox.setChecked(mDream.isDeferred());

        //logic for making sure check boxes can be checked one at a time.
        if (mRealizedCheckBox.isChecked()) { mDeferredCheckBox.setEnabled(false); }
        else {  mDeferredCheckBox.setEnabled(true); }

        if (mDeferredCheckBox.isChecked()) { mRealizedCheckBox.setEnabled(false); }
        else { mRealizedCheckBox.setEnabled(true); }

        refreshEntryButtons();
    }

    private void refreshEntryButtons() {
        List<DreamEntry> entries = mDream.getDreamEntries();
        if (mEntryAdapter == null) {
            mEntryAdapter = new EntryAdapter(entries);
            mEntryRecyclerView.setAdapter(mEntryAdapter);
        } else {
            mEntryAdapter.setDreamEntries(entries);
            mEntryAdapter.notifyDataSetChanged();
        }
    }

    //tablet
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onPause() {
        super.onPause();
        DreamLab.getInstance(getActivity())
                .updateDream(mDream);
        DreamEntryLab.getInstance(getActivity())
                .updateDreamEntries(mDream);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    //implementation to get te action bar up button to prevent a person from going back
    //if the title field is empty.
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                String titleString = mTitleField.getText().toString();
                if (titleString.equals("")) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle(R.string.picker_extra)
                            .setMessage(R.string.extra_credit_dialogue)
                            .setNegativeButton(android.R.string.cancel, null)
                            .setPositiveButton(android.R.string.ok, null)
                            .create().show();
                    return true;
                }
                return false;

            case R.id.share_dream:
                //project4
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getDreamReport());
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.dream_report_subject));
                i = Intent.createChooser(i, getString(R.string.send_report));
                startActivity(i);
                return true;

            case R.id.camera:
                //new camera implementation
                // a package manager know which activities can take a photo
                PackageManager packageManager = getActivity().getPackageManager();
                // create the implicit intent for taking a photo
                final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // see if a photo taking activity even exists
                boolean canTakePhoto = mPhotoFile != null &&
                        captureImage.resolveActivity(packageManager) != null;
                // don't enable the photo button if the needed activity doesn't exist
                item.setEnabled(canTakePhoto);
                // get the URI for the file that will store the photo
                Uri uri = FileProvider.getUriForFile(getActivity(),
                        "edu.vt.cs.cs5254.dreamcatcher.fileprovider",
                        mPhotoFile);
                // add the URI as an extra to the intent
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                // return a list of all activities capable of taking a photo
                List<ResolveInfo> cameraActivities = getActivity()
                        .getPackageManager().queryIntentActivities(captureImage,
                                PackageManager.MATCH_DEFAULT_ONLY);
                // give write permission for the URI to all those activities
                for (ResolveInfo activity : cameraActivities) {
                    getActivity().grantUriPermission(activity.activityInfo.packageName,
                            uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION |
                                    Intent.FLAG_GRANT_READ_URI_PERMISSION);
                }
                // start the activity that takes the photo
                startActivityForResult(captureImage, REQUEST_PHOTO);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private String getDreamReport() {
        String solvedString = null;
        boolean realOrDef = false;
        if (mDream.isRealized()) {
            solvedString = getString(R.string.dream_report_realized);
            realOrDef = true;
        } else if(mDream.isDeferred()){
            solvedString = getString(R.string.dream_report_deferred);
            realOrDef = true;
        } else {
            solvedString = getString(R.string.notRealDef);
        }

        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
        String date = df.format(mDream.getRevealedDate());
        String checkDate = "";
        boolean exitLoop = false;
        for (DreamEntry entry : mDream.getDreamEntries()) {
            switch (entry.getKind()) {
                case DEFERRED:
                    checkDate = df.format(entry.getDate());
                    exitLoop = true;
                    break;
                case REALIZED:
                    checkDate = df.format(entry.getDate());
                    exitLoop = true;
                    break;
                default:
                    checkDate = "";
            }
            if (exitLoop) break;
        }


        String report = "";

        if (realOrDef) {
            report = getString(R.string.dream_report, mDream.getTitle(), date,
                    solvedString, checkDate);
        } else {
            report = getString(R.string.dream_report2, mDream.getTitle(), date,
                    solvedString);
        }
        return report;

    }


    // logic to update a photo
    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }

    //inflating a menu resource
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_dream, menu);
    }
}

