package zh.perfectnumber;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class PerfectNumberWebController {

    PerfectNumberMockedData perfectNumberMockedData = PerfectNumberMockedData.getInstance();

    @GetMapping("/perfnum")
    public List<PerfectNumber> index() {
        return perfectNumberMockedData.getAllPerfectNumbers();
    }

    @PostMapping("/perfnum/verify")
    public boolean verify(@RequestBody Map<String, String> body){
        String valueText = body.get("number");
        return perfectNumberMockedData.isPerfectNumber(valueText);
    }

    @PostMapping("/perfnum/findinrange")
    public List<PerfectNumber> findInRange(@RequestBody Map<String, String> body){
        String lowLimitText = body.get("low_limit");
        String highLimitText = body.get("high_limit");
        return perfectNumberMockedData.getPerfectNumbersByRange(lowLimitText, highLimitText);
    }


}