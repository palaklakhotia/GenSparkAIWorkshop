---
mode: agent
---
You are an AI software architecture assistant. Based on the complete PRD and the following additional technical and non-functional requirements, your task is to:
Identify and list the required features, respecting the system constraints (performance, concurrency, real-time behavior).
Categorize all changes under the following three headings:
Changes in Frontend
Changes in Backend
Changes in Database
1. Changes in Frontend
Implement a calendar UI with performance optimization to load availability data within 2 seconds.
Show real-time updates:
If another user books a time slot, that slot should be auto-disabled/marked in real-time.
Use WebSockets, Firebase Realtime Database, or other pub-sub mechanisms to push live slot updates to connected clients.
Ensure graceful UX for concurrency:
If a user tries to book a slot that is already taken, provide clear UI feedback and refresh calendar availability immediately.

Implement UX states:
Loading spinners, confirmation modals, error messages on failure, and immediate calendar updates on success.
Exclude any support for:
Rescheduling, cancellations, or recurring bookings.
Adding bookings to external calendars like Google Calendar.
Instruction for AI: Specify client-side technologies, how to connect to WebSockets or Firebase listeners, and how to architect the state to refresh availability efficiently.
2. Changes in Backend
Respecting concurrency, real-time updates, and performance goals, make the following backend changes:
Booking API: POST /api/bookings
Must be protected (auth required).
Must be atomic: within a single transaction, create the booking record and update availability.
Handle concurrency using:
Optimistic locking (with version fields or timestamps),
or Pessimistic locking (database-level or Redis locks),
or serialized transactions (ACID-compliant DB behavior).

If two users attempt to book the same slot, ensure only the first request succeeds. Return 409 Conflict for the rest.

Availability API: GET /api/availability
Must respond within < 2 seconds for a given service.
Support indexed queries for fast range-based time slot retrieval.
Real-Time Updates:
On booking success, publish an event (SlotBooked).
Use a message broker (e.g., Kafka, RabbitMQ, or Redis Streams) to notify:
Frontend clients (via WebSockets / Socket.io gateway).
Notification service.
Notification Service:
Upon booking success, send emails to:
The consumer (booking confirmation).
The provider (appointment notification).

Ensure delivery is non-blocking and occurs asynchronously.

Avoid implementing:
APIs or logic for rescheduling, cancellation, recurring bookings, or group bookings.

Instruction for AI: Propose concurrency-safe transaction handling, real-time communication strategies, and non-blocking async communication. Use SOLID principles and isolate service responsibilities.
3. Changes in Database
Propose the following schema-level changes and optimizations while ensuring race conditions are eliminated and lookups are fast:
a. Bookings Table
Fields:
booking_id (UUID)
user_id, service_id, provider_id
date, time_slot, price
status (booked, failed)
created_at
Constraint:
Unique composite key on (service_id, date, time_slot) to prevent double-booking.

b. Availability Table
Fields:
availability_id (UUID)
service_id, date, time_slot
is_booked (boolean)
updated_at

Ensure atomic update with booking table using single transaction or locking mechanism.
c. Notification Log TableFor observability and failure tracking.
Fields:
notification_id, booking_id, recipient, email_type, status, sent_at

d. Indexing & Performance
Add indexes on:
service_id + date
time_slot
Consider partitioning by service_id or date to optimize queries under heavy load.
Instruction for AI: Ensure schema is optimized for both read and write throughput, and atomic operations for availability + booking creation.

AI Behavior Guidelines
Do not propose features that are out of scope:

No cancellations, recurring bookings, group bookings, or calendar integrations.

Propose architecture and code-level suggestions that respect:
Concurrency safety
Real-time responsiveness
Sub-2-second performance
Output must be developer-actionable, divided clearly under the three headings, and mapped to the services or modules to be created or extended.


Prompt 1: Build an Interactive Real-Time Availability Calendar Component
Prompt:
Create a fully responsive Availability Calendar UI component using React and TailwindCSS. The calendar must:
1. Display both weekly and monthly view toggles.
2. Visually differentiate time slots by status:
   - Green: Available
   - Red: Booked
   - Grey: Unavailable
