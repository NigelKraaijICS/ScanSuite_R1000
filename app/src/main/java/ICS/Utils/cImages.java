package ICS.Utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.util.Base64;

public class cImages {

    public static Drawable pConvertToGrayscale(Drawable drawable)
    {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);

        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);

        drawable.setColorFilter(filter);

        return drawable;
    }

    public static Bitmap pBase64ToImage(String imageString) {

        Bitmap decodedByte = null;
        try {
            byte[] decodedString = Base64.decode(imageString, Base64.DEFAULT);
            decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decodedByte;
    }

}
