package org.iitk.brihaspati.modules.actions;

/*
 * @(#) Upload Ans Action.java	
 *
 *  Copyright (c) 2005-2006, 2008-2010, 2010-2011,2012-2013, 2020 ETRG,IIT Kanpur. 
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
 */
//JDK
import java.io.File;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.BufferedOutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
//apache
import org.apache.torque.util.Criteria;
import org.apache.velocity.context.Context;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.fileupload.FileItem;
import org.apache.turbine.util.RunData;
import org.apache.turbine.om.security.User;
import org.apache.turbine.util.parser.ParameterParser;
import org.apache.turbine.services.servlet.TurbineServlet;
import org.apache.turbine.services.security.torque.om.TurbineUser;
import org.apache.turbine.services.security.torque.om.TurbineUserPeer;
import org.apache.turbine.services.security.torque.om.TurbineUserGroupRole;
import org.apache.turbine.services.security.torque.om.TurbineUserGroupRolePeer;
//brihaspati
import org.iitk.brihaspati.om.Liveclass;
import org.iitk.brihaspati.om.LiveclassPeer;
import org.iitk.brihaspati.modules.utils.UserUtil;
import org.iitk.brihaspati.modules.utils.GroupUtil;
import org.iitk.brihaspati.modules.utils.CourseUtil;
import org.iitk.brihaspati.modules.utils.ErrorDumpUtil;
import org.iitk.brihaspati.modules.utils.MultilingualUtil;
import org.iitk.brihaspati.modules.utils.SystemIndependentUtil;
import org.iitk.brihaspati.modules.utils.MailNotificationThread;

/**
 * @author <a href="mailto:nksinghiitk@yahoo.co.in">Nagendra Kumar Singh</a> 
 */
public class UploadAnsAction extends SecureAction_Instructor
{
	private Log log = LogFactory.getLog(this.getClass());
	private String LangFile=null;
	private String msg1=null;
	private String[] msg;
	/**
     	* Size of the buffer to read/write data
     	*/
	private static final int BUFFER_SIZE = 4096;
	
	/**
	 * This method performs the action to announce the live class for course
	 * @param data RunData
	 * @param context Context
	 * @return nothing
	 * @see UserManagement from Utils
	 */

