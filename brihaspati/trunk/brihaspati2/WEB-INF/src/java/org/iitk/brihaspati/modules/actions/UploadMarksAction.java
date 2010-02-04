package org.iitk.brihaspati.modules.actions;

/*
 * @(#)UploadMarksAction.java	
 *
 *  Copyright (c) 2005, 2009 ETRG,IIT Kanpur. 
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
 */

import java.io.File;
import java.util.List;
import java.util.Vector;

import org.apache.velocity.context.Context;
import org.apache.commons.fileupload.FileItem;

import org.apache.torque.util.Criteria;

import org.apache.turbine.util.RunData;
import org.apache.turbine.util.parser.ParameterParser;
import org.apache.turbine.om.security.User;
import org.apache.turbine.services.servlet.TurbineServlet;
import org.apache.turbine.services.security.torque.om.TurbineUser;
import org.apache.turbine.services.security.torque.om.TurbineUserPeer;

import org.iitk.brihaspati.modules.utils.GroupUtil;
import org.iitk.brihaspati.modules.utils.StringUtil;
import org.iitk.brihaspati.modules.utils.MultilingualUtil;
import org.iitk.brihaspati.modules.utils.MailNotification;
import org.iitk.brihaspati.modules.utils.CourseUserDetail;
import org.iitk.brihaspati.modules.utils.UserGroupRoleUtil;

/**
 * In this class, We upload marks in txt format file
 * @author <a href="mailto:nksinghiitk@yahoo.co.in">Nagendra Kumar Singh</a>
 * @author <a href="mailto:ammu_india@yahoo.com">Amit Joshi</a>
 * @author <a href="mailto:awadhesh_trivedi@yahoo.co.in">Awadhesh Kumar Trivedi</a>
 * @author <a href="mailto:shaistashekh@hotmail.com">Shaista</a>
 * @author <a href="mailto:sunil.singh6094@gmail.com">Sunil Kumar Pal</a>
 * @modified date 29-12-2009
 */

public class UploadMarksAction extends SecureAction_Instructor
{
    public void doUpload(RunData data, Context context)
    {
	try{
		String LangFile=data.getUser().getTemp("LangFile").toString();
		User user=data.getUser();
		ParameterParser pp=data.getParameters();
                String serverName= TurbineServlet.getServerName();
                String serverPort= TurbineServlet.getServerPort();
		/**
	 	* Get the course id from user's temporary variable
	 	*/

		String courseHome=(String)user.getTemp("course_id","");
		/**
	 	* Get the file for uploading and check if the file is "null"
	 	*/
		FileItem fileItem=pp.getFileItem("file");
		if((fileItem!=null) && (fileItem.getSize()!=0))
		{
			/**
	 		* Get the name of the file for uploading and check if the
	 		* extension is ".txt"
	 		*/
			String fileName=fileItem.getName();
			String mailMsg ="";
			if(!fileName.endsWith(".txt") && !fileName.endsWith(".csv"))
			{
 				data.setMessage(MultilingualUtil.ConvertedString("Marks_msg1",LangFile));
			}
			else
			{
				/**
		 		* Get the real path of the marks directory
		 		*/

				String coursesRealPath=TurbineServlet.getRealPath("/Courses");
				String destDir=coursesRealPath+"/"+courseHome+"/Marks/";

				File marksDir=new File(destDir);
				File marksFile=new File(marksDir,"MARK.txt");
				boolean marksExist=false;

				/**
		 		* Check if the marks file exists
		 		*/

				if(marksFile.exists())
				{
					marksExist=true;
				}
				marksDir.mkdirs();
				String userName=user.getName();
				File tempFile=new File(TurbineServlet.getRealPath("/tmp")+"/"+userName+".txt");
				fileItem.write(tempFile);
				StringUtil.insertCharacter(tempFile.getAbsolutePath(),marksFile.getAbsolutePath(),',','-');
				tempFile.delete();
				String mailId="";
				String marksUploadStr="Marks are uploaded in"+courseHome;
				Criteria crit=new Criteria();
		                crit.add(TurbineUserPeer.LOGIN_NAME,userName);
				//mail for Instructor
                		try
                		{
		                        List v=TurbineUserPeer.doSelect(crit);
                		        TurbineUser element=(TurbineUser)v.get(0);
		                        mailId=element.getEmail();
					if(mailId != null && mailId != "")
						mailMsg=MailNotification.sendMail(marksUploadStr, mailId, "", "Updation Mail", "", "", "", serverName, serverPort, (String)user.getTemp("LangFile"));
                		}
		                catch(Exception e)
                			{data.setMessage("The error in sending mail to instructor for update/Upload marks !!"+e);}

				//mail for student
                                try{
                                        int grid=GroupUtil.getGID(courseHome);
                                        Vector usDetail=UserGroupRoleUtil.getUDetail(grid, 3);
                                        for(int i=0; i<usDetail.size(); i++){
                                                mailId=((CourseUserDetail) usDetail.elementAt(i)).getEmail().trim();
                                                if(mailId != null && mailId != ""){
                                                mailMsg=MailNotification.sendMail(marksUploadStr, mailId, "", "Updation Mail", "", "", "", serverName, serverPort, (String)user.getTemp("LangFile"));
                                                }
                                                usDetail=null;
                                        }
                                }
                                catch(Exception ex)
                                        { data.setMessage("The error in sending mail to student for update/Upload marks" + ex); }


				String onUploadMessage=MultilingualUtil.ConvertedString("Marks_msg2",LangFile);
				if(marksExist)
				{
					onUploadMessage=MultilingualUtil.ConvertedString("Marks_msg3",LangFile);
				}
				data.setMessage(onUploadMessage);
				if(mailId != null && mailId != "")
					data.addMessage(mailMsg);
				else
					data.addMessage( MultilingualUtil.ConvertedString("mailNotification_msg2",(String)user.getTemp("LangFile")));
			}
		}
		else
		{
			data.setMessage(MultilingualUtil.ConvertedString("Marks_msg4",LangFile));
			
		}
	}
	catch(Exception ex)
	{
		data.setMessage("The error in Uplodaing in Marks !!"+ex);
	}
    }

    /**
     * Default action to perform if the specified action
     * cannot be executed.
     */
    public void doPerform( RunData data,Context context )
        throws Exception
    {
	String action=data.getParameters().getString("actionName","");
	if(action.equals("eventSubmit_doUpload"))
		doUpload(data,context);
	else
        	data.setMessage("Can't find the requested action! ");
    }
}
