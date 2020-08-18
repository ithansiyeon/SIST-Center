--------------------------------------------------------
--  DDL for Procedure ADDSCOREBYSUBJECT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."ADDSCOREBYSUBJECT" (
    pCourseHistoryNum number,       -- 수강내역번호
    pExamNum number,                -- 시험번호
    pWrittenTestScore number,       -- 필기점수
    pPerformanceTestScore number,   -- 실기 점수
    pAttendanceScore number         -- 출석 점수
)
is
begin
    insert into tblscoreBySubject 
        values (scoreBySubjectseq.nextval, pCourseHistoryNum, pExamNum
                    , pWrittenTestScore, pPerformanceTestScore, pAttendanceScore);
end; 

/
--------------------------------------------------------
--  DDL for Procedure POCADDSCOREBYSUBJECT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."POCADDSCOREBYSUBJECT" (
    pCourseHistoryNum number,       -- 수강내역번호
    pExamNum number,                -- 시험번호
    pWrittenTestScore number,       -- 필기성적
    pPerformanceTestScore number,   -- 실기성적
    pAttendanceScore number         -- 출석성적
)
is
    
begin    

    if (select enddate from tblperiodbysubject) < sysdate then

    insert into tblscoreBySubject 
        values (scoreBySubjectseq.nextval, pCourseHistoryNum, pExamNum
                    , pWrittenTestScore, pPerformanceTestScore, pAttendanceScore);

    end if;
end; 

/
--------------------------------------------------------
--  DDL for Procedure PRCOINSERTACTIVITY
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PRCOINSERTACTIVITY" 
(
   
    psnum number,
    pcontent varchar2
)
is
begin
    insert into tbljobActivity values (JOBACTIVITYSEQ.nextval,
                                        (select h.num
                                        from tblcoursehistory h join tblstudent s
                                        on h.studentnum = s.num
                                         where s.num = psnum),pcontent);
end;

/
--------------------------------------------------------
--  DDL for Procedure PRINTCONSULTCONTENT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PRINTCONSULTCONTENT" (
    pConsultRequestNum number,
    pDate out date,
    pContent out varchar2
)
is
begin
    select consultdate, consultcontent into pDate,pContent from tblconsultlog where consultrequestnum = pconsultrequestnum;
exception
    when others then
    pDate := null;
    pContent := '상담 대기중입니다.';
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCADDADMIN
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCADDADMIN" 
(
    pNum number,
    pId varchar2,
    pPassword varchar2
)
is
begin
insert into tblAdmin values (pNum, pId, pPassword);
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCADDATTENDANCE
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCADDATTENDANCE" (
    pattendanceseq number,
    pcoursehistorynum number,
    pentertime date,
    pouttime date,
    pstatus string
)
is
begin
        insert into tblattendance (attendanceseq, Coursehistorynum, entertime, outtime, status) 
        values (pattendanceseq, pcoursehistorynum, pentertime, pouttime, pstatus);
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCADDATTENDANCEENTERTIME
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCADDATTENDANCEENTERTIME" (
    pstnum number
)
is
    achnum number;
    vstatus tblAttendance.status%type;
begin


        select 
        case
            when to_char(sysdate, 'HH24' ) >= '09' then '지각'
            else '정상'
        end into vstatus
        from dual;

       select ch.num into achnum from tblstudent st
            inner join tblcoursehistory ch
                on ch.studentnum = st.num
                    where st.num = pstnum and ch.status = '수강중';


        insert into tblattendance
        values (attendanceseq.nextval, achnum, sysdate, null, vstatus);

end;

/
--------------------------------------------------------
--  DDL for Procedure PROCADDATTENDANCEENTERTIMETEST
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCADDATTENDANCEENTERTIMETEST" (
    pstnum number
)
is
    achnum number;
    vstatus varchar2(10);
begin


        select 
        case
            when to_char(sysdate, 'HH24' ) >= '09' then '지각'
            else '정상'
        end into vstatus
        from dual;

       select ch.num into achnum from tblstudent st
            inner join tblcoursehistory ch
                on ch.studentnum = st.num
                    where st.num = pstnum and ch.status = '수강중';


        insert into tblattendance
        values (attendanceseq.nextval, achnum, sysdate, null, vstatus);

end;

/
--------------------------------------------------------
--  DDL for Procedure PROCADDATTENDANCEOUTTIME
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCADDATTENDANCEOUTTIME" (
    pstnum number
)
is
    achnum number;
    vstatus tblAttendance.status%type;
begin

       select ch.num into achnum from tblstudent st
            inner join tblcoursehistory ch
                on ch.studentnum = st.num
                    where st.num = pstnum and ch.status = '수강중';

        select 
            case
                when to_char(sysdate, 'HH24' ) < '18' then '조퇴'
                when to_char(sysdate, 'HH24' ) >= '09' and to_char(sysdate, 'HH24' ) > '18' then '지각'
                else '정상'
             end
        into vstatus
        from dual;

        update tblattendance
        set
        outtime = sysdate,
        status = vstatus
        where 
        to_char(entertime, 'YYYY-MM-DD') = to_char(sysdate, 'YYYY-MM-DD')
        and coursehistorynum = achnum;

end;

/
--------------------------------------------------------
--  DDL for Procedure PROCADDAVAILABLESUBJECT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCADDAVAILABLESUBJECT" (
    ptnum number, --교사번호
    psnum number, --과목번호
    presult out number  --성공여부 반환값
)
is
    --중복되는지 확인할 변수
    vcnt number;

begin

    presult := 0;
    --1. 해당 교사의 수업가능 과목에 동일한 내역이 있는지 확인
    select count(*) into vcnt from tblAvailableSubject 
        where teacherNum = ptnum and subjectnum = psnum;
    dbms_output.put_line(vcnt);
    --2. 동일한 내역이 없으면 추가 
    if vcnt = 0 then
        insert into tblavailablesubject values (AVAILABLESUBJECTSEQ.nextval, ptnum, psnum);
         presult := 1;
    end if;

end;

/
--------------------------------------------------------
--  DDL for Procedure PROCADDBOOK
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCADDBOOK" (
    
    psubjectnum number,
    pbookname varchar2,
    pwriter varchar2,
    ppublisher varchar2,
    result out number
    
   
)
IS
BEGIN
result :=1;
INSERT INTO tblBook(num,subjectnum,name,writer,publisher) values(bookseq.nextVal,psubjectnum,pbookname,pwriter,ppublisher);

exception 
when others then
result := 0;
END procAddBook;

/
--------------------------------------------------------
--  DDL for Procedure PROCADDCLASSROOM
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCADDCLASSROOM" 
(
    pName varchar2,
    pCapacity number,
    result out number
)
is
begin
result :=1;
insert into tblClassRoom(num,name,capacity) values (classroomseq.nextVal, pName, pCapacity);

exception 
    when others then 
    result :=0;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCADDCONSULTLOG
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCADDCONSULTLOG" (
    pConsultReqNum number,
    pDate date,
    pContent varchar2
)
is
begin
    insert into tblconsultLog values (CONSULTLOGSEQ.nextval,pConsultReqNum,pDate,pContent);
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCADDCONSULTREQUEST
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCADDCONSULTREQUEST" (
    pStudentnum number
)
is
begin
    insert into tblconsultRequest values (COURSEHISTORYSEQ.nextval,(select num from tblcoursehistory where studentnum = pStudentnum and status = '수강중'),default);
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCADDDISTRIBUTION
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCADDDISTRIBUTION" 
(
    presult number,
    pbookNum number,
    pcourseNum number
)
IS
BEGIN
INSERT INTO tblDistribution VALUES(Distributionseq.nextVal,pbookNum,pcourseNum,0);
END;

/
--------------------------------------------------------
--  DDL for Procedure PROCADDDISTRIBUTIONBYCOURSE
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCADDDISTRIBUTIONBYCOURSE" (
    pnum number    --수강내역번호
)
is
    cursor vcursor is   --입력받은 수강내역번호에 해당하는 교재 번호만 가져옴
        select
            b.num
        from tblCourseHistory ch
            inner join tblOpenCourse oc
                on ch.opencourseNum = oc.num
                    inner join tblAllcourse ac
                        on oc.allcourseNum = ac.num
                            inner join tblSubjectByCourse sc
                                on ac.num = sc.allCourseNum
                                    inner join tblSubject sb
                                        on sc.subjectNum = sb.num
                                            inner join tblBook b
                                                on sb.num = b.subjectNum                               
                                                    where ch.num = pnum;
    vrow number;       --가져온 cursor의 값을 받아줄 변수

begin

    open vcursor;
    loop

         fetch vcursor into vrow; --레코드 1개 가져와서 변수에 복사
         exit when vcursor%notfound; --마지막 레코드 이후 loop 탈출
         insert into tblDistribution values (distributionseq.nextVal, vrow, pnum, 0);

    end loop;
    close vcursor;

end procAddDistributionByCourse;

/
--------------------------------------------------------
--  DDL for Procedure PROCADDEQUIPMENT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCADDEQUIPMENT" 
(
    pNum number,
    pName varchar2
)
is
begin
insert into tblEquipment values (pNum, pName);
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCADDEQUIPMENTBYCLASSROOM
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCADDEQUIPMENTBYCLASSROOM" (
/* 강의실별 기자재 데이터 삽입 프로시저 생성*/
    seq number,
    classroomNum number,
    equipmentNum number,
    qty number,
    buyingdate date
)
is 
begin
    insert into tblEquipmentByClassroom values (seq, classroomNum, equipmentNum, qty, buyingdate);
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCADDJOBACTIVITY
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCADDJOBACTIVITY" 
(
    pcourseNum number,
    pcontent varchar2,
    result out number
)
IS
BEGIN
result := 1;
INSERT INTO tblJobActivity VALUES(jobActivitySeq.nextval,
                                  pcourseNum,
                                  pcontent);
exception
when others then
result := 0;
END;

/
--------------------------------------------------------
--  DDL for Procedure PROCADDNEWSUBJECT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCADDNEWSUBJECT" (
    pName varchar2,             -- 과목 이름
    pEssentialType varchar2     -- 공통/필수 구분
)
is
begin
    -- 프로시저에 입력받은 과목이름과 공통/필수 구분을 insert
    insert into tblsubject values (SUBJECTSEQ.nextval,pName,pEssentialType,0);
end procAddNewSubject;

/
--------------------------------------------------------
--  DDL for Procedure PROCADDPERIODBYSUBJECT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCADDPERIODBYSUBJECT" 
(
    pnum number,
    pcoursenum number,
    psubjectnum number,
    pstartdate date,
    penddate date
   --presult number
)
IS   
BEGIN
INSERT INTO tblperiodBySubject(num,opencoursenum,subjectbycoursenum,startdate,enddate) VALUES (pnum,
                                      (SELECT num FROM tblOpenCourse WHERE num=pcoursenum), --과정번호
                                      (SELECT num FROM tblSubjectByCourse WHERE num=psubjectnum), --과정별과목번호
                                       pstartdate,
                                       penddate);
--presult := 1;
--exception 
--when others then 
--presult := 0;
END procAddperiodBySubject;

/
--------------------------------------------------------
--  DDL for Procedure PROCADDQUESTION
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCADDQUESTION" 
(
    pexamnum varchar2,
    ptype varchar2,
    pquestion varchar2,
    panswer varchar2,
    result out number
)
is
begin
result :=1;
    insert into tblquestionbyexam(num,examnum,type,question,answer) 
        values (EXAMSEQ.nextval,pexamnum,ptype,pquestion,panswer);
exception 
when others then
result :=0;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCADDSOCREBYRATING
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCADDSOCREBYRATING" (
    pcourseHistoryNum number,
    pnum1 number,
    pnum2 number,
    pnum3 number,
    pnum4 number,
    pnum5 number,
    pnum6 number   
)
is
    vnum number := ratingseq.nextval; -- 교사평가 번호
begin        

    -- 교사 평가      
    insert into tblrating values (vnum, pcourseHistoryNum, DEFAULT); 
    -- 교사평가 항목별 점수 넣기
    insert into tblScoreByRating values (scorebyratingseq.nextval, vnum, 1, pnum1);
    insert into tblScoreByRating values (scorebyratingseq.nextval, vnum, 2, pnum2);
    insert into tblScoreByRating values (scorebyratingseq.nextval, vnum, 3, pnum3);
    insert into tblScoreByRating values (scorebyratingseq.nextval, vnum, 4, pnum4);
    insert into tblScoreByRating values (scorebyratingseq.nextval, vnum, 5, pnum5);
    insert into tblScoreByRating values (scorebyratingseq.nextval, vnum, 6, pnum6);

end;

/
--------------------------------------------------------
--  DDL for Procedure PROCADDSTUDENT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCADDSTUDENT" (
    pName varchar2,
    pTel varchar2,
    pSsn varchar2
)
is
begin
    insert into tblStudent values(STUDENTSEQ.nextval, pName, pTel, pSsn, default, default); -- 학생 테이블에 추가
    -- insert into 학생아이디비밀번호테이블 values (pTel, substr(pSsn, 8, 7)); -- 학생 로그인 아디이&비밀번호 테이블에 추가
end procAddStudent;

/
--------------------------------------------------------
--  DDL for Procedure PROCADDSUBJECT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCADDSUBJECT" (
    psubject varchar2,
    pEssentialType varchar2,
    result out number
)
IS
BEGIN
result :=1;
INSERT INTO tblSubject(num,name,essentialtype) values(Subjectseq.nextVal,psubject,pEssentialType);

EXCEPTION 
    when others then 
    result :=0;
END procAddSubject;

/
--------------------------------------------------------
--  DDL for Procedure PROCADDSUBJECTNO
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCADDSUBJECTNO" (
    -- 사용자에게 개설 과목 정보를 입력받고, 
    pName varchar2
)
is
begin
    -- 과목에 입력받은 데이터 추가
    insert into tblsubject values (SUBJECTSEQ.nextval,pName, null,default);
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCADDSUBJECTYES
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCADDSUBJECTYES" (
    -- 사용자에게 개설 과목 정보를 입력받고, 
    pName varchar2,
    pEssentialType varchar2
)
is
begin
    -- 과목에 입력받은 데이터 추가
    insert into tblsubject values (SUBJECTSEQ.nextval,pName, pEssentialType,default);
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCADDSUPPORTLIST
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCADDSUPPORTLIST" 
(
    Scontent varchar2,
    result out number
)
is
begin
result := 1;
    insert into tblsupportlist Values (supportlistseq.nextVal,Scontent);
exception
when OTHERS then
result := 0;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCADDTEACHER
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCADDTEACHER" (
    pname varchar2,
    ptel varchar2,
    pssn varchar2,
    presult out number
)
is 
    vcnt number;    --입력받은 교사 정보와 같은 정보가 교사테이블에 존재하는지 확인해줄 변수
begin

    presult := 0;
    select count(*) into vcnt from tblTeacher where name = pname and tel = ptel and ssn = pssn;

    --입력받은 교사번호가 교사 테이블에 없는 경우
    if vcnt = 0  then
        insert into tblTeacher values (teacherSeq.nextval, pname, ptel, pssn, 0);
        presult := 1;
    end if;

end procAddTeacher;

/
--------------------------------------------------------
--  DDL for Procedure PROCADDTESTDATE
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCADDTESTDATE" 
(
    snum number,
    tnum number,
    cnum number,
    ptestdate date,
    result out number
)
is
begin
result := 1;
    update tblexam set testdate = ptestdate where num = (select e.num
                                                        from tblopencourse o join tblexam e
                                                                                on o.num = e.opencoursenum
                                                                             join tblsubjectbycourse sc
                                                                                on e.subjectbycoursenum = sc.num
                                                        where o.teachernum =tnum and o.allcoursenum =cnum and sc.subjectnum =snum and sysdate < (select enddate from tblperiodbysubject where subjectbycoursenum =snum and sysdate<enddate)); 
exception 
when others then
result := 0;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCALLCOURSE
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCALLCOURSE" (
    pname varchar2, /*과정명*/
    ppurpose varchar2, /*과정 목표*/
    pcapacity number, /*개설정원*/
    pperiod number,  /*과정기간*/
    result out number
)
is
begin
result :=1;
    insert into tblallCourse (num, name, purpose, capacity, period)
        values (allCourseseq.nextVal, pname, ppurpose, pcapacity, pperiod);
    
