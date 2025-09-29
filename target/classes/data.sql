-- Demo User
INSERT IGNORE INTO users (name, username, password, role) VALUES ('User One', 'user1', '$2a$10$8K2pz5Vz5Vz5Vz5Vz5Vz5e4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'USER');

-- Demo Account
INSERT INTO account (account_number, account_holder_name, account_type, balance) VALUES ('USER1001', 'user1', 'Savings', 5000.00);
