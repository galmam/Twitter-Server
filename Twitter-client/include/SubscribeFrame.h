/*
 * SubscribeFrame.h
 *
 *  Created on: Jan 7, 2014
 *      Author: matanto
 */

#ifndef SUBSCRIBEFRAME_H_
#define SUBSCRIBEFRAME_H_

#include "StompFrame.h"


class SubscribeFrame : public StompFrame {

private:
	string id;
	string destination;

public:
	SubscribeFrame (const string &id, const string &destination);
	~SubscribeFrame () {};
	string buildFrame ();
};


#endif /* SUBSCRIBEFRAME_H_ */
