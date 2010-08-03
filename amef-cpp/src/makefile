CC={compiler binary}
CFLAGS=-c
LDFLAGS=
SOURCES=ConversionUtil.cpp AMEFObject.cpp AMEFDecoder.cpp AMEFEncoder.cpp TestAMEFProtocol.cpp
OBJECTS=$(SOURCES:.cpp=.o)
EXECUTABLE=amefCpp

all: $(SOURCES) $(EXECUTABLE)
	
$(EXECUTABLE): $(OBJECTS)
	$(CC) $(LDFLAGS) $(OBJECTS) -o $@

.cpp.o:
	$(CC) $(CFLAGS) $< -o $@
