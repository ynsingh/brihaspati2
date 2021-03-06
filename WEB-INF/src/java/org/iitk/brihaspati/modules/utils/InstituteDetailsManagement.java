package org.iitk.brihaspati.modules.utils;

/*
 *  @(#) InstituteDetailsManagement.java
 *  Copyright (c) 2010,2012 ETRG,IIT Kanpur 
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

import java.util.List;
import java.util.Date;
import java.util.Vector;
import org.apache.torque.util.Criteria;
import org.iitk.brihaspati.om.Courses;
import org.iitk.brihaspati.om.CoursesPeer;
import org.apache.turbine.services.security.torque.om.TurbineUser;
import org.apache.turbine.services.security.torque.om.TurbineUserPeer;
import org.apache.turbine.services.security.torque.om.TurbineUserGroupRole;
import org.apache.turbine.services.security.torque.om.TurbineUserGroupRolePeer;
import org.iitk.brihaspati.om.InstituteAdminUser;
import org.iitk.brihaspati.om.InstituteAdminUserPeer;
import org.iitk.brihaspati.om.InstituteAdminRegistrationPeer;
import org.iitk.brihaspati.om.InstituteAdminRegistration;
/**
 * @author <a href="mailto:nksnghiitk@gmail.com">Nagendra Kumar Singh</a>
 * @author <a href="mailto:singh_jaivir@rediffmail.com">Jaivir Singh</a>01Nov2012
 * @author <a href="mailto:sharad23nov@yahoo.com">Sharad Singh</a>
 * @author <a href="mailto:palseema@rediffmail.com">Manorama Pal</a>
 */

