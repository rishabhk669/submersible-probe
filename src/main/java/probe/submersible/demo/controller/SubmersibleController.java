package probe.submersible.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import probe.submersible.demo.model.BaseResponse;
import probe.submersible.demo.model.SubmersibleInfo;

@RestController
@RequestMapping("/submersible")
public class SubmersibleController {

    @PostMapping
    public ResponseEntity<BaseResponse<Void>> initialSetup(@RequestBody SubmersibleInfo request) {

        return ResponseEntity
                .ok()
                .body(new BaseResponse<>("SUCCESS", "200", null));
    }

    @PutMapping()
    public ResponseEntity<BaseResponse<Void>> move(@RequestParam String dir) {

        return ResponseEntity
                .ok()
                .body(new BaseResponse<>("SUCCESS", "200", null));
    }

    @PatchMapping()
    public ResponseEntity<BaseResponse<Void>> changeDir(@RequestParam String facing) {

        return ResponseEntity
                .ok()
                .body(new BaseResponse<>("SUCCESS", "200", null));
    }

    @GetMapping()
    public ResponseEntity<BaseResponse<Void>> getSummary() {

        return ResponseEntity
                .ok()
                .body(new BaseResponse<>("SUCCESS", "200", null));
    }

    @DeleteMapping()
    public ResponseEntity<BaseResponse<Void>> reset() {

        return ResponseEntity
                .ok()
                .body(new BaseResponse<>("SUCCESS", "200", null));
    }



}
