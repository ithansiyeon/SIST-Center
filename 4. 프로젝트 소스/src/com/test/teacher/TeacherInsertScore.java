package com.test.teacher;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Scanner;

import com.test.admin.DBUtil;

import oracle.jdbc.OracleTypes;

/**
 * 교사의 성적 입출력을 위한 클래스입니다.
 * @author leeho
 *
 */
public class TeacherInsertScore {
	int examPknum;
	Scanner scan = new Scanner(System.in);
	
	/**
	 * 성적 관리 메뉴를 출력하는 메소드입니다.
	 * @param teacherUser 로그인 시 데이터를 입력받은 객체
	 */
	public void insertScoreMenu(TeacherUser teacherUser) {
		
		while(true) {
			System.out.println("\t\t\t〓〓〓 성적 관리 〓〓〓〓");
			System.out.println("\t\t\t1. 목록 조회");
			System.out.println("\t\t\t2. 성적 입력");
			System.out.println("\t\t\t0. 뒤로가기");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.print("\t\t\t▷ 입력: \n");
			
			String sel = scan.nextLine();
			
			// print
			if(sel.equals("1")) {
				scorePrint(teacherUser);
			}
			// insert
			else if(sel.equals("2")) {
				insertScore(teacherUser);
			}
			//exit
			else if(sel.equals("0")) {
				break;
			}
		}
	}

