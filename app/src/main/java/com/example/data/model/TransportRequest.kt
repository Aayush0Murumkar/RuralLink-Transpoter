package com.example.data.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TransportRequest(
    @Json(name = "id") val id: String = "",
    @Json(name = "title") val title: String = "",
    @Json(name = "supplier") val supplier: String = "",
    @Json(name = "category") val category: String = "AGRI INPUTS",
    @Json(name = "payload_kg") val payloadKg: Int = 0,
    @Json(name = "payout_amount") val payoutAmount: Int = 0,
    @Json(name = "bonus_amount") val bonusAmount: Int = 0,
    @Json(name = "distance_km") val distanceKm: Double = 0.0,
    @Json(name = "duration_minutes") val durationMinutes: Int = 0,
    @Json(name = "eta") val eta: String = "",
    @Json(name = "pickup_name") val pickupName: String = "",
    @Json(name = "pickup_address") val pickupAddress: String = "",
    @Json(name = "pickup_landmark") val pickupLandmark: String = "",
    @Json(name = "pickup_distance") val pickupDistance: String = "",
    @Json(name = "pickup_phone") val pickupPhone: String = "",
    @Json(name = "dropoff_name") val dropoffName: String = "",
    @Json(name = "dropoff_address") val dropoffAddress: String = "",
    @Json(name = "dropoff_landmark") val dropoffLandmark: String = "",
    @Json(name = "dropoff_phone") val dropoffPhone: String = "",
    @Json(name = "is_prepaid") val isPrepaid: Boolean = true,
    @Json(name = "driver_name") val driverName: String = "",
    @Json(name = "courier_id") val courierId: String = "",
    @Json(name = "status") val status: String = "PENDING",
    @Json(name = "created_at") val createdAt: String? = null
) {
    fun toDeliveryJob(): DeliveryJob {
        return DeliveryJob(
            id = id.ifBlank { "RL-REQ-${System.currentTimeMillis() % 10000}" },
            title = title.ifBlank { "Agri Produce & Equipment Transport" },
            supplier = supplier.ifBlank { "Rural Distribution Center" },
            category = category,
            payloadKg = payloadKg,
            payoutAmount = payoutAmount,
            bonusAmount = bonusAmount,
            distanceKm = distanceKm,
            durationMinutes = durationMinutes,
            eta = eta.ifBlank { "Today, Express Delivery" },
            pickupName = pickupName.ifBlank { "Farmgate Depot" },
            pickupAddress = pickupAddress.ifBlank { "Main Rural Hub" },
            pickupLandmark = pickupLandmark,
            pickupDistance = pickupDistance.ifBlank { "2.5 km away" },
            pickupPhone = pickupPhone,
            dropoffName = dropoffName.ifBlank { "Farmer Cooperative Market" },
            dropoffAddress = dropoffAddress.ifBlank { "District APMC Mandi" },
            dropoffLandmark = dropoffLandmark,
            dropoffPhone = dropoffPhone,
            isPrepaid = isPrepaid,
            driverName = driverName,
            courierId = courierId,
            status = status,
            isReturnTrip = (bonusAmount > 0),
            capacitySharePercent = (payloadKg * 100 / 800).coerceIn(10, 100),
            fuelCostEstimated = ((distanceKm / 10.0) * 98.38).toInt()
        )
    }

    companion object {
        fun fromDeliveryJob(job: DeliveryJob): TransportRequest {
            return TransportRequest(
                id = job.id,
                title = job.title,
                supplier = job.supplier,
                category = job.category,
                payloadKg = job.payloadKg,
                payoutAmount = job.payoutAmount,
                bonusAmount = job.bonusAmount,
                distanceKm = job.distanceKm,
                durationMinutes = job.durationMinutes,
                eta = job.eta,
                pickupName = job.pickupName,
                pickupAddress = job.pickupAddress,
                pickupLandmark = job.pickupLandmark,
                pickupDistance = job.pickupDistance,
                pickupPhone = job.pickupPhone,
                dropoffName = job.dropoffName,
                dropoffAddress = job.dropoffAddress,
                dropoffLandmark = job.dropoffLandmark,
                dropoffPhone = job.dropoffPhone,
                isPrepaid = job.isPrepaid,
                driverName = job.driverName,
                courierId = job.courierId,
                status = job.status
            )
        }
    }
}
