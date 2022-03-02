package com.automatics.rdkb.auth;

import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import com.automatics.error.GeneralError;
import com.automatics.exceptions.FailedTransitionException;

public class Crypto {
	
	 /** The AES key used for encryption and decryption. */
    private static final String AES_ENCRYPTION_KEY = "";
	
	/**
     * Decrypt the given data using known key.
     *
     * @param data
     *            The data to be decrypted.
     *
     * @return The actual decrypted value.
     *
     * @throws GeneralSecurityException
     *             If any upnormal execution.
     */
    public static String decrypt(String data) {
	SecretKeySpec sks = new SecretKeySpec(hexStringToByteArray(AES_ENCRYPTION_KEY), "AES");
	byte[] decrypted = null;

	try {
	    Cipher cipher = Cipher.getInstance("AES");
	    cipher.init(Cipher.DECRYPT_MODE, sks);

	    decrypted = cipher.doFinal(hexStringToByteArray(data));
	} catch (GeneralSecurityException gse) {
	    throw new FailedTransitionException(GeneralError.SECURITY_ISSUE, gse);
	}

	return new String(decrypted);
    }
    
    /**
     * 
     * @param s
     * @return
     */
    private static byte[] hexStringToByteArray(String s) {
	byte[] b = new byte[s.length() / 2];

	for (int i = 0; i < b.length; i++) {
	    int index = i * 2;
	    int v = Integer.parseInt(s.substring(index, index + 2), 16);
	    b[i] = (byte) v;
	}

	return b;
    }


}