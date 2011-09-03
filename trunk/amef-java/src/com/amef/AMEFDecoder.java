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
 * providses the decode method to get the JDBObject from its transmission form
 */
public final class AMEFDecoder
{
	//private String tempVal = "";
	
	
	/**
	 * @param data
	 * @param considerLength
	 * @return JDBObject
	 * @throws AMEFDecodeException
	 * decode the bytestream to give the equivalent JDBObject
	 */
	/*public JDBObject decode(byte[] data,boolean considerLength,boolean ignoreName) throws AMEFDecodeException
	{
		int startWith = 0;		
		if(considerLength)
			startWith = 4;
		String strdata = new String(data);
		strdata = strdata.substring(startWith);
		JDBObject JDBObject = null;
		try
		{
			JDBObject = decodeSinglePacket(strdata,ignoreName);
		}
		catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return JDBObject;
	}
	
	*//**
	 * @param data
	 * @param considerLength
	 * @return JDBObject
	 * @throws AMEFDecodeException
	 * decode the bytestream to give the equivalent JDBObject
	 *//*
	public JDBObject decode(String strdata,boolean considerLength,boolean ignoreName) throws AMEFDecodeException
	{
		int startWith = 0;		
		if(considerLength)
			startWith = 4;
		strdata = strdata.substring(startWith);
		JDBObject JDBObject = null;
		try
		{
			JDBObject = decodeSinglePacket(strdata,ignoreName);
		}
		catch (UnsupportedEncodingException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return JDBObject;
	}
	*/
	
	public AMEFObject decodeB(byte[] buffer,boolean considerLength,boolean ignoreName) throws AMEFDecodeException
	{
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
		//JDBObject JDBObject = ignoreName?decodeSinglePacketBINNew(strdata,position):decodeSinglePacketB(strdata,ignoreName,position);
		AMEFObject JDBObject = decodeSinglePacketBINNew(strdata,position,ignoreName);
		return JDBObject;
	}
	
	
	
