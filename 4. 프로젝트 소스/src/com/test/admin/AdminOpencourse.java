package com.test.admin;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Scanner;

import oracle.jdbc.OracleTypes;

/**
 * 
 * 관리자가 개설 과정 관리하는 클래스입니다.
 * @author siyeon 
 */

public class AdminOpencourse {

	Scanner scan = new Scanner(System.in);
	
	/*
	public static void main(String[] args) {
		AdminOpencourse m = new AdminOpencourse();
		m.menu();
	}
	*/
	/**
	 * 개설 과정 관리 전체 메뉴입니다.
	 */
	public void menu() {

		Boolean loop = true;
		while (loop) {
			System.out.println();
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓  M E N U 〓〓〓〓〓〓〓");
			System.out.println("\t\t\t                          개설 과정 관리");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t1. 신규 개설 과정 등록");
			System.out.println("\t\t\t2. 조회");
			System.out.println("\t\t\t3. 수료 날짜 지정");
			System.out.println("\t\t\t4. 수정");
			System.out.println("\t\t\t5. 삭제");
			System.out.println("\t\t\t0. 뒤로가기");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.print("\t\t\t▷번호:");
			String num = scan.nextLine();

			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

			// 메뉴 선택 번호를 입력받음
			switch (num) {
			case "1":
				procopenCourseInsert();
				break;
			case "2":
				menu1();
				break;
			case "3":
				vwopenCourseSelectEndDate();
				break;
			case "4":
				procopenCourseUpdate();
				break;
			case "5":
				procopenCourseDelete();
				break;
			case "0":
				loop = false;
				break;
			default:
				System.out.println("\t\t\t번호를 다시 입력해주세요.");
			}
		}
	} // menu()

	/**
	 * 신규 조회 메뉴입니다.
	 */

	public void menu1() {

		while (true) {
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t1. 개설 과정 정보 출력1");
			System.out.println("\t\t\t2. 특정 개설 과정 선택 출력");
			System.out.println("\t\t\t3. 개설 과정 조희");
			System.out.println("\t\t\t0. 뒤로가기");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.print("\t\t\t▷입력:");
			String sel = scan.nextLine();

			switch (sel) {
			case "1":
				vwopenCourseInfo();
				break;
			case "2":
				vwopenCourseSubject();
				break;
			case "3":
				vwopenCourse();
				break;
			case "0":
				break;
			default:
				System.out.println("\t\t\t번호를 다시 입력해주세요.");
			}
			break;
		}
	} // menu3()

	/**
	 * 개설 과정 정보 출력시 개설 과정명, 개설 과정기간, 강의실명, 개설 과목 등록 여부, 교육생 등록 인원을 출력합니다.
	 */
	public void vwopenCourseInfo() {

		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();

		System.out.println(
				"[개설과정번호]\t[개설과정기간]\t\t\t[강의실이름]\t[개설 과목 등록 여부]\t[교육생 등록 인원]\t\t\t[과정 이름]");
		try {
			conn = util.open("211.63.89.64", "project", "java1234");
			//conn = util.open("localhost", "project", "java1234");
			stat = conn.createStatement(); // 쿼리를 날릴 수 있는 개체
			String sql = "select * from vwopenCourseInfo";

			rs = stat.executeQuery(sql);
			int count = 0;
			while (rs.next()) {
				System.out.printf("%s\t\t", rs.getString("openCourseNum"));
				System.out.printf("%s\t\t", rs.getString("opening course period"));
				System.out.printf("%s\t\t", rs.getString("classroom name"));
				System.out.printf("%s\t\t", rs.getString("register opening course"));
				System.out.printf("%s\t\t\t\t", rs.getString("trainee registration personnel"));
				System.out.printf("%s", rs.getString("opening course name"));
				System.out.println();

				count++;

				if (count % 100 == 0) {
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println("\t\t\t           -1을 누르면 종료");
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.print("\t\t\t번호:");
					String k = scan.nextLine();
					if (k.equals("-1")) {
						break;
					} else {
						pause();
					}

				}
			}
			conn.close();
			stat.close();
			rs.close();
			System.out.println("\t\t\t완료");
			pause1();
		} catch (Exception e) {
			System.out.println("\t\t\tAdminOpenCourse.vwopenCourseInfo()");
			System.out.println("\t\t\t개설 과정명, 개설과정 기간, 강의실명, 개설 과목 등록 여부, 교육생 등록 인원 출력에 실패했습니다.");
			e.printStackTrace();
		}

	} // vwopenCourseInfo()