exception 
    when others then result :=0;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCATTENDSTATUS
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCATTENDSTATUS" (
     presult out sys_refcursor,
     pyear varchar2,
     pmonth varchar2,
     pday varchar2,
     pteacherNum number
)

is
begin   
open presult for  
    select (select name from tblStudent where num = s.num) as "studentName", count(*) ||'회' as "attendanceCount", h.opencoursenum as "openCourseNum",a.status as attendanceStatus, (select status from tblcoursehistory where studentnum = s.num) as courseStatus from tblattendance a
               inner join tblcourseHistory h
                            on h.num = a.courseHistoryNum 
                                 inner join tblstudent s
                                       on s.num = h.studentNum
                                            inner join tblopenCourse o
                                                on o.num = h.openCourseNum
                                                    where h.status <> '수료' and s.deletestatus <> 1 and h.deletestatus <> 1 and to_char(a.enterTime,'yyyy') = pyear and to_char(a.enterTime,'mm') = pmonth
                                                       and to_char(a.enterTime,'dd') = pday and o.teacherNum = pteacherNum and o.status <> 1
                                                           group by s.num,h.opencoursenum,a.status;
end procattendStatus;

/
--------------------------------------------------------
--  DDL for Procedure PROCATTENDSTATUSCOURSE
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCATTENDSTATUSCOURSE" (
     presult out sys_refcursor,
     pallCourseNum number,
     pstudentNum number,
     pteacherNum number
)

is
begin   
open presult for  
    select (select name from tblStudent where num = s.num) as "studentName", 
    count(decode(a.status,'정상',1)) ||'회' as "normal",
        count(decode(a.status,'지각',1)) ||'회' as "late",count(decode(a.status,'조퇴',1)) ||'회' as "early",count(decode(a.status,'결석',1)) ||'회' as "absent",
        count(decode(a.status,'외출',1)) ||'회' as "outing", count(decode(a.status,'병가',1)) ||'회' as "sickLeave",count(decode(a.status,'기타',1)) ||'회' as "other"
            ,h.opencoursenum as "openCourseNum"
            , (select status from tblcoursehistory where studentnum = s.num and status <> '수료') as courseStatus 
            from tblattendance a
                   inner join tblcourseHistory h
                                on h.num = a.courseHistoryNum 
                                     inner join tblstudent s
                                           on s.num = h.studentNum
                                                inner join tblopenCourse o
                                                    on o.num = h.openCourseNum
                                                        where h.status <> '수료' and s.deletestatus <> 1 and h.deletestatus <> 1 and o.teacherNum = pteacherNum and o.status <> 1 and o.allCourseNum = pallCourseNum and s.num = pstudentNum
                                                               group by s.num,h.opencoursenum;
end procattendStatusCourse;

/
--------------------------------------------------------
--  DDL for Procedure PROCATTENDSTATUSDAY
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCATTENDSTATUSDAY" (
     presult out sys_refcursor,
     pday varchar2,
     pteacherNum number
)

is
begin   
open presult for  
    select 
        (select s.name from tblStudent s inner join tblcoursehistory ch on s.num = ch.studentNum where ch.num = a.coursehistorynum and s.deletestatus <> 1 and ch.deletestatus <> 1 ) as "studentName",
        count(decode(a.status,'정상',1)) ||'회' as "normal",
        count(decode(a.status,'지각',1)) ||'회' as "late",
        count(decode(a.status,'조퇴',1)) ||'회' as "early",
        count(decode(a.status,'결석',1)) ||'회' as "absent",
        count(decode(a.status,'외출',1)) ||'회' as "outing", 
        count(decode(a.status,'병가',1)) ||'회' as "sickLeave",
        count(decode(a.status,'기타',1)) ||'회' as "other",
        (select status from tblcoursehistory where a.coursehistorynum = num and status <> '수료' and deleteStatus <> 1 ) as courseStatus,
        (select oc.num from tblcoursehistory cht inner join tblOpencourse oc on cht.opencoursenum = oc.num where cht.num = a.courseHistoryNum) as opencourseNum
            from tblattendance a
                   inner join tblcourseHistory h
                                on h.num = a.courseHistoryNum 
                                     inner join tblstudent s
                                           on s.num = h.studentNum
                                                inner join tblopenCourse o
                                                    on o.num = h.openCourseNum
                                                        where h.status <> '수료' and s.deletestatus <> 1 and h.deletestatus <> 1 and to_char(a.enterTime,'dd') = pday  and o.teacherNum = pteacherNum and o.status <> 1
                                                               group by a.coursehistorynum;
end procattendStatusDay;

/
--------------------------------------------------------
--  DDL for Procedure PROCATTENDSTATUSMONTH
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCATTENDSTATUSMONTH" (
     presult out sys_refcursor,
     pmonth varchar2,
     pteacherNum number
)

is
begin   
open presult for  
    select (select name from tblStudent where num = s.num) as "studentName",
        count(decode(a.status,'정상',1)) ||'회' as "normal",
        count(decode(a.status,'지각',1)) ||'회' as "late",
        count(decode(a.status,'조퇴',1)) ||'회' as "early",
        count(decode(a.status,'결석',1)) ||'회' as "absent",
        count(decode(a.status,'외출',1)) ||'회' as "outing", 
        count(decode(a.status,'병가',1)) ||'회' as "sickLeave",
        count(decode(a.status,'기타',1)) ||'회' as "other",
        (select courseStatus from tblopencourse oc 
            inner join tblcoursehistory h
                on h.opencourseNum = num where oc.allCourseNum = o.num) as courseStatus
                     from tblattendance a
                       inner join tblcourseHistory h
                                    on h.num = a.courseHistoryNum 
                                         inner join tblstudent s
                                               on s.num = h.studentNum
                                                    inner join tblopenCourse o
                                                        on o.num = h.openCourseNum
                                                            where h.status <> '수료' and s.deletestatus <> 1 and h.deletestatus <> 1 and to_char(a.enterTime,'mm') = pmonth and o.teacherNum = pteacherNum and o.status <> 1
                                                                   group by h.num;
end procattendStatusMonth;

/
--------------------------------------------------------
--  DDL for Procedure PROCATTENDSTATUSYEAR
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCATTENDSTATUSYEAR" (
     presult out sys_refcursor,
     pyear varchar2,
     pteacherNum number
)

is
begin   
open presult for  
    select (select name from tblStudent where num = s.num) as "studentName",count(decode(a.status,'정상',1)) ||'회' as "normal",
        count(decode(a.status,'지각',1)) ||'회' as "late",count(decode(a.status,'조퇴',1)) ||'회' as "early",count(decode(a.status,'결석',1)) ||'회' as "absent",
        count(decode(a.status,'외출',1)) ||'회' as "outing", count(decode(a.status,'병가',1)) ||'회' as "sickLeave",count(decode(a.status,'기타',1)) ||'회' as "other", h.opencoursenum as "openCourseNum", (select status from tblcoursehistory where studentnum = s.num and status <> '수료') as courseStatus from tblattendance a
               inner join tblcourseHistory h
                            on h.num = a.courseHistoryNum 
                                 inner join tblstudent s
                                       on s.num = h.studentNum
                                            inner join tblopenCourse o
                                                on o.num = h.openCourseNum
                                                    where h.status <> '수료' and s.deletestatus <> 1 and h.deletestatus <> 1 and to_char(a.enterTime,'yyyy') = pyear  and o.teacherNum = pteacherNum and o.status <> 1
                                                           group by s.num,h.opencoursenum;
end procattendStatusYear;

/
--------------------------------------------------------
--  DDL for Procedure PROCATTENDSTATUSYEARMONTH
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCATTENDSTATUSYEARMONTH" (
     presult out sys_refcursor,
     pyear varchar2,
     pmonth varchar2,
     pteacherNum number
)

is
begin   
open presult for  
    select (select name from tblStudent where num = s.num) as "studentName", count(decode(a.status,'정상',1)) ||'회' as "normal",
        count(decode(a.status,'지각',1)) ||'회' as "late",count(decode(a.status,'조퇴',1)) ||'회' as "early",count(decode(a.status,'결석',1)) ||'회' as "absent",
        count(decode(a.status,'외출',1)) ||'회' as "outing", count(decode(a.status,'병가',1)) ||'회' as "sickLeave",count(decode(a.status,'기타',1)) ||'회' as "other"
            ,h.opencoursenum as "openCourseNum", (select status from tblcoursehistory where studentnum = s.num and status <> '수료') as courseStatus from tblattendance a
                   inner join tblcourseHistory h
                                on h.num = a.courseHistoryNum 
                                     inner join tblstudent s
                                           on s.num = h.studentNum
                                                inner join tblopenCourse o
                                                    on o.num = h.openCourseNum
                                                        where h.status <> '수료' and s.deletestatus <> 1 and h.deletestatus <> 1 and to_char(a.enterTime,'yyyy') = pyear and to_char(a.enterTime,'mm') = pmonth and o.teacherNum = pteacherNum and o.status <> 1
                                                               group by s.num,h.opencoursenum;
end procattendStatusYearMonth;

/
--------------------------------------------------------
--  DDL for Procedure PROCATTENDTEACHERCOURSE
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCATTENDTEACHERCOURSE" (
     presult out sys_refcursor,
     pallCoursNum number,
     pteacherNum number
)

is
begin   
open presult for  
    select (select name from tblStudent where num = s.num) as "studentName",count(decode(a.status,'정상',1)) ||'회' as "normal",
        count(decode(a.status,'지각',1)) ||'회' as "late",count(decode(a.status,'조퇴',1)) ||'회' as "early",count(decode(a.status,'결석',1)) ||'회' as "absent",
        count(decode(a.status,'외출',1)) ||'회' as "outing", count(decode(a.status,'병가',1)) ||'회' as "sickLeave",count(decode(a.status,'기타',1)) ||'회' as "other", h.opencoursenum as "openCourseNum", (select status from tblcoursehistory where studentnum = s.num) as courseStatus from tblattendance a
               inner join tblcourseHistory h
                            on h.num = a.courseHistoryNum 
                                 inner join tblstudent s
                                       on s.num = h.studentNum
                                            inner join tblopenCourse o
                                                on o.num = h.openCourseNum
                                                    where h.status <> '수료' and s.deletestatus <> 1 and h.deletestatus <> 1 and o.allCourseNum =  pallCoursNum  and o.teacherNum = pteacherNum and o.status <> 1
                                                           group by s.num,h.opencoursenum;
end procattendTeacherCourse;

/
--------------------------------------------------------
--  DDL for Procedure PROCATTENDTEACHERCOURSE1
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCATTENDTEACHERCOURSE1" (
     presult out sys_refcursor,
     pallCoursNum number,
     pteacherNum number
)

is
begin   
open presult for  
    select (select name from tblStudent where num = s.num) as "studentName",count(decode(a.status,'정상',1)) ||'회' as "normal",
        count(decode(a.status,'지각',1)) ||'회' as "late",count(decode(a.status,'조퇴',1)) ||'회' as "early",count(decode(a.status,'결석',1)) ||'회' as "absent",
        count(decode(a.status,'외출',1)) ||'회' as "outing", count(decode(a.status,'병가',1)) ||'회' as "sickLeave",count(decode(a.status,'기타',1)) ||'회' as "other", h.opencoursenum as "openCourseNum", (select status from tblcoursehistory where studentnum = s.num and status <> '수료') as ,(select num from tblallCourse where num = o.allCourseNum) as allCourseNum from tblattendance a
               inner join tblcourseHistory h
                            on h.num = a.courseHistoryNum 
                                 inner join tblstudent s
                                       on s.num = h.studentNum
                                            inner join tblopenCourse o
                                                on o.num = h.openCourseNum
                                                    where h.status <> '수료' and s.deletestatus <> 1 and h.deletestatus <> 1 and o.allCourseNum =  pallCoursNum  and o.teacherNum = pteacherNum and o.status <> 1
                                                           group by s.num,h.opencoursenum;
end procattendTeacherCourse1;

/
--------------------------------------------------------
--  DDL for Procedure PROCAVAILABLEBYTEACHER
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCAVAILABLEBYTEACHER" (
    pnum number,
    presult out SYS_REFCURSOR
)
is 
begin

    open presult for
        select s.name as availableSubject
            from tblTeacher t
                inner join tblavailablesubject a
                    on t.num = a.teachernum
                        inner join tblSubject s
                            on a.subjectNum = s.num
                                where t.num = pnum;

end;

/
--------------------------------------------------------
--  DDL for Procedure PROCAVAILABLECLASSROOM
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCAVAILABLECLASSROOM" (
    pstartDate varchar2,
    presult out SYS_REFCURSOR,
    pcapacity tblclassroom.capacity%type
)
is
begin
    open presult for
        select num as classRoomNum,name as classRoomName,capacity from tblclassroom where num not in (select classRoomNum from tblopenCourse where endDate > to_date(pstartDate,'yyyymmdd') and classroomnum is not null and status = 0) --and status <> 1)
            and deletestatus <> 1 and capacity >= pcapacity order by num;
end procavailableClassRoom;

/
--------------------------------------------------------
--  DDL for Procedure PROCAVAILABLESUBJECT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCAVAILABLESUBJECT" (
pallCourseNum tblopencourse.allcoursenum%type,
presult out sys_refcursor
)
is
begin
open presult for     
    select distinct s.num,s.name as subjectName from tblallCourse a 
        inner join tblSubjectByCourse sb
            on sb.allcoursenum = a.num
                 inner join tblSubject s
                    on s.num = sb.subjectnum where a.deletestatus <> 1 and s.deletestatus <> 1 and a.num = pallCourseNum
                            order by s.num,s.name;
end procavailableSubject;

/
--------------------------------------------------------
--  DDL for Procedure PROCAVAILABLETEACHER
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCAVAILABLETEACHER" (
    pstartDate varchar2,
    presult out sys_refcursor,
    pteacherNum tblteacher.num%type
)
is
begin
 open presult for
        select DISTINCT t.num as teacherNum,t.name as "teacherName" from tblteacher t
                    inner join tblavailableSubject a 
                        on a.teacherNum = t.num
                            where t.num not in (select teacherNum from tblopenCourse where endDate > to_date(pstartDate,'yyyymmdd')
                                 and teacherNum is not null and status = 0) and t.deletestatus <> 1
                                           and t.num = pteacherNum
                                                    order by t.num;
end procavailableTeacher;

/
--------------------------------------------------------
--  DDL for Procedure PROCCHECKENROLLSCORE
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCCHECKENROLLSCORE" (
    pPeriodbysubjectsnum number,
    pOpencoursenum number,
    cntNum out number
)
is
begin
    select count(*) into cntNum
        from tblscorebysubject 
        where examnum = (select num 
                        from tblexam 
                        where opencoursenum = pOpencoursenum and subjectbycoursenum = (select subjectbycoursenum 
                                                                          from tblperiodbysubject 
                                                                          where num = pPeriodbysubjectsnum));
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCCHECKQUESTION
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCCHECKQUESTION" (
    pSubjectnum number,
    pCnt out number
)
is
begin

    select count(*) into pCnt
    from tblquestionbyexam qbe inner join tblexam e 
            on qbe.examnum = e.num inner join tblsubjectbycourse sbc 
                on e.subjectbycoursenum = sbc.num inner join tblsubject s 
                    on sbc.subjectnum = s.num
    where s.num = pSubjectnum;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCCHECKSTDINFO
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCCHECKSTDINFO" (
    pNum number, -- 과정 번호
    pResult out SYS_REFCURSOR
)
is
begin
    open presult for 
        select s.name as stdName, s.tel as stdtel, s.registerdate as stdRegitdate, status 
        from tblcourseHistory ch inner join tblstudent s on ch.studentnum = s.num
        where opencoursenum in (select num from tblopencourse where allcoursenum in (select allcoursenum from tblSubjectByCourse where ch.opencoursenum = pNum) and sysdate > startdate and sysdate < enddate)
        and s.deletestatus = 0;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCCHECKTEACHING
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCCHECKTEACHING" (
    pteachernum number,
    pResult out SYS_REFCURSOR
)
is
begin
    open pResult for
    select 
    a.num as coursenum, ac.name as coursename,a.classroomnum as classroomnum, (Select count(*) from tblcoursehistory where opencoursenum = a.num) as stdNum,a.startdate as startdate, a.enddate as enddate,
    case
        when startdate < sysdate and sysdate < enddate then '강의중'
        when startdate > sysdate then '강의예정'
        when enddate < sysdate then '강의종료'
    end as status
