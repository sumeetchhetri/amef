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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author sumeet.chhetri
 * The Automated Message Exchange Format Object type
 * Is a wrapper for basic as well as complex object heirarchies
 * can be a string, number, date, boolean, character or any complex object
 * Every message consists of only one AMEFObject *
 */
public class AMEFObjectOld
{
	/*The Date type*/
	public static final char DATE_TYPE = 'd';
	
	/*The string type*/
	public static final char STRING_TYPE = 's';
	
	/*The boolean type*/
	public static final char BOOLEAN_TYPE = 'b';
	
	/*The character type*/
	public static final char CHAR_TYPE = 'c';
	
	/*The Number type*/
	public static final char NUMBER_TYPE = 'n';
	
	/*The Object type*/
	public static final char OBJECT_TYPE = 'o';
	
	/*The type of the Object can be string, number, date, boolean, character or any complex object*/
	private char type;
	
	/*The name of the Object if required can be used to represent object properties*/
	private String name;
	
	/*The Length of the Object value*/
	private int length;
	
	/*The Object value in String format*/
	private String value;
	
	/*The properties of a complex object*/
	private List<AMEFObjectOld> packets;
	
	
	/**
	 * @return Array of AMEFObject	 
	 *  
	 */
	public AMEFObjectOld[] getObjects()
	{
		return packets.toArray(new AMEFObjectOld[packets.size()]);
	}
	
	/*Create a new AMEF object which will initilaize the values*/
	public AMEFObjectOld()
	{
		type = OBJECT_TYPE;
		length = 0;
		name = "";
		packets = new ArrayList<AMEFObjectOld>();
	}
	
	
	/**
	 * @param string
	 * @param name
	 * Add a String property to an Object
	 */
	public void addPacket(String string,String name)
	{
		AMEFObjectOld amefObject = addPacket(string);
		amefObject.name = name;
	}
	/**
	 * @param string
	 * Add a String property to an Object
	 */
	public AMEFObjectOld addPacket(String string)
	{
		AMEFObjectOld amefObject = new AMEFObjectOld();
		amefObject.type = STRING_TYPE;
		amefObject.name = "";
		amefObject.length = string.length();
		amefObject.value = string;
		packets.add(amefObject);
		return amefObject;
	}
	
	/**
	 * @param bool
	 * @param name
	 * Add a boolean property to an Object
	 */
	public void addPacket(boolean bool,String name)
	{
		AMEFObjectOld amefObject = addPacket(bool);
		amefObject.name = name;
	}
	/**
	 * @param bool
	 * Add a boolean property to an Object
	 */
	public AMEFObjectOld addPacket(boolean bool)
	{
		AMEFObjectOld amefObject = new AMEFObjectOld();
		amefObject.type = BOOLEAN_TYPE;
		amefObject.name = "";
		amefObject.length = 1;
		if(bool==true)
		{			
			amefObject.value = "1";
		}
		else
		{
			amefObject.value = "0";		
		}		
		packets.add(amefObject);
		return amefObject;
	}
	
	/**
	 * @param char
	 * @param name
	 * Add a char property to an Object
	 */
	public void addPacket(char chr,String name)
	{
		AMEFObjectOld amefObject = addPacket(chr);
		amefObject.name = name;
	}
	/**
	 * @param char
	 * Add a char property to an Object
	 */
	public AMEFObjectOld addPacket(char chr)
	{
		AMEFObjectOld amefObject = new AMEFObjectOld();
		amefObject.type = CHAR_TYPE;
		amefObject.name = "";
		amefObject.length = 1;
		amefObject.value = String.copyValueOf(new char[]{chr});	
		packets.add(amefObject);
		return amefObject;
	}
	
	/**
	 * @param lon
	 * @param name
	 * Add a long property to an Object
	 */
	public void addPacket(long lon,String name)
	{
		AMEFObjectOld amefObject = addPacket(lon);
		amefObject.name = name;
	}
	/**
	 * @param lon
	 * Add a long property to an Object
	 */
	public AMEFObjectOld addPacket(long lon)
	{
		AMEFObjectOld amefObject = new AMEFObjectOld();
		amefObject.type = NUMBER_TYPE;
		amefObject.name = "";
		amefObject.length = String.valueOf(lon).length();
		amefObject.value = String.valueOf(lon);
		packets.add(amefObject);
		return amefObject;
	}
	
