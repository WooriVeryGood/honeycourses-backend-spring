package org.wooriverygood.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleApi {

    public ResponseEntity<String> sample() {
        return ResponseEntity.ok("api is online");
    }

}
