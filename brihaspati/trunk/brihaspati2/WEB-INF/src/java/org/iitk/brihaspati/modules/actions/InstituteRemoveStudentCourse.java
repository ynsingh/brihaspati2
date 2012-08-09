package org.iitk.brihaspati.modules.actions;

/*
 * @(#)InstituteRemoveStudentCourse.java	
 *
 *  Copyright (c) 2010 ETRG,IIT Kanpur. 
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
 */

/**
 * @author  <a href="awadhesh_trivedi@yahoo.co.in">Awadhesh Kumar Trivedi</a>
 * @author  <a href="satyapalsingh@gmail.com">Satyapal Singh</a>
 * @author  <a href="singh_jaivir@rediffmail.com">Jaivir Singh</a>
 * @author <a href="mailto:shaistashekh@hotmail.com">Shaista</a>
 * @author <a href="mailto:richa.tandon1@gmail.com">Richa Tandon</a>
 * @modified date: 08-07-2010
 * @modified date: 20-10-2010,23-12-2010,3-03-2011, 16-06-2011, 08-08-2012
 */
import java.util.Vector;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;
import org.apache.velocity.context.Context;
import org.apache.turbine.util.RunData;
import org.apache.turbine.om.security.User;
import org.apache.turbine.util.parser.ParameterParser;
import org.iitk.brihaspati.modules.utils.UserManagement;
import org.iitk.brihaspati.modules.utils.UserUtil;
import org.iitk.brihaspati.modules.utils.MultilingualUtil;
import org.iitk.brihaspati.modules.utils.InstituteIdUtil;
import org.iitk.brihaspati.modules.utils.MailNotification;
import org.iitk.brihaspati.modules.utils.MailNotificationThread;
import org.iitk.brihaspati.modules.utils.ErrorDumpUtil;
import org.iitk.brihaspati.om.InstituteAdminUser;
import org.iitk.brihaspati.om.InstituteAdminUserPeer;
import org.iitk.brihaspati.modules.utils.StringUtil;
import org.apache.turbine.services.servlet.TurbineServlet;
import org.apache.turbine.services.security.torque.om.TurbineUser;
import org.apache.torque.util.Criteria;
/** 
 * This class contains code for Student registered in new course and remove course
 */

public class InstituteRemoveStudentCourse extends SecureAction_Institute_Admin{
	/**
	 * Place all the data object in the context
	 * for use in the template.
	 * @param data Rundata
	 * @param context Context
         * @see UserManagement From Utils
	 * @exception genric
	 */

