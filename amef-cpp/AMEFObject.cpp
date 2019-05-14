/*
	Copyright 2009-2012, Sumeet Chhetri

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
#include "iostream"

map<char, string> AMEFObject::typeMap;
const char AMEFObject::NULL_STRING = 'a';
const char AMEFObject::NULL_NUMBER = 'b';
const char AMEFObject::NULL_DATE = 'c';
const char AMEFObject::NULL_FPN = 'd';
const char AMEFObject::NULL_BOOL = 'e';
const char AMEFObject::NULL_CHAR = 'f';
const char AMEFObject::NULL_OBJECT = 'g';
const char AMEFObject::INT_TYPE = 'h';
const char AMEFObject::LONG_INT_TYPE = 'i';
const char AMEFObject::BOOLEAN_TYPE = 'j';
const char AMEFObject::CHAR_TYPE = 'k';
const char AMEFObject::DATE_TYPE = 'l';
const char AMEFObject::DOUBLE_FLOAT_TYPE = 'm';
const char AMEFObject::ASCII_STRING_TYPE = 'n';
const char AMEFObject::STRING_TYPE = 'o';
const char AMEFObject::OBJECT_TYPE = 'p';
const char AMEFObject::STRING_TYPE_L = 'q';
const char AMEFObject::ASCII_STRING_TYPE_L = 'r';


AMEFObject::~AMEFObject() {
	for (int var = 0; var < (int)packets.size(); var++) {
		AMEFObject *ob = packets.at(var);
		delete ob;
	}
	clear();
}

string AMEFObject::intTocharArray(const int& l, const int& ind)
{
	string result;
	for (int i = 0; i<ind; i++)
	{
		int offset = (ind - 1 - i) * 8;
		result.push_back((char) ((l >> offset) & 0xFF));
	}
	return result;
}


string AMEFObject::ulonglongTocharArray(const unsigned long long& lon, int ind)
{
	if(ind==-1)
	{
		if(lon<256)
		{
			ind = 1;
		}
		else if(lon<65536)
		{
			ind = 2;
		}
		else if(lon<16777216)
		{
			ind = 3;
		}
		else if(lon<4294967296ULL)
		{
			ind = 4;
		}
		else if(lon<1099511627776ULL)
		{
			ind = 5;
		}
		else if(lon<281474976710656ULL)
		{
			ind = 6;
		}
		else if(lon<72057594037927936ULL)
		{
			ind = 7;
		}
		else
		{
			ind = 8;
		}
	}
	string result;
	for (int i = 0; i<ind; i++)
	{
		int offset = (ind - 1 - i) * 8;
		result.push_back((char) ((lon >> offset) & 0xFF));
	}
	return result;
}

string AMEFObject::ulonglongTocharArrayWithLI(const unsigned long long& lon)
{
	int ind;
	if(lon<256)
	{
		ind = 1;
	}
	else if(lon<65536)
	{
		ind = 2;
	}
	else if(lon<16777216)
	{
		ind = 3;
	}
	else if(lon<4294967296ULL)
	{
		ind = 4;
	}
	else if(lon<1099511627776ULL)
	{
		ind = 5;
	}
	else if(lon<281474976710656ULL)
	{
		ind = 6;
	}
	else if(lon<72057594037927936ULL)
	{
		ind = 7;
	}
	else
	{
		ind = 8;
	}
	string result;
	for (int i = 0; i<ind; i++)
	{
		int offset = (ind - 1 - i) * 8;
		result.push_back((char) ((lon >> offset) & 0xFF));
	}
	return result;
}

int AMEFObject::charArrayToInt(const string& l, const int& off, const int& ind)
{
	int t = 0;
	for (int i = 0; i < ind; i++)
	{
		t = (t << 8) + ((unsigned char)l[off+i] & 0xff);
	}
	return t;
}

unsigned long long AMEFObject::charArrayToULongLong(const string& l)
{
	unsigned long long t = 0;
	int ind = l.length();
	for (int i = 0; i < ind; i++)
	{
		t = (t << 8) + ((unsigned char)l[i] & 0xff);
	}
	return t;
}

unsigned long long AMEFObject::charArrayToULongLong(const string& l, const int& ind)
{
	unsigned long long t = 0;
	for (int i = 0; i < ind; i++)
	{
		t = (t << 8) + ((unsigned char)l[i] & 0xff);
	}
	return t;
}

bool AMEFObject::isNull()
{
	if(type<AMEFObject::INT_TYPE)
		return true;
	else
		return false;
}

/**
 * @return Array of JDBObjectNew
 *
 */

