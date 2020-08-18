package com.test.teacher;

import java.util.Scanner;

/**
 * 교사 메인 메뉴를 위한 클래스입니다.
 * @author leeho
 *
 */
public class TeacherMain {
	
	/**
	 * 교사 메인 메뉴를 출력하는 메소드입니다.
	 * @param teacherUser 로그인 시 데이터를 입력받은 객체
	 */
	public void TeacherMainmenu(TeacherUser teacherUser) {
		
		
		Scanner scan = new Scanner(System.in);
		String sel = "";
		
		while (true) {
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓 M E N U 〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t1. 강의 스케줄 조회");
			System.out.println("\t\t\t2. 배점 입출력");
			System.out.println("\t\t\t3. 성적 입출력");
			System.out.println("\t\t\t4. 출결 관리 및 출결 조회");
			System.out.println("\t\t\t5. 교사 평가 조회");
			System.out.println("\t\t\t6. 상담 내역 관리");
			System.out.println("\t\t\t0. 로그아웃");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.print("\t\t\t▷ 입력: \n");
			// 사용자에게 번호 입력받음
			sel = scan.nextLine();

			// 강의 스케줄 조회
			if (sel.equals("1")) {
				TeacherCheckSchedule checkSchedule = new TeacherCheckSchedule();
				checkSchedule.procCheckTeaching(teacherUser);
				
			}
			// 배점 입출력
			else if (sel.equals("2")) {
				TeacherInsertRatio ratio = new TeacherInsertRatio();
				ratio.insertRatio(teacherUser);
			}
			// 성적 입출력
			else if (sel.equals("3")) {
				TeacherInsertScore score = new TeacherInsertScore();
				score.insertScoreMenu(teacherUser);
			}
			// 출결 관리 및 출결 조회
			else if (sel.equals("4")) {
				TeacherAttendance attendance = new TeacherAttendance();
				attendance.menu(teacherUser);
			}
			// 교사 평가 조회
			else if (sel.equals("5")) {
				TeacherCheckRating rating = new TeacherCheckRating();
				rating.procPrintTeacherMyRating(teacherUser);
			} 
			// 상담 신청 관리
			else if (sel.equals("6")) {
				TeacherManageConsult manageConsult = new TeacherManageConsult();
				manageConsult.manageConsultRq();
			}
			// 뒤로 가기
			else if (sel.equals("0")) {
				break;
			} 
			// 예외
			else {
				System.out.println("번호를 다시 입력해주세요");
			}
		}
	}

}
