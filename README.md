# pg24-bidding-system
PGNO - 24 , SE2030 assignment project - Web based bidding system(Spring Boot + MySQL + Vite)

## Project Structure

### Backend (Spring Boot)
- Location: `/backend/bidding`
- Tech Stack: Spring Boot, MySQL, WebSocket
- Port: 8080

### Frontend (Vite)
- Location: `/frontend`
- Tech Stack: Vite, Vanilla JavaScript, WebSocket
- Port: 3000

## Getting Started

### Backend Setup
1. Ensure MySQL is running
2. Update database credentials in `backend/bidding/src/main/resources/application.properties`
3. Run the Spring Boot application:
   ```bash
   cd backend/bidding
   ./mvnw spring-boot:run
   ```

### Frontend Setup
1. Install dependencies:
   ```bash
   cd frontend
   npm install
   ```
2. Start development server:
   ```bash
   npm run dev
   ```
3. Access the application at `http://localhost:3000`

## Features

### Authentication
- User registration with email and password
- Secure login system
- Session management

### Live Auctions
- View all active auctions
- Real-time bid updates via WebSocket
- Countdown timers for auction end times
- Current bid tracking

### Bidding System
- Place bids on open auctions
- Minimum bid validation
- Real-time bid notifications
- Bid history tracking

### User Interface
- Clean, modern design
- Dark/light theme toggle
- Responsive layout
- Live auction updates

## API Endpoints

### Authentication
- `POST /api/auth/register` - Register new user
- `POST /api/auth/login` - User login

### Auctions
- `GET /api/auctions` - List all auctions
- `GET /api/auctions/{id}` - Get auction details
- `POST /api/auctions` - Create new auction

### Bids
- `POST /api/bids/auction/{auctionId}` - Place a bid
- `GET /api/bids/auction/{auctionId}` - Get auction bids

## WebSocket Topics
- `/topic/auction/{auctionId}` - Real-time bid updates for specific auction

## Progress Log
- Created MySQL database `pg24_bidding` (utf8mb4, utf8mb4_unicode_ci)
- Implemented backend REST APIs for auth, auctions, and bidding
- Ravija: tested bidding api with postman (create bid, list bid, validation)
- Completed frontend with Vite, authentication, live auction listing, and real-time bidding


