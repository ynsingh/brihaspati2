package org.iitk.brihaspati.modules.screens.call.Backups;

/*
 * @(#)BackupList.java	
 *
 *  Copyright (c) 2006,2009 ETRG,IIT Kanpur. 
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
//java
import java.io.File;
import java.io.FilenameFilter;
import java.util.List;
import java.util.Vector;
import java.lang.reflect.Array;

//turbine
import org.apache.turbine.util.RunData;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.context.Context;
import org.apache.turbine.util.parser.ParameterParser;
import org.apache.turbine.services.servlet.TurbineServlet;

//brihaspati
import org.iitk.brihaspati.modules.screens.call.SecureScreen;
import org.iitk.brihaspati.modules.utils.MultilingualUtil;
import org.iitk.brihaspati.modules.utils.ErrorDumpUtil;
import org.iitk.brihaspati.modules.utils.OnlyExt;


public class BackupList extends SecureScreen 
{
	 public void doBuildTemplate( RunData data, Context context )
	{
		String LangFile=(String)data.getUser().getTemp("LangFile");
		context.put("tdcolor",data.getParameters().getString("count",""));
		try{
			String crsName=(String)data.getUser().getTemp("course_id");
			String instituteId = StringUtils.substringAfterLast(crsName,"_");
			//dir=new File(TurbineServlet.getRealPath("/BackupData/"));
			File dir=new File(TurbineServlet.getRealPath("/BackupData/"+instituteId+"/"));
			if(dir.exists()){
				File list[];
				/**
				  * @param str[] added by shaista  
				  */
				String str[]= new String[100]; 
				Vector tmp=new Vector();
				String st=data.getParameters().getString("st","");
				context.put("st",st);
				list=dir.listFiles();
				FilenameFilter filter=null;
				if((Array.getLength(list))>0)
				{
					/**
					  * Below for loop code is added by Shaista
					  */
					//for(int i=0 ; i < Array.getLength(list) ; i++){
						FilenameFilter only = new OnlyExt(data.getUser().getTemp("course_id" ).toString());
						//ErrorDumpUtil.ErrorLog("only="+only);
						str =dir.list(only);  
						//ErrorDumpUtil.ErrorLog("list[]="+str);
						for (int i=0; i < str.length; i++) { 
							tmp.add(str[i]); 
						}
					//}	
					/**
					  * Commented by shaista
					  */
					//context.put("list",list);
					context.put("list",tmp);
				}
				else{
				//	data.setMessage(MultilingualUtil.ConvertedString("backup_msg4",LangFile));
					data.setMessage(MultilingualUtil.ConvertedString("backup_backup",LangFile)+" "+MultilingualUtil.ConvertedString("backup_msg4",LangFile));
					context.put("list","navil");
				}
			}
			else{
		//		data.setMessage(MultilingualUtil.ConvertedString("backup_msg5",LangFile));
				data.setMessage(MultilingualUtil.ConvertedString("backup_backup",LangFile)+" "+MultilingualUtil.ConvertedString("backup_dir",LangFile)+" "+MultilingualUtil.ConvertedString("backup_msg5",LangFile));

			}
			
		}
		catch(Exception e){data.setMessage("new error"+e);}	
	}
	
} 

