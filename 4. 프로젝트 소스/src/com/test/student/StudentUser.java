package com.test.student;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

import com.test.admin.DBUtil;

import oracle.jdbc.OracleTypes;

/**
 * 교육생 로그인 및 데이터 입력을 위한 클래스입니다.
 * @author leeho
 *
 */
public class StudentUser {

	int num;
	String name;
	String tel;
	String ssn;
	String registerDate; 
	boolean loginFlag = false;
	
	/**
	 * 로그인 유효성 검사 및 객체에 대한 데이터 입력을 위한 메소드입니다.
	 * @param studentUser 로그인 성공 시 데이터를 입력하기 위한 객체입니다. 
	 */
	public void login(StudentUser studentUser) {

		// Database connection
		// 데이터베이스 연동
		Connection conn = null;
		Statement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		Scanner scan = new Scanner(System.in);

		// Variable for student Info
		// 관리자 계정 데이터를 넣어줄 변수
		HashMap<String, ArrayList<String>> studentInfo = new HashMap<String, ArrayList<String>>();

		try {

			conn = util.open("211.63.89.64", "project", "java1234");
			stat = conn.createStatement();

			String sql = String.format("select * from tblStudent");
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
					temp.add(rs.getString("registerdate"));
					studentInfo.put(rs.getString("tel"), temp);
				}

			}

			// input id, pw
			System.out.print("\t\t\t▷ ID: ");
			String inputId = scan.nextLine();
			System.out.print("\t\t\t▷ PW: ");
			String inputPw = scan.nextLine();

			// iterator
			Iterator<String> keys = studentInfo.keySet().iterator();

			// loginInfo search
			for (String id : studentInfo.keySet()) {
				// id matching
				if (id.equals(inputId)) {

					// password get
					String ssn = studentInfo.get(id).get(0);
					String pw = ssn.substring(ssn.indexOf('-') + 1);

					// pw matching
					if (pw.equals(inputPw)) {

						
						StudentMain studentMain = new StudentMain();

						// login on
						studentUser.loginFlag = true;
						
						// set info
						studentUser.setSsn(studentInfo.get(id).get(0));
						studentUser.setNum(Integer.parseInt(studentInfo.get(id).get(1)));
						studentUser.setName(studentInfo.get(id).get(2));
						studentUser.setRegisterDate(studentInfo.get(id).get(3));
						studentUser.setTel(id);
						
						System.out.println("\t\t\t――――――――――――――――――――――――――――――――――――――――――――");
						System.out.println();
						
						//학생정보 출력
						procStudentInfoByStudent(studentUser);
						
						System.out.println("\t\t\t――――――――――――――――――――――――――――――――――――――――――――");
						System.out.println();
						
						// start main menu
						studentMain.StudentMainmenu(studentUser);
						
					}

				}

			}
			
			// when enter wrong info
			if(!studentUser.loginFlag) {
				System.out.println("\t\t\t아이디와 비밀번호를 다시 입력해주세요.");
			} 
			// logout
			else {
				System.out.println("\t\t\t로그아웃을 진행합니다.");
				scan.nextLine();
				studentUser.loginFlag = false;
			}

			stat.close();
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
public void procStudentInfoByStudent(StudentUser sUser) {
		
		Connection conn = null;
		CallableStatement stat = null;
		ResultSet rs = null;
		DBUtil util = new DBUtil();
		
		try {
			
			//프로시저 호출 준비
			conn = util.open("211.63.89.64","project","java1234");
			String sql = "{ call procStudentInfoByStudent(?,?) }";
			stat = conn.prepareCall(sql);
			
			//매개변수 설정

			stat.setInt(1, sUser.getNum());						//학생 번호
			stat.registerOutParameter(2, OracleTypes.CURSOR);	//결과셋

			//프로시저 실행
			stat.executeQuery();
			
			//결과값 가져오기
			rs = (ResultSet)stat.getObject(2);
			
			//출력하기
			while(rs.next()) {
				System.out.printf("\t\t\t이름: %s | 연락처: %s \n\t\t\t수강과정: %s \n\t\t\t과정기간: %s | 강의실: %s\n"
																							, rs.getString("studentName")
																							, rs.getString("studentTel")
																							, rs.getString("courseName")
																							, rs.getString("period")
																							, rs.getString("classRoom"));
				System.out.println();
			}
			//010-1212-3893
			//1732673
			//접속종료
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

	public String getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(String registerDate) {
		this.registerDate = registerDate;
	}

	public StudentUser() {
		// TODO Auto-generated constructor stub
	}

}
