/**
 * These java script functions are used for validate the registration form. 
 * Here we create function for common fields which are used in every registeration form in the brihaspati-LMS.
 * These java script functions returns alert messages and highlighting the field if the input values in the form is missing or incorrect.
 * @author <a href="mailto:tejdgurung20@gmail.com">Tej Bahadur</a>
 * @Created Date: 07-02-2012
 **/

// Course Id
function validateCourseId(fld){
var error = "";
        if (fld.value == "") {
                fld.style.background = 'Yellow';
                error = "* you havn't entered course id.\n";
        } else {
                fld.style.background = 'White';
        }
        return error;
}

// Course Name
function validateCourseName(fld){
var error = "";
        if (fld.value == "") {
                fld.style.background = 'Yellow';
                error = "* you havn't entered course name.\n";
        } else {
                fld.style.background = 'White';
        }
        return error;
}

// Email
function trim(s){
        return s.replace(/^\s+|\s+$/, '');
}
function validateEmail(fld) {
        var error="";
        var tfld = trim(fld.value);                        // value of field with whitespace trimmed off
        //var emailFilter = /^[^@]+@[^@.]+\.[^@]*\w\w$/ ;
        var emailFilter = /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/;
        var illegalChars= /[\(\)\<\>\,\;\:\\\"\[\]]/ ;
        if (fld.value == "")
        {
                fld.style.background = 'Yellow';
                error = "* You havn't entered valid  email address.\n";
        }
        else if (!emailFilter.test(tfld))
        {
                fld.style.background = 'Yellow';
                error = "* Please enter a valid email address.\n";
        }
        else if (fld.value.match(illegalChars))
        {
                fld.style.background = 'Yellow';
                error = "* The email address contains illegal characters.\n";
        }
        else
        {
                fld.style.background = 'White';
        }
        return error;
}

// Roll No
function validateRollNo(fld){
        var error="";
        if((fld.rollno.value.length == 0)&&(fld.PrgName.value!="Select Program")){
                fld.rollno.style.background = 'Yellow';
                error="* you havn't entered User's Roll No.\n";
        }
        else
        {
                fld.rollno.style.background = 'White';
        }
        return error;
}

// Institute name
function validateInstName(fld){
        var error="";
        if(fld.value == "Select Institute"){
                error="* You havn't selected Institute name.\n";
        }
        return error;
}

// Group Name
function validateGroup(fld){
        var error="";
        if(fld.value == ""){
                error="* Please search specific group name according to Insitute name.\n";
        }
        return error;
}

// User Role
function validateRole(fld){
        var error="";
        if(fld.value == "Select Role"){
                error="* you havn't selected User's Role.\n";
        }
        return error;
}

// Upload File
function validateFile(fld){
var error = "";
        if (fld.value.length == 0) {
                fld.style.background = 'Yellow';
                error = "* you havn't selected specific file !!\n";
        } else {
                fld.style.background = 'White';
        }
        return error;
}


/**
 * This function is used for enable and disable the Roll No textbox if program selected by user.
 */

function ChkPrg(frm,field)
{
        if((frm.prg.value=="Select Program")||(frm.prg.value=="RWP"))
        {
                frm.rollno.value="";
                frm.rollno.disabled=true;
                frm.rollno.style.background = '';
        }
        else
        {
                frm.rollno.disabled=false;
                frm.rollno.style.background = 'Yellow';
       }
}

/**
 * This java script function is used for validate the registration form of instructor in specific course 
 * in the Institue or organization within super admin and institute admin and also through online mode.
 * @see template:- UserMgmt_InstituteAdmin/InstUserMgmt_Admin.vm, 
 * @see UserMgmt_Admin/RegisterationManagement.vm, OnlineRegistration.vm
 */

function  checkCourseName(frm,fld) {
var reason = "";
    reason += validateCourseId(frm.COURSEID);
    reason += validateCourseName(frm.CNAME);
    reason += validateEmail(frm.EMAIL);
if (reason != "") {
        alert("Some fields need correction:\n\n"+reason);
        return false;
}
        frm.actionName.value=fld.name;
        frm.submit();
}

/**
 * This java script function is used for validate registerartion form of secondary instructor.
 * @see template:- Instructor_Mgmt/UserMgmt_Instructor.vm, UserMgmt_InstituteAdmin/RegisterSecInstructorInst.vm
 */

function  checkFieldInst(frm,fld) {
var reason = "";
    reason += validateEmail(frm.EMAIL);
        if (reason != "") {
        alert("Some fields need correction:\n\n" + reason);
        return false;
}
        frm.actionName.value=fld.name;
        frm.submit();
}

/**
 * This java script function is used for validate registration form of student(online) in the specific course.
 * @see template:- OnlineRegistration.vm 
 */

function  checkField(frm,fld) {
var reason = "";
    reason += validateInstName(frm.instName);
    reason += validateGroup(frm.group);
    reason += validateRole(frm.role);
    reason+= validateEmail(frm.EMAIL);
    reason += ChkRollNo(frm);
if (reason != "") {
        alert("Some fields need correction:\n\n" + reason);
        return false;
}
        frm.actionName.value=fld.name;
        frm.submit();
}

/**
 * This java script function is used for validate the registration form of instructor in specific course in the Institue or organization.
 * @see template, UserMgmt_InstituteAdmin/InstUserMgmt_Admin.vm
 */

function checkFieldAddUser(frm,fld) {
var reason = "";
    reason += validateGroup(frm.group);
    reason += validateRole(frm.role);
    reason += validateEmail(frm.EMAIL);
    //reason += validateRollNo(frm);
    reason += ChkRollNo(frm);
if (reason != "") {
        alert("Some fields need correction:\n\n" + reason);
        return false;
}
        frm.actionName.value=fld.name;
        frm.submit();
}

/**
 * This java script function is used for validate the registration form of student in specific courses.
 * @see template, UserMgmt_User/StudentManagement.vm, 
 */

function  checkFieldStudent(frm,fld) {
var reason = "";
    reason += validateEmail(frm.EMAIL);
    reason += validateRollNo(frm);
if (reason != "") {
        alert("Some fields need correction:\n\n" + reason);
        return false;
}
        frm.actionName.value=fld.name;
        frm.submit();
}

/**
 * This java script is used validate the File Upload field for automatic multiple courses and institute logo.
 * @see template InstUserRegistrationManagement.vm, RegisterationManagement.vm 
 */

function  CheckFieldFile(frm,fld) {
var reason = "";
        reason += validateFile(frm.file);
if (reason != "") {
        alert(""+ reason);
        return false;
}
        frm.actionName.value=fld.name;
        frm.submit();
}

/**
 * This java script function is used for validate the Match String in search option.
 */

function checkNull(frm,field){
	if(frm.valueString.value!=""){
        	frm.submit();
	}
        else{
		frm.valueString.style.background = 'Yellow';
                alert("The 'Match String' text box can not be NULL");
	}
}

/**
 * This java script function is used for select all record for deletion.
 * @see template:- Instructor_Mgmt/UserMgmt_Instructor.vm
 */

function addDeleteList(field,frm)
{
	if(field.checked){
        frm.deleteFileNames.value=frm.deleteFileNames.value+field.name+"^";
        }
        else{
	        var rmFile,index,actualString,preString,postString;
                actualString=frm.deleteFileNames.value;
                index=actualString.indexOf(field.name+"^",0);
                preString=actualString.substring(0,index);
                postString=actualString.substring(index+field.name.length+1);
                actualString=preString+postString;
                frm.deleteFileNames.value=actualString;
        }
}

/**
 * This java script is used for Removing checkbox selected user from List. 
 * @see template UserMgmt_Instructor.vm, RemoveStudent.vm.
 */

function DeleteField(frm,field) {
                if(frm.deleteFileNames.value!="")
                {
                frm.actionName.value=field.name;
                frm.submit();
                }else
                alert("Please select checkbox !!");
        }

/*
 * This function is used for reset the form.
 */

function checkClear(frm,field)
{
     	frm.EMAIL.value="";
	frm.COURSEID.value="";
        frm.CNAME.value="";
        frm.PASSWD.value="";
        frm.FNAME.value="";
        frm.LNAME.value="";
        frm.role.value="";
        frm.rollno.disabled=true;
        frm.rollno.style.background = '';
}
function ChkRollNo(fld){
        var error="";
	if(fld.role.value=="student"){
	if(fld.prg.value!="Select Program")
	{
	        if(fld.rollno.value == 0){
	               fld.rollno.style.background = 'Yellow';
         	       error="* you havn't entered User's Roll No.\n";
		}
        }
        else
        {
                fld.rollno.style.background = 'White';
        }
	}
        return error;
}

function isInteger(s) {
      var cle=document.getElementById('checkint');
      var i;
      s = s.toString();
 for (i = 0; i < s.length; i++) {
      var c = s.charAt(i);
 if (isNaN(c)) {
      cle.value="";
      cle.focus;
      alert("Please Enter number Only");
      return false;
        }
      }
      return true;
    }

function  checkdept(frm,fld) {
var reason = "";
    reason += validatedeptname(frm.deptname);
    reason += validatedcode(frm.dcode);
    reason += validatednick(frm.dnick);
if (reason != "") {
        alert("Some fields need correction:\n\n"+reason);
        return false;
}
        frm.actionName.value=fld.name;
        frm.submit();
}
function validatedeptname(fld){
var error = "";
        if (fld.value == "") {
                fld.style.background = 'Yellow';
                error = "* you haven't entered department name.\n";
        } else {
                fld.style.background = 'White';
        }
        return error;
}

function validatednick(fld){
var error = "";
        if (fld.value == "") {
                fld.style.background = 'Yellow';
                error = "* you haven't entered nick name.\n";
        } else {
                fld.style.background = 'White';
        }
        return error;
}

function validatedcode(fld){
var error = "";
        if (fld.value == "") {
                fld.style.background = 'Yellow';
                error = "* you haven't entered department code.\n";
        } else {
                fld.style.background = 'White';
        }
        return error;
}

function  checkschool(frm,fld) {
var reason = "";
    reason += validateschname(frm.schname);
    reason += validateschcode(frm.schcode);
    reason += validateschnick(frm.schnick);
if (reason != "") {
        alert("Some fields need correction:\n\n"+reason);
        return false;
}
        frm.actionName.value=fld.name;
        frm.submit();
}
function validateschname(fld){
var error = "";
        if (fld.value == "") {
                fld.style.background = 'Yellow';
                error = "* you haven't entered school name.\n";
        } else {
                fld.style.background = 'White';
        }
        return error;
}
function validateschcode(fld){
var error = "";
        if (fld.value == "") {
                fld.style.background = 'Yellow';
                error = "* you haven't entered school code.\n";
        } else {
                fld.style.background = 'White';
        }
        return error;
}

function validateschnick(fld){
var error = "";
        if (fld.value == "") {
                fld.style.background = 'Yellow';
                error = "* you haven't entered nick name.\n";
        } else {
                fld.style.background = 'White';
        }
        return error;
}

function checkValue(frm,field)
{
        if(frm.selectFileNames.value!="")
        {
                frm.actionName.value=field.name;
                frm.submit();
        }
        else
        {
                alert("Please Select check box for School/Department Name !!");
        }
}


function addSelectedList(field,frm)
{
if(field.checked)
{
 frm.selectFileNames.value=frm.selectFileNames.value+field.name+"^";
}
                else
                {
                        var slFile,index,actualString,preString,postString;
                        actualString=frm.selectFileNames.value;
                        index=actualString.indexOf(field.name+"^",0);
                        preString=actualString.substring(0,index);
                        postString=actualString.substring(index+field.name.length+1);
                        actualString=preString+postString;
                        frm.selectFileNames.value=actualString;
                }
}
function popupWin(url,popupName)
        {
                Win1=window.open(url,popupName,"resizable=0,scrollbars=1,height=400,width=400");
        }
$(document).ready(function(){
$("#deldept").click(function() {
alert("Unable to delete Department, used in many institutes");
});
$("#delsch").click(function() {
alert("Unable to delete School, used in many institutes");
});
});

