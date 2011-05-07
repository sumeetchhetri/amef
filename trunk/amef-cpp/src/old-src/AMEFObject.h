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

#ifndef AMEFOBJECT_H
#define AMEFOBJECT_H
#include <iostream>
#include "vector"
#include "ConversionUtil.h"

/**
 * @author sumeet.chhetri
 * The Automated Message Exchange Format Object type
 * Is a wrapper for basic as well as complex object heirarchies
 * can be a string, number, date, bool, char or any complex object
 * Every message consists of only one AMEFObject *
 */

class AMEFObject
{	
	ConversionUtil util;

	/*The type of the Object can be string, number, date, bool, character or any complex object*/
	char type;
	
	/*The name of the Object if required can be used to represent object properties*/
	string name;
	
	/*The Length of the Object value*/
	int length;
	
	/*The Object value in string format*/
	string value;
	
	/*The properties of a complex object*/
	vector<AMEFObject*> packets;
	
	
	public:
		/*Create a new AMEF object which will initilaize the values*/
		AMEFObject();
		
		~AMEFObject();
		
		/**
		 * @param string
		 * @param name
		 * Add a string property to an Object
		 */
		void addPacket(string str,string name);
		void addPacket(string str,const char* name);
		void addPacket(const char* str,const char* name);
		void addPacket(const char* str,string name);
		
		/**
		 * @param string
		 * Add a string property to an Object
		 */
		AMEFObject* addPacket(string str);
		
		
		AMEFObject* addPacket(const char* str);
		/**
		 * @param bool
		 * @param name
		 * Add a bool property to an Object
		 */
		void addPacket(bool boolean,string name);
		void addPacket(bool boolean,const char* name);
		
		/**
		 * @param bool
		 * Add a bool property to an Object
		 */
		AMEFObject* addPacket(bool boolean);
		
		/**
		 * @param char
		 * @param name
		 * Add a char property to an Object
		 */
		void addPacket(char chr,string name);
		void addPacket(char chr,const char* name);
		/**
		 * @param char
		 * Add a char property to an Object
		 */
		AMEFObject* addPacket(char chr);
		
		/**
		 * @param lon
		 * @param name
		 * Add a long property to an Object
		 */
		void addPacket(long lon,string name);
		void addPacket(long lon,const char* name);
		
		/**
		 * @param lon
		 * Add a long property to an Object
		 */
		AMEFObject* addPacket(long lon);
		
		/**
		 * @param doub
		 * @param name
		 * Add a double property to an Object
		 */
		void addPacket(double doub,string name);
		void addPacket(double doub,const char* name);
		/**
		 * @param doub
		 * Add a double property to an Object
		 */
		AMEFObject* addPacket(double doub);
		
		/**
		 * @param integer
		 * @param name
		 * Add an integer property to an Object
		 */
		void addPacket(int integer,string name);
		void addPacket(int integer,const char* name);
		
		/**
		 * @param integer
		 * Add an integer property to an Object
		 */
		AMEFObject* addPacket(int integer);
		
		
		
		/**
		 * @param packet
		 * Add a AMEFObject property to an Object
		 */
		void addPacket(AMEFObject *packet);
		
		int getLength();
		void setLength(int length);
		
		string getName();
		void setName(string name);
		
		vector<AMEFObject*> getPackets();
		void setPackets(vector<AMEFObject*> packets);
		
		char getType();
		void setType(char type);
		
		string getValue();
		void setValue(string value);
		
		
		/**
		 * @return bool value of this object if its type is bool
		 */
		bool getboolValue();
		
		/**
		 * @return integer value of this object if its type is integer
		 */
		int getIntValue();
		
		/**
		 * @return double value of this object if its type is double
		 */
		double getDoubleValue();
		
		/**
		 * @return long value of this object if its type is long
		 */
		long getLongValue();
		
		string displayObject(string);
};

#endif

