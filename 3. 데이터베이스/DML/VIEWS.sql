--------------------------------------------------------
--  DDL for View VWALLATTENDANCE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PROJECT"."VWALLATTENDANCE" ("STUNAME", "COURSENAME", "ENTERT", "OUTT", "STATUS") AS 
  SELECT "STUNAME","COURSENAME","ENTERT","OUTT","STATUS"
from(
SELECT s.name as stuName ,ac.name as courseName
,to_char(a.enterTime,'yy/mm/dd hh24:mi:ss') as enterT,
        to_char(a.outtime,'yy/mm/dd hh24:mi:ss') as outT,a.status as status
FROM tblAttendance a join tblcoursehistory h
    on a.coursehistorynum = h.num
        join tblstudent s    
        on s.num = h.studentnum
            join tblallcourse ac
                on ac.num = h.opencoursenum
order by enterTime)
;
--------------------------------------------------------
--  DDL for View VWALLCOURSE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PROJECT"."VWALLCOURSE" ("allCourseNum", "courseName", "subjectPeriod", "subjectName", "CAPACITY", "coursePeriod") AS 
  select a.num as "allCourseNum", a.name as "courseName", sc.period as "subjectPeriod", s.name as "subjectName",capacity, a.period as "coursePeriod" from tblallCourse a
    inner join tblsubjectbycourse sc
        on sc.allcoursenum = a.num
            inner join tblSubject s
                on s.num = sc.subjectnum
                    where a.deletestatus <> 1 and s.deletestatus <> 1 order by a.num,sc.period
;
--------------------------------------------------------
--  DDL for View VWALLCOURSE1
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PROJECT"."VWALLCOURSE1" ("ALLCOURSENUM", "COURSENAME", "CAPACITY", "COURSEPERIOD") AS 
  select num as allCourseNum,name as courseName,capacity,period as coursePeriod from tblallCourse where deletestatus <> 1 order by num
;
--------------------------------------------------------
--  DDL for View VWAVAILABLECOURSE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PROJECT"."VWAVAILABLECOURSE" ("COURSENUM", "COURSENAME", "COURSECAPACITY", "COURSEPERIOD") AS 
  select num as courseNum,name as courseName,capacity as courseCapacity,period as coursePeriod from tblallCourse where deletestatus <> 1 order by num
;
--------------------------------------------------------
--  DDL for View VWBASICDATA
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PROJECT"."VWBASICDATA" ("과정명", "과목명", "교재명", "강의실명", "강의실정원") AS 
  SELECT ac.name as 과정명, ts.name as 과목명,tb.name as 교재명, cr.name as 강의실명,cr.capacity as 강의실정원
FROM tblallcourse ac join tblsubjectbycourse sbc
        on ac.num = sbc.allcoursenum
            join tblsubject ts
            on sbc.subjectnum = ts.num
                join tblbook tb
                on tb.subjectnum = ts.num
                        join tblopencourse oc 
                            on oc.allcoursenum = ac.num
                                join tblclassroom cr
                                 on oc.classroomnum = cr.num
;
--------------------------------------------------------
--  DDL for View VWCHECKCONSULTREQUEST
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PROJECT"."VWCHECKCONSULTREQUEST" ("CONSULTREQNUM", "RDATE", "STUDENTNAME", "STATUS") AS 
  select cr.num as consultReqNum, cr.requsetdate as rDate, st.name as studentname, status from tblconsultRequest cr inner join tblcoursehistory ch on cr.coursehistorynum = ch.num inner join tblstudent st on st.num = ch.studentnum
where st.deletestatus = 0 and cr.num not in (select consultrequestnum from tblconsultlog) order by rdate
;
--------------------------------------------------------
--  DDL for View VWCOURSEHISTORY
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PROJECT"."VWCOURSEHISTORY" ("student name", "student num", "OPENCOURSENUM", "TEACHERNUM", "ALLCOURSENUM") AS 
  select s.name as "student name",s.num as "student num",h.openCourseNum, o.teacherNum, o.allCourseNum from tblstudent s
    inner join tblcourseHistory h
        on s.num = h.studentNum
            inner join tblopenCourse o
                on o.num = h.openCourseNum
                     where h.status <> '수료' and h.status <> '중도탈락' and s.deletestatus <> 1 and h.deletestatus <> 1 and o.status <> 1
                         order by s.name,h.openCourseNum
