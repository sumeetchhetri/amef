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
#include "AMEFEncoder.h"

AMEFEncoder::AMEFEncoder(){}

AMEFEncoder::~AMEFEncoder(){}
	
/**
 * @param packet
 * @return char[]
 * @throws AMEFEncodeException
 * encode the AMEFObject to the charstream for wire transmission
 */
string AMEFEncoder::encode(AMEFObject *packet,bool ignoreName)
{
	string dat = encodeSinglePacket(packet,ignoreName);
	int l = dat.length();
	string retval;
	retval.push_back((char)((l & 0xff000000) >> 24));
	retval.push_back((char)((l & 0xff0000) >> 16));
	retval.push_back((char)((l & 0xff00) >> 8));
	retval.push_back((char)(l & 0xff));
	retval += dat;
	return retval;
}	

/**
 * @param packet
 * @return string
 * @throws AMEFEncodeException
 * encode a given AMEF Object to its transmission form
 */
string AMEFEncoder::encodeSinglePacket(AMEFObject *packet,bool ignoreName)
{
	string buffer;
	string delim = ",";
	if(packet==NULL)
	{
		throw ("Objcet to be encoded is null");
	}
	int length = packet->getLength();
	if(packet->getPackets().size()==0)
		buffer.append(packet->getValue());
	else
	{
		for (int i=0;i<(int)packet->getPackets().size();i++)
		{
			AMEFObject *obj = packet->getPackets().at(i);
			buffer.append(encodeSinglePacket(obj,ignoreName));
		}
	}	
	if(buffer.length()>0)
	{
		length = buffer.length();			
	}
	string retval;
	retval += (packet->getType() + delim);
	if(!ignoreName)
		retval += (packet->getName() + delim);
	if(packet->getType()=='d' || packet->getType()=='n')
	{
		ConversionUtil util;
		retval += util.to_string<int>(length,std::dec);
		retval += delim;
		retval += buffer;
	}
	else if(packet->getType()=='s' || packet->getType()=='o')
	{
		int l = length;		
		retval.push_back((char)((l & 0xff000000) >> 24));
		retval.push_back((char)((l & 0xff0000) >> 16));
		retval.push_back((char)((l & 0xff00) >> 8));
		retval.push_back((char)(l & 0xff));
		retval += (delim + buffer);
	}
	else if(packet->getType()=='b' || packet->getType()=='c')
	{
		retval += buffer;
	}
	else
	{
		throw ("Not a valid AMEF Object type,only types string,number,boolean,character,date allowed");
	}
	return retval;
}
