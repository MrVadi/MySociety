package ua.mrvadi.mysociety.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import ua.mrvadi.mysociety.R;

/**
 * Created by mrvadi on 04.10.16.
 */
public class ImageHelper {

    public static byte[] getBytesFromRes(Context context, int resId) {
        Drawable drawable = ContextCompat.getDrawable(context, resId);
        Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    public static byte[] getBytesFromImage(ImageView imageView) {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

        return stream.toByteArray();
    }

    public static byte[] getBytesFromUri(Context context, Uri uri) throws IOException {
        InputStream stream =   context.getContentResolver().openInputStream(uri);
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while (stream != null && (len = stream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public static Drawable roundedDrawableFromBytes (Context context, byte[] bytes) {
        float cornerRadius = context.getResources().getDimension(R.dimen.corner_radius);

        Bitmap src = BitmapFactory.decodeByteArray(
                bytes, 0, bytes.length);
        RoundedBitmapDrawable rounded = RoundedBitmapDrawableFactory
                .create(context.getResources(), src);
        rounded.setCornerRadius(cornerRadius);
        return rounded;
    }
    public static Drawable circleDrawableFromBytes (Context context, byte[] bytes) {

        Bitmap src = BitmapFactory.decodeByteArray(
                bytes, 0, bytes.length);
        RoundedBitmapDrawable rounded = RoundedBitmapDrawableFactory
                .create(context.getResources(), src);
        rounded.setCircular(true);
        return rounded;
    }

    public static Bitmap bitmapFromBytes (byte[] bytes) {
        return BitmapFactory.decodeByteArray(
                bytes, 0, bytes.length);

    }
    public static Drawable roundedDrawableFromBitmap (Context context, Bitmap bitmap, float rad) {

        RoundedBitmapDrawable rounded = RoundedBitmapDrawableFactory
                .create(context.getResources(), bitmap);
        rounded.setCornerRadius(rad);
        return rounded;
    }
    public static int getRandomColor() {
        return ColorGenerator.MATERIAL.getRandomColor();
    }

    public static Drawable gmailRect(Context context, String contactName) {
        int cornerRadius = (int) context.getResources().getDimension(R.dimen.corner_radius);

        String letter = String.valueOf(contactName.charAt(0));
        return TextDrawable.builder()
                .buildRoundRect(letter, getRandomColor(), cornerRadius);
    }

    public static Drawable gmailRectWithColor(String contactName, int color) {
        String letter = String.valueOf(contactName.charAt(0));
//        int cornerRadius = (int) context.getResources().getDimension(R.dimen.corner_radius_small);

        return TextDrawable.builder()
                .buildRect(letter, color);
    }

    public static Drawable gmailRoundRectWithColor(String contactName, int color, float rad) {
        String letter = String.valueOf(contactName.charAt(0));

        return TextDrawable.builder()
                .buildRoundRect(letter, color, (int) rad);
    }

    public static Drawable gmaiRound(String contactName) {

        String letter = String.valueOf(contactName.charAt(0));
        return TextDrawable.builder()
                .buildRound(letter, getRandomColor());
    }

    public static Drawable gmailRoundWithColor(String contactName, int color) {
        String letter = String.valueOf(contactName.charAt(0));

        return TextDrawable.builder()
                .buildRound(letter, color);
    }

    public static Bitmap decodeUri(Context context, Uri selectedImage) throws FileNotFoundException {

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 140;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE
                    || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(context.getContentResolver().openInputStream(selectedImage), null, o2);
    }
}
