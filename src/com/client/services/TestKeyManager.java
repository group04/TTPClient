package com.client.services;

import java.io.IOException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

public class TestKeyManager {
	
	public static void main(String[] arg)
	{
		try {
			KeyStore store = KeyManager.getKeyStore("alicestore", "keystore/alicekeystore.jks");
			KeyPair pair = KeyManager.getKeyPair(store, "alice", "alicestore".toCharArray());
			Key privateKey = KeyManager.getPrivateKey(pair);
			Key publicKey = KeyManager.getPublicKey(pair);
//			byte[] pBytes = privateKey.getEncoded();
//			System.out.println(Arrays.toString(pBytes));
//			System.out.println(pBytes.length);
//			System.out.println(publicKey.getEncoded().length);
			
			byte[] fileBy = FileProcessor.fileToBytes("testFiles/input.txt");
			byte[] cipherBytes = CryptoUtil.encrypt((PrivateKey)privateKey, fileBy);
			byte[] plainBytes = CryptoUtil.decrypt((PublicKey)publicKey, cipherBytes);
			FileProcessor.bytesToFile(plainBytes, "testFiles/output.txt");
		} catch (KeyStoreException | NoSuchAlgorithmException
				| CertificateException | IOException | UnrecoverableKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
