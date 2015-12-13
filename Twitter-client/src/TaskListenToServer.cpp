/*
 * TaskListenToServer.cpp
 *
 *  Created on: Jan 10, 2014
 *      Author: matanto
 */

#include "../include/TaskListenToServer.h"

TaskListenToServer::TaskListenToServer(StompClient* client, ConnectionHandler* connectionHandler, boost::mutex* mut, boost::condition_variable* cond ) : connectionHandler(connectionHandler), client(client), _mutex (mut), cond (cond) , shouldStop(false){

}

TaskListenToServer::TaskListenToServer(const TaskListenToServer& listenToServer) : connectionHandler(listenToServer.connectionHandler), client(listenToServer.client), _mutex (listenToServer._mutex), cond (listenToServer.cond) , shouldStop(false) {

}

void TaskListenToServer::run() {
	string answer;

	while (!isShouldStop()) {
		answer = "" ;
		connectionHandler->getFrameAscii(answer, '\0');
		vector<string> tokens;
		User::split(answer, "\n", tokens);
		if (tokens[0].compare("MESSAGE") == 0) {
			gotAMessageFrame (tokens);
		}
		else if (tokens[0].compare("ERROR") == 0) {
			gotAnErrorFrame(tokens);
		}
		else if (tokens[0].compare("RECEIPT") == 0) {
			gotAReceiptFrame(tokens);
			break;
		}
		else if (tokens[0].compare("CONNECTED") == 0) {
			gotAConnectedFrame(tokens);
		}
	}
}

bool TaskListenToServer::isShouldStop() {
	return this->shouldStop;
}

void TaskListenToServer::gotAMessageFrame(vector<string>& tokens) {
	vector<string> info;
	User::split(tokens[1], ":", info);
	vector<string> info2;
	User::split(tokens[2], ":", info2);
	std::stringstream time;
	for (unsigned j=1; j<info2.size()-1; j++)
		time << info2[j] << ":";
	time << info2[info2.size()-1] ;
	std::stringstream message;
	for (unsigned i=6; i<tokens.size(); i++) {
		message << tokens[i] << "\n";
	}
	cout << "Got a message from : " << info[1] << "\n" << message.str() << "on " << time.str() << endl;
	// Writing to the HTML file
	string str = client->getCurrentUser()->getUserName() + ".html";
	std::ofstream myFile;
	myFile.open(str.c_str(), ios::ate | ios::app);
	if (myFile.is_open()) {
		myFile << "<table border='1'><tr><th>Sender</th><th>Message</th><th>Time</th></tr>";
		string msg = message.str().erase(message.str().size()-2, 2);
		myFile << "<tr><td>" << info[1] << "</td><td>" << msg << "</td><td>" << time.str() << "</td></tr>";
	}

}

void TaskListenToServer::gotAReceiptFrame(vector<string>& tokens) {
	vector<string> info;
	User::split(tokens[1], ":", info);
	string receiptId = info[1];
	long receipt = atol (receiptId.c_str());
	if (client->getReceiptCounter() == receipt + 1) {
		cout << "The user: " << client->getCurrentUser()->getUserName() << " logged out successfully" << endl;
		delete client->getCurrentUser();
		client->setCurrentUser(new User()); // Reseting the currentUser
	}
	connectionHandler->close(); // Closing the socket

}

void TaskListenToServer::gotAnErrorFrame(vector<string>& tokens) {
	vector<string> info;
	User::split(tokens[1], ":", info);
	string errorMessage;
	for (unsigned i=1; i < info.size(); i++) {
		errorMessage += string (info[i]);
	}
	cout << errorMessage << endl;
}

void TaskListenToServer::gotAConnectedFrame(vector<string>& tokens) {
	client->getCurrentUser()->logIn();
	boost::mutex::scoped_lock lock(*_mutex);
	cond->notify_all(); // Notifies the TaskListenToKeyboard that a connected frame has been received
}

void TaskListenToServer::setShouldStop(bool shouldStop) {
	this->shouldStop = shouldStop;
}

TaskListenToServer& TaskListenToServer::operator =(const TaskListenToServer& listenToServer) {
	return *this;
}

