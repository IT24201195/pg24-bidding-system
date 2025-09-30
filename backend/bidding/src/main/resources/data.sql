-- Drop old tables if re-running
SET FOREIGN_KEY_CHECKS=0;
DROP TABLE IF EXISTS notification_outbox;
DROP TABLE IF EXISTS payments;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS bids;
DROP TABLE IF EXISTS auctions;
DROP TABLE IF EXISTS users;
SET FOREIGN_KEY_CHECKS=1;

-- USERS
CREATE TABLE users (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       username VARCHAR(100) NOT NULL UNIQUE,
                       password_hash VARCHAR(100) NOT NULL,
                       role ENUM('ADMIN','SELLER','BUYER') NOT NULL,
                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- AUCTIONS
CREATE TABLE auctions (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          seller_id BIGINT NOT NULL,
                          title VARCHAR(200) NOT NULL,
                          description TEXT,
                          start_price DECIMAL(12,2) NOT NULL,
                          min_increment DECIMAL(12,2) NOT NULL,
                          reserve_price DECIMAL(12,2) NULL,
                          start_time TIMESTAMP NOT NULL,
                          end_time TIMESTAMP NOT NULL,
                          status ENUM('DRAFT','ACTIVE','CLOSED') NOT NULL DEFAULT 'DRAFT',
                          current_highest_bid_id BIGINT NULL,
                          version BIGINT NOT NULL DEFAULT 0,
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
                          CONSTRAINT fk_auction_seller FOREIGN KEY (seller_id) REFERENCES users(id)
) ENGINE=InnoDB;

-- BIDS
CREATE TABLE bids (
                      id BIGINT PRIMARY KEY AUTO_INCREMENT,
                      auction_id BIGINT NOT NULL,
                      bidder_id BIGINT NOT NULL,
                      amount DECIMAL(12,2) NOT NULL,
                      created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                      CONSTRAINT fk_bid_auction FOREIGN KEY (auction_id) REFERENCES auctions(id),
                      CONSTRAINT fk_bid_bidder FOREIGN KEY (bidder_id) REFERENCES users(id)
) ENGINE=InnoDB;

-- Link auction.highest_bid
ALTER TABLE auctions
    ADD CONSTRAINT fk_auction_highest_bid
        FOREIGN KEY (current_highest_bid_id) REFERENCES bids(id);

-- ORDERS
CREATE TABLE orders (
                        id BIGINT PRIMARY KEY AUTO_INCREMENT,
                        auction_id BIGINT NOT NULL,
                        buyer_id BIGINT NOT NULL,
                        total DECIMAL(12,2) NOT NULL,
                        status ENUM('PENDING','COMPLETED','CANCELED') NOT NULL DEFAULT 'PENDING',
                        created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                        updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
                        CONSTRAINT fk_order_auction FOREIGN KEY (auction_id) REFERENCES auctions(id),
                        CONSTRAINT fk_order_buyer FOREIGN KEY (buyer_id) REFERENCES users(id)
) ENGINE=InnoDB;

-- PAYMENTS
CREATE TABLE payments (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          order_id BIGINT NOT NULL,
                          method ENUM('BANK_TRANSFER') NOT NULL,
                          slip_url VARCHAR(500) NULL,
                          status ENUM('SUBMITTED','VERIFIED','REJECTED') NOT NULL DEFAULT 'SUBMITTED',
                          created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
                          CONSTRAINT fk_payment_order FOREIGN KEY (order_id) REFERENCES orders(id)
) ENGINE=InnoDB;

-- NOTIFICATION OUTBOX
CREATE TABLE notification_outbox (
                                     id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                     user_id BIGINT NOT NULL,
                                     channel ENUM('EMAIL','IN_APP') NOT NULL,
                                     subject VARCHAR(200) NOT NULL,
                                     body TEXT NOT NULL,
                                     attempts INT NOT NULL DEFAULT 0,
                                     status ENUM('PENDING','SENT','FAILED') NOT NULL DEFAULT 'PENDING',
                                     created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                     updated_at TIMESTAMP NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
                                     CONSTRAINT fk_outbox_user FOREIGN KEY (user_id) REFERENCES users(id)
) ENGINE=InnoDB;

-- Indexes
CREATE INDEX idx_bids_auction_amount ON bids (auction_id, amount DESC, created_at DESC);
CREATE INDEX idx_auctions_status_end ON auctions (status, end_time);
CREATE INDEX idx_orders_buyer ON orders (buyer_id);

-- Summary view
DROP VIEW IF EXISTS v_auction_summary;
CREATE VIEW v_auction_summary AS
SELECT a.id AS auction_id, a.title, a.status, a.end_time,
       u.username AS seller_username,
       (SELECT b.amount FROM bids b WHERE b.auction_id = a.id
        ORDER BY b.amount DESC, b.created_at DESC LIMIT 1) AS highest_bid
FROM auctions a
         JOIN users u ON u.id = a.seller_id;
USE bidding;

-- USERS
INSERT INTO users (email, username, password_hash, role) VALUES
                                                             ('admin@example.com','admin','admin123','ADMIN'),
                                                             ('seller1@example.com','seller1','seller123','SELLER'),
                                                             ('seller2@example.com','seller2','seller123','SELLER'),
                                                             ('buyer1@example.com','buyer1','buyer123','BUYER'),
                                                             ('buyer2@example.com','buyer2','buyer123','BUYER'),
                                                             ('buyer3@example.com','buyer3','buyer123','BUYER');

SET @s1 = (SELECT id FROM users WHERE username='seller1');
SET @s2 = (SELECT id FROM users WHERE username='seller2');

-- AUCTIONS
INSERT INTO auctions (seller_id,title,description,start_price,min_increment,reserve_price,start_time,end_time,status)
VALUES
    (@s1,'iPhone 13','128GB, Good condition',120000,2000,125000,NOW(),DATE_ADD(NOW(),INTERVAL 1 HOUR),'ACTIVE'),
    (@s1,'Gaming Laptop','Ryzen 7, RTX 3060',350000,5000,360000,NOW(),DATE_ADD(NOW(),INTERVAL 2 HOUR),'ACTIVE'),
    (@s2,'Canon EOS M50','Mirrorless camera',150000,2000,0,NOW(),DATE_ADD(NOW(),INTERVAL 90 MINUTE),'ACTIVE'),
    (@s2,'Motorcycle Helmet','DOT certified',30000,1000,0,NOW(),DATE_ADD(NOW(),INTERVAL 45 MINUTE),'ACTIVE'),
    (@s2,'Smart TV 55\"','4K UHD',240000,3000,250000,NOW(),DATE_ADD(NOW(),INTERVAL 3 HOUR),'ACTIVE');

-- BIDS
SET @a1 = (SELECT id FROM auctions WHERE title='iPhone 13');
SET @a2 = (SELECT id FROM auctions WHERE title='Gaming Laptop');
SET @a3 = (SELECT id FROM auctions WHERE title='Canon EOS M50');
SET @a4 = (SELECT id FROM auctions WHERE title='Motorcycle Helmet');
SET @a5 = (SELECT id FROM auctions WHERE title='Smart TV 55\"');

SET @b1 = (SELECT id FROM users WHERE username='buyer1');
SET @b2 = (SELECT id FROM users WHERE username='buyer2');
SET @b3 = (SELECT id FROM users WHERE username='buyer3');

INSERT INTO bids (auction_id, bidder_id, amount) VALUES
                                                     (@a1,@b1,122000),(@a1,@b2,124000),(@a1,@b3,126500),
                                                     (@a2,@b1,352000),(@a2,@b2,358000),
                                                     (@a3,@b2,151000),(@a3,@b1,154000),(@a3,@b3,156000),
                                                     (@a4,@b3,31000),(@a4,@b1,32000),
                                                     (@a5,@b2,241000),(@a5,@b3,246000);

-- Sync highest bid pointer
UPDATE auctions a
    JOIN (SELECT auction_id, id FROM (
                                         SELECT auction_id, id, ROW_NUMBER() OVER (PARTITION BY auction_id ORDER BY amount DESC, created_at DESC) rn
                                         FROM bids
                                     ) x WHERE rn=1) hb
    ON hb.auction_id = a.id
SET a.current_highest_bid_id = hb.id;

USE bidding;
SELECT * FROM v_auction_summary ORDER BY end_time ASC LIMIT 10;
