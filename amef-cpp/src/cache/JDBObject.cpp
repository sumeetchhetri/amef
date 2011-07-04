/*
 * JDBObject.cpp
 *
 *  Created on: Mar 7, 2011
 *      Author: chhetri.sumeet
 */

#include "JDBObject.h"
#include "iostream"


JDBObject::~JDBObject() {
	//cout << packets.size() << endl;
	for (int var = 0; var < (int)packets.size(); var++) {
		JDBObject *ob = packets.at(var);
		delete ob;
	}
	clear();
}
