/*
 * JDBDecoder.h
 *
 *  Created on: Mar 7, 2011
 *      Author: chhetri.sumeet
 */

#ifndef JDBDECODER_H_
#define JDBDECODER_H_

#include "JDBObject.h"
#include "iostream"
class JDBDecoder
{


public:
	~JDBDecoder();
	JDBDecoder();
	static char* longTocharArray(long l,int ind)
		{
			char* result = new char[ind];
			for (int i = 0; i<ind; i++)
			{
				int offset = (ind - 1 - i) * 8;
	            result[i] = (char) ((l >> offset) & 0xFF);
			}
	        return result;
	    }

		static string longTocharArrayS(long l,int ind)
		{
			char* result = new char[ind];
			for (int i = 0; i<ind; i++)
			{
				int offset = (ind - 1 - i) * 8;
	            result[i] = (char) ((l >> offset) & 0xFF);
			}
			string tem(result);
			return tem;
	    }

		static char* intTocharArray(int l,int ind)
		{
			char* result = new char[ind];
			for (int i = 0; i<ind; i++)
			{
				int offset = (ind - 1 - i) * 8;
	            result[i] = (char) ((l >> offset) & 0xFF);
			}
	        return result;
	    }

		static char* intTocharArrayWI(int l)
		{
			int ind = 1;
			if(l<256)
				ind =1;
			else if(l<65536)
				ind = 2;
			else if(l<16777216)
				ind =3;
			else
				ind =4;
			char* result = new char[ind];
			for (int i = 0; i<ind; i++)
			{
				int offset = (ind - 1 - i) * 8;
	            result[i] = (char) ((l >> offset) & 0xFF);
			}
	        return result;
	    }

		static int charArrayToInt(char l[])
		{
			int t = 0;
			int ind = sizeof l;
	        for (int i = 0; i < ind; i++)
			{
	        	int offset = (ind -1 - i) * 8;
	        	t += (l[i] & 0x000000FF) << offset;
			}
	        return t;
	    }

		static int charArrayToInt(string l,int off,int ind)
		{
			int t = 0;
			for (int i = 0; i < ind; i++)
			{
	        	int offset = (ind -1 - i) * 8;
	        	t += (l[off+i] & 0x000000FF) << offset;
			}
	        return t;
	    }

		static long charArrayToLong(char l[])
		{
			long t = 0;
			int ind = sizeof l;
	        for (int i = 0; i < ind; i++)
			{
	        	int offset = (ind -1 - i) * 8;
	        	t += (l[i] & 0x000000FF) << offset;
			}
	        return t;
	    }
		static long charArrayToLong(char* l,int off,int ind)
		{
			long t = 0;
			for (int i = 0; i < ind; i++)
			{
	        	int offset = (ind -1 - i) * 8;
	        	t += (l[off+i] & 0x000000FF) << offset;
			}
	        return t;
	    }
		static long charArrayToLong(char* l,int ind)
		{
			long t = 0;
			for (int i = 0; i < ind; i++)
			{
	        	int offset = (ind -1 - i) * 8;
	        	t += (l[i] & 0x000000FF) << offset;
			}
	        return t;
	    }

		static string intTocharArrayS(int l, int ind)
		{
			char* result = new char[ind];
			for (int i = 0; i<ind; i++)
			{
				int offset = (ind - 1 - i) * 8;
	            result[i] = (char) ((l >> offset) & 0xFF);
			}
			string tem(result);
	        return tem;
	    }

	JDBObject* decodeB(string buffer,bool considerLength,bool ignoreName)
	{
		position = 0;
		string strdata;
		if(considerLength)
		{
			strdata = buffer.substr(4);
		}
		else
		{
			strdata = buffer;
		}
		JDBObject* JDBObject = decodeSinglePacketB(strdata,ignoreName);
		return JDBObject;
	}

	JDBObject* decodeBWL(string buffer,bool ignoreName)
	{
		position = 0;
		JDBObject* JDBObject = decodeSinglePacketB(buffer,ignoreName);
		return JDBObject;
	}



