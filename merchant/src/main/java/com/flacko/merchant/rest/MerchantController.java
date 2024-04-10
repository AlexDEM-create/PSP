package com.flacko.merchant.rest;

import com.flacko.merchant.Merchant;
import com.flacko.merchant.MerchantService;
import com.flacko.merchant.exception.MerchantNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/merchants")
public class MerchantController {
    private final MerchantService merchantService;

    @GetMapping
    public ResponseEntity<List<Merchant>> list() {
        return ResponseEntity.ok(merchantService.list());
    }

    @PostMapping
    public ResponseEntity<Merchant> create(@RequestBody Merchant merchant) {
        return ResponseEntity.ok(merchantService.create(merchant));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Merchant> update(@PathVariable String id, @RequestBody Merchant merchant) throws MerchantNotFoundException {
        return ResponseEntity.ok(merchantService.update(id, merchant));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Merchant> get(@PathVariable String id) throws MerchantNotFoundException {
        return ResponseEntity.ok(merchantService.get(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        merchantService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
