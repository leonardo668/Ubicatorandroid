package com.example.ddd;

import android.support.v4.app.Fragment;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.os.Build;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        TextView texto =(TextView)findViewById(R.id.textView1);
        Typeface fuente= Typeface.createFromAsset(getAssets(), "Lobster 1.4.otf");
        
        texto.setTypeface(fuente);
    }

    public void onClickLanzarServicio(View v){
        Intent in = new Intent(MainActivity.this,Servicio.class);
        if(!Servicio.isRunning())
            MainActivity.this.startService(in);
    }

    
    public void onClickDetenerServicio (View v){
    	 Intent in = new Intent(MainActivity.this,Servicio.class);
    	
                 // TODO Auto-generated method stub
    		MainActivity.this.stopService(in);
    		
        

    }

}
