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
#include "AMEFDecoder.h"

AMEFDecoder::AMEFDecoder(){}

AMEFDecoder::~AMEFDecoder(){}

/**
 * @param data
 * @param considerLength
 * @return AMEFObject
 * decode the bytestream to give the equivalent AMEFObject
 */
AMEFObject* AMEFDecoder::decode(string data,bool considerLength,bool ignoreName)
{
	int startWith = 0;		
	if(considerLength)
		startWith = 4;
	string strdata(data);
	strdata = strdata.substr(startWith);
	AMEFObject *amefObject = decodeSinglePacket(strdata,ignoreName);		
	return amefObject;
}

/**
 * @param strdata
 * @return AMEFObject
 * @throws AMEFDecodeException
 * decode the string to give the equivalent AMEFObject
 */
AMEFObject* AMEFDecoder::decodeSinglePacket(string strdata,bool ignoreName)
{
	AMEFObject *amefObject = NULL;
	char type = strdata.at(0);
	if(type=='s')
	{
		if(strdata.at(1)==',')
		{
			amefObject = new AMEFObject();
			amefObject->setType(type);
			int pos = 2;
			string name = "";
			if(!ignoreName)
			{
				while(strdata.at(pos)!=',')
				{
					name += strdata.at(pos++);
				}
				if(name=="" && strdata.at(2)!=',')
				{
					throw ("Invalid character after name specifier, expected ,");
				}
			}
			if(pos>=strdata.length())
			{
				throw ("Reached end of AMEF string, not found ,");
			}
			amefObject->setName(name);
			string length = "";
			if(!ignoreName)pos++;
			while(length.length()<4)
			{
				length += strdata.at(pos++);
			}
			if(length=="" && strdata.at(3)!=',')
			{
				throw ("Invalid character after length specifier, expected ,");
			}
			else if(pos>=strdata.length())
			{
				throw ("Reached end of AMEF string, not found ,");
			}
			int lengthm = ((length.at(0) & 0xff) << 24) | ((length.at(1) & 0xff) << 16)
							| ((length.at(2) & 0xff) << 8) | ((length.at(3) & 0xff));
			amefObject->setLength(lengthm);
			string value = strdata.substr(pos+1,lengthm);
			amefObject->setValue(value);
			this->tempVal = strdata.substr(pos+lengthm+1);
		}
		else
		{
			throw  ("Invalid character after type specifier, expected ,");
		}
	}
	else if(type=='b')
	{
		if(strdata.at(1)==',')
		{
			amefObject = new AMEFObject();
			amefObject->setType(type);
			int pos = 2;
			string name = "";
			if(!ignoreName)
			{
				while(strdata.at(pos)!=',')
				{
					name += strdata.at(pos++);
				}
				if(name=="" && strdata.at(2)!=',')
				{
					throw ("Invalid character after name specifier, expected ,");
				}
			}
			if(pos>=strdata.length())
			{
				throw  ("Reached end of AMEF string, not found ,");
			}
			amefObject->setName(name);
			if(pos>=strdata.length())
			{
				throw  ("Reached end of AMEF string, not found ,");
			}			
			amefObject->setLength(1);
			string value;
			if(!ignoreName)
			{
				value = strdata.substr(pos+1,1);
				this->tempVal = strdata.substr(pos+2);
			}
			else
			{
				value = strdata.substr(pos,1);
				this->tempVal = strdata.substr(pos+1);
			}
			amefObject->setValue(value);		
		}
		else
		{
			throw  ("Invalid character after type specifier, expected ,");
		}
	}
	else if(type=='c')
	{
		if(strdata.at(1)==',')
		{
			amefObject = new AMEFObject();
			amefObject->setType(type);
			int pos = 2;
			string name = "";
			if(!ignoreName)
			{
				while(strdata.at(pos)!=',')
				{
					name += strdata.at(pos++);
				}
				if(name=="" && strdata.at(2)!=',')
				{
					throw ("Invalid character after name specifier, expected ,");
				}
			}
			if(pos>=strdata.length())
			{
				throw  ("Reached end of AMEF string, not found ,");
			}
			amefObject->setName(name);
			if(pos>=strdata.length())
			{
				throw  ("Reached end of AMEF string, not found ,");
			}
			amefObject->setLength(1);
			string value;
			if(!ignoreName)
			{
				value = strdata.substr(pos+1,1);
				this->tempVal = strdata.substr(pos+2);
			}
			else
			{
				value = strdata.substr(pos,1);
				this->tempVal = strdata.substr(pos+1);
			}
			amefObject->setValue(value);
			
		}
		else
		{
			throw  ("Invalid character after type specifier, expected ,");
		}
	}
	else if(type=='n')
	{
		if(strdata.at(1)==',')
		{
			amefObject = new AMEFObject();
			amefObject->setType(type);
			int pos = 2;
			string name = "";
			if(!ignoreName)
			{
				while(strdata.at(pos)!=',')
				{
					name += strdata.at(pos++);
				}
				if(name=="" && strdata.at(2)!=',')
				{
					throw ("Invalid character after name specifier, expected ,");
				}
			}
			if(pos>=strdata.length())
			{
				throw  ("Reached end of AMEF string, not found ,");
			}
			amefObject->setName(name);
			string length = "";
			if(!ignoreName)pos++;
			while(strdata.at(pos)!=',')
			{
				length += strdata.at(pos++);
			}	
			if(length=="" && strdata.at(3)!=',')
			{
				throw  ("Invalid character after length specifier, expected ,");
			}
			else if(pos>=strdata.length())
			{
				throw  ("Reached end of AMEF string, not found ,");
			}
			int lengthm;
			ConversionUtil util;
			util.from_string<int>(lengthm,length,std::dec);		
			amefObject->setLength(lengthm);
			string value = strdata.substr(pos+1,lengthm);
			amefObject->setValue(value);
			this->tempVal = strdata.substr(pos+lengthm+1);
		}
		else
		{
			throw  ("Invalid character after type specifier, expected ,");
		}
	}
	else if(type=='d')
	{
		if(strdata.at(1)==',')
		{
			amefObject = new AMEFObject();
			amefObject->setType(type);
			int pos = 2;
			string name = "";
			if(!ignoreName)
			{
				while(strdata.at(pos)!=',')
				{
					name += strdata.at(pos++);
				}
				if(name=="" && strdata.at(2)!=',')
				{
					throw ("Invalid character after name specifier, expected ,");
				}
			}
			if(pos>=strdata.length())
			{
				throw  ("Reached end of AMEF string, not found ,");
			}
			amefObject->setName(name);
			string length = "";
			if(!ignoreName)pos++;
			while(strdata.at(pos)!=',')
			{
				length += strdata.at(pos++);
			}
			if(length=="" && strdata.at(3)!=',')
			{
				throw  ("Invalid character after length specifier, expected ,");
			}
			else if(pos>=strdata.length())
			{
				throw  ("Reached end of AMEF string, not found ,");
			}
			int lengthm;
			ConversionUtil util;
			util.from_string<int>(lengthm,length,std::dec);	
			amefObject->setLength(lengthm);
			string value = strdata.substr(pos+1,lengthm);
			amefObject->setValue(value);
			this->tempVal = strdata.substr(pos+lengthm+1);
		}
		else
		{
			throw  ("Invalid character after type specifier, expected ,");
		}
	}
	else if(type=='o')
	{
		if(strdata.at(1)==',')
		{
			amefObject = new AMEFObject();
			amefObject->setType(type);
			int pos = 2;
			string name = "";
			if(!ignoreName)
			{
				while(strdata.at(pos)!=',')
				{
					name += strdata.at(pos++);
				}
				if(name=="" && strdata.at(2)!=',')
				{
					throw ("Invalid character after name specifier, expected ,");
				}
			}
			if(pos>=strdata.length())
			{
				throw  ("Reached end of AMEF string, not found ,");
			}
			amefObject->setName(name);
			string length = "";
			if(!ignoreName)pos++;
			while(length.length()<4)
			{
				length += strdata.at(pos++);
			}
			if(length=="" && strdata.at(3)!=',')
			{
				throw  ("Invalid character after length specifier, expected ,");
			}
			else if(pos>=strdata.length())
			{
				throw  ("Reached end of AMEF string, not found ,");
			}
			int lengthm = ((length.at(0) & 0xff) << 24) | ((length.at(1) & 0xff) << 16)
							| ((length.at(2) & 0xff) << 8) | ((length.at(3) & 0xff));
			amefObject->setLength(lengthm);
			string value = strdata.substr(pos+1,lengthm);
			this->tempVal = value;
			while(this->tempVal!="")
			{
				AMEFObject *obj = decodeSinglePacket(this->tempVal,ignoreName);
				amefObject->addPacket(obj);
				
			}				
			this->tempVal = strdata.substr(pos+lengthm+1);
		}
		else
		{
			throw  ("Invalid character after type specifier, expected ,");
		}
	}
	return amefObject;
}

