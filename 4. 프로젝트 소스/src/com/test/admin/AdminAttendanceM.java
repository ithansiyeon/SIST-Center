package com.test.admin;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Scanner;

import oracle.jdbc.OracleTypes;

/**
 * 관리자가 교육생들의 출결을 확인하는 메뉴
 * @author 김찬우
 * 
 */
public class AdminAttendanceM {
public void manageAttendanceMenu() {

	while(true) {
		/**
		 * 메뉴 번호에 따라 메소드 출력
		 */
		Scanner sc = new Scanner(System.in);
		System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓출결 관리〓〓〓〓〓〓〓〓〓〓〓");
		System.out.println("\t\t\t1. 학생별 출결 조회");
		System.out.println("\t\t\t2. 날짜별 출결 조회");
		System.out.println("\t\t\t0. 뒤로가기");
		System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
		System.out.print("\t\t\t▷입력:");
		String num = sc.nextLine();
		
		if(num.equals("1")) {
			procPrintAttendanceStudent();
		}
		else if(num.equals("2")) {
			PROCPRINTATTENDANCEDATE();
		}
		else if(num.equals("0")) {
			break;
		}
		else {
			System.out.println("\t\t\t잘못된 번호 입력");
		}
	}
}
/**
 * 학생번호와 과정번호를 입력받고 
 * 조건에 맞는 학생의 출결 내역을 출력 
 */
public void procPrintAttendanceStudent() {
	System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
	System.out.println("\t\t\t학생별 출결 내역 확인");
	System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
	Connection conn = null;
	CallableStatement stat = null;
	DBUtil util = new DBUtil();
	ResultSet rs = null;
	Scanner scan = new Scanner(System.in);
	
	
	try {
		/**
		 * procPrintAttendanceStudent 프로시저를 호출해
		 * 입력받은 학생번호와 과정번호에 맞는 학생의 출결내역 출력
		 */
		conn = util.open("211.63.89.64","project","java1234");
		String sql = "{ call procPrintAttendanceStudent(?,?,?) }";
		stat = conn.prepareCall(sql);
		
		System.out.print("\t\t\t학생번호:");
		int stu = scan.nextInt();
		System.out.print("\t\t\t과정번호:");
		int course = scan.nextInt();
		stat.setInt(1,stu);
		stat.setInt(2,course);
		stat.registerOutParameter(3, OracleTypes.CURSOR);
		
		stat.executeQuery();
		
		rs = (ResultSet)stat.getObject(3);
		System.out.println("\t\t\t―――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――");
		while(rs.next()) {
			System.out.printf("\t\t\t[이름]:%s,[총일수]:%s \n"
					+ "\t\t\t[정상]:%s[지각]:%s\t[조퇴]:%s\t[결석]:%s\t[외출]:%s\t[병가]:%s\t[기타]%s\n",
												rs.getString("name"), //이름
												rs.getString("total"),   //총 일수
												rs.getString("normal"),	 //퇴실시각
												rs.getString("lateness"),
												rs.getString("early"),	
												rs.getString("absence"),
												rs.getString("out"),
												rs.getString("sick"),
												rs.getString("etc")
												);
		}
		System.out.println("\t\t\t―――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――");
		
	} catch (Exception e) {
		System.out.println("Ex07_CallableStatment.m5()");
		e.printStackTrace();
	}
	
}
/**
 * 입력받은 날짜에 기록된 학생목록 출력
 */
private void PROCPRINTATTENDANCEDATE() {
	System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
	System.out.println("\t\t\t\t날짜별 출결 내역 확인");
	System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
	Connection conn = null;
	CallableStatement stat = null;
	DBUtil util = new DBUtil();
	ResultSet rs = null;
	Scanner scan = new Scanner(System.in);
	
	
	
	try {
		conn = util.open("211.63.89.64","project","java1234");
		/**
		 * 날짜(년,월,일)를 입력하고 
		 * PROCPRINTATTENDANCEDATE 프로시저 호출 
		 */
			
		System.out.print("\t\t\t날짜입력(YY):");
		String year = scan.nextLine();
		System.out.print("\t\t\t날짜입력(MM):");
		String month = scan.nextLine();
		System.out.print("\t\t\t날짜입력(DD):");
		String date = scan.nextLine();
		String sql = "{call PROCPRINTATTENDANCEDATE(?,?,?,?)}";
		
		stat = conn.prepareCall(sql);
		
		stat.setString(1, year);
		stat.setString(2, month);
		stat.setString(3, date);
		stat.registerOutParameter(4, OracleTypes.CURSOR);
		
		stat.executeQuery();
		
		rs = (ResultSet)stat.getObject(4);
			System.out.println("\t\t\t[학생명]\t[입실시각]\t\t[퇴실시각]\t\t[출결상황]");
		while(rs.next()) {
			System.out.printf("\t\t\t%s\t%s\t%s\t%s\n", 
					rs.getString(1), //학생명
					rs.getString(2),	 //입실시각
					rs.getString(3),   //퇴실시각
					rs.getString(4));//출결상황
			
		}
		
	} catch (Exception e) {
		System.out.println("Ex07_CallableStatment.m5()");
		e.printStackTrace();
	}
	
}

}
	