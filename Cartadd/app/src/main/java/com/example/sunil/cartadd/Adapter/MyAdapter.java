package com.example.sunil.cartadd.Adapter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sunil.cartadd.Async.MyAsync;
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

public class MyAdapter extends BaseAdapter /*implements UpdateListener*/{

    public static final String MyprefK = "Prefkey";
    public static final String UserIDK = "UserIDkey";
    /*public static final String ProductIDK = "ProductIDkey";*/

    SharedPreferences sp;
    SharedPreferences.Editor ed;

    private Context mContext;
    private ArrayList<CartModel> alist;
    LayoutInflater inflater;
    DatabaseHandler db;
   // UpdateListener onUpdateListener;

    public MyAdapter(Context mContext, ArrayList<CartModel> alist) {
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


            for(CartModel cartModel :alist){
                int productId=cartModel.getProdid();
                //buttonDisable(productId);
            }

        }
        else{
            vh= (ViewHolder) view.getTag();
        }

        final CartModel current= (CartModel) getItem(i);

        /*//This can also possible by using getter() method
    vh.prodname.setText(current.getProdname());
    vh.prodprice.setText(String.valueOf(current.getProdprice()));*/

        //Here we setText() by using constructor
        vh.prodname.setText(current.getProdItem().prodname);
        vh.prodprice.setText(String.valueOf(current.getProdItem().prodprice));

        vh.click.setTag(current);

        //It is neccessary else it will select multiple buttons
        Log.d("myTag","FORloop Cartid:"+current.cartid);
        if(current.getProdItem().isClickbutton() || current.cartid!=0){
            vh.click.setEnabled(false);
        }
        else{
            vh.click.setEnabled(true);
        }

        vh.click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Button bt = (Button) view;

                final CartModel tmp = (CartModel) bt.getTag();

                UserModel umd=new UserModel();
                ProductModel pmd=new ProductModel();

                int useridSP=sp.getInt(UserIDK,1);
                Log.d("myTag","Productid:"+current.getProdItem().getPid());

                int productid=current.getProdItem().getPid();

                //Meth 4
                //M 1.1

                //M 3.1
                 MyAsync async=new MyAsync(mContext,productid);
                 async.execute();

                //Meth 2

            }
        });

        return view;
    }



    private class ViewHolder{
        TextView prodname,prodprice;
        Button click;

    }

    /*private void buttonDisable(int productId){

        ArrayList <CartModel> cartlist=alist;

        for(CartModel cartModel:cartlist){

            ProductModel productModel=cartModel.getProdItem();
            if(productModel.getPid() == productId){

                productModel.setClickbutton(true);

                productModel.setPid(cartModel.getProdItem().getPid());
                productModel.setProdname(cartModel.getProdItem().getProdname());
                productModel.setProdprice(cartModel.getProdItem().getProdprice());
                productModel.setProdcat(cartModel.getProdItem().getProdcat());

                cartModel.setProdItem(productModel);
                notifyDataSetChanged();
            }
        }

    }*/

    //Use one of them method
    //M 3.2

    //M 1.2

}





/*Note:-

Code method Modified:-

Meth 1:-
       This is one way to implement interface where we initialize inerface object/variable via method
       and Context of interface object/vaiable is provided in a method where we need to implement.

     public class MyAdapter extends BaseAdapter implements UpdateListener{

    UpdateListener onUpdateListener;

 M 1.1,   onUpdateListener.onUpdateListenernow(cartInserted,i);

 M 1.2,    public void setOnItemListener(UpdateListener onUpdateListener){
        this.onUpdateListener=onUpdateListener;
    }

  }


Meth 2:- (Date:11 Dec 17)

    //This is used when we use BroadcastReceiver AND its registerReceiver() method using Anonymous class in non-Activity class.
      mContext.registerReceiver(new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {

                        boolean cartInserted =intent.getBooleanExtra("status",false);

                        if(cartInserted){
                            tmp.setClickbutton(true);
                        }

                        notifyDataSetChanged();
                        onUpdateListener.onUpdateListenernow(cartInserted,i);
                    }
                }, new IntentFilter("ACTION"));

      //When we try to use this unregisterReceiver() throw error as BroadcastReciever is not registered but it registere already.
      //So,try to avoid this unregisterReceiver() part
      mContext.unregisterReceiver(new BroadcastReceiver() {
                    @Override
                    public void onReceive(Context context, Intent intent) {

                    }
                });


 Meth 3:- (Date: 12 Dec 17)
       This is another method to implement interface by providing Context of Interface class where we need interface method.

       -->
       public class MyAdapter extends BaseAdapter implements UpdateListener{

        UpdateListener onUpdateListener;

    M 3.1,  onUpdateListener= (UpdateListener) mContext;

    M 3.2,  @Override
         public void onUpdateListenernow(boolean status, int position) {
         onUpdateListener.onUpdateListenernow(status, position);
          }

       }

Meth 4:- (Date: 12 Dec 17)

     // This method is used when we not use addtocart() method in AsyncTask, where we use BroadcastReciver to send its status at MyAdapter and in HomeActivity.

   {  //Use this one alist.indexOf(tmp) when you not able to get productid via ProductModel
     ed.putInt(ProductIDK,alist.indexOf(tmp)+1);
     ed.apply();

     int productid=current.getPid(); //When we use ProductModel in constructor
     int productid=current.getProdItem().getPid(); //When we use CartModel in constructor

     boolean cartInserted=db.addtoCart(new CartModel(useridSP,(alist.indexOf(tmp)+1),1));}

     boolean cartInserted=db.addtoCart(new CartModel(useridSP,productid,1));
      if(cartInserted){
          tmp.setClickbutton(true);
                    Toast.makeText(mContext, "Add Button pressed", Toast.LENGTH_LONG).show();
                }

                notifyDataSetChanged();






  Below are the Refrence Sections are used for refrence

 Ref 1:

 Ref 2:


---*/