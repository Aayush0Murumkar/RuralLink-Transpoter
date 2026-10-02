# Rural Link Partner (Transporter App)

**Rural Link Partner** is a modern Android application built with Jetpack Compose and Material 3, designed for last-mile agricultural logistics partners, rural transporters, and electric vehicle (EV) fleet operators.

---

## Features

- **Real-Time Delivery Offers & Farmgate Matching**: Receive instantaneous transport requests from agricultural clusters and farm hubs with payout details, payload specs, distance, and automated countdown timers.
- **Dynamic Pricing Engine**: Automated logistics fare calculation with transparent breakdown of base fare, distance rate, payload weight tiers, rural road factor, and backload/return-trip bonuses.
- **Active Trip & GPS Route Tracking**: Step-by-step navigation simulation with live milestone indicators (En Route to Pickup, Goods Verified, In Transit to Hub, Out for Delivery, Completed).
- **EV Fleet Management & HSRP Support**: Register and toggle active vehicles (Electric 3-Wheelers, Cargo Pickups, Mini Trucks) featuring authentic Indian High Security Registration Plate (HSRP) visual rendering.
- **Direct UPI / BharatQR Payment Collection**: On-spot settlement modal with instant payment verification for transporter earnings.
- **Rural Link AI Copilot**: Intelligent assistant for rural logistics navigation, EV battery swap stations, toll assistance, and cargo handling guidance.
- **Supabase Realtime Sync**: Resilient integration supporting live WebSocket streaming and REST fallbacks for real-time order dispatch.

---

## Tech Stack & Architecture

- **UI & Design**: Jetpack Compose, Material 3, custom vector illustrations and canvas graphics.
- **Architecture**: MVVM with unidirectional data flow (UDF), Kotlin Coroutines, and `StateFlow`.
- **Networking**: Retrofit, OkHttp, Moshi, WebSocket client for Supabase Realtime and Groq LLaMA-3 completions.
- **Persistence & Secrets**: Secrets Gradle Plugin with `.env` / `.env.example`.

---

## Getting Started

### 1. Prerequisites

- Android Studio Ladybug / Meerkat or later
- JDK 17 or higher
- Android SDK with API 36 compile SDK (Minimum API 24)

### 2. Configuration

Copy `.env.example` to `.env` and fill in your service credentials:

```bash
cp .env.example .env
```

| Variable | Description |
| :--- | :--- |
| `SUPABASE_URL` | Supabase project URL (e.g., `https://your-project.supabase.co`) |
| `SUPABASE_ANON_KEY` | Supabase anon/public key |
| `GROQ_API_KEY` | Groq API key for AI Copilot (optional) |
| `GEMINI_API_KEY` | Gemini API key (optional) |

> **Note:** The application includes intelligent local offline fallback data, so the app remains fully functional and navigable even without live backend credentials configured.

### 3. Build & Run

To assemble the debug build using Gradle:

```bash
gradle assembleDebug
```

To run unit tests:

```bash
gradle :app:testDebugUnitTest
```