	private String LangFile=null;
	public void doRemove(RunData data, Context context)
	{	
		/**
                 * Getting the value of file from temporary variable
                 * According to selection of Language.    
		*/
		LangFile=(String)data.getUser().getTemp("LangFile");
		MultilingualUtil m_u=new MultilingualUtil();
		try{
		/**
		 * Here Courselist have student Name and GroupName from Selected
		 * CheckBoxes in String for Removing
		 */
		String CourseList=data.getParameters().getString("deleteFileNames","");
		if(!CourseList.equals("")){  
			User user= data.getUser();
			String server_name=TurbineServlet.getServerName();
                       	String srvrPort=TurbineServlet.getServerPort();
			String luserName = user.getName();
	                //int luserId=UserUtil.getUID(luserName);
        	        //ErrorDumpUtil.ErrorLog("\n\n\n\n login User id in Institute Remove Student Course"+ luserId);
                	String instId= user.getTemp("Institute_id").toString();
			boolean flag = org.apache.commons.lang.StringUtils.isEmpty(instId);
			String instName = "";
			String instFirstLastName =""; 
			if(!flag){
		                instName = InstituteIdUtil.getIstName(Integer.parseInt(instId));
				try{
                	                Criteria crit=new Criteria();
                        	        crit.add(InstituteAdminUserPeer.INSTITUTE_ID,Integer.parseInt(instId));
                                	crit.add(InstituteAdminUserPeer.ADMIN_UNAME,luserName);
	                                List inm=InstituteAdminUserPeer.doSelect(crit);
        	                        InstituteAdminUser element=(InstituteAdminUser)inm.get(0);
					int Auid=UserUtil.getUID(element.getAdminUname());
                        	        instFirstLastName=UserUtil.getFullName(Auid);
	                        }
        	                catch(Exception e){ErrorDumpUtil.ErrorLog("Error in InstituteRemoveStudentCourse class in acion at line 107");}
			}
			String info_new = "", subject = "" , info_Opt="", msgRegard="", msgInstAdmin="";
			if(srvrPort.equals("8080")){
                		info_new = "deleteUser";
				info_Opt = "newUser";
			}
               		else{
       	        		info_new = "deleteUserhttps";	
       	        		info_Opt = "newUserhttps";	
			}
			String fileName=TurbineServlet.getRealPath("/WEB-INF/conf/brihaspati.properties");
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
			Properties pr =MailNotification.uploadingPropertiesFile(fileName);
			subject = MailNotification.subjectFormate(info_new, "", pr );
			msgRegard=pr.getProperty("brihaspati.Mailnotification."+info_Opt+".msgRegard");
                        msgRegard = MailNotification.replaceServerPort(msgRegard, server_name, srvrPort);
                        msgInstAdmin=pr.getProperty("brihaspati.Mailnotification."+info_Opt+".msgInstAdmin");
			msgInstAdmin = MailNotification.getMessage_new(msgInstAdmin, "", "", instFirstLastName, "");
			StringTokenizer st=new StringTokenizer(CourseList,"^");
			for(int i=0;st.hasMoreTokens();i++){ 
				String s=st.nextToken();
				/**
				 * preString have course id
				 * postString have user name
				 */
				int index=s.indexOf(":",0);
				String preString=s.substring(0,index);
				String postString=s.substring(index+1);
				/**
				 * Get the user id from the user name obtained 
				 * @see UserUtil in utils
				 */
				int userId=UserUtil.getUID(postString);
				UserManagement umt=new UserManagement();
				Vector Err_user=new Vector();
				Vector Err_type=new Vector();
				/**
				 * Remove the users selected for removal
				 * @see UserManagement in utils
				String server_name=TurbineServlet.getServerName();
                        	String srvrPort=TurbineServlet.getServerPort();
				String info_new = "", subject = "";
				if(srvrPort.equals("8080"))
	                		info_new = "deleteUser";
                		else
        	        		info_new = "deleteUserhttps";	
				 */
				int uId=UserUtil.getUID(postString);
		                String uid=Integer.toString(uId);
				TurbineUser element=(TurbineUser)UserManagement.getUserDetail(uid).get(0);
				String email=element.getEmail();
				String message = MailNotification.getMessage(info_new, preString, "", "", "", pr);
				message = message.replaceAll("institute_admin",instName);	
				//ErrorDumpUtil.ErrorLog("\n\n\n\n in RemoveStudentCourse message="+message+"      subject="+subject);
                                //String Mail_msg=MailNotification.sendMail(message, email, subject, "", LangFile);
				String Mail_msg=  MailNotificationThread.getController().set_Message(message, "", msgRegard, msgInstAdmin, email, subject, "", LangFile, instId);
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                data.setMessage(Mail_msg);
				String msg=umt.removeUserProfile(postString,preString,LangFile);
				if(umt.flag.booleanValue()==false){ 
					Err_user.addElement(postString);
					Err_type.addElement(msg);
				} 
				context.put("error_user",Err_user);
				context.put("error_type",Err_type);
		 	String varStudent=m_u.ConvertedString("varStudent",LangFile);
			String remStudent=m_u.ConvertedString("remStudent",LangFile);
			if(LangFile.endsWith("en.properties")){
				data.setMessage(varStudent+" "+"'" +postString+"'"+" "+remStudent);}
			else 
				data.setMessage(varStudent +" "+remStudent+"'" +postString+"'");
			}
		} 
		/**
		 * This 'else' is executed when course is not selected
		 */
		else{ 
			String courseMsg=m_u.ConvertedString("c_msg12",LangFile);
			data.setMessage(courseMsg);
		} 
		}
		catch(Exception e)
		{
			data.setMessage("The error in Course Removal"+e);
		}
	}
	/**
	 * Register in new course as a student
	 * @param data Rundata
	 * @param context Context
         * @see UserManagement From Utils
	 * @exception genric
	 */
	public void doRegister(RunData data, Context context){
		
		ParameterParser pp=data.getParameters();
		/**
                  * Getting the value of file from temporary variable
                  * According to selection of Language.
		  * Replacing the String value from property file.
                  * @see MultilingualUtil in utils      
                  */  
		String instName="", msg="";	
               	String instId= data.getUser().getTemp("Institute_id").toString();
		if(!instId.equals(""))
	                instName = InstituteIdUtil.getIstName(Integer.parseInt(instId));
		String mode=pp.getString("mode");	
		LangFile=(String)data.getUser().getTemp("LangFile");
                MultilingualUtil m_u=new MultilingualUtil(); 
		try{
			/**
			 *Retreiving details entered by the user
			 */
			String serverName=data.getServerName();
                        int srvrPort=data.getServerPort();
                        String serverPort=Integer.toString(srvrPort);
			String gName=pp.getString("group");
			String uname=pp.getString("username");
			String roleName=null;
			String program="";
			String rollno="";
			if(mode.equals("instlist")){
				roleName="instructor";
			}
			
			if(mode.equals("sclist")){
				rollno = pp.getString("rollno","").trim();
				/**
		                  * check if rollno have any special character then return message
		                  */
				if(StringUtil.checkString(rollno) != -1)
                        	{
                                	data.addMessage(MultilingualUtil.ConvertedString("c_msg3",LangFile));
	                               	return;
        	                }
				program = pp.getString("prg","");
				roleName="student";
			}
		if(!instId.equals(""))
			msg=UserManagement.CreateUserProfile(uname,"","","",instName,"",gName,roleName,serverName,serverPort,LangFile,rollno,program); // modified by Shikha
		else
			msg=UserManagement.CreateUserProfile(uname,"","","","","",gName,roleName,serverName,serverPort,LangFile,rollno,program); // modified by Shikha
				       data.setMessage(msg);
		}
		catch(Exception e)
		{
			 data.setMessage("The error in User Registration "+e); 
		}
	}
	/** This is default method 
	  * @param data RunData 
	  * @param context Context
          */
	public void doPerform(RunData data,Context context){
		/**
                  * Getting the value of file from temporary variable
                  * According to selection of Language.
                  * Replacing the String value from property file.
                  * @see MultilingualUtil in utils
                  */      
		LangFile=(String)data.getUser().getTemp("LangFile");
                MultilingualUtil m_u=new MultilingualUtil(); 
		String action=data.getParameters().getString("actionName","");
                //context.put("action",action);
                if(action.equals("eventSubmit_doRegister"))
                        doRegister(data,context);
		else{
			String usrMsg=m_u.ConvertedString("usr_prof2",LangFile);
			data.setMessage(usrMsg);
		}
	}
}

