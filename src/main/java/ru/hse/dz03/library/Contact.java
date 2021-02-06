package ru.hse.dz03.library;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;

// TODO переделать без аннотации @JsonAutoDetect
@JsonAutoDetect
public class Contact {

    private String name;
    private String surname;
    private String patronymic;
    private String address;
    private String email;
    // уникальный идентификатор контакта
    private int id;
    private Date birthdate;
    private List<String> numbers;

    private Contact() {
    }

    public Contact(String name, String surname, String patronymic, String address,
                   String email, Date birthdate, List<String> numbers, int id) {
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.address = address;
        this.email = email;
        this.birthdate = birthdate;
        this.numbers = numbers;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public List<String> getNumbers() {
        return Collections.unmodifiableList(numbers);
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public String getSurname() {
        return surname;
    }

    /**
     * Переопределенный метод для получения полной информации о контакте
     *
     * @return вся информация
     */
    @Override
    public String toString() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        return "\nФИО: " + surname + " " + name + " " + patronymic + "\n" +
                "Адрес: " + address + "\nПочта: " + email + "\n" +
                "День рождения: " + formatter.format(birthdate) + "\nНомера: " + numbers + "\n\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contact contact = (Contact) o;
        return id == contact.id &&
                Objects.equals(name, contact.name) &&
                Objects.equals(surname, contact.surname) &&
                Objects.equals(patronymic, contact.patronymic) &&
                Objects.equals(address, contact.address) &&
                Objects.equals(email, contact.email) &&
                Objects.equals(birthdate, contact.birthdate) &&
                Objects.equals(numbers, contact.numbers);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname, patronymic, address, email, id, birthdate, numbers);
    }
}
