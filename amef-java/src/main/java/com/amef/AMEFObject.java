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
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author sumeet.chhetri
 * The Automated Message Exchange Format Object type
 * Is a wrapper for basic as well as complex object heirarchies
 * can be a string, number, date, boolean, character or any complex object
 * Every message consists of only one JDBObjectNew *
 */
public final class AMEFObject implements Serializable
{
	private static final long serialVersionUID = 1L;

	public static final char NULL_STRING = 'a';

	public static final char NULL_NUMBER = 'b';

	public static final char NULL_DATE = 'c';

	public static final char NULL_FPN = 'd';

	public static final char NULL_BOOL = 'e';

	public static final char NULL_CHAR = 'f';

	public static final char NULL_OBJECT = 'g';

	public static final char INT_TYPE = 'h';

	public static final char LONG_INT_TYPE = 'i';

	public static final char BOOLEAN_TYPE = 'j';

	public static final char CHAR_TYPE = 'k';

	public static final char DATE_TYPE = 'l';

	public static final char DOUBLE_FLOAT_TYPE = 'm';

	public static final char ASCII_STRING_TYPE = 'n';

	public static final char STRING_TYPE = 'o';

	public static final char OBJECT_TYPE = 'p';

	public static final char ASCII_STRING_TYPE_L = 'q';

	public static final char STRING_TYPE_L = 'r';
	
	public static final String DATE_FMT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
	
	public static final Map<Character, String> TYPE_NAMES = Collections.unmodifiableMap(new HashMap<Character, String>() {
		private static final long serialVersionUID = 1L;
		{put(NULL_STRING, "NULL_STRING");
		put(NULL_NUMBER, "NULL_NUMBER");
		put(NULL_DATE, "NULL_DATE");
		put(NULL_FPN, "NULL_FPN");
		put(NULL_BOOL, "NULL_BOOL");
		put(NULL_CHAR, "NULL_CHAR");
		put(NULL_OBJECT, "NULL_OBJECT");
		put(INT_TYPE, "INT_TYPE");
		put(LONG_INT_TYPE, "LONG_INT_TYPE");
		put(BOOLEAN_TYPE, "BOOLEAN_TYPE");
		put(CHAR_TYPE, "CHAR_TYPE");
		put(DATE_TYPE, "DATE_TYPE");
		put(DOUBLE_FLOAT_TYPE, "DOUBLE_FLOAT_TYPE");
		put(ASCII_STRING_TYPE, "ASCII_STRING_TYPE");
		put(STRING_TYPE, "STRING_TYPE");
		put(OBJECT_TYPE, "OBJECT_TYPE");}
	});

	protected List<Object> packets = new ArrayList<Object>();

	protected List<Character> types = new ArrayList<Character>();

	protected List<byte[]> names = new ArrayList<byte[]>();

	protected int position = 0;

	protected int blength;

	/*The type of the Object can be string, number, date, boolean, character or any complex object*/
	private char type;

	/*The name of the Object if required can be used to represent object properties*/
	private byte[] name;

	protected Object value;

	private ByteArrayOutputStream baOs = new ByteArrayOutputStream();

	private ByteArrayOutputStream baPosOs = new ByteArrayOutputStream();
	
	private final AmefStateProcessor stateProcessor;
	
	public AMEFObject()
	{
		type = OBJECT_TYPE;
		stateProcessor = new StatelessAmefProcessor();
	}
	
	public AMEFObject(boolean stateful)
	{
		type = OBJECT_TYPE;
		if(stateful)
			stateProcessor = new StateFulAmefProcessor();
		else
			stateProcessor = new StatelessAmefProcessor();
	}

