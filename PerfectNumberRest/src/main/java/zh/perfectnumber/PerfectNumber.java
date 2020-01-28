package zh.perfectnumber;

import java.math.BigInteger;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PerfectNumber {
    // For practical reason, the largest exponent is limited to this value.
    // Thus, the execution time of generating all the known perfect numbers is acceptable.
    // In theory, there is no such a limit.
    public static final long DEFAULT_MAX_EXPONENT = 2300L;


    // The average ratio of exponent value to the number of digits in the perfect number in decimal.
    // The value used here is obtained from Wikipedia.
    public static final double EXPONENT_TO_DIGITS_RATIO = 1.6609640474;

    private int exponent;
    private BigInteger mersenneNumber;
    private BigInteger value;
    private boolean hasMersennePrime;
    private boolean isPerfect;

    public PerfectNumber() {
        initDefault();
    }

    public PerfectNumber(int exponent) {
        if (exponent <= 0) {
            initDefault();
        } else {
            init(exponent);
        }
    }

    private void initDefault() {
        this.exponent = 0;
        this.mersenneNumber = null;
        this.value = null;
        this.hasMersennePrime = false;
        this.isPerfect = false;
    }

    private void init(int exponent) {
        this.exponent = exponent;
        this.mersenneNumber = BigInteger.valueOf(2l).pow(exponent).subtract(BigInteger.ONE);
        this.value = BigInteger.valueOf(2l).pow(exponent - 1).multiply(BigInteger.valueOf(2l).pow(exponent).subtract(BigInteger.ONE));
        this.hasMersennePrime = this.mersenneNumber.isProbablePrime(1);
        this.isPerfect = this.hasMersennePrime;
    }

    public int getExponent() {
        return exponent;
    }

    public BigInteger getValue() {
        return value;
    }

    public boolean isPerfect() {
        return isPerfect;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PerfectNumber that = (PerfectNumber) o;
        return exponent == that.exponent &&
                hasMersennePrime == that.hasMersennePrime &&
                isPerfect == that.isPerfect &&
                Objects.equals(mersenneNumber, that.mersenneNumber) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(exponent, mersenneNumber, value, hasMersennePrime, isPerfect);
    }

    @Override
    public String toString() {
        return "PerfectNumber{" +
                "exponent=" + exponent +
                ", mersenneNumber=" + mersenneNumber +
                ", value=" + value +
                ", hasMersennePrime=" + hasMersennePrime +
                ", isPerfect=" + isPerfect +
                '}';
    }

    public static boolean verifyPerfectNumberByDefinition(final BigInteger value) {
        if (value == null) {
            return false;
        }

        if (value.compareTo(BigInteger.ZERO) <= 0) {
            return false;
        }

        // A square number is not perfect number
        if (value.sqrtAndRemainder()[1].equals(BigInteger.ZERO)) {
            return false;
        }

        BigInteger sum = BigInteger.ZERO;
        final BigInteger valueSqrt = value.sqrt();
        final BigInteger expectedSum = value.add(value);
        for (BigInteger divisor = BigInteger.ONE ; divisor.compareTo(valueSqrt) <= 0 ; ) {
            if (value.remainder(divisor).equals(BigInteger.ZERO)) {
                final BigInteger quotient = value.divide(divisor);
                sum = sum.add(divisor);
                sum = sum.add(quotient);
            }
            divisor = divisor.add(BigInteger.ONE);
        }
        return (sum.equals(expectedSum));
    }

    public static boolean verifyPerfectNumberWithReferenceList(final BigInteger value, final List<PerfectNumber> referenceList) {
        if (value == null) {
            return false;
        }

        if (value.compareTo(BigInteger.ZERO) <= 0) {
            return false;
        }

        if (value.sqrtAndRemainder()[1].equals(BigInteger.ZERO)) {
            // A square number is not perfect number
            return false;
        }

        if (referenceList == null || referenceList.isEmpty()) {
            return false;
        }

        boolean isInRange = false;
        boolean isVerified = false;
        for (PerfectNumber existPerfectNumber : referenceList) {
            if (! existPerfectNumber.isPerfect()) {
                continue;
            }
            if (value.compareTo(existPerfectNumber.getValue()) == 0) {
                isInRange = true;
                isVerified = true;
                break;
            } else if (value.compareTo(existPerfectNumber.getValue()) < 0) {
                isInRange = true;
            }
        }

        return isInRange && isVerified;
    }

    private static int getLowerExponent(final BigInteger value) {
        if (value.compareTo(BigInteger.ZERO) <= 0) {
            throw new IllegalArgumentException("Value must greater than zero");
        }
        int numOfDigits = value.toString().length();
        double exponent = EXPONENT_TO_DIGITS_RATIO * (double)numOfDigits;
        long flooredExponent = (long)Math.floor(exponent);
        return (flooredExponent > 1L ? (int)flooredExponent - 1 : (int)flooredExponent);
    }

    private static int getHigherExponent(final BigInteger value) {
        if (value.compareTo(BigInteger.ZERO) <= 0) {
            throw new IllegalArgumentException("Value must greater than zero");
        }
        int numOfDigits = value.toString().length();
        double exponent = EXPONENT_TO_DIGITS_RATIO * (double)numOfDigits;
        long flooredExponent = (long)Math.ceil(exponent);
        return ((int)flooredExponent + 1);
    }

    public static List<PerfectNumber> findPerfectNumbersInRange(final BigInteger lowLimit, final BigInteger highLimit) {
        if (lowLimit == null || highLimit == null) {
            throw new IllegalArgumentException("Null arguments!");
        }
        if (lowLimit.compareTo(BigInteger.ZERO) <= 0) {
            throw new IllegalArgumentException("Low limit must be greater than zero");
        }
        if (lowLimit.compareTo(highLimit) > 0) {
            throw new IllegalArgumentException("Low limit must be less than or equal to high limit");
        }

        final List<PerfectNumber> allPerfectNumbers = new ArrayList<>();
        final int lowerExponentLimit = getLowerExponent(lowLimit);
        final int higherExponentLimit = getHigherExponent(highLimit);
        for (int exponent = lowerExponentLimit ; exponent <= higherExponentLimit ; ++exponent) {
            final PerfectNumber perfectNumber = new PerfectNumber(exponent);
            if (perfectNumber.isPerfect()
                    && perfectNumber.getValue().compareTo(lowLimit) >= 0
                    && perfectNumber.getValue().compareTo(highLimit) <= 0) {
                allPerfectNumbers.add(perfectNumber);
            }
        }
        return allPerfectNumbers;
    }

    public static List<PerfectNumber> findAllPerfectNumbers() {
        final List<PerfectNumber> allPerfectNumbers = new ArrayList<>();
        for (int exponent = 1 ; exponent <= (int)DEFAULT_MAX_EXPONENT ; ++exponent) {
            final PerfectNumber perfectNumber = new PerfectNumber(exponent);
            if (perfectNumber.isPerfect()) {
                allPerfectNumbers.add(perfectNumber);
            }
        }
        return allPerfectNumbers;
    }

    public static void main(String[] args) {
        final long startTimestamp = Instant.now().toEpochMilli();
        final List<PerfectNumber> allPerfectNumbers = findAllPerfectNumbers();
        final long endTimestamp = Instant.now().toEpochMilli();
        final long timeDiffMillis = endTimestamp - startTimestamp;
        for (PerfectNumber perfectNumber : allPerfectNumbers) {
            System.out.println(perfectNumber);
        }
        System.out.println("Execution Time (millisecond) = " + timeDiffMillis);

    }


}
