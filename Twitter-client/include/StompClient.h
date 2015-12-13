/*
 * StompClient.h
 *
 *  Created on: Jan 7, 2014
 *      Author: matanto
 */

#ifndef STOMPCLIENT_H_
#define STOMPCLIENT_H_

#include <string>
#include <vector>
#include <iostream>
#include "User.h"


using namespace std;

class StompClient {

private:
	long idCounter;
	long receiptCounter;
	vector<User> users;
	User* currentUser; // Saves the user name that is currently logged in


public:
	StompClient ();
	StompClient (const StompClient& client);
	StompClient& operator = (const StompClient& client);
	~StompClient ();
	long generateFollowId ();
	long generateReceipt ();
	User* getCurrentUser ();
	long getReceiptCounter ();
	void setCurrentUser (User* user);


};


#endif /* STOMPCLIENT_H_ */
