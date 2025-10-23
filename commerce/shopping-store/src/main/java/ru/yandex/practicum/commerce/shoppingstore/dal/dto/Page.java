package ru.yandex.practicum.commerce.shoppingstore.dal.dto;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Data
@NoArgsConstructor
public class Page {

    @PositiveOrZero
    private Integer page;
    @Positive
    private Integer size;
    @NonNull
    private List<String> sort;


    // TODO
    public  Pageable toPageable() {
        return PageRequest.of(page > 0 ? page / size : 0, size,
                Sort.by(Sort.Direction.ASC, String.valueOf(sort)));
    }

}
