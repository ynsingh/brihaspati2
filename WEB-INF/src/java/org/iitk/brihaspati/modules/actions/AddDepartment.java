package org.iitk.brihaspati.modules.actions;

/*
 * @(#)AddDepartment.java    
 *
 *  Copyright (c) 2013 ETRG,IIT Kanpur. 
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
 * @author <a href="santoshkumarmiracle@gmail.com">Santosh Kumar</a>
 * @author <a href="tejdgurung20@gmail.com">Tej Bahadur</a>
 * @modify date: 31-05-2013
 */
import java.util.List;
import java.util.Vector;
import java.util.StringTokenizer;
import org.apache.turbine.util.RunData;
import org.apache.velocity.context.Context;
import org.apache.turbine.util.parser.ParameterParser;
import org.iitk.brihaspati.modules.utils.MultilingualUtil;
import org.iitk.brihaspati.modules.utils.ErrorDumpUtil;
import org.iitk.brihaspati.om.DepartmentPeer;
import org.iitk.brihaspati.om.SchoolPeer;
import org.apache.torque.util.Criteria;
import org.iitk.brihaspati.om.DeptSchoolUniv;
import org.iitk.brihaspati.om.DeptSchoolUnivPeer;
import org.iitk.brihaspati.om.Department;

public class AddDepartment extends SecureAction
{

        private String LangFile=null;
	//Initialize criteria for database query
	Criteria crit= new Criteria();

	/**
         * This method actually registers a new Department/School along with the Institute/University. 
         * in the system
         * @param data RunData instance
         * @param context Context instance
         * @exception Exception, a generic exception
         */
 
	public void doInsert(RunData data, Context context) throws Exception
	{
	
		/**
        	 * Getting file value from temporary variable according to selection
         	 * Replacing the value from Property file
         	 **/

		LangFile=(String)data.getUser().getTemp("LangFile");
		ParameterParser pp=data.getParameters();
        	String instituteId=pp.getString("instituteId","");
		if((instituteId.equals(""))||(instituteId.equals(null)))
		{
        		instituteId=(data.getUser().getTemp("Institute_id")).toString();
        	}
		int instituteid=Integer.parseInt(instituteId);
        	String mode=pp.getString("mode","");

	 	/**
	  	* Check Details for Departmernt and School.
          	* Gather details from the page where user has entered them
          	* @param deptname: Getting deptname as a String from Parameter Parser 
          	* @param deptcode: Getting deptcode as a String from Parameter Parser 
          	* @param deptnick: Getting deptnick as a String from Parameter Parser 
          	* @param deptfloor: Getting deptfloor as an int 
          	* @param schname: Getting schname as a String from Parameter Parser 
          	* @param schcode: Getting schcode as a String from Parameter Parser 
          	* @param schnick: Getting schnick as a String from Parameter Parser 
          	* @param schdesc: Getting schdesc as a String from Parameter Parser
          	*/
		
		// Code for Add Department 
		if(mode.equals("dept"))
		{
        		String deptname=pp.getString("deptname","");
		        String deptcode=pp.getString("dcode","");
        		String deptnick=pp.getString("dnick","");
			String fcount=pp.getString("fcount","");
			int deptfloor;
			if(fcount.equals("")){
			deptfloor=0;
			}
			else{
		        deptfloor=Integer.parseInt(fcount);
			}
			try 
			{
				/**
                        	* Register a new Department with Institute.
				*/
				crit= new Criteria();
				crit.add(DepartmentPeer.NAME,deptname);
				crit.add(DepartmentPeer.DEPARTMENT_CODE,deptcode);
				crit.add(DepartmentPeer.NICK_NAME,deptnick);
				crit.add(DepartmentPeer.FLOORS_COUNT,deptfloor);
				crit.add(DepartmentPeer.INSTITUTE_ID,instituteId);
				DepartmentPeer.doInsert(crit);
				//map department automatically if register by user.
				crit=new Criteria();
				crit.addGroupByColumn(DepartmentPeer.DEPARTMENT_ID);
				crit.add(DepartmentPeer.DEPARTMENT_CODE,deptcode);
				List deptlist=DepartmentPeer.doSelect(crit);
				for(int j=0;j<deptlist.size();j++)
				{
					Department element=(Department)deptlist.get(j);
                        		int deptid=element.getDepartmentId();
                        		String schid=null;
					crit=new Criteria();
                        		crit.add(DeptSchoolUnivPeer.DEPT_ID,deptid);
                        		crit.add(DeptSchoolUnivPeer.SCHOOL_ID,schid);
                        		crit.add(DeptSchoolUnivPeer.UNIVERSITY_ID,instituteId);
                        		DeptSchoolUnivPeer.doInsert(crit);
				}
				data.setMessage(MultilingualUtil.ConvertedString("brih_deptadd",LangFile));
			}
			catch(Exception e)
			{
				data.setMessage(MultilingualUtil.ConvertedString("brih_deptexist",LangFile));
			}
		}
	  	
		// Code for Add School/Center 
		if(mode.equals("school")) {
	        	String schname=pp.getString("schname","");
		        String schcode=pp.getString("schcode","");
		        String schnick=pp.getString("schnick","");
		        String schdesc=pp.getString("schdesc","");
                	try
			{
				/**
                        	* Register a new School/Center with Institute.
                        	*/
                		crit= new Criteria();
                		crit.add(SchoolPeer.NAME,schname);
	                	crit.add(SchoolPeer.SCHOOL_CODE,schcode);
        	        	crit.add(SchoolPeer.NICK_NAME,schnick);
                		crit.add(SchoolPeer.DESCRIPTION,schdesc);
                		SchoolPeer.doInsert(crit);
				data.setMessage(MultilingualUtil.ConvertedString("brih_schadd",LangFile));
                	}
                	catch(Exception e)
			{
				data.setMessage(MultilingualUtil.ConvertedString("brih_schexist",LangFile));
                	}
		}
	}
	/**
         * This method delete the existing Department/School along with the Institute/University. 
         * in the system
         * @param data RunData instance
         * @param context Context instance
         * @exception Exception, a generic exception
         */

