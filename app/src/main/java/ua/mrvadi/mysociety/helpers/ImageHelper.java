package ua.mrvadi.mysociety.helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.io.ByteArrayOutputStream;

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

    public static int getRandomColor() {
        return ColorGenerator.MATERIAL.getRandomColor();
    }

    public static Drawable gmailRoundWithColor(String contactName, int color) {
        String letter = String.valueOf(contactName.charAt(0));

        return TextDrawable.builder()
                .buildRound(letter, color);
    }
}
