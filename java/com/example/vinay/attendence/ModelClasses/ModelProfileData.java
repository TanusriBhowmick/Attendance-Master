package com.example.vinay.attendence.ModelClasses;

public class ModelProfileData {


    String name;
    String employee_id;
    String department;
    String mobile_no;
    String email_id;
    String password;
    String emp_type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getMobile_no() {
        return mobile_no;
    }

    public void setMobile_no(String mobile_no) {
        this.mobile_no = mobile_no;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmp_type() {
        return emp_type;
    }

    public void setEmp_type(String emp_type) {
        this.emp_type = emp_type;
    }

    @Override
    public String toString() {
        return "ModelProfileData{" +
                "name='" + name + '\'' +
                ", employee_id='" + employee_id + '\'' +
                ", department='" + department + '\'' +
                ", mobile_no='" + mobile_no + '\'' +
                ", email_id='" + email_id + '\'' +
                ", password='" + password + '\'' +
                ", emp_type='" + emp_type + '\'' +
                '}';
    }
}
