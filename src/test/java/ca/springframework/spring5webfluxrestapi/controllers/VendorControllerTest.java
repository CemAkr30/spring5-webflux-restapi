package ca.springframework.spring5webfluxrestapi.controllers;

import ca.springframework.spring5webfluxrestapi.domain.Category;
import ca.springframework.spring5webfluxrestapi.domain.Vendor;
import ca.springframework.spring5webfluxrestapi.repositories.VendorRepository;
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

class VendorControllerTest {


    VendorRepository vendorRepository;

    VendorController vendorController;

    WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        vendorRepository = Mockito.mock(VendorRepository.class);
        vendorController = new VendorController(vendorRepository);
        webTestClient = WebTestClient.bindToController(vendorController).build();
    }

    @Test
    void list() {
      /*  BDDMockito.given(vendorRepository.findAll())
                .willReturn(Flux.just(Vendor.builder().firstName("Joe").lastName("Buck").build(),
                        Vendor.builder().firstName("Michael").lastName("Weston").build()));*/
        doReturn(Flux.just(Vendor.builder().firstName("Joe").lastName("Buck").build(),
                Vendor.builder().firstName("Michael").lastName("Weston").build()))
                .when(vendorRepository)
                .findAll();

        webTestClient.get().uri(VendorController.API_V1_VENDORS+"/")
                .exchange()
                .expectBodyList(Vendor.class);
    }

    @Test
    void getById() {
      /*  BDDMockito.given(vendorRepository.findById("someid"))
                .willReturn(Mono.just(Vendor.builder().firstName("Joe").lastName("Buck").build()));
       */

        doReturn(Mono.just(Vendor.builder().firstName("Joe").lastName("Buck").build()))
                .when(vendorRepository)
                .findById(ArgumentMatchers.any(String.class));
        //ArgumentMatchers.any(String.class) string veri gelirse taklit objesinde findById olursa
          // joe-buck dönsün

        webTestClient.get().uri(VendorController.API_V1_VENDORS+"/someid")
                .exchange()
                .expectBodyList(Vendor.class);
    }


    @Test
    void testCreateVendor() {
        // ArgumentMatchers.any(Publisher.class) publisher tipinde bir obje gelirse
        //willReturn(Flux.just(Category.builder().build())); flux ile oluşturduğumuz objeyi dön
        doReturn(Flux.just(Vendor.builder().firstName("CA").build()
                , Vendor.builder().firstName("CA2").build()))
                .when(vendorRepository)
                .saveAll(ArgumentMatchers.any(Publisher.class)); // publisher tipinde bir obje gelirse


        Mono<Vendor> vendorToSaveMono = Mono.just(Vendor.builder().lastName("CA").build());

        webTestClient.post().uri(VendorController.API_V1_VENDORS+"/create")
                .body(vendorToSaveMono,Category.class)
                .exchange()
                .expectStatus()
                .isCreated();
    }

    @Test
    void testUpdate() {
        // ArgumentMatchers.any(Publisher.class) publisher tipinde bir obje gelirse
        //willReturn(Flux.just(Category.builder().build())); flux ile oluşturduğumuz objeyi dön
        doReturn(Mono.just(Vendor.builder().firstName("CA").build()))
                .when(vendorRepository)
                .save(ArgumentMatchers.any(Vendor.class)); // publisher tipinde bir obje gelirse


        Mono<Vendor> vendorToUpdateMono = Mono.just(Vendor.builder().firstName("Some Category").build());

        webTestClient.put().uri(VendorController.API_V1_VENDORS+"/update/someid")
                .body(vendorToUpdateMono,Category.class)
                .exchange()
                .expectStatus()
                .isOk();
    }



    @Test
    void testPatch() {
        doReturn(Mono.just(Vendor.builder().firstName("FN:CAKAR").build()))
                .when(vendorRepository)
                .findById(anyString());

        doReturn(Mono.just(Vendor.builder().id("CASAVE").firstName("CASAVE").build()))
                .when(vendorRepository)
                .save(ArgumentMatchers.any(Vendor.class));

        Mono<Vendor> vendorToUpdateMono = Mono.just(Vendor.builder().firstName("BODY:CAKAR").build());

        webTestClient.patch().uri(VendorController.API_V1_VENDORS+"/patch/someid")
                .body(vendorToUpdateMono,Vendor.class)
                .exchange()
                .expectStatus()
                .isOk();

      //  verify(vendorRepository).save(ArgumentMatchers.any(Vendor.class));
    }
}