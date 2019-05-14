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

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author sumeetc
 * The AMEFDecoder class
 * providses the decode method to get the JDBObject from its transmission form
 */
public final class AMEFDecoder
{
	private static final Logger logger = LoggerFactory.getLogger(AMEFDecoder.class);
	
	public AMEFObject decodeB(byte[] buffer, boolean considerLength) throws AMEFDecodeException
	{
		if(buffer.length==0) {
			return null;
		}
		AMEFObject jdbObject = new AMEFObject();
		if(considerLength)
		{
			jdbObject.position = 4;
		}
		decodeSinglePacket(jdbObject, buffer, true);
		return jdbObject;
	}

	public List<AMEFObject> decodeDyn(byte[] buffer, int blength) throws AMEFDecodeException
	{
		List<AMEFObject> objects = new ArrayList<AMEFObject>();
		if(blength==0) {
			return objects;
		}
		int position = 0;
		do {
			AMEFObject JDBObject = new AMEFObject();
			JDBObject.position = position;
			decodeSinglePacket(JDBObject, buffer, true);
			position = JDBObject.position;
			objects.add(JDBObject);
		} while (position<blength);
		return objects;
	}
	
	public List<AMEFObject> decodeDynDebug(byte[] buffer, int blength, boolean stdout) throws AMEFDecodeException
	{
		List<AMEFObject> objects = new ArrayList<AMEFObject>();
		if(blength==0) {
			return objects;
		}
		int position = 0;
		do {
			AMEFObject JDBObject = new AMEFObject();
			JDBObject.position = position;
			decodeSinglePacketDebug(JDBObject, buffer, true, "", stdout);
			position = JDBObject.position;
			objects.add(JDBObject);
		} while (position<blength);
		return objects;
	}

	private byte[] decodeName(AMEFObject jDBObject, byte[] buffer, boolean ignoreName)
	{
		byte[] name = null;
		if(!ignoreName)
		{
			++jDBObject.position;
			int lenident = buffer[jDBObject.position++];
			if(lenident>0)
			{
				name = new byte[lenident];
				System.arraycopy(buffer, jDBObject.position, name, 0, name.length);
				jDBObject.position += lenident;
			}
		}
		else
		{
			++jDBObject.position;
		}
		return name;
	}

