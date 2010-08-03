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
#include "AMEFObject.h"
	
AMEFObject::~AMEFObject()
{
	
}


/*Create a new AMEF object which will initilaize the values*/
AMEFObject::AMEFObject()
{
	type = 'o';
	length = 0;
	name = "";
}


/**
 * @param string
 * @param name
 * Add a string property to an Object
 */
void AMEFObject::addPacket(string str,string name)
{
	AMEFObject *amefObject = addPacket(str);
	amefObject->name = name;
}

void AMEFObject::addPacket(string str,const char* name)
{
	AMEFObject *amefObject = addPacket(str);
	amefObject->name.append(name);
}

void AMEFObject::addPacket(const char* str,const char* name)
{
	string temp(str);
	AMEFObject *amefObject = addPacket(temp);
	amefObject->name.append(name);
}

void AMEFObject::addPacket(const char* str,string name)
{
	string temp(str);
	AMEFObject *amefObject = addPacket(temp);
	amefObject->name = name;
}
/**
 * @param string
 * Add a string property to an Object
 */
AMEFObject* AMEFObject::addPacket(string str)
{
	AMEFObject *amefObject = new AMEFObject();
	amefObject->type = 's';
	amefObject->name = "";
	amefObject->length = str.length();
	amefObject->value = str;
	packets.push_back(amefObject);
	return amefObject;
}

AMEFObject* AMEFObject::addPacket(const char* str)
{
	string temp(str);
	AMEFObject *amefObject = new AMEFObject();
	amefObject->type = 's';
	amefObject->name = "";
	amefObject->length = temp.length();
	amefObject->value = temp;
	packets.push_back(amefObject);
	return amefObject;
}

/**
 * @param bool
 * @param name
 * Add a bool property to an Object
 */
void AMEFObject::addPacket(bool boolean,string name)
{
	AMEFObject *amefObject = addPacket(boolean);
	amefObject->name = name;
}
void AMEFObject::addPacket(bool boolean,const char* name)
{
	AMEFObject *amefObject = addPacket(boolean);
	amefObject->name.append(name);
}

/**
 * @param bool
 * Add a bool property to an Object
 */
AMEFObject* AMEFObject::addPacket(bool boolean)
{
	AMEFObject *amefObject = new AMEFObject();
	amefObject->type = 'b';
	amefObject->name = "";
	amefObject->length = 1;
	if(boolean==true)
	{			
		amefObject->value = "1";
	}
	else
	{
		amefObject->value = "0";		
	}		
	packets.push_back(amefObject);
	return amefObject;
}

/**
 * @param char
 * @param name
 * Add a char property to an Object
 */
void AMEFObject::addPacket(char chr,string name)
{
	AMEFObject *amefObject = addPacket(chr);
	amefObject->name = name;
}
void AMEFObject::addPacket(char chr,const char* name)
{
	AMEFObject *amefObject = addPacket(chr);
	amefObject->name.append(name);
}

/**
 * @param char
 * Add a char property to an Object
 */
AMEFObject* AMEFObject::addPacket(char chr)
{
	AMEFObject *amefObject = new AMEFObject();
	amefObject->type = 'c';
	amefObject->name = "";
	amefObject->length = 1;
	amefObject->value.push_back(chr);		
	packets.push_back(amefObject);
	return amefObject;
}

/**
 * @param lon
 * @param name
 * Add a long property to an Object
 */
void AMEFObject::addPacket(long lon,string name)
{
	AMEFObject *amefObject = addPacket(lon);
	amefObject->name = name;
}
void AMEFObject::addPacket(long lon,const char* name)
{
	AMEFObject *amefObject = addPacket(lon);
	amefObject->name.append(name);
}

/**
 * @param lon
 * Add a long property to an Object
 */
AMEFObject* AMEFObject::addPacket(long lon)
{
	AMEFObject *amefObject = new AMEFObject();
	amefObject->type = 'n';
	amefObject->name = "";
	amefObject->length = util.to_string<long>(lon,std::dec).length();
	amefObject->value = util.to_string<long>(lon,std::dec);
	packets.push_back(amefObject);
	return amefObject;
}

/**
 * @param doub
 * @param name
 * Add a double property to an Object
 */
