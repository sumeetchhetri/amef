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


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


/**
 * @author sumeet.chhetri
 * The Automated Message Exchange Format Object type
 * Is a wrapper for basic as well as complex object heirarchies
 * can be a string, number, date, boolean, character or any complex object
 * Every message consists of only one AMEFObjectNew *
 */
public final class AMEFObject
{
	public static final char NULL_STRING = 'a';
	
	public static final char NULL_NUMBER = 'g';
	
	public static final char NULL_DATE = 'j';
	
	public static final char NULL_FPN = 'k';
	
	public static final char NULL_BOOL = 'v';
	
	public static final char NULL_CHAR = 'z';
	
	/*The Date type*/
	public static final char DATE_TYPE = 'd';
	
	/*The 4GB string type*/
	public static final char STRING_TYPE = 's';
	
	/*The max 256 length string type*/
	public static final char STRING_256_TYPE = 't';
	
	/*The max 65536 length string type*/
	public static final char STRING_65536_TYPE = 'h';
	
	public static final char STRING_16777216_TYPE = 'y';
		
	/*The boolean type*/
	public static final char BOOLEAN_TYPE = 'b';
	
	/*The character type*/
	public static final char CHAR_TYPE = 'c';
	
	/*The Number types*/
	public static final char VERY_SMALL_INT_TYPE = 'n';
	
	public static final char SMALL_INT_TYPE = 'w';
	
	public static final char BIG_INT_TYPE = 'r';
	
	public static final char INT_TYPE = 'i';
	
	public static final char VS_LONG_INT_TYPE = 'f';
	
	public static final char S_LONG_INT_TYPE = 'x';
	
	public static final char B_LONG_INT_TYPE = 'e';
	
	public static final char LONG_INT_TYPE = 'l';
	
	public static final char DOUBLE_FLOAT_TYPE = 'u';
	
	/*The Object type*/
	public static final char VS_OBJECT_TYPE = 'm';
	
	/*The Object type*/
	public static final char S_OBJECT_TYPE = 'q';
	
	/*The Object type*/
	public static final char B_OBJECT_TYPE = 'p';
	
	/*The Object type*/
	public static final char OBJECT_TYPE = 'o';
	
	/*The type of the Object can be string, number, date, boolean, character or any complex object*/
	private char type;
	
	/*The name of the Object if required can be used to represent object properties*/
	private String name;
	
	/*The Length of the Object value*/
	private int length;
	
	/*The Length of the Object value*/
	private int namedLength = 2;
	
	/*The Object value in String format*/
	private byte[] value;
	
	/*The properties of a complex object*/
	private List<AMEFObject> packets;
	
	/**
	 * @return Array of AMEFObjectNew	 
	 *  
	 */
	
	public static char getEqvNullType(char type)
	{
		if(type==STRING_TYPE || type==STRING_256_TYPE || type==STRING_65536_TYPE || type==STRING_16777216_TYPE)
			return NULL_STRING;
		else if(type==VERY_SMALL_INT_TYPE || type==SMALL_INT_TYPE || type==BIG_INT_TYPE || type==S_OBJECT_TYPE
				|| type==VS_LONG_INT_TYPE || type==S_LONG_INT_TYPE || type==B_LONG_INT_TYPE || type==LONG_INT_TYPE)
			return NULL_NUMBER;
		else if(type==DATE_TYPE)
			return NULL_DATE;
		else if(type==BOOLEAN_TYPE)
			return NULL_BOOL;
		else if(type==CHAR_TYPE)
			return NULL_CHAR;
		else 
			return 0;
	}
	
	public AMEFObject[] getObjects()
	{
		return packets.toArray(new AMEFObject[packets.size()]);
	}
	
	public void clear()
	{
		this.packets.clear();
	}
	
	/*Create a new AMEF object which will initilaize the values*/
	public AMEFObject()
	{
		type = OBJECT_TYPE;
		length = 0;
		name = "";
		packets = new ArrayList<AMEFObject>();
	}
	
	public void addNullPacket(char type)
	{
		AMEFObject AMEFObjectNew = new AMEFObject();
		AMEFObjectNew.namedLength += 1;
		length += 1;
		namedLength += 3;
		AMEFObjectNew.type = type;
		packets.add(AMEFObjectNew);
	}
	