from(select * from tblopencourse where teachernum = pteachernum) a inner join tblallcourse ac on ac.num = a.allcoursenum
where sysdate > startdate and sysdate < enddate ;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCCHECKTEACHING2
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCCHECKTEACHING2" (
    pcoursenum number,
    pResult out SYS_REFCURSOR
)
is
begin
    open presult for
    select sbc.subjectnum as subjectnum, sj.name as subjectname, a.startdate as sjStardDate, a.enddate as sjenddate, b.name as bookname
    from(select * from tblperiodbysubject where opencoursenum in (select num from tblopencourse where tblopencourse.num = pcoursenum and sysdate > startdate and sysdate < enddate)) a
        inner join tblsubjectbycourse sbc on sbc.num = a.subjectbycoursenum 
            inner join tblsubject sj on sj.num = sbc.subjectnum
                inner join tblbook b on b.subjectnum = sj.num;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCCOUNT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCCOUNT" (
    pallCourseNum tblallCourse.num%type,
    pteacherNum tblteacher.num%type,
    pstartDate varchar2,
    pcnt out number
)
is
begin
   select count(*) into pcnt from
            (select distinct s.name as subjectName from tblallCourse a 
                    inner join tblSubjectByCourse sb
                        on sb.allcoursenum = pallCourseNum
                             inner join tblSubject s
                                on s.num = sb.subjectnum where a.deletestatus <> 1 and s.deletestatus <> 1

minus 
select s.name as "subjectName" from tblteacher t
            inner join tblavailableSubject a 
                on a.teacherNum = t.num
                    inner join tblSubject s
                        on s.num = a.subjectnum
                             where t.num not in (select teacherNum from tblopenCourse where endDate > to_date(pstartDate,'yyyymmdd')
                                        and teacherNum is not null and status = 0) and t.deletestatus <> 1 and s.deletestatus <> 1 
                                            and t.num = pteacherNum);
end procCount;

/
--------------------------------------------------------
--  DDL for Procedure PROCCOURSEHISTORY
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCCOURSEHISTORY" (
    pstatus varchar2,
    pendDate date
)
is
begin
    insert into tblCourseHistory (num, studentNum, openCourseNum, status, endDate)
        values (courseHistoryseq.nextVal
        , (select c.num from tblCourseHistory c where c.num = (select s.num from tblStudent s))
        , (select c.num from tblCourseHistory c where c.num = (select o.num from tblOpenCourse o))
        , pstatus, pendDate);
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCCOURSEJOBACTIVITY
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCCOURSEJOBACTIVITY" 
(
    presult out sys_refcursor,
    pcoursenum number
   
)
is
begin
    open presult for
        select o.num as coursenum, ja.content as activity, s.name studentname
        from tbljobActivity ja join tblcoursehistory h
        on ja.coursehistorynum = h.num
            join tblstudent s
                on h.studentnum = s.num
                  join tblopencourse o
                    on h.opencoursenum = o.num
        where o.num in (select o.num
                            from tblcoursehistory h join tblstudent s
                                    on h.studentnum = s.num
                                        join tblopencourse o
                                            on h.opencoursenum = o.num
                             where o.num = pcoursenum);
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCCOURSESUBJECTSBYTEACHER
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCCOURSESUBJECTSBYTEACHER" (
    pnum number,    --교사번호
    presult out SYS_REFCURSOR   --결과값 반환 커서
)
is
begin

    open presult for
        select 
            s.name as SubjectName,
            to_char(p.startDate, 'yyyy-mm-dd')|| ' ~ ' ||  to_char(p.endDate, 'yyyy-mm-dd') as SubjectPeriod,
            b.name as book
        from tblTeacher t
            inner join tblopencourse oc
                on t.num = oc.teachernum
                    inner join tblPeriodBySubject p
                        on oc.num = p.opencoursenum
                            inner join tblSubjectByCourse sc
                                on p.subjectByCourseNum = sc.num
                                    inner join tblSubject s
                                        on sc.subjectNum = s.num
                                            inner join tblBook b
                                                on s.num = b.subjectNum
                                                    where t.num = pnum
                                                        order by SubjectPeriod;

end procCourseSubjectsByTeacher;

/
--------------------------------------------------------
--  DDL for Procedure PROCDATEATTEMDANCEINQUIRY
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCDATEATTEMDANCEINQUIRY" 
(
    pyear  varchar2,
    pmonth varchar2,
    pdate  varchar2
)
IS
BEGIN
select enterTime
from tblAttendance
where enterTime =
    to_date(pyear||pmonth||pdate,'yyyy/mm/dd');
END;

CREATE or replace procedure procDateAttemdanceInquiry(
    pdate date,
    presult out sys_refcursor 
)
IS
BEGIN
    open presult
        for select * from tblAttendance where enterTime = pdate;
END;

set serveroutput on;
--커서를 반환한 프로시저 호출
declare 
    vresult sys_refcursor; --커서
    vrow tblAttendance%rowtype;
begin 
    procDateAttemdanceInquiry(19/07/22,vresult);
    loop
        fetch vresult into vrow;
        exit when vresult%notfound;

        dbms_output.put_line(vrow.entertime);

    end loop;
end;






DECLARE 
    vyear varchar2(15);
    vmonth varchar2(15);
    vdate varchar2(15);
BEGIN

END;

SELECT *
FROM tblAttendance
WHERE to_char(entertime,'yy/mm/dd') = '19'||'/'||'05'||'/'||'01';

select '19'||'/'||'05'||'/'||'01'
from dual;



--특정 과정과 특정인원 검색

select name
FROM tblAttendance a join tblCourseHistory h
    on a.coursehistorynum = h.num
        join tblStudent s
            on h.studentnum = s.num
GROUP BY name;

/
--------------------------------------------------------
--  DDL for Procedure PROCDELETEALLCOURSE
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCDELETEALLCOURSE" 
(
    result out number,
    pnum number
)
is
begin
    result :=1;
    update tblallcourse set deletestatus = 1 where num =pnum;
exception 
  when others then  result :=0;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCDELETEBOOK
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCDELETEBOOK" 
(
    pnum number,
    result out number
)
is
begin
    result :=0;
    update tblbook set deletestatus = 1 where num =pnum;
exception
    when others then result :=1;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCDELETECLASSROOM
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCDELETECLASSROOM" 
(
    result out number,
    pnum number
)
is
begin
    result :=1;
    update tblclassroom set deletestatus = 1 where num =pnum;
exception 
  when others then  result :=0;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCDELETECOURSEHISTORY
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCDELETECOURSEHISTORY" (
    pnum number      -- 수강 내역 번호
    
)
is
begin
    update tblcoursehistory -- 수강내역 테이블
        set deletestatus = 1 -- 상태(수료여부) 입력, 날짜 입력, 삭제여부
            where num = pnum; -- 삭제할 수강내역 번호 입력
end procDeleteCoursehistory;

/
--------------------------------------------------------
--  DDL for Procedure PROCDELETESJTINFO
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCDELETESJTINFO" (
    pSubjectNum number
)
is
begin
    update tblsubject set deletestatus = 1 where num = pSubjectNum;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCDELETESTUDENT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCDELETESTUDENT" (
    pnum number 
)
IS
BEGIN
    update tblStudent 
        set deleteStatus = 1
            where num = pnum; -- 수정할 학생 번호
END procDeleteStudent;

/
--------------------------------------------------------
--  DDL for Procedure PROCDELETESUBJECT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCDELETESUBJECT" 
(
    result out number,
    pnum number
)
is
begin
    result := 1;
    update tblsubject set deletestatus = 1 where num =pnum;
exception
    when others then 
    result := 0;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCDELETETEACHER
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCDELETETEACHER" (
    pnum number,    --교사번호
    presult out number  --완료여부 반환값 
)
is 
    vcnt number;    --해당 교사번호가 교사테이블에 존재하는지 확인해줄 변수
begin 

     presult := 0;
      select count(*) into vcnt from tblTeacher where num = pnum;

       --입력받은 교사번호가 교사 테이블에 있는 경우
    if vcnt > 0  then
        --교사별 강의가능목록에서 해당 교사의 내역을 지움
        delete from tblAvailableSubject where teachernum = pnum;
        --교사 테이블에서 해당 교사의 사용 상태를 1로 변경
        update tblTeacher set deleteStatus = 1  where num = pnum;
        presult := 1;
    end if;

end;

/
--------------------------------------------------------
--  DDL for Procedure PROCDISTRIBUTIONBYCOURSE
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCDISTRIBUTIONBYCOURSE" (
    pnum number,    --시행과정번호
    presult out SYS_REFCURSOR       --반환값
)
is
begin
    open presult for
        select 
            --시행번호가 같으면 과정명과 학생이름을 가져옴.
            (select name from tblallcourse ac where ac.num = oc.allCourseNum) as courseName,
            (select name from tblStudent where num = ch.num) as StudentName,
            b.name as BookName,
            --0이면 배부되지 않음, 1이면 배부 완료된 상태
            case
                when d.status = 0 then '배부 X'
                when d.status = 1 then '배부 O'
            end as status
        from tblopencourse oc
            inner join tblcoursehistory ch
                on oc.num = ch.opencoursenum
                     inner join tblDistribution d
                            on ch.num = d.courseHistoryNum
                                inner join tblBook b
                                    on d.bookNum = b.num
                                        --입력받은 과정번호의 내역만 출력
                                        where  pnum = oc.num
                                             order by studentName;
end procDistributionByCourse;

/
--------------------------------------------------------
--  DDL for Procedure PROCENDEXAMRATIO
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCENDEXAMRATIO" 
(
    pwratio number,
    ppratio number,
    paratio number
)
is
begin
insert into (select e.writtentestratio as writtentetratio --강의가 끝난 
   ,e.performancetestratio as performancetetratio
   ,e.attendanceratio as attendanceratio
    from tblteacher t join tblopencourse o
         on t.num = o.teachernum
                 join tblexam e
                     on o.num = e.opencoursenum
                        join tblscoreBysubject b
                            on  e.num = b.examnum) values (0.1,0.1,0.1);
end;

create or replace procedure procdeleteclassroom
(
    pnum number
)
is
begin
    update tblclassroom set deletestatus = 1 where num =pnum;
end;

select *
from tblclassroom;

select *
from tbladmin;


create or replace procedure procPrintRatio
(
    presult out sys_refcursor,
    ptnum number,
    psnum number
)
is
begin
    open presult for
select e.writtentestratio as writtentestratio,
            e.performancetestratio as performancetestratio,
                e.attendanceratio as attendanceratio,
                    t.name as teachername
from tblteacher t join tblopencourse o
        on t.num = o.teachernum
            join tblexam e
                on o.num = e.opencoursenum
                    join tblsubjectbycourse c
                        on e.subjectbycoursenum= c.num
where testdate is not null and t.num = ptnum and s.num = psnum;
end;


select e.writtentestratio as writtentestratio,
            e.performancetestratio as performancetestratio,
                e.attendanceratio as attendanceratio,
                    t.name as teachername,t.num as teachernum,c.subjectnum as subjectnum, s.name as subjectname, testdate, enddate
from tblteacher t join tblopencourse o
        on t.num = o.teachernum
            join tblexam e
                on o.num = e.opencoursenum
                    join tblsubjectbycourse c
                        on e.subjectbycoursenum= c.num
                            join tblsubject s
                             on c.subjectnum = s.num
where testdate  is not null and t.num =5 and o.enddate >testdate;

select *
from tblteacher t join tblopencourse o
on t.num = o.teachernum;
select *
from tblperiodbysubject;
declare 
     presult sys_refcursor;
     vwrittentestratio tblexam.writtentestratio%TYPE;
     vperformancetestratio tblexam.performancetestratio%TYPE;
     vattendanceratio tblexam.attendanceratio%TYPE;
     vname tblteacher.name%type;
begin 
    procprintratio(presult);
     loop
     fetch presult into vwrittentestratio,vperformancetestratio,vattendanceratio,vname;
     exit when presult%notfound;
     dbms_output.put_line('result:');
     dbms_output.put_line(vwrittentestratio);
     dbms_output.put_line(vperformancetestratio);
     dbms_output.put_line(vattendanceratio);
     dbms_output.put_line(vname);
     end loop;
end;

-- 강의를 마친 시험의 배점 입력

create or replace procedure procWriterRatio
(
    presult out sys_refcursor,
    pteachernum number,
    psubjectnum number,
    pwrittenratio number,
    pperforratio number,
    pattendratio number
)
is
begin
    update tblexam set writtentestRaito = pwrittenratio 
                        where testdate is not null and 
                                                    (select * from tblteacher t join tblopencourse o
                                                                    on t.num = o.teachernum
                                                                    where testdate = )

end;
select * from tblexam;

select *
from tblopencourse;
--자신이 강의를 마친 과목 목록중 특정과목 선택, 해당과목의 배점정보를 필,실,출 구분등록
update set 

create or replace procedure procEndSubject
(
    pwritten number,
    pperform number,
    pattend number
)
IS
BEGIN
--esubject := (select distinct sb.num
--        from tblteacher t join tblopencourse o
--                    on t.num = o.teachernum
--                        join tblexam e
--                        on o.num = e.opencoursenum
--                            join tblsubjectbycourse sb
--                                on e.subjectBycoursenum = sb.num
--                    where t.num = pteacher and sb.num = psubject); -- 끝난 과목 중 과목 선택

update tblexam set writtentestratio = pwritten where testdate = not null
                                                    and subjectnum = psubject;
update tblexam set performancetestratio = pperform where testdate = not null
                                                    and subjectnum = psubject;
update tblexam set attendanceratio= pattend where testdate = not null
                                                    and subjectnum = psubject;        


end;

declare
    vsubject tblsubject.num%type;
    vteacher tblteacher.num%type;
    vwritten tblexam.writtentestratio.num%type,
    pperform tblexam.performancetestratio.num%type,
    pattend  tblexam.attendanceratio.num%type
begin

end;

select *
from tblexam;
desc tblexam;

--과목 목록 출력 시 과목번호, 과정명, 과정기간(시작 년월일, 끝 년월일), 강의실, 과목명, 과목기간(시작 년월일, 끝 년월일), 교재명, 출결, 필기, 실기 배점 등이 출력되고, 

create or replace procedure procSubjectlist
(
    presult out sys_refcursor
)
IS
BEGIN
 open presult for
select distinct  s.num as subjectnum,
        oc.num as coursenum,
        oc.startdate as startDate,
        oc.enddate as endDate,
        r.name as classroomName,
        s.name as subjectName,
        pc.startdate as startDate,
        pc.enddate as endDate,
        b.name as bookName,
        e.writtenTestRatio as writtenTestRatio, 
        e.performanceTestratio as performanceTestRatio, 
        e.attendanceRatio as attenDanceRatio
from tblsubject s join tblbook b
            on s.num = b.subjectnum
                join tblsubjectbycourse sc
                    on s.num = sc.subjectnum
                        join tblperiodbysubject pc
                            on sc.num = pc.subjectbycoursenum
                                join tblopencourse oc
                                    on oc.num = pc.opencoursenum
                                        join tblexam e
                                            on e.opencoursenum = oc.num
                                                join tblclassroom r
                                                    on r.num = oc.classroomnum
order by coursenum;
END;


declare
    presult sys_refcursor;
    vsub tblsubject.num%type;
    vcourse tblopencourse.num%type;
    vstart tblopencourse.startdate%type;
    vend tblopencourse.enddate%type;
    vroom tblclassroom.name%type;
    vsubN tblsubject.name%type;
    vpstart tblperiodbysubject.startdate%type;
    vpend tblperiodbysubject.enddate%type;
    vbook tblbook.name%type;
    vwritten tblexam.writtentestratio%type;
    vperform tblexam.performancetestratio%type;
    vattend tblexam.attendanceratio%type;
begin
    procSubjectlist(presult);
    loop
    fetch presult into vsub, vcourse, vstart, vend, vroom,vsubN,vpstart,vpend,vbook,vwritten,vperform,vattend;
    exit when presult%notfound;

    dbms_output.put_line(vsub);
    dbms_output.put_line(vcourse);
    dbms_output.put_line(vstart);
    dbms_output.put_line(vend);
    dbms_output.put_line(vroom);
    dbms_output.put_line(vsubN);
    dbms_output.put_line(vpstart);
    dbms_output.put_line(vpend);
    dbms_output.put_line(vbook);
    dbms_output.put_line(vwritten);
    dbms_output.put_line(vperform);
    dbms_output.put_line(vattend);
    end loop;
