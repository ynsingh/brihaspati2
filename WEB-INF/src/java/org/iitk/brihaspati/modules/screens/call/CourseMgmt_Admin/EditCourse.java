package org.iitk.brihaspati.modules.screens.call.CourseMgmt_Admin;

/*
 * @(#)EditCourse.java	
 *
 *  Copyright (c) 2004-2005,2013 ETRG,IIT Kanpur. 
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

/**
 *  @author: <a href="mailto:awadhk_t@yahoo.com">Awadhesh Kuamr Trivedi</a> 
 *  @author: <a href="mailto:tejdgurung20@gmail.com">Tej Bahadur</a> 
 *  @modify date: 22-04-2013,31-05-2013 
 */
import java.util.List;
import java.util.Vector;
import org.apache.turbine.util.RunData;
import org.apache.velocity.context.Context;
import org.iitk.brihaspati.modules.utils.ListManagement;
import org.iitk.brihaspati.modules.screens.call.SecureScreen_Admin;
import org.iitk.brihaspati.modules.utils.CourseManagement;
import org.iitk.brihaspati.modules.utils.InstituteDetailsManagement;
import org.iitk.brihaspati.modules.utils.ErrorDumpUtil;

public class EditCourse extends SecureScreen_Admin{
	/**
	 * Display all details of a specific course
	 * @param data RunData
	 * @param context Context
	 */ 
	public void doBuildTemplate( RunData data, Context context )
	{
		try{
			/**
			 * Get the group name from the parameter parser
			 * and retreive the all details of course 
			 * @see CourseManagement from Utils
			 */
			String LangFile=null;
			LangFile=(String)data.getUser().getTemp("LangFile");
			String GName = data.getParameters().getString("gName");
			//Vector CDetail=CourseManagement.getCourseNUserDetails(GName);
			context.put("Courseid",GName);
			String counter = data.getParameters().getString("count","");
			context.put("tdcolor",counter);
			String instituteId=InstituteDetailsManagement.getInsId(GName);
			// Get Institute Course User Details using CourseManagent Util.
			Vector CDetail=CourseManagement.getInstituteCourseNUserDetails(GName,instituteId);
			context.put("CourseDetail",CDetail);
                        // Get mapped Department List according the institute for showing in template
			List DeptList=ListManagement.getMapDeptList(instituteId);
                        context.put("deptlist",DeptList);
                        // Get mapped School/Center List according the institute for showing in template
			List mapschlist = ListManagement.getMapSchoolDeptList(instituteId,"school");
                        context.put("schlist",mapschlist);
		}
		catch(Exception e)
		{
			data.setMessage("The Error in Edit course Detail"+e);
		}
	}
}

