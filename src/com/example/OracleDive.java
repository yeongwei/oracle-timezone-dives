package com.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class OracleDive {
	public static void main(String[] args) throws Exception {
		System.out.println("Make sure Client timezone is different from Oracle Server " + java.util.TimeZone.getDefault());
		Connection connection = DriverManager
				.getConnection("jdbc:oracle:thin:ncim/ncim@//tnpminlnx13.persistent.co.in:1521/NCIM");
		PreparedStatement ps = connection
				.prepareStatement("select * from ("
						+ "select TEXT, timestamp '1970-01-01 00:00:00 UTC' +  numToDSInterval(CREATED_TIMESTAMP, 'second') as CREATED_TIMESTAMP "
						+ "from NCIM.DUMMY) WHERE \"CREATED_TIMESTAMP\" > ?  AND \"CREATED_TIMESTAMP\" < CURRENT_TIMESTAMP "
						+ "ORDER BY \"CREATED_TIMESTAMP\" ASC");
		GregorianCalendar gregorianCalendar = new GregorianCalendar(
				TimeZone.getTimeZone("UTC"));
		Timestamp timestamp = new Timestamp(0);
		
		System.out.println("Evaluates how Timestamp(0) is affected by GregorianCalander with UTC");
		gregorianCalendar.setTime(timestamp);
		System.out.println("1->" + gregorianCalendar.getTime());
		System.out.println("2->" + timestamp.getTime());
		
		ps.setTimestamp(1, timestamp, gregorianCalendar);
		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			System.out.println(rs.getString(1) + " " + rs.getTimestamp(2));
		}
	}
}
