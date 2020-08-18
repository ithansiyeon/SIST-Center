package com.test.admin;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import oracle.jdbc.OracleTypes;

/**
 * 관리자 - 기자재 관리 관련 메소드를 멤버로 갖는 클래스입니다.
 * @author Doyun Lee
 *
 */
public class AdminEquipment {
	
	/**
	 * 기자재 관리 메뉴를 출력하는 메소드입니다.
	 */
	public void manageEquipMenu() {
	//이도윤
		
		Scanner scan = new Scanner(System.in);
		
		while (true) {
			
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t\t         기자재 관리");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

			System.out.println("\t\t\t1. 구입날짜별 조회");
			System.out.println("\t\t\t2. 강의실별 조회");
			System.out.println("\t\t\t3. 기자재 내역 추가");
			System.out.println("\t\t\t4. 기자재 수량 수정");
			System.out.println("\t\t\t0. 뒤로가기");
	
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.print("\t\t\t▷ 입력:");
			String cho = scan.nextLine();
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println();
		
			
			if (cho.equals("1")) {
				//구입날짜별 조회
				procEquipmentByDate();
				
			} else if (cho.equals("2")) {
				//강의실별 조회
				procEquipmentByClassroom();
				
			} else if (cho.equals("3")) {
				//기자재 내역 추가
				procInsertEquipmentByClassroom();
				
			} else if (cho.equals("4")) {
				//기자재 수량 수정 
				procUpdateEquipByRoom();
				
			} else if (cho.equals("0")) {
				//뒤로가기
				System.out.println("\t\t\t뒤로가기를 선택하셨습니다.");
				System.out.println("\t\t\t엔터를 입력하시면 이전 페이지로 돌아갑니다.");
				scan.nextLine();
				break;
				
			} else {
				//잘못 입력했을 경우
				System.out.println();
				System.out.println("\t\t\t잘못 입력하셨습니다.");
				System.out.println("\t\t\t1에서 4 사이의 숫자를 입력해주세요.");
				System.out.println();
			}//if
		}//while
		
	}//main
	
	/**
	 * 강의실 번호를 입력받아 강의실별 기자재를 조회하는 메소드입니다.
	 */
	public void procEquipmentByClassroom() {
	//이도윤
		
		//강의실별 기자재 조회
		
		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		ResultSet rs = null;
		Scanner scan = new Scanner(System.in);
		
		//시작날짜, 종료날짜 입력받을 변수
		String roomNum;
		
		try {
			
			conn = util.open("211.63.89.64","project","java1234");
			
			//교사 추가 프로시저 호출 준비
			String sql = "{ call procEquipmentByClassroom(?,?) }";
			stat = conn.prepareCall(sql);
			
			
			//헤더 출력
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t\t        강의실별 조회");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println();
			
			//사용자에게 정보 입력받음
			System.out.print("\t\t\t▷강의실 번호 : ");
			roomNum = scan.nextLine();

			System.out.println();
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			
			//프로시저 호출 준비
			stat.setString(1, roomNum);
			stat.registerOutParameter(2, OracleTypes.CURSOR);
			
			//프로시저 호출
			stat.executeQuery();
			rs = (ResultSet)stat.getObject(2);
			
			//헤더 출력
			System.out.println();
			System.out.println("\t\t[강의실] [기자재]\t\t[수량]\t[구입날짜]");
			System.out.println("\t\t――――――――――――――――――――――――――――――――――――――――――――");
			
			//결과 출력
			while(rs.next()) {
				
				System.out.printf("\t\t%s\t%-15s\t%3s\t%s\n", rs.getString("classroomName")
															, rs.getString("item")
															, rs.getString("qty")
															, rs.getString("purchaseDate"));		
			}
			
			//출력 완료
			System.out.println();
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println();
			System.out.println("\t\t\t조회가 완료되었습니다.");
			System.out.println("\t\t\t엔터를 입력하시면 이전 페이지로 돌아갑니다.");
			scan.nextLine();					
			
			stat.close();
			conn.close();
			
		} catch (Exception e) {
			e.printStackTrace();			
		}	
		
		
	}//procEquipmentByDate
	
