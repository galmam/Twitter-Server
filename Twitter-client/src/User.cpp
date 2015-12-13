/*
 * FollowsByUserName.cpp
 *
 *  Created on: Jan 8, 2014
 *      Author: matanto
 */

#include "../include/User.h"


User::User(const string& followingUserName) : userName (followingUserName), userFollows () , isLoggedIn (false) {
	userFollows[followingUserName] = 0;
}

User::User() : userName (""), userFollows () , isLoggedIn (false) {

}

long User::getSubscriptionId(const string& followedUser) {
	std::map<string , long>::iterator it;
	it = userFollows.find(followedUser);
	if (it == userFollows.end()) {
		cout << "You are not following " << followedUser << " !" << endl;
		return -1;
	}
	return userFollows.find(followedUser)->second;
}

string User::getUserName () {
	return this->userName;
}

void User::addNewFollowedUser(const string& userName, long id) {
	userFollows[userName] = id;
}

void User::removeFollowedUser(const string& userName) {
	userFollows.erase(userFollows.find(userName));
}

void User::logIn() {
	std::cout << userName << " logged in" << endl;
	this->isLoggedIn = true;
}


void User::logOut() {
	this->isLoggedIn = false;
}

bool User::getLoggedIn() {
	return this->isLoggedIn;
}

void User::split(const string& str, const string& delimiters , vector<string>& tokens)
{
	// Skip delimiters at beginning.
	string::size_type lastPos = str.find_first_not_of(delimiters, 0);
	// Find first "non-delimiter".
	string::size_type pos     = str.find_first_of(delimiters, lastPos);
	while (string::npos != pos || string::npos != lastPos)
	{
		// Found a token, add it to the vector.
		tokens.push_back(str.substr(lastPos, pos - lastPos));
		// Skip delimiters.  Note the "not_of"
		lastPos = str.find_first_not_of(delimiters, pos);
		// Find next "non-delimiter"
		pos = str.find_first_of(delimiters, lastPos);
	}
}