	private void decodeSinglePacket(AMEFObject jDBObject, byte[] buffer, boolean ignoreName) throws AMEFDecodeException
	{
		char type = (char)buffer[jDBObject.position];
		if(type<AMEFObject.INT_TYPE)
		{
			byte[] name = decodeName(jDBObject, buffer, ignoreName);
			jDBObject.types.add(type);
			jDBObject.names.add(name);
			jDBObject.packets.add(null);
		}
		else if(type<AMEFObject.BOOLEAN_TYPE)
		{
			byte[] name = decodeName(jDBObject, buffer, ignoreName);
			jDBObject.types.add(type);
			jDBObject.names.add(name);
			if(type==AMEFObject.LONG_INT_TYPE)
			{
				jDBObject.packets.add(AMEFResources.byteArrayToLong(buffer, jDBObject.position, 8));
				jDBObject.position += 8;
			}
			else
			{
				jDBObject.packets.add(AMEFResources.byteArrayToInt(buffer, jDBObject.position, 4));
				jDBObject.position += 4;
			}
		}
		else if(type<AMEFObject.DATE_TYPE)
		{
			byte[] name = decodeName(jDBObject, buffer, ignoreName);
			jDBObject.types.add(type);
			jDBObject.names.add(name);
			if(type==AMEFObject.BOOLEAN_TYPE) {
				jDBObject.packets.add(buffer[jDBObject.position++]=='1'?true:false);
			} else {
				jDBObject.packets.add(buffer[jDBObject.position++]);
			}
		}
		else if(type<AMEFObject.OBJECT_TYPE)
		{
			byte[] name = decodeName(jDBObject, buffer, ignoreName);
			jDBObject.types.add(type);
			jDBObject.names.add(name);
			int lengthm = AMEFResources.byteArrayToInt(buffer, jDBObject.position, 3);
			jDBObject.position += 3;
			byte[] value = new byte[lengthm];
			System.arraycopy(buffer, jDBObject.position, value, 0, value.length);
			if(type==AMEFObject.STRING_TYPE) {
				jDBObject.packets.add(value);
			} else if(type==AMEFObject.ASCII_STRING_TYPE) {
				try {
					jDBObject.packets.add(new String(value, "ISO-8859-1"));
				} catch (UnsupportedEncodingException e) {
				}
			} else if(type==AMEFObject.DATE_TYPE) {
				try {
					String dval = new String(value, "ISO-8859-1");
					try {
						SimpleDateFormat format = new SimpleDateFormat(AMEFObject.DATE_FMT);
						jDBObject.packets.add(format.parse(dval));
					} catch (Exception e) {
						//backward compatibility
						try {
							SimpleDateFormat format = new SimpleDateFormat("ddMMyyyy HHmmss");
							jDBObject.packets.add(format.parse(dval));
						} catch (Exception e2) {
							//Ignore it....
						}
					}
				} catch (Exception e) {
					throw new AMEFDecodeException(e);
				}
			} else if(type==AMEFObject.DOUBLE_FLOAT_TYPE) {
				try {
					jDBObject.packets.add(Double.parseDouble(new String(value, "ISO-8859-1")));
				} catch (Exception e) {
				}
			}
			jDBObject.position += lengthm;
		}
		else if(type>AMEFObject.OBJECT_TYPE)
		{
			byte[] name = decodeName(jDBObject, buffer, ignoreName);
			jDBObject.types.add(type);
			jDBObject.names.add(name);
			int lengthm = AMEFResources.byteArrayToInt(buffer, jDBObject.position, 4);
			jDBObject.position += 4;
			byte[] value = new byte[lengthm];
			System.arraycopy(buffer, jDBObject.position, value, 0, value.length);
			if(type==AMEFObject.STRING_TYPE_L) {
				jDBObject.packets.add(value);
			} else if(type==AMEFObject.ASCII_STRING_TYPE_L) {
				try {
					jDBObject.packets.add(new String(value, "ISO-8859-1"));
				} catch (UnsupportedEncodingException e) {
				}
			}
			jDBObject.position += lengthm;
		}
		else if(type==AMEFObject.OBJECT_TYPE)
		{
			if(ignoreName) {
				++jDBObject.position;
				jDBObject.setType(type);
				jDBObject.blength = 4 + jDBObject.position + AMEFResources.byteArrayToInt(buffer, jDBObject.position, 4);
				jDBObject.position += 4;
				int poslen = AMEFResources.byteArrayToInt(buffer, jDBObject.position, 4);
				jDBObject.position += poslen + 4;
				while(jDBObject.position<jDBObject.blength)
				{
					decodeSinglePacket(jDBObject, buffer, false);
				}
			} else {
				byte[] name = decodeName(jDBObject, buffer, ignoreName);
				AMEFObject njDBObject = new AMEFObject();
				njDBObject.position = jDBObject.position;
				njDBObject.blength = 4 + njDBObject.position + AMEFResources.byteArrayToInt(buffer, njDBObject.position, 4);
				njDBObject.position += 4;
				int poslen = AMEFResources.byteArrayToInt(buffer, njDBObject.position, 4);
				njDBObject.position += poslen + 4;
				while(njDBObject.position<njDBObject.blength)
				{
					decodeSinglePacket(njDBObject, buffer, false);
					jDBObject.position = njDBObject.position;
				}
				jDBObject.types.add(type);
				jDBObject.names.add(name);
				jDBObject.packets.add(njDBObject);
			}
		}
	}
	
