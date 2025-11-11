package probe.submersible.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import probe.submersible.demo.model.BaseResponse;
import probe.submersible.demo.model.SubmersibleInfo;
import probe.submersible.demo.service.SubmersibleService;

@RestController
@RequestMapping("/submersible")
public class SubmersibleController {

    @Autowired
    private SubmersibleService submersibleService;

    @PostMapping
    public ResponseEntity<BaseResponse<Void>> initialSetup(@RequestBody SubmersibleInfo request) {

        return ResponseEntity.ok()
                .body(submersibleService.initialSetup(request));
    }

    @PutMapping()
    public ResponseEntity<BaseResponse<Void>> move(@RequestParam String dir) {

        return ResponseEntity
                .ok()
                .body(submersibleService.move(dir));
    }

    @PatchMapping()
    public ResponseEntity<BaseResponse<Void>> changeDir(@RequestParam String facing) {

        return ResponseEntity
                .ok()
                .body(submersibleService.changeDir(facing));
    }

    @GetMapping()
    public ResponseEntity<BaseResponse<String>> getSummary() {

        return ResponseEntity
                .ok()
                .body(submersibleService.getSummary());
    }

    @DeleteMapping()
    public ResponseEntity<BaseResponse<Void>> reset() {

        return ResponseEntity
                .ok()
                .body(submersibleService.reset());
    }



}
