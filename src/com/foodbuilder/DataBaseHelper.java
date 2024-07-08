package com.foodbuilder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
 
public class DataBaseHelper extends SQLiteOpenHelper{
 
    //The Android's default system path of your application database.
    String DB_PATH =null;
 
    private static String DB_NAME = "CompositionFoodTable_LatinAmerica";
 
    private SQLiteDatabase myDataBase; 
 
    private final Context myContext;
 
    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DataBaseHelper(Context context) {
 
        super(context, DB_NAME, null, 1);
        this.myContext = context;
        DB_PATH = context.getApplicationInfo().dataDir + "/databases/"; 
    }   
 
  /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException{
 
        boolean dbExist = checkDataBase();
 
        if(dbExist){
            //do nothing - database already exist
        }else{
 
            //By calling this method and empty database will be created into the default system path
               //of your application so we are going to be able to overwrite that database with our database.
            this.getReadableDatabase();
 
            try {
                copyDataBase();
 
            } catch (IOException e) {
 
                throw new Error("Error copying database");
 
            }
        }
 
    }
 
    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){
 
        SQLiteDatabase checkDB = null;
 
        try{
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
        }catch(SQLiteException e){
 
            //database does't exist yet.
 
        }
 
        if(checkDB != null){
 
            checkDB.close();
 
        }
 
        return checkDB != null ? true : false;
    }
 
    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transferring bytestream.
     * */
    private void copyDataBase() throws IOException{
 
        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);
 
        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;
 
        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);
 
        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }
 
        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
 
    }
 
    public void openDataBase() throws SQLException{
 
        //Open the database
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
    }
 
    @Override
    public synchronized void close() {
 
            if(myDataBase != null)
                myDataBase.close();
 
            super.close();
 
    }
 
    @Override
    public void onCreate(SQLiteDatabase db) {
 
    }
 
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
 
    }
    //return cursor
    public Cursor query(String table,String[] columns, String selection,String[] selectionArgs,String groupBy,String having,String orderBy){
        return myDataBase.query(table, null, null, null, null, null, null);
    }
    
    public Cursor queryForMultipleTablesEn(String query){
    	
    	String selectAll = null;
    	
    	if(query.length() > 1){
    	
    		selectAll = " SELECT * FROM (SELECT BABY_FOODS.ENGLISH_NAME " + " FROM BABY_FOODS " + "  WHERE BABY_FOODS.ENGLISH_NAME LIKE \'%" + query + "%\' UNION " +
    					" SELECT BAKED_PRODUCTS_AND_CORN_TORTILLAS.ENGLISH_NAME FROM BAKED_PRODUCTS_AND_CORN_TORTILLAS WHERE BAKED_PRODUCTS_AND_CORN_TORTILLAS.ENGLISH_NAME LIKE \'%" + query + "%\' UNION" +
    					" SELECT BEEF_PRODUCTS.ENGLISH_NAME FROM BEEF_PRODUCTS WHERE BEEF_PRODUCTS.ENGLISH_NAME LIKE \'%" + query + "%\' UNION" + 
    					" SELECT BEVERAGES.ENGLISH_NAME FROM BEVERAGES WHERE BEVERAGES.ENGLISH_NAME LIKE \'%" + query + "%\' UNION" +
    					" SELECT CEREAL_GRAINS_PASTA_AND_BREAKFAST_CEREALS.ENGLISH_NAME FROM CEREAL_GRAINS_PASTA_AND_BREAKFAST_CEREALS WHERE CEREAL_GRAINS_PASTA_AND_BREAKFAST_CEREALS.ENGLISH_NAME LIKE \'%" + query + "%\' UNION" +
    					" SELECT DAIRY_PRODUCTS.ENGLISH_NAME FROM DAIRY_PRODUCTS WHERE DAIRY_PRODUCTS.ENGLISH_NAME LIKE \'%" + query + "%\' UNION" +
    					" SELECT DESSERTS_PASTRY_AND_OTHER.ENGLISH_NAME FROM DESSERTS_PASTRY_AND_OTHER WHERE DESSERTS_PASTRY_AND_OTHER.ENGLISH_NAME LIKE \'%" + query + "%\' UNION" +
    					" SELECT EGG_PRODUCTS.ENGLISH_NAME FROM EGG_PRODUCTS WHERE EGG_PRODUCTS.ENGLISH_NAME LIKE \'%" + query + "%\' UNION" +
    					" SELECT FAST_FOODS_SNACKS_AND_OTHER.ENGLISH_NAME FROM FAST_FOODS_SNACKS_AND_OTHER WHERE FAST_FOODS_SNACKS_AND_OTHER.ENGLISH_NAME LIKE \'%" + query + "%\' UNION" +
    					" SELECT FATS_AND_OILS.ENGLISH_NAME FROM FATS_AND_OILS WHERE FATS_AND_OILS.ENGLISH_NAME LIKE \'%" + query + "%\' UNION" +
    					" SELECT FINFISH_AND_SHELLFISH_PRODUCTS.ENGLISH_NAME FROM FINFISH_AND_SHELLFISH_PRODUCTS WHERE FINFISH_AND_SHELLFISH_PRODUCTS.ENGLISH_NAME LIKE \'%" + query + "%\' UNION" +
    					" SELECT FRUITS_AND_FRUIT_JUICE.ENGLISH_NAME FROM FRUITS_AND_FRUIT_JUICE WHERE FRUITS_AND_FRUIT_JUICE.ENGLISH_NAME LIKE \'%" + query + "%\' UNION" +
    					" SELECT GAME_PRODUCTS.ENGLISH_NAME FROM GAME_PRODUCTS WHERE GAME_PRODUCTS.ENGLISH_NAME LIKE \'%" + query + "%\' UNION" +
    					" SELECT LEGUMES_AND_LEGUME_PRODUCTS.ENGLISH_NAME FROM LEGUMES_AND_LEGUME_PRODUCTS WHERE LEGUMES_AND_LEGUME_PRODUCTS.ENGLISH_NAME LIKE \'%" + query + "%\' UNION" +
    					" SELECT NUT_AND_SEED_PRODUCTS.ENGLISH_NAME FROM NUT_AND_SEED_PRODUCTS WHERE NUT_AND_SEED_PRODUCTS.ENGLISH_NAME LIKE \'%" + query + "%\' UNION" +
    					" SELECT PORK_PRODUCTS.ENGLISH_NAME FROM PORK_PRODUCTS WHERE PORK_PRODUCTS.ENGLISH_NAME LIKE \'%" + query + "%\' UNION" +
    					" SELECT POULTRY_PRODUCTS.ENGLISH_NAME FROM POULTRY_PRODUCTS WHERE POULTRY_PRODUCTS.ENGLISH_NAME LIKE \'%" + query + "%\' UNION" +
    					" SELECT SAUSAGES_AND_LUNCHEON_MEATS.ENGLISH_NAME FROM SAUSAGES_AND_LUNCHEON_MEATS WHERE SAUSAGES_AND_LUNCHEON_MEATS.ENGLISH_NAME LIKE \'%" + query + "%\' UNION" +
    					" SELECT SOUPS_SAUCES_AND_GRAVIES.ENGLISH_NAME FROM SOUPS_SAUCES_AND_GRAVIES WHERE SOUPS_SAUCES_AND_GRAVIES.ENGLISH_NAME LIKE \'%" + query + "%\' UNION" +
    					" SELECT SPICES_AND_HERBS.ENGLISH_NAME FROM SPICES_AND_HERBS WHERE SPICES_AND_HERBS.ENGLISH_NAME LIKE \'%" + query + "%\' UNION" +
    					" SELECT SWEETS.ENGLISH_NAME FROM SWEETS WHERE SWEETS.ENGLISH_NAME LIKE \'%" + query + "%\' UNION" +
    					" SELECT VEGETABLES_AND_VEGETABLE_PRODUCTS.ENGLISH_NAME FROM VEGETABLES_AND_VEGETABLE_PRODUCTS WHERE VEGETABLES_AND_VEGETABLE_PRODUCTS.ENGLISH_NAME LIKE \'%" + query + "%\' " + ")" + ";";
    	
    	} else {
    		selectAll = " SELECT * FROM (SELECT BABY_FOODS.ENGLISH_NAME " + " FROM BABY_FOODS " + "  WHERE BABY_FOODS.ENGLISH_NAME LIKE \'" + query + "%\' UNION " +
					" SELECT BAKED_PRODUCTS_AND_CORN_TORTILLAS.ENGLISH_NAME FROM BAKED_PRODUCTS_AND_CORN_TORTILLAS WHERE BAKED_PRODUCTS_AND_CORN_TORTILLAS.ENGLISH_NAME LIKE \'" + query + "%\' UNION" +
					" SELECT BEEF_PRODUCTS.ENGLISH_NAME FROM BEEF_PRODUCTS WHERE BEEF_PRODUCTS.ENGLISH_NAME LIKE \'" + query + "%\' UNION" + 
					" SELECT BEVERAGES.ENGLISH_NAME FROM BEVERAGES WHERE BEVERAGES.ENGLISH_NAME LIKE \'" + query + "%\' UNION" +
					" SELECT CEREAL_GRAINS_PASTA_AND_BREAKFAST_CEREALS.ENGLISH_NAME FROM CEREAL_GRAINS_PASTA_AND_BREAKFAST_CEREALS WHERE CEREAL_GRAINS_PASTA_AND_BREAKFAST_CEREALS.ENGLISH_NAME LIKE \'" + query + "%\' UNION" +
					" SELECT DAIRY_PRODUCTS.ENGLISH_NAME FROM DAIRY_PRODUCTS WHERE DAIRY_PRODUCTS.ENGLISH_NAME LIKE \'" + query + "%\' UNION" +
					" SELECT DESSERTS_PASTRY_AND_OTHER.ENGLISH_NAME FROM DESSERTS_PASTRY_AND_OTHER WHERE DESSERTS_PASTRY_AND_OTHER.ENGLISH_NAME LIKE \'" + query + "%\' UNION" +
					" SELECT EGG_PRODUCTS.ENGLISH_NAME FROM EGG_PRODUCTS WHERE EGG_PRODUCTS.ENGLISH_NAME LIKE \'" + query + "%\' UNION" +
					" SELECT FAST_FOODS_SNACKS_AND_OTHER.ENGLISH_NAME FROM FAST_FOODS_SNACKS_AND_OTHER WHERE FAST_FOODS_SNACKS_AND_OTHER.ENGLISH_NAME LIKE \'" + query + "%\' UNION" +
					" SELECT FATS_AND_OILS.ENGLISH_NAME FROM FATS_AND_OILS WHERE FATS_AND_OILS.ENGLISH_NAME LIKE \'" + query + "%\' UNION" +
					" SELECT FINFISH_AND_SHELLFISH_PRODUCTS.ENGLISH_NAME FROM FINFISH_AND_SHELLFISH_PRODUCTS WHERE FINFISH_AND_SHELLFISH_PRODUCTS.ENGLISH_NAME LIKE \'" + query + "%\' UNION" +
					" SELECT FRUITS_AND_FRUIT_JUICE.ENGLISH_NAME FROM FRUITS_AND_FRUIT_JUICE WHERE FRUITS_AND_FRUIT_JUICE.ENGLISH_NAME LIKE \'" + query + "%\' UNION" +
					" SELECT GAME_PRODUCTS.ENGLISH_NAME FROM GAME_PRODUCTS WHERE GAME_PRODUCTS.ENGLISH_NAME LIKE \'" + query + "%\' UNION" +
					" SELECT LEGUMES_AND_LEGUME_PRODUCTS.ENGLISH_NAME FROM LEGUMES_AND_LEGUME_PRODUCTS WHERE LEGUMES_AND_LEGUME_PRODUCTS.ENGLISH_NAME LIKE \'" + query + "%\' UNION" +
					" SELECT NUT_AND_SEED_PRODUCTS.ENGLISH_NAME FROM NUT_AND_SEED_PRODUCTS WHERE NUT_AND_SEED_PRODUCTS.ENGLISH_NAME LIKE \'" + query + "%\' UNION" +
					" SELECT PORK_PRODUCTS.ENGLISH_NAME FROM PORK_PRODUCTS WHERE PORK_PRODUCTS.ENGLISH_NAME LIKE \'" + query + "%\' UNION" +
					" SELECT POULTRY_PRODUCTS.ENGLISH_NAME FROM POULTRY_PRODUCTS WHERE POULTRY_PRODUCTS.ENGLISH_NAME LIKE \'" + query + "%\' UNION" +
					" SELECT SAUSAGES_AND_LUNCHEON_MEATS.ENGLISH_NAME FROM SAUSAGES_AND_LUNCHEON_MEATS WHERE SAUSAGES_AND_LUNCHEON_MEATS.ENGLISH_NAME LIKE \'" + query + "%\' UNION" +
					" SELECT SOUPS_SAUCES_AND_GRAVIES.ENGLISH_NAME FROM SOUPS_SAUCES_AND_GRAVIES WHERE SOUPS_SAUCES_AND_GRAVIES.ENGLISH_NAME LIKE \'" + query + "%\' UNION" +
					" SELECT SPICES_AND_HERBS.ENGLISH_NAME FROM SPICES_AND_HERBS WHERE SPICES_AND_HERBS.ENGLISH_NAME LIKE \'" + query + "%\' UNION" +
					" SELECT SWEETS.ENGLISH_NAME FROM SWEETS WHERE SWEETS.ENGLISH_NAME LIKE \'" + query + "%\' UNION" +
					" SELECT VEGETABLES_AND_VEGETABLE_PRODUCTS.ENGLISH_NAME FROM VEGETABLES_AND_VEGETABLE_PRODUCTS WHERE VEGETABLES_AND_VEGETABLE_PRODUCTS.ENGLISH_NAME LIKE \'" + query + "%\' " + ")" + ";";
    	}
    	
    	
        return myDataBase.rawQuery(selectAll, null);
    }
}