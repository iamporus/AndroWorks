package com.roomies.contactselector;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract.Contacts;
import android.support.v4.content.AsyncTaskLoader;

public class ContactsLoader extends AsyncTaskLoader<ArrayList<Contact>> {

	// hold a reference to the loader's data
	private ArrayList<Contact> mData;
	private ContactsObserver mObserver;
	private ContentResolver cr;
	private Uri baseUri;
	private String select;
	private Cursor cur;

	static final String[] CONTACTS_SUMMARY_PROJECTION = new String[] {
			Contacts._ID, Contacts.DISPLAY_NAME, Contacts.CONTACT_STATUS,
			Contacts.CONTACT_PRESENCE, Contacts.PHOTO_ID, Contacts.LOOKUP_KEY, };

	public ContactsLoader(Context context) {
		super(context);

		baseUri = Contacts.CONTENT_URI;

		select = "((" + Contacts.DISPLAY_NAME + " NOTNULL) AND ("
				+ Contacts.HAS_PHONE_NUMBER + "=1) AND ("
				+ Contacts.DISPLAY_NAME + " != '' ))";

		cr = context.getContentResolver();

	}

	@Override
	public ArrayList<Contact> loadInBackground() {

		/*
		 * This method is called on a background thread and should generate a
		 * new set of data to be delivered back to the client.
		 */

		ArrayList<Contact> data = new ArrayList<Contact>();

		// TODO: Perform the query here and add the results to 'data'.
		cur = cr.query(baseUri, CONTACTS_SUMMARY_PROJECTION, select, null,
				Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC");

		if (cur.moveToFirst()) {
			do {

				Contact contact = new Contact();
				contact.setFirstName(cur.getString(cur
						.getColumnIndex(Contacts.DISPLAY_NAME_PRIMARY)));
				contact.set_id(cur.getLong(cur.getColumnIndex(Contacts._ID)));
				contact.setThumbnail(Contacts.openContactPhotoInputStream(
						cr,
						ContentUris.withAppendedId(Contacts.CONTENT_URI,
								cur.getLong(cur.getColumnIndex(Contacts._ID)))));

				data.add(contact);
			} while (cur.moveToNext());
		}

		return data;
	}

	 public InputStream openDisplayPhoto(long contactId) {
	     Uri contactUri = ContentUris.withAppendedId(Contacts.CONTENT_URI, contactId);
	     Uri displayPhotoUri = Uri.withAppendedPath(contactUri, Contacts.Photo.DISPLAY_PHOTO);
	     try {
	         AssetFileDescriptor fd =
	             cr.openAssetFileDescriptor(displayPhotoUri, "r");
	         return fd.createInputStream();
	     } catch (IOException e) {
	         return null;
	     }
	 }
	 
	
	public InputStream openPhoto(long contactId) {
		Uri contactUri = ContentUris.withAppendedId(Contacts.CONTENT_URI,
				contactId);
		Uri photoUri = Uri.withAppendedPath(contactUri,
				Contacts.Photo.CONTENT_DIRECTORY);
		Cursor cursor = cr.query(photoUri,
				new String[] { Contacts.Photo.PHOTO }, null, null, null);
		if (cursor == null) {
			return null;
		}
		try {
			if (cursor.moveToFirst()) {
				byte[] data = cursor.getBlob(0);
				if (data != null) {
					return new ByteArrayInputStream(data);
				}
			}
		} finally {
			cursor.close();
		}
		return null;
	}

	@Override
	public void deliverResult(ArrayList<Contact> data) {

		if (isReset()) {
			// The Loader has been reset; ignore the result and invalidate the
			// data.
			onReleaseResources(data);
			return;
		}

		/*
		 * Hold a reference to the old data so it doesn't get garbage collected.
		 * The old data may still be in use (i.e. bound to an adapter, etc.), so
		 * we must protect it until the new data has been delivered.
		 */

		ArrayList<Contact> oldData = mData;
		mData = data;

		if (isStarted()) {

			/*
			 * If the Loader is in a started state, deliver the results to the
			 * client. The superclass method does this for us.
			 */

			super.deliverResult(data);
		}

		// Invalidate the old data as we don't need it any more.
		if (oldData != null && oldData != data) {
			onReleaseResources(oldData);
		}

	}

	@Override
	protected void onStartLoading() {
		super.onStartLoading();
		// Loader is in a started state..

		if (mData != null) {
			// Deliver any previously loaded data immediately.
			deliverResult(mData);
		}
		// Begin monitoring the underlying data source.
		if (mObserver == null) {
			mObserver = new ContactsObserver();
			// TODO: register the observer
		}
		if (takeContentChanged() || mData == null) {
			/*
			 * When the observer detects a change, it should call
			 * onContentChanged() on the Loader, which will cause the next call
			 * to takeContentChanged() to return true. If this is ever the case
			 * (or if the current data is null), we force a new load.
			 */
			forceLoad();
		}
	}

	@Override
	protected void onStopLoading() {
		// The Loader is in a stopped state, so we should attempt to cancel the
		// current load (if there is one).
		cancelLoad();

		// Note that we leave the observer as is; Loaders in a stopped state
		// should still monitor the data source for changes so that the Loader
		// will know to force a new load if it is ever started again.
	}

	@Override
	public void onCanceled(ArrayList<Contact> data) {
		// Attempt to cancel the current asynchronous load.
		super.onCanceled(data);

		// The load has been canceled, so we should release the resources
		// associated with 'data'.
		onReleaseResources(data);
	}

	protected void onReleaseResources(ArrayList<Contact> data) {
		// For a simple List, there is nothing to do. For something like a
		// Cursor, we
		// would close it in this method. All resources associated with the
		// Loader
		// should be released here.
		cur.close();
	}

}
