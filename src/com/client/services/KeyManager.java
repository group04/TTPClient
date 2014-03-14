package com.client.services;

import java.io.File;
import java.io.FileInputStream;
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



public class KeyManager {
	
	/**
	 * 
	 * @param password
	 * @param path
	 * @return
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 * @throws CertificateException
	 * @throws IOException
	 */
	public static KeyStore getKeyStore(String password, String path) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException
	{
		KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
		File file = new File(path);
		FileInputStream is = new FileInputStream(file);
		keyStore.load(is, password.toCharArray());	
		return keyStore;
	}
	
	/**
	 * 
	 * @param keystore
	 * @param alias
	 * @param password
	 * @return
	 * @throws UnrecoverableKeyException
	 * @throws KeyStoreException
	 * @throws NoSuchAlgorithmException
	 */
	public static KeyPair getKeyPair(KeyStore keystore, String alias, char[] password) throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException {	  
	        // Get private key
	        Key privateKey = keystore.getKey(alias, password);
	        if (privateKey instanceof PrivateKey) {
	            // Get certificate of public key
	            java.security.cert.Certificate cert = keystore.getCertificate(alias);

	            // Get public key
	            PublicKey publicKey = cert.getPublicKey();

	            // Return a key pair
	            return new KeyPair(publicKey, (PrivateKey)privateKey);
	        }	   
	    return null;
	}
	
	/**
	 * 
	 * @param keyPair
	 * @return
	 */
	public static Key getPrivateKey(KeyPair keyPair)
	{
		return keyPair.getPrivate();
	}
	
	/**
	 * 
	 * @param keyPair
	 * @return
	 */
	public static Key getPublicKey(KeyPair keyPair)
	{
		return keyPair.getPublic();
	}
}