	/**
	 * 성적을 입력하는 메소드입니다.
	 * @param teacherUser teacherUser 로그인 시 데이터를 입력받은 객체
	 */
	private void insertScore(TeacherUser teacherUser) {
		scorePrint(teacherUser);
		
		
		System.out.println("\n〓〓〓〓〓〓〓〓〓〓〓 성적 입력 〓〓〓〓〓〓〓〓〓");
		System.out.println("학생 번호:");
		String studentNum = scan.nextLine();
		System.out.println("필기:");
		String wScore = scan.nextLine();
		System.out.println("실기:");
		String pScore = scan.nextLine();
		System.out.println("출석:");
		String aScore = scan.nextLine();
		
		
		String sql = "{ call procInsertScoreToScorebyExam(?,?,?,?,?) }";
		Connection conn = new DBUtil().open("211.63.89.64", "project", "java1234");
		try {
			CallableStatement stat7 = conn .prepareCall(sql);
			stat7.setString(1,studentNum ); // studentnum
			stat7.setInt(2,examPknum ); 
			stat7.setString(3, wScore); // wScore
			stat7.setString(4, pScore); // pScore
			stat7.setString(5, aScore); // aScore
			stat7.executeUpdate();
			
			System.out.println("등록이 완료되었습니다.");
			
			stat7.close();
			conn.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * 현재 교사가 강의중인 과목들의 시험 관련 정보를 출력하는 메소드입니다.
	 * @param teacherUser 교사 객체
	 */
	public void scorePrint(TeacherUser teacherUser) {
		Connection conn = null;
		CallableStatement stat = null;
		CallableStatement stat2 = null;
		CallableStatement stat3 = null;
		CallableStatement stat4 = null;
		CallableStatement stat5 = null;

		CallableStatement tmp1stat = null;
		CallableStatement tmp2stat = null;

		ResultSet rs = null;
		ResultSet rs2 = null;
		ResultSet rs3 = null;
		ResultSet rs4 = null;
		ResultSet rs5 = null;

		ResultSet tmp1rs = null;
		ResultSet tmp2rs = null;

		DBUtil util = new DBUtil();

		// 변수 선언
		Scanner scan = new Scanner(System.in);
		int teachernum = teacherUser.getNum();
		String opencoursenum = "";
		try {
			conn = util.open("211.63.89.64", "project", "java1234");

			// 1. 교사가 강의중인 과정 정보 출력
			// procPrintOpencourseInfo
			String sql = "{ call procPrintOpencourseInfo(?,?) }";
			stat = conn.prepareCall(sql);

			// 교사 로그인 시 해당 교사의 교사번호를 넣는 변수
			stat.setInt(1, teachernum); // 교사번호
			stat.registerOutParameter(2, OracleTypes.CURSOR);
			stat.executeQuery();
			rs = (ResultSet) stat.getObject(2);

			if (rs.next()) {
				System.out.println("[과정명]\t[기간]\t[강의실번호]");
				System.out.printf("%s\t%s\t%s\t%s강의실\n",
						rs.getString("coursenum"),
						rs.getString("coursename"),
						rs.getString("startdate").substring(0,10) + " ~ " +
						rs.getString("enddate").substring(0,10),
						rs.getString("classroomnum")
						);
				opencoursenum = rs.getString("coursenum");
			}

			// 2. 해당 과정의 과목 정보를 출력
			// procPrintEndSubjectList

			sql = "{ call procPrintEndSubjectList(?,?) }";
			stat2 = conn.prepareCall(sql);
			stat2.setInt(1, teachernum); // 생성된 교사 객체의 번호
			stat2.registerOutParameter(2, OracleTypes.CURSOR);
			stat2.executeQuery();
			rs2 = (ResultSet) stat2.getObject(2);

			// # 성적 등록 여부를 확인
			// procPrintRegitScore
			sql = "{ call procPrintRegitScore(?,?) }";
			tmp1stat = conn.prepareCall(sql);
			tmp1stat.setString(1, opencoursenum);
			tmp1stat.registerOutParameter(2, OracleTypes.CURSOR);
			tmp1stat.executeQuery();
			tmp1rs = (ResultSet) tmp1stat.getObject(2);

			// 3. 교사는 자신이 강의를 마친 과목의 목록 보여주기
			System.out.println("[과목번호]\t[과목명]\t[기간]\t[교재명]\t[성적 등록 여부]\t[배점(필기/실기/출결)]");
			while (rs2.next()) {
				try {
					
					// 교사가 강의하는 시험범호의 순서를 넘김
					tmp1rs.next();

					// 해당 시험범호에 등록되어있는 학생의 수를 출력
					sql = "{ call procPrintRegitScore2(?,?) }";
					tmp2stat = conn.prepareCall(sql);
					tmp2stat.setString(1, tmp1rs.getString("examnum"));
					tmp2stat.registerOutParameter(2, OracleTypes.NUMBER);
					tmp2stat.executeQuery();
					String enrollstatus = tmp2stat.getInt(2) > 0 ? "등록" : "미등록";
					
					// 과목의 목록 보여주기
					System.out.printf("%s\t%s\t%s\t%s\t%s\t\t", 
							rs2.getString("subjectbyperiodnum"), 
							rs2.getString("subjectname"),
							rs2.getString("startdate").substring(0,10) + " ~ " + 
							rs2.getString("enddate").substring(0,10), 
							rs2.getString("bookname"),
							enrollstatus
					);
					
					;
					// 필기, 실기, 출결 배점 출력
					sql = "{ call procPrintRatioBySub(?,?,?) }";
					stat3 = conn.prepareCall(sql);	
					stat3.setString(1, opencoursenum);
					stat3.setString(2, rs2.getString("subjectbyperiodnum"));
					stat3.registerOutParameter(3, OracleTypes.CURSOR);
					stat3.executeQuery();
					
					rs3 = (ResultSet) stat3.getObject(3);

					while (rs3.next()) {
						System.out.printf("(%s, %s, %s)\n", 
								String.valueOf((int)(rs3.getDouble("wRatio")*100)) + "%", 
								String.valueOf((int)(rs3.getDouble("pRatio")*100)) + "%", 
								String.valueOf((int)(rs3.getDouble("aRatio")*100)) + "%");
					}
				} catch (Exception e) {
					e.printStackTrace();
					break;
				}
				
			}

			// 4. 특정 과목을 선택하면 교육생 정보가 출력되고

			// 사용자에게 특정 과목 선택 받음
			System.out.println("과목 번호:");
			String subjectNum = scan.nextLine();
			sql = "{ call procPrintEndSubjectList2(?,?) }";
			stat4 = conn.prepareCall(sql);
			stat4.setString(1, opencoursenum);
			stat4.registerOutParameter(2, OracleTypes.CURSOR);
			stat4.executeQuery();
			rs4 = (ResultSet) stat4.getObject(2);
			
			System.out.println("[교육생 정보]");
			System.out.println("[수강내역번호]\t[이름]\t[전화번호]\t[상태]\t[점수]");
			
			
			// examnum 추출
			sql = "{ call procReturnExamPK(?,?,?) }";
			stat5 = conn.prepareCall(sql);
			stat5.setString(1, subjectNum); // 사용자에게 입력받은 과목 번호가 들어가야함, subjectNum(212 line)
			stat5.setString(2, opencoursenum);
			stat5.registerOutParameter(3, OracleTypes.NUMBER);
			stat5.executeQuery();
			
			examPknum = stat5.getInt(3);
			
			// 교육생 정보, 성적 출력
			while (rs4.next()) {
				
				// 교육생 성적 출력
				sql = "{ call procPrintScore(?,?,?) }";
				stat4 = conn.prepareCall(sql);
				stat4.setString(1, rs4.getString("coursehistorynum"));
				stat4.setInt(2, examPknum);
				stat4.registerOutParameter(3, OracleTypes.CURSOR);
				stat4.executeQuery();
				rs5 = (ResultSet) stat4.getObject(3);
				
				// 점수를 담기 위한 변수 선언
				String score = "";
				String fail = "";
				// score 변수에 점수 할당
				while (rs5.next()) {
							score = String.format("(%s, %s, %s)", 
							rs5.getString("wscore"), 
							rs5.getString("pscore"),
							rs5.getString("ascore"));
	
				}
				
				// 중도탈락의 경우
				if(!(rs4.getString("enddate") == null)) {
					fail = String.format(" (%s)", rs4.getString("enddate").substring(1,10));
					
				}
				
				// 학생 정보 출력
				System.out.printf("%s\t%s\t%s\t%s\t%s\n", 
						rs4.getString("coursehistorynum"), 
						rs4.getString("studentname"),
						rs4.getString("tel"),
						rs4.getString("status") + fail,
						score);
				
		
			}

			// 출력 끝
			
			// 연결 종료
			stat.close();
			stat2.close();
			stat3.close();
			stat4.close();
			stat5.close();

			rs.close();
			rs2.close();
			rs3.close();
			rs4.close();
			rs5.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}
