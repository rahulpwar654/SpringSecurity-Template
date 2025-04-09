package com.security.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.security.dto.ProductDTO;
import com.security.model.Product;
import com.security.repository.ProductRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {


    private final ProductRepository repository;

//    private static final String IMAGE_DIR = "C:/Java-Developer/React/order/public/images/";
    private static final String IMAGE_DIR = Paths.get("").toAbsolutePath().toString() + "/public/images/";

    // find all products
    public Page<ProductDTO> findAll(String name, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> products = repository.findByNameContainingIgnoreCase(name, pageable);
        return products.map(this::toDTO);
    }

    public ProductDTO findById(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        return toDTO(product);
    }

    public ProductDTO save(@Valid ProductDTO dto) {
        List<String> imagePaths = saveImages(dto.getImageFiles()); // Upload das imagens

        Product product = new Product(null, dto.getName(), imagePaths, dto.getPrice());
        product = repository.save(product);

        return toDTO(product);
    }

    public ProductDTO update(Long id, @Valid ProductDTO dto) {
        Product existingProduct = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        existingProduct.setName(dto.getName());
        existingProduct.setPrice(dto.getPrice());

        // If there are new images sent, we upload them
        if (dto.getImageFiles() != null && !dto.getImageFiles().isEmpty()) {
            List<String> newImages = saveImages(dto.getImageFiles());
            existingProduct.setImages(newImages);
        }

        return toDTO(repository.save(existingProduct));
    }

    public void delete(Long id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
        repository.delete(product);
    }

    // save images to local directory
    private List<String> saveImages(List<MultipartFile> imageFiles) {
        if (imageFiles.isEmpty()) {
            return List.of();
        }

        return imageFiles.stream().map(file -> {
            try {
                // to avoid duplicate names add UUID in name
                String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

                // Caminho onde a imagem será salva
                Path path = Paths.get(IMAGE_DIR + fileName);

                // Criando diretório se não existir
                File directory = new File(IMAGE_DIR);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // Salvando a imagem no disco
                file.transferTo(path.toFile());

                return "/images/" + fileName;
            } catch (IOException e) {
                throw new RuntimeException("Error saving image: " + e.getMessage());
            }
        }).collect(Collectors.toList());
    }

    private ProductDTO toDTO(Product product) {
        return new ProductDTO(product.getId(), product.getName(), product.getImages(), product.getPrice(), null);
    }
}

