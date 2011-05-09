package com.amefc;
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
	/*public AMEFObject decode(char[] data,boolean considerLength,boolean ignoreName) throws AMEFDecodeException
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
	
	public AMEFObject decodeB(String buffer,boolean considerLength,boolean ignoreName) throws AMEFDecodeException
	{
		position = 0;
		char[] strdata = null;
		if(considerLength)
		{
			strdata = buffer.substring(4).toCharArray();
		}
		else
		{
			strdata = buffer.toCharArray();
		}
		AMEFObject AMEFObject = ignoreName?decodeSinglePacketBIN(strdata):decodeSinglePacketB(strdata,ignoreName);
		return AMEFObject;
	}
	
	
	
	/*public AMEFObject decodeJDBString(char[] data,boolean considerLength) throws AMEFDecodeException
	{
		int startWith = 0;		
		if(considerLength)
			startWith = 4;
		String strdata = new String(data);
		strdata = strdata.substring(startWith);
		AMEFObject AMEFObject = decodeSingleJDBStringPacket(strdata);
		return AMEFObject;
	}
	
	private AMEFObject decodeSingleJDBStringPacket(String strdata) throws AMEFDecodeException
	{
		AMEFObject AMEFObject = null;
		char type = strdata.charAt(0);
		if(type==AMEFObject.STRING_TYPE)
		{			
			AMEFObject = new AMEFObject();
			AMEFObject.setType(type);
			int lengthm = ((strdata.charAt(1) & 0xff) << 24) | ((strdata.charAt(2) & 0xff) << 16)
							| ((strdata.charAt(3) & 0xff) << 8) | ((strdata.charAt(4) & 0xff));
			AMEFObject.setLength(lengthm);
			String value = strdata.substring(5,5+lengthm);
			AMEFObject.setValue(value);
			tempVal = strdata.substring(5+lengthm);			
		}
		else if(type==AMEFObject.STRING_65536_TYPE)
		{			
			AMEFObject = new AMEFObject();
			AMEFObject.setType(type);
			int lengthm = ((strdata.charAt(1) & 0xff) << 8) | (strdata.charAt(2) & 0xff);
			AMEFObject.setLength(lengthm);
			String value = strdata.substring(3,3+lengthm);
			AMEFObject.setValue(value);
			tempVal = strdata.substring(3+lengthm);			
		}
		else if(type==AMEFObject.STRING_16777216_TYPE)
		{			
			AMEFObject = new AMEFObject();
			AMEFObject.setType(type);
			int lengthm = ((strdata.charAt(1) & 0xff) << 16) 
							| ((strdata.charAt(2) & 0xff) << 8) | (strdata.charAt(3) & 0xff);
			AMEFObject.setLength(lengthm);
			String value = strdata.substring(3,3+lengthm);
			AMEFObject.setValue(value);
			tempVal = strdata.substring(3+lengthm);			
		}		
		else if(type==AMEFObject.DATE_TYPE || type==AMEFObject.STRING_256_TYPE || type==AMEFObject.DOUBLE_FLOAT_TYPE)
		{			
			AMEFObject = new AMEFObject();
			AMEFObject.setType(type);
			int lengthm = strdata.charAt(1) & 0xff;
			AMEFObject.setLength(lengthm);
			String value = strdata.substring(2,2+lengthm);
			AMEFObject.setValue(value);
			tempVal = strdata.substring(2+lengthm);			
		}
		else if(type==AMEFObject.VERY_SMALL_INT_TYPE)
		{			
			AMEFObject = new AMEFObject();
			AMEFObject.setType(type);
			String value = (strdata.charAt(1) & 0xff) + "";
			AMEFObject.setValue(value);
			tempVal = strdata.substring(2);			
		}
		else if(type==AMEFObject.SMALL_INT_TYPE)
		{			
			AMEFObject = new AMEFObject();
			AMEFObject.setType(type);
			int lengthm = ((strdata.charAt(1) & 0xff) << 8) | ((strdata.charAt(2) & 0xff));
			String value = lengthm + "";
			AMEFObject.setValue(value);
			tempVal = strdata.substring(3);			
		}
		else if(type==AMEFObject.BIG_INT_TYPE)
		{			
			AMEFObject = new AMEFObject();
			AMEFObject.setType(type);
			int lengthm = ((strdata.charAt(1) & 0xff) << 16) | ((strdata.charAt(2) & 0xff) << 8) 
							| ((strdata.charAt(3) & 0xff));
			String value = lengthm + "";
			AMEFObject.setValue(value);
			tempVal = strdata.substring(4);			
		}
		else if(type==AMEFObject.INT_TYPE)
		{			
			AMEFObject = new AMEFObject();
			AMEFObject.setType(type);
			int lengthm = ((strdata.charAt(1) & 0xff) << 24) | ((strdata.charAt(2) & 0xff) << 16)
							| ((strdata.charAt(3) & 0xff) << 8) | ((strdata.charAt(4) & 0xff));
			String value = lengthm + "";
			AMEFObject.setValue(value);
			tempVal = strdata.substring(5);			
		}
		else if(type==AMEFObject.VS_LONG_INT_TYPE)
		{			
			AMEFObject = new AMEFObject();
			AMEFObject.setType(type);
			long lengthm = ((strdata.charAt(1) & 0xff) << 32) | ((strdata.charAt(2) & 0xff) << 24) 
							| ((strdata.charAt(3) & 0xff) << 16) | ((strdata.charAt(4) & 0xff) << 8) 
							| ((strdata.charAt(5) & 0xff));
			String value = lengthm + "";
			AMEFObject.setValue(value);
			tempVal = strdata.substring(6);			
		}
		else if(type==AMEFObject.S_LONG_INT_TYPE)
		{			
			AMEFObject = new AMEFObject();
			AMEFObject.setType(type);
			long lengthm = ((strdata.charAt(1) & 0xff) << 40) | ((strdata.charAt(2) & 0xff) << 32) 
							| ((strdata.charAt(3) & 0xff) << 24) 
							| ((strdata.charAt(4) & 0xff) << 16) | ((strdata.charAt(5) & 0xff) << 8) 
							| ((strdata.charAt(6) & 0xff));
			String value = lengthm + "";
			AMEFObject.setValue(value);
			tempVal = strdata.substring(7);			
		}
		else if(type==AMEFObject.B_LONG_INT_TYPE)
		{			
			AMEFObject = new AMEFObject();
			AMEFObject.setType(type);
			long lengthm = ((strdata.charAt(1) & 0xff) << 48) | ((strdata.charAt(2) & 0xff) << 40) 
							| ((strdata.charAt(3) & 0xff) << 32) 
							| ((strdata.charAt(4) & 0xff) << 24) 
							| ((strdata.charAt(5) & 0xff) << 16) | ((strdata.charAt(6) & 0xff) << 8) 
							| ((strdata.charAt(7) & 0xff));
			String value = lengthm + "";
			AMEFObject.setValue(value);
			tempVal = strdata.substring(8);			
		}
		else if(type==AMEFObject.LONG_INT_TYPE)
		{			
			AMEFObject = new AMEFObject();
			AMEFObject.setType(type);
			long lengthm = ((strdata.charAt(1) & 0xff) << 56) | ((strdata.charAt(2) & 0xff) << 48) 
							| ((strdata.charAt(3) & 0xff) << 40) 
							| ((strdata.charAt(4) & 0xff) << 32) 
							| ((strdata.charAt(5) & 0xff) << 24) 
							| ((strdata.charAt(6) & 0xff) << 16) | ((strdata.charAt(7) & 0xff) << 8) 
							| ((strdata.charAt(8) & 0xff));
			String value = lengthm + "";
			AMEFObject.setValue(value);
			tempVal = strdata.substring(9);			
		}
		else if(type==AMEFObject.BOOLEAN_TYPE || type==AMEFObject.CHAR_TYPE)
		{
			AMEFObject = new AMEFObject();
			AMEFObject.setType(type);
			AMEFObject.setLength(1);
			String value = strdata.charAt(1)+"";
			AMEFObject.setValue(value);
			tempVal = strdata.substring(2);	
		}
		else if(type==AMEFObject.VS_OBJECT_TYPE)
		{			
			AMEFObject = new AMEFObject();
			AMEFObject.setType(type);
			int lengthm = AMEFResources.byteArrayToInt(strdata.substring(1,2).getBytes());
			AMEFObject.setLength(lengthm);
			tempVal = strdata.substring(2,2+lengthm);
			while(!tempVal.equals(""))
			{
				AMEFObject obj = decodeSingleJDBStringPacket(tempVal);
				AMEFObject.addPacket(obj);
			}				
			tempVal = strdata.substring(2+lengthm);
		}
		else if(type==AMEFObject.S_OBJECT_TYPE)
		{			
			AMEFObject = new AMEFObject();
			AMEFObject.setType(type);
			int lengthm = ((strdata.charAt(1) & 0xff) << 8) | ((strdata.charAt(2) & 0xff));
			AMEFObject.setLength(lengthm);
			tempVal = strdata.substring(3,3+lengthm);
			while(!tempVal.equals(""))
			{
				AMEFObject obj = decodeSingleJDBStringPacket(tempVal);
				AMEFObject.addPacket(obj);
			}				
			tempVal = strdata.substring(3+lengthm);
		}
		else if(type==AMEFObject.B_OBJECT_TYPE)
		{			
			AMEFObject = new AMEFObject();
			AMEFObject.setType(type);
			int lengthm = ((strdata.charAt(1) & 0xff) << 16)
							| ((strdata.charAt(2) & 0xff) << 8) | ((strdata.charAt(3) & 0xff));
			AMEFObject.setLength(lengthm);
			tempVal = strdata.substring(4,4+lengthm);
			while(!tempVal.equals(""))
			{
				AMEFObject obj = decodeSingleJDBStringPacket(tempVal);
				AMEFObject.addPacket(obj);
			}				
			tempVal = strdata.substring(4+lengthm);
		}
		else if(type==AMEFObject.OBJECT_TYPE)
		{			
			AMEFObject = new AMEFObject();
			AMEFObject.setType(type);
			int lengthm = ((strdata.charAt(1) & 0xff) << 24) | ((strdata.charAt(2) & 0xff) << 16)
							| ((strdata.charAt(3) & 0xff) << 8) | ((strdata.charAt(4) & 0xff));
			AMEFObject.setLength(lengthm);
			tempVal = strdata.substring(5,5+lengthm);
			while(!tempVal.equals(""))
			{
				AMEFObject obj = decodeSingleJDBStringPacket(tempVal);
				AMEFObject.addPacket(obj);
			}				
			tempVal = strdata.substring(5+lengthm);
		}
		return AMEFObject;
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
		AMEFObject AMEFObject = null;
		char type = strdata.charAt(0);
		if(type==AMEFObject.STRING_TYPE)
		{
			if(strdata.charAt(1)==',')
			{
				AMEFObject = new AMEFObject();
				AMEFObject.setType(type);
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
				AMEFObject.setName(name);
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
				AMEFObject.setLength(lengthm);
				String value = strdata.substring(pos+1,pos+lengthm+1);
				AMEFObject.setValue(value);
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
				AMEFObject = new AMEFObject();
				AMEFObject.setType(type);
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
				AMEFObject.setName(name);
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
				AMEFObject.setLength(lengthm);
				String value = strdata.substring(pos,pos+lengthm);
				AMEFObject.setValue(value);
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
				AMEFObject = new AMEFObject();
				AMEFObject.setType(type);
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
				AMEFObject.setName(name);
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
				AMEFObject.setLength(lengthm);
				String value = strdata.substring(pos,pos+lengthm);
				AMEFObject.setValue(value);
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
				AMEFObject = new AMEFObject();
				AMEFObject.setType(type);
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
				AMEFObject.setName(name);
				if(pos>=strdata.length())
				{
					throw new AMEFDecodeException("Reached end of AMEF string, not found ,");
				}
				AMEFObject.setLength(1);
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
				AMEFObject.setValue(value);				
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
				AMEFObject = new AMEFObject();
				AMEFObject.setType(type);
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
				AMEFObject.setName(name);
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
				AMEFObject.setLength(lengthm);
				String value = strdata.substring(pos,pos+lengthm);
				AMEFObject.setValue(value);
				tempVal = strdata.substring(pos+lengthm);
			}
			else
			{
				throw new AMEFDecodeException("Invalid character after type specifier, expected ,");
			}
		}
		else if(type==AMEFObject.VERY_SMALL_INT_TYPE)
		{
			AMEFObject = getObject(strdata, type, ignoreName, 1);
		}
		else if(type==AMEFObject.SMALL_INT_TYPE)
		{
			AMEFObject = getObject(strdata, type, ignoreName, 2);
		}
		else if(type==AMEFObject.BIG_INT_TYPE)
		{
			AMEFObject = getObject(strdata, type, ignoreName, 3);
		}
		else if(type==AMEFObject.INT_TYPE)
		{
			AMEFObject = getObject(strdata, type, ignoreName, 4);
		}
		else if(type==AMEFObject.VS_LONG_INT_TYPE)
		{
			AMEFObject = getObject(strdata, type, ignoreName, 5);
		}
		else if(type==AMEFObject.S_LONG_INT_TYPE)
		{
			AMEFObject = getObject(strdata, type, ignoreName, 6);
		}
		else if(type==AMEFObject.B_LONG_INT_TYPE)
		{
			AMEFObject = getObject(strdata, type, ignoreName, 7);
		}
		else if(type==AMEFObject.LONG_INT_TYPE)
		{
			AMEFObject = getObject(strdata, type, ignoreName, 8);
		}
		else if(type==AMEFObject.VS_OBJECT_TYPE)
		{
			if(strdata.charAt(1)==',')
			{
				AMEFObject = new AMEFObject();
				AMEFObject.setType(type);
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
				AMEFObject.setName(name);
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
				AMEFObject.setLength(lengthm);
				//
				//String value = strdata.substring(pos+1,pos+lengthm+1);
				tempVal  = strdata.substring(pos,pos+lengthm);
				while(!tempVal.equals(""))
				{
					AMEFObject obj = decodeSinglePacket(tempVal,ignoreName);
					AMEFObject.addPacket(obj);
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
				AMEFObject = new AMEFObject();
				AMEFObject.setType(type);
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
				AMEFObject.setName(name);
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
				AMEFObject.setLength(lengthm);
				//String value = strdata.substring(pos+1,pos+lengthm+1);
				//tempVal = value;
				tempVal  = strdata.substring(pos,pos+lengthm);
				while(!tempVal.equals(""))
				{
					AMEFObject obj = decodeSinglePacket(tempVal,ignoreName);
					AMEFObject.addPacket(obj);
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
				AMEFObject = new AMEFObject();
				AMEFObject.setType(type);
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
				AMEFObject.setName(name);
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
				AMEFObject.setLength(lengthm);
				//String value = strdata.substring(pos+1,pos+lengthm+1);
				//tempVal = value;
				tempVal  = strdata.substring(pos,pos+lengthm);
				while(!tempVal.equals(""))
				{
					AMEFObject obj = decodeSinglePacket(tempVal,ignoreName);
					AMEFObject.addPacket(obj);
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
				AMEFObject = new AMEFObject();
				AMEFObject.setType(type);
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
				AMEFObject.setName(name);
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
				AMEFObject.setLength(lengthm);
				//String value = strdata.substring(pos+1,pos+lengthm+1);
				//tempVal = value;
				tempVal  = strdata.substring(pos,pos+lengthm);
				while(!tempVal.equals(""))
				{
					AMEFObject obj = decodeSinglePacket(tempVal,ignoreName);
					AMEFObject.addPacket(obj);
				}				
				tempVal = strdata.substring(pos+lengthm);
			}
			else
			{
				throw new AMEFDecodeException("Invalid character after type specifier, expected ,");
			}
		}
		return AMEFObject;
	}
	
	private AMEFObject getObject(String strdata,char type,boolean ignoreName,int typ) throws AMEFDecodeException
	{
		AMEFObject AMEFObject = null;
		if(strdata.charAt(1)==',')
		{
			AMEFObject = new AMEFObject();
			AMEFObject.setType(type);
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
			AMEFObject.setName(name);
			if(!ignoreName)pos++;
			String value = strdata.substring(pos,pos+typ);
			AMEFObject.setValue(value);
			tempVal = strdata.substring(pos+typ);
		}
		else
		{
			throw new AMEFDecodeException("Invalid character after type specifier, expected ,");
		}
		return AMEFObject;
	}*/
	
	int position = 0;
	private AMEFObject decodeSinglePacketB(char[] buffer,boolean ignoreName) throws AMEFDecodeException
	{
		char type = (char)buffer[position];
		AMEFObject AMEFObject = null;
		int st, en;
		if(type==com.amef.AMEFObject.NULL_STRING || type==com.amef.AMEFObject.NULL_DATE || type==com.amef.AMEFObject.NULL_NUMBER
				|| type==com.amef.AMEFObject.NULL_BOOL || type==com.amef.AMEFObject.NULL_CHAR)
		{			
			AMEFObject = new AMEFObject();
			AMEFObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				char[] name = new char[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				AMEFObject.setName(name);
			}
			else 
				position++;		
		}
		else if(type==com.amef.AMEFObject.STRING_TYPE)
		{			
			AMEFObject = new AMEFObject();
			AMEFObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				char[] name = new char[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				AMEFObject.setName(name);
			}
			else 
				position++;
			int lengthm = AMEFResources.byteArrayToInt(buffer,position,4);
			AMEFObject.setLength(lengthm);
			position += 4;
			char[] value = new char[lengthm];
			System.arraycopy(buffer, position, value, 0, value.length);
			AMEFObject.setValue(value);
			position += 5+lengthm;			
		}
		else if(type==com.amef.AMEFObject.STRING_65536_TYPE)
		{			
			AMEFObject = new AMEFObject();
			AMEFObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				char[] name = new char[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				AMEFObject.setName(name);
			}
			else 
				position++;
			int lengthm = AMEFResources.byteArrayToInt(buffer,position,2);
			AMEFObject.setLength(lengthm);
			position += 2;
			char[] value = new char[lengthm];
			System.arraycopy(buffer, position, value, 0, value.length);
			AMEFObject.setValue(value);
			position += 3+lengthm;			
		}
		else if(type==com.amef.AMEFObject.STRING_16777216_TYPE)
		{			
			AMEFObject = new AMEFObject();
			AMEFObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				char[] name = new char[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				AMEFObject.setName(name);
			}
			else 
				position++;
			int lengthm = AMEFResources.byteArrayToInt(buffer,position,3);
			AMEFObject.setLength(lengthm);
			position += 3;
			char[] value = new char[lengthm];
			System.arraycopy(buffer, position, value, 0, value.length);
			AMEFObject.setValue(value);
			position += 4+lengthm;			
		}		
		else if(type==com.amef.AMEFObject.DATE_TYPE || type==com.amef.AMEFObject.STRING_256_TYPE || type==com.amef.AMEFObject.DOUBLE_FLOAT_TYPE)
		{			
			AMEFObject = new AMEFObject();
			AMEFObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				char[] name = new char[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				AMEFObject.setName(name);
			}
			else 
				position++;
			int lengthm = AMEFResources.byteArrayToInt(buffer,position,1);			
			AMEFObject.setLength(lengthm);
			position++;
			char[] value = new char[lengthm];			
			System.arraycopy(buffer, position, value, 0, value.length);
			AMEFObject.setValue(value);
			position += lengthm;			
		}
		else if(type==com.amef.AMEFObject.VERY_SMALL_INT_TYPE)
		{			
			AMEFObject = new AMEFObject();
			AMEFObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				char[] name = new char[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				AMEFObject.setName(name);
			}
			else 
				position++;
			AMEFObject.setLength(1);
			AMEFObject.setValue(new char[]{buffer[position]});
			position += 1;	
		}
		else if(type==com.amef.AMEFObject.SMALL_INT_TYPE)
		{			
			AMEFObject = new AMEFObject();
			AMEFObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				char[] name = new char[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				AMEFObject.setName(name);
			}
			else 
				position++;
			AMEFObject.setLength(2);
			AMEFObject.setValue(new char[]{buffer[position],buffer[position+1]});
			position += 2;		
		}
		else if(type==com.amef.AMEFObject.BIG_INT_TYPE)
		{			
			AMEFObject = new AMEFObject();
			AMEFObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				char[] name = new char[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				AMEFObject.setName(name);
			}
			else 
				position++;
			AMEFObject.setLength(3);
			AMEFObject.setValue(new char[]{buffer[position],buffer[position+1],buffer[position+2]});
			position += 3;		
		}
		else if(type==com.amef.AMEFObject.INT_TYPE)
		{			
			AMEFObject = new AMEFObject();
			AMEFObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				char[] name = new char[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				AMEFObject.setName(name);
			}
			else 
				position++;
			AMEFObject.setLength(4);
			AMEFObject.setValue(new char[]{buffer[position],buffer[position+1],buffer[position+2],
					buffer[position+3]});
			position += 4;		
		}
		else if(type==com.amef.AMEFObject.VS_LONG_INT_TYPE)
		{			
			AMEFObject = new AMEFObject();
			AMEFObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				char[] name = new char[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				AMEFObject.setName(name);
			}
			else 
				position++;
			AMEFObject.setLength(5);
			AMEFObject.setValue(new char[]{buffer[position],buffer[position+1],buffer[position+2],
					buffer[position+3],buffer[position+4]});
			position += 5;		
		}
		else if(type==com.amef.AMEFObject.S_LONG_INT_TYPE)
		{			
			AMEFObject = new AMEFObject();
			AMEFObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				char[] name = new char[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				AMEFObject.setName(name);
			}
			else 
				position++;
			AMEFObject.setLength(6);
			AMEFObject.setValue(new char[]{buffer[position],buffer[position+1],buffer[position+2],
					buffer[position+3],buffer[position+4],buffer[position+5]});
			position += 6;		
		}
		else if(type==com.amef.AMEFObject.B_LONG_INT_TYPE)
		{			
			AMEFObject = new AMEFObject();
			AMEFObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				char[] name = new char[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				AMEFObject.setName(name);
			}
			else 
				position++;
			AMEFObject.setLength(7);
			AMEFObject.setValue(new char[]{buffer[position],buffer[position+1],buffer[position+2],
					buffer[position+3],buffer[position+4],buffer[position+5],buffer[position+6]});
			position += 7;		
		}
		else if(type==com.amef.AMEFObject.LONG_INT_TYPE)
		{			
			AMEFObject = new AMEFObject();
			AMEFObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				char[] name = new char[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				AMEFObject.setName(name);
			}
			else 
				position++;
			AMEFObject.setLength(8);
			AMEFObject.setValue(new char[]{buffer[position],buffer[position+1],
					buffer[position+2],buffer[position+3],buffer[position+4],
					buffer[position+5],buffer[position+6],buffer[position+7]});
			position += 8;		
		}
		else if(type==com.amef.AMEFObject.BOOLEAN_TYPE || type==com.amef.AMEFObject.CHAR_TYPE)
		{			
			AMEFObject = new AMEFObject();
			AMEFObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				char[] name = new char[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				AMEFObject.setName(name);
			}
			else 
				position++;
			AMEFObject.setLength(1);
			AMEFObject.setValue(new char[]{buffer[position]});
			position += 1;		
		}
		else if(type==com.amef.AMEFObject.VS_OBJECT_TYPE)
		{				
			AMEFObject = new AMEFObject();
			AMEFObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				char[] name = new char[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				AMEFObject.setName(name);
			}
			else 
				position++;
			int lengthm = AMEFResources.byteArrayToInt(buffer,position,1);
			AMEFObject.setLength(lengthm);
			position++;
			while(position<buffer.length)
			{
				AMEFObject obj = decodeSinglePacketB(buffer,ignoreName);
				if(obj==null)
					System.out.println(new String(buffer)+" "+position);
				AMEFObject.addPacket(obj);
			}
		}
		else if(type==com.amef.AMEFObject.S_OBJECT_TYPE)
		{				
			AMEFObject = new AMEFObject();
			AMEFObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				char[] name = new char[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				AMEFObject.setName(name);
			}
			else 
				position++;
			int lengthm = AMEFResources.byteArrayToInt(buffer,position,2);
			AMEFObject.setLength(lengthm);
			//char[] value = new char[lengthm];
			//System.arraycopy(buffer, 3, value, 0, lengthm);
			position += 2;
			while(position<buffer.length)
			{
				AMEFObject obj = decodeSinglePacketB(buffer,ignoreName);
				AMEFObject.addPacket(obj);
			}
		}
		else if(type==com.amef.AMEFObject.B_OBJECT_TYPE)
		{				
			AMEFObject = new AMEFObject();
			AMEFObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				char[] name = new char[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				AMEFObject.setName(name);
			}
			else 
				position++;
			int lengthm = AMEFResources.byteArrayToInt(buffer,position,3);
			AMEFObject.setLength(lengthm);
			char[] value = new char[lengthm];
			System.arraycopy(buffer, 4, value, 0, lengthm);
			position += 3;
			while(position<buffer.length)
			{
				AMEFObject obj = decodeSinglePacketB(buffer,ignoreName);
				AMEFObject.addPacket(obj);
			}
		}
		else if(type==com.amef.AMEFObject.OBJECT_TYPE)
		{				
			AMEFObject = new AMEFObject();
			AMEFObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				char[] name = new char[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				AMEFObject.setName(name);
			}
			else 
				position++;
			int lengthm = AMEFResources.byteArrayToInt(buffer,position,4);
			AMEFObject.setLength(lengthm);
			char[] value = new char[lengthm];
			System.arraycopy(buffer, 5, value, 0, lengthm);
			position += 4;
			while(position<buffer.length)
			{
				AMEFObject obj = decodeSinglePacketB(buffer,ignoreName);
				AMEFObject.addPacket(obj);
			}
		}
		return AMEFObject;
	}
	
	private AMEFObject decodeSinglePacketBIN(char[] buffer) throws AMEFDecodeException
	{
		char type = (char)buffer[position];
		AMEFObject AMEFObject = null;
		if(type==com.amef.AMEFObject.NULL_STRING || type==com.amef.AMEFObject.NULL_DATE || type==com.amef.AMEFObject.NULL_NUMBER
				|| type==com.amef.AMEFObject.NULL_BOOL || type==com.amef.AMEFObject.NULL_CHAR)
		{			
			AMEFObject = new AMEFObject();
			AMEFObject.setType(type);position++;		
		}
		else if(type==com.amef.AMEFObject.STRING_TYPE)
		{			
			AMEFObject = new AMEFObject();
			AMEFObject.setType(type);
			position++;
			int lengthm = AMEFResources.byteArrayToInt(buffer,position,4);
			AMEFObject.setLength(lengthm);
			position += 4;
			char[] value = new char[lengthm];
			System.arraycopy(buffer, position, value, 0, value.length);
			AMEFObject.setValue(value);
			position += 5+lengthm;			
		}
		else if(type==com.amef.AMEFObject.STRING_65536_TYPE)
		{			
			AMEFObject = new AMEFObject();
			AMEFObject.setType(type);
			position++;
			int lengthm = AMEFResources.byteArrayToInt(buffer,position,2);
			AMEFObject.setLength(lengthm);
			position += 2;
			char[] value = new char[lengthm];
			System.arraycopy(buffer, position, value, 0, value.length);
			AMEFObject.setValue(value);
			position += 3+lengthm;			
		}
		else if(type==com.amef.AMEFObject.STRING_16777216_TYPE)
		{			
			AMEFObject = new AMEFObject();
			AMEFObject.setType(type);
			position++;
			int lengthm = AMEFResources.byteArrayToInt(buffer,position,3);
			AMEFObject.setLength(lengthm);
			position += 3;
			char[] value = new char[lengthm];
			System.arraycopy(buffer, position, value, 0, value.length);
			AMEFObject.setValue(value);
			position += 4+lengthm;			
		}		
		else if(type==com.amef.AMEFObject.DATE_TYPE || type==com.amef.AMEFObject.STRING_256_TYPE || type==com.amef.AMEFObject.DOUBLE_FLOAT_TYPE)
		{			
			AMEFObject = new AMEFObject();
			AMEFObject.setType(type);
			position++;
			int lengthm = AMEFResources.byteArrayToInt(buffer,position,1);			
			AMEFObject.setLength(lengthm);
			position++;
			char[] value = new char[lengthm];			
			System.arraycopy(buffer, position, value, 0, value.length);
			AMEFObject.setValue(value);
			position += lengthm;			
		}
		else if(type==com.amef.AMEFObject.VERY_SMALL_INT_TYPE)
		{			
			AMEFObject = new AMEFObject();
			AMEFObject.setType(type);
			position++;
			AMEFObject.setLength(1);
			AMEFObject.setValue(new char[]{buffer[position]});
			position += 1;	
		}
		else if(type==com.amef.AMEFObject.SMALL_INT_TYPE)
		{			
			AMEFObject = new AMEFObject();
			AMEFObject.setType(type);
			position++;
			AMEFObject.setLength(2);
			AMEFObject.setValue(new char[]{buffer[position],buffer[position+1]});
			position += 2;		
		}
		else if(type==com.amef.AMEFObject.BIG_INT_TYPE)
		{			
			AMEFObject = new AMEFObject();
			AMEFObject.setType(type);
			position++;
			AMEFObject.setLength(3);
			AMEFObject.setValue(new char[]{buffer[position],buffer[position+1],buffer[position+2]});
			position += 3;		
		}
		else if(type==com.amef.AMEFObject.INT_TYPE)
		{			
			AMEFObject = new AMEFObject();
			AMEFObject.setType(type);
			position++;
			AMEFObject.setLength(4);
			AMEFObject.setValue(new char[]{buffer[position],buffer[position+1],buffer[position+2],
					buffer[position+3]});
			position += 4;		
		}
		else if(type==com.amef.AMEFObject.VS_LONG_INT_TYPE)
		{			
			AMEFObject = new AMEFObject();
			AMEFObject.setType(type);
			position++;
			AMEFObject.setLength(5);
			AMEFObject.setValue(new char[]{buffer[position],buffer[position+1],buffer[position+2],
					buffer[position+3],buffer[position+4]});
			position += 5;		
		}
		else if(type==com.amef.AMEFObject.S_LONG_INT_TYPE)
		{			
			AMEFObject = new AMEFObject();
			AMEFObject.setType(type);
			position++;
			AMEFObject.setLength(6);
			AMEFObject.setValue(new char[]{buffer[position],buffer[position+1],buffer[position+2],
					buffer[position+3],buffer[position+4],buffer[position+5]});
			position += 6;		
		}
		else if(type==com.amef.AMEFObject.B_LONG_INT_TYPE)
		{			
			AMEFObject = new AMEFObject();
			AMEFObject.setType(type);
			position++;
			AMEFObject.setLength(7);
			AMEFObject.setValue(new char[]{buffer[position],buffer[position+1],buffer[position+2],
					buffer[position+3],buffer[position+4],buffer[position+5],buffer[position+6]});
			position += 7;		
		}
		else if(type==com.amef.AMEFObject.LONG_INT_TYPE)
		{			
			AMEFObject = new AMEFObject();
			AMEFObject.setType(type);
			position++;
			AMEFObject.setLength(8);
			AMEFObject.setValue(new char[]{buffer[position],buffer[position+1],
					buffer[position+2],buffer[position+3],buffer[position+4],
					buffer[position+5],buffer[position+6],buffer[position+7]});
			position += 8;		
		}
		else if(type==com.amef.AMEFObject.BOOLEAN_TYPE || type==com.amef.AMEFObject.CHAR_TYPE)
		{			
			AMEFObject = new AMEFObject();
			AMEFObject.setType(type);
			position++;
			AMEFObject.setLength(1);
			AMEFObject.setValue(new char[]{buffer[position]});
			position += 1;		
		}
		else if(type==com.amef.AMEFObject.VS_OBJECT_TYPE)
		{				
			AMEFObject = new AMEFObject();
			AMEFObject.setType(type);
			position++;
			int lengthm = AMEFResources.byteArrayToInt(buffer,position,1);
			AMEFObject.setLength(lengthm);
			position++;
			while(position<buffer.length)
			{
				AMEFObject obj = decodeSinglePacketBIN(buffer);
				AMEFObject.addPacket(obj);
			}
		}
		else if(type==com.amef.AMEFObject.S_OBJECT_TYPE)
		{				
			AMEFObject = new AMEFObject();
			AMEFObject.setType(type);
			position++;
			int lengthm = AMEFResources.byteArrayToInt(buffer,position,2);
			AMEFObject.setLength(lengthm);
			//char[] value = new char[lengthm];
			//System.arraycopy(buffer, 3, value, 0, lengthm);
			position += 2;
			while(position<buffer.length)
			{
				AMEFObject obj = decodeSinglePacketBIN(buffer);
				AMEFObject.addPacket(obj);
			}
		}
		else if(type==com.amef.AMEFObject.B_OBJECT_TYPE)
		{				
			AMEFObject = new AMEFObject();
			AMEFObject.setType(type);
			position++;
			int lengthm = AMEFResources.byteArrayToInt(buffer,position,3);
			AMEFObject.setLength(lengthm);
			//char[] value = new char[lengthm];
			//System.arraycopy(buffer, 4, value, 0, lengthm);
			position += 3;
			while(position<buffer.length)
			{
				AMEFObject obj = decodeSinglePacketBIN(buffer);
				AMEFObject.addPacket(obj);
			}
		}
		else if(type==com.amef.AMEFObject.OBJECT_TYPE)
		{				
			AMEFObject = new AMEFObject();
			AMEFObject.setType(type);
			position++;
			int lengthm = AMEFResources.byteArrayToInt(buffer,position,4);
			AMEFObject.setLength(lengthm);
			//char[] value = new char[lengthm];
			//System.arraycopy(buffer, 5, value, 0, lengthm);
			position += 4;
			while(position<buffer.length)
			{
				AMEFObject obj = decodeSinglePacketBIN(buffer);
				AMEFObject.addPacket(obj);
			}
		}
		return AMEFObject;
	}
	
}
