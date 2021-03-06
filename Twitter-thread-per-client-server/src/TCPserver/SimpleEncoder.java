package TCPserver;
import java.nio.charset.Charset; 
  
public class SimpleEncoder implements Encoder { 
  
        private static final String DFL_CHARSET = "ASCII"; 
        private Charset _charset; 
  
        public SimpleEncoder() { 
                this(DFL_CHARSET); 
        } 
  
        SimpleEncoder(String charset) { 
                _charset = Charset.forName(charset); 
        } 
  
        public byte [] toBytes(String s) { 
                return s.getBytes(_charset); 
        } 
  
        public String fromBytes(byte [] buf) { 
                return new String(buf, 0, buf.length, _charset); 
        } 
  
        public Charset getCharset()  { 
                return _charset; 
        } 
}