end;


select *
from tblcoursehistory;
--시험날짜 추가

select *
from tblexam;

create or replace procedure procAddTestdate
(
    pnum number,
    ptestdate date 
)
is
begin
    update tblexam set testdate = ptestdate where num = pnum; 
end;

select *
from tblallcourse;




select 

create or replace procedure procAddQuestion
(
    pexamnum number,
    ptype varchar2,
    pquestion varchar2,
    panswer varchar2
)
is
begin
    insert into tblquestionbyexam(num,examnum,type,question,answer) 
        values (EXAMSEQ.nextval,pexamnum,ptype,pquestion,panswer);
end;

desc tblquestionbyexam;

select *
from tblbook;

select distinct e.writtentestratio as writtentestratio,
            e.performancetestratio as performancetestratio,
                e.attendanceratio as attendanceratio,
                    t.name as teachername,a.num as allcoursenum,
                    (select name from tblsubject where num = c.subjectnum) as subject
from tblteacher t join tblopencourse o
        on t.num = o.teachernum
            join tblexam e
                on o.num = e.opencoursenum
                    join tblsubjectbycourse c
                        on e.subjectbycoursenum= c.num
                            join tblallcourse a
                             on o.allcoursenum = a.num
                                join tblperiodbysubject p
                                    on c.num = p.subjectbycoursenum
where testdate is not null  and t.num = 1 and c.subjectnum = 3;

select *
from tblstudent
order by num;

/
--------------------------------------------------------
--  DDL for Procedure PROCENDSUBJECT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCENDSUBJECT" 
(
    
    presult out sys_refcursor,
    psubject number,
    pteacher number,
    pwritten number,
    pperform number,
    pattend number
)
declare
    esubject number;
    ecourse number;
IS
BEGIN


esubject := (select distinct sb.num
        from tblteacher t join tblopencourse o
                    on t.num = o.teachernum
                        join tblexam e
                        on o.num = e.opencoursenum
                            join tblsubjectbycourse sb
                                on e.subjectBycoursenum = sb.num
                    where t.num = pteacher and sb.num = psubject); -- 끝난 과목 중 과목 선택


update tblexam set writtentestratio = pwritten where testdate = not null
                                                    and subjectnum = esubject;
update tblexam set performancetestratio = pperform where testdate = not null
                                                    and subjectnum = esubject;
update tblexam set attendanceratio= pattend where testdate = not null
                                                    and subjectnum = esubject;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCENROLLPERIODBYSUBJECT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCENROLLPERIODBYSUBJECT" (
    pCourseNum number,          -- 입력받은 과정번호
    pSubjectNum number,         -- 입력받은 과목번호
    pStartdate Date,            -- 입력받은 시작년월일
    pEnddate Date               -- 입력받은 종료년월일
)
is
begin
    -- 입력받은 과정에 과목 추가
    insert into TBLPERIODBYSUBJECT (num, opencoursenum, SUBJECTBYCOURSENUM, STARTDATE, ENDDATE) values (PERIODBYSUBJECTSEQ.nextval,pCourseNum,pSubjectNum, pStartdate,pEnddate);
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCENROLLSUBJECTLISTTOCOURSE
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCENROLLSUBJECTLISTTOCOURSE" (
    pCourseNum number,          -- 입력받은 과정번호
    pSubjectNum number,         -- 입력받은 과목번호
    pSeq number,                -- 입력받은 순서
    pPeriod number             -- 입력받은 기간
)
is
begin
    -- 입력받은 과정에 과목 추가
    insert into tblsubjectbycourse values (SUBJECTBYCOURSESEQ.nextval,pCourseNum,pSubjectNum, pSeq, pPeriod);
end procEnrollSubjectListToCourse;

/
--------------------------------------------------------
--  DDL for Procedure PROCEQUIPMENTBYCLASSROOM
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCEQUIPMENTBYCLASSROOM" (
    pnum number,
    presult out SYS_REFCURSOR
)
is
begin

    open presult for
    select 
        c.name as classroomName,
        e.item as item,
        ec.qty as qty,
        to_char(ec.buyingdate, 'yyyy-mm-dd') as purchaseDate
    from tblClassroom c
        inner join tblequipmentbyclassroom ec
            on c.num = ec.classroomNum
                inner join tblEquipment e
                    on ec.equipmentnum = e.num
                        --강의실 번호와 입력받은 번호가 같은 내역만 조회
                        where c.num = pnum;

end;

/
--------------------------------------------------------
--  DDL for Procedure PROCEQUIPMENTBYDATE
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCEQUIPMENTBYDATE" (
    pstartDate varchar2,
    pendDate varchar2,
    presult out SYS_REFCURSOR
)
is
begin

    open presult for
    select 
        c.name as classroomName,
        e.item as item,
        ec.qty as qty,
        to_char(ec.buyingdate, 'yyyy-mm-dd') as purchaseDate
    from tblClassroom c
        inner join tblequipmentbyclassroom ec
            on c.num = ec.classroomNum
                inner join tblEquipment e
                    on ec.equipmentnum = e.num
                        --날짜를 varchar로 받아 date형식으로 변환 후 기간 내 내역만 조회
                        where ec.buyingdate between to_date(pstartDate, 'yyyy-mm-dd') and to_date(pendDate, 'yyyy-mm-dd')
                            order by classroomname, purchaseDate;

end;

/
--------------------------------------------------------
--  DDL for Procedure PROCEXAM
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCEXAM" (
pnum number,
popencoursenum number,
psubjectnum number,
psubjectnum number,
ptestdate date,
pwrittentestratio number,
pperformancetestratio number,
pattendanceratio number,
presult out number
)
is 
begin
    insert into tblexam(num,opencoursenum,subjectnum,testdate,writtentestratio,performanceTestRatio,attendanceRatio) 
        values(pnum,ppopencoursenum,psubjectnum,ptestdate,pwrittentestratio,pperformanceTestRatio,pattendanceRatio);
          presult := 1;
exception 
    when others then 
        presult := 0;

end procexam;

select 
from(
select num
FROM tblOPenCourse);

/
--------------------------------------------------------
--  DDL for Procedure PROCHIREDGRADESMANDELETE
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCHIREDGRADESMANDELETE" (
    pnum tblhiredGraduates.num%type
) 
is
begin
    delete tblhiredGraduates where num = pnum;
end prochiredGradesManDelete;

/
--------------------------------------------------------
--  DDL for Procedure PROCHIREDGRADESMANINSERT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCHIREDGRADESMANINSERT" (
    pcourseHistoryNum tblhiredGraduates.courseHistorynum%type,
    pcompany tblhiredGraduates.company%type,
    psalary tblhiredGraduates.salary%type,
    pstatus tblhiredGraduates.status%type
)
is
begin
    insert into tblhiredGraduates values(hiredGraduatesseq.nextVal,pcourseHistoryNum,pcompany,psalary,pstatus);
end prochiredGradesManInsert;

/
--------------------------------------------------------
--  DDL for Procedure PROCHIREDGRADESSALARYSELECT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCHIREDGRADESSALARYSELECT" (
    pstart number,
    pend number,
    presult out sys_refcursor
)
is
begin
     open presult for 
            select coursehistorynum,company,salary,status 
                            from tblhiredGraduates where salary between pstart and pend order by salary,coursehistorynum;
end prochiredgradesSalarySelect;

/
--------------------------------------------------------
--  DDL for Procedure PROCHIREDGRADESSELECTNAME
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCHIREDGRADESSELECTNAME" (
    pcompany tblhiredGraduates.company%type,
    presult out sys_refcursor
)
is
begin
    open presult for
        select * from tblhiredGraduates where replace(upper(company),' ','') like replace('%' || upper(pcompany) || '%',' ','');
end prochiredGradesSelectName;

/
--------------------------------------------------------
--  DDL for Procedure PROCHIREDGRADUATESSELECT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCHIREDGRADUATESSELECT" (
    pnum tblopenCourse.num%type,
    presult out sys_refcursor
)
is
begin
    open presult for 
        select * from tblhiredGraduates where num = pnum;
end prochiredGraduatesSelect;

/
--------------------------------------------------------
--  DDL for Procedure PROCHIREDGRADUATESUPDATE
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCHIREDGRADUATESUPDATE" (
    pnum tblhiredGraduates.num%type,
    pstatus tblhiredGraduates.status%type
)
is
begin
    update tblhiredgraduates set status = pstatus where num = pnum;
end prochiredGraduatesUpdate;

/
--------------------------------------------------------
--  DDL for Procedure PROCHIREDGRADUATESUPDATE1
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCHIREDGRADUATESUPDATE1" (
    pnum tblhiredGraduates.num%type,
    pcourseHistoryNum tblhiredGraduates.courseHistoryNum%type,
    pcompany tblhiredGraduates.company%type,
    psalary tblhiredGraduates.salary%type,
    pstatus tblhiredGraduates.status%type
)
is
begin
   update tblhiredgraduates set courseHistoryNum = pcourseHistoryNum,company = pcompany, salary = psalary,status = pstatus where num = pnum;
end prochiredGraduatesUpdate1;

/
--------------------------------------------------------
--  DDL for Procedure PROCHIREDGRADUATESUPDATESELECT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCHIREDGRADUATESUPDATESELECT" (
    pnum tblhiredGraduates.num%type,
    presult out sys_refcursor
)
is
begin
    open presult for
        select * from tblhiredGraduates where num = pnum;
end prochiredGraduatesUpdateSelect;

/
--------------------------------------------------------
--  DDL for Procedure PROCINPUTWRITTENTESTRATIO
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCINPUTWRITTENTESTRATIO" 
(
    pteachernum number,
    pcoursenum number,
    psubjectnum number,
    pwrittenTestRatio number,
    pperformanceRatio number,
    pattendanceRatio number,
    result out number
)
is
BEGIN
result := 1;
update 
tblexam
set
writtenTestRatio = pwrittenTestRatio/100,
performanceTestRatio = pperformanceRatio/100,
attendanceRatio = pattendanceRatio/100
where
subjectbycoursenum in
(select  c.num
from tblteacher t join tblopencourse o
        on t.num = o.teachernum
            join tblexam e
                on o.num = e.opencoursenum
                    join tblsubjectbycourse c
                        on e.subjectbycoursenum= c.num
                            join tblallcourse a
                             on o.allcoursenum = a.num
                                join tblperiodbysubject p
                                    on c.num = p.subjectbycoursenum
where testdate is not null and o.enddate > sysdate and t.num = pteachernum and c.subjectnum = psubjectnum and o.num =pcoursenum);
exception
when others then
result := 0;
END; --procInputWrittenTestRatio

/
--------------------------------------------------------
--  DDL for Procedure PROCINSERTEQUIPMENTBYCLASSROOM
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCINSERTEQUIPMENTBYCLASSROOM" (
    pRoomnum number,
    pEquipnum number,
    pQty number,
    pPurchaseDate varchar2,
    presult out number
)
is
    vcnt number;

begin

    presult := 0;
    --1.입력받은 값과 동일한 내역이 있는지 확인
    select count(*) into vcnt from tblequipmentbyclassroom 
        where classroomnum = pRoomnum and equipmentnum = pEquipnum;

    --2. 동일한 내역이 없으면 추가 
    if vcnt = 0 then
        insert into tblequipmentbyclassroom values (EQUIPMENTBYCLASSROOMSEQ.nextval, pRoomnum, pEquipnum, pQty, to_date(pPurchaseDate, 'yyyymmdd'));
        presult := 1;
    end if;

end;

/
--------------------------------------------------------
--  DDL for Procedure PROCINSERTSCORETOSCOREBYEXAM
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCINSERTSCORETOSCOREBYEXAM" (
    pcoursehistorynum number,
    pExamnum number,
    pWscore number,
    pPscore number,
    pAscore number
)
is
begin
    insert into tblscorebysubject values (SCOREBYSUBJECTSEQ.nextval, pcoursehistorynum, pExamnum, pWscore, pPscore, pAscore);
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCINSERTSUPPORT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCINSERTSUPPORT" 
(
    pstudent number,
    psupport number,
    result out number
)
is
begin
result := 1;
    insert into tblsupport values (SUPPORTSEQ.nextval,
                    (select distinct h.studentnum from tblsupport s join tblcoursehistory h
                                                on s.coursehistorynum = h.num
                                                where h.studentnum = pstudent)
                    ,(select distinct num from tblsupportlist where num = psupport));
exception
when others then
result := 0;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCINTEGRATEDSCOREBYSTUDENT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCINTEGRATEDSCOREBYSTUDENT" (
    pnum number,
    presult out sys_refcursor
) 
is 
begin
    open presult for
        select 
            sb.name as subjectName,
            round((ss.writtenTestScore * e.writtenTestRatio) +  (ss.performanceTestScore * e.performanceTestRatio) +  (ss.attendanceScore * e.attendanceRatio)) as score
        from tblStudent s
            inner join tblCourseHistory ch
                on s.num = ch.studentnum
                    inner join tblScoreBySubject ss
                         on ch.num = ss.courseHistoryNum
                            inner join tblexam e
                                on ss.examnum = e.num
                                    inner join tblSubjectByCourse sc
                                        on e.subjectByCourseNum = sc.num  
                                            inner join tblsubject sb
                                                on sc.subjectnum = sb.num
                                                    where s.num = pnum;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCISERTSNPRINTCOURSEHISTORY
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCISERTSNPRINTCOURSEHISTORY" (
   pname varchar2,
   presult out sys_refcursor
)
is
begin
    open presult for
    select t.num studentNum, t.name studentName, ch.num courseHistoryNum, ac.name openCourseName, ch.status, ch.enddate, ch.deletestatus from tblcoursehistory ch
        inner join tblstudent t
            on t.num = ch.studentnum
                inner join tblopencourse oc
                    on oc.num = ch.opencoursenum
                        inner join tblallcourse ac
                            on ac.num = oc.allcoursenum
                                where t.name = pname
                                    order by ch.num;

end procIsertSNPrintCoursehistory;

/
--------------------------------------------------------
--  DDL for Procedure PROCOPENCOURSE
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCOPENCOURSE" (

    pstartDate varchar2,
    pendDate varchar2,
    pclassRoomNum tblopenCourse.classRoomNum%type,
    pallCourseNum tblopenCourse.allCourseNum%type
  
)
is
begin
    insert into tblopencourse(num,startDate,endDate,classRoomNum,allCourseNum,status) values(OPENCOURSESEQ.nextval,to_date(pstartDate,'yyyymmdd'),to_date(pendDate,'yyyymmdd'),pclassRoomNum,pallCourseNum,default);

end procopenCourse;

/
--------------------------------------------------------
--  DDL for Procedure PROCOPENCOURSEDELETE
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCOPENCOURSEDELETE" (
    pnum tblopenCourse.num%type
)
is 
begin

   -- 상태값을 줘서 삭제된 얘인거를 표시를 해
   update tblopenCourse set status = 1 where num = pnum; 

end procopenCourseDelete;

/
--------------------------------------------------------
--  DDL for Procedure PROCOPENCOURSEINSERT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCOPENCOURSEINSERT" (

    pstartDate varchar2,
    pendDate varchar2,
    pclassRoomNum tblopenCourse.classRoomNum%type,
    pteacherNum tblopenCourse.teacherNum%type,
    pallCourseNum tblopenCourse.allCourseNum%type


)
is 
begin
    insert into tblopenCourse values(openCourseseq.nextVal,to_date(pstartDate,'yyyymmdd'),to_date(pendDate,'yyyymmdd'),pclassRoomNum,pteacherNum,pallCourseNum,default);

end procopenCourseInsert;

/
--------------------------------------------------------
--  DDL for Procedure PROCOPENCOURSESELECT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCOPENCOURSESELECT" (
    pnum tblopenCourse.num%type,
    presult out sys_refcursor
)
is
begin
    open presult for 
        select * from tblopenCourse where num = pnum and status <> 1;
end procopenCourseSelect;

/
--------------------------------------------------------
--  DDL for Procedure PROCOPENCOURSEUPDATE
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCOPENCOURSEUPDATE" (
    pnum tblopenCourse.num%type,
    pstartDate varchar2,
    pendDate varchar2,
    pclassRoomNum tblopenCourse.classRoomNum%type,
    pteacherNum tblopenCourse.teacherNum%type,
    pallCourseNum tblopenCourse.allCourseNum%type

)
is 
begin 
    update tblopenCourse set startDate = to_date(pstartDate,'yyyymmdd'), endDate = to_date(pendDate,'yyyymmdd'), classRoomNum = pclassRoomNum, teacherNum = pteacherNum, allCourseNum = pallCourseNum, status = default where num = pnum;

