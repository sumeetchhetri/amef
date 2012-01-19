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
package com.amef;
import com.amef.AMEFResources;

/**
 * @author sumeetc
 * The AMEFDecoder class
 * providses the decode method to get the AMEFObject from its transmission form
 */
public final class AMEFDecoder
{
	//private String tempVal = "";
	
	
	/**
	 * @param data
	 * @param considerLength
	 * @return AMEFObject
	 * @throws AMEFDecodeException
	 * decode the bytestream to give the equivalent AMEFObject
	 */
	/*public AMEFObject decode(byte[] data,boolean considerLength,boolean ignoreName) throws AMEFDecodeException
	{
		int startWith = 0;		
		if(considerLength)
			startWith = 4;
		String strdata = new String(data);
		strdata = strdata.substring(startWith);
		AMEFObject AMEFObject = null;
		try
		{
			AMEFObject = decodeSinglePacket(strdata,ignoreName);
		}
		catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return AMEFObject;
	}
	
	*//**
	 * @param data
	 * @param considerLength
	 * @return AMEFObject
	 * @throws AMEFDecodeException
	 * decode the bytestream to give the equivalent AMEFObject
	 *//*
	public AMEFObject decode(String strdata,boolean considerLength,boolean ignoreName) throws AMEFDecodeException
	{
		int startWith = 0;		
		if(considerLength)
			startWith = 4;
		strdata = strdata.substring(startWith);
		AMEFObject AMEFObject = null;
		try
		{
			AMEFObject = decodeSinglePacket(strdata,ignoreName);
		}
		catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return AMEFObject;
	}
	*/
	
	public AMEFObject decodeB(byte[] buffer,boolean considerLength,boolean ignoreName) throws AMEFDecodeException
	{
		if(buffer.length==0)
			return null;
		Integer position = 0;
		byte[] strdata = null;
		if(considerLength)
		{
			strdata = new byte[buffer.length - 4];
			System.arraycopy(buffer, 4, strdata, 0, strdata.length);
		}
		else
		{
			strdata = buffer;
		}
		//AMEFObject AMEFObject = ignoreName?decodeSinglePacketBINNew(strdata,position):decodeSinglePacketB(strdata,ignoreName,position);
		AMEFObject AMEFObject = decodeSinglePacketBINNew(strdata,position,ignoreName);
		return AMEFObject;
	}
	
	
	