	/*public JDBObject decodeJDBString(byte[] data,boolean considerLength) throws AMEFDecodeException
	{
		int startWith = 0;		
		if(considerLength)
			startWith = 4;
		String strdata = new String(data);
		strdata = strdata.substring(startWith);
		JDBObject JDBObject = decodeSingleJDBStringPacket(strdata);
		return JDBObject;
	}
	
	private JDBObject decodeSingleJDBStringPacket(String strdata) throws AMEFDecodeException
	{
		JDBObject jDBObject = null;
		char type = strdata.charAt(0);
		if(type==JDBObject.STRING_TYPE)
		{			
			jDBObject = new JDBObject();
			jDBObject.setType(type);
			int lengthm = ((strdata.charAt(1) & 0xff) << 24) | ((strdata.charAt(2) & 0xff) << 16)
							| ((strdata.charAt(3) & 0xff) << 8) | ((strdata.charAt(4) & 0xff));
			jDBObject.setLength(lengthm);
			String value = strdata.substring(5,5+lengthm);
			jDBObject.setValue(value);
			tempVal = strdata.substring(5+lengthm);			
		}
		else if(type==JDBObject.STRING_65536_TYPE)
		{			
			jDBObject = new JDBObject();
			jDBObject.setType(type);
			int lengthm = ((strdata.charAt(1) & 0xff) << 8) | (strdata.charAt(2) & 0xff);
			jDBObject.setLength(lengthm);
			String value = strdata.substring(3,3+lengthm);
			jDBObject.setValue(value);
			tempVal = strdata.substring(3+lengthm);			
		}
		else if(type==JDBObject.STRING_16777216_TYPE)
		{			
			jDBObject = new JDBObject();
			jDBObject.setType(type);
			int lengthm = ((strdata.charAt(1) & 0xff) << 16) 
							| ((strdata.charAt(2) & 0xff) << 8) | (strdata.charAt(3) & 0xff);
			jDBObject.setLength(lengthm);
			String value = strdata.substring(3,3+lengthm);
			jDBObject.setValue(value);
			tempVal = strdata.substring(3+lengthm);			
		}		
		else if(type==JDBObject.DATE_TYPE || type==JDBObject.STRING_256_TYPE || type==JDBObject.DOUBLE_FLOAT_TYPE)
		{			
			jDBObject = new JDBObject();
			jDBObject.setType(type);
			int lengthm = strdata.charAt(1) & 0xff;
			jDBObject.setLength(lengthm);
			String value = strdata.substring(2,2+lengthm);
			jDBObject.setValue(value);
			tempVal = strdata.substring(2+lengthm);			
		}
		else if(type==JDBObject.VERY_SMALL_INT_TYPE)
		{			
			jDBObject = new JDBObject();
			jDBObject.setType(type);
			String value = (strdata.charAt(1) & 0xff) + "";
			jDBObject.setValue(value);
			tempVal = strdata.substring(2);			
		}
		else if(type==JDBObject.SMALL_INT_TYPE)
		{			
			jDBObject = new JDBObject();
			jDBObject.setType(type);
			int lengthm = ((strdata.charAt(1) & 0xff) << 8) | ((strdata.charAt(2) & 0xff));
			String value = lengthm + "";
			jDBObject.setValue(value);
			tempVal = strdata.substring(3);			
		}
		else if(type==JDBObject.BIG_INT_TYPE)
		{			
			jDBObject = new JDBObject();
			jDBObject.setType(type);
			int lengthm = ((strdata.charAt(1) & 0xff) << 16) | ((strdata.charAt(2) & 0xff) << 8) 
							| ((strdata.charAt(3) & 0xff));
			String value = lengthm + "";
			jDBObject.setValue(value);
			tempVal = strdata.substring(4);			
		}
		else if(type==JDBObject.INT_TYPE)
		{			
			jDBObject = new JDBObject();
			jDBObject.setType(type);
			int lengthm = ((strdata.charAt(1) & 0xff) << 24) | ((strdata.charAt(2) & 0xff) << 16)
							| ((strdata.charAt(3) & 0xff) << 8) | ((strdata.charAt(4) & 0xff));
			String value = lengthm + "";
			jDBObject.setValue(value);
			tempVal = strdata.substring(5);			
		}
		else if(type==JDBObject.VS_LONG_INT_TYPE)
		{			
			jDBObject = new JDBObject();
			jDBObject.setType(type);
			long lengthm = ((strdata.charAt(1) & 0xff) << 32) | ((strdata.charAt(2) & 0xff) << 24) 
							| ((strdata.charAt(3) & 0xff) << 16) | ((strdata.charAt(4) & 0xff) << 8) 
							| ((strdata.charAt(5) & 0xff));
			String value = lengthm + "";
			jDBObject.setValue(value);
			tempVal = strdata.substring(6);			
		}
		else if(type==JDBObject.S_LONG_INT_TYPE)
		{			
			jDBObject = new JDBObject();
			jDBObject.setType(type);
			long lengthm = ((strdata.charAt(1) & 0xff) << 40) | ((strdata.charAt(2) & 0xff) << 32) 
							| ((strdata.charAt(3) & 0xff) << 24) 
							| ((strdata.charAt(4) & 0xff) << 16) | ((strdata.charAt(5) & 0xff) << 8) 
							| ((strdata.charAt(6) & 0xff));
			String value = lengthm + "";
			jDBObject.setValue(value);
			tempVal = strdata.substring(7);			
		}
		else if(type==JDBObject.B_LONG_INT_TYPE)
		{			
			jDBObject = new JDBObject();
			jDBObject.setType(type);
			long lengthm = ((strdata.charAt(1) & 0xff) << 48) | ((strdata.charAt(2) & 0xff) << 40) 
							| ((strdata.charAt(3) & 0xff) << 32) 
							| ((strdata.charAt(4) & 0xff) << 24) 
							| ((strdata.charAt(5) & 0xff) << 16) | ((strdata.charAt(6) & 0xff) << 8) 
							| ((strdata.charAt(7) & 0xff));
			String value = lengthm + "";
			jDBObject.setValue(value);
			tempVal = strdata.substring(8);			
		}
		else if(type==JDBObject.LONG_INT_TYPE)
		{			
			jDBObject = new JDBObject();
			jDBObject.setType(type);
			long lengthm = ((strdata.charAt(1) & 0xff) << 56) | ((strdata.charAt(2) & 0xff) << 48) 
							| ((strdata.charAt(3) & 0xff) << 40) 
							| ((strdata.charAt(4) & 0xff) << 32) 
							| ((strdata.charAt(5) & 0xff) << 24) 
							| ((strdata.charAt(6) & 0xff) << 16) | ((strdata.charAt(7) & 0xff) << 8) 
							| ((strdata.charAt(8) & 0xff));
			String value = lengthm + "";
			jDBObject.setValue(value);
			tempVal = strdata.substring(9);			
		}
		else if(type==JDBObject.BOOLEAN_TYPE || type==JDBObject.CHAR_TYPE)
		{
			jDBObject = new JDBObject();
			jDBObject.setType(type);
			jDBObject.setLength(1);
			String value = strdata.charAt(1)+"";
			jDBObject.setValue(value);
			tempVal = strdata.substring(2);	
		}
		else if(type==JDBObject.VS_OBJECT_TYPE)
		{			
			jDBObject = new JDBObject();
			jDBObject.setType(type);
			int lengthm = AMEFResources.byteArrayToInt(strdata.substring(1,2).getBytes());
			jDBObject.setLength(lengthm);
			tempVal = strdata.substring(2,2+lengthm);
			while(!tempVal.equals(""))
			{
				JDBObject obj = decodeSingleJDBStringPacket(tempVal);
				jDBObject.addPacket(obj);
			}				
			tempVal = strdata.substring(2+lengthm);
		}
		else if(type==JDBObject.S_OBJECT_TYPE)
		{			
			jDBObject = new JDBObject();
			jDBObject.setType(type);
			int lengthm = ((strdata.charAt(1) & 0xff) << 8) | ((strdata.charAt(2) & 0xff));
			jDBObject.setLength(lengthm);
			tempVal = strdata.substring(3,3+lengthm);
			while(!tempVal.equals(""))
			{
				JDBObject obj = decodeSingleJDBStringPacket(tempVal);
				jDBObject.addPacket(obj);
			}				
			tempVal = strdata.substring(3+lengthm);
		}
		else if(type==JDBObject.B_OBJECT_TYPE)
		{			
			jDBObject = new JDBObject();
			jDBObject.setType(type);
			int lengthm = ((strdata.charAt(1) & 0xff) << 16)
							| ((strdata.charAt(2) & 0xff) << 8) | ((strdata.charAt(3) & 0xff));
			jDBObject.setLength(lengthm);
			tempVal = strdata.substring(4,4+lengthm);
			while(!tempVal.equals(""))
			{
				JDBObject obj = decodeSingleJDBStringPacket(tempVal);
				jDBObject.addPacket(obj);
			}				
			tempVal = strdata.substring(4+lengthm);
		}
		else if(type==JDBObject.OBJECT_TYPE)
		{			
			jDBObject = new JDBObject();
			jDBObject.setType(type);
			int lengthm = ((strdata.charAt(1) & 0xff) << 24) | ((strdata.charAt(2) & 0xff) << 16)
							| ((strdata.charAt(3) & 0xff) << 8) | ((strdata.charAt(4) & 0xff));
			jDBObject.setLength(lengthm);
			tempVal = strdata.substring(5,5+lengthm);
			while(!tempVal.equals(""))
			{
				JDBObject obj = decodeSingleJDBStringPacket(tempVal);
				jDBObject.addPacket(obj);
			}				
			tempVal = strdata.substring(5+lengthm);
		}
		return jDBObject;
	}

	*//**
	 * @param strdata
	 * @return JDBObject
	 * @throws AMEFDecodeException
	 * decode the string to give the equivalent JDBObject
	 * @throws UnsupportedEncodingException 
	 *//*
	private JDBObject decodeSinglePacket(String strdata,boolean ignoreName) throws AMEFDecodeException, UnsupportedEncodingException
	{
		JDBObject jDBObject = null;
		char type = strdata.charAt(0);
		if(type==JDBObject.STRING_TYPE)
		{
			if(strdata.charAt(1)==',')
			{
				jDBObject = new JDBObject();
				jDBObject.setType(type);
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
				jDBObject.setName(name);
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
				jDBObject.setLength(lengthm);
				String value = strdata.substring(pos+1,pos+lengthm+1);
				jDBObject.setValue(value);
				tempVal = strdata.substring(pos+lengthm+1);
			}
			else
			{
				throw new AMEFDecodeException("Invalid character after type specifier, expected ,");
			}
		}
		else if(type==JDBObject.STRING_65536_TYPE)
		{
			if(strdata.charAt(1)==',')
			{
				jDBObject = new JDBObject();
				jDBObject.setType(type);
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
				jDBObject.setName(name);
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
				jDBObject.setLength(lengthm);
				String value = strdata.substring(pos,pos+lengthm);
				jDBObject.setValue(value);
				tempVal = strdata.substring(pos+lengthm);
			}
			else
			{
				throw new AMEFDecodeException("Invalid character after type specifier, expected ,");
			}
		}
		else if(type==JDBObject.STRING_16777216_TYPE)
		{
			if(strdata.charAt(1)==',')
			{
				jDBObject = new JDBObject();
				jDBObject.setType(type);
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
				jDBObject.setName(name);
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
				jDBObject.setLength(lengthm);
				String value = strdata.substring(pos,pos+lengthm);
				jDBObject.setValue(value);
				tempVal = strdata.substring(pos+lengthm);
			}
			else
			{
				throw new AMEFDecodeException("Invalid character after type specifier, expected ,");
			}
		}
		else if(type==JDBObject.BOOLEAN_TYPE || type==JDBObject.CHAR_TYPE)
		{
			if(strdata.charAt(1)==',')
			{
				jDBObject = new JDBObject();
				jDBObject.setType(type);
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
				jDBObject.setName(name);
				if(pos>=strdata.length())
				{
					throw new AMEFDecodeException("Reached end of AMEF string, not found ,");
				}
				jDBObject.setLength(1);
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
				jDBObject.setValue(value);				
			}
			else
			{
				throw new AMEFDecodeException("Invalid character after type specifier, expected ,");
			}
		}
		else if(type==JDBObject.STRING_256_TYPE || type==JDBObject.DATE_TYPE || type==JDBObject.DOUBLE_FLOAT_TYPE)
		{
			if(strdata.charAt(1)==',')
			{
				jDBObject = new JDBObject();
				jDBObject.setType(type);
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
				jDBObject.setName(name);
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
				jDBObject.setLength(lengthm);
				String value = strdata.substring(pos,pos+lengthm);
				jDBObject.setValue(value);
				tempVal = strdata.substring(pos+lengthm);
			}
			else
			{
				throw new AMEFDecodeException("Invalid character after type specifier, expected ,");
			}
		}
		else if(type==JDBObject.VERY_SMALL_INT_TYPE)
		{
			jDBObject = getObject(strdata, type, ignoreName, 1);
		}
		else if(type==JDBObject.SMALL_INT_TYPE)
		{
			jDBObject = getObject(strdata, type, ignoreName, 2);
		}
		else if(type==JDBObject.BIG_INT_TYPE)
		{
			jDBObject = getObject(strdata, type, ignoreName, 3);
		}
		else if(type==JDBObject.INT_TYPE)
		{
			jDBObject = getObject(strdata, type, ignoreName, 4);
		}
		else if(type==JDBObject.VS_LONG_INT_TYPE)
		{
			jDBObject = getObject(strdata, type, ignoreName, 5);
		}
		else if(type==JDBObject.S_LONG_INT_TYPE)
		{
			jDBObject = getObject(strdata, type, ignoreName, 6);
		}
		else if(type==JDBObject.B_LONG_INT_TYPE)
		{
			jDBObject = getObject(strdata, type, ignoreName, 7);
		}
		else if(type==JDBObject.LONG_INT_TYPE)
		{
			jDBObject = getObject(strdata, type, ignoreName, 8);
		}
		else if(type==JDBObject.VS_OBJECT_TYPE)
		{
			if(strdata.charAt(1)==',')
			{
				jDBObject = new JDBObject();
				jDBObject.setType(type);
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
				jDBObject.setName(name);
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
				jDBObject.setLength(lengthm);
				//
				//String value = strdata.substring(pos+1,pos+lengthm+1);
				tempVal  = strdata.substring(pos,pos+lengthm);
				while(!tempVal.equals(""))
				{
					JDBObject obj = decodeSinglePacket(tempVal,ignoreName);
					jDBObject.addPacket(obj);
				}				
				tempVal = strdata.substring(pos+lengthm);
			}
			else
			{
				throw new AMEFDecodeException("Invalid character after type specifier, expected ,");
			}
		}
		else if(type==JDBObject.S_OBJECT_TYPE)
		{
			if(strdata.charAt(1)==',')
			{
				jDBObject = new JDBObject();
				jDBObject.setType(type);
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
				jDBObject.setName(name);
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
				jDBObject.setLength(lengthm);
				//String value = strdata.substring(pos+1,pos+lengthm+1);
				//tempVal = value;
				tempVal  = strdata.substring(pos,pos+lengthm);
				while(!tempVal.equals(""))
				{
					JDBObject obj = decodeSinglePacket(tempVal,ignoreName);
					jDBObject.addPacket(obj);
				}				
				tempVal = strdata.substring(pos+lengthm);
			}
			else
			{
				throw new AMEFDecodeException("Invalid character after type specifier, expected ,");
			}
		}
		else if(type==JDBObject.B_OBJECT_TYPE)
		{
			if(strdata.charAt(1)==',')
			{
				jDBObject = new JDBObject();
				jDBObject.setType(type);
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
				jDBObject.setName(name);
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
				jDBObject.setLength(lengthm);
				//String value = strdata.substring(pos+1,pos+lengthm+1);
				//tempVal = value;
				tempVal  = strdata.substring(pos,pos+lengthm);
				while(!tempVal.equals(""))
				{
					JDBObject obj = decodeSinglePacket(tempVal,ignoreName);
					jDBObject.addPacket(obj);
				}				
				tempVal = strdata.substring(pos+lengthm);
			}
			else
			{
				throw new AMEFDecodeException("Invalid character after type specifier, expected ,");
			}
		}
		else if(type==JDBObject.OBJECT_TYPE)
		{
			if(strdata.charAt(1)==',')
			{
				jDBObject = new JDBObject();
				jDBObject.setType(type);
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
				jDBObject.setName(name);
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
				jDBObject.setLength(lengthm);
				//String value = strdata.substring(pos+1,pos+lengthm+1);
				//tempVal = value;
				tempVal  = strdata.substring(pos,pos+lengthm);
				while(!tempVal.equals(""))
				{
					JDBObject obj = decodeSinglePacket(tempVal,ignoreName);
					jDBObject.addPacket(obj);
				}				
				tempVal = strdata.substring(pos+lengthm);
			}
			else
			{
				throw new AMEFDecodeException("Invalid character after type specifier, expected ,");
			}
		}
		return jDBObject;
	}
	
	private JDBObject getObject(String strdata,char type,boolean ignoreName,int typ) throws AMEFDecodeException
	{
		JDBObject jDBObject = null;
		if(strdata.charAt(1)==',')
		{
			jDBObject = new JDBObject();
			jDBObject.setType(type);
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
			jDBObject.setName(name);
			if(!ignoreName)pos++;
			String value = strdata.substring(pos,pos+typ);
			jDBObject.setValue(value);
			tempVal = strdata.substring(pos+typ);
		}
		else
		{
			throw new AMEFDecodeException("Invalid character after type specifier, expected ,");
		}
		return jDBObject;
	}*/
	
