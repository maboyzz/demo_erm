package com.nthuy.demo_erm.until;


import com.nthuy.demo_erm.dto.Meta;
import com.nthuy.demo_erm.dto.ResultPaginationDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class PaginationUtils {

    public static <T, R> ResultPaginationDTO<R> buildResult(Page<T> pageResult, java.util.List<R> dtoList, Pageable pageable) {
        Meta meta = new Meta();
        meta.setPage(pageResult.getNumber());
        meta.setSize(pageResult.getSize());
        meta.setTotalElements(pageResult.getTotalElements());
        meta.setTotalPages(pageResult.getTotalPages());
        meta.setNumberOfElements(pageResult.getNumberOfElements());
        meta.setSort(pageable.getSort().toString());

        ResultPaginationDTO<R> result = new ResultPaginationDTO<>();
        result.setContent(dtoList);
        result.setMeta(meta);

        return result;
    }
}