	/**
	 * 기간을 입력받아 구입 날짜별로 기자재를 조회하는 메소드입니다.
	 */
	public void procEquipmentByDate() {
	//이도윤
		
		//구입날짜별 기자재 조회
		
		Connection conn = null;
		CallableStatement stat = null;
		DBUtil util = new DBUtil();
		ResultSet rs = null;
		Scanner scan = new Scanner(System.in);
		
		String syear;
		String smonth;
		String sdate;
		String eyear;
		String emonth;
		String edate;
		
		try {
			
			conn = util.open("211.63.89.64","project","java1234");
			
			//교사 추가 프로시저 호출 준비
			String sql = "{ call procEquipmentByDate(?,?,?) }";
			stat = conn.prepareCall(sql);
			
			
			//헤더 출력
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t\t        날짜별 조회");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println();
			
			//사용자에게 정보 입력받음
			System.out.println("\t\t\t[시작날짜]");
			System.out.print("\t\t\t년 : ");
			syear = scan.nextLine();
			System.out.print("\t\t\t월 : ");
			smonth = scan.nextLine();
			System.out.print("\t\t\t일 : ");
			sdate = scan.nextLine();	
			System.out.println("\t\t\t[종료날짜]");
			System.out.print("\t\t\t년 : ");
			eyear = scan.nextLine();
			System.out.print("\t\t\t월 : ");
			emonth = scan.nextLine();
			System.out.print("\t\t\t일 : ");
			edate = scan.nextLine();	
			System.out.println();
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			
			//프로시저 호출 준비
			stat.setString(1, syear+smonth+sdate);
			stat.setString(2, eyear+emonth+edate);
			stat.registerOutParameter(3, OracleTypes.CURSOR);
			
			//프로시저 호출
			stat.executeQuery();
			rs = (ResultSet)stat.getObject(3);
			
			//헤더 출력
			System.out.println();
			System.out.println("\t\t[강의실] [기자재]\t\t[수량]\t[구입날짜]");
			System.out.println("\t\t――――――――――――――――――――――――――――――――――――――――――――");
			
			//결과 출력
			while(rs.next()) {
				
				System.out.printf("\t\t%s\t%-15s\t%3s\t%s\n", rs.getString("classroomName")
															, rs.getString("item")
															, rs.getString("qty")
															, rs.getString("purchaseDate"));		
			}
			
			//출력 완료
			System.out.println();
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println();
			System.out.println("\t\t\t조회가 완료되었습니다.");
			System.out.println("\t\t\t엔터를 입력하시면 이전 페이지로 돌아갑니다.");
			scan.nextLine();		
			
			stat.close();
			conn.close();
			
		} catch (Exception e) {
			e.printStackTrace();			
		}	

	}//procEquipmentByDate
	
	/**
	 * 강의실 번호, 기자재 번호, 수량, 구입일을 입력받아 기자재 내역을 추가하는 메소드입니다.
	 * 프로시저 실행 후 추가 성공 여부를 출력합니다.
	 */
	public void procInsertEquipmentByClassroom() {
	//이도윤
		
		//기자재 내역 추가
		
		Connection conn = null;
		
		//기자재 목록 호출용
		Statement estat = null;
		ResultSet rs = null;
		//기자재 추가 프로시저 호출용
		CallableStatement stat = null;
		
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);
		
