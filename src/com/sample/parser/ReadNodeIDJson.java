package com.sample.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.util.JsonReader;
import android.util.Log;

@TargetApi(Build.VERSION_CODES.HONEYCOMB) @SuppressLint("NewApi") 
public class ReadNodeIDJson {
	
	static int readNodeJson(JsonReader reader) throws IOException {
		int nodeId = -1;
			reader.beginObject();
		 
	        while (reader.hasNext()) {
	        	
	          String name = reader.nextName();
	         
	          
	          
	          if (name.equals("results")) {
	        	  Log.w("parsing json results", name);
	        	 
	        	 reader.beginArray();
	         	 while (reader.hasNext()) {
	         		nodeId = readData(reader);
	         	     }
	         	 reader.endArray();
	          }
	          else
	        	  reader.skipValue();
	
	      }
	        reader.endObject();

	        return nodeId;
	}
	
	
	private static int readData(JsonReader reader) throws IOException {
		int nodeId = -1;
		
		reader.beginObject();

		while (reader.hasNext()) 
		{

			// i value = reader.nextInt();
			String name = reader.nextName();
			// Log.w("MainActivity", name);
			

			if (name.equals("data")) 
			{
				Log.w("parsing json data", name);
				reader.beginArray();
				while (reader.hasNext()) {
					nodeId = readRow(reader);
				}
				reader.endArray();
			}
			
			 else
		            reader.skipValue();

		
	      }
		reader.endObject();

        return nodeId;
	}

	private static int readRow(JsonReader reader) throws IOException {
		//List<Result> results = new ArrayList<Result>();
		int nodeId = -1;
			reader.beginObject();
		 
	        while (reader.hasNext()) {
	          String name = reader.nextName();
	        
	          if (name.equals("row")) {
	        	  Log.w("parsing json row", name);
	        	  reader.beginArray();
					while (reader.hasNext()) {
						nodeId = reader.nextInt();
					}
					reader.endArray();
	        	// nodeId = reader.nextInt();
	          }
	          else
	              reader.skipValue();
	       
	      }
	        reader.endObject();

	        return nodeId;
	}
}
