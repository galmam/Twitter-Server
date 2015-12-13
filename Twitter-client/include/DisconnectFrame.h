/*
 * DisconnectFrame.h
 *
 *  Created on: Jan 7, 2014
 *      Author: matanto
 */

#ifndef DISCONNECTFRAME_H_
#define DISCONNECTFRAME_H_

#include "StompFrame.h"
#include "TaskListenToKeyboard.h"

class DisconnectFrame : public StompFrame {

private:
	long receipt;

public:
	DisconnectFrame (long receipt);
	~DisconnectFrame () {};
	string buildFrame ();
};


#endif /* DISCONNECTFRAME_H_ */
