package org.iitk.brihaspati.modules.utils;

/*
 * @(#)TopicMetaDataXmlWriter.java
 *
 *  Copyright (c) 2005-2008, 2009,2010-13 ETRG,IIT Kanpur.
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


import java.util.Date;
import java.io.File;
import java.util.Vector;
import java.util.List;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.FileOutputStream;
import javax.xml.parsers.DocumentBuilder;
import org.apache.xerces.dom.DocumentImpl;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import javax.xml.parsers.DocumentBuilderFactory;
import org.iitk.brihaspati.modules.utils.ErrorDumpUtil;
import org.iitk.brihaspati.modules.utils.QuesPaperFileEntry;

/**
 * This class generate Xml file with attributes and values
* @author: <a href="mailto:palseema30@gmail.com">Manorama Pal</a>
*/

public class QuestionPaperXmlWriter{


	public static String  QuespaperXml(String filePath,int id,String question, String questype,  String opt1,String opt2,String opt3,String opt4,String marks,String answer,String cdate){
		String message="UnSuccessfull";
		try{

			//Create instance of DocumentBuilderFactory
                        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                        //Get the DocumentBuilder       
                        DocumentBuilder builder = factory.newDocumentBuilder();
                        //Create blank DOM Document
                        Document doc = builder.parse(getFile(filePath));
			

			//creating tag or field for institute information
                        // root element
                        Element root = doc.getDocumentElement();
                        //create the root element
                        Element quespaper = doc.createElement("Question");
			//set id attribute
        		quespaper.setAttribute("id", Integer.toString(id));
			//create Question element 
        		quespaper.appendChild(getqpElements(doc, quespaper, "question", question));
        		quespaper.appendChild(getqpElements(doc, quespaper, "questype", questype));
			if(questype.equals("mcq")){
        		quespaper.appendChild(getqpElements(doc, quespaper, "option1",opt1));
        		quespaper.appendChild(getqpElements(doc, quespaper, "option2",opt2));
        		quespaper.appendChild(getqpElements(doc, quespaper, "option3",opt3));
        		quespaper.appendChild(getqpElements(doc, quespaper, "option4",opt4));
			}
        		quespaper.appendChild(getqpElements(doc, quespaper, "marks",marks));
        		quespaper.appendChild(getqpElements(doc, quespaper, "answer",answer));
        		quespaper.appendChild(getqpElements(doc, quespaper, "creationDate",cdate));
			root.appendChild(quespaper);
			message=saveXML(doc,filePath);

		}catch(Exception ex){
                        System.out.println("Error on creating xml file : QuestionPaperXmlWriter method name:(QuespaperXml)  "+ex.getMessage());
                }
                return message;
        }
	 //utility method to create text node
   	private static Node getqpElements(Document doc, Element element, String name, String value) {
        	Element node = doc.createElement(name);
        	node.appendChild(doc.createTextNode(value));
        	return node;
    	}

	/**
         *This method uses Xerces specific classes
         *prints the XML document to file.
         *@param doc (Document)
         *@param filePath (String)
         *return String
         */
        private static  String saveXML(Document doc,String filePath) {
                try{
                        //print
                        OutputFormat format = new OutputFormat(doc);
                        format.setIndenting(true);
                        //to generate a file output use fileoutputstream
                        XMLSerializer output = new XMLSerializer(new FileOutputStream(filePath), format);
                        output.serialize(doc);
                        return "Successfull";
                }catch(Exception e){System.out.println(e.getMessage());}
                return "UnSuccessfull";
        }
        /**method for creating blank xml file
         *with root element
         *@param filePath (String)
         */
        private static File getFile(String filePath) {
                File file=new File(filePath);
                if(!file.exists()){
                        try {
                                //Create instance of DocumentBuilderFactor
                                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                                //Get the DocumentBuilder
                                DocumentBuilder builder = factory.newDocumentBuilder();
                                //Create blank DOM Document
                                Document doc = builder.newDocument();
                                //create the root element       
                                Element rootEle = doc.createElement("QuestionPaper");
                                //all it to the xml tre
                                doc.appendChild(rootEle);
                                saveXML(doc,filePath);
                        }catch (Exception ex) { System.out.println("Error in ex "+ex.getMessage()); }
                } return file;
        }

