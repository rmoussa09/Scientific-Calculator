package com.example.Scientific_Calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.AppCompatButton
import com.example.Scientific_Calculator.databinding.ActivityMainBinding
import kotlin.math.sqrt //Used for Square Root operation

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding //View Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    //C clears the entire display screen and the current calculation.
    fun setButtonEventCE(view: View) {
        binding.dataTv!!.text = "" //Puts an empty string
        binding.resultTv.visibility = View.GONE
        }

    //CE clears the most recent entry (e.g., the last digit you just entered)
    fun setButtonEventClear(view: View) {
        val expression = binding.dataTv!!.text.toString()
        if (expression.isNotEmpty()) {
            binding.dataTv!!.text = expression.substring(0, expression.length - 1)
        }
    }

    // Equal button evaluate expression
    fun setButtonEventEqual(view: View) {
        try {
            val result = setButtonEventEvaluateExpression(binding.dataTv!!.text.toString())
            binding.resultTv.text = "= $result"
            binding.resultTv.visibility = View.VISIBLE
        } catch (e: Exception) {
            // Handle invalid expression
        }
    }

    fun setButtonEventDigits(view: View) {
        val digit = (view as AppCompatButton).text.toString()
        binding.dataTv!!.append(digit)
    }

    fun setButtonEventOperator(view: View) {
        val operator = (view as AppCompatButton).text.toString()
        binding.dataTv!!.append(" $operator ")
    }

    //Square root function for square root button
    fun setButtonEventSquareRoot(view: View) {
        val expression = binding.dataTv!!.text.toString()
        //This evaluates the expression and formats the decimal to three places to the right
        //When two numbers have done an operation, the square root would be done on the final result, not last number entered
        if (expression.isNotEmpty()) {
            try {
                val result = setButtonEventEvaluateExpression(expression)
                val sqrtResult = sqrt(result)
                val formattedResult = String.format("%.3f", sqrtResult)
                binding.dataTv!!.text = formattedResult
            } catch (e: Exception) {
                // Handle invalid expression
            }
        }
    }

    fun setButtonEventPlusMinus(view: View) {
        val expression = binding.dataTv!!.text.toString()
        if (expression.isNotEmpty()) {
            // Get the last character in the expression
            val lastChar = expression.last()

            // If the last character is a digit, add a "-" sign in front of it
            if (lastChar.isDigit()) {
                // Split the expression into tokens and get the last number
                val tokens = expression.split(" ")
                val lastNum = tokens.last()

                // Replace the last number with its negation
                val negLastNum = if (lastNum.startsWith('-')) {
                    lastNum.substring(1) // remove "-" sign
                } else {
                    "-$lastNum" // add "-" sign
                }
                val newExpression = expression.substring(0, expression.length - lastNum.length) + negLastNum
                binding.dataTv!!.text = newExpression
            }
            // If the last character is an operator, insert "-0 " after the operator
            else if (lastChar in setOf('+', '-', '*', '/')) {
                binding.dataTv!!.append("-0 ")
            }
        }
        // If the expression is empty, insert "-0"
        else {
            binding.dataTv!!.text = "-0"
        }
    }

    //evaluating the expressions
    private fun setButtonEventEvaluateExpression(expression: String): Double {
        val operators = mutableListOf<String>()
        val operands = mutableListOf<Double>()
        val tokens = expression.split(" ")

        for (token in tokens) {
            when (token) {
                in setOf("+", "-", "*", "/") -> operators.add(token)
                else -> operands.add(token.toDouble())
            }
        }

        while (operators.isNotEmpty()) {
            val op = operators.removeAt(0)
            val firstOperand = operands.removeAt(0)
            val secondOperand = operands.removeAt(0)

            when (op) {
                "+" -> operands.add(0, firstOperand + secondOperand)
                "-" -> operands.add(0, firstOperand - secondOperand)
                "*" -> operands.add(0, firstOperand * secondOperand)
                "/" -> operands.add(0, firstOperand / secondOperand)
            }
        }
        return operands[0]
    }
}
