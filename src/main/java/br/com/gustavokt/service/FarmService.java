package br.com.gustavokt.service;

import br.com.gustavokt.domain.Farm;
import br.com.gustavokt.domain.Producer;
import br.com.gustavokt.repository.FarmRepository;

import java.util.Optional;
import java.util.Scanner;

public class FarmService {
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void menu(int op) {
        switch (op) {
            case 1 -> findByName();
            case 2 -> delete();
            case 3 -> save();
            case 4 -> update();
        }
    }

    private static void findByName() {
        System.out.println("Type the name or empty to all");
        String name = SCANNER.nextLine();
        FarmRepository.findByName(name)
                .forEach(p -> {
                    System.out.printf("[%d] - %s $d %s%n", p.getId(), p.getName(), p.getValues(), p.getProducer().getName());
                });
    }

    public static void delete() {
        System.out.println("Type the id of the Farm you want to delete");
        findByName();
        int id = Integer.parseInt(SCANNER.nextLine());
        System.out.println("Are you sure? Y/N");
        String choice = SCANNER.nextLine();
        if ("y".equalsIgnoreCase(choice)) {
            FarmRepository.delete(id);
        }

    }

    public static void save() {
        System.out.println("Type the name of the Farm to be saved");
        String name = SCANNER.nextLine();
        System.out.println("Type the number of values");
        int values = Integer.parseInt(SCANNER.nextLine());
        System.out.println("Type the id of the producer");
        Integer producerId = Integer.parseInt(SCANNER.nextLine());
        Farm farm = Farm.builder()
                .name(name)
                .values(values)
                .producer(Producer.builder().id(producerId).build())
                .build();
        FarmRepository.save(farm);

    }

    private static void update() {
        System.out.println("Type the id of the object you want to update");
        Optional<Farm> producerOptional = FarmRepository.findById(Integer.parseInt(SCANNER.nextLine()));
        if (producerOptional.isEmpty()) {
            System.out.println("Farm not found");
            return;
        }
        Farm producerFromDb = producerOptional.get();
        System.out.println("Farm Found" + producerFromDb);
        System.out.println("Type the new name or enter to keep the same");
        String name = SCANNER.nextLine();
        name = name.isEmpty() ? producerFromDb.getName() : name;

        System.out.println("Type the new name or enter to keep the same");
        int values = Integer.parseInt(SCANNER.nextLine());
        name = name.isEmpty() ? producerFromDb.getName() : name;

        Farm producerToUpdate = Farm.builder()
                .id(producerFromDb.getId())
                .values(values)
                .producer(producerFromDb.getProducer())
                .name(name)
                .build();
        FarmRepository.update(producerToUpdate);
    }

}
