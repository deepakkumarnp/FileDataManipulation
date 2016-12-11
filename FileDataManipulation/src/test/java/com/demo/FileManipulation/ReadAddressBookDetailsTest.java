package com.demo.FileManipulation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.MockitoAnnotations.initMocks;

/**
 * Created by DeepakKumar_N01 on 10/12/2016.
 */
@RunWith(MockitoJUnitRunner.class)
public class ReadAddressBookDetailsTest {

    @InjectMocks
    ReadAddressBookDetails readAddressBookDetails;

    @Before
    public void before() throws Exception {
        initMocks(this);
    }


    @Test
    public void shouldCheckForCorrectFileName() {
        String fileName = "address/AddressBook";
        File file = readAddressBookDetails.getAddressBookFile(fileName);
        assertNotNull(fileName);
        assertTrue(file.isFile());
    }

    @Test
    public void shouldCheckForInCorrectFileName() {
        String fileNameTest = "NoFileLocation";
        File fileTest = readAddressBookDetails.getAddressBookFile(fileNameTest);
        assertNull(fileTest);
    }

    @Test
    public void shouldCheckForWordCountName() throws IOException {
        String fileName = "address/AddressBook";
        File file = readAddressBookDetails.getAddressBookFile(fileName);
        Map<String, Integer> words = readAddressBookDetails.getWordsCountOfFile(file);
        assertNotNull(words);
        assertTrue(words.containsKey(" Female"));
    }

    @Test
    public void shouldCheckForWordCountNameWithEmptytFile() throws IOException {
        String fileName = "address/AddressBookTest";
        File file = readAddressBookDetails.getAddressBookFile(fileName);
        List<UserDetailsDO> list = readAddressBookDetails.getUserDetails(file);
        assertNotNull(list);
        assertEquals(0, list.size());
    }

    @Test
    public void shouldCheckForWordCountNameWithCorrectFile() throws IOException {
        String fileName = "address/AddressBook";
        File file = readAddressBookDetails.getAddressBookFile(fileName);
        List<UserDetailsDO> list = readAddressBookDetails.getUserDetails(file);
        assertNotNull(list);
        assertEquals(5, list.size());
    }




}
