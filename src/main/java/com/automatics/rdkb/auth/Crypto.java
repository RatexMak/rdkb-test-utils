/*
 * Copyright 2021 Comcast Cable Communications Management, LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package com.automatics.rdkb.auth;

import java.security.GeneralSecurityException;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import com.automatics.error.GeneralError;
import com.automatics.exceptions.FailedTransitionException;
import com.automatics.rdkb.constants.BroadBandPropertyKeyConstants;
import com.automatics.utils.AutomaticsPropertyUtility;

public class Crypto {
	

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
    	
	SecretKeySpec sks = new SecretKeySpec(hexStringToByteArray(AutomaticsPropertyUtility
			.getProperty(BroadBandPropertyKeyConstants.PROP_KEY_AES_ENCRYPTION_KEY)), "AES");
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
