package org.iitk.brihaspati.modules.screens.call.OLES;

/*
 * @(#)Create_QuesPaper.java	
 *
 *  Copyright (c) 2010-13 ETRG,IIT Kanpur. 
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
import java.util.Vector;

import org.apache.velocity.context.Context;
import org.apache.turbine.util.RunData;
import org.apache.turbine.om.security.User;
import org.apache.turbine.util.parser.ParameterParser;
import org.apache.turbine.modules.screens.VelocityScreen;
import org.apache.turbine.services.servlet.TurbineServlet;
import org.apache.commons.io.FilenameUtils;

import org.iitk.brihaspati.modules.utils.UserUtil;
import org.iitk.brihaspati.modules.utils.CourseTimeUtil;
import org.iitk.brihaspati.modules.utils.ModuleTimeUtil;
import org.iitk.brihaspati.modules.utils.QuestionPaperXmlWriter;
import org.iitk.brihaspati.modules.utils.ErrorDumpUtil;
import org.iitk.brihaspati.modules.screens.call.SecureScreen;
import org.iitk.brihaspati.modules.utils.AdminProperties;
import org.iitk.brihaspati.modules.utils.CommonUtility;

/**
* This class create quesstion paper 
* @author <a href="mailto:palseema30@gmail.com">Manorama Pal</a>
*/

public class Create_QuesPaper extends SecureScreen{
	public void doBuildTemplate(RunData data,Context context) 
	{
		try{

			User user=data.getUser();
			ParameterParser pp=data.getParameters();
			context.put("tdcolor",pp.getString("count","1"));
			String crsId=(String)data.getUser().getTemp("course_id");
			context.put("course",crsId);
			String Role = (String)user.getTemp("role");

			String Questype=pp.getString("Questype","");
                	context.put("Questype",Questype);
			//String mode=pp.getString("mode","");
                        //context.put("mode",mode);
			String addques=pp.getString("addques","");
                        context.put("addques",addques);

			String qpname=pp.getString("qpfile","");
                        context.put("qpfile",qpname);
			//String fname=pp.getString("fname","");
                        int uid=UserUtil.getUID(user.getName());
                        if((Role.equals("student")) || (Role.equals("instructor")))
                        {
                                CourseTimeUtil.getCalculation(uid);
                                ModuleTimeUtil.getModuleCalculation(uid);
                        }


			Vector listfile=new Vector();
			String filePath=TurbineServlet.getRealPath("/Courses"+"/"+crsId+"/"+"QuestionPaper");
			File folder = new File(filePath);
			File[] files = folder.listFiles();
			
			for (int i = 0; i < files.length; i++) {
                		String ffname=files[i].getName();
				String fNamewoExt = FilenameUtils.removeExtension(ffname);
				listfile.add(fNamewoExt);
				
				context.put("listfile",listfile);
            		}	
			//ErrorDumpUtil.ErrorLog("\n listfile====="+listfile+"folder===="+folder+"=====fname==="+fname);
			
			 String path=TurbineServlet.getRealPath("/WEB-INF")+"/conf"+"/"+"Admin.properties";
                         String conf =AdminProperties.getValue(path,"brihaspati.admin.listconfiguration.value");
                         int list_conf=Integer.parseInt(conf);
                         context.put("userConf",new Integer(list_conf));
                         context.put("userConf_string",conf);
			 Vector vct=CommonUtility.PListing(data,context,listfile,list_conf);

		}//try
		catch(Exception ex)
		{
		data.setMessage("The error in Create_QuesPaper !! "+ex);
		}
	}
}

