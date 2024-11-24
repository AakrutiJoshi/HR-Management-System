package com.thinking.machines.hr.bl.manager;
import com.thinking.machines.hr.bl.interfaces.manager.*;
import com.thinking.machines.hr.bl.interfaces.pojo.*;
import com.thinking.machines.hr.bl.pojo.*;
import com.thinking.machines.hr.bl.exceptions.*;
import com.thinking.machines.hr.dl.interfaces.dto.*;
import com.thinking.machines.hr.dl.interfaces.dao.*;
import com.thinking.machines.hr.dl.dto.*;
import com.thinking.machines.hr.dl.dao.*;
import com.thinking.machines.hr.dl.exceptions.*;
import java.util.*;
public class DesignationManager implements DesignationManagerInterface
{
private Map<Integer,DesignationInterface> codeWiseDesignationMap;
private Map<String,DesignationInterface> titleWiseDesignationMap;
private Set<DesignationInterface> designationSet;
private DesignationManager() throws BLException
{
populateDataStructure();
}
void populateDataStructure() throws BLException
{
this.codeWiseDesignationMap=new HashMap<>();
this.titleWiseDesignationMap=new HashMap<>();
this.designationSet=new TreeSet<>();
try
{
Set<DesignationDTOInterface> dlDesignations;
dlDesignations=new DesignationDAO().getAll();
DesignationInterface designation;
for(DesignationDTOInterface dlDesignation:dlDesignations)
{
designation=new Designation();
designation.setCode(dlDesignation.getCode());
designation.setTitle(dlDesignation.getTitle());
this.codeWiseDesignationMap.put(new Integer(designation.getCode()),designation);
this.titleWiseDesignationMap.put(designation.getTitle().toUpperCase(),designation);
this.designationSet.add(designation);
}
}catch(DAOException daoException)
{
BLException blException=new BLException();
blException.setGenericException(daoException.getMessage());
throw blException;
}
}
private static DesignationManager designationManager=null;
public static DesignationManagerInterface getDesignationManager() throws BLException
{
if(designationManager==null) designationManager= new DesignationManager();
return designationManager;
}
public void addDesignation(DesignationInterface designation) throws BLException
{
BLException blException=new BLException();
if(designation==null)
{
blException.setGenericException("Designation Required");
throw blException;
}
int code=designation.getCode();
String title=designation.getTitle();
if(code!=0)
{
blException.addException("code","Code should be zero");
}
if(title==null)
{
blException.addException("title","Title required");
title="";
}
else
{
title=title.trim();
if(title.length()==0)
{
blException.addException("title","Title required");
}
}
if(title.length()>0)
{
if(this.titleWiseDesignationMap.containsKey(title.toUpperCase()))
{
blException.addException("title","Title :"+title+" exists");
}
}
if(blException.hasExceptions())
{
throw blException;
}
try
{
DesignationDTOInterface designationDTO=new DesignationDTO();
designationDTO.setTitle(title);
DesignationDAOInterface designationDAO=new DesignationDAO();
designationDAO.add(designationDTO);
code=designationDTO.getCode();
designation.setCode(code);
Designation dsDesignation=new Designation();
dsDesignation.setCode(code);
dsDesignation.setTitle(title);
this.codeWiseDesignationMap.put(new Integer(code),dsDesignation);
this.titleWiseDesignationMap.put(title,dsDesignation);
this.designationSet.add(dsDesignation);
}catch(DAOException daoException)
{
blException.setGenericException(daoException.getMessage());
}
}
public void updateDesignation(DesignationInterface designation) throws BLException
{
BLException blException=new BLException();
if(designation==null)
{
blException.setGenericException("Designation Required");
throw blException;
}
int code=designation.getCode();
String title=designation.getTitle();
if(code<=0)
{
blException.addException("code","Invalid Code:"+code);
}
if(code>0)
{
if(!(this.codeWiseDesignationMap.containsKey(code)))
{
blException.addException("code","Invalid Code:"+code);
throw blException;
}
}
if(title==null)
{
blException.addException("title","Title required");
title="";
}
else
{
title=title.trim();
if(title.length()==0)
{
blException.addException("title","Title required");
}
}
if(title.length()>0)
{
DesignationInterface d=this.titleWiseDesignationMap.get(title.toUpperCase());
if(d!=null && d.getCode()!=code)
{
blException.addException("title","Title :"+title+"Exists.");
}
}
if(blException.hasExceptions())
{
throw blException;
}
try
{
DesignationInterface dsDesignation=codeWiseDesignationMap.get(code);
DesignationDTOInterface designationDTO=new DesignationDTO();
designationDTO.setCode(code);
designationDTO.setTitle(title);
DesignationDAOInterface designationDAO= new DesignationDAO();
designationDAO.update(designationDTO);
//remove old title designation from map
this.codeWiseDesignationMap.remove(code);
this.titleWiseDesignationMap.remove(dsDesignation.getTitle().toUpperCase());
this.designationSet.remove(dsDesignation);
//update the DS object
dsDesignation.setTitle(title);
//Update the DS
this.codeWiseDesignationMap.put(code,dsDesignation);
this.titleWiseDesignationMap.put(title.toUpperCase(),dsDesignation);
this.designationSet.add(dsDesignation);
}catch(DAOException daoException)
{
blException.setGenericException(daoException.getMessage());
}
}
public void removeDesignation(int code) throws BLException
{
BLException blException=new BLException();
if(code<=0)
{
blException.addException("code","Invalid Code:"+code);
throw blException;
}
if(code>0)
{
if(!(this.codeWiseDesignationMap.containsKey(code)))
{
blException.addException("code","Invalid Code:"+code);
throw blException;
}
}
try
{
DesignationInterface dsDesignation=new Designation();
dsDesignation=codeWiseDesignationMap.get(code);
DesignationDAOInterface designationDAO= new DesignationDAO();
designationDAO.delete(code);
//remove old one designation from map
codeWiseDesignationMap.remove(code);
titleWiseDesignationMap.remove(dsDesignation.getTitle().toUpperCase());
designationSet.remove(dsDesignation);
}catch(DAOException daoException)
{
blException.setGenericException(daoException.getMessage());
throw blException;
}
}
public DesignationInterface getDesignationByCode(int code) throws BLException
{
DesignationInterface designation=new Designation();
designation=codeWiseDesignationMap.get(code);
if(designation==null)
{
BLException blException=new BLException();
blException.addException("code","Invalid code:"+code);
throw blException;
}
return designation;
}
DesignationInterface getDSDesignationByCode(int code)
{
 DesignationInterface designation;
designation =codeWiseDesignationMap.get(code);
return designation;
}

public DesignationInterface getDesignationByTitle(String title) throws BLException
{
DesignationInterface designation=new Designation();
designation=titleWiseDesignationMap.get(title.toUpperCase());
if(designation==null)
{
BLException blException=new BLException();
blException.addException("title","Invalid Designation:"+title);
throw blException;
}
return designation;
}
public int getDesignationCount()
{
return this.designationSet.size();
}
public boolean designationCodeExists(int code )
{
return this.codeWiseDesignationMap.containsKey(code);
}
public boolean designationTitleExists(String title)
{
return this.titleWiseDesignationMap.containsKey(title.toUpperCase());
}
public Set<DesignationInterface> getDesignations() throws BLException
{
Set<DesignationInterface> designations=new TreeSet<>();
for(DesignationInterface designation:designationSet)
{
DesignationInterface dsDesignation=new Designation();
dsDesignation.setCode(designation.getCode());
dsDesignation.setTitle(designation.getTitle());
designations.add(dsDesignation);
}
return designations;
}
}