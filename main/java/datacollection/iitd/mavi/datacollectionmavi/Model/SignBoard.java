package datacollection.iitd.mavi.datacollectionmavi.Model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import datacollection.iitd.mavi.datacollectionmavi.Helper.Constants;
import datacollection.iitd.mavi.datacollectionmavi.Helper.FileUtils;

/**
 * Created by nikhil on 2/3/17.
 */

public class SignBoard {

    private int mId;
    private String mName;
    private int mAngle;
    private String mLat;
    private String mLong;
    private String mImagePath;
    private String mComment;


    public String getComment() {
        return mComment;
    }

    public void setComment(String mComment) {
        this.mComment = mComment;
    }


    public int getId() {
        return mId;
    }
    public void setId(int id) {
        mId = id;
    }


    public String getName() {
        return mName;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public int getAngle() {
        return mAngle;
    }

    public void setAngle(int mAngle) {
        this.mAngle = mAngle;
    }

    public String getLat() {
        return mLat;
    }

    public void setLat(String mLat) {
        this.mLat = mLat;
    }

    public String getLong() {
        return mLong;
    }

    public void setLong(String mLong) {
        this.mLong = mLong;
    }

    public String getImagePath() {
        return mImagePath;
    }

    public void setImagePath(String mImagePath) {
        this.mImagePath = mImagePath;
    }

    public boolean hasImage() {

        return getImagePath() != null && !getImagePath().isEmpty();
    }

    /**
     * Get a thumbnail of this Signboards's picture, or a default image if the signbaord doesn't have a
     * Image.
     *
     * @return Thumbnail of the Signboard
     */

    public Drawable getThumbnail(Context context) {

        return getScaledImage(context, 128, 128);
    }



    /**
     * Get this Signbboards's picture, or a default image if the Signboarddoesn't have a Image.
     *
     * @return Image of the Signboard
     */
    public Drawable getImage(Context context) {

        return getScaledImage(context, 512, 512);
    }

    /**
     * Get a scaled version of this Signboard's Image, or a default image if the Signboard's doesn't have
     * a Image.
     *
     * @return Image of the Signboard
     */
    private Drawable getScaledImage(Context context, int reqWidth, int reqHeight) {

        // If Signboard has a Image.
        if (hasImage()) {

            // Decode the input stream into a bitmap.
            Bitmap bitmap = FileUtils.getResizedBitmap(getImagePath(), reqWidth, reqHeight);

            // If was successfully created.
            if (bitmap != null) {

                // Return a drawable representation of the bitmap.
                return new BitmapDrawable(context.getResources(), bitmap);
            }
        }

        // Return the default image drawable.
        return context.getResources().getDrawable(Constants.DEFAULT_IMAGE_RESOURCE);
    }
}
