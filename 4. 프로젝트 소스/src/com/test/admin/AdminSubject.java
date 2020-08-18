package com.test.admin;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

import oracle.jdbc.OracleTypes;

/**
 * 시행과정 별 과목을 관리하기 위한 클래스입니다.
 * @author leeho
 *
 */
public class AdminSubject {

	DBUtil util = new DBUtil();
	CallableStatement stat = null;

	/**
	 * 개설 과목관리 메인 화면을 출력하는 메소드입니다.
	 */
	public void manageSubjectmenu() {

		while (true) {

			Connection conn = null;
			Scanner scan = new Scanner(System.in);
			// Database Connection
			conn = util.open("211.63.89.64", "project", "java1234");

			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t\t\t개설 과정별 과목 관리");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t1. 조회");
			System.out.println("\t\t\t2. 과목별 기간 등록");
			System.out.println("\t\t\t3. 수정");
			System.out.println("\t\t\t4. 삭제");
			System.out.println("\t\t\t5. 교재 배부");
			System.out.println("\t\t\t0. 뒤로가기");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.print("\t\t\t▷ 입력: ");

			String sel = scan.nextLine();

			// 조회
			if (sel.equals("1")) {
				PrintSjInfoPeriodBySubject();
			}
			// 등록
			// enroll new Subject
			else if (sel.equals("2")) {
				enrollPeriodBySubject();
			}

			// 수정
			// modify subject info
			else if (sel.equals("3")) {
				modifyPeriodBySubject();
			}
			// 삭제
			// delete subject info
			else if (sel.equals("4")) {
				deletePeriodBySubject();
			} else if (sel.equals("5")) {
				distributionMenu();
			}
			// exit
			else if (sel.equals("0")) {
				break;
			} else {
				System.out.println("\t\t\t번호를 다시 입력해주세요.");
			}

		}
	}
	
	public void distributionMenu() {
		// 이도윤

		Scanner scan = new Scanner(System.in);

		while (true) {

			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t\t\t교재 배부 관리");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

			System.out.println("\t\t\t1. 과정별 배부 내역 조회");
			System.out.println("\t\t\t2. 미배부 학생 내역 조회");
			System.out.println("\t\t\t3. 과목별 배부 내역 수정");
			System.out.println("\t\t\t4. 학생별 배부 내역 수정");
			System.out.println("\t\t\t0. 뒤로가기");

			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.print("\t\t\t▷ 입력:");
			String cho = scan.nextLine();
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println();

			if (cho.equals("1")) {

				// 과정별 배부 내역 조회
				procDistributionByCourse();

			} else if (cho.equals("2")) {
				// 미배부 학생 내역 조회
				vwNotDistributed();

			} else if (cho.equals("3")) {
				// 과목별 배부 내역 수정
				procUpdateDistrByOpenCourse();

			} else if (cho.equals("4")) {
				// 학생별 배부 내역 수정
				procUpdateDistrByStudent();

			} else if (cho.equals("0")) {
				// 뒤로가기
				System.out.println("\t\t\t뒤로가기를 선택하셨습니다.");
				System.out.println("\t\t\t엔터를 입력하시면 이전 페이지로 돌아갑니다.");
				scan.nextLine();
				break;

			} else {
				// 잘못 입력했을 경우
				System.out.println();
				System.out.println("\t\t\t잘못 입력하셨습니다. ");
				System.out.println("\t\t\t1에서 4 사이의 숫자를 입력해주세요.");
				System.out.println();
			} // if
		} // while
	}

	public void procDistributionByCourse() {

		Connection conn = null;

		// 강의명 출력용
		Statement cstat = null;
		ResultSet crs = null;

		// 교재배부내역 프로시저 출력용
		CallableStatement stat = null;
		ResultSet rs = null;

		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);

		try {

			conn = util.open("211.63.89.64", "project", "java1234");
			String cnum = "";

			// 헤더 출력
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t\t\t과정별 배부 내역 조회");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println();

			// 사용자에게 정보 입력받음
			System.out.print("\t\t\t과정번호 : ");
			cnum = scan.nextLine();

			System.out.println();
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println();

			// 선택한 과정번호의 과정이름 불러옴

			String sql = "select ac.name as courseName from tblOpencourse oc inner join tblAllcourse ac on oc.allcourseNum = ac.num where oc.num = "
					+ cnum;
			cstat = conn.createStatement();
			crs = cstat.executeQuery(sql);

			// 과정이름 출력해줌
			while (crs.next()) {
				System.out.printf("\t\t\t[%s]\n", crs.getString("courseName"));
			}

			// 과정별 교재배부목록 프로시저 호출 준비
			sql = "{ call procDistributionByCourse(?,?) }";
			stat = conn.prepareCall(sql);

			stat.setString(1, cnum);
			stat.registerOutParameter(2, OracleTypes.CURSOR);

			// 프로시저 호출
			stat.executeQuery();

			rs = (ResultSet) stat.getObject(2);

			System.out.println();
			System.out.println("\t\t\t[이름]\t\t[교재명]\t\t[상태]");
			System.out.println("\t\t\t――――――――――――――――――――――――――――――――――――――――――――");

			while (rs.next()) {
				System.out.printf("\t\t\t%s\t\t%-15s\t%s\n", rs.getString("StudentName"), rs.getString("BookName"),
						rs.getString("status"));
			}
			System.out.println();
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

			// DB와 연결 종료
			cstat.close();
			stat.close();
			conn.close();

		} catch (Exception e) {

			e.printStackTrace();
		}

		System.out.println();
		System.out.println("\t\t\t조회가 완료되었습니다.");
		System.out.println("\t\t\t엔터를 입력하시면 이전 페이지로 돌아갑니다.");
		scan.nextLine();

	}

	public void vwNotDistributed() {
		// 이도윤

		// 전체 과정의 교재 배부받지 못한 학생 목록 출력

		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);

		try {

			conn = util.open("211.63.89.64", "project", "java1234");

			// 헤더 출력
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t\t\t미배부 학생 목록 조회");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println();

			// 선택한 과정번호의 과정이름 불러옴

			String sql = "select * from vwNotDistributed";
			stat = conn.createStatement();
			rs = stat.executeQuery(sql);

			// 헤더 출력
			System.out.println("\t\t[이름]\t[교재명]\t\t[과정명]");
			System.out.println(
					"\t\t―――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――");

			// 목록 출력하기
			while (rs.next()) {
				System.out.printf("\t\t%s\t%-15s\t%s\n", rs.getString("StudentName"), rs.getString("bookName"),
						rs.getString("courseName"));
			}
			System.out.println();
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

			stat.close();
			conn.close();

		} catch (Exception e) {

			e.printStackTrace();
		}

		System.out.println();
		System.out.println("\t\t\t조회가 완료되었습니다.");
		System.out.println("\t\t\t엔터를 입력하시면 이전 페이지로 돌아갑니다.");
		scan.nextLine();

	}

	public void procUpdateDistrByOpenCourse() {
		// 이도윤

		// 특정 시행과정의 특정 과목 전체 배부내역 수정

		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);

		String courseNum = "";
		String subjectNum = "";

		try {

			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t\t\t과목별 배부 내역 수정");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.print("\t\t\t시행과정 번호 : ");
			courseNum = scan.nextLine();
			System.out.print("\t\t\t과목 번호 : ");
			subjectNum = scan.nextLine();
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println();

			// 프로시저 호출 준비
			conn = util.open("211.63.89.64", "project", "java1234");
			String sql = "{ call procUpdateDistrByOpenCourse(?,?,?) }";
			stat = conn.prepareCall(sql);

			// 프로시저 매개변수 설정
			stat.setString(1, courseNum);
			stat.setString(2, subjectNum);
			stat.registerOutParameter(3, OracleTypes.NUMBER);

			// 프로시저 실행
			stat.executeUpdate();

			if (stat.getInt(3) > 0) {

				// 과목별 배부 내역 수정에 성공한 경우
				System.out.println("\t\t\t과목별 배부 내역 수정이 완료되었습니다.");
				System.out.println("\t\t\t엔터를 입력하시면 이전 페이지로 돌아갑니다.");
				scan.nextLine();

			} else {

				// 과목별 배부 내역 수정에 실패한 경우
				System.out.println("\t\t\t과목별 배부 내역 수정에 실패했습니다.");
				System.out.println("\t\t\t엔터를 입력하시면 이전 페이지로 돌아갑니다.");
				scan.nextLine();
			}

			// DB 연결 끊기
			stat.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void procUpdateDistrByStudent() {
		// 이도윤

		// 특정 학생의 특정 과목 배부내역 수정

		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);

		String studentNum = "";
		String subjectNum = "";

		try {

			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t\t\t학생별 배부 내역 수정");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println();
			System.out.print("\t\t\t학생 번호 : ");
			studentNum = scan.nextLine();
			System.out.print("\t\t\t과목 번호 : ");
			subjectNum = scan.nextLine();
			System.out.println();
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

			// 프로시저 호출 준비
			conn = util.open("211.63.89.64", "project", "java1234");
			String sql = "{ call procUpdateDistrByStudent(?,?,?) }";
			stat = conn.prepareCall(sql);

			// 프로시저 매개변수 설정
			stat.setString(1, studentNum);
			stat.setString(2, subjectNum);
			stat.registerOutParameter(3, OracleTypes.NUMBER);

			// 프로시저 실행
			stat.executeUpdate();

			if (stat.getInt(3) > 0) {

				// 교사 계정 삭제에 성공한 경우
				System.out.println();
				System.out.println("\t\t\t학생별 배부 내역 수정이 완료되었습니다.");
				System.out.println("\t\t\t엔터를 입력하시면 이전 페이지로 돌아갑니다.");
				scan.nextLine();

			} else {

				// 교사 계정 삭제에 실패한 경우
				System.out.println();
				System.out.println("\t\t\t학생별 배부 내역 수정에 실패했습니다.");
				System.out.println("\t\t\t엔터를 입력하시면 이전 페이지로 돌아갑니다.");
				scan.nextLine();
			}

			// DB 연결 끊기
			stat.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 시행과정별 과목들의 기간을 삭제하기 위한 메소드입니다.
	 */
	private void deletePeriodBySubject() {
		Connection conn = new DBUtil().open("211.63.89.64","project","java1234");
		Scanner scan = new Scanner(System.in);
		// print opencourse info
		vwOpencourse();

		// input allcoursenum
		System.out.println("과정 번호:");
		String openCourseNum = scan.nextLine();

		try {

			// declare variable
			String sql = "{ call procPrintSjInfoPeriodBySubject(?,?,?)}";
			CallableStatement stat = conn.prepareCall(sql);
			ResultSet rs = null;
			ResultSet rs2 = null;

			// set
			stat.setString(1, openCourseNum);
			stat.registerOutParameter(2, OracleTypes.CURSOR);
			stat.registerOutParameter(3, OracleTypes.CURSOR);
			stat.executeUpdate();

			// get result set
			rs = (ResultSet) stat.getObject(2);
			rs2 = (ResultSet) stat.getObject(3);

			// print rs
			System.out.println("[과정명]\t[시작년월일]\t[종료년웡릴]\t[강의실명]\t[교사명]");
			while (rs.next()) {
				System.out.printf("%s\t%s\t%s\t%s\t%s\n", rs.getString("ocName"), rs.getString("startdate"),
						rs.getString("enddate"), rs.getString("classroomnum"), rs.getString("teachername"));
			}

			// print rs2
			System.out.println("[구성 과목 리스트]");
			System.out.println("[과목명]\t[시작년월일]\t[종료년월일]\t[교재명]\t");
			while (rs2.next()) {
				System.out.printf("%s\t%s\t%s\t%s\t%s\n", rs2.getString("subjectnum"), rs2.getString("subjectname"),
						rs2.getString("startdate"), rs2.getString("enddate"), rs2.getString("bookname"));
			}

			// input subject number
			System.out.println("과목 번호:");
			String subjectnum = scan.nextLine();

			rs.close();
			rs2.close();
			stat.close();

			Statement stat2 = conn.createStatement();
			sql = String.format("delete from tblperiodBySubject where num = %s", subjectnum);

			stat2.executeUpdate(sql);

			// fin message
			System.out.println("삭제가 완료되었습니다.");
			stat2.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 시행과정별 과목들의 기간을 수정하기 위한 메소드입니다.
	 */
	private void modifyPeriodBySubject() {

		// print opencourse info
		Connection conn = new DBUtil().open("211.63.89.64", "project", "java1234");
		Scanner scan = new Scanner(System.in);
		vwOpencourse();

		// input allcoursenum
		System.out.println("과정 번호:");
		String openCourseNum = scan.nextLine();

		try {

			// declare variable
			String sql = "{ call procPrintSjInfoPeriodBySubject(?,?,?)}";
			CallableStatement stat = conn.prepareCall(sql);
			ResultSet rs = null;
			ResultSet rs2 = null;

			// set
			stat.setString(1, openCourseNum);
			stat.registerOutParameter(2, OracleTypes.CURSOR);
			stat.registerOutParameter(3, OracleTypes.CURSOR);
			stat.executeUpdate();

			// get result set
			rs = (ResultSet) stat.getObject(2);
			rs2 = (ResultSet) stat.getObject(3);

			// print rs
			System.out.println("[과정명]\t[시작년월일]\t[종료년웡릴]\t[강의실명]\t[교사명]");
			while (rs.next()) {
				System.out.printf("%s\t%s\t%s\t%s\t%s\n", rs.getString("ocName"), rs.getString("startdate"),
						rs.getString("enddate"), rs.getString("classroomnum"), rs.getString("teachername"));
			}

			// print rs2
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("[구성 과목 리스트]");
			System.out.println("[과목명]\t[시작년월일]\t[종료년월일]\t[교재명]\t");
			while (rs2.next()) {
				System.out.printf("%s\t%s\t%s\t%s\t%s\n", rs2.getString("subjectnum"), rs2.getString("subjectname"),
						rs2.getString("startdate"), rs2.getString("enddate"), rs2.getString("bookname"));
			}

			// input subject number
			System.out.println("과목 번호:");
			String subjectnum = scan.nextLine();

			rs.close();
			rs2.close();

			System.out.println("시작년월일");
			System.out.print("날짜입력(YY):");
			String startyear = scan.nextLine();
			System.out.print("날짜입력(MM):");
			String startmonth = scan.nextLine();
			System.out.print("날짜입력(DD):");
			String startdate = scan.nextLine();

			System.out.println("종료년월일");
			System.out.print("날짜입력(YY):");
			String endyear = scan.nextLine();
			System.out.print("날짜입력(MM):");
			String endmonth = scan.nextLine();
			System.out.print("날짜입력(DD):");
			String enddate = scan.nextLine();

			Statement stat2 = conn.createStatement();
			System.out.println("stat2");
			sql = String.format("update tblperiodBySubject set startdate = '%s', enddate = '%s' where num = %s",
					"20" + startyear + startmonth + startdate, "20" + endyear + endmonth + enddate, subjectnum);
			System.out.println(sql);
			stat2.executeUpdate(sql);
			System.out.println("update");
			// fin message
			System.out.println("수정이 완료되었습니다.");

			stat.close();
			stat2.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 개설 과정별 과목들의 정보를 조회하는 메소드입니다.
	 */
	private void PrintSjInfoPeriodBySubject() {

		Connection conn = new DBUtil().open("211.63.89.64", "project", "java1234");
		Scanner scan = new Scanner(System.in);
		// print opencourse info
		vwOpencourse();

		// input allcoursenum
		System.out.println("과정 번호:");
		String openCourseNum = scan.nextLine();

		try {

			// declare variable
			String sql = "{ call procPrintSjInfoPeriodBySubject(?,?,?)}";
			CallableStatement stat = conn.prepareCall(sql);
			ResultSet rs = null;
			ResultSet rs2 = null;

			// set
			stat.setString(1, openCourseNum);
			stat.registerOutParameter(2, OracleTypes.CURSOR);
			stat.registerOutParameter(3, OracleTypes.CURSOR);
			stat.executeUpdate();

			// get result set
			rs = (ResultSet) stat.getObject(2);
			rs2 = (ResultSet) stat.getObject(3);

			// print rs
			System.out.println("[기간]\t\t\t[교사명]\t[강의실명]\t[과정명]");
			while (rs.next()) {
				System.out.printf("%s\t%s\t%s\t\t%s",
						rs.getString("startdate").substring(0, 10) + " ~ " + rs.getString("enddate").substring(0, 10),
						rs.getString("teachername"), rs.getString("classroomnum") + "강의실", rs.getString("ocName"));
			}

			// print rs2
			System.out.println();
			System.out.println("\n[구성 과목 리스트]");
			System.out.println("[과목번호]\t[기간]\t\t\t[과목명]\t\t\t[교재명]");
			while (rs2.next()) {
				System.out.printf("%5s\t%s\t\t%-14s\t\t%-15s\n", rs2.getString("subjectnum"),
						rs2.getString("startdate").substring(0, 10) + " ~ " + rs2.getString("enddate").substring(0, 10),
						rs2.getString("subjectname"), rs2.getString("bookname"));
			}

			// fin message
			System.out.println("\n\t\t\t계속 하시려면 엔터를 입력해주세요.");
			scan.nextLine();
			rs.close();
			rs2.close();
			stat.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void vwOpencourse() {

		try {
			Connection conn = new DBUtil().open("211.63.89.64", "project", "java1234");
			// get subject list
			// 과목 리스트 가져오는 sql문
			Statement stat = conn.createStatement();
			String sql = "select * from vwopencourseinfo2 order by opencoursenum";
			ResultSet rs = stat.executeQuery(sql);

			System.out.println("[과정번호]\t[기간]\t\t[강의실]\t\t[과정명]\n");

			// print subject list
			// 과목 목록 출력
			while (rs.next()) {
				System.out.printf("%s\t%s\t%s\t%s\n", rs.getString("opencoursenum"), rs.getString("period"),
						rs.getString("classroomname"), rs.getString("opencoursename"));

			}
			System.out.println("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void printSubjectList() {
		Connection conn = new DBUtil().open("211.63.89.64", "project", "java1234");
		Scanner scan = new Scanner(System.in);
		// print info of allcourse
		vwAllcourse(conn);

		// input allcoursenum
		System.out.println("과정 번호:");
		String allCourseNum = scan.nextLine();

		try {

			// declare variables
			String sql = "{ call procPrintSubjectList(?,?,?)}";
			CallableStatement stat = conn.prepareCall(sql);
			ResultSet rs = null;
			ResultSet rs2 = null;

			// set param
			stat.setString(1, allCourseNum);
			stat.registerOutParameter(2, OracleTypes.CURSOR);
			stat.registerOutParameter(3, OracleTypes.CURSOR);
			stat.executeQuery();

			// get ResultSet
			// Course name
			rs = (ResultSet) stat.getObject(2);
			// subject list of the course
			rs2 = (ResultSet) stat.getObject(3);

			// print rs
			while (rs.next()) {
				System.out.printf("과정명 : %s\n", rs.getString("name"));
			}

			// print rs2
			System.out.println("[구성 과목 리스트]");
			int i = 1;
			while (rs2.next()) {
				System.out.printf("%d. %s\n", i, rs2.getString("subjectname"));
				i++;
			}

			// fin message
			System.out.println("\n\t\t\t계속 하시려면 엔터를 입력해주세요");
			scan.nextLine();
			stat.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	/**
	 * 시행과정 별 과목들의 기간을 등록하기 위한 메소드입니다.
	 */
	private void enrollPeriodBySubject() {

		while (true) {
			Connection conn = new DBUtil().open("211.63.89.64", "project", "java1234");
			Scanner scan = new Scanner(System.in);
			// print list of opencourse
			// 개설과정 출력
			vwOpencourse();
			System.out.println("0. 뒤로가기");

			// input courseNum
			System.out.println("과정번호: ");
			String courseNum = scan.nextLine();
			if (courseNum.equals("0")) {
				break;
			}

			// get the period and number of subject by course
			String period = getPeriodOfOpencourse(courseNum);
			int numOfSubject = period.equals("5.5") ? 8 : period.equals("6") ? 9 : 10;

			// print list of subject of Opencourse
			// 선택된 과목 출력
			procSubjectListOfOpenCourse(courseNum);
			System.out.println("\t\t\t0. 뒤로가기");

//			System.out.println("\t\t\t[강의 가능 교사 목록]");
//			System.out.println("\t\t\t[교사번호]\t[교사명]");
//			getTeacherOfOpencourse(conn, courseNum, period);

			// input subjectnum, startdate, enddate as number of subject
			for (int i = 0; i < numOfSubject; i++) {
				System.out.print("\t\t\t과목번호: ");
				String subjectNum = scan.nextLine();
				if (subjectNum.equals("0")) {
					break;
				}

				System.out.print("\t\t\t시작년월일: ");
				String startDate = scan.nextLine();
				if (startDate.equals("0")) {
					break;
				}

				System.out.print("\t\t\t종료년월일: ");
				String endDate = scan.nextLine();
				if (endDate.equals("0")) {
					break;
				}

				// insert input period
				try {
					String sql = "{ call procEnrollperiodBySubject(?,?,?,?)}";
					CallableStatement stat = conn.prepareCall(sql);

					// set
					stat.setString(1, courseNum);
					stat.setString(2, subjectNum);
					stat.setString(3, startDate);
					stat.setString(4, endDate);
					stat.executeUpdate();

					stat.close();
					// fin message
					System.out.println("\t\t\t등록이 완료되었습니다.");

				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			// Database connection close
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	private void procSubjectListOfOpenCourse(String courseNum) {

		try {
			Connection conn = new DBUtil().open("211.63.89.64", "project", "java1234");
			// get subject list
			// 과목 리스트 가져오는 sql문
			String sql = "{ call procPrintSjListofOpencourse(?,?)}";
			CallableStatement stat = conn.prepareCall(sql);
			stat.setString(1, courseNum);
			stat.registerOutParameter(2, OracleTypes.CURSOR);
			stat.executeQuery();

			ResultSet rs = (ResultSet) stat.getObject(2);

			System.out.println("\t\t\t[과목번호]\t[과목명]\t[구분]\t[순서]");

			// print subject list
			// 과목 목록 출력
			while (rs.next()) {
				System.out.printf("\t\t\t%s\t\t%s\t\t\t%s\t%s\n", rs.getString("num"), rs.getString("name"),
						rs.getString("type"), rs.getString("seq"));

			}
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private String getPeriodOfOpencourse(String courseNum) {
		String period = "";
		try {
			Connection conn = new DBUtil().open("211.63.89.64", "project", "java1234");
			// get subject list
			// 과목 리스트 가져오는 sql문
			Statement stat = conn.createStatement();
			String sql = String.format(
					"select period from tblopencourse oc inner join tblallcourse ac on oc.allcoursenum = ac.num where oc.num = %s",
					courseNum);
			ResultSet rs = stat.executeQuery(sql);

			if (rs.next()) {
				period = rs.getString("period");
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return period;
	}

	private void getTeacherOfOpencourse(String courseNum, String period) {

		boolean teacherAble = false;

		try {
			Connection conn = new DBUtil().open("211.63.89.64", "project", "java1234");
			// get subject number
			Statement stat = conn.createStatement();
			String sql = String.format(
					"select subjectnum from tblsubjectbycourse where allcoursenum = (select oc.allcoursenum from tblopencourse oc inner join tblallcourse ac on oc.allcoursenum = ac.num where oc.num = %s) order by subjectnum",
					courseNum);
			ResultSet rs = stat.executeQuery(sql);

			// the first 3 subject is mandatory, so we have to pass three times
			// 1,2,3 과목은 필수여서 과목번호 3번을 넘깁니다.
			rs.next();
			rs.next();
			rs.next();

			// call procedure by period

			// when period is 5.5
			// 5.5개월 과정
			if (period.equals("5.5")) {

				try {
					sql = "{ call procPrintAvailableTeacer55(?,?,?,?,?,?)}";
					CallableStatement stat2 = conn.prepareCall(sql);

					// rs is list of subject number, get in the number to param of
					// procPrintAvailableTeacer
					// rs는 과목번호를 담고 있고, procPrintAvailableTeacer 매개변수에 과목번호를 넣어줍니다.
					int i = 1;
					while (rs.next()) {
						stat2.setString(i, rs.getString("subjectnum"));
						i++;
					}
					stat2.registerOutParameter(6, OracleTypes.CURSOR);
					stat2.executeQuery();
					ResultSet rs2 = (ResultSet) stat2.getObject(6);

					// print teacher info
					while (rs2.next()) {
						teacherAble = true;
						System.out.printf("\t\t\t%s\t\t%s\n", rs2.getString("teachernum"),
								rs2.getString("teachername"));
					}
					if (!teacherAble) {
						System.out.println("\t\t\t강의 가능 교사가 없습니다.");
					}
					stat.close();
					stat2.close();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			// when period is 6
			else if (period.equals("6")) {

				try {

					sql = "{ call procPrintAvailableTeacer6(?,?,?,?,?,?,?)}";
					CallableStatement stat2 = conn.prepareCall(sql);

					int i = 1;
					while (rs.next()) {
						stat2.setString(i, rs.getString("subjectnum"));
						i++;
					}

					stat2.registerOutParameter(7, OracleTypes.CURSOR);
					stat2.executeQuery();

					ResultSet rs2 = (ResultSet) stat2.getObject(7);

					while (rs2.next()) {
						teacherAble = true;
						System.out.printf("\t\t\t%s\t\t%s\n", rs2.getString("teachernum"),
								rs2.getString("teachername"));
					}
					if (!teacherAble) {
						System.out.println("\t\t\t강의 가능 교사가 없습니다.");
					}

					stat.close();
					stat2.close();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}

			// when period is 7
			else if (period.equals("7")) {
				try {
					sql = "{ call procPrintAvailableTeacer7(?,?,?,?,?,?,?,?)}";
					CallableStatement stat2 = conn.prepareCall(sql);

					int i = 1;
					while (rs.next()) {
						stat2.setString(i, rs.getString("subjectnum"));
						i++;
					}

					stat2.registerOutParameter(8, OracleTypes.CURSOR);
					stat2.executeQuery();

					ResultSet rs2 = (ResultSet) stat2.getObject(8);

					while (rs2.next()) {
						teacherAble = true;
						System.out.printf("\t\t\t%s\t\t%s\n", rs2.getString("teachernum"),
								rs2.getString("teachername"));
					}
					if (!teacherAble) {
						System.out.println("\t\t\t강의 가능 교사가 없습니다.");
					}

					stat.close();
					stat2.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private void enrollSubjectListToCourse(Connection conn, Scanner scan) {

		while (true) {

			// print list of allcourse
			// 전체과정 출력
			vwAllcourse(conn);
			System.out.println("0. 뒤로가기");

			// input courseNum
			System.out.println("과정번호: ");
			String courseNum = scan.nextLine();
			if (courseNum.equals("0")) {
				break;
			}

			// print list of subject
			// 전체 과목 출력
			viewSubjectList(conn);
			System.out.println("0. 뒤로가기");

			// input subjectNum
			System.out.println("과목번호: ");
			String subjectNum = scan.nextLine();
			if (subjectNum.equals("0")) {
				break;
			}

			System.out.println("순서: ");
			String sequence = scan.nextLine();
			if (sequence.equals("0")) {
				break;
			}

			System.out.println("기간: ");
			String period = scan.nextLine();
			if (period.equals("0")) {
				break;
			}

			// needMore : 과정에 대해 과목이 등록되어있는지를 보여줘야하나? 과목 등록여부 출력? 지금까지 입력한 정보 보여주기?

			// enroll input subject to input course
			// 전체 과정의 구성에 과목 추가
			try {

				String sql = "{ call procEnrollSubjectListToCourse(?,?,?,?)}";
				CallableStatement stat = conn.prepareCall(sql);

				// set
				stat.setString(1, courseNum);
				stat.setString(2, subjectNum);
				stat.setString(3, sequence);
				stat.setString(4, period);
				stat.executeUpdate();

				// fin message
				System.out.println("등록이 완료되었습니다.");
				stat.close();
				conn.close();
				break;

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	private void vwAllcourse(Connection conn) {
		try {

			// get subject list
			// 과목 리스트 가져오는 sql문
			Statement stat = conn.createStatement();
			String sql = "select * from vwallcourse";
			ResultSet rs = stat.executeQuery(sql);

			System.out.println("[과정번호]\t[과정명]\t\t[목표]\t[정원]\t[기간]");

			// print subject list
			// 과목 목록 출력
			while (rs.next()) {
				System.out.printf("%s\t%s\t\t%s\t%s\t%s\n", rs.getString("num"), rs.getString("name"),
						rs.getString("purpose"), rs.getString("capacity"), rs.getString("period"));

			}
			System.out.println("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void enrollNewSubject(Connection conn, Scanner scan) {

		// input subjectName, type
		while (true) {

			System.out.println("〓〓〓〓〓〓〓〓〓 신 규 등 록 〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("0. 뒤로가기");
			System.out.println("과목명: ");
			String subjectName = scan.nextLine();
			if (subjectName.equals("0")) {
				break;
			}
			System.out.println("구분: ");
			String essentialType = scan.nextLine();
			if (essentialType.equals("0")) {
				break;
			}
			System.out.println("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

			try {
				// call procedure procAddSubjectYes(subjectName, essentialType)
				System.out.println(subjectName);
				System.out.println(essentialType);
				String sql = "{ call procAddSubjectYes(?,?)}";
				CallableStatement stat = conn.prepareCall(sql);

				// set
				stat.setString(1, subjectName);
				stat.setString(2, essentialType);
				stat.executeUpdate();

				// fin message
				System.out.println("신규 등록이 완료되었습니다.");

				stat.close();
				conn.close();
				break;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void viewSubjectList(Connection conn) {
		try {

			// get subject list
			// 과목 리스트 가져오는 sql문
			Statement stat = conn.createStatement();
			String sql = "select * from VWSUBJECT";
			ResultSet rs = stat.executeQuery(sql);

			System.out.println("[과목번호]\t[과목명]\t\t\t[구분]");

			// print subject list
			// 과목 목록 출력
			while (rs.next()) {
				System.out.printf("%s\t%s\t\t\t%s\n", rs.getString("num"), rs.getString("name"),
						rs.getString("essentialtype"));

			}
			System.out.println("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public AdminSubject() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
}
