# heyCoach 🧠💼  
**Your Smart Career Assistant – Powered by Spring Boot**

**heyCoach** is a backend-only Spring Boot application that empowers job seekers with intelligent tools to navigate the job market more effectively. It offers real-time job market trend analysis, tailored resume generation with modern templates, and AI-driven mock interview questions – all driven by live job data from LinkedIn and advanced AI models.

---

## 🚀 Key Features

### 🔍 Real-Time Job Market Trends
- **Gemini API** integration to analyze and summarize market trends based on job search queries.
- Understand trending skills, technologies, and job titles in your domain.

### 🔗 LinkedIn Job Search API Integration
- Retrieves job listings based on user-defined keywords.
- Feeds job descriptions into the resume generator and mock interview modules.

### 📄 Resume Generator with Templates
- Dynamically generates ATS-friendly resumes tailored to specific job descriptions.
- Automatically formats the resume using **professional templates**.
- Uses NLP to highlight relevant skills and experience for the role.

### 🎯 AI-Based Mock Interview Preparation
- Provides targeted mock interview questions based on the job description.
- Covers technical, behavioral, and HR rounds using context-aware AI.

---

## 🛠️ Tech Stack

| Technology       | Purpose                              |
|------------------|--------------------------------------|
| **Java 17+**      | Core language                        |
| **Spring Boot**   | Backend framework                    |
| **MongoDB**       | NoSQL database for storing user/job data |
| **Gemini API**    | Market trend and NLP intelligence    |
| **LinkedIn API**  | Real-time job listings               |
| **Swagger UI**    | API testing and documentation        |
| **Lombok**        | Boilerplate code reduction           |
| **Maven**         | Dependency management                |

---

## 📂 Folder Structure

heyCoach/
├── src/main/java/com/heycoach
│ ├── controller/ # API endpoints
│ ├── service/ # Business logic
│ ├── model/ # MongoDB entities
│ ├── repository/ # MongoDB interfaces
│ └── config/ # Swagger & API config
├── resources/
│ ├── templates/ # Resume templates
│ └── application.properties

yaml
Copy
Edit

---

## 📑 Sample API Endpoints

| Method | Endpoint                    | Description                              |
|--------|-----------------------------|------------------------------------------|
| `POST` | `/api/jobmarket`            | Get job market trends using Gemini       |
| `POST` | `/api/resume/generate`      | Generate a custom resume (PDF/Docx)      |
| `POST` | `/api/mock-interview`       | Get mock interview questions             |
| `GET`  | `/swagger-ui/index.html`    | Swagger UI for API documentation         |

---

## ⚙️ Getting Started

### Prerequisites
- Java 17+
- Maven
- MongoDB running locally or on Atlas
- API Keys for:
  - LinkedIn API
  - Gemini (Google AI/PaLM)
  - (Optional) Resume template service or OpenAI if fallback

### Installation

```bash
git clone https://github.com/Vikrant25122004/heyCoach.git
cd heyCoach
mvn clean install
mvn spring-boot:run
Configure your application.properties:

properties
Copy
Edit
linkedin.api.key=YOUR_KEY
gemini.api.key=YOUR_KEY
spring.data.mongodb.uri=mongodb://localhost:27017/heyCoach