	public void addNullPacket(char type,String name)
	{
		AMEFObject AMEFObjectNew = new AMEFObject();
		AMEFObjectNew.name = name;		
		namedLength += name.length();
		AMEFObjectNew.namedLength += name.length() + 1;
		length += 1;
		namedLength += 3;
		AMEFObjectNew.type = type;
		packets.add(AMEFObjectNew);
	}
	
	/**
	 * @param string
	 * @param name
	 * Add a String property to an Object
	 */
	public void addPacket(String string,String name)
	{
		AMEFObject AMEFObjectNew = addPacket(string);
		AMEFObjectNew.name = name;
		//length += name.length();
		namedLength += name.length();
		AMEFObjectNew.namedLength += name.length();
	}
	/**
	 * @param string
	 * Add a String property to an Object
	 */
	public AMEFObject addPacket(String string)
	{
		AMEFObject AMEFObjectNew = new AMEFObject();		
		AMEFObjectNew.name = "";
		
		if(string.length()<=256)
		{
			AMEFObjectNew.type = STRING_256_TYPE;
			length += string.length() + 2;
			namedLength += string.length() + 4;
			AMEFObjectNew.namedLength = string.length() + 2;
		}
		else if(string.length()<=65536)
		{
			AMEFObjectNew.type = STRING_65536_TYPE;
			length += string.length() + 3;
			namedLength += string.length() + 5;
			AMEFObjectNew.namedLength = string.length() + 3;
		}
		else if(string.length()<=16777216)
		{
			AMEFObjectNew.type = STRING_16777216_TYPE;
			length += string.length() + 4;
			namedLength += string.length() + 6;
			AMEFObjectNew.namedLength = string.length() + 4;
		}
		else
		{
			AMEFObjectNew.type = STRING_TYPE;
			length += string.length() + 5;
			namedLength += string.length() + 7;
			AMEFObjectNew.namedLength = string.length() + 5;
		}
		AMEFObjectNew.length = string.length();		
		AMEFObjectNew.value = string.getBytes();
		packets.add(AMEFObjectNew);
		return AMEFObjectNew;
	}
	
	/**
	 * @param string
	 * @param name
	 * Add a String property to an Object
	 */
	public void addPacket(byte[] string,String name)
	{
		AMEFObject AMEFObjectNew = addPacket(string);
		AMEFObjectNew.name = name;
		//length += name.length();
		namedLength += name.length();
		AMEFObjectNew.namedLength += name.length();
	}
	/**
	 * @param string
	 * Add a String property to an Object
	 */
	public AMEFObject addPacket(byte[] string)
	{
		AMEFObject AMEFObjectNew = new AMEFObject();		
		AMEFObjectNew.name = "";
		
		if(string.length<=256)
		{
			AMEFObjectNew.type = STRING_256_TYPE;
			length += string.length + 2;
			namedLength += string.length + 4;
			AMEFObjectNew.namedLength = string.length + 2;
		}
		else if(string.length<=65536)
		{
			AMEFObjectNew.type = STRING_65536_TYPE;
			length += string.length + 3;
			namedLength += string.length + 5;
			AMEFObjectNew.namedLength = string.length + 3;
		}
		else if(string.length<=16777216)
		{
			AMEFObjectNew.type = STRING_16777216_TYPE;
			length += string.length + 4;
			namedLength += string.length + 6;
			AMEFObjectNew.namedLength = string.length + 4;
		}
		else
		{
			AMEFObjectNew.type = STRING_TYPE;
			length += string.length + 5;
			namedLength += string.length + 7;
			AMEFObjectNew.namedLength = string.length + 5;
		}
		AMEFObjectNew.length = string.length;		
		AMEFObjectNew.value = string;
		packets.add(AMEFObjectNew);
		return AMEFObjectNew;
	}
	
