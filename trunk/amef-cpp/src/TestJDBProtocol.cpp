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
#include "TestJDBProtocol.h"
#include "Client.h"
#include "AMEFResources.h"
#define BACKLOGM 500
#define MAXEPOLLSIZE 1000
#include "map"

#include <boost/thread/mutex.hpp>
using namespace std;

typedef map<long,string> tablemap;
map<string, tablemap*> cached;
boost::mutex lock;

int main1()
{
	Client client;
	client.connection("localhost",7001);
	AMEFEncoder* encoder = new AMEFEncoder();

	Timer t;
	AMEFObject* query = NULL;
	for (int var = 0; var < 500; ++var)
	{
		t.start();
		AMEFObject* object = new AMEFObject();
		object->addNullPacket(AMEFObject::NULL_STRING);
		object->addPacket("asdasD");
		object->addPacket(12312321);
		object->addPacket(true);
		object->addPacket(1233444555);

		query = new AMEFObject();
		query->addPacket("insert sent");
		query->addPacket(encoder->encodeWL(object, true));
		string dat = encoder->encodeB(query, false);
		client.sendData(dat);
		string ret = client.getData();
		cout << ret << " Insert " << t.elapsedMilliSeconds() << endl;
		//object->~AMEFObject();
		query->~AMEFObject();
	}

	for (int var = 0; var < 500; ++var)
	{
		t.start();

		AMEFObject* object = new AMEFObject();
		object->addPacket("aaaaaaaaa","1");

		query = new AMEFObject();
		query->addPacket("update sent 498");
		query->addPacket(encoder->encodeWL(object, false));
		string dat = encoder->encodeB(query, false);
		client.sendData(dat);
		string ret = client.getData();
		cout << ret << " Update " << t.elapsedMilliSeconds() << endl;
		//object->~AMEFObject();
		query->~AMEFObject();
	}

	//sleep(2);

	for (int var = 0; var < 500; ++var)
	{
		AMEFDecoder decoder;
		t.start();
		query = new AMEFObject();
		query->addPacket("select sent = 498");
		string dat = encoder->encodeB(query, false);
		client.sendData(dat);
		string ret = client.getData();
		AMEFObject* obj = decoder.decodeB(ret.substr(0,ret.length()-1),false,true);
		cout << "Select " << t.elapsedMilliSeconds() << endl;
		query->~AMEFObject();
	}
	encoder->~AMEFEncoder();
	client.closeConnection();
	return 0;
}

void sigchld_handler(int s)
{
    while(waitpid(-1, NULL, WNOHANG) > 0);
}

void signalSIGSEGV(int dummy)
{
	signal(SIGSEGV,signalSIGSEGV);
	/*string filename;
	stringstream ss;
	ss << servd;
	ss << getpid();
	ss >> filename;
	filename.append(".cntrl");
	remove(filename.c_str());*/
	void * array[25];
	int nSize = backtrace(array, 25);
	char ** symbols = backtrace_symbols(array, nSize);
	string tempo;
	for (int i = 0; i < nSize; i++)
	{
		tempo = symbols[i];
		tempo += "\n";
	}
	free(symbols);
	cout << "segmentation fault" << getpid() << "\n" << tempo << flush;
	//abort();
}


void signalSIGPIPE(int dummy)
{
	signal(SIGPIPE,signalSIGPIPE);
	/*string filename;
	stringstream ss;
	ss << servd;
	ss << getpid();
	ss >> filename;
	filename.append(".cntrl");
	remove(filename.c_str());*/
	void * array[25];
	int nSize = backtrace(array, 25);
	char ** symbols = backtrace_symbols(array, nSize);
	string tempo;
	for (int i = 0; i < nSize; i++)
	{
		tempo = symbols[i];
		tempo += "\n";
	}
	free(symbols);
	cout << "Broken pipe ignore it" << getpid() << "\n" << tempo << flush;
	//abort();
}

void signalSIGKILL(int dummy)
{

}

int getLength(string header,int size)
{
	int totsize = header[size-1] & 0xff;
	for (int var = 0; var < size-1; var++)
	{
		totsize |= ((header[var] & 0xff) << (size-1-var)*8);
	}
	return totsize;
}

void insert(string quer,string data)
{
	vector<string> que;
	boost::iter_split(que, quer, boost::first_finder(" "));
	tablemap *mapo = cached[que[1]];

	long key = boost::lexical_cast<long>(que[2]);
	//cout << data.length() <<endl;
	lock.lock();
	mapo->insert(pair<long,string>(key, data));
	lock.unlock();
	//cout<< que[1] << mapo->size() <<key<<endl;
}

