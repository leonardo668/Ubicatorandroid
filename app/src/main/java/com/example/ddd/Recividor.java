package com.example.ddd;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Recividor extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		 Intent serviceIntent = new Intent(context,Servicio.class);
	        if(!Servicio.isRunning())context.startService(serviceIntent);
	        
//	        Intent servint = new Intent();
//            servint.setAction("com.example.Servicio");
//            context.startService(servint);
	        
	}
	

}
