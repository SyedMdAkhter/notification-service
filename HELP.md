# Notification Service – Help & Integration Guide

This document explains how to use the **Notification Service** from a frontend
application (for example, a Contact Us form).

The service is designed to accept user messages and send notifications
(email) asynchronously in the background.

---

## 1. Purpose

The Notification Service is used to:

- Accept messages submitted from the frontend
- Send email notifications asynchronously
- Immediately confirm submission to the user

The frontend **does not wait** for email delivery.
It only waits for confirmation that the request was accepted.

---

## 2. API Endpoint

### Submit Notification

**URL**
POST /api/v1/notifications

**Headers**
Content-Type: application/json

---

## 3. Request Body

The frontend must send the request in the following JSON format:

```json
{
  "channel": "EMAIL",
  "to": ["admin@example.com"],
  "subject": "New Contact Form Submission",
  "template": "basic-email",
  "data": {
    "message": "User message from the contact form"
  }
}

```
| Field          | Description                                   |
| -------------- | --------------------------------------------- |
| `channel`      | Notification channel (currently only `EMAIL`) |
| `to`           | List of recipient email addresses             |
| `subject`      | Email subject                                 |
| `template`     | Email template name (without `.ftl`)          |
| `data.message` | Message content entered by the user           |

---

4. Email Processing Behaviour

Email sending happens asynchronously

The API responds immediately

Email delivery may take place after the response is returned

Email failures do not block the API response

---

5. Successful Response (Frontend Important)

If the request is accepted successfully, the API responds with:

HTTP Status
202 Accepted

Response Body
{
"status": "SUCCESS",
"message": "Your message has been submitted successfully."
}
---
6. Frontend Behaviour

The frontend should:

Call the API on form submission

Read the response JSON

Display the message field to the user

User-facing message to display

Your message has been submitted successfully.

This confirms to the user that their message has been received.

7. Error Handling (Basic)

If validation fails or the request is malformed, the API may return:

400 Bad Request with validation details

500 Internal Server Error for unexpected issues

The frontend may show a generic error message such as:

Something went wrong. Please try again later.

8. Notes for Deployment

The service is Dockerized

SMTP configuration is provided via environment variables

No credentials are stored in the codebase

Works on both on-premise and cloud servers

9. Summary

Frontend submits message → API accepts request

API returns success message immediately

Email is sent in the background

User always sees a confirmation message

This ensures a fast and reliable user experience.