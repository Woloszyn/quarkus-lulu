CREATE TABLE IF NOT EXISTS tournament_match (
  id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
  home_team_subscription_id BIGINT NOT NULL,
  versus_team_subscription_id BIGINT NOT NULL,
  winner_team_subscription_id BIGINT,
  scoreboard VARCHAR(100),
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (home_team_subscription_id) REFERENCES tournament_subscription(id),
    FOREIGN KEY (versus_team_subscription_id) REFERENCES tournament_subscription(id),
    FOREIGN KEY (winner_team_subscription_id) REFERENCES tournament_subscription(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;