package com.orlandodelavega.suitabilityscoretest.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ReceivedData
(
    @SerializedName("drivers")
    var driver: ArrayList<String>,

    @SerializedName("shipments")
    var shipment: ArrayList<String>
) : Serializable

data class ResultsData
(
    var driver: String,
    var shipment: String,
    var score: Double
) : Serializable