package com.sparta.myselectshop.naver;

import com.sparta.myselectshop.dto.ItemDto;
import com.sparta.myselectshop.dto.ProductRequestDto;
import com.sparta.myselectshop.dto.ProductResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class NaverApiController {
    private final NaverApiService naverApiService;

    @GetMapping("/search")
    public List<ItemDto> searchItems(@RequestParam String query){
        return naverApiService.searchItems(query);
    }


}
