package com.test.student;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import com.test.admin.DBUtil;

import oracle.jdbc.OracleTypes;


/**
 * 교사평가 클래스입니다.
 * @author 희준
 *
 */
public class Studentrating {
	
	/**
	 * 수료상태를 출력하는 메소드
	 * @param suser 교육생 객체
	 * @return  학생의 수료상태
	 */
	public String proCouresStatusFinish(StudentUser suser) {
	
		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		ResultSet rs = null;
		Scanner scan = new Scanner(System.in);

		try {
			
			//프로시저 호출 준비
			String sql = "{ call proCouresStatusFinish(?,?) }";
			conn = util.open("211.63.89.64", "project", "java1234");
			stat = conn.prepareCall(sql);
			
			//프로시저 매개변수 설정
			stat.setInt(1, suser.getNum());
			stat.registerOutParameter(2, OracleTypes.CURSOR); //성공여부 받아올 매개변수 설정
			
			//프로시저 실행
			stat.executeQuery();
			
			rs = (ResultSet) stat.getObject(2);
			
			//수료상태를 받기위해  
			String status = "";

			while (rs.next()) {
				
				// 학생 수료상태 받음.
				status = rs.getString("status"); 
			}

			rs.close();
			stat.close();
			conn.close();

			// 학생 수료상태  반환 -> procAddSocreByRating()에서 쓰인다.
			return status; 

		} catch (Exception e) {
			System.out.println("실패");
		}

		return "반환실패";

	}// 수강내역을 넣어 수료상태 결과(끝) 

	/**
	 * 수료한 학생이 교사평가를 하는 메소드
	 * @param suser 교육생 객체
	 */
	public void procAddSocreByRating(StudentUser suser) {

		// proCouresStatusFinish(suser) 반환값[학생 수료여부]을 result에 담는다.
		String result = proCouresStatusFinish(suser);

		Scanner scan = new Scanner(System.in);

		// result로 수료상태 여부 확인 하는 if문
		if (result.equals("수료")) {
			
			//수료 상태일 경우 실행
			while (true) {

				Connection conn = null;
				CallableStatement stat = null;
				DBUtil util = new DBUtil();

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++		
// 수강내역 번호, 과정명, 교사이름 출력		

				Connection conn2 = null;
				Statement stat2 = null;
				ResultSet rs2 = null;
				DBUtil util2 = new DBUtil();
				String courseHistorynum = "";
				String openCourseName = "";
				String teacherName = "";

				try {

					conn2 = util2.open("211.63.89.64", "project", "java1234");
					stat2 = conn2.createStatement();
					
					//수료한 학생이 들은 과정의 수강내역번호, 과정명, 교사이름을 출력 
					String sql2 = String.format(
							"select ch.num chnum, ac.name acname, t.name tname from tblcoursehistory ch inner join tblopenCourse oc on oc.num = ch.opencoursenum inner join tblteacher t on t.num = oc.teachernum inner join tblallCourse ac on ac.num = oc.allCourseNum where studentnum = %d",
							suser.getNum());

					rs2 = stat2.executeQuery(sql2);

					if (rs2.next()) {
						// 수강내역번호
						courseHistorynum = rs2.getString("chnum");
						// 과정명
						openCourseName = rs2.getString("acname");
						// 교사이름
						teacherName = rs2.getString("tname");

					}

					stat2.close();
					conn2.close();

				} catch (Exception e) {
					e.printStackTrace();
				}

//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++		

				// tblsocreByRating 작성 시작
				try {

					//교사평가  프로시저 호출 준비
					conn = util.open("211.63.89.64", "project", "java1234");
					String sql = "{ call procAddSocreByRating(?,?,?,?,?,?,?) }";

					stat = conn.prepareCall(sql);

					// 수강내역 번호를 -> 테이블 tblrating 입력 (tblsocreByRating 작성시 tblrating이 자동으로 생성)
					stat.setString(1, courseHistorynum);

					//수료한 학생이 들은 과정명과 교사명 출력 
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.printf("\t\t\t[과정명]: %s\n", openCourseName);
					System.out.printf("\t\t\t[교사명]: %s\n", teacherName);
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

					// 항목별 점수 입력 -> tblScoreByRating 에 입력
					// 1.항목
					System.out.println("\t\t\t[항목1]. 강의는 계획적이고 체계적이었다.");
					System.out.print("\t\t\t점수: ");
					String num1 = scan.nextLine();
					stat.setString(2, num1);

					// 2.항목
					System.out.println("\t\t\t[항목2]. 필요한 지식과 경험을 갖추고 있었다.");
					System.out.print("\t\t\t점수: ");
					String num2 = scan.nextLine();
					stat.setString(3, num2);

					// 3.항목
					System.out.println("\t\t\t[항목3]. 강의의 전체적인 난이도는 적절하였다.");
					System.out.print("\t\t\t점수: ");
					String num3 = scan.nextLine();
					stat.setString(4, num3);

					// 4.항목
					System.out.println("\t\t\t[항목4]. 학생들과의 상호작용에 적극적이었다.");
					System.out.print("\t\t\t점수: ");
					String num4 = scan.nextLine();
					stat.setString(5, num4);

					// 5.항목
					System.out.println("\t\t\t[항목5]. 부과된 과제는 학습에 도움이 되었다.");
					System.out.print("\t\t\t점수: ");
					String num5 = scan.nextLine();
					stat.setString(6, num5);

					// 6.항목
					System.out.println("\t\t\t[항목6]. 해당 분야의 지식과 능력이 발전하였다.");
					System.out.print("\t\t\t점수: ");
					String num6 = scan.nextLine();
					stat.setString(7, num6);

					stat.executeUpdate();

					stat.close();
					conn.close();

					System.out.println();
					System.out.println("\t\t\t평가를 완료했습니다.");
					System.out.println("\t\t\t엔터를 입력하시면 이전 페이지로 돌아갑니다.");
					scan.nextLine();
					break;

				} catch (Exception e) {
					System.out.println("\t\t\t평가를 실패했습니다.");
					System.out.println(e);
				}

			} // while

		} else {
			
			//수료상태가 아니면 교사 평가 불가능 
			System.out.println("\t\t\t수료상태가 아닙니다.");
			System.out.println("\t\t\t엔터를 입력하시면 이전 페이지로 돌아갑니다.");
			scan.nextLine();

		} // else

	}// 교육생 교사평가 항목별 점수 등록(끝)

}
