package com.sample.parser;

import java.io.IOException;
import java.util.List;

import android.util.JsonReader;
import android.util.Log;

/*
{ 

	 "weight" : 5.0,  
	 
	 "start" : "http://ec2-54-68-225-240.us-west-2.compute.amazonaws.com:7474/db/data/node/94", 

	 "nodes" : 
	[ 

	"http://ec2-54-68-225-240.us-west-2.compute.amazonaws.com:7474/db/data/node/94", 

	"http://ec2-54-68-225-240.us-west-2.compute.amazonaws.com:7474/db/data/node/95" 

	], 


	 "length" : 1,  
	 "relationships" : 
	[ 
	"http://ec2-54-68-225-240.us-west-2.compute.amazonaws.com:7474/db/data/relationship/20" 

	],  


	"end" : "http://ec2-54-68-225-240.us-west-2.compute.amazonaws.com:7474/db/data/node/95"

	}*/
public class ReadPathJson {

	public static List<String> readShortestPathJson(JsonReader reader) throws IOException {
		int nodeId = -1;
		reader.beginObject();
		
		List<String> nodes = null;
	 
        while (reader.hasNext()) {
        	
          String name = reader.nextName();
         
        int weight;
		if (name.equals("weight")) 
        	  weight = reader.nextInt();
          
          
        else if (name.equals("nodes")) {
        	  Log.w("parsing json nodes", name);
        	 
        	 reader.beginArray();
         	 while (reader.hasNext()) {
         		 
         		nodes.add(reader.nextString());
         		//nodeId = readData(reader);
         	     }
         	 reader.endArray();
          }
		
        else if (name.equals("relationships")) {
      	  Log.w("parsing json nodes", name);
      	 
      	 reader.beginArray();
       	 while (reader.hasNext()) {
       		 
       		nodes.add(reader.nextString());
       		//nodeId = readData(reader);
       	     }
       	 reader.endArray();
        }
		
          else
        	  reader.skipValue();

      }
        reader.endObject();

        return nodes;
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
