package fr.jacquemet.fanimally;

import java.io.FileOutputStream;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;

public class TakePhotoActivity extends Activity
{
	private String pathPhoto;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE );
		intent.putExtra(MediaStore.EXTRA_SCREEN_ORIENTATION, ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); 		
		startActivityForResult( intent, 0 );
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{		
		if (requestCode == 0) {
            if (resultCode == Activity.RESULT_OK) {
                        	
            	// Find the last picture
            	String[] projection = new String[]{
            	    MediaStore.Images.ImageColumns._ID,
            	    MediaStore.Images.ImageColumns.DATA,
            	    MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME,
            	    MediaStore.Images.ImageColumns.DATE_TAKEN,
            	    MediaStore.Images.ImageColumns.MIME_TYPE
            	    };
            	final Cursor cursor = getContentResolver()
            	        .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, 
            	               null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");

            	// Put it in the image view
            	if (cursor.moveToFirst()) {
            	    
            		pathPhoto = cursor.getString(1); 
            	    
            		
            		//permet de faire en sorte que l'image soit une icône
            	    BitmapFactory.Options options = new BitmapFactory.Options();
            	    options.inSampleSize = 2;
            	    Bitmap bm = BitmapFactory.decodeFile(pathPhoto, options); 
            	    bm=Bitmap.createScaledBitmap(bm, 150, 150, false);
            	    //photoAnimal.setImageBitmap(Bitmap.createScaledBitmap(bm, 150, 150, false));    

            	    FileOutputStream out = null;
            	    try {
            	           out = new FileOutputStream(pathPhoto);
            	           bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            	    } catch (Exception e) {
            	        e.printStackTrace();
            	    } finally {
            	           try{
            	               out.close();
            	           } catch(Throwable ignore) {}
            	    }
            	   
            	} 

            }
        } 
		
		finish();
	}
	
	

}
