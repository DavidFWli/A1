package com.example.expensetracker

import android.annotation.SuppressLint
import android.app.Application
import android.os.Bundle
import android.os.Process
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.a1.R



class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // 设置全局异常捕获器
        Thread.setDefaultUncaughtExceptionHandler { thread, ex -> // 在这里处理异常，例如记录日志或上传到服务器
            Log.e("GlobalError", "Uncaught exception: ", ex)
            // 防止应用崩溃
            Process.killProcess(Process.myPid())
        }
    }
}
class MainActivity : AppCompatActivity() {

    private val expenses = ArrayList<String>()
    private lateinit var totalTextView: TextView
    private lateinit var expenseNameEditText: EditText
    private lateinit var expenseAmountEditText: EditText
    private lateinit var expenseRecyclerView: RecyclerView
    private lateinit var expenseAdapter: ExpenseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        totalTextView = findViewById(R.id.totalTextView)
        expenseNameEditText = findViewById(R.id.expenseNameEditText)
        expenseAmountEditText = findViewById(R.id.expenseAmountEditText)
        expenseRecyclerView = findViewById(R.id.expenseRecyclerView)

        // Setup RecyclerView
        expenseAdapter = ExpenseAdapter(expenses)
        expenseRecyclerView.layoutManager = LinearLayoutManager(this)
        expenseRecyclerView.adapter = expenseAdapter

        // Add expense button click listener
        findViewById<Button>(R.id.addExpenseButton).setOnClickListener {
            val name = expenseNameEditText.text.toString()
            val amount = expenseAmountEditText.text.toString()

            if (name.isNotEmpty() && amount.isNotEmpty()) {
                expenses.add("$name: $$amount")
                expenseAdapter.notifyDataSetChanged()
                updateTotal()
                expenseNameEditText.text.clear()
                expenseAmountEditText.text.clear()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateTotal() {
        var total = 0.0
        for (expense in expenses) {
            try {
                total += expense.split(": ")[1].replace("$", "").toDouble()
            } catch (e: NumberFormatException) {
                // Ignore invalid amounts
            }
        }
        totalTextView.text = "Total: $$total"
    }
}