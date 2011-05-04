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

#include "Timer.h"

Timer::Timer() {
	// TODO Auto-generated constructor stub

}

Timer::~Timer() {
	// TODO Auto-generated destructor stub
}

void Timer::start()
{
	clock_gettime(CLOCK_REALTIME, &st);
	//cout << "\n--------------------------start Timer-------------------------::" << ((st.tv_sec*1000000000 + st.tv_nsec)/1000) << "\n"<< flush;
}

long Timer::getCurrentTime()
{
	timespec en;
	clock_gettime(CLOCK_REALTIME, &en);
	return ((en.tv_sec * 1000000000) + en.tv_nsec);
}

long Timer::getTimestamp()
{
	timespec en;
	clock_gettime(CLOCK_REALTIME, &en);
	return en.tv_sec;
}

void Timer::end()
{
	timespec en;
	clock_gettime(CLOCK_REALTIME, &en);
	long elap = (((en.tv_sec - st.tv_sec) * 1000000000) + (en.tv_nsec - st.tv_nsec))/1000;
	cout << "\n--------------------------end Timer-------------------------::" << ((en.tv_sec*1000000000 + en.tv_nsec)/1000) << "\n"<< flush;
	cout << "\n" << elap << "\n"<< flush;
}
int Timer::elapsedMicroSeconds()
{
	timespec en;
	clock_gettime(CLOCK_REALTIME, &en);
	return (((en.tv_sec - st.tv_sec) * 1000000000) + (en.tv_nsec - st.tv_nsec))/1000;
}
int Timer::elapsedMilliSeconds()
{
	timespec en;
	clock_gettime(CLOCK_REALTIME, &en);
	return (((en.tv_sec - st.tv_sec) * 1000000000) + (en.tv_nsec - st.tv_nsec))/1000000;
}
int Timer::elapsedNanoSeconds()
{
	timespec en;
	clock_gettime(CLOCK_REALTIME, &en);
	return (((en.tv_sec - st.tv_sec) * 1000000000) + (en.tv_nsec - st.tv_nsec));
}
int Timer::elapsedSeconds()
{
	timespec en;
	clock_gettime(CLOCK_REALTIME, &en);
	return (en.tv_sec - st.tv_sec);
}