end procopenCourseUpdate;

/
--------------------------------------------------------
--  DDL for Procedure PROCOURESSTATUSFINISH
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCOURESSTATUSFINISH" (
    pnum number, -- 수강내역 번호
    presult out sys_refcursor
)
is
begin
    open presult for
    -- 수강내역 넣으면 수료상태 결과 출력
    select ch.status from tblstudent s  inner join tblcoursehistory ch on s.num = ch.studentnum where ch.studentnum = pnum;    
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCOUTSTANDINGREWARD
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCOUTSTANDINGREWARD" (
    pnum number, -- 과목번호
    presult out sys_refcursor
)
is
begin
    open presult for
    select st.name as name, substr(st.ssn, 8, 7) as ssn, s.name as subjectname, sbs.writtenTestScore as writtenTestScore, sbs.performanceTestScore as performanceTestScore, sbs.attendancescore as attendanceScore  from tblStudent st
        inner join tblcourseHistory ch
            on st.num = ch.studentnum
                inner join tblscoreBySubject sbs
                    on sbs.courseHistoryNum = ch.num
                        inner join tblexam ex
                            on ex.num =  sbs.examNum
                                inner join tblSubjectByCourse sbc
                                    on sbc.num = ex.subjectByCourseNum
                                        inner join tblSubject s
                                            on s.num = sbc.subjectNum
                                                where (writtenTestScore * ex.writtenTestRatio + performancetestscore * ex.performanceTestRatio + attendancescore * ex.attendanceratio ) >=90
                                                    and s.num = pnum;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCPRINETBASICSTUDENT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCPRINETBASICSTUDENT" (
   pnum number,
   presult out sys_refcursor
)
is
begin
    open presult for
        select * from tblStudent where num = pnum;
end procPrinetBasicStudent;

/
--------------------------------------------------------
--  DDL for Procedure PROCPRINTALLTEACHERRATING
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCPRINTALLTEACHERRATING" (
    presult out sys_refcursor
)
is
begin
    open presult for
    select t.name teacherName, ch.openCourseNum openCourseNum, round(avg(sbr.score),1) score from tblratingQuestion rq
    inner join tblscoreByRating sbr
        on rq.num = sbr.ratingQuestionNum
            inner join tblrating r
                on r.num = sbr.ratingNum
                    inner join tblcourseHistory ch
                        on ch.num = r.coursehistorynum
                            inner join tblopenCourse oc
                                on oc.num = ch.openCourseNum
                                    inner join tblteacher t
                                        on t.num = oc.teacherNum
                                            group by t.name, ch.openCourseNum;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCPRINTATTENDANCEDATE
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCPRINTATTENDANCEDATE" (
    pyear varchar2,
    pmonth varchar2,
    pdate varchar2,
    presult out sys_refcursor
)
is
begin
open presult for
    SELECT s.name as sname 
        ,to_char(a.enterTime, 'hh24:mi:ss') as enterTime,
        to_char(a.outtime, 'hh24:mi:ss') as outTime,a.status as status
        from tblattendance a join tblcourseHistory h
        on a.coursehistorynum = h.num  
            join tblStudent s
            on s.num = h.studentnum
                join tblallcourse ac
                on ac.num = h.opencoursenum
            where to_char(a.entertime,'yy/mm/dd') = pyear||'/'||pmonth||'/'||pdate;-- 출력 원하는 학생 번호 입력    
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCPRINTATTENDANCESTUDENT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCPRINTATTENDANCESTUDENT" (
    pstudent in number,
    pcourse number,
    presult out sys_refcursor
)
is
begin
open presult for
   SELECT s.name as name,
        count(*) as total,
        count(decode(a.status,'정상',1)) as normal,
        count(decode(a.status,'지각',1)) as lateness,
        count(decode(a.status,'조퇴',1)) as early,
        count(decode(a.status,'결석',1)) as absence,
        count(decode(a.status,'외출',1)) as out,
        count(decode(a.status,'병가',1)) as sick,
        count(decode(a.status,'기타',1)) as etc
        from tblattendance a join tblcourseHistory h
         on a.coursehistorynum = h.num  
             join tblStudent s
             on s.num = h.studentnum
                join tblallcourse ac
                on ac.num = h.opencoursenum
            where s.num = pstudent and ac.num = pcourse
            group by s.name;-- 출력 원하는 학생 번호,강의번호 입력    
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCPRINTAVAILABLETEACER55
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCPRINTAVAILABLETEACER55" (
    pFirstSubject number,
    pSecondSubject number,
    pThirdSubject number,
    pFourSubject number,
    pFiveSubject number,
    pResult out sys_refcursor
)
is 
begin 
open pResult for

select teachernum as teachernum, tc.name as teachername
from(select teachernum
from(
    select *
    from tblavailablesubject
    where teachernum in (select teachernum 
                         from(
                            select * 
                            from tblavailablesubject 
                            where teachernum in (select teachernum 
                                                from(select * 
                                                     from tblavailablesubject 
                                                     where teachernum in (select teachernum 
                                                                          from(select * 
                                                                               from tblavailablesubject 
                                                                               where teachernum in (select teachernum 
                                                                                                    from tblavailablesubject 
                                                                                                    where pFirstSubject in subjectnum)) 
                                                                          where pSecondSubject in subjectnum))                         
                                                where pThirdSubject in subjectnum))                                                    
                        where pFourSubject in subjectnum))
where pFiveSubject in subjectnum) tn inner join tblteacher tc on tn.teachernum = tc.num;                                                                           

end;

/
--------------------------------------------------------
--  DDL for Procedure PROCPRINTAVAILABLETEACER6
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCPRINTAVAILABLETEACER6" (
    pFirstSubject number,
    pSecondSubject number,
    pThirdSubject number,
    pFourSubject number,
    pFiveSubject number,
    pSixSubject number,
    pResult out sys_refcursor
)
is 
begin 
open pResult for

select tc.num as teachernum, tc.name as teachername
from(select teachernum
from(select *
    from tblavailablesubject
    where teachernum in (select teachernum
                    from(
                        select *
                        from tblavailablesubject
                        where teachernum in (select teachernum 
                                             from(
                                                select * 
                                                from tblavailablesubject 
                                                where teachernum in (select teachernum 
                                                                    from(select * 
                                                                         from tblavailablesubject 
                                                                         where teachernum in (select teachernum 
                                                                                              from(select * 
                                                                                                   from tblavailablesubject 
                                                                                                   where teachernum in (select teachernum 
                                                                                                                        from tblavailablesubject 
                                                                                                                        where pFirstSubject in subjectnum)) 
                                                                                              where pSecondSubject in subjectnum))                         
                                                                    where pThirdSubject in subjectnum))                                                    
                                            where pFourSubject in subjectnum))
                    where pFiveSubject in subjectnum))
    where pSixSubject in subjectnum) tn inner join tblteacher tc on tn.teachernum = tc.num;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCPRINTAVAILABLETEACER7
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCPRINTAVAILABLETEACER7" (
    pFirstSubject number,
    pSecondSubject number,
    pThirdSubject number,
    pFourSubject number,
    pFiveSubject number,
    pSixSubject number,
    pSevenSubject number,
    pResult out sys_refcursor
)
is 
begin 
open pResult for

select tc.num as teachernum, tc.name as teachername
from(select teachernum
from(select *
    from tblavailablesubject
    where teachernum in (select teachernum
                        from(
                            select *
                            from tblavailablesubject
                            where teachernum in (select teachernum
                                                from(
                                                    select *
                                                    from tblavailablesubject
                                                    where teachernum in (select teachernum 
                                                                         from(
                                                                            select * 
                                                                            from tblavailablesubject 
                                                                            where teachernum in (select teachernum 
                                                                                                from(select * 
                                                                                                     from tblavailablesubject 
                                                                                                     where teachernum in (select teachernum 
                                                                                                                          from(select * 
                                                                                                                               from tblavailablesubject 
                                                                                                                                where teachernum in (select teachernum 
                                                                                                                                                    from tblavailablesubject 
                                                                                                                                                    where pFirstSubject in subjectnum)) 
                                                                                                                          where pSecondSubject in subjectnum))                         
                                                                                                where pThirdSubject in subjectnum))                                                    
                                                                        where pFourSubject in subjectnum))
                                                where pFiveSubject in subjectnum))
                        where pSixSubject in subjectnum))
    where pSevenSubject in subjectnum) tn inner join tblteacher tc on tn.teachernum = tc.num;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCPRINTBASICSTUDENT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCPRINTBASICSTUDENT" (
   presult out sys_refcursor
)
is
begin
    open presult for
    select num, name, ssn, tel, registerdate, deletestatus from tblStudent order by num;

end;

/
--------------------------------------------------------
--  DDL for Procedure PROCPRINTCONSULTCONTENT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCPRINTCONSULTCONTENT" (
    pConsultRequestNum number,
    pDate out date,
    pContent out varchar2
)
is
begin
    select consultdate, consultcontent into pDate,pContent from tblconsultlog where consultrequestnum = pconsultrequestnum;
exception
    when others then
    pDate := null;
    pContent := '상담 대기중입니다.';
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCPRINTCONSULTRQLIST
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCPRINTCONSULTRQLIST" (
    pOpenCourseNum number,
    pResult out SYS_REFCURSOR
)
is
begin
    open pResult for
        select cr.num as requestnum, std.num as studentnum, std.name as studentname
        from tblconsultrequest cr inner join tblcoursehistory ch on cr.coursehistorynum = ch.num inner join tblstudent std on ch.studentnum = std.num 
        where ch.opencoursenum = pOpenCourseNum and ch.deletestatus = 0;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCPRINTCONSULTRQLISTBYN
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCPRINTCONSULTRQLISTBYN" (
    pName varchar2,
    pResult out SYS_REFCURSOR
)
is

begin
    open pResult for
        
    select cr.num as requestnum, cr.requsetdate as requestdate, std.name as studentname
        from tblconsultrequest cr inner join tblcoursehistory ch on cr.coursehistorynum = ch.num inner join tblstudent std on ch.studentnum = std.num 
        where std.name = pName and ch.deletestatus = 0;
    
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCPRINTCONSULTRQLISTBYNAME
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCPRINTCONSULTRQLISTBYNAME" (
    pName varchar2,
    pResult out SYS_REFCURSOR
)
is
begin
    open pResult for
        select cr.num as requestnum, std.num as studentnum, std.name as studentname
        from tblconsultrequest cr inner join tblcoursehistory ch on cr.coursehistorynum = ch.num inner join tblstudent std on ch.studentnum = std.num 
        where std.name = pName and ch.deletestatus = 0;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCPRINTCOURSEHISTORY
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCPRINTCOURSEHISTORY" (
   presult out sys_refcursor
)
is
begin
    open presult for
    select t.num studentNum, t.name studentName, ch.num courseHistoryNum, ac.name openCourseName, ch.status, ch.enddate, ch.deletestatus from tblcoursehistory ch
        inner join tblstudent t
            on t.num = ch.studentnum
                inner join tblopencourse oc
                    on oc.num = ch.opencoursenum
                        inner join tblallcourse ac
                            on ac.num = oc.allcoursenum;

end procPrintCoursehistory;

/
--------------------------------------------------------
--  DDL for Procedure PROCPRINTENDSUBJECTLIST
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCPRINTENDSUBJECTLIST" (
    pTeachernum number,
    pResult out SYS_REFCURSOR
)
is
begin
    open presult for
    select opencoursenum, sbc.num as subjectbyperiodnum, s.name as subjectname, pbs.startdate as startdate, pbs.enddate as enddate, b.name as bookname
    from tblperiodbysubject pbs inner join tblsubjectbycourse sbc on pbs.subjectbycoursenum = sbc.num inner join tblsubject s on sbc.subjectnum = s.num inner join tblbook b on s.num = b.subjectnum 
    where opencoursenum in (select num from tblopencourse oc where teachernum = pTeachernum and startdate < sysdate and sysdate < enddate) and sysdate > pbs.enddate order by sbc.num;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCPRINTENDSUBJECTLIST2
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCPRINTENDSUBJECTLIST2" (
    pOpencoursenum number,
    pResult out SYS_REFCURSOR
)
is
begin
    open presult for
    select ch.num as coursehistorynum, std.name as studentname, std.tel as tel, ch.status as status, ch.enddate as enddate
    from tblcoursehistory ch inner join tblstudent std on ch.studentnum = std.num 
    where opencoursenum = pOpencoursenum;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCPRINTOPENCOURSEINFO
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCPRINTOPENCOURSEINFO" (
    pTeacherNum number,
    pResult out SYS_REFCURSOR
)
is
begin
    open presult for
        select oc.num as coursenum, ac.name as coursename, oc.startdate as startdate, oc.enddate as enddate, oc.classroomnum as classroomnum from tblopencourse oc inner join tblallcourse ac on oc.allcoursenum = ac.num where oc.teachernum = pTeacherNum and oc.startdate < sysdate and sysdate < oc.enddate;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCPRINTRATIO
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCPRINTRATIO" 
(
    presult out sys_refcursor,
    tnum number,
    snum number
)
is
begin
    open presult for
select distinct e.writtentestratio as writtentestratio,
            e.performancetestratio as performancetestratio,
                e.attendanceratio as attendanceratio,
                    t.name as teachername,a.num as allcoursenum,
                    (select name from tblsubject where num = c.subjectnum) as subject
from tblteacher t join tblopencourse o
        on t.num = o.teachernum
            join tblexam e
                on o.num = e.opencoursenum
                    join tblsubjectbycourse c
                        on e.subjectbycoursenum= c.num
                            join tblallcourse a
                             on o.allcoursenum = a.num
                                join tblperiodbysubject p
                                    on c.num = p.subjectbycoursenum
where testdate is not null  and t.num = tnum and c.subjectnum = snum;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCPRINTRATIOBYSUB
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCPRINTRATIOBYSUB" (
    pOpencourseNum number,
    pSubjectNum number,
    pResult out SYS_REFCURSOR
)
is
begin
    open pResult for
    select e.writtentestratio as wRatio, e.performancetestratio as pRatio, e.attendanceratio as aRatio
    from tblexam e inner join tblsubjectbycourse sbc on e.subjectbycoursenum = sbc.num inner join tblsubject s on s.num = sbc.subjectnum
    where e.opencoursenum = pOpencourseNum and e.subjectbycoursenum = pSubjectNum;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCPRINTREGITSCORE
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCPRINTREGITSCORE" (
    pOpencourseNum number,
    presult out SYS_REFCURSOR
)
is
begin
    open presult for 
    select num as examnum from tblexam where opencoursenum = pOpencourseNum;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCPRINTREGITSCORE2
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCPRINTREGITSCORE2" (
    pSubjectNum number,
    pCnt out number
)
is
begin
    select count(*) as cnt into pCnt from(select * from tblscorebysubject where examnum = pSubjectNum);
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCPRINTSCORE
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCPRINTSCORE" (
    pCourseHistoryNum number,
    pExamnum number,
    pResult out SYS_REFCURSOR
)
is
begin
    open pResult for
    select writtentestscore as wscore, performancetestscore as pscore, attendancescore as ascore from tblscorebysubject where coursehistorynum = pCourseHistoryNum and examnum = pExamnum;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCPRINTSCOREBYENDCOURSE
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCPRINTSCOREBYENDCOURSE" (
)
is
begin
   select b.writtentestscore, b.performancetestscore, b.attendancescore
    from tblteacher t join tblopencourse o
         on t.num = o.teachernum
                 join tblexam e
                     on o.num = e.opencoursenum
                        join tblscoreBysubject b
                            on  e.num = b.examnum;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCPRINTSCOREBYSUBJECT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCPRINTSCOREBYSUBJECT" (
    presult out sys_refcursor
)
is
begin
    open presult for
    select * from tblScoreBySubject;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCPRINTSCOREBYSUBJECTSTATUS
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCPRINTSCOREBYSUBJECTSTATUS" (
        presult out sys_refcursor
    )
    is
    begin
        open presult for
        select s.num studentNum, s.name studentName, ch.openCourseNum openCourseNum, ch.status status, sbc.subjectnum subjectnum,
        case
            when not writtenTestScore is null then '등록'
            else '미등록'
        end as writtenTestScore,
        case
            when not performanceTestScore is null then '등록'
            else '미등록'
        end as performanceTestScore,
        case
            when not attendanceScore is null then '등록'
            else '미등록'
        end as attendanceScore
    from tblStudent s
        left outer join tblcourseHistory ch
            on s.num = ch.studentnum
                left outer join tblscoreBySubject sbs
                    on ch.num = sbs.coursehistorynum
                        left outer join tblexam e
                            on e.num = sbs.examNum
                                left outer join tblSubjectByCourse sbc
                                    on e.subjectByCourseNum = sbc.num;

