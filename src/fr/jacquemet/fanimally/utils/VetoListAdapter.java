package fr.jacquemet.fanimally.utils;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import fr.jacquemet.fanimally.R;

public class VetoListAdapter extends ArrayAdapter<VetoName> {

	private List<VetoName> items;
	private int layoutResourceId;
	private Context context;

	public VetoListAdapter(Context context, int layoutResourceId, List<VetoName> items) {
		super(context, layoutResourceId, items);
		this.layoutResourceId = layoutResourceId;
		this.context = context;
		this.items = items;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		AtomPaymentHolder holder = null;

		LayoutInflater inflater = ((Activity) context).getLayoutInflater();
		row = inflater.inflate(layoutResourceId, parent, false);

		holder = new AtomPaymentHolder();
		holder.atomVeto = items.get(position);
		holder.buttonCall = (Button)row.findViewById(R.id.callbutton);
		holder.buttonCall.setTag(holder.atomVeto);

		holder.name = (TextView)row.findViewById(R.id.nametxt);
	

		row.setTag(holder);

		setupItem(holder);
		return row;
	}

	private void setupItem(AtomPaymentHolder holder) {
		holder.name.setText(holder.atomVeto.getName());
	
	}

	public static class AtomPaymentHolder {
		VetoName atomVeto;
		TextView name;	
		Button buttonCall;
	}
}