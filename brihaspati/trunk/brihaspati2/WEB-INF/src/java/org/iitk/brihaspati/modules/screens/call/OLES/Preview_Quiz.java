package org.iitk.brihaspati.modules.screens.call.OLES;

/*
 * @(#)Preview_Quiz.java	
 *
 *  Copyright (c) 2010 MHRD, DEI Agra. 
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
 *  Contributors: Members of MHRD, DEI Agra. 
 * 
 */

//Jdk
import java.io.File;
import java.util.Vector;
//Turbine
import org.apache.velocity.context.Context;
import org.apache.turbine.services.servlet.TurbineServlet;
import org.apache.turbine.util.RunData;
import org.apache.turbine.om.security.User;
import org.apache.turbine.util.parser.ParameterParser;
//brihaspati
import org.iitk.brihaspati.modules.utils.ErrorDumpUtil;
import org.iitk.brihaspati.modules.screens.call.SecureScreen;
import org.iitk.brihaspati.modules.utils.QuizMetaDataXmlReader;

/**
* This class displays the list of quizzes for preview in online examination system 
* @author <a href="mailto:noopur.here@gmail.com">Nupur Dixit</a>
*/

public class Preview_Quiz extends SecureScreen{
	public void doBuildTemplate(RunData data,Context context){
	/**
        *Retrieve the Parameters by using the Parameter Parser
        *Get the UserName and put it in the context
        *for template use
        */
        ParameterParser pp=data.getParameters();
        try
        {
        	String LangFile=data.getUser().getTemp("LangFile").toString();
        	User user=data.getUser();
        	
        	String mode =pp.getString("mode"," ");
        	String type = pp.getString("type","");
        	String courseid=(String)user.getTemp("course_id");
        	
        	context.put("tdcolor",pp.getString("count",""));
        	context.put("course",(String)user.getTemp("course_name"));
			context.put("mode",mode);
			context.put("type",type);
			
			String filePath=TurbineServlet.getRealPath("/Courses"+"/"+courseid+"/Exam/");
            String quizPath="/Quiz.xml";
            
            File file=new File(filePath+"/"+quizPath);
			Vector quizList=new Vector();
			QuizMetaDataXmlReader quizmetadata=null;
			
			if(file.exists()){
				context.put("isFile","exist");
				quizmetadata=new QuizMetaDataXmlReader(filePath+"/"+quizPath);				
				quizList=quizmetadata.getModeQuiz_Detail("random");
				if(quizList!=null){
					if(quizList.size()!=0){
						context.put("quizList",quizList);
					}
				}
			}
			else
				context.put("isFile","");
	    }
        catch(Exception e) {
        	ErrorDumpUtil.ErrorLog("The exception in Preview_Quiz screen::"+e);
        	data.setMessage("See ExceptionLog !! ");
        }
    }
}