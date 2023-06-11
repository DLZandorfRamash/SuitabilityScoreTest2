package com.orlandodelavega.suitabilityscoretest

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.orlandodelavega.suitabilityscoretest.data.ReceivedData
import com.orlandodelavega.suitabilityscoretest.data.ResultsData
import com.orlandodelavega.suitabilityscoretest.databinding.ActivityMainBinding
import com.orlandodelavega.suitabilityscoretest.view.DriverDataAdp
import com.orlandodelavega.suitabilityscoretest.view.ResultDataAdp
import com.orlandodelavega.suitabilityscoretest.view.ShipmentDataAdp
import java.io.InputStream

class MainActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityMainBinding

    var selectedDriverPos = -1
    var selectedShipmentPos = -1
    private var suitabilityScore = 0.0
    private val jsonName = "ENG-MobileSDECodeExcercise-V2.json"

    private lateinit var dataList: ReceivedData
    private var resultList = ArrayList<ResultsData>()
    private lateinit var driverAdapter: DriverDataAdp
    private lateinit var shipmentAdapter: ShipmentDataAdp
    private lateinit var resultsAdapter: ResultDataAdp

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        prepareData()
        initComponents()
    }

    /**
     * Will prepare and load data to be manipulated later by the user.
     */
    private fun prepareData()
    {
        val jsonData = readJson()

        dataList = if (jsonData.isNullOrEmpty()) {
            ReceivedData(ArrayList(), ArrayList())
        } else {
            Gson().fromJson(jsonData, ReceivedData::class.java)
        }

        driverAdapter = DriverDataAdp(dataList.driver, this)
        shipmentAdapter = ShipmentDataAdp(dataList.shipment, this)
        resultsAdapter = ResultDataAdp(resultList)
    }

    /**
     * Will return in the form of a string whatever was found in the json file.
     */
    private fun readJson(): String?
    {
        return try
        {
            val inputStream: InputStream = assets.open(jsonName)
            inputStream.bufferedReader().use { it.readText() }
        }
        catch (ex: Exception)
        {
            ex.printStackTrace()
            null
        }
    }

    /**
     * Will initialize components with their proper data previously loaded.
     */
    @SuppressLint("NotifyDataSetChanged")
    private fun initComponents()
    {
        initRecyclerViews()

        binding.btnConfirm.isEnabled = false
        binding.btnConfirm.setOnClickListener {
            resultList.add(
                ResultsData(
                    driver = dataList.driver[selectedDriverPos],
                    shipment = dataList.shipment[selectedShipmentPos],
                    score = suitabilityScore
                )
            )
            dataList.driver.removeAt(selectedDriverPos)
            dataList.shipment.removeAt(selectedShipmentPos)

            driverAdapter.notifyDataSetChanged()
            shipmentAdapter.notifyDataSetChanged()
            resultsAdapter.notifyDataSetChanged()

            binding.btnConfirm.isEnabled = false
            selectedDriverPos = -1
            selectedShipmentPos = -1
            suitabilityScore = 0.0
            updatePreview()
            checkContentInLists()
        }

        updatePreview()
        checkContentInLists()
    }

    /**
     * Will initialize all recycler views with their proper lists.
     */
    private fun initRecyclerViews()
    {
        binding.rvDrivers.apply {
            adapter = driverAdapter
            layoutManager = LinearLayoutManager(context)
        }

        binding.rvShipments.apply {
            adapter = shipmentAdapter
            layoutManager = LinearLayoutManager(context)
        }

        binding.rvResults.apply{
            adapter = resultsAdapter
            addItemDecoration(DividerItemDecoration(context, RecyclerView.VERTICAL))
            layoutManager = LinearLayoutManager(context)
        }
    }

    /**
     * Each time a driver or shipment is selected, it will do a quick check to display the score of each match.
     */
    fun updatePreview()
    {
        val selectedDriverStr = if (selectedDriverPos >= 0) dataList.driver[selectedDriverPos] else ""
        val selectedShipmentStr = if (selectedShipmentPos >= 0) dataList.shipment[selectedShipmentPos] else ""

        if (selectedDriverStr.isNotEmpty() && selectedShipmentStr.isNotEmpty())
        {
            execScoreCalculations(selectedDriverStr, selectedShipmentStr)
            binding.btnConfirm.isEnabled = true
        }
        else
        {
            binding.btnConfirm.isEnabled = false
        }

        binding.txtPreviewDriver.text = selectedDriverStr.ifEmpty { resources.getString(R.string.no_driver_selected) }
        binding.txtPreviewShipment.text = selectedShipmentStr.ifEmpty { resources.getString(R.string.no_shipment_selected) }
        binding.txtPreviewScore.text = resources.getString(R.string.suitability_no, suitabilityScore.toString())
    }

    /**
     * Does the process of calculating Suitability Score to show on screen, depending on driver and shipment address selected.
     *
     * @param selectedDriverStr Driver's name to get calculated on.
     * @param selectedShipmentStr Shipment address to get calculated on.
     */
    private fun execScoreCalculations(selectedDriverStr: String, selectedShipmentStr: String)
    {
        if (selectedShipmentStr.length % 2 == 0) // Even
        {
            var vowelsInName = 0

            for (i in selectedDriverStr.indices)
            {
                if (isVowel(selectedDriverStr[i]))
                {
                    vowelsInName++
                }
            }

            suitabilityScore = vowelsInName * 1.5
        }
        else // Odd
        {
            var consonantsInName = selectedDriverStr.replace(" ", "").length

            for (i in selectedDriverStr.indices)
            {
                if (isVowel(selectedDriverStr[i]))
                {
                    consonantsInName--
                }
            }

            suitabilityScore = consonantsInName.toDouble()
        }

        if (selectedShipmentStr.length == selectedDriverStr.length)
        {
            suitabilityScore *= 1.5
        }

    }

    /**
     * Will verify if each list has an element to show text indicating that the lists are empty.
     */
    private fun checkContentInLists()
    {
        binding.txtEmptyDriverList.visibility = if (dataList.driver.isEmpty()) View.VISIBLE else View.GONE
        binding.txtEmptyShipmentList.visibility = if (dataList.shipment.isEmpty()) View.VISIBLE else View.GONE
        binding.txtEmptyResultList.visibility = if (resultList.isEmpty()) View.VISIBLE else View.GONE
    }

    /**
     * Will verify if the provided character is a vowel and return true if it is.
     *
     * @param curChar character to get evaluated.
     */
    private fun isVowel(curChar: Char): Boolean = curChar == 'a' || curChar == 'e' || curChar == 'i' || curChar == 'o' || curChar == 'u'

}