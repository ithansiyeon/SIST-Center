--------------------------------------------------------
--  Ref Constraints for Table TBLATTENDANCE
--------------------------------------------------------

  ALTER TABLE "PROJECT"."TBLATTENDANCE" ADD FOREIGN KEY ("COURSEHISTORYNUM")
	  REFERENCES "PROJECT"."TBLCOURSEHISTORY" ("NUM") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TBLAVAILABLESUBJECT
--------------------------------------------------------

  ALTER TABLE "PROJECT"."TBLAVAILABLESUBJECT" ADD FOREIGN KEY ("TEACHERNUM")
	  REFERENCES "PROJECT"."TBLTEACHER" ("NUM") ENABLE;
  ALTER TABLE "PROJECT"."TBLAVAILABLESUBJECT" ADD FOREIGN KEY ("SUBJECTNUM")
	  REFERENCES "PROJECT"."TBLSUBJECT" ("NUM") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TBLBOOK
--------------------------------------------------------

  ALTER TABLE "PROJECT"."TBLBOOK" ADD FOREIGN KEY ("SUBJECTNUM")
	  REFERENCES "PROJECT"."TBLSUBJECT" ("NUM") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TBLCONSULTLOG
--------------------------------------------------------

  ALTER TABLE "PROJECT"."TBLCONSULTLOG" ADD FOREIGN KEY ("CONSULTREQUESTNUM")
	  REFERENCES "PROJECT"."TBLCONSULTREQUEST" ("NUM") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TBLCONSULTREQUEST
--------------------------------------------------------

  ALTER TABLE "PROJECT"."TBLCONSULTREQUEST" ADD FOREIGN KEY ("COURSEHISTORYNUM")
	  REFERENCES "PROJECT"."TBLCOURSEHISTORY" ("NUM") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TBLCOURSEHISTORY
--------------------------------------------------------

  ALTER TABLE "PROJECT"."TBLCOURSEHISTORY" ADD FOREIGN KEY ("STUDENTNUM")
	  REFERENCES "PROJECT"."TBLSTUDENT" ("NUM") ENABLE;
  ALTER TABLE "PROJECT"."TBLCOURSEHISTORY" ADD FOREIGN KEY ("OPENCOURSENUM")
	  REFERENCES "PROJECT"."TBLOPENCOURSE" ("NUM") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TBLDISTRIBUTION
--------------------------------------------------------

  ALTER TABLE "PROJECT"."TBLDISTRIBUTION" ADD FOREIGN KEY ("BOOKNUM")
	  REFERENCES "PROJECT"."TBLBOOK" ("NUM") ENABLE;
  ALTER TABLE "PROJECT"."TBLDISTRIBUTION" ADD FOREIGN KEY ("COURSEHISTORYNUM")
	  REFERENCES "PROJECT"."TBLCOURSEHISTORY" ("NUM") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TBLEQUIPMENTBYCLASSROOM
--------------------------------------------------------

  ALTER TABLE "PROJECT"."TBLEQUIPMENTBYCLASSROOM" ADD FOREIGN KEY ("CLASSROOMNUM")
	  REFERENCES "PROJECT"."TBLCLASSROOM" ("NUM") ENABLE;
  ALTER TABLE "PROJECT"."TBLEQUIPMENTBYCLASSROOM" ADD FOREIGN KEY ("EQUIPMENTNUM")
	  REFERENCES "PROJECT"."TBLEQUIPMENT" ("NUM") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TBLEXAM
--------------------------------------------------------

  ALTER TABLE "PROJECT"."TBLEXAM" ADD FOREIGN KEY ("OPENCOURSENUM")
	  REFERENCES "PROJECT"."TBLOPENCOURSE" ("NUM") ENABLE;
  ALTER TABLE "PROJECT"."TBLEXAM" ADD FOREIGN KEY ("SUBJECTBYCOURSENUM")
	  REFERENCES "PROJECT"."TBLSUBJECTBYCOURSE" ("NUM") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TBLHIREDGRADUATES
--------------------------------------------------------

  ALTER TABLE "PROJECT"."TBLHIREDGRADUATES" ADD FOREIGN KEY ("COURSEHISTORYNUM")
	  REFERENCES "PROJECT"."TBLCOURSEHISTORY" ("NUM") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TBLJOBACTIVITY
--------------------------------------------------------

  ALTER TABLE "PROJECT"."TBLJOBACTIVITY" ADD FOREIGN KEY ("COURSEHISTORYNUM")
	  REFERENCES "PROJECT"."TBLCOURSEHISTORY" ("NUM") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TBLMAKEUPCLASSINFO