	//int position = 0;
	/*private JDBObject decodeSinglePacketB(byte[] buffer,boolean ignoreName,Integer position) throws AMEFDecodeException
	{
		char type = (char)buffer[position];
		JDBObject jDBObject = null;
		int st, en;
		if(type==JDBObject.NULL_STRING || type==JDBObject.NULL_DATE || type==JDBObject.NULL_NUMBER
				|| type==JDBObject.NULL_BOOL || type==JDBObject.NULL_CHAR)
		{			
			jDBObject = new JDBObject();
			jDBObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				byte[] name = new byte[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				jDBObject.setName(name);
			}
			else 
				position++;		
		}
		else if(type==JDBObject.STRING_TYPE)
		{			
			jDBObject = new JDBObject();
			jDBObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				byte[] name = new byte[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				jDBObject.setName(name);
			}
			else 
				position++;
			int lengthm = AMEFResources.byteArrayToInt(buffer,position,4);
			jDBObject.setLength(lengthm);
			position += 4;
			byte[] value = new byte[lengthm];
			System.arraycopy(buffer, position, value, 0, value.length);
			jDBObject.setValue(value);
			position += 5+lengthm;			
		}
		else if(type==JDBObject.STRING_65536_TYPE)
		{			
			jDBObject = new JDBObject();
			jDBObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				byte[] name = new byte[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				jDBObject.setName(name);
			}
			else 
				position++;
			int lengthm = AMEFResources.byteArrayToInt(buffer,position,2);
			jDBObject.setLength(lengthm);
			position += 2;
			byte[] value = new byte[lengthm];
			System.arraycopy(buffer, position, value, 0, value.length);
			jDBObject.setValue(value);
			position += 3+lengthm;			
		}
		else if(type==JDBObject.STRING_16777216_TYPE)
		{			
			jDBObject = new JDBObject();
			jDBObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				byte[] name = new byte[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				jDBObject.setName(name);
			}
			else 
				position++;
			int lengthm = AMEFResources.byteArrayToInt(buffer,position,3);
			jDBObject.setLength(lengthm);
			position += 3;
			byte[] value = new byte[lengthm];
			System.arraycopy(buffer, position, value, 0, value.length);
			jDBObject.setValue(value);
			position += 4+lengthm;			
		}		
		else if(type==JDBObject.DATE_TYPE || type==JDBObject.STRING_256_TYPE || type==JDBObject.DOUBLE_FLOAT_TYPE)
		{			
			jDBObject = new JDBObject();
			jDBObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				byte[] name = new byte[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				jDBObject.setName(name);
			}
			else 
				position++;
			int lengthm = AMEFResources.byteArrayToInt(buffer,position,1);			
			jDBObject.setLength(lengthm);
			position++;
			byte[] value = new byte[lengthm];			
			System.arraycopy(buffer, position, value, 0, value.length);
			jDBObject.setValue(value);
			position += lengthm;			
		}
		else if(type==JDBObject.VERY_SMALL_INT_TYPE)
		{			
			jDBObject = new JDBObject();
			jDBObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				byte[] name = new byte[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				jDBObject.setName(name);
			}
			else 
				position++;
			jDBObject.setLength(1);
			jDBObject.setValue(new byte[]{buffer[position]});
			position += 1;	
		}
		else if(type==JDBObject.SMALL_INT_TYPE)
		{			
			jDBObject = new JDBObject();
			jDBObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				byte[] name = new byte[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				jDBObject.setName(name);
			}
			else 
				position++;
			jDBObject.setLength(2);
			jDBObject.setValue(new byte[]{buffer[position],buffer[position+1]});
			position += 2;		
		}
		else if(type==JDBObject.BIG_INT_TYPE)
		{			
			jDBObject = new JDBObject();
			jDBObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				byte[] name = new byte[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				jDBObject.setName(name);
			}
			else 
				position++;
			jDBObject.setLength(3);
			jDBObject.setValue(new byte[]{buffer[position],buffer[position+1],buffer[position+2]});
			position += 3;		
		}
		else if(type==JDBObject.INT_TYPE)
		{			
			jDBObject = new JDBObject();
			jDBObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				byte[] name = new byte[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				jDBObject.setName(name);
			}
			else 
				position++;
			jDBObject.setLength(4);
			jDBObject.setValue(new byte[]{buffer[position],buffer[position+1],buffer[position+2],
					buffer[position+3]});
			position += 4;		
		}
		else if(type==JDBObject.VS_LONG_INT_TYPE)
		{			
			jDBObject = new JDBObject();
			jDBObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				byte[] name = new byte[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				jDBObject.setName(name);
			}
			else 
				position++;
			jDBObject.setLength(5);
			jDBObject.setValue(new byte[]{buffer[position],buffer[position+1],buffer[position+2],
					buffer[position+3],buffer[position+4]});
			position += 5;		
		}
		else if(type==JDBObject.S_LONG_INT_TYPE)
		{			
			jDBObject = new JDBObject();
			jDBObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				byte[] name = new byte[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				jDBObject.setName(name);
			}
			else 
				position++;
			jDBObject.setLength(6);
			jDBObject.setValue(new byte[]{buffer[position],buffer[position+1],buffer[position+2],
					buffer[position+3],buffer[position+4],buffer[position+5]});
			position += 6;		
		}
		else if(type==JDBObject.B_LONG_INT_TYPE)
		{			
			jDBObject = new JDBObject();
			jDBObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				byte[] name = new byte[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				jDBObject.setName(name);
			}
			else 
				position++;
			jDBObject.setLength(7);
			jDBObject.setValue(new byte[]{buffer[position],buffer[position+1],buffer[position+2],
					buffer[position+3],buffer[position+4],buffer[position+5],buffer[position+6]});
			position += 7;		
		}
		else if(type==JDBObject.LONG_INT_TYPE)
		{			
			jDBObject = new JDBObject();
			jDBObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				byte[] name = new byte[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				jDBObject.setName(name);
			}
			else 
				position++;
			jDBObject.setLength(8);
			jDBObject.setValue(new byte[]{buffer[position],buffer[position+1],
					buffer[position+2],buffer[position+3],buffer[position+4],
					buffer[position+5],buffer[position+6],buffer[position+7]});
			position += 8;		
		}
		else if(type==JDBObject.BOOLEAN_TYPE || type==JDBObject.CHAR_TYPE)
		{			
			jDBObject = new JDBObject();
			jDBObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				byte[] name = new byte[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				jDBObject.setName(name);
			}
			else 
				position++;
			jDBObject.setLength(1);
			jDBObject.setValue(new byte[]{buffer[position]});
			position += 1;		
		}
		else if(type==JDBObject.VS_OBJECT_TYPE)
		{				
			jDBObject = new JDBObject();
			jDBObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				byte[] name = new byte[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				jDBObject.setName(name);
			}
			else 
				position++;
			int lengthm = AMEFResources.byteArrayToInt(buffer,position,1);
			jDBObject.setLength(lengthm);
			position++;
			while(position<buffer.length)
			{
				JDBObject obj = decodeSinglePacketB(buffer,ignoreName,position);
				position = obj.position;
				jDBObject.addPacket(obj);
			}
		}
		else if(type==JDBObject.S_OBJECT_TYPE)
		{				
			jDBObject = new JDBObject();
			jDBObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				byte[] name = new byte[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				jDBObject.setName(name);
			}
			else 
				position++;
			int lengthm = AMEFResources.byteArrayToInt(buffer,position,2);
			jDBObject.setLength(lengthm);
			//byte[] value = new byte[lengthm];
			//System.arraycopy(buffer, 3, value, 0, lengthm);
			position += 2;
			while(position<buffer.length)
			{
				JDBObject obj = decodeSinglePacketB(buffer,ignoreName,position);
				position = obj.position;
				jDBObject.addPacket(obj);
			}
		}
		else if(type==JDBObject.B_OBJECT_TYPE)
		{				
			jDBObject = new JDBObject();
			jDBObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				byte[] name = new byte[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				jDBObject.setName(name);
			}
			else 
				position++;
			int lengthm = AMEFResources.byteArrayToInt(buffer,position,3);
			jDBObject.setLength(lengthm);
			byte[] value = new byte[lengthm];
			System.arraycopy(buffer, 4, value, 0, lengthm);
			position += 3;
			while(position<buffer.length)
			{
				JDBObject obj = decodeSinglePacketB(buffer,ignoreName,position);
				position = obj.position;
				jDBObject.addPacket(obj);
			}
		}
		else if(type==JDBObject.OBJECT_TYPE)
		{				
			jDBObject = new JDBObject();
			jDBObject.setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				byte[] name = new byte[en - st];
				System.arraycopy(buffer, st, name, 0, name.length);
				jDBObject.setName(name);
			}
			else 
				position++;
			int lengthm = AMEFResources.byteArrayToInt(buffer,position,4);
			jDBObject.setLength(lengthm);
			byte[] value = new byte[lengthm];
			System.arraycopy(buffer, 5, value, 0, lengthm);
			position += 4;
			while(position<buffer.length)
			{
				JDBObject obj = decodeSinglePacketB(buffer,ignoreName,position);
				position = obj.position;
				jDBObject.addPacket(obj);
			}
		}
		jDBObject.position = position;
		return jDBObject;
	}
	
	private JDBObject decodeSinglePacketBIN(byte[] buffer,Integer position) throws AMEFDecodeException
	{
		char type = (char)buffer[position];
		JDBObject jDBObject = null;
		if(type==JDBObject.NULL_STRING || type==JDBObject.NULL_DATE || type==JDBObject.NULL_NUMBER
				|| type==JDBObject.NULL_BOOL || type==JDBObject.NULL_CHAR)
		{			
			jDBObject = new JDBObject();
			jDBObject.setLength(1);
			jDBObject.setType(type);position++;		
		}
		else if(type==JDBObject.STRING_TYPE)
		{			
			jDBObject = new JDBObject();
			jDBObject.setType(type);
			position++;
			int lengthm = AMEFResources.byteArrayToInt(buffer,position,4);
			jDBObject.setLength(lengthm);
			position += 4;
			byte[] value = new byte[lengthm];
			System.arraycopy(buffer, position, value, 0, value.length);
			jDBObject.setValue(value);
			position += 5+lengthm;			
		}
		else if(type==JDBObject.STRING_65536_TYPE)
		{			
			jDBObject = new JDBObject();
			jDBObject.setType(type);
			position++;
			int lengthm = AMEFResources.byteArrayToInt(buffer,position,2);
			jDBObject.setLength(lengthm);
			position += 2;
			byte[] value = new byte[lengthm];
			System.arraycopy(buffer, position, value, 0, value.length);
			jDBObject.setValue(value);
			position += 3+lengthm;			
		}
		else if(type==JDBObject.STRING_16777216_TYPE)
		{			
			jDBObject = new JDBObject();
			jDBObject.setType(type);
			position++;
			int lengthm = AMEFResources.byteArrayToInt(buffer,position,3);
			jDBObject.setLength(lengthm);
			position += 3;
			byte[] value = new byte[lengthm];
			System.arraycopy(buffer, position, value, 0, value.length);
			jDBObject.setValue(value);
			position += 4+lengthm;			
		}		
		else if(type==JDBObject.DATE_TYPE || type==JDBObject.STRING_256_TYPE || type==JDBObject.DOUBLE_FLOAT_TYPE)
		{			
			jDBObject = new JDBObject();
			jDBObject.setType(type);
			position++;
			int lengthm = AMEFResources.byteArrayToInt(buffer,position,1);			
			jDBObject.setLength(lengthm);
			position++;
			byte[] value = new byte[lengthm];			
			System.arraycopy(buffer, position, value, 0, value.length);
			jDBObject.setValue(value);
			position += lengthm;			
		}
		else if(type==JDBObject.VERY_SMALL_INT_TYPE)
		{			
			jDBObject = new JDBObject();
			jDBObject.setType(type);
			position++;
			jDBObject.setLength(1);
			jDBObject.setValue(new byte[]{buffer[position]});
			position += 1;	
		}
		else if(type==JDBObject.SMALL_INT_TYPE)
		{			
			jDBObject = new JDBObject();
			jDBObject.setType(type);
			position++;
			jDBObject.setLength(2);
			jDBObject.setValue(new byte[]{buffer[position],buffer[position+1]});
			position += 2;		
		}
		else if(type==JDBObject.BIG_INT_TYPE)
		{			
			jDBObject = new JDBObject();
			jDBObject.setType(type);
			position++;
			jDBObject.setLength(3);
			jDBObject.setValue(new byte[]{buffer[position],buffer[position+1],buffer[position+2]});
			position += 3;		
		}
		else if(type==JDBObject.INT_TYPE)
		{			
			jDBObject = new JDBObject();
			jDBObject.setType(type);
			position++;
			jDBObject.setLength(4);
			jDBObject.setValue(new byte[]{buffer[position],buffer[position+1],buffer[position+2],
					buffer[position+3]});
			position += 4;		
		}
		else if(type==JDBObject.VS_LONG_INT_TYPE)
		{			
			jDBObject = new JDBObject();
			jDBObject.setType(type);
			position++;
			jDBObject.setLength(5);
			jDBObject.setValue(new byte[]{buffer[position],buffer[position+1],buffer[position+2],
					buffer[position+3],buffer[position+4]});
			position += 5;		
		}
		else if(type==JDBObject.S_LONG_INT_TYPE)
		{			
			jDBObject = new JDBObject();
			jDBObject.setType(type);
			position++;
			jDBObject.setLength(6);
			jDBObject.setValue(new byte[]{buffer[position],buffer[position+1],buffer[position+2],
					buffer[position+3],buffer[position+4],buffer[position+5]});
			position += 6;		
		}
		else if(type==JDBObject.B_LONG_INT_TYPE)
		{			
			jDBObject = new JDBObject();
			jDBObject.setType(type);
			position++;
			jDBObject.setLength(7);
			jDBObject.setValue(new byte[]{buffer[position],buffer[position+1],buffer[position+2],
					buffer[position+3],buffer[position+4],buffer[position+5],buffer[position+6]});
			position += 7;		
		}
		else if(type==JDBObject.LONG_INT_TYPE)
		{			
			jDBObject = new JDBObject();
			jDBObject.setType(type);
			position++;
			jDBObject.setLength(8);
			jDBObject.setValue(new byte[]{buffer[position],buffer[position+1],
					buffer[position+2],buffer[position+3],buffer[position+4],
					buffer[position+5],buffer[position+6],buffer[position+7]});
			position += 8;		
		}
		else if(type==JDBObject.BOOLEAN_TYPE || type==JDBObject.CHAR_TYPE)
		{			
			jDBObject = new JDBObject();
			jDBObject.setType(type);
			position++;
			jDBObject.setLength(1);
			jDBObject.setValue(new byte[]{buffer[position]});
			position += 1;		
		}
		else if(type==JDBObject.VS_OBJECT_TYPE)
		{				
			jDBObject = new JDBObject();
			jDBObject.setType(type);
			position++;
			int lengthm = AMEFResources.byteArrayToInt(buffer,position,1);
			jDBObject.setLength(lengthm);
			position++;
			while(position<buffer.length)
			{
				JDBObject obj = decodeSinglePacketBIN(buffer,position);
				position = obj.position;
				jDBObject.addPacket(obj);
			}
		}
		else if(type==JDBObject.S_OBJECT_TYPE)
		{				
			jDBObject = new JDBObject();
			jDBObject.setType(type);
			position++;
			int lengthm = AMEFResources.byteArrayToInt(buffer,position,2);
			jDBObject.setLength(lengthm);
			//byte[] value = new byte[lengthm];
			//System.arraycopy(buffer, 3, value, 0, lengthm);
			position += 2;
			while(position<buffer.length)
			{
				JDBObject obj = decodeSinglePacketBIN(buffer,position);
				position = obj.position;
				jDBObject.addPacket(obj);
			}
		}
		else if(type==JDBObject.B_OBJECT_TYPE)
		{				
			jDBObject = new JDBObject();
			jDBObject.setType(type);
			position++;
			int lengthm = AMEFResources.byteArrayToInt(buffer,position,3);
			jDBObject.setLength(lengthm);
			//byte[] value = new byte[lengthm];
			//System.arraycopy(buffer, 4, value, 0, lengthm);
			position += 3;
			while(position<buffer.length)
			{
				JDBObject obj = decodeSinglePacketBIN(buffer,position);
				position = obj.position;
				jDBObject.addPacket(obj);
			}
		}
		else if(type==JDBObject.OBJECT_TYPE)
		{				
			jDBObject = new JDBObject();
			jDBObject.setType(type);
			position++;
			int lengthm = AMEFResources.byteArrayToInt(buffer,position,4);
			jDBObject.setLength(lengthm);
			//byte[] value = new byte[lengthm];
			//System.arraycopy(buffer, 5, value, 0, lengthm);
			position += 4;
			while(position<buffer.length)
			{
				JDBObject obj = decodeSinglePacketBIN(buffer,position);
				position = obj.position;
				jDBObject.addPacket(obj);
			}
		}
		jDBObject.position = position;
		return jDBObject;
	}
	*/
	private void addPrimitive(AMEFObject jDBObject,byte[] buffer,Integer position,boolean ignoreName)
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
				jDBObject.addNullPacket(type,new String(name));
			}
			else
			{
				position++;
				jDBObject.addNullPacket(type);
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
			if(names==null)jDBObject.addPacket(value);else jDBObject.addPacket(value,names);
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
			if(names==null)jDBObject.addPacket(value);else jDBObject.addPacket(value,names);
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
			if(names==null)jDBObject.addPacket(value);else jDBObject.addPacket(value,names);
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
			//jDBObject.setLength(lengthm);
			position++;
			byte[] value = new byte[lengthm];			
			System.arraycopy(buffer, position, value, 0, value.length);
			if(names==null)jDBObject.addPacket(value);else jDBObject.addPacket(value,names);
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
			//jDBObject.setLength(1);
			long value = AMEFResources.byteArrayToLong(new byte[]{buffer[position]});
			if(names==null)jDBObject.addPacket(value);else jDBObject.addPacket(value,names);
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
			//jDBObject.setLength(2);
			long value = AMEFResources.byteArrayToLong(new byte[]{buffer[position],buffer[position+1]});
			if(names==null)jDBObject.addPacket(value);else jDBObject.addPacket(value,names);
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
			//jDBObject.setLength(3);
			long value = AMEFResources.byteArrayToLong(new byte[]{buffer[position],buffer[position+1],buffer[position+2]});
			if(names==null)jDBObject.addPacket(value);else jDBObject.addPacket(value,names);
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
			//jDBObject.setLength(4);
			long value = AMEFResources.byteArrayToLong(new byte[]{buffer[position],buffer[position+1],buffer[position+2],
					buffer[position+3]});
			if(names==null)jDBObject.addPacket(value);else jDBObject.addPacket(value,names);
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
			//jDBObject.setLength(5);
			long value = AMEFResources.byteArrayToLong(new byte[]{buffer[position],buffer[position+1],buffer[position+2],
					buffer[position+3],buffer[position+4]});
			if(names==null)jDBObject.addPacket(value);else jDBObject.addPacket(value,names);
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
			//jDBObject.setLength(6);
			long value = AMEFResources.byteArrayToLong(new byte[]{buffer[position],buffer[position+1],buffer[position+2],
					buffer[position+3],buffer[position+4],buffer[position+5]});
			if(names==null)jDBObject.addPacket(value);else jDBObject.addPacket(value,names);
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
			//jDBObject.setLength(7);
			long value = AMEFResources.byteArrayToLong(new byte[]{buffer[position],buffer[position+1],buffer[position+2],
					buffer[position+3],buffer[position+4],buffer[position+5],buffer[position+6]});
			if(names==null)jDBObject.addPacket(value);else jDBObject.addPacket(value,names);
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
			//jDBObject.setLength(8);
			long value = AMEFResources.byteArrayToLong(new byte[]{buffer[position],buffer[position+1],
					buffer[position+2],buffer[position+3],buffer[position+4],
					buffer[position+5],buffer[position+6],buffer[position+7]});
			if(names==null)jDBObject.addPacket(value);else jDBObject.addPacket(value,names);
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
				if(names==null)jDBObject.addPacket(buffer[position]=='1'?true:false);
				else jDBObject.addPacket(buffer[position]=='1'?true:false);
			}
			else
			{
				if(names==null)jDBObject.addPacket((char)buffer[position]);
				else
					jDBObject.addPacket((char)buffer[position],names);
			}
			position += 1;		
		}
		jDBObject.position = position;
	}
	
	private AMEFObject decodeSinglePacketBINNew(byte[] buffer,Integer position,boolean ignoreName) throws AMEFDecodeException
	{
		char type = (char)buffer[position];
		AMEFObject jDBObject = null;
		if(type==AMEFObject.VS_OBJECT_TYPE)
		{	
			String names = null;
			jDBObject = new AMEFObject();
			//jDBObject.setType(type);
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
			//jDBObject.setLength(lengthm);
			position++;
			while(position<buffer.length)
			{
				addPrimitive(jDBObject, buffer, position,ignoreName);
				position = jDBObject.position;
			}
			if(names!=null)jDBObject.setName(names);
		}
		else if(type==AMEFObject.S_OBJECT_TYPE)
		{	
			String names = null;
			jDBObject = new AMEFObject();
			//jDBObject.setType(type);
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
			//jDBObject.setLength(lengthm);
			//byte[] value = new byte[lengthm];
			//System.arraycopy(buffer, 3, value, 0, lengthm);
			position += 2;
			while(position<buffer.length)
			{
				addPrimitive(jDBObject, buffer, position,ignoreName);
				position = jDBObject.position;
			}
			if(names!=null)jDBObject.setName(names);
		}
		else if(type==AMEFObject.B_OBJECT_TYPE)
		{	
			String names = null;
			jDBObject = new AMEFObject();
			//jDBObject.setType(type);
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
			//jDBObject.setLength(lengthm);
			//byte[] value = new byte[lengthm];
			//System.arraycopy(buffer, 4, value, 0, lengthm);
			position += 3;
			while(position<buffer.length)
			{
				addPrimitive(jDBObject, buffer, position,ignoreName);
				position = jDBObject.position;
			}
			if(names!=null)jDBObject.setName(names);
		}
		else if(type==AMEFObject.OBJECT_TYPE)
		{	
			String names = null;
			jDBObject = new AMEFObject();
			//jDBObject.setType(type);
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
			//jDBObject.setLength(lengthm);
			//byte[] value = new byte[lengthm];
			//System.arraycopy(buffer, 5, value, 0, lengthm);
			position += 4;
			while(position<buffer.length)
			{
				addPrimitive(jDBObject, buffer, position,ignoreName);
				position = jDBObject.position;
			}
			if(names!=null)jDBObject.setName(names);
		}
		jDBObject.position = position;
		return jDBObject;
	}
	
}
