package com.kizora.base;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;
import org.apache.commons.lang3.StringUtils;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.MimetypesFileTypeMap;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Properties;


@SuppressWarnings( "unused" )
public class SES {
	
	// Change this according to environment
	private static final Regions REGION = Regions.EU_WEST_1;

	// AWS SES Client
	private final AmazonSimpleEmailService client;

	public SES (Regions region) {
		this.client = AmazonSimpleEmailServiceClientBuilder.standard().withRegion( region ).build();
	}

	public SES()
	{
		this.client = AmazonSimpleEmailServiceClientBuilder.standard().withRegion( REGION ).build();
	}

	/**
	 * Sends a PLAIN/TEXT email through SES returning the Message ID
	 *
	 * @param from    From Address
	 * @param to      To Address (CSV Format)
	 * @param subject Subject Line
	 * @param body    Body Line
	 * @return MessageId
	 * @throws Exception Amazon Exception
	 */
	public String sendPlainTextEmail( String from, String to, String subject, String body ) throws Exception {
		return sendPlainTextEmail( from, to, null, subject, body );
	}


	/**
	 * Sends a PLAIN/TEXT email through SES returning the Message ID
	 *
	 * @param from    From Address
	 * @param to      To Address (CSV Format)
	 * @param bcc     BCC Address (CSV Format)
	 * @param subject Subject Line
	 * @param body    Body Line
	 * @return MessageId
	 * @throws Exception Amazon Exception
	 */
	public String sendPlainTextEmail( String from, String to, String bcc, String subject, String body ) throws Exception {

		// Construct an object to contain the recipient address.
		Destination destination = new Destination().withToAddresses( StringUtils.split( to.trim(), ',' ) );
		if( bcc != null ) {
			destination.withBccAddresses( StringUtils.split( bcc.trim(), ',' ) );
		}

		// Create the subject and body of the message.
		Content sesSubject = new Content().withData( subject );
		Body sesBody = new Body().withText( new Content().withData( body ) );

		// Create a message with the specified subject and body.
		Message message = new Message().withSubject( sesSubject ).withBody( sesBody );

		// Assemble the email.
		SendEmailRequest request = new SendEmailRequest().withSource( from ).withDestination( destination ).withMessage( message );

		try {

			SendEmailResult result = client.sendEmail( request );
			return result.getMessageId();

		} catch( AmazonServiceException ase ) {
			throw new Exception( ase );
		} catch( AmazonClientException ace ) {
			throw new Exception( ace );
		}

	}

	/**
	 * @param from        From Address
	 * @param to          To Address (CSV Format)
	 * @param subject     Subject Line
	 * @param body        Body Line
	 * @param attachments Attachments
	 * @return MessageId
	 * @throws MessagingException Messaging Exception
	 * @throws IOException        IO Exception
	 * @throws Exception    Amazon Exception
	 */
	public String sendPlainTextEmailWithAttachment( String from, String to, String subject, String body, File... attachments ) throws MessagingException, IOException, Exception {
		return sendPlainTextEmailWithAttachment( from, to, null, subject, body, attachments );
	}

	/**
	 * @param from        From Address
	 * @param to          To Address (CSV Format)
	 * @param bcc         BCC Address (CSV Format)
	 * @param subject     Subject Line
	 * @param body        Body Line
	 * @param attachments Attachments
	 * @return MessageId
	 * @throws MessagingException Messaging Exception
	 * @throws IOException        IO Exception
	 * @throws Exception    Amazon Exception
	 */
	public String sendPlainTextEmailWithAttachment( String from, String to, String bcc, String subject, String body,
	                                                File... attachments ) throws MessagingException, IOException, Exception {

		Session session = Session.getDefaultInstance( new Properties() );

		MimeMessage message = new MimeMessage( session );
		message.setSubject( subject, "UTF-8" );
		message.setFrom( new InternetAddress( from ) );
		message.setRecipients( MimeMessage.RecipientType.TO, InternetAddress.parse( to.trim() ) );
		if( bcc != null ) {
			message.setRecipients( MimeMessage.RecipientType.BCC, InternetAddress.parse( bcc.trim() ) );
		}

		// Constructs mail body with attachments
		MimeMultipart mimeMultipart = new MimeMultipart();

		// Body
		MimeBodyPart mimeBody = new MimeBodyPart();
		mimeBody.setContent( body, "text/html" );
		mimeMultipart.addBodyPart( mimeBody );

		// Attachments
		for( File file : attachments ) {

			// Construct attachment
			MimeBodyPart mimeAttachment = new MimeBodyPart();
			mimeAttachment.setFileName( file.getName() );
			DataSource ds = new ByteArrayDataSource( new FileInputStream( file ),
					new MimetypesFileTypeMap().getContentType( file ) );
			mimeAttachment.setDataHandler( new DataHandler( ds ) );

			// Add attachment to body
			mimeMultipart.addBodyPart( mimeAttachment );
		}

		message.setContent( mimeMultipart );

		// Construct raw email
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		message.writeTo( outputStream );
		RawMessage rawMessage = new RawMessage( ByteBuffer.wrap( outputStream.toByteArray() ) );

		// Create a raw email request
		SendRawEmailRequest rawEmailRequest = new SendRawEmailRequest( rawMessage );

		try {

			SendRawEmailResult result = client.sendRawEmail( rawEmailRequest );
			return result.getMessageId();

		} catch( AmazonServiceException ase ) {
			throw new Exception( ase );
		} catch( AmazonClientException ace ) {
			throw new Exception( ace );
		}
	}

