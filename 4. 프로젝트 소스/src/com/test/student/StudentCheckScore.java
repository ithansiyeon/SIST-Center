package com.test.student;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Scanner;

import com.test.admin.DBUtil;

import oracle.jdbc.OracleTypes;

/**
 * 교육생 성적 조회와 관련된 메소드들을 멤버로 갖는 클래스입니다.
 * @author Doyun Lee
 *
 */
public class StudentCheckScore {

	/**
	 * 교육생 성적 조회의 메뉴를 출력하는 메소드입니다.
	 * @param studentUser 로그인시 생성된 StudentUser를 StudentMain에서 매개변수로 받아오고, 또 세부 메뉴에 StudentUser를 매개변수로 넘겨줍니다.
	 */
	public void studentScoreMain(StudentUser studentUser) {

		Scanner scan = new Scanner(System.in);
		String sel = "";
		
		while(true) {
			
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t\t\t    성적 조회");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t1. 성적 조회");
			System.out.println("\t\t\t2. 시험 정보 조회");
			System.out.println("\t\t\t3. 시험 문제 확인");
			System.out.println("\t\t\t0. 뒤로가기");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			// 사용자에게 번호 입력받음
			System.out.print("\t\t\t▷입력: ");
			sel = scan.nextLine();
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println();
			
			
			// 성적 조회
			if(sel.equals("1")) {
				procScoreByStudent(studentUser);
			}
			// 시험 정보 조회
			else if(sel.equals("2")) {
				procTestInfoByStudent(studentUser);
			}
			// 시험 문제 확인
			else if(sel.equals("3")) {
				procQuestionBySubject(studentUser);
			}
			// 뒤로 가기
			else if(sel.equals("0")) {
				System.out.println("\t\t\t뒤로가기를 선택하셨습니다.");
				System.out.println("\t\t\t엔터를 입력하시면 이전 페이지로 돌아갑니다.");
				scan.nextLine();
				break;
			} 
			// 예외
			else {
				System.out.println("\t\t\t번호를 다시 입력해주세요.");
			}
		}//while
		
	}//studentScoreMain
	
	/**
	 * 로그인한 학생의 배점이 반영된 과목별 최종 시험점수와 상세 시험점수를 출력해주는 메소드입니다.
	 * @param studentUser 로그인시 생성된 StudentUser객체를 매개변수로 받아옵니다.
	 */
	public void procScoreByStudent(StudentUser studentUser) {
	//이도윤
		
		//로그인한 학생의 시험점수 출력
		//procScoreByStudent
		//procIntegratedScoreByStudent
		
		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		ResultSet rs = null;
		Scanner scan = new Scanner(System.in);
		
		try {
			
			//프로시저 호출 준비
			conn = util.open("211.63.89.64","project","java1234");
			String sql = "{ call procIntegratedScoreByStudent(?,?) }";
			
			stat = conn.prepareCall(sql);
			
			//매개변수 준비
			stat.setInt(1, studentUser.getNum());
			stat.registerOutParameter(2, OracleTypes.CURSOR);
			
			//프로시저 실행
			stat.executeQuery();
			
			//결과값 받아오기
			rs = (ResultSet)stat.getObject(2);
			
			//헤더 출력
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t\t\t    성적 조회");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println();
			System.out.println("\t\t\t[과목]\t\t\t[최종점수]");
			System.out.println("\t\t\t―――――――――――――――――――――――――――――――――――――――――――");
			
			//결과 출력
			while(rs.next()) {
				
				System.out.printf("\t\t\t%-14s\t\t%5s점\n", rs.getString("subjectName")
													, rs.getString("score"));		
			}
			
			System.out.println();
			System.out.println("\t\t\t상세 성적 조회를 시작합니다.");
			System.out.println("\t\t\t계속하시려면 엔터를 입력하세요.");
			scan.nextLine();
			
			//두번째 프로시저 실행 준비
			sql = "{ call procScoreByStudent(?,?) }";
			stat = conn.prepareCall(sql);
			
			//매개변수 설정
			stat.setInt(1, studentUser.getNum());
			stat.registerOutParameter(2, OracleTypes.CURSOR);
			
			//프로시저 실행
			stat.executeQuery();
			
			//결과값 받아오기
			rs = (ResultSet)stat.getObject(2);
			
			//헤더 출력
			System.out.println("\t\t\t[과목]\t\t      [필기]   [실기]   [출결]");
			System.out.println("\t\t\t―――――――――――――――――――――――――――――――――――――――――――");
			
			while(rs.next()) {
				
				System.out.printf("\t\t\t%-14s\t\t%3s점\t%3s점     %3s점\n", rs.getString("subjectName")
																, rs.getString("writtenTestScore")
																, rs.getString("performanceTestScore")
																, rs.getString("attendancescore"));
			}
			
			//DB연결 끊기
			stat.close();
			conn.close();
			
			System.out.println();
			System.out.println("\t\t\t성적 조회가 완료되었습니다.");
			System.out.println("\t\t\t엔터를 입력하시면 이전 페이지로 돌아갑니다.");
			scan.nextLine();	
			
		} catch (Exception e) {
			e.printStackTrace();		
		}
		
	}
	
	/**
	 * 로그인한 학생의 과목별 시험 정보(배점, 시험 날짜 등)을 출력하는 메소드입니다.
	 * @param studentUser 로그인시 생성된 StudentUser객체를 매개변수로 받아옵니다.
	 */
	public void procTestInfoByStudent(StudentUser studentUser) {		
	//이도윤
	
			//로그인한 학생의 시험정보 출력
			//procTestInfoByStudent
			//procSubjectInfoByStudent
			
			Connection conn = null;
			CallableStatement stat = null;
			DBUtil util = new DBUtil();
			ResultSet rs = null;
			Scanner scan = new Scanner(System.in);
			
			try {
				
				//프로시저 호출 준비
				conn = util.open("211.63.89.64","project","java1234");
				String sql = "{ call procTestInfoByStudent(?,?) }";
				
				stat = conn.prepareCall(sql);
				
				//매개변수 준비
				stat.setInt(1, studentUser.getNum());
				stat.registerOutParameter(2, OracleTypes.CURSOR);
				
				//프로시저 실행
				stat.executeQuery();
				
				//결과값 받아오기
				rs = (ResultSet)stat.getObject(2);
				
				//헤더 출력
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.println("\t\t\t\t\t시험 정보 조회");
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.println();
				System.out.println("\t\t[과목]\t\t\t[시험일]\t        [필기]   [실기]   [출결]");
				System.out.println("\t\t―――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――");
				
				//결과 출력
				while(rs.next()) {
					
					System.out.printf("\t\t%-14s\t\t%s\t%s\t%s\t%s\n", rs.getString("subjectName")
														, rs.getString("examDate")
														, (int)(rs.getDouble("writtenTestRatio") * 100) + "%"
														, (int)(rs.getDouble("performanceTestRatio") * 100) + "%"
														, (int)(rs.getDouble("attendanceRatio") * 100) + "%");		
				}
				
				//DB연결 끊기
				stat.close();
				conn.close();
				
				//완료 안내문 출력
				System.out.println();
				System.out.println("\t\t\t시험 정보 조회가 완료되었습니다.");
				System.out.println("\t\t\t엔터를 입력하시면 이전 페이지로 돌아갑니다.");
				scan.nextLine();	
				
			} catch (Exception e) {
				e.printStackTrace();		
			}
	}
	
	/**
	 * 로그인한 학생이 응시한 시험의 문제를 과목별로 출력하는 메소드입니다.
	 * @param studentUser 로그인시 생성된 StudentUser객체를 매개변수로 받아옵니다.
	 */
	public void procQuestionBySubject(StudentUser studentUser) {
	//이도윤
		
		//과목별 시험문제 출력
		//procSubjectInfoByStudent
		//procQuestionBySubject
		
		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		ResultSet rs = null;
		Scanner scan = new Scanner(System.in);
		
		try {
			
			//프로시저 호출 준비
			conn = util.open("211.63.89.64","project","java1234");

			//프로시저 실행 준비
			String sql = "{ call procSubjectInfoByStudent(?,?) }";
			stat = conn.prepareCall(sql);
			
			//매개변수 설정
			stat.setInt(1, studentUser.getNum());
			stat.registerOutParameter(2, OracleTypes.CURSOR);
			
			//프로시저 실행
			stat.executeQuery();
			
			//결과값 받아오기
			rs = (ResultSet)stat.getObject(2);
			

			//헤더 출력
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t\t\t시험 문제 조회");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println();
			System.out.println("\t\t[과목번호] [과목이름]\t[과목기간]\t\t [교재명]");
			System.out.println("\t\t―――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――");
			
			while(rs.next()) {
				
				System.out.printf("\t\t%3s\t%-14s\t%s\t %s\n", rs.getString("SubjectNum")
																, rs.getString("SubjectName")
																, rs.getString("period")
																, rs.getString("bookName"));
			}
			
			while (true) {
				
				System.out.println();
				System.out.println("\t\t\t시험 문제를 확인할 과목 번호를 입력하세요.(종료: 0)");
				System.out.print("\t\t\t▷입력: ");
				String snum = scan.nextLine();	
				System.out.println();
				
				if (snum.equals("0") ) {	//사용자가 0 입력하면 종료
				
					break;
				
				} else {
					
					sql = "{ call procQuestionBySubject(?,?,?) }";
					stat = conn.prepareCall(sql);
					
					//매개변수 설정
					stat.setInt(1, studentUser.getNum());
					stat.setString(2, snum);
					stat.registerOutParameter(3, OracleTypes.CURSOR);
					
					//프로시저 실행
					stat.executeQuery();
					
					//결과값 받아오기
					rs = (ResultSet)stat.getObject(3);
					
					while (rs.next()) {
						
						System.out.printf("\t\t문제: %s\n\t\t답: %s\n", rs.getString("question")
																			, rs.getString("answer"));
					}//while
					
				}//if
				
			}//while
			
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println();
			System.out.println("\t\t\t시험 문제 조회가 완료되었습니다.");
			System.out.println("\t\t\t엔터를 입력하시면 이전 페이지로 돌아갑니다.");
			scan.nextLine();
			
			//DB연결 끊기
			stat.close();
			conn.close();
			
			
		} catch (Exception e) {
			e.printStackTrace();		
		}	
		
	}
	
	
}
