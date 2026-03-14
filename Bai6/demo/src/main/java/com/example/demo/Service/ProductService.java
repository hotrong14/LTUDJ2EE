package com.example.demo.Service;

import com.example.demo.Model.Product;
import com.example.demo.Repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
public class ProductService {

	private final ProductRepository productRepository;
	public ProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	public List<Product> getAll() {
		return productRepository.findAll();
	}

	public Product getById(Long id) {
		return productRepository.findById(id).orElse(null);
	}

	public Product save(Product product) {
		return productRepository.save(product);
	}

	public void delete(Long id) {
		productRepository.deleteById(id);
	}

	public String uploadImage(MultipartFile file) throws IOException {

    if (file == null || file.isEmpty()) {
        return null;
    }

    // Lấy đường dẫn tuyệt đối của project
    String uploadDir = "src/main/resources/static/assets/";

    // Tạo thư mục nếu chưa có
    Files.createDirectories(Paths.get(uploadDir));

    // Lấy phần mở rộng file
    String originalFilename = file.getOriginalFilename();
    String ext = originalFilename.substring(originalFilename.lastIndexOf("."));

    // Tạo tên file ngẫu nhiên để tránh trùng
    String fileName = UUID.randomUUID() + ext;

    Path filePath = Paths.get(uploadDir, fileName);

    Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

    return fileName; // Chỉ trả về tên file
}
}