	/*public AMEFObject decodeAMEFString(byte[] data,boolean considerLength) throws AMEFDecodeException
	{
		int startWith = 0;		
		if(considerLength)
			startWith = 4;
		String strdata = new String(data);
		strdata = strdata.substring(startWith);
		AMEFObject AMEFObject = decodeSingleAMEFStringPacket(strdata);
		return AMEFObject;
	}
	
	private AMEFObject decodeSingleAMEFStringPacket(String strdata) throws AMEFDecodeException
	{
		AMEFObject aMEFObject = null;
		char type = strdata.charAt(0);
		if(type==AMEFObject.STRING_TYPE)
		{			
			aMEFObject = new AMEFObject();
			aMEFObject.setType(type);
			int lengthm = ((strdata.charAt(1) & 0xff) << 24) | ((strdata.charAt(2) & 0xff) << 16)
							| ((strdata.charAt(3) & 0xff) << 8) | ((strdata.charAt(4) & 0xff));
			aMEFObject.setLength(lengthm);
			String value = strdata.substring(5,5+lengthm);
			aMEFObject.setValue(value);
			tempVal = strdata.substring(5+lengthm);			
		}
		else if(type==AMEFObject.STRING_65536_TYPE)
		{			
			aMEFObject = new AMEFObject();
			aMEFObject.setType(type);
			int lengthm = ((strdata.charAt(1) & 0xff) << 8) | (strdata.charAt(2) & 0xff);
			aMEFObject.setLength(lengthm);
			String value = strdata.substring(3,3+lengthm);
			aMEFObject.setValue(value);
			tempVal = strdata.substring(3+lengthm);			
		}
		else if(type==AMEFObject.STRING_16777216_TYPE)
		{			
			aMEFObject = new AMEFObject();
			aMEFObject.setType(type);
			int lengthm = ((strdata.charAt(1) & 0xff) << 16) 
							| ((strdata.charAt(2) & 0xff) << 8) | (strdata.charAt(3) & 0xff);
			aMEFObject.setLength(lengthm);
			String value = strdata.substring(3,3+lengthm);
			aMEFObject.setValue(value);
			tempVal = strdata.substring(3+lengthm);			
		}		
		else if(type==AMEFObject.DATE_TYPE || type==AMEFObject.STRING_256_TYPE || type==AMEFObject.DOUBLE_FLOAT_TYPE)
		{			
			aMEFObject = new AMEFObject();
			aMEFObject.setType(type);
			int lengthm = strdata.charAt(1) & 0xff;
			aMEFObject.setLength(lengthm);
			String value = strdata.substring(2,2+lengthm);
			aMEFObject.setValue(value);
			tempVal = strdata.substring(2+lengthm);			
		}
		else if(type==AMEFObject.VERY_SMALL_INT_TYPE)
		{			
			aMEFObject = new AMEFObject();
			aMEFObject.setType(type);
			String value = (strdata.charAt(1) & 0xff) + "";
			aMEFObject.setValue(value);
			tempVal = strdata.substring(2);			
		}
		else if(type==AMEFObject.SMALL_INT_TYPE)
		{			
			aMEFObject = new AMEFObject();
			aMEFObject.setType(type);
			int lengthm = ((strdata.charAt(1) & 0xff) << 8) | ((strdata.charAt(2) & 0xff));
			String value = lengthm + "";
			aMEFObject.setValue(value);
			tempVal = strdata.substring(3);			
		}
		else if(type==AMEFObject.BIG_INT_TYPE)
		{			
			aMEFObject = new AMEFObject();
			aMEFObject.setType(type);
			int lengthm = ((strdata.charAt(1) & 0xff) << 16) | ((strdata.charAt(2) & 0xff) << 8) 
							| ((strdata.charAt(3) & 0xff));
			String value = lengthm + "";
			aMEFObject.setValue(value);
			tempVal = strdata.substring(4);			
		}
		else if(type==AMEFObject.INT_TYPE)
		{			
			aMEFObject = new AMEFObject();
			aMEFObject.setType(type);
			int lengthm = ((strdata.charAt(1) & 0xff) << 24) | ((strdata.charAt(2) & 0xff) << 16)
							| ((strdata.charAt(3) & 0xff) << 8) | ((strdata.charAt(4) & 0xff));
			String value = lengthm + "";
			aMEFObject.setValue(value);
			tempVal = strdata.substring(5);			
		}
		else if(type==AMEFObject.VS_LONG_INT_TYPE)
		{			
			aMEFObject = new AMEFObject();
			aMEFObject.setType(type);
			long lengthm = ((strdata.charAt(1) & 0xff) << 32) | ((strdata.charAt(2) & 0xff) << 24) 
							| ((strdata.charAt(3) & 0xff) << 16) | ((strdata.charAt(4) & 0xff) << 8) 
							| ((strdata.charAt(5) & 0xff));
			String value = lengthm + "";
			aMEFObject.setValue(value);
			tempVal = strdata.substring(6);			
		}
		else if(type==AMEFObject.S_LONG_INT_TYPE)
		{			
			aMEFObject = new AMEFObject();
			aMEFObject.setType(type);
			long lengthm = ((strdata.charAt(1) & 0xff) << 40) | ((strdata.charAt(2) & 0xff) << 32) 
							| ((strdata.charAt(3) & 0xff) << 24) 
							| ((strdata.charAt(4) & 0xff) << 16) | ((strdata.charAt(5) & 0xff) << 8) 
							| ((strdata.charAt(6) & 0xff));
			String value = lengthm + "";
			aMEFObject.setValue(value);
			tempVal = strdata.substring(7);			
		}
		else if(type==AMEFObject.B_LONG_INT_TYPE)
		{			
			aMEFObject = new AMEFObject();
			aMEFObject.setType(type);
			long lengthm = ((strdata.charAt(1) & 0xff) << 48) | ((strdata.charAt(2) & 0xff) << 40) 
							| ((strdata.charAt(3) & 0xff) << 32) 
							| ((strdata.charAt(4) & 0xff) << 24) 
							| ((strdata.charAt(5) & 0xff) << 16) | ((strdata.charAt(6) & 0xff) << 8) 
							| ((strdata.charAt(7) & 0xff));
			String value = lengthm + "";
			aMEFObject.setValue(value);
			tempVal = strdata.substring(8);			
		}
		else if(type==AMEFObject.LONG_INT_TYPE)
		{			
			aMEFObject = new AMEFObject();
			aMEFObject.setType(type);
			long lengthm = ((strdata.charAt(1) & 0xff) << 56) | ((strdata.charAt(2) & 0xff) << 48) 
							| ((strdata.charAt(3) & 0xff) << 40) 
							| ((strdata.charAt(4) & 0xff) << 32) 
							| ((strdata.charAt(5) & 0xff) << 24) 
							| ((strdata.charAt(6) & 0xff) << 16) | ((strdata.charAt(7) & 0xff) << 8) 
							| ((strdata.charAt(8) & 0xff));
			String value = lengthm + "";
			aMEFObject.setValue(value);
			tempVal = strdata.substring(9);			
		}
		else if(type==AMEFObject.BOOLEAN_TYPE || type==AMEFObject.CHAR_TYPE)
		{
			aMEFObject = new AMEFObject();
			aMEFObject.setType(type);
			aMEFObject.setLength(1);
			String value = strdata.charAt(1)+"";
			aMEFObject.setValue(value);
			tempVal = strdata.substring(2);	
		}
		else if(type==AMEFObject.VS_OBJECT_TYPE)
		{			
			aMEFObject = new AMEFObject();
			aMEFObject.setType(type);
			int lengthm = AMEFResources.byteArrayToInt(strdata.substring(1,2).getBytes());
			aMEFObject.setLength(lengthm);
			tempVal = strdata.substring(2,2+lengthm);
			while(!tempVal.equals(""))
			{
				AMEFObject obj = decodeSingleAMEFStringPacket(tempVal);
				aMEFObject.addPacket(obj);
			}				
			tempVal = strdata.substring(2+lengthm);
		}
		else if(type==AMEFObject.S_OBJECT_TYPE)
		{			
			aMEFObject = new AMEFObject();
			aMEFObject.setType(type);
			int lengthm = ((strdata.charAt(1) & 0xff) << 8) | ((strdata.charAt(2) & 0xff));
			aMEFObject.setLength(lengthm);
			tempVal = strdata.substring(3,3+lengthm);
			while(!tempVal.equals(""))
			{
				AMEFObject obj = decodeSingleAMEFStringPacket(tempVal);
				aMEFObject.addPacket(obj);
			}				
			tempVal = strdata.substring(3+lengthm);
		}
		else if(type==AMEFObject.B_OBJECT_TYPE)
		{			
			aMEFObject = new AMEFObject();
			aMEFObject.setType(type);
			int lengthm = ((strdata.charAt(1) & 0xff) << 16)
							| ((strdata.charAt(2) & 0xff) << 8) | ((strdata.charAt(3) & 0xff));
			aMEFObject.setLength(lengthm);
			tempVal = strdata.substring(4,4+lengthm);
			while(!tempVal.equals(""))
			{
				AMEFObject obj = decodeSingleAMEFStringPacket(tempVal);
				aMEFObject.addPacket(obj);
			}				
			tempVal = strdata.substring(4+lengthm);
		}
		else if(type==AMEFObject.OBJECT_TYPE)
		{			
			aMEFObject = new AMEFObject();
			aMEFObject.setType(type);
			int lengthm = ((strdata.charAt(1) & 0xff) << 24) | ((strdata.charAt(2) & 0xff) << 16)
							| ((strdata.charAt(3) & 0xff) << 8) | ((strdata.charAt(4) & 0xff));
			aMEFObject.setLength(lengthm);
			tempVal = strdata.substring(5,5+lengthm);
			while(!tempVal.equals(""))
			{
				AMEFObject obj = decodeSingleAMEFStringPacket(tempVal);
				aMEFObject.addPacket(obj);
			}				
			tempVal = strdata.substring(5+lengthm);
		}
		return aMEFObject;
	}

	*//**
	 * @param strdata
	 * @return AMEFObject
	 * @throws AMEFDecodeException
	 * decode the string to give the equivalent AMEFObject
	 * @throws UnsupportedEncodingException 
	 *//*
	private AMEFObject decodeSinglePacket(String strdata,boolean ignoreName) throws AMEFDecodeException, UnsupportedEncodingException
	{
		AMEFObject aMEFObject = null;
		char type = strdata.charAt(0);
		if(type==AMEFObject.STRING_TYPE)
		{
			if(strdata.charAt(1)==',')
			{
				aMEFObject = new AMEFObject();
				aMEFObject.setType(type);
				int pos = 2;
				String name = "";
				if(!ignoreName)
				{
					while(strdata.charAt(pos)!=',')
					{
						name += strdata.charAt(pos++);
					}
					if(name.equals("") && strdata.charAt(2)!=',')
					{
						throw new AMEFDecodeException("Invalid character after name specifier, expected ,");
					}
				}
				if(pos>=strdata.length())
				{
					throw new AMEFDecodeException("Reached end of AMEF string, not found ,");
				}
				aMEFObject.setName(name);
				String length = "";
				if(!ignoreName)pos++;
				while(length.length()<4)
				{
					length += strdata.charAt(pos++);
				}
				if(length.equals("") && strdata.charAt(3)!=',')
				{
					throw new AMEFDecodeException("Invalid character after length specifier, expected ,");
				}
				else if(pos>=strdata.length())
				{
					throw new AMEFDecodeException("Reached end of AMEF string, not found ,");
				}
				int lengthm = ((length.getBytes()[0] & 0xff) << 24) | ((length.getBytes()[1] & 0xff) << 16)
								| ((length.getBytes()[2] & 0xff) << 8) | ((length.getBytes()[3] & 0xff));
				aMEFObject.setLength(lengthm);
				String value = strdata.substring(pos+1,pos+lengthm+1);
				aMEFObject.setValue(value);
				tempVal = strdata.substring(pos+lengthm+1);
			}
			else
			{
				throw new AMEFDecodeException("Invalid character after type specifier, expected ,");
			}
		}
		else if(type==AMEFObject.STRING_65536_TYPE)
		{
			if(strdata.charAt(1)==',')
			{
				aMEFObject = new AMEFObject();
				aMEFObject.setType(type);
				int pos = 2;
				String name = "";
				if(!ignoreName)
				{
					while(strdata.charAt(pos)!=',')
					{
						name += strdata.charAt(pos++);
					}
					if(name.equals("") && strdata.charAt(2)!=',')
					{
						throw new AMEFDecodeException("Invalid character after name specifier, expected ,");
					}
				}
				if(pos>=strdata.length())
				{
					throw new AMEFDecodeException("Reached end of AMEF string, not found ,");
				}
				aMEFObject.setName(name);
				String length = "";
				if(!ignoreName)pos++;
				while(length.length()<2)
				{
					length += strdata.charAt(pos++);
				}
				if(length.equals("") && strdata.charAt(3)!=',')
				{
					throw new AMEFDecodeException("Invalid character after length specifier, expected ,");
				}
				else if(pos>=strdata.length())
				{
					throw new AMEFDecodeException("Reached end of AMEF string, not found ,");
				}
				int lengthm = ((length.getBytes()[0] & 0xff) << 8) | ((length.getBytes()[1] & 0xff));
				aMEFObject.setLength(lengthm);
				String value = strdata.substring(pos,pos+lengthm);
				aMEFObject.setValue(value);
				tempVal = strdata.substring(pos+lengthm);
			}
			else
			{
				throw new AMEFDecodeException("Invalid character after type specifier, expected ,");
			}
		}
		else if(type==AMEFObject.STRING_16777216_TYPE)
		{
			if(strdata.charAt(1)==',')
			{
				aMEFObject = new AMEFObject();
				aMEFObject.setType(type);
				int pos = 2;
				String name = "";
				if(!ignoreName)
				{
					while(strdata.charAt(pos)!=',')
					{
						name += strdata.charAt(pos++);
					}
					if(name.equals("") && strdata.charAt(2)!=',')
					{
						throw new AMEFDecodeException("Invalid character after name specifier, expected ,");
					}
				}
				if(pos>=strdata.length())
				{
					throw new AMEFDecodeException("Reached end of AMEF string, not found ,");
				}
				aMEFObject.setName(name);
				String length = "";
				if(!ignoreName)pos++;
				while(length.length()<3)
				{
					length += strdata.charAt(pos++);
				}
				if(length.equals("") && strdata.charAt(3)!=',')
				{
					throw new AMEFDecodeException("Invalid character after length specifier, expected ,");
				}
				else if(pos>=strdata.length())
				{
					throw new AMEFDecodeException("Reached end of AMEF string, not found ,");
				}
				int lengthm = ((length.getBytes()[0] & 0xff) << 16) | ((length.getBytes()[1] & 0xff) << 8) 
								| ((length.getBytes()[2] & 0xff));
				aMEFObject.setLength(lengthm);
				String value = strdata.substring(pos,pos+lengthm);
				aMEFObject.setValue(value);
				tempVal = strdata.substring(pos+lengthm);
			}
			else
			{
				throw new AMEFDecodeException("Invalid character after type specifier, expected ,");
			}
		}
		else if(type==AMEFObject.BOOLEAN_TYPE || type==AMEFObject.CHAR_TYPE)
		{
			if(strdata.charAt(1)==',')
			{
				aMEFObject = new AMEFObject();
				aMEFObject.setType(type);
				int pos = 2;
				String name = "";
				if(!ignoreName)
				{
					while(strdata.charAt(pos)!=',')
					{
						name += strdata.charAt(pos++);
					}
					if(name.equals("") && strdata.charAt(2)!=',')
					{
						throw new AMEFDecodeException("Invalid character after name specifier, expected ,");
					}
				}
				if(pos>=strdata.length())
				{
					throw new AMEFDecodeException("Reached end of AMEF string, not found ,");
				}
				aMEFObject.setName(name);
				if(pos>=strdata.length())
				{
					throw new AMEFDecodeException("Reached end of AMEF string, not found ,");
				}
				aMEFObject.setLength(1);
				String value = "";
				if(!ignoreName)
				{
					value = strdata.substring(pos+1,pos+2);
					tempVal = strdata.substring(pos+2);
				}
				else
				{
					value = strdata.substring(pos,pos+1);
					tempVal = strdata.substring(pos+1);
				}
				aMEFObject.setValue(value);				
			}
			else
			{
				throw new AMEFDecodeException("Invalid character after type specifier, expected ,");
			}
		}
		else if(type==AMEFObject.STRING_256_TYPE || type==AMEFObject.DATE_TYPE || type==AMEFObject.DOUBLE_FLOAT_TYPE)
		{
			if(strdata.charAt(1)==',')
			{
				aMEFObject = new AMEFObject();
				aMEFObject.setType(type);
				int pos = 2;
				String name = "";
				if(!ignoreName)
				{
					while(strdata.charAt(pos)!=',')
					{
						name += strdata.charAt(pos++);
					}
					if(name.equals("") && strdata.charAt(2)!=',')
					{
						throw new AMEFDecodeException("Invalid character after name specifier, expected ,");
					}
				}
				if(pos>=strdata.length())
				{
					throw new AMEFDecodeException("Reached end of AMEF string, not found ,");
				}
				aMEFObject.setName(name);
				String length = "";
				if(!ignoreName)pos++;
				while(length.length()<1)
				{
					length += strdata.charAt(pos++);
				}	
				if(length.equals("") && strdata.charAt(3)!=',')
				{
					throw new AMEFDecodeException("Invalid character after length specifier, expected ,");
				}
				else if(pos>=strdata.length())
				{
					throw new AMEFDecodeException("Reached end of AMEF string, not found ,");
				}
				int lengthm = length.charAt(0) & 0xff;		
				aMEFObject.setLength(lengthm);
				String value = strdata.substring(pos,pos+lengthm);
				aMEFObject.setValue(value);
				tempVal = strdata.substring(pos+lengthm);
			}
			else
			{
				throw new AMEFDecodeException("Invalid character after type specifier, expected ,");
			}
		}
		else if(type==AMEFObject.VERY_SMALL_INT_TYPE)
		{
			aMEFObject = getObject(strdata, type, ignoreName, 1);
		}
		else if(type==AMEFObject.SMALL_INT_TYPE)
		{
			aMEFObject = getObject(strdata, type, ignoreName, 2);
		}
		else if(type==AMEFObject.BIG_INT_TYPE)
		{
			aMEFObject = getObject(strdata, type, ignoreName, 3);
		}
		else if(type==AMEFObject.INT_TYPE)
		{
			aMEFObject = getObject(strdata, type, ignoreName, 4);
		}
		else if(type==AMEFObject.VS_LONG_INT_TYPE)
		{
			aMEFObject = getObject(strdata, type, ignoreName, 5);
		}
		else if(type==AMEFObject.S_LONG_INT_TYPE)
		{
			aMEFObject = getObject(strdata, type, ignoreName, 6);
		}
		else if(type==AMEFObject.B_LONG_INT_TYPE)
		{
			aMEFObject = getObject(strdata, type, ignoreName, 7);
		}
		else if(type==AMEFObject.LONG_INT_TYPE)
		{
			aMEFObject = getObject(strdata, type, ignoreName, 8);
		}
		else if(type==AMEFObject.VS_OBJECT_TYPE)
		{
			if(strdata.charAt(1)==',')
			{
				aMEFObject = new AMEFObject();
				aMEFObject.setType(type);
				int pos = 2;
				String name = "";
				if(!ignoreName)
				{
					while(strdata.charAt(pos)!=',')
					{
						name += strdata.charAt(pos++);
					}
					if(name.equals("") && strdata.charAt(2)!=',')
					{
						throw new AMEFDecodeException("Invalid character after name specifier, expected ,");
					}
				}
				if(pos>=strdata.length())
				{
					throw new AMEFDecodeException("Reached end of AMEF string, not found ,");
				}
				aMEFObject.setName(name);
				String length = "";
				if(!ignoreName)pos++;
				while(length.length()<1)
				{
					length += strdata.charAt(pos++);
				}
				if(length.equals("") && strdata.charAt(3)!=',')
				{
					throw new AMEFDecodeException("Invalid character after length specifier, expected ,");
				}
				else if(pos>=strdata.length())
				{
					throw new AMEFDecodeException("Reached end of AMEF string, not found ,");
				}
				int lengthm = ((length.getBytes()[0]) & 0xff);
				aMEFObject.setLength(lengthm);
				//
				//String value = strdata.substring(pos+1,pos+lengthm+1);
				tempVal  = strdata.substring(pos,pos+lengthm);
				while(!tempVal.equals(""))
				{
					AMEFObject obj = decodeSinglePacket(tempVal,ignoreName);
					aMEFObject.addPacket(obj);
				}				
				tempVal = strdata.substring(pos+lengthm);
			}
			else
			{
				throw new AMEFDecodeException("Invalid character after type specifier, expected ,");
			}
		}
		else if(type==AMEFObject.S_OBJECT_TYPE)
		{
			if(strdata.charAt(1)==',')
			{
				aMEFObject = new AMEFObject();
				aMEFObject.setType(type);
				int pos = 2;
				String name = "";
				if(!ignoreName)
				{
					while(strdata.charAt(pos)!=',')
					{
						name += strdata.charAt(pos++);
					}
					if(name.equals("") && strdata.charAt(2)!=',')
					{
						throw new AMEFDecodeException("Invalid character after name specifier, expected ,");
					}
				}
				if(pos>=strdata.length())
				{
					throw new AMEFDecodeException("Reached end of AMEF string, not found ,");
				}
				aMEFObject.setName(name);
				String length = "";
				if(!ignoreName)pos++;
				while(length.length()<2)
				{
					length += strdata.charAt(pos++);
				}
				if(length.equals("") && strdata.charAt(3)!=',')
				{
					throw new AMEFDecodeException("Invalid character after length specifier, expected ,");
				}
				else if(pos>=strdata.length())
				{
					throw new AMEFDecodeException("Reached end of AMEF string, not found ,");
				}
				int lengthm = ((length.getBytes()[0] & 0xff) << 8) | ((length.getBytes()[1]) & 0xff);
				aMEFObject.setLength(lengthm);
				//String value = strdata.substring(pos+1,pos+lengthm+1);
				//tempVal = value;
				tempVal  = strdata.substring(pos,pos+lengthm);
				while(!tempVal.equals(""))
				{
					AMEFObject obj = decodeSinglePacket(tempVal,ignoreName);
					aMEFObject.addPacket(obj);
				}				
				tempVal = strdata.substring(pos+lengthm);
			}
			else
			{
				throw new AMEFDecodeException("Invalid character after type specifier, expected ,");
			}
		}
		else if(type==AMEFObject.B_OBJECT_TYPE)
		{
			if(strdata.charAt(1)==',')
			{
				aMEFObject = new AMEFObject();
				aMEFObject.setType(type);
				int pos = 2;
				String name = "";
				if(!ignoreName)
				{
					while(strdata.charAt(pos)!=',')
					{
						name += strdata.charAt(pos++);
					}
					if(name.equals("") && strdata.charAt(2)!=',')
					{
						throw new AMEFDecodeException("Invalid character after name specifier, expected ,");
					}
				}
				if(pos>=strdata.length())
				{
					throw new AMEFDecodeException("Reached end of AMEF string, not found ,");
				}
				aMEFObject.setName(name);
				String length = "";
				if(!ignoreName)pos++;
				while(length.length()<3)
				{
					length += strdata.charAt(pos++);
				}
				if(length.equals("") && strdata.charAt(3)!=',')
				{
					throw new AMEFDecodeException("Invalid character after length specifier, expected ,");
				}
				else if(pos>=strdata.length())
				{
					throw new AMEFDecodeException("Reached end of AMEF string, not found ,");
				}
				int lengthm = ((length.getBytes()[0] & 0xff) << 16)
								| ((length.getBytes()[1] & 0xff) << 8) | ((length.getBytes()[2]) & 0xff);
				aMEFObject.setLength(lengthm);
				//String value = strdata.substring(pos+1,pos+lengthm+1);
				//tempVal = value;
				tempVal  = strdata.substring(pos,pos+lengthm);
				while(!tempVal.equals(""))
				{
					AMEFObject obj = decodeSinglePacket(tempVal,ignoreName);
					aMEFObject.addPacket(obj);
				}				
				tempVal = strdata.substring(pos+lengthm);
			}
			else
			{
				throw new AMEFDecodeException("Invalid character after type specifier, expected ,");
			}
		}
		else if(type==AMEFObject.OBJECT_TYPE)
		{
			if(strdata.charAt(1)==',')
			{
				aMEFObject = new AMEFObject();
				aMEFObject.setType(type);
				int pos = 2;
				String name = "";
				if(!ignoreName)
				{
					while(strdata.charAt(pos)!=',')
					{
						name += strdata.charAt(pos++);
					}
					if(name.equals("") && strdata.charAt(2)!=',')
					{
						throw new AMEFDecodeException("Invalid character after name specifier, expected ,");
					}
				}
				if(pos>=strdata.length())
				{
					throw new AMEFDecodeException("Reached end of AMEF string, not found ,");
				}
				aMEFObject.setName(name);
				String length = "";
				if(!ignoreName)pos++;
				while(length.length()<4)
				{
					length += strdata.charAt(pos++);
				}
				if(length.equals("") && strdata.charAt(3)!=',')
				{
					throw new AMEFDecodeException("Invalid character after length specifier, expected ,");
				}
				else if(pos>=strdata.length())
				{
					throw new AMEFDecodeException("Reached end of AMEF string, not found ,");
				}
				int lengthm = ((length.getBytes()[0] & 0xff) << 24) | ((length.getBytes()[1] & 0xff) << 16)
								| ((length.getBytes()[2] & 0xff) << 8) | ((length.getBytes()[3]) & 0xff);
				aMEFObject.setLength(lengthm);
				//String value = strdata.substring(pos+1,pos+lengthm+1);
				//tempVal = value;
				tempVal  = strdata.substring(pos,pos+lengthm);
				while(!tempVal.equals(""))
				{
					AMEFObject obj = decodeSinglePacket(tempVal,ignoreName);
					aMEFObject.addPacket(obj);
				}				
				tempVal = strdata.substring(pos+lengthm);
			}
			else
			{
				throw new AMEFDecodeException("Invalid character after type specifier, expected ,");
			}
		}
		return aMEFObject;
	}
	
	private AMEFObject getObject(String strdata,char type,boolean ignoreName,int typ) throws AMEFDecodeException
	{
		AMEFObject aMEFObject = null;
		if(strdata.charAt(1)==',')
		{
			aMEFObject = new AMEFObject();
			aMEFObject.setType(type);
			int pos = 2;
			String name = "";
			if(!ignoreName)
			{
				while(strdata.charAt(pos)!=',')
				{
					name += strdata.charAt(pos++);
				}
				if(name.equals("") && strdata.charAt(2)!=',')
				{
					throw new AMEFDecodeException("Invalid character after name specifier, expected ,");
				}
			}
			if(pos>=strdata.length())
			{
				throw new AMEFDecodeException("Reached end of AMEF string, not found ,");
			}
			aMEFObject.setName(name);
			if(!ignoreName)pos++;
			String value = strdata.substring(pos,pos+typ);
			aMEFObject.setValue(value);
			tempVal = strdata.substring(pos+typ);
		}
		else
		{
			throw new AMEFDecodeException("Invalid character after type specifier, expected ,");
		}
		return aMEFObject;
	}*/
	
