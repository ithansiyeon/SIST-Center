import java.util.Scanner;

import com.test.admin.AdminUser;
import com.test.student.StudentUser;
import com.test.teacher.TeacherUser;
/**
 * 메인 메뉴 시작을 위한 클래스입니다.
 * @author Team 2
 *
 */
public class main {
	
	public static void main(String[] args) {
		
		Scanner scan = new Scanner(System.in);
		
		while (true) {
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.println("\t\t\t\t            교육센터 관리 시스템");
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

			System.out.println("\t\t\t1. 관리자 로그인");
			System.out.println("\t\t\t2. 교사 로그인");
			System.out.println("\t\t\t3. 교육생 로그인");
			System.out.println("\t\t\t0. 종료");

			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
			System.out.print("\t\t\t▷ 입력: ");

			String sel = scan.nextLine();
			System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");

			// 관리자 로그인
			if (sel.equals("1")) {
				AdminUser adminUser = new AdminUser();
				adminUser.login(adminUser);
			} 
			// 교사 로그인
			else if (sel.equals("2")) {
				TeacherUser teacherUser = new TeacherUser();
				teacherUser.login(teacherUser);
			} 
			// 교육생 로그인
			else if (sel.equals("3")) {
				StudentUser studentUser = new StudentUser();
				studentUser.login(studentUser);
			} 
			// 종료
			else if (sel.equals("0")) {
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				System.out.println("\t\t\t프로그램을 종료합니다."); // needMore
				System.out.println("\t\t\t〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓");
				break;
			} 
			// 예외
			else {
				System.out.println("\t\t\t번호를 다시 입력해주세요.");
			}
		}

	}
}
