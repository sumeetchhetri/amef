/*
 * TestJDBProtocol.cpp
 *
 *  Created on: Mar 7, 2011
 *      Author: chhetri.sumeet
 */
#include "Timer.h"
#include "TestJDBProtocol.h"
#include "Client.h"
#include "JdbResources.h"
#define BACKLOGM 500
#define MAXEPOLLSIZE 1000
#include "map"
using namespace std;

typedef map<long,string> tablemap;
map<string, tablemap*> cached;
boost::mutex lock;

int main1()
{
	Client client;
	client.connection("localhost",7001);
	JDBEncoder* encoder = new JDBEncoder();

	Timer t;
	JDBObject* query = NULL;
	for (int var = 0; var < 500; ++var)
	{
		t.start();
		JDBObject* object = new JDBObject();
		object->addNullPacket(JDBObject::NULL_STRING);
		object->addPacket("asdasD");
		object->addPacket(12312321);
		object->addPacket(true);
		object->addPacket(1233444555);

		query = new JDBObject();
		query->addPacket("insert sent");
		query->addPacket(encoder->encodeWL(object, true));
		string dat = encoder->encodeB(query, false);
		client.sendData(dat);
		string ret = client.getData();
		cout << ret << " Insert " << t.elapsedMilliSeconds() << endl;
		//object->~JDBObject();
		query->~JDBObject();
	}

	for (int var = 0; var < 500; ++var)
	{
		t.start();

		JDBObject* object = new JDBObject();
		object->addPacket("aaaaaaaaa","1");

		query = new JDBObject();
		query->addPacket("update sent 498");
		query->addPacket(encoder->encodeWL(object, false));
		string dat = encoder->encodeB(query, false);
		client.sendData(dat);
		string ret = client.getData();
		cout << ret << " Update " << t.elapsedMilliSeconds() << endl;
		//object->~JDBObject();
		query->~JDBObject();
	}

	//sleep(2);

	for (int var = 0; var < 500; ++var)
	{
		JDBDecoder decoder;
		t.start();
		query = new JDBObject();
		query->addPacket("select sent = 498");
		string dat = encoder->encodeB(query, false);
		client.sendData(dat);
		string ret = client.getData();
		JDBObject* obj = decoder.decodeB(ret.substr(0,ret.length()-1),false,true);
		cout << "Select " << t.elapsedMilliSeconds() << endl;
		query->~JDBObject();
	}
	encoder->~JDBEncoder();
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
	cout << data.length() <<endl;
	mapo->insert(pair<long,string>(key, data));
	cout<< que[1] << mapo->size() <<key<<endl;
}

void update(string quer,string data)
{
	vector<string> que;
	boost::iter_split(que, quer, boost::first_finder(" "));
	tablemap *mapo = cached[que[1]];

	long key = boost::lexical_cast<long>(que[2]);
	cout << data.length() <<endl;
	string odata = (*mapo)[key];
	JDBDecoder decoder;
	JDBObject* objn = new JDBObject;
	JDBObject* obje = decoder.decodeB(data,false,false);
	JDBObject* objo = decoder.decodeB(odata,false,true);
	mapo->erase(key);
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
	JDBEncoder encoder;
	string ndata = encoder.encodeWL(objn, true);
	mapo->insert(pair<long,string>(key, ndata));
	delete obje;
	delete objo;
	delete objn;
	cout<< que[1] << " " << key << " " << ndata <<key<<endl;
}

