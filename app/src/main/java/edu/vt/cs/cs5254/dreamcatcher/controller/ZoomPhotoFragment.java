package edu.vt.cs.cs5254.dreamcatcher.controller;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.appcompat.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import java.io.File;
import java.util.UUID;
import edu.vt.cs.cs5254.dreamcatcher.R;
import edu.vt.cs.cs5254.dreamcatcher.model.Dream;
import edu.vt.cs.cs5254.dreamcatcher.model.DreamLab;

/**
 * Created by Aaron on 4/1/2018.
 */

public class ZoomPhotoFragment extends DialogFragment {
    private static final String ARG_IMAGE = "image";
    private File mPhotoFile;
    private ImageView mPhotoView;

    public static ZoomPhotoFragment newInstance(UUID dreamId) {
        ZoomPhotoFragment zoom = new ZoomPhotoFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_IMAGE, dreamId);
        zoom.setArguments(args);
        return zoom;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.zoom_image, null);
        //get the dreamId from the bundle
        UUID dreamId = (UUID) getArguments().getSerializable(ARG_IMAGE);
        Dream mDream = DreamLab.getInstance(getActivity()).getDream(dreamId);
        mPhotoFile = DreamLab.getInstance(getActivity()).getPhotoFile(mDream);
        mPhotoView = v.findViewById(R.id.image_zoom);
        updatePhotoView();
        //style is needed to make screen big android.R.style.Theme_Black_NoTitleBar_Fullscreen
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.picture_zoom)
                .setPositiveButton(
                        android.R.string.ok,
                        null)
                .create();
    }

    //project4
    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(
                    mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }


}
