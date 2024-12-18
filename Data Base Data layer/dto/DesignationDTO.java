package com.thinking.machines.hr.dl.dto;
import com.thinking.machines.hr.dl.interfaces.dto.*;
public class DesignationDTO implements DesignationDTOInterface
{
private int code;
private String title;
public void setCode(int code)
{
this.code=code;
}
public int getCode()
{
return code;
}
public void setTitle(String title)
{
this.title=title;
}
public String getTitle()
{
return title;
}
public boolean equals(Object other)
{
if(!(other instanceof DesignationDTOInterface)) return false;
DesignationDTOInterface designationDTO;
designationDTO=(DesignationDTO) other;
return this.code==designationDTO.getCode();
}
public int hashCode()
{
return code;
}
public int compareTo(DesignationDTOInterface designationDTO)
{
return this.code-designationDTO.getCode();
}
}