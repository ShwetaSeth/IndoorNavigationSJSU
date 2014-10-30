package com.sample.compass;



import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;


import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.ExecutionContext;

import com.sample.parser.JsonParser;

//import com.google.gson.Gson;

//import android.support.v7.app.ActionBarActivity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceActivity.Header;
import android.util.Log;
import android.view.*;
import android.widget.*;


public class DatabaseUtil extends AsyncTask<Context, Context, Context>
{
	
	private Button button;
	static Context  context;
	public static String SERVER_ROOT_URI = "http://ec2-54-68-225-240.us-west-2.compute.amazonaws.com:7474/db/data/";
	@Override
	protected Context doInBackground(Context... params) {
	
	    context = params[0];
	    
		//checkDatabaseIsRunning(context);
	   URI firstNode = null;
	   URI secondNode = null;
	   URI thirdNode = null;
	   URI fourthNode = null;
	   try {
		   
	   int id1 = getNodeId("101");
	   
	   if(id1<0)
	   {
		firstNode = createNode();
	
	   addProperty( firstNode, "roomNo", "101" );
	   }
	   
	   int id2 = getNodeId("102");
	   
	   if(id2<0)
	   {
	   secondNode = null;
	   secondNode = createNode();
	   addProperty( secondNode, "roomNo", "102" );
	   }
	   
	   int id3 = getNodeId("103");
	   
	   if(id3<0)
	   {
	   thirdNode = null;
	   thirdNode = createNode();
	   addProperty( thirdNode, "roomNo", "103" );
	   }
	   
	   int id4 = getNodeId("104");
	   
	   if(id4<0)
	   {
	   fourthNode = null;
	   fourthNode = createNode();
	   addProperty( fourthNode, "roomNo", "104" );
	   }
	   }
	   catch (URISyntaxException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	   

		String string1 = "101";
		String string2 = "102";
		String string3 = "103";
		String string4 = "104";
		
		int id1 = -1, id2 = -1,id3 = -1, id4 = -1;
		
		try {
			id1 = getNodeId(string1);
			id2 = getNodeId(string2);
			id3 = getNodeId(string3);
			id4 = getNodeId(string4);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		String result = null;
	
		try {
			firstNode = new URI(SERVER_ROOT_URI+"node/"+String.valueOf(id1));
			secondNode = new URI(SERVER_ROOT_URI+"node/"+String.valueOf(id2));
			thirdNode = new URI(SERVER_ROOT_URI+"node/"+String.valueOf(id3));
			fourthNode = new URI(SERVER_ROOT_URI+"node/"+String.valueOf(id4));
			//getShortestPath(firstNode, secondNode, 10, result, "dijkstra");
		} 
	catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   
	   
		URI relationshipUri = null;
		try {
			relationshipUri = addRelationship( firstNode, secondNode, "to",
			           "{ \"from\" : \"101\", \"to\" : \"102\" }" );
			addPropertyToRelationship(relationshipUri,5);
			
			relationshipUri = addRelationship( secondNode, thirdNode, "to",
			           "{ \"from\" : \"102\", \"to\" : \"103\" }" );
			addPropertyToRelationship(relationshipUri,5);
			
			
			relationshipUri = addRelationship( thirdNode, fourthNode, "to",
			           "{ \"from\" : \"103\", \"to\" : \"104\" }" );
			addPropertyToRelationship(relationshipUri,5);
			
			
			
			
		} catch (URISyntaxException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		addPropertyToRelationship(relationshipUri,5);
		
		//Creating relationship between second and third node
		//System.out.println("RELATIONSHIP URI"+ relationshipUri);
		
		//get nodeid of a specific label and property
		
	
		
	
		try {
			
			getShortestPath(firstNode, secondNode, 10, result, "dijkstra");
		} 
	/*	catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} */
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}
	private void addPropertyToRelationship(URI relationshipUri,int distance) {
		HttpClient httpClient = new DefaultHttpClient();
		String propertyOfRelationshipUri = null;
		HttpResponse response = null;
		
		try {
			
			String propValue = "Room";
			propertyOfRelationshipUri = relationshipUri + "/properties/cost";
			HttpPut put = new HttpPut(propertyOfRelationshipUri);
			StringEntity entity = new StringEntity( "\"" + distance + "\"");
			//StringEntity entity = new StringEntity( "Room");
			entity.setContentType("application/json");
			put.setEntity(entity);	
			response = httpClient.execute(put);	
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String content = "";
		try{
            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
            
            String output  = "";
            while ((output = br.readLine()) != null) {
                content += output;
            }
		}catch(Exception e){
			e.printStackTrace();
		}
		Log.i("DBUTILS addLabel", String.format("PUT to [%s], status code [%d]", propertyOfRelationshipUri, response.getStatusLine().getStatusCode()) );
		//Log.i("DBUTILS addLabel", "Status Code: " + response.getStatusLine().getStatusCode());
		httpClient.getConnectionManager().shutdown();
		
		
		
		
	}
	private void addLabel(URI nodeEntryPointUri, String string, String string2) {
		HttpClient httpClient = new DefaultHttpClient();
		String propertyUri = null;
		HttpResponse response = null;
		try {
			
			String propValue = "Room";
			propertyUri = nodeEntryPointUri + "/labels/";
			HttpPut put = new HttpPut(propertyUri);
			StringEntity entity = new StringEntity( "\"" + propValue + "\"");
			//StringEntity entity = new StringEntity( "Room");
			entity.setContentType("application/json");
			put.setEntity(entity);	
			response = httpClient.execute(put);	
		} 
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String content = "";
		try{
            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
            
            String output  = "";
            while ((output = br.readLine()) != null) {
                content += output;
            }
		}catch(Exception e){
			e.printStackTrace();
		}
		Log.i("DBUTILS addLabel", String.format("PUT to [%s], status code [%d]", propertyUri, response.getStatusLine().getStatusCode()) );
		//Log.i("DBUTILS addLabel", "Status Code: " + response.getStatusLine().getStatusCode());
		httpClient.getConnectionManager().shutdown();
	}
	
	@SuppressLint("NewApi") 
	private int getNodeId(String roomNo) throws IOException {
		
		//String query ="MATCH (n) WHERE n.roomNo =\""+ roomNo+"\" RETURN n";
		String query = "start n=node(*) where (n.roomNo = '"+roomNo+"') return ID(n)";
		
		String payload = "{\"statements\" : [ {\"statement\" : \"" +query + "\"} ]}";
		
		final String txUri = SERVER_ROOT_URI + "transaction/commit";
		//String nodeUri =  SERVER_ROOT_URI + "/db/data/nodes?roomNo="+roomNo ;
		//String relationshipJson = generateJsonRelationship( endNode,relationshipType, jsonAttributes );
		HttpResponse response = null;
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(txUri);
		StringEntity entity;
		try {		
			entity = new StringEntity(payload);
			entity.setContentType("application/json");
			post.setEntity(entity);		
			response = httpClient.execute(post);	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String content = "";
		try{
            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
            String output = "";
            while ((output = br.readLine()) != null) {
				content += output;
				//Log.i("DBUTILS", output);
			}
			/*Log.i("DBUTILS", "--------------------------------");
			Log.i("DBUTILS", content);
			Log.i("DBUTILS", payload);*/
		}catch(Exception e){
			e.printStackTrace();
		}
		//Log.i("DBUTILS getNodeID", String.format("POST to [%s], status code [%d]", txUri, response.getStatusLine().getStatusCode()) );
		//Log.i("DBUTILS getNodeID", "Status Code: " + response.getStatusLine().getStatusCode());
		//return new URI(response.getLastHeader("Location").getValue());
		httpClient.getConnectionManager().shutdown();
		
		InputStream stream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
		
		int nodeID = JsonParser.readJsonStream(stream);
		Log.i("NodeID", String.valueOf(nodeID));
		
		return nodeID;
	}
	private String getShortestPath(URI fromNode, URI toNode, int max_depth,String json , String algorithm) throws IOException
    {
		// TODO Auto-generated method stub
		String fromURI = fromNode.toString() +"/path";
		String content = "";
		
		
		
		String[] shortestPathJson = generateJsonPath(toNode,algorithm,"{\"type\":\"to\",\"direction\" : \"out\"},");
		HttpResponse response = null;
		
		
		
		
		String payload = shortestPathJson[0].toString()+shortestPathJson[1].toString();
		
		Log.i("DBUTILS getShortestPath JSON", "JSON PAYLOAD "+payload);
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(fromURI);
		StringEntity entity;
        
		entity = new StringEntity(payload);
		//entity = new StringEntity("{ \"to\":\"http://ec2-54-68-225-240.us-west-2.compute.amazonaws.com:7474/db/data/node/62\", \"max_depth\" : 3, \"relationships\" : { \"type\" : \"Left\", \"direction\" : \"out\"},\"algorithm\" : \"shortestPath\"}");
		
		entity.setContentType("application/json");
        
		post.setEntity(entity);
        
		response = httpClient.execute(post);
		
		httpClient.getConnectionManager().shutdown();
		try{
            BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
            
            String output  = "";
            while ((output = br.readLine()) != null) {
                content += output;
            }
		}catch(Exception e){
			e.printStackTrace();
		}
		Log.i("DBUTILS", content);
		
		Log.i("DBUTILS getShortestPath", "Status Code: " + response.getStatusLine().getStatusCode());
		return content;
        
	}
    
    private String[] generateJsonPath(URI toNode,String algo,String...jsonAttributes)
	{
		String[] myStringArray = new String[2];
		// TODO Auto-generated method stub
		StringBuilder sb = new StringBuilder();
		sb.append( "{ \"to\" : \"" );
	    sb.append( toNode.toString() );
	    sb.append( "\", " );
	 //  sb.append( "\"max_depth\" : ");
	    myStringArray[0] = sb.toString();
	    
        //   int depth = Integer.parseInt(max_depth);
        //   sb.append(depth);
	    StringBuilder sb1 = new StringBuilder();
	    //sb1.append( "," );
	    if ( jsonAttributes == null || jsonAttributes.length < 1 )
	    {
	        sb1.append( "\"" );
	    }
	    else
	    {
	    	sb1.append("\"cost_property\" : \"cost\",");
	    	
	        sb1.append( "\"relationships\" : " );
	        for ( int i = 0; i < jsonAttributes.length; i++ )
	        {
	            sb1.append( jsonAttributes[i] );
                /** if ( i < jsonAttributes.length - 1 )
                 { // Miss off the final comma
                 sb.append( ", " );
                 }**/
	        }
	        sb1.append( "\"algorithm\" : \"" );
		    sb1.append(algo);
		    sb1.append("\"");
		    sb1.append("}");
            
	    }
       // System.out.println("SHORTEST PATH"+sb.toString());
        //System.out.println("SHORTEST PATH"+sb1.toString());
	    myStringArray[1] = sb1.toString();
	    return myStringArray;
    }
	private URI addRelationship(URI startNode, URI endNode, String relationshipType,String jsonAttributes) throws URISyntaxException{
		// TODO Auto-generated method stub
		String fromUri =  startNode.toString() + "/relationships" ;
		String relationshipJson = generateJsonRelationship( endNode,relationshipType, jsonAttributes );
		HttpResponse response = null;
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(fromUri);
		StringEntity entity;
		try {		
			entity = new StringEntity(relationshipJson);
			entity.setContentType("application/json");
			post.setEntity(entity);		
			response = httpClient.execute(post);	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		httpClient.getConnectionManager().shutdown();
		return new URI(response.getLastHeader("Location").getValue());
	}

	private String generateJsonRelationship(URI endNode,String relationshipType, String... jsonAttributes) {
		// TODO Auto-generated method stub
		System.out.println("INSIDE GENERATE JSON RELATIONSHIP");
		StringBuilder sb = new StringBuilder();
	    sb.append( "{ \"to\" : \"" );
	    sb.append( endNode.toString() );
	    sb.append( "\", " );

	    sb.append( "\"type\" : \"" );
	    sb.append( relationshipType );
	    if ( jsonAttributes == null || jsonAttributes.length < 1 )
	    {
	        sb.append( "\"" );
	    }
	    else
	    {
	        sb.append( "\", \"data\" : " );
	        for ( int i = 0; i < jsonAttributes.length; i++ )
	        {
	            sb.append( jsonAttributes[i] );
	            if ( i < jsonAttributes.length - 1 )
	            { // Miss off the final comma
	                sb.append( ", " );
	            }
	        }
	    }

	    sb.append( " }" );
	    System.out.println("JSON RELATIONSHIP "+ sb.toString());
	    return sb.toString();
		
	}

	private void addProperty(URI nodeEntryPointUri, String propName, String propValue) {
		HttpClient httpClient = new DefaultHttpClient();
		try {
			
			
			String propertyUri = nodeEntryPointUri + "/properties/" + propName;
			HttpPut put = new HttpPut(propertyUri);
			
			StringEntity entity = new StringEntity( "\"" + propValue + "\"");
			entity.setContentType("application/json");
			put.setEntity(entity);	
			// http://localhost:7474/db/data/node/{node_id}/properties/{property_name}
			HttpResponse response = httpClient.execute(put);	
			
			
			/*BufferedReader br = new BufferedReader(new InputStreamReader((response.getEntity().getContent())));
			String output = "";
			String content = "";
		
			while ((output = br.readLine()) != null) {
				content += output;
				Log.i("DBUTILS", output);
			}
			Log.i("DBUTILS", "--------------------------------");
			Log.i("DBUTILS", content);
			
			Log.i("DBUTILS", String.format("PUT to [%s], status code [%d]", propertyUri, response.getStatusLine().getStatusCode()) );*/
			Log.i("DBUTILS", String.format("PUT to [%s], status code [%d]", propertyUri, response.getStatusLine().getStatusCode()) );
		} 
		
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		httpClient.getConnectionManager().shutdown();
		
	}

	public static boolean isNetworkAvailable(Context context) {
	    ConnectivityManager connectivityManager 
	         = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
	    return activeNetworkInfo != null;
	}
	
	public static boolean hasActiveInternetConnection(Context context) {
	    if (isNetworkAvailable(context)) {
	        try {
	            HttpURLConnection urlc = (HttpURLConnection) (new URL("http://www.google.com").openConnection());
	            urlc.setRequestProperty("User-Agent", "Test");
	            urlc.setRequestProperty("Connection", "close");
	            urlc.setConnectTimeout(1500); 
	            urlc.connect();
	            Log.i("DBUTILS", "Internet: " + (urlc.getResponseCode() == 200));
	            return (urlc.getResponseCode() == 200);
	        } catch (IOException e) {
	            Log.e("DBUTILS", "Error checking internet connection", e);
	        }
	    } else {
	        Log.d("DBUTILS", "No network available!");
	    }
	    return false;
	}
	
	static void checkDatabaseIsRunning(Context context){
		if(hasActiveInternetConnection(context)){
			HttpClient httpClient = new DefaultHttpClient();
			
			try {
				HttpGet request = new HttpGet(SERVER_ROOT_URI + "node/1");
				request.addHeader("accept", "application/json");
				HttpResponse httpResponse = httpClient.execute(request);
				BufferedReader br = new BufferedReader(new InputStreamReader((httpResponse.getEntity().getContent())));
				/*String output = "";
				String content = "";
				while ((output = br.readLine()) != null) {
					content += output;
					Log.i("DBUTILS", output);
				}
				Log.i("DBUTILS", "--------------------------------");
				Log.i("DBUTILS", content);
				
				Log.i("DBUTILS", "Status Code: " + httpResponse.getStatusLine().getStatusCode());*/
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	static URI createNode() throws URISyntaxException {
		final String nodeEntryPointUri = SERVER_ROOT_URI + "node";
		 HttpResponse response = null;
		
		HttpClient httpClient = new DefaultHttpClient();
		HttpPost post = new HttpPost(nodeEntryPointUri);
		
		StringEntity entity;
		try {		
			entity = new StringEntity("{}");
			entity.setContentType("application/json");
			post.setEntity(entity);		
			response = httpClient.execute(post);	
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		httpClient.getConnectionManager().shutdown();
		
		return  new URI(response.getLastHeader("Location").getValue());
		
		
		
	 }
	
	
	
}

