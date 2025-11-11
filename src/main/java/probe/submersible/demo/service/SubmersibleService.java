package probe.submersible.demo.service;

import lombok.Getter;
import org.springframework.stereotype.Service;
import probe.submersible.demo.model.SubmersibleInfo;

@Service
public class SubmersibleService {
    @Getter
    private SubmersibleInfo info;


    public void validate(SubmersibleInfo info) {

    }

}