        public void doAnnounceLiveClass(RunData data, Context context)
        {
		User user=data.getUser();
                LangFile=(String)user.getTemp("LangFile");
		String dir=(String)user.getTemp("course_id");
		Criteria crit=new Criteria();
		
		String uName=user.getName();
   		int uid=UserUtil.getUID(uName);
   		String fullName = UserUtil.getFullName(uid);
		String courseName = CourseUtil.getCourseName(dir);
            	String Mail_msg = "";
                
		ParameterParser pp=data.getParameters();
		String liveclsname= pp.getString("lcname");
 		String startYear = pp.getString("Start_year","");
                String startMonth = pp.getString("Start_mon","");
                String startDay = pp.getString("Start_day","");
                String startHour = pp.getString("Start_hr","");
                String startMinute = pp.getString("Start_min","");

                String startDate = startYear+"-"+startMonth+"-"+startDay;
                String startTime = startHour+":"+startMinute;
		String duration= pp.getString("duration","");
		String passcode= pp.getString("passcode","");
		try{
		crit=new Criteria();
		crit.add(LiveclassPeer.COURSE_ID,dir);
		List ls1=LiveclassPeer.doSelect(crit);
		if(ls1.size() == 0){
			crit=new Criteria();
			crit.add(LiveclassPeer.COURSE_ID,dir);
			crit.add(LiveclassPeer.CLASS_NAME,liveclsname);
			crit.add(LiveclassPeer.CLASS_DATE,startDate);
			crit.add(LiveclassPeer.CLASS_TIME,startTime);
			crit.add(LiveclassPeer.CLASS_DURATION,duration);
			crit.add(LiveclassPeer.PASSKEY,passcode);
			LiveclassPeer.doInsert(crit);
		}
		else{
			Liveclass ele=(Liveclass)ls1.get(0);
			int id=ele.getId();
			crit=new Criteria();
			crit.add(LiveclassPeer.ID,id);
			crit.add(LiveclassPeer.COURSE_ID,dir);
			crit.add(LiveclassPeer.CLASS_NAME,liveclsname);
			crit.add(LiveclassPeer.CLASS_DATE,startDate);
			crit.add(LiveclassPeer.CLASS_TIME,startTime);
			crit.add(LiveclassPeer.CLASS_DURATION,duration);
			crit.add(LiveclassPeer.PASSKEY,passcode);
			LiveclassPeer.doUpdate(crit);
		}
		String Add = MultilingualUtil.ConvertedString("brih_announce",LangFile);
                String live = MultilingualUtil.ConvertedString("brih_live",LangFile);
                String lclass = MultilingualUtil.ConvertedString("brih_class",LangFile);
                String success = MultilingualUtil.ConvertedString("brih_successfully",LangFile);
		data.setMessage(live +" "+lclass+" "+Add+" " +success);
		log.info("Live class added successfully with name "+liveclsname+" By "+data.getUser().getName()+" | IP Address : "+data.getRemoteAddr());
		}
		catch(Exception e){
			ErrorDumpUtil.ErrorLog("Exception in Live Class Announced function in answer book action under db insert "+e.getMessage());

		}
		//send mail to all student
                try{
                        String newText="<br>Date :"+startDate+"<br> Time :"+startTime+"<br> URL : https://meet.jit.si/"+liveclsname+"<br>You can reuse the same classsession multiple times.<br>";
			int gid = GroupUtil.getGID(dir);
                        int roleId[]={2,3};
                        int userId[]={uid,0};
                        crit = new Criteria();
                        crit.add(TurbineUserGroupRolePeer.GROUP_ID,gid);
                        crit.addIn(TurbineUserGroupRolePeer.ROLE_ID,roleId);
                        crit.andNotIn(TurbineUserGroupRolePeer.USER_ID,userId);
                        List v1=TurbineUserGroupRolePeer.doSelect(crit);
                        if(v1.size() >0){

                           for(int i=0; i < v1.size(); i ++) {
                              int usrId =((TurbineUserGroupRole) v1.get(i)).getUserId();
                              crit = new Criteria();
                              crit.add(TurbineUserPeer.USER_ID, usrId);
                              List usrList = TurbineUserPeer.doSelect(crit);
                              String userEmail = ((TurbineUser) usrList.get(0)).getEmail();
                              Mail_msg=  MailNotificationThread.getController().set_Message("\n\nLive Class is announced in "+courseName+" taught by "+fullName+" ." +"the Information regarding the live class as a link is as given below ."+"<br>"+newText +"."+"<br>"+fullName, "", "", "", userEmail, "Live Class Announced", "", LangFile);
                           }
                           if(Mail_msg.equals("Success")) {
                              crit = new Criteria();
                              crit.add(TurbineUserPeer.USER_ID, uid);
                              List usrList = TurbineUserPeer.doSelect(crit);
                              String senderEmail = ((TurbineUser) usrList.get(0)).getEmail();
                              Mail_msg=  MailNotificationThread.getController().set_Message("\n\nLive class is announced in "+courseName+" taught by "+fullName+ " . "+"the Information regarding the live class as given below"+"<br>"+newText+"."+"<br>"+ fullName, "", "", "", senderEmail, "Live class Announced", "", LangFile);
                              Mail_msg=MultilingualUtil.ConvertedString("mail_msg",LangFile);
                              data.addMessage(Mail_msg);
                           }
                        }
                }
 		catch(Exception e) { 
			ErrorDumpUtil.ErrorLog("Exception in Live Class Announced function in answer book action "+e.getMessage());
		}
                

                setTemplate(data,"call,Answerbook,AnnounceLiveClass.vm");

	}

	/**
        * Method for deleting the file/folder from copyans
        * @param data RunData
        * @param context Context
        */
	public void doDelete(RunData data,Context context)
        {
                String langFile=data.getUser().getTemp("LangFile").toString();
                try
                {
                        User user=data.getUser();
			String dir=(String)user.getTemp("course_id");

                        /**
                        *Retreive the Parameters by help of
                        *Parameter Parser and put in the context
                        *for using in templates
                        */
                        ParameterParser pp=data.getParameters();
                        String Filename=pp.getString("filename","");
                        String mode=pp.getString("mode","");
                        String Content=pp.getString("topic","");
			context.put("topic",Content);
                        /**
                        *Get the RealPath and Path of
                        *Private Area in which the topic exist
                        */
			String fileRealPath=TurbineServlet.getRealPath("/Courses/"+dir);
                        if(mode.equals("DirRemoval"))
                        {
                                /**
                                *Delete the topic name exist
                                *@see SystemIndependentUtil in utils
                                */
				File filePath=new File(fileRealPath+"/AnsCopy/"+Content);
                                SystemIndependentUtil.deleteFile(filePath);
                                String del1=MultilingualUtil.ConvertedString("personal_del1",langFile);
                                if(langFile.equals("english"))
                                        data.setMessage(Content+" "+del1);
                                else
                                        data.setMessage(del1+" "+Content+" ");
                        }
                        else
                        {
                               /**
                                *Get the Path where the file exist in
                                *the particular topic in the 
                                */
                                File fileway=new File(fileRealPath+"/AnsCopy/"+Content+"/"+Filename);
                                SystemIndependentUtil.deleteFile(fileway);
                                String del2=MultilingualUtil.ConvertedString("personal_del2",langFile);
                                        if(langFile.equals("english"))
                                                data.setMessage(Filename+" "+del2);
                                        else
                                                data.setMessage(del2+" "+Filename+" ");
                        }
                }
                catch(Exception e){
			ErrorDumpUtil.ErrorLog("Exception in Delete function in answer book action "+e.getMessage());
		}
       }