	/**
	 * @param bool
	 * @param name
	 * Add a boolean property to an Object
	 */
	public void addPacket(boolean bool,String name)
	{
		AMEFObject AMEFObjectNew = addPacket(bool);
		AMEFObjectNew.name = name;
		//length += name.length();
		namedLength += name.length();
		AMEFObjectNew.namedLength += name.length();
	}
	/**
	 * @param bool
	 * Add a boolean property to an Object
	 */
	public AMEFObject addPacket(boolean bool)
	{
		AMEFObject AMEFObjectNew = new AMEFObject();
		AMEFObjectNew.type = BOOLEAN_TYPE;
		AMEFObjectNew.name = "";
		AMEFObjectNew.length = 1;
		if(bool==true)
		{			
			AMEFObjectNew.value = new byte[]{'1'};
		}
		else
		{
			AMEFObjectNew.value = new byte[]{'0'};		
		}		
		packets.add(AMEFObjectNew);
		length += 2;
		namedLength += 4;
		AMEFObjectNew.namedLength = 2;
		return AMEFObjectNew;
	}
	
	public void addPacket(char chr,String name)
	{
		AMEFObject AMEFObjectNew = addPacket(chr);
		AMEFObjectNew.name = name;
		//length += name.length();
		namedLength += name.length();
		AMEFObjectNew.namedLength += name.length();
	}
	/**
	 * @param bool
	 * Add a boolean property to an Object
	 */
	public AMEFObject addPacket(char chr)
	{
		AMEFObject AMEFObjectNew = new AMEFObject();
		AMEFObjectNew.type = CHAR_TYPE;
		AMEFObjectNew.name = "";
		AMEFObjectNew.length = 1;
		AMEFObjectNew.value = new byte[]{(byte)chr};	
		packets.add(AMEFObjectNew);
		length += 2;
		namedLength += 4;
		AMEFObjectNew.namedLength = 2;
		return AMEFObjectNew;
	}
	
	/**
	 * @param lon
	 * @param name
	 * Add a long property to an Object
	 */
	public void addPacket(long lon,String name)
	{
		AMEFObject AMEFObjectNew = addPacket(lon);
		AMEFObjectNew.name = name;
		//length += name.length();
		namedLength += name.length();
		AMEFObjectNew.namedLength += name.length();
	}
	/**
	 * @param lon
	 * Add a long property to an Object
	 */
	public AMEFObject addPacket(long lon)
	{
		AMEFObject AMEFObjectNew = new AMEFObject();
		if(lon<256)
		{
			AMEFObjectNew.type = VERY_SMALL_INT_TYPE;
			AMEFObjectNew.value = AMEFResources.longToByteArray(lon, 1);
			length += 2;
			namedLength += 4;
			AMEFObjectNew.namedLength  = 2;
			AMEFObjectNew.length = 1;
		}
		else if(lon<65536)
		{
			AMEFObjectNew.type = SMALL_INT_TYPE;
			AMEFObjectNew.value = AMEFResources.longToByteArray(lon, 2);
			length += 3;
			namedLength += 5;
			AMEFObjectNew.namedLength  = 3;
			AMEFObjectNew.length = 2;
		}
		else if(lon<16777216)
		{
			AMEFObjectNew.type = BIG_INT_TYPE;
			AMEFObjectNew.value = AMEFResources.longToByteArray(lon, 3);
			length += 4;
			namedLength += 6;
			AMEFObjectNew.namedLength  = 4;
			AMEFObjectNew.length = 3;
		}
		else if(lon<4294967296L)
		{
			AMEFObjectNew.type = INT_TYPE;
			AMEFObjectNew.value = AMEFResources.longToByteArray(lon, 4);
			length += 5;
			namedLength += 7;
			AMEFObjectNew.namedLength  = 5;
			AMEFObjectNew.length = 4;
		}
		else if(lon<1099511627776L)
		{
			AMEFObjectNew.type = VS_LONG_INT_TYPE;
			AMEFObjectNew.value = AMEFResources.longToByteArray(lon, 5);
			length += 6;
			namedLength += 8;
			AMEFObjectNew.namedLength  = 6;
			AMEFObjectNew.length = 5;
		}
		else if(lon<281474976710656L)
		{
			AMEFObjectNew.type = S_LONG_INT_TYPE;
			AMEFObjectNew.value = AMEFResources.longToByteArray(lon, 6);
			length += 7;
			namedLength += 9;
			AMEFObjectNew.namedLength  = 7;
			AMEFObjectNew.length = 6;
		}
		else if(lon<72057594037927936L)
		{
			AMEFObjectNew.type = B_LONG_INT_TYPE;
			AMEFObjectNew.value = AMEFResources.longToByteArray(lon, 7);
			length += 8;
			namedLength += 10;
			AMEFObjectNew.namedLength  = 8;
			AMEFObjectNew.length = 7;
		}
		else
		{
			AMEFObjectNew.type = LONG_INT_TYPE;
			AMEFObjectNew.value = AMEFResources.longToByteArray(lon, 8);
			length += 9;
			namedLength += 11;
			AMEFObjectNew.namedLength  = 9;
			AMEFObjectNew.length = 8;
		}
		AMEFObjectNew.name = "";		
		packets.add(AMEFObjectNew);		
		return AMEFObjectNew;
	}
	