	/**
	 * 계속 할려면 엔터를 눌러야 합니다.
	 */

	/**
	 * 100개를 출력 후 계속 출력 하려면 엔터를 눌러야지 계속 할 수 있는 기능입니다.
	 */
	public void pause() {
		System.out.println("\t\t\t100개를 계속 출력하실려면 엔터를 누르세요...");
		scan.nextLine();
	}
	/**
	 *  계속할려면 엔터를 눌러야 하는 기능입니다.
	 */
	public void pause1() {
		System.out.println("\t\t\t계속 하실려면 엔터를 누르세요...");
		scan.nextLine();
	}
	/**
	 * 과정 번호를 입력하면 개설 과목 정보(과목명, 과목기간 및 등록된 교육생 정보)를 보여줍니다.
	 */
	public void vwopenCourseSubject() {

		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		String sql = null;
		// 특정 개설 과정 선택시 개설 과정에 등록된 개설 과목 정보(과목명, 과목기간 및 등록된 교육생 정보)

		try {
			conn = util.open("211.63.89.64", "project", "java1234");
			//conn = util.open("localhost", "project", "java1234");
			stat = conn.createStatement(); // 쿼리를 날릴 수 있는 개체
			sql = "select * from vwopenCourseName";
			rs = stat.executeQuery(sql);
			System.out.println("\t\t\t[개설과정번호]\t[과정이름]");
			while (rs.next()) {
				System.out.printf("\t\t\t%s\t\t", rs.getString("openCourseNum"));
				System.out.printf("%s\t", rs.getString("courseName"));
				System.out.println();
			}
			System.out.print("\t\t\t▷과정 번호:");
			String num = scan.nextLine();

			sql = String.format("select * from vwopenCourseSubjectSelect where num = %s", num);
			int count = 0;
			rs = stat.executeQuery(sql);
			System.out.println();
			System.out.println("\t\t\t*** 시행 과정 과목별 정보 ***");
			System.out.println("\t\t\t[과목기간]\t\t\t[선생님이름]\t[과목이름]\t\t\t\t[교재]");
			while (rs.next()) {
				System.out.printf("\t\t\t%s\t", rs.getString("SubjectDuration"));
				System.out.printf("%s\t\t", rs.getString("teacherName"));
				System.out.printf("%-10s\t", rs.getString("textbookName"));
				System.out.printf("\t\t%s", rs.getString("subjectName"));
				System.out.println();

				count++;

				if (count % 100 == 0) {
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println("\t\t\t           -1을 누르면 종료");
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.print("\t\t\t번호:");
					String k = scan.nextLine();
					if (k == "-1") {
						break;
					} else {
						pause();
					}

				}
			}
			count = 0;
			sql = String.format("select * from vwopenCourseStudentSelect where num = %s", num);
			System.out.println();
			rs = stat.executeQuery(sql);
			System.out.println("\t\t\t*** 시행 과정 과목별 학생정보 ***");
			System.out
					.println("\t\t\t[학생이름]\t\t[학생 주민등록번호(뒷자리)]\t[학생 연락처]\t[등록일]\t\t[수료상태]");
			while (rs.next()) {
				System.out.printf("\t\t\t%s\t\t", rs.getString("studentName"));
				System.out.printf("%s\t\t\t", rs.getString("studentSsn"));
				System.out.printf("%s\t", rs.getString("studentTel"));
				System.out.printf("%s\t", rs.getString("registrationDate"));
				System.out.printf("%s", rs.getString("completionStatus"));
				System.out.println();

				count++;

				if (count % 100 == 0) {
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println("\t\t\t           -1을 누르면 종료");
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.print("\t\t\t번호:");
					String k = scan.nextLine();
					if (k == "-1") {
						break;
					} else {
						pause();
					}

				}
			}

			conn.close();
			rs.close();
			stat.close();
			pause1();
		} catch (Exception e) {
			System.out.println("\t\t\tAdminOpenCourse.vwopenCourseSubject()");
			System.out.println("\t\t\t개설 과정 선택시 개설 과목 정보 출력에 실패했습니다.");
			e.printStackTrace();
		}

	} // vwopenCourseSubject()