;
--------------------------------------------------------
--  DDL for View VWCOURSEINFOBYTEACHER
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PROJECT"."VWCOURSEINFOBYTEACHER" ("TEACHERNUM", "TEACHERNAME", "COURSENAME", "PERIOD", "CLASSROOM", "COURSESTATUS") AS 
  select 
    t.num as teacherNum,
    t.name as teacherName,
    a.name as courseName,
    to_char(o.startDate, 'yyyy-mm-dd')|| ' ~ ' ||  to_char(o.endDate, 'yyyy-mm-dd') as period,
    o.classroomNum || '강의실' as classRoom,
    case
        when sysdate between o.startdate and o.enddate then '시행중' 
        when sysdate < o.startdate then '강의 예정' 
        when sysdate > o.enddate then '강의 종료'
    end as courseStatus
from tblTeacher t 
    inner join tblOpenCourse o
        on t.num = o.teachernum
            inner join tblAllcourse a
                on o.allcourseNum = a.num
                    order by teacherNum
;
--------------------------------------------------------
--  DDL for View VWHIREDGRADESMANSELECT
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PROJECT"."VWHIREDGRADESMANSELECT" ("NUM", "COURSEHISTORYNUM", "COMPANY", "SALARY", "STATUS") AS 
  select num,courseHistoryNum,company,salary,status from tblhiredGraduates order by num
;
--------------------------------------------------------
--  DDL for View VWHIREDGRADUATESSELECT
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PROJECT"."VWHIREDGRADUATESSELECT" ("NUM", "COURSEHISTORYNUM", "COMPANY", "SALARY", "STATUS", "employment insurance", "teacherNum") AS 
  select a."NUM",a."COURSEHISTORYNUM",a."COMPANY",a."SALARY",a."STATUS", decode(status,'재직중','O','X') as "employment insurance", (select o.teacherNum from tblcourseHistory h
                                                                                    inner join tblopenCourse o
                                                                                      on o.num = h.openCourseNum
                                                                                            where a.courseHistoryNum = h.num and h.deletestatus <> 1 and o.status <> 1) as "teacherNum" from tblhiredGraduates a order by num
;
--------------------------------------------------------
--  DDL for View VWNOTDISTRIBUTED
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PROJECT"."VWNOTDISTRIBUTED" ("COURSENAME", "STUDENTNAME", "BOOKNAME") AS 
  select 
         --시행번호가 같은 과정명과 학생이름을 가져옴.
        (select name from tblallcourse ac where ac.num = oc.allCourseNum) as courseName,
        (select name from tblStudent where num = ch.num) as StudentName,
        b.name as BookName   
    from tblopencourse oc
        inner join tblcoursehistory ch
            on oc.num = ch.opencoursenum
                 inner join tblDistribution d
                        on ch.num = d.courseHistoryNum
                            inner join tblBook b
                                on d.bookNum = b.num
                                    --미완료(0) 내역만 출력
                                    where d.status = 0
                                         order by courseName, studentName
;
--------------------------------------------------------
--  DDL for View VWOPENCOURSE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PROJECT"."VWOPENCOURSE" ("NUM", "STARTDATE", "ENDDATE", "CLASSROOMNUM", "TEACHERNUM", "ALLCOURSENUM", "STATUS") AS 
  select "NUM","STARTDATE","ENDDATE","CLASSROOMNUM","TEACHERNUM","ALLCOURSENUM","STATUS" from tblopenCourse where status <> 1 order by num
;
--------------------------------------------------------
--  DDL for View VWOPENCOURSECLASSROOM
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PROJECT"."VWOPENCOURSECLASSROOM" ("openingCourseNumber", "startDate", "endDate", "classRoomNum") AS 
  select o.num as "openingCourseNumber", o.startDate as "startDate", o.endDate as "endDate", o.classRoomNum as "classRoomNum" from tblopenCourse o
            where not sysdate between o.startDate and o.endDate and o.classRoomNum is null and o.status <> 1
