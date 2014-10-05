package fr.jacquemet.fanimally;

import java.text.DecimalFormat;
import java.util.Locale;

import fr.jacquemet.fanimally.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class DefaultFragment extends Fragment
{
  
	// 
	private static boolean isReCreated=true;
	 



	
	private BroadcastReceiver broadcastReceiver;
	private IntentFilter intention;
		
	public static DecimalFormat df = new DecimalFormat("#.##");
	
	  
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
		setRetainInstance(true);		
				
	
	} 
 

	@Override
	public void onDestroy()
	{
		//getActivity().unregisterReceiver(broadcastReceiver);
		
		//sauvegarder les data
		
		super.onDestroy();
	}  
	
	@Override
	public void onStop()
	{
		getActivity().unregisterReceiver(broadcastReceiver);
		
		//sauvegarder les data
		
		
		super.onDestroy();
	}
	
		
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		broadcastReceiver= new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) 
			{
		
				
			}
		};		
		   
		intention=new IntentFilter();
	
		getActivity().registerReceiver(broadcastReceiver, intention);
		
	
            
	} 
  
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.bottom, null);
		
		
		return view;
	}
	

	 
	   
}
