package ca.springframework.spring5webfluxrestapi.controllers;

import ca.springframework.spring5webfluxrestapi.domain.Category;
import ca.springframework.spring5webfluxrestapi.domain.Vendor;
import ca.springframework.spring5webfluxrestapi.repositories.VendorRepository;
import org.reactivestreams.Publisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(VendorController.API_V1_VENDORS)
public class VendorController {

    protected static final String API_V1_VENDORS = "/api/v1/vendors";

    private final VendorRepository vendorRepository;

    public VendorController(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }


    @GetMapping("/")
    public Flux<Vendor> list(){
        return vendorRepository.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Vendor> getById(@PathVariable("id") String id){
        return vendorRepository.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/create")
    public Mono<Void> create(@RequestBody Publisher<Vendor> vendorStream){
        // Publisher yayıncı,Void ise dönüş tipi yok demek
        return vendorRepository.saveAll(vendorStream).then();
    }


    @PutMapping("/update/{id}")
    public Mono<Vendor> update(@PathVariable("id") String id, @RequestBody Vendor vendor){
        vendor.setId(id);
        return vendorRepository.save(vendor);
    }


    @PatchMapping("/patch/{id}")
    public Mono<Vendor> patch(@PathVariable("id") String id, @RequestBody Vendor vendor){
        Vendor foundVendor = vendorRepository.findById(id).block();
        // block() metodu ile Mono nesnesini bekletiyoruz. Bu sayede Mono nesnesinin içindeki değeri alabiliyoruz.
        // reactive programming'de request geldiğinde thread tarafından repo'da non-blocking olarak çalışır.
        // bu sayede thread'in çalışması engellenmez. Kullanıcıya hızlı message dönülür. Asenkron çalışır. Başka servislere giderek
        // ve tek bi thread üzerinden yürüyerek cpu şişirmez. Bu sayede çoklu kullanıcıya hızlı cevap verir.
        if(!foundVendor.getFirstName().equals(foundVendor.getFirstName())){
            foundVendor.setFirstName(foundVendor.getFirstName());
            return vendorRepository.save(foundVendor);
        }
        return Mono.just(foundVendor);
    }

}
