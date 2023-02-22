package org.example;

import java.io.*;
import java.math.BigDecimal;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        Main main = new Main();
        List<Manufacturer> manufacturers = new ArrayList<>();
        List<Souvenir> souvenirs = new ArrayList<>();
        ListIterator listIterator;
        Scanner scanner = new Scanner(System.in);

        int choice = -1;
        do {
            System.out.println("""
                    Choose what action you want to do:
                    1 - Add manufacturer
                    2 - Add souvenir
                    3 - Change manufacturer
                    4 - Change souvenir
                    5 - Get all manufacturers
                    6 - Get all souvenirs
                    7 - Get all souvenirs from specified manufacturer
                    8 - Get all souvenirs from specified country
                    9 - Get all manufacturers whose souvenirs price less than specified
                    10 - Get all manufacturers and their souvenirs
                    11 - Get info about manufacturer of specified souvenir, which was made in specified year
                    12 - Get all souvenirs for every year
                    13 - Delete specified manufacturer and his souvenirs
                    0 - Exit
                    """);
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("Enter how many manufacturers you want to add:");
                    int amount = scanner.nextInt();
                    scanner.nextLine();

                    IntStream.range(0, amount).forEach(i -> {
                        System.out.println("Please enter manufacturer name:");
                        String name = scanner.nextLine();
                        System.out.println("Please enter manufacturer country:");
                        String country = scanner.nextLine();
                        manufacturers.add(new Manufacturer(name, country));
                    });
                    main.writeManufacturersToFile(manufacturers);

                    break;
                case 2:
                    System.out.println("Enter how many souvenirs you want to add:");
                    int amount1 = scanner.nextInt();
                    scanner.nextLine();

                    IntStream.range(0, amount1).forEach(i -> {
                        System.out.println("Please enter souvenir name:");
                        String name = scanner.nextLine();
                        System.out.println("Please enter souvenir creation date(y-m-d):");
                        LocalDate date = LocalDate.parse(scanner.nextLine());
                        System.out.println("Please enter souvenir price:");
                        BigDecimal price = scanner.nextBigDecimal();
                        scanner.nextLine();
                        System.out.println("Choose souvenir manufacturer:");
                        System.out.println("--------------------------------------------");
                        Iterator iterator = main.getAllManufacturers().listIterator();
                        while (iterator.hasNext()) {
                            System.out.println(iterator.next());
                        }
                        System.out.println("--------------------------------------------");
                        System.out.println("Please enter manufacturer name:");
                        String name1 = scanner.nextLine();
                        System.out.println("Please enter manufacturer country:");
                        String country1 = scanner.nextLine();
                        if (main.checkIfManufacturerExists(name1, country1)) {
                            souvenirs.add(new Souvenir(name, date, price, new Manufacturer(name1, country1)));
                        } else {
                            System.out.println("Manufacturer with such name and country doesnt exist so he is added!");
                            Manufacturer manufacturer = new Manufacturer(name1, country1);
                            manufacturers.add(manufacturer);
                            main.writeManufacturersToFile(manufacturers);
                            souvenirs.add(new Souvenir(name, date, price, manufacturer));
                        }
                    });
                    main.writeSouvenirsToFile(souvenirs);

                    break;


                case 3:
                    System.out.println("Enter name of manufacturer which info you want to change:");
                    String name = scanner.nextLine();
                    System.out.println("Enter country of manufacturer which info you want to change:");
                    String country1 = scanner.nextLine();
                    System.out.println("--------------------------------------------");
                    main.changeManufacturer(name, country1, scanner);
                    break;

                case 4:
                    System.out.println("Enter name of souvenir which info you want to change:");
                    String sName = scanner.nextLine();
                    System.out.println("Enter creation date of souvenir which info you want to change:");
                    LocalDate date = LocalDate.parse(scanner.nextLine());
                    System.out.println("Enter price of souvenir which info you want to change:");
                    BigDecimal price = scanner.nextBigDecimal();
                    System.out.println("Enter manufacturer name of souvenir which info you want to change:");
                    String mName = scanner.nextLine();
                    System.out.println("Enter manufacturer country of souvenir which info you want to change:");
                    String country = scanner.nextLine();
                    System.out.println("--------------------------------------------");
                    main.changeSouvenir(sName, date, price, new Manufacturer(mName, country), scanner);
                    break;

                case 5:
                    System.out.println("All manufacturers:");
                    System.out.println("--------------------------------------------");
                    listIterator = main.getAllManufacturers().listIterator();
                    while (listIterator.hasNext()) {
                        System.out.println(listIterator.next());
                    }
                    System.out.println("--------------------------------------------");

                    break;
                case 6:
                    System.out.println("All souvenirs:");
                    System.out.println("--------------------------------------------");
                    listIterator = main.getAllSouvenirs().listIterator();
                    while (listIterator.hasNext()) {
                        System.out.println(listIterator.next());
                    }
                    System.out.println("--------------------------------------------");

                    break;
            }
        }
        while (choice != 0);
    }

    private void writeManufacturersToFile(List<Manufacturer> manufacturers) {

        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("manufacturers.txt", true))) {
            objectOutputStream.writeObject(manufacturers);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void writeSouvenirsToFile(List<Souvenir> souvenirs) {

        try (ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("souvenirs.txt", true))) {
            objectOutputStream.writeObject(souvenirs);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private List<Manufacturer> getAllManufacturers() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("manufacturers.txt"))) {
            return (List<Manufacturer>) objectInputStream.readObject();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            return null;

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Souvenir> getAllSouvenirs() {

        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("souvenirs.txt"))) {
            return (List<Souvenir>) objectInputStream.readObject();

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            return null;

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void changeManufacturer(String name, String country, Scanner scanner) {
        if (checkIfManufacturerExists(name, country)) {
            List<Manufacturer> manufacturers = getAllManufacturers();
            ListIterator listIterator = manufacturers.listIterator();
            System.out.println("Please enter new manufacturer name:");
            String newName = scanner.nextLine();
            System.out.println("Please enter new manufacturer country:");
            String newCountry = scanner.nextLine();
            while (listIterator.hasNext()) {
                Manufacturer manufacturer = (Manufacturer) listIterator.next();
                if (manufacturer.getMName().equals(name) && manufacturer.getCountry().equals(country)) {
                    listIterator.set(new Manufacturer(newName, newCountry));

                }
            }

            writeManufacturersToFile(manufacturers);
            System.out.println("Manufacturer successfully changed!!!");
        } else {
            System.out.println("Manufacturer with such name and country isn't found!!!");
        }
    }


    private Manufacturer getManufacturer(String name, String country) {
        Manufacturer manufacturer = new Manufacturer();
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("manufacturers.txt"))) {
            List<Manufacturer> manufacturers = (List<Manufacturer>) objectInputStream.readObject();
            ListIterator listIterator = manufacturers.listIterator();
            while (listIterator.hasNext()) {
                Manufacturer manufacturer1 = (Manufacturer) listIterator.next();
                if (manufacturer1.getMName().equals(name) && manufacturer.getCountry().equals(country)) {
                    manufacturer.setMName(manufacturer1.getMName());
                    manufacturer.setCountry(manufacturer1.getCountry());
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return manufacturer;
    }

    private Boolean checkIfManufacturerExists(String name, String country) {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("manufacturers.txt"))) {
            List<Manufacturer> manufacturers = (List<Manufacturer>) objectInputStream.readObject();
            ListIterator listIterator = manufacturers.listIterator();
            while (listIterator.hasNext()) {
                Manufacturer manufacturer = (Manufacturer) listIterator.next();
                if (manufacturer.getMName().equals(name) && manufacturer.getCountry().equals(country)) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return false;

    }

    private void changeSouvenir(String name, LocalDate date, BigDecimal price, Manufacturer manufacturer, Scanner scanner) {
        if (checkIfSouvenirExists(name, date, price, manufacturer)) {
            List<Souvenir> souvenirs = getAllSouvenirs();
            ListIterator listIterator = souvenirs.listIterator();
            System.out.println("Please enter new souvenir name:");
            String newSName = scanner.nextLine();
            System.out.println("Please enter new souvenir date:");
            LocalDate newDate = LocalDate.parse(scanner.nextLine());
            System.out.println("Please enter new souvenir price:");
            BigDecimal newPrice = scanner.nextBigDecimal();

            while (listIterator.hasNext()) {
                Souvenir souvenir = (Souvenir) listIterator.next();
                if (souvenir.getSName().equals(name) && souvenir.getPrice().equals(price) && souvenir.getCreationDate().equals(date) && souvenir.getManufacturer().equals(manufacturer)) {
                    changeManufacturer(souvenir.getManufacturer().getMName(), souvenir.getManufacturer().getCountry(), scanner);
                    listIterator.set(new Souvenir(newSName, newDate, newPrice, manufacturer));

                }
            }

            writeSouvenirsToFile(souvenirs);
            System.out.println("Souvenir successfully changed!!!");
        } else {
            System.out.println("Souvenir with such parameters isn't found!!!");
        }
    }

    private Boolean checkIfSouvenirExists(String name, LocalDate date, BigDecimal price, Manufacturer manufacturer) {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream("souvenirs.txt"))) {
            List<Souvenir> souvenirs = (List<Souvenir>) objectInputStream.readObject();
            ListIterator listIterator = souvenirs.listIterator();
            while (listIterator.hasNext()) {
                Souvenir souvenir = (Souvenir) listIterator.next();
                if (souvenir.getSName().equals(name) && souvenir.getPrice().equals(price) && souvenir.getCreationDate().equals(date) && souvenir.getManufacturer().equals(manufacturer)) {
                    return true;
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        return false;

    }


}
