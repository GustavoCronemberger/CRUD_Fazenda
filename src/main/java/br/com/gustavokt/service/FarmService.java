package br.com.gustavokt.service;

import br.com.gustavokt.domain.Farm;
import br.com.gustavokt.domain.Producer;
import br.com.gustavokt.repository.FarmRepository;
import br.com.gustavokt.repository.ProducerRepository;

import java.util.List;
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
        try {
            System.out.println("Type the name of the farm");
            String name = SCANNER.nextLine();
            List<Farm> farms = FarmRepository.findByName(name);
            if (farms.isEmpty()) {
                System.out.println("Farm not found, please try again");
            } else {
                farms.forEach(p -> System.out.printf("[%d] - %s %d %s%n", p.getId(), p.getName(), p.getValues(), p.getProducer().getName()));
            }
        } catch (Exception e) {
            System.out.println("An error occurred while searching for the farm: " + e.getMessage());
        }
    }

    public static void delete() {
        try {
            System.out.println("Type the id of the Farm you want to delete");
            int id = Integer.parseInt(SCANNER.nextLine());
            System.out.println("Are you sure? Y/N");
            String choice = SCANNER.nextLine();
            if ("y".equalsIgnoreCase(choice)) {
                FarmRepository.delete(id);
                System.out.println("Farm deleted successfully.");
            }
        } catch (Exception e) {
            System.out.println("An error occurred while deleting the farm: " + e.getMessage());
        }
    }

    public static void save() {
        try {
            System.out.println("Type the name of the Farm to be saved");
            String farmName = SCANNER.nextLine();
            System.out.println("Type the number of value");
            int values = Integer.parseInt(SCANNER.nextLine());
            System.out.println("Type the id of the producer");
            int producerId = Integer.parseInt(SCANNER.nextLine());

            Optional<Producer> optionalProducer = ProducerRepository.findById(producerId);
            Producer producer;
            if (optionalProducer.isPresent()) {
                producer = optionalProducer.get();
            } else {
                System.out.println("Type the name of the Producer to be saved");
                String producerName = SCANNER.nextLine();
                producer = Producer.builder().id(producerId).name(producerName).build();
                ProducerRepository.save(producer);
            }

            Farm farm = Farm.builder()
                    .name(farmName)
                    .values(values)
                    .producer(producer)
                    .build();
            FarmRepository.save(farm);
        } catch (Exception e) {
            System.out.println("An error occurred while saving the farm: " + e.getMessage());
        }
    }

    private static void update() {
        try {
            System.out.println("Type the id of the object you want to update");
            int id = Integer.parseInt(SCANNER.nextLine());
            Optional<Farm> farmOptional = FarmRepository.findById(id);
            if (farmOptional.isEmpty()) {
                System.out.println("Farm not found");
                return;
            }
            Farm farmFromDb = farmOptional.get();
            System.out.println("Farm Found: " + farmFromDb);
            System.out.println("Type the new name or enter to keep the same");
            String name = SCANNER.nextLine();
            name = name.isEmpty() ? farmFromDb.getName() : name;

            System.out.println("Type the new value or enter to keep the same");
            int values = Integer.parseInt(SCANNER.nextLine());

            Farm farmToUpdate = Farm.builder()
                    .id(farmFromDb.getId())
                    .values(values)
                    .producer(farmFromDb.getProducer())
                    .name(name)
                    .build();
            FarmRepository.update(farmToUpdate);
        } catch (Exception e) {
            System.out.println("An error occurred while updating the farm: " + e.getMessage());
        }
    }
}

