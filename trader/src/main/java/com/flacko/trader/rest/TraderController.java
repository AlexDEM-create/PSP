package com.flacko.trader.rest;

import com.flacko.trader.Trader;
import com.flacko.trader.TraderService;
import com.flacko.trader.exception.TraderNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/traders")
public class TraderController {
    private final TraderService traderService;

    @GetMapping
    public ResponseEntity<List<Trader>> list() {
        return ResponseEntity.ok(traderService.list());
    }

    @PostMapping
    public ResponseEntity<Trader> create(@RequestBody Trader trader) {
        return ResponseEntity.ok(traderService.create(trader));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Trader> update(@PathVariable String id, @RequestBody Trader trader) throws TraderNotFoundException {
        return ResponseEntity.ok(traderService.update(id, trader));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Trader> get(@PathVariable String id) throws TraderNotFoundException {
        return ResponseEntity.ok(traderService.get(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        traderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}