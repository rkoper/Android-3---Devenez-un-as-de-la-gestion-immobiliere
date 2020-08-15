package com.sofianem.realestatemanager.controller.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.appyvet.materialrangebar.RangeBar
import com.sofianem.realestatemanager.R
import com.sofianem.realestatemanager.utils.Utils
import kotlinx.android.synthetic.main.activity_calculator.*

class CalculatorActivity : AppCompatActivity() {

    var amount: Int = 1
    var rate: Float = 1f
    var term: Int = 1


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)

        cal_rangebar_amount.setOnRangeBarChangeListener(object :
            RangeBar.OnRangeBarChangeListener {
            override fun onTouchEnded(rangeBar: RangeBar?) {}
            override fun onRangeChangeListener(rangeBar: RangeBar?, leftPinIndex: Int, rightPinIndex: Int, leftPinValue: String?, rightPinValue: String?) {
                val value = rightPinValue.toString() + "0000"
                val displayValue = Utils.addWhiteSpace(value)
                cal_ed_amount.text = displayValue + " $"
                amount = value.toInt() }
            override fun onTouchStarted(rangeBar: RangeBar?) {} })

        cal_rangebar_rate.setOnRangeBarChangeListener(object :
            RangeBar.OnRangeBarChangeListener {
            override fun onTouchEnded(rangeBar: RangeBar?) {}
            override fun onRangeChangeListener(rangeBar: RangeBar?, leftPinIndex: Int, rightPinIndex: Int, leftPinValue: String?, rightPinValue: String?) {
                cal_ed_rate.text =  rightPinValue.toString() + " % "
                rate = rightPinValue!!.toFloat() }
            override fun onTouchStarted(rangeBar: RangeBar?) {} })

        cal_rangebar_term.setOnRangeBarChangeListener(object :
            RangeBar.OnRangeBarChangeListener {
            override fun onTouchEnded(rangeBar: RangeBar?) {}
            override fun onRangeChangeListener(rangeBar: RangeBar?, leftPinIndex: Int, rightPinIndex: Int, leftPinValue: String?, rightPinValue: String?) {
                cal_ed_term.text = rightPinValue.toString() + " months "
                term = rightPinValue!!.toInt() }
            override fun onTouchStarted(rangeBar: RangeBar?) {} })

        onClickHome()
        calcResult() }

    private fun calcResult() {
        //m = [C × t/12]/[1−(1 + t/12)−n]
        cal_txt_button.setOnClickListener {
            cal_txt_result.isVisible = true
            cal_txt_month.isVisible = true
            val resultDisplay =  Utils.calculateLoanAmount(rate, amount, term)
            cal_txt_result.text = "$resultDisplay $" } }


    private fun onClickHome() {
        cal_fb_home.setOnClickListener { val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()} }


}
