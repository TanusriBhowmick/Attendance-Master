package com.example.vinay.attendence.ModelClasses;

public class ModelStudentRegistrationData {
    private String name;
    private String department;
    private String enrollmentNo;
    private String semester;
    private String email;
    private String mobileNo;
    private int finger1A;
    private String finger1B;
    private String finger1C;
    private String finger2A;
    private String finger2B;
    private String finger2C;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEnrollmentNo() {
        return enrollmentNo;
    }

    public void setEnrollmentNo(String enrollmentNo) {
        this.enrollmentNo = enrollmentNo;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public int getFinger1A() {
        return finger1A;
    }

    public void setFinger1A(int finger1A) {
        this.finger1A = finger1A;
    }

    public String getFinger1B() {
        return finger1B;
    }

    public void setFinger1B(String finger1B) {
        this.finger1B = finger1B;
    }

    public String getFinger1C() {
        return finger1C;
    }

    public void setFinger1C(String finger1C) {
        this.finger1C = finger1C;
    }

    public String getFinger2A() {
        return finger2A;
    }

    public void setFinger2A(String finger2A) {
        this.finger2A = finger2A;
    }

    public String getFinger2B() {
        return finger2B;
    }

    public void setFinger2B(String finger2B) {
        this.finger2B = finger2B;
    }

    public String getFinger2C() {
        return finger2C;
    }

    public void setFinger2C(String finger2C) {
        this.finger2C = finger2C;
    }


    @Override
    public String toString() {
        return "ModelStudentRegistrationData{" +
                "name='" + name + '\'' +
                ", department='" + department + '\'' +
                ", enrollmentNo='" + enrollmentNo + '\'' +
                ", semester='" + semester + '\'' +
                ", email='" + email + '\'' +
                ", mobileNo='" + mobileNo + '\'' +
                ", finger1A='" + finger1A + '\'' +
                ", finger1B='" + finger1B + '\'' +
                ", finger1C='" + finger1C + '\'' +
                ", finger2A='" + finger2A + '\'' +
                ", finger2B='" + finger2B + '\'' +
                ", finger2C='" + finger2C + '\'' +
                '}';
    }
}
