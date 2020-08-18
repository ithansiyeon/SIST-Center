package com.test.teacher;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Scanner;

import com.test.admin.DBUtil;

import oracle.jdbc.OracleTypes;

/**
 * 강의 스케줄 조희를 위한 메소드입니다.
 * @author 희준
 *
 */
public class TeacherCheckSchedule {
	
	/**
	 * 해당 교사의 스케줄을 출력하는 메소드입니다.
	 * @param tuser 로그인 시 데이터를 입력받은 객체
	 */
	public void procCheckTeaching(TeacherUser tuser) {

		Scanner scan = new Scanner(System.in);

		Connection conn = null;
		CallableStatement stat = null;
		CallableStatement stat2 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		DBUtil util = new DBUtil();

		try {

			while (true) {

				conn = util.open("211.63.89.64", "project", "java1234");

				String sql = "{ call procCheckTeaching(?,?) }";
				stat = conn.prepareCall(sql);

				// 교사번호 get으로 가져 오기
				stat.setInt(1, tuser.getNum());

				stat.registerOutParameter(2, OracleTypes.CURSOR);

				stat.executeQuery();

				rs = (ResultSet) stat.getObject(2);

				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.println("\t\t\t\t\t강의 스케줄 조회");
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.println("[과정번호]\t[기간]\t\t\t[강의실] [정원] [상태] \t[과정명]");

				while (rs.next()) {

					System.out.printf("%4s\t\t%s ~ %s\t %3s   \t%2s      %s\t%-10s", rs.getString("coursenum"), rs.getString("startdate").substring(0, 10), rs.getString("enddate").substring(0, 10), 
			                  rs.getString("classroomnum"), rs.getString("stdNum"), rs.getString("status"), rs.getString("coursename")
					);

					sql = "{ call procCheckTeaching2(?,?)}";
					stat2 = conn.prepareCall(sql);

					System.out.println();
					
				}

				System.out.println();
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.println("\t\t\t\t과정번호를 입력해주세요.");
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.println("\t\t\t0. 뒤로가기");
				System.out.print("\t\t\t▷ 입력:");
				
				// 사용자 한테 입력 받는 값
				String cho = scan.nextLine();

				if (cho.equals("0")) {
					System.out.println();
					System.out.println("\t\t\t뒤로가기를 선택하셨습니다.");
					System.out.println("\t\t\t엔터를 입력하시면 이전 페이지로 돌아갑니다.");
					scan.nextLine();
					break;
				} else {
										
					// 사용자에게 과정 입력받고 과목 출력
					stat2.setString(1, cho);
					stat2.registerOutParameter(2, OracleTypes.CURSOR);
					stat2.executeQuery();
					rs2 = (ResultSet) stat2.getObject(2);
					
					System.out.println();
					System.out.println("\t\t\t[과목번호]\t[기간]\t\t\t [과정명]\t\t [교재명]");
					
					while (rs2.next()) {
						System.out.printf("\t\t\t%4s\t\t%s ~ %s\t %-10s \t  %s\n", rs2.getString("subjectnum"), rs2.getString("sjStardDate").substring(0, 10), rs2.getString("sjenddate").substring(0, 10),
								rs2.getString("subjectname"), rs2.getString("bookname"));
					}

					System.out.println();

					// 과목번호 입력시 해당 학생들 출력
					// cho 은 과정번호!!!!!
					procCheckStdInfo(cho); 

				} // else

			} // while

			rs.close();
			rs2.close();
			stat.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}// 강의 스케줄(끝) //procCheckTeaching(TeacherUser tuser)

//==============================================================================================================	

	/**
	 * 입력받은 과목번호에 수강중인 교육생 정보를 출력하는 메소드
	 * @param cho2 사용자가 입력한 교육생 번호
	 */
	// 과목번호 입력시 해당 학생들 출력
	private static void procCheckStdInfo(String cho2) {

		Scanner scan = new Scanner(System.in);

		Connection conn = null;
		CallableStatement stat = null;
		CallableStatement stat2 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		DBUtil util = new DBUtil();

		while (true) {

			try {
				conn = util.open("211.63.89.64", "project", "java1234");

				String sql = "{ call procCheckStdInfo(?,?) }";
				stat = conn.prepareCall(sql);

				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.println("\t\t\t학생 명단을 출력하려면 Enter키를 눌러주세요.");
				System.out.println("\t\t\t0. 뒤로가기");
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.print("\t\t\t▷ 입력:");
				
				// 사용자 한테 입력 받는 값
				String cho = scan.nextLine();

				if (cho.equals("0")) {
					System.out.println();
					System.out.println("\t\t\t뒤로가기를 선택하셨습니다.");
					System.out.println("\t\t\t엔터를 입력하시면 이전 페이지로 돌아갑니다.");
					scan.nextLine();
					break;
				} else {
					
					//cho2 는 과정번호 이다.!!!!
					stat.setString(1, cho2);
					stat.registerOutParameter(2, OracleTypes.CURSOR);

					stat.executeQuery();

					rs = (ResultSet) stat.getObject(2);

					System.out.println();
					System.out.println("\t\t\t[구분]\t[학생명]\t [전화번호]\t[최초 등록일]\t[수료구분]");

					int num = 1;
					
					while (rs.next()) {

						System.out.printf("\t\t\t%3d\t%5s\t %s\t%s\t%5s\n",num ,rs.getString("stdName"), rs.getString("stdtel"),
								rs.getString("stdRegitdate").substring(0, 10), rs.getString("status"));
						num++;
					}

				} // else
				rs.close();
				stat.close();
				conn.close();

				System.out.println();
				System.out.println("\t\t\t엔터를 입력하시면 이전 페이지로 돌아갑니다.");
				scan.nextLine();

				break;

			} catch (Exception e) {
				e.printStackTrace();
			}

		} // while

	}// 과목번호 입력시 해당 학생들 출력(끝) //procCheckStdInfo()

}// class