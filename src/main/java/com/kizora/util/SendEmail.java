package com.kizora.util;

import java.io.File;
import com.amazonaws.regions.Regions;
import com.kizora.base.SES;


public class SendEmail {
	private static final SES sesEmailSender = new SES( Regions.EU_WEST_1 );

//	public static void main( String[] args ) {
//		try {
//			// set aws credentials into system property
//			System.setProperty( "aws.accessKeyId", "" );
//			System.setProperty( "aws.secretKey", "" );
//			
//			File attachment = new File(System.getProperty("user.dir")+ "/zip file/util.zip");
//
//			//sesEmailSender.sendPlainTextEmail( "neha.gour@kizora.com", "kirti.vidhani@kizora.com", "Test email", "simple text mail" );
//			sesEmailSender.sendPlainTextEmailWithAttachment("neha.gour@kizora.com", "neha.gour@kizora.com", "", "TestMail", "PFA Attachment",attachment );
//			System.out.println("Email sent");
//			
//		} catch( Exception e ) {
//			throw new RuntimeException( e );
//		}
//	}
	
	public static void TriggerEmailwithAttachment(String From, String To, String Bcc, String Subject, String Body, File attachment) {
		
		try {
			// set aws credentials into system property
			System.setProperty( "aws.accessKeyId", "" );
			System.setProperty( "aws.secretKey", "" );

			//sesEmailSender.sendPlainTextEmail( "neha.gour@kizora.com", "kirti.vidhani@kizora.com", "Test email", "simple text mail" );
			sesEmailSender.sendPlainTextEmailWithAttachment(From, To, Bcc,Subject,Body,attachment );
			System.out.println("Email sent");
			
		} catch( Exception e ) {
			throw new RuntimeException( e );
		}
	}
		
	}