public class InstituteDetailsManagement 
{
	public static Vector getInstUserDeatil(String instituteId)
	{
		Vector vct=new Vector();
		Vector uidvct=new Vector();
		List details=null;
		try{
			Criteria crit=new Criteria();
			List userList=UserManagement.getUserDetail1("All",instituteId);
			for(int i=0;i<userList.size();i++)
			{
				TurbineUser element=(TurbineUser)(userList.get(i));
				int userid=element.getUserId();
				vct.add(userid);
			}
			for(int j=0;j<vct.size();j++){
				String str=(vct.get(j)).toString();
                        	crit=new Criteria();
                                crit.add(TurbineUserGroupRolePeer.USER_ID,str);
                                details=TurbineUserGroupRolePeer.doSelect(crit);
				for(int r=0;r<details.size();r++){
					TurbineUserGroupRole tugr=(TurbineUserGroupRole) (details.get(r));
					int roleid=tugr.getRoleId();
					String roleId=Integer.toString(roleid);
					if(roleId.equals("2"))
					{
						int userid = tugr.getUserId();
						uidvct.add(userid);			
						
					}
                                }

			}
		}
		catch(Exception ex){}
		return uidvct;	
	}

/** 
 * This method is gives the institute id  from course id
 */
	public static String getInsId(String cName){
		String insId="";
		try{
			insId=org.apache.commons.lang.StringUtils.substringAfterLast(cName, "_");
		}
		catch(Exception e){
                ErrorDumpUtil.ErrorLog("The error in getInsId() - InstituteDetailsManagement class !!"+e);
        	}
        return insId;
        }//end of Method
/**
 * This method return all uid of an institute.
 */ 
	public static Vector getAllUid(String instituteId)
        {
                Vector vct=new Vector();
                Vector uidvct=new Vector();
                List details=null;
                try{
                        Criteria crit=new Criteria();
                        List userList=UserManagement.getUserDetail1("All",instituteId);
                        for(int i=0;i<userList.size();i++)
                        {
                                TurbineUser element=(TurbineUser)(userList.get(i));
                                int userid=element.getUserId();
                                vct.add(userid);
                        }
                        for(int j=0;j<vct.size();j++){
                                String str=(vct.get(j)).toString();
                                crit=new Criteria();
                                crit.add(TurbineUserGroupRolePeer.USER_ID,str);
                                details=TurbineUserGroupRolePeer.doSelect(crit);
                                for(int r=0;r<details.size();r++){
                                        TurbineUserGroupRole tugr=(TurbineUserGroupRole) (details.get(r));
                                        int userid=tugr.getUserId();
                                        uidvct.add(userid);
			//		ErrorDumpUtil.ErrorLog("all uid from tugr===="+uidvct);
                                }

                        }
                }
                catch(Exception ex){}
                return uidvct;
        }

/**
 * This method return the Course Details like Course name,group name, group alias,instructor name.  
 */ 
	public static Vector getInstituteCourseDetails(String instituteId)
	{
		Vector Cdetails=new Vector();
                try
                {
			/**
 			*Select all details from 'Courses' table 
			*/
			Criteria crit=new Criteria();
                        crit.addGroupByColumn(CoursesPeer.GROUP_NAME);
			List v=CoursesPeer.doSelect(crit);
ErrorDumpUtil.ErrorLog("The value in vector are (Institute Detail management )"+v.size());
			/**
 			*Get GroupName,CourseName,GroupAlias,etc 
 			*/ 
                        for(int i=0;i<v.size();i++)
                        {
				
                                String GName=((Courses)v.get(i)).getGroupName();
				/**
 				*Check for particular Institute. 	
 				*/
				if(GName.endsWith(instituteId)){
                                	String courseName=((Courses)v.get(i)).getCname();
                                	String dept=((Courses)v.get(i)).getDept();
                                	String gAlias=((Courses)v.get(i)).getGroupAlias();
                                	String description=((Courses)v.get(i)).getDescription();
                                	byte active=((Courses)v.get(i)).getActive();
                                	String act=Byte.toString(active);
                                	Date CDate=((Courses)v.get(i)).getCreationdate();
                                	String CrDate=CDate.toString();
                                	CourseUserDetail cuDetail=new CourseUserDetail();
					String insId=org.apache.commons.lang.StringUtils.substringAfterLast(GName, "_");
					String loginName=org.apache.commons.lang.StringUtils.substringBetween(GName, gAlias,"_"+insId);
                                	int UId=UserUtil.getUID(loginName);
					String uID=Integer.toString(UId);
//ErrorDumpUtil.ErrorLog("The  loop running times (Institute Detail management two)"+i +" login name is "+loginName +" The UID is "+uID);
                                	List userDetails=UserManagement.getUserDetail(uID);
					if(userDetails.size() > 0){
                                	TurbineUser element=(TurbineUser)userDetails.get(0);
                                	String firstName=element.getFirstName().toString();
                                	String lastName=element.getLastName().toString();
                                	String email=element.getEmail().toString();
                                	String userName=firstName+lastName;
                                	if(org.apache.commons.lang.StringUtils.isBlank(userName)){
                                		userName=loginName;
                                	}
					/**
 					*Set the value in CourseUserDetail for using in templates.		
 					*/ 
                                	cuDetail.setLoginName(loginName);
                                	cuDetail.setUserName(userName);
                                	cuDetail.setEmail(email);
					}
					cuDetail.setGroupName(GName);
                                	cuDetail.setCourseName(courseName);
                                	cuDetail.setCAlias(gAlias);
                                	cuDetail.setDept(dept);
                                	cuDetail.setActive(act);
                                	cuDetail.setDescription(description);
                                	cuDetail.setCreateDate(CrDate);
                                	Cdetails.add(cuDetail);
				}
			}
		}
                catch(Exception e)
                {
                        ErrorDumpUtil.ErrorLog("The error in getInstituteCourseDetails()"+e);
                }
                return(Cdetails);
	}

public static Vector getInstAdminUid(String instId)
        {
                Vector uid=new Vector();
                Vector evect=new Vector();
                try
                {
                        List lst=null;
                        Criteria crit=new Criteria();
                        crit.add(InstituteAdminUserPeer.INSTITUTE_ID,instId);
                        List v=InstituteAdminUserPeer.doSelect(crit);
                        for(int i=0;i<v.size();i++)
                        {
                                InstituteAdminUser iauser=(InstituteAdminUser)v.get(i);
                                String email=iauser.getAdminUname();
                                evect.add(email);
                        }
                        for(int n=0;n<evect.size();n++)
                        {
                                String mail=evect.get(n).toString();
                                crit=new Criteria();
                                crit.add(TurbineUserPeer.EMAIL,mail);
                                lst=TurbineUserPeer.doSelect(crit);
                        //}
                        for(int j=0;j<lst.size();j++){
                                TurbineUser tuser=(TurbineUser)lst.get(j);
                                int userid=tuser.getUserId();
                                uid.add(userid);
                        }
                        }
                }
                catch(Exception e)
                {
                        ErrorDumpUtil.ErrorLog("The error in getInstituteAdminUtil()"+e);
                }
                return(uid);
}

