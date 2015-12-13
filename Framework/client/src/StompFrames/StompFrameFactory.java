package StompFrames;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;


/**
 * A factory for creating StompFrame objects.
 */
public class StompFrameFactory {
	
	/**
	 * Creates an instance of a stompframe by the command
	 *
	 * @param String Command the command of the stomp frame to create
	 * @return the stomp frame
	 */
	@SuppressWarnings("rawtypes")
	public  static StompFrame Create(String Command){
	        StompFrame object=null;
	    	try {
	        	File file = new File(Command);
	            URL url;
	            URL[] urls;
				url = file.toURI().toURL();
	            urls = new URL[]{url};
	            ClassLoader cl = new URLClassLoader(urls);          
	            Class cls = cl.loadClass("StompFrames."+Command);     
	            object = (StompFrame) cls.newInstance();
	        } 
	        catch (InstantiationException e) {System.out.println("idiot");} 
	        catch (IllegalAccessException e) {System.out.println("idiot");}
	        catch (ClassNotFoundException e) {System.out.println("wrong message format");}
	    	catch (MalformedURLException e) {System.out.println("strange");}

			return object;
	     }
	}

