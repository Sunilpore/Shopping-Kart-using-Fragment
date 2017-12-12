package com.example.sunil.cartadd.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sunil.cartadd.Adapter.CartAdapter;
import com.example.sunil.cartadd.Interface.CartTotalPriceUpdateListener;
import com.example.sunil.cartadd.Model.CartModel;
import com.example.sunil.cartadd.Database.DatabaseHandler;
import com.example.sunil.cartadd.R;

import java.util.ArrayList;

public class CartView extends AppCompatActivity implements CartTotalPriceUpdateListener{

    private final String PLUS="INCREMENT";
    private final String MINUS="DECREMENT";
    private final String DEL="DELETE";

    DatabaseHandler db;
    ListView cartlv;
    TextView totalprice;
    int displayTotalPrice;
    Context mContext;
    CartAdapter ctadapter;
    ArrayList<CartModel> cartlist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartview);

        cartlv= (ListView) findViewById(R.id.cartlist_view);
        totalprice= (TextView) findViewById(R.id.Total_price);

        mContext=this;
        db=new DatabaseHandler(mContext);

        cartlist=db.getCartData();
        ctadapter=new CartAdapter(mContext,cartlist);
        cartlv.setAdapter(ctadapter);
        ctadapter.setOnCartPriceListener((CartTotalPriceUpdateListener) this);

        displayTotalPrice=db.getCartTotalPrice();
        totalprice.setText("Total Cost:Rs."+displayTotalPrice);



    }

    @Override
    protected void onStart() {
        super.onStart();
        mContext.registerReceiver(A,new IntentFilter(PLUS));
        mContext.registerReceiver(A,new IntentFilter(MINUS));
        mContext.registerReceiver(A,new IntentFilter(DEL));
    }

    BroadcastReceiver A=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().equals(PLUS) || intent.getAction().equals(MINUS) ){

                //No need here Intent reciever for this if() part.
                Toast.makeText(mContext,"BroadcastReciever",Toast.LENGTH_SHORT).show();

                //If you are not using above code then use it for refresh the value for textview i.e Cart Product Quantity
               /* Intent i = getIntent();
                finish();
                startActivity(i);*/
            }else if (intent.getAction().equals(DEL)){
                //Here for removing cartitem from cartlist it is necessary to recieve it vai Broadcast and get the postion of item
                //Else notify it at CartAdapter class itself inside vch.cartDel.setOnClickListener(); method

                int position=intent.getIntExtra("position",0);
//                Log.d("myTag",""+position);
                cartlist.remove(position);
                ctadapter.notifyDataSetChanged();
            }

        }
    };

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(A);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.order_activity,menu);

        ActionBar ab=getSupportActionBar();
        ab.setLogo(R.drawable.tick);
        ab.setDisplayUseLogoEnabled(true);   //This method will enable your logo

        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);  //This method will enable your home

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){

            case R.id.logout_id2:
                Intent i1=new Intent(CartView.this,MainActivity.class);
                i1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i1);
                finish();
                break;

              //without this also possible to call back previous intent Activity via Manifest
            case android.R.id.home:

               /* Intent i=new Intent(CartView.this,HomeActivity.class);

//                i.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
                startActivity(i);*/
                onBackPressed();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCartTotalPriceUpdate(boolean status) {
        if(status){
            displayTotalPrice=db.getCartTotalPrice();
            totalprice.setText("Total Cost:Rs"+displayTotalPrice);
        }

    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(CartView.this,HomeActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }
}
