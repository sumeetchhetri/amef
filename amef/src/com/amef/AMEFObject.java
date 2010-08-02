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
public class AMEFObject
{
	/*The Date type*/
	protected static final char DATE_TYPE = 'd';
	
	/*The string type*/
	protected static final char STRING_TYPE = 's';
	
	/*The boolean type*/
	protected static final char BOOLEAN_TYPE = 'b';
	
	/*The character type*/
	protected static final char CHAR_TYPE = 'c';
	
	/*The Number type*/
	protected static final char NUMBER_TYPE = 'n';
	
	/*The Object type*/
	protected static final char OBJECT_TYPE = 'o';
	
	/*The type of the Object can be string, number, date, boolean, character or any complex object*/
	private char type;
	
	/*The name of the Object if required can be used to represent object properties*/
	private String name;
	
	/*The Length of the Object value*/
	private int length;
	
	/*The Object value in String format*/
	private String value;
	
	/*The properties of a complex object*/
	private List<AMEFObject> packets;
	
	
	/**
	 * @return Array of AMEFObject	 
	 *  
	 */
	public AMEFObject[] getObjects()
	{
		return packets.toArray(new AMEFObject[packets.size()]);
	}
	
	/*Create a new AMEF object which will initilaize the values*/
	public AMEFObject()
	{
		type = OBJECT_TYPE;
		length = 0;
		name = "";
		packets = new ArrayList<AMEFObject>();
	}
	
	
	/**
	 * @param string
	 * @param name
	 * Add a String property to an Object
	 */
	protected void addPacket(String string,String name)
	{
		AMEFObject amefObject = addPacket(string);
		amefObject.name = name;
	}
	/**
	 * @param string
	 * Add a String property to an Object
	 */
	protected AMEFObject addPacket(String string)
	{
		AMEFObject amefObject = new AMEFObject();
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
	protected void addPacket(boolean bool,String name)
	{
		AMEFObject amefObject = addPacket(bool);
		amefObject.name = name;
	}
	/**
	 * @param bool
	 * Add a boolean property to an Object
	 */
	protected AMEFObject addPacket(boolean bool)
	{
		AMEFObject amefObject = new AMEFObject();
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
	 * @param lon
	 * @param name
	 * Add a long property to an Object
	 */
	protected void addPacket(long lon,String name)
	{
		AMEFObject amefObject = addPacket(lon);
		amefObject.name = name;
	}
	/**
	 * @param lon
	 * Add a long property to an Object
	 */
	protected AMEFObject addPacket(long lon)
	{
		AMEFObject amefObject = new AMEFObject();
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
	protected void addPacket(double doub,String name)
	{
		AMEFObject amefObject = addPacket(doub);
		amefObject.name = name;
	}
	/**
	 * @param doub
	 * Add a double property to an Object
	 */
	protected AMEFObject addPacket(double doub)
	{
		AMEFObject amefObject = new AMEFObject();
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
	protected void addPacket(int integer,String name)
	{
		AMEFObject amefObject = addPacket(integer);
		amefObject.name = name;
	}
	/**
	 * @param integer
	 * Add an integer property to an Object
	 */
	protected AMEFObject addPacket(int integer)
	{
		AMEFObject amefObject = new AMEFObject();
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
	protected void addPacket(Date date,String name)
	{
		AMEFObject amefObject = addPacket(date);
		amefObject.name = name;
	}
	/**
	 * @param date
	 * Add a Date property to an Object
	 */
	protected AMEFObject addPacket(Date date)
	{
		AMEFObject amefObject = new AMEFObject();
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
	protected void addPacket(AMEFObject packet)
	{
		packets.add(packet);
	}
	
	
	protected int getLength()
	{
		return length;
	}
	protected void setLength(int length)
	{
		this.length = length;
	}
	
	protected String getName()
	{
		return name;
	}
	protected void setName(String name)
	{
		this.name = name;
	}
	
	protected List<AMEFObject> getPackets()
	{
		return packets;
	}
	protected void setPackets(List<AMEFObject> packets)
	{
		this.packets = packets;
	}
	
	protected char getType()
	{
		return type;
	}
	protected void setType(char type)
	{
		this.type = type;
	}
	
	protected String getValue()
	{
		return value;
	}
	protected void setValue(String value)
	{
		this.value = value;
	}
	
	
	/**
	 * @return boolean value of this object if its type is boolean
	 */
	protected boolean getBooleanValue()
	{
		if(type=='b')
			return (value=="1"?true:false);
		else
			return false;
	}
	
	/**
	 * @return integer value of this object if its type is integer
	 */
	protected int getIntValue()
	{
		if(type=='n')
			return (Integer.valueOf(value));
		else
			return -1;
	}
	
	/**
	 * @return double value of this object if its type is double
	 */
	protected double getDoubleValue()
	{
		if(type=='n')
			return (Double.valueOf(value));
		else
			return -1;
	}
	
	/**
	 * @return long value of this object if its type is long
	 */
	protected long getLongValue()
	{
		if(type=='n')
			return (Long.valueOf(value));
		else
			return -1;
	}
	
	/**
	 * @return Date value of this object if its type is Date
	 */
	protected Date getDateValue()
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
}
