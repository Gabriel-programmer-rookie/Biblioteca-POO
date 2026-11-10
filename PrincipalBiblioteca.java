import java.util.List;
import java.util.Scanner;

public class PrincipalBiblioteca {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BibliotecaManager manager = new BibliotecaManager();
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n==========================================");
            System.out.println("  SISTEMA DE GESTÃO DE BIBLIOTECA (POO)");
            System.out.println("==========================================");
            System.out.println("1. Cadastrar Usuário");
            System.out.println("2. Cadastrar Livro");
            System.out.println("3. Cadastrar Revista");
            System.out.println("4. Cadastrar DVD");
            System.out.println("5. Listar Todo o Acervo");
            System.out.println("6. Listar Usuários Cadastrados");
            System.out.println("7. Pesquisar Item por Título");
            System.out.println("8. Realizar Empréstimo");
            System.out.println("9. Realizar Devolução");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            try {
                opcao = Integer.parseInt(scanner.nextLine());

                switch (opcao) {
                    case 1:
                        System.out.print("ID: ");
                        int idU = Integer.parseInt(scanner.nextLine());
                        System.out.print("Nome: ");
                        String nomeU = scanner.nextLine();
                        System.out.println("Tipo (1 - Aluno, 2 - Professor, 3 - Funcionário): ");
                        int tipoU = Integer.parseInt(scanner.nextLine());

                        if (tipoU == 1) manager.cadastrarUsuario(new Aluno(idU, nomeU));
                        else if (tipoU == 2) manager.cadastrarUsuario(new Professor(idU, nomeU));
                        else if (tipoU == 3) manager.cadastrarUsuario(new Funcionario(idU, nomeU));
                        else System.out.println("Tipo inválido.");
                        break;

                    case 2:
                        System.out.print("ID: ");
                        int idL = Integer.parseInt(scanner.nextLine());
                        System.out.print("Título: ");
                        String titL = scanner.nextLine();
                        System.out.print("Ano: ");
                        int anoL = Integer.parseInt(scanner.nextLine());
                        System.out.print("Autor: ");
                        String autL = scanner.nextLine();
                        System.out.print("Número de Páginas: ");
                        int pagL = Integer.parseInt(scanner.nextLine());

                        manager.adicionarItem(new Livro(idL, titL, anoL, autL, pagL));
                        break;

                    case 3:
                        System.out.print("ID: ");
                        int idR = Integer.parseInt(scanner.nextLine());
                        System.out.print("Título: ");
                        String titR = scanner.nextLine();
                        System.out.print("Ano: ");
                        int anoR = Integer.parseInt(scanner.nextLine());
                        System.out.print("Edição: ");
                        int edR = Integer.parseInt(scanner.nextLine());

                        manager.adicionarItem(new Revista(idR, titR, anoR, edR));
                        break;

                    case 4:
                        System.out.print("ID: ");
                        int idD = Integer.parseInt(scanner.nextLine());
                        System.out.print("Título: ");
                        String titD = scanner.nextLine();
                        System.out.print("Ano: ");
                        int anoD = Integer.parseInt(scanner.nextLine());
                        System.out.print("Duração (minutos): ");
                        int durD = Integer.parseInt(scanner.nextLine());

                        manager.adicionarItem(new DVD(idD, titD, anoD, durD));
                        break;

                    case 5:
                        if (manager.getAcervo().isEmpty()) {
                            System.out.println("Acervo vazio.");
                        } else {
                            for (ItemBiblioteca item : manager.getAcervo()) {
                                System.out.println(item.toString());
                            }
                        }
                        break;

                    case 6:
                        if (manager.getUsuarios().isEmpty()) {
                            System.out.println("Lista vazia.");
                        } else {
                            for (Usuario u : manager.getUsuarios()) {
                                System.out.println(u.toString());
                            }
                        }
                        break;

                    case 7:
                        System.out.print("Termo de busca: ");
                        String termo = scanner.nextLine();
                        List<ItemBiblioteca> res = manager.pesquisarPorTitulo(termo);
                        for (ItemBiblioteca item : res) {
                            System.out.println(item.toString());
                        }
                        break;

                    case 8:
                        System.out.print("ID do Usuário: ");
                        int usrId = Integer.parseInt(scanner.nextLine());
                        System.out.print("ID do Item: ");
                        int itemId = Integer.parseInt(scanner.nextLine());

                        manager.realizarEmprestimo(usrId, itemId);
                        break;

                    case 9:
                        System.out.print("ID do Usuário: ");
                        int usrDev = Integer.parseInt(scanner.nextLine());
                        System.out.print("ID do Item: ");
                        int itemDev = Integer.parseInt(scanner.nextLine());

                        manager.realizarDevolucao(usrDev, itemDev);
                        break;

                    case 0:
                        break;

                    default:
                        System.out.println("Opção inválida.");
                }
            } catch (LimiteExcedidoException | ItemIndisponivelException e) {
                System.out.println("Negócio: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Formato inválido.");
            } catch (Exception e) {
                System.out.println("Erro genérico: " + e.getMessage());
            }
        }
        scanner.close();
    }
}