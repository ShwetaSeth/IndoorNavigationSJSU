package com.sample.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.util.JsonReader;
import android.util.JsonToken;
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
@TargetApi(Build.VERSION_CODES.HONEYCOMB) @SuppressLint("NewApi") 
public class ReadPathJson {

	@SuppressWarnings("null")
	public static ShortestPath readShortestPathJson(JsonReader reader) throws IOException {
		int nodeId = -1;
		reader.beginObject();
		
		ShortestPath path = new ShortestPath();
		
		List<String> nodes = new ArrayList<String>();
		List<String> relationships = new ArrayList<String>();
	 
        while (reader.hasNext()) {
        	
          String name = reader.nextName();
         
        int weight;
		if (name.equals("weight")) 
        	  path.setWeight(reader.nextInt());
          
          
		else if (name.equals("start")) 
      	  path.setStartNode(reader.nextString());
		
		
		
        else if (name.equals("nodes")&& reader.peek() != JsonToken.NULL) {
        	  Log.w("parsing json nodes", name);
  
        	 reader.beginArray();
         	 while (reader.hasNext()) {
         		nodes.add(reader.nextString());
         		//nodeId = readData(reader);
         	     }
         	 path.setNodes(nodes);
         	 reader.endArray();
          }
		
        else if (name.equals("length")) 
	      	  path.setLengthPath(reader.nextInt());
		
        else if (name.equals("relationships")&& reader.peek() != JsonToken.NULL) {
      	  Log.w("parsing json relationships", name);
      	 
      	 reader.beginArray();
       	 while (reader.hasNext()) {
       		 
       		relationships.add(reader.nextString());
       		//nodeId = readData(reader);
       	     }
       	 path.setRelationshipBetNodesOfPath(relationships);
       	 reader.endArray();
        }
		
    	else if (name.equals("end")) 
        	  path.setEnd(reader.nextString());
		
          else
        	  reader.skipValue();

      }
        reader.endObject();

        return path;
}
	
	public static String readClassFromNodeURI(JsonReader reader) throws IOException {
		String nodeId = null;
		reader.beginObject();
	 
        while (reader.hasNext()) {
        	
          String name = reader.nextName();
         
          
          
          if (name.equals("data")) 
          {
        	 Log.w("parsing json data", name); 
        	 reader.beginObject();
         	 while (reader.hasNext()) 
         	 {
         		name = reader.nextName();
         		if (name.equals("roomNo")) {
         		nodeId = reader.nextString();
         	     }
         		
         		 else
               	  reader.skipValue();
            }
         	reader.endObject();
          }
          else
        	  reader.skipValue();

      }
        reader.endObject();

        return nodeId;
	}
	
	public static Relationship readRelationFromRelationshipURI(JsonReader reader) throws IOException {
		String nodeId = null;
		reader.beginObject();
		Relationship rel = new Relationship();
	 
        while (reader.hasNext()) {
        	
          String name = reader.nextName();
         
    
          if (name.equals("data")) 
          {  
        	 reader.beginObject();
         	 while (reader.hasNext()) 
         	 {
         		name = reader.nextName();
         		if (name.equals("to")) 
         			 rel.setToNode( reader.nextString());
         		
         		else if (name.equals("orientation")) 
        			 rel.setOrientation( reader.nextString());
         		
         		else if (name.equals("from")) 
       			 rel.setFromNode( reader.nextString());
         		
         		else if (name.equals("cost")) 
       			 rel.setCost( reader.nextString());
  
         		 else
               	  reader.skipValue();
            }
         	reader.endObject();
          }
          else
        	  reader.skipValue();

      }
        reader.endObject();

        return rel;
	}
	

}
