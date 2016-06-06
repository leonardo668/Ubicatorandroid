package com.example.ddd;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;



public class Servicio extends Service{

	String respuesta;
	Activity act;
	private String best;
	private String imei;
	private String latitud;
	private String longitud;
	
	private LocationManager locManager;
	private LocationListener locListener;
	TelephonyManager mTelephonyManager;

	private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; //
	private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000;
	
	private static Activity ctx;
	private static Servicio instancia  = null;

	private NotificationManager ntm;
	private int NOTIFICATION = R.string.hello_world;
	public static boolean isRunning() {
		return instancia != null;
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onCreate() {

		instancia=this;
		

	}

	@Override
	public void onDestroy() {
		 ntm.cancel(NOTIFICATION);

		 Toast.makeText(this,"Se detubo el Servicio Ubicator", Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onStart(Intent intent, int startid) {

		Toast.makeText(getApplicationContext(), "Servicio Ubicator iniciado", Toast.LENGTH_SHORT).show();

		mTelephonyManager=(TelephonyManager)getSystemService(getApplicationContext().TELEPHONY_SERVICE);
		imei=mTelephonyManager.getSimSerialNumber();
		if(imei.equals(""))
		{
			imei=mTelephonyManager.getDeviceId();
		}
		
		comenzarLocalizacion();
		
	}

	private void comenzarLocalizacion()
	{
		//Obtenemos una referencia al LocationManager
		locManager =(LocationManager)getSystemService(Context.LOCATION_SERVICE);

			calcBestProvider();
		//Obtenemos la �ltima posici�n conocida
	
		Location loc0 = locManager.getLastKnownLocation(best);
		Location loc1 =locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		Location loc2= locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		Location finalloc;
		if(loc0 == null)
	    {
		    if(loc1 == null)
		    {
		    	finalloc = loc2;
		    }
		    else
		    {
		    	finalloc = loc1;
		    }
	    }
	    else
	    {
	    	finalloc = loc0;
	    }
		
		//Mostramos la �ltima posici�n conocida
		try {
			mostrarPosicion(finalloc);
		} catch (Exception e) {
			// TODO: handle exception
		}
		

		//Nos registramos para recibir actualizaciones de la posici�n
		locListener = new LocationListener() {
			public void onLocationChanged(Location location) {
				try {
					
					mostrarPosicion(location);
					
					//aqui llama el metodo cuando cambia la ubicacion pero eso  no tiene que ver con tiempo
				} catch (Exception e) {
					// TODO: handle exception
				}
				
				Log.i("", "location change: " + location);
			}
			public void onProviderDisabled(String provider){
				//lblEstado.setText("Provider OFF");
				Log.i("", "Provider disable: " + provider);
			}
			public void onProviderEnabled(String provider){
				//lblEstado.setText("Provider ON ");
				Log.i("", "Provider enable: " + provider);
			}
			public void onStatusChanged(String provider, int status, Bundle extras){
				Log.i("", "Provider Status: " + status);
				
			}

		};
		//aqui actualiza el cambio de pocicion cada 3000 milisegundos y llama al metodo de arriba otra vez
	     
	     if (!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
	    	  locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 0, locListener);
		    }else{
		    	 locManager.requestLocationUpdates(
		    		     LocationManager.GPS_PROVIDER, 3000, 0, locListener);
		    }
	    
	     ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);
	 	exec.scheduleAtFixedRate(new Runnable() {
	 	           public void run() {
	 	                // code to execute repeatedly
	 	        		/*ServiceInfoUbicacion  sesion= new ServiceInfoUbicacion();
	 	        		sesion.execute();*/
	 	           }
	 	       }, 0, 60, TimeUnit.SECONDS);
	     
	    }
	
	
	private void calcBestProvider() {
		//Establecemos los criterios para obtener el mejor proveeder en base a los requisitos
		//deseados
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setCostAllowed(true);
		criteria.setPowerRequirement(Criteria.POWER_LOW);
		best = locManager.getBestProvider(criteria, true);
	}
	
	
	
	private void mostrarPosicion(Location loc){
	if(loc != null)
	{
		  boolean resul = true;
		Log.i("", "imei 2" + imei);
		latitud = String.valueOf(loc.getLatitude());
		longitud = String.valueOf(loc.getLongitude());
		//esto va a ejecutar el llamado, ahora debemos declarar variables fijas para informarlas
		}else{

		}
	}

	/*@SuppressLint({ "ShowToast", "NewApi" })
	private class ServiceInfoUbicacion extends AsyncTask<String,Integer,Boolean> {
		 
	 
	    protected Boolean doInBackground(String... params) {
	 
		    boolean resul = true;
		    
		    try
		    {

		    	
				    final String NAMESPACE = "http://tempuri.org/";
				    final String URL="http://190.98.227.152/WSV3zt45/service.asmx";//act.getString(R.string.serv);
				    final String METHOD_NAME = "InfoUbicacion";
				    final String SOAP_ACTION = "http://tempuri.org/InfoUbicacion";
				 
				    SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);
				    request.addProperty("mySIM", imei);
				    request.addProperty("myLong", longitud);
				    request.addProperty("myLat", latitud);
				    
				    SoapSerializationEnvelope envelope =
				           new SoapSerializationEnvelope(SoapEnvelope.VER11);
				    envelope.dotNet = true;
				 
				    envelope.setOutputSoapObject(request);
				    HttpTransportSE transporte = new HttpTransportSE(URL,70320000);
				    
			        transporte.call(SOAP_ACTION, envelope);
			        SoapObject resSoap =(SoapObject)envelope.getResponse();
			 
			        
			        for (int i = 0; i < resSoap.getPropertyCount(); i++)
			        {
		               SoapObject ic = (SoapObject)resSoap.getProperty(i);
		               //en caso de que proximamente necesites una respuesta de la operacion
		               //la consultas con ic.getProperty(0).toString();
		               Log.i("", "Provider enable: " +ic.getProperty(0).toString() );
			        }
		        

		    }
		    catch (Exception e)
		    {
		        resul = false;
		    }
		    finally{
		    	
		    }
		    
	       return resul;
	       
	   }
	 
	   protected void onPostExecute(Boolean result) {
		   Calendar cal = Calendar.getInstance(); 

		   int segundo = cal.get(Calendar.SECOND);
		   int minuto = cal.get(Calendar.MINUTE);
		         //12 formato doce horas
		   int horafo= cal.get(Calendar.HOUR);
		         //24 formato 24 horas
		   int horafodia = cal.get(Calendar.HOUR_OF_DAY);
	  
		   Toast.makeText(getApplicationContext(),"Latitud: "+latitud+",Longitud:"+longitud+"Imei:"+imei+"Hora:"+horafo+":"+minuto+":"+segundo, Toast.LENGTH_LONG).show();
		   
		   Log.i("", "paso " );
	    }
	}*/
	
	   
	   
	    
	    
}
