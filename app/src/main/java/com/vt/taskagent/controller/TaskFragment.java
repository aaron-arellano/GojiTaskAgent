package com.vt.taskagent.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import java.io.File;
import java.text.DateFormat;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.vt.taskagent.R;
import com.vt.taskagent.model.Task;
import com.vt.taskagent.model.TaskEntry;
import com.vt.taskagent.model.TaskEntryLab;
import com.vt.taskagent.model.TaskLab;
import com.vt.taskagent.view.EntryAdapter;

/** This class acts as the android fragment for the contents of a task, this means entries and other
 *  entities within a task. You cannot update entries here, but they are delegated here. Controls
 *  the view life cycle of the task fragments.
 *
 *  @author Aaron Arellano
 *  @version 2020.05.22
 */
public class TaskFragment extends Fragment {

    //view field
    private RecyclerView mEntryRecyclerView;
    private EntryAdapter mEntryAdapter;
    private static final int REQUEST_COMMENT = 0;
    private static final int REQUEST_PHOTO = 2;
    private static final int REQUEST_IMAGE = 3;
    private static final String ARG_TASK_ID = "task_id";
    private static final String ARG_IMAGE = "arg_image";
    private static final String DIALOG_COMMENT = "DialogComment";
    private static final String DIALOG_ZOOM = "DialogZoom";

