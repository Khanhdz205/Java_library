package com.library.khanhnqph57627.service;

import com.library.khanhnqph57627.entity.Category;
import com.library.khanhnqph57627.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @PostConstruct
    public void initSampleData() {
        if (categoryRepository.count() == 0) {
            Category category1 = new Category();
            category1.setName("Bài giảng");
            category1.setDescription("Slide và tài liệu bài giảng môn học");

            Category category2 = new Category();
            category2.setName("Bài tập");
            category2.setDescription("Bài tập và lời giải môn học");

            Category category3 = new Category();
            category3.setName("Đề thi");
            category3.setDescription("Đề thi các năm và đáp án");

            Category category4 = new Category();
            category4.setName("Tài liệu tham khảo");
            category4.setDescription("Sách và tài liệu tham khảo bổ sung");

            Category category5 = new Category();
            category5.setName("Đồ án");
            category5.setDescription("Đồ án môn học và bài tập lớn");

            categoryRepository.saveAll(Arrays.asList(category1, category2, category3, category4, category5));
        }
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryById(Integer id) {
        return categoryRepository.findById(id);
    }

    public Category saveCategory(Category category) {
        return categoryRepository.save(category);
    }

    public void deleteCategory(Integer id) {
        categoryRepository.deleteById(id);
    }

    public List<Category> searchCategories(String keyword) {
        return categoryRepository.findByNameContainingIgnoreCase(keyword);
    }

    public Category findByName(String name) {
        return categoryRepository.findByName(name);
    }
}