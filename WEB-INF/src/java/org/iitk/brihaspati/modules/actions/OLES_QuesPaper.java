package org.iitk.brihaspati.modules.actions;
/*
 * @(#)OLES_QuesPaper.java
 *
 *  Copyright (c) 2020, IITK.
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
//JDK
import java.io.File;
import java.util.Properties;
import java.text.SimpleDateFormat;
//Turbine
import org.apache.turbine.util.RunData;
import org.apache.turbine.om.security.User;
import org.apache.velocity.context.Context;
import org.apache.turbine.util.parser.ParameterParser;
import org.apache.turbine.services.servlet.TurbineServlet;
//Brihaspati
import org.iitk.brihaspati.modules.utils.ExpiryUtil;
import org.iitk.brihaspati.modules.utils.ErrorDumpUtil;
import org.iitk.brihaspati.modules.utils.MultilingualUtil;
import org.iitk.brihaspati.modules.utils.QuestionPaperXmlWriter;

/**
 * This Action class for Generate ques paper  module of online examination system
 * @author <a href="mailto:palseema30@gmail.com">Manorama Pal</a>
 */
public class OLES_QuesPaper extends SecureAction{

	String CoursePath=TurbineServlet.getRealPath("/Courses");
	private String crsId=new String();
	private String LangFile=new String();

	/**
	 * This method is invoked when no button corresponding to
	 * Action is found
	 * @param data RunData
	 * @param context Context
	 * @exception Exception, a generic exception
	 */
	public void doPerform(RunData data,Context context) throws Exception{
		String action=data.getParameters().getString("actionName","");
		context.put("actionName",action);
                        //ErrorDumpUtil.ErrorLog("qpfile=====in ation=== "+action);
		if(action.equals("eventSubmit_doInserQuestion"))
                        doInsertQuestion(data,context,"More");
                else if(action.equals("eventSubmit_doFinishQuestion"))
                        doInsertQuestion(data,context,"Finish");
                else if(action.equals("eventSubmit_dodeleteQuesPaper"))
                        dodeleteQuesPaper(data,context);
                else if(action.equals("eventSubmit_dodeleteQuestion"))
                        dodeleteQuestion(data,context);
                else if(action.equals("eventSubmit_doEditQuestion"))
                        doEditQuestion(data,context);
			
		else
			data.setMessage(MultilingualUtil.ConvertedString("action_msg",LangFile));
	}

