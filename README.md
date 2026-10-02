# 🚚 Rural Link Partner — Transporter App

> **Empowering rural logistics through intelligent transport matching, real-time delivery tracking, and AI-assisted fleet operations.**

**Rural Link Partner** is an Android application designed for **rural transporters, agricultural logistics partners, and EV fleet operators**. It connects transporters with delivery opportunities from farmers and rural businesses while providing intelligent pricing, trip tracking, vehicle management, and AI-powered assistance.

---

## 🌾 Why Rural Link?

Rural logistics often faces challenges such as:

* 🚛 Limited availability of reliable transporters
* 📦 Empty return trips and unused vehicle capacity
* 💰 Unpredictable transportation costs
* 🛣️ Poor rural road conditions
* 📍 Difficulty finding nearby delivery opportunities
* 🔋 EV fleet management challenges
* 📞 Manual coordination between farmers and transporters

**Rural Link** aims to create a technology-driven logistics ecosystem where transporters can discover suitable delivery jobs, optimize their trips, and manage their fleet from a single application.

---

## ✨ Key Features

### 📦 1. Real-Time Delivery Offers

Transporters can receive delivery requests with important information such as:

* Pickup location
* Delivery destination
* Cargo type
* Cargo weight
* Required vehicle type
* Estimated distance
* Expected payout
* Delivery deadline
* Countdown timer

This helps transporters quickly identify suitable jobs.

---

### 🤖 2. Intelligent Transport Matching

The platform considers multiple factors when matching transporters with delivery requests:

* 📍 Pickup proximity
* 🛣️ Route similarity
* 📦 Available vehicle capacity
* 🚛 Vehicle type
* 📏 Distance
* ⏰ Delivery time
* 🔄 Return-trip opportunities
* ⭐ Transporter rating

The goal is to reduce unnecessary empty trips and improve vehicle utilization.

---

### 💰 3. Dynamic Pricing Engine

The application provides a transparent fare calculation based on factors such as:

* Base fare
* Distance
* Cargo weight
* Rural road conditions
* Vehicle type
* Return-trip/backload availability

This allows transporters to understand how their payout is calculated.

---

### 🗺️ 4. Active Trip & Route Tracking

Transporters can track the progress of an active delivery through different milestones:

```text
En Route to Pickup
        ↓
Goods Verified
        ↓
In Transit to Hub
        ↓
Out for Delivery
        ↓
Completed
```

The interface provides a clear view of the current delivery stage.

---

### 🚛 5. Fleet Management

Transporters can manage multiple vehicles from the application.

Supported vehicle categories include:

* ⚡ Electric 3-Wheelers
* 🛻 Cargo Pickups
* 🚚 Mini Trucks

The application also includes vehicle registration and HSRP-style registration plate visualization.

---

### 💳 6. UPI / BharatQR Payment Collection

The platform supports digital payment workflows for transporter settlements.

Transporters can:

* View delivery earnings
* Collect payments
* Verify payment status
* Track completed settlements

---

### 🤖 7. Rural Link AI Copilot

The built-in AI assistant provides contextual assistance for rural logistics operations.

Potential use cases include:

* 🗺️ Navigation assistance
* 🔋 EV battery-swap guidance
* 🛣️ Toll assistance
* 📦 Cargo handling guidance
* 🚚 Transport-related questions

The AI layer is designed to act as a digital assistant for transport partners.

---

### ⚡ 8. Supabase Realtime

The application uses **Supabase Realtime** to support live updates between the logistics system and transporter application.

This enables:

* Real-time delivery offers
* Live order updates
* Transporter status updates
* Backend synchronization

REST fallbacks can also be used when required.

---

## 🏗️ Architecture

The application follows an **MVVM architecture** with **Unidirectional Data Flow (UDF)**.

```text
┌──────────────────────────────┐
│          UI Layer            │
│      Jetpack Compose        │
└──────────────┬───────────────┘
               │
               ▼
┌──────────────────────────────┐
│       ViewModel Layer        │
│     StateFlow + Coroutines   │
└──────────────┬───────────────┘
               │
               ▼
┌──────────────────────────────┐
│      Repository Layer        │
│   Business & Data Logic      │
└──────────────┬───────────────┘
               │
       ┌───────┴────────┐
       ▼                ▼
┌─────────────┐  ┌──────────────┐
│  Supabase   │  │ External APIs│
│  Realtime   │  │ AI / Network │
└─────────────┘  └──────────────┘
```

---

## 🛠️ Tech Stack

