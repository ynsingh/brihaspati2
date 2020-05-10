package org.iitk.brihaspati.modules.screens.call.Answerbook;
/*
 * @(#) UploadAndwerbook.java
 *
 *  Copyright (c) 2005, 2008-2009 2020 ETRG,IIT Kanpur.
 *  All Rights Reserved.
 *
 *  Redistribution and use in source and binary forms, with or
 *  without modification, are permitted provided that the following
 *  conditions are met:
 *
 *  Redistributions of source code must retain the above copyright
 *  notice, this  list of conditions and the following disclaimer.
 *
 *  Redistribution in binary form must reproducuce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL ETRG OR ITS CONTRIBUTORS BE LIABLE
 *  FOR ANY DIRECT, INDIRECT, INCIDENTAL,SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT
 *  OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 *  BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 *  WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 *  OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 *  EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *
 *  Contributors: Members of ETRG, I.I.T. Kanpur
 *
 */

import java.io.File;
import java.util.List;
import java.math.BigDecimal;
import java.util.Vector;
import java.util.Calendar;

import org.apache.turbine.util.RunData;
import org.apache.turbine.om.security.User;
import org.apache.torque.util.Criteria;
import org.apache.velocity.context.Context;

import org.iitk.brihaspati.om.Courses;
import org.iitk.brihaspati.om.CoursesPeer;
import org.iitk.brihaspati.om.LiveclassPeer;
import org.iitk.brihaspati.modules.utils.UserUtil;
import org.iitk.brihaspati.modules.utils.QuotaUtil;
import org.iitk.brihaspati.modules.utils.CourseUtil;
import org.iitk.brihaspati.modules.utils.InstituteIdUtil;
import org.iitk.brihaspati.modules.utils.InstituteDetailsManagement;
import org.iitk.brihaspati.modules.utils.ExpiryUtil;
import org.iitk.brihaspati.modules.utils.YearListUtil;
import org.iitk.brihaspati.modules.utils.PasswordUtil;
import org.iitk.brihaspati.modules.utils.ErrorDumpUtil;
import org.iitk.brihaspati.modules.utils.MailNotificationThread;
import org.iitk.brihaspati.modules.screens.call.SecureScreen_Instructor;

/**
 * This class Upload file for course Answer book
 * @author <a href="mailto:nksinghiitk@yahoo.co.in">Nagendra Kumar Singh</a>
 *
 */
public class AnnounceLiveClass extends SecureScreen_Instructor {

	public void doBuildTemplate(RunData data,Context context)
	{
		try{
		
	//	Vector v=new Vector();
		User user=data.getUser();
		String uName=user.getName();
                int uid=UserUtil.getUID(uName);
//		ParameterParser pp=data.getParameters(); 	
		String dir=(String)user.getTemp("course_id");
		
		context.put("course",(String)user.getTemp("course_name"));
		
		String Caname=CourseUtil.getCourseAlias(dir);
	//	String insid=InstituteDetailsManagement.getInsId(dir);
		int iid=Integer.parseInt((String)user.getTemp("Institute_id"));
		String iname=(InstituteIdUtil.getIstName(iid)).replaceAll(" ","");
		String ranst=PasswordUtil.randmPass();
		String lcname=Caname+iname+ranst;
		context.put("lcname",lcname);

                String currentdate=ExpiryUtil.getCurrentDate("-");
                String[] temp4 = currentdate.split("-");
                String month = temp4[1];
                context.put("cmonth",month);
                String date = temp4[2];
                context.put("cday",date);
                String strdatetype=currentdate.replaceAll("-","");
                int currentdate1=Integer.parseInt(strdatetype);
                int cyear1=currentdate1/10000;
                String cyear=Integer.toString(cyear1);
                context.put("cyear",cyear);
                Vector year=YearListUtil.getYearList();
                context.put("year_list",year);
                Calendar cld = Calendar.getInstance();
                //cld.clear();
                cld.add(Calendar.DAY_OF_MONTH, 30);
                cld.add(Calendar.MINUTE, 5);
                String eYear = Integer.toString(cld.get(cld.YEAR));
                int eMon = (cld.get(cld.MONTH))+1;
                String eMonth = Integer.toString(eMon);
                int eDat = cld.get(cld.DAY_OF_MONTH);
                String eDate = Integer.toString(eDat);
                int eHou = cld.get(cld.HOUR_OF_DAY);
                String eHour = Integer.toString(eHou);
                int eMin = cld.get(cld.MINUTE);
                String eMinute = Integer.toString(eMin);
                if(eMon<10)
                        eMonth="0"+eMonth;
                        if(eDat<10)
                                eDate="0"+eDate;
                        if(eHou<10)
                                eHour="0"+eHour;
                        if(eMin<10)
                                eMinute="0"+eMinute;
                context.put("eYear",eYear);
                context.put("eMonth",eMonth);
                context.put("eDate",eDate);
                context.put("eHour",eHour);
                context.put("eMinute",eMinute);

		Criteria crit=new Criteria();
		crit.add(LiveclassPeer.COURSE_ID,dir);
		List lst=LiveclassPeer.doSelect(crit);
		context.put("lclist",lst);

		 /*
                  *method for how much time user spend in this page.
                  */

		String Role = (String)user.getTemp("role");
		if((Role.equals("student")) || (Role.equals("instructor")))
                {
                       //CourseTimeUtil.getCalculation(uid);
                       //ModuleTimeUtil.getModuleCalculation(uid);
			int eid=0;
		       MailNotificationThread.getController().CourseTimeSystem(uid,eid);
                }

		}catch(Exception ex){data.setMessage("the error in announce screens !!"+ex);}
	}
}

