package datacollection.iitd.mavi.datacollectionmavi.Helper;

import android.os.Environment;


import java.io.File;

import datacollection.iitd.mavi.datacollectionmavi.R;

/**
 * Created by Valentine on 4/10/2015.
 */
public class Constants {

    public static final String ARG_SECTION_NUMBER = "section_number";
//    public static final String ARG_CUST_ID = "customer_id";


    public static final String COLUMN_SIGNBOARD_ID = "_id";
    public static final String COLUMN_NAME= "name";
    public static final String COLUMN_ANGLE = "angle";
    public static final String COLUMN_LAT = "lat";
    public static final String COLUMN_LONG = "lng";
    public static final String COLUMN_COMMENT = "comment";
    public static final String COLUMN_IMAGE_PATH = "imagePath";
    public static final String COLUMN_CATEGORY= "category";
    public static final String COLUMN_PUSHEDTOSERVER= "server";

    public static final String KEY_IMAGE_URI = "image_uri";

    public static final String KEY_IMAGE_PATH = "key_image_path";
    public static int ACTION_REQUEST_IMAGE = 1000;
    public static int SELECT_IMAGE = 1001;
    public static final String TAKE_PHOTO = "Take Photo";
    public static final String CHOOSE_FROM_GALLERY = "Choose from gallery";
    public static final String CANCEL= "Cancel";

    public static final int DEFAULT_IMAGE_RESOURCE = R.drawable.default_customer_picture;
    public static final String PICTURE_DIRECTORY = Environment.getExternalStorageDirectory()
            + File.separator + "DCIM" + File.separator + "ProfilePicture" + File.separator;

    public static final String LOGIN_URL="/mavi/api/obtain-token/";
    public static final String DATA_URL="/mavi/api/signboards";

}
