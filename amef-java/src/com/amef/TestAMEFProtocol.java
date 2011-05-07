/*
	Copyright 2011, Sumeet Chhetri 
  
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Date;

/**
 * @author sumeetc
 * The TestAMEFProtocol Class , to test the AMEF protocol
 */
public class TestAMEFProtocol
{
	static class A
	{
		A()
		{
			System.out.println("A");
		}
		A(int i)
		{
			System.out.println("Ai");
		}
		private void methodA() throws Exception
		{
			System.out.println("methodA");
		}
	}
	
	static class B extends A
	{
		B()
		{
			System.out.println("B");
		}
		B(int i)
		{
			System.out.println("Bi");
		}
		public final static void methodA() throws IOException
		{
			System.out.println("methodB");
		}
	}
	/**
	 * @param args
	 * @throws AMEFEncodeException 
	 * @throws AMEFDecodeException 
	 * Test the Encode and Decode functions
	 */
	public static void main(String[] args) throws Exception
	{
		AMEFObject object = new AMEFObject();
		object.addNullPacket(AMEFObject.NULL_STRING, "");
		object.addPacket("asdasD");
		System.out.println(new String(new AMEFEncoder().encodeWL(object, true)));
		System.out.println(new AMEFDecoder().decodeB(new AMEFEncoder().encodeWL(object, true), false, true));
		/*object.addPacket("TransactionCode","NOTIFICATION");
		object.addPacket("OLC","PublisherApplID");
		object.addPacket("5","PublisherApplThreadID");
		object.addPacket("2010-06-30T10:23:35","IssueDate");
		object.addPacket("2010-06-30T10:23:35","EffectiveDate");
		object.addPacket("300023741","RoutingId");
		object.addPacket("ACT001","ActionCode");
		object.addPacket("NO","ActionGroup");
		object.addPacket("122","PayChannel");
		object.addPacket("122","SubscriberNo");
		object.addPacket("510.11.23411.37567","GPPUserLocationInfo");
		object.addPacket("2010-08-06T10:00:31","ActivationStartTime");	
		//System.out.println(JDBEncoder.byteArrayToInt(JDBEncoder.intToByteArray(1111, 2).getBytes()));
		
		JDBObject object1 = new JDBObject();
		object1.addPacket("OLC","PublisherApplID");
		object1.addPacket(1111111111);
		object1.addPacket(1111111111111111111L);
		object1.addPacket(11111.12312);
		object1.addPacket(true);
		object1.addPacket('a');
		
		JDBObject object3 = new JDBObject();
		object3.addPacket("OLC","PublisherApplID");
		
		JDBObject object4 = new JDBObject();
		object4.addPacket("OLC","PublisherApplID");
		
		
		JDBObject object5 = new JDBObject();
		object5.addPacket("OLC","PublisherApplID");
		object4.addPacket(object5);
		object3.addPacket(object4);
		//object1.addPacket(object3);
		object1.addPacket(object3);
		//object.addPacket(object1);
		//new JDBEncoder().encodeB(object3,false);
		//System.out.println(new String(new JDBEncoder().encodeB(object1,false)));
		System.out.println(new String(new JDBEncoder().encodeB(object1,true)));
		System.out.println(new String(new JDBEncoder().encodeB(object1,false)).length());
		System.out.println(object1.getNamedLength(false));
		//BufferedWriter bw = new BufferedWriter(new FileWriter(new File("temp")));
		//bw.write(new String(new AMEFEncoder().encode(object,false)));
		//bw.close();
		JDBObject object2 = new JDBDecoder().decodeB(new JDBEncoder().encodeB(object1,false),true,false);
		System.out.println(object2);
		object2 = new JDBDecoder().decodeB(new JDBEncoder().encodeB(object1,true),true,true);
		System.out.println(object2);
		//System.out.println(object2.getPackets().get(1).getIntValue());
		//System.out.println(object2.getPackets().get(2).getLongValue());
		//System.out.println(object2.getPackets().get(3).getDoubleValue());
		//System.out.println(new JDBEncoder().getPacketValue("asdfasdasdasdasdasda"));
*/	}
}