	//int position = 0;
	/*private AMEFObject decodeSinglePacketB(byte[] buffer,boolean ignoreName,Integer position) throws AMEFDecodeException
	{
		char type = (char)buffer[position];
		AMEFObject aMEFObject = null;
		int st, en;
		if(type==AMEFObject.NULL_STRING || type==AMEFObject.NULL_DATE || type==AMEFObject.NULL_NUMBER
				|| type==AMEFObject.NULL_BOOL || type==AMEFObject.NULL_CHAR)
		{			
			aMEFObject = new AMEFObject();
			aMEFObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				byte[] name = new byte[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				aMEFObject.setName(name);
			}
			else 
				position++;		
		}
		else if(type==AMEFObject.STRING_TYPE)
		{			
			aMEFObject = new AMEFObject();
			aMEFObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				byte[] name = new byte[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				aMEFObject.setName(name);
			}
			else 
				position++;
			int lengthm = AMEFResources.byteArrayToInt(buffer,position,4);
			aMEFObject.setLength(lengthm);
			position += 4;
			byte[] value = new byte[lengthm];
			System.arraycopy(buffer, position, value, 0, value.length);
			aMEFObject.setValue(value);
			position += 5+lengthm;			
		}
		else if(type==AMEFObject.STRING_65536_TYPE)
		{			
			aMEFObject = new AMEFObject();
			aMEFObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				byte[] name = new byte[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				aMEFObject.setName(name);
			}
			else 
				position++;
			int lengthm = AMEFResources.byteArrayToInt(buffer,position,2);
			aMEFObject.setLength(lengthm);
			position += 2;
			byte[] value = new byte[lengthm];
			System.arraycopy(buffer, position, value, 0, value.length);
			aMEFObject.setValue(value);
			position += 3+lengthm;			
		}
		else if(type==AMEFObject.STRING_16777216_TYPE)
		{			
			aMEFObject = new AMEFObject();
			aMEFObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				byte[] name = new byte[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				aMEFObject.setName(name);
			}
			else 
				position++;
			int lengthm = AMEFResources.byteArrayToInt(buffer,position,3);
			aMEFObject.setLength(lengthm);
			position += 3;
			byte[] value = new byte[lengthm];
			System.arraycopy(buffer, position, value, 0, value.length);
			aMEFObject.setValue(value);
			position += 4+lengthm;			
		}		
		else if(type==AMEFObject.DATE_TYPE || type==AMEFObject.STRING_256_TYPE || type==AMEFObject.DOUBLE_FLOAT_TYPE)
		{			
			aMEFObject = new AMEFObject();
			aMEFObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				byte[] name = new byte[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				aMEFObject.setName(name);
			}
			else 
				position++;
			int lengthm = AMEFResources.byteArrayToInt(buffer,position,1);			
			aMEFObject.setLength(lengthm);
			position++;
			byte[] value = new byte[lengthm];			
			System.arraycopy(buffer, position, value, 0, value.length);
			aMEFObject.setValue(value);
			position += lengthm;			
		}
		else if(type==AMEFObject.VERY_SMALL_INT_TYPE)
		{			
			aMEFObject = new AMEFObject();
			aMEFObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				byte[] name = new byte[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				aMEFObject.setName(name);
			}
			else 
				position++;
			aMEFObject.setLength(1);
			aMEFObject.setValue(new byte[]{buffer[position]});
			position += 1;	
		}
		else if(type==AMEFObject.SMALL_INT_TYPE)
		{			
			aMEFObject = new AMEFObject();
			aMEFObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				byte[] name = new byte[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				aMEFObject.setName(name);
			}
			else 
				position++;
			aMEFObject.setLength(2);
			aMEFObject.setValue(new byte[]{buffer[position],buffer[position+1]});
			position += 2;		
		}
		else if(type==AMEFObject.BIG_INT_TYPE)
		{			
			aMEFObject = new AMEFObject();
			aMEFObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				byte[] name = new byte[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				aMEFObject.setName(name);
			}
			else 
				position++;
			aMEFObject.setLength(3);
			aMEFObject.setValue(new byte[]{buffer[position],buffer[position+1],buffer[position+2]});
			position += 3;		
		}
		else if(type==AMEFObject.INT_TYPE)
		{			
			aMEFObject = new AMEFObject();
			aMEFObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				byte[] name = new byte[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				aMEFObject.setName(name);
			}
			else 
				position++;
			aMEFObject.setLength(4);
			aMEFObject.setValue(new byte[]{buffer[position],buffer[position+1],buffer[position+2],
					buffer[position+3]});
			position += 4;		
		}
		else if(type==AMEFObject.VS_LONG_INT_TYPE)
		{			
			aMEFObject = new AMEFObject();
			aMEFObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				byte[] name = new byte[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				aMEFObject.setName(name);
			}
			else 
				position++;
			aMEFObject.setLength(5);
			aMEFObject.setValue(new byte[]{buffer[position],buffer[position+1],buffer[position+2],
					buffer[position+3],buffer[position+4]});
			position += 5;		
		}
		else if(type==AMEFObject.S_LONG_INT_TYPE)
		{			
			aMEFObject = new AMEFObject();
			aMEFObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				byte[] name = new byte[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				aMEFObject.setName(name);
			}
			else 
				position++;
			aMEFObject.setLength(6);
			aMEFObject.setValue(new byte[]{buffer[position],buffer[position+1],buffer[position+2],
					buffer[position+3],buffer[position+4],buffer[position+5]});
			position += 6;		
		}
		else if(type==AMEFObject.B_LONG_INT_TYPE)
		{			
			aMEFObject = new AMEFObject();
			aMEFObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				byte[] name = new byte[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				aMEFObject.setName(name);
			}
			else 
				position++;
			aMEFObject.setLength(7);
			aMEFObject.setValue(new byte[]{buffer[position],buffer[position+1],buffer[position+2],
					buffer[position+3],buffer[position+4],buffer[position+5],buffer[position+6]});
			position += 7;		
		}
		else if(type==AMEFObject.LONG_INT_TYPE)
		{			
			aMEFObject = new AMEFObject();
			aMEFObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				byte[] name = new byte[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				aMEFObject.setName(name);
			}
			else 
				position++;
			aMEFObject.setLength(8);
			aMEFObject.setValue(new byte[]{buffer[position],buffer[position+1],
					buffer[position+2],buffer[position+3],buffer[position+4],
					buffer[position+5],buffer[position+6],buffer[position+7]});
			position += 8;		
		}
		else if(type==AMEFObject.BOOLEAN_TYPE || type==AMEFObject.CHAR_TYPE)
		{			
			aMEFObject = new AMEFObject();
			aMEFObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				byte[] name = new byte[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				aMEFObject.setName(name);
			}
			else 
				position++;
			aMEFObject.setLength(1);
			aMEFObject.setValue(new byte[]{buffer[position]});
			position += 1;		
		}
		else if(type==AMEFObject.VS_OBJECT_TYPE)
		{				
			aMEFObject = new AMEFObject();
			aMEFObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				byte[] name = new byte[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				aMEFObject.setName(name);
			}
			else 
				position++;
			int lengthm = AMEFResources.byteArrayToInt(buffer,position,1);
			aMEFObject.setLength(lengthm);
			position++;
			while(position<buffer.length)
			{
				AMEFObject obj = decodeSinglePacketB(buffer,ignoreName,position);
				position = obj.position;
				aMEFObject.addPacket(obj);
			}
		}
		else if(type==AMEFObject.S_OBJECT_TYPE)
		{				
			aMEFObject = new AMEFObject();
			aMEFObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				byte[] name = new byte[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				aMEFObject.setName(name);
			}
			else 
				position++;
			int lengthm = AMEFResources.byteArrayToInt(buffer,position,2);
			aMEFObject.setLength(lengthm);
			//byte[] value = new byte[lengthm];
			//System.arraycopy(buffer, 3, value, 0, lengthm);
			position += 2;
			while(position<buffer.length)
			{
				AMEFObject obj = decodeSinglePacketB(buffer,ignoreName,position);
				position = obj.position;
				aMEFObject.addPacket(obj);
			}
		}
		else if(type==AMEFObject.B_OBJECT_TYPE)
		{				
			aMEFObject = new AMEFObject();
			aMEFObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				byte[] name = new byte[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				aMEFObject.setName(name);
			}
			else 
				position++;
			int lengthm = AMEFResources.byteArrayToInt(buffer,position,3);
			aMEFObject.setLength(lengthm);
			byte[] value = new byte[lengthm];
			System.arraycopy(buffer, 4, value, 0, lengthm);
			position += 3;
			while(position<buffer.length)
			{
				AMEFObject obj = decodeSinglePacketB(buffer,ignoreName,position);
				position = obj.position;
				aMEFObject.addPacket(obj);
			}
		}
		else if(type==AMEFObject.OBJECT_TYPE)
		{				
			aMEFObject = new AMEFObject();
			aMEFObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				byte[] name = new byte[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				aMEFObject.setName(name);
			}
			else 
				position++;
			int lengthm = AMEFResources.byteArrayToInt(buffer,position,4);
			aMEFObject.setLength(lengthm);
			byte[] value = new byte[lengthm];
			System.arraycopy(buffer, 5, value, 0, lengthm);
			position += 4;
			while(position<buffer.length)
			{
				AMEFObject obj = decodeSinglePacketB(buffer,ignoreName,position);
				position = obj.position;
				aMEFObject.addPacket(obj);
			}
		}
		aMEFObject.position = position;
		return aMEFObject;
	}
	
	private AMEFObject decodeSinglePacketBIN(byte[] buffer,Integer position) throws AMEFDecodeException
	{
		char type = (char)buffer[position];
		AMEFObject aMEFObject = null;
		if(type==AMEFObject.NULL_STRING || type==AMEFObject.NULL_DATE || type==AMEFObject.NULL_NUMBER
				|| type==AMEFObject.NULL_BOOL || type==AMEFObject.NULL_CHAR)
		{			
			aMEFObject = new AMEFObject();
			aMEFObject.setLength(1);
			aMEFObject.setType(type);position++;		
		}
		else if(type==AMEFObject.STRING_TYPE)
		{			
			aMEFObject = new AMEFObject();
			aMEFObject.setType(type);
			position++;
			int lengthm = AMEFResources.byteArrayToInt(buffer,position,4);
			aMEFObject.setLength(lengthm);
			position += 4;
			byte[] value = new byte[lengthm];
			System.arraycopy(buffer, position, value, 0, value.length);
			aMEFObject.setValue(value);
			position += 5+lengthm;			
		}
		else if(type==AMEFObject.STRING_65536_TYPE)
		{			
			aMEFObject = new AMEFObject();
			aMEFObject.setType(type);
			position++;
			int lengthm = AMEFResources.byteArrayToInt(buffer,position,2);
			aMEFObject.setLength(lengthm);
			position += 2;
			byte[] value = new byte[lengthm];
			System.arraycopy(buffer, position, value, 0, value.length);
			aMEFObject.setValue(value);
			position += 3+lengthm;			
		}
		else if(type==AMEFObject.STRING_16777216_TYPE)
		{			
			aMEFObject = new AMEFObject();
			aMEFObject.setType(type);
			position++;
			int lengthm = AMEFResources.byteArrayToInt(buffer,position,3);
			aMEFObject.setLength(lengthm);
			position += 3;
			byte[] value = new byte[lengthm];
			System.arraycopy(buffer, position, value, 0, value.length);
			aMEFObject.setValue(value);
			position += 4+lengthm;			
		}		
		else if(type==AMEFObject.DATE_TYPE || type==AMEFObject.STRING_256_TYPE || type==AMEFObject.DOUBLE_FLOAT_TYPE)
		{			
			aMEFObject = new AMEFObject();
			aMEFObject.setType(type);
			position++;
			int lengthm = AMEFResources.byteArrayToInt(buffer,position,1);			
			aMEFObject.setLength(lengthm);
			position++;
			byte[] value = new byte[lengthm];			
			System.arraycopy(buffer, position, value, 0, value.length);
			aMEFObject.setValue(value);
			position += lengthm;			
		}
		else if(type==AMEFObject.VERY_SMALL_INT_TYPE)
		{			
			aMEFObject = new AMEFObject();
			aMEFObject.setType(type);
			position++;
			aMEFObject.setLength(1);
			aMEFObject.setValue(new byte[]{buffer[position]});
			position += 1;	
		}
		else if(type==AMEFObject.SMALL_INT_TYPE)
		{			
			aMEFObject = new AMEFObject();
			aMEFObject.setType(type);
			position++;
			aMEFObject.setLength(2);
			aMEFObject.setValue(new byte[]{buffer[position],buffer[position+1]});
			position += 2;		
		}
		else if(type==AMEFObject.BIG_INT_TYPE)
		{			
			aMEFObject = new AMEFObject();
			aMEFObject.setType(type);
			position++;
			aMEFObject.setLength(3);
			aMEFObject.setValue(new byte[]{buffer[position],buffer[position+1],buffer[position+2]});
			position += 3;		
		}
		else if(type==AMEFObject.INT_TYPE)
		{			
			aMEFObject = new AMEFObject();
			aMEFObject.setType(type);
			position++;
			aMEFObject.setLength(4);
			aMEFObject.setValue(new byte[]{buffer[position],buffer[position+1],buffer[position+2],
					buffer[position+3]});
			position += 4;		
		}
		else if(type==AMEFObject.VS_LONG_INT_TYPE)
		{			
			aMEFObject = new AMEFObject();
			aMEFObject.setType(type);
			position++;
			aMEFObject.setLength(5);
			aMEFObject.setValue(new byte[]{buffer[position],buffer[position+1],buffer[position+2],
					buffer[position+3],buffer[position+4]});
			position += 5;		
		}
		else if(type==AMEFObject.S_LONG_INT_TYPE)
		{			
			aMEFObject = new AMEFObject();
			aMEFObject.setType(type);
			position++;
			aMEFObject.setLength(6);
			aMEFObject.setValue(new byte[]{buffer[position],buffer[position+1],buffer[position+2],
					buffer[position+3],buffer[position+4],buffer[position+5]});
			position += 6;		
		}
		else if(type==AMEFObject.B_LONG_INT_TYPE)
		{			
			aMEFObject = new AMEFObject();
			aMEFObject.setType(type);
			position++;
			aMEFObject.setLength(7);
			aMEFObject.setValue(new byte[]{buffer[position],buffer[position+1],buffer[position+2],
					buffer[position+3],buffer[position+4],buffer[position+5],buffer[position+6]});
			position += 7;		
		}
		else if(type==AMEFObject.LONG_INT_TYPE)
		{			
			aMEFObject = new AMEFObject();
			aMEFObject.setType(type);
			position++;
			aMEFObject.setLength(8);
			aMEFObject.setValue(new byte[]{buffer[position],buffer[position+1],
					buffer[position+2],buffer[position+3],buffer[position+4],
					buffer[position+5],buffer[position+6],buffer[position+7]});
			position += 8;		
		}
		else if(type==AMEFObject.BOOLEAN_TYPE || type==AMEFObject.CHAR_TYPE)
		{			
			aMEFObject = new AMEFObject();
			aMEFObject.setType(type);
			position++;
			aMEFObject.setLength(1);
			aMEFObject.setValue(new byte[]{buffer[position]});
			position += 1;		
		}
		else if(type==AMEFObject.VS_OBJECT_TYPE)
		{				
			aMEFObject = new AMEFObject();
			aMEFObject.setType(type);
			position++;
			int lengthm = AMEFResources.byteArrayToInt(buffer,position,1);
			aMEFObject.setLength(lengthm);
			position++;
			while(position<buffer.length)
			{
				AMEFObject obj = decodeSinglePacketBIN(buffer,position);
				position = obj.position;
				aMEFObject.addPacket(obj);
			}
		}
		else if(type==AMEFObject.S_OBJECT_TYPE)
		{				
			aMEFObject = new AMEFObject();
			aMEFObject.setType(type);
			position++;
			int lengthm = AMEFResources.byteArrayToInt(buffer,position,2);
			aMEFObject.setLength(lengthm);
			//byte[] value = new byte[lengthm];
			//System.arraycopy(buffer, 3, value, 0, lengthm);
			position += 2;
			while(position<buffer.length)
			{
				AMEFObject obj = decodeSinglePacketBIN(buffer,position);
				position = obj.position;
				aMEFObject.addPacket(obj);
			}
		}
		else if(type==AMEFObject.B_OBJECT_TYPE)
		{				
			aMEFObject = new AMEFObject();
			aMEFObject.setType(type);
			position++;
			int lengthm = AMEFResources.byteArrayToInt(buffer,position,3);
			aMEFObject.setLength(lengthm);
			//byte[] value = new byte[lengthm];
			//System.arraycopy(buffer, 4, value, 0, lengthm);
			position += 3;
			while(position<buffer.length)
			{
				AMEFObject obj = decodeSinglePacketBIN(buffer,position);
				position = obj.position;
				aMEFObject.addPacket(obj);
			}
		}
		else if(type==AMEFObject.OBJECT_TYPE)
		{				
			aMEFObject = new AMEFObject();
			aMEFObject.setType(type);
			position++;
			int lengthm = AMEFResources.byteArrayToInt(buffer,position,4);
			aMEFObject.setLength(lengthm);
			//byte[] value = new byte[lengthm];
			//System.arraycopy(buffer, 5, value, 0, lengthm);
			position += 4;
			while(position<buffer.length)
			{
				AMEFObject obj = decodeSinglePacketBIN(buffer,position);
				position = obj.position;
				aMEFObject.addPacket(obj);
			}
		}
		aMEFObject.position = position;
		return aMEFObject;
	}
	*/
	private void addPrimitive(AMEFObject aMEFObject,byte[] buffer,Integer position,boolean ignoreName)
	{
		char type = (char)buffer[position];
		if(type==AMEFObject.NULL_STRING || type==AMEFObject.NULL_DATE || type==AMEFObject.NULL_NUMBER
				|| type==AMEFObject.NULL_BOOL || type==AMEFObject.NULL_CHAR)
		{			
			
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				int st = position;
				while(buffer[position++]!=44){}
				int en = position - 1;
				byte[] name = new byte[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				aMEFObject.addNullPacket(type,new String(name));
			}
			else
			{
				position++;
				aMEFObject.addNullPacket(type);
			}
		}
		else if(type==AMEFObject.STRING_TYPE)
		{	
			String names = null;
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				int st = position;
				while(buffer[position++]!=44){}
				int en = position - 1;
				byte[] name = new byte[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				names = new String(name);
			}
			else
				position++;
			int lengthm = AMEFResources.byteArrayToInt(buffer,position,4);
			position += 4;
			byte[] value = new byte[lengthm];
			System.arraycopy(buffer, position, value, 0, value.length);
			if(names==null)aMEFObject.addPacket(value);else aMEFObject.addPacket(value,names);
			position += 5+lengthm;			
		}
		else if(type==AMEFObject.STRING_65536_TYPE)
		{		
			String names = null;
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				int st = position;
				while(buffer[position++]!=44){}
				int en = position - 1;
				byte[] name = new byte[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				names = new String(name);
			}
			else
				position++;
			int lengthm = AMEFResources.byteArrayToInt(buffer,position,2);
			position += 2;
			byte[] value = new byte[lengthm];
			System.arraycopy(buffer, position, value, 0, value.length);
			if(names==null)aMEFObject.addPacket(value);else aMEFObject.addPacket(value,names);
			position += 3+lengthm;			
		}
		else if(type==AMEFObject.STRING_16777216_TYPE)
		{		
			String names = null;
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				int st = position;
				while(buffer[position++]!=44){}
				int en = position - 1;
				byte[] name = new byte[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				names = new String(name);
			}
			else
				position++;
			int lengthm = AMEFResources.byteArrayToInt(buffer,position,3);
			position += 3;
			byte[] value = new byte[lengthm];
			System.arraycopy(buffer, position, value, 0, value.length);
			if(names==null)aMEFObject.addPacket(value);else aMEFObject.addPacket(value,names);
			position += 4+lengthm;			
		}		
		else if(type==AMEFObject.DATE_TYPE || type==AMEFObject.STRING_256_TYPE || type==AMEFObject.DOUBLE_FLOAT_TYPE)
		{	
			String names = null;
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				int st = position;
				while(buffer[position++]!=44){}
				int en = position - 1;
				byte[] name = new byte[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				names = new String(name);
			}
			else
				position++;
			int lengthm = AMEFResources.byteArrayToInt(buffer,position,1);			
			//aMEFObject.setLength(lengthm);
			position++;
			byte[] value = new byte[lengthm];			
			System.arraycopy(buffer, position, value, 0, value.length);
			AMEFObject temp = null;
			if(names==null)temp = aMEFObject.addPacket(value);
			else{ aMEFObject.addPacket(value,names);temp = aMEFObject.getPackets().get(aMEFObject.getPackets().size()-1);}
			if(temp!=null)
				temp.setType(type);
			position += lengthm;			
		}
		else if(type==AMEFObject.VERY_SMALL_INT_TYPE)
		{	
			String names = null;
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				int st = position;
				while(buffer[position++]!=44){}
				int en = position - 1;
				byte[] name = new byte[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				names = new String(name);
			}
			else
				position++;
			//aMEFObject.setLength(1);
			long value = AMEFResources.byteArrayToLong(new byte[]{buffer[position]});
			if(names==null)aMEFObject.addPacket(value);else aMEFObject.addPacket(value,names);
			position += 1;	
		}
		else if(type==AMEFObject.SMALL_INT_TYPE)
		{	
			String names = null;
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				int st = position;
				while(buffer[position++]!=44){}
				int en = position - 1;
				byte[] name = new byte[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				names = new String(name);
			}
			else
				position++;
			//aMEFObject.setLength(2);
			long value = AMEFResources.byteArrayToLong(new byte[]{buffer[position],buffer[position+1]});
			if(names==null)aMEFObject.addPacket(value);else aMEFObject.addPacket(value,names);
			position += 2;		
		}
		else if(type==AMEFObject.BIG_INT_TYPE)
		{	
			String names = null;
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				int st = position;
				while(buffer[position++]!=44){}
				int en = position - 1;
				byte[] name = new byte[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				names = new String(name);
			}
			else
				position++;
			//aMEFObject.setLength(3);
			long value = AMEFResources.byteArrayToLong(new byte[]{buffer[position],buffer[position+1],buffer[position+2]});
			if(names==null)aMEFObject.addPacket(value);else aMEFObject.addPacket(value,names);
			position += 3;		
		}
		else if(type==AMEFObject.INT_TYPE)
		{	
			String names = null;
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				int st = position;
				while(buffer[position++]!=44){}
				int en = position - 1;
				byte[] name = new byte[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				names = new String(name);
			}
			else
				position++;
			//aMEFObject.setLength(4);
			long value = AMEFResources.byteArrayToLong(new byte[]{buffer[position],buffer[position+1],buffer[position+2],
					buffer[position+3]});
			if(names==null)aMEFObject.addPacket(value);else aMEFObject.addPacket(value,names);
			position += 4;		
		}
		else if(type==AMEFObject.VS_LONG_INT_TYPE)
		{	
			String names = null;
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				int st = position;
				while(buffer[position++]!=44){}
				int en = position - 1;
				byte[] name = new byte[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				names = new String(name);
			}
			else
				position++;
			//aMEFObject.setLength(5);
			long value = AMEFResources.byteArrayToLong(new byte[]{buffer[position],buffer[position+1],buffer[position+2],
					buffer[position+3],buffer[position+4]});
			if(names==null)aMEFObject.addPacket(value);else aMEFObject.addPacket(value,names);
			position += 5;		
		}
		else if(type==AMEFObject.S_LONG_INT_TYPE)
		{	
			String names = null;
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				int st = position;
				while(buffer[position++]!=44){}
				int en = position - 1;
				byte[] name = new byte[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				names = new String(name);
			}
			else
				position++;
			//aMEFObject.setLength(6);
			long value = AMEFResources.byteArrayToLong(new byte[]{buffer[position],buffer[position+1],buffer[position+2],
					buffer[position+3],buffer[position+4],buffer[position+5]});
			if(names==null)aMEFObject.addPacket(value);else aMEFObject.addPacket(value,names);
			position += 6;		
		}
		else if(type==AMEFObject.B_LONG_INT_TYPE)
		{	
			String names = null;
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				int st = position;
				while(buffer[position++]!=44){}
				int en = position - 1;
				byte[] name = new byte[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				names = new String(name);
			}
			else
				position++;
			//aMEFObject.setLength(7);
			long value = AMEFResources.byteArrayToLong(new byte[]{buffer[position],buffer[position+1],buffer[position+2],
					buffer[position+3],buffer[position+4],buffer[position+5],buffer[position+6]});
			if(names==null)aMEFObject.addPacket(value);else aMEFObject.addPacket(value,names);
			position += 7;		
		}
		else if(type==AMEFObject.LONG_INT_TYPE)
		{	
			String names = null;
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				int st = position;
				while(buffer[position++]!=44){}
				int en = position - 1;
				byte[] name = new byte[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				names = new String(name);
			}
			else
				position++;
			//aMEFObject.setLength(8);
			long value = AMEFResources.byteArrayToLong(new byte[]{buffer[position],buffer[position+1],
					buffer[position+2],buffer[position+3],buffer[position+4],
					buffer[position+5],buffer[position+6],buffer[position+7]});
			if(names==null)aMEFObject.addPacket(value);else aMEFObject.addPacket(value,names);
			position += 8;		
		}
		else if(type==AMEFObject.BOOLEAN_TYPE || type==AMEFObject.CHAR_TYPE)
		{	
			String names = null;
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				int st = position;
				while(buffer[position++]!=44){}
				int en = position - 1;
				byte[] name = new byte[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				names = new String(name);
			}
			else
				position++;
			if(type==AMEFObject.BOOLEAN_TYPE)
			{	
				if(names==null)aMEFObject.addPacket(buffer[position]=='1'?true:false);
				else aMEFObject.addPacket(buffer[position]=='1'?true:false);
			}
			else
			{
				if(names==null)aMEFObject.addPacket((char)buffer[position]);
				else
					aMEFObject.addPacket((char)buffer[position],names);
			}
			position += 1;		
		}
		aMEFObject.position = position;
	}
	
