/*
 * UnsubscribeFrame.cpp
 *
 *  Created on: Jan 7, 2014
 *      Author: matanto
 */
#include "../include/UnsubscribeFrame.h"

UnsubscribeFrame::UnsubscribeFrame(long id) : id (id) {

}

string UnsubscribeFrame::buildFrame() {
	string frame = string("UNSUBSCRIBE") + "\n" + string("id:") + TaskListenToKeyboard::longToString(id) + "\n";
	return frame;
}