	//Should always be an exact replica of the above function decodeSinglePacketDebug
	private void decodeSinglePacketDebug(AMEFObject jDBObject, byte[] buffer, boolean ignoreName, 
			String indent, boolean stdout) throws AMEFDecodeException
	{
		char type = (char)buffer[jDBObject.position];
		if(type<AMEFObject.INT_TYPE)
		{
			byte[] name = decodeName(jDBObject, buffer, ignoreName);
			jDBObject.types.add(type);
			jDBObject.names.add(name);
			jDBObject.packets.add(null);
			String str = String.format("%stype=%s, name=%s", indent, AMEFObject.TYPE_NAMES.get(type), name);
			if(stdout) {
				System.out.println(str);
			} else {
				logger.info(str);
			}
		}
		else if(type<AMEFObject.BOOLEAN_TYPE)
		{
			byte[] name = decodeName(jDBObject, buffer, ignoreName);
			jDBObject.types.add(type);
			jDBObject.names.add(name);
			if(type==AMEFObject.LONG_INT_TYPE)
			{
				jDBObject.packets.add(AMEFResources.byteArrayToLong(buffer, jDBObject.position, 8));
				jDBObject.position += 8;
			}
			else
			{
				jDBObject.packets.add(AMEFResources.byteArrayToInt(buffer, jDBObject.position, 4));
				jDBObject.position += 4;
			}
			String str = indent + "type="+AMEFObject.TYPE_NAMES.get(type)+", name="+name+", " +
					"value="+jDBObject.packets.get(jDBObject.packets.size()-1);
			if(stdout) {
				System.out.println(str);
			} else {
				logger.info(str);
			}
		}
		else if(type<AMEFObject.DATE_TYPE)
		{
			byte[] name = decodeName(jDBObject, buffer, ignoreName);
			jDBObject.types.add(type);
			jDBObject.names.add(name);
			if(type==AMEFObject.BOOLEAN_TYPE) {
				jDBObject.packets.add(buffer[jDBObject.position++]=='1'?true:false);
			} else {
				jDBObject.packets.add(buffer[jDBObject.position++]);
			}
			String str = indent + "type="+AMEFObject.TYPE_NAMES.get(type)+", name="+name+", " +
					"value="+jDBObject.packets.get(jDBObject.packets.size()-1);
			if(stdout) {
				System.out.println(str);
			} else {
				logger.info(str);
			}
		}
		else if(type<AMEFObject.OBJECT_TYPE)
		{
			byte[] name = decodeName(jDBObject, buffer, ignoreName);
			jDBObject.types.add(type);
			jDBObject.names.add(name);
			int lengthm = AMEFResources.byteArrayToInt(buffer, jDBObject.position, 3);
			jDBObject.position += 3;
			byte[] value = new byte[lengthm];
			System.arraycopy(buffer, jDBObject.position, value, 0, value.length);
			if(type==AMEFObject.STRING_TYPE) {
				jDBObject.packets.add(value);
			} else if(type==AMEFObject.ASCII_STRING_TYPE) {
				try {
					jDBObject.packets.add(new String(value, "ISO-8859-1"));
				} catch (UnsupportedEncodingException e) {
				}
			} else if(type==AMEFObject.DATE_TYPE) {
				try {
					String dval = new String(value, "ISO-8859-1");
					try {
						SimpleDateFormat format = new SimpleDateFormat(AMEFObject.DATE_FMT);
						jDBObject.packets.add(format.parse(dval));
					} catch (Exception e) {
						//backward compatibility
						try {
							SimpleDateFormat format = new SimpleDateFormat("ddMMyyyy HHmmss");
							jDBObject.packets.add(format.parse(dval));
						} catch (Exception e2) {
							//Ignore it....
						}
					}
				} catch (Exception e) {
					throw new AMEFDecodeException(e);
				}
			} else if(type==AMEFObject.DOUBLE_FLOAT_TYPE) {
				try {
					jDBObject.packets.add(Double.parseDouble(new String(value, "ISO-8859-1")));
				} catch (Exception e) {
				}
			}
			jDBObject.position += lengthm;
			String str = indent + "type="+AMEFObject.TYPE_NAMES.get(type)+", name="+name+", " +
					"value="+jDBObject.packets.get(jDBObject.packets.size()-1);
			if(stdout) {
				System.out.println(str);
			} else {
				logger.info(str);
			}
		}
		else if(type>AMEFObject.OBJECT_TYPE)
		{
			byte[] name = decodeName(jDBObject, buffer, ignoreName);
			jDBObject.types.add(type);
			jDBObject.names.add(name);
			int lengthm = AMEFResources.byteArrayToInt(buffer, jDBObject.position, 4);
			jDBObject.position += 4;
			byte[] value = new byte[lengthm];
			System.arraycopy(buffer, jDBObject.position, value, 0, value.length);
			if(type==AMEFObject.STRING_TYPE_L) {
				jDBObject.packets.add(value);
			} else if(type==AMEFObject.ASCII_STRING_TYPE_L) {
				try {
					jDBObject.packets.add(new String(value, "ISO-8859-1"));
				} catch (UnsupportedEncodingException e) {
				}
			}
			jDBObject.position += lengthm;
			String str = indent + "type="+AMEFObject.TYPE_NAMES.get(type)+", name="+name+", " +
					"value="+jDBObject.packets.get(jDBObject.packets.size()-1);
			if(stdout) {
				System.out.println(str);
			} else {
				logger.info(str);
			}
		}
		else if(type==AMEFObject.OBJECT_TYPE)
		{
			if(ignoreName) {
				++jDBObject.position;
				jDBObject.setType(type);
				jDBObject.blength = 4 + jDBObject.position + AMEFResources.byteArrayToInt(buffer, jDBObject.position, 4);
				jDBObject.position += 4;
				int poslen = AMEFResources.byteArrayToInt(buffer, jDBObject.position, 4);
				jDBObject.position += poslen + 4;
				while(jDBObject.position<jDBObject.blength)
				{
					decodeSinglePacketDebug(jDBObject, buffer, false, indent, stdout);
				}
				String str = jDBObject.getStringRepr(indent);
				if(stdout) {
					System.out.println(str+"\n");
				} else {
					logger.info(str+"\n");
				}
			} else {
				byte[] name = decodeName(jDBObject, buffer, ignoreName);
				AMEFObject njDBObject = new AMEFObject();
				njDBObject.position = jDBObject.position;
				njDBObject.blength = 4 + njDBObject.position + AMEFResources.byteArrayToInt(buffer, njDBObject.position, 4);
				njDBObject.position += 4;
				int poslen = AMEFResources.byteArrayToInt(buffer, njDBObject.position, 4);
				njDBObject.position += poslen + 4;
				String str = indent + "type="+AMEFObject.TYPE_NAMES.get(type)+", name="+name+", value=";
				if(stdout) {
					System.out.println(str);
				} else {
					logger.info(str);
				}
				while(njDBObject.position<njDBObject.blength)
				{
					decodeSinglePacketDebug(njDBObject, buffer, false, indent+"\t", stdout);
					jDBObject.position = njDBObject.position;
				}
				if(stdout) {
					System.out.println();
				} else {
					logger.info("");
				}
				jDBObject.types.add(type);
				jDBObject.names.add(name);
				jDBObject.packets.add(njDBObject);
			}
		}
	}

