package hk.ust.cse.hunkim.questionroom;

import android.media.Image;
import android.net.Uri;
import android.test.AndroidTestCase;

import java.io.File;
import java.net.URISyntaxException;

import hk.ust.cse.hunkim.questionroom.db.DBHelper;
import hk.ust.cse.hunkim.questionroom.db.DBUtil;
import hk.ust.cse.hunkim.questionroom.db.ImageHelper;

/**
 * Created by hunkim on 7/15/15.
 */
public class DBUtilTest extends AndroidTestCase {

    DBUtil dbutil;

    public DBUtilTest() {
        super();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        // get the DB Helper
        DBHelper mDbHelper = new DBHelper(getContext());
        dbutil = new DBUtil(mDbHelper);
    }

    public void testPut () {
        String key = "1234";
        dbutil.put(key);
        assertTrue("Put the key", dbutil.contains(key));

        dbutil.delete(key);

        assertFalse("Key is deleted!", dbutil.contains(key));
    }

    public void testCompress() {
        ImageHelper.compressFile("/data/local/1.png");
        ImageHelper.compressFile("/data/local/2.png");
        ImageHelper.compressFile("/data/local/3.png");
    }
}
