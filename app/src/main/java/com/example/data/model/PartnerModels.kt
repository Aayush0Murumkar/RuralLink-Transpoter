package com.example.data.model

data class PartnerProfile(
    val id: String = "RL-PARTNER-9042",
    val name: String = "Ramesh Patil",
    val email: String = "ramesh.patil@rurallink.in",
    val phone: String = "+91 98901 88234",
    val aadhaarNumber: String = "4829 1048 9201",
    val vehicleType: String = "Hero Electric Cargo Trike (800kg)",
    val numberPlate: String = "MH 15 EV 9201",
    val address: String = "House No. 42, Pimple Gaon Farmgate Road",
    val locationCluster: String = "Nashik - Niphad Agricultural Belt",
    val agreedToTerms: Boolean = true,
    val isSignedUp: Boolean = true,
    val rating: Double = 4.92,
    val totalRuns: Int = 218,
    val memberSince: String = "Aug 2025"
)

data class DeliveryJob(
    val id: String = "RL-AGRI-7731",
    val title: String = "15 Bags Bio-NPK Fertilizer & Drip Irrigation Drippers",
    val supplier: String = "Krishi Seva Kendra Outlet",
    val category: String = "AGRI INPUTS",
    val payloadKg: Int = 375,
    val payoutAmount: Int = 290,
    val bonusAmount: Int = 30,
    val distanceKm: Double = 11.2,
    val durationMinutes: Int = 22,
    val eta: String = "Today, 09:35 am",
    val pickupName: String = "Krishi Seva Store",
    val pickupAddress: String = "Niphad Town, Nashik",
    val pickupLandmark: String = "Main Market Road, opposite State Bank",
    val pickupDistance: String = "1.8 km away",
    val pickupPhone: String = "+91 98231 44550",
    val dropoffName: String = "Anandrao Shinde Organic Farm",
    val dropoffAddress: String = "Vinchur Village, Nashik",
    val dropoffLandmark: String = "Near Solar Water Pump #4, North Field",
    val dropoffPhone: String = "+91 94220 88190",
    val isPrepaid: Boolean = true,
    val driverName: String = "Jason Smith",
    val courierId: String = "AGRI",
    val status: String = "ACCEPTED",
    val isReturnTrip: Boolean = false,
    val capacitySharePercent: Int = 45,
    val fuelCostEstimated: Int = 120
)

data class PartnerVehicle(
    val id: String,
    val name: String,
    val categoryTag: String = "EV ⚡",
    val payloadKg: Int,
    val rangeKm: Int,
    val plateNumber: String,
    val powerType: String = "EV ⚡",
    val batteryPack: String = "12.4 kWh Lithium-Ion",
    val chargePercent: Int = 78,
    val isActive: Boolean = false
)

data class TripHistoryItem(
    val id: String,
    val title: String,
    val supplierOrFarm: String,
    val fromLocation: String,
    val toLocation: String,
    val distanceKm: Double,
    val weightKg: Int,
    val payout: Int,
    val status: String, // "ACCEPTED", "DELIVERED"
    val category: String // "FARM_PRODUCE", "AGRI_INPUTS", "ARTISAN"
)

data class WeeklyDayEarning(
    val dayName: String,
    val isToday: Boolean = false,
    val ordersCount: Int,
    val distanceKm: Double,
    val earnings: Int,
    val bonus: Int
)

data class CopilotMessage(
    val id: String,
    val isUser: Boolean,
    val text: String,
    val time: String
)