	/**
	 * 과정 수료 상태시 등록된 교육생 전체에 대해서 수료날짜를 지정합니다.
	 */
	public void vwopenCourseSelectEndDate() {
		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		Statement stat1 = null;
		DBUtil util = new DBUtil();
		String sql = null;
		// *** 특정 개설 과정이 수료한 경우 등록된 교육생 전체에 대해서 수료날짜를 지정 ***
		try {
			conn = util.open("211.63.89.64", "project", "java1234");
			//conn = util.open("localhost", "project", "java1234");

			stat1 = conn.createStatement();
			sql = "select * from vwopenCourseSelectEndDate";
			rs = stat1.executeQuery(sql);
			int count = 0;
			System.out.println("\t\t\t[수강내역번호]\t[개설과정번호]\t\t[수료 상태]\t\t[학생이름]\t\t[수료날짜]");
			while (rs.next()) {
				System.out.printf("\t\t\t%s\t\t\t", rs.getString("courseHistoryNum"));
				System.out.printf("%s\t\t", rs.getString("openCourseNum"));
				System.out.printf("%s\t\t", rs.getString("status"));
				System.out.printf("%s\t\t", rs.getString("student name"));
				System.out.printf("%s", rs.getString("endDate"));
				System.out.println();

				count++;

				if (count % 100 == 0) {
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println("\t\t\t           -1을 누르면 종료");
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.print("\t\t\t번호:");
					String k = scan.nextLine();
					if (k.equals("-1")) {
						break;
					} else {
						pause();
					}

				}

			}

			System.out.print("\t\t\t▷개설과정번호:");
			String openCourseNum = scan.nextLine();
			System.out.println("\t\t\t[과정 수료 날짜]");
			System.out.print("\t\t\t▷과정 수료 년(yyyy):");
			String endyear = scan.nextLine();
			System.out.print("\t\t\t▷과정 수료 월(mm):");
			String endmonth = scan.nextLine();
			System.out.print("\t\t\t▷과정 수료 일(dd):");
			String endday = scan.nextLine();

			sql = "{call  procstudentEndDate(?,?)}";
			stat = conn.prepareCall(sql);

			stat.setString(1, endyear + endmonth + endday);
			stat.setString(2, openCourseNum);

			stat.executeUpdate();

			System.out.println("\t\t\t완료");

			sql = "select * from vwopenCourseSelectEndDate";
			rs = stat1.executeQuery(sql);
			count = 0;
			System.out.println("\t\t\t[수강내역번호]\t[개설과정번호]\t[수료상태]\t[학생이름]\t[수료날짜]");
			while (rs.next()) {
				System.out.printf("\t\t\t%s\t", rs.getString("courseHistoryNum"));
				System.out.printf("%s\t", rs.getString("openCourseNum"));
				System.out.printf("%s\t", rs.getString("status"));
				System.out.printf("%s\t", rs.getString("student name"));
				System.out.printf("%s\t", rs.getString("endDate"));
				System.out.println();

				count++;

				if (count % 100 == 0) {
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println("\t\t\t           -1을 누르면 종료");
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.print("\t\t\t번호:");
					String k = scan.nextLine();
					if (k.equals("-1")) {
						break;
					} else {
						pause();
					}

				}

			}
			stat.close();
			conn.close();
		} catch (Exception e) {
			System.out.println("\t\t\tAdminOpenCourse.vwopenCourseSelectEndDate()");
			System.out.println("\t\t\t특정 개설 과정을 수료시 수료날짜 수정에 실패했습니다.");
			e.printStackTrace();
		}
	} // vwopenCourseSelectEndDate()

