package com.amef;

import java.io.ByteArrayOutputStream;


public class AMEFResources
{
	private AMEFResources()
	{
		encoder = new AMEFEncoder();
		decoder = new AMEFDecoder();
	}
	private volatile static AMEFResources jdbResources;

	private AMEFEncoder encoder;

	private AMEFDecoder decoder;

	private static AMEFResources get()
	{
		if(jdbResources==null)
		{
			synchronized(AMEFResources.class)
			{
				if(jdbResources==null)
				{
					jdbResources= new AMEFResources();
				}
			}
		}
		return jdbResources;
	}

	public static AMEFEncoder getEncoder()
	{
		return get().encoder;
	}

	public static AMEFDecoder getDecoder()
	{
		return get().decoder;
	}

	public static byte[] longToByteArray(long l,int ind)
	{
		byte[] result = new byte[ind];
		for (int i = 0; i<ind; i++)
		{
			int offset = (ind - 1 - i) * 8;
			result[i] = (byte) (l >>> offset & 0xFF);
		}
		return result;
	}

	public static String longToByteArrayS(long l,int ind)
	{
		byte[] result = new byte[ind];
		for (int i = 0; i<ind; i++)
		{
			int offset = (ind - 1 - i) * 8;
			result[i] = (byte) (l >>> offset & 0xFF);
		}
		return new String(result);
	}

	public static byte[] intToByteArray(int l,int ind)
	{
		byte[] result = new byte[ind];
		for (int i = 0; i<ind; i++)
		{
			int offset = (ind - 1 - i) * 8;
			result[i] = (byte) (l >>> offset & 0xFF);
		}
		return result;
	}

	public static void intToByteArray(byte[] result, int l, int ind)
	{
		for (int i = 0; i<ind; i++)
		{
			int offset = (ind - 1 - i) * 8;
			result[i] = (byte) (l >>> offset & 0xFF);
		}
	}

	public static void intToByteArray(byte[] result, int off, int l, int ind)
	{
		for (int i = 0; i<ind; i++)
		{
			int offset = (ind - 1 - i) * 8;
			result[off+i] = (byte) (l >>> offset & 0xFF);
		}
	}

	public static void intToByteArray(ByteArrayOutputStream result, int l, int ind)
	{
		for (int i = 0; i<ind; i++)
		{
			int offset = (ind - 1 - i) * 8;
			result.write((byte) (l >>> offset & 0xFF));
		}
	}

	public static void intToByteArray(ByteArrayOutputStream result, long l, int ind)
	{
		for (int i = 0; i<ind; i++)
		{
			int offset = (ind - 1 - i) * 8;
			result.write((byte) (l >>> offset & 0xFF));
		}
	}

	public static byte[] intToByteArrayWI(int l)
	{
		int ind = 1;
		if(l<256) {
			ind =1;
		} else if(l<65536) {
			ind = 2;
		} else if(l<16777216) {
			ind =3;
		} else {
			ind =4;
		}
		byte[] result = new byte[ind];
		for (int i = 0; i<ind; i++)
		{
			int offset = (ind - 1 - i) * 8;
			if(offset>0) {
				result[i] = (byte) (l >>> offset & 0xFF);
			} else {
				result[i] = (byte) l;
			}
		}
		return result;
	}

	public static byte[] longToByteArrayWI(long l)
	{
		int ind = 1;
		if(l<256) {
			ind =1;
		} else if(l<65536) {
			ind = 2;
		} else if(l<16777216) {
			ind =3;
		} else if(l<4294967296L) {
			ind =4;
		} else if(l<1099511627776L) {
			ind =5;
		} else if(l<281474976710656L) {
			ind =6;
		} else if(l<72057594037927936L) {
			ind =7;
		} else {
			ind =8;
		}
		byte[] result = new byte[ind];
		for (int i = 0; i<ind; i++)
		{
			int offset = (ind - 1 - i) * 8;
			result[i] = (byte) (l >>> offset & 0xFF);
		}
		return result;
	}

	public static int byteArrayToInt(byte[] l)
	{
		int t = 0;
		for (int i = 0; i < l.length; i++)
		{
			t = (t << 8) + (l[i] & 0xff);
		}
		return t;
	}

	public static int byteArrayToInt(byte[] l,int off,int ind)
	{
		int t = 0;
		for (int i = 0; i < ind; i++)
		{
			//int offset = (ind -1 - i) * 8;
			//t += (l[off+i] & 0x000000FF) << offset;
			t = (t << 8) + (l[off+i] & 0xff);
		}
		return t;
	}

	public static long byteArrayToLong(byte[] l)
	{
		long t = 0;
		//int ind = l.length;
		for (int i = 0; i < l.length; i++)
		{
			//int offset = (ind -1 - i) * 8;
			//t += (l[i] & 0x000000FF) << offset;
			t = (t << 8) + (l[i] & 0xff);
		}
		return t;
	}
	public static long byteArrayToLong(byte[] l,int off,int ind)
	{
		long t = 0;
		for (int i = 0; i < ind; i++)
		{
			//int offset = (ind -1 - i) * 8;
			//t += (l[off+i] & 0x000000FF) << offset;
			t = (t << 8) + (l[off+i] & 0xff);
		}
		return t;
	}
	public static long byteArrayToLong(byte[] l,int ind)
	{
		long t = 0;
		for (int i = 0; i < ind; i++)
		{
			//int offset = (ind -1 - i) * 8;
			//t += (l[i] & 0x000000FF) << offset;
			t = (t << 8) + (l[i] & 0xff);
		}
		//ByteBuffer wrapped = ByteBuffer.wrap(l, 0, ind); // big-endian by default
		//t = wrapped.getLong();
		//if(t<0 || t>1024000) {
		//}
		return t;
	}

	public static String intToByteArrayS(int l, int ind)
	{
		byte[] result = new byte[ind];
		for (int i = 0; i<ind; i++)
		{
			int offset = (ind - 1 - i) * 8;
			result[i] = (byte) (l >>> offset & 0xFF);
		}
		return new String(result);
	}


}
