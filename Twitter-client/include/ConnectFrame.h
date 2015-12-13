/*
 * ConnectFrame.h
 *
 *  Created on: Jan 7, 2014
 *      Author: matanto
 */

#ifndef CONNECTFRAME_H_
#define CONNECTFRAME_H_

#include "StompFrame.h"

class ConnectFrame : public StompFrame {

private:
	string acceptVersion;
	string host;
	string loginName;
	string passcode;
	string port;

public:
	ConnectFrame (const string &acceptVersion, const string &host, const string &loginName, const string &passcode, const string &port);
	~ConnectFrame () {};
	string buildFrame ();
};


#endif /* CONNECTFRAME_H_ */