/
--------------------------------------------------------
--  DDL for Procedure PROCPRINTSJINFOPERIODBYSUBJECT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCPRINTSJINFOPERIODBYSUBJECT" (
    pnum number, -- 시행과목번호
    presult1 out sys_refcursor,
    presult2 out sys_refcursor
)
is
begin
    open presult1 for
    -- 개설 과정 정보(과정명, 과정기간(시작 년월일, 끝 년월일), 강의실)와 과목명, 과목기간(시작 년월일, 끝 년월일), 교재명, 교사명
    select distinct ac.name as ocName, oc.startdate as startdate, oc.enddate as enddate, oc.classroomnum as classroomnum, tc.name as teachername
    from 
    tblperiodBySubject pbs inner join tblopenCourse oc 
        on pbs.opencoursenum = oc.num inner join tblallcourse ac
            on oc.allcoursenum = ac.num inner join tblsubject s
                on pbs.subjectbycoursenum = s.num inner join tblteacher tc
                    on oc.teachernum = tc.num inner join tblbook b
                        on s.num = b.num
    where opencoursenum = pnum;

    open presult2 for
    select  pbs.num as subjectnum, s.name as subjectname,  pbs.startdate as startdate, pbs.enddate as enddate, b.name as bookname
    from 
    tblperiodBySubject pbs inner join tblopenCourse oc 
        on pbs.opencoursenum = oc.num inner join tblallcourse ac
            on oc.allcoursenum = ac.num inner join tblsubject s
                on pbs.subjectbycoursenum = s.num inner join tblteacher tc
                    on oc.teachernum = tc.num inner join tblbook b
                        on s.num = b.num
    where opencoursenum = pnum;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCPRINTSJLISTOFOPENCOURSE
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCPRINTSJLISTOFOPENCOURSE" (
    pOpencoursenum number,
    pResult out SYS_REFCURSOR
    )
is
begin
    open pResult for

   select sbc.num as num, s.name as name, s.essentialtype as type, sbc.seq as seq
    from tblsubjectbycourse sbc inner join tblsubject s on sbc.subjectnum = s.num
    where allcoursenum = (select allcoursenum from tblopencourse where num = pOpencoursenum) and s.deletestatus = 0
    order by sbc.seq;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCPRINTSPECIFICSTUDENT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCPRINTSPECIFICSTUDENT" (
    pnum in number, -- 학생번호
    presult out sys_refcursor
)
is
begin
open presult for
select ac.name courseName, oc.startdate courseStartDate, oc.enddate courseEndDate, oc.classroomnum classroomnum, ch.status status, ch.enddate enddate
    from tblStudent s
        inner join tblcoursehistory ch on s.num = ch.studentnum
            inner join tblOpenCourse oc on ch.opencoursenum = oc.num
                inner join tblallcourse ac on oc.allCoursenum = ac.num
                    where s.num = pnum;  -- 출력 원하는 학생 번호     
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCPRINTSTUDENT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCPRINTSTUDENT" (
   presult out sys_refcursor
)
is
begin
    open presult for
    select s.name as name, s.ssn as ssn, s.tel as tel, s.registerdate as registerdate, count(ch.studentnum) as countCourseHistory from tblStudent s -- 학생 테이블
        inner join tblcoursehistory ch -- 수강내역 테이블
           on s.num = ch.studentnum -- 학생테이블 번호 = 수강내역에 학생테이블 번호
                group by s.name, s.ssn, s.tel, s.registerdate;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCPRINTSTUDENTCOURSECOUNT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCPRINTSTUDENTCOURSECOUNT" (
   presult out sys_refcursor
)
is
begin
    open presult for
    select s.name as name, s.ssn as ssn, s.tel as tel, s.registerdate as registerdate, count(ch.studentnum) as countCourseHistory from tblStudent s -- 학생 테이블
        inner join tblcoursehistory ch -- 수강내역 테이블
           on s.num = ch.studentnum -- 학생테이블 번호 = 수강내역에 학생테이블 번호
                group by s.name, s.ssn, s.tel, s.registerdate;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCPRINTSUBJECTINFOBYCOURSE
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCPRINTSUBJECTINFOBYCOURSE" (
    pnum number, -- 개설과정 번호
    presult out sys_refcursor
)
is
begin
    open presult for
        select openCoursenum, ac.name as coursename, sub.sn as subject from
            (select oc.num as openCoursenum, sb.name as sn, sbc.period as sp  from tblopenCourse oc
                join tblperiodBySubject pbs
                    on pbs.openCourseNum = oc.num
                        join tblSubjectByCourse sbc
                            on sbc.num = pbs.subjectByCourseNum
                                join tblSubject sb
                                    on sb.num = sbc.subjectNum) sub
                                         join tblallCourse ac
                                            on ac.num = sub.openCoursenum
                                                where sub.openCoursenum = pnum;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCPRINTSUBJECTLIST
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCPRINTSUBJECTLIST" (
    pCourseNum number,          -- 입력받은 과정번호
    pResult1 out sys_refcursor,  --반환값으로 커서를 사용할 때 사용하는 자료형
    pResult2 out sys_refcursor
)
is
begin
    open pResult1
        -- 입력받은 과정번호와 일치하는 과목 리스트 
        for 
        select name  from tblallcourse where num = pCourseNum;

    open pResult2
        for
        select sc.num snumbycourse, s.name subjectname from tblsubjectbycourse sc inner join tblallcourse a on sc.allcoursenum = a.num inner join tblsubject s on sc.subjectnum = s.num where allcoursenum = pCourseNum;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCPRINTSUPPORTLIST
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCPRINTSUPPORTLIST" 
(
    presult out sys_refcursor,
    pstudentnum number
)
is
begin
open presult for

select s.name,h.studentnum,spl.name
from tblsupport sp join tblsupportlist spl
                on spl.num = sp.supportlistnum
                join tblcoursehistory h
                on sp.coursehistorynum = h.num
                join tblstudent s
                on h.studentnum = s.num
                where studentnum = pstudentnum;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCPRINTTEACHERMYRATING
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCPRINTTEACHERMYRATING" (
    pnum in number, -- 교사번호
    presult out sys_refcursor
)
is
begin
    open presult for
    select t.name teacherName, ch.openCourseNum, rq.description, round(avg(sbr.score),1) score 
    from tblratingQuestion rq -- 교사평가별 항목
        inner join tblscoreByRating sbr -- 교사평가 항목별 점수
            on rq.num = sbr.ratingQuestionNum
                inner join tblrating r -- 교사 평가
                    on r.num = sbr.ratingNum
                        inner join tblcourseHistory ch -- tblcourseHistory
                            on ch.num = r.coursehistorynum
                                inner join tblopenCourse oc -- 시행 과정(강의)
                                    on oc.num = ch.openCourseNum
                                        inner join tblteacher t -- tblteacher
                                            on t.num = oc.teacherNum
                                                where t.num = pnum -- 교사번호
                                                    group by t.name, ch.openCourseNum, rq.description; -- 교사이름, 시행과정번호, 평가항목  
end;   

/
--------------------------------------------------------
--  DDL for Procedure PROCQUESTIONBYSUBJECT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCQUESTIONBYSUBJECT" (
    pStudentNum number,     --학생번호
    pSubjectNum number,     --과목번호
    presult out SYS_REFCURSOR
)
is
begin

open presult for
select 
    q.type,
    q.question,
    q.answer
from tblcoursehistory ch
    inner join tblopencourse oc
        on ch.opencoursenum = oc.num
            inner join tblperiodbysubject ps
                on oc.num = ps.opencoursenum
                    inner join tblsubjectbycourse sc
                        on ps.subjectbycoursenum = sc.num
                            inner join tblexam e
                                on sc.num = e.subjectbycoursenum
                                    inner join tblquestionbyexam q
                                        on e.num = q.examnum
                                            where ch.studentnum = pStudentNum and sc.subjectnum =pSubjectNum;

end procQuestionBySubject;

/
--------------------------------------------------------
--  DDL for Procedure PROCRATINGQUESTION
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCRATINGQUESTION" (
    pnum number,    -- tblRatingquestion의 고유 번호 
    pdescription varchar2,  -- tblRatingquestion의 고유 번호 평가 항목
    presult out number  
)
is 
begin
    --insert
    insert into tblratingQuestion(num,description)
    values(pnum,pdescription);
    presult := 1;
exception 
    when others then 
        presult := 0;

end procratingQuestion;

/
--------------------------------------------------------
--  DDL for Procedure PROCRETURNEXAMPK
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCRETURNEXAMPK" (
    pSubjectNum number,
    pOpencourseNum number,
    pExamPknum out number
)
is
begin
    select e.num into pExamPknum from tblsubjectbycourse sbc inner join tblexam e on e.subjectbycoursenum = sbc.num where e.subjectbycoursenum = pSubjectNum and e.opencoursenum = pOpencourseNum;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCSCOREBASICINFOBYSTUDENT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCSCOREBASICINFOBYSTUDENT" (
    pnum number,    -- 학생번호
    presult out sys_refcursor
)
is
begin
    open presult for
        select distinct st.name as studentname, substr(st.ssn, 8, 7) as ssn, ac.name as coursename, to_char(oc.startdate, 'yyyy-mm-dd') as startdate, to_char(oc.enddate, 'yyyy-mm-dd') as enddate, oc.classroomnum as classroomname from tblstudent st
            inner join tblcoursehistory ch
                on st.num = ch.studentnum
                    inner join tblopencourse oc
                        on oc.num = ch.opencoursenum
                            inner join tblallcourse ac
                                on ac.num = oc.allcoursenum
                                    where st.num = pnum;
end;     

/
--------------------------------------------------------
--  DDL for Procedure PROCSCOREBASICINFOBYSUBJECT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCSCOREBASICINFOBYSUBJECT" (
    pnum number, -- 과목 번호
    presult out sys_refcursor
)
is
begin
    open presult for
        select sub.onum as opencoursenum, sub.name as coursename, to_char(sub.sd, 'yyyy-mm-dd') as startdate, to_char(sub.ed, 'yyyy-mm-dd') as enddate, sub.crn as classromname, sub.sbbame as subjectname, tc.name as teachername, sub.bkname as bookname from (
            select ac.name as name, oc.startdate as sd, oc.enddate as ed, oc.classRoomNum as crn, sb.name as sbbame, bk.name as bkname, oc.teachernum as tnum, sb.num as subjectnum, oc.num as onum from tblopenCourse oc
                 join tblallCourse ac
                    on ac.num = oc.allCourseNum
                         join tblSubjectByCourse sbc
                            on sbc.allCourseNum = ac.num
                                 join tblsubject sb
                                    on sb.num = sbc.subjectnum
                                         join tblBook bk
                                            on bk.subjectNum = sb.num) sub
                                                 join tblteacher tc
                                                    on tc.num = sub.tnum
                                                        where sub.subjectnum = pnum order by onum; 
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCSCOREBYSTUDENT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCSCOREBYSTUDENT" (
    pnum number,
    presult out sys_refcursor
) 
is 
begin
    open presult for
       select 
            s.name as studentName,
            sb.name as subjectName,
            ss.writtenTestScore as writtenTestScore,
            ss.performanceTestScore as performanceTestScore,
            ss.attendancescore as attendancescore
        from tblStudent s
            inner join tblCourseHistory ch
                on s.num = ch.studentnum
                    inner join tblScoreBySubject ss
                         on ch.num = ss.courseHistoryNum
                            inner join tblexam e
                                on ss.examnum = e.num
                                    inner join tblSubjectByCourse sc
                                        on e.subjectByCourseNum = sc.num  
                                            inner join tblsubject sb
                                                on sc.subjectnum = sb.num
                                                    where s.num = pnum;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCSCOREINFOBYSTUDENT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCSCOREINFOBYSTUDENT" (
    pnum number,    -- 학생번호
    presult out sys_refcursor
)
is
begin
    open presult for
    select distinct sb.name as subjectname, sbs.attendancescore as attendancescore ,sbs.writtentestscore as writtentestscore, sbs.performancetestscore as performancetestscore from tblstudent st
        inner join tblcourseHistory ch
            on ch.studentnum = st.num
                inner join tblscoreBySubject sbs
                    on sbs.coursehistorynum =ch.num
                        inner join tblexam ex
                            on ex.num = sbs.examnum
                                inner join tblSubjectByCourse sbc
                                    on sbc.num = ex.subjectByCourseNum
                                        inner join tblSubject sb
                                            on sb.num = sbc.subjectnum
                                                where st.num = pnum;
end;     

/
--------------------------------------------------------
--  DDL for Procedure PROCSCOREINFOBYSUBJECT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCSCOREINFOBYSUBJECT" (
    pnum number, -- 과목 번호
    ocnum number, -- 과정 번호
    presult out sys_refcursor
)
is
begin
    open presult for
select distinct sb.name as subjectname, st.name as studentname, substr(st.ssn, 8, 7) as studentssn, sbs.writtentestscore as writtentestscore, sbs.performancetestscore as performancetestscore from tblstudent st
    inner join tblcoursehistory ch
        on ch.studentnum = st.num
            inner join tblscorebysubject sbs
                on sbs.coursehistorynum = ch.num
                    inner join tblexam ex
                        on ex.num = sbs.examNum
                            inner join tblsubjectbycourse sbc
                                on sbc.num = ex.subjectbycoursenum
                                    inner join tblSubject sb
                                        on sb.num = sbc.subjectnum
                                            where sb.num = pnum and ch.opencoursenum = ocnum
                                                order by studentname;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCSCOREINFOTNAMEBYSTUDENT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCSCOREINFOTNAMEBYSTUDENT" (
    pnum number,    -- 학생번호
    presult out sys_refcursor
)
is
begin
    open presult for
select sb.name as subjectname, to_char(pbs.startdate, 'yyyy-mm-dd') as startDate, to_char(pbs.enddate, 'yyyy-mm-dd') as enddate, sub.tcname as teachername from tblstudent st
    inner join tblcoursehistory ch
        on ch.studentnum = st.num
            inner join
                (select oc.num as ocnum, tc.name as tcname from tblopencourse oc
                    inner join tblteacher tc
                        on oc.teacherNum = tc.num) sub
                            on sub.ocnum = ch.opencoursenum
                                inner join tblperiodBySubject pbs
                                    on pbs.opencoursenum = sub.ocnum
                                        inner join tblSubjectByCourse sbc
                                            on sbc.num = pbs.subjectbycoursenum
                                                inner join tblsubject sb
                                                    on sb.num = sbc.subjectnum 
                                                         where st.num = pnum;
end;     