char AMEFObject::getEqvNullType(const char& type)
{
	if(type==STRING_TYPE)
		return NULL_STRING;
	else if(type==INT_TYPE || type==LONG_INT_TYPE)
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


void AMEFObject::clear()
{
	this->packets.clear();
}

/*Create a new AMEF object which will initilaize the values*/
AMEFObject::AMEFObject()
{
	type = OBJECT_TYPE;
	name = "";
	position = 0;
	blength = 0;
}

void AMEFObject::addNullPacket(const char& type, const string& name)
{
	AMEFObject* JDBObjectNew = new AMEFObject();
	JDBObjectNew->name = name;
	JDBObjectNew->type = type;
	packets.push_back(JDBObjectNew);
}

/**
 * @param string
 * @param name
 * Add a string property to an Object
 */
void AMEFObject::addPacket(const string& stringa, const string name)
{
	AMEFObject* JDBObjectNew = addStringPacket(stringa);
	JDBObjectNew->name = name;
}
/**
 * @param string
 * Add a string property to an Object
 */
AMEFObject* AMEFObject::addStringPacket(const string& stringa)
{
	AMEFObject* JDBObjectNew = new AMEFObject();
	JDBObjectNew->name = "";
	JDBObjectNew->type = stringa.length()>8388607?STRING_TYPE_L:STRING_TYPE;
	JDBObjectNew->value = stringa;
	packets.push_back(JDBObjectNew);
	return JDBObjectNew;
}

/**
 * @param string
 * @param name
 * Add a string property to an Object
 */
void AMEFObject::addPacket(char* stringa, const string name)
{
	AMEFObject* JDBObjectNew = addCharStringPacket(stringa);
	JDBObjectNew->name = name;
}
/**
 * @param string
 * Add a string property to an Object
 */
AMEFObject* AMEFObject::addCharStringPacket(char* stringa)
{
	AMEFObject* JDBObjectNew = new AMEFObject();
	JDBObjectNew->name = "";
	JDBObjectNew->value = string(stringa);
	JDBObjectNew->type = JDBObjectNew->value.length()>8388607?STRING_TYPE_L:STRING_TYPE;
	packets.push_back(JDBObjectNew);
	return JDBObjectNew;
}

/**
 * @param bool
 * @param name
 * Add a bool  property to an Object
 */
void AMEFObject::addPacket(const bool& boole, const string name)
{
	AMEFObject* JDBObjectNew = addBoolPacket(boole);
	JDBObjectNew->name = name;
}
/**
 * @param bool
 * Add a bool  property to an Object
 */
AMEFObject* AMEFObject::addBoolPacket(const bool& boole)
{
	AMEFObject* JDBObjectNew = new AMEFObject();
	JDBObjectNew->type = BOOLEAN_TYPE;
	JDBObjectNew->name = "";
	if(boole==true)
	{
		JDBObjectNew->value = "1";
	}
	else
	{
		JDBObjectNew->value = "0";
	}
	packets.push_back(JDBObjectNew);
	return JDBObjectNew;
}

void AMEFObject::addPacket(const char& chr, const string name)
{
	AMEFObject* JDBObjectNew = addCharPacket(chr);
	JDBObjectNew->name = name;
}

void AMEFObject::addPacket(const unsigned char& chr, const string name)
{
	AMEFObject* JDBObjectNew = addCharPacket(chr);
	JDBObjectNew->name = name;
}

AMEFObject* AMEFObject::addCharPacket(const char& chr)
{
	AMEFObject* JDBObjectNew = new AMEFObject();
	JDBObjectNew->type = CHAR_TYPE;
	JDBObjectNew->name = "";
	JDBObjectNew->value.push_back(chr);
	packets.push_back(JDBObjectNew);
	return JDBObjectNew;
}

void AMEFObject::addPacket(const short& lon, const string name)
{
	AMEFObject* JDBObjectNew = addULLPacket((unsigned long long)lon);
	JDBObjectNew->name = name;
}

void AMEFObject::addPacket(const unsigned short& lon, const string name)
{
	AMEFObject* JDBObjectNew = addULLPacket((unsigned long long)lon);
	JDBObjectNew->name = name;
}

void AMEFObject::addPacket(const int& lon, const string name)
{
	AMEFObject* JDBObjectNew = addULLPacket((unsigned long long)lon);
	JDBObjectNew->name = name;
}

void AMEFObject::addPacket(const unsigned int& lon, const string name)
{
	AMEFObject* JDBObjectNew = addULLPacket((unsigned long long)lon);
	JDBObjectNew->name = name;
}

void AMEFObject::addPacket(const long& lon, const string name)
{
	AMEFObject* JDBObjectNew = addULLPacket((unsigned long long)lon);
	JDBObjectNew->name = name;
}

void AMEFObject::addPacket(const unsigned long& lon, const string name)
{
	AMEFObject* JDBObjectNew = addULLPacket((unsigned long long)lon);
	JDBObjectNew->name = name;
}

void AMEFObject::addPacket(const long long& lon, const string name)
{
	AMEFObject* JDBObjectNew = addULLPacket((unsigned long long)lon);
	JDBObjectNew->name = name;
}

void AMEFObject::addPacket(const unsigned long long& lon, const string name)
{
	AMEFObject* JDBObjectNew = addULLPacket(lon);
	JDBObjectNew->name = name;
}

AMEFObject* AMEFObject::addULLPacket(const unsigned long long& lon)
{
	AMEFObject* JDBObjectNew = new AMEFObject();
	if(lon<4294967296ULL)
	{
		JDBObjectNew->type = INT_TYPE;
		AMEFObject::ulonglongTocharArrayF(lon, JDBObjectNew->value, 4);
	}
	else
	{
		JDBObjectNew->type = LONG_INT_TYPE;
		ulonglongTocharArrayF(lon, JDBObjectNew->value, 8);
	}
	JDBObjectNew->name = "";
	packets.push_back(JDBObjectNew);
	return JDBObjectNew;
}

/**
 * @param doub
 * @param name
 * Add a double property to an Object
 */
void AMEFObject::addPacket(const float& doub, const string name)
{
	AMEFObject* JDBObjectNew = addFloatPacket(doub);
	JDBObjectNew->name = name;
}
/**
 * @param doub
 * Add a double property to an Object
 */
AMEFObject* AMEFObject::addFloatPacket(const float& doub)
{
	AMEFObject* JDBObjectNew = new AMEFObject();
	JDBObjectNew->type = DOUBLE_FLOAT_TYPE;
	JDBObjectNew->name = "";
	JDBObjectNew->value = CastUtil::lexical_cast<string>(doub);
	packets.push_back(JDBObjectNew);
	return JDBObjectNew;
}



void AMEFObject::addPacket(const double& doub, const string name)
{
	AMEFObject* JDBObjectNew = addDoublePacket(doub);
	JDBObjectNew->name = name;
}

void AMEFObject::addPacket(const long double& doub, const string name)
{
	AMEFObject* JDBObjectNew = addLDoublePacket(doub);
	JDBObjectNew->name = name;
}

AMEFObject* AMEFObject::addLDoublePacket(const long double& doub)
{
	AMEFObject* JDBObjectNew = new AMEFObject();
	JDBObjectNew->type = DOUBLE_FLOAT_TYPE;
	JDBObjectNew->name = "";
	JDBObjectNew->value = CastUtil::lexical_cast<string>(doub);
	packets.push_back(JDBObjectNew);
	return JDBObjectNew;
}


AMEFObject* AMEFObject::addDoublePacket(const double& doub)
{
	AMEFObject* JDBObjectNew = new AMEFObject();
	JDBObjectNew->type = DOUBLE_FLOAT_TYPE;
	JDBObjectNew->name = "";
	JDBObjectNew->value = CastUtil::lexical_cast<string>(doub);
	packets.push_back(JDBObjectNew);
	return JDBObjectNew;
}

void AMEFObject::addPacket(AMEFObject *packet)
{
	packets.push_back(packet);
}


bool AMEFObject::isFloatingPoint(const char& type)
{
	if(type==DOUBLE_FLOAT_TYPE || type==NULL_FPN)
		return true;
	return false;
}

bool AMEFObject::isNumber(const char& type)
{
	if(type==INT_TYPE || type==LONG_INT_TYPE)
		return true;
	return false;
}

bool AMEFObject::isInteger(const char& type)
{
	if(type==INT_TYPE)
		return true;
	return false;
}

bool AMEFObject::isLong(const char& type)
{
	return isNumber(type);
}

bool AMEFObject::isChar(const char& type)
{
	if(type==NULL_CHAR ||  type==CHAR_TYPE)
		return true;
	return false;
}

bool AMEFObject::isBoolean(const char& type)
{
	if(type==NULL_BOOL || type==BOOLEAN_TYPE)
		return true;
	return false;
}

bool AMEFObject::isBoolean()
{
	return isBoolean(type);
}

bool AMEFObject::isDate(const char& type)
{
	if(type==NULL_DATE || type==DATE_TYPE)
		return true;
	return false;
}

bool  AMEFObject::isStringOrNullString()
{
	if(type==NULL_STRING || type==STRING_TYPE)
		return true;
	return false;
}

bool  AMEFObject::isString()
{
	if(type==STRING_TYPE)
		return true;
	return false;
}

bool  AMEFObject::isFloatingPoint()
{
	if(type==NULL_FPN || type==DOUBLE_FLOAT_TYPE)
		return true;
	return false;
}

bool  AMEFObject::isNumber()
{
	return isNumber(type);
}

bool  AMEFObject::isNumberOrNullNumber()
{
	if(isNumber(type) || type==NULL_NUMBER)
		return true;
	return false;
}

bool  AMEFObject::isChar()
{
	if(isBoolean(type) || isChar(type))
		return true;
	return false;
}

bool  AMEFObject::isDate()
{
	return isDate(type);
}

char* AMEFObject::getName()
{
	return (char*)name.c_str();
}
string AMEFObject::getNameStr()
{
	return name;
}
void AMEFObject::setName(const string& name)
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
void AMEFObject::setType(const char& type)
{
	this->type = type;
}

string AMEFObject::getValue()
{
	return value;
}

string AMEFObject::getValueStr()
{
	return value;
}
void AMEFObject::pushChar(const char& v)
{
	this->value.push_back(v);
}

void AMEFObject::setValue(unsigned long long lon)
{
	if(lon<4294967296ULL)
	{
		type = INT_TYPE;
		ulonglongTocharArrayF(lon, value, 4);
	}
	else
	{
		type = LONG_INT_TYPE;
		ulonglongTocharArrayF(lon, value, 8);
	}
}

void AMEFObject::setValue(char* value)
{
	this->value.clear();
	int len = strlen(value);
	for (int var = 0; var < len; var++) {
		this->value.push_back(value[var]);
	}
}
void AMEFObject::setValue(char *value, const int& len)
{
	this->value.clear();
	this->value.append(value,len);
}
void AMEFObject::setValue(const string& value)
{
	this->value = value;
}

char AMEFObject::getCharValue()
{
	if(type==CHAR_TYPE)
		return value[0];
	else
		return '\0';
}

unsigned char AMEFObject::getUCharValue()
{
	if(type==CHAR_TYPE)
		return (unsigned char)value[0];
	else
		return '\0';
}

/**
 * @return bool  value of this object if its type is boolean
 */
bool AMEFObject::getBoolValue()
{
	if(type==BOOLEAN_TYPE)
		return (value[0]=='1'?true:false);
	else
		return false;
}

/**
 * @return integer value of this object if its type is integer
 */
int AMEFObject::getIntValue()
{
	if(type==INT_TYPE || type==LONG_INT_TYPE)
	{
		return (int)charArrayToULongLong(value);
	}
	else
		return -1;
}

unsigned int AMEFObject::getUIntValue()
{
	if(type==INT_TYPE || type==LONG_INT_TYPE)
	{
		return (unsigned int)charArrayToULongLong(value);
	}
	else
		return -1;
}

/**
 * @return integer value of this object if its type is integer
 */
short AMEFObject::getShortValue()
{
	if(type==INT_TYPE || type==LONG_INT_TYPE)
	{
		return (short)charArrayToULongLong(value);
	}
	else
		return -1;
}

unsigned short AMEFObject::getUShortValue()
{
	if(type==INT_TYPE || type==LONG_INT_TYPE)
	{
		return (unsigned short)charArrayToULongLong(value);
	}
	else
		return -1;
}

double AMEFObject::getDoubleValue()
{
	if(type==DOUBLE_FLOAT_TYPE)
		return (CastUtil::lexical_cast<double>(getValueStr()));
	else
		return -1;
}

long double AMEFObject::getLongDoubleValue()
{
	if(type==DOUBLE_FLOAT_TYPE)
		return (CastUtil::lexical_cast<long double>(getValueStr()));
	else
		return -1;
}

/**
 * @return double value of this object if its type is double
 */
float AMEFObject::getFloatValue()
{
	if(type==DOUBLE_FLOAT_TYPE)
		return (CastUtil::lexical_cast<float>(getValueStr()));
	else
		return -1;
}

/**
 * @return long value of this object if its type is long
 */
long AMEFObject::getLongValue()
{
	if(type==INT_TYPE || type==LONG_INT_TYPE)
	{
		return (long)charArrayToULongLong(value);
	}
	else
		return -1;
}

unsigned long AMEFObject::getULongValue()
{
	if(type==INT_TYPE || type==LONG_INT_TYPE)
	{
		return (unsigned long)charArrayToULongLong(value);
	}
	else
		return -1;
}

long long AMEFObject::getLongLongValue()
{
	if(type==INT_TYPE || type==LONG_INT_TYPE)
	{
		return (long long)charArrayToULongLong(value);
	}
	else
		return -1;
}

unsigned long long AMEFObject::getULongLongValue()
{
	if(type==INT_TYPE || type==LONG_INT_TYPE)
	{
		return charArrayToULongLong(value);
	}
	else
		return -1;
}

unsigned long long AMEFObject::getNumericValue()
{
	if(type==INT_TYPE || type==LONG_INT_TYPE)
	{
		return charArrayToULongLong(value);
	}
	else
		return -1;
}


string AMEFObject::tostring()
{
	if(typeMap.size()==0) {
		typeMap[AMEFObject::NULL_STRING] = "NULL_STRING";
		typeMap[AMEFObject::NULL_NUMBER] = "NULL_NUMBER";
		typeMap[AMEFObject::NULL_DATE] = "NULL_DATE";
		typeMap[AMEFObject::NULL_FPN] = "NULL_FPN";
		typeMap[AMEFObject::NULL_BOOL] = "NULL_BOOL";
		typeMap[AMEFObject::NULL_CHAR] = "NULL_CHAR";
		typeMap[AMEFObject::NULL_OBJECT] = "NULL_OBJECT";
		typeMap[AMEFObject::INT_TYPE] = "INT_TYPE";
		typeMap[AMEFObject::LONG_INT_TYPE] = "LONG_INT_TYPE";
		typeMap[AMEFObject::BOOLEAN_TYPE] = "BOOLEAN_TYPE";
		typeMap[AMEFObject::CHAR_TYPE] = "CHAR_TYPE";
		typeMap[AMEFObject::DATE_TYPE] = "DATE_TYPE";
		typeMap[AMEFObject::DOUBLE_FLOAT_TYPE] = "DOUBLE_FLOAT_TYPE";
		typeMap[AMEFObject::ASCII_STRING_TYPE] = "ASCII_STRING_TYPE";
		typeMap[AMEFObject::STRING_TYPE] = "STRING_TYPE";
		typeMap[AMEFObject::OBJECT_TYPE] = "OBJECT_TYPE";
	}
	return displayObject("\t");
}

string AMEFObject::displayObject(const string& tab)
{
	string displ = "";
	for (int i=0;i<(int)getPackets().size();i++)
	{
		AMEFObject* obj = getPackets().at(i);
		displ += "type=";
		displ += typeMap[obj->type];
		displ += ", name=" + obj->name + ", value=";
		if(obj->type<AMEFObject::INT_TYPE) {
			displ.append("null");
		}
		else if(obj->type<AMEFObject::BOOLEAN_TYPE)
		{
			displ.append(CastUtil::lexical_cast<string>(obj->getLongValue()));
		}
		else if(obj->type<AMEFObject::DATE_TYPE)
		{
			if(obj->type==AMEFObject::BOOLEAN_TYPE) {
				displ.append(CastUtil::lexical_cast<string>(obj->getBoolValue()));
			} else {
				displ.append(CastUtil::lexical_cast<string>((int)obj->getCharValue()));
			}
		}
		else if(obj->type<AMEFObject::OBJECT_TYPE)
		{
			displ.append(obj->getValueStr());
		}
		else if(obj->type==AMEFObject::OBJECT_TYPE)
		{
			displ.append("\n");
			displ.append(obj->displayObject(tab+"\t"));
		}
		displ.append("\n");
	}
	return displ;
}

void AMEFObject::addStaticPacket(AMEFObject *obj)
{
	packets.push_back(obj);
}
