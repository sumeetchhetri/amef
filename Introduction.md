# Introduction #
```
Sometimes there is a need of a compact message exchange protocol, which can represent 
not only the usual datatypes but also complex object representations. The ISO8583 and the
ASN.1 protocols solve the problem in these cases providing a standard message exchange 
protocol.

The only hitch being that we need to know the format before hand, and only then the 
message can be encoded/decoded on the ends.

Also the ISO 8583 protocol does not provide an object oriented solution. The ASN.1 
potocol defines object boundaries but already has a limitation mentioned above.

One more limitation is building encoders and decoders for the message exchange protocol,
I remember in one of the applications, i developed i had to go for a proprietary protocol 
implementation of ASN.1.

Thus to overcome all these limitations i decided to come up with my own implementation of 
a new message exchange protocol, which will be simple, compact, totally object oriented 
and absolutely free to use. 

I have provided a JAVA and C++ implementation of the Encoder-Decoder solution.
```