	/**method for Create blank DOM Document
         *@param filepath (String)
         *return document
         */
        private static Document getCreateDocument(String filePath ){
                Document doc=null;
                try{
                        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder builder = factory.newDocumentBuilder();
                        doc = builder.parse(getFile(filePath));

                } catch( Exception e ){
                        e.printStackTrace();
                }
                return doc;
        }


	/** method for get all  elements with the name "Institute"
         *@param filepath (String)
         *return NodeList
         */
        private static NodeList getNodeList(String filePath ){
                NodeList list=null;
                try{
                        Document doc =getCreateDocument(filePath);
                        list = doc.getElementsByTagName("Question");

                } catch( Exception e ){
                        e.printStackTrace();
                }
                return list;
        }
        /** method for get node value by passing element(tag)
         *@param tag (String)
         *@param element (Element)
         *return String
         */
        private static String getTagValue(String tag, Element element) {
                NodeList nodes = element.getElementsByTagName(tag).item(0).getChildNodes();
                Node node = (Node) nodes.item(0);
                return node.getNodeValue().trim();
        }

	/**Method for Delete Institute from Xml
         *@param filePath (String)
         *@param domain (String)
         *return string 
         */
        public static String RemoveElement(String filePath,String quesid)
        {
                String message="UnSuccessfull";
                Element element=null;
                Node node=null;
                try{
                        /**Create blank DOM Document
                         *@see getCreateDocument
                         */
                        Document doc =getCreateDocument(filePath);

                        /**Find all elements with the name "Institute"*/

                        NodeList list = doc.getElementsByTagName("Question");
                        if(list!=null){
                                for( int i=0; i<list.getLength(); i++ ){
                                        node = list.item(i );
                                        if( node.getNodeType() == node.ELEMENT_NODE ){
                                                element = ( Element )node;
                                                 /*if match id then delete the entry from xml file
                                                 */
						String value=element.getAttributes().getNamedItem("id").getNodeValue();
                                                if(value.equals(quesid)){
                                                        doc.getDocumentElement().removeChild(element);
                                                        saveXML(doc,filePath);
                                                }
                                        }
                                }
                        }
                }
                catch(Exception e){ErrorDumpUtil.ErrorLog("Error in util QuestionPaperXmlWriter method name:(RemoveElement)"+e);}
                return message;
        }


