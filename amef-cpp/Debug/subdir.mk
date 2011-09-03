################################################################################
# Automatically-generated file. Do not edit!
################################################################################

# Add inputs and outputs from these tool invocations to the build variables 
CPP_SRCS += \
../AMEFDecoder.cpp \
../AMEFEncoder.cpp \
../AMEFObject.cpp \
../AMEFResources.cpp \
../TestAMEFProtocol.cpp 

OBJS += \
./AMEFDecoder.o \
./AMEFEncoder.o \
./AMEFObject.o \
./AMEFResources.o \
./TestAMEFProtocol.o 

CPP_DEPS += \
./AMEFDecoder.d \
./AMEFEncoder.d \
./AMEFObject.d \
./AMEFResources.d \
./TestAMEFProtocol.d 


# Each subdirectory must supply rules for building sources it contributes
%.o: ../%.cpp
	@echo 'Building file: $<'
	@echo 'Invoking: GCC C++ Compiler'
	g++ -O0 -g3 -Wall -c -fmessage-length=0 -fPIC -MMD -MP -MF"$(@:%.o=%.d)" -MT"$(@:%.o=%.d)" -o"$@" "$<"
	@echo 'Finished building: $<'
	@echo ' '


