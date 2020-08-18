
package com.test.teacher;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import com.test.admin.DBUtil;

import oracle.jdbc.OracleTypes;

/**
 * 교사의 상담 관리를 위한 클래스입니다.
 * @author leeho
 * 
 *
 */
public class TeacherManageConsult {

	/**
	 * 상담 관리 메인 메뉴를 출력하는 메소드입니다.
	 */
	public void manageConsultRq() {
		Scanner scan = new Scanner(System.in);
		while (true) {
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓 상담 관리 〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t1. 상담신청 내역 조회");
			System.out.println("\t\t\t2. 상담일지 입력");
			System.out.println("\t\t\t0. 뒤로가기");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t▷입력:");
			String sel = scan.nextLine();

			if (sel.equals("1")) {
				vwCheckConsultRequest();
			} else if (sel.equals("2")) {
				insertConsultContent();
			} else if (sel.equals("0")) {
				break;
			} else {
				System.out.println("\t\t\t번호를 다시 입력해주세요.");
			}
		}
	}

	/**
	 * 상담내용을 입력하는 메소드입니다.
	 */
	private void insertConsultContent() {

		// print list of uncompleted consult request
		vwCheckConsultRequest();
		Scanner scan = new Scanner(System.in);

		System.out.print("\t\t\t상담번호: ");
		String rqNum = scan.nextLine();
		System.out.print("\t\t\t상담날짜: ");
		String conusultDate = scan.nextLine();
		System.out.print("\t\t\t상담내용: ");
		String consultContent = scan.nextLine();

		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		try {

			String sql = "{call procAddConsultLog(?,?,?)}"; // address 테이블 결과셋이 들어 있어
			conn = util.open("211.63.89.64", "project", "java1234");
			stat = conn.prepareCall(sql);

			stat.setString(1, rqNum);
			stat.setString(2, conusultDate);
			stat.setString(3, consultContent);

			stat.executeUpdate();
			System.out.println("\t\t\t등록이 완료되었습니다.");
			stat.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * 현재까지 신청된 상담번호를 출력하는 메소드입니다.
	 */
	private void vwCheckConsultRequest() {

		// declare variable
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();

		try {

			conn = util.open("211.63.89.64", "project", "java1234");
			String sql = "select * from vwCheckconsultRequest";
			stat = conn.createStatement();
			rs = stat.executeQuery(sql);

			System.out.println("\t\t\t[상담요청번호]\t[신청날짜]\t[이름]\t[수강상태]");

			// print list of uncompleted consult request
			while (rs.next()) {
				System.out.printf("\t\t\t%s\t\t%s\t%s\t%s\n", rs.getString("consultReqNum"),
						rs.getString("rDate").substring(0, 10), rs.getString("studentname"), rs.getString("status"));
			}
			System.out.println("\n\t\t\t계속 하시려면 엔터를 입력해주세요.");
			new Scanner(System.in).nextLine();

			stat.close();
			conn.close();
		} catch (Exception e) {
			e.printStackTrace();

		}

	}

}
