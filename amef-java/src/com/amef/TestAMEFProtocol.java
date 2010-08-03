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
		AMEFObject object = new AMEFObject();
		object.addPacket("This is the Automated Message Exchange Format Object property!!");
		object.addPacket(21213);
		object.addPacket(true);
		object.addPacket(new Date());
		AMEFObject object2 = new AMEFObject();
		object2.addPacket("This is the property of a nested Automated Message Exchange Format Object");
		object2.addPacket(134123);
		object2.addPacket(false);
		object.addPacket(object2);
		System.out.println(new String(new AMEFEncoder().encode(object)));
		AMEFObject object1 = new AMEFDecoder().decode(new AMEFEncoder().encode(object),true);
		System.out.println(object1);
	}
}
