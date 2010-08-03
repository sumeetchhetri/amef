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

#ifndef AMEFDECODER_H
#define AMEFDECODER_H

#include "string"
#include "AMEFObject.h"
#include <iostream>
using namespace std;

/**
 * @author sumeet.chhetri
 * The AMEFDecoder class
 * providses the decode method to get the AMEFObject from its transmission form
 */
 
class AMEFDecoder
{
	string tempVal;
	AMEFObject* decodeSinglePacket(string strdata,bool ignoreName);
	public:
		AMEFDecoder();
		~AMEFDecoder();
		AMEFObject* decode(string data,bool considerLength,bool ignoreName);	
};

#endif
