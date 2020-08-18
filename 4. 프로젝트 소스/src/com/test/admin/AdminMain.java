package com.test.admin;

import java.util.Scanner;

/**
 * 관리자 로그인 시 메인 메뉴 클래스입니다.
 * @author leeho
 *
 */
public class AdminMain {

	/**
	 * 관리자 메뉴 목록을 출력하는 메소드입니다.
	 * @param adminUser 관리자 객체
	 */
	public void mainmenu(AdminUser adminUser) {
		
		
		AdminUser user = adminUser;
		

		Scanner scan = new Scanner(System.in);
		String sel = "";

		while (true) {
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓  M E N U 〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t1. 기초 정보 관리");
			System.out.println("\t\t\t2. 교사 계정 관리");
			System.out.println("\t\t\t3. 개설 과정 관리");
			System.out.println("\t\t\t4. 개설 과목 관리");
			System.out.println("\t\t\t5. 교육생 관리");
			System.out.println("\t\t\t6. 시험 관리");
			System.out.println("\t\t\t7. 출결 관리");
			System.out.println("\t\t\t8. 기자재 관리");
			System.out.println("\t\t\t0. 로그아웃");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.print("\t\t\t▷ 입력: ");
			sel = scan.nextLine();
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println();
			
			// 사용자에게 번호 입력받음

			// 기초 정보 관리
			if (sel.equals("1")) {
				AdminBasic basic = new AdminBasic();
				basic.basicMenu();
			}
			// 교사 계정 관리
			else if (sel.equals("2")) {
				AdminTeacher teacher = new AdminTeacher();
				teacher.manageTeacherMenu();
			}
			// 개설 과정 관리
			else if (sel.equals("3")) {
				AdminOpencourse opencourse = new AdminOpencourse();
				opencourse.menu();
			}
			// 개설 과목 관리
			else if (sel.equals("4")) {
				AdminSubject subject = new AdminSubject();
				subject.manageSubjectmenu();
			}
			// 교육생 관리
			else if (sel.equals("5")) {
				AdminStudent student = new AdminStudent();
				student.menu();
			}
			// 시험 관리
			else if (sel.equals("6")) {
				AdminExam exam = new AdminExam();
				exam.manageExamMenu();
			}
			// 출결 관리
			else if (sel.equals("7")) {
				AdminAttendanceM attendance = new AdminAttendanceM();
				attendance.manageAttendanceMenu();
			}
			// 기자재 관리
			else if (sel.equals("8")) {
				AdminEquipment equipment = new AdminEquipment();
				equipment.manageEquipMenu();
			}
			// 뒤로 가기
			else if (sel.equals("0")) {
				break;
			}
			// 예외
			else {
				System.out.println("\t\t\t번호를 다시 입력해주세요");
			}
		}

	}
}
