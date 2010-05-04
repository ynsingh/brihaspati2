package org.iitk.brihaspati.modules.screens.call.Local_Mail;

/*
 * @(#)MailView.java	
 *
 *  Copyright (c) 2005-2006 ETRG,IIT Kanpur. 
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
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.util.Vector;
import java.util.List;
import java.util.Date;
//import org.apache.turbine.modules.screens.VelocitySecureScreen;
import org.apache.turbine.Turbine;
import org.apache.turbine.services.servlet.TurbineServlet;
import org.apache.turbine.util.security.AccessControlList;
import org.apache.turbine.util.RunData;
import org.apache.velocity.context.Context;
import org.apache.turbine.om.security.User;
import org.iitk.brihaspati.modules.utils.UserUtil;
import org.iitk.brihaspati.modules.utils.NotInclude;
import org.iitk.brihaspati.om.MailReceivePeer;
import org.iitk.brihaspati.om.MailSendPeer;
import org.iitk.brihaspati.om.MailSend;
import org.apache.torque.util.Criteria;
import org.iitk.brihaspati.modules.screens.call.SecureScreen;
import org.iitk.brihaspati.modules.utils.ErrorDumpUtil;
/**
 * This class contains code for display message with details
 *
 * @author  <a href="mailto:chitvesh@yahoo.com">Chitvesh Dutta</a>
 * @author  <a href="mailto:awadhesh_trivedi@yahoo.co.in">Awadhesh Kumar Trivedi</a>
 */
//public class MailView extends VelocitySecureScreen{
public class MailView extends SecureScreen{
/**
 * This method actually viewing the messages in the Local Mail 
 * @param data RunData instance
 * @param context Context instance
 * @exception Exception, a generic exception
 */
	public void doBuildTemplate(RunData data,Context context)
	{
		Vector v=new Vector();
		User user=data.getUser();
		String user_name = user.getName();
		int msg_id=data.getParameters().getInt("msgid");
		AccessControlList acl=data.getACL();
                String mode=data.getParameters().getString("mod","");
		context.put("mode",mode);
		/**
		 * Retrive the CourseName and CourseId from Temp variables
		*/ 
		
		String dir=(String)user.getTemp("course_id");
		String cname=(String)user.getTemp("course_name");
		context.put("CName",cname);
		/**
		 * Getting the actual path where the Mail is stored
		 * @return String
		 */
		String filePath=data.getServletContext().getRealPath("/UserArea")+"/"+user_name+"/Msg.txt";	
				
		String topicDesc="";
		/**
		 * read the file using XML descriptior
		 *  @try and catch Identifies statements that user want to monitor
		 *  and catch for exceptoin.
		 */ 
		try
		{
			String str[] = new String[10000];
			int i =0;
			int start = 0;
			int stop = 0;
			try{
			BufferedReader br=new BufferedReader(new FileReader (filePath));
			while ((str[i]=br.readLine()) != null)
			{
				if (str[i].equals("<"+msg_id+">")) {
				start = i;	}
				else if(str[i].equals("</"+msg_id+">"))
				{
					stop = i;
	
				}
					i= i +1;
				}
				br.close();

			}
			catch(IOException e)
			{
				e.printStackTrace();
			}
			for(int j=start+1;j<stop;j++)
			{
				topicDesc=topicDesc+ "\n"+str[j];
			}

			
		}catch(Exception e){data.setMessage("The Error in reading description !!"+e);}	
		/**
		 * here comes the code to view attachment with the message
		 */
		context.put("message",topicDesc);
		String chkPath = data.getServletContext().getRealPath("/UserArea")+"/"+user_name+"/"+"Attachment";
		File fchk1 = new File(chkPath);
		if(fchk1.exists() && fchk1.isDirectory()) 	
		{
			String smsg_id = Integer.toString(msg_id);
			String fPath=data.getServletContext().getRealPath("/UserArea")+"/"+user_name+"/"+"Attachment/"+smsg_id;	
			File dirHandle=new File(fPath);
			if(dirHandle.exists() && dirHandle.isDirectory())				{
				FilenameFilter exclude=new NotInclude("__des.txt");
	      			File file[]=dirHandle.listFiles(exclude);
				String servContext=TurbineServlet.getContextPath();
				Vector content=new Vector();
				for(int i=0;i<file.length;i++)
				{
					Date date=new Date(file[i].lastModified());
					String fileData[]={file[i].getName(),date.toString()};
					content.addElement(fileData);
				}
				context.put("dirContent",content);	
			}
		}
	/** 
	 * From Here comes the code for change in Status.( Unread to Read ) 
	 * @return nothing
	 * @try and catch Identifies statements that user want to monitor
	 * and catch for exceptoin
	 */
		Criteria crit=new Criteria();
		try
		{
			String dbtopic=Integer.toString(msg_id);
			context.put("msgID",dbtopic);
			String Username=user.getName();
			int receiver_user_id=UserUtil.getUID(Username);
			crit=new Criteria();
			crit.add(MailReceivePeer.MAIL_ID,msg_id);
			crit.add(MailReceivePeer.RECEIVER_ID,receiver_user_id);
			crit.add(MailReceivePeer.MAIL_READFLAG,1);
			MailReceivePeer.doUpdate(crit);
		}
		catch(Exception e)
		{data.setMessage("The Error in Mail Receive table for read flag updatation "+e);}
	
	/**
	 * From Here is the code for sender and posted date 
	 */
		try
		{
		        crit=new Criteria();
			crit.add(MailSendPeer.MAIL_ID,msg_id);
			List u=MailSendPeer.doSelect(crit);
                         for(int count=0;count<u.size();count++)
			 {
				  MailSend element=(MailSend)(u.get(count));
    				  String subject=(element.getMailSubject());
                                  int userid=(element.getSenderId());
                                  String username=UserUtil.getLoginName(userid);
                                  String retrive_date=(element.getPostTime()).toString();
				  /**
				    *      Adds the information to the context
				    */      
				  
       				  context.put("sub",subject);
                                  context.put("retrive_user",username);
                                  context.put("retrive_date",retrive_date);
			 }
		}
		catch(Exception e)
		{
			data.setMessage("The error in retrive details from Mail Send "+e);
		}

	}

 /**
  *    Performs the security check required
  *    @param data RunData
  *    @return boolean
  */           
	/*public boolean isAuthorized(RunData data)
	{
		boolean authorised=true;
		try
		{
		AccessControlList acl=data.getACL();
		User user=data.getUser();
		String g=user.getTemp("course_id").toString();*/

		 /**
		  *  Checks if the user has logged in as an instructor. If so, then he is
		  *  authorized to view this page
		  **/ 
		/*if(g!=null && acl.hasRole("instructor",g) || acl.hasRole("student",g))
		{
			authorised=true;
		}
		else
		{
			data.setScreenTemplate(Turbine.getConfiguration().getString("template.login"));
			authorised=false;
		}	
		}
		catch(Exception e){}
		return authorised;
	}*/

}




