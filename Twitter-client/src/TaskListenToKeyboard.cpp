/*
 * TaskListenToKeyboard.cpp
 *
 *  Created on: Jan 7, 2014
 *      Author: matanto
 */

#include "../include/TaskListenToKeyboard.h"


TaskListenToKeyboard::TaskListenToKeyboard (StompClient* stompClient) : client(stompClient), connectionHandler() , _mutex (), cond (), listenToServer (), shouldStop (false), NUM_OF_ARGUMENTS_LOGIN(5),NUM_OF_ARGUMENTS_FOLLOW(2), NUM_OF_ARGUMENTS_LOGOUT(1) {

}

TaskListenToKeyboard::TaskListenToKeyboard(const TaskListenToKeyboard& listenToKeyboard) : client(listenToKeyboard.client), connectionHandler(listenToKeyboard.connectionHandler),
		_mutex (listenToKeyboard._mutex), cond (listenToKeyboard.cond), listenToServer (listenToKeyboard.listenToServer), shouldStop (false), NUM_OF_ARGUMENTS_LOGIN (5), NUM_OF_ARGUMENTS_FOLLOW(2), NUM_OF_ARGUMENTS_LOGOUT(1) {

}

void TaskListenToKeyboard::run() {

	string line;

	while (getline (cin, line) && (!shouldStop)) {
		vector<string> tokens;
		User::split(line, " ", tokens);
		if ((tokens[0].compare("login") == 0) && (tokens.size() == NUM_OF_ARGUMENTS_LOGIN)) {
			executeLoginCommand(tokens);
		}
		else if ((tokens[0].compare("follow") == 0) && (tokens.size() == NUM_OF_ARGUMENTS_FOLLOW) &&
				(client->getCurrentUser()->getLoggedIn())) {
			executeFollowCommand(tokens);
		}
		else if ((tokens[0].compare("tweet") == 0) && (client->getCurrentUser()->getLoggedIn())) {
			executeTweetCommand(tokens);
		}
		else if ((tokens[0].compare("unfollow") == 0) && (tokens.size() == NUM_OF_ARGUMENTS_FOLLOW) &&
				(client->getCurrentUser()->getLoggedIn())) {
			executeUnfollowCommand(tokens);
		}
		else if ((tokens[0].compare("clients") == 0) && (tokens.size() == NUM_OF_ARGUMENTS_LOGOUT) &&
				(client->getCurrentUser()->getLoggedIn())) {
			executeClientsCommand();
		}
		else if ((tokens[0].compare("clients") == 0) && (tokens[1].compare("online") == 0) && (tokens.size() == NUM_OF_ARGUMENTS_FOLLOW) &&
				(client->getCurrentUser()->getLoggedIn())) {
			executeClientsOnlineCommand();
		}
		else if ((tokens[0].compare("stats") == 0) && (tokens.size() == NUM_OF_ARGUMENTS_LOGOUT) &&
				(client->getCurrentUser()->getLoggedIn())) {
			executeStatsCommand();
		}
		else if ((tokens[0].compare("logout") == 0) && (tokens.size() == NUM_OF_ARGUMENTS_LOGOUT) &&
				(client->getCurrentUser()->getLoggedIn())) {
			executeLogoutCommand();
		}
		else if ((tokens[0].compare("exit_client") == 0) && (tokens.size() == NUM_OF_ARGUMENTS_LOGOUT)) {
			if (client->getCurrentUser()->getLoggedIn())
				executeLogoutCommand();
			executeExitClientCommand();
			break;
		}
		else if ((tokens[0].compare("stop") == 0) && (tokens.size() == NUM_OF_ARGUMENTS_LOGOUT)) {
			executeStopCommand();
		}
		else
			cout << "Please enter the right arguments or login first!" << endl;
	}
}


string TaskListenToKeyboard::longToString(long num) {
	ostringstream convert;
	convert << num;
	string result = convert.str();
	return result;
}

string TaskListenToKeyboard::concatenateMessage(vector<string>& tokens) {
	string message("");
	for (unsigned i=1; i < tokens.size(); i++) {
		message += string (tokens[i]) + string(" ");
	}
	return message;
}

void TaskListenToKeyboard::executeLoginCommand(vector<string>& tokens) {
	string messageToSend;
	int port = atoi (tokens[2].c_str());
	ConnectionHandler *ch = new ConnectionHandler (tokens[1], port);
	delete this->connectionHandler;
	this->connectionHandler = ch;
	if (connectionHandler->connect()) {
		User *currentUser = new User (tokens[3]); // Initializing the current user
		delete this->client->getCurrentUser();
		client->setCurrentUser(currentUser);
		boost::mutex* mut = new boost::mutex ();
		boost::condition_variable* cond = new boost::condition_variable ();
		delete this->_mutex;
		this->_mutex = mut;
		delete this->cond;
		this->cond = cond;
		boost::mutex::scoped_lock lock(*mut);
		delete this->listenToServer;
		listenToServer = new TaskListenToServer (client, ch, mut, cond);
		boost::thread t2(&TaskListenToServer::run, listenToServer); // Start running the TaskListenToServer
		ConnectFrame *connectFrame = new ConnectFrame ("1.2", tokens[1], tokens[3], tokens[4], tokens[2]);
		messageToSend = connectFrame->buildFrame();
		connectionHandler->sendFrameAscii(messageToSend, '\0');
		cond->wait(lock); // Making the TaskListenToKeyboard wait until a connected frame is received
		delete connectFrame;
	}
	else
		cout << "Could not connect to server. Check your Internet connection, IP and port." << endl;
}

