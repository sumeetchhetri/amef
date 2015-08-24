
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
package com.amef;

import java.util.Date;

/**
 * @author sumeetc
 * The TestAMEFProtocol Class , to test the AMEF protocol
 */
public class TestAMEFProtocol
{

	/**
	 * @param args
	 * @throws AMEFEncodeException 
	 * @throws AMEFDecodeException 
	 * Test the Encode and Decode functions
	 */
	public static void main(String[] args) throws AMEFEncodeException, AMEFDecodeException
	{
                //Create a new AMEF Object 
		AMEFObject object = new AMEFObject();

                //Add a child string object
		object.addPacket("This is the Automated Message Exchange Format Object property!!");

                //Add a child integer object
		object.addPacket(21213);

                //Add a child boolean object
		object.addPacket(true);

                //Add a child dateobject
		object.addPacket(new Date());

                
		AMEFObject object2 = new AMEFObject();
                object2.addPacket("This is the property of a nested Automated Message Exchange Format Object");
		object2.addPacket(134123);
		object2.addPacket(false);

                //Add a child AMEF object
		object.addPacket(object2);

                //Encode the AMEF Object
		System.out.println(new String(new AMEFEncoder().encode(object)));

                //Decode the AMEF encoded byte-stream to an AMEF Object
		AMEFObject object1 = new AMEFDecoder().decode(new AMEFEncoder().encode(object),true);
		System.out.println(object1);
	}
}

```