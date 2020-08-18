package com.test.admin;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import oracle.jdbc.OracleTypes;

/**
 * 시험관련 메소드를 멤버로 갖는 클래스입니다.
 * @author Team 2
 *
 */
public class AdminExam {
	
	/**
	 *  관리자 - 시험관리 및 성적 조회 관련 메소드를 멤버로 갖는 클래스입니다. 
	 * 
	 */
	public void manageExamMenu() {
		
		/**
		 *  관리자 - 시험관리 및 성적 조회 메뉴 출력
		 */
		Scanner scan = new Scanner(System.in);
		
		
		while (true) {
			
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t\t    시험 관리 및 성적 조회");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
	
			System.out.println("\t\t\t1. 시험 성적 등록 내역 조회");
			System.out.println("\t\t\t2. 시험 문제 등록 내역 조회");
			System.out.println("\t\t\t3. 개설 과목별 성적 조회");
			System.out.println("\t\t\t4. 교육생 개인별 성적 조회");
			System.out.println("\t\t\t0. 뒤로가기");
	
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.print("\t\t\t▷ 입력:");
			String mnum = scan.nextLine();
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println();
		
			
			if (mnum.equals("1")) {
				checkScoreRegister();
				//시험 성적 등록 내역 조회
				
			} else if (mnum.equals("2")) {
				checkQuestionRegister();
				//시험 문제 등록 내역 조회
				
			} else if (mnum.equals("3")) {
				
				procScoreInfoBySubject();
				//개설 과목별 성적 조회
					
			} else if (mnum.equals("4")) {
				
				procScoreInfoByStudent();
				//교육생 개인별 성적 조회
				
			} else if (mnum.equals("0")) {
				//뒤로가기
				System.out.println("\t\t\t뒤로가기를 선택하셨습니다.");
				System.out.println("\t\t\t엔터를 입력하시면 이전 페이지로 돌아갑니다.");
				scan.nextLine();
				break;
				
			} else {
				// 메인메뉴 유효성 검사
				System.out.println();
				System.out.println("\t\t\t잘못 입력하셨습니다. ");
				System.out.println("\t\t\t1에서 4 사이의 숫자를 입력해주세요.");
				System.out.println();
			}
		}//while

	}//mainMenu()
	
	
	/**
	 * 과목별 시험문제의 등록 여부를 출력하는 메소드입니다.
	 */
	private void checkQuestionRegister() {
		
		Connection conn = new DBUtil().open("211.63.89.64", "project", "java1234");
	    Scanner scan = new Scanner(System.in);
	    Statement stat = null;
	    ResultSet rs = null;
	    try {
	    	
	    	// 과목 목록 출력
	    	stat = conn.createStatement();
	    	String sql = "select * from tblsubject where deletestatus = 0";
	    	rs = stat.executeQuery(sql);
	    	
	    	System.out.println("\t\t\t[과목번호] [구분]\t[과목명]");
	    	while(rs.next()) {
	    		System.out.printf("\t\t\t%s\t  %s\t%-15s\n",
	    				rs.getString("num"),
	    				rs.getString("essentialtype"),
	    				rs.getString("name"));
	    		
	    	}
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
	    	// 입력받기
	    	System.out.print("\t\t\t과목 번호: ");
	    	String subjectNum = scan.nextLine();
	    	
	    	
	    	//시험문제 등록 여부
	    	sql = "{ call procCheckQuestion(?,?)}";
            CallableStatement stat2 = conn.prepareCall(sql);
            stat2.setString(1, subjectNum);
            stat2.registerOutParameter(2, OracleTypes.NUMBER);
            stat2.executeQuery();
            
            String enrollstatus = stat2.getInt(2) > 0 ? "등록" : "미등록";
            System.out.printf("\t\t\t선택하신 과목은 '%s' 상태입니다.\n",enrollstatus);
            
            
            
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	/**
	 * 과목별 성적 등록 여부를 출력하는 메소드입니다.
	 */
	private void checkScoreRegister() {


	      Connection conn = new DBUtil().open("211.63.89.64", "project", "java1234");
	      Scanner scan = new Scanner(System.in);

	      // print list of open course
	      try {

	         // get subject list
	         // 과목 리스트 가져오는 sql문
	         Statement stat = conn.createStatement();
	         String sql = "select * from vwopencourseinfo2";
	         ResultSet rs = stat.executeQuery(sql);

	         System.out.println("\t\t\t[과정번호]\t\t[과정명]\t\t\t\t[기간]\t\t[강의실]");

	         // print subject list
	         // 과목 목록 출력
	         while (rs.next()) {
	            System.out.printf("\t\t\t%s\t%s\t\t\t\t%s\t\t%s\n", rs.getString("opencoursenum"),
	                  rs.getString("opencoursename"), rs.getString("period"), rs.getString("classroomname"));
	         }
	         System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

	         System.out.println("\t\t\t과정번호 :");
	         String opencoursenum = scan.nextLine();

	         try {

	            // declare variable
	            sql = "{ call procPrintSjInfoPeriodBySubject(?,?,?)}";
	            CallableStatement stat2 = conn.prepareCall(sql);
	            ResultSet rs2 = null;
	            ResultSet rs3 = null;

	            // set
	            stat2.setString(1, opencoursenum);
	            stat2.registerOutParameter(2, OracleTypes.CURSOR);
	            stat2.registerOutParameter(3, OracleTypes.CURSOR);
	            stat2.executeUpdate();

	            // get result set
	            rs2 = (ResultSet) stat2.getObject(2);
	            rs3 = (ResultSet) stat2.getObject(3);

	            // print rs
	            System.out.println("\t\t\t[과정명]\t[기간]\t[강의실명]\t[교사명]");
	            while (rs2.next()) {
	               System.out.printf("\t\t\t%s\t%s\t%s\t%s\n", 
	                     rs2.getString("ocName"), 
	                     rs2.getString("startdate").substring(0,10) + " ~ " +
	                     rs2.getString("enddate").substring(0,10), 
	                     rs2.getString("classroomnum") + "강의실", 
	                     rs2.getString("teachername"));
	            }

	            // print rs2
	            System.out.println("\t\t\t[구성 과목 리스트]");
	            System.out.println("\t\t\t[과목명]\t[기간]\t[교재명]\t");
	            
	            while (rs3.next()) {
	               System.out.printf("\t\t\t%s\t%s\t%s\t%s\n", 
	                     rs3.getString("subjectnum"),
	                     rs3.getString("subjectname"),
	                     rs3.getString("startdate").substring(0,10) + " ~ " +
	                     rs3.getString("enddate").substring(0,10), 
	                     rs3.getString("bookname"));
	            }
	            System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

	            
	            // input periodBySubject number
	            System.out.println("\t\t\t과목 번호: ");
	            String pbsNum = scan.nextLine();
	            sql = "{ call procCheckEnrollScore(?,?,?)}";
	            CallableStatement stat3 = conn.prepareCall(sql);
	            stat3.setString(1, pbsNum);
	            stat3.setString(2, opencoursenum);
	            stat3.registerOutParameter(3, OracleTypes.NUMBER);
	            stat3.executeQuery();
	            
	            // get the number of enrolled score
	            int checkCnt = stat3.getInt(3);
	            
	            // print whether score was enrolled
	            System.out.println("\t\t\t[등록여부] : ");
	            System.out.printf("\t\t\t 해당 과목에는 %d명의 성적이 등록되어있습니다.\n", checkCnt);
	         } catch (Exception e) {
	            // TODO: handle exception
	         }
	      } catch (Exception e) {
	         // TODO: handle exception
	      }

	}

	//3. 개설 과목별 성적 조회
	
	/**
	 * 해당 과목이 포함된 과정 리스트에서 과정 번호를 입력받아 수강한 교육생들의 성적을 출력합니다.
	 */
	private static void procScoreInfoBySubject() {
		
		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);
		String snum = procScoreBasicInfoBySubject();
		
		try {

			conn = util.open("211.63.89.64", "project" ,"java1234");
			String sql = "{ call procScoreInfoBySubject(?,?,?)";
			
			stat = conn.prepareCall(sql);
			
			System.out.println();
			System.out.println("\t\t\t상세 내역을 조회할 과정 번호를 입력하세요");
			

			String ocnum = "";
			System.out.print("\t\t\t▷ 과정 번호 입력 : "); //과정번호입력
			ocnum = scan.nextLine();
			System.out.println();
			
			stat.setString(1, snum);
			stat.setString(2, ocnum);
			stat.registerOutParameter(3, OracleTypes.CURSOR);
			stat.executeQuery();
			
			rs = (ResultSet)stat.getObject(3);
			
			
			System.out.println();
			System.out.println("\t       [개설과목명]    [교육생 이름]  [주민번호]  [필기점수] [실기점수]");
			System.out.println("\t       ――――――――――――――――――――――――――――――――――――――――――――――――――――");
			
			while (rs.next()) {
				System.out.printf("\t\t%-15s%10s%16s%8s%8s\n"
						, rs.getString("subjectname")
						, rs.getString("studentname")
						, rs.getString("studentssn")
						, rs.getString("writtentestscore")
						, rs.getString("performancetestscore"));
			}
			
			stat.close();
			conn.close();
						

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		//뒤로가기 전 pause
		System.out.println();
		System.out.println("\t\t\t엔터를 입력하시면 이전 페이지로 돌아갑니다.");
		scan.nextLine();
		
		
	}//procScoreInfoBySubject()


	/**
	 * 과목 번호를 입력받아 개설 과목별 성적 조회 전 해당 과목이 포함된 과정 리스트를 출력합니다.
	 * @return 과목 번호
	 */
	private static String procScoreBasicInfoBySubject() {
		
		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);
		
		String snum = "";

		try {

			conn = util.open("211.63.89.64", "project" ,"java1234");
			String sql = "{ call procScoreBasicInfoBySubject(?,?)";
			
			stat = conn.prepareCall(sql);

			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t\t           개설 과목별 성적 조회 ");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println();
			

			System.out.print("\t\t\t▷ 과목 번호 입력 : "); //과목번호입력
			snum = scan.nextLine();			
			System.out.println();
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

			stat.setString(1, snum);
			
			stat.registerOutParameter(2, OracleTypes.CURSOR);
			stat.executeQuery();
			
			rs = (ResultSet)stat.getObject(2);
			
			
			System.out.println();
			System.out.println("[과정번호]    [과목명]     [과정시작일]       [과정종료일]       [강의실명]   [교사명]   [교재명]       [개설과정명] ");
			System.out.println("―――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――");
			
			while (rs.next()) {
				System.out.printf("%-8s    %-16s%-15s%-15s%7s\t       %5s     %-12s     %-30s\n"
						, rs.getString("opencoursenum")		
						, rs.getString("subjectname")
						, rs.getString("startdate")
						, rs.getString("enddate")
						, rs.getString("classromname") + "강의실"
						, rs.getString("teachername")
						, rs.getString("bookname")
						, rs.getString("coursename"));
			}
			
			stat.close();
			conn.close();
						
			

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return snum;
	}
	
	
	//4. 교육생 개인별 성적 조회
	
	/**
	 *  해당 교육생이 수강한 과목과 기간 출력 후 교육생의 성적이 마지막으로 출력됩니다.
	 */
	private static void procScoreInfoByStudent() {
		
		
		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);
		String snum = procScoreInfoTnameByStudent();

		try {
			

			conn = util.open("211.63.89.64", "project" ,"java1234");
			String sql = "{ call procScoreInfoByStudent(?,?)";
			
			stat = conn.prepareCall(sql);
			
			//실행 전 pause
			System.out.println();
			System.out.println();
			System.out.println("\t\t          엔터를 입력하시면 해당 교육생의 수강 과목별 성적이 조회됩니다.");
			scan.nextLine();
			
			//처음 입력한 교육생 번호
			stat.setString(1, snum);
	
			stat.registerOutParameter(2, OracleTypes.CURSOR);
			stat.executeQuery();
			
			rs = (ResultSet)stat.getObject(2);
			
			System.out.println();
			System.out.println();
			System.out.println("\t        [개설과목명]            [출결점수]   [필기점수]   [실기점수]");
			System.out.println("\t        ―――――――――――――――――――――――――――――――――――――――――――――――――――");
			
			while (rs.next()) {
				System.out.printf("\t\t%-15s\t         %-11s%-11s%-11s\n"
						, rs.getString("subjectname")
						, rs.getString("attendancescore")
						, rs.getString("writtentestscore")
						, rs.getString("performancetestscore"));
			}

			
			stat.close();
			conn.close();
						

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		//뒤로가기 전 pause
		System.out.println();
		System.out.println("\t\t\t엔터를 입력하시면 이전 페이지로 돌아갑니다.");
		scan.nextLine();
		
	}//procScoreInfoByStudent()

	

	/**
	 * 해당 교육생 수강 정보 출력 후 교육생이 수강한 과목과 기간을 추가로 출력합니다.
	 * @return 교육생 번호
	 */
	private static String procScoreInfoTnameByStudent() {

		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);
		String snum = procScoreBasicInfoByStudent();

		try {
			

			conn = util.open("211.63.89.64", "project" ,"java1234");
			String sql = "{ call procScoreInfoTnameByStudent(?,?)";
			
			stat = conn.prepareCall(sql);
			
			//실행 전 pause
			System.out.println();
			System.out.println();
			System.out.println("\t\t           엔터를 입력하시면 해당 교육생의 수강 과목과 기간이 출력됩니다.");
			scan.nextLine();
			
			

			stat.setString(1, snum);
			
			stat.registerOutParameter(2, OracleTypes.CURSOR);
			stat.executeQuery();
			
			rs = (ResultSet)stat.getObject(2);
			
			System.out.println();
			System.out.println();
			System.out.println("\t        [개설과목명]           [시작일]        [종료일]    [교사명]");
			System.out.println("\t        ――――――――――――――――――――――――――――――――――――――――――――――――――――");
			
			while (rs.next()) {
				System.out.printf("\t\t%-15s\t  %-14s%-14s      %-10s\n"
						, rs.getString("subjectname")
						, rs.getString("startdate")
						, rs.getString("enddate")
						, rs.getString("teachername"));
			}

			
			stat.close();
			conn.close();
						

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return snum;
		
		
	}//procScoreInfoTnameByStudent()


	/**
	 * 교육생 번호를 입력 받아 교육생 개인별 성적을 조회 전 수강 정보를 출력합니다.
	 * @return 교육생 번호
	 */
	private static String procScoreBasicInfoByStudent() {

		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);

		String snum = "";
		
		try {

			conn = util.open("211.63.89.64", "project" ,"java1234");
			String sql = "{ call procScoreBasicInfoByStudent(?,?)";
			
			stat = conn.prepareCall(sql);

			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t\t           교육생 개인별 성적 조회 ");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println();
			
			
			System.out.print("\t\t\t▷ 교육생 번호 입력 : "); //교육생 번호입력
			snum = scan.nextLine();
			System.out.println();
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

			stat.setString(1, snum);
			
			stat.registerOutParameter(2, OracleTypes.CURSOR);
			stat.executeQuery();
			
			rs = (ResultSet)stat.getObject(2);
			
			System.out.println();
			System.out.println();
			System.out.println("[교육생 이름] [주민번호]     [시작일]      [종료일]     [강의실명]  [개설과정명]");
			System.out.println("―――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――");
			
			while (rs.next()) {
				System.out.printf("%-8s%16s%13s%13s  %7s        %-50s\n"
						, rs.getString("studentname")
						, rs.getString("ssn")
						, rs.getString("startdate")
						, rs.getString("enddate")
						, rs.getString("classroomname") + "강의실"
						, rs.getString("coursename"));
			}
			
			stat.close();
			conn.close();
						

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return snum;
		
	}//procScoreBasicInfoByStudent()
	
	
	
	
	
	
	
	
	
	
	

}//AdminExam