	/*JDBObject decodeJDBString(char* data,bool considerLength)
	{
		int startWith = 0;
		if(considerLength)
			startWith = 4;
		String strdata = new String(data);
		strdata = strdata.substring(startWith);
		JDBObject JDBObject = decodeSingleJDBStringPacket(strdata);
		return JDBObject;
	}

	JDBObject decodeSingleJDBStringPacket(String strdata)
	{
		JDBObject jDBObject = null;
		char type = strdata.charAt(0);
		if(type==JDBObject::STRING_TYPE)
		{
			jDBObject = new JDBObject();
			jDBObject->setType(type);
			int lengthm = ((strdata.charAt(1) & 0xff) << 24) | ((strdata.charAt(2) & 0xff) << 16)
							| ((strdata.charAt(3) & 0xff) << 8) | ((strdata.charAt(4) & 0xff));
			jDBObject->setLength(lengthm);
			String value = strdata.substring(5,5+lengthm);
			jDBObject->setValue(value,lengthm);
			tempVal = strdata.substring(5+lengthm);
		}
		else if(type==JDBObject::STRING_65536_TYPE)
		{
			jDBObject = new JDBObject();
			jDBObject->setType(type);
			int lengthm = ((strdata.charAt(1) & 0xff) << 8) | (strdata.charAt(2) & 0xff);
			jDBObject->setLength(lengthm);
			String value = strdata.substring(3,3+lengthm);
			jDBObject->setValue(value,lengthm);
			tempVal = strdata.substring(3+lengthm);
		}
		else if(type==JDBObject::STRING_16777216_TYPE)
		{
			jDBObject = new JDBObject();
			jDBObject->setType(type);
			int lengthm = ((strdata.charAt(1) & 0xff) << 16)
							| ((strdata.charAt(2) & 0xff) << 8) | (strdata.charAt(3) & 0xff);
			jDBObject->setLength(lengthm);
			String value = strdata.substring(3,3+lengthm);
			jDBObject->setValue(value,lengthm);
			tempVal = strdata.substring(3+lengthm);
		}
		else if(type==JDBObject::DATE_TYPE || type==JDBObject::STRING_256_TYPE || type==JDBObject::DOUBLE_FLOAT_TYPE)
		{
			jDBObject = new JDBObject();
			jDBObject->setType(type);
			int lengthm = strdata.charAt(1) & 0xff;
			jDBObject->setLength(lengthm);
			String value = strdata.substring(2,2+lengthm);
			jDBObject->setValue(value,lengthm);
			tempVal = strdata.substring(2+lengthm);
		}
		else if(type==JDBObject::VERY_SMALL_INT_TYPE)
		{
			jDBObject = new JDBObject();
			jDBObject->setType(type);
			String value = (strdata.charAt(1) & 0xff) + "";
			jDBObject->setValue(value,lengthm);
			tempVal = strdata.substring(2);
		}
		else if(type==JDBObject::SMALL_INT_TYPE)
		{
			jDBObject = new JDBObject();
			jDBObject->setType(type);
			int lengthm = ((strdata.charAt(1) & 0xff) << 8) | ((strdata.charAt(2) & 0xff));
			String value = lengthm + "";
			jDBObject->setValue(value,lengthm);
			tempVal = strdata.substring(3);
		}
		else if(type==JDBObject::BIG_INT_TYPE)
		{
			jDBObject = new JDBObject();
			jDBObject->setType(type);
			int lengthm = ((strdata.charAt(1) & 0xff) << 16) | ((strdata.charAt(2) & 0xff) << 8)
							| ((strdata.charAt(3) & 0xff));
			String value = lengthm + "";
			jDBObject->setValue(value,lengthm);
			tempVal = strdata.substring(4);
		}
		else if(type==JDBObject::INT_TYPE)
		{
			jDBObject = new JDBObject();
			jDBObject->setType(type);
			int lengthm = ((strdata.charAt(1) & 0xff) << 24) | ((strdata.charAt(2) & 0xff) << 16)
							| ((strdata.charAt(3) & 0xff) << 8) | ((strdata.charAt(4) & 0xff));
			String value = lengthm + "";
			jDBObject->setValue(value,lengthm);
			tempVal = strdata.substring(5);
		}
		else if(type==JDBObject::VS_LONG_INT_TYPE)
		{
			jDBObject = new JDBObject();
			jDBObject->setType(type);
			long lengthm = ((strdata.charAt(1) & 0xff) << 32) | ((strdata.charAt(2) & 0xff) << 24)
							| ((strdata.charAt(3) & 0xff) << 16) | ((strdata.charAt(4) & 0xff) << 8)
							| ((strdata.charAt(5) & 0xff));
			String value = lengthm + "";
			jDBObject->setValue(value,lengthm);
			tempVal = strdata.substring(6);
		}
		else if(type==JDBObject::S_LONG_INT_TYPE)
		{
			jDBObject = new JDBObject();
			jDBObject->setType(type);
			long lengthm = ((strdata.charAt(1) & 0xff) << 40) | ((strdata.charAt(2) & 0xff) << 32)
							| ((strdata.charAt(3) & 0xff) << 24)
							| ((strdata.charAt(4) & 0xff) << 16) | ((strdata.charAt(5) & 0xff) << 8)
							| ((strdata.charAt(6) & 0xff));
			String value = lengthm + "";
			jDBObject->setValue(value,lengthm);
			tempVal = strdata.substring(7);
		}
		else if(type==JDBObject::B_LONG_INT_TYPE)
		{
			jDBObject = new JDBObject();
			jDBObject->setType(type);
			long lengthm = ((strdata.charAt(1) & 0xff) << 48) | ((strdata.charAt(2) & 0xff) << 40)
							| ((strdata.charAt(3) & 0xff) << 32)
							| ((strdata.charAt(4) & 0xff) << 24)
							| ((strdata.charAt(5) & 0xff) << 16) | ((strdata.charAt(6) & 0xff) << 8)
							| ((strdata.charAt(7) & 0xff));
			String value = lengthm + "";
			jDBObject->setValue(value,lengthm);
			tempVal = strdata.substring(8);
		}
		else if(type==JDBObject::LONG_INT_TYPE)
		{
			jDBObject = new JDBObject();
			jDBObject->setType(type);
			long lengthm = ((strdata.charAt(1) & 0xff) << 56) | ((strdata.charAt(2) & 0xff) << 48)
							| ((strdata.charAt(3) & 0xff) << 40)
							| ((strdata.charAt(4) & 0xff) << 32)
							| ((strdata.charAt(5) & 0xff) << 24)
							| ((strdata.charAt(6) & 0xff) << 16) | ((strdata.charAt(7) & 0xff) << 8)
							| ((strdata.charAt(8) & 0xff));
			String value = lengthm + "";
			jDBObject->setValue(value,lengthm);
			tempVal = strdata.substring(9);
		}
		else if(type==JDBObject::BOOLEAN_TYPE || type==JDBObject::CHAR_TYPE)
		{
			jDBObject = new JDBObject();
			jDBObject->setType(type);
			jDBObject->setLength(1);
			String value = strdata.charAt(1)+"";
			jDBObject->setValue(value,lengthm);
			tempVal = strdata.substring(2);
		}
		else if(type==JDBObject::VS_OBJECT_TYPE)
		{
			jDBObject = new JDBObject();
			jDBObject->setType(type);
			int lengthm = charArrayToInt(strdata.substring(1,2).getchars());
			jDBObject->setLength(lengthm);
			tempVal = strdata.substring(2,2+lengthm);
			while(!tempVal.equals(""))
			{
				JDBObject obj = decodeSingleJDBStringPacket(tempVal);
				jDBObject->addPacket(obj);
			}
			tempVal = strdata.substring(2+lengthm);
		}
		else if(type==JDBObject::S_OBJECT_TYPE)
		{
			jDBObject = new JDBObject();
			jDBObject->setType(type);
			int lengthm = ((strdata.charAt(1) & 0xff) << 8) | ((strdata.charAt(2) & 0xff));
			jDBObject->setLength(lengthm);
			tempVal = strdata.substring(3,3+lengthm);
			while(!tempVal.equals(""))
			{
				JDBObject obj = decodeSingleJDBStringPacket(tempVal);
				jDBObject->addPacket(obj);
			}
			tempVal = strdata.substring(3+lengthm);
		}
		else if(type==JDBObject::B_OBJECT_TYPE)
		{
			jDBObject = new JDBObject();
			jDBObject->setType(type);
			int lengthm = ((strdata.charAt(1) & 0xff) << 16)
							| ((strdata.charAt(2) & 0xff) << 8) | ((strdata.charAt(3) & 0xff));
			jDBObject->setLength(lengthm);
			tempVal = strdata.substring(4,4+lengthm);
			while(!tempVal.equals(""))
			{
				JDBObject obj = decodeSingleJDBStringPacket(tempVal);
				jDBObject->addPacket(obj);
			}
			tempVal = strdata.substring(4+lengthm);
		}
		else if(type==JDBObject::OBJECT_TYPE)
		{
			jDBObject = new JDBObject();
			jDBObject->setType(type);
			int lengthm = ((strdata.charAt(1) & 0xff) << 24) | ((strdata.charAt(2) & 0xff) << 16)
							| ((strdata.charAt(3) & 0xff) << 8) | ((strdata.charAt(4) & 0xff));
			jDBObject->setLength(lengthm);
			tempVal = strdata.substring(5,5+lengthm);
			while(!tempVal.equals(""))
			{
				JDBObject obj = decodeSingleJDBStringPacket(tempVal);
				jDBObject->addPacket(obj);
			}
			tempVal = strdata.substring(5+lengthm);
		}
		return jDBObject;
	}

	*//**
	 * @param strdata
	 * @return JDBObject
	 * @throws AMEFDecodeException
	 * decode the string to give the equivalent JDBObject
	 * @throws UnsupportedEncodingException
	 *//*
	JDBObject decodeSinglePacket(String strdata,bool ignoreName), UnsupportedEncodingException
	{
		JDBObject jDBObject = null;
		char type = strdata.charAt(0);
		if(type==JDBObject::STRING_TYPE)
		{
			if(strdata.charAt(1)==',')
			{
				jDBObject = new JDBObject();
				jDBObject->setType(type);
				int pos = 2;
				String name = "";
				if(!ignoreName)
				{
					while(strdata.charAt(pos)!=',')
					{
						name += strdata.charAt(pos++);
					}
					if(name.equals("") && strdata.charAt(2)!=',')
					{
						throw new AMEFDecodeException("Invalid character after name specifier, expected ,");
					}
				}
				if(pos>=strdata.length())
				{
					throw new AMEFDecodeException("Reached end of AMEF string, not found ,");
				}
				jDBObject->setName(buffer.substr(st,en-st));}
				String length = "";
				if(!ignoreName)pos++;
				while(length.length()<4)
				{
					length += strdata.charAt(pos++);
				}
				if(length.equals("") && strdata.charAt(3)!=',')
				{
					throw new AMEFDecodeException("Invalid character after length specifier, expected ,");
				}
				else if(pos>=strdata.length())
				{
					throw new AMEFDecodeException("Reached end of AMEF string, not found ,");
				}
				int lengthm = ((length.getchars()[0] & 0xff) << 24) | ((length.getchars()[1] & 0xff) << 16)
								| ((length.getchars()[2] & 0xff) << 8) | ((length.getchars()[3] & 0xff));
				jDBObject->setLength(lengthm);
				String value = strdata.substring(pos+1,pos+lengthm+1);
				jDBObject->setValue(value,lengthm);
				tempVal = strdata.substring(pos+lengthm+1);
			}
			else
			{
				throw new AMEFDecodeException("Invalid character after type specifier, expected ,");
			}
		}
		else if(type==JDBObject::STRING_65536_TYPE)
		{
			if(strdata.charAt(1)==',')
			{
				jDBObject = new JDBObject();
				jDBObject->setType(type);
				int pos = 2;
				String name = "";
				if(!ignoreName)
				{
					while(strdata.charAt(pos)!=',')
					{
						name += strdata.charAt(pos++);
					}
					if(name.equals("") && strdata.charAt(2)!=',')
					{
						throw new AMEFDecodeException("Invalid character after name specifier, expected ,");
					}
				}
				if(pos>=strdata.length())
				{
					throw new AMEFDecodeException("Reached end of AMEF string, not found ,");
				}
				jDBObject->setName(buffer.substr(st,en-st));}
				String length = "";
				if(!ignoreName)pos++;
				while(length.length()<2)
				{
					length += strdata.charAt(pos++);
				}
				if(length.equals("") && strdata.charAt(3)!=',')
				{
					throw new AMEFDecodeException("Invalid character after length specifier, expected ,");
				}
				else if(pos>=strdata.length())
				{
					throw new AMEFDecodeException("Reached end of AMEF string, not found ,");
				}
				int lengthm = ((length.getchars()[0] & 0xff) << 8) | ((length.getchars()[1] & 0xff));
				jDBObject->setLength(lengthm);
				String value = strdata.substring(pos,pos+lengthm);
				jDBObject->setValue(value,lengthm);
				tempVal = strdata.substring(pos+lengthm);
			}
			else
			{
				throw new AMEFDecodeException("Invalid character after type specifier, expected ,");
			}
		}
		else if(type==JDBObject::STRING_16777216_TYPE)
		{
			if(strdata.charAt(1)==',')
			{
				jDBObject = new JDBObject();
				jDBObject->setType(type);
				int pos = 2;
				String name = "";
				if(!ignoreName)
				{
					while(strdata.charAt(pos)!=',')
					{
						name += strdata.charAt(pos++);
					}
					if(name.equals("") && strdata.charAt(2)!=',')
					{
						throw new AMEFDecodeException("Invalid character after name specifier, expected ,");
					}
				}
				if(pos>=strdata.length())
				{
					throw new AMEFDecodeException("Reached end of AMEF string, not found ,");
				}
				jDBObject->setName(buffer.substr(st,en-st));}
				String length = "";
				if(!ignoreName)pos++;
				while(length.length()<3)
				{
					length += strdata.charAt(pos++);
				}
				if(length.equals("") && strdata.charAt(3)!=',')
				{
					throw new AMEFDecodeException("Invalid character after length specifier, expected ,");
				}
				else if(pos>=strdata.length())
				{
					throw new AMEFDecodeException("Reached end of AMEF string, not found ,");
				}
				int lengthm = ((length.getchars()[0] & 0xff) << 16) | ((length.getchars()[1] & 0xff) << 8)
								| ((length.getchars()[2] & 0xff));
				jDBObject->setLength(lengthm);
				String value = strdata.substring(pos,pos+lengthm);
				jDBObject->setValue(value,lengthm);
				tempVal = strdata.substring(pos+lengthm);
			}
			else
			{
				throw new AMEFDecodeException("Invalid character after type specifier, expected ,");
			}
		}
		else if(type==JDBObject::BOOLEAN_TYPE || type==JDBObject::CHAR_TYPE)
		{
			if(strdata.charAt(1)==',')
			{
				jDBObject = new JDBObject();
				jDBObject->setType(type);
				int pos = 2;
				String name = "";
				if(!ignoreName)
				{
					while(strdata.charAt(pos)!=',')
					{
						name += strdata.charAt(pos++);
					}
					if(name.equals("") && strdata.charAt(2)!=',')
					{
						throw new AMEFDecodeException("Invalid character after name specifier, expected ,");
					}
				}
				if(pos>=strdata.length())
				{
					throw new AMEFDecodeException("Reached end of AMEF string, not found ,");
				}
				jDBObject->setName(buffer.substr(st,en-st));}
				if(pos>=strdata.length())
				{
					throw new AMEFDecodeException("Reached end of AMEF string, not found ,");
				}
				jDBObject->setLength(1);
				String value = "";
				if(!ignoreName)
				{
					value = strdata.substring(pos+1,pos+2);
					tempVal = strdata.substring(pos+2);
				}
				else
				{
					value = strdata.substring(pos,pos+1);
					tempVal = strdata.substring(pos+1);
				}
				jDBObject->setValue(value,lengthm);
			}
			else
			{
				throw new AMEFDecodeException("Invalid character after type specifier, expected ,");
			}
		}
		else if(type==JDBObject::STRING_256_TYPE || type==JDBObject::DATE_TYPE || type==JDBObject::DOUBLE_FLOAT_TYPE)
		{
			if(strdata.charAt(1)==',')
			{
				jDBObject = new JDBObject();
				jDBObject->setType(type);
				int pos = 2;
				String name = "";
				if(!ignoreName)
				{
					while(strdata.charAt(pos)!=',')
					{
						name += strdata.charAt(pos++);
					}
					if(name.equals("") && strdata.charAt(2)!=',')
					{
						throw new AMEFDecodeException("Invalid character after name specifier, expected ,");
					}
				}
				if(pos>=strdata.length())
				{
					throw new AMEFDecodeException("Reached end of AMEF string, not found ,");
				}
				jDBObject->setName(buffer.substr(st,en-st));}
				String length = "";
				if(!ignoreName)pos++;
				while(length.length()<1)
				{
					length += strdata.charAt(pos++);
				}
				if(length.equals("") && strdata.charAt(3)!=',')
				{
					throw new AMEFDecodeException("Invalid character after length specifier, expected ,");
				}
				else if(pos>=strdata.length())
				{
					throw new AMEFDecodeException("Reached end of AMEF string, not found ,");
				}
				int lengthm = length.charAt(0) & 0xff;
				jDBObject->setLength(lengthm);
				String value = strdata.substring(pos,pos+lengthm);
				jDBObject->setValue(value,lengthm);
				tempVal = strdata.substring(pos+lengthm);
			}
			else
			{
				throw new AMEFDecodeException("Invalid character after type specifier, expected ,");
			}
		}
		else if(type==JDBObject::VERY_SMALL_INT_TYPE)
		{
			jDBObject = getObject(strdata, type, ignoreName, 1);
		}
		else if(type==JDBObject::SMALL_INT_TYPE)
		{
			jDBObject = getObject(strdata, type, ignoreName, 2);
		}
		else if(type==JDBObject::BIG_INT_TYPE)
		{
			jDBObject = getObject(strdata, type, ignoreName, 3);
		}
		else if(type==JDBObject::INT_TYPE)
		{
			jDBObject = getObject(strdata, type, ignoreName, 4);
		}
		else if(type==JDBObject::VS_LONG_INT_TYPE)
		{
			jDBObject = getObject(strdata, type, ignoreName, 5);
		}
		else if(type==JDBObject::S_LONG_INT_TYPE)
		{
			jDBObject = getObject(strdata, type, ignoreName, 6);
		}
		else if(type==JDBObject::B_LONG_INT_TYPE)
		{
			jDBObject = getObject(strdata, type, ignoreName, 7);
		}
		else if(type==JDBObject::LONG_INT_TYPE)
		{
			jDBObject = getObject(strdata, type, ignoreName, 8);
		}
		else if(type==JDBObject::VS_OBJECT_TYPE)
		{
			if(strdata.charAt(1)==',')
			{
				jDBObject = new JDBObject();
				jDBObject->setType(type);
				int pos = 2;
				String name = "";
				if(!ignoreName)
				{
					while(strdata.charAt(pos)!=',')
					{
						name += strdata.charAt(pos++);
					}
					if(name.equals("") && strdata.charAt(2)!=',')
					{
						throw new AMEFDecodeException("Invalid character after name specifier, expected ,");
					}
				}
				if(pos>=strdata.length())
				{
					throw new AMEFDecodeException("Reached end of AMEF string, not found ,");
				}
				jDBObject->setName(buffer.substr(st,en-st));}
				String length = "";
				if(!ignoreName)pos++;
				while(length.length()<1)
				{
					length += strdata.charAt(pos++);
				}
				if(length.equals("") && strdata.charAt(3)!=',')
				{
					throw new AMEFDecodeException("Invalid character after length specifier, expected ,");
				}
				else if(pos>=strdata.length())
				{
					throw new AMEFDecodeException("Reached end of AMEF string, not found ,");
				}
				int lengthm = ((length.getchars()[0]) & 0xff);
				jDBObject->setLength(lengthm);
				//
				//String value = strdata.substring(pos+1,pos+lengthm+1);
				tempVal  = strdata.substring(pos,pos+lengthm);
				while(!tempVal.equals(""))
				{
					JDBObject obj = decodeSinglePacket(tempVal,ignoreName);
					jDBObject->addPacket(obj);
				}
				tempVal = strdata.substring(pos+lengthm);
			}
			else
			{
				throw new AMEFDecodeException("Invalid character after type specifier, expected ,");
			}
		}
		else if(type==JDBObject::S_OBJECT_TYPE)
		{
			if(strdata.charAt(1)==',')
			{
				jDBObject = new JDBObject();
				jDBObject->setType(type);
				int pos = 2;
				String name = "";
				if(!ignoreName)
				{
					while(strdata.charAt(pos)!=',')
					{
						name += strdata.charAt(pos++);
					}
					if(name.equals("") && strdata.charAt(2)!=',')
					{
						throw new AMEFDecodeException("Invalid character after name specifier, expected ,");
					}
				}
				if(pos>=strdata.length())
				{
					throw new AMEFDecodeException("Reached end of AMEF string, not found ,");
				}
				jDBObject->setName(buffer.substr(st,en-st));}
				String length = "";
				if(!ignoreName)pos++;
				while(length.length()<2)
				{
					length += strdata.charAt(pos++);
				}
				if(length.equals("") && strdata.charAt(3)!=',')
				{
					throw new AMEFDecodeException("Invalid character after length specifier, expected ,");
				}
				else if(pos>=strdata.length())
				{
					throw new AMEFDecodeException("Reached end of AMEF string, not found ,");
				}
				int lengthm = ((length.getchars()[0] & 0xff) << 8) | ((length.getchars()[1]) & 0xff);
				jDBObject->setLength(lengthm);
				//String value = strdata.substring(pos+1,pos+lengthm+1);
				//tempVal = value;
				tempVal  = strdata.substring(pos,pos+lengthm);
				while(!tempVal.equals(""))
				{
					JDBObject obj = decodeSinglePacket(tempVal,ignoreName);
					jDBObject->addPacket(obj);
				}
				tempVal = strdata.substring(pos+lengthm);
			}
			else
			{
				throw new AMEFDecodeException("Invalid character after type specifier, expected ,");
			}
		}
		else if(type==JDBObject::B_OBJECT_TYPE)
		{
			if(strdata.charAt(1)==',')
			{
				jDBObject = new JDBObject();
				jDBObject->setType(type);
				int pos = 2;
				String name = "";
				if(!ignoreName)
				{
					while(strdata.charAt(pos)!=',')
					{
						name += strdata.charAt(pos++);
					}
					if(name.equals("") && strdata.charAt(2)!=',')
					{
						throw new AMEFDecodeException("Invalid character after name specifier, expected ,");
					}
				}
				if(pos>=strdata.length())
				{
					throw new AMEFDecodeException("Reached end of AMEF string, not found ,");
				}
				jDBObject->setName(buffer.substr(st,en-st));}
				String length = "";
				if(!ignoreName)pos++;
				while(length.length()<3)
				{
					length += strdata.charAt(pos++);
				}
				if(length.equals("") && strdata.charAt(3)!=',')
				{
					throw new AMEFDecodeException("Invalid character after length specifier, expected ,");
				}
				else if(pos>=strdata.length())
				{
					throw new AMEFDecodeException("Reached end of AMEF string, not found ,");
				}
				int lengthm = ((length.getchars()[0] & 0xff) << 16)
								| ((length.getchars()[1] & 0xff) << 8) | ((length.getchars()[2]) & 0xff);
				jDBObject->setLength(lengthm);
				//String value = strdata.substring(pos+1,pos+lengthm+1);
				//tempVal = value;
				tempVal  = strdata.substring(pos,pos+lengthm);
				while(!tempVal.equals(""))
				{
					JDBObject obj = decodeSinglePacket(tempVal,ignoreName);
					jDBObject->addPacket(obj);
				}
				tempVal = strdata.substring(pos+lengthm);
			}
			else
			{
				throw new AMEFDecodeException("Invalid character after type specifier, expected ,");
			}
		}
		else if(type==JDBObject::OBJECT_TYPE)
		{
			if(strdata.charAt(1)==',')
			{
				jDBObject = new JDBObject();
				jDBObject->setType(type);
				int pos = 2;
				String name = "";
				if(!ignoreName)
				{
					while(strdata.charAt(pos)!=',')
					{
						name += strdata.charAt(pos++);
					}
					if(name.equals("") && strdata.charAt(2)!=',')
					{
						throw new AMEFDecodeException("Invalid character after name specifier, expected ,");
					}
				}
				if(pos>=strdata.length())
				{
					throw new AMEFDecodeException("Reached end of AMEF string, not found ,");
				}
				jDBObject->setName(buffer.substr(st,en-st));}
				String length = "";
				if(!ignoreName)pos++;
				while(length.length()<4)
				{
					length += strdata.charAt(pos++);
				}
				if(length.equals("") && strdata.charAt(3)!=',')
				{
					throw new AMEFDecodeException("Invalid character after length specifier, expected ,");
				}
				else if(pos>=strdata.length())
				{
					throw new AMEFDecodeException("Reached end of AMEF string, not found ,");
				}
				int lengthm = ((length.getchars()[0] & 0xff) << 24) | ((length.getchars()[1] & 0xff) << 16)
								| ((length.getchars()[2] & 0xff) << 8) | ((length.getchars()[3]) & 0xff);
				jDBObject->setLength(lengthm);
				//String value = strdata.substring(pos+1,pos+lengthm+1);
				//tempVal = value;
				tempVal  = strdata.substring(pos,pos+lengthm);
				while(!tempVal.equals(""))
				{
					JDBObject obj = decodeSinglePacket(tempVal,ignoreName);
					jDBObject->addPacket(obj);
				}
				tempVal = strdata.substring(pos+lengthm);
			}
			else
			{
				throw new AMEFDecodeException("Invalid character after type specifier, expected ,");
			}
		}
		return jDBObject;
	}

	JDBObject getObject(String strdata,char type,bool ignoreName,int typ)
	{
		JDBObject jDBObject = null;
		if(strdata.charAt(1)==',')
		{
			jDBObject = new JDBObject();
			jDBObject->setType(type);
			int pos = 2;
			String name = "";
			if(!ignoreName)
			{
				while(strdata.charAt(pos)!=',')
				{
					name += strdata.charAt(pos++);
				}
				if(name.equals("") && strdata.charAt(2)!=',')
				{
					throw new AMEFDecodeException("Invalid character after name specifier, expected ,");
				}
			}
			if(pos>=strdata.length())
			{
				throw new AMEFDecodeException("Reached end of AMEF string, not found ,");
			}
			jDBObject->setName(buffer.substr(st,en-st));}
			if(!ignoreName)pos++;
			String value = strdata.substring(pos,pos+typ);
			jDBObject->setValue(value,lengthm);
			tempVal = strdata.substring(pos+typ);
		}
		else
		{
			throw new AMEFDecodeException("Invalid character after type specifier, expected ,");
		}
		return jDBObject;
	}*/

