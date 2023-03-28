package com.kizora.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.kizora.base.TestBase;

public class DatabaseUtil extends TestBase{
	// Connection object
		static Connection con = null;
		// Statement object
		private static Statement stmt;

		@BeforeMethod
		public void setUp() throws SQLException {

			String dbUrl = prop.getProperty( "dbUrl" );
			String dbname = prop.getProperty( "databaseName" );
			String username = prop.getProperty( "dbUser" );
			String pwd = prop.getProperty( "dbPassword" );

//	       Create a variable for the connection string.
			String connectionUrl = dbUrl + ";databaseName= " + dbname + " ;user= " + username + ";password= " + pwd + ";integratedSecurity=false;trustServerCertificate=true;";
//			String connectionUrl = "jdbc:sqlserver://KI-LT-73\\SQLEXPRESS:1433;databaseName= RCS;user=sa;password=Kizora@123;integratedSecurity=false;trustServerCertificate=true;";

			Connection con = DriverManager.getConnection( connectionUrl );
//			
			stmt = con.createStatement();
			System.out.println( "Connected to DB" );

		}

		@Test
		public void test() throws SQLException {

			String query =
					"select * from roster";
			// Get the contents of Roster table from DB
			ResultSet res = stmt.executeQuery( query );
			System.out.println( res.next() );
			System.out.println( res );
//				System.out.println(res.getRow());
			// Print the result untill all the records are printed
			// res.next() returns true if there is any next record else returns false
			while( res.next() ) {
				System.out.print( res.getString( 1 ) );
				System.out.print( "\t" + res.getString( 2 ) );
				System.out.print( "\t" + res.getString( 3 ) );
				System.out.println( "\t" + res.getString( 4 ) );
			}


		}

		@AfterMethod
		public void tearDown() throws Exception {
			// Close DB connection
			if( con != null ) {
				con.close();
			}
		}

}