	public byte[] updatePacket(byte[] orig, AMEFObject jDBObject) throws Exception {
		ByteArrayOutputStream baOs = new ByteArrayOutputStream();
		List<Integer> propLengths = new ArrayList<Integer>();
		int poslen = AMEFResources.byteArrayToInt(orig, 5, 4);
		for (int var = 0; var < poslen/4; ++var) {
			propLengths.add(AMEFResources.byteArrayToInt(orig, 9+var*4, 4));
		}
		int start = 0, lindx = 0;
		for (int var = 3; var < jDBObject.size(); var+=2) {
			int ind = jDBObject.getValueAt(var);
			byte[] val = jDBObject.<byte[]>getValueAt(var+1);
			int tlen = propLengths.get(ind);
			int count = start==0?poslen+9:0;
			for (int v1 = lindx; v1 <= ind; ++v1) {
				count += propLengths.get(v1);
			}
			byte[] narr = new byte[count];
			System.arraycopy(orig, start, narr, 0, count);
			lindx = ind+1;
			byte[] nlength = AMEFResources.intToByteArray(val.length, 4);
			narr[9+ind*4] = nlength[0];
			narr[10+ind*4] = nlength[1];
			narr[11+ind*4] = nlength[2];
			narr[12+ind*4] = nlength[3];
			baOs.write(narr);
			baOs.write(val);
			start += count + tlen;
		}
		if(start!=orig.length) {
			for (int i = start; i < orig.length-start; i++) {
				baOs.write(orig[i]);
			}
		}
		byte[] np = baOs.toByteArray();
		if(orig.length!=np.length)
		{
			int clen = np.length - 5;
			byte[] nlength = AMEFResources.intToByteArray(clen, 4);
			np[1] = nlength[0];
			np[2] = nlength[1];
			np[3] = nlength[2];
			np[4] = nlength[3];
		}
		return np;
	}
}
