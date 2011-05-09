function Test()
{
	this.i = 22222;
	this.j = "Some Text which is important";
	this.k = true;
	this.l = 1234.123;
}

var AMEF = {
	/*The 4GB string type*/
	STRING_TYPE : 's',
	
	/*The max 256 length string type*/
	STRING_256_TYPE : 't',
	
	/*The max 65536 length string type*/
	STRING_65536_TYPE : 'h',
	
	STRING_16777216_TYPE : 'y',

	/*The boolean type*/
	BOOLEAN_TYPE : 'b',
	
	/*The character type*/
	CHAR_TYPE : 'c',
	
	/*The Number types*/
	VERY_SMALL_INT_TYPE : 'n',
	
	SMALL_INT_TYPE : 'w',
	
	BIG_INT_TYPE : 'r',
	
	INT_TYPE : 'i',
	
	VS_LONG_INT_TYPE : 'f',
	
	S_LONG_INT_TYPE : 'x',
	
	B_LONG_INT_TYPE : 'e',
	
	LONG_INT_TYPE : 'l',

	DOUBLE_FLOAT_TYPE : 'u',

	/*The Object type*/
	VS_OBJECT_TYPE : 'm',
	
	/*The Object type*/
	S_OBJECT_TYPE : 'q',
	
	/*The Object type*/
	B_OBJECT_TYPE : 'p',
	
	/*The Object type*/
	OBJECT_TYPE : 'o',
	
	_numToByteArrayWI : function _numToByteArrayWI(l) 
	{
		var ind = 1;
		if(l<256)
			ind =1;
		else if(l<65536)
			ind = 2;
		else if(l<16777216)
			ind =3;
		else if(l<4294967296)
			ind =4;
		else if(l<1099511627776)
			ind =5;
		else if(l<281474976710656)
			ind =6;
		else if(l<72057594037927936)
			ind =7;
		else
			ind =8;
		var result = "";
		for (var i = 0; i<ind; i++)
		{
			var offset = (ind - 1 - i) * 8;
			result += String.fromCharCode((l >>> offset) & 0xFF);
		}
		return result;
	},

	_byteArrayToInt : function _byteArrayToInt(l) 
	{
		var t = 0;
		var ind = l.length;
		for (var i = 0; i < l.length; i++)
		{
			var offset = (ind -1 - i) * 8;//alert(l.substr(i,1));
			t += (l.substr(i,1).charCodeAt() & 0x000000FF) << offset; 
		}
		return t;
	},

	encode : function encode(_o)
	{ 	
		var a = "", t;   
		
		for(var p in _o)
		{        
			if(_o.hasOwnProperty(p)){            
				t = _o[p];                
				if(t && typeof t == "object"){ 
					var temp = encode(t);
					if(temp.length+1<256)
						a += this.VS_OBJECT_TYPE + "," + p + "," + this._numToByteArrayWI(temp.length) + temp;
					else if(temp.length+1<65536)
						a += this.S_OBJECT_TYPE + "," + p + "," + this._numToByteArrayWI(temp.length) + temp;
					else if(temp.length+1<16777216)
						a += this.B_OBJECT_TYPE + "," + p + "," + this._numToByteArrayWI(temp.length) + temp;
					else
						a += this.OBJECT_TYPE + "," + p + "," + this._numToByteArrayWI(temp.length) + temp;                 
				}
				else if(typeof t == "boolean"){
					a += this.BOOLEAN_TYPE + "," + p + "," + (t?"1":"0");
				}
				else if(typeof t == "string"){
					var temp  = "";
					if(t.length<=256)
					{
						temp += this.STRING_256_TYPE + "," + p + "," + this._numToByteArrayWI(t.length) + t;
					}
					else if(t.length<=65536)
					{
						temp += this.STRING_65536_TYPE + "," + p + "," + this._numToByteArrayWI(t.length) + t;
					}
					else if(t.length<=16777216)
					{
						temp += this.STRING_16777216_TYPE + "," + p + "," + this._numToByteArrayWI(t.length) + t;
					}
					else
					{
						temp += this.STRING_TYPE + "," + p + "," + this._numToByteArrayWI(t.length) + t;
					}
					a += temp;
				}
				else if(typeof t == "number"){
					if((t+"").indexOf(".")!=-1)
					{
						a += this.DOUBLE_FLOAT_TYPE + "," + p + "," + this._numToByteArrayWI((t+"").length) + t;
					}
					else
					{
						var temp  = "";
						if(t<256)
						{
							temp += this.VERY_SMALL_INT_TYPE + "," + p + "," + this._numToByteArrayWI(t);
						}
						else if(t<65536)
						{
							temp += this.SMALL_INT_TYPE + "," + p + "," + this._numToByteArrayWI(t);
						}
						else if(t<16777216)
						{
							temp += this.BIG_INT_TYPE + "," + p + "," + this._numToByteArrayWI(t);
						}
						else if(t<4294967296)
						{
							temp += this.INT_TYPE + "," + p + "," + this._numToByteArrayWI(t);
						}
						else if(t<1099511627776)
						{
							temp += this.VS_LONG_INT_TYPE + "," + p + "," + this._numToByteArrayWI(t);
						}
						else if(t<281474976710656)
						{
							temp += this.S_LONG_INT_TYPE + "," + p + "," + this._numToByteArrayWI(t);
						}
						else if(t<72057594037927936)
						{
							temp += this.B_LONG_INT_TYPE + "," + p + "," + this._numToByteArrayWI(t);
						}
						else
						{
							temp += this.LONG_INT_TYPE + "," + p + "," + this._numToByteArrayWI(t);
						}
						a += temp;
					}
				}
			}
		}
		var p = /(\w+)\(/.exec(_o.constructor.toString())[1];
		if(a.length+1<256)
			a = this.VS_OBJECT_TYPE + "," + p + "," + this._numToByteArrayWI(a.length) + a;
		else if(a.length+1<65536)
			a = this.S_OBJECT_TYPE + "," + p + "," + this._numToByteArrayWI(a.length) + a;
		else if(a.length+1<16777216)
			a = this.B_OBJECT_TYPE + "," + p + "," + this._numToByteArrayWI(a.length) + a;
		else
			a = this.OBJECT_TYPE + "," + p + "," + this._numToByteArrayWI(a.length) + a;
		return a;
	},
	
	decode : function decode(o)
	{ 
		var a = "", t; 
		
		var naam = "";

		var index = 0;

		if(o.substr(0,1)==this.VS_OBJECT_TYPE)
		{
			naam = o.substr(2);
			naam = naam.substr(0,naam.indexOf(","));
			index = 4 + naam.length;
		}
		else if(o.substr(0,1)==this.S_OBJECT_TYPE)
		{
			naam = o.substr(2);
			naam = naam.substr(0,naam.indexOf(","));
			index = 5 + naam.length;
		}
		else if(o.substr(0,1)==this.B_OBJECT_TYPE)
		{
			naam = o.substr(2);
			naam = naam.substr(0,naam.indexOf(","));
			index = 6 + naam.length;
		}
		else if(o.substr(0,1)==this.OBJECT_TYPE)
		{
			naam = o.substr(2);
			naam = naam.substr(0,naam.indexOf(","));
			index = 7 + naam.length;
		}

		var a = "_o={";

		while(index<o.length)
		{
			if(o.substr(index,1)==this.VERY_SMALL_INT_TYPE)
			{
				index += 2;
				var naam = o.substr(index);	
				naam = naam.substr(0,naam.indexOf(","));
				index += naam.length + 1;
				a += "\""+naam+"\" : "+this._byteArrayToInt(o.substr(index,1));
				index++;
			}
			else if(o.substr(index,1)==this.SMALL_INT_TYPE)
			{
				index += 2;
				var naam = o.substr(index);	
				naam = naam.substr(0,naam.indexOf(","));
				index += naam.length + 1;
				a += "\""+naam+"\" : "+this._byteArrayToInt(o.substr(index,2));
				index += 2;
			}
			else if(o.substr(index,1)==this.BIG_INT_TYPE)
			{
				index += 2;
				var naam = o.substr(index);	
				naam = naam.substr(0,naam.indexOf(","));
				index += naam.length + 1;
				a += "\""+naam+"\" : "+this._byteArrayToInt(o.substr(index,3));
				index += 3;
			}
			else if(o.substr(index,1)==this.INT_TYPE)
			{
				index += 2;
				var naam = o.substr(index);	
				naam = naam.substr(0,naam.indexOf(","));
				index += naam.length + 1;
				a += "\""+naam+"\" : "+this._byteArrayToInt(o.substr(index,4));
				index += 4;
			}
			else if(o.substr(index,1)==this.VS_LONG_INT_TYPE)
			{
				index += 2;
				var naam = o.substr(index);	
				naam = naam.substr(0,naam.indexOf(","));
				index += naam.length + 1;
				a += "\""+naam+"\" : "+this._byteArrayToInt(o.substr(index,5));
				index += 5;
			}
			else if(o.substr(index,1)==this.S_LONG_INT_TYPE)
			{
				index += 2;
				var naam = o.substr(index);	
				naam = naam.substr(0,naam.indexOf(","));
				index += naam.length + 1;
				a += "\""+naam+"\" : "+this._byteArrayToInt(o.substr(index,6));
				index += 6;
			}
			else if(o.substr(index,1)==this.B_LONG_INT_TYPE)
			{
				index += 2;
				var naam = o.substr(index);this._byteArrayToInt(o.substr(index,1))	
				naam = naam.substr(0,naam.indexOf(","));
				index += naam.length + 1;
				a += "\""+naam+"\" : "+this._byteArrayToInt(o.substr(index,7));
				index += 7;
			}
			else if(o.substr(index,1)==this.LONG_INT_TYPE)
			{
				index += 2;
				var naam = o.substr(index);	
				naam = naam.substr(0,naam.indexOf(","));
				index += naam.length + 1;
				a += "\""+naam+"\" : "+this._byteArrayToInt(o.substr(index,8));
				index += 8;
			}
			else if(o.substr(index,1)==this.STRING_256_TYPE || o.substr(index,1)==this.DOUBLE_FLOAT_TYPE)
			{
				var strtyp = (o.substr(index,1)==this.DOUBLE_FLOAT_TYPE)?false:true;
				index += 2;
				var naam = o.substr(index);	
				naam = naam.substr(0,naam.indexOf(","));
				index += naam.length + 1;
				var len = o.substr(index,1);	
				len = this._byteArrayToInt(len);
				index += 1;
				var valu = o.substr(index,len);
				if(strtyp)a += "\""+naam+"\" : '"+valu+"'";
				else a += "\""+naam+"\" : "+valu;
				index += len;
			}
			else if(o.substr(index,1)==this.STRING_65536_TYPE)
			{
				index += 2;
				var naam = o.substr(index);	
				naam = naam.substr(0,naam.indexOf(","));
				index += naam.length + 1;
				var len = o.substr(index,2);	
				len = this._byteArrayToInt(len);
				index += 2;
				var valu = o.substr(index,len);
				a += "\""+naam+"\" : '"+valu+"'";
				index += len;
			}
			else if(o.substr(index,1)==this.STRING_16777216_TYPE)
			{
				index += 2;
				var naam = o.substr(index);	
				naam = naam.substr(0,naam.indexOf(","));
				index += naam.length + 1;
				var len = o.substr(index,3);	
				len = this._byteArrayToInt(len);
				index += 3;
				var valu = o.substr(index,len);
				a += "\""+naam+"\" : '"+valu+"'";
				index += len;
			}
			else if(o.substr(index,1)==this.STRING_TYPE)
			{
				index += 2;
				var naam = o.substr(index);	
				naam = naam.substr(0,naam.indexOf(","));
				index += naam.length + 1;
				var len = o.substr(index,4);	
				len = this._byteArrayToInt(len);
				index += 4;
				var valu = o.substr(index,len);
				a += "\""+naam+"\" : '"+valu+"'";
				index += len;
			}
			else if(o.substr(index,1)==this.BOOLEAN_TYPE)
			{
				index += 2;
				var naam = o.substr(index);	
				naam = naam.substr(0,naam.indexOf(","));
				index += naam.length + 1;
				a += "\""+naam+"\" : "+(o.substr(index,1)=="1"?true:false);
				index++;
			}
			a += ",";
		}
		if(a.charAt(a.length-1)==",")
			a = a.substr(0,a.length-1);
		a += "};";
		eval(a);
		return _o;
	}
};

var val = AMEF.encode(new Test());
alert(val);
var t = AMEF.decode(val);
val = AMEF.encode(t);
alert(val);