	/**
	 * @param doub
	 * @param name
	 * Add a double property to an Object
	 */
	public void addPacket(float doub,String name)
	{
		AMEFObject AMEFObjectNew = addPacket(doub);
		AMEFObjectNew.name = name;
		//length += name.length();
		namedLength += name.length();
		AMEFObjectNew.namedLength += name.length();
	}
	/**
	 * @param doub
	 * Add a double property to an Object
	 */
	public AMEFObject addPacket(float doub)
	{
		AMEFObject AMEFObjectNew = new AMEFObject();
		AMEFObjectNew.type = DOUBLE_FLOAT_TYPE;
		AMEFObjectNew.name = "";
		AMEFObjectNew.length = String.valueOf(doub).length();
		AMEFObjectNew.value = String.valueOf(doub).getBytes();
		packets.add(AMEFObjectNew);
		length += AMEFObjectNew.value.length + 2;
		namedLength += AMEFObjectNew.value.length + 4;
		AMEFObjectNew.namedLength = AMEFObjectNew.value.length + 2;
		return AMEFObjectNew;
	}
	
	
	/**
	 * @param doub
	 * @param name
	 * Add a double property to an Object
	 */
	public void addPacket(double doub,String name)
	{
		AMEFObject AMEFObjectNew = addPacket(doub);
		AMEFObjectNew.name = name;
		//length += name.length();
		namedLength += name.length();
		AMEFObjectNew.namedLength += name.length();
	}
	/**
	 * @param doub
	 * Add a double property to an Object
	 */
	public AMEFObject addPacket(double doub)
	{
		AMEFObject AMEFObjectNew = new AMEFObject();
		AMEFObjectNew.type = DOUBLE_FLOAT_TYPE;
		AMEFObjectNew.name = "";
		AMEFObjectNew.length = String.valueOf(doub).length();
		AMEFObjectNew.value = String.valueOf(doub).getBytes();
		packets.add(AMEFObjectNew);
		length += AMEFObjectNew.value.length + 2;
		namedLength += AMEFObjectNew.value.length + 4;
		AMEFObjectNew.namedLength = AMEFObjectNew.value.length + 2;
		return AMEFObjectNew;
	}
	
	/**
	 * @param integer
	 * @param name
	 * Add an integer property to an Object
	 */
	public void addPacket(int integer,String name)
	{
		AMEFObject AMEFObjectNew = addPacket(integer);
		AMEFObjectNew.name = name;
		//length += name.length();
		namedLength += name.length();
		AMEFObjectNew.namedLength += name.length();
	}
	/**
	 * @param integer
	 * Add an integer property to an Object
	 */
	public AMEFObject addPacket(int integer)
	{
		AMEFObject AMEFObjectNew = new AMEFObject();
		if(integer<256)
		{
			AMEFObjectNew.type = VERY_SMALL_INT_TYPE;
			AMEFObjectNew.value = AMEFResources.intToByteArray(integer, 1);
			length += 2;
			namedLength += 4;
			AMEFObjectNew.namedLength  = 2;
			AMEFObjectNew.length = 1;
		}
		else if(integer<65536)
		{
			AMEFObjectNew.type = SMALL_INT_TYPE;
			AMEFObjectNew.value = AMEFResources.intToByteArray(integer, 2);
			length += 3;
			namedLength += 5;
			AMEFObjectNew.namedLength  = 3;
			AMEFObjectNew.length = 2;
		}
		else if(integer<16777216)
		{
			AMEFObjectNew.type = BIG_INT_TYPE;
			AMEFObjectNew.value = AMEFResources.intToByteArray(integer, 3);
			length += 4;
			namedLength += 6;
			AMEFObjectNew.namedLength  = 4;
			AMEFObjectNew.length = 3;
		}
		else
		{
			AMEFObjectNew.type = INT_TYPE;
			AMEFObjectNew.value = AMEFResources.intToByteArray(integer, 4);
			length += 5;
			namedLength += 7;
			AMEFObjectNew.namedLength  = 5;
			AMEFObjectNew.length = 4;
		}
		AMEFObjectNew.name = "";
		packets.add(AMEFObjectNew);		
		return AMEFObjectNew;
	}
	
