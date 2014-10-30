package com.sample.parser;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;




import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.util.JsonReader;
import android.util.JsonToken;
import android.util.Log;

//{
//	"results":
//	[
//     {
//		"columns":["ID(n)"],
//		 "data":[
//			{"row":[63]}
//				]
//		}
//	],
//"errors":[]
//}


@SuppressLint("NewApi") public class JsonParser {
	
	  @TargetApi(Build.VERSION_CODES.HONEYCOMB) @SuppressLint("NewApi") 
	  
	  public static int readJsonStream(InputStream in) throws IOException {
	        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
	        try {
	          return  ReadNodeIDJson.readNodeJson(reader);
	        }
	         finally {
	          reader.close();
	        }
	      }
	  
	  public static List<String> readJsonPathStream(InputStream in) throws IOException {
	        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
	        try {
	          return  ReadPathJson.readShortestPathJson(reader);
	        }
	         finally {
	          reader.close();
	        }
	      }
	  

	
	      
}
