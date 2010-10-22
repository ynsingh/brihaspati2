package org.iitk.brihaspati.modules.screens.call.AdminProfile;

/*
 * @(#)AdminParam.java	
 *
 *  Copyright (c) 2005,2009,2010 ETRG,IIT Kanpur. 
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
 *  Contributors: Members of ETRG, I.I.T. Kanpur 
 */
import org.iitk.brihaspati.modules.utils.AdminProperties;
import org.iitk.brihaspati.modules.utils.MultilingualUtil;
import org.iitk.brihaspati.modules.screens.call.SecureScreen;
import org.apache.turbine.om.security.User;
import org.apache.turbine.util.RunData;
import org.apache.velocity.context.Context;
import org.iitk.brihaspati.modules.utils.ErrorDumpUtil;

/**
 * @author <a href="mailto:chitvesh@yahoo.com ">Chitvesh Dutta</a> 
 * @author <a href="mailto:awadhk_t@yahoo.com ">Awadhesh Kumar Trivedi</a> 
 * @author <a href="mailto:shaistashekh@hotmail.com"> Shaista </a>
 * @modified date: 17-10-2009
 * @author <a href="mailto:sharad23nov@yahoo.com ">Sharad Singhi</a> 
 * @author <a href="mailto:jaivir_singh@rediffmail.com">Jaivir Singh</a> 
 * @author <a href="mailto:sunil.singh6094@gmail.com">Sunil Kumar</a> 
 * @modified date: 29-09-2010
 */


/**
 * Loads the template page for Administrator
 */

//public class AdminParam extends SecureScreen_Admin{
public class AdminParam extends SecureScreen{
	public void doBuildTemplate(RunData data, Context context){
		User user = data.getUser();
		String path="";	
		//if((user.getName()).equals("admin"))
			path=data.getServletContext().getRealPath("/WEB-INF")+"/conf"+"/"+"Admin.properties";
		//else
		//	path=data.getServletContext().getRealPath("/WEB-INF")+"/conf"+"/"+"InstituteAdmin.properties";
		String LangFile=data.getUser().getTemp("LangFile").toString();
		context.put("tdcolor",data.getParameters().getString("count",""));
		try{
		/**
		 * getting value of configuration parameter 	
		 * @see AdminProperties in utils
		 */
		 String AdminConf = AdminProperties.getValue(path,"brihaspati.admin.listconfiguration.value");
		 context.put("AdminConf",new Integer(AdminConf));
		 String CrsExp = AdminProperties.getValue(path,"brihaspati.admin.courseExpiry");
		 context.put("CrsExp",new Integer(CrsExp));
		 String mserv = AdminProperties.getValue(path,"brihaspati.mail.server");
		 context.put("mServer",mserv);
		 String mServerPort = AdminProperties.getValue(path,"brihaspati.mail.smtp.port");
                 context.put("mServerPort",mServerPort);
		 String smtp = AdminProperties.getValue(path,"brihaspati.mail.smtp.from");
		 context.put("mFrom",smtp);
		 String mailnm = AdminProperties.getValue(path,"brihaspati.mail.username");
		 context.put("muname",mailnm);
		 String mailpass = AdminProperties.getValue(path,"brihaspati.mail.password");
		 context.put("mPassword",mailpass);
		 String domainNm = AdminProperties.getValue(path,"brihaspati.mail.local.domain.name");
		 context.put("dName",domainNm);
		 String email = AdminProperties.getValue(path,"brihaspati.mail.email");//admin email(add in turbinr_user)
		 context.put("eMail","");
		 String cquota = AdminProperties.getValue(path,"brihaspati.admin.quota.value");
		 context.put("cquota",cquota);
		 String uquota = AdminProperties.getValue(path,"brihaspati.user.quota.value");
		 context.put("uquota",uquota);
		 String hdir = AdminProperties.getValue(path,"brihaspati.home.dir.value");
		 if(hdir.equals("")){
			hdir=System.getProperty("user.home");
		 }
		 context.put("hdir",hdir);
		//----------------------------------FAQ---------------------------
                 String FaqExp = AdminProperties.getValue(path,"brihaspati.admin.FaqExpiry");
                 context.put("FaqExp",new Integer(FaqExp));
		//----------------------------------FAQ---------------------------

		}
		catch(Exception e) {	
			data.addMessage(MultilingualUtil.ConvertedString("adm_msg1",LangFile)); 
			//data.addMessage("Some Problem Occured in getting the Parameter Value"); 
		}
		context.put("afname",user.getFirstName());
		context.put("alname",user.getLastName());	
		context.put("eMail",user.getEmail());	
	}
}

