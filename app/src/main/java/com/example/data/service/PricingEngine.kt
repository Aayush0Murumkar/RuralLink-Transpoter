package com.example.data.service

import kotlin.math.roundToInt

object PricingEngine {
    // Configurable Pricing Parameters (MVP defaults)
    var dieselPricePerLiter = 98.38
    var platformFeePercent = 0.05
    var backloadDiscountPercent = 0.25

    // Operating Cost Defaults (per km)
    var defaultDriverCostPerKm = 3.5
    var defaultMaintenanceCostPerKm = 2.0
    var defaultOtherCostPerKm = 1.0

    // Vehicle Configuration (Assuming Tata Ace / Small EV Cargo for partner)
    var vehicleCapacityKg = 800.0 // 800kg
    var vehicleMileageKmpl = 10.0 // 10 km/L (Equivalent for EV/Diesel mix for calculation)
    var defaultProfitMarginPercent = 0.20 // 20%

    data class ShipmentPricing(
        val fuelCost: Double,
        val totalOperatingCost: Double,
        val requiredRevenue: Double,
        val farmerCapacityShare: Double,
        val isBackload: Boolean,
        val farmerBaseShare: Double,
        val backloadDiscountAmount: Double,
        val transportCharge: Double,
        val platformFee: Double,
        val finalFarmerPrice: Double,
        val transporterPayout: Int
    )

    fun calculatePricing(
        distanceKm: Double,
        shipmentWeightKg: Double,
        isBackloadTrip: Boolean = false,
        tolls: Double = 0.0
    ): ShipmentPricing {
        // 1. Fuel Cost
        val fuelCost = (distanceKm / vehicleMileageKmpl) * dieselPricePerLiter

        // 2. Total Operating Cost
        val driverCost = distanceKm * defaultDriverCostPerKm
        val maintenanceCost = distanceKm * defaultMaintenanceCostPerKm
        val otherCost = distanceKm * defaultOtherCostPerKm
        val totalOperatingCost = fuelCost + driverCost + maintenanceCost + otherCost + tolls

        // 3. Transporter Profit
        val requiredRevenue = totalOperatingCost * (1.0 + defaultProfitMarginPercent)

        // 4. Shared Capacity Share
        // E.g., 200kg out of 800kg = 0.25 (25%)
        // Don't allow > 1.0
        val capacityShare = (shipmentWeightKg / vehicleCapacityKg).coerceIn(0.1, 1.0) 

        // 5. Farmer Base Transport Share
        val farmerBaseShare = requiredRevenue * capacityShare

        // 6. Backload (Return-Trip) Discount
        val backloadDiscount = if (isBackloadTrip) {
            farmerBaseShare * backloadDiscountPercent
        } else {
            0.0
        }

        val transportCharge = farmerBaseShare - backloadDiscount

        // 7. Platform Fee
        val platformFee = transportCharge * platformFeePercent

        val finalFarmerPrice = transportCharge + platformFee

        // Transporter receives the transport charge (platform keeps platform fee)
        val transporterPayout = transportCharge.roundToInt()

        return ShipmentPricing(
            fuelCost = fuelCost,
            totalOperatingCost = totalOperatingCost,
            requiredRevenue = requiredRevenue,
            farmerCapacityShare = capacityShare,
            isBackload = isBackloadTrip,
            farmerBaseShare = farmerBaseShare,
            backloadDiscountAmount = backloadDiscount,
            transportCharge = transportCharge,
            platformFee = platformFee,
            finalFarmerPrice = finalFarmerPrice,
            transporterPayout = transporterPayout
        )
    }
}
