--------------------------------------------------------
--  DDL for Trigger TRGADDREWARDMAKEUP
--------------------------------------------------------

  CREATE OR REPLACE TRIGGER "PROJECT"."TRGADDREWARDMAKEUP" 
    after
    insert on tblScorebySubject
    for each row
declare 
    vwRatio number;         --필기배점 저장할 변수
    wpRatio number;         --실기배점 저장할 변수
    vaRatio number;          --출석배점 저장할 변수
    vwScore number;         --필기점수 저장할 변수
    vpScore number;          --실시점수 저장할 변수
    vaScore number;          --출석점수 저장할 변수
    vExamNum number;    --시험번호 가져올 변수

begin

    vExamNum := :new.examnum;   --시험번호 변수에 과목별 성적 테이블에 새롭게 등록된 레코드의 시험 번호를 저장함.

    select writtentestratio into vwRatio from tblexam where vExamNum = num;               --해당 시험번호의 필기배점을 가져옴
    select performancetestratio into wpRatio from tblexam where vExamNum = num;     --해당 시험번호의 실기배점을 가져옴
    select attendanceratio into vaRatio from tblexam where vExamNum = num;                --해당 시험번호의 출석배점을 가져옴

    vwScore := :new.writtenTestScore;                --해당 시험의 필기점수를 가져옴
    vpScore :=  :new.performanceTestScore;      --해당 시험의 실기점수를 가져옴
    vaScore := :new.attendanceScore;                  --해당 시험의 출석점수를 가져옴         

    if vwscore * vwratio + vpScore * wpratio + vaScore * varatio>= 90 then          --만약 90점이 넘으면
        insert into tbloutstandingreward values (:new.num, '미지급');                                 --성적우수자 보상테이블에 내역 추가 
    elsif vwscore * vwratio + vpScore * wpratio + vaScore * varatio < 50 then       --만약 50점 이하면
        insert into tblmakeupclassinfo values (:new.num, null, null);                         --보강수업 테이블에 내역 추가   
    end if;

end;
/
ALTER TRIGGER "PROJECT"."TRGADDREWARDMAKEUP" ENABLE;
