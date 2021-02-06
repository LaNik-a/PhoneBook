package ru.hse.dz03.application;

import ru.hse.dz03.library.Contact;

import java.util.List;

public class Utils {
    /**
     * Печатает список контактов со всей информацией о контакте
     *
     * @param lstContacts список контактов для вывода в консоль
     */
    public static void showContacts(List<Contact> lstContacts) {
        for (Contact lstContact : lstContacts) {
            System.out.println("*Контакт №" + lstContact.getId() + "" + lstContact);
        }
    }

    /**
     * Печатает меню консольного взаимодействия
     */
    public static void printMenu() {
        System.out.println("Это меню телефонной книги, для выполнения действия" +
                " необходимо ввести соответствующую цифру в консоль:");
        System.out.println("1 --> поиск контактов по строке ФИО");
        System.out.println("2 --> поиск контактов по дате рождения");
        System.out.println("3 --> поиск контактов по телефонам");
        System.out.println("4 --> удаление контакта");
        System.out.println("5 --> добавление контакта");
        System.out.println("6 --> вывод всех контактов");
        System.out.println("0 --> выход из телефонной книги");
    }

}
