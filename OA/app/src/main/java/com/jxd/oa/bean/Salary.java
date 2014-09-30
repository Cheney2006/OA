package com.jxd.oa.bean;

import com.jxd.oa.bean.base.AbstractBean;
import com.yftools.db.annotation.Table;

/**
 * *****************************************
 * Description ：员工工资
 * Created by cy on 2014/9/24.
 * *****************************************
 */
@Table(name = "t_salary")
public class Salary extends AbstractBean {
    private String id;
    private String yearMonth;//年月
    private double actualSalary;//实发工资

    private double payableTotal;//实发合计
    private double paySalary;//薪级工资
    private double meritSalary;//绩效工资
    private double transitivitySubsidy;//过渡性补贴
    private double postAllowance;//岗位津贴
    private double overtimeFee;//加班费
    private double medicineFee;//特保药费
    private double premium;//特保费
    private double telephoneFee;//电话费
    private double healthFee;//保健费
    private double lifeAllowance;//生活补贴
    private double serviceFee;//劳务费
    private double otherFee;//其它费用

    private double deductTotal;//扣款合计
    private double leaveDeduction;//病事假捐款
    private double rent;//房租
    private double pensionInsurance;//养老保险
    private double medicalInsurance;//医疗保险
    private double birthInsurance;//生育保险
    private double unemploymentInsurance;//失业保险
    private double workInsurance;//工作保险
    private double housingFund;//住房公积金
    private double withholdTax;//代扣税
    private double otherDeductions;//其它扣税

    private double employeeBenefit;//员工福利


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getYearMonth() {
        return yearMonth;
    }

    public void setYearMonth(String yearMonth) {
        this.yearMonth = yearMonth;
    }

    public double getActualSalary() {
        return actualSalary;
    }

    public void setActualSalary(double actualSalary) {
        this.actualSalary = actualSalary;
    }

    public double getPayableTotal() {
        return payableTotal;
    }

    public void setPayableTotal(double payableTotal) {
        this.payableTotal = payableTotal;
    }

    public double getPaySalary() {
        return paySalary;
    }

    public void setPaySalary(double paySalary) {
        this.paySalary = paySalary;
    }

    public double getMeritSalary() {
        return meritSalary;
    }

    public void setMeritSalary(double meritSalary) {
        this.meritSalary = meritSalary;
    }

    public double getTransitivitySubsidy() {
        return transitivitySubsidy;
    }

    public void setTransitivitySubsidy(double transitivitySubsidy) {
        this.transitivitySubsidy = transitivitySubsidy;
    }

    public double getPostAllowance() {
        return postAllowance;
    }

    public void setPostAllowance(double postAllowance) {
        this.postAllowance = postAllowance;
    }

    public double getOvertimeFee() {
        return overtimeFee;
    }

    public void setOvertimeFee(double overtimeFee) {
        this.overtimeFee = overtimeFee;
    }

    public double getMedicineFee() {
        return medicineFee;
    }

    public void setMedicineFee(double medicineFee) {
        this.medicineFee = medicineFee;
    }

    public double getPremium() {
        return premium;
    }

    public void setPremium(double premium) {
        this.premium = premium;
    }

    public double getTelephoneFee() {
        return telephoneFee;
    }

    public void setTelephoneFee(double telephoneFee) {
        this.telephoneFee = telephoneFee;
    }

    public double getHealthFee() {
        return healthFee;
    }

    public void setHealthFee(double healthFee) {
        this.healthFee = healthFee;
    }

    public double getLifeAllowance() {
        return lifeAllowance;
    }

    public void setLifeAllowance(double lifeAllowance) {
        this.lifeAllowance = lifeAllowance;
    }

    public double getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(double serviceFee) {
        this.serviceFee = serviceFee;
    }

    public double getOtherFee() {
        return otherFee;
    }

    public void setOtherFee(double otherFee) {
        this.otherFee = otherFee;
    }

    public double getDeductTotal() {
        return deductTotal;
    }

    public void setDeductTotal(double deductTotal) {
        this.deductTotal = deductTotal;
    }

    public double getLeaveDeduction() {
        return leaveDeduction;
    }

    public void setLeaveDeduction(double leaveDeduction) {
        this.leaveDeduction = leaveDeduction;
    }

    public double getRent() {
        return rent;
    }

    public void setRent(double rent) {
        this.rent = rent;
    }

    public double getPensionInsurance() {
        return pensionInsurance;
    }

    public void setPensionInsurance(double pensionInsurance) {
        this.pensionInsurance = pensionInsurance;
    }

    public double getMedicalInsurance() {
        return medicalInsurance;
    }

    public void setMedicalInsurance(double medicalInsurance) {
        this.medicalInsurance = medicalInsurance;
    }

    public double getBirthInsurance() {
        return birthInsurance;
    }

    public void setBirthInsurance(double birthInsurance) {
        this.birthInsurance = birthInsurance;
    }

    public double getUnemploymentInsurance() {
        return unemploymentInsurance;
    }

    public void setUnemploymentInsurance(double unemploymentInsurance) {
        this.unemploymentInsurance = unemploymentInsurance;
    }

    public double getWorkInsurance() {
        return workInsurance;
    }

    public void setWorkInsurance(double workInsurance) {
        this.workInsurance = workInsurance;
    }

    public double getHousingFund() {
        return housingFund;
    }

    public void setHousingFund(double housingFund) {
        this.housingFund = housingFund;
    }

    public double getWithholdTax() {
        return withholdTax;
    }

    public void setWithholdTax(double withholdTax) {
        this.withholdTax = withholdTax;
    }

    public double getOtherDeductions() {
        return otherDeductions;
    }

    public void setOtherDeductions(double otherDeductions) {
        this.otherDeductions = otherDeductions;
    }

    public double getEmployeeBenefit() {
        return employeeBenefit;
    }

    public void setEmployeeBenefit(double employeeBenefit) {
        this.employeeBenefit = employeeBenefit;
    }
}