	/**
	 * @param date
	 * @param name
	 * Add a Date property to an Object
	 */
	public void addPacket(Date date,String name)
	{
		AMEFObject AMEFObjectNew = addPacket(date);
		AMEFObjectNew.name = name;
		namedLength += name.length();
		AMEFObjectNew.namedLength += name.length();
	}
	/**
	 * @param date
	 * Add a Date property to an Object
	 */
	public AMEFObject addPacket(Date date)
	{
		AMEFObject AMEFObjectNew = new AMEFObject();
		AMEFObjectNew.type = DATE_TYPE;
		AMEFObjectNew.name = "";
		SimpleDateFormat format = new SimpleDateFormat("ddMMyyyy HHmmss");
		AMEFObjectNew.length = 15;
		AMEFObjectNew.value = format.format(date).getBytes();
		AMEFObjectNew.namedLength += AMEFObjectNew.value.length;
		length += AMEFObjectNew.value.length + 2;
		namedLength += AMEFObjectNew.value.length + 4;
		AMEFObjectNew.namedLength  = AMEFObjectNew.value.length + 2;
		AMEFObjectNew.length = AMEFObjectNew.value.length;
		packets.add(AMEFObjectNew);
		return AMEFObjectNew;
	}
	
	/**
	 * @param packet
	 * Add a AMEFObjectNew property to an Object
	 */
	public void addPacket(AMEFObject packet)
	{
		packets.add(packet);
		if(packet.type==OBJECT_TYPE)
		{
			if(packet.length+1<256)
				packet.type = VS_OBJECT_TYPE;
			else if(packet.length+1<65536)
				packet.type = S_OBJECT_TYPE;
			else if(packet.length+1<16777216)
				packet.type = B_OBJECT_TYPE;
			else
				packet.type = OBJECT_TYPE;
		}
		length += packet.getLength();
		namedLength += packet.getNamedLength(false);
	}
	
	
	public void set(int i,AMEFObject jdbo)
	{
		packets.set(i,jdbo); 
	}
	
	/**
	 * @param packet
	 * Add a AMEFObjectNew property to an Object
	 */
	public void addPacket(byte[] packet,char type)
	{
		if(type==STRING_TYPE || type==DATE_TYPE || type==STRING_256_TYPE || type==STRING_65536_TYPE || type==STRING_16777216_TYPE)
		{
			addPacket(packet);
		}
		else if(type==VERY_SMALL_INT_TYPE || type==SMALL_INT_TYPE || type==BIG_INT_TYPE || type==INT_TYPE)
		{
			addPacket(AMEFResources.byteArrayToInt(packet));
		}
		else if(type==VS_LONG_INT_TYPE || type==S_LONG_INT_TYPE || type==B_LONG_INT_TYPE || type==LONG_INT_TYPE)
		{
			addPacket(AMEFResources.byteArrayToLong(packet));
		}
		else if(type==DOUBLE_FLOAT_TYPE)
		{
			addPacket(Double.parseDouble(new String(packet)));
		}
		else if(type==BOOLEAN_TYPE)
		{
			addPacket(packet[0]=='1'?true:false);
		}
		else if(type==CHAR_TYPE)
		{
			addPacket((char)packet[0]);
		}
		else if(type==NULL_STRING || type==NULL_NUMBER || type==NULL_DATE || type==NULL_FPN || type==NULL_BOOL || type==NULL_CHAR)
		{
			addNullPacket(type);
		}
	}
	
