package org.iitk.brihaspati.modules.screens.call.OLES;

/*
 * @(#)View_QPaper.java
 *
 *  Copyright (c) 2020 IIT Kanpur..
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
 *   Contributors: Members of IIT Kanpur.
 *
 */

//Jdk
import java.io.File;
import java.util.Vector;
//Turbine
import org.apache.turbine.util.RunData;
import org.apache.velocity.context.Context;
import org.apache.turbine.util.parser.ParameterParser;
import org.apache.turbine.om.security.User;
import org.apache.turbine.services.servlet.TurbineServlet;
//brihaspati
import org.iitk.brihaspati.modules.screens.call.SecureScreen;
import org.iitk.brihaspati.modules.utils.ErrorDumpUtil;
import org.iitk.brihaspati.modules.utils.ModuleTimeThread;
import org.iitk.brihaspati.modules.utils.MultilingualUtil;
import org.iitk.brihaspati.modules.utils.QuestionPaperXmlWriter;

import org.iitk.brihaspati.modules.utils.UserUtil;
/**
 * This class manages the preview feature of quiz questions 
 * @author <a href="mailto:palseema30@gmail.com">Manorama pal</a>
 */

public class  View_QPaper extends  SecureScreen{
        public void doBuildTemplate(RunData data, Context context){
                /**
                *Retrieve the Parameters by using the Parameter Parser
                *Get the UserName and put it in the context
                *for template use
                */

            	ParameterParser pp=data.getParameters();
                String LangFile=data.getUser().getTemp("LangFile").toString();
                try{
                        User user=data.getUser();

                        String Role = (String)user.getTemp("role");
                        /**
                         *Time calculaion for how long user use this page.
                         */
                         int uid=UserUtil.getUID(user.getName());
                         if((Role.equals("student")) || (Role.equals("instructor")) || (Role.equals("teacher_assistant")))
                         {
                                int eid=0;
                                 ModuleTimeThread.getController().CourseTimeSystem(uid,eid);
                         }

                        String courseid=(String)user.getTemp("course_id");
                        String courseName=(String)user.getTemp("course_name");
			context.put("course",courseName);

                        String CoursePath=TurbineServlet.getRealPath("/Courses");
                        String count = pp.getString("count","");
                        String username=data.getUser().getName();
                        String qpfile = pp.getString("qpfile","");
			context.put("exmqp",qpfile);
                        String questype = pp.getString("Questype","");
			context.put("Questype",questype);
                        String filePath=data.getServletContext().getRealPath("/Courses"+"/"+courseid+"/QuestionPaper");
                        File ff=new File(filePath+"/"+qpfile+".xml");

                        context.put("tdcolor",count);
			if(ff.exists())
                        filePath=filePath+"/"+qpfile+".xml";
			Vector Qpaper=QuestionPaperXmlWriter.ReadQPDeatils(filePath);
			context.put("qpaper",Qpaper);
                        //ErrorDumpUtil.ErrorLog("----Question paperPreview------"+Qpaper+"filePath==="+filePath);
                }
                catch(Exception e) {
                        ErrorDumpUtil.ErrorLog("The exception in Preview screen::"+e);
                        data.setMessage(MultilingualUtil.ConvertedString("brih_exception"+e,LangFile));
                }
        }
}
                                                                            