void update(string quer,string data)
{
	vector<string> que;
	boost::iter_split(que, quer, boost::first_finder(" "));
	tablemap *mapo = cached[que[1]];

	long key = boost::lexical_cast<long>(que[2]);
	//cout << data.length() <<endl;
	string odata = (*mapo)[key];
	AMEFDecoder decoder;
	AMEFObject* objn = new AMEFObject;
	AMEFObject* obje = decoder.decodeB(data,false,false);
	AMEFObject* objo = decoder.decodeB(odata,false,true);
	lock.lock();
	mapo->erase(key);
	lock.unlock();
	for (int i = 0; i < (int)objo->getPackets().size(); i++)
	{
		bool flag = false;
		for (int j = 0; j < (int)obje->getPackets().size(); j++)
		{
			if(obje->getPackets().at(j)->getNameStr()==boost::lexical_cast<string>(i))
			{
				flag = true;
				if(obje->getPackets().at(j)->isString())
				{
					objn->addPacket(obje->getPackets().at(j)->getValue());
				}
				else if(obje->getPackets().at(j)->isFloatingPoint())
				{
					objn->addPacket(obje->getPackets().at(j)->getDoubleValue());
				}
				else if(obje->getPackets().at(j)->isnumber())
				{
					objn->addPacket(obje->getPackets().at(j)->getNumericValue());
				}
				else if(obje->getPackets().at(j)->isNull())
				{
					objn->addNullPacket(obje->getPackets().at(j)->getType());
				}
				else if(obje->getPackets().at(j)->getType()=='b')
				{
					objn->addPacket(obje->getPackets().at(j)->getBooleanValue());
				}
				else
				{
					objn->addPacket(obje->getPackets().at(j)->getValue()[0]);
				}
			}
		}
		if(!flag)
		{
			if(objo->getPackets().at(i)->isString())
			{
				objn->addPacket(objo->getPackets().at(i)->getValue());
			}
			else if(objo->getPackets().at(i)->isFloatingPoint())
			{
				objn->addPacket(objo->getPackets().at(i)->getDoubleValue());
			}
			else if(objo->getPackets().at(i)->isnumber())
			{
				objn->addPacket(objo->getPackets().at(i)->getNumericValue());
			}
			else if(objo->getPackets().at(i)->isNull())
			{
				objn->addNullPacket(objo->getPackets().at(i)->getType());
			}
			else if(objo->getPackets().at(i)->getType()=='b')
			{
				objn->addPacket(objo->getPackets().at(i)->getBooleanValue());
			}
			else
			{
				objn->addPacket(objo->getPackets().at(i)->getValue()[0]);
			}
		}
	}
	AMEFEncoder encoder;
	string ndata = encoder.encodeWL(objn, true);
	lock.lock();
	mapo->insert(pair<long,string>(key, ndata));
	lock.unlock();
	delete obje;
	delete objo;
	delete objn;
	//cout<< que[1] << " " << key << " " << ndata <<key<<endl;
}

int main4()
{
	for(int i = 1; i < 10; ++i){
		cached["sent"] = new map<long,string>;
		for (int var = 1; var < 10000; ++var) {
			string dat = "asdasdasdasdasdasdas";
			AMEFObject* object = new AMEFObject();
			object->addNullPacket(AMEFObject::NULL_STRING);
			object->addPacket("asdasD");
			object->addPacket(12312321);
			object->addPacket(true);
			object->addPacket(1233444555);
			cached["sent"]->insert(pair<long,string>(var, "asasd"));
			delete object;
		}
		cout << "done writing" << endl;
		sleep(5);
		cached["sent"]->clear();
		cached["sent"]->empty();
		cout << "done clearing" << endl;
		sleep(5);
	}
}


