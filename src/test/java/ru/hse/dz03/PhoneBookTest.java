package ru.hse.dz03;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.hse.dz03.library.Contact;
import ru.hse.dz03.library.PhoneBook;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PhoneBookTest {
    PhoneBook phoneBook;
    SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");

    @Test
    @DisplayName("Инициализация по неверному пути")
    void initOnTheIncorrectPath() {
        String path = "src/main/resources/PhoneBook1223.txt";
        IOException ex = assertThrows(IOException.class, () -> new PhoneBook(path));
        assertEquals("Записной книжки по такому пути не существует", ex.getMessage());
    }

    @BeforeEach
    void init() throws IOException {
        // чистим записную книжку
        String path = "src/main/resources/PhoneBook.txt";
        phoneBook = new PhoneBook(path);
        phoneBook.cleanPhoneBook();
    }

    @Test
    @DisplayName("Парсинг некорректно введенной даты")
    void parseIncorrectDate() {
        ParseException ex = assertThrows(ParseException.class, () -> formatter.parse("testIncorrect"));
        assertEquals("Unparseable date: \"testIncorrect\"", ex.getMessage());
    }

    @Test
    @DisplayName("Сериалазация контакта")
    void serializeContact() throws ParseException, IOException {
        Date testDate = formatter.parse("01.01.2001");
        List<String> testList = new ArrayList<>();
        testList.add("0000000000");
        Contact contact1 = new Contact("testName", "testSurname", "testPatronymic",
                "testAddress", "testEmail", testDate, testList, 1);
        String strSerialize = phoneBook.serializeContact(contact1);
        assertEquals("{\"name\":\"testName\",\"surname\":\"testSurname\",\"patronymic\":" +
                "\"testPatronymic\",\"address\":\"testAddress\",\"email\":\"testEmail\",\"id\":1," +
                "\"birthdate\":978296400000,\"numbers\":[\"0000000000\"]}", strSerialize);
    }

    @Test
    @DisplayName("Десериалазация контакта")
    void deserializeContact() throws IOException, ParseException {
        Contact desContact = phoneBook.deserializeContact("{\"name\":\"testName\",\"surname\":" +
                "\"testSurname\",\"patronymic\":" + "\"testPatronymic\",\"address\":\"testAddress\",\"" +
                "email\":\"testEmail\",\"id\":1," + "\"birthdate\":978296400000,\"numbers\":[\"0000000000\"]}");
        List<String> testList = new ArrayList<>();
        testList.add("0000000000");
        Date testDate = formatter.parse("01.01.2001");
        Contact expectContact = new Contact("testName", "testSurname", "testPatronymic",
                "testAddress", "testEmail", testDate, testList, 1);

        assertEquals(expectContact, desContact);
    }


    @Test
    @DisplayName("Добавление уникального контакта в телефонную книгу")
    void addContactToPhoneBook() throws IOException, ParseException {
        int size = phoneBook.showAllContacts().size();
        Date testDate = formatter.parse("01.01.2001");
        List<String> testList = new ArrayList<>();
        testList.add("89858432274");
        Contact contact1 = new Contact("Aleksandr", "Pushkin", "Sergeevich",
                "testAddress", "testEmail", testDate, testList, 1);
        phoneBook.addContact(contact1);
        int sizeAfterAdd = phoneBook.showAllContacts().size();
        assertEquals(1, sizeAfterAdd - size);
    }

    @Test
    @DisplayName("Попытка добавления дубликата в телефонную книгу")
    void addDuplicateContactToPhoneBook() throws IOException, ParseException {
        Date testDate = formatter.parse("01.01.2001");
        List<String> testList = new ArrayList<>();
        testList.add("89858432274");
        Contact contact1 = new Contact("Aleksandr", "Pushkin", "Sergeevich",
                "testAddress", "testEmail", testDate, testList, 1);
        phoneBook.addContact(contact1);
        int size = phoneBook.showAllContacts().size();

        Contact contact2 = new Contact("Viktoria", "Zolotovskaya", "Vadimovna",
                "123", "kilop@yandex.ru", testDate, testList, 1);
        phoneBook.addContact(contact2);
        int sizeAfterAdd = phoneBook.showAllContacts().size();
        assertEquals(0, sizeAfterAdd - size);
    }

    @Test
    @DisplayName("Удаление контакта, когда телефонная книга пуста")
    void removeContactFromEmptyPhoneBook() throws IOException {
        assertEquals(0, phoneBook.showAllContacts().size());
        phoneBook.removeContact("Kirill");
        assertEquals(0, phoneBook.showAllContacts().size());
    }

    @Test
    @DisplayName("Удаление контакта, которого не существует в телефонной книге")
    void removeNonExistentContactFromPhoneBook() throws IOException, ParseException {
        Date testDate = formatter.parse("01.01.2001");
        List<String> testList = new ArrayList<>();
        testList.add("89858432274");
        Contact contact1 = new Contact("Aleksandr", "Pushkin", "Sergeevich",
                "testAddress", "testEmail", testDate, testList, 1);
        phoneBook.addContact(contact1);
        int size = phoneBook.showAllContacts().size();
        phoneBook.removeContact("Kirill");
        assertEquals(size, phoneBook.showAllContacts().size());
    }

    @Test
    @DisplayName("Удаление контакта, существующего в телефонной книге")
    void removeContactFromPhoneBook() throws IOException, ParseException {
        Date testDate = formatter.parse("01.01.2001");
        List<String> testList = new ArrayList<>();
        testList.add("89858432274");
        Contact contact1 = new Contact("Aleksandr", "Pushkin", "Sergeevich",
                "testAddress", "testEmail", testDate, testList, 1);
        phoneBook.addContact(contact1);
        int size = phoneBook.showAllContacts().size();
        phoneBook.removeContact("Aleksandr");
        assertEquals(size, phoneBook.showAllContacts().size() + 1);
    }


    @Test
    @DisplayName("Поиск контактов по ФИО")
    void filterContactsByName() throws IOException, ParseException {
        Date testDate = formatter.parse("01.01.2001");
        List<String> testList1 = new ArrayList<>();
        testList1.add("89858432274");
        List<String> testList2 = new ArrayList<>();
        testList2.add("89858432275");
        List<String> testList3 = new ArrayList<>();
        testList3.add("89858432276");
        List<String> testList4 = new ArrayList<>();
        testList4.add("89858432277");
        Contact contact1 = new Contact("Aleksandr", "Pushkin", "Sergeevich",
                "testAddress", "testEmail", testDate, testList1, 1);
        phoneBook.addContact(contact1);
        Contact contact2 = new Contact("aleksandr", "Pushkin", "Sergeevich",
                "testAddress", "testEmail", testDate, testList2, 2);
        phoneBook.addContact(contact2);
        Contact contact3 = new Contact("ALEKSAndR", "Pushkin", "Sergeevich",
                "testAddress", "testEmail", testDate, testList3, 3);
        phoneBook.addContact(contact3);
        Contact contact4 = new Contact("Kirill", "Pushkin", "Sergeevich",
                "testAddress", "testEmail", testDate, testList4, 4);
        phoneBook.addContact(contact4);
        int filterSize = phoneBook.searchByName("aleksandr").size();
        assertEquals(3, filterSize);
    }

    @Test
    @DisplayName("Поиск контактов по Дате рождения")
    void filterContactsByBirthdate() throws IOException, ParseException {
        Date testDate1 = formatter.parse("11.01.2001");
        Date testDate2 = formatter.parse("11.02.2001");
        Date testDate3 = formatter.parse("21.01.2001");
        Date testDate4 = formatter.parse("11.02.2001");
        List<String> testList1 = new ArrayList<>();
        testList1.add("89858432274");
        List<String> testList2 = new ArrayList<>();
        testList2.add("89858432275");
        List<String> testList3 = new ArrayList<>();
        testList3.add("89858432276");
        List<String> testList4 = new ArrayList<>();
        testList4.add("89858432277");
        Contact contact1 = new Contact("Aleksandr", "Pushkin", "Sergeevich",
                "testAddress", "testEmail", testDate1, testList1, 1);
        phoneBook.addContact(contact1);
        Contact contact2 = new Contact("aleksandr", "Pushkin", "Sergeevich",
                "testAddress", "testEmail", testDate2, testList2, 2);
        phoneBook.addContact(contact2);
        Contact contact3 = new Contact("ALEKSAndR", "Pushkin", "Sergeevich",
                "testAddress", "testEmail", testDate3, testList3, 3);
        phoneBook.addContact(contact3);
        Contact contact4 = new Contact("Kirill", "Pushkin", "Sergeevich",
                "testAddress", "testEmail", testDate4, testList4, 4);
        phoneBook.addContact(contact4);
        int filterSize = phoneBook.searchByBirthdate("11.02").size();
        assertEquals(2, filterSize);
    }

    @Test
    @DisplayName("Поиск контактов по номеру")
    void filterContactsByNumber() throws IOException, ParseException {
        Date testDate1 = formatter.parse("11.01.2001");
        List<String> testList1 = new ArrayList<>();
        testList1.add("89858432274");
        testList1.add("89858432274");
        testList1.add("89858432274");
        List<String> testList2 = new ArrayList<>();
        testList2.add("84322744444");
        testList2.add("99999999999");
        List<String> testList3 = new ArrayList<>();
        testList3.add("32279843227");
        List<String> testList4 = new ArrayList<>();
        testList4.add("89859432277");
        Contact contact1 = new Contact("Aleksandr", "Pushkin", "Sergeevich",
                "testAddress", "testEmail", testDate1, testList1, 1);
        phoneBook.addContact(contact1);
        Contact contact2 = new Contact("aleksandr", "Pushkin", "Sergeevich",
                "testAddress", "testEmail", testDate1, testList2, 2);
        phoneBook.addContact(contact2);
        Contact contact3 = new Contact("ALEKSAndR", "Pushkin", "Sergeevich",
                "testAddress", "testEmail", testDate1, testList3, 3);
        phoneBook.addContact(contact3);
        Contact contact4 = new Contact("Kirill", "Pushkin", "Sergeevich",
                "testAddress", "testEmail", testDate1, testList4, 4);
        phoneBook.addContact(contact4);
        int filterSize = phoneBook.searchByNumbers("843227").size();
        assertEquals(3, filterSize);
    }

}