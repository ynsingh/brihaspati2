/*
 * @(#)Twtwin.java
 *
 *  Copyright (c) 2012 ETRG,IIT Kanpur.
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

/**                     
 *  @author: <a href="sisaudiya.dewan17@gmail.com">Dewanshu Singh Sisaudiya</a>
 */


package org.iitk.brihaspati.modules.screens.call;


import org.apache.turbine.util.RunData;
import org.apache.turbine.util.parser.ParameterParser;
import org.apache.velocity.context.Context;
import org.apache.turbine.om.security.User;
import org.iitk.brihaspati.modules.screens.call.SecureScreen;

public class Twtwin extends SecureScreen{

        public void doBuildTemplate( RunData data, Context context )
	{

			try{
				User user=data.getUser();
				ParameterParser pp=data.getParameters();
        	                String ip=data.getServerName();
                	        String port=Integer.toString(data.getServerPort());
                        	String sch=data.getServerScheme();
	                        String ipadd=sch+"://"+ip+":"+port;
        	                context.put("ipadd",ipadd);
				pp.add("str","twitter");
				String fname=user.getFirstName();
  	                      	String lname=user.getLastName();
                	        context.put("firstname",fname);
                        	context.put("lastname",lname);
                        	context.put("fullname",fname+lname);

			}catch(Exception exp){}
	}
}
