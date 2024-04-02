package org.wooriverygood.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleApi {

    @GetMapping("/")
    public ResponseEntity<String> sample() {
        return ResponseEntity.ok("api is online");
    }

}