/
--------------------------------------------------------
--  DDL for Procedure PROCSELECTATTENDANCESTATUS
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCSELECTATTENDANCESTATUS" (

    pnum number,
    pentertimey varchar2,
    pentertimem varchar2,
    pentertimed varchar2,
    presult out sys_refcursor
    
)
is
begin
    open presult for
        select 
            distinct s.name as name , to_char(a.entertime, 'YYYY-MM-DD-HH24-MI-SS') as entertime, to_char(a.outtime, 'YYYY-MM-DD-HH24-MI-SS') as outtime, a.status as status, a.num as anum from tblstudent s
                inner join tblcourseHistory c
                    on s.num = c.studentNum
                        inner join tblattendance a
                            on a.coursehistorynum = c.num
                                    where s.num = pnum
                                        and to_char(a.entertime, 'YYYY-MM-DD') = to_date( pentertimey || pentertimem || pentertimed, 'yyyy-mm-dd');
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCSELECTMAKEUPCLASSSTUDENT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCSELECTMAKEUPCLASSSTUDENT" (
    pnum number, -- 과목번호
    presult out sys_refcursor
)
is
begin
    open presult for
    select st.name as studentname, substr(st.ssn, 8, 7) as ssn, 
             s.name as subjectname, sbs.writtenTestScore as writtenTestScore, 
             sbs.performanceTestScore as performanceTestScore, 
             sbs.attendancescore as attendanceScore, 
             to_char(mc.startdate, 'yyyy-hh-dd') || ' ~ ' ||to_char(mc.enddate, 'yyyy-hh-dd') as period
             from tblStudent st
        inner join tblcourseHistory ch
            on st.num = ch.studentnum
                inner join tblscoreBySubject sbs
                    on sbs.courseHistoryNum = ch.num
                        inner join tblexam ex
                            on ex.num =  sbs.examNum
                                inner join tblSubjectByCourse sbc
                                    on sbc.num = ex.subjectByCourseNum
                                        inner join tblSubject s
                                            on s.num = sbc.subjectNum
                                                inner join tblmakeupClassInfo mc
                                                    on mc.scorebysubjectnum = sbs.num
                                                        where (writtenTestScore * ex.writtenTestRatio + performancetestscore * ex.performanceTestRatio + attendancescore * ex.attendanceratio ) <=50
                                                            and s.num = pnum;  
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCSSELECTOUTSTANDINGREWARD
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCSSELECTOUTSTANDINGREWARD" (
    pnum number, -- 과목번호
    presult out sys_refcursor
)
is
begin
    open presult for
    select osr.scoreBySubjectNum as scorebysubjectnum, 
            sbs.num as studentnum, st.name as name, substr(st.ssn, 8, 7) as ssn, 
            s.name as subjectname, sbs.writtenTestScore as writtenTestScore, 
            sbs.performanceTestScore as performanceTestScore, 
            sbs.attendancescore as attendanceScore, osr.status as status  from tblStudent st
        inner join tblcourseHistory ch
            on st.num = ch.studentnum
                inner join tblscoreBySubject sbs
                    on sbs.courseHistoryNum = ch.num
                        inner join tblexam ex
                            on ex.num =  sbs.examNum
                                inner join tblSubjectByCourse sbc
                                    on sbc.num = ex.subjectByCourseNum
                                        inner join tblSubject s
                                            on s.num = sbc.subjectNum
                                                inner join tbloutstandingReward osr
                                                    on osr.scorebysubjectnum = sbs.num
                                                        where (writtenTestScore * ex.writtenTestRatio + performancetestscore * ex.performanceTestRatio + attendancescore * ex.attendanceratio ) >=90
                                                            and s.num = pnum;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCSTUDENTATTENDANCE
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCSTUDENTATTENDANCE" (

    pname String,
    pentertime date,
    pouttime date
    
)
is
begin

    for select 
        distinct s.name, a.entertime, a.outtime, a.status from tblstudent s
            inner join tblcourseHistory c
                on s.num = c.studentNum
                    inner join tblattendance a
                        on a.coursehistorynum = c.num
                            order by name
                                where a.name = pname
                                    and a.entertime = to_char(pentertime, 'DD-MM');

end;



-- 매일 근태 관리를 기록할 수 있어야 한다.(출근 1회, 퇴근 1회)
-- insert into tblattendance (attendanceseq, Coursehistorynum, entertime, outtime, status) values (attendanceseq, Coursehistorynum, entertime, outtime, status);
create or replace procedure procAddAttendance(

    pattendanceseq number,
    pcoursehistorynum number,
    pentertime date,
    pouttime date,
    pstatus string

)
is
begin
    for insert into tblattendance (attendanceseq, Coursehistorynum, entertime, outtime, status) 
    values (pattendanceseq, pcoursehistorynum, pentertime, pouttime, pstatus);
end;





, sub.name as 과목명


-- [성적우수자]
--------------------------------------------------------------------------------------------------------------------
-- 과목별로 성적우수자 출력 (평균 95점 이상)

select 
    st.name as studentname,
    aa.subjectname as subject,
    ss.writtenTestScore as writtenScore,
    ss.performanceTestScore as performanceScore,
    ss.attendanceScore as attendanceScore,
    aa.snum


from tblStudent st
    inner join tblCourseHistory h
        on st.num = h.studentNum
            inner join tblScoreBySubject ss
                on ss.courseHistoryNum = h.num,
                    (select e.num as examNum, s.name as subjectName, s.num as snum from tblExam e
                        inner join tblSubjectByCourse sc
                            on e.subjectByCourseNum = sc.num
                                inner join tblSubject s
                                    on s.num = sc.subjectNum) aa
                                        where  aa.examNum = ss.examNum
                                            order by studentname , snum;



select * from tblsubjectbycourse;

select * from tblstudent where name = '강시수';

/
--------------------------------------------------------
--  DDL for Procedure PROCSTUDENTATTENDANCEBYDATE
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCSTUDENTATTENDANCEBYDATE" (

    pnum number,
    pentertimey varchar2,
    pentertimem varchar2,
    pentertimed varchar2,
    pouttimey varchar2,
    pouttimem varchar2,
    pouttimed varchar2,
    presult out sys_refcursor
    
)
is
begin
    open presult for
        select 
            distinct s.name as name , to_char(a.entertime, 'YYYY-MM-DD-HH24-MI-SS') as entertime, to_char(a.outtime, 'YYYY-MM-DD-HH24-MI-SS') as outtime, a.status as status from tblstudent s
                inner join tblcourseHistory c
                    on s.num = c.studentNum
                        inner join tblattendance a
                            on a.coursehistorynum = c.num
                                    where s.num = pnum
                                        and a.entertime between to_date( pentertimey || pentertimem || pentertimed, 'yyyy-mm-dd') and to_date(pouttimey || pouttimem || pouttimed, 'yyyy-mm-dd') + (INTERVAL '1' DAY) 
                                            order by entertime;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCSTUDENTENDDATE
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCSTUDENTENDDATE" (
    pendDate varchar2,
    pcourseHistoryNum tblcourseHistory.num%type
)
is 
begin
    update tblcourseHistory set enddate = to_date(pendDate,'yyyymmdd') where num = pcourseHistoryNum and deletestatus <> 1;

end procstudentEndDate;

/
--------------------------------------------------------
--  DDL for Procedure PROCSTUDENTINFOBYSTUDENT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCSTUDENTINFOBYSTUDENT" (
    pnum number,
    presult out sys_refcursor
)
is
begin
    open presult
        for select 
                s.name as studentName,
                s.tel as studentTel,
                ac.name as courseName, 
                to_char(oc.startDate, 'yyyy-mm-dd')|| ' ~ ' ||  to_char(oc.endDate, 'yyyy-mm-dd') as period,
                r.name as classRoom
             from tblStudent s
                    inner join tblCourseHistory h
                        on s.num = h.studentnum
                            inner join tblOpenCourse oc
                                on oc.num = h.opencoursenum
                                    inner join  tblAllCourse ac 
                                        on oc.allcoursenum = ac.num
                                            inner join tblClassRoom r
                                                on oc.classroomnum = r.num
                                                    --입력한 학생 번호와 일치하는 내용만 가져오기!!
                                                    where s.num = pnum;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCSTUDENTJOBACTIVITY
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCSTUDENTJOBACTIVITY" 
(
    psnum number,
    presult out sys_refcursor
)
is
begin
open presult for
select h.num as coursenum, ja.content as activity, s.name studentname
from tbljobActivity ja join tblcoursehistory h
        on ja.coursehistorynum = h.num
            join tblstudent s
                on h.studentnum = s.num
where ja.coursehistorynum in (select h.num
                            from tblcoursehistory h join tblstudent s
                            on h.studentnum = s.num
                             where s.num = psnum and h.status = '수료');
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCSUBJECTINFOBYCOURSE
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCSUBJECTINFOBYCOURSE" (
    pnum number, -- 개설과정 번호
    presult out sys_refcursor
)
is
begin
    open presult for
        select ac.name as coursename, sub.sn as subject from
            (select oc.num as openCoursenum, sb.name as sn, sbc.period as sp  from tblopenCourse oc
                join tblperiodBySubject pbs
                    on pbs.openCourseNum = oc.num
                        join tblSubjectByCourse sbc
                            on sbc.num = pbs.subjectByCourseNum
                                join tblSubject sb
                                    on sb.num = sbc.subjectNum) sub
                                         join tblallCourse ac
                                            on ac.num = sub.openCoursenum
                                                where sub.openCoursenum = pnum;
end;         

/
--------------------------------------------------------
--  DDL for Procedure PROCSUBJECTINFOBYSTUDENT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCSUBJECTINFOBYSTUDENT" (
    pnum number,
    presult out sys_refcursor
) 
is 
begin
    open presult for
        select 
            distinct
            sb.num as SubjectNum,
            sb.name as SubjectName,
            to_char(p.startdate, 'yyyy-mm-dd') || ' ~ ' ||  to_char(p.endDate, 'yyyy-mm-dd') as period,
            b.name as bookName
        from tblStudent s
            inner join tblCourseHistory ch
                on s.num = ch.studentnum
                    inner join tblOpenCourse oc
                        on ch.openCourseNum = oc.num
                            inner join tblPeriodBySubject p
                                on oc.num = p.openCourseNum
                                    inner join tblSubjectByCourse sc
                                        on p.subjectByCourseNum = sc.num
                                            inner join tblSubject sb
                                                on sc.subjectNum = sb.num
                                                    inner join tblBook b
                                                        on sb.num = b.subjectNum
                                                            where s.num  = pnum
                                                                order by period;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCSUBJECTLIST
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCSUBJECTLIST" 
(
    presult out sys_refcursor
)
IS
BEGIN
 open presult for
select distinct  s.num as subjectnum,
        oc.num as coursenum,
        oc.startdate as startDate,
        oc.enddate as endDate,
        r.name as classroomName,
        s.name as subjectName,
        pc.startdate as startDate,
        pc.enddate as endDate,
        b.name as bookName,
        e.writtenTestRatio as writtenTestRatio, 
        e.performanceTestratio as performanceTestRatio, 
        e.attendanceRatio as attenDanceRatio
from tblsubject s join tblbook b
            on s.num = b.subjectnum
                join tblsubjectbycourse sc
                    on s.num = sc.subjectnum
                        join tblperiodbysubject pc
                            on sc.num = pc.subjectbycoursenum
                                join tblopencourse oc
                                    on oc.num = pc.opencoursenum
                                        join tblexam e
                                            on e.opencoursenum = oc.num
                                                join tblclassroom r
                                                    on r.num = oc.classroomnum
order by coursenum;
END;

/
--------------------------------------------------------
--  DDL for Procedure PROCSUBJECTLIST1
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCSUBJECTLIST1" 
(
    presult out sys_refcursor,
    ptnum number
)
is
begin
open presult for
select distinct a.name as coursenum,c.name as roomname,o.startdate ||' ~ '|| o.enddate as cperiod
from tblallcourse a join tblopencourse o
                    on a.num = o.allcoursenum
                    join tblclassroom c
                    on c.num = o.classroomnum
    where o.teachernum = ptnum and c.num = o.classroomnum
order by cperiod;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCSUBJECTLIST2
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCSUBJECTLIST2" 
(
 presult out sys_refcursor,
 ptnum number
)
is
begin
open presult for
select distinct s.num as snum,s.name as sname,b.name as bookname,a.name as coursename, p.startdate ||' ~ '||p.enddate as speriod 
from tblallcourse a join tblsubjectbycourse sc
                    on a.num = sc.allcoursenum
                    join tblperiodbysubject p
                    on p.subjectbycoursenum = sc.num
                    join tblsubject s
                    on sc.subjectnum = s.num
                    join tblbook b
                    on b.subjectnum = s.num
                    join tblopencourse o
                    on a.num = o.allcoursenum
where o.teachernum = ptnum
order by speriod;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCSUPDATEOUTSTANDINGREWARD
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCSUPDATEOUTSTANDINGREWARD" (
    pnum number -- 시험 점수 번호
)
is
begin

    update tbloutstandingReward
    set
    status = '지급'
    where 
    scoreBySubjectNum = pnum;

end;

/
--------------------------------------------------------
--  DDL for Procedure PROCSUPPORT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCSUPPORT" 
(
    pstudent number,
    psupport number
)
is
begin
    insert into tblsupport values (SUPPORTSEQ.nextval,pstudent,psupport);
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCSUPPORTLIST
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCSUPPORTLIST" (
    pnum number,    -- proSupportList의 고유번호
    pname varchar2, -- 취업지원 이름
    presult out number
)
is 
begin
    --insert
    insert into tblSupportList(num,name)
    values(pnum,pname);
    presult := 1;
exception 
    when others then 
        presult := 0;

end  procSupportList;

/
--------------------------------------------------------
--  DDL for Procedure PROCTEACHERATTENDANCE
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCTEACHERATTENDANCE" (
    pnum tblopenCourse.num%type,
    pteacherNum tblopenCourse.teacherNum%type,
    presult out sys_refcursor
)
is
begin
open presult for 
  select o.num as "openCourseNum",s.name as "studentName", t.name "teacherName", o.startDate || '~' || o.endDate as "coursePeriod", h.status as "courseStatus", a.status as "attendanceStatus", to_char(a.enterTime,'yyyy-mm-dd hh24:mi:ss') as "enterTime",
  to_char(a.outTime,'yyyy-mm-dd hh24:mi:ss') as "outTime", o.allCourseNum, o.teacherNum
     from tblstudent s
        inner join tblcourseHistory h
            on h.studentNum = s.num
             inner join tblattendance a 
                 on a.courseHistoryNum = h.num
                    inner join tblopenCourse o
                        on h.openCourseNum = o.num
                            inner join tblteacher t
                                on t.num = o.teacherNum
                                     where o.num = pnum and o.teacherNum = pteacherNum and o.status <> 1 and h.status <> '수료' and h.status <> '중도탈락' and s.deletestatus <> 1 and h.deletestatus <> 1
                                        and t.deletestatus <> 1;

end procteacherAttendance;

/
--------------------------------------------------------
--  DDL for Procedure PROCTEACHERATTENDANCE1
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCTEACHERATTENDANCE1" (
    presult out sys_refcursor,
    pyear varchar2,
    pteacherNum number
)
is
begin
open presult for 
    select h.opencoursenum, s.name as "student name", o.teacherNum, to_char(a.enterTime,'yyyy-mm-dd hh24:mi:ss') as "enterTime", to_char(a.outTime,'yyyy-mm-dd hh24:mi:ss') as "outTime", a.status as "attendance status",h.status as "course status" 
        from tblattendance a
            inner join tblcourseHistory h
                 on h.num = a.courseHistoryNum 
                    inner join tblstudent s
                        on s.num = h.studentnum
                            inner join tblopenCourse o
                                on o.num = h.opencoursenum
                                     where to_char(a.enterTime,'yyyy') = pyear and s.deleteStatus <> 1 and h.status <> '수료' and h.status <> '중도탈락' and h.deletestatus <> 1 and o.teacherNum = pteacherNum and o.status <> 1
                                        order by to_char(a.enterTime,'mm'),to_char(a.enterTime,'dd'),a.num;
end procteacherAttendance1;

/
--------------------------------------------------------
--  DDL for Procedure PROCTEACHERATTENDANCE2
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCTEACHERATTENDANCE2" (
    presult out sys_refcursor,
    pmonth varchar2,
    pteacherNum number
)
is
begin
open presult for 
      select h.opencoursenum, s.name as "student name", to_char(a.enterTime,'yyyy-mm-dd hh24:mi:ss') as "enterTime", to_char(a.outTime,'yyyy-mm-dd hh24:mi:ss') as "outTime", a.status as "attendance status",h.status as "course status" 
        from tblattendance a
            inner join tblcourseHistory h
                on h.num = a.courseHistoryNum 
                    inner join tblstudent s
                        on s.num = h.studentnum
                         inner join tblopenCourse o
                                on o.num = h.opencoursenum
                                     where to_char(a.enterTime,'mm') = pmonth and s.deleteStatus <> 1 and h.status <> '수료' and h.status <> '중도탈락' and h.deletestatus <> 1 and o.status <> 1 and o.teacherNum = pteacherNum
                                        order by to_char(a.enterTime,'yyyy'),to_char(a.enterTime,'dd'),a.num;
end procteacherAttendance2;