;
--------------------------------------------------------
--  DDL for View VWOPENCOURSEINFO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PROJECT"."VWOPENCOURSEINFO" ("openCourseNum", "opening course name", "opening course period", "classroom name", "register opening course", "trainee registration personnel") AS 
  select o.num as "openCourseNum",(select name from tblallcourse where num = o.allcoursenum) as "opening course name",o.startDate || '~' || o.endDate as "opening course period",(select name from tblclassroom where num = o.classroomnum) as "classroom name"
   ,decode((select distinct allCourseNum from tblSubjectByCourse where o.allcoursenum = allCourseNum),null,'X','O')  as "register opening course"
   , (select count(*) from tblcourseHistory h where h.openCourseNum = o.num and h.deletestatus <> 1) as "trainee registration personnel"
from tblopenCourse o
          where o.status <> 1
                    order by o.num
;
--------------------------------------------------------
--  DDL for View VWOPENCOURSEINFO2
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PROJECT"."VWOPENCOURSEINFO2" ("OPENCOURSENUM", "OPENCOURSENAME", "period", "classroomname", "register opening course", "trainee registration personnel") AS 
  select o.num as opencoursenum,a.name as opencoursename,o.startDate || ' ~ ' || o.endDate as "period",c.name as "classroomname"
   ,decode((select distinct allCourseNum from tblSubjectByCourse where a.num = allCourseNum),null,'X','O')  as "register opening course"
   , (select count(*) from tblcourseHistory h where h.openCourseNum = o.num and h.status = '수강중' and h.deletestatus <> 1) as "trainee registration personnel"
from tblopenCourse o
    inner join  tblallCourse a
        on o.allCourseNum = a.num
             inner join tblclassRoom c
                 on o.classRoomNum = c.num
                    where o.status <> 1 and a.deletestatus <> 1
;
--------------------------------------------------------
--  DDL for View VWOPENCOURSELIST
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PROJECT"."VWOPENCOURSELIST" ("OPENCOURSENUM", "COURSENAME", "STARTDATE", "ENDDATE") AS 
  select oc.num as opencoursenum, ac.name as coursename, oc.startdate as startdate, oc.enddate as enddate from tblopencourse oc inner join tblallcourse ac on oc.allcoursenum = ac.num where oc.status = 0
;
--------------------------------------------------------
--  DDL for View VWOPENCOURSENAME
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PROJECT"."VWOPENCOURSENAME" ("OPENCOURSENUM", "COURSENAME") AS 
  select o.num as openCourseNum, a.name as courseName from tblopenCourse o
    inner join tblAllCourse a
        on o.allCourseNum = a.num 
            where o.status <> 1 and a.deletestatus <> 1
                order by o.num
;
--------------------------------------------------------
--  DDL for View VWOPENCOURSESELECTENDDATE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PROJECT"."VWOPENCOURSESELECTENDDATE" ("courseHistoryNum", "OPENCOURSENUM", "STATUS", "student name", "ENDDATE") AS 
  select h.num as "courseHistoryNum", h.openCourseNum, h.status, s.name as "student name", h.endDate from tblcourseHistory h 
    inner join tblstudent s
        on s.num = h.studentnum
            where h.status = '수료' and h.deletestatus <> 1 and s.deleteStatus <> 1 order by h.num
;
--------------------------------------------------------
--  DDL for View VWOPENCOURSESTUDENTSELECT
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PROJECT"."VWOPENCOURSESTUDENTSELECT" ("NUM", "studentName", "studentSsn", "studentTel", "registrationDate", "completionStatus") AS 
  select o.num, st.name as "studentName",substr(st.ssn,8) as "studentSsn", st.tel as "studentTel",to_char(st.registerDate,'yyyy-mm-dd') as "registrationDate",c.status as "completionStatus" from tblopenCourse o 
     inner join tblcourseHistory c
        on c.openCourseNum = o.num
            inner join tblstudent st
                 on st.num = c.studentnum
                    where o.status <> 1 and st.deleteStatus <> 1 and c.deleteStatus <> 1
                        order by o.num, st.registerdate,st.name
