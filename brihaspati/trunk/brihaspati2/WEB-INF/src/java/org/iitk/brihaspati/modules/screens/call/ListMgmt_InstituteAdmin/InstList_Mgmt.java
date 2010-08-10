package org.iitk.brihaspati.modules.screens.call.ListMgmt_InstituteAdmin;

/*
 * @(#)InstList_Mgmt.java	
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
 * 
 *  
 *  Contributors: Members of ETRG, I.I.T. Kanpur 
 * 
 */

/**
 * @author <a href="mailto:singh_jaivir@rediffmail.com">Jaivir Singh -20100810</a>
 * @author <a href="mailto:sharad23nov@yahoo.com">Sharad Singh - 20100810</a>
 */

import java.util.Vector;
import org.apache.turbine.util.RunData;
import org.apache.velocity.context.Context;
import org.apache.turbine.util.parser.ParameterParser;
import org.iitk.brihaspati.modules.utils.CourseManagement;
import org.iitk.brihaspati.modules.utils.ListManagement;
import org.iitk.brihaspati.modules.utils.AdminProperties;
import org.iitk.brihaspati.modules.utils.MultilingualUtil;
import org.iitk.brihaspati.modules.utils.StringUtil;
import org.iitk.brihaspati.modules.utils.ErrorDumpUtil;
import org.iitk.brihaspati.modules.screens.call.SecureScreen_Institute_Admin;

/*
 * This class called when institute admin view the list of all courses and all users.
 */

public class InstList_Mgmt extends SecureScreen_Institute_Admin
{
    private String file=null;
    public void doBuildTemplate( RunData data, Context context )
    {
    try {
	String lcourse=data.getParameters().getString("listcourse","");
	context.put("listcourse",lcourse);
	String counter=data.getParameters().getString("count","");
	context.put("tdcolor",counter);
	String instituteId=(data.getUser().getTemp("Institute_id")).toString();
                 	/**
                  	* Getting Language value from temporary variable
                  	* Getting file value from temporary variable according to selection
                  	* Replacing the value from Property file
                 	**/
			if(lcourse.equals("listcourse")){
                	file=(String)data.getUser().getTemp("LangFile");
                	MultilingualUtil m_u=new MultilingualUtil();
                        Vector courseList=new Vector();
                        String stat=data.getParameters().getString("status","");
                        context.put("stat",stat);
                        String query="";
                        String valueString="";
                        String Mode=data.getParameters().getString("mode","");
			/**
			*get all courselist using CourseManagement util
			*@see CourseManagement util in utils 
			*if by search string the else part executed. 
			*/
                        if(Mode.equals("All"))
                        {
                                courseList=CourseManagement.getInstituteCourseNUserDetails("All",instituteId);
                                context.put("mode","All");
                        }
                        else
                        {
                        /**
                         * Get the search criteria and the search string
                         * from the screen
                         */
                                query=data.getParameters().getString("queryList");
                                if(query.equals(""))
                                        query=data.getParameters().getString("query");
                        /**
                          * Check for special characters
                          */
                           valueString =StringUtil.replaceXmlSpecialCharacters(data.getParameters().getString("valueString"));
			
                                context.put("query",query);
                                context.put("valueString",valueString);
                                courseList=ListManagement.getInstituteListBySearchString("CourseWise",query,valueString,instituteId);
                                context.put("mode","Search");
                        }
			/**
			*read the Admin.properties for pagination
			*@see AdminProperties util in utils
			*@see ListManagement util in utils for link visible and
			*list divide.	
			*/
                        String path=data.getServletContext().getRealPath("/WEB-INF")+"/conf"+"/"+"Admin.properties";
                        int AdminConf = Integer.valueOf(AdminProperties.getValue(path,"brihaspati.admin.listconfiguration.value"));

//                      int AdminConf = AdminProperties.getValue(path);
                        context.put("AdminConf",new Integer(AdminConf));
                        context.put("AdminConf_str",Integer.toString(AdminConf));
                        ParameterParser pp=data.getParameters();
                        int startIndex=pp.getInt("startIndex",0);
                        String status=new String();
                        int t_size=courseList.size();

                        if(courseList.size()!=0){

                                status="Noblank";
                                int value[]=new int[7];
                                value=ListManagement.linkVisibility(startIndex,t_size,AdminConf);

                                int k=value[6];
                                context.put("k",String.valueOf(k));

                                Integer total_size=new Integer(t_size);
                                context.put("total_size",total_size);

                                int eI=value[1];
                                Integer endIndex=new Integer(eI);
                                context.put("endIndex",endIndex);
				 int check_first=value[2];
                                context.put("check_first",String.valueOf(check_first));

                                int check_pre=value[3];
                                context.put("check_pre",String.valueOf(check_pre));

                                int check_last1=value[4];
                                context.put("check_last1",String.valueOf(check_last1));

                                int check_last=value[5];
                                context.put("check_last",String.valueOf(check_last));

                                context.put("startIndex",String.valueOf(eI));
                                Vector splitlist=ListManagement.listDivide(courseList,startIndex,AdminConf);
                                context.put("courseDetail",splitlist);
                        }
                        else
                        {
                                if(Mode.equals("All"))
                                {
                                        data.setMessage(m_u.ConvertedString("regCourseMsg",file));
                                }
                                else
                                {
                                        String str=m_u.ConvertedString("listCourseReg",file);
                                        String str1=m_u.ConvertedString("notExist",file);
                                        if(((String)data.getUser().getTemp("lang")).equals("hindi"))
                                                data.setMessage(query+" "+"'"+ valueString+"'"+" "+ str +"  "+ str1);
                                        else
                                                data.setMessage(str+" "+query+" "+"'"+ valueString+"'"+" "+ str1);
                                }
                                status="blank";
                        }
                        context.put("status",status);
			}
		 }//1st if
                catch (Exception e)
                {
                        data.setMessage("The error in Course Listing :-"+e);
                }

    }
}