	private byte[] binaryRepr(String value)
	{
		try {
			return value.getBytes("ISO-8859-1");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void writeData(byte[] data, boolean writeLength, int lenlen)
	{
		try {
			if(writeLength) {
				AMEFResources.intToByteArray(baOs, data.length, lenlen);
			}
			baOs.write(data);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private int writeName(String name)
	{
		try {
			if(name==null) {
				AMEFResources.intToByteArray(baOs, 0, 1);
				return 2;
			} else {
				byte[] arr = binaryRepr(name);
				AMEFResources.intToByteArray(baOs, arr.length, 1);
				baOs.write(arr);
				return arr.length + 2;
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private void writeHeader(char type, String name, int valLength)
	{
		baOs.write(type);
		int len = writeName(name) + valLength;
		AMEFResources.intToByteArray(baPosOs, len, 4);
	}

	public void addNullPacket(char type)
	{
		writeHeader(type, null, 0);
		stateProcessor.onAddPacket(this, null, null, type);
	}

	public void addNullPacket(char type, String name)
	{
		writeHeader(type, name, 0);
		stateProcessor.onAddPacket(this, null, name, type);
	}

	public void addPacket(byte[] string)
	{
		if(string!=null) {
			char typ = string.length>8388607?STRING_TYPE_L:STRING_TYPE;
			int exlen = string.length>8388607?4:3;
			writeHeader(typ, null, string.length+exlen);
			writeData(string, true, exlen);
			stateProcessor.onAddPacket(this, string, null, typ);
		} else {
			addNullPacket(NULL_STRING);
		}
	}

	public void addPacket(byte[] string, String name)
	{
		if(name!=null) {
			char typ = string.length>8388607?STRING_TYPE_L:STRING_TYPE;
			int exlen = string.length>8388607?4:3;
			writeHeader(typ, name, string.length+exlen);
			writeData(string, true, exlen);
			stateProcessor.onAddPacket(this, string, name, typ);
		} else {
			addPacket(string);
		}
	}

	public void addPacket(String string)
	{
		if(string!=null) {
			byte[] arr = binaryRepr(string);
			char typ = arr.length>8388607?ASCII_STRING_TYPE_L:ASCII_STRING_TYPE;
			int exlen = arr.length>8388607?4:3;
			writeHeader(typ, null, arr.length+exlen);
			writeData(arr, true, exlen);
			stateProcessor.onAddPacket(this, string, null, typ);
		} else {
			addNullPacket(NULL_STRING);
		}
	}

	public void addPacket(String string, String name)
	{
		if(name!=null) {
			byte[] arr = binaryRepr(string);
			char typ = arr.length>8388607?ASCII_STRING_TYPE_L:ASCII_STRING_TYPE;
			int exlen = arr.length>8388607?4:3;
			writeHeader(typ, name, arr.length+exlen);
			writeData(arr, true, exlen);
			stateProcessor.onAddPacket(this, string, name, typ);
		} else {
			addPacket(string);
		}
	}

	public void addPacket(Date date)
	{
		if(date!=null) {
			SimpleDateFormat format = new SimpleDateFormat(DATE_FMT);
			byte[] arr = binaryRepr(format.format(date));
			writeHeader(DATE_TYPE, null, arr.length+3);
			writeData(arr, true, 3);
			stateProcessor.onAddPacket(this, date, null, DATE_TYPE);
		} else {
			addNullPacket(NULL_DATE);
		}
	}

	public void addPacket(Date date, String name)
	{
		if(name!=null) {
			SimpleDateFormat format = new SimpleDateFormat(DATE_FMT);
			byte[] arr = binaryRepr(format.format(date));
			writeHeader(DATE_TYPE, name, arr.length+3);
			writeData(arr, true, 3);
			stateProcessor.onAddPacket(this, date, name, DATE_TYPE);
		} else {
			addPacket(date);
		}
	}

	public void addPacket(Long lon)
	{
		if(lon!=null) {
			if(lon<4294967296L)
			{
				writeHeader(INT_TYPE, null, 4);
				AMEFResources.intToByteArray(baOs, lon, 4);
				stateProcessor.onAddPacket(this, lon, null, INT_TYPE);
			}
			else
			{
				writeHeader(LONG_INT_TYPE, null, 8);
				AMEFResources.intToByteArray(baOs, lon, 8);
				stateProcessor.onAddPacket(this, lon, null, LONG_INT_TYPE);
			}
		} else {
			addNullPacket(NULL_NUMBER);
		}
	}

	public void addPacket(Long lon, String name)
	{
		if(name!=null) {
			if(lon<4294967296L)
			{
				writeHeader(INT_TYPE, name, 4);
				AMEFResources.intToByteArray(baOs, lon, 4);
				stateProcessor.onAddPacket(this, lon, name, INT_TYPE);
			}
			else
			{
				writeHeader(LONG_INT_TYPE, name, 8);
				AMEFResources.intToByteArray(baOs, lon, 8);
				stateProcessor.onAddPacket(this, lon, name, LONG_INT_TYPE);
			}
		} else {
			addPacket(lon);
		}
	}

	public void addPacket(Integer lon)
	{
		if(lon!=null) {
			addPacket((long)lon);
		} else {
			addNullPacket(NULL_NUMBER);
		}
	}

	public void addPacket(Integer lon, String name)
	{
		if(lon!=null) {
			addPacket((long)lon, name);
		} else {
			addNullPacket(NULL_NUMBER, name);
		}
	}

	public void addPacket(Short lon)
	{
		if(lon!=null) {
			addPacket((long)lon);
		} else {
			addNullPacket(NULL_NUMBER);
		}
	}

	public void addPacket(Short lon, String name)
	{
		if(lon!=null) {
			addPacket((long)lon, name);
		} else {
			addNullPacket(NULL_NUMBER, name);
		}
	}

	public void addPacket(Double doub)
	{
		if(doub!=null) {
			String val = String.valueOf(doub);
			byte[] arr = binaryRepr(val);
			writeHeader(DOUBLE_FLOAT_TYPE, null, arr.length+3);
			writeData(arr, true, 3);
			stateProcessor.onAddPacket(this, doub, null, DOUBLE_FLOAT_TYPE);
		} else {
			addNullPacket(NULL_FPN);
		}
	}

	public void addPacket(Double doub, String name)
	{
		if(name!=null) {
			String val = String.valueOf(doub);
			byte[] arr = binaryRepr(val);
			writeHeader(DOUBLE_FLOAT_TYPE, name, arr.length+3);
			writeData(arr, true, 3);
			stateProcessor.onAddPacket(this, doub, name, DOUBLE_FLOAT_TYPE);
		} else {
			addPacket(doub);
		}
	}

	public void addPacket(Float doub)
	{
		if(doub!=null) {
			String val = String.valueOf(doub);
			byte[] arr = binaryRepr(val);
			writeHeader(DOUBLE_FLOAT_TYPE, null, arr.length+3);
			writeData(arr, true, 3);
			stateProcessor.onAddPacket(this, doub, null, DOUBLE_FLOAT_TYPE);
		} else {
			addNullPacket(NULL_FPN);
		}
	}

	public void addPacket(Float doub, String name)
	{
		if(name!=null) {
			String val = String.valueOf(doub);
			byte[] arr = binaryRepr(val);
			writeHeader(DOUBLE_FLOAT_TYPE, name, arr.length+3);
			writeData(arr, true, 3);
			stateProcessor.onAddPacket(this, doub, name, DOUBLE_FLOAT_TYPE);
		} else {
			addPacket(doub);
		}
	}

	public void addPacket(Character doub)
	{
		if(doub!=null) {
			writeHeader(CHAR_TYPE, null, 1);
			baOs.write((byte)doub.charValue());
			stateProcessor.onAddPacket(this, doub, null, CHAR_TYPE);
		} else {
			addNullPacket(NULL_CHAR);
		}
	}

	public void addPacket(Character doub, String name)
	{
		if(name!=null) {
			writeHeader(CHAR_TYPE, name, 1);
			baOs.write((byte)doub.charValue());
			stateProcessor.onAddPacket(this, doub, name, CHAR_TYPE);
		} else {
			addPacket(doub);
		}
	}

	public void addPacket(Byte lon)
	{
		if(lon!=null) {
			addPacket((long)lon);
		} else {
			addNullPacket(NULL_NUMBER);
		}
	}

	public void addPacket(Byte lon, String name)
	{
		if(lon!=null) {
			addPacket((long)lon, name);
		} else {
			addNullPacket(NULL_NUMBER, name);
		}
	}

	public void addPacket(Boolean doub)
	{
		if(doub!=null) {
			writeHeader(BOOLEAN_TYPE, null, 1);
			baOs.write(doub.booleanValue()?'1':'0');
			stateProcessor.onAddPacket(this, doub, null, BOOLEAN_TYPE);
		} else {
			addNullPacket(NULL_BOOL);
		}
	}

	public void addPacket(Boolean doub, String name)
	{
		if(name!=null) {
			writeHeader(BOOLEAN_TYPE, name, 1);
			baOs.write(doub.booleanValue()?'1':'0');
			stateProcessor.onAddPacket(this, doub, name, BOOLEAN_TYPE);
		} else {
			addPacket(doub);
		}
	}

	public void addPacket(AMEFObject ob)
	{
		if(ob!=null) {
			ob.resetState();
			int tsize = 4+ob.baPosOs.size()+ob.baOs.size();
			writeHeader(OBJECT_TYPE, null, tsize+2);
			AMEFResources.intToByteArray(baOs, tsize, 4);
			AMEFResources.intToByteArray(baOs, ob.baPosOs.size(), 4);
			try {
				baOs.write(ob.baPosOs.toByteArray());
				baOs.write(ob.baOs.toByteArray());
			} catch (IOException e) {
			}
			stateProcessor.onAddPacket(this, ob, null, OBJECT_TYPE);
		} else {
			addNullPacket(NULL_OBJECT);
		}
	}

	public void addPacket(AMEFObject ob, String name)
	{
		if(ob==null) {
			addNullPacket(NULL_OBJECT, name);
		} else if(name!=null) {
			ob.resetState();
			int tsize = 4+ob.baPosOs.size()+ob.baOs.size();
			writeHeader(OBJECT_TYPE, name, tsize+2);
			AMEFResources.intToByteArray(baOs, tsize, 4);
			AMEFResources.intToByteArray(baOs, ob.baPosOs.size(), 4);
			try {
				baOs.write(ob.baPosOs.toByteArray());
				baOs.write(ob.baOs.toByteArray());
			} catch (IOException e) {
			}
			stateProcessor.onAddPacket(this, ob, name, OBJECT_TYPE);
		} else {
			addPacket(ob);
		}
	}

	public void addNumber(Long number, String name)
	{
		addPacket(number, name);
	}

	public void addNumber(Long number)
	{
		addPacket(number);
	}

	public void addNumber(Integer number, String name)
	{
		addPacket(number, name);
	}

	public void addNumber(Integer number)
	{
		addPacket(number);
	}

	public void addNumber(Short number, String name)
	{
		addPacket(number, name);
	}

	public void addNumber(Short number)
	{
		addPacket(number);
	}

	public void addFloatingPointNumber(Double number, String name)
	{
		addPacket(number, name);
	}

	public void addFloatingPointNumber(Double number)
	{
		addPacket(number);
	}

	public void addFloatingPointNumber(Float number, String name)
	{
		addPacket(number, name);
	}

	public void addFloatingPointNumber(Float number)
	{
		addPacket(number);
	}

	public void addDate(Date date, String name)
	{
		addPacket(date, name);
	}

	public void addDate(Date date)
	{
		addPacket(date);
	}

	public void addString(String strin, String name)
	{
		addPacket(strin, name);
	}

	public void addString(String strin)
	{
		addPacket(strin);
	}

	public void addBoolean(Boolean bool, String name)
	{
		addPacket(bool, name);
	}

	public void addBoolean(Boolean bool)
	{
		addPacket(bool);
	}

	public char getType() {
		return type;
	}

	public void setType(char type) {
		this.type = type;
	}

	public byte[] getName() {
		return name;
	}

	public void setName(byte[] name) {
		this.name = name;
	}

	public void setName(String name) {
		try {
			this.name = name.getBytes("ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
		}
	}

	public byte[] getObjectRepr(boolean considerLength)
	{
		resetState();
		byte[] finalData = null;
		if(considerLength) {
			int tsize = 4+1+4+4+baPosOs.size()+baOs.size();
			finalData = new byte[tsize];
			AMEFResources.intToByteArray(finalData, 0, tsize-4, 4);
			finalData[4] = OBJECT_TYPE;
			AMEFResources.intToByteArray(finalData, 5, baOs.size()+4+baPosOs.size(), 4);
			AMEFResources.intToByteArray(finalData, 9, baPosOs.size(), 4);
			System.arraycopy(baPosOs.toByteArray(), 0, finalData, 13, baPosOs.size());
			System.arraycopy(baOs.toByteArray(), 0, finalData, 13+baPosOs.size(), baOs.size());
		} else {
			int tsize = 1+4+4+baPosOs.size()+baOs.size();
			finalData = new byte[tsize];
			finalData[0] = OBJECT_TYPE;
			AMEFResources.intToByteArray(finalData, 1, baOs.size()+4+baPosOs.size(), 4);
			AMEFResources.intToByteArray(finalData, 5, baPosOs.size(), 4);
			System.arraycopy(baPosOs.toByteArray(), 0, finalData, 9, baPosOs.size());
			System.arraycopy(baOs.toByteArray(), 0, finalData, 9+baPosOs.size(), baOs.size());
		}
		return finalData;
	}

	public byte[] getRepr()
	{
		resetState();
		return baOs.toByteArray();
	}
	
	private void resetState()
	{
		if(packets.size()>0) {
			baOs.reset();
			baPosOs.reset();
			
			List<Character> ttypes = new ArrayList<Character>(types);
			types.clear();
			
			List<byte[]> tnames = new ArrayList<byte[]>(names);
			names.clear();
			
			List<Object> tpackets = new ArrayList<Object>(packets);
			packets.clear();
			
			for (int i=0;i<ttypes.size();i++) {
				Character type = ttypes.get(i);
				String name = null;
				if(tnames.get(i)!=null) {
					try {
						name = new String(tnames.get(i), "ISO-8859-1");
					} catch (UnsupportedEncodingException e) {
					}
				}
				if(type<AMEFObject.INT_TYPE)
				{
					addNullPacket(type, name);
				}
				else if(type<AMEFObject.BOOLEAN_TYPE)
				{
					if(type==AMEFObject.LONG_INT_TYPE) {
						addPacket(((Number)tpackets.get(i)).longValue(), name);
					} else {
						addPacket(((Number)tpackets.get(i)).intValue(), name);
					}
				}
				else if(type<AMEFObject.DATE_TYPE)
				{
					if(type==AMEFObject.BOOLEAN_TYPE) {
						addPacket((Boolean)tpackets.get(i), name);
					} else {
						if(tpackets.get(i) instanceof Byte) {
							addPacket((char)((Byte)tpackets.get(i) & 0xFF), name);
						} else {
							addPacket((Character)tpackets.get(i), name);
						}
					}
				}
				else if(type<AMEFObject.OBJECT_TYPE)
				{
					if(type==AMEFObject.STRING_TYPE) {
						addPacket((byte[])tpackets.get(i), name);
					} else if(type==AMEFObject.ASCII_STRING_TYPE) {
						addPacket((String)tpackets.get(i), name);
					} else if(type==AMEFObject.STRING_TYPE_L) {
						addPacket((byte[])tpackets.get(i), name);
					} else if(type==AMEFObject.ASCII_STRING_TYPE_L) {
						addPacket((String)tpackets.get(i), name);
					} else if(type==AMEFObject.DATE_TYPE) {
						addPacket((Date)tpackets.get(i), name);
					} else if(type==AMEFObject.DOUBLE_FLOAT_TYPE) {
						if(tpackets.get(i) instanceof Float) {
							addPacket((Float)tpackets.get(i), name);
						} else {
							addPacket((Double)tpackets.get(i), name);
						}
					}
				}
				else if(type==AMEFObject.OBJECT_TYPE)
				{
					addPacket((AMEFObject)tpackets.get(i), name);
				}
			}
		}
	}
	
	public String getStringRepr(String indent)
	{
		if(indent==null)indent = "";
		StringBuilder build = new StringBuilder();
		if(packets.size()>0) {
			for (int i=0;i<types.size();i++) {
				Character type = types.get(i);
				String name = null;
				if(names.get(i)!=null) {
					try {
						name = new String(names.get(i), "ISO-8859-1");
					} catch (UnsupportedEncodingException e) {
					}
				}
				
				build.append(indent);
				build.append("type=");
				build.append(TYPE_NAMES.containsKey(type)?TYPE_NAMES.get(type):type);
				build.append(", name=");
				build.append(name);
				build.append(", value=");
				
				if(type<AMEFObject.INT_TYPE) {
				}
				else if(type<AMEFObject.BOOLEAN_TYPE)
				{
					if(type==AMEFObject.LONG_INT_TYPE) {
						build.append(((Number)packets.get(i)).longValue());
					} else {
						build.append(((Number)packets.get(i)).intValue());
					}
				}
				else if(type<AMEFObject.DATE_TYPE)
				{
					if(type==AMEFObject.BOOLEAN_TYPE) {
						build.append((Boolean)packets.get(i));
					} else {
						if(packets.get(i) instanceof Byte) {
							build.append((Byte)packets.get(i));
						} else {
							build.append((Character)packets.get(i));
						}
					}
				}
				else if(type<AMEFObject.OBJECT_TYPE)
				{
					if(type==AMEFObject.STRING_TYPE) {
						build.append((byte[])packets.get(i));
					} else if(type==AMEFObject.ASCII_STRING_TYPE) {
						build.append((String)packets.get(i));
					} else if(type==AMEFObject.STRING_TYPE_L) {
						build.append((byte[])packets.get(i));
					} else if(type==AMEFObject.ASCII_STRING_TYPE_L) {
						build.append((String)packets.get(i));
					} else if(type==AMEFObject.DATE_TYPE) {
						build.append((Date)packets.get(i));
					} else if(type==AMEFObject.DOUBLE_FLOAT_TYPE) {
						if(packets.get(i) instanceof Float) {
							build.append((Float)packets.get(i));
						} else {
							build.append((Double)packets.get(i));
						}
					}
				}
				else if(type==AMEFObject.OBJECT_TYPE)
				{
					AMEFObject o = (AMEFObject)packets.get(i);
					if(o==null) {
						build.append("null");
					} else {
						build.append("\n");
						if(o.stateProcessor instanceof StatelessAmefProcessor) {
							byte[] arr = o.getObjectRepr(false);
							try {
								o = new AMEFDecoder().decodeB(arr, false);
							} catch (AMEFDecodeException e) {
								build.append("error\n");
								continue;
							}
						}
						build.append(o.getStringRepr(indent+"\t"));
					}
				}
				build.append("\n");
			}
		}
		return build.toString();
	}

	@SuppressWarnings("unchecked")
	public <T> T getValueAt(int position)
	{
		if(position>=0 && position<packets.size()) {
			return (T)packets.get(position);
		}
		return null;
	}

	public String getNameAt(int position)
	{
		if(position>=0 && position<names.size() && names.get(position)!=null) {
			try {
				return new String(names.get(position), "ISO-8859-1");
			} catch (UnsupportedEncodingException e) {
			}
		}
		return null;
	}

	public Character getTypeAt(int position)
	{
		if(position>=0 && position<types.size()) {
			return types.get(position);
		}
		return null;
	}

	public int size()
	{
		return packets.size();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (names == null ? 0 : names.hashCode());
		result = prime * result + (packets == null ? 0 : packets.hashCode());
		result = prime * result + (types == null ? 0 : types.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		AMEFObject other = (AMEFObject) obj;
		if (names == null) {
			if (other.names != null) {
				return false;
			}
		} else if (!names.equals(other.names)) {
			return false;
		}
		if (packets == null) {
			if (other.packets != null) {
				return false;
			}
		} else if (!packets.equals(other.packets)) {
			return false;
		}
		if (types == null) {
			if (other.types != null) {
				return false;
			}
		} else if (!types.equals(other.types)) {
			return false;
		}
		return true;
	}
	
	private interface AmefStateProcessor {
		void onAddPacket(AMEFObject amef, Object ob, String name, char type);
	}
	
	private class StatelessAmefProcessor implements AmefStateProcessor {
		public void onAddPacket(AMEFObject amef, Object ob, String name, char type) {
			//No operation
		}
	}
	
	private class StateFulAmefProcessor implements AmefStateProcessor {
		public void onAddPacket(AMEFObject amef, Object ob, String name, char type) {
			amef.packets.add(ob);
			if(name==null) {
				amef.names.add(null);
			} else {
				try {
					amef.names.add(name.getBytes("ISO-8859-1"));
				} catch (UnsupportedEncodingException e) {
				}
			}
			amef.types.add(type);
		}
	}
	
	public int getEncodedSizeEstimate(boolean considerLength) {
		resetState();
		if(considerLength) {
			return 4+1+4+4+baPosOs.size()+baOs.size();
		} else {
			return 1+4+4+baPosOs.size()+baOs.size();
		}
	}
}
