package com.roomies.contactselector;

import java.util.ArrayList;

import com.google.gson.Gson;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;

public class ContactPickerFragment extends Fragment implements
		LoaderCallbacks<ArrayList<Contact>> {

	public static ContactPickerFragment newInstance(int arg) {

		ContactPickerFragment f = new ContactPickerFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("index", arg);
		f.setArguments(bundle);

		return f;
	}

	private AppListAdapter mAdapter;
	private AutoCompleteTextView mAutoCompleteTextView;
	private ListView mlistView;
	private ArrayList<Contact> selectedContacts;
	private AppListAdapter mListAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("onCreate()");
		int mIndex = getArguments().getInt("index");
		System.out.println(mIndex);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		System.out.println("onCreateView()");

		View view = inflater.inflate(R.layout.activity_main, container, false);
		mAutoCompleteTextView = (AutoCompleteTextView) view
				.findViewById(R.id.multiAutoCompleteTextView1);
		mAutoCompleteTextView.setHighlightColor(Color.GREEN);
		mAutoCompleteTextView.setThreshold(1);
		mlistView = (ListView) view.findViewById(R.id.listView1);

		mAutoCompleteTextView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> adapterView, View view,
					int position, long arg3) {

				selectedContacts.add(mAdapter.getItem(position));
				mAdapter.remove(mAdapter.getItem(position));
				mAdapter.notifyDataSetChanged();
				mListAdapter.setData(selectedContacts);
				mListAdapter.notifyDataSetChanged();
				mAutoCompleteTextView.setText("");
			}
		});

		if (selectedContacts == null) {
			selectedContacts = new ArrayList<Contact>();
			mListAdapter = new AppListAdapter(getActivity(),
					selectedContacts,true);
		} else {
			mListAdapter = new AppListAdapter(getActivity(),
					selectedContacts,true);
			mListAdapter.setData(selectedContacts);
		}

		mlistView.setAdapter(mListAdapter);

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		System.out.println("onActivityCreated()");
		getLoaderManager().initLoader(1, null, this);
	}

	@Override
	public Loader<ArrayList<Contact>> onCreateLoader(int arg0, Bundle arg1) {

		System.out.println("onCreateLoader()");
		return new ContactsLoader(getActivity());
	}

	@Override
	public void onLoadFinished(Loader<ArrayList<Contact>> arg0,
			ArrayList<Contact> data) {

		System.out.println("onLoaderFinished()");
		mAdapter = new AppListAdapter(getActivity(), data,false);
		mAdapter.setData(data);

		mAutoCompleteTextView.setAdapter(mAdapter);

	}

	@Override
	public void onLoaderReset(Loader<ArrayList<Contact>> arg0) {
		// This is called when the last Cursor provided to onLoadFinished()
		// above is about to be closed. We need to make sure we are no
		// longer using it.
		mAdapter.setData(null);
	}

}
