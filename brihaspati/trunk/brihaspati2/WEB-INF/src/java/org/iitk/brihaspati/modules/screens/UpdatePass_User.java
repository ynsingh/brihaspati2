package org.iitk.brihaspati.modules.screens;

/*
 * @(#)UpdatePass_User.java	
 *
 *  Copyright (c) 2015 ETRG,IIT Kanpur. 
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

/**
 * @author <a href="mailto:nksinghiitk@gmail.com">Nagendra Kumar Singh</a> 
 */

import org.apache.turbine.util.RunData;
import org.apache.velocity.context.Context;
import org.iitk.brihaspati.modules.utils.ErrorDumpUtil;
import org.apache.turbine.modules.screens.VelocityScreen;
import org.apache.turbine.util.parser.ParameterParser;


public class UpdatePass_User extends VelocityScreen{
	/**
	 * Loads the template page
	 */
	public void doBuildTemplate( RunData data, Context context)
	{
                try{
                        ParameterParser pp=data.getParameters();
                        String lang=pp.getString("lang","english");
                        String stat=pp.getString("status");
                        context.put("lang",lang);
                        context.put("status",stat);
                }
                catch(Exception ex)
                {
                        data.setMessage("The error in ForgotPasswd Screen !! "+ex);
                }


	}
}

