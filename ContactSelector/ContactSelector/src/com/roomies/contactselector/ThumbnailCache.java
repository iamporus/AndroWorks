package com.roomies.contactselector;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class ThumbnailCache extends LruCache<Long,Bitmap> {

	public ThumbnailCache(int maxSize) {
		super(maxSize);

	}
	
	@Override
	protected int sizeOf(Long key, Bitmap value) {
		
		ByteArrayOutputStream bao = new ByteArrayOutputStream();
		value.compress(Bitmap.CompressFormat.PNG, 100, bao);
		byte[] ba = bao.toByteArray();
				
		return ba.length;
	}

}
