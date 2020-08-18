package com.test.teacher;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

import com.test.admin.AdminMain;
import com.test.admin.AdminUser;
import com.test.admin.DBUtil;
import com.test.student.StudentMain;

/**
 *  교사 로그인 및 데이터 입력을 위한 클래스입니다.
 * @author leeho
 *
 */
public class TeacherUser {

	int num;
	String name;
	String tel;
	String ssn;
	boolean loginFlag = false;
	
	/**
	 * 로그인 유효성 검사 및 객체에 대한 데이터 입력을 위한 메소드입니다.
	 * @param teacherUser 로그인 성공 시 데이터를 입력하기 위한 객체입니다. 
	 */
	public void login(TeacherUser teacherUser) {

		// Database connection
		// 데이터베이스 연동
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);

		// Variable for teacher Info
		// 관리자 계정 데이터를 넣어줄 변수
		HashMap<String, ArrayList<String>> teacherInfo = new HashMap<String, ArrayList<String>>();

		try {
			conn = util.open("211.63.89.64", "project", "java1234");
			stat = conn.createStatement();

			String sql = String.format("select * from tblTeacher");
			rs = stat.executeQuery(sql);

			// Insert info to loginInfo map
			// 데이터 입력
			while (rs.next()) {
				ArrayList<String> temp = new ArrayList<String>();

				// only used data
				if (rs.getString("deleteStatus").equals("0")) {
					// ssn, num, name
					temp.add(rs.getString("ssn"));
					temp.add(rs.getString("num"));
					temp.add(rs.getString("name"));
					teacherInfo.put(rs.getString("tel"), temp);
				}

			}

			// input id, pw
			System.out.print("\t\t\t▷ ID: ");
			String inputId = scan.nextLine();
			System.out.print("\t\t\t▷ PW: ");
			String inputPw = scan.nextLine();

			// iterator
			Iterator<String> keys = teacherInfo.keySet().iterator();

			// loginInfo search
			for (String id : teacherInfo.keySet()) {
				// id matching
				if (id.equals(inputId)) {

					// password get
					String ssn = teacherInfo.get(id).get(0);
					String pw = ssn.substring(ssn.indexOf('-') + 1);

					// pw matching
					if (pw.equals(inputPw)) {

						TeacherMain teacherMain = new TeacherMain();
						
						// login on
						teacherUser.loginFlag = true;
						
						// set info
						teacherUser.setSsn(teacherInfo.get(id).get(0));
						teacherUser.setNum(Integer.parseInt(teacherInfo.get(id).get(1)));
						teacherUser.setName(teacherInfo.get(id).get(2));
						teacherUser.setTel(id);

						teacherMain.TeacherMainmenu(teacherUser);
					}

				}

			}
			// when enter wrong info
			if(!teacherUser.loginFlag) {
				System.out.println("\t\t\t아이디와 비밀번호를 다시 입력해주세요.");
			} 
			// logout
			else {
				System.out.println("\t\t\t로그아웃을 진행합니다.");
				scan.nextLine();
				teacherUser.loginFlag = false;
			}

			stat.close();
			conn.close();

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getSsn() {
		return ssn;
	}

	public void setSsn(String ssn) {
		this.ssn = ssn;
	}

}
