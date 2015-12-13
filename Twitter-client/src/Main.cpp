/*
 * Main.cpp
 *
 *  Created on: Jan 10, 2014
 *      Author: matanto
 */


#include "../include/TaskListenToKeyboard.h"
#include <boost/thread.hpp>

using namespace std;

int main (int argc, char **argv) {

	StompClient *client = new StompClient ();
	TaskListenToKeyboard* listenToKeyboard = new TaskListenToKeyboard (client) ;

	boost::thread t1(&TaskListenToKeyboard::run, listenToKeyboard);

	t1.join();
	delete client;
	delete listenToKeyboard;
	return 0;
}


