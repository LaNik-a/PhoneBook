package ru.hse.dz03.library;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.hse.dz03.application.Utils;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PhoneBook {
    private final String path;
    private List<Contact> contacts = new ArrayList<>();

    public PhoneBook(String path) throws IOException {
        this.path = path;
        readContactsFromPhoneBook();
    }

    /**
     * Записывает контакт в телефонную книгу
     *
     * @param contact контакт для записи
     * @param append  способ записи в файл
     * @throws IOException в случае ошибки при записи
     */
    public void writeContactToPhoneBook(Contact contact, boolean append) throws IOException {
        try (FileWriter writer = new FileWriter(path, append)) {
            String serializedContact = serializeContact(contact);
            writer.write(serializedContact);
            writer.append("\n");
            writer.flush();
        } catch (IOException ex) {
            throw new IOException("Ошибка при записи контакта!");
        }
    }

    /**
     * Сериализация контакта
     *
     * @param contact контакт который надо сериализовать
     * @return json строка
     * @throws IOException исключение, возникшее в процессе сериализации
     */
    public String serializeContact(Contact contact) throws IOException {
        // писать результат сериализации будем во Writer(StringWriter)
        StringWriter writer = new StringWriter();
        // это объект Jackson, который выполняет сериализацию
        ObjectMapper mapper = new ObjectMapper();
        // сама сериализация: 1-куда, 2-что
        mapper.writeValue(writer, contact);
        // преобразовываем все записанное во StringWriter в строку
        return writer.toString();
    }

    /**
     * Чтение всех контактов из телефонной книги и добавление их в список
     *
     * @throws IOException при чтении телефонной книги
     */
    public void readContactsFromPhoneBook() throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            throw new IOException("Записной книжки по такому пути не существует");
        }
        try (FileReader reader = new FileReader(path);
             BufferedReader br = new BufferedReader(reader)) {
            String line;
            while ((line = br.readLine()) != null) {
                contacts.add(deserializeContact(line));
            }
        } catch (IOException ex) {
            throw new IOException("Ошибка при чтении записной книжки!");
        }
    }

    /**
     * Десериализация контакта из строки
     *
     * @param strContact строка json
     * @return Обьект класса Contact
     * @throws IOException в процессе десериализации
     */
    public Contact deserializeContact(String strContact) throws IOException {
        StringReader reader = new StringReader(strContact);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(reader, Contact.class);
    }

    /**
     * Поиск контактов по ФИО
     *
     * @param str часть ФИО
     * @return список подходящих контактов
     */
    public List<Contact> searchByName(String str) {
        String lowStr = str.toLowerCase();
        return contacts.stream().filter(
                x -> x.getName().toLowerCase().contains(lowStr) ||
                        x.getSurname().toLowerCase().contains(lowStr) ||
                        x.getPatronymic().toLowerCase().contains(lowStr)).collect(Collectors.toList());
    }

    /**
     * Поиск контактов по дате рождения
     *
     * @param date часть даты рождения
     * @return список подходящих контактов
     */
    public List<Contact> searchByBirthdate(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        return contacts.stream().filter(
                x -> formatter.format(x.getBirthdate()).contains(date)).collect(Collectors.toList());
    }

    /**
     * Поиск контактов по номеру
     *
     * @param strNumber часть номера
     * @return список подходящих контактов
     */
    public List<Contact> searchByNumbers(String strNumber) {
        return contacts.stream()
                .filter(x -> x.getNumbers().stream().anyMatch(t -> t.contains(strNumber)))
                .collect(Collectors.toList());
    }

    /**
     * Возвращает список всех контактов
     *
     * @return список всех контактов
     */
    public List<Contact> showAllContacts() {
        return contacts;
    }

    /**
     * Удаление контакта по поданной строке
     *
     * @param str часть ФИО
     * @return true - удалось удалить или нечего удалять
     * false - непонятно какой контакт удалять
     * @throws IOException в процессе перезаписи телефонной книги
     */
    public boolean removeContact(String str) throws IOException {
        List<Contact> lstFilterContacts = searchByName(str);
        // непонятно что удалять
        if (lstFilterContacts.size() > 1) {
            Utils.showContacts(lstFilterContacts);
            return false;
        } else {
            // успешно удалили
            if (lstFilterContacts.size() == 1) {
                contacts.remove(lstFilterContacts.get(0));
                rewritePhoneBook();
            }
            // нечего удалять
            return true;
        }

    }

    /**
     * Удаление контакта по уникальному id контакта
     *
     * @param id уникальный идентификатор контакта
     * @return true - удаление прошло успешно
     * false - удаление не произошло
     * @throws IOException в процессе перезаписи телефонной книги
     */
    public boolean removeContactByIndex(int id) throws IOException {
        int index = -1;
        for (int i = 0; i < contacts.size(); i++) {
            if (contacts.get(i).getId() == id) {
                index = i;
            }
        }
        if (index >= 0 && index < contacts.size()) {
            contacts.remove(index);
            rewritePhoneBook();
            return true;
        }
        return false;
    }

    /**
     * Добавление контакта
     *
     * @param contact сам контакт
     * @return true - добавить удалось, false - добавить не удалось так как дубликат
     * @throws IOException в процессе записи в телефонную книгу
     */
    public boolean addContact(Contact contact) throws IOException {
        boolean existNumber = false;
        for (int i = 0; i < contact.getNumbers().size(); i++) {
            String number = contact.getNumbers().get(i);
            if (contacts.stream().anyMatch(t -> t.getNumbers().contains(number))) {
                existNumber = true;
                break;
            }
        }
        if (!existNumber) {
            this.contacts.add(contact);
            writeContactToPhoneBook(contact, true);
            return true;
        }
        return false;
    }

    /**
     * Перезаписывает телефонную книжку из списка
     *
     * @throws IOException в процессе записи
     */
    public void rewritePhoneBook() throws IOException {
        FileWriter writer = new FileWriter(path, false);
        writer.flush();
        for (Contact contact : contacts) {
            writeContactToPhoneBook(contact, true);
        }
    }

    /**
     * Чистит телефонную книгу от контактов
     *
     * @throws IOException в процессе получения доступа к содержимому файла
     */
    public void cleanPhoneBook() throws IOException {
        FileWriter writer = new FileWriter(path, false);
        writer.flush();
        contacts = new ArrayList<>();
    }
}