	/**
	 * 개설 과정 정보에 대한 추가 기능입니다.
	 */
	public void procopenCourseInsert() {

		Connection conn = null;
		CallableStatement stat = null;
		Statement stat1 = null;
		CallableStatement stat2 = null;
		CallableStatement stat3 = null;

		DBUtil util = new DBUtil();
		ResultSet rs = null;
		// 개설 과정 정보에 대한 추가 기능
		String pcourseCapacity = null;
		String pcoursePeriod = null;
		String sql = null;
		//ResultSet rs1 = null;
		
		System.out.println();
		int count = -1;
		try {
			conn = util.open("211.63.89.64", "project", "java1234");
			//conn = util.open("localhost", "project", "java1234");

			sql = "{ call procopenCourseInsert(?,?,?,?,?) }";
			stat2 = conn.prepareCall(sql);

			System.out.println("\t\t\t[개설 과정 정보 시작날짜]");

			System.out.print("\t\t\t▷시작날짜 년(yyyy):");
			String startyear = scan.nextLine();
			System.out.print("\t\t\t▷시작날짜 월(mm):");
			String startmonth = scan.nextLine();
			System.out.print("\t\t\t▷시작날짜 일(dd):");
			String startday = scan.nextLine();

			String startDate = startyear + startmonth + startday;
			stat2.setString(1, startDate);
			System.out.println();
			System.out.println("\t\t\t*** 기초 과정 정보 ***");
			sql = "select * from vwallCourse1";
			stat1 = conn.createStatement();
			rs = stat1.executeQuery(sql);
			System.out.println("\t\t\t[전체과정번호]\t[과정 정원]\t[과정 기간]\t[과정 이름]");
			while (rs.next()) {
				System.out.printf("\t\t\t%s\t\t", rs.getString("allCourseNum"));
				System.out.printf("%s\t", rs.getString("capacity"));
				System.out.printf("%s\t", rs.getString("coursePeriod"));
				System.out.printf("%s", rs.getString("courseName"));
				System.out.println();
			}
			System.out.println();
			System.out.print("\t\t\t▷과정 번호(allCourseNum): ");
			String pallCourseNum = scan.nextLine();
			System.out.println();
			System.out.println("\t\t\t*** 과정 번호에 해당하는 과목 정보 ***");
			sql = String.format("select * from vwSubject where num = %s", pallCourseNum);
			rs = stat1.executeQuery(sql);
			System.out.println("\t\t\t[과목기간]\t\t[과목이름]");
			while (rs.next()) {
				System.out.printf("\t\t\t%-10s\t", rs.getString("subjectPeriod"));
				System.out.printf("%-15s\t", rs.getString("subjectName"));
				System.out.println();
			}

			sql = String.format("select * from vwavailableCourse where courseNum = %s", pallCourseNum);
			rs = stat1.executeQuery(sql);
			if (rs.next()) {
				pcourseCapacity = rs.getString("courseCapacity");
				pcoursePeriod = rs.getString("coursePeriod");
			}
			
			sql = "{call procavailableClassRoom(?,?,?)}";
			stat = conn.prepareCall(sql);
			stat.setString(1, startDate);
			stat.registerOutParameter(2, OracleTypes.CURSOR);
			stat.setString(3, pcourseCapacity);

			stat.executeQuery();

			rs = (ResultSet) stat.getObject(2);

			System.out.println();
			System.out.println("\t\t\t*** 가능한 교실 ***");
			System.out.println("\t\t\t[강의실번호]\t[강의실이름]\t[강의실 인원]");
			while (rs.next()) {
				System.out.printf("\t\t\t%s\t\t", rs.getString("classRoomNum"));
				System.out.printf("%s\t\t", rs.getString("classRoomName"));
				System.out.printf("%s", rs.getString("capacity"));
				System.out.println();
			}
			System.out.println();
			System.out.print("\t\t\t교실 번호:");
			String classRoomNum = scan.nextLine();

			sql = "select * from tblTeacher";
			rs = stat1.executeQuery(sql);
			int cnt = 0;
			ResultSet rs2 = null;
			while (rs.next()) {
				sql = "{call procCount(?,?,?,?)}";
				stat = conn.prepareCall(sql);

				String pteacherNum = rs.getString("num");
				stat.setString(1, pallCourseNum);
				stat.setString(2, pteacherNum);
				stat.setString(3, startDate);
				stat.registerOutParameter(4, OracleTypes.NUMBER);
				stat.executeQuery();
				count = stat.getInt(4);

				if (count == 0) {
					sql = "{call procavailableTeacher(?,?,?)}";
					stat3 = conn.prepareCall(sql);
					stat3.setString(1, startDate);
					stat3.registerOutParameter(2, OracleTypes.CURSOR);
					stat3.setString(3, pteacherNum);
					stat3.executeQuery();

					rs2 = (ResultSet) stat3.getObject(2);

					System.out.println("\t\t\t*** 가능한 선생님 ***");
					System.out.println("\t\t\t[선생님 번호]\t[선생님 이름]");
					while (rs2.next()) {
						System.out.printf("\t\t\t%s\t", rs2.getString("teacherNum"));
						System.out.printf("%s", rs2.getString("teacherName"));
						System.out.println();
						cnt++;
					}
					
					stat3.close();
				}
				// System.out.println(count);
			}
			if (cnt == 0) {
				System.out.println("\t\t\t가능한 선생님이 없습니다.");
				// menu();
				//return;
			
			} else {
				
				System.out.print("\t\t\t선생님 번호:");
				String teacherNum = scan.nextLine();
				// 선생님이 과정에서 과목을 정하면 선생님이 가능한 과목이 모두 포함되는 얘만 찾으면 되는데 이거는 개설 과목 관리에서
				// 만약에 선생님이 가능하지 않는 과목이 포함되어 있으면 강의를 개설을 못하는

				Calendar c = Calendar.getInstance();

				c.set(Integer.parseInt(startyear), Integer.parseInt(startmonth), Integer.parseInt(startday));
				c.add(c.MONTH, Integer.parseInt(pcoursePeriod));
				String s = String.format("%tF", c).replace("-", "");
				stat2.setString(2, s);
				stat2.setString(3, classRoomNum);
				stat2.setString(4, teacherNum);
				stat2.setString(5, pallCourseNum);

				stat2.executeUpdate();

				System.out.println("\t\t\t완료");
				
			
			}
			
			rs.close();
			conn.close();
			stat3.close();
			stat1.close();
			stat.close();
			
			stat2.close();
			
		} catch (Exception e) {
			System.out.println("\t\t\tAdminOpenCourse.procopenCourseInsert()");
			System.out.println("\t\t\t개설 과정 정보에 대한 추가에 실패했습니다.");
			e.printStackTrace();
		}
		
	} // procopenCourseInsert()

