package hk.ust.cse.hunkim.questionroom.db;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Joel on 31/10/2015.
 */
public class ImageHelper {
    public static int RESULT_LOAD_IMG = 1;
    private final static int IMAGE_MAX_HEIGHT = 1200;
    private final static int IMAGE_MAX_WIDTH = 1600;
    private final static int IMAGE_QUALITY = 75;
    public static String picturePath = "";

    public static byte[] compressFile(String imagePath){
        // http://stackoverflow.com/a/3549021

        //Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, o);

        int scale = 1;
        if (o.outHeight > o.outWidth  && o.outHeight > IMAGE_MAX_HEIGHT) {
            scale = (int)Math.pow(2, (int) Math.ceil(Math.log(IMAGE_MAX_HEIGHT /
                    (double)o.outHeight) / Math.log(0.5)));
        }
        else if (o.outWidth > IMAGE_MAX_WIDTH) {
            scale = (int)Math.pow(2, (int) Math.ceil(Math.log(IMAGE_MAX_WIDTH /
                    (double)o.outWidth) / Math.log(0.5)));
        }

        //Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        Bitmap myImg =  BitmapFactory.decodeFile(imagePath, o2);

        // Compress the Image to jpeg to reduce image size to make upload easy
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        myImg.compress(Bitmap.CompressFormat.JPEG, IMAGE_QUALITY, stream);


        // Clear picturePath when done.
        picturePath = "";
        return stream.toByteArray();
    }
}