	/**
	 * @param doub
	 * @param name
	 * Add a double property to an Object
	 */
	public void addPacket(double doub,String name)
	{
		AMEFObjectOld amefObject = addPacket(doub);
		amefObject.name = name;
	}
	/**
	 * @param doub
	 * Add a double property to an Object
	 */
	public AMEFObjectOld addPacket(double doub)
	{
		AMEFObjectOld amefObject = new AMEFObjectOld();
		amefObject.type = NUMBER_TYPE;
		amefObject.name = "";
		amefObject.length = String.valueOf(doub).length();
		amefObject.value = String.valueOf(doub);
		packets.add(amefObject);
		return amefObject;
	}
	
	/**
	 * @param integer
	 * @param name
	 * Add an integer property to an Object
	 */
	public void addPacket(int integer,String name)
	{
		AMEFObjectOld amefObject = addPacket(integer);
		amefObject.name = name;
	}
	/**
	 * @param integer
	 * Add an integer property to an Object
	 */
	public AMEFObjectOld addPacket(int integer)
	{
		AMEFObjectOld amefObject = new AMEFObjectOld();
		amefObject.type = NUMBER_TYPE;
		amefObject.name = "";
		amefObject.length = String.valueOf(integer).length();
		amefObject.value = String.valueOf(integer);
		packets.add(amefObject);
		return amefObject;
	}
	
	/**
	 * @param date
	 * @param name
	 * Add a Date property to an Object
	 */
	public void addPacket(Date date,String name)
	{
		AMEFObjectOld amefObject = addPacket(date);
		amefObject.name = name;
	}
	/**
	 * @param date
	 * Add a Date property to an Object
	 */
	public AMEFObjectOld addPacket(Date date)
	{
		AMEFObjectOld amefObject = new AMEFObjectOld();
		amefObject.type = DATE_TYPE;
		amefObject.name = "";
		SimpleDateFormat format = new SimpleDateFormat("ddMMyyyy HHmmss");
		amefObject.length = 15;
		amefObject.value = format.format(date);
		packets.add(amefObject);
		return amefObject;
	}
	
	/**
	 * @param packet
	 * Add a AMEFObject property to an Object
	 */
	public void addPacket(AMEFObjectOld packet)
	{
		packets.add(packet);
	}
	
	
	public int getLength()
	{
		return length;
	}
	public void setLength(int length)
	{
		this.length = length;
	}
	
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	
	public List<AMEFObjectOld> getPackets()
	{
		return packets;
	}
	public void setPackets(List<AMEFObjectOld> packets)
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
	
	public String getValue()
	{
		return value;
	}
	public void setValue(String value)
	{
		this.value = value;
	}
	
	
	/**
	 * @return boolean value of this object if its type is boolean
	 */
	public boolean getBooleanValue()
	{
		if(type=='b')
			return (value=="1"?true:false);
		else
			return false;
	}
	
	/**
	 * @return integer value of this object if its type is integer
	 */
	public int getIntValue()
	{
		if(type=='n')
			return (Integer.valueOf(value));
		else
			return -1;
	}
	
	/**
	 * @return double value of this object if its type is double
	 */
	public double getDoubleValue()
	{
		if(type=='n')
			return (Double.valueOf(value));
		else
			return -1;
	}
	
	/**
	 * @return long value of this object if its type is long
	 */
	public long getLongValue()
	{
		if(type=='n')
			return (Long.valueOf(value));
		else
			return -1;
	}
	
	/**
	 * @return Date value of this object if its type is Date
	 */
	public Date getDateValue()
	{
		if(type=='b') 
		{
			try
			{
				return new SimpleDateFormat("ddMMyyyy HHmmss").parse(value);
			}
			catch (ParseException e)
			{
				return new Date();
			}
		}
		else
			return new Date();
	}
	
	/**
	 * @return String representation of this object
	 */
	public String toString()
	{
		return displayObject("");
	}
	
	/**
	 * @return String representation of this object
	 */
	private String displayObject(String tab)
	{
		String displ = "";
		for (int i=0;i<(int)getPackets().size();i++)
		{
			AMEFObjectOld obj = getPackets().get(i);		
			displ += tab + "Object Type = ";
			displ += obj.type;
			displ += "\n" + tab + "Object Name = " + obj.name + "\n";
			displ += tab + "Object Length = ";
			displ += obj.length;
			displ += "\n" + tab + "Object Value = " + obj.value + "\n";
			if(obj.type=='o')
			{
				displ += obj.displayObject("\t");
			}
		}
		return displ;
	}
}