| Category                | Technology                     |
| ----------------------- | ------------------------------ |
| Platform                | Android                        |
| Language                | Kotlin                         |
| UI                      | Jetpack Compose                |
| Design System           | Material 3                     |
| Architecture            | MVVM + UDF                     |
| State Management        | StateFlow                      |
| Async Programming       | Kotlin Coroutines              |
| Networking              | Retrofit                       |
| HTTP Client             | OkHttp                         |
| JSON Serialization      | Moshi                          |
| Backend                 | Supabase                       |
| Realtime                | Supabase Realtime / WebSockets |
| AI                      | Groq / LLaMA & Gemini          |
| Build System            | Gradle                         |
| Minimum Android Version | API 24                         |
| Compile SDK             | API 36                         |
| JDK                     | 17+                            |

---

## 📁 Project Structure

```text
RuralLink-Transpoter/
│
├── app/
│   └── src/
│       └── main/
│           ├── java/
│           ├── res/
│           └── AndroidManifest.xml
│
├── gradle/
│
├── build.gradle.kts
├── gradle.properties
├── settings.gradle.kts
├── metadata.json
│
└── README.md
```

---

## 🚀 Getting Started

### Prerequisites

Before running the project, make sure you have:

* Android Studio **Ladybug / Meerkat or newer**
* JDK **17 or higher**
* Android SDK
* Android SDK Platform **API 36**
* Android device or emulator running **API 24+**

---

## 📥 Installation

### 1. Clone the repository

```bash
git clone https://github.com/Aayush0Murumkar/RuralLink-Transpoter.git
```

### 2. Open the project

Open the cloned folder in **Android Studio**.

Allow Gradle to sync and download the required dependencies.

### 3. Configure environment variables

Create your environment configuration based on the project's environment template.

Example:

```env
SUPABASE_URL=your_supabase_project_url
SUPABASE_ANON_KEY=your_supabase_anon_key

GROQ_API_KEY=your_groq_api_key
GEMINI_API_KEY=your_gemini_api_key
```

> ⚠️ Never commit private API keys or secrets to GitHub.

### 4. Build the application

Using Gradle:

```bash
gradle assembleDebug
```

Or build and run directly from Android Studio.

---

## 🧪 Running Tests

Run the unit tests using:

```bash
gradle :app:testDebugUnitTest
```

---

## 🔄 Application Workflow

```text
Transporter
     │
     ▼
Login / Registration
     │
     ▼
Transporter Dashboard
     │
     ├───────────────┐
     │               │
     ▼               ▼
Delivery Offers    Fleet
     │             Management
     ▼
Accept Delivery
     │
     ▼
Pickup
     │
     ▼
Goods Verification
     │
     ▼
Transit
     │
     ▼
Delivery
     │
     ▼
Payment Verification
     │
     ▼
Trip Completed
```

---

## 🎯 Target Users

Rural Link Partner is designed for:

* 🚚 Rural transporters
* 🌾 Agricultural logistics partners
* 🛻 Small commercial vehicle owners
* ⚡ EV fleet operators
* 📦 Rural delivery operators
* 🏪 Local businesses requiring transportation

---

## 🌱 Future Scope

The platform can be extended with:

* 🧠 ML-based demand prediction
* 🗺️ Advanced route optimization
* 📡 Offline-first logistics support
* 🌦️ Weather-aware route planning
* ⛽ Fuel consumption prediction
* 🔋 EV battery-range prediction
* 📊 Transporter performance analytics
* 🔄 Automated return-load matching
* 🌐 Multi-language voice interface
* 🛰️ Advanced GPS tracking
* 🔐 Stronger identity and vehicle verification

---

## 🔐 Security

The application is designed to keep sensitive credentials outside the source code through environment-based configuration.

Recommended production practices include:

* Secure API key management
* Supabase Row Level Security
* Authentication-based authorization
* Encrypted communication
* Minimal exposure of sensitive transporter data

---

## 📌 Project Status

**🚧 Active Development**

Rural Link Partner is currently being developed as a prototype for a rural logistics platform. Some features may use simulated or fallback data during development.

---

## 🤝 Contributing

Contributions are welcome.

1. Fork the repository
2. Create a feature branch

```bash
git checkout -b feature/your-feature
```

3. Commit your changes

```bash
git commit -m "Add your feature"
```

4. Push the branch

```bash
git push origin feature/your-feature
```

5. Open a Pull Request

---

## 📄 License

This project currently does not specify a separate open-source license.

If you intend to make the project open source, consider adding an appropriate license such as MIT, Apache 2.0, or GPL.

---

## 👨‍💻 Developer

**Aayush Murumkar**

Built with ❤️ to explore technology-driven solutions for rural transportation and logistics.

---

## ⭐ Support

If you find this project interesting, consider giving the repository a ⭐ on GitHub and sharing it with others interested in rural technology and logistics.

**Rural Link — Connecting Rural Transport with Opportunity.**