	/**
	 * Sends a HTML/TEXT email through SES returning the Message ID
	 *
	 * @param from    From Address
	 * @param to      To Address (CSV Format)
	 * @param subject Subject Line
	 * @param body    Body Line
	 * @return MessageId
	 * @throws Exception Amazon Exception
	 */
	public String sendHTMLTextEmail( String from, String to, String subject, String body ) throws Exception {
		return sendHTMLTextEmail( from, to, null, subject, body );
	}

	/**
	 * Sends a HTML/TEXT email through SES returning the Message ID
	 *
	 * @param from    From Address
	 * @param to      To Address (CSV Format)
	 * @param bcc     BCC Address (CSV Format)
	 * @param subject Subject Line
	 * @param body    Body Line
	 * @return MessageId
	 * @throws Exception Amazon Exception
	 */
	public String sendHTMLTextEmail( String from, String to, String bcc, String subject, String body ) throws Exception {

		// Construct an object to contain the recipient address.
		Destination destination = new Destination().withToAddresses( StringUtils.split( to.trim(), ',' ) );
		if( bcc != null ) {
			destination.withBccAddresses( StringUtils.split( bcc.trim(), ',' ) );
		}

		// Create the subject and body of the message.
		Content sesSubject = new Content().withData( subject );
		Body sesBody = new Body().withHtml( new Content().withData( body ).withCharset( "UTF-8" ) );

		// Create a message with the specified subject and body.
		Message message = new Message().withSubject( sesSubject ).withBody( sesBody );

		// Assemble the email.
		SendEmailRequest request = new SendEmailRequest().withSource( from ).withDestination( destination ).withMessage( message );

		try {

			SendEmailResult result = client.sendEmail( request );
			return result.getMessageId();

		} catch( AmazonServiceException ase ) {
			throw new Exception( ase );
		} catch( AmazonClientException ace ) {
			throw new Exception( ace );
		}

	}

	public void sendCalendarInvite( String from, String to, String bcc, String subject, String body ) throws MessagingException, IOException, Exception {
		sendCalendarInvite( from, to, bcc, subject, body, null );
	}

	public void sendCalendarInvite( String from, String to, String bcc, String subject, String body, String eventDescription ) throws MessagingException, IOException, Exception {
		try {
			Session session = Session.getDefaultInstance( new Properties() );

			// Define message
			MimeMessage message = new MimeMessage( session );
			message.setHeader( "Content-Type", "text/calendar; charset=UTF-8; method=REQUEST" );
			message.setFrom( new InternetAddress( from ) );
			message.setSubject( subject, "UTF-8" );
			message.setRecipients( MimeMessage.RecipientType.TO, InternetAddress.parse( to.trim() ) );
			if( bcc != null ) {
				message.setRecipients( MimeMessage.RecipientType.BCC, InternetAddress.parse( bcc.trim() ) );
			}

			// iCalendar Part
			final MimeBodyPart iCalPart = new MimeBodyPart();
			iCalPart.setDataHandler( new DataHandler( new ByteArrayDataSource( body, "text/calendar" ) ) );
			iCalPart.setHeader( "Content-Type", "text/calendar; charset=UTF-8; method=REQUEST" );


			// Attachment Part
			final MimeBodyPart textAttachmentPart = new MimeBodyPart();
			textAttachmentPart.setContent( body, "text/calendar; charset=UTF-8" );
			textAttachmentPart.addHeader( "Content-Disposition", "attachment; filename=invite.ics" );

			// Fill the message
			final MimeMultipart mixedMultipart = new MimeMultipart( "mixed" );
			mixedMultipart.addBodyPart( iCalPart );
			if( eventDescription != null) {
				// HTML Description Part
				MimeBodyPart htmlDescription = new MimeBodyPart();
				htmlDescription.setContent( eventDescription, "text/html" );
				mixedMultipart.addBodyPart( htmlDescription );
			}
			mixedMultipart.addBodyPart( textAttachmentPart );
			message.setContent( mixedMultipart );

			// Construct raw email
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			message.writeTo( outputStream );
			RawMessage rawMessage = new RawMessage( ByteBuffer.wrap( outputStream.toByteArray() ) );

			// Create a raw email request
			SendRawEmailRequest rawEmailRequest = new SendRawEmailRequest( rawMessage );

			client.sendRawEmail( rawEmailRequest );
		} catch( AmazonServiceException ase ) {
			throw new Exception( ase );
		} catch( AmazonClientException ace ) {
			throw new Exception( ace );
		}
	}
}
