package eterface.tools;

/**
 * Provides security measures for the e-terface, such as encryption.
 * Since browsing history could be saved to a computer, users may
 * want to encrypt the method and parameter portion of their request.
 * This is to avoid exposing the directory structure of their
 * desktop, and possibly any files or work being done.
 * 
 * For now, this is mostly just a stub - only implementing a very
 * simplistic encryption just for the sake of having one. This could
 * grow in the future.
 * 
 * @author Mike Czapik
 */

import java.util.*;

public class EterfaceSecurity {
	private boolean enabled;
	private int key;
	
	public EterfaceSecurity() {
		enabled = false;
		key = 1;
	}
	
	public EterfaceSecurity(boolean doEnable) {
		enabled = doEnable;
		key = 0x9B;
	}
	
	public int getKey() { return key; }
	
	public void setKey(int k) { key = k; }
	
	public String encrypt(String data) {
		if(!enabled) {
			return data;
		}
		
		byte[] arr = data.getBytes();
		
		for(int i = 0; i < arr.length; i++) {
			arr[i] ^= key;
		}
		
		return arr.toString();
	}
}
