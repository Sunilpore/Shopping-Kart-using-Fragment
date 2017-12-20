package com.example.sunil.cartadd.Database;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.sunil.cartadd.Model.CartModel;
import com.example.sunil.cartadd.Model.ProductModel;
import com.example.sunil.cartadd.Model.UserModel;
import com.example.sunil.cartadd.Singleton.PreferenceHelper;

import java.util.ArrayList;

/**
 * Created by Sunil on 11/10/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    public static final String MyprefK = "Prefkey";
    public static final String UserIDK = "UserIDkey";

    PreferenceHelper sp;
//    SharedPreferences sp;
//    SharedPreferences.Editor ed;

    private static String tag = "myTag";
    Context mContext;

    //Create Database Version
    public static final int DATABASE_VERSION = 1;

    //Define Database Name
    public static final String DATABASE_NAME = "User.db";

    //Create Table
    public static final String TABLE_NAME_USER = "user_table";
    public static final String TABLE_NAME_PROD = "product_table";
    public static final String TABLE_NAME_CART = "cart_table";

    //Create coloumns

    //For Table 1
    public static final String COL_ID = "USER_ID";
    public static final String COL_FULLNAME = "FULLNAME";
    public static final String COL_USERNAME = "NAME";
    public static final String COL_PASS = "PASS";

    //For Table 2
    public static final String COL_PROD_ID = "PRODUCT_ID";
    public static final String COL_PROD_NAME = "PRODUCT_NAME";
    public static final String COL_PROD_PRICE = "PRODUCT_PRICE";
    public static final String COL_PROD_CATEGORY = "PRODUCT_CATEGORY";


    //For Table 3
    public static final String COL_CART_ID = "CART_ID";
    public static final String COL_CART_QUANTITY = "CART_QTY";
    public static final String COL_CART_USERID = "CART_USERID";
    public static final String COL_CART_PRODID = "CART_PRODID";

    //Here pass 'DATABASE_NAME' instead of 'TABLE_NAME'
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.mContext = context;
    }


    //Create query
    @Override
    public void onCreate(SQLiteDatabase db) {

        //Define query For Table 1
        String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_USER +
                "(" + COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COL_FULLNAME + " TEXT,"
                + COL_USERNAME + " TEXT UNIQUE,"
                + COL_PASS + " TEXT" + ")";

        //Define query For Table 2
        String CREATE_TABLE_PROD = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_PROD +
                "(" + COL_PROD_ID + " INTEGER PRIMARY KEY,"
                + COL_PROD_NAME + " TEXT,"
                + COL_PROD_PRICE + " INT,"
                + COL_PROD_CATEGORY + " TEXT" + ")";

        //Define query For Table 3
        String CREATE_TABLE_CART = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_CART +
                "(" + COL_CART_ID + " INTEGER PRIMARY KEY,"
                + COL_CART_QUANTITY + " INTEGER DEFAULT '1',"
                + COL_CART_USERID + " INTEGER,"
                + COL_CART_PRODID + " INTEGER,"
                + " FOREIGN KEY " + "(" + COL_CART_USERID + ")" + " REFERENCES " + TABLE_NAME_USER + "(" + COL_ID + ")"
                + " FOREIGN KEY " + "(" + COL_CART_PRODID + ")" + " REFERENCES " + TABLE_NAME_PROD + "(" + COL_PROD_ID + ")" + ")";

        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_PROD);
        db.execSQL(CREATE_TABLE_CART);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_USER);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_PROD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_CART);
        onCreate(db);
    }


        /*-----------------------------------*-*-*-----------------------------------*/
                      /*Add User,Product,Cart Data */

    //Add data for UserModel For signin
    public boolean addUserData(UserModel umd) {
        boolean addCheck = false;

        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues value = new ContentValues();
            value.put(COL_FULLNAME, umd.getFullname());
            value.put(COL_USERNAME, umd.getUname());
            value.put(COL_PASS, umd.getPass());

            long result = db.insertOrThrow(TABLE_NAME_USER, null, value);
            if (result == -1)
                addCheck = false;
            else
                addCheck = true;

        } catch (android.database.sqlite.SQLiteConstraintException e) {
            Toast.makeText(mContext, "User name Already Exist", Toast.LENGTH_LONG).show();
        }
        return addCheck;
    }


    public boolean addProductData(ProductModel pmd) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues value = new ContentValues();
        value.put(COL_PROD_NAME, pmd.getProdname());
        value.put(COL_PROD_PRICE, pmd.getProdprice());
        value.put(COL_PROD_CATEGORY, pmd.getProdcat());

        long result = db.insert(TABLE_NAME_PROD, null, value);
        if (result == -1)
            return false;
        else
            return true;

    }


    public boolean addtoCart(CartModel cmd) {

        boolean addCheck = false;

        try {

            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues value = new ContentValues();
            value.put(COL_CART_QUANTITY, cmd.getCartquantity());
            value.put(COL_CART_USERID, cmd.getUserid());
            value.put(COL_CART_PRODID, cmd.getProdid());

            long result = db.insertOrThrow(TABLE_NAME_CART, null, value);
            if (result == -1)
                addCheck = false;
            else
                addCheck = true;

        } catch (android.database.sqlite.SQLiteConstraintException e) {
            Toast.makeText(mContext, "Product already added into the Cart", Toast.LENGTH_LONG).show();
        }

        return addCheck;
    }

    /*-----------------------------------*-*-*-----------------------------------*/
                       /*Verify user credential at Login Page*/

    public Cursor getAllUserData(String username, String password) {

        SQLiteDatabase db = this.getWritableDatabase();

        String loginQuery = "SELECT " + COL_ID + " FROM " + TABLE_NAME_USER + " WHERE " + COL_USERNAME + " = ? AND " + COL_PASS + " = ?";

        Cursor cur = db.rawQuery(loginQuery, new String[]{username, password});


        return cur;
    }


    //This method is call for view Login user's SignUp Record on Login page
    public Cursor getAllUserData() {

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cur = db.rawQuery("select * from " + TABLE_NAME_USER, null);
        return cur;
    }


    /*-----------------------------------*-*-*-----------------------------------*/
                      /*Update Cart Quantity for individual item*/

    public boolean qtyUpdate(CartModel cmd) {

        SQLiteDatabase db = this.getWritableDatabase();

        //Ref 1

        ContentValues value = new ContentValues();
        value.put(COL_CART_QUANTITY, cmd.getCartquantity());
        //Log.d(tag, "" + cmd.getCartid());
        long result = db.update(TABLE_NAME_CART, value, COL_CART_ID + "=?", new String[]{String.valueOf(cmd.cartid)});
        if (result == -1)
            return false;
        else
            return true;

    }


    public boolean cartItemdelete(int cartid) {

        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(TABLE_NAME_CART, "CART_ID=?", new String[]{String.valueOf(cartid)});
        if (result > 0)
            return true;
        else
            return false;
    }


                /*-----------------------------------*-*-*-----------------------------------*/
    /*Get Cart item data to Display on Screen.Used for cart item count on HomePage.Use for Total cost in Cartview  */


    //M 3.1, (Date 6 Dec 17) Set ProductList on Adapter without using fragment


    public Cursor getAllCartData() {

//        sp = mContext.getSharedPreferences(MyprefK, Context.MODE_PRIVATE);
//        ed = sp.edit();
        sp = PreferenceHelper.getmInstance(mContext);

        int useridSP = sp.getUserID(UserIDK, 0);

        SQLiteDatabase db = this.getWritableDatabase();

        String cartJointQuery = "SELECT * FROM " + TABLE_NAME_CART
                + " JOIN " + TABLE_NAME_USER
                + " ON " + TABLE_NAME_CART + "." + COL_CART_USERID + " = " + TABLE_NAME_USER + "." + COL_ID
                + " JOIN " + TABLE_NAME_PROD
                + " ON " + TABLE_NAME_CART + "." + COL_CART_PRODID + " = " + TABLE_NAME_PROD + "." + COL_PROD_ID
                + " WHERE " + COL_ID + " =?";

        //Important step.At here we can use UserID in where clause for individual record
        Cursor cur = db.rawQuery(cartJointQuery, new String[]{String.valueOf(useridSP)});
        return cur;
    }

    //M 3.2, (Date 6 Dec 17) Set ProductList on Adapter without using fragment


    public ArrayList<CartModel> getCartData(/*int useridSP*/) {

        Cursor cur = getAllCartData();

        //M 4.1, Setter method
        ProductModel pmd;
        ArrayList<CartModel> cartlist = new ArrayList<>();

        if (cur != null) {
            while (cur.moveToNext()) {

                int productid = cur.getInt(cur.getColumnIndex(COL_PROD_ID));
                String cartProdname = cur.getString(cur.getColumnIndex(COL_PROD_NAME));
                int cartProdprize = cur.getInt(cur.getColumnIndex(COL_PROD_PRICE));
                int cartItemQty = cur.getInt(cur.getColumnIndex(COL_CART_QUANTITY));
                int cartid = cur.getInt(cur.getColumnIndex(COL_CART_ID));
               // int productid = cur.getInt(cur.getColumnIndex(COL_CART_PRODID));

                // M 4.2, Setter method

                //    Log.d(tag, cartProdname);

                pmd = new ProductModel(productid, cartProdname, cartProdprize);
                cartlist.add(new CartModel(pmd, cartItemQty, cartid));
            }
        }
        return cartlist;
    }


    // CartView.Ref 1
    public int getCartTotalPrice() {

        Cursor cur = getAllCartData();

        int getTotalPrice = 0;
        if (cur != null) {
            while (cur.moveToNext()) {

                int cartProdprize = cur.getInt(cur.getColumnIndex(COL_PROD_PRICE));
                int cartItemQty = cur.getInt(cur.getColumnIndex(COL_CART_QUANTITY));

                getTotalPrice = cartProdprize * cartItemQty + getTotalPrice;
            }
        }

        return getTotalPrice;
    }

    public int cartCount(int userId) {
        int count = 0;
        SQLiteDatabase db = this.getReadableDatabase();

        String queryCount = "SELECT " + COL_CART_ID + " FROM " + TABLE_NAME_CART + " WHERE " + COL_CART_USERID + " =" + userId;

        Cursor cur = db.rawQuery(queryCount, null);

        count = cur.getCount();

        return count;
    }


              /*-----------------------------------*-*-*-----------------------------------*/
                                    /*Product List using Fragment*/

    public Cursor getAllelecdProductData() {

//        sp = mContext.getSharedPreferences(MyprefK, Context.MODE_PRIVATE);
//        ed = sp.edit();
        sp = PreferenceHelper.getmInstance(mContext);

        int useridSP = sp.getUserID(UserIDK, 0);

        SQLiteDatabase db = this.getWritableDatabase();
       // Cursor cur = db.rawQuery("select * from " + TABLE_NAME_PROD + " WHERE " + COL_PROD_CATEGORY + " =" + "'ELECTRONICS'", null);

        //Left JOIN and INNER QUERY use for Disable ADD button in Homepage. It disable ADD Button to those item which already present in CartView
        Cursor cur=db.rawQuery("select * from " + TABLE_NAME_PROD + " LEFT JOIN "
                + "(select * from " + TABLE_NAME_CART + " WHERE " + COL_CART_USERID + " = " +useridSP + ")"
                + " ON " + COL_PROD_ID + " = " + COL_CART_PRODID + " WHERE " + COL_PROD_CATEGORY + " = " + "'ELECTRONICS'",null);

        return cur;
    }

    public ArrayList<CartModel> getElectronicsProduct() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = getAllelecdProductData();

        ArrayList<CartModel> mlist = new ArrayList<>();
        ProductModel pmd;

        if (cur != null) {

            cur.moveToFirst();
            do {
                int productid = cur.getInt(cur.getColumnIndex(COL_PROD_ID));
                String prodname = cur.getString(cur.getColumnIndex(COL_PROD_NAME));
                int prodprice = cur.getInt(cur.getColumnIndex(COL_PROD_PRICE));
                int cartId = cur.getInt(cur.getColumnIndex(COL_CART_ID));
                int cartProdId = cur.getInt(cur.getColumnIndex(COL_CART_PRODID));

                /*String checkAdded=cur.getString(cur.getColumnIndex(COL_CART_ID));
                boolean bt;
                if(checkAdded==null)
                    bt=false;
                else
                    bt=true;*/
                //Ref 2



                pmd =new ProductModel(productid, prodname, prodprice);
                mlist.add(new CartModel(cartId,cartProdId,pmd));
                // mlist.add(new ProductModel(productid, prodname, prodprice,bt));
            } while (cur.moveToNext());
        }
        return mlist;
    }


    public Cursor getAllgroceryProductData() {

//        sp = mContext.getSharedPreferences(MyprefK, Context.MODE_PRIVATE);
//        ed = sp.edit();
        sp = PreferenceHelper.getmInstance(mContext);

        int useridSP = sp.getUserID(UserIDK, 0);

        SQLiteDatabase db = this.getWritableDatabase();
       // Cursor cur = db.rawQuery("select * from " + TABLE_NAME_PROD + " WHERE " + COL_PROD_CATEGORY + " =" + "'GROCERY'", null);

        Cursor cur=db.rawQuery("SELECT * FROM " + TABLE_NAME_PROD + " LEFT JOIN "
                + "(SELECT * FROM " + TABLE_NAME_CART + " WHERE " + COL_CART_USERID + " = " +useridSP + ")"
                + " ON " + COL_PROD_ID + " = " + COL_CART_PRODID + " WHERE " + COL_PROD_CATEGORY + " = " +"'GROCERY'",null);

        return cur;
    }


    public ArrayList<CartModel> getGroceryProduct() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = getAllgroceryProductData();

        ArrayList<CartModel> mlist = new ArrayList<>();
        ProductModel pmd;

        if (cur != null) {

            cur.moveToFirst();
            do {
                int productid = cur.getInt(cur.getColumnIndex(COL_PROD_ID));
                String prodname = cur.getString(cur.getColumnIndex(COL_PROD_NAME));
                int prodprice = cur.getInt(cur.getColumnIndex(COL_PROD_PRICE));
                int cartId = cur.getInt(cur.getColumnIndex(COL_CART_ID));
                int cartProdId = cur.getInt(cur.getColumnIndex(COL_CART_PRODID));

                /*String checkAdded=cur.getString(cur.getColumnIndex(COL_CART_PRODID));
                boolean bt;
                if (checkAdded==null)
                    bt=false;
                else
                    bt=true;*/

                pmd =new ProductModel(productid, prodname, prodprice);
                mlist.add(new CartModel(cartId,cartProdId,pmd));
                // mlist.add(new ProductModel(productid, prodname, prodprice,bt));
            } while (cur.moveToNext());
        }
        return mlist;
    }


    public Cursor getAllsportsProductData() {

//        sp = mContext.getSharedPreferences(MyprefK, Context.MODE_PRIVATE);
//        ed = sp.edit();
        sp = PreferenceHelper.getmInstance(mContext);

        int useridSP = sp.getUserID(UserIDK, 0);

        SQLiteDatabase db = this.getWritableDatabase();
       // Cursor cur = db.rawQuery("select * from " + TABLE_NAME_PROD + " WHERE " + COL_PROD_CATEGORY + " =" + "'SPORTS'", null);

        /*Cursor cur3 = db.rawQuery("select * from " + TABLE_NAME_PROD + " left join " + TABLE_NAME_CART
                + " ON " + COL_CART_PRODID + " = " + COL_PROD_ID + " WHERE " + COL_PROD_CATEGORY + " =" + "'SPORTS'", null);*/

        Cursor cur = db.rawQuery("SELECT * FROM " + TABLE_NAME_PROD + " LEFT JOIN "
                + "(SELECT * FROM " +TABLE_NAME_CART + " WHERE " + COL_CART_USERID + " = " +useridSP +")"
                + " ON " + COL_PROD_ID + " = " + COL_CART_PRODID + " WHERE " + COL_PROD_CATEGORY + " = " +"'SPORTS'",null);

        return cur;
    }


    public ArrayList<CartModel> getSportsProduct() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = getAllsportsProductData();

        ArrayList<CartModel> mlist = new ArrayList<>();
        ProductModel pmd;

        if (cur != null) {

            cur.moveToFirst();
            do {
                int productid = cur.getInt(cur.getColumnIndex(COL_PROD_ID));
                String prodname = cur.getString(cur.getColumnIndex(COL_PROD_NAME));
                int prodprice = cur.getInt(cur.getColumnIndex(COL_PROD_PRICE));
                int cartId = cur.getInt(cur.getColumnIndex(COL_CART_ID));
                int cartProdId = cur.getInt(cur.getColumnIndex(COL_CART_PRODID));

                /*String checkAdded=cur.getString(cur.getColumnIndex(COL_CART_PRODID));
                boolean bt;
                if (checkAdded==null)
                    bt=false;
                else
                    bt=true;*/


                pmd =new ProductModel(productid, prodname, prodprice);
                mlist.add(new CartModel(cartId,cartProdId,pmd));
                //  mlist.add(new ProductModel(productid, prodname, prodprice,bt));
            } while (cur.moveToNext());
        }
        return mlist;
    }

}





