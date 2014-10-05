package fr.jacquemet.fanimally.photo;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import fr.jacquemet.fanimally.R;
import fr.jacquemet.fanimally.view.ViewAddAnimal;



/**
 * Classe permettant d'utiliser la camera de l'appareil pour prendre une photo
 * Résolution de l'ecran= taille totale de l'écran
 * @author JulienJ
 *
 */
public class ViewPhoto extends Fragment implements SurfaceHolder.Callback,
		OnClickListener {
	
	//indique que l'on va utiliser l'appareil en mode photo
	private static final int FOTO_MODE = 0;
	private Camera mCamera;
	boolean mPreviewRunning = false;
	private BroadcastReceiver broadcastReceiver;
	private IntentFilter intention;
	
	private Button retour_btn;
	private Button photo_take_btn;

		
	@SuppressWarnings("unused")
	public void onCreate(Bundle arg) 
	{
		super.onCreate(arg);
				
		Bundle extras = getActivity().getIntent().getExtras();
		
		/*definition de la taille d'écran */
		getActivity().getWindow().setFormat(PixelFormat.TRANSLUCENT);

		
		//ajout des broadcast Receiver pour le header
        broadcastReceiver= new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) 
			{
			
			}
		};
		 
		//attache les intentions 
		intention=new IntentFilter();		
		getActivity().registerReceiver(broadcastReceiver, intention); 
		
		
	}
	
	@Override
	public void onDestroy() 
	{
		try
		{
			getActivity().unregisterReceiver(broadcastReceiver);
		}
		catch(Exception E)
		{
			
		}
		
		super.onDestroy();		
	}

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) 
	{
		View view=null;
		
		view= inflater.inflate(R.layout.camera, null);
		 
		/*attache de l'action clique sur l'écran = prise de photo */
		mSurfaceView = (SurfaceView) view.findViewById(R.id.surface_camera);
		mSurfaceView.setOnClickListener(this);
		mSurfaceHolder = mSurfaceView.getHolder();
		mSurfaceHolder.addCallback(this);
		mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		retour_btn= (Button) view.findViewById(R.id.photo_retour_btn);
		retour_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
				 
				Fragment newFragment = new ViewAddAnimal();
				ft.replace(R.id.contentFrag, newFragment);
				ft.commit();
				
			}
		});
		
		photo_take_btn=(Button) view.findViewById(R.id.photo_take_btn);
		photo_take_btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mCamera.takePicture(null, mPictureCallback, mPictureCallback);		
				
			}
		});
		
		
		return view;
	}
	 
	
	/**
	 * Enregistre la photo
	 */
	Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {
		public void onPictureTaken(byte[] imageData, Camera c) {

			if (imageData != null) {
				
				Bitmap myImage = BitmapFactory.decodeByteArray(imageData, 0,imageData.length);
				
				 ByteArrayOutputStream stream = new ByteArrayOutputStream();

				 myImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
	                byte[] byteArray = stream.toByteArray();

	                // Convert ByteArray to Bitmap::

	                Bitmap bitmap = BitmapFactory.decodeByteArray(byteArray, 0,
	                        byteArray.length);


				/*Intent mIntent = new Intent();

				CompressImage img= new CompressImage(100, getActivity().getApplicationContext());
				img.setImageToByte(imageData, imageData.length);
				
				img.compress("test");
				mCamera.startPreview();
				getActivity().setResult(FOTO_MODE, mIntent);	*/
				
				
				
				//Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.enregistrementPhoto), 1000).show();
				 

			}
		}
	};

	
	/**
	 * Création de la surface et ouverture de la camera
	 */
	public void surfaceCreated(SurfaceHolder holder)
	{		
		if(mCamera !=null)
			mCamera.release();			
		
		mCamera = Camera.open();  
	}

	/**
	 * Permet de
	 */
	public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) 
	{	
		// XXX stopPreview() will crash if preview is not running
		if (mPreviewRunning) {
			mCamera.stopPreview();
		}

		Camera.Parameters p = mCamera.getParameters();
		
		/**
		 * A faire propre pour s'adapter à la taille de l'écran
		 */
	
		//par défaut on met cette taille si on n'a pas d'appareil photo
		//a voir si on met la taille dans le fichier config.properties
		
		boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
		if (tabletSize) {
		    // do something
		} else {
			mCamera.setDisplayOrientation(90);
		}
		
		//p.set("orientation", "portrait");
		p.setPreviewSize(720, 480);
		mCamera.setParameters(p);
		try {
			mCamera.setPreviewDisplay(holder);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mCamera.startPreview();
		mPreviewRunning = true; 
	}

	public void surfaceDestroyed(SurfaceHolder holder) 
	{
		mCamera.stopPreview();
		mPreviewRunning = false;
		mCamera.release();
		
		
	}

	private SurfaceView mSurfaceView;
	private SurfaceHolder mSurfaceHolder;

	/**
	 * Permet d'effectuer l'action lorsqu'il y a clique sur le bouton
	 */
	public void onClick(View arg0) 
	{
		mCamera.takePicture(null, mPictureCallback, mPictureCallback);		
		
	}
	
}