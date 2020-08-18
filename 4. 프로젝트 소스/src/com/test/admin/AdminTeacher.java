package com.test.admin;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;
import oracle.jdbc.OracleTypes;


/**
 * 관리자의 교사 계정 관리에 관련된 메소드들을 멤버로 갖는 클래스입니다.
 * @author Doyun Lee
 *
 */
public class AdminTeacher {
	
	/**
	 * 교사 계정 관리 메뉴 출력 메소드입니다.
	 */
	public void manageTeacherMenu() {
	//이도윤	
		
		Scanner scan = new Scanner(System.in);
		
		while (true) {
			
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t\t\t교사 계정 관리");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
	
			System.out.println("\t\t\t1. 교사 신규 등록");
			System.out.println("\t\t\t2. 교사 정보 수정");
			System.out.println("\t\t\t3. 교사 계정 삭제");
			System.out.println("\t\t\t4. 강의가능 과목 추가");
			System.out.println("\t\t\t5. 전체 교사 정보 조회");
			System.out.println("\t\t\t6. 교사별 강의 내역 조회");
			System.out.println("\t\t\t7. 교사평가 결과 조회");
			System.out.println("\t\t\t0. 뒤로가기");
	
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.print("\t\t\t▷ 입력:");
			String cho = scan.nextLine();
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println();
		
			
			if (cho.equals("1")) {
				//교사정보 등록
				procAddTeacher();
				
			} else if (cho.equals("2")) {
				//교사정보 수정
				procUpdateTeacherInfo();
				
			} else if (cho.equals("3")) {
				//교사계정 삭제 상태로 변경
				procDeleteTeacher();
					
			} else if (cho.equals("4")) {
				//강의가능 과목 추가
				procAddAvailableSubject();
				
			} else if (cho.equals("5")) {
				//전체 교사 정보 조회
				procAvailableByTeacher();
				
			} else if (cho.equals("6")) {
				//교사별 강의 내역 조회
				procCourseSubjectsByTeacher();
				
			} else if (cho.equals("7")) {
				//교사평가 결과 조회
				procPrintAllteacherRating();
				
			} else if (cho.equals("0")) {
				//뒤로가기
				System.out.println("\t\t\t뒤로가기를 선택하셨습니다.");
				System.out.println("\t\t\t엔터를 입력하시면 이전 페이지로 돌아갑니다.");
				scan.nextLine();
				break;
				
			} else {
				//잘못 입력했을 경우
				System.out.println();
				System.out.println("\t\t\t잘못 입력하셨습니다. ");
				System.out.println("\t\t\t1에서 7 사이의 숫자를 입력해주세요.");
				System.out.println();
			}//if
		}//while
	}//mainMenu()
	
	/**
	 * 이름, 연락처, 주민번호를 입력받아 교사를 추가하는 메소드입니다.
	 * 프로시저 실행 후 추가 성공 여부를 출력합니다.
	 */
	public void procAddTeacher() {
	//이도윤	
		
		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);
		
		String name = "";
		String tel = "";
		String ssn = "";
		
		try {
			
			conn = util.open("211.63.89.64","project","java1234");
			
			//교사 추가 프로시저 호출 준비
			String sql = "{ call procAddTeacher(?,?,?,?) }";
			stat = conn.prepareCall(sql);
			
			//헤더 출력
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t\t\t교사 신규 등록");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println();
			
			//사용자에게 정보 입력받음
			System.out.print("\t\t\t이름 : ");
			name = scan.nextLine();
			System.out.print("\t\t\t연락처 : ");
			tel = scan.nextLine();
			System.out.print("\t\t\t주민등록번호 : ");
			ssn = scan.nextLine();			
			System.out.println();
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
		
			//입력받은 정보 프로시저 매개변수에 넣어줌
			stat.setString(1, name);	
			stat.setString(2, tel);			
			stat.setString(3, ssn);
			stat.registerOutParameter(4, OracleTypes.NUMBER);
			
			//프로시저 실행해서 성공한 라인 수 받아오기 
			stat.executeUpdate();
			
			
			if (stat.getInt(4) > 0) {
				
				//교사 추가에 성공한 경우
				System.out.println();
				System.out.println("\t\t\t교사 추가가 완료되었습니다.");
				System.out.println("\t\t\t엔터를 입력하시면 이전 페이지로 돌아갑니다.");
				scan.nextLine();
				
			} else {
				
				//교사 추가에 실패한 경우
				System.out.println();
				System.out.println("\t\t\t교사 추가에 실패했습니다.");
				System.out.println("\t\t\t엔터를 입력하시면 이전 페이지로 돌아갑니다.");
				scan.nextLine();
			}

			//DB와 연결 종료
			stat.close();
			conn.close();
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}//procAddTeacher()
	
