package com.example.sunil.cartadd.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.sunil.cartadd.Database.DatabaseHandler;
import com.example.sunil.cartadd.Interface.UpdateListener;
import com.example.sunil.cartadd.Model.CartModel;
import com.example.sunil.cartadd.Model.ProductModel;
import com.example.sunil.cartadd.Model.UserModel;
import com.example.sunil.cartadd.R;

import java.util.ArrayList;

/**
 * Created by Sunil on 11/13/2017.
 */

public class MyAdapter extends BaseAdapter{

    public static final String MyprefK = "Prefkey";
    public static final String UserIDK = "UserIDkey";
    /*public static final String ProductIDK = "ProductIDkey";*/

    SharedPreferences sp;
    SharedPreferences.Editor ed;

    private Context mContext;
    private ArrayList<ProductModel> alist;
    LayoutInflater inflater;
    DatabaseHandler db;
    UpdateListener onUpdateListener;

    public MyAdapter(Context mContext, ArrayList<ProductModel> alist) {
        this.mContext=mContext;
        this.alist = alist;
        inflater=LayoutInflater.from(mContext);
        db=new DatabaseHandler(mContext);
    }

    public MyAdapter() {
    }

    @Override
    public int getCount() {
        return alist.size();
    }

    @Override
    public Object getItem(int i) {
        return alist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup vg) {

        sp = mContext.getSharedPreferences(MyprefK, Context.MODE_PRIVATE);
        ed = sp.edit();

        final ViewHolder vh;
        if(view==null) {
            vh=new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.lay, vg, false);

            vh.prodname=view.findViewById(R.id.tv_productname);
            vh.prodprice=view.findViewById(R.id.tv_prodprize);
            vh.click=view.findViewById(R.id.bt_click);
            view.setTag(vh);
        }
        else{
            vh= (ViewHolder) view.getTag();
        }

        final ProductModel current= (ProductModel) getItem(i);

        /*//This can also possible by using getter() method
    vh.prodname.setText(current.getProdname());
    vh.prodprice.setText(String.valueOf(current.getProdprice()));*/

        //Here we setText() by using constructor
        vh.prodname.setText(current.prodname);
        vh.prodprice.setText(String.valueOf(current.prodprice));

        vh.click.setTag(current);

        final int useridSP=sp.getInt(UserIDK,1);
        final int productid=current.getPid();

        //It is neccessary else it will select multiple buttons
        if(current.isClickbutton()){
            vh.click.setEnabled(false);
        }
        else{
            vh.click.setEnabled(true);
        }

        vh.click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Button bt = (Button) view;

                ProductModel tmp = (ProductModel) bt.getTag();

                UserModel umd=new UserModel();
                ProductModel pmd=new ProductModel();

                //int useridSP=sp.getInt(UserIDK,1);
                Log.d("myTag","Productid:"+current.getPid());


                /*ed.putInt(ProductIDK,alist.indexOf(tmp)+1);
                ed.apply();*/

                //int productid=current.getPid();

//                boolean cartInserted=db.addtoCart(new CartModel(useridSP,(alist.indexOf(tmp)+1),1));
                boolean cartInserted=db.addtoCart(new CartModel(useridSP,productid,1));
                if(cartInserted){
                    tmp.setClickbutton(true);
                }

                notifyDataSetChanged();

                onUpdateListener.onUpdateListenernow(cartInserted,i);

            }
        });


        /*Cursor cur=db.addButtonStatus(*//*productid,useridSP*//*);

        if(cur!=null){

            while(cur.moveToNext()){

                ProductModel pd=new ProductModel();

                int cartid,prodid=0;
                cartid=cur.getInt(cur.getColumnIndex(DatabaseHandler.COL_CART_ID));
                prodid=cur.getInt(cur.getColumnIndex(DatabaseHandler.COL_CART_PRODID));

                ProductModel position= (ProductModel) getItem(prodid);

                *//*if(cartid!=0)
                {
                    Log.d("myTag2","Cartid:"+prodid);
                    position.setClickbutton(true);
                }*//*

            }
        }*/


        return view;
    }

    private class ViewHolder{
        TextView prodname,prodprice;
        Button click;

    }

    public void setOnItemListener(UpdateListener onUpdateListener){
        this.onUpdateListener=onUpdateListener;
    }

}
