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
import java.math.*;
import com.thinking.machines.enums.*;
import java.text.*;
public class EmployeeManager implements EmployeeManagerInterface
{
private Map<String,EmployeeInterface> employeeIdWiseEmployeesMap;
private Map<String,EmployeeInterface> panNumberWiseEmployeesMap;
private Map<String,EmployeeInterface> aadharCardNumberWiseEmployeesMap;
private Set<EmployeeInterface> employeesSet;
private Map<Integer,Set<EmployeeInterface>> designationCodeWiseEmployeesMap;
private static EmployeeManager employeeManager=null;
private EmployeeManager() throws BLException
{
populateDataStructure();
}
void populateDataStructure() throws BLException
{
this.employeeIdWiseEmployeesMap =new HashMap<>();
this.panNumberWiseEmployeesMap=new HashMap<>();
this.aadharCardNumberWiseEmployeesMap=new HashMap<>();
this.employeesSet=new TreeSet<>();
this.designationCodeWiseEmployeesMap=new HashMap<>();
try
{
Set<EmployeeDTOInterface> dlEmployees;
dlEmployees=new EmployeeDAO().getAll();
EmployeeInterface employee;
DesignationManagerInterface designationManager=DesignationManager.getDesignationManager();
DesignationInterface designation;
Set<EmployeeInterface> ets;
for(EmployeeDTOInterface dlEmployee:dlEmployees)
{
employee=new Employee();
employee.setEmployeeId(dlEmployee.getEmployeeId());
employee.setName(dlEmployee.getName());
designation=designationManager.getDesignationByCode(dlEmployee.getDesignationCode());
employee.setDesignation(designation);
employee.setDateOfBirth(dlEmployee.getDateOfBirth());
if(dlEmployee.getGender()=='M')
{
employee.setGender(GENDER.MALE);
}
else
{
employee.setGender(GENDER.FEMALE);
}
employee.setIsIndian(dlEmployee.getIsIndian());
employee.setBasicSalary(dlEmployee.getBasicSalary());
employee.setPANNumber(dlEmployee.getPANNumber());
employee.setAadharCardNumber(dlEmployee.getAadharCardNumber());
this.employeeIdWiseEmployeesMap.put(employee.getEmployeeId().toUpperCase(),employee);
this.panNumberWiseEmployeesMap.put(employee.getPANNumber().toUpperCase(),employee);
this.aadharCardNumberWiseEmployeesMap.put(employee.getAadharCardNumber().toUpperCase(),employee);
this.employeesSet.add(employee);
ets=this.designationCodeWiseEmployeesMap.get(designation.getCode());
if(ets==null)
{
ets=new TreeSet<>();
ets.add(employee);
designationCodeWiseEmployeesMap.put(new Integer(designation.getCode()),ets);
}
else
{
ets.add(employee);
}
}
}catch(DAOException daoException)
{
BLException blException=new BLException();
blException.setGenericException(daoException.getMessage());
throw blException;
}
}
public static EmployeeManagerInterface getEmployeeManager() throws BLException
{
if(employeeManager==null) employeeManager=new  EmployeeManager();
return employeeManager;
}
public void addEmployee(EmployeeInterface employee) throws BLException
{
BLException blException =new BLException();
String employeeId=employee.getEmployeeId();
String name=employee.getName();
DesignationInterface designation=employee.getDesignation();
Date dateOfBirth=employee.getDateOfBirth();
char gender=employee.getGender();
Boolean isIndian=employee.getIsIndian();
BigDecimal basicSalary=employee.getBasicSalary();
String panNumber=employee.getPANNumber();
String aadharCardNumber=employee.getAadharCardNumber();
if(employeeId!=null)
{
employeeId=employeeId.trim();
if(employeeId.length()>0) blException.addException("employeeId","Employee Id is auto generated");
}
if(name==null)
{
blException.addException("name","name required.");
}
else
{
name=name.trim();
if(name.length()==0) blException.addException("name","name required.");
}
int designationCode=0;
DesignationManagerInterface designationManager=DesignationManager.getDesignationManager();
if(designation==null)
{
blException.addException("designation","designation required.");
}else
{
designationCode=designation.getCode();
if(designationManager.designationCodeExists(designationCode)==false) blException.addException("designation","Invalid Designation Code.");
}
if(dateOfBirth==null)
{
blException.addException("dateOfBirth","Date of birth required.");
}
if(gender==' ')
{
blException.addException("gender","Gender required.");
}
if(basicSalary==null)
{
blException.addException("basicSalary","Basic salary required.");
}
else
{
if(basicSalary.signum()==-1)
{
blException.addException("basicSalary","Basic salary can not be negative");
}
}

if(panNumber==null)
{
blException.addException("panNumber"," Pan Number required.");
}
else
{
panNumber=panNumber.trim();
if(panNumber.length()==0)
{
blException.addException("panNumber"," Pan number required.");
}
}

if(aadharCardNumber==null)
{
blException.addException("aadharCardNumber"," Aadhar Card Number required.");
}
else
{
aadharCardNumber=aadharCardNumber.trim();
if(aadharCardNumber.length()==0)
{
blException.addException("aadharCardNumber","Aadhar Card number required.");
}
}

if(panNumber!=null && panNumber.length()>=0)
{
if(panNumberWiseEmployeesMap.containsKey(panNumber.toUpperCase()))
{
blException.addException("panNumber","Pan number "+panNumber+" Exists.");
}
}
if(aadharCardNumber!=null && aadharCardNumber.length()>=0)
{
if(aadharCardNumberWiseEmployeesMap.containsKey(aadharCardNumber.toUpperCase()))
{
blException.addException("aadharCardNumber","Aadhar card number "+aadharCardNumber+" Exists.");
}
}
if(blException.hasExceptions())
{
throw blException;
}
try
{
EmployeeDTOInterface dlEmployee=new EmployeeDTO();
EmployeeDAOInterface employeeDAO=new EmployeeDAO();
dlEmployee.setName(name);
dlEmployee.setDesignationCode(designation.getCode());
dlEmployee.setDateOfBirth(dateOfBirth);
dlEmployee.setGender((gender=='M')? GENDER.MALE:GENDER.FEMALE);
dlEmployee.setIsIndian(isIndian);
dlEmployee.setBasicSalary(basicSalary);
dlEmployee.setPANNumber(panNumber);
dlEmployee.setAadharCardNumber(aadharCardNumber);
employeeDAO.add(dlEmployee);
employee.setEmployeeId(dlEmployee.getEmployeeId());
EmployeeInterface dsEmployee=new Employee();
dsEmployee.setEmployeeId(employee.getEmployeeId());
dsEmployee.setName(employee.getName());
dsEmployee.setDesignation(((DesignationManager)designationManager).getDSDesignationByCode(designation.getCode()));
dsEmployee.setDateOfBirth((Date)dateOfBirth.clone());
dsEmployee.setGender((gender=='M')? GENDER.MALE:GENDER.FEMALE);
dsEmployee.setIsIndian(isIndian);
dsEmployee.setBasicSalary(basicSalary);
dsEmployee.setPANNumber(panNumber);
dsEmployee.setAadharCardNumber(aadharCardNumber);
this.employeeIdWiseEmployeesMap.put(dsEmployee.getEmployeeId().toUpperCase(),dsEmployee);
this.panNumberWiseEmployeesMap.put(panNumber.toUpperCase(),dsEmployee);
this.aadharCardNumberWiseEmployeesMap.put(aadharCardNumber.toUpperCase(),dsEmployee);
this.employeesSet.add(dsEmployee);

Set<EmployeeInterface> ets;
ets=this.designationCodeWiseEmployeesMap.get(dsEmployee.getDesignation().getCode());
if(ets==null)
{
ets=new TreeSet<>();
ets.add(dsEmployee);
designationCodeWiseEmployeesMap.put(new Integer(dsEmployee.getDesignation().getCode()),ets);
}
else
{
ets.add(dsEmployee);
}



}catch(DAOException daoException)
{
blException.setGenericException(daoException.getMessage());
throw blException;
}
}
public void updateEmployee(EmployeeInterface employee) throws BLException
{
BLException blException =new BLException();
String employeeId=employee.getEmployeeId();
String name=employee.getName();
DesignationInterface designation=employee.getDesignation();
Date dateOfBirth=employee.getDateOfBirth();
char gender=employee.getGender();
Boolean isIndian=employee.getIsIndian();
BigDecimal basicSalary=employee.getBasicSalary();
String panNumber=employee.getPANNumber();
String aadharCardNumber=employee.getAadharCardNumber();
 if(employeeId==null)
{
blException.setGenericException("Employee Id required.");
}
else
{
employeeId=employeeId.trim();
if(employeeId.length()<=0) blException.setGenericException("Employee Id required.");
}
if(!this.employeeIdWiseEmployeesMap.containsKey(employeeId.toUpperCase())) 
{
blException.setGenericException("Invalid Employee Id :"+employeeId);
}
if(blException.hasGenericException())
{
throw blException;
}
if(name==null)
{
blException.addException("name","name required.");
}
else
{
name=name.trim();
if(name.length()==0) blException.addException("name","name required.");
}
int designationCode=0;
DesignationManagerInterface designationManager=DesignationManager.getDesignationManager();
if(designation==null)
{
blException.addException("designation","designation required.");
}
designationCode=designation.getCode();
if(dateOfBirth==null)
{
blException.addException("dateOfBirth","Date of birth required.");
}
if(gender==' ')
{
blException.addException("gender","Gender required.");
}
if(basicSalary==null)
{
blException.addException("basicSalary","Basic salary required.");
}
else
{
if(basicSalary.signum()==-1)
{
blException.addException("basicSalary","Basic salary can not be negative");
}
}

if(panNumber==null)
{
blException.addException("panNumber"," Pan Number required.");
}
else
{
panNumber=panNumber.trim();
if(panNumber.length()==0)
{
blException.addException("panNumber"," Pan number required.");
}
}
if(aadharCardNumber==null)
{
blException.addException("aadharCardNumber"," Aadhar Card Number required.");
}
else
{
aadharCardNumber=aadharCardNumber.trim();
if(aadharCardNumber.length()==0)
{
blException.addException("aadharCardNumber","Aadhar Card number required.");
}
}
if(panNumber!=null && panNumber.length()>=0)
{
if(panNumberWiseEmployeesMap.containsKey(panNumber.toUpperCase()))
{
EmployeeInterface emp=panNumberWiseEmployeesMap.get(panNumber.toUpperCase());
String eId= emp.getEmployeeId();
if(eId.equalsIgnoreCase(employeeId)==false)
{
blException.addException("panNumber","Pan number "+panNumber+" Exists.");
}
}
}
if(aadharCardNumber!=null && aadharCardNumber.length()>=0)
{
if(aadharCardNumberWiseEmployeesMap.containsKey(aadharCardNumber.toUpperCase()))
{
EmployeeInterface emp=aadharCardNumberWiseEmployeesMap.get(aadharCardNumber.toUpperCase());
String eId= emp.getEmployeeId();
if(eId.equalsIgnoreCase(employeeId)==false)
{
blException.addException("aadharCardNumber","aadharCard number "+aadharCardNumber+" Exists.");
}
}
}
if(blException.hasExceptions())
{
throw blException;
}
try
{
EmployeeInterface dsEmployee;
dsEmployee=employeeIdWiseEmployeesMap.get(employeeId.toUpperCase());
String oldPANNumber=dsEmployee.getPANNumber();
String oldAadharCardNumber=dsEmployee.getAadharCardNumber();
int oldDesignationCode=dsEmployee.getDesignation().getCode();
EmployeeDAOInterface employeeDAO=new EmployeeDAO();
EmployeeDTOInterface dlEmployee=new EmployeeDTO();
dlEmployee.setEmployeeId(dsEmployee.getEmployeeId());
dlEmployee.setName(name);
dlEmployee.setDesignationCode(designation.getCode());
dlEmployee.setDateOfBirth(dateOfBirth);
dlEmployee.setGender((gender=='M')? GENDER.MALE:GENDER.FEMALE);
dlEmployee.setIsIndian(isIndian);
dlEmployee.setBasicSalary(basicSalary);
dlEmployee.setPANNumber(panNumber);
dlEmployee.setAadharCardNumber(aadharCardNumber);
employeeDAO.update(dlEmployee);
dsEmployee.setName(employee.getName());
dsEmployee.setDesignation(((DesignationManager)designationManager).getDSDesignationByCode(designation.getCode()));
dsEmployee.setDateOfBirth((Date)dateOfBirth.clone());
dsEmployee.setGender((gender=='M')? GENDER.MALE:GENDER.FEMALE);
dsEmployee.setIsIndian(isIndian);
dsEmployee.setBasicSalary(basicSalary);
dsEmployee.setPANNumber(panNumber);
dsEmployee.setAadharCardNumber(aadharCardNumber);
//remove the old data from data structure
this.employeesSet.remove(dsEmployee);
this.employeeIdWiseEmployeesMap.remove(employeeId.toUpperCase());
this.panNumberWiseEmployeesMap.remove(oldPANNumber.toUpperCase());
this.aadharCardNumberWiseEmployeesMap.remove(oldAadharCardNumber.toUpperCase());
//Set New Data in data structure
this.employeesSet.add(dsEmployee);
this.employeeIdWiseEmployeesMap.put(dsEmployee.getEmployeeId().toUpperCase(),dsEmployee);
this.panNumberWiseEmployeesMap.put(panNumber.toUpperCase(),dsEmployee);
this.aadharCardNumberWiseEmployeesMap.put(aadharCardNumber.toUpperCase(),dsEmployee);
if(oldDesignationCode!=dsEmployee.getDesignation().getCode())
{
Set<EmployeeInterface> ets;
ets=this.designationCodeWiseEmployeesMap.get(oldDesignationCode);
ets.remove(dsEmployee);
ets=this.designationCodeWiseEmployeesMap.get(dsEmployee.getDesignation().getCode());
if(ets==null)
{
ets=new TreeSet<>();
ets.add(dsEmployee);
designationCodeWiseEmployeesMap.put(new Integer(dsEmployee.getDesignation().getCode()),ets);
}
else
{
ets.add(dsEmployee);
}
}

}catch(DAOException daoException)
{
blException.setGenericException(daoException.getMessage());
throw blException;
}


}
public void removeEmployee(String employeeId) throws BLException
{

BLException blException =new BLException();
 if(employeeId==null)
{
blException.setGenericException("Employee Id required.");
}
else
{
employeeId=employeeId.trim();
if(employeeId.length()<=0) blException.setGenericException("Employee Id required.");
}
if(!this.employeeIdWiseEmployeesMap.containsKey(employeeId.toUpperCase())) 
{
blException.setGenericException("Invalid Employee Id :"+employeeId);
}
if(blException.hasGenericException())
{
throw blException;
}
try
{
EmployeeInterface dsEmployee=employeeIdWiseEmployeesMap.get(employeeId.toUpperCase());

EmployeeDAOInterface employeeDAO=new EmployeeDAO();
employeeDAO.delete(dsEmployee.getEmployeeId());

this.employeesSet.remove(dsEmployee);
this.employeeIdWiseEmployeesMap.remove(employeeId.toUpperCase());
this.panNumberWiseEmployeesMap.remove(dsEmployee.getPANNumber().toUpperCase());
this.aadharCardNumberWiseEmployeesMap.remove(dsEmployee.getAadharCardNumber().toUpperCase());

Set<EmployeeInterface> ets;
ets=this.designationCodeWiseEmployeesMap.get(dsEmployee.getDesignation().getCode());
ets.remove(dsEmployee);


}catch(DAOException daoException)
{
blException.setGenericException(daoException.getMessage());
throw blException;
}
}
public EmployeeInterface getEmployeeByEmployeeId(String employeeId) throws BLException
{

BLException blException =new BLException();
 if(employeeId==null)
{
blException.setGenericException("Employee Id required.");
}
else
{
employeeId=employeeId.trim();
if(employeeId.length()<=0) blException.setGenericException("Employee Id required.");
}
if(!this.employeeIdWiseEmployeesMap.containsKey(employeeId.toUpperCase())) 
{
blException.setGenericException("Invalid Employee Id :"+employeeId);
}
if(blException.hasGenericException())
{
throw blException;
}
EmployeeInterface employee=this.employeeIdWiseEmployeesMap.get(employeeId);
EmployeeInterface dsEmployee=new Employee();
dsEmployee.setEmployeeId(employee.getEmployeeId());
dsEmployee.setName(employee.getName());
DesignationInterface designation=employee.getDesignation();
DesignationManagerInterface designationManager=DesignationManager.getDesignationManager();
dsEmployee.setDesignation(((DesignationManager)designationManager).getDSDesignationByCode(designation.getCode()));
dsEmployee.setDateOfBirth(employee.getDateOfBirth());
dsEmployee.setGender((employee.getGender()=='M')? GENDER.MALE:GENDER.FEMALE);
dsEmployee.setIsIndian(employee.getIsIndian());
dsEmployee.setBasicSalary(employee.getBasicSalary());
dsEmployee.setPANNumber(employee.getPANNumber());
dsEmployee.setAadharCardNumber(employee.getAadharCardNumber());
return  dsEmployee;
}
public EmployeeInterface getEmployeeByPanNumber(String panNumber) throws BLException
{

BLException blException =new BLException();
 if(panNumber==null)
{
blException.setGenericException("pan number required.");
}
else
{
panNumber=panNumber.trim();
if(panNumber.length()<=0) blException.setGenericException("Pan Number required.");
}
if(!this.panNumberWiseEmployeesMap.containsKey(panNumber.toUpperCase())) 
{
blException.setGenericException("Invalid Pan Number :"+panNumber);
}
if(blException.hasGenericException())
{
throw blException;
}
EmployeeInterface employee=this.panNumberWiseEmployeesMap.get(panNumber);
EmployeeInterface dsEmployee=new Employee();
dsEmployee.setEmployeeId(employee.getEmployeeId());
dsEmployee.setName(employee.getName());
DesignationInterface designation=employee.getDesignation();
DesignationManagerInterface designationManager=DesignationManager.getDesignationManager();
dsEmployee.setDesignation(((DesignationManager)designationManager).getDSDesignationByCode(designation.getCode()));
dsEmployee.setDateOfBirth(employee.getDateOfBirth());
dsEmployee.setGender((employee.getGender()=='M')? GENDER.MALE:GENDER.FEMALE);
dsEmployee.setIsIndian(employee.getIsIndian());
dsEmployee.setBasicSalary(employee.getBasicSalary());
dsEmployee.setPANNumber(employee.getPANNumber());
dsEmployee.setAadharCardNumber(employee.getAadharCardNumber());
return dsEmployee;


}
public EmployeeInterface getEmployeeByAadharCardNumber(String aadharCardNumber) throws BLException
{
BLException blException =new BLException();
 if(aadharCardNumber==null)
{
blException.setGenericException("Aadhar card number required.");
}
else
{
aadharCardNumber=aadharCardNumber.trim();
if(aadharCardNumber.length()<=0) blException.setGenericException("Aadhar card Number required.");
}
if(!this.aadharCardNumberWiseEmployeesMap.containsKey(aadharCardNumber.toUpperCase())) 
{
blException.setGenericException("Invalid Aadhar card Number :"+aadharCardNumber);
}
if(blException.hasGenericException())
{
throw blException;
}
EmployeeInterface employee=this.aadharCardNumberWiseEmployeesMap.get(aadharCardNumber);
EmployeeInterface dsEmployee=new Employee();
dsEmployee.setEmployeeId(employee.getEmployeeId());
dsEmployee.setName(employee.getName());
DesignationInterface designation=employee.getDesignation();
DesignationManagerInterface designationManager=DesignationManager.getDesignationManager();
dsEmployee.setDesignation(((DesignationManager)designationManager).getDSDesignationByCode(designation.getCode()));
dsEmployee.setDateOfBirth(employee.getDateOfBirth());
dsEmployee.setGender((employee.getGender()=='M')? GENDER.MALE:GENDER.FEMALE);
dsEmployee.setIsIndian(employee.getIsIndian());
dsEmployee.setBasicSalary(employee.getBasicSalary());
dsEmployee.setPANNumber(employee.getPANNumber());
dsEmployee.setAadharCardNumber(employee.getAadharCardNumber());
return  dsEmployee;

}
public int getEmployeeCount()
{
return employeesSet.size();
}
public boolean employeeIdExists(String employeeId )
{
return this.employeeIdWiseEmployeesMap.containsKey(employeeId);
}
public boolean panNumberExists(String panNumber)
{
return this.panNumberWiseEmployeesMap.containsKey(panNumber);
}
public boolean aadharCardNumberExists(String aadharCardNumber)
{
return this.aadharCardNumberWiseEmployeesMap.containsKey(aadharCardNumber);
}
public Set<EmployeeInterface> getEmployees() throws BLException
{
Set<EmployeeInterface> employees=new TreeSet<>();
EmployeeInterface employee;
DesignationInterface designation;
DesignationInterface dsDesignation;
DesignationManagerInterface designationManager=DesignationManager.getDesignationManager();
for(EmployeeInterface dsEmployee:employeesSet)
{
employee=new Employee();
employee.setEmployeeId(dsEmployee.getEmployeeId());
employee.setName(dsEmployee.getName());
designation=new Designation();
dsDesignation=new Designation();
dsDesignation=dsEmployee.getDesignation();
designation.setCode(dsDesignation.getCode());
designation.setTitle(dsDesignation.getTitle());
employee.setDesignation(designation);
employee.setDateOfBirth((Date)dsEmployee.getDateOfBirth().clone());
employee.setGender((dsEmployee.getGender()=='M')? GENDER.MALE:GENDER.FEMALE);
employee.setIsIndian(dsEmployee.getIsIndian());
employee.setBasicSalary(dsEmployee.getBasicSalary());
employee.setPANNumber(dsEmployee.getPANNumber());
employee.setAadharCardNumber(dsEmployee.getAadharCardNumber());
employees.add(employee);
}
return employees;
}
public Set<EmployeeInterface> getByDesignationCode(int designationCode) throws BLException
{
DesignationManagerInterface designationManager=DesignationManager.getDesignationManager();
if(designationManager.designationCodeExists(designationCode)==false) 
{
BLException blException=new BLException();
blException.setGenericException("Invalid designation code"+designationCode);
}
Set<EmployeeInterface> employees=new TreeSet<>();
Set<EmployeeInterface> ets;
ets=designationCodeWiseEmployeesMap.get(designationCode);
if(ets==null)
{
return employees;
}
EmployeeInterface employee;
DesignationInterface designation;
DesignationInterface dsDesignation;
for(EmployeeInterface dsEmployee:ets)
{
employee=new Employee();
employee.setEmployeeId(dsEmployee.getEmployeeId());
employee.setName(dsEmployee.getName());
dsDesignation=dsEmployee.getDesignation();
designation=new Designation();
designation.setCode(dsDesignation.getCode());
designation.setTitle(dsDesignation.getTitle());
employee.setDesignation(designation);
employee.setDateOfBirth((Date)dsEmployee.getDateOfBirth().clone());
employee.setGender((dsEmployee.getGender()=='M')? GENDER.MALE:GENDER.FEMALE);
employee.setIsIndian(dsEmployee.getIsIndian());
employee.setBasicSalary(dsEmployee.getBasicSalary());
employee.setPANNumber(dsEmployee.getPANNumber());
employee.setAadharCardNumber(dsEmployee.getAadharCardNumber());
employees.add(employee);
}
return employees;
}
public int getEmployeeCountByDesignationCode(int designationCode) throws BLException
{
Set<EmployeeInterface> ets;
ets=this.designationCodeWiseEmployeesMap.get(designationCode);
if(ets==null) return 0;
return ets.size();
}
public boolean designationAlloted(int designationCode) throws BLException
{
return this.designationCodeWiseEmployeesMap.containsKey(designationCode);
}
}