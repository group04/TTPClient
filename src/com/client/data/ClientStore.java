package com.client.data;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;

import com.client.model.User;
import com.client.services.CryptoUtil;
import com.client.services.FileProcessor;
import com.client.services.KeyManager;

public class ClientStore {
	private User sender;
	private User receiver;
	private String senderAlias;
	private String receiverAlias;
	private String keyStorePassword;
	private String keyStorePath;
	private byte[] docToSend;
	private byte[] hashOfDoc;
	private byte[] eoo;
	private byte[] eor;
	
	private File file;
	
	/**
	 * 
	 * @param client
	 * @param alias
	 * @param keyStorePassword
	 * @param keyStorePath
	 */
	public ClientStore(User sender, User receiver, String senderAlias, String receiverAlias, String keyStorePassword, String keyStorePath)
	{
		this.sender = sender;
		this.receiver = receiver;
		this.senderAlias = senderAlias;
		this.receiverAlias = receiverAlias;
		this.keyStorePassword = keyStorePassword;
		this.keyStorePath = keyStorePath;	
		
	    // set client's private and public keys
		prepareKeys();
	}
	
	/**
	 * 
	 */
	private void prepareKeys()
	{
		try {
			KeyStore keyStore = KeyManager.getKeyStore(keyStorePassword, keyStorePath);
			KeyPair keyPair = KeyManager.getKeyPair(keyStore, senderAlias, keyStorePassword.toCharArray());
			sender.setPrivateKey(keyPair.getPrivate());
			sender.setPublicKey(keyPair.getPublic());
			
			// public key for receiver 
			// Get certificate of public key
            Certificate cert = keyStore.getCertificate(receiverAlias);
            PublicKey publicKey = cert.getPublicKey();
			receiver.setPublicKey(publicKey);
		} catch (KeyStoreException | NoSuchAlgorithmException
				| CertificateException | IOException | UnrecoverableKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * 
	 * @param docPath
	 */
	public void prepareDocToSend(String docPath)
	{
		try {
			file = new File(docPath);
			docToSend = FileProcessor.fileToBytes(docPath);
			hashOfDoc = CryptoUtil.getHash(docToSend);
			eoo = CryptoUtil.signDoc(hashOfDoc, sender.getPrivateKey());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SignatureException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @return the client
	 */
	public User getSender() {
		return sender;
	}

	/**
	 * @param client the client to set
	 */
	public void setSender(User client) {
		this.sender = client;
	}

	public User getReceiver() {
		return receiver;
	}

	public void setReceiver(User receiver) {
		this.receiver = receiver;
	}

	/**
	 * @return the alias
	 */
	public String getAlias() {
		return senderAlias;
	}

	/**
	 * @param alias the alias to set
	 */
	public void setAlias(String alias) {
		this.senderAlias = alias;
	}

	/**
	 * @return the keyStorePassword
	 */
	public String getKeyStorePassword() {
		return keyStorePassword;
	}

	/**
	 * @param keyStorePassword the keyStorePassword to set
	 */
	public void setKeyStorePassword(String keyStorePassword) {
		this.keyStorePassword = keyStorePassword;
	}

	/**
	 * @return the keyStorePath
	 */
	public String getKeyStorePath() {
		return keyStorePath;
	}

	/**
	 * @param keyStorePath the keyStorePath to set
	 */
	public void setKeyStorePath(String keyStorePath) {
		this.keyStorePath = keyStorePath;
	}

	/**
	 * @return the docToSend
	 */
	public byte[] getDocToSend() {
		return docToSend;
	}

	/**
	 * @param docToSend the docToSend to set
	 */
	public void setDocToSend(byte[] docToSend) {
		this.docToSend = docToSend;
	}

	/**
	 * @return the hashOfDoc
	 */
	public byte[] getHashOfDoc() {
		return hashOfDoc;
	}

	/**
	 * @param hashOfDoc the hashOfDoc to set
	 */
	public void setHashOfDoc(byte[] hashOfDoc) {
		this.hashOfDoc = hashOfDoc;
	}

	/**
	 * @return the eoo
	 */
	public byte[] getEoo() {
		return eoo;
	}

	/**
	 * @param eoo the eoo to set
	 */
	public void setEoo(byte[] eoo) {
		this.eoo = eoo;
	}

	/**
	 * @return the eor
	 */
	public byte[] getEor() {
		return eor;
	}

	/**
	 * @param eor the eor to set
	 */
	public void setEor(byte[] eor) {
		this.eor = eor;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}
}