	public void addPacket(Object obj)
	{
		if(obj instanceof Long)
			addPacket(((Long)obj).longValue());
		else if(obj instanceof Double)
			addPacket(((Double)obj).doubleValue());
		else if(obj instanceof String)
			addPacket((String)obj);
		else if(obj instanceof Byte)
			addPacket(((Byte)obj).byteValue());
	}
	
	public int getlength()
	{
		return length;
	}
	
	public int getLength()
	{
		if(type==VS_OBJECT_TYPE)
		{
			return 2 + length;
		}
		else if(type==S_OBJECT_TYPE)
		{
			return 3 + length;
		}
		else if(type==B_OBJECT_TYPE)
		{
			return 4 + length;
		}
		else if(type==OBJECT_TYPE)
		{
			return 5 + length;
		}
		else
			return length;
	}
	
	
	public static boolean isString(char type)
	{
		if(type==STRING_256_TYPE || type==STRING_65536_TYPE || 
				type==STRING_16777216_TYPE || type==STRING_TYPE || type==NULL_STRING)
			return true;
		return false;		
	}
	
	public static boolean isFloatingPoint(char type)
	{
		if(type==DOUBLE_FLOAT_TYPE || type==NULL_FPN)
			return true;
		return false;		
	}
	
	public static boolean isNumber(char type)
	{
		if(type==VERY_SMALL_INT_TYPE || type==SMALL_INT_TYPE || type==BIG_INT_TYPE || type==INT_TYPE
			|| type==S_LONG_INT_TYPE || type==B_LONG_INT_TYPE || type==VS_LONG_INT_TYPE || type==LONG_INT_TYPE || type==NULL_NUMBER)
			return true;	
		return false;
	}
	
	public static boolean isInteger(char type)
	{
		if(type==VERY_SMALL_INT_TYPE || type==SMALL_INT_TYPE || type==BIG_INT_TYPE || type==INT_TYPE)
			return true;	
		return false;
	}
	
	public static boolean isLong(char type)
	{
		if(type==VERY_SMALL_INT_TYPE || type==SMALL_INT_TYPE || type==BIG_INT_TYPE || type==INT_TYPE
				|| type==S_LONG_INT_TYPE || type==B_LONG_INT_TYPE || type==VS_LONG_INT_TYPE || type==LONG_INT_TYPE)
			return true;	
		return false;
	}
	
	public static boolean isChar(char type)
	{
		if(type==CHAR_TYPE ||  type==NULL_CHAR)
			return true;	
		return false;
	}
	
	public static boolean isBoolean(char type)
	{
		if(type==BOOLEAN_TYPE || type==NULL_BOOL)
			return true;	
		return false;
	}
	
	public static boolean isDate(char type)
	{
		if(type==DATE_TYPE || type==NULL_DATE)
			return true;
		return false;
	}
	
	public boolean isString()
	{
		return isString(type);	
	}
	
	public boolean isFloatingPoint()
	{
		return isFloatingPoint(type);		
	}
	
	public boolean isNumber()
	{
		return isNumber(type);
	}
	
	public boolean isChar()
	{
		return isChar(type);
	}
	
	public boolean isDate()
	{
		return isDate(type);
	}
	
