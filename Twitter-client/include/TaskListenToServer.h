/*
 * TaskListenToServer.h
 *
 *  Created on: Jan 10, 2014
 *      Author: matanto
 */

#ifndef TASKLISTENTOSERVER_H_
#define TASKLISTENTOSERVER_H_

#include "ConnectionHandler.h"
#include "StompClient.h"
#include <boost/thread.hpp>
#include <fstream>
#include <iostream>


class TaskListenToServer {

private:

	ConnectionHandler* connectionHandler;
	StompClient* client;
	boost::mutex* _mutex;
	boost::condition_variable* cond;
	bool shouldStop;

public:
	TaskListenToServer (StompClient* client, ConnectionHandler* connectionHandler, boost::mutex* mut, boost::condition_variable* cond);
	TaskListenToServer (const TaskListenToServer& listenToServer);
	TaskListenToServer& operator = (const TaskListenToServer& listenToServer);
	~TaskListenToServer () {};
	void run ();
	bool isShouldStop ();
	void gotAMessageFrame (vector<string>& tokens);
	void gotAnErrorFrame (vector<string>& tokens);
	void gotAReceiptFrame (vector<string>& tokens);
	void gotAConnectedFrame (vector<string>& tokens);
	void setShouldStop (bool shouldStop);
};


#endif /* TASKLISTENTOSERVER_H_ */
