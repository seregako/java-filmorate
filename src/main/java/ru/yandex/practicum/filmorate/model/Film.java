package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import lombok.Data;
import org.springframework.data.relational.core.sql.In;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.*;

//@Data
public class Film {
    @JsonIgnore
    Set<Integer> likes = new HashSet();//Сет с уникальными айдишниками лайкнувших
    private int id;

    @NotBlank()
    private String name;

    @Size(max = 200)
    private String description;

    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate releaseDate;

    @Positive
    private int duration;

    Set<Genre> genres = new HashSet<>();

    Mpa mpa;

    private int rating;

  //  @AssertTrue
    private boolean dateValidator;//Валидатор даты релиза

    public Film(String name, String description, LocalDate releaseDate, int duration, Mpa mpa, Set <Genre> genres) {
        this.name = name;
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.dateValidator = releaseDate.isAfter(LocalDate.of(1895, 12, 28));
        this.mpa = mpa;
        this.genres = genres;
    }

    public Film() {
        //this.dateValidator = releaseDate.isAfter(LocalDate.of(1895, 12, 28));
    }
    private List <Genre> genresList(List <Integer> genresIds){
        List <Genre> genresList = new ArrayList<>();
        for (Integer id: genresIds){
            genresList.add(new Genre(id));
        }
        return genresList;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public int getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return "Film{" +
                "likes=" + likes +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", releaseDate=" + releaseDate +
                ", duration=" + duration +
               ", genres=" + genres +
                ", mpa=" + mpa +
                ", rating=" + rating +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Film film = (Film) o;
        return getId() == film.getId() && getDuration() == film.getDuration() && dateValidator == film.dateValidator &&
                Objects.equals(getName(), film.getName()) && Objects.equals(getDescription(), film.getDescription()) &&
                Objects.equals(getReleaseDate(), film.getReleaseDate());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getDescription(), getReleaseDate(), getDuration(), dateValidator);
    }

    public int getRate() {
        return likes.size();
    }

    public Set<Integer> getLikes() {
        return likes;
    }

    public void setLikes(Set<Integer> likes) {
        this.likes = likes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setReleaseDate(LocalDate releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

   public Set<Genre> getGenres() {
        return genres;
    }

    public void setGenres(Set<Genre> genres) {
       this.genres = genres;
  }

    public Mpa getMpa() {
        return mpa;
    }

    public void setMpa(Mpa mpa) {
        this.mpa = mpa;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public boolean isDateValidator() {
        return dateValidator;
    }

    public void setDateValidator(boolean dateValidator) {
        this.dateValidator = dateValidator;
    }

}