/* Important Notes:-

Code method Modified:-


Meth 3:- (Date 6 Dec 17) Set ProductList on Adapter without using fragment

    //This method used when we were not used Product Category and display all the Products item in one Tab only.
    //Where we not used even Fragment,TabLayout method


    M 3.1, public Cursor getAllProductData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = db.rawQuery("select * from " + TABLE_NAME_PROD, null);

        return cur;
    }


    M 3.2, public ArrayList<ProductModel> getProductData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cur = getAllProductData();

        ArrayList<ProductModel> mlist = new ArrayList<>();

        if (cur != null) {
            while (cur.moveToNext()) {

                String prodname = cur.getString(cur.getColumnIndex(COL_PROD_NAME));
                int prodprice = cur.getInt(cur.getColumnIndex(COL_PROD_PRICE));

                mlist.add(new ProductModel(prodname,prodprice));
            }

            cur.moveToFirst();
                    do {
                    int productid = cur.getInt(cur.getColumnIndex(COL_PROD_ID));
                    String prodname = cur.getString(cur.getColumnIndex(COL_PROD_NAME));
                    int prodprice = cur.getInt(cur.getColumnIndex(COL_PROD_PRICE));

                    mlist.add(new ProductModel(productid, prodname, prodprice));
                    } while (cur.moveToNext());
                    }
                    return mlist;
                    }


Meth 4:- Setter method

      //This is alternative setter method for values which set using CONSTRUCTOR.

       M 4.1, CartModel cmd = new CartModel();

       M 4.2, cmd.setCartquantity(cur.getInt(cur.getColumnIndex(COL_CART_QUANTITY)));

                pmd.setProdname(cur.getString(cur.getColumnIndex(COL_PROD_NAME)));
                cmd.setProdItem(pmd);

                cartlist.add(cmd);


  Below are the Reference Sections are used for reference

Ref 1:

Here parameter, qtyUpdate(parameter)
         Don't use this.Because it creates new instance and take first value if this is used > new CartModel(cartid,qty) in Database;
            Hence don't create object/instance of CartModel
            Either take value from parameter and put it inside value.put(COLMN_NAME,put here); and in update() Query
            If we use above constructor method it will take and update only First value of User

            OR Create CartModel object in parameter and get its value through model class i.e. CartModel.getterMethod()
        new CartModel(userid,cartid,qty);




 Ref 2:

Here twister Logic for bt is necessary.Means if bt=true,it will set isClickbutton() method also true via constructor.
 Which further used at MyAdapter in 'if(current.isClickbutton())'.
 If isClickbutton is true then it will execute if condition make the button false.


---*/
