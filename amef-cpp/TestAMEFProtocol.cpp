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
#include "AMEFEncoder.h"
#include <iostream>
int maintest()
{
	AMEFObject *object = new AMEFObject();
	string h = "This is the Automated Message Exchange Format Object property!!";
	object->addPacket("This is the Automated Message Exchange Format Object property!!","adasd");	
	object->addPacket(21213);
	object->addPacket(true);
	AMEFObject *object2 = new AMEFObject();
	string j = "This is the property of a nested Automated Message Exchange Format Object";
	object2->addPacket(j);
	object2->addPacket(134123);
	object2->addPacket(false);
	object2->addPacket('d');
	object->addPacket(object2);
	AMEFEncoder encoder;
	string str = encoder.encodeB(object,false);
	cout << str << flush;
	AMEFDecoder decoder;
	AMEFObject *object1 = decoder.decodeB(str,true,false);
	cout << object1->displayObject("") << flush;
	return 1;
}
