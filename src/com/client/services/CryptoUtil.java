package com.client.services;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

import javax.crypto.Cipher;

public class CryptoUtil {
		
	/**
	 * 
	 * @param pk
	 * @param data
	 * @return
	 */
		public static byte[] encrypt(PrivateKey pk, byte[] data) {
			byte[] re = null;

			try {
				Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
				cipher.init(Cipher.ENCRYPT_MODE, pk);
				re = cipher.doFinal(data);
			} catch (Exception e) {

			}
			return re;

		}

		/**
		 * 
		 * @param pk
		 * @param raw
		 * @return
		 */
		public static byte[] decrypt(PublicKey pk, byte[] raw){

			byte[] re=null;
			try{
				Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
				cipher.init(Cipher.DECRYPT_MODE, pk);
				re=cipher.doFinal(raw);

			}catch(Exception e){
				
			}
				return re;
		}
		
		/**
		 * Hash a given byte array using SHA agorithm
		 * @param file byte array
		 * @return hash of the file
		 */

		public static byte[] getHash(byte[] file) 
		{
			MessageDigest md = null;
			try 
			{
				md = MessageDigest.getInstance("SHA");
			} 
			catch (NoSuchAlgorithmException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			md.update(file);
			byte[] Digest = md.digest();
			return Digest;

		}
		
		/**
		 * 
		 * @param doc
		 * @param key
		 * @return
		 * @throws InvalidKeyException 
		 * @throws NoSuchAlgorithmException 
		 * @throws SignatureException 
		 */
		public static byte[] signDoc(byte[] doc, PrivateKey key) throws InvalidKeyException, NoSuchAlgorithmException, SignatureException
		{
			Signature signature = Signature.getInstance("SHA256withRSA");
	        signature.initSign(key);
	        signature.update(doc);
	        return signature.sign();
		}
		
	
		/**
		 * 
		 * @param data
		 * @param sigBytes
		 * @param publicKey
		 * @return
		 * @throws NoSuchAlgorithmException
		 * @throws InvalidKeyException
		 * @throws SignatureException
		 */
		public boolean verifyDoc(byte[] data, byte[] sigBytes, PublicKey publicKey) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException {
	        Signature signature = Signature.getInstance("SHA256withRSA");
	        signature.initVerify(publicKey);
	        signature.update(data);
	        return signature.verify(sigBytes);
		}
}
