package com.vt.taskagent.controller;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import java.io.File;
import java.util.Objects;
import java.util.UUID;
import com.vt.taskagent.R;
import com.vt.taskagent.model.Task;
import com.vt.taskagent.model.TaskLab;

/**
 * Created by Aaron on 4/1/2018.
 */

public class ZoomPhotoFragment extends DialogFragment {
    private static final String ARG_IMAGE = "image";
    private File mPhotoFile;
    private ImageView mPhotoView;

    public static ZoomPhotoFragment newInstance(UUID taskId) {
        ZoomPhotoFragment zoom = new ZoomPhotoFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_IMAGE, taskId);
        zoom.setArguments(args);
        return zoom;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.zoom_image, null);
        //get the taskId from the bundle
        UUID taskId = (UUID) getArguments().getSerializable(ARG_IMAGE);
        Task mTask = TaskLab.getInstance(getActivity()).getTask(taskId);
        mPhotoFile = TaskLab.getInstance(getActivity()).getPhotoFile(mTask);
        mPhotoView = v.findViewById(R.id.image_zoom);
        updatePhotoView();
        //style is needed to make screen big android.R.style.Theme_Black_NoTitleBar_Fullscreen
        return new AlertDialog.Builder(Objects.requireNonNull(getActivity()))
                .setView(v)
                .setTitle(R.string.picture_zoom)
                .setPositiveButton(
                        android.R.string.ok,
                        null)
                .create();
    }

    // update the photo on the dialog given to user
    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    mPhotoFile.getPath(), Objects.requireNonNull(getActivity()));
            mPhotoView.setImageBitmap(bitmap);
        }
    }


}