	/**
         * This method return the Course Details like Course name,group name, group alias,instructor name for any group name.  
         */
  	public static Vector getGroupCourseDetails(Vector GroupName)
        {
                Vector Cdetails=new Vector();
                try
                {
			for(int j=0;j<GroupName.size();j++)
			{
				String gname = (String)GroupName.elementAt(j);
				Criteria crit=new Criteria();
        	                crit.add(CoursesPeer.GROUP_NAME,gname);
                	        List v=CoursesPeer.doSelect(crit);
				for(int i=0;i<v.size();i++)
                        	{
					String courseName=((Courses)v.get(i)).getCname();
	                                String dept=((Courses)v.get(i)).getDept();
	                                String gAlias=((Courses)v.get(i)).getGroupAlias();
	                                String GName=((Courses)v.get(i)).getGroupName();
	                                String description=((Courses)v.get(i)).getDescription();
	                                byte active=((Courses)v.get(i)).getActive();
	                                String act=Byte.toString(active);
	                                Date CDate=((Courses)v.get(i)).getCreationdate();
	                                String CrDate=CDate.toString();
	                                CourseUserDetail cuDetail=new CourseUserDetail();
	                                String insId=org.apache.commons.lang.StringUtils.substringAfterLast(GName, "_");
	                                String loginName=org.apache.commons.lang.StringUtils.substringBetween(GName, gAlias,"_"+insId);
	                                int UId=UserUtil.getUID(loginName);
	                                String uID=Integer.toString(UId);
	                                List userDetails=UserManagement.getUserDetail(uID);
	                                TurbineUser element=(TurbineUser)userDetails.get(0);
	                                String firstName=element.getFirstName().toString();
	                                String lastName=element.getLastName().toString();
	                                String email=element.getEmail().toString();
	                                String userName=firstName+lastName;
	                                if(org.apache.commons.lang.StringUtils.isBlank(userName)){
	                                	userName=loginName;
	                         	}
						cuDetail.setLoginName(loginName);
		                                cuDetail.setUserName(userName);
		                                cuDetail.setEmail(email);
		                                cuDetail.setGroupName(GName);
		                                cuDetail.setCourseName(courseName);
		                                cuDetail.setCAlias(gAlias);
		                                cuDetail.setDept(dept);
		                                cuDetail.setActive(act);
		                                cuDetail.setDescription(description);
		                                cuDetail.setCreateDate(CrDate);
		                                Cdetails.add(cuDetail);
        		       }
			}
                }
                catch(Exception e)
                {
                        ErrorDumpUtil.ErrorLog("The error in getGroupCourseDetails()"+e);
                }
                return(Cdetails);
        }
	/** This method return the courselist of an institute*/
	public static Vector InstwiseCourse(String Instname) throws Exception
        {
		Vector CourseList=new Vector();
		try
		{
                	Criteria crit=new Criteria();
                	crit.add(InstituteAdminRegistrationPeer.INSTITUTE_NAME,Instname);
                	List lst=InstituteAdminRegistrationPeer.doSelect(crit);
                	int instid=((InstituteAdminRegistration)lst.get(0)).getInstituteId();
                	CourseList=getInstituteCourseDetails(Integer.toString(instid));
		}
                catch(Exception e)
                {
                        ErrorDumpUtil.ErrorLog("The Error in InstituteDetailsManagementUtil Method(InstwiseCourse)"+e);
                }
		return CourseList;
	}//method


}	
