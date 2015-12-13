package client;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import StompFrames.CONNECT;
import StompFrames.DISCONNECT;
import StompFrames.SEND;
import StompFrames.SUBSCRIBE;
import StompFrames.StompFrame;
import StompFrames.UNSUBSCRIBE;

public class TcpClient { 
	
	private  final  Logger LOGGER=Logger.getLogger("Logger");
    protected Map<String, String> subscrubtion_numbers = new HashMap<String, String>();
    private int subscrubtion_ids=1;

	
    private  void run() throws InterruptedException 
    { 
        try{ 

        	
            Encoder encoder = new SimpleEncoder(); 
            //InetAddress address = InetAddress.getByName(serverName);  // This interacts with a DNS server 
            InetAddress address=InetAddress.getLocalHost();
            System.out.println(address.getHostAddress());
            Socket socket = new Socket("127.0.1.1", 23456); 
            OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream(), encoder.getCharset());  
            // listen to the keyboard and send lines through the socket 
            InputStreamReader isr = new InputStreamReader(socket.getInputStream(), encoder.getCharset()); 
            Thread Listner=new Thread(new Listner(isr,this.LOGGER,socket));
            Listner.setName("Listner");
            Listner.start();
            System.out.println("wow");
            InputStreamReader keyboard = new InputStreamReader(System.in, encoder.getCharset());
            BufferedReader commands=new BufferedReader(keyboard);
            String line;
        	String username="";
            while((line=commands.readLine())!=null){
            	line.trim();
            	StompFrame msg=null;
            	String[] parts=line.split(" ");
            	if (parts[0].equals("connect")&& parts.length==3){msg=new CONNECT(parts[1],parts[2],address.getHostAddress());username=parts[1];}
            	if (parts[0].equals("follow")){
            		msg=new SUBSCRIBE(String.valueOf(this.subscrubtion_ids),parts[1]);
            		this.subscrubtion_numbers.put(parts[1], String.valueOf(this.subscrubtion_ids));
            		this.subscrubtion_ids++;}
            	if (parts[0].equals("unfollow")){msg=new UNSUBSCRIBE(this.subscrubtion_numbers.get(parts[1]),parts[1]);}
            	if (parts[0].equals("clients") && parts.length==1){msg=new SEND("server",parts[0]);}
            	if (parts[0].equals("clients") && parts.length==2){msg=new SEND("server",parts[0]+" "+parts[1]);}
            	if (parts[0].equals("logout")){msg=new DISCONNECT("23423");}
            	if (parts[0].equals("tweet") ){
            		StringBuilder s=new StringBuilder();
            		for(int i=1;i<parts.length;i++) { s.append(parts[i]);s.append(" ");}
            		msg=new SEND(username,s.toString());}
           		if (parts[0].equals("stats")){msg=new SEND("server",parts[0]);}
           		if (parts[0].equals("exit")){socket.close();}
           		
           		if (msg!=null) {osw.write(msg.toString());osw.flush();}
        	}


            
        } catch (UnknownHostException e ) { 
        	this.LOGGER.info("shit");
            return; 
        } catch (IOException e) { 
        	this.LOGGER.warning("shit2");
            return; 
        } 
    } 
     
    public static void main(String[] args) { 

        try {
    		final  Logger LOGGER = Logger.getLogger("Logger");
    		FileHandler handler=null;
			handler = new FileHandler("c1.log");
    		handler.setFormatter(new CustomizeFormatter()); 
    		LOGGER.getParent().getHandlers()[0].setFormatter(new CustomizeFormatter());
    		LOGGER.addHandler(handler);
        	
    		TcpClient client=new TcpClient();
			client.run();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
        catch (SecurityException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();}
        catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    } 
}