void AMEFObject::addPacket(double doub,string name)
{
	AMEFObject *amefObject = addPacket(doub);
	amefObject->name = name;
}
void AMEFObject::addPacket(double doub,const char* name)
{
	AMEFObject *amefObject = addPacket(doub);
	amefObject->name.append(name);
}

/**
 * @param doub
 * Add a double property to an Object
 */
AMEFObject* AMEFObject::addPacket(double doub)
{
	AMEFObject *amefObject = new AMEFObject();
	amefObject->type = 'n';
	amefObject->name = "";
	amefObject->length = util.to_string<double>(doub,std::dec).length();
	amefObject->value = util.to_string<double>(doub,std::dec);
	packets.push_back(amefObject);
	return amefObject;
}

/**
 * @param integer
 * @param name
 * Add an integer property to an Object
 */
void AMEFObject::addPacket(int integer,string name)
{
	AMEFObject *amefObject = addPacket(integer);
	amefObject->name = name;
}
void AMEFObject::addPacket(int integer,const char* name)
{
	AMEFObject *amefObject = addPacket(integer);
	amefObject->name.append(name);
}

/**
 * @param integer
 * Add an integer property to an Object
 */
AMEFObject* AMEFObject::addPacket(int integer)
{
	AMEFObject *amefObject = new AMEFObject();
	amefObject->type = 'n';
	amefObject->name = "";
	amefObject->length = util.to_string<int>(integer,std::dec).length();
	amefObject->value = util.to_string<int>(integer,std::dec);
	packets.push_back(amefObject);
	return amefObject;
}


/**
 * @param packet
 * Add a AMEFObject property to an Object
 */
void AMEFObject::addPacket(AMEFObject *packet)
{
	packets.push_back(packet);
}


int AMEFObject::getLength()
{
	return length;
}
void AMEFObject::setLength(int length)
{
	this->length = length;
}

string AMEFObject::getName()
{
	return name;
}
void AMEFObject::setName(string name)
{
	this->name = name;
}

vector<AMEFObject*> AMEFObject::getPackets()
{
	return packets;
}
void AMEFObject::setPackets(vector<AMEFObject*> packets)
{
	this->packets = packets;
}

char AMEFObject::getType()
{
	return type;
}
void AMEFObject::setType(char type)
{
	this->type = type;
}

string AMEFObject::getValue()
{
	return value;
}
void AMEFObject::setValue(string value)
{
	this->value = value;
}


/**
 * @return bool value of this object if its type is bool
 */
bool AMEFObject::getboolValue()
{
	if(type=='b')
		return (value=="1"?true:false);
	else
		return false;
}

/**
 * @return integer value of this object if its type is integer
 */
int AMEFObject::getIntValue()
{
	if(type=='n')
	{
		int i;
		util.from_string<int>(i, value, std::dec);
		return (i);
	}
	else
		return -1;
}

/**
 * @return double value of this object if its type is double
 */
double AMEFObject::getDoubleValue()
{
	double i;
	if(type=='n')
	{		
		util.from_string<double>(i, value, std::dec);
		return (i);
	}
	else
		return i;
}

/**
 * @return long value of this object if its type is long
 */
long AMEFObject::getLongValue()
{
	long i;
	if(type=='n')
	{		
		util.from_string<long>(i, value, std::dec);
		return (i);
	}
	else
		return i;
}

string AMEFObject::displayObject(string tab)
{
	string displ;
	cout << "number of Children = " << getPackets().size() << endl;
	for (int i=0;i<(int)getPackets().size();i++)
	{
		AMEFObject *obj = getPackets().at(i);		
		displ += tab + "Object Type = ";
		displ.push_back(obj->type);
		displ += "\n" + tab + "Object Name = " + obj->name + "\n";
		displ += tab + "Object Length = ";
		displ.append(util.to_string<int>(obj->length,std::dec));
		displ += "\n" + tab + "Object Value = " + obj->value + "\n";
		if(obj->type=='o')
		{
			displ += obj->displayObject("\t");
		}
	}
	return displ;
}

/**
 * @return Date value of this object if its type is Date
 */
/*Date AMEFObject::getDateValue()
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
}*/
