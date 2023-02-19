package ca.springframework.spring5webfluxrestapi.controllers;

import ca.springframework.spring5webfluxrestapi.domain.Category;
import ca.springframework.spring5webfluxrestapi.repositories.CategoryRepository;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.Flow;

@RestController
@RequestMapping(CategoryController.API_V1_CATEGORIES)
public class CategoryController {


    public static final String API_V1_CATEGORIES = "/api/v1/categories";

    private final CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @GetMapping("/")
    public Flux<Category> list(){
        return categoryRepository.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Category> getById(@PathVariable("id") String id){
        return categoryRepository.findById(id);
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    public Mono<Void> create(@RequestBody Publisher<Category> categoryStream){
                // Publisher yayıncı,Void ise dönüş tipi yok demek
        return categoryRepository.saveAll(categoryStream).then();
    }


    @PutMapping("/update/{id}")
   public Mono<Category> update(@PathVariable("id") String id, @RequestBody Category category){
        category.setId(id);
        return categoryRepository.save(category);
    }

    @PatchMapping("/patch/{id}")
    public Mono<Category> patch(@PathVariable("id") String id, @RequestBody Category category){
        Category foundCategory = categoryRepository.findById(id).block();
            // block() metodu ile Mono nesnesini bekletiyoruz. Bu sayede Mono nesnesinin içindeki değeri alabiliyoruz.
           // reactive programming'de request geldiğinde thread tarafından repo'da non-blocking olarak çalışır.
              // bu sayede thread'in çalışması engellenmez. Kullanıcıya hızlı message dönülür. Asenkron çalışır. Başka servislere giderek
            // ve tek bi thread üzerinden yürüyerek cpu şişirmez. Bu sayede çoklu kullanıcıya hızlı cevap verir.
        if(!foundCategory.getDescription().equals(category.getDescription())){
            foundCategory.setDescription(category.getDescription());
            return categoryRepository.save(foundCategory);
        }
        return Mono.just(foundCategory);
    }

}
