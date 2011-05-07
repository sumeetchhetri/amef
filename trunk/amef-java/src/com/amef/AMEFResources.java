package com.amef;
/*
        Copyright 2011, Sumeet Chhetri 
  
    Licensed under the Apache License, Version 2.0 (the "License"); 
    you may not use this file except in compliance with the License. 
    You may obtain a copy of the License at 
  
        http://www.apache.org/licenses/LICENSE-2.0 
  
    Unless required by applicable law or agreed to in writing, software 
    distributed under the License is distributed on an "AS IS" BASIS, 
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. 
    See the License for the specific language governing permissions and 
    limitations under the License.  
*/


import com.amef.AMEFDecoder;
import com.amef.AMEFEncoder;

public final class AMEFResources
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
		return new AMEFEncoder();
	}
	
	public static AMEFDecoder getDecoder()
	{
		return new AMEFDecoder();
	}
	
	public static byte[] longToByteArray(long l,int ind) 
	{
		byte[] result = new byte[ind];
		for (int i = 0; i<ind; i++)
		{
			int offset = (ind - 1 - i) * 8;
            result[i] = (byte) ((l >>> offset) & 0xFF);
		}
        return result;
    }
	
	public static String longToByteArrayS(long l,int ind) 
	{
		byte[] result = new byte[ind];
		for (int i = 0; i<ind; i++)
		{
			int offset = (ind - 1 - i) * 8;
            result[i] = (byte) ((l >>> offset) & 0xFF);
		}
        return new String(result);
    }
	
	public static byte[] intToByteArray(int l,int ind) 
	{
		byte[] result = new byte[ind];
		for (int i = 0; i<ind; i++)
		{
			int offset = (ind - 1 - i) * 8;
            result[i] = (byte) ((l >>> offset) & 0xFF);
		}
        return result;
    }
	
	public static byte[] intToByteArrayWI(int l) 
	{
		int ind = 1;
		if(l<256)
			ind =1;
		else if(l<65536)
			ind = 2;
		else if(l<16777216)
			ind =3;
		else
			ind =4;
		byte[] result = new byte[ind];
		for (int i = 0; i<ind; i++)
		{
			int offset = (ind - 1 - i) * 8;
            result[i] = (byte) ((l >>> offset) & 0xFF);
		}
        return result;
    }
	
	public static int byteArrayToInt(byte[] l) 
	{
		int t = 0;
		int ind = l.length;
        for (int i = 0; i < l.length; i++)
		{
        	int offset = (ind -1 - i) * 8;
        	t += (l[i] & 0x000000FF) << offset; 
		}
        return t;
    }
	
	public static int byteArrayToInt(byte[] l,int off,int ind) 
	{
		int t = 0;
		for (int i = 0; i < ind; i++)
		{
        	int offset = (ind -1 - i) * 8;
        	t += (l[off+i] & 0x000000FF) << offset; 
		}
        return t;
    }
	
	public static long byteArrayToLong(byte[] l) 
	{
		long t = 0;
		int ind = l.length;
        for (int i = 0; i < l.length; i++)
		{
        	int offset = (ind -1 - i) * 8;
        	t += (l[i] & 0x000000FF) << offset; 
		}
        return t;
    }
	public static long byteArrayToLong(byte[] l,int off,int ind) 
	{
		long t = 0;
		for (int i = 0; i < ind; i++)
		{
        	int offset = (ind -1 - i) * 8;
        	t += (l[off+i] & 0x000000FF) << offset; 
		}
        return t;
    }
	public static long byteArrayToLong(byte[] l,int ind) 
	{
		long t = 0;
		for (int i = 0; i < ind; i++)
		{
        	int offset = (ind -1 - i) * 8;
        	t += (l[i] & 0x000000FF) << offset; 
		}
        return t;
    }

	public static String intToByteArrayS(int l, int ind)
	{
		byte[] result = new byte[ind];
		for (int i = 0; i<ind; i++)
		{
			int offset = (ind - 1 - i) * 8;
            result[i] = (byte) ((l >>> offset) & 0xFF);
		}
        return new String(result);
    }
	
	
}
