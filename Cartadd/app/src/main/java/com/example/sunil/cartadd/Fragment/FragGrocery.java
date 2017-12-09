package com.example.sunil.cartadd.Fragment;

import android.content.Context;
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
import com.example.sunil.cartadd.Model.ProductModel;
import com.example.sunil.cartadd.R;

import java.util.ArrayList;


public class FragGrocery extends Fragment /*implements UpdateListener*/ {

    Context mContext;
    DatabaseHandler db;
    ListView lvgrocery;
    ArrayList <ProductModel> grocerylist;
    MyAdapter gadapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_frag_grocery,container,false);
        lvgrocery=view.findViewById(R.id.fraggrocery_listview);

        this.mContext=container.getContext();
        db=new DatabaseHandler(mContext);

        grocerylist=db.getGroceryProduct();
        gadapter=new MyAdapter(mContext,grocerylist);
        lvgrocery.setAdapter(gadapter);

        gadapter.setOnItemListener((UpdateListener) mContext);

        return view;
    }


    /*@Override
    public void onUpdateListenernow(boolean status, int position) {

    }*/


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
