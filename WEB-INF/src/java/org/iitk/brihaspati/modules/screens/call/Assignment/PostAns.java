package org.iitk.brihaspati.modules.screens.call.Assignment;

/*
 * @(#) PostAns.java 
 *  Copyright (c) 2005-2006,2013 ETRG,IIT Kanpur.
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

import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.io.File;
import java.text.SimpleDateFormat;
import java.text.DateFormat;

import org.apache.turbine.util.RunData;
import org.apache.velocity.context.Context;
import org.apache.turbine.om.security.User;            
import org.apache.torque.util.Criteria;

import org.iitk.brihaspati.om.Assignment;
import org.iitk.brihaspati.om.AssignmentPeer;
import org.iitk.brihaspati.modules.utils.ExpiryUtil;
import org.iitk.brihaspati.modules.utils.ErrorDumpUtil;
import org.iitk.brihaspati.modules.screens.call.SecureScreen;
	
import org.apache.turbine.services.servlet.TurbineServlet;
import org.apache.turbine.util.parser.ParameterParser;

import org.iitk.brihaspati.modules.utils.UserUtil;
import org.iitk.brihaspati.modules.utils.ModuleTimeThread;
	/** 
	* This class contains code of Post Answer to the Assignment
	* @author<a href="arvindjss17@yahoo.co.in">Arvind Pal</a>
	* @author<a href="smita37uiet@gmail.com">Smita Pal</a>
	* @author<a href="tejdgurung20@gmail.com">Tej Bahadur</a>
	*/
 
public class PostAns extends  SecureScreen
{
	 /**
         * @param data RunData instance
         * @param context Context instance
         * @return nothing
         * @try and catch Identifies statements that user want to monitor
         * and catch for exceptoin.
        */

        public void doBuildTemplate(RunData data, Context context)
        {
                try
                {
 			User user=data.getUser(); 
			String Role = (String)user.getTemp("role");
			context.put("user_role",Role);            
			context.put("coursename",(String)user.getTemp("course_name"));
			context.put("Ans","Ans");
			context.put("tdcolor",data.getParameters().getString("count",""));
			context.put("topicnm",data.getParameters().getString("topicname",""));

			DateFormat  dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date curDate1=new Date();
                        String curdate=dateFormat.format(curDate1);
			long longcurdate=Long.parseLong(((curdate.replaceAll("-","")).replaceAll(":","")).replaceAll(" ",""));
		//	Date curDate=new Date();
                  //      long longCurDate= curDate.getTime();
			/*
                         *Time calculaion for how long user use this page.
                         */
                         int uid=UserUtil.getUID(user.getName());
                         if((Role.equals("student")) || (Role.equals("instructor")) || (Role.equals("teacher_assistant")))
                         {
				int eid=0;
				ModuleTimeThread.getController().CourseTimeSystem(uid,eid);
                         }

			Vector v=new Vector();
                        Vector w=new Vector();
			Criteria crit=new Criteria();
                        crit.add(AssignmentPeer.GROUP_NAME,(String)user.getTemp("course_id"));
                        List u=AssignmentPeer.doSelect(crit);
                        for(int i=0;i<u.size();i++)
                        {
			 	Assignment element=(Assignment)(u.get(i));
                                Date date1=(element.getDueDate());
                                Date date2=(element.getCurDate());
				String duedate=dateFormat.format(date1);
				String startdate=dateFormat.format(date2);
				long longduedate=Long.parseLong(((duedate.replaceAll("-","")).replaceAll(":","")).replaceAll(" ",""));
				long longstartdate=Long.parseLong(((startdate.replaceAll("-","")).replaceAll(":","")).replaceAll(" ",""));
				long coursedate =longduedate-longcurdate;
				long coursesdate =longcurdate-longstartdate;
                               // long longCurDate1= date1.getTime();
                               // long coursedate=(longCurDate1-longCurDate)/(24*3600*1000);
                        	/**
				*To view all the Topic name in particular Courses
				*
				*/
			        if(coursedate<0)
                                {
                                        String str2=(element.getTopicName());
                                        w.add(str2);
				}
				if((coursedate>=0)&&(coursesdate >=0))
				{
					String str2=(element.getTopicName());
					v.add(str2);  
				}
                        }
			// list for teacher
             		context.put("allTopics",w);    
			//list for student
             		context.put("allTopics1",v);    
			/**  get Instructor or Student */
			u=null;
			w=null;v=null;
			if(Role.equals("instructor")) {
				
				ParameterParser pp=data.getParameters();
	                	String topicList=pp.getString("topicList","");
				if(!topicList.equals("")) {
					context.put("answerfilecheck","notchecked");	
        	        		context.put("topicList ",topicList);
					try{
					crit=new Criteria();
	                                crit.add(AssignmentPeer.GROUP_NAME,(String)user.getTemp("course_id"));
        	                        crit.add(AssignmentPeer.TOPIC_NAME,topicList);
                	                List u1=AssignmentPeer.doSelect(crit);
					String str2="";
					for(int i=0;i<u1.size();i++) {
						Assignment element=(Assignment)(u1.get(i));
						str2=(element.getAssignId());	
					}
					u1=null;
					String path=TurbineServlet.getRealPath("/Courses"+"/"+(String)user.getTemp("course_id")+"/Assignment"+"/"+str2);
					File f1=new File(path+"/Answerfile.txt");
					if(f1.exists()) {
						context.put("answerfile1","yes");			
						ErrorDumpUtil.ErrorLog("arvind yes "+path);
					}
					else {
						ErrorDumpUtil.ErrorLog("arvind no "+path);
						context.put("answerfile1","no");			
					}
					} catch(Exception e){ ErrorDumpUtil.ErrorLog(" "+e);  }
					
				} else
					context.put("answerfilecheck","checked");
			} 
			
//			String cdate = Integer.toString(Integer.parseInt(ExpiryUtil.getCurrentDate("")));
  //                      String date3=cdate.substring(0,4);
//			date3=date3+"-"+cdate.substring(4,6)+"-"+cdate.substring(6,8);
 //			context.put("date",date3);
 			context.put("date",curdate);
 			
						
                       
     		
	 	}//try
                catch(Exception e){  }
        }
}

