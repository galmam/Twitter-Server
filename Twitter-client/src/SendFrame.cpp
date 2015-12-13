/*
 * SendFrame.cpp
 *
 *  Created on: Jan 7, 2014
 *      Author: matanto
 */
#include "../include/SendFrame.h"

SendFrame::SendFrame(const string &msg, const string &destination) : destination (destination), message (msg) {

}

string SendFrame::buildFrame() {
	string frame = string("SEND") + '\n' + string("destination:") + destination + "\n\n" + message + '\n';
	return frame;
}

