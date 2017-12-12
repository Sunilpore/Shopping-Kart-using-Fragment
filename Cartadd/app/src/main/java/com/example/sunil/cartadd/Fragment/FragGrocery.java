package com.example.sunil.cartadd.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.sunil.cartadd.Adapter.MyAdapter;
import com.example.sunil.cartadd.Database.DatabaseHandler;
import com.example.sunil.cartadd.Interface.UpdateListener;
import com.example.sunil.cartadd.Model.CartModel;
import com.example.sunil.cartadd.Model.ProductModel;
import com.example.sunil.cartadd.R;

import java.util.ArrayList;


public class FragGrocery extends Fragment /*implements UpdateListener*/ {

    Context mContext;
    DatabaseHandler db;
    ListView lvgrocery;
    ArrayList <CartModel> grocerylist;
    MyAdapter gadapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_frag_grocery,container,false);
        lvgrocery=view.findViewById(R.id.fraggrocery_listview);

        this.mContext=getActivity();
       // this.mContext=container.getContext();  //Why this is wrong ?.But still working
        db=new DatabaseHandler(mContext);

        grocerylist=db.getGroceryProduct();
        gadapter=new MyAdapter(mContext,grocerylist);
        lvgrocery.setAdapter(gadapter);

        //gadapter.setOnItemListener((UpdateListener) mContext);

        return view;
    }


    /*@Override
    public void onUpdateListenernow(boolean status, int position) {

    }*/

    @Override
    public void onStart() {
        super.onStart();
        mContext.registerReceiver(recallBroadcastReciever, new IntentFilter("ACTION"));
    }


    BroadcastReceiver recallBroadcastReciever=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            Boolean isCartInserted=intent.getBooleanExtra("status",false);
            int productid=intent.getIntExtra("ProductId",0);

            if(isCartInserted)
                buttonDisable(productid);

        }
    };

    @Override
    public void onStop() {
        super.onStop();
        mContext.unregisterReceiver(recallBroadcastReciever);
    }


    private void buttonDisable(int productId){

        ArrayList <CartModel> cartlist=grocerylist;

        for(CartModel cartModel:cartlist){

            ProductModel productModel=cartModel.getProdItem();
            if(productModel.getPid() == productId){

                productModel.setClickbutton(true);

                productModel.setPid(cartModel.getProdItem().getPid());
                productModel.setProdname(cartModel.getProdItem().getProdname());
                productModel.setProdprice(cartModel.getProdItem().getProdprice());
                productModel.setProdcat(cartModel.getProdItem().getProdcat());

                cartModel.setProdItem(productModel);
                gadapter.notifyDataSetChanged();
            }
        }

    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
