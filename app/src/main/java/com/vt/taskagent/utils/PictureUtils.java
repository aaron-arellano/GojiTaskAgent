package com.vt.taskagent.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import androidx.exifinterface.media.ExifInterface;
import android.util.Log;
import java.io.IOException;

/** This class outputs the correct sized image of the task when the user clicks on it. Image
 *  dimensions are scaled here to fit new dialog which shows the image.
 *
 * @author Aaron Arellano
 * @version 2020.05.22
 */

public class PictureUtils {
    //read in the dimensions of the image on disk
    public static Bitmap getScaledBitmap(String path, Activity activity) {
        Point size = new Point();
        activity.getWindowManager().getDefaultDisplay()
                .getSize(size);
        return getScaledBitmap(path, size.x, size.y);
    }

    private static Bitmap getScaledBitmap(String path, int destWidth, int destHeight) {
        // Read in the dimensions of the image on disk
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);
        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;
        // Figure out how much to scale down by
        int inSampleSize = 1;
        if (srcHeight > destHeight || srcWidth > destWidth) {
            float heightScale = srcHeight / destHeight;
            float widthScale = srcWidth / destWidth;
            inSampleSize = Math.round(Math.max(heightScale, widthScale));
        }
        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        // Read in and create final bitmap
        Bitmap tempBipmap = BitmapFactory.decodeFile(path, options);

        // rotate the image
        try {
            tempBipmap = getRotateImage(path, tempBipmap);
        }
        catch (IOException ioe) {
            Log.e("BipmapIO","The image file was not found with exception: " + ioe.toString());
        }
        return tempBipmap;
    }

    /**  Method gets a photo and appropriately rotates it based on EI setting in photo meta.
     *
     * @throws IOException if the image is not found at the path provided
     * @param bitmap the photo in serializable form
     * @param photoPath the path to the photo
     */
    private static Bitmap getRotateImage(String photoPath, Bitmap bitmap) throws IOException {
        ExifInterface ei = new ExifInterface(photoPath);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        Bitmap rotatedBitmap;
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                rotatedBitmap = rotateImage(bitmap, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                rotatedBitmap = rotateImage(bitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                rotatedBitmap = rotateImage(bitmap, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                rotatedBitmap = bitmap;
        }
        return rotatedBitmap;
    }

    /**  Method gets a photo and appropriately rotates it based on EI setting in photo meta.
     *
     *  @param source serializable form of the photo
     *  @param angle angle of the photo to rotate
     */
    private static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }
}