	public int getNamedLength(boolean ignoreName)
	{
		if(ignoreName)
		{
			if(getType()==OBJECT_TYPE)
			{
				if(length<256)
					type = VS_OBJECT_TYPE;
				else if(length<65536)
					type = S_OBJECT_TYPE;
				else if(length<16777216)
					type = B_OBJECT_TYPE;
				else
					type = OBJECT_TYPE;
				return getLength();
			}
			else
			{
				int len = length;
				if(getType()!=VERY_SMALL_INT_TYPE && getType()!=SMALL_INT_TYPE && getType()!=BIG_INT_TYPE 
					&& getType()!=INT_TYPE && getType()!=VS_LONG_INT_TYPE && getType()!=S_LONG_INT_TYPE 
						&& getType()!=B_LONG_INT_TYPE && getType()!=LONG_INT_TYPE && getType()!=BOOLEAN_TYPE
							&& getType()!=CHAR_TYPE)
				{
					len++;
				}
				if(getType()==NULL_STRING || getType()==NULL_NUMBER
					|| getType()==NULL_DATE || getType()==NULL_BOOL || getType()==NULL_CHAR)
					return len;
				if(length<256)
					len++;
				else if(length<65536)
					len+=2;
				else if(length<16777216)
					len+=3;
				else
					len+=4;
				return len;
			}
		}
		else
		{
			if(getType()==OBJECT_TYPE)
			{
				if(2 + namedLength<256)
				{
					type = VS_OBJECT_TYPE;
					namedLength += 2;
				}
				else if(2 + namedLength<65536)
				{
					type = S_OBJECT_TYPE;
					namedLength += 3;
				}
				else if(2 + namedLength<16777216)
				{
					type = B_OBJECT_TYPE;
					namedLength += 4;
				}
				else
				{
					type = OBJECT_TYPE;
					namedLength += 5;
				}
				return namedLength;
			}
			else if(getType()==VS_OBJECT_TYPE || getType()==B_OBJECT_TYPE || getType()==S_OBJECT_TYPE)
			{
				return namedLength;
			}
			else
			{
				return 2 + namedLength;
			}
		}
		
	}
	
	public void setLength(int length)
	{
		this.length = length;
	}
	
	public byte[] getName()
	{
		return name.getBytes();
	}
	public String getNameStr()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public void setName(byte[] name)
	{
		this.name = new String(name);
	}
	public List<AMEFObject> getPackets()
	{
		return packets;
	}
	public void setPackets(List<AMEFObject> packets)
	{
		this.packets = packets;
	}
	
	public char getType()
	{
		return type;
	}
	public void setType(char type)
	{
		this.type = type;
	}
	
	public byte[] getValue()
	{
		return value;
	}
	public Object getTValue()
	{
		if(type==STRING_TYPE || type==STRING_256_TYPE || type==DATE_TYPE || type==STRING_65536_TYPE || type==STRING_16777216_TYPE || type==DOUBLE_FLOAT_TYPE)
			return value;
		else if(getType()!=VERY_SMALL_INT_TYPE && getType()!=SMALL_INT_TYPE && getType()!=BIG_INT_TYPE 
				&& getType()!=INT_TYPE)
		{
			return getIntValue();
		}
		else if(getType()!=VS_LONG_INT_TYPE && getType()!=S_LONG_INT_TYPE 
					&& getType()!=B_LONG_INT_TYPE && getType()!=LONG_INT_TYPE)
		{
			return getLongValue();
		}
		else if(getType()!=BOOLEAN_TYPE)
		{
			return getBooleanValue();
		}
		else if(getType()!=CHAR_TYPE)
		{
			return (char)value[0];
		}
		return this;
	}
	public String getValueStr()
	{
		if(value==null)
			return null;
		return new String(value);
	}
	public void setValue(byte[] value)
	{
		this.value = value;
	}
	public void setValue(String value)
	{
		this.value = value.getBytes();
	}
	
	
	/**
	 * @return boolean value of this object if its type is boolean
	 */
	public boolean getBooleanValue()
	{
		if(type==BOOLEAN_TYPE)
			return (value.equals("1")?true:false);
		else
			return false;
	}
	
	/**
	 * @return integer value of this object if its type is integer
	 */
	public int getIntValue()
	{
		if(type==VERY_SMALL_INT_TYPE || type==SMALL_INT_TYPE || type==BIG_INT_TYPE || type==INT_TYPE)
		{
			return AMEFResources.byteArrayToInt(value);
		}
		else
			return -1;
	}
	
	/**
	 * @return double value of this object if its type is double
	 */
	public double getDoubleValue()
	{
		if(type==DOUBLE_FLOAT_TYPE)
			return (Double.valueOf(new String(value)));
		else
			return -1;
	}
	
	/**
	 * @return long value of this object if its type is long
	 */
	public long getLongValue()
	{
		if(type==VS_LONG_INT_TYPE || type==S_LONG_INT_TYPE || type==B_LONG_INT_TYPE || type==LONG_INT_TYPE)
		{
			return AMEFResources.byteArrayToLong(value);
		}
		else
			return -1;
	}
	
