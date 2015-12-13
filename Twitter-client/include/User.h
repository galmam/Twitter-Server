/*
 * FollowsByUserName.h
 *
 *  Created on: Jan 8, 2014
 *      Author: matanto
 */

#ifndef FOLLOWSBYUSERNAME_H_
#define FOLLOWSBYUSERNAME_H_

#include <string>
#include <vector>
#include <map>
#include <iostream>

using namespace std;

class User {

private:

	string userName;
	map<string, long> userFollows;
	bool isLoggedIn;

public:

	User (const string& followingUserName);
	User ();
	~User () {};
	long getSubscriptionId (const string& followedUser);
	string getUserName ();
	void addNewFollowedUser (const string& userName, long id);
	void removeFollowedUser (const string& userName);
	void logIn ();
	void logOut ();
	bool getLoggedIn ();
	static void split (const string& str, const string& delimiters , vector<string>& tokens);

};


#endif /* FOLLOWSBYUSERNAME_H_ */
