package org.iitk.brihaspati.modules.screens.call.UserMgmt_Admin;

/*
 * @(#)RegisterationManagement.java	
 *
 *  Copyright (c) 2005,2009 ETRG,IIT Kanpur. 
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
 * @author <a href="mailto:shaistashekh@hotmail.com">Shaista</a>
 * @author <a href="mailto:awadhk_t@yahoo.com">Awadhesh Kumar Trivedi</a>
 * @author <a href="mailto:singh_jaivir@rediffmail.com">Jaivir Singh</a>
 * @modified date: 03-08-2011 (Shaista)
 */

import java.util.Vector;
import java.util.List;

import org.apache.torque.util.Criteria;
import org.apache.turbine.util.RunData;
import org.apache.turbine.util.parser.ParameterParser;
import org.apache.velocity.context.Context;

import org.iitk.brihaspati.modules.utils.StringUtil;
import org.iitk.brihaspati.modules.utils.ListManagement;
import org.iitk.brihaspati.modules.utils.AdminProperties;
import org.iitk.brihaspati.modules.utils.MultilingualUtil;
import org.iitk.brihaspati.modules.utils.ErrorDumpUtil;
import org.iitk.brihaspati.modules.utils.CourseManagement;
import org.iitk.brihaspati.modules.screens.call.SecureScreen_Admin;
import org.iitk.brihaspati.om.InstituteAdminRegistrationPeer;

public class RegisterationManagement extends SecureScreen_Admin
{
	private String LangFile= null;
    public void doBuildTemplate( RunData data, Context context )
    {
		String mode=data.getParameters().getString("mode","");
		context.put("mode",mode);
		String counter=data.getParameters().getString("count","");
		context.put("tdcolor",counter);
		try
                {
			String scrs=data.getParameters().getString("scourse","");
			context.put("scourse",scrs);
                        LangFile=(String)data.getUser().getTemp("LangFile");
                        MultilingualUtil m_u=new MultilingualUtil();
                        Vector courseList=new Vector();
                        String stat=data.getParameters().getString("status","");
                        context.put("stat",stat);
                        String query="";
			if((mode == "") && (mode != "MCRegister") && (mode != "All")){
				/**
				* Added by shaista to show the list of institute 
	                        *Getting the list of Institute and set in context 
        	                *for using in templates and
                	        * set the status for User Registration 
                        	*as well as Course Registration,count for tab colour.
	                        */	
        	                Criteria crit=new Criteria();
                	        int addnot[]={0,2};
                        	crit.addGroupByColumn(InstituteAdminRegistrationPeer.INSTITUTE_NAME);
	                        crit.addNotIn(InstituteAdminRegistrationPeer.INSTITUTE_STATUS,addnot);
        	                List list=InstituteAdminRegistrationPeer.doSelect(crit);
                	        context.put("instList",list);
			}
                        String valueString="";
			if(scrs.equals("scourse")){
                        if(mode.equals("All"))
                        {
                                courseList=CourseManagement.getCourseNUserDetails("All");
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
        //                              valueString=data.getParameters().getString("valueString");
                        /**
                          * Check for special characters
                          */
                           valueString =StringUtil.replaceXmlSpecialCharacters(data.getParameters().getString("valueString"));

                                context.put("query",query);
				context.put("valueString",valueString);
                                courseList=ListManagement.getListBySearchString("CourseWise",query,valueString);
                                context.put("mode","Search");
                        }
                        String path=data.getServletContext().getRealPath("/WEB-INF")+"/conf"+"/"+"Admin.properties";
                        int AdminConf = Integer.valueOf(AdminProperties.getValue(path,"brihaspati.admin.listconfiguration.value"));
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
                                if(mode.equals("All"))
                                {
                                        /**
                                         * and replacing the String from property file define in LangFile.
                                         * @param regCourseMsg String type variable
                                         * @param LangFile String type variable
                                         * @see MultilingualUtil in utils
                                         */
                                         data.setMessage(m_u.ConvertedString("regCourseMsg",LangFile));
                                }
                                else
                                {
                                        /**
                                         * and replacing the String from property file define in LangFile.
                                         * @param listCourseReg String type variable
                                         * @param LangFile String type variable
                                         * @see MultilingualUtil in utils
                                         */
                                        String str=m_u.ConvertedString("listCourseReg",LangFile);
                                        String str1=m_u.ConvertedString("notExist",LangFile);
                                        data.setMessage(str+" " +query+" "+"'" +valueString+"'"+" "+ str1);
                                }
                                status="blank";
                        }
			    context.put("status",status);
                }
                }//if scrs
                catch (Exception e)
                {
                        data.setMessage("The error in Course Listing for Secondary Instructor:-"+e);
                }
    }
}

