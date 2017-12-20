package com.example.sunil.cartadd.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sunil.cartadd.Database.DatabaseHandler;
import com.example.sunil.cartadd.Interface.CartTotalPriceUpdateListener;
import com.example.sunil.cartadd.Model.CartModel;
import com.example.sunil.cartadd.R;

import java.util.ArrayList;

/**
 * Created by Sunil on 11/21/2017.
 */

public class CartAdapter extends BaseAdapter {

    public static final String MyprefK = "Prefkey";
    public static final String UserIDK = "UserIDkey";

    SharedPreferences sp;
    SharedPreferences.Editor ed;

    private final String PLUS = "INCREMENT";
    private final String MINUS = "DECREMENT";
    private final String DEL = "DELETE";
    private static String tag = "myTag3";

    Intent a, b, c;

    static int Qtychange;
    private Context mContext;
    private ArrayList<CartModel> cartlist;
    LayoutInflater inflater;
    CartTotalPriceUpdateListener onPriceListener;
    DatabaseHandler db;


    public CartAdapter(Context mContext, ArrayList<CartModel> cartlist) {
        this.mContext = mContext;
        this.cartlist = cartlist;
        inflater = LayoutInflater.from(mContext);
        db = new DatabaseHandler(mContext);
    }

    @Override
    public int getCount() {
        return cartlist.size();
    }

    @Override
    public Object getItem(int i) {
        return cartlist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup vg) {

        sp = mContext.getSharedPreferences(MyprefK, Context.MODE_PRIVATE);
        ed = sp.edit();

        final CartViewHolder vch;
        if (view == null) {

            vch = new CartViewHolder();

            view = LayoutInflater.from(mContext).inflate(R.layout.lay2, vg, false);

            vch.cartProdname = view.findViewById(R.id.tv_cartProdname);
            vch.cartPrize = view.findViewById(R.id.tv_cartPrize);
            vch.cartItemQty = view.findViewById(R.id.tv_cartQty);
            vch.incrQty = view.findViewById(R.id.bt_cartIncrement);
            vch.decrQty = view.findViewById(R.id.bt_cartDecrement);
            vch.cartDel = view.findViewById(R.id.bt_cartDelete);
            view.setTag(vch);
        } else {
            vch = (CartViewHolder) view.getTag();
        }

        final CartModel currentCart = (CartModel) getItem(i);

        int setprize = currentCart.getProdItem().getProdprice() * currentCart.cartquantity;
        String prize = "Rs." + setprize;


        if (view != null) {

            vch.incrQty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    a = new Intent(PLUS);
                    int qty = currentCart.cartquantity + 1;

                    //CartModel currentPlus= (CartModel) getItem(i);
             /*
                int userid = sp.getInt(UserIDK, 0);
                int qty = currentCart.cartquantity + 1;

                int cartid=cartlist.indexOf(currentPlus)+1;
                  boolean isQtyUpdate = db.qtyUpdate(cartid,qty);*/


                    //Log.d("myTag", "Cart Adapter cartId: " + currentCart.getCartid());
                    currentCart.setCartquantity(qty);
                    boolean isQtyUpdate = db.qtyUpdate(currentCart);
                    if(isQtyUpdate)
                        onPriceListener.onCartTotalPriceUpdate(isQtyUpdate);

                /*if(isQtyUpdate)
                Toast.makeText(mContext,"Qty. increased\t After Update:"+currentCart.cartquantity,Toast.LENGTH_LONG).show();
                else
                Toast.makeText(mContext,"Qty. NOT increased",Toast.LENGTH_LONG).show();*/

                    //mContext.sendBroadcast(a);
                    notifyDataSetChanged();


                }
            });



            vch.decrQty.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    b = new Intent(MINUS);
                    int qty = currentCart.cartquantity;


                    if (qty > 1) {
                        qty = currentCart.cartquantity - 1;
                        currentCart.setCartquantity(qty);
                        boolean isQtyUpdate = db.qtyUpdate(currentCart);
                        if (isQtyUpdate){
                            Toast.makeText(mContext, "Qty. decreased Success", Toast.LENGTH_LONG).show();
                            onPriceListener.onCartTotalPriceUpdate(isQtyUpdate);
                        }

                        else
                            Toast.makeText(mContext, "Qty. decreased Not Success", Toast.LENGTH_LONG).show();

                        //mContext.sendBroadcast(b);
                        notifyDataSetChanged();
                    }



               /* Toast.makeText(mContext,"Qty. decreased",Toast.LENGTH_LONG).show();*/
                }
            });



            vch.cartDel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    c = new Intent(DEL);
                    int cartid = currentCart.getCartid();

                    cartlist.remove(i);     //Ref 2
                    c.putExtra("position",i);
                   // mContext.sendBroadcast(c);  //Ref 1
                    boolean isDeleted = db.cartItemdelete(cartid);
                    if(isDeleted)
                        onPriceListener.onCartTotalPriceUpdate(isDeleted);

                    Toast.makeText(mContext, "Qty. deleted", Toast.LENGTH_LONG).show();

//                  cartlist.remove(i);   //Use it if you are not using BroadcastReciever method in Cartview.java.
                    notifyDataSetChanged();
                }
            });

            vch.cartProdname.setText(currentCart.getProdItem().getProdname());
            vch.cartPrize.setText(prize);
            vch.cartItemQty.setText(String.valueOf(currentCart.cartquantity));
        }

        return view;
    }

    private class CartViewHolder {

        TextView cartProdname, cartPrize, cartItemQty;
        Button incrQty, decrQty, cartDel;
    }

    public void setOnCartPriceListener(CartTotalPriceUpdateListener onPriceListener){
        this.onPriceListener=onPriceListener;
    }

}


/* Important Notes:-

Code method Modified:-

Meth 1:-


  Below are the Reference Sections are used for reference

 Ref 1: (Date 17 Dec 17)
     Here no need to use BroadcastReciver when you use getTotalCostArray() method in 'CartView.java' to get Total cost.


Problem???, Explained Below:-
When we use getTotalCostArray() method and still use BroadcastReciver,it will not update value in Total Cost Textview as you hit the DELETE button
But when you perform some action like use PLUS,MINUS button or change activity using Intent it will update new Total cost value properly.

Conclusion- When you press DELETE Button
     ArrayList run before and take previous value for it and calulate Total cost in TextView which is previous one
and after its execution BroadcastReciver recieve the position and remove the item from ArrayList and notify to Adapter,

Note-But it will work properly when you use db.getCartTotalPrice() method in 'CartView.java' to calculate Total cost throgh Database.


 Ref 2: (Date 17 Dec 17)
    When you call cartlist.remove(i) method before db.cartItemdelete(cartid) method, then only it will update Total cost in TextView properly.
    Else item will removed from cartview Screen but price is not updated.

---*/