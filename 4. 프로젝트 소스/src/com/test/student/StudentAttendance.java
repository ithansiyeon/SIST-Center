package com.test.student;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

import com.test.admin.DBUtil;

import oracle.jdbc.OracleTypes;

/**
 * 교육생 - 출결 관리 관련 메소드를 멤버로 갖는 클래스입니다.
 * @author Yerim Choi
 *
 */
public class StudentAttendance {
	
	/**
	 * 출결 관리 메뉴를 출력하는 메소드입니다.
	 * @param studentUser 교육생 객체
	 */
	public void printAttendanceMenu(StudentUser studentUser) {

		Scanner scan = new Scanner(System.in);
		String sel = "";

		while (true) {
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t1. 입실 기록");
			System.out.println("\t\t\t2. 퇴실 기록");
			System.out.println("\t\t\t3. 근태 신청");
			System.out.println("\t\t\t4. 출결현황 조회");
			System.out.println("\t\t\t0. 뒤로가기");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

			// 사용자에게 번호 입력받음
			System.out.print("\t\t\t▷입력: ");
			sel = scan.nextLine();
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println();

			// 입실 기록
			if (sel.equals("1")) {

				procAddAttendanceEntertime(studentUser);

			}
			// 퇴실 기록
			if (sel.equals("2")) {

				procAddAttendanceOuttime(studentUser);

			}
			// 근태 신청
			if (sel.equals("3")) {

				procSelectAttendanceStatus(studentUser);

			}
			// 출결현황 조회
			if (sel.equals("4")) {

				procStudentAttendanceByDate(studentUser);

			}
			// 뒤로 가기
			else if (sel.equals("0")) {
				break;
			}
			// 예외
			else {
				System.out.println("\t\t\t번호를 다시 입력해주세요.");
			}
		}

	}// main
	
	/**
	 * 현재 시각으로 교육생의 입실 기록이 등록됩니다.
	 * @param studentUser 교육생 번호
	 */
	// 1. 입실기록
	public void procAddAttendanceEntertime(StudentUser studentUser) {

		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);

		try {

			conn = util.open("211.63.89.64", "project", "java1234");
			String sql = "{ call procAddAttendanceEntertime(?) }";
			stat = conn.prepareCall(sql);

			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t                               교육생 입실 기록 ");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println();

			// 현재 시각 표시
			SimpleDateFormat format = new SimpleDateFormat("\t\t\t          yyyy년 MM월dd일 HH시mm분ss초");
			Calendar time = Calendar.getInstance();
			String format_time = format.format(time.getTime());
			System.out.println(format_time);
			System.out.println();
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

			// 로그인 시 학생 번호
			stat.setInt(1, studentUser.getNum());
			stat.executeQuery();

			System.out.println("\t\t\t현재 시각으로 입실 기록이 완료되었습니다.");

			stat.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		// 뒤로가기 전 pause
		System.out.println();
		System.out.println("\t\t\t엔터를 입력하시면 이전 페이지로 돌아갑니다.");
		scan.nextLine();

	}// procAddAttendanceEntertime();
	
	
	/**
	 * 현재 시각으로 교육생의 퇴실 기록이 등록됩니다.
	 * @param studentUser 교육생 번호
	 */
	// 2.퇴실 기록
	public void procAddAttendanceOuttime(StudentUser studentUser) {

		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);

		try {

			conn = util.open("211.63.89.64", "project", "java1234");
			String sql = "{ call procAddAttendanceOuttime(?) }";
			stat = conn.prepareCall(sql);

			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t                               교육생 퇴실 기록 ");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println();

			// 현재 시각 표시
			SimpleDateFormat format = new SimpleDateFormat("\t\t\t          yyyy년 MM월dd일 HH시mm분ss초");
			Calendar time = Calendar.getInstance();
			String format_time = format.format(time.getTime());
			System.out.println(format_time);
			System.out.println();
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

			// 로그인 시 학생 번호
			stat.setInt(1, studentUser.getNum());
			stat.executeQuery();

			System.out.println("\t\t\t현재 시각으로 퇴실 기록이 완료되었습니다.");

			stat.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		// 뒤로가기 전 pause
		System.out.println();
		System.out.println("\t\t\t엔터를 입력하시면 이전 페이지로 돌아갑니다.");
		scan.nextLine();

	}// procAddAttendanceOuttime();

	/**
	 * 출력된 근태리스트의 번호와 변경할 근태 상태를 입력 받아 해당 리스트의 근태 상태를 업데이트합니다. 
	 */
	// 3-1. 근태 상태 업데이트
	public void procUpdateAttendanceStatus() {

		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);

		try {

			conn = util.open("211.63.89.64", "project", "java1234");
			String sql = "{ call procUpdateAttendanceStatus(?,?)";
			stat = conn.prepareCall(sql);

			System.out.println();
			System.out.println();
			System.out.println("\t\t\t근태 상태를 변경할 번호를 입력하세요");
			;

			String snum = "";
			System.out.print("\t\t\t▷ 번호 : ");
			snum = scan.nextLine();

			String status = "";
			System.out.print("\t\t\t▷ 근태 상태 입력 : ");
			status = scan.nextLine();

			System.out.println();
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

			stat.setString(1, snum);
			stat.setString(2, status);

			stat.executeQuery();

			System.out.println();
			System.out.println("\t\t\t해당 리스트의 근태 상태 변경이 완료되었습니다.");

			stat.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		// 뒤로가기 전 pause
		System.out.println();
		System.out.println("\t\t\t엔터를 입력하시면 이전 페이지로 돌아갑니다.");
		scan.nextLine();

	}// procUpdateAttendanceStatus()
	
	
	/**
	 * 근태 신청할 날짜를 입력 받아 해당 교육생의 출결 리스트를 출력합니다.
	 * @param studentUser 교육생 번호
	 */
	// 3.근태 신청
	public void procSelectAttendanceStatus(StudentUser studentUser) {

		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);

