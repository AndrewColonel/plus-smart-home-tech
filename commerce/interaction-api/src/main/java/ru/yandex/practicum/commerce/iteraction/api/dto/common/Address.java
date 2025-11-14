package ru.yandex.practicum.commerce.iteraction.api.dto.common;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Address {
   private String country;
   private String city;
   private String street;
   private String house;
   private String flat;
}
