/*
 * ConnectFrame.cpp
 *
 *  Created on: Jan 7, 2014
 *      Author: matanto
 */
#include "../include/ConnectFrame.h"

ConnectFrame::ConnectFrame(const string &acceptVersion, const string &host, const string &loginName, const string &passcode, const string &port)
	: acceptVersion(acceptVersion), host(host), loginName(loginName), passcode(passcode), port(port) {

}

string ConnectFrame::buildFrame() {
	string frame = string("CONNECT") + "\n" + string("accept-version:") + acceptVersion + "\n" + string("host:") + host + "\n" + string("login:") + loginName + "\n" + string("password:") + passcode + "\n";
	return frame;
}