		try {
			
			conn = util.open("211.63.89.64","project","java1234");
			estat = conn.createStatement();
			
			//기자재 번호를 알려주기 위해 기자재 목록 출력
			String sql = "select * from tblEquipment";
			rs = estat.executeQuery(sql);
			
			//헤더 출력
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t\t\t기자재 내역 추가");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println();
			
			//기자재 목록 출력 
			System.out.println("\t\t\t[번호]\t[기자재]");
			System.out.println("\t\t\t――――――――――――――――――――――――――――――――――――――――――――");
			
			while(rs.next()) {
				System.out.printf("\t\t\t%s\t%s\n", rs.getString("num"), rs.getString("item"));
			}//while
			
			System.out.println();
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			
			//연결 끊기 
			estat.close();

			sql = "{ call procInsertEquipmentByClassroom(?,?,?,?,?) }";
			stat = conn.prepareCall(sql);
			
			String roomNum, equipNum, qty, year, month, date;
			stat.registerOutParameter(5, OracleTypes.NUMBER);
			
			//사용자가 종료하기 전까지 계속 입력받기
			boolean loop = true;
			
			while (loop) {
				
				System.out.println();
				System.out.println("\t\t\t종료하시려면 0을 입력하세요.");
				System.out.print("\t\t\t강의실 번호: ");
				roomNum = scan.nextLine(); 
				stat.setString(1, roomNum);
				
				if (roomNum.equals("0")) { //0을 입력하면 종료
					System.out.println();
					System.out.println("\t\t\t기자재 내역 추가를 종료합니다.");
					break;
				}
				
				System.out.print("\t\t\t기자재 번호: ");
				equipNum = scan.nextLine();
				stat.setString(2, equipNum);


				if (equipNum.equals("0")) {	//0을 입력하면 종료
					
					System.out.println();
					System.out.println("\t\t\t기자재 내역 추가를 종료합니다.");
					break;
				}
				
				System.out.print("\t\t\t수량: ");
				qty = scan.nextLine();
				stat.setString(3, qty);
	
				
				if (qty.equals("0")) {	//0을 입력하면 종료
					
					System.out.println();
					System.out.println("\t\t\t기자재 내역 추가를 종료합니다.");
					break;
				}
				
				//구입일 입력받기
				System.out.print("\t\t\t구입년도: ");
				year = scan.nextLine();
				System.out.print("\t\t\t구입월: ");
				month = scan.nextLine();				
				System.out.print("\t\t\t구입일: ");
				date = scan.nextLine();
				stat.setString(4, year+month+date);
				System.out.println();
				System.out.println("\t\t\t――――――――――――――――――――――――――――――――――――――――――――");
				
				
				if (qty.equals("0")) {	//0을 입력하면 종료
					
					System.out.println();
					System.out.println("\t\t\t기자재 내역 추가를 종료합니다.");
					System.out.println("\t\t\t엔터를 입력하시면 이전 페이지로 돌아갑니다.");
					scan.nextLine();
					break;
				}
				
				//추가 프로시저 실행
				stat.executeUpdate();
				
				if (stat.getInt(5) > 0) { //추가 성공시
					System.out.println("\t\t\t추가에 성공했습니다.");
					System.out.println("\t\t\t――――――――――――――――――――――――――――――――――――――――――――");
				} else {	//추가 실패시
					System.out.println("\t\t\t추가에 실패했습니다.");
					System.out.println("\t\t\t――――――――――――――――――――――――――――――――――――――――――――");
				}
				
			}//while
			
			stat.close();
			conn.close();
			
			//뒤로가기 전 pause
			System.out.println("\t\t\t엔터를 입력하시면 이전 페이지로 돌아갑니다.");
			scan.nextLine();	
			
		} catch (Exception e) {
			e.printStackTrace();
		}//try-catch
		
	}
	
	/**
	 * 강의실번호, 기자재번호, 수량을 입력받아 기자재 내역을 수정하는 메소드입니다.
	 * 프로시저 실행 후 수정 성공 여부를 출력합니다.
	 */
	public void procUpdateEquipByRoom() {		
	//이도윤
	
			//기자재 수량 수정
			
			Connection conn = null;
			
			//기자재 목록 호출용
			Statement estat = null;
			ResultSet rs = null;
			//기자재 수정 프로시저 호출용
			CallableStatement stat = null;
			
			DBUtil util = new DBUtil();
			Scanner scan = new Scanner(System.in);
			
			try {
				
				conn = util.open("211.63.89.64","project","java1234");
				estat = conn.createStatement();
				
				//기자재 번호를 알려주기 위해 기자재 목록 출력
				String sql = "select * from tblEquipment";
				rs = estat.executeQuery(sql);
				
				//헤더 출력
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.println("\t\t\t\t\t기자재 수량 수정");
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.println();
				
				//기자재 목록 출력 
				System.out.println("\t\t\t[번호]\t[기자재]");
				System.out.println("\t\t\t――――――――――――――――――――――――――――――――――――――――――――");
				
				while(rs.next()) {
					System.out.printf("\t\t\t%s\t%s\n", rs.getString("num"), rs.getString("item"));
				}//while
				
				System.out.println();
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				
				//연결 끊기 
				estat.close();

				sql = "{ call procUpdateEquipByRoom(?,?,?,?) }";
				stat = conn.prepareCall(sql);
				
				String roomNum, equipNum, qty;
				stat.registerOutParameter(4, OracleTypes.NUMBER);	//성공여부 받아오기
				
				//사용자가 종료하기 전까지 계속 입력받기
				boolean loop = true;
				
				while (loop) {
					
					System.out.println();
					System.out.println("\t\t\t종료하시려면 0을 입력하세요.");
					System.out.print("\t\t\t강의실 번호: ");
					roomNum = scan.nextLine(); 
					stat.setString(1, roomNum);
					
					if (roomNum.equals("0")) { //0을 입력하면 종료
						System.out.println();
						System.out.println("\t\t\t기자재 수량 수정을 종료합니다.");
						break;
					}
					
					System.out.print("\t\t\t기자재 번호: ");
					equipNum = scan.nextLine();
					stat.setString(2, equipNum);


					if (equipNum.equals("0")) {	//0을 입력하면 종료
						
						System.out.println();
						System.out.println("\t\t\t기자재 수량 수정을 종료합니다.");
						break;
					}
					
					System.out.print("\t\t\t수량: ");
					qty = scan.nextLine();
					stat.setString(3, qty);
					System.out.println("\t\t\t――――――――――――――――――――――――――――――――――――――――――――");
		
					
					if (qty.equals("0")) {	//0을 입력하면 종료
						
						System.out.println();
						System.out.println("\t\t\t기자재 수량 수정을 종료합니다.");
						break;
					}
					
					//실행 후 실행된 레코드 수를 int로 받아오기
					stat.executeUpdate();
					
					if (stat.getInt(4)> 0) { //추가 성공시
						System.out.println("\t\t\t수정에 성공했습니다.");
						System.out.println("\t\t\t――――――――――――――――――――――――――――――――――――――――――――");
					} else {	//추가 실패시
						System.out.println("\t\t\t수정에 실패했습니다.");
						System.out.println("\t\t\t――――――――――――――――――――――――――――――――――――――――――――");
					}
					
				}//while
				
				//접속 끊기
				stat.close();
				conn.close();
				
				//뒤로가기 전 pause
				System.out.println("\t\t\t엔터를 입력하시면 이전 페이지로 돌아갑니다.");
				scan.nextLine();	
				
			} catch (Exception e) {
				e.printStackTrace();
			}//try-catch
		
	}
	
}
