package com.client.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriBuilder;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import com.client.data.ClientStore;
import com.client.model.User;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.representation.Form;

public class ClientTTPExchange {
	private ClientStore clientStore;
	private Client client;
	private static  String BASE_URI = "http://localhost:8080/project/rest";

	
	public ClientTTPExchange(ClientStore clientStore)
	{
		this.clientStore = clientStore;
	}
	
	/**
	 * 
	 */
	private void connect() {
		   ClientConfig config = new DefaultClientConfig();
		   client = Client.create(config);
		   client.setReadTimeout(50000);		   
		}
	
	/**
	 * 
	 */
	private void disconnect() {
	    client.destroy();
	}
	
	/**
	 * 
	 */
	public void registerWithTTP()
	{
		User user = clientStore.getSender();		
		WebResource service = client.resource(UriBuilder.fromUri(BASE_URI).build());
		Form form = new Form(); 
		form.add("id", user.getId());    
		form.add("publicKkey", user.getPublicKey());    		
		
		//send this form to the server and get response
	    ClientResponse response = service.path("/register/client").type(MediaType.APPLICATION_FORM_URLENCODED)
	            .post(ClientResponse.class, form);
	    System.out.println(response.getEntity(String.class));
	}
	
	/**
	 * 
	 * @param path
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public void sendFile(String path) throws ClientProtocolException, IOException
	{
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(BASE_URI + "/upload/file");

		FileBody file = new FileBody(clientStore.getFile());
		StringBody sender = new StringBody(clientStore.getSender().getId());
		StringBody receiver = new StringBody(clientStore.getReceiver().getId());
		StringBody eoo = new StringBody(new String(Base64.encodeBase64(clientStore.getEoo())));
		MultipartEntity reqEntity = new MultipartEntity();
		reqEntity.addPart("file", file);
		reqEntity.addPart("receiver", receiver);
		reqEntity.addPart("sender", sender);
		reqEntity.addPart("eoo", eoo);		
		httppost.setEntity(reqEntity);
		HttpResponse response = httpclient.execute(httppost);
		HttpEntity resEntity = response.getEntity();		
		BufferedReader rd = new BufferedReader(new InputStreamReader(resEntity.getContent()));
	      String line = "";
	      while ((line = rd.readLine()) != null) {
	        System.out.println(line);
	      }     
	}
	
	/**
	 * 
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public void getFile() throws ClientProtocolException, IOException
	{
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(BASE_URI + "/download/file");
	
		byte[] hash = clientStore.getHashOfDoc();
		StringBody fileId = new StringBody(new String(Base64.encodeBase64(hash)));
		StringBody sender = new StringBody(clientStore.getSender().getId());
		StringBody receiver = new StringBody(clientStore.getReceiver().getId());
		StringBody eor = new StringBody(new String(Base64.encodeBase64(clientStore.getEor())));
		MultipartEntity reqEntity = new MultipartEntity();
		reqEntity.addPart("receiver", receiver);
		reqEntity.addPart("sender", sender);
		reqEntity.addPart("eor", eor);
		reqEntity.addPart("fileId", fileId);
		
		httppost.setEntity(reqEntity);

		HttpResponse response = httpclient.execute(httppost);
		HttpEntity resEntity = response.getEntity();
		
		if (response.getStatusLine().getStatusCode() != 200) {
			System.out.println("Error: Bad request");
			BufferedReader rd = new BufferedReader(new InputStreamReader(resEntity.getContent()));
			String line = "";
			while ((line = rd.readLine()) != null) {
				System.out.println(line);
			}
		}
	}
	
	/**
	 * 
	 */
	public void getEor()
	{
		User user = clientStore.getSender();
        byte[] hash = clientStore.getHashOfDoc();
        String fileId = new String(Base64.encodeBase64(hash));
		WebResource service = client.resource(UriBuilder.fromUri(BASE_URI).build());
		Form form = new Form(); 
		form.add("clientId", user.getId());    
		form.add("fileId", fileId);    		
		
		//send this form to the server and get response
	    ClientResponse response = service.path("/get/eor").type(MediaType.APPLICATION_FORM_URLENCODED)
	            .post(ClientResponse.class, form);
	    System.out.println(response.getEntity(String.class));
	    
		if (response.getStatus() == 200) {
			byte[] eor = response.getEntity(String.class).getBytes();
			clientStore.setEor(eor);
			try {
				boolean verified = CryptoUtil.verifyDoc(clientStore.getEoo(),
						eor, clientStore.getReceiver().getPublicKey());
				if (!verified)
					System.out.println("Sender could not verify the EOR.");
			} catch (InvalidKeyException | NoSuchAlgorithmException
					| SignatureException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			 System.out.println("Error: " + response.getEntity(String.class));
		}
	}
	
	/**
	 * 
	 */
	public void getEoo()
	{
		User user = clientStore.getSender();
        byte[] hash = clientStore.getHashOfDoc();
        String fileId = new String(Base64.encodeBase64(hash));
		WebResource service = client.resource(UriBuilder.fromUri(BASE_URI).build());
		Form form = new Form(); 
		form.add("clientId", user.getId());    
		form.add("fileId", fileId);    		
		
		//send this form to the server and get response
	    ClientResponse response = service.path("/get/eoo").type(MediaType.APPLICATION_FORM_URLENCODED)
	            .post(ClientResponse.class, form);
	    System.out.println(response.getEntity(String.class));
	    
	    if (response.getStatus() == 200) {
			byte[] eoo = response.getEntity(String.class).getBytes();
			try {
				byte[] eor = CryptoUtil.signDoc(eoo, clientStore.getSender().getPrivateKey());
				clientStore.setEoo(eoo);
			} catch (InvalidKeyException | NoSuchAlgorithmException
					| SignatureException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		else
		{
			 System.out.println("Error: " + response.getEntity(String.class));
		}
	}
	

}
