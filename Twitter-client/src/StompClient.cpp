/*
 * StompClient.cpp
 *
 *  Created on: Jan 7, 2014
 *      Author: matanto
 */
#include "../include/StompClient.h"


using namespace std;

StompClient::StompClient (): idCounter(1), receiptCounter(0), users(), currentUser () {

}

StompClient::StompClient(const StompClient& client) : idCounter(1), receiptCounter(0), users(), currentUser () {

}

long StompClient::generateFollowId() {
	long temp = this->idCounter;
	this->idCounter ++;
	return temp;
}

long StompClient::generateReceipt() {
	long temp = this->receiptCounter;
	this->receiptCounter ++;
	return temp;
}


User* StompClient::getCurrentUser() {
	return this->currentUser;
}

long StompClient::getReceiptCounter() {
	return this->receiptCounter;
}

void StompClient::setCurrentUser(User* user) {
	this->currentUser = user;
}

StompClient& StompClient::operator = (const StompClient& client) {
	return *this;
}

StompClient::~StompClient() {
	delete currentUser;
}