       	public void doDelete(RunData data, Context context) throws Exception
	{
		/**
        	*Getting file value from temporary variable according to selection
        	*Replacing the value from Property file
        	**/
		
		LangFile=(String)data.getUser().getTemp("LangFile");
		ParameterParser pp=data.getParameters();
        	String mode=pp.getString("mode","");
		
		/**
        	 * Check Details for Departmernt and School.
        	 * Gather details from the page where user has entered them
        	 * @param deptid: Getting deptid as a String from Parameter Parser 
        	 * @param schid: Getting schid as a String from Parameter Parser 
        	 * @param instituteid: Getting instituteid as a String from Parameter Parser
       		 */

		// Code for Delete Department
                if(mode.equals("deptdel"))
		{
                        String deptid=pp.getString("deptid","");
                	try
			{
				/**
                        	* Delete Existing Department along with Institute.
                        	*/
                        	crit= new Criteria();
                      	  	crit.add(DepartmentPeer.DEPARTMENT_ID,deptid);
                       	 	DepartmentPeer.doDelete(crit);
				//unmap department
				crit= new Criteria();
                        	crit.add(DeptSchoolUnivPeer.DEPT_ID,deptid);
                        	DeptSchoolUnivPeer.doDelete(crit);
				data.setMessage(MultilingualUtil.ConvertedString("brih_deptdel",LangFile));
				context.put("mode","lstdept");
                	}
                	catch(Exception e)
			{
                        	ErrorDumpUtil.ErrorLog("Error In Department Deletion"+e.getMessage());
                	}
        	}
		// Code for Delete School/Center 
                if(mode.equals("schdel"))
		{
                        String schid=pp.getString("schid","");
                	try
			{
				/**
                        	* Delete Existing School/Center along with Institute.
                       		*/
                        	crit= new Criteria();
                        	crit.add(SchoolPeer.SCHOOL_ID,schid);
                        	SchoolPeer.doDelete(crit);
				data.setMessage(MultilingualUtil.ConvertedString("brih_schdel",LangFile));
                	}
                	catch(Exception e)
			{
                        	ErrorDumpUtil.ErrorLog("Error In school/center Deletion !!! "+e.getMessage());
                	}
        	}
		 // Code for Unmap Department
                if(mode.equals("deptunmap"))
		{
			String instituteId=pp.getString("instituteId","");
		     	if((instituteId.equals(""))||(instituteId.equals(null)))
			{
        			instituteId=(data.getUser().getTemp("Institute_id")).toString();
			}
                        String deptid=pp.getString("deptid","");
                	try 
			{
                        	/**
                        	* Delete Existing Department along with Institute.
                        	*/
                        	crit= new Criteria();
                        	crit.add(DeptSchoolUnivPeer.DEPT_ID,deptid);
                        	crit.add(DeptSchoolUnivPeer.UNIVERSITY_ID,instituteId);
                        	DeptSchoolUnivPeer.doDelete(crit);
                        	data.setMessage(MultilingualUtil.ConvertedString("brih_deptunmap",LangFile));
                	}
               		catch(Exception e)
			{
                        	ErrorDumpUtil.ErrorLog("Error In unmam department"+e.getMessage());
               		}
        	}
		// Code for Unmap School
                if(mode.equals("schunmap"))
		{
                        String instituteId=pp.getString("instituteId","");
                        if((instituteId.equals(""))||(instituteId.equals(null)))
			{
                        	instituteId=(data.getUser().getTemp("Institute_id")).toString();
                        }
                        String schid = pp.getString("schid","");
                	try 
			{
                        	/**
                        	* Delete Existing Department along with Institute.
                        	*/
                        	crit= new Criteria();
                      		crit.add(DeptSchoolUnivPeer.SCHOOL_ID,schid);
                        	crit.add(DeptSchoolUnivPeer.UNIVERSITY_ID,instituteId);
                        	List schlist= DeptSchoolUnivPeer.doSelect(crit);
				for(int j=0;j<schlist.size();j++)
				{
                        		DeptSchoolUniv element=(DeptSchoolUniv)schlist.get(j);
                        		String schh_id=element.getSchoolId();
                        		crit=new Criteria();
                        		crit.add(DeptSchoolUnivPeer.SCHOOL_ID,schh_id);
                        		crit.add(DeptSchoolUnivPeer.UNIVERSITY_ID,instituteId);
                        		DeptSchoolUnivPeer.doDelete(crit);
                        	}
                        	data.setMessage(MultilingualUtil.ConvertedString("brih_schunmap",LangFile));
                        	context.put("mode","schunmap");
                	}
                	catch(Exception e)
			{
                        	ErrorDumpUtil.ErrorLog("Error In unmam department"+e.getMessage());
                	}
        	}	
	}