static void run(int fd)
{
	struct epoll_event ev;
	struct epoll_event events[1];
	int epoll_handle = epoll_create(1);
	ev.events = EPOLLIN | EPOLLPRI;
	ev.data.fd = fd;
	if (epoll_ctl(epoll_handle, EPOLL_CTL_ADD, fd, &ev) < 0)
	{
		fprintf(stderr, "epoll set insertion error: fd=%d\n", fd);
		return;
	}
	AMEFDecoder decoder;
	char buf[4];
	while(1)
	{
		int nfds = epoll_wait(epoll_handle, events, 1,-1);
		if (nfds == -1)
		{
			cout << "error" << endl;
		}
		else
		{
			string ln;
			int err = recv(fd,buf,4,MSG_WAITALL);
			if(err==0)break;
			for (int var = 0; var < 4; var++) {
				ln.push_back(buf[var]);
			}
			int len = getLength(ln,4);
			//cout << len << endl;
			char* buff = new char[len];
	
			err = recv(fd,buff,len,MSG_WAITALL);
			if(err==0)break;
			string data;
			for (int var = 0; var < len; var++) {
				data.push_back(buff[var]);
			}
			//cout << *data << endl;
			//memset(&buf[0], 0, sizeof(buff));
			delete[] buff;
			AMEFObject* query = decoder.decodeB(data,false,false);
			//cout << query << endl;
			string quer = query->getPackets().at(0)->getValue();
			if(quer.find("insert ")!=-1 && query->getPackets().size()==2
					&& query->getPackets().at(1)->getValue()!="")
			{
				insert(quer, query->getPackets().at(1)->getValue());
				int err = send(fd, "T", 1, 0);
				if(err==0)break;
			}
			else if(quer.find("update ")!=-1)
			{
				update(quer, query->getPackets().at(1)->getValue());
				int err = send(fd, "T", 1, 0);
				if(err==0)break;
			}
			else if(quer.find("delete ")!=-1)
			{
				//cout << quer << endl;
				vector<string> que;
				boost::iter_split(que, quer, boost::first_finder(" "));
				tablemap *mapo = cached[que[1]];
				//cout << que[1] << mapo->size() << que[2]<<  endl;
				if(que[2]=="*")
				{
					mapo->empty();
					mapo->clear();
					delete mapo;
					cached["sent"] = new map<long,string>;
				}
				else
				{
					mapo->erase(boost::lexical_cast<long>(que[2]));
				}
				int err = send(fd, "T", 1, 0);
				if(err==0)break;
			}
			else if(quer.find("select ")!=-1 && quer.find(" = ")!=-1)
			{
				vector<string> que;
				boost::iter_split(que, quer, boost::first_finder(" "));
				tablemap *mapo = cached[que[1]];
				//cout << que[1] << mapo->size() << que[3]<<  endl;
				string dat = (*mapo)[boost::lexical_cast<long>(que[3])];
				dat += "F";
				//cout << dat << endl;
				int err = send(fd, dat.c_str(), dat.length(), 0);
				if(err==0)break;
			}
			else if(quer.find("select ")!=-1 &&
				(quer.find(" < ")!=-1 ||  quer.find(" > ")!=-1 || quer.find(" *")!=-1))
			{
				vector<string> que;
				boost::iter_split(que, quer, boost::first_finder(" "));
				tablemap *mapo = cached[que[1]];
				if(quer.find(" *")!=-1)
				{
					tablemap::iterator it;
					string dat;
					long cnter = 1;
					for (it=mapo->begin();it!=mapo->end();it++,cnter++)
					{
						dat += it->second;
						usleep(5);
					}
					dat += "F";
					int totn = dat.length();
					int sentn = 0;
					while(totn>0)
					{
						sentn = send(fd, dat.c_str(), dat.length(), 0);
						cout << sentn << " " << errno << endl;
						if(sentn!=-1)
						{
							dat = dat.substr(sentn);
							totn -= sentn;
						}
						else
							usleep(100);
					}
					//cout << dat.length() << " " << sentn << " done sending all" << endl;
				}
				else if(quer.find(" < ")!=-1 && que.size()==4)
				{
					tablemap::iterator it;
					string dat;
					long cnter = 1;
					for (it=mapo->begin();it!=mapo->end();it++,cnter++)
					{
						if(it->first<boost::lexical_cast<long>(que[3]))
							dat += it->second;
					}
					dat += "F";
					int totn = dat.length();
					int sentn = 0;
					while(totn>0)
					{
						sentn = send(fd, dat.c_str(), dat.length(), 0);
						cout << sentn << " " << errno << endl;
						if(sentn!=-1)
						{
							dat = dat.substr(sentn);
							totn -= sentn;
						}
						else
							usleep(100);
					}
					//cout << dat.length() << " " << sentn << " done sending all" << endl;
				}
				else if(que.size()==4)
				{
					tablemap::iterator it;
					string dat;
					long cnter = 1;
					for (it=mapo->begin();it!=mapo->end();it++,cnter++)
					{
						if(it->first>boost::lexical_cast<long>(que[3]))
							dat += it->second;
					}
					dat += "F";
					int totn = dat.length();
					int sentn = 0;
					while(totn>0)
					{
						sentn = send(fd, dat.c_str(), dat.length(), 0);
						cout << sentn << " " << errno << endl;
						if(sentn!=-1)
						{
							dat = dat.substr(sentn);
							totn -= sentn;
						}
						else
							usleep(100);
					}
					//cout << dat.length() << " " << sentn << " done sending all" << endl;
				}
				else
				{
					int err = send(fd, "F", 1, 0);
					if(err==0)break;
				}
			}
			//query->~AMEFObject();
			//cout << "end=" << query << endl;
			delete query;
			usleep(100);
			memset(&buf[0], 0, sizeof(buf));
		}
	}
}