	/**
	 * 선택적으로 개설 과정 정보에 대한 수정기능입니다.
	 */
	public void procopenCourseUpdate() {

		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		ResultSet rs = null;
		// Statement stat1 = null;

		String sql = null;
		String ostartDate = null;
		String oendDate = null;
		String oclassRoomNum = null;
		String oteacherNum = null;
		String oallCourseNum = null;
		vwopenCourse();
		// 개설 과정 정보에 대한 수정 기능
		boolean loop = true;
		while(loop) {
		try {
			conn = util.open("211.63.89.64", "project", "java1234");
			//conn = util.open("localhost", "project", "java1234");

			sql = "{ call procopenCourseSelect(?,?) }";
			stat = conn.prepareCall(sql);
			System.out.print("\t\t\t▷시행 과정 번호:");
			String num = scan.nextLine();
			stat.setString(1, num);
			stat.registerOutParameter(2, OracleTypes.CURSOR);

			stat.executeQuery();

			rs = (ResultSet) stat.getObject(2);
			String a = null;
			String b = null;
			if (rs.next()) {
				ostartDate = rs.getString("startDate");
				oendDate = rs.getString("endDate");
				oclassRoomNum = rs.getString("classRoomNum");
				oteacherNum = rs.getString("teacherNum");
				oallCourseNum = rs.getString("allCourseNum");
			}

			rs.close();

			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t1. 과정 시작 날짜 수정");
			System.out.println("\t\t\t2. 과정 종료 날짜 수정");
			System.out.println("\t\t\t3. 과정 강의실 번호 수정");
			System.out.println("\t\t\t4. 선생님 번호 수정");
			System.out.println("\t\t\t5. 과정 번호 수정");
			System.out.println("\t\t\t0. 뒤로가기");
		
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.print("\t\t\t▷입력:");
			String sel = scan.nextLine();
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

			switch (sel) {
			case "1":
				System.out.print("\t\t\t▷과정 시작 년(yyyy):");
				String start_year = scan.nextLine();
				System.out.print("\t\t\t▷과정 시작 월(mm):");
				String start_month = scan.nextLine();
				System.out.print("\t\t\t▷과정 시작 일(dd):");
				String start_day = scan.nextLine();
				//// String sql = "update tblAddress set age = age + 1";
				// String startDate = start_year + start_month + start_day;

				sql = "{call procopenCourseUpdate(?,?,?,?,?,?)}";
				stat = conn.prepareCall(sql);
				stat.setString(1, num);
				stat.setString(2, start_year + start_month + start_day);
				a = oendDate.substring(0, 10); // yyyy-mm-dd hh:mm:ss
				oendDate = a.replace("-", "");
				stat.setString(3, oendDate);
				stat.setString(4, oclassRoomNum);
				stat.setString(5, oteacherNum);
				stat.setString(6, oallCourseNum);
				stat.executeUpdate();

				stat.close();
				System.out.println("\t\t\t완료");
				break;
			case "2":
				System.out.print("\t\t\t▷과정 종료 년(yyyy):");
				String end_year = scan.nextLine();
				System.out.print("\t\t\t▷과정 종료 월(mm):");
				String end_month = scan.nextLine();
				System.out.print("\t\t\t▷과정 종료 일(dd):");
				String end_day = scan.nextLine();

				sql = "{call procopenCourseUpdate(?,?,?,?,?,?)}";
				stat = conn.prepareCall(sql);
				stat.setString(1, num);
				b = ostartDate.substring(0, 10);
				ostartDate = b.replace("-", "");
				stat.setString(2, ostartDate);
				stat.setString(3, end_year + end_month + end_day);
				stat.setString(4, oclassRoomNum);
				stat.setString(5, oteacherNum);
				stat.setString(6, oallCourseNum);
				stat.executeUpdate();

				stat.close();
				System.out.println("\t\t\t완료");
				break;
			case "3":
				System.out.print("\t\t\t▷과정 강의실 번호:");
				String classRoomNum = scan.nextLine();
				sql = "{call procopenCourseUpdate(?,?,?,?,?,?)}";
				stat = conn.prepareCall(sql);
				a = oendDate.substring(0, 10);
				oendDate = a.replace("-", "");
				b = ostartDate.substring(0, 10);
				ostartDate = b.replace("-", "");
				stat.setString(1, num);
				stat.setString(2, ostartDate);
				stat.setString(3, oendDate);
				stat.setString(4, classRoomNum);
				stat.setString(5, oteacherNum);
				stat.setString(6, oallCourseNum);
				stat.executeUpdate();

				stat.close();
				System.out.println("\t\t\t완료");
				break;
			case "4":
				System.out.print("\t\t\t▷선생님 번호:");
				String teacherNum = scan.nextLine();
				sql = "{call procopenCourseUpdate(?,?,?,?,?,?)}";
				stat = conn.prepareCall(sql);
				a = oendDate.substring(0, 10);
				oendDate = a.replace("-", "");
				b = ostartDate.substring(0, 10);
				ostartDate = b.replace("-", "");
				stat.setString(1, num);
				stat.setString(2, ostartDate);
				stat.setString(3, oendDate);
				stat.setString(4, oclassRoomNum);
				stat.setString(5, teacherNum);
				stat.setString(6, oallCourseNum);
				stat.executeUpdate();

				stat.close();
				System.out.println("\t\t\t완료");
				break;
			case "5":
				System.out.print("\t\t\t▷과정 번호:");
				String allCourseNum = scan.nextLine();
				sql = "{call procopenCourseUpdate(?,?,?,?,?,?)}";
				stat = conn.prepareCall(sql);
				a = oendDate.substring(0, 10);
				oendDate = a.replace("-", "");
				b = ostartDate.substring(0, 10);
				ostartDate = b.replace("-", "");
				stat.setString(1, num);
				stat.setString(2, ostartDate);
				stat.setString(3, oendDate);
				stat.setString(4, oclassRoomNum);
				stat.setString(5, oteacherNum);
				stat.setString(6, allCourseNum);
				stat.executeUpdate();

				stat.close();
				System.out.println("\t\t\t완료");
				break;
				case "0":
					loop = false;
					break;
				default : 
					System.out.println("번호를 다시 입력해주세요.");
					
			}
			stat.close();
			conn.close();

		} catch (Exception e) {
			System.out.println("\t\t\tAdminOpenCourse.procopenCourseUpdate()");
			System.out.println("\t\t\t개설 과정 정보 수정에 실패했습니다.");
			e.printStackTrace();
			}
		}
	} // procopenCourseUpdate()