	/**
	 * 교사 정보를 수정하는 메소드입니다.
	 * 프로시저 실행 후 수정 성공 여부를 출력합니다.
	 */
	public void procUpdateTeacherInfo() {
	//이도윤			
		
		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);
		
		String teacherNum = "";
		String content = "";
		String select = "";
		
		try {
			
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t\t\t교사 정보 수정");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t1. 이름 수정");
			System.out.println("\t\t\t2. 전화번호 수정");
			System.out.println("\t\t\t3. 주민등록번호 수정");
			
			while (true) { //1~3 이외의 숫자일 경우 재입력받기위해 while문 사용
				
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.print("\t\t\t▷ 입력 : ");
				select = scan.nextLine();
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");	
				System.out.println();
				
				if (select.equals("1")) {//이름수정 선택시
					
					System.out.print("\t\t\t교사 번호 : ");
					teacherNum = scan.nextLine();
					System.out.print("\t\t\t이름 : ");	
					content = scan.nextLine();
					break; //정보 입력받고 while문 밖으로 나가 쿼리 실행하기
					
				} else if(select.equals("2")) { //전화번호 선택시
					
					System.out.print("\t\t\t교사 번호 : ");
					teacherNum = scan.nextLine();
					System.out.print("\t\t\t전화번호 : ");	
					content = scan.nextLine();
					break; //정보 입력받고 while문 밖으로 나가 쿼리 실행하기
					
				} else if(select.equals("3")) {
					
					System.out.print("\t\t\t교사 번호 : ");
					teacherNum = scan.nextLine();
					System.out.print("\t\t\t주민등록번호 : ");	
					content = scan.nextLine();
					break; //정보 입력받고 while문 밖으로 나가 쿼리 실행하기
					
				} else { //잘못 입력했을 경우 
				
					System.out.println("\t\t\t잘못 입력하셨습니다. ");
					System.out.println("\t\t\t1에서 3 사이의 숫자를 입력해주세요.");
					System.out.println();
				
				}//if
			}//while
			
			System.out.println();
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");	
			
			conn = util.open("211.63.89.64","project","java1234");
			
			//프로시저 실행 준비
			String sql = "{ call procUpdateTeacherInfo(?,?,?,?) }";
			stat = conn.prepareCall(sql);
			
			//프로시저 매개변수 설정
			stat.setString(1, select);
			stat.setString(2, teacherNum);
			stat.setString(3, content);
			stat.registerOutParameter(4, OracleTypes.NUMBER);	//성공여부 받아올 매개변수 설정
			
			//프로시저 실행
			stat.executeUpdate();
			
			
			//결과 안내 메시지 출력
			if (stat.getInt(4) > 0) {
				
				//교사 정보 수정에 성공한 경우
				System.out.println();
				System.out.println("\t\t\t교사 정보 수정이 완료되었습니다.");
				System.out.println("\t\t\t엔터를 입력하시면 이전 페이지로 돌아갑니다.");
				scan.nextLine();
				
			} else {
				
				//교사 정보 수정에 실패한 경우
				System.out.println();
				System.out.println("\t\t\t교사 정보 수정에 실패했습니다.");
				System.out.println("\t\t\t엔터를 입력하시면 이전 페이지로 돌아갑니다.");
				scan.nextLine();
			}

			//DB연결끊기
			stat.close();
			conn.close();			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 교사를 삭제상태로 변경하고, 강의가능과목에서 해당 교사의 내역을 삭제하는 메소드입니다.
	 * 프로시저 실행 후 변경 및 삭제 성공 여부를 출력합니다.
	 */
	public void procDeleteTeacher() {
	//이도윤		
		
		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);
		
		String teacherNum = "";
		
