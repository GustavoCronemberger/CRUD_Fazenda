package br.com.gustavokt.service;

import br.com.gustavokt.domain.Producer;
import br.com.gustavokt.repository.ProducerRepository;

import java.util.Optional;
import java.util.Scanner;

public class ProducerService {
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void menu(int op) {
        switch (op) {
            case 1 -> findByName();
            case 2 -> delete();
            case 3 -> save();
            case 4 -> update();
            default -> throw new IllegalArgumentException("Not a valid option");
        }
    }

    private static void findByName() {
        System.out.println("Type the name or empty to all");
        String name = SCANNER.nextLine();
        ProducerRepository.findByName(name)
                .forEach(p -> System.out.printf("[%d] - %s%n", p.getId(), p.getName()));
    }

    public static void delete() {
        try { System.out.println("Type the id of the producer you want to delete");
            int id = Integer.parseInt(SCANNER.nextLine());
            System.out.println("Are you sure? Y/N");
            String choice = SCANNER.nextLine();
            if ("y".equalsIgnoreCase(choice)) {
                ProducerRepository.delete(id);
                System.out.println("Producer deleted successfully.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred while deleting the producer: " + e.getMessage()); } }

    public static void save() {
        System.out.println("Type the name of the producer to be saved");
        String name = SCANNER.nextLine();
        Producer producer = Producer.builder().name(name).build();
        ProducerRepository.save(producer);

    }
    private static void update(){
        System.out.println("Type the id of the object you want to update");
        Optional<Producer> producerOptional = ProducerRepository.findById(Integer.parseInt(SCANNER.nextLine()));
        if (producerOptional.isEmpty()){
            System.out.println("Producer not found");
            return;
        }
        Producer producerFromDb = producerOptional.get();
        System.out.println("Producer Found"+ producerFromDb);
        System.out.println("Type the new name or enter to keep the same");
        String name = SCANNER.nextLine();
        name = name.isEmpty() ? producerFromDb.getName() : name;

        Producer producerToUpdate = Producer.builder()
                .id(producerFromDb.getId())
                .name(name)
                .build();
        ProducerRepository.update(producerToUpdate);
    }

}