	/**method for reading details of a institute from xml file
         *@param filePath (String)
         *return Vector
         */
        public static Vector ReadQPDeatils(String filePath) {
        	Vector v=new Vector();
                Element eElement=null;
                try {
                        File f=new File(filePath);
                        if(f.exists()) {
                                //Create instance of DocumentBuilderFactory
                                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                                //Get the DocumentBuilder
                                DocumentBuilder builder = factory.newDocumentBuilder();
                                //Create blank DOM Document
                                Document doc = builder.parse(getFile(filePath));
                                //Normalize All of the Text in a Document
                                doc.getDocumentElement().normalize();
                                // Find all elements with the name "Question"
                                NodeList nodeList = doc.getElementsByTagName("Question");
                                for( int i=0; i<nodeList.getLength(); i++ ) {
                                        Node nNode = nodeList.item(i);
                                        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                                               eElement = (Element) nNode;


                                                /** get the node value by passing element
                                                 *set the value in InstituteFileEntry object
                                                 *@see InstituteFileEntry in utils
                                                 */
						String opt1="";String opt2=""; String opt3=""; String opt4="";
                                                QuesPaperFileEntry qpfileEntry=new QuesPaperFileEntry();
						String quesid=eElement.getAttributes().getNamedItem("id").getNodeValue();
                                                String ques=getTagValue("question",eElement);
                                                String questype=getTagValue("questype",eElement);
						if(questype.equals("mcq")){
						opt1=getTagValue("option1",eElement);
						opt2=getTagValue("option2",eElement);
						opt3=getTagValue("option3",eElement);
						opt4=getTagValue("option4",eElement);
						}
						String marks=getTagValue("marks",eElement);
						String answer=getTagValue("answer",eElement);
                                                String cdate=getTagValue("creationDate",eElement);
                                               	qpfileEntry.setquesID(Integer.parseInt(quesid));
                                               	qpfileEntry.setQuestion(ques);
                                               	qpfileEntry.setQuestype(questype);
						if(questype.equals("mcq")){
                                               	qpfileEntry.setOption1(opt1);
                                               	qpfileEntry.setOption2(opt2);
                                               	qpfileEntry.setOption3(opt3);
                                               	qpfileEntry.setOption4(opt4);
						}
                                               	qpfileEntry.setMarks(marks);
                                               	qpfileEntry.setAnswer(answer);
                                               	qpfileEntry.setcreationDate(cdate);
                                                /*store all values in the vector*/
                                                v.add(qpfileEntry);
                                      }
                                }
                        }
                }
                catch(Exception e){ErrorDumpUtil.ErrorLog("Error in util QuestionPaperXmlWriter method name:(ReadQPDeatils)"+e);}
                return v;
        }

	/**
         * This method get maximum quizid to generate new quizID
         */
        public static int getMaxQuizID(String filepath){
		int maxnum=0;String num="";
		try{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder builder = factory.newDocumentBuilder();;
                        Document doc = builder.parse(getFile(filepath));
			doc.getDocumentElement().normalize();
			NodeList nodeList = doc.getElementsByTagName("Question");
			for(int x=0,size= nodeList.getLength(); x<size; x++) {
    				num=doc.getElementsByTagName("Question").item(x).getAttributes().getNamedItem("id").getNodeValue();
			} 
			maxnum=Integer.parseInt(num);
		}//try
		catch(Exception e){ErrorDumpUtil.ErrorLog("Error in util QuestionPaperXmlWriter  method name:(getMaxQuizID)"+e);}
		return maxnum+1;
    	}
	/**Method for Delete Institute from Xml
         *@param filePath (String)
         *@param domain (String)
         *return string 
         */
        public static  Vector QuestionDetails(String filePath,String quesid)
        {
		Vector v=new Vector();
                Element eElement=null;
                Node node=null;
                try{    
                        /**Create blank DOM Document
                         *@see getCreateDocument
                         */
                        Document doc =getCreateDocument(filePath);

                        /**Find all elements with the name "Institute"*/

                        NodeList list = doc.getElementsByTagName("Question");
                        if(list!=null){
                                for( int i=0; i<list.getLength(); i++ ){
                                        node = list.item(i );
                                        if( node.getNodeType() == node.ELEMENT_NODE ){
                                                eElement = ( Element )node;
                                                 /*if match id then delete the entry from xml file
                                                 */
                                                String valueid=eElement.getAttributes().getNamedItem("id").getNodeValue();
						if(quesid.equalsIgnoreCase(valueid)){
							String opt1="";String opt2=""; String opt3=""; String opt4="";
                                                	QuesPaperFileEntry qpfileEntry=new QuesPaperFileEntry();
                                                	String ques=getTagValue("question",eElement);
                                                	String questype=getTagValue("questype",eElement);
                                                	if(questype.equals("mcq")){
                                                		opt1=getTagValue("option1",eElement);
                                                		opt2=getTagValue("option2",eElement);
                                                		opt3=getTagValue("option3",eElement);
                                                		opt4=getTagValue("option4",eElement);
                                                	}
                                                	String marks=getTagValue("marks",eElement);
                                                	String answer=getTagValue("answer",eElement);
                                                	String cdate=getTagValue("creationDate",eElement);
						
                                                	qpfileEntry.setquesID(Integer.parseInt(valueid));
                                                	qpfileEntry.setQuestion(ques);
                                                	qpfileEntry.setQuestype(questype);
                                                	if(questype.equals("mcq")){
                                                		qpfileEntry.setOption1(opt1);
                                                		qpfileEntry.setOption2(opt2);
                                                		qpfileEntry.setOption3(opt3);
                                                		qpfileEntry.setOption4(opt4);
						ErrorDumpUtil.ErrorLog(opt1+","+opt2+","+opt3+","+opt4);
                                                	}
                                                	qpfileEntry.setMarks(marks);
                                                	qpfileEntry.setAnswer(answer);
                                                	qpfileEntry.setcreationDate(cdate);
                                                	/*store all values in the vector*/
                                                	v.add(qpfileEntry);
                                        	}
                               	 	}
                        	}
                	}
		}
                catch(Exception e){ErrorDumpUtil.ErrorLog("Error in util QuestionPaperXmlWriter method name:(RemoveElement)"+e);}
		return v;
        }

	 /**method for update the xml file
         *@param filePath (String)
         */
         public static void UpdateQuesPaperxml(String filePath,String quesid,String ques,String qptype,String opt1,String opt2,String opt3,String opt4,String mpq,String ans,String cDate) {
                Node node=null;
                Element eElement=null;
                try {
                        /**Create blank DOM Document
                         *@see getCreateDocument
                         */
                        Document doc =getCreateDocument(filePath);

                        /**Find all elements with the name "Institute"*/
                        NodeList list = doc.getElementsByTagName("Question");
							//ErrorDumpUtil.ErrorLog("llist==="+list);
                        if(list!=null){
                                for( int i=0; i<list.getLength(); i++ ){
                                	eElement = (Element) list.item(i);
                                       /**get tag value by passing tag(question id)
                                       *@see getTagValue method
                                        */
					String valueid=eElement.getAttributes().getNamedItem("id").getNodeValue();

					if(quesid.equalsIgnoreCase(valueid)){
							
						String qtype=getTagValue("questype",eElement);
					
						Node quest = eElement.getElementsByTagName("question").item(0).getFirstChild();
            					quest.setNodeValue(ques);

						Node questype = eElement.getElementsByTagName("questype").item(0).getFirstChild();
                                                questype.setNodeValue(qptype);
						if(qtype.equalsIgnoreCase("mcq")){
							Node option1 = eElement.getElementsByTagName("option1").item(0).getFirstChild();
                                                        option1.setNodeValue(opt1);
							
							Node option2 = eElement.getElementsByTagName("option2").item(0).getFirstChild();
                                                        option2.setNodeValue(opt2);
							
							Node option3 = eElement.getElementsByTagName("option3").item(0).getFirstChild();
                                                        option3.setNodeValue(opt3);
							
							Node option4 = eElement.getElementsByTagName("option4").item(0).getFirstChild();
                                                        option4.setNodeValue(opt4);
						}
						Node marks = eElement.getElementsByTagName("marks").item(0).getFirstChild();
            					marks.setNodeValue(mpq);
						
						Node answer = eElement.getElementsByTagName("answer").item(0).getFirstChild();
            					answer.setNodeValue(ans);
						
						Node creationDate = eElement.getElementsByTagName("creationDate").item(0).getFirstChild();
            					creationDate.setNodeValue(cDate);

							//ErrorDumpUtil.ErrorLog("node1==222="+"question=="+ques+","+qptype+","+opt1+","+opt2+","+opt3+","+opt4+","+mpq+","+ans+","+cDate);
                                                saveXML(doc,filePath);
                                       }
                                }
                        }

                }//try
                catch(Exception e){ErrorDumpUtil.ErrorLog("Error in util QuestionPaperXmlWriter method name:(UpdateQuesPaperxml)"+e);}
        }//method


}//class

