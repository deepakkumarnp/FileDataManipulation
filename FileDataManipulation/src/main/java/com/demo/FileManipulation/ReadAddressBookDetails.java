package com.demo.FileManipulation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;



/**
 * Created by DeepakKumar_N01 on 08/12/2016.
 */
public class ReadAddressBookDetails {

    public static void main(String args[]) throws Exception {

        String fileName = "address/AddressBook";
        File file = getAddressBookFile(fileName);
        if (null != file) {
            Map<String, Integer> words = getWordsCountOfFile(file);

            if (null != words) {
                readAndDisplayFemaleUsersInAddressBook(words);
            }

            List<UserDetailsDO> userDetailslist = getUserDetails(file);
            if (null != userDetailslist) {
                readAndDisplayEldestPersonInAddressBook(userDetailslist);
                readAndDisplayDaysInBetweenDOB(userDetailslist);
            }
        }
    }

    public static File getAddressBookFile(String fileName) {
        ClassLoader classLoader = new ReadAddressBookDetails().getClass().getClassLoader();
        File file = null;
        if(null!=classLoader.getResource(fileName)) {
            file=  new File(classLoader.getResource(fileName).getFile());
        }
        return file;
    }


    public static Map getWordsCountOfFile(File file) throws IOException {
        Map<String, Integer> words = null;
        try {
            Path path = Paths.get(String.valueOf(file.getAbsoluteFile()));
            words = Files.lines(path).flatMap(line -> Arrays.stream(line.trim().split(",")))
                    .filter(word -> word.length() > 0)
                    .map(word -> new AbstractMap.SimpleEntry<>(word, 1))
                    .collect(toMap(e -> e.getKey(), e -> e.getValue(), (v1, v2) -> v1 + v2));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return words;
    }

    public static void readAndDisplayFemaleUsersInAddressBook(Map<String, Integer> words) {
        //words.forEach((k, v) ->  System.out.println(String.format("%s ==>> %d", k, v)));
        words.forEach((k, v) -> {
            if ("Female".equalsIgnoreCase(k.trim())) {
                System.out.println(String.format("Number of %s's in Address Book is %d .", k, v));
            }
        });
    }

    public static List<UserDetailsDO> getUserDetails(File file) {
        List<UserDetailsDO> userDetailsDolist = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(String.valueOf(file.getAbsoluteFile())))) {
            List eachLine = stream.collect(toList());

            eachLine.forEach(lst -> {
                String nameDobArray[] = lst.toString().split(",");
                if (nameDobArray.length == 3) {
                    UserDetailsDO userDetailsDO = new UserDetailsDO();
                    userDetailsDO.setUserName(nameDobArray[0]);
                    userDetailsDO.setGender(nameDobArray[1]);
                    userDetailsDO.setDateOfBirth(parseDate(nameDobArray[2]));
                    userDetailsDolist.add(userDetailsDO);
                }

            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return userDetailsDolist;

    }

    public static Date parseDate(String date) {
        Date dob = null;
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        try {
            dob = formatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dob;
    }

    public static void readAndDisplayEldestPersonInAddressBook(List<UserDetailsDO> userDetailsDolist) {
        userDetailsDolist.sort((o1, o2) -> o1.getDateOfBirth().compareTo(o2.getDateOfBirth()));
        if (userDetailsDolist.size() > 0) {
            UserDetailsDO userDo = userDetailsDolist.get(0);
            System.out.println("The Oldest person in the Address Book is " + userDo.getUserName() + ".");
        }
    }

    public static void readAndDisplayDaysInBetweenDOB(List<UserDetailsDO> userDetailsDolist) {
        Date date1 = null;
        Date date2 = null;
        for (UserDetailsDO usedetailsDO : userDetailsDolist) {
            if (usedetailsDO.getUserName().startsWith("Bill")) {
                date1 = usedetailsDO.getDateOfBirth();
            } else if (usedetailsDO.getUserName().startsWith("Paul")) {
                date2 = usedetailsDO.getDateOfBirth();
            }
            if (null != date2 && null != date1) {
                long diff = date2.getTime() - date1.getTime();
                System.out.println("Bill is " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS) + " days older than Paul.");
                break;
            }
        }

    }


}
