package com.test.student;

import java.util.Scanner;

/**
 * 교육생 메인 메뉴를 출력하는 메소드를 멤버로 갖는 클래스입니다.
 * @author Doyun Lee
 *
 */
public class StudentMain {
	
	/**
	 * 메인 메뉴를 출력하고 번호를 입력받는 메소드입니다.
	 * @param studentUser 로그인 시 생성된 StudentUser를 매개변수로 받아와 세부 메뉴의 메소드에 매개 변수로 다시 넘겨줍니다.
	 */
	public void StudentMainmenu(StudentUser studentUser) {
		
		// 우리가 메뉴에서 사용할 학생 유저
		Scanner scan = new Scanner(System.in);
		String sel = "";
		
		while(true) {
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓  M E N U 〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t1. 성적 조회");
			System.out.println("\t\t\t2. 출결 관리");
			System.out.println("\t\t\t3. 상담 신청");
			System.out.println("\t\t\t4. 교사 평가");
			System.out.println("\t\t\t5. 취업 활동");
			System.out.println("\t\t\t0. 로그아웃");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			// 사용자에게 번호 입력받음
			System.out.print("\t\t\t▷입력: ");
			sel = scan.nextLine();
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println();

			
			// 성적 조회
			if(sel.equals("1")) {
				StudentCheckScore checkScore = new StudentCheckScore();
				checkScore.studentScoreMain(studentUser);
			}
			// 출결 관리 및 출결 조회
			else if(sel.equals("2")) {
				StudentAttendance attendance = new StudentAttendance();
				attendance.printAttendanceMenu(studentUser);
			}
			// 상담신청
			else if(sel.equals("3")) {
				StudentConsult consulting = new StudentConsult();
				consulting.requestConsulting(studentUser);
			}
			// 교사 평가
			else if(sel.equals("4")) {
				Studentrating rating = new Studentrating();
				rating.procAddSocreByRating(studentUser);
			}
			// 취업 활동목록
			else if(sel.equals("5")) {
				StudentJob job = new StudentJob();
				job.procStudentjobActivity(studentUser);
			}
			// 뒤로 가기
			else if(sel.equals("0")) {
				break;
			} 
			// 예외
			else {
				System.out.println("\t\t\t번호를 다시 입력해주세요.");
			}
		}
		
	}
}
