package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Logger;


public class Listner implements Runnable {

	private BufferedReader BR;
	private  final  Logger LOGGER;
	private Socket socket;
	
	public Listner(InputStreamReader isr,Logger logger,Socket socket){
		this.BR=new BufferedReader(isr);
		this.LOGGER=logger;
		this.socket=socket;

	}
	@Override
	public void run() {
    	try {
    		String line;
    		while((line=BR.readLine())!=null){
    			this.LOGGER.warning(line);
    			if (line.equals("RECEIPT")) {this.socket.close();}
    		}
		} 
    	catch (IOException e) {this.LOGGER.warning("somtihing worng");}

	}

}