	/**
	 * This method performs the action for upload student answer book in the course
	 * @param data RunData
	 * @param context Context
	 * @return nothing
	 */

        public void doUploadAnsCopy(RunData data, Context context)
        {
		User user=data.getUser();
                LangFile=(String)user.getTemp("LangFile");
		String dir=(String)user.getTemp("course_id");
                ParameterParser pp=data.getParameters();
		String examName=pp.getString("ENAME");
		FileItem file = pp.getFileItem("file1");
		ErrorDumpUtil.ErrorLog(" The error is "+file.toString());
                String fileName=file.getName();
		String uploadRealPath=TurbineServlet.getRealPath("/Courses/"+dir);
		if(fileName.endsWith("zip"))
                {
			try{
				File filePath=new File(uploadRealPath+"/AnsCopy/"+examName);
				filePath.mkdirs();
				File filePath1=new File(filePath+"/"+fileName);
        	                file.write(filePath1);
				unzip(filePath1.toString(),filePath.toString());

	                        msg1=MultilingualUtil.ConvertedString("c_msg5",LangFile);
                                data.setMessage(msg1);
//				File zofile = new File(filePath.toString()+fileName);
				filePath1.delete();
			}
			catch(Exception ex){data.setMessage("The Error in Uploading!! "+ex);}
		} 
                else
                {
                	msg1=MultilingualUtil.ConvertedString("upload_msg3",LangFile);
                        data.setMessage(msg1);
                        setTemplate(data,"call,Answerbook,UploadAnsBook.vm");
		}


	}

	/**
     * Extracts a zip file specified by the zipFilePath to a directory specified by
     * destDirectory (will be created if does not exists)
     * @param zipFilePath
     * @param destDirectory
     * @throws IOException
     */
    public void unzip(String zipFilePath, String destDirectory) throws IOException {
        File destDir = new File(destDirectory);
        if (!destDir.exists()) {
            destDir.mkdir();
        }
        ZipInputStream zipIn = new ZipInputStream(new FileInputStream(zipFilePath));
        ZipEntry entry = zipIn.getNextEntry();
        // iterates over entries in the zip file
        while (entry != null) {
            String filePath = destDirectory + File.separator + entry.getName();
            if (!entry.isDirectory()) {
                // if the entry is a file, extracts it
                extractFile(zipIn, filePath);
            } else {
                // if the entry is a directory, make the directory
                File dir = new File(filePath);
                dir.mkdir();
            }
            zipIn.closeEntry();
            entry = zipIn.getNextEntry();
        }
        zipIn.close();
    }
    /**
     * Extracts a zip entry (file entry)
     * @param zipIn
     * @param filePath
     * @throws IOException
     */
    private void extractFile(ZipInputStream zipIn, String filePath) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(filePath));
        byte[] bytesIn = new byte[BUFFER_SIZE];
        int read = 0;
        while ((read = zipIn.read(bytesIn)) != -1) {
            bos.write(bytesIn, 0, read);
        }
        bos.close();
    }

    public void doPerform(RunData data,Context context) throws Exception{
		String action=data.getParameters().getString("actionName","");
		context.put("action",action);
		User user=data.getUser();
                LangFile=(String)user.getTemp("LangFile");  
                if(action.equals("eventSubmit_doUpload"))
                        doUploadAnsCopy(data,context);
		else if(action.equals("eventSubmit_doAnnouncment"))
			doAnnounceLiveClass(data,context);
		else if(action.equals("eventSubmit_doDelete"))
			doDelete(data,context);

		else
			data.setMessage(MultilingualUtil.ConvertedString("usr_prof2",LangFile));
	}
}

