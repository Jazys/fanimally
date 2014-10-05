package fr.jacquemet.fanimally;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;
import fr.jacquemet.fanimally.bdd.FanimallyContentProvider;
import fr.jacquemet.fanimally.bdd.Table_Animal;
import fr.jacquemet.fanimally.log.SRSDexception;
import fr.jacquemet.fanimally.utils.VetoName;
import fr.jacquemet.fanimally.view.ViewAnimalList;
import fr.jacquemet.fanimally.view.ViewItemGestion;

public class FanimallyActivity extends FragmentActivity {
	
	private Button gere_animal;
	private Button album;
	private Button amis;
	private Button veto;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		
		//Thread.setDefaultUncaughtExceptionHandler(new SRSDexception());

		requestWindowFeature(Window.FEATURE_NO_TITLE);   

		/*this.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
				| WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
				| WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
				| WindowManager.LayoutParams.FLAG_FULLSCREEN);*/

		
		
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

		Fragment newFragment = new ViewAnimalList();
		ft.add(R.id.contentFrag, newFragment);
		ft.commit();
		
		setContentView(R.layout.main); 
		
		/*ContentValues contact = new ContentValues();
		contact.put(Table_Animal.NAME, "test");
	
		getContentResolver().insert(FaminallyContentProvider.CONTENT_URI_ANIMAL, contact);

		contact.clear();*/
		
		String columns[] = new String[] { Table_Animal.ID };
		Uri mContacts = FanimallyContentProvider.CONTENT_URI_ANIMAL;
		Cursor cur = getContentResolver().query(mContacts, columns, null, null, null);
		//System.out.println("testest "+cur.getCount());
		//cur.close();
		
		gere_animal=(Button) findViewById(R.id.buttonGererFaminally);
		
		gere_animal.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

				Fragment newFragment = new ViewItemGestion();
				ft.replace(R.id.contentFrag, newFragment);
				ft.commit();
				
			}
		});
		

		/*if (savedInstanceState == null) {
			getSupportFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.famimally, menu);
		return true;
	}  
	
	@Override
	public void onSaveInstanceState(Bundle state) {
		super.onSaveInstanceState(state);	
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.contentFrag);
	    fragment.onActivityResult(requestCode, resultCode, data);
	}
	
	public void callVeto(View v) 
	{
		VetoName item = (VetoName)v.getTag();
		Toast.makeText(this, item.getTel(), Toast.LENGTH_LONG).show(); 
		
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_fanimally,
					container, false);
			return rootView;
		}
	}

}
