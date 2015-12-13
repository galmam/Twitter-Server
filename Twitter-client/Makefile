Twitter_client: ConnectFrame.o ConnectionHandler.o DisconnectFrame.o User.o SendFrame.o StompClient.o Main.o StompFrame.o TaskListenToKeyboard.o SubscribeFrame.o TaskListenToServer.o UnsubscribeFrame.o 
	g++ -Wall -Weffc++ -g bin/UnsubscribeFrame.o bin/User.o bin/ConnectFrame.o bin/ConnectionHandler.o bin/DisconnectFrame.o bin/SendFrame.o bin/StompClient.o bin/Main.o bin/StompFrame.o bin/SubscribeFrame.o bin/TaskListenToServer.o bin/TaskListenToKeyboard.o -o bin/Twitter_client -L/usr/lib -L/usr/local/lib -L/usr/local/include -L/usr/include -lboost_thread -lboost_locale -lboost_system -lPocoFoundation -lPocoNet -lPocoXML -lPocoUtil

ConnectFrame.o : src/ConnectFrame.cpp
	g++ -Wall -Weffc++ -g -c src/ConnectFrame.cpp -o bin/ConnectFrame.o -Iinclude

ConnectionHandler.o : src/ConnectionHandler.cpp
	g++ -Wall -Weffc++ -g -c src/ConnectionHandler.cpp -o bin/ConnectionHandler.o -Iinclude

DisconnectFrame.o : src/DisconnectFrame.cpp
	g++ -Wall -Weffc++ -g -c src/DisconnectFrame.cpp -o bin/DisconnectFrame.o -Iinclude

User.o : src/User.cpp
	g++ -Wall -Weffc++ -g -c src/User.cpp -o bin/User.o -Iinclude	

SendFrame.o : src/SendFrame.cpp
	g++ -Wall -Weffc++ -g -c src/SendFrame.cpp -o bin/SendFrame.o -Iinclude

StompClient.o : src/StompClient.cpp
	g++ -Wall -Weffc++ -g -c src/StompClient.cpp -o bin/StompClient.o -Iinclude

Main.o : src/Main.cpp
	g++ -Wall -Weffc++ -g -c src/Main.cpp -o bin/Main.o -Iinclude

SubscribeFrame.o : src/SubscribeFrame.cpp
	g++ -Wall -Weffc++ -g -c src/SubscribeFrame.cpp -o bin/SubscribeFrame.o -Iinclude

StompFrame.o : src/StompFrame.cpp
	g++ -Wall -Weffc++ -g -c src/StompFrame.cpp -o bin/StompFrame.o -Iinclude

TaskListenToKeyboard.o : src/TaskListenToKeyboard.cpp
	g++ -Wall -Weffc++ -g -c src/TaskListenToKeyboard.cpp -o bin/TaskListenToKeyboard.o -Iinclude

TaskListenToServer.o : src/TaskListenToServer.cpp
	g++ -Wall -Weffc++ -g -c src/TaskListenToServer.cpp -o bin/TaskListenToServer.o -Iinclude
	
UnsubscribeFrame.o : src/UnsubscribeFrame.cpp
	g++ -Wall -Weffc++ -g -c src/UnsubscribeFrame.cpp -o bin/UnsubscribeFrame.o -Iinclude
	

clean:
	rm -f bin/*
