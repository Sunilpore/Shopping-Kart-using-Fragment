package com.example.sunil.cartadd.Singleton;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Sunil on 12/18/2017.
 */

public class PreferenceHelper {


   private static PreferenceHelper mInstance=null;
   private Context mContext;
   private SharedPreferences mSP;
   SharedPreferences.Editor ed;

   private static final String MyprefK = "Prefkey";
   private static final String CheckK = "Checkkey";
   private static final String UserIDK = "UserIDkey";

   //Private Constructor()
   private PreferenceHelper(Context context){
      mContext=context;
      mSP=mContext.getSharedPreferences(MyprefK,Context.MODE_PRIVATE);
      ed=mSP.edit();
   }

   public static PreferenceHelper getmInstance(Context context){
      if(mInstance==null){
         mInstance=new PreferenceHelper(context);
      }
      return  mInstance;
   }

   public boolean getCheck(String key, boolean defValue) {
      return  mSP.getBoolean(key, defValue);
   }

   public void setCheck(String key, boolean value) {
      ed.putBoolean(key, value);
      ed.commit();
   }

   public int getUserID(String key, int defValue) {
      return  mSP.getInt(key, defValue);
   }

   public void setUserID(String key, int value) {
      ed.putInt(key, value);
      ed.commit();
   }
}
