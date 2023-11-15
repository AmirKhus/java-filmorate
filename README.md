# Filmorate
# ***ER-диаграмма***
![er-diagram.png](src%2Fmain%2Fresources%2Fer-diagram.png)

# ***Примеры запросов***
<details>
  <summary><h3>Для пользователей:</h3></summary>

* создание пользователя
```SQL
INSERT INTO users (email, login, name, birthday)
VALUES ( ?, ?, ?, ? );
```
* редактирование пользователя
```SQL
UPDATE users
SET email = ?,
    login = ?,
    name = ?,
    birthday = ?
WHERE user_id = ?
```
* получение списка всех пользователей
```SQL
SELECT *
FROM users
```

</details>

<details>
  <summary><h3>Для фильмов:</h3></summary>

* создание фильма
```SQL
INSERT INTO films (name, description, release_date, duration_in_minutes, mpa_rating_id)
VALUES (?, ?, ?, ?, ?)
```
* редактирование фильма
```SQL
UPDATE films
SET name = ?,
    description = ?,
    release_date = ?,
    duration_in_minutes = ?,
    mpa_rating_id = ?
WHERE film_id = ?
```
* получение списка всех фильмов
```SQL
SELECT films.*, mpa_rating.mpa_name, COUNT(film_likes.user_id) AS rate
FROM films
LEFT JOIN mpa_rating ON films.mpa_rating_id = mpa_rating.mpa_rating_id
LEFT JOIN film_likes ON films.film_id = film_likes.film_id
GROUP BY films.film_id
ORDER BY films.film_id
```

</details>
