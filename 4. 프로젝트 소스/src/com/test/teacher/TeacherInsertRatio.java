package com.test.teacher;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Scanner;

import com.test.admin.DBUtil;

import oracle.jdbc.OracleTypes;

/**
 * 배점관리 관련 메소드를 멤버로 갖는 클래스입니다.
 * @author 김찬우
 *
 */
public class TeacherInsertRatio {
	/**
	 * 선생님이 배점을 관리할 수 있는 메뉴입니다.
	 * @param user 교사 객체
	 */
	public void insertRatio(TeacherUser user) {
		while (true) {
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t\t시험 과목 관리");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t1. 과목 별 배점확인");
			System.out.println("\t\t\t2. 과목 별 배점입력");
			System.out.println("\t\t\t3. 과정,과목 목록 조회");
			System.out.println("\t\t\t4. 시험 날짜 입력");
			System.out.println("\t\t\t5. 시험 문제 입력");
			System.out.println("\t\t\t0. 뒤로가기");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.print("\t\t\t▷입력:");
			Scanner sc = new Scanner(System.in);
			int cho = sc.nextInt();
			sc.skip("\r\n");
			if (cho == 1) {
				ProcPrintRatio(user.getNum());
			} else if (cho == 2) {
				procInputTestRatio(user.getNum());
			} else if (cho == 3) {
				while (true) {
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println("\t\t\t\t과정, 과목 조회");
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					System.out.println("\t\t\t1. 과정 정보");
					System.out.println("\t\t\t2. 과목 정보");
					System.out.println("\t\t\t0. 뒤로가기");
					System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
					Scanner sc2 = new Scanner(System.in);
					System.out.print("\t\t\t▷ 입력:");				
					String sel = sc2.nextLine();
					if (sel.equals("1")) {
						procsubjectlist1(user.getNum());
					} else if (sel.equals("2")) {
						procsubjectlist2(user.getNum());
					} else if (sel.equals("3")) {
						procInputTestRatio(user.getNum());
						Scanner sc3 = new Scanner(System.in);
					} else if (sel.equals("0")) {
						break;
					}
				}
			} else if (cho == 4) {
				procAddTestdate();
			} else if (cho == 5) {
				procAddQuestion();
			} else if (cho == 0) {
				break;
			}
		}

	}
	/**
	 * procAddQuestion 프로시저를 호출해 문제를 추가는 메뉴입니다.
	 */
	public void procAddQuestion() {
		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);
		String type ="";

		try {
			conn = util.open("211.63.89.64","project","java1234");
			String sql = "{ call procAddQuestion(?,?,?,?) }";
			
			conn.setAutoCommit(false);
			stat = conn.prepareCall(sql);
			boolean chk = true;
			
			for(;chk;) {
					System.out.print("\t\t\t시험 구분(필기/실기):");
				    type = scan.nextLine();
					if(type.equals("필기") || type.equals("실기")) {
						chk = false;
					}
					else {
					System.out.println("\t\t\t시험구분엔 '필기'나 '실기'만 입력가능합니다.");
					}
			}
			System.out.print("\t\t\t 문제 번호 입력:");
			String qnum = scan.nextLine();
			System.out.print("\t\t\t문제 입력:");
			String question = scan.nextLine();
			System.out.print("\t\t\t답 입력:");
			String answer = scan.nextLine();
		
			stat.setString(1,qnum);
			stat.setString(2,type);
			stat.setString(3,question);
			stat.setString(4,answer);
			stat.registerOutParameter(5, OracleTypes.NUMBER);
			
			int result = stat.getInt(5);
					stat.executeUpdate();
			
			if(result == 1) {
				System.out.println("문제 입력 완료!");
				conn.commit();
			}
			else {
				System.out.println("문제 입력 실패!");
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
	 * procAddTestdate 프로시저를 호출해 시험 날짜를 추가합니다.
	 */
	public void  procAddTestdate() {
		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);

		try {
			conn = util.open("211.63.89.64","project","java1234");
			String sql = "{ call procAddTestdate(?,?,?,?,?) }";
			
			conn.setAutoCommit(false);
			stat = conn.prepareCall(sql);
			
			System.out.print("\t\t\t교수 번호:");
			String tnum = scan.nextLine();
			System.out.print("\t\t\t과정 번호:");
			String cnum = scan.nextLine();
			System.out.print("\t\t\t과목 번호:");
			String snum = scan.nextLine();
			System.out.print("\t\t\t시험 날짜:");
			String test = scan.nextLine();
			
			stat.setString(1,snum);
			stat.setString(2,tnum);
			stat.setString(3,cnum);
			stat.setString(4,test);
			stat.registerOutParameter(5, OracleTypes.NUMBER);
			
			
			int result = stat.getInt(5);
					stat.executeUpdate();
			
			if(result == 1) {
				System.out.println("\t\t\t날짜 입력 완료!");
				conn.commit();
			}
			else {
				System.out.println("\t\t\t날짜 입력 실패!");
				conn.rollback();
			}
			stat.close();
			conn.close();
			
		} catch (Exception e) {
			System.out.println("\t\t\t에러~~");
			System.out.println(e);
		}

	}
	/**
	 * 자신이 강의하는 과목번호,과목명,교재,과목의 기간,과정명을 출력합니다.
	 * @param i 교사번호
	 */
	public void procsubjectlist2(int i) {
		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		ResultSet rs = null;
		Scanner scan = new Scanner(System.in);

		try {
			conn = util.open("211.63.89.64","project","java1234");
			String sql = "{ call procsubjectlist2(?,?) }";
			
			conn.setAutoCommit(false);
			stat = conn.prepareCall(sql);
			
		    stat.registerOutParameter(1, OracleTypes.CURSOR);
		    stat.setInt(2, i);
		    stat.executeQuery();
		    rs =(ResultSet)stat.getObject(1);
		    System.out.println("\t\t\t[과목번호]\t\t[과목기간]\t\t[과목명]\t\t[교재명]\t\t\t[과정명]");
		    System.out.println();
		    while(rs.next()) {
		    	System.out.printf("\t\t\t%2s\t%-10s\t%-13s\t%-22s\t%s \n",
		    							 rs.getString(1) 
		    							 ,rs.getString(5)
		    							 ,rs.getString(2)
		    							 ,rs.getString(3)
									    ,rs.getString(4));
		    }
		    
			
			stat.close();
			conn.close();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		} 
	}
	
	/**
	 * 
	 * @param i 교사번호
	 */
	public void procsubjectlist1(int i) {
		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		ResultSet rs = null;

		try {
			conn = util.open("211.63.89.64","project","java1234");
			String sql = "{ call procsubjectlist1(?,?) }";
			
			conn.setAutoCommit(false);
			stat = conn.prepareCall(sql);

		    stat.registerOutParameter(1, OracleTypes.CURSOR);
		    stat.setInt(2, i);
		    stat.executeQuery();
		    rs =(ResultSet)stat.getObject(1);
		    System.out.println("\t\t\t[강의실명]\t\t[과정기간]\t\t\t[과정명]");
		    System.out.println();
		    while(rs.next()) {
		    	System.out.printf("\t\t\t%s\t\t%s\t%s \n",
		    							 rs.getString(2)
		    							,rs.getString(3)
									    ,rs.getString(1));
		    }
		    
			
			stat.close();
			conn.close();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		} 
	}
	/**
	 * 로그인 한 강사가 강의를 마친 시험의 배점을 입력합니다.
	 * @param i 강사번호
	 */
	public void procInputTestRatio(int i) {
		System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
		System.out.println("\t\t\t시험 배점 입력");
		System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		ResultSet rs = null;
		Scanner scan = new Scanner(System.in);
		
		
		try {
			
			conn = util.open("211.63.89.64","project","java1234");
			String sql = "{ call procInputWrittenTestRatio(?,?,?,?,?,?)}";
			stat = conn.prepareCall(sql);
			
			boolean atrc = true;

			int thr = i;
			System.out.print("\t\t\t과정번호:");
			int cnum = Integer.parseInt(scan.nextLine());
			System.out.print("\t\t\t과목번호:");
			int sub = Integer.parseInt(scan.nextLine());
			System.out.print("\t\t\t필기배점:");
			int wtr = Integer.parseInt(scan.nextLine());
			System.out.print("\t\t\t실기배점:");
			int ptr = Integer.parseInt(scan.nextLine());
			System.out.print("\t\t\t출결배점(20점이상):");
			int atr = Integer.parseInt(scan.nextLine());
			if( atr < 20 ) {
				System.out.println("\t\t\t출결배점은 20점 이상이어야 합니다.");
				while(atrc) {
				System.out.print("\t\t\t출결배점(20점이상):");
				atr = Integer.parseInt(scan.nextLine());
					if(atr>=20) {
						atrc =false;
					}
				}
			}
			stat.setInt(1,thr);
			stat.setInt(2,cnum);
			stat.setInt(3,sub);
			stat.setDouble(4,wtr);
			stat.setDouble(5,ptr);
			stat.setDouble(6,atr);
			
			
			stat.executeUpdate();
			
			if( wtr + ptr + atr ==100) {
				System.out.println("\t\t\t배점입력완료");
				conn.commit();
			}
			else {
				System.out.println("\t\t\t실기,필기,출결 배점의 합이 100이 어야합니다.");
				conn.rollback();
			}
			
			
			
		} catch (Exception e) {
			System.out.println("Ex07_CallableStatment.m5()");
			e.printStackTrace();
		}
		
	}
	/**
	 * 로그인 강사가 강의한 과목 별 배점을 확인합니다.
	 * @param i 강사번호
	 */
	public void ProcPrintRatio(int i) {
		System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
		System.out.println("\t\t\t과목별 시험 배점 확인");
		System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		ResultSet rs = null;
		Scanner scan = new Scanner(System.in);

		try {
			conn = util.open("211.63.89.64","project","java1234");
			String sql = "{ call procPrintRatio(?,?,?)}";
			
			conn.setAutoCommit(false);
			stat = conn.prepareCall(sql);
			
		
			
			int tnum = i;
			System.out.print("\t\t\t과목번호 입력:");
			int snum = scan.nextInt();
			scan.skip("\r\n");
		    stat.registerOutParameter(1, OracleTypes.CURSOR);
		    stat.setInt(2, tnum);
		    stat.setInt(3, snum);
		    

		    stat.executeQuery();
		
		    rs =(ResultSet)stat.getObject(1);
		    
		    System.out.println("\t\t\t[과목명]\t[필기배점]\t[실기배점]\t[출결배점]\t[교사명]\t[과정번호]");
		    while(rs.next()) {
		    	System.out.printf("\t\t\t%s\t%4s\t%4s\t%4s\t%s\t%s \n",
		    							rs.getString(6)
		    							,0+rs.getString(1)
		    							,0+rs.getString(2)
		    							,0+rs.getString(3)
		    							,rs.getString(4)
		    							,rs.getString(5));
		    }
		    
			
			stat.close();
			conn.close();
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		} 

	}
}
