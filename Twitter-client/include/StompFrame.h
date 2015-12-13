/*
 * StompFrame.h
 *
 *  Created on: Jan 7, 2014
 *      Author: matanto
 */

#ifndef STOMPFRAME_H_
#define STOMPFRAME_H_

#include <string>

using namespace std;

class StompFrame {

public:
	virtual string buildFrame () =0;
	virtual ~StompFrame () {};

};


#endif /* STOMPFRAME_H_ */