		try {
			
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t\t\t교사 계정 삭제");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.print("\t\t\t교사 번호 : ");
			teacherNum = scan.nextLine();
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println();

			//프로시저 호출 준비
			conn = util.open("211.63.89.64","project","java1234");
			String sql = "{ call procDeleteTeacher(?,?) }";
			stat = conn.prepareCall(sql);
			
			//프로시저 매개변수 설정
			stat.setString(1, teacherNum);
			stat.registerOutParameter(2, OracleTypes.NUMBER);	//성공여부 받아올 매개변수 설정

			//프로시저 실행
			stat.executeUpdate();
			
			
			if (stat.getInt(2) > 0) {
				
				//교사 계정 삭제에 성공한 경우
				System.out.println("\t\t\t교사 계정 삭제가 완료되었습니다.");
				System.out.println("\t\t\t엔터를 입력하시면 이전 페이지로 돌아갑니다.");
				scan.nextLine();
				
			} else {
				
				//교사 계정 삭제에 실패한 경우
				System.out.println("\t\t\t교사 계정 삭제에 실패했습니다.");
				System.out.println("\t\t\t엔터를 입력하시면 이전 페이지로 돌아갑니다.");
				scan.nextLine();
			}
			
			//DB 연결 끊기
			stat.close();
			conn.close();			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 과목 목록을 출력한 뒤, 교사번호와 과목번호를 입력받아 교사별 강의가능과목을 추가하는 메소드입니다.
	 * 프로시저 실행 후 추가 성공 여부를 출력합니다.
	 */
	public void procAddAvailableSubject() {
	//이도윤		
		
		//강의가능과목 추가
		
		Connection conn = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);

		//과목정보용 
		Statement subjectStat = null;
		ResultSet subjectRS = null;
		//강의가능과목추가용
		CallableStatement stat = null;
		
		try {
			
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t\t\t강의 가능 과목 추가");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println();
			
			conn = util.open("211.63.89.64","project","java1234");
			
			//과목목록 불러오기
			String subjectSQL = "select num, name from tblSubject where deletestatus = 0";
			subjectStat = conn.createStatement();
			subjectRS = subjectStat.executeQuery(subjectSQL);
			
			//과목목록 출력
			System.out.println("\t\t\t[번호]\t[과목명]");
			while(subjectRS.next()) {
				System.out.printf("\t\t\t%4s\t %s\n", subjectRS.getString("num"), subjectRS.getString("name"));
			}
			
			//과목 출력용 stat닫기
			subjectStat.close();

			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

			
			//강의가능과목 추가 프로시저 호출
			String sql = " { call procAddAvailableSubject(?,?,?) }";
			stat = conn.prepareCall(sql);
			
			String tnum = "";
			String snum = "";
			
			stat.registerOutParameter(3, OracleTypes.NUMBER); //성공여부 반환값 받아올 준비 
			
			//사용자가 종료하기 전까지 계속 입력받기
			boolean loop = true;
			while (loop) {
				
				System.out.println();
				System.out.println("\t\t\t종료하시려면 0을 입력하세요.");
				System.out.print("\t\t\t교사 번호: ");
				tnum = scan.nextLine(); 
				stat.setString(1, tnum);
				
				if (tnum.equals("0")) { //사용자가 교사 번호에 0을 입력하면 종료
					System.out.println();
					System.out.println("\t\t\t강의 가능 과목 추가를 종료합니다.");
					System.out.println("\t\t\t엔터를 입력하시면 이전 페이지로 돌아갑니다.");
					scan.nextLine();
					break;
				}
				
				System.out.print("\t\t\t과목 번호: ");
				snum = scan.nextLine();
				stat.setString(2, snum);
				System.out.println();
				System.out.println("\t\t\t――――――――――――――――――――――――――――――――――――――――――――");


				if (snum.equals("0")) {	//사용자가 과목 번호에 0을 입력하면 종료
					
					System.out.println();
					System.out.println("\t\t\t강의 가능 과목 추가를 종료합니다.");
					System.out.println("\t\t\t엔터를 입력하시면 이전 페이지로 돌아갑니다.");
					scan.nextLine();
					break;
				}
				
				//실행 후 실행된 레코드 수를 int로 받아오기
				stat.executeUpdate();
				
				if (stat.getInt(3) > 0) { //추가 성공시
					System.out.println("\t\t\t추가에 성공했습니다.");
					System.out.println("\t\t\t――――――――――――――――――――――――――――――――――――――――――――");
				} else {	//추가 실패시
					System.out.println("\t\t\t추가에 실패했습니다.");
					System.out.println("\t\t\t――――――――――――――――――――――――――――――――――――――――――――");
				}
				
			}//while
			
			//DB연결끊기
			stat.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 전체 교사의 이름, 주민번호 뒷자리(비밀번호), 전화번호, 강의가능 과목을 출력하는 메소드입니다.
	 */
	public void procAvailableByTeacher() {
	//이도윤	
		
		// 교사 전체 명단의 교사명, 주민번호 뒷자리, 전화번호, 강의 가능 과목을 출력
		//procAvailableByTeacher
		
		Connection conn = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);

		//교사정보용
		Statement tStat = null;
		ResultSet trs = null;
		
		//교사강의가능과목용
		CallableStatement sStat = null;
		ResultSet srs = null;
		
		
		try {
			
			conn = util.open("211.63.89.64","project","java1234");
			tStat = conn.createStatement();
			
			//삭제상태가 아닌 교사 목록만 가져옴.
			String sql = "select * from vwTeacherInfo where deleteStatus = 0";
			trs = tStat.executeQuery(sql);
			
			//헤더 출력
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t\t\t교사 정보 조회");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println();
			
			//교사 한명씩 출력 
			while(trs.next()) {
				
				System.out.println();
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.println("\t\t\t[이름]\t[비밀번호]\t[연락처]");
				System.out.println("\t\t\t――――――――――――――――――――――――――――――――――――――――――――");
				System.out.printf("\t\t\t%s\t%s\t\t%s\t\n", trs.getString("name"), trs.getString("pw"), trs.getString("tel"));
				
				//강의가능과목 목록 출력 준비
				String sql2 = "{ call procAvailableByTeacher(?,?) }";
				sStat = conn.prepareCall(sql2);
				
				//강의가능과목 프로시저 매개변수 설정
				sStat.setString(1, trs.getString("num"));				
				sStat.registerOutParameter(2, OracleTypes.CURSOR);
				
				//프로시저 실행
				sStat.executeQuery();
				srs = (ResultSet)sStat.getObject(2);
				
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.println("\t\t\t[강의 가능 과목]");
				System.out.println("\t\t\t――――――――――――――――――――――――――――――――――――――――――――");
				
				//강의가능과목 목록 출력
				while(srs.next()) {	
					System.out.println("\t\t\t " + srs.getString("availableSubject"));
				}//while
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			
			}//while
			
			//DB연결 끊기
			sStat.close();
			tStat.close();
			conn.close();
			
			//뒤로가기 전 pause
			System.out.println();
			System.out.println("\t\t\t엔터를 입력하시면 이전 페이지로 돌아갑니다.");
			scan.nextLine();	
			
		} catch (Exception e) {
			e.printStackTrace();
		}//try-catch
	}
	
	/**
	 * 전체 교사의 강의내역과 교사 목록을 출력한 후, 교사번호를 입력받아 세부 강의 스케줄을 출력하는 메소드입니다.
	 */
	public void procCourseSubjectsByTeacher() {
	//이도윤
		
		//vwCourseInfoByteacher
		//procCourseSubjectsByTeacher
		
		Connection conn = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);

		//전체(all) 교사 강의내역용 
		Statement aStat = null;
		ResultSet ars = null;
		
		//특정(specific) 교사 강의 세부내역용
		CallableStatement sStat = null;
		ResultSet srs = null;
		
		try {
			
			//헤더 출력
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t\t\t교사별 강의 내역 조회");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println();
			
			conn = util.open("211.63.89.64","project","java1234");
			
			//전체 교사 강의내역 불러오기 
			aStat = conn.createStatement();
			String sql = "select * from vwCourseInfoByteacher";
			ars = aStat.executeQuery(sql);
			
			//헤더 출력
			System.out.println("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println(" [이름]\t [상태]\t\t[기간]\t\t\t\t[강의실]\t\t[담당과정]");
			System.out.println("―――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――");
			
			//vwCourseInfoByteacher(전체교사 강의내역) 출력
			while (ars.next()) {
			
				System.out.printf(" %s\t %-10s\t%s\t\t%s\t\t%s\n"
											, ars.getString("teacherName")
											, ars.getString("courseStatus")
											, ars.getString("period")
											, ars.getString("classRoom")
											, ars.getString("courseName"));
			}//while
			
			System.out.println("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println();
			
			
			//강의 상세내역 조회 시작
			System.out.println("\t\t\t교사별 강의 상세 내역을 조회합니다.");
			System.out.println("\t\t\t계속하시려면 엔터를 입력하세요.");
			scan.nextLine();			
			
			//교사이름, 번호 불러오기 위함
			sql = "select * from tblTeacher where deleteStatus = 0";
			ars = aStat.executeQuery(sql);

			
			//교사번호, 이름 먼저 출력해줌
			System.out.println("\t\t\t――――――――――――――――――――――――――――――――――――――――――――");
			System.out.println("\t\t\t[번호]\t[이름]");
			System.out.println("\t\t\t――――――――――――――――――――――――――――――――――――――――――――");

			while (ars.next()) {
				
				System.out.printf("\t\t\t%3s\t%s\n", ars.getString("num"), ars.getString("name"));
			}			
			System.out.println("\t\t\t――――――――――――――――――――――――――――――――――――――――――――");

			//교사번호 입력받아 담당강의 상세내역 출력하기
			System.out.print("\t\t\t상세 내역을 조회할 교사 번호를 입력하세요: ");
			String tnum = scan.nextLine();
			System.out.println("\t\t\t――――――――――――――――――――――――――――――――――――――――――――");
			System.out.println();
			
			//교사별 상세 강의내역 프로시저 호출 준비
			sql = "{ call procCourseSubjectsByTeacher(?,?) }";
			sStat = conn.prepareCall(sql);
			
			//프로시저 매개변수 설정
			sStat.setString(1, tnum);
			sStat.registerOutParameter(2, OracleTypes.CURSOR);
			
			//프로시저 실행
			sStat.executeQuery();
			srs = (ResultSet)sStat.getObject(2);

			//교사별 강의 상세내역 출력하기 
			System.out.println("―――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――");
			System.out.println("[기간]\t\t\t\t[과목]\t\t\t[교재]");
			System.out.println("―――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――");
			
			while (srs.next()) {
				System.out.printf("%s\t\t%-14s\t%-30s\n"
													, srs.getString("subjectPeriod")
													, srs.getString("subjectName")
													, srs.getString("book"));
			}//while
			System.out.println("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
		
			//닫아주기!
			aStat.close();
			sStat.close();
			conn.close();

			//뒤로가기 전 pause
			System.out.println();
			System.out.println("\t\t\t엔터를 입력하시면 이전 페이지로 돌아갑니다.");
			scan.nextLine();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 전체 교사의 강의별 교사평가 평균 점수를 출력하는 메소드입니다.
	 */
	public void procPrintAllteacherRating() {
	   
	      
	      Connection conn = null;
	      CallableStatement stat = null;
	      DBUtil util = new DBUtil();
	      ResultSet rs = null;
	      Scanner scan = new Scanner(System.in);
	            
	      try {
	         
	               
	         String sql = "{ call procPrintAllteacherRating(?) }";

	         conn = util.open("211.63.89.64","project","java1234");
	         stat = conn.prepareCall(sql);
	         
	         stat.registerOutParameter(1, OracleTypes.CURSOR);
	         
	         stat.executeQuery();
	                  
	         rs = (ResultSet)stat.getObject(1); 
	         
	         //헤더 출력
	         System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
	         System.out.println("\t\t\t\t\t교사평가 점수 조회");
	         System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

	         //교사이름, 과정번호, 종합(항목:1,2,3,4,5,6)평균점수
	         System.out.println("\t\t\t[번호]\t[이름]\t[과정번호]\t[종합평균점수]");
	         System.out.println("\t\t\t――――――――――――――――――――――――――――――――――――――――――――");
	         
	         int num = 1;
	         
	         while(rs.next()) {
	            
	            System.out.printf("\t\t\t%3d\t%s\t%5s\t\t%6s점\n"
	                                    , num
	                                    , rs.getString("teacherName")
	                                    , rs.getString("openCourseNum")
	                                    , rs.getString("score"));
	            num++;
	         }
	         System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
	         
	         rs.close();
	         stat.close();
	         conn.close();
	         
	      } catch (Exception e) {
	         System.out.println("\t\t\t교사 평가 점수 조회를 실패했습니다.");
	      }
	      
	      //뒤로가기 전 pause
	      System.out.println();
	      System.out.println("\t\t\t엔터를 입력하시면 이전 페이지로 돌아갑니다.");
	      scan.nextLine();
	      
	   }//관리자가 모든 교사 평가 점수(끝)

	
	
}
 