int main4()
{
	for(int i = 1; i < 10; ++i){
		cached["sent"] = new map<long,string>;
		for (int var = 1; var < 10000; ++var) {
			string dat = "asdasdasdasdasdasdas";
			JDBObject* object = new JDBObject();
			object->addNullPacket(JDBObject::NULL_STRING);
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


void move(string quer)
{
	vector<string> que;
	boost::iter_split(que, quer, boost::first_finder(" "));
	tablemap *mapof = cached[que[1]];
	tablemap *mapot = cached[que[2]];
	long keyo = boost::lexical_cast<long>(que[3]);
	long key = keyo;
	if(que.size()==5)
		key = boost::lexical_cast<long>(que[4]);
	////cout << data.length() <<endl;
	lock.lock();
	mapot->insert(pair<long,string>(key, (*mapof)[keyo]));
	mapof->erase(keyo);
	lock.unlock();
	//cout<< que[1]  <<":"<< keyo <<":"<<mapof->size()<< "  " << que[2] <<":"<<key<<":"<<mapot->size()<<endl;
}


void movewrt(string quer)
{
	JDBDecoder decoder;
	vector<string> que;
	boost::iter_split(que, quer, boost::first_finder(" "));
	tablemap *mapof = cached[que[1]];
	tablemap *mapot = cached[que[2]];
	long keyo = boost::lexical_cast<long>(que[3]);
	long key = keyo;
	if(que.size()==5)
		key = boost::lexical_cast<long>(que[4]);
	JDBObject* obj = decoder.decodeB((*mapof)[keyo],false,true);
	if(obj!=NULL)
	{
		if(obj->getPackets().size()>key)
		{
			key = boost::lexical_cast<long>(obj->getPackets().at(key)->getValueStr());
		}
	}
	//cout << "movewrt command key is "<< key <<endl;
	lock.lock();
	mapot->insert(pair<long,string>(key, (*mapof)[keyo]));
	mapof->erase(keyo);
	lock.unlock();
	delete obj;
	//cout<< que[1]  <<":"<< keyo <<":"<<mapof->size()<< "  " << que[2] <<":"<<key<<":"<<mapot->size()<<endl;
}

void apmov(string quer,string val)
{
	JDBDecoder decoder;
	vector<string> que;
	boost::iter_split(que, quer, boost::first_finder(" "));
	tablemap *mapof = cached[que[1]];
	tablemap *mapot = cached[que[2]];
	long keyo = boost::lexical_cast<long>(que[3]);
	long key = keyo;
	if(que.size()==5)
		key = boost::lexical_cast<long>(que[4]);
	JDBObject* objn = new JDBObject;
	JDBObject* obj = decoder.decodeB((*mapof)[keyo],false,true);
	JDBObject* obje = decoder.decodeB(val,false,true);
	for (int j = 0; j < (int)obj->getPackets().size(); j++)
	{
		if(obj->getPackets().at(j)->isString())
		{
			objn->addPacket(obj->getPackets().at(j)->getValue());
		}
		else if(obj->getPackets().at(j)->isFloatingPoint())
		{
			objn->addPacket(obj->getPackets().at(j)->getDoubleValue());
		}
		else if(obj->getPackets().at(j)->isnumber())
		{
			objn->addPacket(obj->getPackets().at(j)->getNumericValue());
		}
		else if(obj->getPackets().at(j)->isNull())
		{
			objn->addNullPacket(obj->getPackets().at(j)->getType());
		}
		else if(obj->getPackets().at(j)->getType()=='b')
		{
			objn->addPacket(obj->getPackets().at(j)->getBooleanValue());
		}
		else
		{
			objn->addPacket(obj->getPackets().at(j)->getValue()[0]);
		}
	}
	for (int j = 0; j < (int)obje->getPackets().size(); j++)
	{
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
	JDBEncoder encoder;
	string ndata = encoder.encodeWL(objn, true);
	lock.lock();
	mapot->insert(pair<long,string>(key, ndata));
	mapof->erase(keyo);
	lock.unlock();
	delete obj;
	delete obje;
	delete objn;
	//cout<< que[1]  <<":"<< keyo <<":"<<mapof->size()<< "  " << que[2] <<":"<<key<<":"<<mapot->size()<<endl;
}


void apmovwrt(string quer,string val)
{
	JDBDecoder decoder;
	vector<string> que;
	boost::iter_split(que, quer, boost::first_finder(" "));
	tablemap *mapof = cached[que[1]];
	tablemap *mapot = cached[que[2]];
	long keyo = boost::lexical_cast<long>(que[3]);
	long key = keyo;
	if(que.size()==5)
		key = boost::lexical_cast<long>(que[4]);
	JDBObject* obj = decoder.decodeB((*mapof)[keyo],false,true);
	if(obj!=NULL)
	{
		if(obj->getPackets().size()>key)
		{
			key = boost::lexical_cast<long>(obj->getPackets().at(key)->getValueStr());
		}
	}
	JDBObject* objn = new JDBObject;
	JDBObject* obje = decoder.decodeB(val,false,true);
	for (int j = 0; j < (int)obj->getPackets().size(); j++)
	{
		if(obj->getPackets().at(j)->isString())
		{
			objn->addPacket(obj->getPackets().at(j)->getValue());
		}
		else if(obj->getPackets().at(j)->isFloatingPoint())
		{
			objn->addPacket(obj->getPackets().at(j)->getDoubleValue());
		}
		else if(obj->getPackets().at(j)->isnumber())
		{
			objn->addPacket(obj->getPackets().at(j)->getNumericValue());
		}
		else if(obj->getPackets().at(j)->isNull())
		{
			objn->addNullPacket(obj->getPackets().at(j)->getType());
		}
		else if(obj->getPackets().at(j)->getType()=='b')
		{
			objn->addPacket(obj->getPackets().at(j)->getBooleanValue());
		}
		else
		{
			objn->addPacket(obj->getPackets().at(j)->getValue()[0]);
		}
	}
	for (int j = 0; j < (int)obje->getPackets().size(); j++)
	{
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
	JDBEncoder encoder;
	string ndata = encoder.encodeWL(objn, true);
	lock.lock();
	mapot->insert(pair<long,string>(key, ndata));
	mapof->erase(keyo);
	lock.unlock();
	delete obj;
	delete obje;
	delete objn;
	//cout<< que[1]  <<":"<< keyo <<":"<<mapof->size()<< "  " << que[2] <<":"<<key<<":"<<mapot->size()<<endl;
}

int main(int argc, char* argv[])
{
	cached["sent"] = new map<long,string>;
	cached["send"] = new map<long,string>;
	cached["sendhlr"] = new map<long,string>;
	cached["deliver"] = new map<long,string>;
	cached["delivered"] = new map<long,string>;
	cached["routes"] = new map<long,string>;
	cached["provision"] = new map<long,string>;
	cached["client"] = new map<long,string>;
	cached["tinvalid"] = new map<long,string>;
	cached["invalid"] = new map<long,string>;
	cached["retry"] = new map<long,string>;
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
		vector<string> que;
		string ln,data,quer;
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
					curfds++;
					fcntl(new_fd, F_SETFL, fcntl(new_fd, F_GETFD, 0) | O_NONBLOCK);
					ev.events = EPOLLIN | EPOLLPRI;
					ev.data.fd = new_fd;
					if (epoll_ctl(epoll_handle, EPOLL_CTL_ADD, new_fd, &ev) < 0)
					{
						perror("epoll");
						cout << "\nerror adding to epoll cntl list" << flush;
						return -1;
					}
				}
			}
			else
			{
				int err;
				if((err=recv(events[n].data.fd,buf,4,MSG_WAITALL))==0)
				{
					close(events[n].data.fd);
					//cout << "\nsocket conn closed before being serviced" << flush;
					epoll_ctl(epoll_handle, EPOLL_CTL_DEL, events[n].data.fd,&ev);
					curfds--;
				}
				else if(err==4)
				{
					int fd = events[n].data.fd;
					JDBDecoder decoder;
					//cout << "got some query" << endl;
					for (int var = 0; var < 4; var++) {
						ln.push_back(buf[var]);
					}
					int len = getLength(ln,4);
					//cout << len << endl;
					char* buff = new char[len];

					err = recv(events[n].data.fd,buff,len,MSG_WAITALL);
					for (int var = 0; var < len; var++) {
						data.push_back(buff[var]);
					}
					//if(data.find("mov")!=string::npos)cout << data << endl;
					//memset(&buf[0], 0, sizeof(buf));
					delete[] buff;
					JDBObject* query = decoder.decodeB(data,false,false);
					//cout << query << endl;
					string quer = query->getPackets().at(0)->getValue();
					if(quer.find("insert ")!=-1 && query->getPackets().size()==2/*
										&& query->getPackets().at(1)->getValue()!=""*/)
					{
						//insert(quer, query->getPackets().at(1)->getValue());
						boost::iter_split(que, quer, boost::first_finder(" "));
						tablemap *mapo = cached[que[1]];
						long key = boost::lexical_cast<long>(que[2]);
						string val = query->getPackets().at(1)->getValue();
						////cout << data.length() <<endl;
						lock.lock();
						mapo->insert(pair<long,string>(key, val));
						lock.unlock();
						int err = send(fd, "T", 1, 0);
						if(err==0)break;
					}
					else if(quer.find("select ")!=-1 && quer.find(" = ")!=-1)
					{
						boost::iter_split(que, quer, boost::first_finder(" "));
						tablemap *mapo = cached[que[1]];
						////cout << que[1] << mapo->size() << que[3]<<  endl;
						string dat = (*mapo)[boost::lexical_cast<long>(que[3])];
						dat += "F";
						////cout << dat << endl;
						int err = send(fd, dat.c_str(), dat.length(), 0);
						if(err==0)break;
					}
					else if(quer.find("select ")!=-1 &&
						(quer.find(" < ")!=-1 ||  quer.find(" > ")!=-1 || quer.find(" *")!=-1))
					{
						boost::iter_split(que, quer, boost::first_finder(" "));
						tablemap *mapo = cached[que[1]];
						if(quer.find(" *")!=-1)
						{
							tablemap::iterator it;
							string dat;
							long cnter = 1;
							long limit = mapo->size();
							if(que.size()>4 && que[3]=="limit")
								limit = boost::lexical_cast<long>(que[4]);
							for (it=mapo->begin();it!=mapo->end();it++,cnter++)
							{
								dat += it->second;
								if(cnter==limit)
									break;
							}
							dat += "F";
							int totn = dat.length();
							int sentn = 0;
							while(totn>0)
							{
								sentn = send(fd, dat.c_str(), dat.length(), 0);
								//cout << sentn << " " << errno << endl;
								if(sentn!=-1)
								{
									dat = dat.substr(sentn);
									totn -= sentn;
								}
								else
									usleep(100);
							}
							//cout << dat << " done sending all" << endl;
						}
						else if(quer.find(" < ")!=-1 && que.size()>=4)
						{
							tablemap::iterator it;
							string dat;
							long cnter = 1;
							long limit = mapo->size();
							if(que[4]=="limit")
								limit = boost::lexical_cast<long>(que[5]);
							for (it=mapo->begin();it!=mapo->end();it++)
							{
								if(it->first<boost::lexical_cast<long>(que[3]))
								{
									dat += it->second;
									if(cnter++>=limit)
										break;
								}
							}
							dat += "F";
							int totn = dat.length();
							int sentn = 0;
							while(totn>0)
							{
								sentn = send(fd, dat.c_str(), dat.length(), 0);
								//cout << sentn << " " << errno << endl;
								if(sentn!=-1)
								{
									dat = dat.substr(sentn);
									totn -= sentn;
								}
								else
									usleep(100);
							}
							////cout << dat.length() << " " << sentn << " done sending all" << endl;
						}
						else if(que.size()>=4)
						{
							tablemap::iterator it;
							string dat;
							long cnter = 1;
							long limit = mapo->size();
							if(que[4]=="limit")
								limit = boost::lexical_cast<long>(que[5]);
							for (it=mapo->begin();it!=mapo->end();it++)
							{
								//cout << it->first << " > " << que[3] << endl;
								if(it->first>boost::lexical_cast<long>(que[3]))
								{
									dat += it->second;
									if(cnter++>=limit)
										break;
								}
							}
							dat += "F";
							int totn = dat.length();
							int sentn = 0;
							//cout << dat << " done sending all" << endl;
							while(totn>0)
							{
								sentn = send(fd, dat.c_str(), dat.length(), 0);
								//cout << sentn << " " << errno << endl;
								if(sentn!=-1)
								{
									dat = dat.substr(sentn);
									totn -= sentn;
								}
								else
									usleep(100);
							}
						}
						else
						{
							//cout << "invalid command " << quer << endl;
							int err = send(fd, "F", 1, 0);
							if(err==0)break;
						}
					}
					else if(quer.find("update ")!=-1)
					{
						update(quer, query->getPackets().at(1)->getValue());
						int err = send(fd, "T", 1, 0);
						if(err==0)break;
					}
					else if(quer.find("move ")!=-1)
					{
						move(quer);
						int err = send(fd, "T", 1, 0);
						if(err==0)break;
					}
					else if(quer.find("movegt ")!=-1)
					{
						tablemap::iterator it;
						boost::iter_split(que, quer, boost::first_finder(" "));
						tablemap *mapf = cached[que[1]];
						tablemap *mapt = cached[que[2]];
						long did = boost::lexical_cast<long>(que[3]);
						for (it=mapf->begin();it!=mapf->end();it++)
						{
							if(it->first>did)
							{
								lock.lock();
								mapt->insert(pair<long,string>(it->first, (*mapf)[it->first]));
								mapf->erase(it->first);
								lock.unlock();
							}
						}
						int err = send(fd, "T", 1, 0);
						if(err==0)break;
					}
					else if(quer.find("movelt ")!=-1)
					{
						tablemap::iterator it;
						boost::iter_split(que, quer, boost::first_finder(" "));
						tablemap *mapf = cached[que[1]];
						tablemap *mapt = cached[que[2]];
						long did = boost::lexical_cast<long>(que[3]);
						for (it=mapf->begin();it!=mapf->end();it++)
						{
							if(it->first<did)
							{
								lock.lock();
								mapt->insert(pair<long,string>(it->first, (*mapf)[it->first]));
								mapf->erase(it->first);
								lock.unlock();
							}
						}
						int err = send(fd, "T", 1, 0);
						if(err==0)break;
					}
					else if(quer.find("movewrt ")!=-1)
					{
						movewrt(quer);
						int err = send(fd, "T", 1, 0);
						if(err==0)break;
					}
					else if(quer.find("apmov ")!=-1)
					{
						string val = query->getPackets().at(1)->getValue();
						apmov(quer,val);
						int err = send(fd, "T", 1, 0);
						if(err==0)break;
					}
					else if(quer.find("apmovwrt ")!=-1)
					{
						string val = query->getPackets().at(1)->getValue();
						apmovwrt(quer,val);
						int err = send(fd, "T", 1, 0);
						if(err==0)break;
					}
					else if(quer.find("delete ")!=-1)
					{
						////cout << quer << endl;
						boost::iter_split(que, quer, boost::first_finder(" "));
						tablemap *mapo = cached[que[1]];
						////cout << que[1] << mapo->size() << que[2]<<  endl;
						if(que[2]=="*")
						{
							lock.lock();
							mapo->empty();
							mapo->clear();
							lock.unlock();
						}
						else if(que[2]=="=")
						{
							lock.lock();
							mapo->erase(boost::lexical_cast<long>(que[3]));
							lock.unlock();
						}
						else if(que[2]=="<")
						{
							tablemap::iterator it;
							long did = boost::lexical_cast<long>(que[3]);
							for (it=mapo->begin();it!=mapo->end();it++)
							{
								if(it->first<did)
								{
									lock.lock();
									mapo->erase(did);
									lock.unlock();
								}
							}
						}
						else if(que[2]==">")
						{
							tablemap::iterator it;
							long did = boost::lexical_cast<long>(que[3]);
							for (it=mapo->begin();it!=mapo->end();it++)
							{
								if(it->first>did)
								{
									lock.lock();
									mapo->erase(did);
									lock.unlock();
								}
							}
						}
						int err = send(fd, "T", 1, 0);
						if(err==0)break;
					}
					//query->~JDBObject();
					//cout << "end=" << query << endl;
					delete query;
					usleep(100);
					quer.clear();
					data.clear();
					ln.clear();
					que.clear();
					memset(&buf[0], 0, sizeof(buf));
					//cout << "endnull=" << query << endl;
				}

			}
		}
	}
    ifs.close();
    return 0;
}

