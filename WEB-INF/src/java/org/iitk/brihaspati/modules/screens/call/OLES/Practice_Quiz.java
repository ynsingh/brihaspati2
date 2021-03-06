package org.iitk.brihaspati.modules.screens.call.OLES;

/*
 * @(#)Practice_Quiz.java	
 *
 *  Copyright (c) 2010-13 MHRD, DEI Agra. 
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
import org.iitk.brihaspati.modules.utils.MultilingualUtil;
import org.iitk.brihaspati.modules.utils.UserUtil;
import org.iitk.brihaspati.modules.utils.ModuleTimeThread;
import org.iitk.brihaspati.modules.utils.GroupUtil;
import org.iitk.brihaspati.modules.utils.UserGroupRoleUtil;
import org.iitk.brihaspati.modules.utils.CourseUserDetail;
import org.iitk.brihaspati.modules.utils.TopicMetaDataXmlReader;
import org.iitk.brihaspati.modules.utils.QuizFileEntry;
import org.iitk.brihaspati.modules.utils.FileEntry;
import org.iitk.brihaspati.modules.utils.ViewAllQuestionUtil;
/**
* This class is used to create quiz randomly 
* @author <a href="mailto:aayushi.sr@gmail.com">Aayushi</a>
* @author <a href="mailto:tejdgurung20@gmail.com">Tej Bahadur</a>
* @modify date: 14aug2013
*/

public class Practice_Quiz extends SecureScreen{
	public void doBuildTemplate(RunData data,Context context){
		/**
        	*Retrieve the Parameters by using the Parameter Parser
        	*Get the UserName and put it in the context
        	*for template use
        	*/
        	ParameterParser pp=data.getParameters();
		String lang=data.getUser().getTemp("LangFile").toString();
        	try{
        		User user=data.getUser();
        	
        		String mode =pp.getString("mode"," ");
        		String quizMode =pp.getString("quizMode"," ");        			
        		String type = pp.getString("type","");
        		String count = pp.getString("count","");
        		String courseID=(String)user.getTemp("course_id");
        		String quizDetail="";
        	
        		context.put("tdcolor",count);
        		context.put("course",(String)user.getTemp("course_name"));
			context.put("mode",mode);
			context.put("quizMode",quizMode);
			context.put("type",type);
			context.put("courseID",courseID);
			
			String username=user.getName();
			int GID=GroupUtil.getGID(courseID);
                        Vector UDetail=UserGroupRoleUtil.getUDetail(GID,2);
                        Vector topicList=new Vector();
			Vector allTopics=new Vector();
                        for(int j= 0; j< UDetail.size(); j++)
                        {
                                String uname=((CourseUserDetail) UDetail.elementAt(j)).getLoginName();
            			String filePath=TurbineServlet.getRealPath("/QuestionBank"+"/"+uname+"/"+courseID);
            			String quizPath="/QBtopiclist.xml";
           			File file=new File(filePath+"/"+quizPath);
				//QuizMetaDataXmlReader topipcmetadata=null;
				TopicMetaDataXmlReader topicmetadata=null;
			
				if(file.exists())
                        	{
                        		topicmetadata=new TopicMetaDataXmlReader(filePath+"/QBtopiclist.xml");
                                	allTopics=topicmetadata.getQuesBanklist_Detail();
                                	if(allTopics!=null)
					{
                                		for(int i=0;i<allTopics.size();i++)
                                		{//for
                                			String topicnew=((FileEntry) allTopics.elementAt(i)).getTopic();
                                           		QuizFileEntry fileEntry=new QuizFileEntry();
                                                	String questiontype=((FileEntry) allTopics.elementAt(i)).getTypeofquestion();
                                                	String difflevel=((FileEntry) allTopics.elementAt(i)).getDifflevel();
                                                	Vector qno=ViewAllQuestionUtil.ReadTopicAllFile(topicnew,filePath,questiontype,difflevel);
                                                	fileEntry.setTopic(topicnew);
                                                	fileEntry.setUserID(uname);
                                                	fileEntry.setQuestionNumber(Integer.toString(qno.size()));
                                                	topicList.add(fileEntry);
                                 		}
                              		}
                  		}
			}

			//if(file.exists()){
			//	topipcmetadata=new QuizMetaDataXmlReader(filePath+"/"+quizPath);				
			//	Vector topicList1=topipcmetadata.getTopicNames();
			//	topicList.addAll(topicList1);
			//	}
			if(topicList.size()!=0){
				context.put("topicList",topicList);
			}	            
			if(mode.equalsIgnoreCase("update")){
				quizDetail = pp.getString("quizDetail","");
				String quizName = pp.getString("quizName","");
				context.put("quizName",quizName);
				String quizSetting = pp.getString("quizSetting","");
				context.put("quizSetting",quizSetting);
				String[] temp1 = quizSetting.split(",");
				String topic=temp1[0];
				context.put("topic",topic);
				String questionType=temp1[1];
				context.put("questionType",questionType);
				String questionLevel=temp1[2];
				context.put("questionLevel",questionLevel);
				String markPerQuestion=temp1[3];
				context.put("markPerQuestion",markPerQuestion);
				String questionNumber=temp1[4];
				context.put("questionNumber",questionNumber);
				String topicID = temp1[5];
				context.put("topicID",temp1[5]);
			}
			else{
				quizDetail = pp.getString("quizName","");
				context.put("quizName",quizDetail);
			}			
			context.put("quizDetail",quizDetail);
			String[] temp = quizDetail.split(",");
			String quizID=temp[0];
			context.put("quizID",quizID);
			String maxMarks=temp[1];
			context.put("maxMarks",maxMarks);
			String noQuestions=temp[2];
			context.put("noQuestions",noQuestions);
			String allowPractice = temp[3];
			context.put("allowPractice",allowPractice);
			/**
                         *Time calculaion for how long user use this page.
                         */
			String Role = (String)user.getTemp("role");
			int uid=UserUtil.getUID(user.getName());
                        if((Role.equals("student")) || (Role.equals("instructor")) || (Role.equals("teacher_assistant")))
                        {
				int eid=0;
				 ModuleTimeThread.getController().CourseTimeSystem(uid,eid);
                        }
			
        	}
        	catch(Exception e) {
        	ErrorDumpUtil.ErrorLog("The exception in Random_Quiz screen::"+e);
        		data.setMessage(MultilingualUtil.ConvertedString("brih_exception"+e,lang));
        	}
    	}
}
