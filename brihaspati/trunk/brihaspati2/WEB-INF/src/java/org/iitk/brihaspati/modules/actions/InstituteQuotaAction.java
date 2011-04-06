package org.iitk.brihaspati.modules.actions;
/*
 * @(#)InstituteQuotaAction.java
 *
 *  Copyright (c)2011 ETRG,IIT Kanpur.
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
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
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

import java.util.List;
import org.iitk.brihaspati.modules.utils.MultilingualUtil;
import org.iitk.brihaspati.modules.utils.QuotaUtil;
import org.iitk.brihaspati.modules.utils.ErrorDumpUtil;
import org.apache.velocity.context.Context;
import org.apache.turbine.util.RunData;
import org.apache.torque.util.Criteria;
import org.apache.turbine.util.parser.ParameterParser;
import org.iitk.brihaspati.om.InstituteQuotaPeer;

/**
 *This action is called when SysAdmin update the Institute Quota.
 * @author <a href="mailto:singh_jaivir@rediffmail.com">Jaivir Singh</a>
*/

public class  InstituteQuotaAction extends SecureAction
{
	/**
	 * In this method, Updation of Institute Quota
	 * @param data RunData
	 * @param context Context
	 * @return nothing
	 * 
	 */
        public void doUpdate(RunData data, Context context)
        {
		try
		{
			ParameterParser pp=data.getParameters();
			String mode=pp.getString("mode");
			context.put("mode",mode);
			String msg="";
			Criteria crt=new Criteria();
                        long LsValue=QuotaUtil.getFileSystemSpaceinGB();
			String LangFile=data.getUser().getTemp("LangFile").toString();
				String iid=pp.getString("instid");
				String iquota=pp.getString("iquota");
				long ASpace=Long.valueOf(iquota).longValue();
				crt=new Criteria();
				crt.add(InstituteQuotaPeer.INSTITUTE_ID,iid);
				crt.add(InstituteQuotaPeer.INSTITUTE_AQUOTA,iquota);
				if(LsValue > ASpace){
	                        	InstituteQuotaPeer.doUpdate(crt);
					msg=MultilingualUtil.ConvertedString("c_msg5",LangFile);
                        		data.setMessage(msg);
				}
				else{
					msg=MultilingualUtil.ConvertedString("qmgmt_msg1",LangFile);
					data.addMessage(msg);	
				}
		}
		catch(Exception ex)
		{data.setMessage("The Error in updating the quota"+ex);}
	}
	public void doPerform(RunData data,Context context) throws Exception
        {
		String LangFile=data.getUser().getTemp("LangFile").toString();
        	String action=data.getParameters().getString("actionName","");
                if(action.equals("eventSubmit_doUpdate"))
                	doUpdate(data,context);
                else{    
			String msg=MultilingualUtil.ConvertedString("action_msg",LangFile);
                	data.setMessage(msg);
		}	
       }
}	