	private AMEFObject decodeSinglePacketBINNew(byte[] buffer,Integer position,boolean ignoreName) throws AMEFDecodeException
	{
		char type = (char)buffer[position];
		AMEFObject aMEFObject = null;
		if(type==AMEFObject.VS_OBJECT_TYPE)
		{	
			String names = null;
			aMEFObject = new AMEFObject();
			//aMEFObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				int st = position;
				while(buffer[position++]!=44){}
				int en = position - 1;
				byte[] name = new byte[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				names = new String(name);
			}
			else
				position++;
			//int lengthm = AMEFResources.byteArrayToInt(buffer,position,1);
			//aMEFObject.setLength(lengthm);
			position++;
			while(position<buffer.length)
			{
				addPrimitive(aMEFObject, buffer, position,ignoreName);
				position = aMEFObject.position;
			}
			if(names!=null)aMEFObject.setName(names);
		}
		else if(type==AMEFObject.S_OBJECT_TYPE)
		{	
			String names = null;
			aMEFObject = new AMEFObject();
			//aMEFObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				int st = position;
				while(buffer[position++]!=44){}
				int en = position - 1;
				byte[] name = new byte[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				names = new String(name);
			}
			else
				position++;
			//int lengthm = AMEFResources.byteArrayToInt(buffer,position,2);
			//aMEFObject.setLength(lengthm);
			//byte[] value = new byte[lengthm];
			//System.arraycopy(buffer, 3, value, 0, lengthm);
			position += 2;
			while(position<buffer.length)
			{
				addPrimitive(aMEFObject, buffer, position,ignoreName);
				position = aMEFObject.position;
			}
			if(names!=null)aMEFObject.setName(names);
		}
		else if(type==AMEFObject.B_OBJECT_TYPE)
		{	
			String names = null;
			aMEFObject = new AMEFObject();
			//aMEFObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				int st = position;
				while(buffer[position++]!=44){}
				int en = position - 1;
				byte[] name = new byte[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				names = new String(name);
			}
			else
				position++;
			//int lengthm = AMEFResources.byteArrayToInt(buffer,position,3);
			//aMEFObject.setLength(lengthm);
			//byte[] value = new byte[lengthm];
			//System.arraycopy(buffer, 4, value, 0, lengthm);
			position += 3;
			while(position<buffer.length)
			{
				addPrimitive(aMEFObject, buffer, position,ignoreName);
				position = aMEFObject.position;
			}
			if(names!=null)aMEFObject.setName(names);
		}
		else if(type==AMEFObject.OBJECT_TYPE)
		{	
			String names = null;
			aMEFObject = new AMEFObject();
			//aMEFObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				int st = position;
				while(buffer[position++]!=44){}
				int en = position - 1;
				byte[] name = new byte[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				names = new String(name);
			}
			else
				position++;
			//int lengthm = AMEFResources.byteArrayToInt(buffer,position,4);
			//aMEFObject.setLength(lengthm);
			//byte[] value = new byte[lengthm];
			//System.arraycopy(buffer, 5, value, 0, lengthm);
			position += 4;
			while(position<buffer.length)
			{
				addPrimitive(aMEFObject, buffer, position,ignoreName);
				position = aMEFObject.position;
			}
			if(names!=null)aMEFObject.setName(names);
		}
		aMEFObject.position = position;
		return aMEFObject;
	}
	
}
