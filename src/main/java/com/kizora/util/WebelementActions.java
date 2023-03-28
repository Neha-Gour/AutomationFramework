package com.kizora.util;

import java.io.File;

import org.openqa.selenium.JavascriptExecutor;
import com.kizora.base.TestBase;

public class WebelementActions extends TestBase{
	
	//Scroll at the bottom of the page
	public static void scrollbottom() {
		( ( JavascriptExecutor ) driver ).executeScript( "window.scrollTo(0, document.body.scrollHeight)" );

	}
	
	//Function to get downloaded path		
		public static String getDownloadsPath() {

			String downloadPath = System.getProperty( "user.home" );
			File file = new File( downloadPath + "/Downloads/" );
			return file.getAbsolutePath();
		}

		public static boolean isFileDownloaded( String downloadPath, String fileName ) {
			File dir = new File( downloadPath );
			File[] dir_contents = dir.listFiles();

			if( dir_contents != null ) {
				for( File dir_content : dir_contents ) {
//					System.out.println(dir_content.getName());
					if( dir_content.getName().contains( fileName ) )
						return true;
				}
			}

			return false;
		}

}