--------------------------------------------------------

  ALTER TABLE "PROJECT"."TBLMAKEUPCLASSINFO" ADD FOREIGN KEY ("SCOREBYSUBJECTNUM")
	  REFERENCES "PROJECT"."TBLSCOREBYSUBJECT" ("NUM") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TBLOPENCOURSE
--------------------------------------------------------

  ALTER TABLE "PROJECT"."TBLOPENCOURSE" ADD FOREIGN KEY ("CLASSROOMNUM")
	  REFERENCES "PROJECT"."TBLCLASSROOM" ("NUM") ENABLE;
  ALTER TABLE "PROJECT"."TBLOPENCOURSE" ADD FOREIGN KEY ("TEACHERNUM")
	  REFERENCES "PROJECT"."TBLTEACHER" ("NUM") ENABLE;
  ALTER TABLE "PROJECT"."TBLOPENCOURSE" ADD FOREIGN KEY ("ALLCOURSENUM")
	  REFERENCES "PROJECT"."TBLALLCOURSE" ("NUM") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TBLOUTSTANDINGREWARD
--------------------------------------------------------

  ALTER TABLE "PROJECT"."TBLOUTSTANDINGREWARD" ADD FOREIGN KEY ("SCOREBYSUBJECTNUM")
	  REFERENCES "PROJECT"."TBLSCOREBYSUBJECT" ("NUM") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TBLPERIODBYSUBJECT
--------------------------------------------------------

  ALTER TABLE "PROJECT"."TBLPERIODBYSUBJECT" ADD FOREIGN KEY ("OPENCOURSENUM")
	  REFERENCES "PROJECT"."TBLOPENCOURSE" ("NUM") ENABLE;
  ALTER TABLE "PROJECT"."TBLPERIODBYSUBJECT" ADD FOREIGN KEY ("SUBJECTBYCOURSENUM")
	  REFERENCES "PROJECT"."TBLSUBJECTBYCOURSE" ("NUM") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TBLQUESTIONBYEXAM
--------------------------------------------------------

  ALTER TABLE "PROJECT"."TBLQUESTIONBYEXAM" ADD FOREIGN KEY ("EXAMNUM")
	  REFERENCES "PROJECT"."TBLEXAM" ("NUM") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TBLRATING
--------------------------------------------------------

  ALTER TABLE "PROJECT"."TBLRATING" ADD FOREIGN KEY ("COURSEHISTORYNUM")
	  REFERENCES "PROJECT"."TBLCOURSEHISTORY" ("NUM") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TBLSCOREBYRATING
--------------------------------------------------------

  ALTER TABLE "PROJECT"."TBLSCOREBYRATING" ADD FOREIGN KEY ("RATINGNUM")
	  REFERENCES "PROJECT"."TBLRATING" ("NUM") ENABLE;
  ALTER TABLE "PROJECT"."TBLSCOREBYRATING" ADD FOREIGN KEY ("RATINGQUESTIONNUM")
	  REFERENCES "PROJECT"."TBLRATINGQUESTION" ("NUM") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TBLSCOREBYSUBJECT
--------------------------------------------------------

  ALTER TABLE "PROJECT"."TBLSCOREBYSUBJECT" ADD FOREIGN KEY ("COURSEHISTORYNUM")
	  REFERENCES "PROJECT"."TBLCOURSEHISTORY" ("NUM") ENABLE;
  ALTER TABLE "PROJECT"."TBLSCOREBYSUBJECT" ADD FOREIGN KEY ("EXAMNUM")
	  REFERENCES "PROJECT"."TBLEXAM" ("NUM") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TBLSUBJECTBYCOURSE
--------------------------------------------------------

  ALTER TABLE "PROJECT"."TBLSUBJECTBYCOURSE" ADD FOREIGN KEY ("ALLCOURSENUM")
	  REFERENCES "PROJECT"."TBLALLCOURSE" ("NUM") ENABLE;
  ALTER TABLE "PROJECT"."TBLSUBJECTBYCOURSE" ADD FOREIGN KEY ("SUBJECTNUM")
	  REFERENCES "PROJECT"."TBLSUBJECT" ("NUM") ENABLE;
--------------------------------------------------------
--  Ref Constraints for Table TBLSUPPORT
--------------------------------------------------------

  ALTER TABLE "PROJECT"."TBLSUPPORT" ADD FOREIGN KEY ("COURSEHISTORYNUM")
	  REFERENCES "PROJECT"."TBLCOURSEHISTORY" ("NUM") ENABLE;
  ALTER TABLE "PROJECT"."TBLSUPPORT" ADD FOREIGN KEY ("SUPPORTLISTNUM")
	  REFERENCES "PROJECT"."TBLSUPPORTLIST" ("NUM") ENABLE;
