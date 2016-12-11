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
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

/**
 * Created by DeepakKumar_N01 on 06/12/2016.
 */
public class UserDetailsManipulationForReferenceOnly {

    private static final Pattern DATE_REGEX = Pattern.compile("^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$");

    public static void main(String args[]) throws Exception {

        String fileName1 = "address/AddressBook";
        ClassLoader classLoader = new UserDetailsManipulationForReferenceOnly().getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName1).getFile());

      //  readUserDetails(file);
        countFemaleUsers(file);
        //constructUserNameAndDOBMap(file);
        List<UserDetailsDO>  userDetailslist =  getUserDetails( file);
        getEldestPerson(userDetailslist);
        getDaysInBetweenDOB(userDetailslist);

    }

    private static void readUserDetails(File file) throws IOException {
        //read file into stream
        try (Stream<String> stream = Files.lines(Paths.get(String.valueOf(file.getAbsoluteFile())))) {
            stream.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static void countFemaleUsers(File file) throws IOException {
        //read file into stream
        try  {
            Predicate<String> predicate = s-> s.equalsIgnoreCase("Female");
            Path path = Paths.get(String.valueOf(file.getAbsoluteFile()));
            Map<String, Integer> words = Files.lines(path).flatMap(line -> Arrays.stream(line.trim().split(",")))
                    .filter(word -> word.length() > 0)
                    .map(word -> new AbstractMap.SimpleEntry<>(word, 1))
                    .collect(toMap(e -> e.getKey(), e -> e.getValue(), (v1, v2) -> v1 + v2));
           //words.forEach((k, v) ->  System.out.println(String.format("%s ==>> %d", k, v)));
            words.forEach((k, v)->{
                if("Female".equalsIgnoreCase(k.trim())){
                    System.out.println(String.format("Number of %s's in Address Book is %d .", k, v));
                }
                /*if(DATE_REGEX.matcher(k.trim()).matches()){
                   // System.out.println(String.format("Date of %s Repeated %d", k, v));
                }*/

            });

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void constructUserNameAndDOBMap(File file) {
        Map<String , String>  nameDOBMap =new HashMap<>();
        try (Stream<String> stream = Files.lines(Paths.get(String.valueOf(file.getAbsoluteFile())))) {
            List eachLine = stream.collect(toList());
            eachLine.forEach(lst->{
               String nameDobArray[]=  lst.toString().split(",");
                nameDOBMap.put(nameDobArray[0],nameDobArray[2]);
            });
            System.out.println(nameDOBMap);

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private static List<UserDetailsDO> getUserDetails(File file){
        List<UserDetailsDO> userDetailsDolist = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get(String.valueOf(file.getAbsoluteFile())))) {
            List eachLine = stream.collect(toList());
            eachLine.forEach(lst->{
                String nameDobArray[]=  lst.toString().split(",");
                UserDetailsDO userDetailsDO = new UserDetailsDO();
                userDetailsDO.setUserName(nameDobArray[0]);
                userDetailsDO.setGender(nameDobArray[1]);
                Date dob =null;
               /* try {
                    dob = DateUtils.parseDateStrictly(nameDobArray[2],"dd/MM/yyyy");
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                userDetailsDO.setDateOfBirth(dob);
                userDetailsDolist.add(userDetailsDO);*/
                SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                try {
                     dob = formatter.parse(nameDobArray[2]);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                userDetailsDO.setDateOfBirth(dob);
                userDetailsDolist.add(userDetailsDO);

            });
           // System.out.println(userDetailsDolist);


        } catch (IOException e) {
            e.printStackTrace();
        }

    return userDetailsDolist;

    }

    private static void getEldestPerson(List<UserDetailsDO> userDetailsDolist){
        userDetailsDolist.sort((o1, o2)->o1.getDateOfBirth().compareTo(o2.getDateOfBirth()));
        UserDetailsDO userDo = userDetailsDolist.get(0);
        System.out.println("The Oldest person in the Address Book is "+userDo.getUserName()+".");
    }

    private static void getDaysInBetweenDOB(List<UserDetailsDO> userDetailsDolist){
        Date date1 = null;
        Date date2 = null;
        for(UserDetailsDO usedetailsDO:userDetailsDolist){
            if(usedetailsDO.getUserName().startsWith("Bill")){
                date1 = usedetailsDO.getDateOfBirth();
            }else if(usedetailsDO.getUserName().startsWith("Paul")){
                date2 = usedetailsDO.getDateOfBirth();
            }
            if(null!=date2 && null!=date1) {
                long diff = date2.getTime() - date1.getTime();
                System.out.println("Bill is " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)+" days older than Paul.");
                break;
            }
        }

    }



}
