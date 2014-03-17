package com.client.services;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.client.data.ClientStore;
import com.client.model.User;

public class TestKeyManager {
	
	public static void main(String[] arg)
	{
		
		User user1 = new User("alice");
		User user2 = new User("bob");
		
		ClientStore store = new ClientStore(user1, user2, "alice", "bob", "alicestore", "keystore/alicekeystore.jks");
		store.prepareDocToSend("testFiles/input.txt");
		ClientTTPExchange ex = new ClientTTPExchange(store);
		ex.registerWithTTP();
//		try {
//			ex.sendFile();
//			ex.getFile();
//			ex.getEoo();
//			ex.getEor();
//		} catch (ClientProtocolException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
	
		
		
		
		
		
//		try {
//			KeyStore store = KeyManager.getKeyStore("alicestore", "keystore/alicekeystore.jks");
//			KeyPair pair = KeyManager.getKeyPair(store, "alice", "alicestore".toCharArray());
//			PrivateKey privateKey = KeyManager.getPrivateKey(pair);
//			PublicKey publicKey = KeyManager.getPublicKey(pair);
//			byte[] pBytes = privateKey.getEncoded();
////			System.out.println(Arrays.toString(pBytes));
////			System.out.println(pBytes.length);
////			System.out.println(publicKey.getEncoded().length);
//			
//			byte[] fileBy = FileProcessor.fileToBytes("testFiles/input.txt");
//			//byte[] cipherBytes = CryptoUtil.encrypt(privateKey, fileBy);
//			//byte[] plainBytes = CryptoUtil.decrypt(publicKey, cipherBytes);
//			byte[] sigBytes = CryptoUtil.signDoc(fileBy, privateKey);
//			System.out.println(CryptoUtil.verifyDoc(fileBy, sigBytes, publicKey));
//			FileProcessor.bytesToFile(fileBy, "testFiles/output.txt");
//		} catch (KeyStoreException | NoSuchAlgorithmException
//				| CertificateException | IOException | UnrecoverableKeyException | InvalidKeyException | SignatureException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

}
