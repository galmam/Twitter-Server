/*
 * SubscribeFrame.cpp
 *
 *  Created on: Jan 7, 2014
 *      Author: matanto
 */
#include "../include/SubscribeFrame.h"

SubscribeFrame::SubscribeFrame(const string& id, const string& destination) : id (id), destination (destination) {

}


string SubscribeFrame::buildFrame() {
	string frame = string("SUBSCRIBE") + "\n" + string("destination:") + destination + "\n" + string("id:") + id + "\n";
	return frame;
}

