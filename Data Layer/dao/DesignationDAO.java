package com.thinking.machines.hr.dl.dao;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.dto.*;
import java.io.*;
import java.util.*;
public class DesignationDAO implements DesignationDAOInterface
{
private final static String FILE_NAME ="designation.data";
public void add(DesignationDTOInterface designationDTO) throws DAOException
{
if(designationDTO==null) throw new DAOException("Designation is null");
String title=designationDTO.getTitle();
if(title==null) throw new DAOException("Designation is null");
title=title.trim();
if(title.length()==0) throw new DAOException("Length of designation is zero");
try
{
File file= new File(FILE_NAME);
RandomAccessFile randomAccessFile;
randomAccessFile=new RandomAccessFile(file,"rw");
int lastGeneratedCode=0;
int recordCount=0;
String lastGeneratedCodeString=" ";
String recordCountString=" ";
String fTitle;
if(randomAccessFile.length()==0)
{
lastGeneratedCodeString="0";
while(lastGeneratedCodeString.length()<10) lastGeneratedCodeString+=" ";
recordCountString="0";
while(recordCountString.length()<10) recordCountString+=" ";
randomAccessFile.writeBytes(lastGeneratedCodeString);
randomAccessFile.writeBytes("\n");
randomAccessFile.writeBytes(recordCountString);
randomAccessFile.writeBytes("\n");
}else
{
lastGeneratedCodeString=randomAccessFile.readLine().trim();
recordCountString=randomAccessFile.readLine().trim();
lastGeneratedCode=Integer.parseInt(lastGeneratedCodeString);
recordCount=Integer.parseInt(recordCountString);
}
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
randomAccessFile.readLine();
fTitle=randomAccessFile.readLine();
if(title.equalsIgnoreCase(fTitle))
{
randomAccessFile.close();
throw new DAOException("TITLE EXISTS.");
}
}
int code=lastGeneratedCode+1;
randomAccessFile.writeBytes(String.valueOf(code));
randomAccessFile.writeBytes("\n");
randomAccessFile.writeBytes(title);
randomAccessFile.writeBytes("\n");
designationDTO.setCode(code);
lastGeneratedCode++;
recordCount++;
lastGeneratedCodeString=(String.valueOf(lastGeneratedCode));
while(lastGeneratedCodeString.length()<10) lastGeneratedCodeString+=" ";
recordCountString=(String.valueOf(recordCount));
while(recordCountString.length()<10) recordCountString+=" ";
randomAccessFile.seek(0);
randomAccessFile.writeBytes(lastGeneratedCodeString);
randomAccessFile.writeBytes("\n");
randomAccessFile.writeBytes(recordCountString);
randomAccessFile.writeBytes("\n");
randomAccessFile.close();
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
public void update(DesignationDTOInterface designationDTO) throws DAOException
{
if(designationDTO==null) throw new DAOException("Designation is null");
int code=designationDTO.getCode();
if(code<=0) throw new DAOException("Invalid code");
String title=designationDTO.getTitle();
if(title==null) throw new DAOException("Designation is null");
title=title.trim();
if(title.length()==0) throw new DAOException("Length of designation is zero");
try
{
File file= new File(FILE_NAME);
if(!(file.exists())) throw new DAOException("Invalid Code"+code);
RandomAccessFile randomAccessFile;
randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
throw new DAOException("Invalid Code"+code);
}
int fcode=0;
String fTitle=" ";
randomAccessFile.readLine();
randomAccessFile.readLine();
boolean found=false;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fcode=Integer.parseInt(randomAccessFile.readLine());
fTitle=randomAccessFile.readLine();
if(fcode==code)
{
found=true;
break;
}
}
if(found==false)
{
randomAccessFile.close();
throw new DAOException("Invalid code"+code);
}
randomAccessFile.seek(0);
randomAccessFile.readLine();
randomAccessFile.readLine();
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fcode=Integer.parseInt(randomAccessFile.readLine());
fTitle=randomAccessFile.readLine();
if(fcode!=code && title.equalsIgnoreCase(fTitle)==true)
{
randomAccessFile.close();
throw new DAOException("Title Exists");
}
}
File tmpFile=new File("tmp.data");
RandomAccessFile tmpRandomAccessFile=new RandomAccessFile(tmpFile,"rw");
randomAccessFile.seek(0);
tmpRandomAccessFile.writeBytes(randomAccessFile.readLine());
tmpRandomAccessFile.writeBytes("\n");
tmpRandomAccessFile.writeBytes(randomAccessFile.readLine());
tmpRandomAccessFile.writeBytes("\n");
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fcode=Integer.parseInt(randomAccessFile.readLine());
fTitle=randomAccessFile.readLine();
if(fcode==code)
{
tmpRandomAccessFile.writeBytes(String.valueOf(code));
tmpRandomAccessFile.writeBytes("\n");
tmpRandomAccessFile.writeBytes(title);
tmpRandomAccessFile.writeBytes("\n");
continue;
}
tmpRandomAccessFile.writeBytes(String.valueOf(fcode));
tmpRandomAccessFile.writeBytes("\n");
tmpRandomAccessFile.writeBytes(fTitle);
tmpRandomAccessFile.writeBytes("\n");
}
randomAccessFile.seek(0);
tmpRandomAccessFile.seek(0);
while(tmpRandomAccessFile.getFilePointer()<tmpRandomAccessFile.length())
{
randomAccessFile.writeBytes(tmpRandomAccessFile.readLine());
randomAccessFile.writeBytes("\n");
}
randomAccessFile.setLength(tmpRandomAccessFile.length());
tmpRandomAccessFile.setLength(0);
tmpRandomAccessFile.close();
randomAccessFile.close();
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
public void delete(int code) throws DAOException
{
try
{
File file= new File(FILE_NAME);
if(!(file.exists())) throw new DAOException("Invalid Code"+code);
RandomAccessFile randomAccessFile;
randomAccessFile=new RandomAccessFile(file,"rw");
if(randomAccessFile.length()==0)
{
randomAccessFile.close();
throw new DAOException("Invalid Code"+code);
}
int fcode=0;
String fTitle="";
randomAccessFile.readLine();
String recordCountString=randomAccessFile.readLine().trim();
int recordCount=Integer.parseInt(recordCountString);
boolean found=false;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fcode=Integer.parseInt(randomAccessFile.readLine());
fTitle=randomAccessFile.readLine();
if(fcode==code)
{
found =true;
break;
}
}
if(found==false)
{
randomAccessFile.close();
throw new DAOException("Invalid code"+code);
}
if(new EmployeeDAO().isDesignationAlloted(code))
{
randomAccessFile.close();
throw new DAOException("Employee exists with Designation:"+fTitle);
}
File tmpFile=new File("tmp.data");
if(tmpFile.exists()) tmpFile.delete();
RandomAccessFile tmpRandomAccessFile=new RandomAccessFile(tmpFile,"rw");
randomAccessFile.seek(0);
tmpRandomAccessFile.writeBytes(randomAccessFile.readLine());
tmpRandomAccessFile.writeBytes("\n");
tmpRandomAccessFile.writeBytes(randomAccessFile.readLine());
tmpRandomAccessFile.writeBytes("\n");
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fcode=Integer.parseInt(randomAccessFile.readLine());
fTitle=randomAccessFile.readLine();
if(fcode!=code)
{
tmpRandomAccessFile.writeBytes(String.valueOf(fcode));
tmpRandomAccessFile.writeBytes("\n");
tmpRandomAccessFile.writeBytes(fTitle);
tmpRandomAccessFile.writeBytes("\n");
}
}
randomAccessFile.seek(0);
tmpRandomAccessFile.seek(0);
recordCount--;
randomAccessFile.writeBytes(tmpRandomAccessFile.readLine());
randomAccessFile.writeBytes("\n");
tmpRandomAccessFile.readLine();
recordCountString=String.valueOf(recordCount);
while(recordCountString.length()<10) recordCountString+=" ";
randomAccessFile.writeBytes(recordCountString);
randomAccessFile.writeBytes("\n");
while(tmpRandomAccessFile.getFilePointer()<tmpRandomAccessFile.length())
{
randomAccessFile.writeBytes(tmpRandomAccessFile.readLine());
randomAccessFile.writeBytes("\n");
}
randomAccessFile.setLength(tmpRandomAccessFile.length());
tmpRandomAccessFile.setLength(0);
tmpRandomAccessFile.close();
randomAccessFile.close();

}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}

}
public Set<DesignationDTOInterface> getAll() throws DAOException
{
try
{
File file=new File(FILE_NAME);
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
randomAccessFile.readLine();
randomAccessFile.readLine();
int fcode=0;
String ftitle=" ";
Set<DesignationDTOInterface> designations=new TreeSet<>();
DesignationDTOInterface designationDTO;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fcode=Integer.parseInt(randomAccessFile.readLine());
ftitle=randomAccessFile.readLine();
designationDTO=new DesignationDTO();
designationDTO.setCode(fcode);
designationDTO.setTitle(ftitle);
designations.add(designationDTO);
}
randomAccessFile.close();
return designations;
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
public DesignationDTOInterface getByCode(int code) throws DAOException
{
try
{
File file=new File(FILE_NAME);
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
randomAccessFile.readLine();
randomAccessFile.readLine();
int fcode=0;
String ftitle=" ";
boolean found=false;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fcode=Integer.parseInt(randomAccessFile.readLine());
ftitle=randomAccessFile.readLine();
if(fcode==code)
{
found=true;
break;
}
}
if(found==false)
{
randomAccessFile.close();
throw new DAOException("invalid code:"+code);
}
DesignationDTOInterface designationDTO;
designationDTO=new DesignationDTO();
designationDTO.setCode(code);
designationDTO.setTitle(ftitle);
randomAccessFile.close();
return designationDTO;
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
public DesignationDTOInterface getByTitle(String title) throws DAOException
{
try
{
File file=new File(FILE_NAME);
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
randomAccessFile.readLine();
randomAccessFile.readLine();
int fcode=0;
String ftitle=" ";
boolean found=false;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fcode=Integer.parseInt(randomAccessFile.readLine());
ftitle=randomAccessFile.readLine();
if(title.equalsIgnoreCase(ftitle))
{
found=true;
break;
}
}
if(found==false)
{
randomAccessFile.close();
throw new DAOException("invalid title:"+title);
}
DesignationDTOInterface designationDTO;
designationDTO=new DesignationDTO();
designationDTO.setCode(fcode);
designationDTO.setTitle(title);
randomAccessFile.close();
return designationDTO;
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
public boolean codeExists(int code)throws DAOException
{
try
{
File file=new File(FILE_NAME);
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
randomAccessFile.readLine();
randomAccessFile.readLine();
int fcode=0;
String ftitle=" ";
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fcode=Integer.parseInt(randomAccessFile.readLine());
ftitle=randomAccessFile.readLine();
if(fcode==code)
{
randomAccessFile.close();
return true;
}
}
randomAccessFile.close();
throw new DAOException("Invalid code:"+code);
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
public boolean titleExists(String title)throws DAOException
{
try
{
File file=new File(FILE_NAME);
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
randomAccessFile.readLine();
randomAccessFile.readLine();
int fcode;
String ftitle;
while(randomAccessFile.getFilePointer()<randomAccessFile.length())
{
fcode=Integer.parseInt(randomAccessFile.readLine());
ftitle=randomAccessFile.readLine();
if(title.equalsIgnoreCase(ftitle))
{
randomAccessFile.close();
return true;
}
}
randomAccessFile.close();
throw new DAOException("Invalid title:"+title);
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
RandomAccessFile randomAccessFile=new RandomAccessFile(file,"rw");
randomAccessFile.readLine();
String recordCountString=randomAccessFile.readLine().trim();
int recordCount=Integer.parseInt(recordCountString);
randomAccessFile.close();
return recordCount;
}catch(IOException ioException)
{
throw new DAOException(ioException.getMessage());
}
}
}