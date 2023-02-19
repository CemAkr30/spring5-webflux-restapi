package ca.springframework.spring5webfluxrestapi.domain;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Vendor {

    @Id
    private String id;
    private String firstName;
    private String lastName;


}
