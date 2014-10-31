package com.sample.parser;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.util.JsonReader;
import android.util.Log;

@TargetApi(Build.VERSION_CODES.HONEYCOMB) @SuppressLint("NewApi") 
public class ReadNodeinPath {
	public static String readClassFromNode(JsonReader reader) throws IOException {
		String nodeId = null;
		reader.beginObject();
	 
        while (reader.hasNext()) {
        	
          String name = reader.nextName();
         
          
          
          if (name.equals("data")) 
          {
        	 Log.w("parsing json data", name); 
        	 reader.beginObject();
         	 while (reader.hasNext()) {
         		if (name.equals("roomNo")) {
         		nodeId = reader.nextString();
         	     }
         		
         		 else
               	  reader.skipValue();

         	 reader.endArray();
            }
         	reader.endObject();
          }
          else
        	  reader.skipValue();

      }
        reader.endObject();

        return nodeId;
	}

}
