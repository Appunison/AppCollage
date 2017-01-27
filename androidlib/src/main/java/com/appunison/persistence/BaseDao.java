package com.appunison.persistence;

import java.util.List;
import java.util.Map;

import com.appunison.log.AndroidLibLogger;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * This Clas is Base class of all the DAo 
 * to perform add update and Delete using 
 * Android API 
 * @author appunison
 *
 */
public class BaseDao {

	private SQLiteOpenHelper sqliteDb;
	private SQLiteDatabase db;
	
	public SQLiteDatabase getDb() {
		return db;
	}



	public void setDb(SQLiteDatabase db) {
		this.db = db;
	}



	public SQLiteOpenHelper getSqliteDb() {
		return sqliteDb;
	}



	public void setSqliteDb(SQLiteOpenHelper sqliteDb) {
		this.sqliteDb = sqliteDb;
	}



	public BaseDao(SQLiteOpenHelper sqliteOpenHelper)
	{
		
		this.sqliteDb=sqliteOpenHelper;
	}

	
	
	/**
	 * To insert Data in Table
	 * @param tableName
	 * @param contentValues
	 * @return
	 */
	public boolean addTable(String tableName,ContentValues contentValues)
	{
		
		try
		{
			db=sqliteDb.getWritableDatabase();
			db.insert(tableName, null, contentValues);
		}
		catch(SQLiteException ex){
			AndroidLibLogger.e("SqliteException", ex.getMessage());
			return Boolean.FALSE;
		}
		finally
		{
			//db.close();
		}
		return Boolean.TRUE;
	}
	/**
	 * To update field value in table
	 * @param tableName
	 * @param update
	 * @return
	 */
	
	public boolean  updateTable(String tableName,List<Map<String,?>> update)
	{
		

		return Boolean.TRUE;
	}
	/**
	 * to update data in one colom of row
	 * @param update colom
	 * @return
	 */
	
	public int updateTableThroughValue(String table,ContentValues cv,String whereClause,String[] values) 
    {  try
    {
		db=sqliteDb.getReadableDatabase();
		int row =db.update(table, cv, whereClause, values);
	
		if(row>0){
			return row;
		}else{
			return 0;
		}
    }
		catch(SQLiteException sql)
		{
		  AndroidLibLogger.d("kitzzzzz","update exception="+sql.getMessage());	
		}
	return 0;
		}
   
	
	
	/**
	 * To Retrieve all data of a table
	 * @param tableName
	 * @return 
	 */
	public  Cursor retrieveAllData(String tablename)
	{
		 Cursor cursor=null;
		 String selectQuery = "SELECT  * FROM " + tablename;
		     try{
		    	 	db=sqliteDb.getReadableDatabase();
		    	  	cursor = db.rawQuery(selectQuery, null);
		      }
		     catch(SQLiteException ex){
					AndroidLibLogger.e("SqliteException", ex.getMessage());
					return null;
			}
		        
			return cursor;
		  
	}

	/**
	 * To Retrieve data with where clause of a table 
	 * by executing rawQuery()
	  * @param executeQuery
	 * @return 
	 */
	
	public Cursor retrieveDataByExecQuery(String executeQuery)
	{
		 Cursor cursor=null;
		
		     try{
		    	 	db=sqliteDb.getReadableDatabase();
		    	 	cursor = db.rawQuery(executeQuery, null);
		     }
		     catch(SQLiteException ex){
					AndroidLibLogger.e("SqliteException", ex.getMessage());
					return null;
				}
				/*finally
				{
					db.close();
				}*/
				return cursor;
	}
	
	
	
	/**
	 * To Retrieve data with fieldname with query() method of Sqlite 
	 * @param exectquery
	 * @return 
	 */
	
	public Cursor retrieveDataByFields(String tablename,String[] fields,String selection,String[] selectionArgs )
	{
		 Cursor cursor=null;
		
		     try{
		    	 	db=sqliteDb.getReadableDatabase();
		    	 	
		         cursor=  db.query(tablename, fields, selection, selectionArgs, null, null, null);
		        
		     }catch(SQLiteException ex){
					AndroidLibLogger.e("SqliteException", ex.getMessage());
					return null;
				}
				
				return cursor;
	}
	
	public boolean deleteDataByExecQuery(String executeQuery)
	{
		try
		{
			db=sqliteDb.getReadableDatabase();
			
			db.execSQL(executeQuery);
		}
		catch(SQLiteException ex){
			AndroidLibLogger.e("SqliteException", ex.getMessage());
			return false;
		}
		
		
		return true;
	}
	public int sqlDelete(String tableName,String where,String[] whereArg)
	{
		int rows;
		try
		{
			db=sqliteDb.getReadableDatabase();
			 rows=db.delete(tableName, where, whereArg);
			
			
		}
		catch(SQLiteException ex){
			AndroidLibLogger.e("SqliteException", ex.getMessage());
			return 0;
		}
		
		
		return rows;
	}
	
	/**
	 * to update by query
	 */
	/*public Cursor updateByQuery()
	{
		db.query(table, columns, selection, selectionArgs, groupBy, having, orderBy)
		return null;
	}
	*/
	
  

}
