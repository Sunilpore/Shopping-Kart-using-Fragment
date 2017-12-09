package com.example.sunil.cartadd.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.sunil.cartadd.Adapter.PagerAdapter;
import com.example.sunil.cartadd.Database.DatabaseHandler;
import com.example.sunil.cartadd.Adapter.MyAdapter;
import com.example.sunil.cartadd.Fragment.FragElectronics;
import com.example.sunil.cartadd.Fragment.FragGrocery;
import com.example.sunil.cartadd.Fragment.FragSports;
import com.example.sunil.cartadd.Model.ProductModel;
import com.example.sunil.cartadd.R;
import com.example.sunil.cartadd.Interface.UpdateListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements UpdateListener, FragElectronics.OnFragmentInteractionListener, FragGrocery.OnFragmentInteractionListener, FragSports.OnFragmentInteractionListener {

    public static final String MyprefK="Prefkey";
    public static final String CheckK="Checkkey";
    public static final String UserIDK = "UserIDkey";

    String add;
    SharedPreferences sp;
    SharedPreferences.Editor ed;

    /*ListView lv;*/
    CoordinatorLayout cordlay;
    MenuItem countview;
    DatabaseHandler db;
    /*MyAdapter adapter;
    ArrayList<ProductModel> plist;*/
    Context mContext;
    int count;

    TabLayout tablay;
    ViewPager vpager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ActionBar ab=getSupportActionBar();
        ab.setLogo(R.drawable.tick);
        ab.setDisplayUseLogoEnabled(true);   //This method will enable your logo
        ab.setDisplayShowHomeEnabled(true);  //This method will enable your home

        mContext=this;
//        adapter=new MyAdapter();
        db=new DatabaseHandler(mContext);

        /*lv= (ListView) findViewById(R.id.home_listview);*/
        cordlay= (CoordinatorLayout) findViewById(R.id.cord_layhome);
        tablay= (TabLayout) findViewById(R.id.lay_Tablayout);
        vpager= (ViewPager) findViewById(R.id.View_pager);

        tablay.addTab(tablay.newTab().setText("ELECTRONICS"));
        tablay.addTab(tablay.newTab().setText("GROCERY"));
        tablay.addTab(tablay.newTab().setText("SPORTS"));
        tablay.setTabGravity(TabLayout.GRAVITY_FILL);

        //Don't use this one if you are using
//        tablay.setupWithViewPager(vpager);

        /*//Not neccessary in this type of fragment implementation
        FragElectronics fm1=new FragElectronics();
        FragGrocery fm2=new FragGrocery();
        FragSports fm3=new FragSports();*/

        PagerAdapter pgadapter=new PagerAdapter(this.getSupportFragmentManager(), tablay.getTabCount());

        vpager.setAdapter(pgadapter);
        vpager.setOnPageChangeListener( new TabLayout.TabLayoutOnPageChangeListener(tablay));


        tablay.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                  vpager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



        sp=getSharedPreferences(MyprefK, Context.MODE_PRIVATE);
        ed=sp.edit();



        if(sp.getBoolean(CheckK,true)){
            db.addProductData(new ProductModel("Poduct 1",10,"ELECTRONICS"));
            db.addProductData(new ProductModel("Poduct 2",20,"ELECTRONICS"));
            db.addProductData(new ProductModel("Poduct 3",20,"ELECTRONICS"));
            db.addProductData(new ProductModel("Poduct 4",20,"ELECTRONICS"));
            db.addProductData(new ProductModel("Poduct 5",10,"ELECTRONICS"));
            db.addProductData(new ProductModel("Poduct 6",10,"GROCERY"));
            db.addProductData(new ProductModel("Poduct 7",20,"GROCERY"));
            db.addProductData(new ProductModel("Poduct 8",20,"GROCERY"));
            db.addProductData(new ProductModel("Poduct 9",30,"GROCERY"));
            db.addProductData(new ProductModel("Poduct 10",30,"GROCERY"));
            db.addProductData(new ProductModel("Poduct 11",40,"SPORTS"));
            db.addProductData(new ProductModel("Poduct 12",50,"SPORTS"));
            db.addProductData(new ProductModel("Poduct 13",60,"SPORTS"));
            db.addProductData(new ProductModel("Poduct 14",60,"SPORTS"));
            db.addProductData(new ProductModel("Poduct 15",60,"SPORTS"));

            //Here used apply() instead of commit();
            //Because, commit() blocks and writes its data to persistent storage immediately,where apply() will handle data in background.

            ed.putBoolean(CheckK,false);
            ed.apply();
//           ed.commit();
        }

        /*plist=db.getProductData();
        adapter=new MyAdapter(mContext,plist);
        lv.setAdapter(adapter);
        adapter.setOnItemListener(this);*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_activity,menu);
        countview=menu.findItem(R.id.count_id);
        getCount();
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){

            case R.id.count_id:

                Intent in=new Intent(HomeActivity.this,CartView.class);

                in.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(in);

                //When you call finish() method it will not hold the current status of 'ADD' button,while returning from CartView to HomeActivity
//                finish();
                break;

            case R.id.view_id:
                Toast.makeText(this,"Move to next Page for My Order",Toast.LENGTH_LONG).show();
                break;

            case R.id.logout_id:

                Intent i=new Intent(HomeActivity.this,MainActivity.class);
                startActivity(i);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onUpdateListenernow(boolean status, int position) {

        if(status){

            count=count+1;
            add=String.valueOf(count) + " Items Added";
            countview.setTitle(add);

            Snackbar.make(cordlay,count+" Product added in Cart",Snackbar.LENGTH_LONG).show();
        }
        else{

            Snackbar sn=Snackbar.make(cordlay,"Item is already added into the Cart",Snackbar.LENGTH_LONG);
            View snackbarView = sn.getView();
            snackbarView.setBackgroundColor(Color.RED);
            sn.show();
        }
    }

    private void getCount(){

        count=db.cartCount(sp.getInt(UserIDK,0));
        countview.setTitle(""+count+" Items Added");

    }

    @Override
    protected void onRestart() {
        super.onRestart();

        getCount();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
