/*
 * UnsubscribeFrame.h
 *
 *  Created on: Jan 7, 2014
 *      Author: matanto
 */

#ifndef UNSUBSCRIBEFRAME_H_
#define UNSUBSCRIBEFRAME_H_

#include "StompFrame.h"
#include "TaskListenToKeyboard.h"

class UnsubscribeFrame : public StompFrame {

private:
	long id;

public:
	UnsubscribeFrame (long id);
	~UnsubscribeFrame () {};
	string buildFrame ();
};


#endif /* UNSUBSCRIBEFRAME_H_ */
