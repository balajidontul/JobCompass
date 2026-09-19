# JobCompass

JobCompass is a Java Spring Boot project designed to automate the full job application workflow using a candidate's resume, live Naukri job listings, and AI-powered matching.

## Objective

The main goal of this project is to build an intelligent job application assistant that can:

- read and analyze a candidate's resume profile,
- search for relevant jobs on Naukri,
- compare the resume with each job requirement using an LLM,
- select the best jobs to apply for,
- automatically submit applications on Naukri for the recommended jobs.

The system is built around a simple but powerful idea: the resume becomes the candidate profile, Naukri becomes the job marketplace, and the LLM acts as the decision engine that decides which jobs are worth applying to and whether the application should be submitted automatically.

## Proposed workflow

1. Resume input
   - The user provides a resume file path or resume details.
   - The system extracts skills, work experience, education, location, notice period, preferred role, and other profile data.

2. Job discovery
   - The application searches Naukri for jobs using keywords, location, experience, and filters.
   - Relevant jobs are collected from the Naukri job feed.

3. AI-based matching
   - The candidate's profile and each job description are sent to the LLM.
   - The LLM evaluates whether the candidate is a good fit for the role.
   - It ranks jobs by match quality and decides which ones are strong candidates for applying.

4. Automated application
   - Once the best jobs are identified, the system triggers the Naukri application flow automatically.
   - The AI can decide whether the profile is a good match and whether to apply, rather than simply suggesting jobs.

5. Recommendation and apply result
   - The system returns ranked jobs, match reasons, and apply status.
   - This helps reduce wasted effort by focusing only on jobs with high fit and by automating the apply process where appropriate.

## Core idea

JobCompass combines the following components:

- Resume/profile data
- Naukri job listing data
- LLM-based decision-making
- Automated application execution on Naukri

This turns the project from a simple job search tool into an AI-powered job application assistant that tries to reduce manual effort and improve application quality.

## Key features

- Resume-driven job matching
- Naukri job discovery and crawling
- AI-based ranking of jobs based on fit
- Automatic decision to apply or skip a role
- End-to-end job application automation workflow
- Extensible design for future improvements like resume tailoring, job tracking, and auto-follow-up

## System design

A typical flow looks like this:

Resume/Profile -> Job Search -> AI Matching -> Apply Decision -> Naukri Application

Where:

- Resume/Profile is the candidate context
- Job Search gathers job opportunities from Naukri
- AI Matching evaluates alignment with role requirements
- Apply Decision determines whether the job is worth applying to
- Naukri Application executes the job submission process automatically

## Tech stack

- Java
- Spring Boot
- Gradle
- OpenAI API integration
- Naukri integration and automation flow

## Project structure

- `src/main/java/com/jobcompass/naukri/controller` — API controllers for the application workflow
- `src/main/java/com/jobcompass/naukri/service` — business logic for search, matching, and automation
- `src/main/java/com/jobcompass/naukri/config` — configuration classes for external integrations
- `src/main/java/com/jobcompass/naukri/dto` — data models for requests and responses
- `src/main/resources/application.yaml` — application configuration

## Example use case

A user uploads a resume with Java, Spring Boot, AWS, and 4 years of experience. The system searches Naukri for backend roles, sends the resume and job descriptions to the LLM, and the LLM decides that:

- Java Backend Developer — Strong match, apply automatically
- Senior Java Developer — Good match, apply
- Full Stack Developer — Weak match, skip

The system filters out low-fit jobs and submits applications only for the best opportunities.

## Run the application

```bash
./gradlew bootRun
```

## Notes

This project is intended to be an AI-powered job application automation system focused on Naukri. Its long-term goal is to minimize manual job searching and improve hiring efficiency by automatically identifying and applying to relevant roles that best match the candidate profile.