	 /**
         * This method actually maps existing Department with School 
	 * along with the Institute/University in the system
	 * It is getting file value as a input and store all mapped deparmtent  
	 * into table which is DEPARTMENT_SCHOOL_UNIVERSITY table
         * This table is used for showing mapped department list in template.
         * @param data RunData instance
         * @param context Context instance
         * @exception Exception, a generic exception
         */

       	public void doMap(RunData data, Context context) throws Exception
	{
		/**
        	* Getting file value from temporary variable according to selection
       		* Replacing the value from Property file
        	**/
		LangFile=(String)data.getUser().getTemp("LangFile");
		ParameterParser pp=data.getParameters();
        	String mode=pp.getString("mode","");
        	String sdept=pp.getString("sdept","");
		String instituteId=pp.getString("instituteId","");
		//Get institute id from temp if it is not getting form file value.
        	if((instituteId.equals(""))||(instituteId.equals(null)))
		{
        		instituteId=(data.getUser().getTemp("Institute_id")).toString();
        	}
		String List=pp.getString("selectFileNames","");
		String schl=pp.getString("school","");
        	context.put("selectFile",List);
		String schid = null;
		String deptid = null;
		String univid = null;
		try 
		{
			/**
         		* Use StringTokenizer to break string after "^".
         		*/
			StringTokenizer st=new StringTokenizer(List,"^");
                	Vector v=new Vector();
                	for(int i=0;st.hasMoreTokens();i++)
                	{
                		v.addElement(st.nextToken());
                	}
	
        	        /**
                	 * Get All Department/School Id, obtained from the list 
                	 * then insert into table one by one for mapping Department with School.
                	 */
                	for(int i=0;i<v.size();i++)
                	{
				String temp=(v.elementAt(i).toString()).toUpperCase();
                        	String tempSplit[]=temp.split(":");
				// Department Map 
				if(mode.equals("deptmap"))
				{
                              		deptid=temp;
				}
				// School/Center Map
				if(mode.equals("schoolmap") && (!List.equals("")))
				{
					schid=tempSplit[0];
					if(!sdept.equals("sdept"))
                                		deptid=tempSplit[1];
				}
				try
				{
					/**
					* Map Department With School
					*/
                                	crit=new Criteria();
                            		crit.add(DeptSchoolUnivPeer.DEPT_ID,deptid);
	                                crit.add(DeptSchoolUnivPeer.SCHOOL_ID,schid);
        	                        crit.add(DeptSchoolUnivPeer.UNIVERSITY_ID,instituteId);
                	                DeptSchoolUnivPeer.doInsert(crit);
					//Department map Message
					if(mode.equals("deptmap")) 
					{
						data.setMessage(MultilingualUtil.ConvertedString("brih_deptmap",LangFile));
					}
					// School/Center Map Message
					else
					{
						data.setMessage(MultilingualUtil.ConvertedString("brih_schmap",LangFile));
					}
				}
				catch(Exception e)
				{
					data.setMessage(MultilingualUtil.ConvertedString("brih_already",LangFile));
				}
			}
		}
	        catch(Exception e)
		{
        		ErrorDumpUtil.ErrorLog("Error In Department/school maping "+e.getMessage());
       		}
	}

  	/**
         * Default action to perform if the specified action
         * cannot be executed.
         * @param data RunData
         * @param context Context
         */
        public void doPerform(RunData data, Context context) throws Exception
	{
		String action=data.getParameters().getString("actionName","");
                if(action.equals("eventSubmit_doInsert"))
		{
                        doInsert(data,context);
		}
		if(action.equals("eventSubmit_doDelete"))
		{
                        doDelete(data,context);
		}
	  	if(action.equals("eventSubmit_doMap"))
		{
                        doMap(data,context);
		}
        }
}          
