package com.codecool.javabst;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;

@SpringBootApplication
public class Main {



    public static void main(String[] args) throws IOException {

        SpringApplication.run(Main.class, args);
        System.out.println("done");
    }


   @PostConstruct
    public void init(){
       ArrayList<Integer> numbers = new ArrayList<>();

       for (int i = 0; i < 50; i++) {
           numbers.add(i * 2 + 5);
       }
       BinarySearchTree binarySearchTree = new BinarySearchTree(numbers);
       binarySearchTree.add(22);
       binarySearchTree.search(22);
       binarySearchTree.remove(35);
       binarySearchTree.search(22);
       binarySearchTree.search(35);
   }

}