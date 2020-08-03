package org.iitk.brihaspati.modules.utils;
/*
 * @(#)QuesPaperFileEntry.java
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
 * This class set some value and get in templates from XML file 
 * @author: <a href="mailto:palseema30@gmail.com">Manorama Pal</a>
 */
public class QuesPaperFileEntry
{
	private String question;
	private String questype;
	private String option1;
	private String option2;
	private String option3;
	private String option4;
	private String marks;
	private String answer;
	private String creationDate;
	private int quesId; 

	public void setQuestion(String question)
        {
                this.question=question;
        }
        public String getQuestion()
        {
                return question;
        }

	public void setQuestype(String questype)
        {
                this.questype=questype;
        }
        public String getQuestype()
        {
                return questype;
        }

	public void setOption1(String option1){
		this.option1 = option1;
	}
	
	public String getOption1(){
		return option1;
	}
	
	public void setOption2(String option2){
		this.option2 = option2;
	}
	
	public String getOption2(){
		return option2;
	}

	public void setOption3(String option3){
		this.option3 = option3;
	}
	
	public String getOption3(){
		return option3;
	}
	public String getOption4(){
		return option4;
	}

	public void setOption4(String option4){
		this.option4 = option4;
	}
	
	public void setMarks(String marks)
        {
                this.marks=marks;
        }

        public String getMarks()
        {
                return marks;
        }

	public void setAnswer(String answer)
        {
                this.answer=answer;
        }
        public String getAnswer()
        {
                return answer;
        }


	public void setquesID(int quesId)
        {
                this.quesId=quesId;
        }

        public int getquesID()
        {
                return quesId;
        }

	public void setcreationDate(String creationDate)
        {
                this.creationDate=creationDate;
        }

        public String getcreationDate()
        {
                return creationDate;
        }

}
