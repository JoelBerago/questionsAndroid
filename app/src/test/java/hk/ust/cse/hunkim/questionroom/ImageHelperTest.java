package hk.ust.cse.hunkim.questionroom;

import android.test.AndroidTestCase;
import android.test.suitebuilder.annotation.SmallTest;

import junit.framework.TestCase;

import hk.ust.cse.hunkim.questionroom.db.ImageHelper;

/**
 * Created by Joel on 22/11/2015.
 */
public class ImageHelperTest extends TestCase {
    @SmallTest
    public void testCompress() {
        // Test large portrait
        ImageHelper.compressFile("/data/local/tmp/1.png");

        // Test large landscape
        ImageHelper.compressFile("/data/local/tmp/2.png");

        // Test small picture
        ImageHelper.compressFile("/data/local/tmp/3.png");
    }
}
