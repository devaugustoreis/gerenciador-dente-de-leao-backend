package com.gerenciadordentedeleao.application.softexclusion;

import com.gerenciadordentedeleao.domain.category.CategoryEntity;
import com.gerenciadordentedeleao.domain.category.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@SpringBootTest
@ActiveProfiles("test")
class CascadeFilterManagerTest {

    @Autowired
    private CategoryRepository repository;
    
    @MockitoSpyBean
    private SoftExclusionFilter softExclusionFilter;

    @Test
    void testSoftExclusion() {
        // Arrange
        var category = new CategoryEntity();
        category.setLabel("Test Category");
        category.setExcluded(false);
        repository.save(category);
        
        var softExcludedCategory = new CategoryEntity();
        softExcludedCategory.setLabel("Soft Excluded Category");
        softExcludedCategory.setExcluded(true);
        repository.save(softExcludedCategory);

        // Act
        var list = repository.findAll();

        // Assert
        Assertions.assertEquals(1, list.size());
    }

}