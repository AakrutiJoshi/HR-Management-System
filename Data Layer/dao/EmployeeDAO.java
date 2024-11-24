package com.thinking.machines.hr.dl.dao;
import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.enums.*;
import java.util.*;
import java.math.*;
import java.text.*;
import java.io.*;
public class EmployeeDAO implements EmployeeDAOInterface
{
private final static String FILE_NAME ="employee.data";
public void add(EmployeeDTOInterface employeeDTO) throws DAOException
{
if(employeeDTO==null) throw new DAOException("Employee DTO is NULL");
String employeeId;
String name=employeeDTO.getName();
if(name==null) throw new DAOException("invalid name");
name=name.trim();
if(name.length()==0) throw new DAOException("name length is zero");
int employeeDesignationCode= employeeDTO.getDesignationCode();
if(employeeDesignationCode<=0) throw new DAOException("invalid Designation Code");
DesignationDAOInterface designationDAO=new DesignationDAO();
boolean isDesignationCodeExist=designationDAO.codeExists(employeeDesignationCode);
if(isDesignationCodeExist==false) throw new DAOException("Invalid DesignationCode"+employeeDesignationCode);
Date dateOfBirth=employeeDTO.getDateOfBirth();	
if(dateOfBirth==null) throw new DAOException("invalid date of birth");
char gender=employeeDTO.getGender();
if(gender==' ') throw new DAOException("Gender not set to Male/Female");
boolean isIndian=employeeDTO.getIsIndian();
BigDecimal basicSalary=employeeDTO.getBasicSalary();
if(basicSalary==null) throw new DAOException("basic salary is null");
if(basicSalary.signum()==-1) throw new DAOException("invalid basic salary");
String panNumber=employeeDTO.getPANNumber();
if(panNumber==null) throw new DAOException("PAN number is NULL");
panNumber=panNumber.trim();
if(panNumber.length()==0) throw new DAOException("PAN Number length is zero");
String aadharCardNumber=employeeDTO.getAadharCardNumber();
if(aadharCardNumber==null) throw new DAOException("Aadhar card number is NULL");
aadharCardNumber=aadharCardNumber.trim();
if(aadharCardNumber.length()==0) throw new DAOException("Aadhar Card Number length is zero");
try
{
File file=new File(FILE_NAME);
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
String lastGeneratedCodeString="";
String recordCountString="";
int lastGeneratedCode=10000000;
int recordCount=0;
if(randomAccessFile.length()==0)
{
lastGeneratedCodeString=String.format("%-10s","10000000");
randomAccessFile.writeBytes(lastGeneratedCodeString+"\n");
recordCountString=String.format("%-10s","0");
randomAccessFile.writeBytes(recordCountString+"\n");
}
else
{
lastGeneratedCode=Integer.parseInt(randomAccessFile.readLine().trim());
recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
}
String fPanNumber,fAadharCardNumber;
int i;
boolean panCardExists=false;
boolean AadharCardExists=false;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
for(i=1;i<=7;i++) randomAccessFile.readLine();
fPanNumber=randomAccessFile.readLine();
fAadharCardNumber=randomAccessFile.readLine();
if(panCardExists==false && fPanNumber.equalsIgnoreCase(panNumber))
{
panCardExists=true;
}
if(AadharCardExists==false && fAadharCardNumber.equalsIgnoreCase(aadharCardNumber))
{
AadharCardExists=true;
}
if(panCardExists && AadharCardExists) break;
}
if(panCardExists && AadharCardExists)
{
randomAccessFile.close();
throw new DAOException("PAN card number:"+panNumber+" Aadhar card Number:"+aadharCardNumber+" already Exists. ");
}
if(panCardExists )
{
randomAccessFile.close();
throw new DAOException("PAN card number:"+panNumber+" already Exists. ");
}
if(AadharCardExists)
{
randomAccessFile.close();
throw new DAOException("Aadhar card Number:"+aadharCardNumber+" already Exists. ");
}
lastGeneratedCode++;
employeeId="A"+lastGeneratedCode;
recordCount++;
randomAccessFile.writeBytes(employeeId+"\n");
randomAccessFile.writeBytes(name+"\n");
randomAccessFile.writeBytes(employeeDesignationCode+"\n");
SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");
String dateOfBirthString=sdf.format(dateOfBirth);
randomAccessFile.writeBytes(dateOfBirthString+"\n");
randomAccessFile.writeBytes(gender+"\n");
randomAccessFile.writeBytes(isIndian+"\n");
randomAccessFile.writeBytes(basicSalary.toPlainString()+"\n");
randomAccessFile.writeBytes(panNumber+"\n");
randomAccessFile.writeBytes(aadharCardNumber+"\n");
randomAccessFile.seek(0);
lastGeneratedCodeString=String.format("%-10s",lastGeneratedCode);
recordCountString=String.format("%-10s",recordCount);
randomAccessFile.writeBytes(lastGeneratedCodeString+"\n");
randomAccessFile.writeBytes(recordCountString+"\n");
randomAccessFile.close();
employeeDTO.setEmployeeId(employeeId);
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}

}
public void update(EmployeeDTOInterface employeeDTO) throws DAOException
{
if(employeeDTO==null) throw new DAOException("Employee DTO is NULL");
String employeeId=employeeDTO.getEmployeeId();
if(employeeId==null) throw new DAOException("Invalid EmployeeId:"+employeeId);
if(employeeId.length()==0) throw new DAOException("Length of employee ID is zero");
String name=employeeDTO.getName();
if(name==null) throw new DAOException("Invalid Name:"+name);
name=name.trim();
if(name.length()==0) throw new DAOException("Name length is zero");
int employeeDesignationCode= employeeDTO.getDesignationCode();
if(employeeDesignationCode<=0) throw new DAOException("Invalid Designation Code");
DesignationDAOInterface designationDAO=new DesignationDAO();
boolean isDesignationCodeExist=designationDAO.codeExists(employeeDesignationCode);
if(isDesignationCodeExist==false) throw new DAOException("Invalid DesignationCode"+employeeDesignationCode);
Date dateOfBirth=employeeDTO.getDateOfBirth();	
if(dateOfBirth==null) throw new DAOException("invalid date of birth");
char gender=employeeDTO.getGender();
if(gender==' ') throw new DAOException("Gender not set to Male/Female");
boolean isIndian=employeeDTO.getIsIndian();
BigDecimal basicSalary=employeeDTO.getBasicSalary();
if(basicSalary==null) throw new DAOException("basic salary is null");
if(basicSalary.signum()==-1) throw new DAOException("invalid basic salary");
String panNumber=employeeDTO.getPANNumber();
if(panNumber==null) throw new DAOException("PAN number is NULL");
panNumber=panNumber.trim();
if(panNumber.length()==0) throw new DAOException("PAN Number length is zero");
String aadharCardNumber=employeeDTO.getAadharCardNumber();
if(aadharCardNumber==null) throw new DAOException("Aadhar card number is NULL");
aadharCardNumber=aadharCardNumber.trim();
if(aadharCardNumber.length()==0) throw new DAOException("Aadhar Card Number length is zero");
try
{
File file=new File(FILE_NAME);
if(file.exists()==false) throw new DAOException("Invalid EmployeeId:"+employeeId);
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
throw new DAOException("Invalid Employee ID:"+employeeId);
}
randomAccessFile.readLine();
randomAccessFile.readLine();
String fEmployeeId;
String fPanNumber;
String fAadharCardNumber;
boolean employeeIdFound=false;
boolean panNumberFound=false;
boolean aadharCardNumberFound=false;
String panNumberFoundAgainstEmployeeId="";
String aadharCardNumberFoundAgainstEmployeeId="";
long foundAt=0;
int i;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
if(employeeIdFound==false) foundAt=randomAccessFile.getFilePointer();
fEmployeeId=randomAccessFile.readLine();
for(i=1;i<=6;i++) randomAccessFile.readLine();
fPanNumber=randomAccessFile.readLine();
fAadharCardNumber=randomAccessFile.readLine();
if(employeeIdFound==false && fEmployeeId.equalsIgnoreCase(employeeId))
{
employeeIdFound=true;
}
if(panNumberFound==false && fPanNumber.equalsIgnoreCase(panNumber))
{
panNumberFound=true;
panNumberFoundAgainstEmployeeId=fEmployeeId;
}
if(aadharCardNumberFound==false && fAadharCardNumber.equalsIgnoreCase(aadharCardNumber))
{
aadharCardNumberFound=true;
aadharCardNumberFoundAgainstEmployeeId=fEmployeeId;
}
if(employeeIdFound && panNumberFound && aadharCardNumberFound) break;
}
if(employeeIdFound==false)
{
randomAccessFile.close();
throw new DAOException("Invalid EmployeeId:"+employeeId);
}
if(panNumberFound==false)
{
randomAccessFile.close();
throw new DAOException("Invalid PanNumber:"+panNumber);
}
if(aadharCardNumberFound==false)
{
randomAccessFile.close();
throw new DAOException("Invalid Aadhar Card Number:"+aadharCardNumber);
}
boolean panNumberExists=false;
if(panNumberFound==true && panNumberFoundAgainstEmployeeId.equalsIgnoreCase(employeeId)==false)
{
panNumberExists=true;
}
boolean aadharCardNumberExists=false;
if(aadharCardNumberFound==true && aadharCardNumberFoundAgainstEmployeeId.equalsIgnoreCase(employeeId)==false)
{
aadharCardNumberExists=true;
}
if(panNumberExists && aadharCardNumberExists)
{
randomAccessFile.close();
throw new DAOException("Invalid Pan Number:"+panNumber+"and invalid AadharCard Number:"+aadharCardNumber);
}
if(panNumberExists)
{
randomAccessFile.close();
throw new DAOException("Invalid PanNumber:"+panNumber);
}
if(aadharCardNumberExists)
{
randomAccessFile.close();
throw new DAOException("Invalid Aadhar Card Number:"+aadharCardNumber);
}
randomAccessFile.seek(foundAt);
for(i=0;i<=9;i++) randomAccessFile.readLine();
File tmpFile=new File("tmp.tmp");
if(tmpFile.exists()) tmpFile.delete();
RandomAccessFile tmpRandomAccessFile=new RandomAccessFile(tmpFile,"rw");
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
tmpRandomAccessFile.writeBytes(randomAccessFile.readLine()+"\n");
}
randomAccessFile.seek(foundAt);
randomAccessFile.writeBytes(employeeId+"\n");
randomAccessFile.writeBytes(name+"\n");
randomAccessFile.writeBytes(employeeDesignationCode+"\n");
SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
String dateOfBirthString=sdf.format(dateOfBirth);
randomAccessFile.writeBytes(dateOfBirthString+"\n");
randomAccessFile.writeBytes(gender+"\n");
randomAccessFile.writeBytes(isIndian+"\n");
randomAccessFile.writeBytes(basicSalary.toPlainString()+"\n");
randomAccessFile.writeBytes(panNumber+"\n");
randomAccessFile.writeBytes(aadharCardNumber+"\n");
tmpRandomAccessFile.seek(0);
while(tmpRandomAccessFile.getFilePointer()<tmpRandomAccessFile.length())
{
randomAccessFile.writeBytes(tmpRandomAccessFile.readLine()+"\n");
}
randomAccessFile.setLength(randomAccessFile.getFilePointer());
tmpRandomAccessFile.setLength(0);
randomAccessFile.close();
tmpRandomAccessFile.close();
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
public void delete(String employeeId) throws DAOException
{
if(employeeId==null) throw new DAOException("Invalid EmployeeId:"+employeeId);
if(employeeId.length()==0) throw new DAOException("Length of employee ID is zero");
try
{
File file=new File(FILE_NAME);
if(file.exists()==false) throw new DAOException("Invalid EmployeeId:"+employeeId);
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
throw new DAOException("Invalid Employee ID:"+employeeId);
}
randomAccessFile.readLine();
int recordCount=Integer.parseInt(randomAccessFile.readLine().trim());
String fEmployeeId;
boolean employeeIdFound=false;
long foundAt=0;
int i;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
foundAt=randomAccessFile.getFilePointer();
fEmployeeId=randomAccessFile.readLine();
for(i=1;i<=8;i++) randomAccessFile.readLine();
if(fEmployeeId.equalsIgnoreCase(employeeId))
{
employeeIdFound=true;
break;
}
}
if(employeeIdFound==false)
{
randomAccessFile.close();
throw new DAOException("Invalid EmployeeId:"+employeeId);
}
File tmpFile=new File("tmp.tmp");
if(tmpFile.exists()) tmpFile.delete();
RandomAccessFile tmpRandomAccessFile=new RandomAccessFile(tmpFile,"rw");
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
tmpRandomAccessFile.writeBytes(randomAccessFile.readLine()+"\n");
}
randomAccessFile.seek(foundAt);
tmpRandomAccessFile.seek(0);
while(tmpRandomAccessFile.getFilePointer()<tmpRandomAccessFile.length())
{
randomAccessFile.writeBytes(tmpRandomAccessFile.readLine()+"\n");
}
randomAccessFile.setLength(randomAccessFile.getFilePointer());
recordCount--;
String recordCountString;
recordCountString=String.format("%-10s",recordCount);
randomAccessFile.seek(0);
randomAccessFile.readLine();
randomAccessFile.writeBytes(recordCountString+"\n");
tmpRandomAccessFile.setLength(0);
randomAccessFile.close();
tmpRandomAccessFile.close();
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
public Set<EmployeeDTOInterface> getAll() throws DAOException
{
Set<EmployeeDTOInterface> employee=new TreeSet<>();
try
{
File file=new File(FILE_NAME);
if(file.exists()==false)return employee;
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
return employee;
}
randomAccessFile.readLine();
randomAccessFile.readLine();
SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
EmployeeDTOInterface employeeDTO;
char fgender;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
employeeDTO=new EmployeeDTO();
employeeDTO.setEmployeeId(randomAccessFile.readLine());
employeeDTO.setName(randomAccessFile.readLine());
employeeDTO.setDesignationCode(Integer.parseInt(randomAccessFile.readLine()));
try
{
employeeDTO.setDateOfBirth(sdf.parse(randomAccessFile.readLine()));
}catch(ParseException pe)
{
//doNothing
}
fgender=randomAccessFile.readLine().charAt(0);
if(fgender=='M')
{
employeeDTO.setGender(GENDER.MALE);
}
if(fgender=='F')
{
employeeDTO.setGender(GENDER.FEMALE);
}
employeeDTO.setIsIndian(Boolean.parseBoolean(randomAccessFile.readLine()));
employeeDTO.setBasicSalary(new BigDecimal(randomAccessFile.readLine()));
employeeDTO.setPANNumber(randomAccessFile.readLine());
employeeDTO.setAadharCardNumber(randomAccessFile.readLine());
employee.add(employeeDTO);
}
randomAccessFile.close();
return employee;
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
public Set<EmployeeDTOInterface> getByDesignationCode(int designationCode) throws DAOException
{
Set<EmployeeDTOInterface> employee=new TreeSet<>();
DesignationDAOInterface designationDAO=new DesignationDAO();
if(designationDAO.codeExists(designationCode)==false) throw new DAOException("Invalid Designation Code:"+designationCode);
try
{
File file=new File(FILE_NAME);
if(file.exists()==false) throw new DAOException("Invalid designation Code :"+designationCode);
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
throw new DAOException("File is empty");
}
randomAccessFile.readLine();
randomAccessFile.readLine();
SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
EmployeeDTOInterface employeeDTO;
String fEmployeeId;
String fName;
int fDesignationCode,i;
char fgender;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
employeeDTO=new EmployeeDTO();
fEmployeeId=randomAccessFile.readLine();
fName=randomAccessFile.readLine();
fDesignationCode=Integer.parseInt(randomAccessFile.readLine());
if(fDesignationCode!=designationCode)
{
for(i=0;i<6;i++) randomAccessFile.readLine();
continue;
}
employeeDTO.setEmployeeId(fEmployeeId);
employeeDTO.setName(fName);
employeeDTO.setDesignationCode(fDesignationCode);
try
{
employeeDTO.setDateOfBirth(sdf.parse(randomAccessFile.readLine()));
}catch(ParseException pe)
{
//doNothing
}
fgender=randomAccessFile.readLine().charAt(0);
if(fgender=='M')
{
employeeDTO.setGender(GENDER.MALE);
}
if(fgender=='F')
{
employeeDTO.setGender(GENDER.FEMALE);
}
employeeDTO.setIsIndian(Boolean.parseBoolean(randomAccessFile.readLine()));
employeeDTO.setBasicSalary(new BigDecimal(randomAccessFile.readLine()));
employeeDTO.setPANNumber(randomAccessFile.readLine());
employeeDTO.setAadharCardNumber(randomAccessFile.readLine());
employee.add(employeeDTO);
}
randomAccessFile.close();
return employee;
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
public boolean isDesignationAlloted(int designationCode) throws DAOException
{
DesignationDAOInterface designationDAO=new DesignationDAO();
if(designationDAO.codeExists(designationCode)==false) throw new DAOException("Invalid Designation Code:"+designationCode);
try
{
File file=new File(FILE_NAME);
if(file.exists()==false) return false;
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
throw new DAOException("File is empty");
}
randomAccessFile.readLine();
randomAccessFile.readLine();
int i;
int fDesignationCode;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
randomAccessFile.readLine();
randomAccessFile.readLine();
fDesignationCode=Integer.parseInt(randomAccessFile.readLine());
if(fDesignationCode==designationCode)
{
randomAccessFile.close();
return true;
}
for(i=0;i<6;i++) randomAccessFile.readLine();
}
randomAccessFile.close();
return false;
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
public EmployeeDTOInterface getByEmployeeID(String employeeId) throws DAOException
{
if(employeeId==null) throw new DAOException("Invalid EmployeeId");
employeeId=employeeId.trim();
if(employeeId.length()==0) throw new DAOException("Employee ID length is zero");
try
{
File file=new File(FILE_NAME);
if(file.exists()==false)if(file.exists()==false) throw new DAOException("Invalid Employee Id :"+employeeId);
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
throw new DAOException("Invalid EmployeeID.:"+employeeId);
}
randomAccessFile.readLine();
randomAccessFile.readLine();
SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
EmployeeDTOInterface employeeDTO;
String fEmployeeId;
int i;
char fgender;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fEmployeeId=randomAccessFile.readLine();
if(fEmployeeId.equalsIgnoreCase(employeeId))
{
employeeDTO=new EmployeeDTO();
employeeDTO.setEmployeeId(fEmployeeId);
employeeDTO.setName(randomAccessFile.readLine());
employeeDTO.setDesignationCode(Integer.parseInt(randomAccessFile.readLine()));
try
{
employeeDTO.setDateOfBirth(sdf.parse(randomAccessFile.readLine()));
}catch(ParseException pe)
{
//doNothing
}
fgender=randomAccessFile.readLine().charAt(0);
if(fgender=='M')
{
employeeDTO.setGender(GENDER.MALE);
}
if(fgender=='F')
{
employeeDTO.setGender(GENDER.FEMALE);
}
employeeDTO.setIsIndian(Boolean.parseBoolean(randomAccessFile.readLine()));
employeeDTO.setBasicSalary(new BigDecimal(randomAccessFile.readLine()));
employeeDTO.setPANNumber(randomAccessFile.readLine());
employeeDTO.setAadharCardNumber(randomAccessFile.readLine());
randomAccessFile.close();
return employeeDTO;
}
for(i=1;i<=8;i++) randomAccessFile.readLine();
}
randomAccessFile.close();
throw new DAOException("Invalid EmployeeId.:"+employeeId);
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
public EmployeeDTOInterface getByPANNumber(String panNumber) throws DAOException
{
if(panNumber==null) throw new DAOException("Invalid PAN Number");
panNumber=panNumber.trim();
if(panNumber.length()==0) throw new DAOException("PAN Number length is zero");
try
{
File file=new File(FILE_NAME);
if(file.exists()==false) if(file.exists()==false) throw new DAOException("Invalid PAN Card Number :"+panNumber);
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
throw new DAOException("Invalid PAN Number.:"+panNumber);
}
randomAccessFile.readLine();
randomAccessFile.readLine();
EmployeeDTOInterface employeeDTO;
String fEmployeeId,fName,fPanNumber,fAadharCardNumber;
int fDesignationCode;
Date fDateOfBirth=new Date();
BigDecimal fBasicSalary;
Boolean fIsIndian;
int i;
SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
GENDER fGender;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fEmployeeId=randomAccessFile.readLine();
fName=randomAccessFile.readLine();
fDesignationCode=Integer.parseInt(randomAccessFile.readLine());
try
{
fDateOfBirth=sdf.parse(randomAccessFile.readLine());
}catch(ParseException pe)
{
//doNothing
}
fGender=(randomAccessFile.readLine().charAt(0)=='M')?GENDER.MALE:GENDER.FEMALE;
fIsIndian=Boolean.parseBoolean(randomAccessFile.readLine());
fBasicSalary=new BigDecimal(randomAccessFile.readLine());
fPanNumber=randomAccessFile.readLine();
fAadharCardNumber=randomAccessFile.readLine();
if(fPanNumber.equalsIgnoreCase(panNumber))
{
employeeDTO=new EmployeeDTO();
employeeDTO.setEmployeeId(fEmployeeId);
employeeDTO.setName(fName);
employeeDTO.setDesignationCode(fDesignationCode);
employeeDTO.setDateOfBirth(fDateOfBirth);
employeeDTO.setGender(fGender);
employeeDTO.setIsIndian(fIsIndian);
employeeDTO.setBasicSalary(fBasicSalary);
employeeDTO.setPANNumber(fPanNumber);
employeeDTO.setAadharCardNumber(fAadharCardNumber);
randomAccessFile.close();
return employeeDTO;
}
}
randomAccessFile.close();
throw new DAOException("Invalid PAN Number:"+panNumber);
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
public EmployeeDTOInterface getByAadharCardNumber(String aadharCardNumber) throws DAOException
{
if(aadharCardNumber==null) throw new DAOException("Invalid Aadhar card Number");
aadharCardNumber=aadharCardNumber.trim();
if(aadharCardNumber.length()==0) throw new DAOException("Aadhar card Number length is zero");
try
{
File file=new File(FILE_NAME);
if(file.exists()==false)if(file.exists()==false) throw new DAOException("Invalid Aadhar Card Number :"+aadharCardNumber);
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
throw new DAOException("Invalid Aadhar Card Number.:"+aadharCardNumber);
}
randomAccessFile.readLine();
randomAccessFile.readLine();
EmployeeDTOInterface employeeDTO;
String fEmployeeId,fName,fPanNumber,fAadharCardNumber;
int fDesignationCode;
Date fDateOfBirth=new Date();
BigDecimal fBasicSalary;
Boolean fIsIndian;
int i;
SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
GENDER fGender;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fEmployeeId=randomAccessFile.readLine();
fName=randomAccessFile.readLine();
fDesignationCode=Integer.parseInt(randomAccessFile.readLine());
try
{
fDateOfBirth=sdf.parse(randomAccessFile.readLine());
}catch(ParseException pe)
{
//doNothing
}
fGender=(randomAccessFile.readLine().charAt(0)=='M')?GENDER.MALE:GENDER.FEMALE;
fIsIndian=Boolean.parseBoolean(randomAccessFile.readLine());
fBasicSalary=new BigDecimal(randomAccessFile.readLine());
fPanNumber=randomAccessFile.readLine();
fAadharCardNumber=randomAccessFile.readLine();
if(fAadharCardNumber.equalsIgnoreCase(aadharCardNumber))
{
employeeDTO=new EmployeeDTO();
employeeDTO.setEmployeeId(fEmployeeId);
employeeDTO.setName(fName);
employeeDTO.setDesignationCode(fDesignationCode);
employeeDTO.setDateOfBirth(fDateOfBirth);
employeeDTO.setGender(fGender);
employeeDTO.setIsIndian(fIsIndian);
employeeDTO.setBasicSalary(fBasicSalary);
employeeDTO.setPANNumber(fPanNumber);
employeeDTO.setAadharCardNumber(fAadharCardNumber);
randomAccessFile.close();
return employeeDTO;
}
}
randomAccessFile.close();
throw new DAOException("Invalid Aadhar Card Number Number:"+aadharCardNumber);
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
public boolean employeeIdExists(String employeeId)throws DAOException
{
if(employeeId==null) return false;
employeeId=employeeId.trim();
if(employeeId.length()==0) return false;
try
{
File file=new File(FILE_NAME);
if(file.exists()==false) return false;
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
return false;
}
randomAccessFile.readLine();
randomAccessFile.readLine();
String fEmployeeId;
int i;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fEmployeeId=randomAccessFile.readLine();
if(fEmployeeId.equalsIgnoreCase(employeeId))
{
randomAccessFile.close();
return true;
}
for(i=1;i<=8;i++) randomAccessFile.readLine();
}
randomAccessFile.close();
return false;
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
public boolean PANNumberExists(String panNumber)throws DAOException
{
if(panNumber==null) return false;
panNumber=panNumber.trim();
if(panNumber.length()==0) return false;
try
{
File file=new File(FILE_NAME);
if(file.exists()==false)return false;
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
return false;
}
randomAccessFile.readLine();
randomAccessFile.readLine();
int i;
String fPanNumber;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
for(i=1;i<=7;i++) randomAccessFile.readLine();
fPanNumber=randomAccessFile.readLine();
randomAccessFile.readLine();
if(fPanNumber.equalsIgnoreCase(panNumber))
{
randomAccessFile.close();
return true;
}
}
randomAccessFile.close();
return false;
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
public boolean aadharCardNumberExists(String aadharCardNumber)throws DAOException
{
if(aadharCardNumber==null) return false;
aadharCardNumber=aadharCardNumber.trim();
if(aadharCardNumber.length()==0) return false;
try
{
File file=new File(FILE_NAME);
if(file.exists()==false)return false;
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
return false;
}
randomAccessFile.readLine();
randomAccessFile.readLine();
int i;
String faadharCardNumber;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
for(i=1;i<=8;i++) randomAccessFile.readLine();
faadharCardNumber=randomAccessFile.readLine();
if(faadharCardNumber.equalsIgnoreCase(aadharCardNumber))
{
randomAccessFile.close();
return true;
}
}
randomAccessFile.close();
return false;
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}


}
public int getCount()throws DAOException
{
try
{
File file=new File(FILE_NAME);
if(file.exists()==false)
{
throw new DAOException("Zero Records");
}
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
throw new DAOException("Zero Records");
}
String recordCountString;
int recordCount;
randomAccessFile.readLine();
recordCountString=randomAccessFile.readLine().trim();
recordCount=Integer.parseInt(recordCountString);
return recordCount;

}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}

}
public int getCountByDesignation(int designationCode)throws DAOException
{
try
{
if(new DesignationDAO().codeExists(designationCode)==false) throw new DAOException("Invalid designation Code:"+designationCode);
File file=new File(FILE_NAME);
if(file.exists()==false)
{
throw new DAOException("Zero Records");
}
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
throw new DAOException("Zero Records");
}
randomAccessFile.readLine();
randomAccessFile.readLine();
int fDesignationCode;
int recordCount=0;
int i;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
randomAccessFile.readLine();
randomAccessFile.readLine();
fDesignationCode=Integer.parseInt(randomAccessFile.readLine());
if(fDesignationCode==designationCode)
{
recordCount++;
}
for(i=1;i<=6;i++) randomAccessFile.readLine();
}
randomAccessFile.close();
return recordCount;
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}

}
}