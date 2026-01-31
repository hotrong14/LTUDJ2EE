import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        List<Book> listBooks = new ArrayList<>();
        Scanner x = new Scanner(System.in);
        String msg = """
                Chuon trinh quan ly sach
                1. Them 1 cuon sach
                2. Xoa 1 cuon sach
                3. Thay doi sach
                4. Xuat thong tin
                5. Tim sach lap trinh
                6. Lay sach toi da theo gia
                7. Tim kiem theo tac gia
                0. Thoat
                Chon chuc nang: """;

        int chon = 0;
        do {
            System.out.println(msg);
            chon = x.nextInt();
            switch (chon) {
                case 1 -> {
                    Book newBook = new Book();
                    newBook.input();
                    listBooks.add(newBook);
                }
                case 2 -> {
                    System.out.print("Nhap vao ma sach can xoa: ");
                    int bookId = x.nextInt();
                    //Kiểm tra mã sách
                    Book find = listBooks.stream().filter(p -> p.getId() == bookId).findFirst().orElseThrow();
                    listBooks.remove(find);
                    System.out.println("Da xoa sach thanh cong!");
                }
                case 3 -> {
                    System.out.println("Nhap ma sach can dieu chinh: ");
                    int bookId = x.nextInt();
                    Book find = listBooks.stream().filter(p -> p.getId() == bookId).findFirst().orElseThrow();
                }
                case 4 -> {
                    System.out.println("Xuat thong tin danh sach ");
                    listBooks.forEach(p -> p.output());
                }
                case 5 -> {
                    List<Book> list5 = listBooks.stream()
                            .filter(p -> p.getTitle().toLowerCase().contains("lap trinh"))
                            .toList();
                    list5.forEach(Book::output);
                }
                case 6 -> {
                    System.out.println("Sach co gia cao nhat: ");
                    listBooks.stream()
                        .sorted((b1, b2) -> Double.compare(b2.getPrice(), b1.getPrice()))
                        .limit(1)
                        .forEach(Book::output);
                }
                case 7-> {
                    System.out.print("Nhap ten tac gia can tim: ");
                    x.nextLine();
                    String inputArr = x.nextLine();

                    Set<String> authorSet = Arrays.stream(inputArr.split(","))
                        .map(p -> p.trim().toLowerCase())
                        .collect(Collectors.toSet());

                    System.out.println("Ket qua tim kiem:");

                    listBooks.stream()
                        .filter(p -> authorSet.contains(p.getAuthor().toLowerCase())) 
                        .forEach(Book::output);
                }
            }
        } while (chon != 0);
    }
}