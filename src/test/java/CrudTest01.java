import br.com.gustavokt.service.FazendaService;
import br.com.gustavokt.service.ProducerService;

import java.util.Scanner;

public class CrudTest01 {
    private static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {
        int op;
        while (true) {
            producerMenu();
            op = Integer.parseInt(SCANNER.nextLine());
            if (op == 0) break;
            switch (op) {
                case 1 ->{
                    producerMenu();
                    op = Integer.parseInt(SCANNER.nextLine());
                    ProducerService.menu(op);
                }
                case 2 -> {
                    fazendaMenu();
                    op = Integer.parseInt(SCANNER.nextLine());
                    FazendaService.menu(op);
                }
            }
            FazendaService.menu(op);
        }
    }

    private static void menu() {
        System.out.println("Type the number of your operation");
        System.out.println("1. Producer");
        System.out.println("2. Fazenda");
        System.out.println("0. Exit");
    }

    private static void producerMenu() {
        System.out.println("Type the number of your operation");
        System.out.println("1. Search for producer");
        System.out.println("2. Delete producer");
        System.out.println("3. Save producer");
        System.out.println("4. Update producer");
        System.out.println("9. Go Back");
    }

    private static void fazendaMenu() {
        System.out.println("Type the number of your operation");
        System.out.println("1. Search for fazenda");
        System.out.println("2. Delete fazenda");
        System.out.println("3. Save fazenda");
        System.out.println("4. Update fazenda");
        System.out.println("9. Go Back");
    }
}
