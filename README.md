# Automated Message Exchange Format Protocol

Features
- Simple to use
- No complex encoding-decoding logic required
- Totally Object oriented
- Inter/Intra Application communication
- Completely platform-independent
- No need to know the format of the message exchanged (for eg. ASN, ISO)
- Compact (even when compared to JSON)


```
The AMEF protocol defines object boundaries and also conatins a name-value pair mapping 
for each and every object automatically.
The core of the protocol is the AMEFObject.
Every AMEF message will always contain one and only one AMEFObject, which may internally
consist of one or more primitive AMEF types or other AMEF Objects.
The primitive AMEF types viz, string, boolean, number(long,double,int),character and date
are also wrapped within an AMEFObject type.

Every AMEFObject may in turn consist of a set of child AMEFObjects, primitive AMEF types
would never contain any child AMEFObjects and hence are considered primitive.
The core AMEFObject or the lone AMEFObject comprising the AMEF message will never be a 
primitive AMEF type.

Every AMEF message is an AMEF encoded byte-stream, begining with a 4-byte header length
designating the length of the AMEFObject encoded byte-stream, without the header-length.
Every AMEFObject whether a primitive or otherwise will always be encoded in the form
AMEF_TYPE,AMEF_NAME,AMEF_LENGTH,AMEF_VALUE
where,
      AMEF_TYPE = 's'->string,'b'->boolean,'c'->character,'d'->date,'n'->number,
                  'o'->object
      AMEF_NAME = a name assigned for the AMEFObject
      AMEF_LENGTH = 1 for boolean and character types
                    4 byte length of the string value
                    1 byte number of digits in the number
                    1 byte length of the date string
                    4 byte length of the AMEFObject encoded byte-stream for an AMEFObject
      AMEF_VALUE = the value of the AMEFObject
Also the boolean and character types will always contain a character of length 1 and hence
the length can be ignored in this case.

Thus every AMEF Message will contain a byte-stream of the following format,
AMEF_HEADER_LENGTH,o,AMEF_NAME,AMEF_LENGTH,AMEF_VALUE
The o in the message shows that the AMEF message consists of one and only one AMEF Object.

For eg., a string of the value "Test Message" will be encoded as,
FOUR_BYTE_LENGTH=4 byte value of 12
s,,FOUR_BYTE_LENGTH,Test Message

a boolean value of true will be encoded as,
b,,1
a boolean value of true will be encoded as,
b,,0

a character value of 'l' and name of "char-value" will be encoded as,
c,char-value,l

a number value of 2345678 will be encoded as,
n,,7,2345678

a date value as "ddMMyyyy HHmmss" 11111999 111111 will be encoded as,
d,,15,11111999 111111

and so on....
```
