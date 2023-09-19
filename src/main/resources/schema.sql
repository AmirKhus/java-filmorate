drop all objects;

CREATE TABLE IF NOT EXISTS `users` (
  `id` integer PRIMARY KEY,
  `email` varchar(20),
  `login` varchar(30),
  `birthday` timestamp,
  `name` varchar(30)
);

CREATE TABLE IF NOT EXISTS `genres` (
  `id` integer PRIMARY KEY,
  `name` varchar(255)
);

CREATE TABLE IF NOT EXISTS `MPA` (
  `id` integer PRIMARY KEY,
  `name` varchar(10)
);

CREATE TABLE IF NOT EXISTS `friends` (
  `user_id` integer,
  `friend_id` integer,
  `is_friends` boolean
);

CREATE TABLE IF NOT EXISTS `film_genre` (
  `film_id` integer,
  `genre_id` integer
);

CREATE TABLE IF NOT EXISTS `films` (
  `id` integer PRIMARY KEY,
  `name` varchar(20),
  `description` varchar(100),
  `releaseDate` timestamp,
  `duration` varchar(100),
  `rate` integer,
  `mpa_id` integer
);

CREATE TABLE IF NOT EXISTS `likes` (
  `film_id` integer,
  `user_id` integer
);

ALTER TABLE `friends` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

ALTER TABLE `friends` ADD FOREIGN KEY (`friend_id`) REFERENCES `users` (`id`);

ALTER TABLE `films` ADD FOREIGN KEY (`mpa_id`) REFERENCES `MPA` (`id`);

ALTER TABLE `likes` ADD FOREIGN KEY (`film_id`) REFERENCES `films` (`id`);

ALTER TABLE `likes` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);

ALTER TABLE `film_genre` ADD FOREIGN KEY (`film_id`) REFERENCES `films` (`id`);

ALTER TABLE `film_genre` ADD FOREIGN KEY (`genre_id`) REFERENCES `genres` (`id`);
