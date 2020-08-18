package com.test.admin;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

/**
 * 관리자 로그인 검사 및 데이터 삽입을 위한 클래스입니다. 
 * @author leeho
 *
 */
public class AdminUser {

	int num;
	String id;
	String pw;
	boolean loginFlag = false;
	
	/**
	 * 로그인 유효성 검사 및 객체에 대한 데이터 입력을 위한 메소드입니다.
	 * @param adminUser 로그인 성공 시 데이터를 입력하기 위한 객체입니다. 
	 */
	public void login(AdminUser adminUser) {

		// Database connection
		// 데이터베이스 연동
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);
		

		// Variable for Admin Info
		// 교사 계정 데이터를 넣어줄 변수
		HashMap<String, String> loginInfo = new HashMap<String, String>();

		try {

			conn = util.open("211.63.89.64", "project", "java1234");
			stat = conn.createStatement();

			String sql = String.format("select * from tbladmin");
			rs = stat.executeQuery(sql);
			
			// Insert info to loginInfo map
			// 데이터 입력
			while (rs.next()) {
				loginInfo.put(rs.getString("id"), rs.getString("password"));
			}
			// input id,pw
			// 사용자에게 id,pw 입력받기
			System.out.print("\t\t\t▷ ID: ");
			String inputId = scan.nextLine();
			System.out.print("\t\t\t▷ PW: ");
			String inputPw = scan.nextLine();
			System.out.println();

			// iterator
			Iterator<String> keys = loginInfo.keySet().iterator();

			// loginInfo search
			// id 탐색
			for (String id : loginInfo.keySet()) {

				// id matching
				if (id.equals(inputId)) {

					// password get
					String pw = loginInfo.get(id);

					// password matching
					if (pw.equals(inputPw)) {

						AdminMain adminMain = new AdminMain();
						
						// set info
						adminUser.setId(id);
						adminUser.setPw(pw);
						
						// login on
						adminUser.loginFlag = true;

						// mainmenu method
						// 메인메뉴 메소드 실행
						adminMain.mainmenu(adminUser);
						
						// Database connection close
						stat.close();
						conn.close();

					}
				} 
			}
			
			// when enter wrong info
			if(!adminUser.loginFlag) {
				System.out.println("\t\t\t아이디와 비밀번호를 다시 입력해주세요.");
			} 
			// logout
			else {
				System.out.println("\t\t\t로그아웃을 진행합니다.");
				scan.nextLine();
				adminUser.loginFlag = false;
			}
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// getter & setter
	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPw() {
		return pw;
	}

	public void setPw(String pw) {
		this.pw = pw;
	}
}
