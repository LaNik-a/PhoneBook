package ru.hse.dz03.application;

import ru.hse.dz03.library.Contact;
import ru.hse.dz03.library.PhoneBook;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Application {
    private static final Logger logger = LogManager.getLogger();
    private static int id = 1;
    private static final String path = "src/main/resources/PhoneBook.txt";

    public static void main(String[] args) {
        logger.info("Приложение стартовало");
        PhoneBook phoneBook;
        try {
            phoneBook = new PhoneBook(path);
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            logger.error("Сообщение исключения", ex);
            return;
        }
        Scanner sc = new Scanner(System.in);
        while (true) {
            Utils.printMenu();
            String inputStr = sc.next();
            logger.info("Пользователь ввёл строку " + inputStr);
            switch (inputStr) {
                case "1":
                    System.out.println("Введите строку для поиска подходящих контактов по ФИО");
                    String inputName = sc.next();
                    logger.info("Пользователь ввёл часть ФИО " + inputName);
                    logger.info("Демонстрация отфильтрованных контактов");
                    Utils.showContacts(phoneBook.searchByName(inputName));
                    logger.info("Автоматический переход в главное меню...");
                    break;
                case "2":
                    System.out.println("Введите строку для поиска подходящих контактов по дате рождения");
                    String inputBirthdate = sc.next();
                    logger.info("Пользователь ввёл часть дня рождения " + inputBirthdate);
                    logger.info("Демонстрация отфильтрованных контактов");
                    Utils.showContacts(phoneBook.searchByBirthdate(inputBirthdate));
                    logger.info("Автоматический переход в главное меню...");
                    break;
                case "3":
                    System.out.println("Введите строку для поиска подходящих контактов по номеру");
                    String inputNumber = sc.next();
                    logger.info("Пользователь ввёл часть номера " + inputNumber);
                    logger.info("Демонстрация отфильтрованных контактов");
                    Utils.showContacts(phoneBook.searchByNumbers(inputNumber));
                    logger.info("Автоматический переход в главное меню...");
                    break;
                case "4":
                    System.out.println("Введите фамилию или имя или отчество контакта для удаления");
                    String str = sc.next();
                    logger.info("Пользователь ввёл часть ФИО для удаления контакта " + str);
                    try {
                        if (!phoneBook.removeContact(str)) {
                            logger.info("Пользователь ввёл часть ФИО, которое есть у нескольких контактов");
                            System.out.println("Вверху список подходящих контактов");
                            System.out.println("Для удаления введите номер контакта, для отмены любой набор букв");
                            if (sc.hasNextInt()) {
                                int num = sc.nextInt();
                                logger.info("Пользователь ввёл часть номер контакта для удаления: " + num);
                                if (phoneBook.removeContactByIndex(num)) {
                                    System.out.println("Контакт был успешно удален!");
                                    logger.info("Произошло успешное удаление контакта");
                                } else {
                                    System.out.println("Контакт удалить не удалось!");
                                    logger.info("Удаление контакта не произошло");
                                }
                            } else {
                                logger.info("Пользователь отменил удаление контакта");
                                break;
                            }
                        }
                    } catch (IOException ex) {
                        System.out.println("Ошибка при удалении контакта\n" + ex.getMessage());
                        logger.error("Сообщение исключения", ex);
                    }
                    logger.info("Автоматический переход в главное меню...");
                    break;
                // TODO сделать через regex проверку на корректность введенных данных
                case "5":
                    if (phoneBook.showAllContacts().size() > 0) {
                        id = phoneBook.showAllContacts().get(phoneBook.showAllContacts().size() - 1).getId();
                        logger.info("Чтение последнего id контакта в телефонной книге " + id);
                    }
                    id++;
                    System.out.println("Введите имя:");
                    String name = sc.next();
                    logger.info("Пользователь ввел имя для нового контакта " + name);
                    System.out.println("Введите фамилию:");
                    String surname = sc.next();
                    logger.info("Пользователь ввел фамилию для нового контакта " + surname);
                    System.out.println("Введите отчество");
                    String patronymic = sc.next();
                    logger.info("Пользователь ввел отчество для нового контакта " + patronymic);
                    System.out.println("Введите адрес");
                    String address = sc.next();
                    logger.info("Пользователь ввел адресс для нового контакта " + address);
                    System.out.println("Введите email");
                    String email = sc.next();
                    logger.info("Пользователь ввел почту для нового контакта " + email);
                    System.out.println("Введите день рождения в формате \"dd.MM.yyyy\"");
                    String birthdate = sc.next();
                    logger.info("Пользователь ввел дату рождения для нового контакта " + birthdate);
                    SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
                    Date date;
                    try {
                        logger.info("Парс введенной пользователем даты");
                        date = formatter.parse(birthdate);
                    } catch (ParseException e) {
                        System.out.println("Дата введена некорректно, контакт не может быть добавлен");
                        logger.error("Сообщение исключения", e);
                        break;
                    }
                    logger.info("Парс введенной пользователем даты закончился успешно");
                    System.out.println("Вводите номера пока не введете 0");
                    List<String> numbers = new ArrayList<>();
                    String number = sc.next();
                    logger.info("Пользователь ввел номер для нового контакта " + number);
                    while (!number.equals("0")) {
                        numbers.add(number);
                        number = sc.next();
                        logger.info("Пользователь ввел ещё номер для нового контакта " + number);
                    }
                    Contact newContact = new Contact(name, surname, patronymic, address, email,
                            date, numbers, id);
                    logger.info("Произошло создание нового контакта");
                    try {
                        if (phoneBook.addContact(newContact)) {
                            logger.info("Успешное добавление контакта в телефонную книгу");
                            System.out.println("Контакт был успешно добавлен!");
                        } else {
                            logger.info("Неудачное добавление контакта в телефонную книгу");
                            System.out.println("Контакт добавить не удалось!");
                        }
                    } catch (IOException ex) {
                        System.out.println("Контакт добавить не удалось!\n" + ex.getMessage());
                        logger.error("Сообщение исключения", ex);
                    }
                    logger.info("Автоматический переход в главное меню...");
                    break;
                case "6":
                    Utils.showContacts(phoneBook.showAllContacts());
                    logger.info("Автоматический переход в главное меню...");
                    break;
                case "0":
                    System.out.println("\n\nКорректное закрытие блокнота!");
                    logger.info("Выход из процесса взаимодействия с телефонной книгой...");
                    return;
            }
        }

    }


}
