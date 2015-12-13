/*
 * DisconnectFrame.cpp
 *
 *  Created on: Jan 7, 2014
 *      Author: matanto
 */
#include "../include/DisconnectFrame.h"

DisconnectFrame::DisconnectFrame(long receipt) : receipt (receipt) {

}


string DisconnectFrame::buildFrame() {
	string msg = TaskListenToKeyboard::longToString(receipt);
	string frame = string("DISCONNECT") + "\n" + string("receipt:") + msg + "\n";
	return frame;
}