int main(int argc, char* argv[])
{
	cached["sent"] = new map<long,string>;
	//signal(SIGSEGV,signalSIGSEGV);
	//signal(SIGPIPE,signalSIGPIPE);
	signal(SIGKILL,signalSIGKILL);
	int sockfd, new_fd;  // listen on sock_fd, new connection on new_fd
	struct addrinfo hints, *servinfo, *p;
	struct sockaddr_storage their_addr; // connector's address information
	socklen_t sin_size;
	struct sigaction sa;


	struct epoll_event ev;
	//struct rlimit rt;
	int yes=1;
	//char s[INET6_ADDRSTRLEN];
	int rv,nfds;
	memset(&hints, 0, sizeof hints);
    hints.ai_family = AF_UNSPEC;
    hints.ai_socktype = SOCK_STREAM;
    hints.ai_flags = AI_PASSIVE; // use my IP
    if ((rv = getaddrinfo(NULL, "6001", &hints, &servinfo)) != 0) {
        fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(rv));
        return 1;
    }

    // loop through all the results and bind to the first we can
    for(p = servinfo; p != NULL; p = p->ai_next) {
        if ((sockfd = socket(p->ai_family, p->ai_socktype,
                p->ai_protocol)) == -1) {
            perror("server: socket");
            continue;
        }
        if (setsockopt(sockfd, SOL_SOCKET, SO_REUSEADDR, &yes,
                sizeof(int)) == -1) {
            perror("setsockopt");
            exit(1);
        }
        if (bind(sockfd, p->ai_addr, p->ai_addrlen) == -1) {
			close(sockfd);
            perror("server: bind");
            continue;
        }
        break;
    }

    if (p == NULL)  {
        fprintf(stderr, "server: failed to bind\n");
        return 2;
    }
    freeaddrinfo(servinfo); // all done with this structure
    if (listen(sockfd, BACKLOGM) == -1) {
        perror("listen");
        exit(1);
    }
    fcntl(sockfd, F_SETFL, fcntl(sockfd, F_GETFD, 0) | O_NONBLOCK);
    sa.sa_handler = sigchld_handler; // reap all dead processes
    sigemptyset(&sa.sa_mask);
    sa.sa_flags = SA_RESTART;
    if (sigaction(SIGCHLD, &sa, NULL) == -1) {
        perror("sigaction");
        exit(1);
    }
    struct epoll_event events[MAXEPOLLSIZE];
	int epoll_handle = epoll_create(MAXEPOLLSIZE);
	ev.events = EPOLLIN | EPOLLPRI;
	ev.data.fd = sockfd;
	if (epoll_ctl(epoll_handle, EPOLL_CTL_ADD, sockfd, &ev) < 0)
	{
		fprintf(stderr, "epoll set insertion error: fd=%d\n", sockfd);
		return -1;
	}
	else
		printf("listener socket to join epoll success!\n");

    ofstream ofs("serv.ctrl");
    ofs << "Proces" << flush;
    ofs.close();
    cout << "listening on port "<< 6001 << endl;

    ifstream ifs("serv.ctrl");
    int curfds = 1;
    char buf[4];
    while(ifs.is_open())
	{

    	nfds = epoll_wait(epoll_handle, events, curfds,-1);

		if (nfds == -1)
		{
			perror("epoll_wait main process");
			//logfile << "Interruption Signal Received\n" << flush;
			printf("Interruption Signal Received\n");
			curfds = 1;
			if(errno==EBADF)
				cout << "\nInavlid fd" <<flush;
			else if(errno==EFAULT)
				cout << "\nThe memory area pointed to by events is not accessible" <<flush;
			else if(errno==EINTR)
				cout << "\ncall was interrupted by a signal handler before any of the requested events occurred" <<flush;
			else
				cout << "\nnot an epoll file descriptor" <<flush;
			//break;
		}

		for(int n=0;n<nfds;n++)
		{
			if (events[n].data.fd == sockfd)
			{
				new_fd = -1;
				sin_size = sizeof their_addr;
				new_fd = accept(sockfd, (struct sockaddr *)&their_addr, &sin_size);
				if (new_fd == -1)
				{
					perror("accept");
					continue;
				}
				else
				{
					boost::thread(boost::bind(&run,new_fd));
				}
			}
		}
	}
    ifs.close();
    return 0;
}
