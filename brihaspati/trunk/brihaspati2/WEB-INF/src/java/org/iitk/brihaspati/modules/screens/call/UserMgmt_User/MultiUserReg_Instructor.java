package org.iitk.brihaspati.modules.screens.call.UserMgmt_User;

/*
 * @(#) MultiUserReg_Instructor.java
 *
 *  Copyright (c) 2005 ETRG,IIT Kanpur. 
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
 */
import org.apache.turbine.om.security.User;
import org.apache.turbine.util.RunData;
import org.apache.velocity.context.Context;
import org.iitk.brihaspati.modules.screens.call.SecureScreen_Instructor;

import org.iitk.brihaspati.modules.utils.UserUtil;
import org.iitk.brihaspati.modules.utils.ModuleTimeThread;

/**
 * This class loads the vm file and checks for the access rights of the user if he can access this page 
 * @author <a href="mailto:awadhesh_trivedi@yahoo.co.in">Awadhesh Kumar Trivedi</a> 
 * @author <a href="mailto:shaistashekh@gmail.com">Shaista</a>
 */

public class MultiUserReg_Instructor extends SecureScreen_Instructor
{
    /*
     * Places all the data objects in the context for further use
     */   
	public void doBuildTemplate( RunData data, Context context )
    	{
		context.put("tdcolor",data.getParameters().getString("count",""));
		User user=data.getUser();
		String CName=(String)user.getTemp("course_name");
	 	context.put("course",CName);
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

	}
}
