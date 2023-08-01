package com.easytipcalculator

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.easytipcalculator.databinding.ActivityMainBinding
import java.text.DecimalFormat


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        delegate.localNightMode= AppCompatDelegate.MODE_NIGHT_NO


        // Set the status bar color
        val window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = Color.parseColor("#444C5D") // semi-transparent black color

        //your code start from here
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE) //for keyboard

        binding.slider.addOnChangeListener { _, value, _ ->
            binding.tipAmount.text=value.toInt().toString()
        }


        //code for tipPercent
        var num=1
        binding.plusBtn.setOnClickListener {
            num++
            binding.personTv.text=num.toString()
            if(num==2){
                binding.negBtn.isEnabled = true
            }

        }

        binding.negBtn.setOnClickListener {
            num--
            binding.personTv.text=num.toString()
            if(num==1){
                binding.negBtn.isEnabled = false
            }

        binding.negBtn.setOnLongClickListener {
            binding.personTv.text="1"
            num=1
            binding.negBtn.isEnabled = false
            true
        }


        }


        //logic
        binding.calculateBtn.setOnClickListener {

            if(binding.etAmount.text.toString()==""){
                Toast.makeText(this,"Please enter the bill amount!" +
                        "",Toast.LENGTH_SHORT).show()
            }

            val billAmount= binding.etAmount.text.toString().toFloatOrNull()?:0.0f
            val tipPercent=binding.tipAmount.text.toString().toFloatOrNull()?:0.0f
            val noOfPerson=binding.personTv.text.toString().toFloatOrNull()?:0.0f

            val tipAmount = billAmount * (tipPercent / 100)
//            val formatTipAmount= String.format("%.2f",tipAmount)
            val formatTipAmount=formatAmount(tipAmount.toDouble() ) //original

            // total amount
            val totalAmount:Double=formatTipAmount.toDouble()+billAmount.toDouble()
            val totalAmountNew=formatAmount(totalAmount)

            //perPerson
            val perPerson=totalAmount/noOfPerson
            val perPersonNew=formatAmount(perPerson)

            binding.resultTv.text=formatTipAmount
            binding.perPersonAmount.text=perPersonNew
            binding.totalAmount.text=totalAmountNew
            hideKeyboard()

        }

        //copyButton
        binding.copyBtn.setOnClickListener {


            val amountCoyText1=binding.etAmount.text
            val percentCopyText2=binding.tipAmount.text
            val peopleCopyText3=binding.personTv.text
            val totalTipCopyTex4=binding.resultTv.text
            val perPersonCopyTex5=binding.perPersonAmount.text
            val allTotalCopyTex6=binding.totalAmount.text

            if (amountCoyText1.isNotEmpty() && percentCopyText2.isNotEmpty() &&
                peopleCopyText3.isNotEmpty() && totalTipCopyTex4.isNotEmpty() &&
                perPersonCopyTex5.isNotEmpty() && allTotalCopyTex6.isNotEmpty()
            ) {
                val allData =
                    "Bill Amount: $amountCoyText1 \nTip Percent(%): $percentCopyText2\nNo of Persons: $peopleCopyText3\n\n--------------\n\n" +
                            "Total tip: $totalTipCopyTex4\nPer Head Amount: $perPersonCopyTex5\nTotal Amount(To Pay): $allTotalCopyTex6\n"

                val clipboardManager =
                    getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clipDate = ClipData.newPlainText("text", allData)
                clipboardManager.setPrimaryClip(clipDate)
                Toast.makeText(this,"Bill copied to Clipboard",Toast.LENGTH_SHORT).show()

            }else {
                // Handle the case when one or more fields are empty
                Toast.makeText(this, "Please fill all the fields before copying.", Toast.LENGTH_SHORT).show()
            }
        }

        //share button
        binding.shareBtn.setOnClickListener {
            val intent=Intent(Intent.ACTION_SEND)
            intent.type="text/plain"

            val amountCoyText1=binding.etAmount.text
            val percentCopyText2=binding.tipAmount.text
            val peopleCopyText3=binding.personTv.text
            val totalTipCopyTex4=binding.resultTv.text
            val perPersonCopyTex5=binding.perPersonAmount.text
            val allTotalCopyTex6=binding.totalAmount.text

            if (amountCoyText1.isNotEmpty() && percentCopyText2.isNotEmpty() &&
                peopleCopyText3.isNotEmpty() && totalTipCopyTex4.isNotEmpty() &&
                perPersonCopyTex5.isNotEmpty() && allTotalCopyTex6.isNotEmpty()
            ){
                val allData="Bill Amount: $amountCoyText1 \nTip Percent(%): $percentCopyText2\nNo of Persons: $peopleCopyText3\n\n----------------\n\n" +
                    "Total tip: $totalTipCopyTex4\nPer Head Amount: $perPersonCopyTex5\nTotal Amount(To Pay): $allTotalCopyTex6\n"

                intent.putExtra(Intent.EXTRA_TEXT, allData)

            val chooser=Intent.createChooser(intent,"Share Using...")
            startActivity(chooser)
            }else {
                // Handle the case when one or more fields are empty
                Toast.makeText(this, "Please fill all the fields before sharing.", Toast.LENGTH_SHORT).show()
                }
        }

    }

    private fun formatAmount(tipAmount: Double): String {
        val decimalFormat = DecimalFormat("0.#")
        return decimalFormat.format(tipAmount)
    }

    private fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }




}
//logic : tipAmount = billAmount * (tipPercentage / 100).
//totalAmount = billAmount + tipAmount.