3. Only allow selecting a time slot if it is marked as "available".
4. Automatically disable past dates to prevent invalid selections.
5. On clicking an available slot, emit a callback:
   onSlotSelected(dateTimeSlot)

Props:
slots: Array of objects
  Example:
  [
    { date: "2025-07-20", time: "10:00", status: "available" },
    { date: "2025-07-20", time: "11:00", status: "booked" },
    ...
  ]
loading: boolean — if true, show a centered loading spinner.
selectedDate: optional — for date navigation or highlighting.
UX:
Display a “No slots available” message if all shown slots are booked.
Ensure accessibility (ARIA roles, keyboard nav) and full mobile responsiveness.
Use TailwindCSS for styling — clean, padded, readable layout.
Goal:
To provide a real-time calendar view with dynamic interaction and a simple developer interface.

Prompt 2: Create a Booking Confirmation Modal Component
Prompt:
Build a booking confirmation modal in React using Headless UI or Radix UI. It should trigger when a time slot is selected and must handle user interaction smoothly.
Props:
{
  isOpen: boolean,
  onClose: () => void,
  onConfirm: (slot) => void,
  slot: { date: string, time: string },
  service: { name: string, price: number },
  isConfirming: boolean,
  isConfirmed: boolean,
  errorMessage?: string
}

Functionality:
1. Display service name, date, time, and price.
2. Two buttons:
   - "Confirm Booking" → calls onConfirm(slot)
   - "Cancel" → calls onClose()
3. While confirming, show a loading spinner on the "Confirm" button.
4. On success (isConfirmed becomes true), auto-close the modal.
5. On error (errorMessage provided), display it clearly inside the modal.
6. Use soft shadows, bold buttons, and modal transitions for modern UX.
Goal:
To ensure a smooth confirmation step with clean transitions, validation, and error feedback.
Prompt 3: Create Real-Time Booking Status Handling with API & UI Sync
Prompt:
Implement frontend logic for booking a selected time slot and synchronizing the calendar in real-time.

Functionality:
1. Booking API Integration:
   - Use Axios or Fetch to call:
     POST /api/bookings
     Payload: { date, time, serviceId }
     Headers: Authorization token


2. Atomic UI State Handling:
   - Disable "Confirm" button after first click to prevent double submission.
   - Show loading spinner while waiting for the API.
   - On success:
     - Update the local calendar state to mark the slot as booked.
     - Show a UI message: "Booking confirmed – email sent"
   - On failure:
     - If due to concurrency (slot already booked), show error message:
       "Slot just got booked by another user."
     - Refresh calendar data from the server.

3. Real-Time Availability Sync:
   - Connect to WebSocket or Firestore listener.
   - On receiving updates, refresh the calendar component state immediately.

Goal:
Ensure smooth and reactive booking logic that handles API integration, real-time conflicts, and optimistic UI feedback.
Prompt 4: Integrate Real-Time Updates with WebSocket or Firestore
Prompt:
Enhance the calendar component with real-time data synchronization using Firebase Firestore or Socket.io.
Functional Requirements:
1. Real-Time Subscription:
   - On component mount, subscribe to a slot status channel:
     - Firebase: Listen to a Firestore collection for slot updates.
     - Socket.io: Connect to a namespace/channel like /availability.

2. Event Handling:
   - On receiving an event/update (e.g., slot status changed to "booked"):
     - Update the local slot state.
     - Re-render the calendar with the new availability.

3. User-Side Conflict Handling:
   - If the user is viewing a slot that becomes booked during selection:
     - Disable the "Confirm Booking" button.
     - Show a non-blocking alert: "This slot was just booked by someone else."

4. Cleanup:
   - Unsubscribe/close listeners on component unmount to avoid memory leaks.


UX Expectation:
Calendar should feel live and dynamic without requiring a refresh.
Matches the responsiveness of tools like Calendly.
Goal:
Push the app UX to real-time standards using push-based data architecture.
This full suite of prompts empowers a frontend developer or an AI agent to build a modern, reactive, and user-friendly real-time booking interface, step-by-step:
Prompt 1: Calendar UI
Prompt 2: Booking Confirmation Modal
Prompt 3: Real-time API Handling & Booking Logic
Prompt 4: Real-time Sync with Firestore/WebSockets