	int position;
	JDBObject* decodeSinglePacketB(string buffer,bool ignoreName)
	{
		char type = (char)buffer[position];
		JDBObject *jDBObject = NULL;
		int st, en;
		if(type==JDBObject::NULL_STRING || type==JDBObject::NULL_DATE || type==JDBObject::NULL_NUMBER
				|| type==JDBObject::NULL_BOOL || type==JDBObject::NULL_CHAR)
		{
			jDBObject = new JDBObject();
			jDBObject->setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				if(en>st){

				jDBObject->setName(buffer.substr(st,en-st));}
			}
			else
				position++;
		}
		else if(type==JDBObject::STRING_TYPE)
		{
			jDBObject = new JDBObject();
			jDBObject->setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				if(en>st){

				jDBObject->setName(buffer.substr(st,en-st));}
			}
			else
				position++;
			int lengthm = charArrayToInt(buffer,position,4);
			jDBObject->setLength(lengthm);
			position += 4;
			string value = buffer.substr(position,lengthm);
			jDBObject->setValue(value);
			position += 5+lengthm;
		}
		else if(type==JDBObject::STRING_65536_TYPE)
		{
			jDBObject = new JDBObject();
			jDBObject->setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				if(en>st){

				jDBObject->setName(buffer.substr(st,en-st));}
			}
			else
				position++;
			int lengthm = charArrayToInt(buffer,position,2);
			jDBObject->setLength(lengthm);
			position += 2;
			string value = buffer.substr(position,lengthm);
			jDBObject->setValue(value);
			position += 3+lengthm;
		}
		else if(type==JDBObject::STRING_16777216_TYPE)
		{
			jDBObject = new JDBObject();
			jDBObject->setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				if(en>st){

				jDBObject->setName(buffer.substr(st,en-st));}
			}
			else
				position++;
			int lengthm = charArrayToInt(buffer,position,3);
			jDBObject->setLength(lengthm);
			position += 3;
			string value = buffer.substr(position,lengthm);
			jDBObject->setValue(value);
			position += 4+lengthm;
		}
		else if(type==JDBObject::DATE_TYPE || type==JDBObject::STRING_256_TYPE || type==JDBObject::DOUBLE_FLOAT_TYPE)
		{
			jDBObject = new JDBObject();
			jDBObject->setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				if(en>st){

				jDBObject->setName(buffer.substr(st,en-st));}
			}
			else
				position++;
			int lengthm = charArrayToInt(buffer,position,1);
			jDBObject->setLength(lengthm);
			position++;
			string value = buffer.substr(position,lengthm);
			jDBObject->setValue(value);
			position += lengthm;
		}
		else if(type==JDBObject::VERY_SMALL_INT_TYPE)
		{
			jDBObject = new JDBObject();
			jDBObject->setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				if(en>st){

				jDBObject->setName(buffer.substr(st,en-st));}
			}
			else
				position++;
			jDBObject->setLength(1);
			jDBObject->pushChar(buffer[position]);
			position += 1;
		}
		else if(type==JDBObject::SMALL_INT_TYPE)
		{
			jDBObject = new JDBObject();
			jDBObject->setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				if(en>st){

				jDBObject->setName(buffer.substr(st,en-st));}
			}
			else
				position++;
			jDBObject->setLength(2);
			jDBObject->pushChar(buffer[position]);
			jDBObject->pushChar(buffer[position+1]);
			position += 2;
		}
		else if(type==JDBObject::BIG_INT_TYPE)
		{
			jDBObject = new JDBObject();
			jDBObject->setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				if(en>st){

				jDBObject->setName(buffer.substr(st,en-st));}
			}
			else
				position++;
			jDBObject->setLength(3);
			jDBObject->pushChar(buffer[position]);
			jDBObject->pushChar(buffer[position+1]);
			jDBObject->pushChar(buffer[position+2]);
			position += 3;
		}
		else if(type==JDBObject::INT_TYPE)
		{
			jDBObject = new JDBObject();
			jDBObject->setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				if(en>st){

				jDBObject->setName(buffer.substr(st,en-st));}
			}
			else
				position++;
			jDBObject->setLength(4);
			jDBObject->pushChar(buffer[position]);
			jDBObject->pushChar(buffer[position+1]);
			jDBObject->pushChar(buffer[position+2]);
			jDBObject->pushChar(buffer[position+3]);
			position += 4;
		}
		else if(type==JDBObject::VS_LONG_INT_TYPE)
		{
			jDBObject = new JDBObject();
			jDBObject->setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				if(en>st){

				jDBObject->setName(buffer.substr(st,en-st));}
			}
			else
				position++;
			jDBObject->setLength(5);
			jDBObject->pushChar(buffer[position]);
			jDBObject->pushChar(buffer[position+1]);
			jDBObject->pushChar(buffer[position+2]);
			jDBObject->pushChar(buffer[position+3]);
			jDBObject->pushChar(buffer[position+4]);
			position += 5;
		}
		else if(type==JDBObject::S_LONG_INT_TYPE)
		{
			jDBObject = new JDBObject();
			jDBObject->setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				if(en>st){

				jDBObject->setName(buffer.substr(st,en-st));}
			}
			else
				position++;
			jDBObject->setLength(6);
			jDBObject->pushChar(buffer[position]);
			jDBObject->pushChar(buffer[position+1]);
			jDBObject->pushChar(buffer[position+2]);
			jDBObject->pushChar(buffer[position+3]);
			jDBObject->pushChar(buffer[position+4]);
			jDBObject->pushChar(buffer[position+5]);
			position += 6;
		}
		else if(type==JDBObject::B_LONG_INT_TYPE)
		{
			jDBObject = new JDBObject();
			jDBObject->setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				if(en>st){

				jDBObject->setName(buffer.substr(st,en-st));}
			}
			else
				position++;
			jDBObject->setLength(7);
			jDBObject->pushChar(buffer[position]);
			jDBObject->pushChar(buffer[position+1]);
			jDBObject->pushChar(buffer[position+2]);
			jDBObject->pushChar(buffer[position+3]);
			jDBObject->pushChar(buffer[position+4]);
			jDBObject->pushChar(buffer[position+5]);
			jDBObject->pushChar(buffer[position+6]);
			position += 7;
		}
		else if(type==JDBObject::LONG_INT_TYPE)
		{
			jDBObject = new JDBObject();
			jDBObject->setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				if(en>st){

				jDBObject->setName(buffer.substr(st,en-st));}
			}
			else
				position++;
			jDBObject->setLength(8);
			jDBObject->pushChar(buffer[position]);
			jDBObject->pushChar(buffer[position+1]);
			jDBObject->pushChar(buffer[position+2]);
			jDBObject->pushChar(buffer[position+3]);
			jDBObject->pushChar(buffer[position+4]);
			jDBObject->pushChar(buffer[position+5]);
			jDBObject->pushChar(buffer[position+6]);
			jDBObject->pushChar(buffer[position+7]);
			position += 8;
		}
		else if(type==JDBObject::BOOLEAN_TYPE || type==JDBObject::CHAR_TYPE)
		{
			jDBObject = new JDBObject();
			jDBObject->setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				if(en>st){

				jDBObject->setName(buffer.substr(st,en-st));}
			}
			else
				position++;
			jDBObject->setLength(1);
			jDBObject->pushChar(buffer[position]);
			position += 1;
		}
		else if(type==JDBObject::VS_OBJECT_TYPE)
		{
			jDBObject = new JDBObject();
			jDBObject->setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				if(en>st){

				jDBObject->setName(buffer.substr(st,en-st));}
			}
			else
				position++;
			int lengthm = charArrayToInt(buffer,position,1);
			jDBObject->setLength(lengthm);
			position++;
			while(position<buffer.length())
			{
				JDBObject *obj = decodeSinglePacketB(buffer,ignoreName);
				jDBObject->addPacket(obj);
			}
		}
		else if(type==JDBObject::S_OBJECT_TYPE)
		{
			jDBObject = new JDBObject();
			jDBObject->setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				if(en>st){

				jDBObject->setName(buffer.substr(st,en-st));}
			}
			else
				position++;
			int lengthm = charArrayToInt(buffer,position,2);
			jDBObject->setLength(lengthm);
			//char* value = new char[lengthm];
			//System.arraycopy(buffer, 3, value, 0, lengthm);
			position += 2;
			while(position<buffer.length())
			{
				JDBObject* obj = decodeSinglePacketB(buffer,ignoreName);
				jDBObject->addPacket(obj);
			}
		}
		else if(type==JDBObject::B_OBJECT_TYPE)
		{
			jDBObject = new JDBObject();
			jDBObject->setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				if(en>st){

				jDBObject->setName(buffer.substr(st,en-st));}
			}
			else
				position++;
			int lengthm = charArrayToInt(buffer,position,3);
			jDBObject->setLength(lengthm);
			position += 3;
			while(position<buffer.length())
			{
				JDBObject* obj = decodeSinglePacketB(buffer,ignoreName);
				jDBObject->addPacket(obj);
			}
		}
		else if(type==JDBObject::OBJECT_TYPE)
		{
			jDBObject = new JDBObject();
			jDBObject->setType(type);
			if(!ignoreName)
			{
				while(buffer[position++]!=44){}
				st = position;
				while(buffer[position++]!=44){}
				en = position - 1;
				if(en>st){

				jDBObject->setName(buffer.substr(st,en-st));}
			}
			else
				position++;
			int lengthm = charArrayToInt(buffer,position,4);
			jDBObject->setLength(lengthm);
			position += 4;
			while(position<buffer.length())
			{
				JDBObject* obj = decodeSinglePacketB(buffer,ignoreName);
				jDBObject->addPacket(obj);
			}
		}
		return jDBObject;
	}

};

#endif /* JDBDECODER_H_ */
