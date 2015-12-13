/*
 * TaskListenToKeyboard.h


 *
 *  Created on: Jan 7, 2014
 *      Author: matanto
 */



#ifndef TASKLISTENTOKEYBOARD_H_
#define TASKLISTENTOKEYBOARD_H_

#include <string>
#include <vector>
#include <iostream>
#include "SendFrame.h"
#include "ConnectFrame.h"
#include "SubscribeFrame.h"
#include "UnsubscribeFrame.h"
#include "DisconnectFrame.h"
#include "ConnectionHandler.h"
#include "TaskListenToServer.h"
#include <boost/thread.hpp>

using namespace std;

class TaskListenToKeyboard {

private:
	StompClient* client;
	ConnectionHandler* connectionHandler;
	boost::mutex* _mutex;
	boost::condition_variable* cond;
	TaskListenToServer* listenToServer;
	bool shouldStop;

	const unsigned int NUM_OF_ARGUMENTS_LOGIN;
	const unsigned int NUM_OF_ARGUMENTS_FOLLOW;
	const unsigned int NUM_OF_ARGUMENTS_LOGOUT;

public:
	TaskListenToKeyboard (StompClient* client);
	TaskListenToKeyboard (const TaskListenToKeyboard& listenToKeyboard);
	TaskListenToKeyboard& operator = (const TaskListenToKeyboard& listenToKeyboard);
	~TaskListenToKeyboard () {};
	void run ();
	string concatenateMessage (vector<string>& tokens);
	static string longToString (long num);
	void executeLoginCommand (vector<string>& tokens);
	void executeFollowCommand (vector<string>& tokens);
	void executeUnfollowCommand (vector<string>& tokens);
	void executeLogoutCommand ();
	void executeTweetCommand (vector<string>& tokens);
	void executeClientsCommand ();
	void executeClientsOnlineCommand ();
	void executeStatsCommand ();
	void executeExitClientCommand ();
	void executeStopCommand ();

};


#endif /* TASKLISTENTOKEYBOARD_H_ */

