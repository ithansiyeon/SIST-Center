package com.test.admin;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import oracle.jdbc.OracleTypes;

/**
 *  관리자가 교육생 관리하는 클래스입니다.
 * @author siyeon
 * 
 */
public class AdminStudent {
	Connection conn = new DBUtil().open("211.63.89.64","project","java1234");
	Scanner scan = new Scanner(System.in);

	public void menu() {

		while (true) {
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t\t\t교육생 관리");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t1. 취업완료 수료생 관리");
			System.out.println("\t\t\t2. 상담 관리");
			System.out.println("\t\t\t3. 성적 우수자 관리");
			System.out.println("\t\t\t4. 보강 수업 관리");
			System.out.println("\t\t\t0. 뒤로가기");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.print("\t\t\t▷입력:");
			String sel = scan.nextLine();
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

			// 취업완료 수료생 관리
			if (sel.equals("1")) {
				manageStudentmenu();
			}

			// 상담 관리
			else if (sel.equals("2")) {
				manangeConsulting();
			}

			// 성적 우수자
			else if (sel.equals("3")) {
				procsSelectOutstandingReward();
			}

			// 보강 수업
			else if (sel.equals("4")) {
				procSelectMakeupClassStudent();
			}

			// 뒤로가기
			else if (sel.equals("0")) {
				break;
			} else {
				System.out.println("\t\t\t번호를 다시 입력해주세요.");
			}

		}

	} // menu()

	private void procSelectMakeupClassStudent() {

		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);

		try {

			conn = util.open("211.63.89.64", "project", "java1234");

			String sql = "{ call procSelectMakeupClassStudent(?,?) }";
			stat = conn.prepareCall(sql);

			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t                보강수업 교육생 리스트 조회 ");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println();

			String snum = "";
			System.out.print("\t\t\t▷ 과목 번호 입력 : "); // 과목 번호입력
			snum = scan.nextLine();
			System.out.println();
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

			stat.setString(1, snum);
			stat.registerOutParameter(2, OracleTypes.CURSOR);
			stat.executeQuery();

			rs = (ResultSet) stat.getObject(2);

			System.out.println();
			System.out.println("[교육생 이름] [주민번호]     [과목]      [필기]  [실기]   [출결]         [보강수업기간]");
			System.out.println("――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――");

			while (rs.next()) {
				System.out.printf("%-8s%15s    %-14s\t  %3s\t%3s\t%-6s       %-20s\n", rs.getString("studentname"),
						rs.getString("ssn"), rs.getString("subjectname"), rs.getString("writtenTestScore"),
						rs.getString("performanceTestScore"), rs.getString("attendanceScore"), rs.getString("period"));
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
	}

	private void procsSelectOutstandingReward() {

		Connection conn = null;
		CallableStatement stat = null; // procedure
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);

		try {

			conn = util.open("211.63.89.64", "project", "java1234");
			String sql = "{ call procsSelectOutstandingReward(?, ?) }";
			stat = conn.prepareCall(sql);

			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t                 성적 우수자 교육생 리스트 ");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println();

			// list of subject, needMore

			String snum = "";
			System.out.print("\t\t\t▷ 과목 번호 입력 : "); // 과목 번호입력
			snum = scan.nextLine();
			System.out.println();

			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

			stat.setString(1, snum);
			stat.registerOutParameter(2, OracleTypes.CURSOR);
			stat.executeQuery();

			rs = (ResultSet) stat.getObject(2);

			System.out.println();
			System.out.println("      [교육생 이름]  [주민번호]    [과목]       [필기]   [실기]   [출결]  [지급여부] [성적번호]");
			System.out.println("      ――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――");

			while (rs.next()) {
				System.out.printf("             %-10s%15s    %-14s          %3s\t%3s\t%3s\t%-10s\t%-5s\n",
						rs.getString("name"), rs.getString("ssn"), rs.getString("subjectname"),
						rs.getString("writtenTestScore"), rs.getString("performanceTestScore"),
						rs.getString("attendanceScore"), rs.getString("status"), rs.getString("scorebysubjectnum"));
			}

			stat.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		// 지급 여부 변경 실행
		procsUpdateOutstandingReward();
	}

	private void procsUpdateOutstandingReward() {

		Connection conn = null;
		CallableStatement stat = null; // procedure
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);

		try {

			conn = util.open("211.63.89.64", "project", "java1234");
			String sql = "{ call procsUpdateOutstandingReward(?) }";
			stat = conn.prepareCall(sql);

			System.out.println();
			System.out.println();
			System.out.println("\t\t\t지급여부를 변경할 성적번호를 입력하세요");

			String snum = "";
			System.out.print("\t\t\t▷ 성적번호 입력 : "); // 과정번호입력
			snum = scan.nextLine();
			System.out.println();

			stat.setString(1, snum);
			stat.executeUpdate();

			System.out.println();
			System.out.println("\t\t\t해당 리스트의 지급여부가 변경되었습니다.");
			System.out.println();
			System.out.println("\t\t\t엔터를 입력하시면 이전 페이지로 돌아갑니다.");
			scan.nextLine();

			stat.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 상담 내역를 관리하기 위한 메소드입니다.
	 */
	private void manangeConsulting() {

		// declare variable
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();

		// Database connection
		conn = util.open("211.63.89.64", "project", "java1234");
		while (true) {

			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓 상담 관리 〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t1. 과정별 조회");
			System.out.println("\t\t\t2. 이름별 조회");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t▷입력:");
			String courseOrName = scan.nextLine();

			// search by course
			if (courseOrName.equals("1")) {
				searchByCourse();
			}

			// search by name
			else if (courseOrName.equals("2")) {
				searchByName();
			}
		}
	}

	/**
	 * 이름별 상담내역 조회 메소드입니다.
	 */
	private void searchByName() {
		System.out.print("\t\t\t이름:");
		String name = scan.nextLine();
		procPrintConsultRqListByN(name);
	}
	
	/**
	 * 과정별 학생의 상담 내역을 출력하는 메소드입니다.
	 */
	private void searchByCourse() {
		// print list of open course
		vwOpencourse();

		// input course num
		System.out.print("\t\t\t과정 번호:");
		String courseNum = scan.nextLine();

		// print consult request list by course num
		procPrintConsultRqList(courseNum);

		// input course num
		System.out.print("\t\t\t상담 번호:");
		String requestNum = scan.nextLine();
		procPrintConsultContent(requestNum);
	}

	/**
	 * 입력받은 이름과 일치하는 교육생의 상담번호
	 * @param name
	 */
	private void procPrintConsultRqListByN( String name) {

		CallableStatement stat = null;
		ResultSet rs = null;
		
		try {

			String sql = "{call procPrintConsultRqListByN(?,?)}";
			stat = conn.prepareCall(sql);
			stat.setString(1, name);
			stat.registerOutParameter(2, OracleTypes.CURSOR);
			stat.executeQuery();

			rs = (ResultSet) stat.getObject(2);

			while (rs.next()) {
				System.out.println("\t\t\t[상담요청번호]\t\t[날짜]\t[이름]");
				System.out.printf("\t\t\t%s\t\t%s\t%s\n", rs.getString("requestnum"),
						rs.getString("requestdate").substring(1, 10), rs.getString("studentname"));
			}
			System.out.print("\t\t\t상담 번호:");
			String requestNum = scan.nextLine();
			procPrintConsultContent(requestNum);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 입력받은 요청번호에 대한 상담 내역을 출력하는 메소드입니다.
	 * @param requestNum 사용자에게 입력받은 상담 요청 번호
	 */
	private void procPrintConsultContent( String requestNum) {

		CallableStatement stat = null;
		Connection conn = new DBUtil().open("211.63.89.64","project","java1234");
		try {

			String sql = "{call procPrintConsultContent(?,?,?)}";
			stat = conn.prepareCall(sql);
			stat.setString(1, requestNum);
			stat.registerOutParameter(2, OracleTypes.DATE);
			stat.registerOutParameter(3, OracleTypes.VARCHAR);
			stat.executeQuery();

			System.out.println("\t\t\t[상담날짜]\t\t[상담내용]");
			System.out.printf("\t\t\t%s\t\t%s\n", stat.getString(2).substring(0, 10), stat.getString(3));
			System.out.println("\t\t\t\n계속 하시려면 엔터를 입력해주세요");
			scan.nextLine();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 과정별 상담요청번호를 출력하는 메소드입니다.
	 * @param coursenum 사용자가 입력한 시행과정 번호
	 */
	private void procPrintConsultRqList( String coursenum) {

		CallableStatement stat = null;
		ResultSet rs = null;
		Connection conn = new DBUtil().open("211.63.89.64","project","java1234");
		try {
			String sql = "{call procPrintConsultRqList(?,?)}";
			stat = conn.prepareCall(sql);
			stat.setString(1, coursenum);
			stat.registerOutParameter(2, OracleTypes.CURSOR);
			stat.executeQuery();

			// get result set
			rs = (ResultSet) stat.getObject(2);
			System.out.println("\t\t\t[상담요청번호]\t[학생번호]\t[학생이름]");

			// print request list
			while (rs.next()) {
				System.out.printf("\t\t\t%s\t\t\t%s\t%s\n", rs.getString("requestnum"), rs.getString("studentnum"),
						rs.getString("studentname"));
			}
			rs.close();
			stat.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 현재 시행중인 과정을 출력하는 메소드입니다.
	 */
	private void vwOpencourse() {
		Statement stat;
		ResultSet rs;
		try {
			Connection conn = new DBUtil().open("211.63.89.64","project","java1234");
			// Database connection
			String sql = "select * from vwOpencourseList";
			stat = conn.createStatement();
			rs = stat.executeQuery(sql);

			System.out.println("\t\t\t[과정번호]\t[과정명]\t\t\t\t\t[기간]");
			while (rs.next()) {

				// print list of opencourse
				System.out.printf("\t\t\t%s\t\t%s\t\t%s\n", rs.getString("opencoursenum"), rs.getString("coursename"),
						rs.getString("startdate").substring(0, 10) + " ~ " + rs.getString("enddate").substring(0, 10));
				;
			}
			rs.close();
			stat.close();
		} catch (Exception e) {
			e.printStackTrace();

		}
	}

	/**
	 * 취업완료 수료생 관리 전체 메뉴입니다.
	 */

	public void manageStudentmenu() {
		boolean loop = true;
		while (loop) {
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t1. 고용보험 여부 결과 수정");
			System.out.println("\t\t\t2. 고용보험 여부 조회");
			System.out.println("\t\t\t3. 취업완료 수료생 관리(추가,조회,수정,삭제)");
			System.out.println("\t\t\t4. 연봉별 검색");
			System.out.println("\t\t\t5. 회사명 검색");
			System.out.println("\t\t\t0. 뒤로가기");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.print("\t\t\t▷입력:");
			String sel = scan.nextLine();
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

			switch (sel) {
			case "1":
				prochiredGraduatesUpdate();
				break;
			case "2":
				prochiredGraduatesSelect();
				break;
			case "3":
				menu2();
				break;
			case "4":
				prochiredgradesSalarySelect();
				break;
			case "5":
				prochiredGradesSelectName();
				break;
			case "0":
				loop = false;
				break;
			default:
				System.out.println("\t\t\t번호를 다시 입력해주세요.");
			}
		}

	} // menu1()

	private void jobActivity() {
		/**
		 * 수료생의 취업활동과 취업지원 내역을 확인하는 메뉴입니다.
		 */
		System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓 수료생의 취업 활동 내역 〓〓〓〓〓〓〓〓〓");
		System.out.println("\t\t\t1. 모든 수료생의 취업활동");
		System.out.println("\t\t\t2. 학생별 취업 활동");
		System.out.println("\t\t\t3. 강의별 수료생의 취업활동");
		System.out.println("\t\t\t4. 취업지원 내역");
		System.out.println("\t\t\t0. 뒤로가기");
		System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
		System.out.print("\t\t\t▷ 입력:");
		Scanner sc = new Scanner(System.in);
		int cho = sc.nextInt();

		if (cho == 1) {
			/**
			 * 모든 취업활동을 출력합니다.
			 */
			Connection conn = null;
			Statement stat = null;
			ResultSet rs = null;
			DBUtil util = new DBUtil();

			try {
				conn = util.open("211.63.89.64", "project", "java1234");

				String sql = String.format("select * from tblJobActivity");

				stat = conn.createStatement();

				stat.executeQuery(sql); // select -> rs

				rs = stat.executeQuery(sql);

				while (rs.next()) {
					System.out.println("\t\t\t[강의번호]\t[활동]\t[학생명]");
					System.out.printf("\t\t\t%s\t%s\t%s\n", rs.getInt(1), rs.getString(2), rs.getString(3));
				}

				stat.close();
				conn.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (cho == 2) {
			Connection conn = null;
			CallableStatement stat = null;
			ResultSet rs = null;
			DBUtil util = new DBUtil();

			try {
				/**
				 * 입력받은 학생번호를 가진 수료생의 활동을 조회합니다.
				 */
				conn = util.open("211.63.89.64", "project", "java1234");

				String sql = "{ call procStudentJobActivity(?,?) }";

				System.out.print("\t\t\t학생 번호 입력:");
				String snum = sc.nextLine();

				stat = conn.prepareCall(sql);
				stat.setString(1, snum);
				stat.registerOutParameter(2, OracleTypes.CURSOR);

				stat.executeQuery(); // select -> rs

				rs = (ResultSet) stat.getObject(2);

				while (rs.next()) {
					System.out.println("\t\t\t[강의번호]\t[활동]\t[학생명]");
					System.out.printf("\t\t\t%s\t%s\t%s\n", rs.getInt("coursenum"), rs.getString("activity"),
							rs.getString("studentname"));
				}

				stat.close();
				conn.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (cho == 3) {
			/**
			 * 강의 별 취업활동을 조회합니다.
			 */
			Connection conn = null;
			CallableStatement stat = null;
			ResultSet rs = null;
			DBUtil util = new DBUtil();

			try {
				conn = util.open("211.63.89.64", "project", "java1234");

				String sql = "{ call procCourseJobActivity(?,?) }";

				System.out.print("\t\t\t강의 번호 입력:");
				String cnum = sc.nextLine();

				stat = conn.prepareCall(sql);
				stat.registerOutParameter(1, OracleTypes.CURSOR);
				stat.setString(2, cnum);

				stat.executeQuery(); // select -> rs
				rs = (ResultSet) stat.getObject(1);
				while (rs.next()) {
					System.out.println("\t\t\t[강의번호]\t[활동]\t\t[학생명]");
					System.out.printf("\t\t\t%s\t%s\t%s\n", rs.getString(1), rs.getString(2), rs.getString(3));
				}

				stat.close();
				conn.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (cho == 4) {
			/**
			 * 센터에서 지원한 내역을 확인하는 메뉴입니다.
			 */
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓 취업 지원 〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t1. 취업 지원 활동 추가");
			System.out.println("\t\t\t2. 학생별 취업 지원 조회");
			System.out.println("\t\t\t3. 취업 지원 내역 추가");
			System.out.println("\t\t\t0. 뒤로가기");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.print("\t\t\t ▷ 입력:");
			cho = sc.nextInt();

			if (cho == 1) {
				procAddSupportList();
			} else if (cho == 2) {
				procprintsupportlist();
			} else if (cho == 3) {
				procinsertSupport();
			} else if (cho == 0) {

			}
		}
	}
	/**
	 * 취업지원 활동을 추가합니다.
	 */
	private void procinsertSupport() {

		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);

		try {
			conn = util.open("211.63.89.64", "project", "java1234");
			String sql = "{ call procinsertSupport(?,?) }";

			conn.setAutoCommit(false);
			stat = conn.prepareCall(sql);
			boolean chk = true;

			System.out.print("\t\t\t 학생 번호 입력:");
			String snum = scan.nextLine();
			System.out.print("\t\t\t취원 지원 번호 입력:");
			String scon = scan.nextLine();

			stat.setInt(1, Integer.parseInt(snum));
			stat.setInt(2, Integer.parseInt(scon));

			int result = stat.executeUpdate();

			if (result == 1) {
				System.out.println("지원 내용 입력 완료!");
				conn.commit();
			} else {
				System.out.println("지원 내용 입력 실패!");
				conn.rollback();
			}
			stat.close();
			conn.close();

		} catch (Exception e) {
			System.out.println("에러~~");
			System.out.println(e);
		}

	}
	/**
	 * 취업지원 활동 목록을 출력합니다.
	 */
	private void procprintsupportlist() {

		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		ResultSet rs = null;
		Scanner scan = new Scanner(System.in);

		try {
			conn = util.open("211.63.89.64", "project", "java1234");
			String sql = "{ call procprintsupportlist(?,?)}";

			conn.setAutoCommit(false);
			stat = conn.prepareCall(sql);

			System.out.print("\t\t\t학생 번호 입력:");
			int snum = scan.nextInt();
			scan.skip("\r\n");
			stat.registerOutParameter(1, OracleTypes.CURSOR);
			stat.setInt(2, snum);

			stat.executeQuery();
			rs = (ResultSet) stat.getObject(1);
			System.out.println("\t\t\t[학생명]\t[학생 번호]\t[취업 지원 내용]");
			while (rs.next()) {
				System.out.printf("\t\t\t%s\t\t\t%s\t%s \n", rs.getString(1), rs.getString(2), rs.getString(3));
			}

			stat.close();
			conn.close();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}

	}
	/**
	 * 취업지원 활동을 추가합니다.
	 */
	private void procAddSupportList() {

		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);

		try {
			conn = util.open("211.63.89.64", "project", "java1234");
			String sql = "{ call procAddSupportList(?) }";

			conn.setAutoCommit(false);
			stat = conn.prepareCall(sql);
			boolean chk = true;

			System.out.print("\t\t\t ▷취업 지원 내용 입력:");
			String scon = scan.nextLine();

			stat.setString(1, scon);

			int result = stat.executeUpdate();

			if (result == 1) {
				System.out.println("\t\t\t지원 내용 입력 완료!");
				conn.commit();
			} else {
				System.out.println("\t\t\t지원 내용 입력 실패!");
				conn.rollback();
			}
			stat.close();
			conn.close();

		} catch (Exception e) {
			System.out.println("에러~~");
			System.out.println(e);
		}
	}

	/**
	 * 수료생의 고용보험 여부를 조회합니다.
	 */
	private void prochiredGraduatesSelect() {

		Connection conn = null;
		ResultSet rs = null;
		Statement stat1 = null;
		DBUtil util = new DBUtil();

		String sql = null;

		int count = 0;
		try {
			conn = util.open("211.63.89.64", "project", "java1234");
			// conn = util.open("localhost", "project", "java1234");
			stat1 = conn.createStatement();
			sql = "select * from vwhiredGraduatesSelect";
			rs = stat1.executeQuery(sql);

			System.out.println("\t\t\t[취업완료 번호]\t\t[수강내역 번호]\t\t\t[연봉]\t\t[상태]\t\t[고용보험 여부]\t\t[회사]");
			while (rs.next()) {
				System.out.printf("\t\t\t%s\t\t\t", rs.getString("num"));
				System.out.printf("%s\t\t\t", rs.getString("courseHistoryNum"));
				System.out.printf("%s\t\t", rs.getString("salary"));
				System.out.printf("%s\t\t", rs.getString("status"));
				System.out.printf("%s\t\t\t", rs.getString("employment insurance"));
				System.out.printf("%s", rs.getString("company"));
				System.out.println();
				count++;
				// 100개씩 출력 하다가 계속 출력 할려면 -1 이 아닌 번호를 입력해야 됨
				if (count % 100 == 0) {
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println("\t\t\t       -1을 누르면 종료");
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.print("\t\t\t▷번호:");
					String k = scan.nextLine();

					if (k.equals("-1")) {
						break;
					} else {
						pause();
					}
				}
			}
			stat1.close();
			rs.close();

			System.out.println("\t\t\t완료");
			pause1();
		} catch (Exception e) {
			System.out.println("\t\t\tAdminStudent.prochiredGraduatesSelect()");
			System.out.println("\t\t\t고용 보험 조회에 실패했습니다.");
			e.printStackTrace();
		}

	}

	/**
	 * 취업완료 수료생 관리 소메뉴(추가,조회,수정,삭제)입니다.
	 */

	public void menu2() {

		while (true) {
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t1. 추가");
			System.out.println("\t\t\t2. 조회");
			System.out.println("\t\t\t3. 수정");
			System.out.println("\t\t\t4. 삭제");
			System.out.println("\t\t\t0. 뒤로가기");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.print("\t\t\t▷입력:");
			String sel = scan.nextLine();

			switch (sel) {
			case "1":
				prochiredGradesManInsert();
				break;
			case "2":
				vwhiredGradesManSelect();
				break;
			case "3":
				prochiredGraduatesUpdate1();
				break;
			case "4":
				prochiredGradesManDelete();
				break;
			case "0":
				break;
			default:
				System.out.println("\t\t\t번호를 다시 입력해주세요.");
			}
			break;
		}
	} // menu2()

	/**
	 * 관리자는 수료생의 고용보험 여부를 조회 후 그 결과를 입력합니다.
	 */

	public void prochiredGraduatesUpdate() {
		Connection conn = null;
		ResultSet rs = null;
		CallableStatement stat = null;
		Statement stat1 = null;
		DBUtil util = new DBUtil();

		String sql = null;
		// *** 관리자는 수료생의 고용보험 여부를 조회 후 그 결과를 입력 ***
		try {
			conn = util.open("211.63.89.64","project","java1234");
			//conn = util.open("localhost", "project", "java1234");
			stat1 = conn.createStatement();
			sql = "select * from vwhiredGraduatesSelect";
			rs = stat1.executeQuery(sql);
			int count = 0;
			System.out.println("\t\t\t[취업완료 번호]\t\t[수강내역 번호]\t\t\t[연봉]\t\t[상태]\t\t[고용보험 여부]\t\t[회사]");
			while (rs.next()) {
				System.out.printf("\t\t\t%s\t\t\t", rs.getString("num"));
				System.out.printf("%s\t\t\t", rs.getString("courseHistoryNum"));
				System.out.printf("%s\t\t", rs.getString("salary"));
				System.out.printf("%s\t\t", rs.getString("status"));
				System.out.printf("%s\t\t\t", rs.getString("employment insurance"));
				System.out.printf("%s", rs.getString("company"));
				System.out.println();

				count++;
				if (count % 100 == 0) {
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println("\t\t\t       -1을 누르면 종료");
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.print("\t\t\t▷번호:");
					String k = scan.nextLine();

					if (k.equals("-1")) {
						break;
					} else {
						pause();
					}
				}

			}
			stat1.close();
			rs.close();
			// 수정할 취직 번호를 입력 받아서 취직 상태를 퇴사 또는 재직중으로 수정을 함
			System.out.print("\t\t\t▷수정할 취직 번호(hiredgraduates num):");
			String num = scan.nextLine();
			System.out.print("\t\t\t▷상태(퇴사 or 재직중):");
			String status = scan.nextLine();
			sql = "{call prochiredGraduatesUpdate(?,?)}";
			stat = conn.prepareCall(sql);
			stat.setString(1, num);
			stat.setString(2, status);
			stat.executeUpdate();

			stat.close();
			conn.close();
			System.out.println("\t\t\t완료");
		} catch (Exception e) {
			System.out.println("\t\t\tAdminStudent.prochiredGraduatesUpdate()");
			System.out.println("\t\t\t고용 보험 조회 후 결과 수정에 실패했습니다.");
			e.printStackTrace();
		}

	}

	/**
	 * 취업완료 수료생 관리 중 수정 기능입니다.
	 */
	public void prochiredGraduatesUpdate1() {

		Connection conn = null;
		ResultSet rs = null;
		CallableStatement stat = null;

		DBUtil util = new DBUtil();

		String sql = null;

		String pcourseHistoryNum = null;
		String pcompany = null;
		String psalary = null;
		String pstatus = null;
		// *** 수정 기능 ***
		vwhiredGradesManSelect();
		boolean loop = true;
		while(loop) {
		try {
			//conn = util.open("211.63.89.64","project","java1234");
			conn = util.open("211.63.89.64", "project", "java1234");
			
			sql = "{call prochiredGraduatesUpdateSelect(?,?)}";
			stat = conn.prepareCall(sql);
			System.out.print("\t\t\t▷수정할 번호(hiredGraduatesNum):");
			String num = scan.nextLine();
			stat.setString(1, num);
			stat.registerOutParameter(2, OracleTypes.CURSOR);
			stat.executeQuery();
			Statement stat1 = null;
			rs = (ResultSet) stat.getObject(2);

			// 원본 데이터
			if (rs.next()) {
				pcourseHistoryNum = rs.getString("courseHistoryNum");
				pcompany = rs.getString("company");
				psalary = rs.getString("salary");
				pstatus = rs.getString("status");
			}
			rs.close();

			sql = "{call prochiredGraduatesUpdate1(?,?,?,?,?)}";
			stat = conn.prepareCall(sql);
			// 수정하고 싶은 번호를 입력 받음
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t1. 수강내역 번호");
			System.out.println("\t\t\t2. 회사명");
			System.out.println("\t\t\t3. 연봉");
			System.out.println("\t\t\t4. 재직 상태");
			System.out.println("\t\t\t0. 뒤로가기");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.print("\t\t\t▷입력:");
			String a = scan.nextLine();
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			switch (a) {

			// 해당하는 번호에 따라서 수정을 함
			case "1":
				stat1 = conn.createStatement();
				System.out.println("\t\t\t*** 기초 과정명 ***");
				sql = "select * from vwallCourse";
				rs = stat1.executeQuery(sql);
				while (rs.next()) {
					System.out.printf("\t\t\t%s\t", rs.getString("allCourseNum"));
					System.out.printf("%s\t", rs.getString("courseName"));
					System.out.println();
				}
				System.out.println();
				System.out.print("\t\t\t▷수강내역 번호(courseHistoryNum):");
				String courseHistoryNum = scan.nextLine();
				stat.setString(1, num);
				stat.setString(2, courseHistoryNum);
				stat.setString(3, pcompany);
				stat.setString(4, psalary);
				stat.setString(5, pstatus);

				break;
			case "2":
				System.out.print("\t\t\t▷회사이름:");
				String company = scan.nextLine();
				stat.setString(1, num);
				stat.setString(2, pcourseHistoryNum);
				stat.setString(3, company);
				stat.setString(4, psalary);
				stat.setString(5, pstatus);

				break;
			case "3":
				System.out.print("\t\t\t▷연봉:");
				String salary = scan.nextLine();
				stat.setString(1, num);
				stat.setString(2, pcourseHistoryNum);
				stat.setString(3, pcompany);
				stat.setString(4, salary);
				stat.setString(5, pstatus);
				break;
			case "4":
				System.out.print("\t\t\t▷취업 상태:");
				String status = scan.nextLine();
				stat.setString(1, num);
				stat.setString(2, pcourseHistoryNum);
				stat.setString(3, pcompany);
				stat.setString(4, psalary);
				stat.setString(5, status);
				break;
			case "0":
				loop = false;
				break;
			default: 
				System.out.println("\t\t\t번호를 다시 입력해주세요.");
			}
			stat.executeUpdate();

			vwhiredGradesManSelect();
			stat.close();
			conn.close();

			System.out.println("\t\t\t완료");
		} catch (Exception e) {
			System.out.println("\t\t\tAdminStudent.prochiredGraduatesUpdate1()");
			System.out.println("\t\t\t취업완료 수료생 관리 수정 기능에 실패했습니다.");
			e.printStackTrace();
			}
		}

	}

	/**
	 * 취업 완료 수료생 출력하는 기능입니다.
	 */

	public void prochiredgradesSalarySelect() {
		Connection conn = null;
		ResultSet rs = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		String sql = null;

		// *** 관리자 취업 완료 수료생 리스트 조회 시 연봉별 검색 ***
		try {

			conn = util.open("211.63.89.64", "project", "java1234");
			// conn = util.open("localhost", "project", "java1234");
			sql = "{call prochiredgradesSalarySelect(?,?,?)}";

			stat = conn.prepareCall(sql);
			System.out.print("\t\t\t▷시작 연봉:");
			String start = scan.nextLine();
			System.out.print("\t\t\t▷끝 연봉:");
			String end = scan.nextLine();

			stat.setString(1, start);
			stat.setString(2, end);
			stat.registerOutParameter(3, OracleTypes.CURSOR);
			stat.executeQuery();
			rs = (ResultSet) stat.getObject(3);

			// 시작 연봉에서 끝 연봉에 해당하는 범위 내의 취업 완료 수료생 리스트를 출력함
			System.out.println("\t\t\t[수강내역번호]\t[연봉]\t[상태]\t[회사]");
			while (rs.next()) {
				System.out.printf("\t\t\t%s\t", rs.getString("coursehistorynum"));
				System.out.printf("%s\t", rs.getString("salary"));
				System.out.printf("%s\t", rs.getString("status"));
				System.out.printf("%s", rs.getString("company"));
				System.out.println();
			}
			rs.close();
			stat.close();
			conn.close();

		} catch (Exception e) {
			System.out.println("\t\t\tAdminStudent.prochiredgradesSalarySelect()");
			System.out.println("\t\t\t취업완료 수료생 출력에 실패했습니다.");
			e.printStackTrace();
		}

	}

	/**
	 * 취업완료 수료생을 회사 이름으로 검색하는 기능입니다.
	 */
	public void prochiredGradesSelectName() {
		Connection conn = null;
		ResultSet rs = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		String sql = null;

		// *** 관리자 회사명으로 조회 ***
		try {

			conn = util.open("211.63.89.64", "project", "java1234");
			// conn = util.open("localhost", "project", "java1234");
			sql = "{call prochiredGradesSelectName(?,?)}";
			stat = conn.prepareCall(sql);
			stat.registerOutParameter(2, OracleTypes.CURSOR);

			System.out.print("\t\t\t▷회사 이름:");
			String company = scan.nextLine();
			stat.setString(1, company);
			stat.executeQuery();
			rs = (ResultSet) stat.getObject(2);
			System.out.println("\t\t\t[취업완료 번호]\t[수강내역 번호]\t\t\t[연봉]\t\t[상태]\t[회사]");
			// 회사 이름을 포함한 취업 완료 수료생 리스트를 출력함
			while (rs.next()) {
				System.out.printf("\t\t\t%s\t\t\t", rs.getString("num"));
				System.out.printf("%s\t\t\t", rs.getString("courseHistoryNum"));
				System.out.printf("%s\t", rs.getString("salary"));
				System.out.printf("%s\t", rs.getString("status"));
				System.out.printf("%s", rs.getString("company"));
				System.out.println();
			}
			conn.close();
			rs.close();
			stat.close();

		} catch (Exception e) {
			System.out.println("\t\t\tAdminStudent.prochiredGradesSelectName()");
			System.out.println("\t\t\t취업완료 수료생을 회사 이름으로 검색에 실패했습니다.");
			e.printStackTrace();
		}

	}

	/**
	 * 취업 완료 수료생을 삭제하는 기능입니다.
	 */
	public void prochiredGradesManDelete() {
		Connection conn = null;
		ResultSet rs = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		String sql = null;

		vwhiredGradesManSelect();
		// *** 관리자가 취업 완료 수료생 관리 삭제 ***
		try {
			conn = util.open("211.63.89.64", "project", "java1234");
			// conn = util.open("localhost", "project", "java1234");
			sql = "{call prochiredGradesManDelete(?)}";
			System.out.print("\t\t\t▷삭제할 번호(hiredGraduatesNum): ");
			String num = scan.nextLine();

			stat = conn.prepareCall(sql);
			stat.setString(1, num);
			stat.executeUpdate();

			System.out.println("\t\t\t완료");

			conn.close();
			stat.close();

		} catch (Exception e) {
			System.out.println("\t\t\tAdminStudent.prochiredGradesManDelete()");
			System.out.println("\t\t\t취업 완료 수료생 삭제에 실패했습니다.");
			e.printStackTrace();
		}

	}

	/**
	 * 취업 완료 수료생을 추가하는 기능입니다.
	 */
	public void prochiredGradesManInsert() {
		Connection conn = null;
		ResultSet rs = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		String sql = null;
		Statement stat1 = null;

		// *** 관리자가 취업 완료 수료생 관리 추가 ***
		try {
			conn = util.open("211.63.89.64", "project", "java1234");
			// conn = util.open("localhost", "project", "java1234");

			// 기초과정명에 해당하는 allCourseNum과 courseName을 출력함
			System.out.println("\t\t\t*** 기초 과정명 ***");
			stat1 = conn.createStatement();
			sql = "select * from vwallCourse1";
			rs = stat1.executeQuery(sql);
			while (rs.next()) {
				System.out.printf("\t\t\t%s\t", rs.getString("allCourseNum"));
				System.out.printf("%s\t", rs.getString("courseName"));
				System.out.println();
			}
			System.out.println();
			stat1.close();
			rs.close();

			// 강좌 번호, 회사이름, 연봉, 고용 상태를 입력받아서 추가를 함
			System.out.print("\t\t\t▷강좌 번호(pcourseHistoryNum):");
			String num = scan.nextLine();
			System.out.print("\t\t\t▷회사이름:");
			String company = scan.nextLine();
			System.out.print("\t\t\t▷연봉:");
			String salary = scan.nextLine();
			System.out.print("\t\t\t▷고용 상태(재직중,퇴사):");
			String status = scan.nextLine();

			sql = "{call prochiredGradesManInsert(?,?,?,?)}";
			stat = conn.prepareCall(sql);

			stat.setString(1, num);
			stat.setString(2, company);
			stat.setString(3, salary);
			stat.setString(4, status);

			System.out.println("\t\t\t완료");

			stat.executeUpdate();

			stat.close();
			conn.close();

		} catch (Exception e) {
			System.out.println("\t\t\tAdminStudent.prochiredGradesManInsert()");
			System.out.println("\t\t\t취업완료 수료생 추가에 실패했습니다.");
			e.printStackTrace();
		}

	}

	/**
	 * 취업 완료 수료생을 출력하는 기능입니다.
	 */
	public void vwhiredGradesManSelect() {
		Connection conn = null;
		ResultSet rs = null;
		Statement stat = null;
		DBUtil util = new DBUtil();
		String sql = null;

		// *** 관리자가 취업 완료 수료생 관리 출력 ***
		try {
			//conn = util.open("211.63.89.64", "project", "java1234");
			conn = util.open("211.63.89.64", "project", "java1234");
			sql = "select  * from vwhiredGradesManSelect";

			stat = conn.createStatement();
			rs = stat.executeQuery(sql);
			int count = 0;
			// 취업 완료 수료생 리스트를 출력함

			System.out.println("\t\t\t[취업완료 번호]\t\t[수강내역 번호]\t\t[연봉]\t\t\t[상태]\t\t[회사]");
			while (rs.next()) {
				System.out.printf("\t\t\t%s\t\t\t", rs.getString("num"));
				System.out.printf("%s\t\t\t", rs.getString("courseHistoryNum"));
				System.out.printf("%s\t\t", rs.getString("salary"));
				System.out.printf("%s\t\t", rs.getString("status"));
				System.out.printf("%s", rs.getString("company"));
				System.out.println();
				count++;
				if (count % 100 == 0) {
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println("\t\t\t       -1을 누르면 종료");
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.print("\t\t\t▷번호:");
					String k = scan.nextLine();

					if (k.equals("-1")) {
						break;
					} else {
						pause();
					}
				}
			}
			rs.close();
			conn.close();
			stat.close();
			pause1();
		} catch (Exception e) {
			System.out.println("\t\t\tAdminStudent.vwhiredGradesManSelect()");
			System.out.println("\t\t\t취업 완료 수료생 출력에 실패했습니다.");
			e.printStackTrace();
		}

	}

	/**
	 * 100개를 출력 후 계속 출력 하려면 엔터를 눌러야지 계속 할 수 있는 기능입니다.
	 */
	public void pause() {
		System.out.println("\t\t\t100개를 계속 출력하실려면 엔터를 누르세요...");
		scan.nextLine();
	} // pause()
	/**
	 * 계속할려면 엔터를 눌러야 하는 기능입니다.
	 */
	public void pause1() {
		System.out.println("\t\t\t계속 하실려면 엔터를 누르세요...");
		scan.nextLine();
	} //pause1()

}