/
--------------------------------------------------------
--  DDL for Procedure PROCTEACHERATTENDANCE3
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCTEACHERATTENDANCE3" (
    presult out sys_refcursor,
    pday varchar2,
    pteacherNum number
)
is
begin
open presult for 
       select h.opencoursenum, s.name as "student name", to_char(a.enterTime,'yyyy-mm-dd hh24:mi:ss') as "enterTime", to_char(a.outTime,'yyyy-mm-dd hh24:mi:ss') as "outTime", a.status as "attendance status",h.status as "course status" 
        from tblattendance a
            inner join tblcourseHistory h
                on h.num = a.courseHistoryNum 
                    inner join tblstudent s
                        on s.num = h.studentnum
                             inner join tblopenCourse o
                                    on o.num = h.opencoursenum
                                         where to_char(a.enterTime,'dd') = pday and s.deleteStatus <> 1 and h.status <> '수료' and h.status <> '중도탈락' and h.deletestatus <> 1 and o.status <> 1 and o.teacherNum = pteacherNum
                                            order by to_char(a.enterTime,'yy'),to_char(a.enterTime,'mm'),a.num;
end procteacherAttendance3;

/
--------------------------------------------------------
--  DDL for Procedure PROCTEACHERATTENDANCESELECT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCTEACHERATTENDANCESELECT" (
    presult out sys_refcursor,
    pnum tblopenCourse.num%type,
    pstudentNum tblcourseHistory.studentNum%type,
    pteacherNum number
)
is
begin
open presult for
    select o.num, o.startDate || '~' || o.endDate as "course period", (select name from tblallCourse where num = o.allCourseNum and o.status <> 1) as "course name", to_char(a.entertime,'yyyy-mm-dd hh24:mi:ss') as "enterTime",
    to_char(a.outtime,'yyyy-mm-dd hh24:mi:ss') as "outTime", s.name as "student name"
    from tblopenCourse o
        inner join tblcourseHistory h
            on o.num = h.openCourseNum
                inner join tblattendance a
                    on a.courseHistoryNum = h.num
                        inner join tblstudent s
                            on s.num = h.studentNum
                                where o.num = pnum and h.studentNum = pstudentNum and o.status <> 1 and h.deletestatus <> 1 and s.deletestatus <> 1 and h.status <> '수료' and h.status <> '중도탈락' and o.teacherNum = pteacherNum;
end procteacherAttendanceSelect;

/
--------------------------------------------------------
--  DDL for Procedure PROCTEACHERATTENDANCESTATUS
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCTEACHERATTENDANCESTATUS" (
     presult out sys_refcursor,
     pyear varchar2,
     pmonth varchar2,
     pteacherNum number
   
)
is
begin   
open presult for  
    select s.name as "studentName",to_char(a.enterTime,'yyyy:mm:dd hh24:mi:ss') as "enterTime", to_char(a.outTime,'yyyy:mm:dd hh24:mi:ss') as "outTime", h.opencoursenum as "openCourseNum" from tblattendance a
               inner join tblcourseHistory h
                            on h.num = a.courseHistoryNum 
                                 inner join tblstudent s
                                       on s.num = h.studentNum
                                            inner join tblopenCourse o
                                                on o.num = h.openCourseNum
                                                    where h.status <> '수료' and s.deletestatus <> 1 and h.deletestatus <> 1 and to_char(a.enterTime,'yyyy') = pyear and to_char(a.enterTime,'mm') = pmonth and o.teacherNum = pteacherNum and o.status <> 1;
end procteacherAttendanceStatus;

/
--------------------------------------------------------
--  DDL for Procedure PROCTEACHERBYSTUDENT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCTEACHERBYSTUDENT" (
    pnum number,
    presult out sys_refcursor
) 
is 
begin
    open presult for
        select 
            t.name as teacherName
        from tblStudent s
            inner join tblCourseHistory ch
                on s.num = ch.studentnum
                    inner join tblOpenCourse oc
                        on ch.openCourseNum = oc.num
                            inner join tblTeacher t
                                on oc.teacherNum = t.num
                                     where s.num  = pnum;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCTESTINFOBYSTUDENT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCTESTINFOBYSTUDENT" (
    pnum number,
    presult out sys_refcursor
) 
is 
begin
    open presult for
        select 
            sb.name as subjectName,
            to_char(e.testDate, 'yyyy-mm-dd') as examDate,
            e.writtenTestRatio as writtenTestRatio,
            e.performanceTestRatio as performanceTestRatio,
            e.attendanceRatio as attendanceRatio
        from tblStudent s
            inner join tblCourseHistory ch
                on s.num = ch.studentnum
                    inner join tblScoreBySubject ss
                         on ch.num = ss.courseHistoryNum
                            inner join tblexam e
                                on ss.examnum = e.num
                                    inner join tblSubjectByCourse sc
                                        on e.subjectByCourseNum = sc.num  
                                            inner join tblsubject sb
                                                on sc.subjectnum = sb.num
                                                    where s.num = pnum;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCUPDATEALLCOURSE
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCUPDATEALLCOURSE" 
(
    pnum number,
    pname varchar2,
    ppurpose varchar2,
    pcapacity number,
    pperiod number,
    result out number
)
IS
begin
result := 1;
    update tblallcourse set   
                            name = pname,
                            purpose = ppurpose,
                            capacity = pcapacity,
                            period = pperiod
                             where num = pnum;
exception
when others then
result := 0;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCUPDATEATTENDANCESTATUS
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCUPDATEATTENDANCESTATUS" (
    pnum number, -- 출석 기록
    pstauts varchar2 -- 출석 현황
)
is
begin

    update tblattendance
    set
    status = pstauts
    where 
    num = pnum;

end;

/
--------------------------------------------------------
--  DDL for Procedure PROCUPDATEBOOK
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCUPDATEBOOK" 
(
    pnum number,
    psubjectnum number,
    pname varchar2,
    pwriter varchar2,
    ppublisher varchar2,
    result out number
)
IS
begin
    result := 1;
    update  tblbook set   name = pname,
                            subjectnum = psubjectnum,
                             writer = pwriter,
                             publisher = ppublisher 
                             where num = pnum;
exception
when others then
result := 0;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCUPDATECLASSROOM
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCUPDATECLASSROOM" 
(
    pnum number,
    pname varchar2,
    pcapacity number,
    result out number
)
IS
begin
result := 1;
    update tblclassroom set name = pname, capacity = pcapacity where num = pnum;
exception
when others then
result := 0;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCUPDATECOURSE
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCUPDATECOURSE" 
(
    pkey number,
    pname varchar2,
    ppurpose varchar2,
    pcapacity number,
    pperiod number    
)
IS
BEGIN
    update tblAllcourse set num = pname where num = pkey;
    update tblAllcourse set num = ppurpose where num = pkey;
    update tblAllcourse set num = pnum where num = pkey;
    update tblAllcourse set num = pperiod where num = pkey;
END;

/
--------------------------------------------------------
--  DDL for Procedure PROCUPDATECOURSEHISTORY
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCUPDATECOURSEHISTORY" (
    pnum number,      -- 학생번호
    pStatus varchar,  -- 수료구분
    pdate date        -- 수료날짜
)
is
begin
    update tblcoursehistory -- 수강내역 테이블
        set status = pStatus, enddate = pdate -- 상태(수료여부) 입력, 날짜 입력
            where num = pnum; -- 수정할 학생 번호 입력
end procUpdateCoursehistory;

/
--------------------------------------------------------
--  DDL for Procedure PROCUPDATEDISTRBYOPENCOURSE
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCUPDATEDISTRBYOPENCOURSE" (
    pOCNum number,          --시행강좌번호
    pSubjectNum number   --과목번호
)
is 
    --입력받은 시행과정번호에 해당하는 수강내역 번호만 가져옴.
    cursor vcursor is 
        select num from tblCourseHistory where OpenCourseNum = pOCNum;
    vrow number; --수강내역 번호 레코드들을 받아줄 변수

    vBookNum number; --과목번호를 받아 교재번호로 변경하여 값을 저장할 변수

begin
    --vBooknum에 과목번호와 일치하는 교재번호를 넣어줌.
    select num into vBookNum from tblbook where subjectNum = pSubjectNum;

    open vcursor;
    loop

        fetch vcursor into vrow; --레코드 1개 가져와서 변수에 복사
        exit when vcursor%notfound; --마지막 레코드 이후 loop 탈출

        --해당 시행과정의 전체 수강내역 중 입력받은 과목번호와 맞는 레코드만 status를 1로 수정 
        update tblDistribution set status = 1 
            where courseHistoryNum = vrow 
                and booknum = vBookNum;

    end loop;
    close vcursor;
end procUpdateDistrByOpenCourse;

/
--------------------------------------------------------
--  DDL for Procedure PROCUPDATEDISTRBYSTUDENT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCUPDATEDISTRBYSTUDENT" (
    pStudentNum number,          --시행강좌번호
    pSubjectNum number   --과목번호
)
is     
    vCourseHistoryNum number; --학생번호를 받아 수강내역번호로 변경하여 값을 저장할 변수
    vBookNum number;

begin

    --vCourseHistoryNum에  학생번호와 일치하는 수강내역번호를 넣어줌.
    select num into vCourseHistoryNum from tblCourseHistory where studentNum = pStudentNum;
    --vBooknum에 과목번호와 일치하는 교재번호를 넣어줌.
    select num into vBookNum from tblbook where subjectNum = pSubjectNum;

    --해당 시행과정의 전체 수강내역 중 입력받은 과목번호와 맞는 레코드만 status를 1로 수정 
    update tblDistribution set status = 1 
            where courseHistoryNum = vCourseHistoryNum 
                and booknum = vBookNum;
end procUpdateDistrByStudent;

/
--------------------------------------------------------
--  DDL for Procedure PROCUPDATEEQUIPBYROOM
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCUPDATEEQUIPBYROOM" (

    pClassroomNum number,
    PEquipNum number,
    pQty  number,
    presult out number

)
is
    vcount number;
begin

    presult := 0;
    select count(*) into vcount from tblequipmentbyclassroom where classroomnum = pClassroomNum and equipmentnum = PEquipNum; 

    if vcount > 0 then
        update tblequipmentbyclassroom set qty = pQty 
            where classroomnum = pClassroomNum and equipmentnum = PEquipNum;
        presult := 1;
    end if;

end;

/
--------------------------------------------------------
--  DDL for Procedure PROCUPDATESCOREBYSUBJECT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCUPDATESCOREBYSUBJECT" (
    pCourseHistoryNum number,       -- 수강내역번호
    pExamNum number,                -- 시험번호
    pWrittenTestScore number,       -- 필기점수
    pPerformanceTestScore number,   -- 실기 점수
    pAttendanceScore number         -- 출석 점수
)
is
begin
    update tblscoreBySubject 
        set writtenTestScore = pWrittenTestScore
                , performanceTestScore = pPerformanceTestScore
                    , attendanceScore = pAttendanceScore
                        where courseHistoryNum = pCourseHistoryNum and examNum = pExamNum;
end; 

/
--------------------------------------------------------
--  DDL for Procedure PROCUPDATESJTINFO
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCUPDATESJTINFO" (
    pSubjectNum number,
    pName varchar2,
    pEssentialType varchar2
)
is
begin
    update tblsubject set name = pSubjectNum, essentialtype = pEssentialType where num = pSubjectNum;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCUPDATESTUDENT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCUPDATESTUDENT" (
    pnum number, 
    pname varchar2,
    ptel varchar2,
    pssn varchar2,
    pregisterdate date
)
IS
BEGIN
    update tblStudent 
        set name = pname, tel = ptel, ssn = pssn, registerdate = pregisterdate
            where num = pnum; -- 수정할 학생 번호
END procUpdateStudent;

/
--------------------------------------------------------
--  DDL for Procedure PROCUPDATESTUDENTTEST
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCUPDATESTUDENTTEST" (
    pnum number, 
    pname varchar2,
    ptel varchar2,
    pssn varchar2,
    pregisterdate date
)
IS
BEGIN
    update tblStudent 
        set name = pname, tel = ptel, ssn = pssn, registerdate = pregisterdate
            where num = pnum; -- 수정할 학생 번호
END procUpdateStudentTest;

/
--------------------------------------------------------
--  DDL for Procedure PROCUPDATESUBJECT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCUPDATESUBJECT" 
(
    pnum number,
    pname varchar2,
    pEssentialtype varchar2,
    result out number
)
IS
begin
    result := 1;
    update tblsubject set name = pname, essentialtype = pessentialtype where num = pnum;
exception
when others then
result := 0;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCUPDATETEACHERINFO
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCUPDATETEACHERINFO" (
   
    pSelect number,         --1. 교사이름변경 / 2. 교사전화번호변경 / 3. 교사주민번호변경 
    pTeacherNum number, --교사번호
    pContent varchar2,   --변경할 값
    presult out number      --완료여부 반환값 
)
is
    vcnt number;    --해당 교사번호가 교사테이블에 존재하는지 확인해줄 변수
begin

    presult := 0;
    select count(*) into vcnt from tblTeacher where num = pTeacherNum;

    --입력받은 교사번호가 교사 테이블에 있는 경우
    if vcnt > 0  then

        if pSelect = 1 then     --1번을 선택하면 교사이름 변경
            update tblTeacher set name = pContent where num = pTeacherNum;
            presult := 1;
        elsif pselect = 2 then      --2번을 선택하면 교사전화번호 변경
             update tblTeacher set tel = pContent where num = pTeacherNum;
             presult := 1;
        elsif pselect = 3 then      --3번을 선택하면 교사주민번호 변경
             update tblTeacher set ssn = pContent where num = pTeacherNum;
             presult := 1;
        end if;       
    end if;
end procUpdateTeacherInfo;

/
--------------------------------------------------------
--  DDL for Procedure PROCWRITERRATIO
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCWRITERRATIO" 
(
    presult out sys_refcursor,
    psubjectnum number,
    pwrittenratio number,
    pperforratio number,
    pattendratio number
)
is
begin
    open presult for
select e.writtentestratio as writtentestratio,
            e.performancetestratio as performancetestratio,
                e.attendanceratio as attendanceratio,
                        o.allcoursenum,
                            e.subjectBycoursenum           
from tblteacher t join tblopencourse o
        on t.num = o.teachernum
            join tblexam e
                on o.num = e.opencoursenum
where testdate is not null;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROCWRITTENRATIO
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROCWRITTENRATIO" 
(
    presult out sys_refcursor
)
is
begin
    open presult for
select e.writtentestratio as writtentestratio,
            e.performancetestratio as performancetestratio,
                e.attendanceratio as attendanceratio,
                    t.name as teachername
from tblteacher t join tblopencourse o
        on t.num = o.teachernum
            join tblexam e
                on o.num = e.opencoursenum
where testdate is not null;
end;

/
--------------------------------------------------------
--  DDL for Procedure PROSEARCHSTUDENT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROSEARCHSTUDENT" (
  pname varchar2, -- 학생이름
  presult out sys_refcursor
)
is      
begin
    open presult for    
    select * from tblStudent where name = pname;   
end;

/
--------------------------------------------------------
--  DDL for Procedure PROUPDATECOURSEHISTORY
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."PROUPDATECOURSEHISTORY" (
    pnum number,      -- 학생번호
    pStatus varchar,  -- 수료구분
    pdate date        -- 수료날짜
)
is
begin
    update tblcoursehistory -- 수강내역 테이블
        set status = pStatus, enddate = pdate -- 상태(수료여부) 입력, 날짜 입력
            where studentnum = (select num from tblStudent where num = pnum); -- 수정할 학생 번호 입력
end proUpdateCoursehistory;

/
--------------------------------------------------------
--  DDL for Procedure TEST
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."TEST" (
    presult out sys_refcursor  
)    
is
begin
    open presult
    for
    select s.name, s.ssn, s.tel, s.registerdate, count(ch.studentnum) as 수강횟수 from tblStudent s -- 학생 테이블
        inner join tblcoursehistory ch -- 수강내역 테이블
           on s.num = ch.studentnum 
                group by s.name, s.ssn, s.tel, s.registerdate;
end;

/
--------------------------------------------------------
--  DDL for Procedure UPDATESCOREBYSUBJECT
--------------------------------------------------------
set define off;

  CREATE OR REPLACE PROCEDURE "PROJECT"."UPDATESCOREBYSUBJECT" (
    pCourseHistoryNum number,       -- 수강내역번호
    pExamNum number,                -- 시험번호
    pWrittenTestScore number,       -- 필기점수
    pPerformanceTestScore number,   -- 실기 점수
    pAttendanceScore number         -- 출석 점수
)
is
begin
    update tblscoreBySubject 
        set writtenTestScore = pWrittenTestScore
                , performanceTestScore = pPerformanceTestScore
                    , attendanceScore = pAttendanceScore
                        where courseHistoryNum = pCourseHistoryNum and examNum = pExamNum;
end; 

/
