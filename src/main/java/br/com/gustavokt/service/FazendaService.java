package br.com.gustavokt.service;

import br.com.gustavokt.domain.Fazenda;
import br.com.gustavokt.domain.Producer;
import br.com.gustavokt.repository.FazendaRepository;

import java.util.Optional;
import java.util.Scanner;

public class FazendaService {
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
        FazendaRepository.findByName(name)
                .forEach(p -> {
                    System.out.printf("[%d] - %s $d %s%n", p.getId(), p.getName(), p.getValues(), p.getProducer().getName());
                });
    }

    public static void delete() {
        System.out.println("Type the id of the Fazenda you want to delete");
        findByName();
        int id = Integer.parseInt(SCANNER.nextLine());
        System.out.println("Are you sure? Y/N");
        String choice = SCANNER.nextLine();
        if ("y".equalsIgnoreCase(choice)) {
            FazendaRepository.delete(id);
        }

    }

    public static void save() {
        System.out.println("Type the name of the Fazenda to be saved");
        String name = SCANNER.nextLine();
        System.out.println("Type the number of values");
        int values = Integer.parseInt(SCANNER.nextLine());
        System.out.println("Type the id of the producer");
        Integer producerId = Integer.parseInt(SCANNER.nextLine());
        Fazenda fazenda = Fazenda.builder()
                .name(name)
                .values(values)
                .producer(Producer.builder().id(producerId).build())
                .build();
        FazendaRepository.save(fazenda);

    }

    private static void update() {
        System.out.println("Type the id of the object you want to update");
        Optional<Fazenda> producerOptional = FazendaRepository.findById(Integer.parseInt(SCANNER.nextLine()));
        if (producerOptional.isEmpty()) {
            System.out.println("Fazenda not found");
            return;
        }
        Fazenda producerFromDb = producerOptional.get();
        System.out.println("Fazenda Found" + producerFromDb);
        System.out.println("Type the new name or enter to keep the same");
        String name = SCANNER.nextLine();
        name = name.isEmpty() ? producerFromDb.getName() : name;

        System.out.println("Type the new name or enter to keep the same");
        int values = Integer.parseInt(SCANNER.nextLine());
        name = name.isEmpty() ? producerFromDb.getName() : name;

        Fazenda producerToUpdate = Fazenda.builder()
                .id(producerFromDb.getId())
                .values(values)
                .producer(producerFromDb.getProducer())
                .name(name)
                .build();
        FazendaRepository.update(producerToUpdate);
    }
}
