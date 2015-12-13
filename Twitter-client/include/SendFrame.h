/*
 * SendFrame.h
 *
 *  Created on: Jan 7, 2014
 *      Author: matanto
 */

#ifndef SENDFRAME_H_
#define SENDFRAME_H_

#include "StompFrame.h"

class SendFrame : public StompFrame {

private:
	string destination;
	string message;

public:
	SendFrame (const string &msg, const string &destination);
	~SendFrame () {};
	string buildFrame ();
};


#endif /* SENDFRAME_H_ */