;
--------------------------------------------------------
--  DDL for View VWOPENCOURSESUBJECTSELECT
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PROJECT"."VWOPENCOURSESUBJECTSELECT" ("NUM", "subjectName", "SubjectDuration", "textbookName", "teacherName") AS 
  select o.num,s.name as "subjectName",p.startDate || '~' || p.endDate as "SubjectDuration", b.name as "textbookName", t.name as "teacherName" from tblopenCourse o 
    inner join tblperiodBySubject p
        on p.openCourseNum = o.num
            inner join tblSubjectByCourse sc
                on sc.num = p.subjectByCourseNum
                    inner join tblSubject s
                        on s.num = sc.subjectNum
                            inner join tblBook b
                                on b.subjectNum = s.num
                                    inner join tblteacher t
                                    on t.num = o.teacherNum
                                        where o.status <> 1 and s.deletestatus <> 1 and b.deletestatus <> 1 and t.deletestatus <> 1
                                            order by o.num, p.startDate
;
--------------------------------------------------------
--  DDL for View VWPRINTSCORE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PROJECT"."VWPRINTSCORE" ("WRITTENTETRATIO", "PERFORMANCETETRATIO", "ATTENDANCERATIO") AS 
  select e.writtentestratio as writtentetratio
   ,e.performancetestratio as performancetetratio
   ,e.attendanceratio as attendanceratio
    from tblteacher t join tblopencourse o
         on t.num = o.teachernum
                 join tblexam e
                     on o.num = e.opencoursenum
                        join tblscoreBysubject b
                            on  e.num = b.examnum
;
--------------------------------------------------------
--  DDL for View VWPRINTSTUDENTLIST
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PROJECT"."VWPRINTSTUDENTLIST" ("NAME", "SSN", "TEL", "REGISTERDATE", "수강횟수") AS 
  select s.name, s.ssn, s.tel, s.registerdate, count(ch.studentnum) as 수강횟수 from tblStudent s -- 학생 테이블
        inner join tblcoursehistory ch -- 수강내역 테이블
           on s.num = ch.studentnum 
                group by s.name, s.ssn, s.tel, s.registerdate
;
--------------------------------------------------------
--  DDL for View VWSUBJECT
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PROJECT"."VWSUBJECT" ("NUM", "subjectPeriod", "subjectName", "CAPACITY", "coursePeriod") AS 
  select a.num, sc.period as "subjectPeriod", s.name as "subjectName",capacity, a.period as "coursePeriod" from tblallCourse a
    inner join tblsubjectbycourse sc
        on sc.allcoursenum = a.num
            inner join tblSubject s
                on s.num = sc.subjectnum
                    where a.deletestatus <> 1 and s.deletestatus <> 1 order by a.num,sc.period
;
--------------------------------------------------------
--  DDL for View VWTEACHER
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PROJECT"."VWTEACHER" ("TEACHERNUM", "availablesubjectName") AS 
  select t.num as teacherNum, s.name "availablesubjectName" from tblteacher t
    inner join tblavailablesubject a
        on t.num = a.teachernum
            inner join tblSubject s
                on s.num = a.subjectnum
                      where t.deletestatus <> 1 and s.deletestatus <> 1
                            order by teacherNum
;
--------------------------------------------------------
--  DDL for View VWTEACHERATTENDANCE
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PROJECT"."VWTEACHERATTENDANCE" ("openCourseNum", "ALLCOURSENUM", "subject name", "teacher name", "TEACHERNUM") AS 
  select DISTINCT o.num as "openCourseNum",o.allCourseNum,(select name from tblallCourse where o.allCourseNum = num) as "subject name",t.name as "teacher name", o.teacherNum from tblopenCourse o
        inner join tblteacher t
            on o.teacherNum = t.num 
                inner join tblcourseHistory h
                    on h.opencoursenum = o.num
                         where o.status <> 1 and t.deletestatus <> 1 and h.deletestatus <> 1 and h.status <> '수료' and h.status <> '중도탈락'
                                 order by allCourseNum
;
--------------------------------------------------------
--  DDL for View VWTEACHERINFO
--------------------------------------------------------

  CREATE OR REPLACE FORCE VIEW "PROJECT"."VWTEACHERINFO" ("NUM", "NAME", "PW", "TEL", "DELETESTATUS") AS 
  select num, name, substr(ssn, 8, 7) as pw, tel, deleteStatus from tblTeacher
;
