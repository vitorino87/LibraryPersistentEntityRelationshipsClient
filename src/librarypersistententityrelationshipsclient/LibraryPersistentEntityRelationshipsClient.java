/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package librarypersistententityrelationshipsclient;

import com.tutorialspoint.entity.Author;
import com.tutorialspoint.entity.Book1;
import com.tutorialspoint.stateless.LibraryPersistentEntityRelationshipsRemote;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 *
 * @author tiago.lucas
 */
public class LibraryPersistentEntityRelationshipsClient {

    BufferedReader brConsoleReader = null;
    Properties props;
    InitialContext ctx;

    {
        props = new Properties();
        try {
            props.load(new FileInputStream("jndi.properties"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        try {
            ctx = new InitialContext(props);
        } catch (NamingException ex) {
            ex.printStackTrace();
        }
        brConsoleReader = new BufferedReader(new InputStreamReader(System.in));
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        LibraryPersistentEntityRelationshipsClient ejbTester = new LibraryPersistentEntityRelationshipsClient();
        ejbTester.testEmbeddedObjects();
    }

    public void showGUI() {
        System.out.println("********************************");
        System.out.println("Welcome to Book Store");
        System.out.println("********************************");
        System.out.println("Option\n1. Add Book\n2. Exit\nEnter Choice: ");
    }

    private void testEmbeddedObjects() {
        try {
            int choice = 1;
            LibraryPersistentEntityRelationshipsRemote libraryBean
                    = (LibraryPersistentEntityRelationshipsRemote) ctx.lookup("LibraryPersistentEntityRelationships/remote");

            while (choice != 2) {
                String bookName;
                String authorName;

                showGUI();
                String strChoice = brConsoleReader.readLine();
                choice = Integer.parseInt(strChoice);

                if (choice == 1) {
                    System.out.print("Enter book name: ");
                    bookName = brConsoleReader.readLine();
                    System.out.print("Enter author name: ");
                    authorName = brConsoleReader.readLine();
                    Book1 book = new Book1();
                    book.setName(bookName);
                    Author author = new Author();
                    author.setName(authorName);
                    Set<Author> authors = new HashSet<Author>();
                    authors.add(author);
                    book.setAuthors(authors);

                    libraryBean.addBook(book);
                } else if (choice == 2) {
                    break;
                }
            }
            List<Book1> booksList = libraryBean.getBooks();
            System.out.println("Book(s) entered so far: " + booksList.size());
            int i = 0;
            for (Book1 book : booksList) {
                System.out.println((i + 1) + ". " + book.getName());
                System.out.print("Author: ");
                Author[] authors = (Author[]) book.getAuthors().toArray();
                for (int j = 0; j < authors.length; j++) {
                    System.out.println(authors[j]);
                }
                i++;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (brConsoleReader != null) {
                    brConsoleReader.close();
                }
            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }

}