		try {

			conn = util.open("211.63.89.64", "project", "java1234");
			String sql = "{ call procSelectAttendanceStatus(?,?,?,?,?)";
			stat = conn.prepareCall(sql);

			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t\t                    근태 신청 ");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println();

			String sentertimey = "";
			System.out.print("\t\t\t▷ 신청 날짜 년도 : "); // 조회 날짜 기간 시작일
			sentertimey = scan.nextLine();

			String sentertimem = "";
			System.out.print("\t\t\t▷ 신청 날짜 월 : "); // 조회 날짜 기간 시작일
			sentertimem = scan.nextLine();

			String sentertimed = "";
			System.out.print("\t\t\t▷ 신청 날짜 일 : "); // 조회 날짜 기간 시작일
			sentertimed = scan.nextLine();

			System.out.println();
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

			stat.setInt(1, studentUser.getNum());

			stat.setString(2, sentertimey);
			stat.setString(3, sentertimem);
			stat.setString(4, sentertimed);

			stat.registerOutParameter(5, OracleTypes.CURSOR);
			stat.executeQuery();

			rs = (ResultSet) stat.getObject(5);

			System.out.println();
			System.out.println("\t   [이름]\t    [입실시각]              [퇴실시각]       [현황]  [번호]");
			System.out.println("\t   ――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――");

			while (rs.next()) {
				System.out.printf("                       %s         %s    %s   %s       %s\n", rs.getString("name"),
						rs.getString("entertime"), rs.getString("outtime"), rs.getString("status"),
						rs.getString("anum"));
			}

			stat.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		// 근태 상태 변경 메소드 호출
		procUpdateAttendanceStatus();

	}// procSelectAttendanceStatus();

	/**
	 * 조회하고 싶은 날짜의 시작일, 종료일을 입력 받아 해당 기간의 출결 기록을 조회합니다.
	 * @param studentUser 교육생 번호
	 */
	// 4.근태 기록 조회
	public void procStudentAttendanceByDate(StudentUser studentUser) {

		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);

		try {

			conn = util.open("211.63.89.64", "project", "java1234");
			String sql = "{ call procStudentAttendanceByDate(?,?,?,?,?,?,?,?)";
			stat = conn.prepareCall(sql);

			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t\t                근태 기록 조회 ");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println();

			String sentertimey = "";
			System.out.print("\t\t\t▷ 조회 날짜 기간 시작년도 : "); // 조회 날짜 기간 시작일
			sentertimey = scan.nextLine();

			String sentertimem = "";
			System.out.print("\t\t\t▷ 조회 날짜 기간 시작월 : "); // 조회 날짜 기간 시작일
			sentertimem = scan.nextLine();

			String sentertimed = "";
			System.out.print("\t\t\t▷ 조회 날짜 기간 시작일 : "); // 조회 날짜 기간 시작일
			sentertimed = scan.nextLine();

			String souttimey = "";
			System.out.print("\t\t\t▷ 조회 날짜 기간 종료년도 : "); // 조회 날짜 기간 시작일
			souttimey = scan.nextLine();

			String souttimem = "";
			System.out.print("\t\t\t▷ 조회 날짜 기간 종료월 : "); // 조회 날짜 기간 시작일
			souttimem = scan.nextLine();

			String souttimed = "";
			System.out.print("\t\t\t▷ 조회 날짜 기간 종료일 : "); // 조회 날짜 기간 종료일
			souttimed = scan.nextLine();
			System.out.println();
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

			stat.setInt(1, studentUser.getNum());

			stat.setString(2, sentertimey);
			stat.setString(3, sentertimem);
			stat.setString(4, sentertimed);

			stat.setString(5, souttimey);
			stat.setString(6, souttimem);
			stat.setString(7, souttimed);

			stat.registerOutParameter(8, OracleTypes.CURSOR);
			stat.executeQuery();

			rs = (ResultSet) stat.getObject(8);

			System.out.println();
			System.out.println("\t     [이름]\t   [입실시각]\t           [퇴실시각]       [현황]");
			System.out.println("\t     ――――――――――――――――――――――――――――――――――――――――――――――――――――――――――");

			while (rs.next()) {
				System.out.printf("                          %s         %s    %s   %s\n", rs.getString("name"),
						rs.getString("entertime"),
						rs.getString("outtime")==null ? "\t       --\t   " : rs.getString("outtime"), rs.getString("status"));
			}

			stat.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		// 뒤로가기 전 pause
		System.out.println();
		System.out.println("\t\t\t엔터를 입력하시면 이전 페이지로 돌아갑니다.");
		scan.nextLine();

	}// procStudentAttendanceByDate();
}
