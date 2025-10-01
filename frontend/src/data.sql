-- Insert sample users
INSERT INTO users (email, username, password, role, created_at, updated_at) VALUES
                                                                                ('seller1@example.com', 'seller1', 'password123', 'SELLER', GETDATE(), GETDATE()),
                                                                                ('bidder1@example.com', 'bidder1', 'password123', 'BIDDER', GETDATE(), GETDATE()),
                                                                                ('bidder2@example.com', 'bidder2', 'password123', 'BIDDER', GETDATE(), GETDATE()),
                                                                                ('admin@example.com', 'admin', 'admin123', 'ADMIN', GETDATE(), GETDATE());

-- Insert sample auctions
INSERT INTO auctions (title, description, starting_price, current_bid, seller_id, highest_bidder_id, start_time, end_time, status, created_at, updated_at) VALUES
                                                                                                                                                               ('Vintage Camera', 'Beautiful vintage camera from 1970s, fully functional', 50.00, 75.00, 1, 2, DATEADD(HOUR, -2, GETDATE()), DATEADD(HOUR, 1, GETDATE()), 'ACTIVE', GETDATE(), GETDATE()),
                                                                                                                                                               ('Antique Watch', 'Rare antique pocket watch from 1920s', 100.00, 150.00, 1, 3, DATEADD(HOUR, -1, GETDATE()), DATEADD(HOUR, 2, GETDATE()), 'ACTIVE', GETDATE(), GETDATE()),
                                                                                                                                                               ('Smartphone', 'Latest smartphone model, brand new', 200.00, NULL, 1, NULL, GETDATE(), DATEADD(DAY, 7, GETDATE()), 'ACTIVE', GETDATE(), GETDATE()),
                                                                                                                                                               ('Expired Auction', 'This auction has expired for testing', 10.00, NULL, 1, NULL, DATEADD(DAY, -2, GETDATE()), DATEADD(HOUR, -1, GETDATE()), 'ACTIVE', GETDATE(), GETDATE());

-- Insert sample bids
INSERT INTO bids (auction_id, bidder_id, amount, bid_time, is_winning_bid) VALUES
                                                                               (1, 2, 60.00, DATEADD(MINUTE, -90, GETDATE()), false),
                                                                               (1, 3, 75.00, DATEADD(MINUTE, -60, GETDATE()), false),
                                                                               (2, 2, 120.00, DATEADD(MINUTE, -45, GETDATE()), false),
                                                                               (2, 3, 150.00, DATEADD(MINUTE, -30, GETDATE()), false);