	public long getNumericValue()
	{
		if(type==VS_LONG_INT_TYPE || type==S_LONG_INT_TYPE || type==B_LONG_INT_TYPE || type==LONG_INT_TYPE
			|| type==VERY_SMALL_INT_TYPE || type==SMALL_INT_TYPE || type==BIG_INT_TYPE || type==INT_TYPE)
		{
			return AMEFResources.byteArrayToLong(value);
		}
		else
			return -1;
	}
	
	/**
	 * @return Date value of this object if its type is Date
	 */
	public Date getDateValue()
	{
		if(type==BOOLEAN_TYPE) 
		{
			try
			{
				return new SimpleDateFormat("ddMMyyyy HHmmss").parse(new String(value));
			}
			catch (ParseException e)
			{
				return new Date();
			}
		}
		else
			return new Date();
	}
	
	public String toString()
	{
		return displayObject("");
	}
	
	private String displayObject(String tab)
	{
		String displ = "";
		for (int i=0;i<(int)getPackets().size();i++)
		{
			AMEFObject obj = getPackets().get(i);		
			displ += tab + "Object Type = ";
			displ += obj.type;
			displ += "\n" + tab + "Object Name = " + obj.name + "\n";
			displ += tab + "Object Value = ";
			if(obj.isString() || obj.isFloatingPoint() || obj.isDate())
				displ += obj.getValueStr() + "\n";
			else if(obj.isChar())
			{
				if(type==BOOLEAN_TYPE)
					displ += obj.getBooleanValue() + "\n";
				else
					displ += (char)obj.value[0] + "\n";
			}
			else if(obj.isNumber())
			{
				displ += obj.getNumericValue() + "\n";
			}
			if(obj.type==OBJECT_TYPE || obj.type==B_OBJECT_TYPE || obj.type==S_OBJECT_TYPE || obj.type==VS_OBJECT_TYPE)
			{
				displ += obj.displayObject(tab+"\t");
			}
		}
		return displ;
	}

	@Override
	public int hashCode()
	{
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((name == null) ? 0 : name.hashCode());
		result = PRIME * result + ((packets == null) ? 0 : packets.hashCode());
		result = PRIME * result + type;
		result = PRIME * result + Arrays.hashCode(value);
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		final AMEFObject other = (AMEFObject) obj;
		if (name == null)
		{
			if (other.name != null) return false;
		}
		else if (!name.equals(other.name)) return false;
		if (packets == null)
		{
			if (other.packets != null) return false;
		}
		else if (!packets.equals(other.packets)) return false;
		if (type != other.type) return false;
		if (!Arrays.equals(value, other.value)) return false;
		return true;
	}

	public void addStaticPacket(AMEFObject obj)
	{
		packets.add(obj);
	}
	
	public int compare(AMEFObject obj)
	{
		if(value==null && obj.value!=null)
			return -1;
		else if(value!=null && obj.value==null)
			return 1;
		else if(value==null && obj.value==null)
			return 0;
		else if(isString())
		{
			return getValueStr().compareTo(obj.getValueStr());
		}
		else if(isNumber())
		{
			return new Long(getNumericValue()).
						compareTo(new Long(obj.getNumericValue()));
		}
		else if(isChar())
		{
			return new Character((char)getValue()[0]).
						compareTo(new Character((char)obj.getValue()[0]));
		}
		else if(isFloatingPoint())
		{
			return getValueStr().compareTo(obj.getValueStr());
		}
		else if(isDate())
		{
			return getDateValue().compareTo(obj.getDateValue());
		}
		else
			return 0;
	}
	
	public boolean isNullString()
	{
		return type==NULL_STRING;
	}
	
	public boolean isNullNumber()
	{
		return type==NULL_NUMBER;
	}
	
	public boolean isNullFPN()
	{
		return type==NULL_FPN;
	}
	
	public boolean isNullDate()
	{
		return type==NULL_DATE;
	}
	
	public boolean isNullChar()
	{
		return type==NULL_CHAR;
	}
	
}