    // model fields
    private Task mTask;
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
        void onTaskUpdated(Task task);
        void onEntrySelected(TaskEntry entry);
    }

    //called whenever the activity needs to create a new fragment
    public static TaskFragment newInstance(UUID taskId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TASK_ID, taskId);
        TaskFragment fragment = new TaskFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //retrieves the UID form the fragment arguments
        super.onCreate(savedInstanceState);
        UUID taskId = (UUID) getArguments().getSerializable(ARG_TASK_ID);
        mTask = TaskLab.getInstance(getActivity()).getTask(taskId);
        //needed in order to call the method for the action bar up button
        setHasOptionsMenu(true);
        //project4
        mPhotoFile = TaskLab.getInstance(getActivity()).getPhotoFile(mTask);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task, container, false);
        mEntryRecyclerView = view.findViewById(R.id.entry_recycler);
        mEntryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mPhotoView = view.findViewById(R.id.task_photo);
        updatePhotoView();

        // initialize view fields
        mTitleField = view.findViewById(R.id.task_title_hint);
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mTask.setTitle(charSequence.toString());
                updateTask();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // do nothing
            }
        });

        // set the listener on the Realized check box
        mRealizedCheckBox = view.findViewById(R.id.task_realized);
        mRealizedCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                // add the DR message
                if (!mTask.isRealized()) mTask.addTaskRealized();
            } else {
                // remove the DR message
                if (mTask.isRealized()) mTask.removeTaskRealized();
            }
            mTask.setRealized(b);
            updateTask();
            refreshView();
        });

        // set the listener on the deferred box
        mDeferredCheckBox = view.findViewById(R.id.task_deferred);
        mDeferredCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                // add the DR message
                if (!mTask.isDeferred()) mTask.addTaskDeferred();
            } else {
                // remove the DR message
                if (mTask.isDeferred()) mTask.removeTaskDeferred();
            }
            mTask.setDeferred(b);
            updateTask();
            refreshView();
        });


        // add a listener to the '+' button
        mAddCommentFAB = view.findViewById(R.id.myFAB);
        mAddCommentFAB.setOnClickListener(
                v -> {
                    FragmentManager manager =
                            TaskFragment.this.getFragmentManager();
                    DialogPickerFragment dialog = new DialogPickerFragment();
                    // callback for onActivityResult, second arg is resultCode
                    dialog.setTargetFragment(
                            TaskFragment.this, REQUEST_COMMENT);
                    dialog.show(manager, DIALOG_COMMENT);
                });

        //add listener to photo view
        mPhotoView.setOnClickListener(v -> {
            FragmentManager man =
                    TaskFragment.this.getFragmentManager();
            ZoomPhotoFragment dialog = ZoomPhotoFragment.newInstance(mTask.getId());
            //dialog.setPhotoFile(mPhotoFile);
            dialog.setTargetFragment(
                    TaskFragment.this, REQUEST_IMAGE);

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
                mTask.updateEntryComment(comment.replaceAll(regex, ""), uuid);
                isUpdate = true;
                break;
            }
            if (!isUpdate) {
                mTask.addComment(comment);
            }
            updateTask();
            refreshEntryButtons();
        } //handle other dialogs here
        else if (requestCode == REQUEST_PHOTO) {
            Uri uri = FileProvider.getUriForFile(Objects.requireNonNull(getActivity()),
                    "com.vt.taskagent.fileprovider",
                    mPhotoFile);
            getActivity().revokeUriPermission(uri,
                    Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
            updateTask();
            updatePhotoView();
        }
    }

    // update a task
    private void updateTask() {
        TaskLab.getInstance(getActivity()).updateTask(mTask);
        TaskEntryLab.getInstance(getActivity()).updateTaskEntries(mTask);
        // IMPORTANT, this method is only called for tablets
        mCallbacks.onTaskUpdated(mTask);
    }

    // not used, unless needed for a tablet
    private void updateEntry() { }

    private void refreshView() {
        if (mTask.getTitle() != null) {
            mTitleField.setText(mTask.getTitle());
        }
        mRealizedCheckBox.setChecked(mTask.isRealized());
        mDeferredCheckBox.setChecked(mTask.isDeferred());

        //logic for making sure check boxes can be checked one at a time.
        if (mRealizedCheckBox.isChecked()) { mDeferredCheckBox.setEnabled(false); }
        else {  mDeferredCheckBox.setEnabled(true); }

        if (mDeferredCheckBox.isChecked()) { mRealizedCheckBox.setEnabled(false); }
        else { mRealizedCheckBox.setEnabled(true); }

        refreshEntryButtons();
    }

    private void refreshEntryButtons() {
        List<TaskEntry> entries = mTask.getTaskEntries();
        if (mEntryAdapter == null) {
            mEntryAdapter = new EntryAdapter(entries);
            mEntryRecyclerView.setAdapter(mEntryAdapter);
        } else {
            mEntryAdapter.setTaskEntries(entries);
            mEntryAdapter.notifyDataSetChanged();
        }
    }

    //tablet
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onPause() {
        super.onPause();
        TaskLab.getInstance(getActivity())
                .updateTask(mTask);
        TaskEntryLab.getInstance(getActivity())
                .updateTaskEntries(mTask);
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
                    View alertView = LayoutInflater.from(getActivity())
                            .inflate(R.layout.dialog_alert, null);
                    new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                            .setCustomTitle(alertView)
                            .setMessage(R.string.extra_credit_dialogue)
                            .setPositiveButton(android.R.string.ok, null)
                            .create().show();
                    return true;
                }
                return false;

            case R.id.share_task:
                //project4
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getTaskReport());
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.task_report_subject));
                i = Intent.createChooser(i, getString(R.string.send_report));
                startActivity(i);
                return true;

            case R.id.camera:
                //new camera implementation
                // a package manager know which activities can take a photo
                PackageManager packageManager = Objects.requireNonNull(getActivity()).getPackageManager();
                // create the implicit intent for taking a photo
                final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // see if a photo taking activity even exists
                boolean canTakePhoto = mPhotoFile != null &&
                        captureImage.resolveActivity(packageManager) != null;
                // don't enable the photo button if the needed activity doesn't exist
                item.setEnabled(canTakePhoto);
                // get the URI for the file that will store the photo
                Uri uri = FileProvider.getUriForFile(getActivity(),
                        "com.vt.taskagent.fileprovider",
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

    private String getTaskReport() {
        String solvedString = null;
        boolean realOrDef = false;
        if (mTask.isRealized()) {
            solvedString = getString(R.string.task_report_realized);
            realOrDef = true;
        } else if(mTask.isDeferred()){
            solvedString = getString(R.string.task_report_deferred);
            realOrDef = true;
        } else {
            solvedString = getString(R.string.notRealDef);
        }

        DateFormat df = DateFormat.getDateInstance(DateFormat.MEDIUM);
        String date = df.format(mTask.getRevealedDate());
        String checkDate = "";
        boolean exitLoop = false;
        for (TaskEntry entry : mTask.getTaskEntries()) {
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
            report = getString(R.string.task_report, mTask.getTitle(), date,
                    solvedString, checkDate);
        } else {
            report = getString(R.string.task_report2, mTask.getTitle(), date,
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
                    mPhotoFile.getPath(), Objects.requireNonNull(getActivity()));
            mPhotoView.setImageBitmap(bitmap);
        }
    }

    //inflating a menu resource
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_task, menu);
    }
}

