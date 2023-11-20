package com.amber.lib.thread_tracker.ui

import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.os.Message
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amber.lib.thread_tracker.R
import com.amber.lib.thread_tracker.ThreadInfoManager
import com.amber.lib.thread_tracker.TrackerUtils.setStatusBarColor

/**
 * 线程/线程池列表
 */
class TrackerActivity : Activity() {

    private lateinit var refreshBtn:ImageView
    private lateinit var refreshProgress:ProgressBar
    private lateinit var statisticsText:TextView
    private lateinit var recyclerView:RecyclerView

    private val refreshHandlerThread = HandlerThread("ThreadTracker-Refresh").apply {
        start()
    }
    private val refreshHandler = object : Handler(refreshHandlerThread.looper) {
        override fun handleMessage(msg: Message) {
            refreshList(msg.what == 1)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.threadtracker_activity_tracker)
        setStatusBarColor(window)
        refreshBtn = findViewById(R.id.refreshBtn)
        refreshProgress = findViewById(R.id.refreshProgress)
        statisticsText = findViewById(R.id.statisticsText)
        recyclerView = findViewById(R.id.recyclerView)

        refreshBtn.setOnClickListener {
            refreshBtn.visibility = View.GONE
            refreshProgress.visibility = View.VISIBLE
            refreshHandler.sendEmptyMessage(1)
        }
        val adapter = TrackerAdapter(emptyList(), object : OnItemClickListener {
            override fun onItemClick(view: View) {
                val position: Int = recyclerView.getChildAdapterPosition(view)
                ThreadDetailsActivity.startDetailsActivity(
                    this@TrackerActivity,
                    (recyclerView.adapter as TrackerAdapter).getItemList()[position]
                )
            }
        })
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        refreshHandler.sendEmptyMessage(0)
    }

    private fun refreshList(toast: Boolean) {
        val infoResult = ThreadInfoManager.INSTANCE.buildAllThreadInfo()
        runOnUiThread {
            (recyclerView.adapter as TrackerAdapter).setItemList(infoResult.list)
            refreshBtn.visibility = View.VISIBLE
            refreshProgress.visibility = View.GONE
             statisticsText.text = "total: ${infoResult.totalNum}      system/unknown: ${infoResult.unknownNum}"
            if (toast) {
                Toast.makeText(
                    this,
                    "total: ${infoResult.totalNum}  system/unknown: ${infoResult.unknownNum}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun onDestroy() {
        refreshHandler.removeCallbacksAndMessages(null)
        refreshHandlerThread.quit()
        super.onDestroy()
    }
}