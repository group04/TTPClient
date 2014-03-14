package com.client.services;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Arrays;

public class TestKeyManager {
	
	public static void main(String[] arg)
	{
		try {
			KeyStore store = KeyManager.getKeyStore("alicestore", "keystore/alicekeystore.jks");
			KeyPair pair = KeyManager.getKeyPair(store, "alice", "alicestore".toCharArray());
			PrivateKey privateKey = KeyManager.getPrivateKey(pair);
			PublicKey publicKey = KeyManager.getPublicKey(pair);
			byte[] pBytes = privateKey.getEncoded();
//			System.out.println(Arrays.toString(pBytes));
//			System.out.println(pBytes.length);
//			System.out.println(publicKey.getEncoded().length);
			
			byte[] fileBy = FileProcessor.fileToBytes("testFiles/input.txt");
			//byte[] cipherBytes = CryptoUtil.encrypt(privateKey, fileBy);
			//byte[] plainBytes = CryptoUtil.decrypt(publicKey, cipherBytes);
			byte[] sigBytes = CryptoUtil.signDoc(fileBy, privateKey);
			System.out.println(CryptoUtil.verifyDoc(fileBy, sigBytes, publicKey));
			FileProcessor.bytesToFile(fileBy, "testFiles/output.txt");
		} catch (KeyStoreException | NoSuchAlgorithmException
				| CertificateException | IOException | UnrecoverableKeyException | InvalidKeyException | SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
