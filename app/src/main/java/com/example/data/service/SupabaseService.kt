package com.example.data.service

import android.util.Log
import com.example.data.model.TransportRequest
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit

/**
 * Service to interact with Supabase backend for the transporter app.
 * Connects to Supabase REST API and Realtime WebSockets to manage and stream
 * transport requests from the `transport_requests` table in real time without Firebase.
 */
class SupabaseService(
    private var supabaseUrl: String = getBuildConfigValue("SUPABASE_URL").ifBlank { "https://xtdeopclcoqdpgukhbmk.supabase.co" },
    private var supabaseKey: String = getBuildConfigValue("SUPABASE_ANON_KEY").ifBlank { getBuildConfigValue("SUPABASE_KEY") }.ifBlank { "sbp_d0ba49efc7bd2ff803a40520b4ce5247a157d076" },
    private val client: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()
) {

    private val moshi: Moshi = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val listType = Types.newParameterizedType(List::class.java, TransportRequest::class.java)
    private val jsonListAdapter = moshi.adapter<List<TransportRequest>>(listType)
    private val jsonObjectAdapter = moshi.adapter(TransportRequest::class.java)

    companion object {
        private const val TAG = "SupabaseService"
        private const val TABLE_NAME = "transport_requests"

        private fun getBuildConfigValue(fieldName: String): String {
            return try {
                val buildConfigClass = Class.forName("com.example.BuildConfig")
                val field = buildConfigClass.getField(fieldName)
                field.get(null) as? String ?: ""
            } catch (e: Throwable) {
                ""
            }
        }
    }

    private var isAuthValid: Boolean = true

    /**
     * Checks if valid Supabase credentials are set.
     */
    fun isConfigured(): Boolean {
        return isAuthValid &&
                supabaseUrl.isNotBlank() &&
                supabaseKey.isNotBlank() &&
                !supabaseUrl.contains("your-supabase-project", ignoreCase = true) &&
                !supabaseUrl.contains("YOUR_PROJECT_ID", ignoreCase = true) &&
                !supabaseKey.contains("YOUR_SUPABASE_ANON_KEY", ignoreCase = true) &&
                supabaseKey.length > 20
    }

    /**
     * Updates Supabase URL and Anon Key at runtime if configured dynamically.
     */
    fun updateCredentials(url: String, key: String) {
        this.supabaseUrl = url.trim().removeSuffix("/")
        this.supabaseKey = key.trim()
        this.isAuthValid = true
    }

    /**
     * Get the base REST API endpoint for `transport_requests`.
     */
    private fun getRestUrl(): String {
        val baseUrl = supabaseUrl.trim().removeSuffix("/").removeSuffix("/rest/v1")
        return "$baseUrl/rest/v1/$TABLE_NAME"
    }

    /**
     * Fetches all transport requests from the `transport_requests` table.
     */
    suspend fun fetchTransportRequests(): Result<List<TransportRequest>> = withContext(Dispatchers.IO) {
        try {
            if (!isConfigured()) {
                return@withContext Result.failure(IllegalStateException("Supabase credentials not set or invalid"))
            }

            val request = Request.Builder()
                .url("${getRestUrl()}?select=*&order=created_at.desc")
                .header("apikey", supabaseKey)
                .header("Authorization", "Bearer $supabaseKey")
                .header("Accept", "application/json")
                .get()
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""

            if (!response.isSuccessful) {
                if (response.code == 401 || response.code == 403) {
                    isAuthValid = false
                    Log.i(TAG, "Supabase API key requires configuration. Falling back to local offline mode.")
                } else {
                    Log.w(TAG, "Fetch returned status ${response.code}: $responseBody")
                }
                return@withContext Result.failure(IOException("HTTP ${response.code}: $responseBody"))
            }

            val requests = parseTransportRequestsJson(responseBody)
            Result.success(requests)
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching transport requests from Supabase", e)
            Result.failure(e)
        }
    }

    /**
     * Creates a new transport request in the `transport_requests` table.
     */
    suspend fun createTransportRequest(transportRequest: TransportRequest): Result<TransportRequest> = withContext(Dispatchers.IO) {
        try {
            if (!isConfigured()) {
                return@withContext Result.failure(IllegalStateException("Supabase credentials not configured"))
            }

            val jsonPayload = JSONObject().apply {
                if (transportRequest.id.isNotBlank()) put("id", transportRequest.id)
                put("title", transportRequest.title)
                put("supplier", transportRequest.supplier)
                put("category", transportRequest.category)
                put("payload_kg", transportRequest.payloadKg)
                put("payout_amount", transportRequest.payoutAmount)
                put("bonus_amount", transportRequest.bonusAmount)
                put("distance_km", transportRequest.distanceKm)
                put("duration_minutes", transportRequest.durationMinutes)
                put("eta", transportRequest.eta)
                put("pickup_name", transportRequest.pickupName)
                put("pickup_address", transportRequest.pickupAddress)
                put("pickup_landmark", transportRequest.pickupLandmark)
                put("pickup_distance", transportRequest.pickupDistance)
                put("pickup_phone", transportRequest.pickupPhone)
                put("dropoff_name", transportRequest.dropoffName)
                put("dropoff_address", transportRequest.dropoffAddress)
                put("dropoff_landmark", transportRequest.dropoffLandmark)
                put("dropoff_phone", transportRequest.dropoffPhone)
                put("is_prepaid", transportRequest.isPrepaid)
                put("driver_name", transportRequest.driverName)
                put("courier_id", transportRequest.courierId)
                put("status", transportRequest.status)
            }.toString()

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val body = jsonPayload.toRequestBody(mediaType)

            val request = Request.Builder()
                .url(getRestUrl())
                .header("apikey", supabaseKey)
                .header("Authorization", "Bearer $supabaseKey")
                .header("Content-Type", "application/json")
                .header("Prefer", "return=representation")
                .post(body)
                .build()

            val response = client.newCall(request).execute()
            val responseBody = response.body?.string() ?: ""

            if (!response.isSuccessful) {
                return@withContext Result.failure(IOException("Create request failed: ${response.code} $responseBody"))
            }

            val createdList = parseTransportRequestsJson(responseBody)
            val createdItem = createdList.firstOrNull() ?: transportRequest
            Result.success(createdItem)
        } catch (e: Exception) {
            Log.e(TAG, "Error creating transport request", e)
            Result.failure(e)
        }
    }

    /**
     * Updates the status of a transport request (e.g., ACCEPTED, IN_TRANSIT, DELIVERED).
     */
    suspend fun updateRequestStatus(requestId: String, newStatus: String): Result<Boolean> = withContext(Dispatchers.IO) {
        try {
            if (!isConfigured()) {
                return@withContext Result.failure(IllegalStateException("Supabase credentials not configured"))
            }

            val jsonPayload = JSONObject().apply {
                put("status", newStatus)
            }.toString()

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val body = jsonPayload.toRequestBody(mediaType)

            val request = Request.Builder()
                .url("${getRestUrl()}?id=eq.$requestId")
                .header("apikey", supabaseKey)
                .header("Authorization", "Bearer $supabaseKey")
                .header("Content-Type", "application/json")
                .header("Prefer", "return=minimal")
                .patch(body)
                .build()

            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                Result.success(true)
            } else {
                val err = response.body?.string() ?: ""
                Result.failure(IOException("Update status failed ${response.code}: $err"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error updating status for request $requestId", e)
            Result.failure(e)
        }
    }

    /**
     * Connects to Supabase Realtime WebSocket endpoint and streams transport request table updates.
     * Continuously emits updated list of TransportRequests in real-time.
     * Also incorporates a resilient periodic sync loop so subscribers receive initial data immediately.
     */
    fun getTransportRequestsRealtime(): Flow<List<TransportRequest>> = callbackFlow {
        var webSocket: WebSocket? = null

        // Trigger immediate fetch upon subscription
        val initialFetch = fetchTransportRequests()
        if (initialFetch.isSuccess) {
            initialFetch.getOrNull()?.let { trySend(it) }
        }

        if (isConfigured()) {
            val wsHost = supabaseUrl.trim().removeSuffix("/").removeSuffix("/rest/v1")
                .replace("https://", "wss://")
                .replace("http://", "ws://")
            val wsUrl = "$wsHost/realtime/v1/websocket?apikey=$supabaseKey&vsn=1.0.0"

            val wsRequest = Request.Builder()
                .url(wsUrl)
                .build()

            webSocket = client.newWebSocket(wsRequest, object : WebSocketListener() {
                override fun onOpen(webSocket: WebSocket, response: Response) {
                    Log.d(TAG, "Supabase Realtime WebSocket connected.")
                    // Subscribe to public:transport_requests table changes
                    val joinMsg = JSONObject().apply {
                        put("topic", "realtime:public:$TABLE_NAME")
                        put("event", "phx_join")
                        put("payload", JSONObject().apply {
                            put("config", JSONObject().apply {
                                put("postgres_changes", JSONArray().apply {
                                    put(JSONObject().apply {
                                        put("event", "*")
                                        put("schema", "public")
                                        put("table", TABLE_NAME)
                                    })
                                })
                            })
                        })
                        put("ref", "1")
                    }.toString()

                    webSocket.send(joinMsg)
                }

                override fun onMessage(webSocket: WebSocket, text: String) {
                    try {
                        val json = JSONObject(text)
                        val event = json.optString("event")

                        if (event == "postgres_changes" || event == "phx_reply") {
                            // On real-time update event, re-fetch fresh list and emit
                            launchFetchAndEmit()
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Error handling WebSocket message", e)
                    }
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    Log.w(TAG, "Supabase Realtime WebSocket connection issue: ${t.message}")
                }

                private fun launchFetchAndEmit() {
                    // Trigger async fetch on IO dispatcher
                    val scope = kotlinx.coroutines.CoroutineScope(Dispatchers.IO)
                    scope.launch {
                        fetchTransportRequests().getOrNull()?.let { updatedList ->
                            trySend(updatedList)
                        }
                    }
                }
            })
        }

        // Active polling loop every 5 seconds to guarantee real-time updates even if WebSockets are interrupted
        val pollingJob = kotlinx.coroutines.CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                delay(5000)
                if (isConfigured()) {
                    fetchTransportRequests().getOrNull()?.let { requests ->
                        trySend(requests)
                    }
                }
            }
        }

        awaitClose {
            pollingJob.cancel()
            webSocket?.close(1000, "Flow closed")
            Log.d(TAG, "Realtime transport requests flow closed.")
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Alias for getTransportRequestsRealtime for intuitive naming.
     */
    fun streamTransportRequests(): Flow<List<TransportRequest>> = getTransportRequestsRealtime()

    /**
     * Helper parser using Moshi with org.json fallback.
     */
    private fun parseTransportRequestsJson(jsonString: String): List<TransportRequest> {
        return try {
            jsonListAdapter.fromJson(jsonString) ?: emptyList()
        } catch (e: Exception) {
            // Fallback manual parsing via org.json if needed
            val result = mutableListOf<TransportRequest>()
            try {
                val array = JSONArray(jsonString)
                for (i in 0 until array.length()) {
                    val obj = array.getJSONObject(i)
                    result.add(
                        TransportRequest(
                            id = obj.optString("id", ""),
                            title = obj.optString("title", ""),
                            supplier = obj.optString("supplier", ""),
                            category = obj.optString("category", "AGRI INPUTS"),
                            payloadKg = obj.optInt("payload_kg", 0),
                            payoutAmount = obj.optInt("payout_amount", 0),
                            bonusAmount = obj.optInt("bonus_amount", 0),
                            distanceKm = obj.optDouble("distance_km", 0.0),
                            durationMinutes = obj.optInt("duration_minutes", 0),
                            eta = obj.optString("eta", ""),
                            pickupName = obj.optString("pickup_name", ""),
                            pickupAddress = obj.optString("pickup_address", ""),
                            pickupLandmark = obj.optString("pickup_landmark", ""),
                            pickupDistance = obj.optString("pickup_distance", ""),
                            pickupPhone = obj.optString("pickup_phone", ""),
                            dropoffName = obj.optString("dropoff_name", ""),
                            dropoffAddress = obj.optString("dropoff_address", ""),
                            dropoffLandmark = obj.optString("dropoff_landmark", ""),
                            dropoffPhone = obj.optString("dropoff_phone", ""),
                            isPrepaid = obj.optBoolean("is_prepaid", true),
                            driverName = obj.optString("driver_name", ""),
                            courierId = obj.optString("courier_id", ""),
                            status = obj.optString("status", "PENDING"),
                            createdAt = if (obj.has("created_at") && !obj.isNull("created_at")) obj.getString("created_at") else null
                        )
                    )
                }
            } catch (ex: Exception) {
                Log.e(TAG, "Failed to parse transport requests JSON", ex)
            }
            result
        }
    }
}
