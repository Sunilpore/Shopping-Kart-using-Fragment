package com.example.sunil.cartadd.Async;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.example.sunil.cartadd.Database.DatabaseHandler;
import com.example.sunil.cartadd.Model.CartModel;
import com.example.sunil.cartadd.Singleton.PreferenceHelper;

/**
 * Created by Sunil on 12/11/2017.
 */

public class MyAsync extends AsyncTask<Integer, Void, Boolean> {

    public static final String MyprefK="Prefkey";
    public static final String CheckK="Checkkey";
    public static final String UserIDK = "UserIDkey";

    PreferenceHelper sp;
//    SharedPreferences sp;
//    SharedPreferences.Editor ed;

    Context mContext;
    DatabaseHandler db;
    int productid=0;

    public MyAsync(Context mContext, int prodid) {

        this.mContext=mContext;
        this.productid=prodid;
        db=new DatabaseHandler(mContext);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(Integer... integers) {

//        sp = mContext.getSharedPreferences(MyprefK, Context.MODE_PRIVATE);
//        ed = sp.edit();
        sp = PreferenceHelper.getmInstance(mContext);

        int useridSP = sp.getUserID(UserIDK, 0);

        boolean cartInserted=db.addtoCart(new CartModel(useridSP,productid,1));
        return cartInserted;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);

        Intent i=new Intent();
        i.setAction("ACTION");
        i.putExtra("ProductId",productid);
        i.putExtra("status",aBoolean);
        mContext.sendBroadcast(i);
    }

}