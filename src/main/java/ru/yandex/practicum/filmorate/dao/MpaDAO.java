package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.mpa.MpaStorage;

import java.util.LinkedList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MpaDAO implements MpaStorage {
    private final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final JdbcTemplate jdbcTemplate;

    public List<Mpa> getAllMpa() {
        List<Mpa> mpas = new LinkedList<>();
        String sql = "select * from MPA";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql);
        while (sqlRowSet.next()){
            mpas.add(new Mpa(
                    sqlRowSet.getLong("id"),
                    sqlRowSet.getString("name")));
        }
        return mpas;
    }

    public Mpa getMpaById(Long id){
        String sql = "select * from MPA where ID = ?";
        log.info("Возвращаем строку по id из таблицы MPA!");
        return getMpaObject(jdbcTemplate.queryForRowSet(sql, id));
    }

    public Mpa getMpaObject(SqlRowSet sqlRowSet) {
        if (sqlRowSet.next()) {
            return new Mpa(
                    sqlRowSet.getLong("id"),
                    sqlRowSet.getString("name"));
        }throw new NotFoundException("Таблица MPA пустая или в нем не найдена нужная строка");
    }
}
