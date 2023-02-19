package ca.springframework.spring5webfluxrestapi.controllers;

import ca.springframework.spring5webfluxrestapi.domain.Category;
import ca.springframework.spring5webfluxrestapi.domain.Vendor;
import ca.springframework.spring5webfluxrestapi.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.reactivestreams.Publisher;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

class CategoryControllerTest {

    WebTestClient webTestClient;
    CategoryRepository categoryRepository;
    CategoryController categoryController;
    @BeforeEach
    public void setUp() throws Exception{
        categoryRepository = Mockito.mock(CategoryRepository.class); // mockito taklit objesini oluşturuyoruz
        categoryController = new CategoryController(categoryRepository);
        webTestClient = WebTestClient.bindToController(categoryController).build(); // webtestclient objesini oluşturuyoruz ve controllerı bind ediyoruz
    }

    @Test
    void list() {

        doReturn(Flux.just(Category.builder().description("Cat1").build(),
                Category.builder().description("Cat2").build()))
                .when(categoryRepository)
                .findAll();

        webTestClient.get().uri(CategoryController.API_V1_CATEGORIES+"/")
                .exchange()
                .expectBodyList(Category.class);
    }

    @Test
    void getById() {

        doReturn(Mono.just(Category.builder().description("Cat1").build()))
                .when(categoryRepository)
                .findById(ArgumentMatchers.any(String.class));


        webTestClient.get().uri(CategoryController.API_V1_CATEGORIES+"/someid")
                .exchange()
                .expectBodyList(Category.class);
    }


    @Test
    void testCreateCategory() {
        // ArgumentMatchers.any(Publisher.class) publisher tipinde bir obje gelirse
      //willReturn(Flux.just(Category.builder().build())); flux ile oluşturduğumuz objeyi dön
        doReturn(Flux.just(Category.builder().description("Cat1").build(),
                Category.builder().description("Cat2").build()))
                .when(categoryRepository)
                .saveAll(ArgumentMatchers.any(Publisher.class)); // publisher tipinde bir obje gelirse


        Mono<Category> categoryToSaveMono = Mono.just(Category.builder().description("Some Category").build());

        webTestClient.post().uri(CategoryController.API_V1_CATEGORIES+"/create")
                .body(categoryToSaveMono,Category.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }



    @Test
    void testUpdate() {
        // ArgumentMatchers.any(Publisher.class) publisher tipinde bir obje gelirse
        //willReturn(Flux.just(Category.builder().build())); flux ile oluşturduğumuz objeyi dön
        doReturn(Mono.just(Category.builder().description("CA").build()))
                .when(categoryRepository)
                .save(ArgumentMatchers.any(Category.class)); // publisher tipinde bir obje gelirse


        Mono<Category> categoryToUpdateMono = Mono.just(Category.builder().description("Some Category").build());

        webTestClient.put().uri(CategoryController.API_V1_CATEGORIES+"/update/someid")
                .body(categoryToUpdateMono,Category.class)
                .exchange()
                .expectStatus()
                .isOk();
    }

    @Test
    void testPatch() {
        doReturn(Mono.just(Category.builder().id("CAFINDBYID").description("CAFINDBYID").build()))
                .when(categoryRepository)
                .findById(anyString());

        doReturn(Mono.just(Category.builder().id("CASAVE").description("CASAVE").build()))
                .when(categoryRepository)
                .save(ArgumentMatchers.any(Category.class));

        Mono<Category> categoryToUpdateMono = Mono.just(Category.builder().description("Some Category").build());

        webTestClient.patch().uri(CategoryController.API_V1_CATEGORIES+"/patch/someid")
                .body(categoryToUpdateMono,Category.class)
                .exchange()
                .expectStatus()
                .isOk();
        verify(categoryRepository).save(ArgumentMatchers.any(Category.class));
    }
}