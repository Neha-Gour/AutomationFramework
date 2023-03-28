import java.io.File;

import javax.mail.MessagingException;

import com.kizora.util.SendEmail;


public class TestSendMail {
	
public static void main(String[] args) throws MessagingException {
	
	 File attachment = new File(System.getProperty("user.dir")+ "/zip file/util.zip");
	    
	 SendEmail.TriggerEmailwithAttachment("neha.gour@kizora.com", "neha.gour@kizora.com", "","JUnit Report", "Check the PDF attachment.", attachment);		

	  }

}
