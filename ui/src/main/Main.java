package main;

import javafx.util.Pair;
import loan.Loan;
import loan.category.Category;
import menu.*;
import client.*;
import time.Yaz;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        /*Client client=new Client("Natan", 5000);
        Client client1=new Client("Rotem", 200);
        Client client2=new Client("Tal", 100);
        Loan loan=new Loan("1", 5000, client1, new Category("happy"),100, 50, 10);
        Loan loan1=new Loan("2", 4000,  client1, new Category("happy"), 100,50, 10);
        Loan loan2=new Loan("3", 1000,  client1, new Category("happy"), 100,50, 10);
        Loan loan3=new Loan("4", 2000,  client1, new Category("happy"), 100,50, 10);*/

        while(!Menu.isExit()){
            Menu.printMenu();
            Menu.getUsersInput();
            Menu.methodToCall();
        }
    }
}