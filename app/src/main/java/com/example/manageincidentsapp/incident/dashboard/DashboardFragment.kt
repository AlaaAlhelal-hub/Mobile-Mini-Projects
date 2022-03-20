package com.example.manageincidentsapp.incident.dashboard

import android.animation.ValueAnimator
import android.app.ActionBar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.anychart.AnyChart
import com.anychart.AnyChartView
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.anychart.enums.Align
import com.anychart.enums.Layout
import com.anychart.enums.LegendLayout
import com.example.manageincidentsapp.R
import com.example.manageincidentsapp.databinding.FragmentDashboardBinding
import com.example.manageincidentsapp.user.IncidentApiStatus


class DashboardFragment : Fragment() {
    lateinit var dashboardViewModel: DashboardViewModel
    lateinit var binding: FragmentDashboardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_dashboard, container, false)

        dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        binding.lifecycleOwner = this

        binding.dashboardViewModel = dashboardViewModel

        dashboardViewModel.dashboardData.observe(viewLifecycleOwner, Observer {

            //update dashboard
            //setUpChart()
        })

        dashboardViewModel.status.observe(viewLifecycleOwner, Observer {
            newStatus ->
            if (newStatus == IncidentApiStatus.Done)
            {

                setUpChart()

            }// if
         })

        return binding.root
    }

    fun setUpChart() {
        val anyChartView: AnyChartView = binding.ChartView

        val pie = AnyChart.pie()


        //get data
        val allData = dashboardViewModel.dashboardData.value

        var statusOneCounter = 0
        var statusTwoCounter = 0
        var statusThreeCounter = 0
        var statusFourCounter = 0

        allData!!.forEach {
            it ->
            when (it.status){
                0 -> statusOneCounter = it._count.status
                1 -> statusTwoCounter = it._count.status
                2 -> statusThreeCounter = it._count.status
                3 -> statusFourCounter = it._count.status
            }
        }
        val dataList: MutableList<DataEntry> = ArrayList()
        dataList.add(ValueDataEntry("Submitted", statusOneCounter))
        dataList.add(ValueDataEntry("InProgress", statusTwoCounter))
        dataList.add(ValueDataEntry("Completed", statusThreeCounter))
        dataList.add(ValueDataEntry("Rejected", statusFourCounter))

        pie.data(dataList)

        //pie.title("Total incidents")

        pie.labels().position("outside")

        pie.legend().title().enabled(true)

        pie.legend().title()
            .text("Incidents status")
            .padding(0.0, 0.0, 8.0, 0.0)

        pie.legend()
            .position("center-bottom")
            .itemsLayout(LegendLayout.HORIZONTAL_EXPANDABLE)
            .align(Align.CENTER)

        pie.palette(arrayOf("#26c6da", "#EF893C", "#4CAF65", "#CF3F34"))
        //anyChartView!!.clear()

        anyChartView.setChart(pie)
        anyChartView.setZoomEnabled(true)
        anyChartView.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT

        //anyChartView.animation
        val total = statusOneCounter + statusTwoCounter + statusThreeCounter + statusFourCounter
        startCountAnimation(total)
    }

    private fun startCountAnimation(total: Int) {
        val animator = ValueAnimator.ofInt(0, total)
        animator.duration = 2000
        animator.addUpdateListener { animation -> binding.totalIncidentsValue.text = animation.animatedValue.toString() }
        animator.start()
    }
}