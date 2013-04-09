package com.roomies.contactselector;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

public class SelectedListAdapter extends ArrayAdapter<Contact> implements Filterable {
	private LayoutInflater mInflater;
	private Bitmap bmp;
	private ByteArrayOutputStream baos;
	private Context context;
	private ArrayList<Contact> data;
	private ArrayList<Contact> tempData;
	private ArrayList<Contact> suggestions;

	@SuppressWarnings("unchecked")
	public SelectedListAdapter(Context ctx, ArrayList<Contact> data) {
		super(ctx, android.R.layout.simple_list_item_2);
		mInflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		context = ctx;
		this.tempData = ((ArrayList<Contact>) data.clone());
		this.suggestions = new ArrayList<Contact>();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view;

		if (convertView == null) {
			view = mInflater.inflate(R.layout.selected_list_item_icon_text, parent, false);
		} else {
			view = convertView;
		}

		Contact item = getItem(position);

		bmp = MainActivity.cacheManager.get(item.get_id());
		if (bmp != null)
			((ImageView) view.findViewById(R.id.icon)).setImageBitmap(bmp);
		else {
			bmp = BitmapFactory.decodeStream(item.getThumbnail());
			if (bmp != null){
				MainActivity.cacheManager.put(item.get_id(), bmp);
				
			}
			else {
				bmp = BitmapFactory.decodeResource(context.getResources(),
						R.drawable.ic_launcher_shortcut_contact);
				((ImageView) view.findViewById(R.id.icon)).setImageBitmap(bmp);
			}
		}
		((TextView) view.findViewById(R.id.text)).setText(item.getFirstName());

		return view;
	}

	@Override
	public Filter getFilter() {

		Filter filter = new Filter() {

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence arg0,
					FilterResults filterResults) {

				data = ((ArrayList<Contact>) filterResults.values);

				setData(data);
				notifyDataSetChanged();
			}

			@Override
			public CharSequence convertResultToString(Object resultValue) {
				String string = ((Contact) resultValue).getFirstName();
				return string;
			}

			@Override
			public FilterResults performFiltering(CharSequence searchQuery) {

				FilterResults filterResults = new FilterResults();
				System.out.println("performing filtering.." + searchQuery);
				if (searchQuery != null) {

					suggestions.clear();

					for (int i = 0; i < tempData.size(); i++) {
						if (tempData
								.get(i)
								.getFirstName().toLowerCase().startsWith(
										searchQuery.toString().toLowerCase())) {
							
							System.out.println("Found at : " + i);
							suggestions.add(tempData.get(i));
						}
					}

					filterResults.values = suggestions;
					filterResults.count = suggestions.size();

					return filterResults;

				} else {
					return filterResults;
				}

			}
		};

		return filter;
	}

	public void setData(ArrayList<Contact> data) {
		clear();
		if (data != null) {
			for (int i = 0; i < data.size(); i++) {
				add(data.get(i));
			}
		}
		this.data = data;

	}
}
