
```
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

int main()
{
        //Create a new AMEF object
	AMEFObject *object = new AMEFObject();
	
        //Add a child string object
        object->addPacket("This is the Automated Message Exchange Format Object property!!","adasd");	

        //Add a child integer object
	object->addPacket(21213);

        //Add a child boolean object
	object->addPacket(true);

	AMEFObject *object2 = new AMEFObject();
	string j = "This is the property of a nested Automated Message Exchange Format Object";
	object2->addPacket(j);
	object2->addPacket(134123);
	object2->addPacket(false);
        
        //Add a child character object
	object2->addPacket('d');

        //Add a child AMEF Object
	object->addPacket(object2);

        //Encode the AMEF obejct
	string str = new AMEFEncoder()->encode(object,false);
	cout << str << flush;

	//Deocde the AMEF encoded byte-stream to an AMEFObject
	AMEFObject *object1 = new AMEFDecoder()->decode(str,true,false);
	cout << object1->displayObject("") << flush;
	return 1;
}
```