/*
	Copyright 2010, Sumeet Chhetri 
  
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


/**
 * @author sumeetc
 * The AMEFDecoder class
 * providses the decode method to get the AMEFObject from its transmission form
 */
public class AMEFDecoderOld
{
	private String tempVal = "";
	
	
	/**
	 * @param data
	 * @param considerLength
	 * @return AMEFObject
	 * @throws AMEFDecodeException
	 * decode the bytestream to give the equivalent AMEFObject
	 */
	public AMEFObjectOld decode(byte[] data,boolean considerLength,boolean ignoreName) throws AMEFDecodeException
	{
		int startWith = 0;		
		if(considerLength)
			startWith = 4;
		String strdata = new String(data);
		strdata = strdata.substring(startWith);
		AMEFObjectOld amefObject = decodeSinglePacket(strdata,ignoreName);		
		return amefObject;
	}
	
	/**
	 * @param strdata
	 * @return AMEFObject
	 * @throws AMEFDecodeException
	 * decode the string to give the equivalent AMEFObject
	 */
	private AMEFObjectOld decodeSinglePacket(String strdata,boolean ignoreName) throws AMEFDecodeException
	{
		AMEFObjectOld amefObject = null;
		char type = strdata.charAt(0);
		if(type==AMEFObjectOld.STRING_TYPE)
		{
			if(strdata.charAt(1)==',')
			{
				amefObject = new AMEFObjectOld();
				amefObject.setType(type);
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
				amefObject.setName(name);
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
				int lengthm = ((length.charAt(0) & 0xff) << 24) | ((length.charAt(1) & 0xff) << 16)
								| ((length.charAt(2) & 0xff) << 8) | ((length.charAt(3) & 0xff));
				amefObject.setLength(lengthm);
				String value = strdata.substring(pos+1,pos+lengthm+1);
				amefObject.setValue(value);
				tempVal = strdata.substring(pos+lengthm+1);
			}
			else
			{
				throw new AMEFDecodeException("Invalid character after type specifier, expected ,");
			}
		}
		else if(type==AMEFObjectOld.BOOLEAN_TYPE)
		{
			if(strdata.charAt(1)==',')
			{
				amefObject = new AMEFObjectOld();
				amefObject.setType(type);
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
				amefObject.setName(name);
				if(pos>=strdata.length())
				{
					throw new AMEFDecodeException("Reached end of AMEF string, not found ,");
				}
				amefObject.setLength(1);
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
				amefObject.setValue(value);				
			}
			else
			{
				throw new AMEFDecodeException("Invalid character after type specifier, expected ,");
			}
		}
		else if(type==AMEFObjectOld.CHAR_TYPE)
		{
			if(strdata.charAt(1)==',')
			{
				amefObject = new AMEFObjectOld();
				amefObject.setType(type);
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
				amefObject.setName(name);
				if(pos>=strdata.length())
				{
					throw new AMEFDecodeException("Reached end of AMEF string, not found ,");
				}
				amefObject.setLength(1);
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
				amefObject.setValue(value);	
			}
			else
			{
				throw new AMEFDecodeException("Invalid character after type specifier, expected ,");
			}
		}
		else if(type==AMEFObjectOld.NUMBER_TYPE)
		{
			if(strdata.charAt(1)==',')
			{
				amefObject = new AMEFObjectOld();
				amefObject.setType(type);
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
				amefObject.setName(name);
				String length = "";
				if(!ignoreName)pos++;
				while(strdata.charAt(pos)!=',')
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
				int lengthm = Integer.parseInt(length);			
				amefObject.setLength(lengthm);
				String value = strdata.substring(pos+1,pos+lengthm+1);
				amefObject.setValue(value);
				tempVal = strdata.substring(pos+lengthm+1);
			}
			else
			{
				throw new AMEFDecodeException("Invalid character after type specifier, expected ,");
			}
		}
		else if(type==AMEFObjectOld.DATE_TYPE)
		{
			if(strdata.charAt(1)==',')
			{
				amefObject = new AMEFObjectOld();
				amefObject.setType(type);
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
				amefObject.setName(name);
				String length = "";
				if(!ignoreName)pos++;
				while(strdata.charAt(pos)!=',')
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
				int lengthm = Integer.parseInt(length);
				amefObject.setLength(lengthm);
				String value = strdata.substring(pos+1,pos+lengthm+1);
				amefObject.setValue(value);
				tempVal = strdata.substring(pos+lengthm+1);
			}
			else
			{
				throw new AMEFDecodeException("Invalid character after type specifier, expected ,");
			}
		}
		else if(type==AMEFObjectOld.OBJECT_TYPE)
		{
			if(strdata.charAt(1)==',')
			{
				amefObject = new AMEFObjectOld();
				amefObject.setType(type);
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
				amefObject.setName(name);
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
				int lengthm = ((length.charAt(0) & 0xff) << 24) | ((length.charAt(1) & 0xff) << 16)
								| ((length.charAt(2) & 0xff) << 8) | ((length.charAt(3) & 0xff));
				amefObject.setLength(lengthm);
				String value = strdata.substring(pos+1,pos+lengthm+1);
				tempVal = value;
				while(!tempVal.equals(""))
				{
					AMEFObjectOld obj = decodeSinglePacket(tempVal,ignoreName);
					amefObject.addPacket(obj);
				}				
				tempVal = strdata.substring(pos+lengthm+1);
			}
			else
			{
				throw new AMEFDecodeException("Invalid character after type specifier, expected ,");
			}
		}
		return amefObject;
	}
}
