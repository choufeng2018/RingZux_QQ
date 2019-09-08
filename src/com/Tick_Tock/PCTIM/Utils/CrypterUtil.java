package com.Tick_Tock.PCTIM.Utils;
import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;
import java.security.*;

public class CrypterUtil
{

	public CrypterUtil()
	{
	}

	public static long getUnsignedInt(byte in[], int offset, int len)
	{
		long ret = 0L;
		int end = 0;
		if(len > 8)
			end = offset + 8;
		else
			end = offset + len;
		for(int i = offset; i < end; i++)
		{
			ret <<= 8;
			ret |= in[i] & 0xff;
		}

		return ret & 0xffffffffL | ret >>> 32;
	}


	
	
	public static Random random()
	{
		if(random == null)
			random = new Random();
		return random;
	}

	public static byte[] randomKey()
	{
		byte key[] = new byte[16];
		random().nextBytes(key);
		return key;
	}

	
	private static Random random;
}