	/**
	 * 개설 과정을 삭제하는 기능입니다.
	 */
	public void procopenCourseDelete() {

		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();

		String num = null;
		vwopenCourse();
		// *** 개설 과정 정보에 대한 삭제 기능 ***
		try {
			//conn = util.open("211.63.89.64", "project", "java1234");
			conn = util.open("211.63.89.64", "project", "java1234");

			String sql = "{ call procopenCourseDelete(?) }";
			stat = conn.prepareCall(sql);
			System.out.print("\t\t\t▷삭제할 시행 과정 번호:");
			num = scan.nextLine();
			stat.setString(1, num);
			stat.executeUpdate();

			stat.close();
			conn.close();
			System.out.println("\t\t\t완료");

		} catch (Exception e) {
			System.out.println("\t\t\tAdminOpenCourse.procopenCourseDelete()");
			System.out.println("\t\t\t개설 과정 정보 삭제에 실패했습니다.");
			e.printStackTrace();
		}

	} // procopenCourseDelete()

	/**
	 * 개설 과정 정보에 대한 출력기능입니다.
	 */
	public void vwopenCourse() {

		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		// *** 개설 과정 정보에 대한 출력 기능 ***
		try {
			conn = util.open("211.63.89.64", "project", "java1234");
			//conn = util.open("localhost", "project", "java1234");
			stat = conn.createStatement(); // 쿼리를 날릴 수 있는 개체
			String sql = "select * from vwopenCourse";
			rs = stat.executeQuery(sql);
			System.out.println("\t\t\t[개설과정 번호]\t[과정 기간]\t\t\t[강의실 번호]\t[선생님 번호]\t[전체과정번호]");
			while (rs.next()) {

				System.out.printf("\t\t\t%s\t\t", rs.getString("num"));
				System.out.printf("%s~%s\t\t", rs.getString("startDate").substring(0, 10),
						rs.getString("endDate").substring(0, 10));
				System.out.printf("%s\t\t", rs.getString("classRoomNum"));
				System.out.printf("%s\t\t", rs.getString("teacherNum"));
				System.out.printf("%s\t", rs.getString("allCourseNum"));
				System.out.println();
			}
			stat.close();
			conn.close();
			rs.close();
			pause1();
		} catch (Exception e) {
			System.out.println("AdminOpenCourse.vwopenCourse()");
			System.out.println("개설 과정 정보 출력에 실패했습니다.");
			e.printStackTrace();
		}
	} // vwopenCourse()

} // AdminOpenCourse