void TaskListenToKeyboard::executeLogoutCommand() {
	string messageToSend;
	long disconnectReceipt = this->client->generateReceipt();
	DisconnectFrame *disconnectFrame = new DisconnectFrame (disconnectReceipt);
	messageToSend = disconnectFrame->buildFrame();
	connectionHandler->sendFrameAscii(messageToSend, '\0');
	delete disconnectFrame;
}

void TaskListenToKeyboard::executeExitClientCommand() {
	this->shouldStop = true;
	listenToServer->setShouldStop(true);
	cout << "GoodBye!" << endl;
}

void TaskListenToKeyboard::executeStopCommand() {
	string messageToSend;
	string shutDownMessage ("stop");
	string destination ("/topic/server");
	SendFrame *sendFrame = new SendFrame (shutDownMessage, destination);
	messageToSend = sendFrame->buildFrame();
	connectionHandler->sendFrameAscii(messageToSend, '\0');
	cout << "The server stopped! GoodBye :)" << endl;
	connectionHandler->close();
	delete sendFrame;
}

void TaskListenToKeyboard::executeClientsCommand() {
	string messageToSend;
	string requestListOfAllUsersMessage ("clients");
	string destination ("/topic/server");
	SendFrame *sendFrame = new SendFrame (requestListOfAllUsersMessage, destination);
	messageToSend = sendFrame->buildFrame();
	connectionHandler->sendFrameAscii(messageToSend, '\0');
	delete sendFrame;
}

void TaskListenToKeyboard::executeStatsCommand() {
	string messageToSend;
	string requestListOfAllUsersMessage ("stats");
	string destination ("/topic/server");
	SendFrame *sendFrame = new SendFrame (requestListOfAllUsersMessage, destination);
	messageToSend = sendFrame->buildFrame();
	connectionHandler->sendFrameAscii(messageToSend, '\0');
	delete sendFrame;
}

void TaskListenToKeyboard::executeTweetCommand(vector<string>& tokens) {
	string messageToSend;
	string messageToTweet = concatenateMessage (tokens);
	SendFrame *sendFrame = new SendFrame (messageToTweet, string("/topic/") + client->getCurrentUser()->getUserName());
	messageToSend = sendFrame->buildFrame();
	connectionHandler->sendFrameAscii(messageToSend, '\0');
	delete sendFrame;
}

void TaskListenToKeyboard::executeClientsOnlineCommand() {
	string messageToSend;
	string requestListOfAllUsersMessage ("clients online");
	string destination ("/topic/server");
	SendFrame *sendFrame = new SendFrame (requestListOfAllUsersMessage, destination);
	messageToSend = sendFrame->buildFrame();
	connectionHandler->sendFrameAscii(messageToSend, '\0');
	delete sendFrame;
}

void TaskListenToKeyboard::executeUnfollowCommand(vector<string>& tokens) {
	string messageToSend;
	string followedUser = tokens[1]; // The user we want to unfollow
	long subscriptionId = client->getCurrentUser()->getSubscriptionId(tokens[1]);
	if (subscriptionId != -1) {
		UnsubscribeFrame *unsubscribeFrame = new UnsubscribeFrame (subscriptionId);
		messageToSend = unsubscribeFrame->buildFrame();
		client->getCurrentUser()->removeFollowedUser(tokens[1]);
		connectionHandler->sendFrameAscii(messageToSend, '\0');
		delete unsubscribeFrame;
	}

}

void TaskListenToKeyboard::executeFollowCommand(vector<string>& tokens) {
	string messageToSend;
	long id = this->client->generateFollowId(); // Generating unique id for the "follow" action
	client->getCurrentUser()->addNewFollowedUser(tokens[1], id);
	string stringId = longToString (id); // Converting the id into string
	string topicName = string("/topic/") + tokens[1];
	SubscribeFrame *subscribeFrame = new SubscribeFrame (stringId, topicName);
	messageToSend = subscribeFrame->buildFrame();
	connectionHandler->sendFrameAscii(messageToSend, '\0');
	delete subscribeFrame;
}


TaskListenToKeyboard& TaskListenToKeyboard::operator =(const TaskListenToKeyboard& listenToKeyboard) {
	return *this;
}


