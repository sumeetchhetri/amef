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

 
/**
 * @author sumeetc
 * The AMEF Encoder Class
 * provides the encode method to encode the AMEFObject
 */
public class AMEFEncoderOld
{
	/*The default delimiter for single object representation*/
	private String delim = ",";
		
	/**
	 * @param packet
	 * @return byte[]
	 * @throws AMEFEncodeException
	 * encode the AMEFObject to the bytestream for wire transmission
	 */
	public byte[] encode(AMEFObjectOld packet,boolean ignoreName) throws AMEFEncodeException
	{
		String dat = encodeSinglePacket(packet,ignoreName);
		int l = dat.length();		
		byte[] buf = new byte[4];
		buf[0] = (byte)((l & 0xff000000) >>> 24);
		buf[1] = (byte)((l & 0x00ff0000) >>> 16);
		buf[2] = (byte)((l & 0x0000ff00) >>> 8);
		buf[3] = (byte)(l & 0x000000ff);
		return (new String(buf) + dat).getBytes();
	}	
	
	/**
	 * @param packet
	 * @return String
	 * @throws AMEFEncodeException
	 * encode a given AMEF Object to its transmission form
	 */
	private String encodeSinglePacket(AMEFObjectOld packet,boolean ignoreName) throws AMEFEncodeException
	{
		StringBuffer buffer = new StringBuffer();
		if(packet==null)
		{
			throw new AMEFEncodeException("Objcet to be encoded is null");
		}
		int length = packet.getLength();
		for (AMEFObjectOld pack : packet.getPackets())
		{
			buffer.append(encodeSinglePacket(pack,ignoreName));
		}
		if(packet.getPackets().size()==0)
			buffer.append(packet.getValue());
		if(buffer.length()>0)
		{
			length = buffer.length();			
		}
		String retval = packet.getType() + delim;
		if(!ignoreName)
			retval +=  packet.getName() + delim;
		if(packet.getType()==AMEFObjectOld.DATE_TYPE || packet.getType()==AMEFObjectOld.NUMBER_TYPE)
		{
			retval += length + delim + buffer.toString();
		}
		else if(packet.getType()==AMEFObjectOld.STRING_TYPE || packet.getType()==AMEFObjectOld.OBJECT_TYPE)
		{
			int l = length;		
			byte[] buf = new byte[4];
			buf[0] = (byte)((l & 0xff000000) >>> 24);
			buf[1] = (byte)((l & 0x00ff0000) >>> 16);
			buf[2] = (byte)((l & 0x0000ff00) >>> 8);
			buf[3] = (byte)(l & 0x000000ff);
			retval += new String(buf) + delim + buffer.toString();
		}
		else if(packet.getType()==AMEFObjectOld.BOOLEAN_TYPE || packet.getType()==AMEFObjectOld.CHAR_TYPE)
		{
			retval += buffer.toString();
		}
		else
		{
			throw new AMEFEncodeException("Not a valid AMEF Object type,only types string,number,boolean,character,date allowed");
		}
		return retval;
	}
}
