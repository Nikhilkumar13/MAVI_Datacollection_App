package datacollection.iitd.mavi.datacollectionmavi.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import datacollection.iitd.mavi.datacollectionmavi.Helper.Constants;
import datacollection.iitd.mavi.datacollectionmavi.Model.SignBoard;

/**
 * Created by nikhil on 28/2/17.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {

    private Context mContext;
    private static final String DATABASE_NAME = "signboard.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_SIGNBOARD = "Signboard";
    private final static String TAG = MySQLiteHelper.class.getSimpleName();
    // Database lock to prevent conflicts.

    public static final Object[] databaseLock = new Object[0];



    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_SIGNBOARD_TABLE);


    }

    private static final String CREATE_SIGNBOARD_TABLE = "CREATE TABLE " + TABLE_SIGNBOARD + " ("
            + Constants.COLUMN_SIGNBOARD_ID + " INTEGER PRIMARY KEY, "
            + Constants.COLUMN_NAME + " TEXT, "
            + Constants.COLUMN_ANGLE + " INTEGER, "
            + Constants.COLUMN_LAT + " TEXT, "
            + Constants.COLUMN_IMAGE_PATH + " TEXT, "
            + Constants.COLUMN_COMMENT + " TEXT, "
            + Constants.COLUMN_LONG + " TEXT) ";
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SIGNBOARD);


    }

    public List<SignBoard> getAllCustomers() {
        //Initialize an empty list of Customers
        List<SignBoard> signboardList = new ArrayList<SignBoard>();

        //Command to select all Customers
        String selectQuery = "SELECT * FROM " + TABLE_SIGNBOARD;

        //lock database for reading
        synchronized (databaseLock) {
            //Get a readable database
            SQLiteDatabase database = getReadableDatabase();

            //Make sure database is not empty
            if (database != null) {

                //Get a cursor for all Customers in the database
                Cursor cursor = database.rawQuery(selectQuery, null);
                if (cursor.moveToFirst()) {
                    while (!cursor.isAfterLast()) {
                        SignBoard  signboard = getSignboard(cursor);
                        signboardList.add(signboard);
                        cursor.moveToNext();
                    }
                }
                //Close the database connection
                database.close();
            }
            //Return the list of customers
            return signboardList;
        }

    }

    private static SignBoard getSignboard(Cursor cursor) {
        SignBoard customer = new SignBoard();
        customer.setId(cursor.getInt(cursor.getColumnIndex(Constants.COLUMN_SIGNBOARD_ID)));
        customer.setName(cursor.getString(cursor.getColumnIndex(Constants.COLUMN_NAME)));
        customer.setAngle(cursor.getInt(cursor.getColumnIndex(Constants.COLUMN_ANGLE)));
        customer.setComment(cursor.getString(cursor.getColumnIndex(Constants.COLUMN_COMMENT)));
        customer.setLat(cursor.getString(cursor.getColumnIndex(Constants.COLUMN_LAT)));
        customer.setLong(cursor.getString(cursor.getColumnIndex(Constants.COLUMN_LONG)));
        customer.setImagePath(cursor.getString(cursor.getColumnIndex(Constants.COLUMN_IMAGE_PATH)));
        return customer;
    }

    public Long addSignboard(SignBoard signBoard) {
        Long ret = null;

        //Lock database for writing
        synchronized (databaseLock) {
            //Get a writable database
            SQLiteDatabase database = getWritableDatabase();

            //Ensure the database exists
            if (database != null) {
                //Prepare the customer information that will be saved to the database
                ContentValues values = new ContentValues();
                values.put(Constants.COLUMN_NAME, signBoard.getName());
                values.put(Constants.COLUMN_COMMENT, signBoard.getComment());
                values.put(Constants.COLUMN_ANGLE, signBoard.getAngle());
                values.put(Constants.COLUMN_LAT, signBoard.getLat());
                values.put(Constants.COLUMN_LONG, signBoard.getLong());
                values.put(Constants.COLUMN_IMAGE_PATH, signBoard.getImagePath());

                //Attempt to insert the client information into the transaction table
                try {
                    ret = database.insert(TABLE_SIGNBOARD, null, values);
                } catch (Exception e) {
                    Log.e(TAG, "Unable to add Customer to database " + e.getMessage());
                }
                //Close database connection
                database.close();
            }
        }
        return ret;
    }
    public SignBoard getCustomerById(long id){
        List<SignBoard> tempCustomerList = getAllCustomers();
        for (SignBoard customer : tempCustomerList){
            if (customer.getId() == id){
                return customer;
            }
        }
        return null;
    }
    public boolean  signboardExists(long id){
        //Check if there is an existing customer
        List<SignBoard> tempCustomerList = getAllCustomers();
        for (SignBoard customer : tempCustomerList){
            if (customer.getId() == id){
                return true;
            }
        }
        return false;
    }
}
