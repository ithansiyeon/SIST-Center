package com.test.teacher;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import com.test.admin.DBUtil;

import oracle.jdbc.OracleTypes;

/**
 * 교사 - 교사평가 조회 클래스입니다.
 * @author 희준
 *
 */
public class TeacherCheckRating {

   
 /**
  * 
  * @param tuser 선생님 객체
  */
   public void procPrintTeacherMyRating(TeacherUser tuser) {

      Connection conn = null;
      CallableStatement stat = null;
      DBUtil util = new DBUtil();
      ResultSet rs = null;
      Scanner scan = new Scanner(System.in);
      
      Statement stat2 = null;
      ResultSet rs2 = null;
            
      try {
         
         //프로시저 호출 준비
         String sql = "{ call procPrintTeacherMyRating(?,?) }";
         
         conn = util.open("211.63.89.64", "project", "java1234");
         stat = conn.prepareCall(sql);
               
         // 교사번호를 매개변수로 설정한다.
         stat.setInt(1, tuser.getNum());
         
         stat.registerOutParameter(2, OracleTypes.CURSOR);
         
         stat.executeQuery();
                  
         rs = (ResultSet)stat.getObject(2); 
         
         
         String opencoursnum = "";
         String description = "";
         String score = "";
         String ocName = "";
         
         
         // opencoursenum, 과정명 찍기 위해 커서 next()
         
         //과정명을 담을 배열
         String[] s_array = new String[100]; 
         //항목별 문항을 담을 배열
         String[] t_array = new String[600]; 
         //항목별 점수를 담을 배열
         String[] i_array = new String[600];
         
         // 과정명 가져오기
         try {
        	 
        	//교사가 가르친 과정명을 출력.
            stat2 = conn.createStatement();
            sql = String.format("select name from tblallcourse ac inner join tblopencourse oc on ac.num = oc.allcoursenum where oc.teachernum  = %s", tuser.getNum());            
            
            rs2 = stat2.executeQuery(sql);
            
            
            
            int k = 0;
            
            while(rs2.next()) {
               
               //과정명을 배열에 담는다
               s_array[k] =  rs2.getString("name");
                        
               k++;
                                 
            }
            

            int i = 0;
            int j = 0;
            
            while(rs.next()) { 
               
               // 교사 평가 항목별 문항을 배열에 담는다.
               t_array[j] = rs.getString("description");
               // 교사 평가 항목별 점수를 배열에 담는다.
               i_array[i] = rs.getString("score");
               
               i++;
               j++;
            }
            
            
            int b=0;
            int d=1;
            
            // s_array[a] -> 방배열 마다 과정명 출력
            // t_array[b] & i_array[b] -> 방 배열  (6배수 -1) 마다  1~6 항목 출력
            // ex) s_array[1] -> 2번째 과정명 / t_array[6] & i_array[6] ~ t_array[11] & i_array[11] -> 2번째 과정 항목별 문항 & 점수 
            //과정별 항목별 문항과 점수를 출력한다.
            for(int a = 0; a < 100; a++) {
            	
            	//과정명이 더이상 없을떄 break
            	if(s_array[a]== null) {
            		break;
            	}
            	
               System.out.println();
               System.out.print("\t\t\t[시행과정명]\t");   
               System.out.println("" + s_array[a]);
               System.out.print("\t\t\t----------------------------------");
               System.out.println("\n\t\t\t[평가항목]\t\t\t\t\t\t[점수]");
               
               
               
               for(int c = 1; b < 600 & c<7; b++, c++) {
                  
                  System.out.print("\t\t\t" + c +"."+ t_array[b]);
                  System.out.printf("\t\t\t\t" + i_array[b] + '점' + "\n");
                  
                  //다음 과정으로 조회하기 위해  break를 한다.
                  if(b == 6 * d - 1 ) {
       	  
                	  d++;
                	  break;
                  }
                  
                  
               }
               
               //다음 과정명을 조회하기 위해한 설정
               b = 6 * (d-1);
              
            }
        
      
            
            rs2.close();
         } catch (Exception e) {
            e.printStackTrace();
         }
         
         
         rs.close();
         stat.close();
         conn.close();
         
      
         
      } catch (Exception e) {
         System.out.println("교사 자신이 자신의 평가 점수 조회를 실패했습니다.");
         e.printStackTrace();
      }

      System.out.println();
      System.out.println("\t\t\t엔터를 입력하시면 이전 페이지로 돌아갑니다.");
      scan.nextLine();
      
      
   }//교사 자신이 자신의 평가 점수 조회(끝)  
   
   
}//class