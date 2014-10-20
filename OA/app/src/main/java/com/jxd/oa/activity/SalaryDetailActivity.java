package com.jxd.oa.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jxd.oa.R;
import com.jxd.oa.activity.base.AbstractActivity;
import com.jxd.oa.bean.AccountWage;
import com.yftools.ViewUtil;
import com.yftools.view.annotation.ContentView;
import com.yftools.view.annotation.ViewInject;
import com.yftools.view.annotation.event.OnClick;

/**
 * *****************************************
 * Description ：我的工资详情
 * Created by cy on 2014/9/24.
 * *****************************************
 */
@ContentView(R.layout.activity_salary_detail)
public class SalaryDetailActivity extends AbstractActivity {

    @ViewInject(R.id.yearMonth_tv)
    private TextView yearMonth_tv;
    @ViewInject(R.id.payableTotal_tv)
    private TextView payableTotal_tv;
    @ViewInject(R.id.payableTotalIndicator_iv)
    private ImageView payableTotalIndicator_iv;
    @ViewInject(R.id.payableTotalDetail_ll)
    private LinearLayout payableTotalDetail_ll;
    @ViewInject(R.id.paySalary_tv)
    private TextView paySalary_tv;
    @ViewInject(R.id.meritSalary_tv)
    private TextView meritSalary_tv;
    @ViewInject(R.id.transitivitySubsidy_tv)
    private TextView transitivitySubsidy_tv;
    @ViewInject(R.id.postAllowance_tv)
    private TextView postAllowance_tv;
    @ViewInject(R.id.overtimeFee_tv)
    private TextView overtimeFee_tv;
    @ViewInject(R.id.medicineFee_tv)
    private TextView medicineFee_tv;
    @ViewInject(R.id.premium_tv)
    private TextView premium_tv;
    @ViewInject(R.id.telephoneFee_tv)
    private TextView telephoneFee_tv;
    @ViewInject(R.id.healthFee_tv)
    private TextView healthFee_tv;
    @ViewInject(R.id.lifeAllowance_tv)
    private TextView lifeAllowance_tv;
    @ViewInject(R.id.serviceFee_tv)
    private TextView serviceFee_tv;
    @ViewInject(R.id.otherFee_tv)
    private TextView otherFee_tv;

    @ViewInject(R.id.deductTotal_tv)
    private TextView deductTotal_tv;
    @ViewInject(R.id.deductTotalIndicator_iv)
    private ImageView deductTotalIndicator_iv;
    @ViewInject(R.id.deductTotalDetail_ll)
    private LinearLayout deductTotalDetail_ll;
    @ViewInject(R.id.leaveDeduction_tv)
    private TextView leaveDeduction_tv;
    @ViewInject(R.id.rent_tv)
    private TextView rent_tv;
    @ViewInject(R.id.pensionInsurance_tv)
    private TextView pensionInsurance_tv;
    @ViewInject(R.id.medicalInsurance_tv)
    private TextView medicalInsurance_tv;
    @ViewInject(R.id.birthInsurance_tv)
    private TextView birthInsurance_tv;
    @ViewInject(R.id.unemploymentInsurance_tv)
    private TextView unemploymentInsurance_tv;
    @ViewInject(R.id.workInsurance_tv)
    private TextView workInsurance_tv;
    @ViewInject(R.id.housingFund_tv)
    private TextView housingFund_tv;
    @ViewInject(R.id.withholdTax_tv)
    private TextView withholdTax_tv;
    @ViewInject(R.id.otherDeductions_tv)
    private TextView otherDeductions_tv;

    @ViewInject(R.id.employeeBenefit_tv)
    private TextView employeeBenefit_tv;

    @ViewInject(R.id.actualSalary_tv)
    private TextView actualSalary_tv;

    private AccountWage accountWage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewUtil.inject(this);
        accountWage = (AccountWage) getIntent().getSerializableExtra("salary");
        getSupportActionBar().setTitle(getString(R.string.txt_title_salary_detail));
        initData();
    }

    private void initData() {
        yearMonth_tv.setText(accountWage.getYearMonth());
        payableTotal_tv.setText(accountWage.getPayableTotal() + "");
        paySalary_tv.setText(accountWage.getPaySalary() + "");
        meritSalary_tv.setText(accountWage.getMeritSalary() + "");
        transitivitySubsidy_tv.setText(accountWage.getTransitivitySubsidy() + "");
        postAllowance_tv.setText(accountWage.getPostAllowance() + "");
        overtimeFee_tv.setText(accountWage.getOtherFee() + "");
        medicineFee_tv.setText(accountWage.getMedicineFee() + "");
        premium_tv.setText(accountWage.getPremium() + "");
        telephoneFee_tv.setText(accountWage.getTelephoneFee() + "");
        healthFee_tv.setText(accountWage.getHealthFee() + "");
        lifeAllowance_tv.setText(accountWage.getLifeAllowance() + "");
        serviceFee_tv.setText(accountWage.getServiceFee() + "");
        otherFee_tv.setText(accountWage.getOtherFee() + "");

        deductTotal_tv.setText(accountWage.getDeductTotal() + "");
        leaveDeduction_tv.setText(accountWage.getLeaveDeduction() + "");
        rent_tv.setText(accountWage.getRent() + "");
        pensionInsurance_tv.setText(accountWage.getPensionInsurance() + "");
        medicalInsurance_tv.setText(accountWage.getMedicalInsurance() + "");
        birthInsurance_tv.setText(accountWage.getBirthInsurance() + "");
        unemploymentInsurance_tv.setText(accountWage.getUnemploymentInsurance() + "");
        workInsurance_tv.setText(accountWage.getWorkInsurance() + "");
        housingFund_tv.setText(accountWage.getHousingFund() + "");
        withholdTax_tv.setText(accountWage.getWithholdTax() + "");
        otherDeductions_tv.setText(accountWage.getOtherDeductions() + "");

        employeeBenefit_tv.setText(accountWage.getEmployeeBenefit() + "");

        actualSalary_tv.setText(accountWage.getActualSalary() + "");
    }

    @OnClick(R.id.payableTotal_ll)
    public void payableTotalClick(View view) {
        if (payableTotalDetail_ll.getVisibility() == View.VISIBLE) {
            payableTotalDetail_ll.setVisibility(View.GONE);
            payableTotalIndicator_iv.setImageResource(R.drawable.icon_group_down);
        } else {
            payableTotalDetail_ll.setVisibility(View.VISIBLE);
            payableTotalIndicator_iv.setImageResource(R.drawable.icon_group_up);
        }
    }

    @OnClick(R.id.deductTotal_ll)
    public void deductTotalClick(View view) {
        if (deductTotalDetail_ll.getVisibility() == View.VISIBLE) {
            deductTotalDetail_ll.setVisibility(View.GONE);
            deductTotalIndicator_iv.setImageResource(R.drawable.icon_group_down);
        } else {
            deductTotalDetail_ll.setVisibility(View.VISIBLE);
            deductTotalIndicator_iv.setImageResource(R.drawable.icon_group_up);
        }
    }
}
