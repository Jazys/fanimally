package fr.jacquemet.fanimally.photo;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;



public class CompressImage
{
	private int quality;
	private byte[] imageToByte;
	private FileOutputStream outputFile;
	private Context ctx;
    
	/**
	 * Permet de compresser une image
	 * @param quality
	 */
	public CompressImage(int quality, Context ctx)
	{
		this.quality=quality;		
		this.ctx=ctx;
	}
	
	/**
	 * Permet de compresser une photo
	 * @param nameOfFile
	 * @return
	 */
	public boolean compress(String nameOfFile)
	{
		
		
		
		
		BitmapFactory.Options options=new BitmapFactory.Options();
		options.inSampleSize = 0;
			
			
		Bitmap myImage = BitmapFactory.decodeByteArray(imageToByte, 0,imageToByte.length);
		try {
		
		String path = Environment.getExternalStorageDirectory().toString();
		OutputStream fOut = null;
		File file = new File(path, "test"+".jpg");
		fOut = new FileOutputStream(file);

		myImage.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
		fOut.flush();
		fOut.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//MediaStore.Images.Media.insertImage(ctx.getContentResolver(), myImage, "test" , "test");
			
		try {
			
			//enregistre l'image au chemin indiqué 
			
			
			outputFile = new FileOutputStream(Environment.getExternalStorageDirectory()+"/"+nameOfFile+".jpg");
			
			BufferedOutputStream bos = new BufferedOutputStream(outputFile);			
			
			//compression de l'image
			myImage.compress(CompressFormat.JPEG, quality, bos);

			bos.flush();
			bos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return true;
		
	}
	
	/**
	 * Permet de copier une image
	 * @param img
	 * @param taille
	 */
	public void setImageToByte(byte[] img, int taille)
	{
		imageToByte=new byte[taille];
		imageToByte=img;
	}
  

}
