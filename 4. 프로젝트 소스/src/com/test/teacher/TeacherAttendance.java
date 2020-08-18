package com.test.teacher;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import com.test.admin.DBUtil;
// 주석 달았음
import oracle.jdbc.OracleTypes;

/**
 * 선생님이 출결 관리 및 출결 조회를 할 수 있는 클래스입니다.
 * @author siyeon 
 * 
 */
public class TeacherAttendance {

	Scanner scan = new Scanner(System.in);
	/*
	public static void main(String[] args) {
		TeacherAttendance m = new TeacherAttendance();
		// TeacherUser t = new TeacherUser();
		m.menu(1);
	}
	 */
	/**
	 * 출결 관리 및 출결 조회 전체 메뉴입니다.
	 * 
	 * @param teacherUser 선생님 객체
	 */
	public void menu(TeacherUser teacherUser) { // TeacherUser teacherUser (int pteacherNum)
		boolean loop = true;
		while (loop) {
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓   M E N U 〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t          출결 관리 및 출결 조회");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t1. 출결 조회");
			System.out.println("\t\t\t2. 출결 현황(년,월,일) 조회");
			System.out.println("\t\t\t3. 특정(특정 과정, 특정 인원) 출결 현황 조회");
			System.out.println("\t\t\t4. 년,월 별로 출결 조회");
			System.out.println("\t\t\t0. 뒤로가기");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.print("\t\t\t▷입력:");

			String num = scan.nextLine();

			// teacherUser.getNum()
			// 선택 번호 입력 받음
			switch (num) {
			case "1":
				procteacherAttendance(teacherUser.getNum()); // pteacherNum
				break;
			case "2":
				procteacherAttendance1(teacherUser.getNum()); // pteacherNum
				break;
			case "3":
				procteacherAttendanceSelect(teacherUser.getNum());
				break;
			case "4":
				procteacherAttendanceStatus(teacherUser.getNum());
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
	 * 선생님이 강의한 과정에 한해 과정 번호를 입력하면 교육생의 출결을 조회하는 기능입니다.
	 * 
	 * @param teacherNum 선생님 번호
	 */
	public void procteacherAttendance(int teacherNum) {

		Connection conn = null;
		CallableStatement stat1 = null;
		Statement stat = null;
		DBUtil util = new DBUtil();
		String sql = null;
		ResultSet rs = null;

		try {
			conn = util.open("211.63.89.64","project","java1234");
			//conn = util.open("localhost", "project", "java1234");
			stat = conn.createStatement();
			sql = String.format("select * from vwteacherAttendance where teacherNum = %d", teacherNum);
			rs = stat.executeQuery(sql);

			// 개설 과정에 대한 정보를 보여줌
			System.out.println("\t\t\t[시행과정번호]\t[과정번호]\t[선생님이름]\t[선생님번호]\t[과목이름]");
			while (rs.next()) {
				System.out.printf("\t\t\t%s\t\t", rs.getString("openCourseNum"));
				System.out.printf("%s\t", rs.getString("allCourseNum"));
				System.out.printf("%s\t\t", rs.getString("teacher name"));
				System.out.printf("%s\t\t", rs.getString("teacherNum"));
				System.out.printf("%s", rs.getString("subject name"));
				System.out.println();

			}
			System.out.println();
			stat.close();
			
			// 개설 과정을 입력받아서 해당하는 교육생의 출결을 조회함

			System.out.print("\t\t\t▷과정 번호:");
			String num = scan.nextLine();
			
			sql = "{call procattendTeacherCourse1(?,?,?)}";
			stat1 = conn.prepareCall(sql);
			stat1.registerOutParameter(1, OracleTypes.CURSOR);
			stat1.setString(2, num);

			stat1.setInt(3, teacherNum);
			stat1.executeQuery();

			rs = (ResultSet) stat1.getObject(1);
			System.out.println();
			System.out.println(
					"[학생이름]\t[정상 갯수]\t\t[지각 갯수]\t\t[조퇴 갯수]\t\t[결석 갯수]\t\t[외출 갯수]\t\t[병가 갯수]\t\t[기타]\t\t[과정번호]\t\t[과정 상태]");
			int count = 0;
			while (rs.next()) {
				System.out.printf("%s", rs.getString("studentName"));
				System.out.printf("\t%s", rs.getString("normal"));
				System.out.printf("\t\t%s", rs.getString("late"));
				System.out.printf("\t\t%s", rs.getString("early"));
				System.out.printf("\t\t%s", rs.getString("absent"));
				System.out.printf("\t\t%s", rs.getString("outing"));
				System.out.printf("\t\t%s", rs.getString("sickLeave"));
				System.out.printf("\t\t%s", rs.getString("other"));
				System.out.printf("\t\t%s", rs.getString("allCourseNum"));
				System.out.printf("\t\t%s", rs.getString("courseStatus"));
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

			conn.close();
			stat1.close();
			rs.close();
			pause1();
		} catch (Exception e) {
			System.out.println("\t\t\tteacherAttendacne.procteacherAttendance()");
			System.out.println("\t\t\t교육생 출결 조회에 실패했습니다.");
			e.printStackTrace();
		}

	} // procteacherAttendance()

	/**
	 * 선생님이 출결 현황을 기간별(년,월,일)로 조회하는 기능입니다.
	 * 
	 * @param teacherNum 선생님 번호
	 */

	public void procteacherAttendance1(int teacherNum) {
		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();

		String sql = null;

		// *** 선생님 출결 현황을 기간별(년, 월, 일) 조회 ***
		boolean loop = true;
		while (loop) {
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t1. 년 조회");
			System.out.println("\t\t\t2. 월 조회");
			System.out.println("\t\t\t3. 일 조회");
			System.out.println("\t\t\t0. 뒤로가기");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.print("\t\t\t▷입력: ");
			String num = scan.nextLine();
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			try {
				conn = util.open("211.63.89.64","project","java1234");
				//conn = util.open("localhost", "project", "java1234");
				switch (num) {
				// 년 조회
				case "1":

					// 년을 입력 받음
					System.out.print("\t\t\t▷년(yyyy):");
					String year = scan.nextLine();

					int count = 0;
					
					sql = "{call procattendStatusYear(?,?,?)}";
					stat = conn.prepareCall(sql);
					stat.registerOutParameter(1, OracleTypes.CURSOR);
					stat.setString(2, year);

					stat.setInt(3, teacherNum);
					stat.executeQuery();

					rs = (ResultSet) stat.getObject(1);
					System.out.println();

					System.out.println(
							"[학생이름]\t[정상 갯수]\t\t[지각 갯수]\t\t[조퇴 갯수]\t\t[결석 갯수]\t\t[외출 갯수]\t\t[병가 갯수]\t\t[기타]\t\t[과정번호]\t\t[과정 상태]");
					count = 0;
					while (rs.next()) {
						System.out.printf("%s", rs.getString("studentName"));
						System.out.printf("\t%s", rs.getString("normal"));
						System.out.printf("\t\t%s", rs.getString("late"));
						System.out.printf("\t\t%s", rs.getString("early"));
						System.out.printf("\t\t%s", rs.getString("absent"));
						System.out.printf("\t\t%s", rs.getString("outing"));
						System.out.printf("\t\t%s", rs.getString("sickLeave"));
						System.out.printf("\t\t%s", rs.getString("other"));
						System.out.printf("\t\t%s", rs.getString("allCourseNum"));
						System.out.printf("\t\t%s", rs.getString("courseStatus"));
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
					pause1();
					break;
				// 월 조회
				case "2":

					// 월을 입력 받음
					System.out.print("\t\t\t▷월(mm):");
					String month = scan.nextLine();

					count = 0;
					
					sql = "{call procattendStatusMonth(?,?,?)}";
					stat = conn.prepareCall(sql);
					stat.registerOutParameter(1, OracleTypes.CURSOR);
					stat.setString(2, month);

					stat.setInt(3, teacherNum);
					stat.executeQuery();

					rs = (ResultSet) stat.getObject(1);
					System.out.println();
					System.out.println(
							"[학생이름]\t[정상 갯수]\t\t[지각 갯수]\t\t[조퇴 갯수]\t\t[결석 갯수]\t\t[외출 갯수]\t\t[병가 갯수]\t\t[기타]\t\t[과정번호]\t\t[과정 상태]");
					count = 0;
					while (rs.next()) {
						System.out.printf("%s", rs.getString("studentName"));
						System.out.printf("\t%s", rs.getString("normal"));
						System.out.printf("\t\t%s", rs.getString("late"));
						System.out.printf("\t\t%s", rs.getString("early"));
						System.out.printf("\t\t%s", rs.getString("absent"));
						System.out.printf("\t\t%s", rs.getString("outing"));
						System.out.printf("\t\t%s", rs.getString("sickLeave"));
						System.out.printf("\t\t%s", rs.getString("other"));
						System.out.printf("\t\t%s", rs.getString("allcourseNum"));
						System.out.printf("\t\t%s", rs.getString("courseStatus"));
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
					pause1();
					break;
				// 일 조회
				case "3":
		
					// 일을 입력 받음
					System.out.print("\t\t\t▷일(dd):");
					String day = scan.nextLine();

					count = 0;
					
					sql = "{call procattendStatusDay(?,?,?)}";
					stat = conn.prepareCall(sql);
					stat.registerOutParameter(1, OracleTypes.CURSOR);
					stat.setString(2, day);

					stat.setInt(3, teacherNum);
					stat.executeQuery();

					rs = (ResultSet) stat.getObject(1);
					System.out.println();

					System.out.println(
							"[학생이름]\t[정상 갯수]\t\t[지각 갯수]\t\t[조퇴 갯수]\t\t[결석 갯수]\t\t[외출 갯수]\t\t[병가 갯수]\t\t[기타]\t\t[과정번호]\t\t[과정 상태]");
					count = 0;
					while (rs.next()) {
						System.out.printf("%s", rs.getString("studentName"));
						System.out.printf("\t%s", rs.getString("normal"));
						System.out.printf("\t\t%s", rs.getString("late"));
						System.out.printf("\t\t%s", rs.getString("early"));
						System.out.printf("\t\t%s", rs.getString("absent"));
						System.out.printf("\t\t%s", rs.getString("outing"));
						System.out.printf("\t\t%s", rs.getString("sickLeave"));
						System.out.printf("\t\t%s", rs.getString("other"));
						System.out.printf("\t\t%s", rs.getString("allCourseNum"));
						System.out.printf("\t\t%s", rs.getString("courseStatus"));
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
					pause1();
					break;
				case "0":
					loop = false;
					conn.close();
					rs.close();
					break;
				default:
					System.out.println("\t\t\t번호를 다시 입력해주세요.");
				}

				System.out.println("\t\t\t완료");
				stat.close();
				conn.close();
				rs.close();
			} catch (Exception e) {
				System.out.println("\t\t\tteacherAttendance.procteacherAttendance1()");
				System.out.println("\t\t\t출결 현황을 날짜별(년,월,일) 조회에 실패했습니다.");
				e.printStackTrace();
			}
		}

	} // procteacherAttendance1()

	/**
	 * 100개를 출력 후 계속 출력 하려면 엔터를 눌러야지 계속 할 수 있는 기능입니다.
	 */

	public void pause() {
		System.out.println("\t\t\t100개를 계속 출력하실려면 엔터를 누르세요...");
		scan.nextLine();
	} // pause()

	/**
	 * 선생님이 특정과정, 특정인원의 출결 현황을 조회하는 기능입니다.
	 * 
	 * @param pteacherNum 선생님 번호
	 */
	public void procteacherAttendanceSelect(int pteacherNum) {

		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		Statement stat1 = null;
		DBUtil util = new DBUtil();

		String sql = null;
		
		try {
			conn = util.open("211.63.89.64","project","java1234");
			//conn = util.open("localhost", "project", "java1234");
			sql = String.format("select * from vwcourseHistory where teacherNum = %d", pteacherNum);
			stat1 = conn.createStatement();
			rs = stat1.executeQuery(sql);

			System.out.println("\t\t\t[학생이름]\t\t[시행과정번호]\t[학생번호]\t\t[과정 번호]");
			// 학생 이름, 시행 과정 번호, 학생 번호, 과정 번호를 보여줌
			while (rs.next()) {
				System.out.printf("\t\t\t%s\t\t", rs.getString("student name"));
				System.out.printf("%s\t\t", rs.getString("openCourseNum"));
				System.out.printf("%s\t\t", rs.getString("student num"));
				System.out.printf("%s", rs.getString("allCourseNum"));
				System.out.println();
			}

			System.out.print("\t\t\t▷개설 강좌 번호(과정 번호):");
			String num = scan.nextLine();
			System.out.print("\t\t\t▷학생 번호:");
			String name = scan.nextLine();
			
			sql = "{call procattendStatusCourse(?,?,?,?)}";
			stat = conn.prepareCall(sql);
			stat.registerOutParameter(1, OracleTypes.CURSOR);
			stat.setString(2, num);
			stat.setString(3, name);
			stat.setInt(4, pteacherNum);

			stat.executeQuery();

			rs = (ResultSet) stat.getObject(1);
			System.out.println();
			System.out.println(
					"[학생이름]\t[정상 갯수]\t\t[지각 갯수]\t\t[조퇴 갯수]\t\t[결석 갯수]\t\t[외출 갯수]\t\t[병가 갯수]\t\t[기타]\t[시행과정번호]\t\t[과정 상태]");
			int count = 0;
			while (rs.next()) {
				System.out.printf("%s", rs.getString("studentName"));
				System.out.printf("\t%s", rs.getString("normal"));
				System.out.printf("\t\t%s", rs.getString("late"));
				System.out.printf("\t\t%s", rs.getString("early"));
				System.out.printf("\t\t%s", rs.getString("absent"));
				System.out.printf("\t\t%s", rs.getString("outing"));
				System.out.printf("\t\t%s", rs.getString("sickLeave"));
				System.out.printf("\t\t%s", rs.getString("other"));
				System.out.printf("\t\t%s", rs.getString("openCourseNum"));
				System.out.printf("\t\t%s", rs.getString("courseStatus"));

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

		} catch (Exception e) {
			System.out.println("\t\t\tteacherAttendance.procteacherAttendanceSelect");
			System.out.println("\t\t\t특정과정, 특정 인원 조회에 실패했습니다.");
			e.printStackTrace();
		}

	} // procteacherAttendance1()

	/**
	 * 선생님이 년,월 을 입력 한 후 근태 상황을 구분하여 출결조회를 할 수 있습니다.
	 * 
	 * @param teacherNum 선생님 번호
	 */

	public void procteacherAttendanceStatus(int teacherNum) {

		Connection conn = null;
		ResultSet rs = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();

		String sql = null;
		// *** 선생님이 출결 조회 ***
		try {
			conn = util.open("211.63.89.64","project","java1234");
			//conn = util.open("localhost", "project", "java1234");
			sql = "{call procteacherAttendanceStatus(?,?,?,?)}";
			stat = conn.prepareCall(sql);
			stat.registerOutParameter(1, OracleTypes.CURSOR);
			System.out.print("\t\t\t▷년(yyyy):");
			String year = scan.nextLine();
			System.out.print("\t\t\t▷월(mm):");
			String month = scan.nextLine();

			stat.setString(2, year);
			stat.setString(3, month);
			stat.setInt(4, teacherNum);

			stat.executeQuery();
			rs = (ResultSet) stat.getObject(1);
			int count = 0;
			System.out.println();
			
			sql = "{call procattendStatusYearMonth(?,?,?,?)}";
			stat = conn.prepareCall(sql);
			stat.registerOutParameter(1, OracleTypes.CURSOR);
			stat.setString(2, year);
			stat.setString(3, month);

			stat.setInt(4, teacherNum);
			stat.executeQuery();

			rs = (ResultSet) stat.getObject(1);
			System.out.println();
			System.out.println(
					"[학생이름]\t[정상 갯수]\t\t[지각 갯수]\t\t[조퇴 갯수]\t\t[결석 갯수]\t\t[외출 갯수]\t\t[병가 갯수]\t\t[기타]\t\t[시행과정번호]\t[과정 상태]");
			count = 0;
			while (rs.next()) {
				System.out.printf("%s", rs.getString("studentName"));
				System.out.printf("\t%s", rs.getString("normal"));
				System.out.printf("\t\t%s", rs.getString("late"));
				System.out.printf("\t\t%s", rs.getString("early"));
				System.out.printf("\t\t%s", rs.getString("absent"));
				System.out.printf("\t\t%s", rs.getString("outing"));
				System.out.printf("\t\t%s", rs.getString("sickLeave"));
				System.out.printf("\t\t%s", rs.getString("other"));
				System.out.printf("\t\t%s", rs.getString("allCourseNum"));
				System.out.printf("\t\t%s", rs.getString("courseStatus"));
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

		} catch (Exception e) {
			System.out.println("\t\t\tteacherAttendance.procteacherAttendanceStatus()");
			System.out.println("\t\t\t년,월을 입력한 후 근태 상황을 구분하여 출결 조회에 실패했습니다.");
			e.printStackTrace();
		}

	}

	/**
	 * 계속할려면 엔터를 눌러야 하는 기능입니다.
	 */
	public void pause1() {
		System.out.println("\t\t\t계속 하실려면 엔터를 누르세요...");
		scan.nextLine();
	} //pause1()
} // procteacherAttendanceStatus()