	/** This method is responsible for writing the question in xml file 
          * @param data RunData instance
          * @param context Context instance
          * @exception Exception, a generic exception
          */
        public void doInsertQuestion(RunData data, Context context,String status)
        {
		try
                {//try
            		//ErrorDumpUtil.ErrorLog("status");
                        LangFile=(String)data.getUser().getTemp("LangFile");
                	ParameterParser pp=data.getParameters();
                        User user=data.getUser();
			String username=pp.getString("username","");
                        crsId=(String)data.getUser().getTemp("course_id");

			String qpname=pp.getString("qpname","");
			String ques=pp.getString("Question","");
			String marks=pp.getString("marks","");
			String questype=pp.getString("Questype","");
			String option1=pp.getString("opt1","");
            		String option2=pp.getString("opt2","");
            		String option3=pp.getString("opt3","");
            		String option4=pp.getString("opt4","");
            		String answer=pp.getString("answer","");

			String curdate=ExpiryUtil.getCurrentDate("-");
			
			String filepath=CoursePath+"/"+crsId+"/"+"QuestionPaper";
			File ff=new File(filepath);
                	if(!ff.exists())
                	ff.mkdirs();
			filepath=filepath+"/"+qpname+".xml";
			//ErrorDumpUtil.ErrorLog("qpname "+qpname+"filepath===="+filepath);
		
			int id=QuestionPaperXmlWriter.getMaxQuizID(filepath);
			String writeinxmlfile=QuestionPaperXmlWriter.QuespaperXml(filepath,id,ques,questype,option1,option2,option3,option4,marks,answer,curdate);

			//ErrorDumpUtil.ErrorLog("writeinxmlfile "+writeinxmlfile+"id====="+id+"ques==="+ques+""+questype+""+option1+""+option2+""+option3+""+option4+""+marks+""+answer+""+curdate);

			
			if(status.equals("More")){
                		setTemplate(data,"call,OLES,Create_QuesPaper.vm");

			}
			if(status.equals("Finish")){
                		setTemplate(data,"call,OLES,Oles_QB.vm");
			}
			//ErrorDumpUtil.ErrorLog("status==="+status+"id==="+id);
			data.setMessage(MultilingualUtil.ConvertedString("brih_qus",LangFile)+" "+MultilingualUtil.ConvertedString("oles_msg",LangFile));
			
		}//try close
		catch(Exception e){
                    	ErrorDumpUtil.ErrorLog("The exception in OLES_QuesPaper - doInserQuestion "+e);
            		data.setMessage("Error in action[OLES_QuesPaper:doInserQuestion]"+e);
		}

	}
	public void dodeleteQuesPaper(RunData data, Context context){
		try{
			LangFile=(String)data.getUser().getTemp("LangFile");
                        ParameterParser pp=data.getParameters();
                        crsId=(String)data.getUser().getTemp("course_id");
                        String qpfile=pp.getString("qpfile","");
			String filepath=CoursePath+"/"+crsId+"/"+"QuestionPaper";
                        filepath=filepath+"/"+qpfile+".xml";
                        File ff=new File(filepath);
                        if(ff.exists())
                        ff.delete();
                        //ErrorDumpUtil.ErrorLog("qpfile=====in ation=== "+qpfile+"filepath=i actions==="+filepath);
			//data.setMessage(MultilingualUtil.ConvertedString("brih_qus",LangFile)+" "+MultilingualUtil.ConvertedString("oles_bank",LangFile)+" "+MultilingualUtil.ConvertedString("brih_hasbeendelete",LangFile));
			 data.setMessage(MultilingualUtil.ConvertedString("brih_quespaper",LangFile)+" "+MultilingualUtil.ConvertedString("brih_delete",LangFile)+" "+MultilingualUtil.ConvertedString("brih_successfully",LangFile));




		}//try close
                catch(Exception e){
                        ErrorDumpUtil.ErrorLog("The exception in OLES_QuesPaper - dodeleteQuesPaper "+e);
                        data.setMessage("Error in action[OLES_QuesPaper - dodeleteQuesPaper ]"+e);
                }

	}
	public void dodeleteQuestion(RunData data, Context context){
                try{
                        LangFile=(String)data.getUser().getTemp("LangFile");
                        ParameterParser pp=data.getParameters();
                        crsId=(String)data.getUser().getTemp("course_id");
                        String qpfile=pp.getString("qpfile","");
                        String quesid=pp.getString("quesid","");
                        String filepath=CoursePath+"/"+crsId+"/"+"QuestionPaper";
                        filepath=filepath+"/"+qpfile+".xml";

			QuestionPaperXmlWriter.RemoveElement(filepath,quesid);

                        //ErrorDumpUtil.ErrorLog("qpfile=====in ation=== "+qpfile+"filepath=i actions==="+filepath+"quesid===="+quesid);
                        data.setMessage(MultilingualUtil.ConvertedString("brih_quespaper",LangFile)+" "+MultilingualUtil.ConvertedString("brih_delete",LangFile)+" "+MultilingualUtil.ConvertedString("brih_successfully",LangFile));




                }//try close
                catch(Exception e){
                        ErrorDumpUtil.ErrorLog("The exception in OLES_QuesPaper - dodeleteQuestion "+e);
                        data.setMessage("Error in action[OLES_QuesPaper - dodeleteQuestion]"+e);
                }

        }
	public void doEditQuestion(RunData data, Context context){
                try{
                        LangFile=(String)data.getUser().getTemp("LangFile");
                        ParameterParser pp=data.getParameters();
                        crsId=(String)data.getUser().getTemp("course_id");
                        String qpfile=pp.getString("qpfile","");
			String quesid=pp.getString("quesid","");

                        String ques=pp.getString("Question","");
                        String marks=pp.getString("marks","");
                        String questype=pp.getString("Questype","");
                        String option1=pp.getString("op1","");
                        String option2=pp.getString("op2","");
                        String option3=pp.getString("op3","");
                        String option4=pp.getString("op4","");
                        String answer=pp.getString("answer","");

                        String curdate=ExpiryUtil.getCurrentDate("-");

                        String filepath=CoursePath+"/"+crsId+"/"+"QuestionPaper";
                        filepath=filepath+"/"+qpfile+".xml";
                        File ff=new File(filepath);
		
//                	ErrorDumpUtil.ErrorLog("==in ation=edit== "+qpfile+"actions==="+filepath+"option1==="+option1+","+option2+","+option3+","+option4+","+answer+","+quesid+","+ques+","+marks+","+questype+","+curdate);

			QuestionPaperXmlWriter.UpdateQuesPaperxml(filepath,quesid,ques,questype,option1,option2,option3,option4,marks,answer,curdate);

                        data.setMessage(MultilingualUtil.ConvertedString("brih_qus",LangFile)+" "+MultilingualUtil.ConvertedString("brih_hasbeenedit",LangFile)); 


                }//try close
                catch(Exception e){
                        ErrorDumpUtil.ErrorLog("The exception in OLES_QuesPaper - dodeleteeditQuestion "+e);
                        data.setMessage("Error in action[OLES_QuesPaper - dodeleteeditQuestion]"+e);
                }

        }



}
