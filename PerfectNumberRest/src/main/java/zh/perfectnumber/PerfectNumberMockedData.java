package zh.perfectnumber;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

public class PerfectNumberMockedData {
    private List<PerfectNumber> perfectNumbers;

    private static PerfectNumberMockedData instance = null;

    public static PerfectNumberMockedData getInstance() {
        if (instance == null) {
            instance = new PerfectNumberMockedData();
        }
        return instance;
    }

    public PerfectNumberMockedData() {
        this.perfectNumbers = PerfectNumber.findAllPerfectNumbers();
    }

    public BigInteger getMaxPerfectNumberValue() {
        return new BigInteger(perfectNumbers.get(perfectNumbers.size() - 1).getValue().toString());
    }

    public List<PerfectNumber> getAllPerfectNumbers() {
        return perfectNumbers;
    }

    public List<PerfectNumber> getPerfectNumbersByRange(final String lowLimitText, final String highLimitText) {
        List<PerfectNumber> foundPerfectNumbers = new ArrayList<>();
        try {
            final BigInteger lowLimit = new BigInteger(lowLimitText);
            final BigInteger highLimit = new BigInteger(highLimitText);
            foundPerfectNumbers = PerfectNumber.findPerfectNumbersInRange(lowLimit, highLimit);
        } catch (RuntimeException npe) {
            npe.printStackTrace();
        }
        return foundPerfectNumbers;
    }

    public boolean isPerfectNumber(final String valueText) {
        boolean result = false;
        try {
            final BigInteger value = new BigInteger(valueText);
            final BigInteger maxExistValue = getMaxPerfectNumberValue();
            if (value.compareTo(maxExistValue) <= 0) {
                result = PerfectNumber.verifyPerfectNumberWithReferenceList(value, perfectNumbers);
            } else {
                result = PerfectNumber.verifyPerfectNumberByDefinition(value);
            }
        } catch (RuntimeException re) {
            re.printStackTrace();
        }
        return result;
    }
}
