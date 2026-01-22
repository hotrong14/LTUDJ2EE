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
                Chương trình quản lý sách
                1. Thêm 1 cuốn sách
                2. Xóa 1 cuốn sách
                3. Thay đổi sách
                4. Xuất thông tin
                5. Tìm sách lập trình
                6. Lấy sách tối đa theo giá
                7. Tìm kiếm theo tác giả
                0. Thoát
                Chọn chức năng: """;

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
                    System.out.print("Nhập vào mã sách cần xóa: ");
                    int bookId = x.nextInt();
                    //Kiểm tra mã sách
                    Book find = listBooks.stream().filter(p -> p.getId() == bookId).findFirst().orElseThrow();
                    listBooks.remove(find);
                    System.out.println("Đã xóa sách thành công!");
                }
                case 3 -> {
                    System.out.println("Nhập mã sách cần điều chỉnh: ");
                    int bookId = x.nextInt();
                    Book find = listBooks.stream().filter(p -> p.getId() == bookId).findFirst().orElseThrow();
                }
                case 4 -> {
                    System.out.println("Xuất thông tin danh sách ");
                    listBooks.forEach(p -> p.output());
                }
                case 5 -> {
                    List<Book> list5 = listBooks.stream()
                            .filter(p -> p.getTitle().toLowerCase().contains("lập trình"))
                            .toList();
                    list5.forEach(Book::output);
                }
                case 6 -> {
                    System.out.println("Sách có giá cao nhất: ");
                    listBooks.stream()
                        .sorted((b1, b2) -> Double.compare(b2.getPrice(), b1.getPrice()))
                        .limit(1)
                        .forEach(Book::output);
                }
                case 7-> {
                    System.out.print("Nhập tên tác giả cần tìm: ");
                    x.nextLine();
                    String inputArr = x.nextLine();

                    Set<String> authorSet = Arrays.stream(inputArr.split(","))
                        .map(p -> p.trim().toLowerCase())
                        .collect(Collectors.toSet());

                    System.out.println("Kết quả tìm kiếm:");
    
                    listBooks.stream()
                        .filter(p -> authorSet.contains(p.getAuthor().toLowerCase())) 
                        .forEach(Book::output);
                }
            }
        } while (chon != 0);
    }
}