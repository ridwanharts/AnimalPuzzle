package com.labs.jangkriek.animalpuzzle.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.labs.jangkriek.animalpuzzle.util.AppConstants;

/**
 * The Database helper class to open the database and all related methods.
 * 
 * @author vishalbodkhe
 * 
 */
public class DBHelper extends SQLiteOpenHelper {

	public static int DATABASE_VERSION = 2;

	private static final String TABLE_NAME = "Scores";
	private static final String HINT_TAKEN = "hint_taken";
	private static final String MOVES = "moves";
	private static final String LEVEL = "level";
	private static final String TIME = "time";
	private static final String SECONDS = "seconds";

	private static DBHelper dbHelper;
	private SQLiteDatabase sqLiteDatabase;

	/**
	 * Private constructor
	 * 
	 * @param context
	 */
	private DBHelper(Context context) {
		super(context, AppConstants.DATABASE_NAME, null, DATABASE_VERSION);
		sqLiteDatabase = this.getWritableDatabase();
	}

	/**
	 * Method to get sqlite database
	 * 
	 * @return sqLiteDatabase
	 */
	public SQLiteDatabase getSqLiteDb() {
		return sqLiteDatabase;
	}

	/**
	 * Single instance of DB helper through application life.
	 * 
	 * @param context
	 * @return
	 */
	public static DBHelper getInstance(Context context) {
		if (dbHelper == null) {
			dbHelper = new DBHelper(context);
		}
		// if database is closed accidently.. it will reopen it here.
		if (!dbHelper.sqLiteDatabase.isOpen()) {
			dbHelper.sqLiteDatabase = dbHelper.getWritableDatabase();
		}

		return dbHelper;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		String sql = "CREATE TABLE " + TABLE_NAME + "("
				+ HINT_TAKEN +" VARCHAR,"
				+ MOVES +" VARCHAR,"
				+ LEVEL +" VARCHAR,"
				+ TIME +" VARCHAR,"
				+ SECONDS +" VARCHAR"
				+ ")";

		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onCreate(db);
	}

	/**
	 * 
	 * @param tableName
	 * @param contentValues
	 * @return row id
	 */
	public long insert(String tableName, ContentValues contentValues) {
		return sqLiteDatabase.insert(tableName, null, contentValues);
	}

	/**
	 * Update the database entry/ies
	 * 
	 * @param table
	 * @param values
	 * @param whereClause
	 * @param whereArgs
	 * @return number of rows affected
	 */
	public int update(String table, ContentValues values, String whereClause,
			String[] whereArgs) {
		return sqLiteDatabase.update(table, values, whereClause, whereArgs);
	}

	/**
	 * Delete the rows in the mentioned table/ empties the table depending upon
	 * the whereClause
	 * 
	 * @param table
	 * @param whereClause
	 * @param whereArgs
	 * @return number of rows affected
	 */
	public int delete(String table, String whereClause, String[] whereArgs) {
		return sqLiteDatabase.delete(table, whereClause, whereArgs);
	}

	/**
	 * Select query for the db operations.
	 * 
	 * @param table
	 * @param columns
	 * @param selection
	 * @param selectionArgs
	 * @param groupBy
	 * @param having
	 * @param orderBy
	 * @return cursor holding the data returned out of query
	 */
	public Cursor select(String table, String[] columns, String selection,
			String[] selectionArgs, String groupBy, String having,
			String orderBy) {
		return sqLiteDatabase.query(table, columns, selection, selectionArgs,
				groupBy, having, orderBy);
	}

	/**
	 * Close database
	 */
	public void closeDb() {
		sqLiteDatabase.close();
	}
}
