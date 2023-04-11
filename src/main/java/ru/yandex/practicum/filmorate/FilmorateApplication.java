package ru.yandex.practicum.filmorate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.dao.FilmDao;

@SpringBootApplication
public class FilmorateApplication {

	public static void main(String[] args) {
		/*ApplicationContext applicationContext =	*/SpringApplication.run(FilmorateApplication.class, args);
		/*FilmDao dao  = applicationContext.getBean(FilmDao.class);
		dao.createTable();*/

	}
}
