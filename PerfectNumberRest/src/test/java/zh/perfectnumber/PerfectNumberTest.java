package zh.perfectnumber;

import org.apache.tomcat.jni.BIOCallback;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PerfectNumberTest {

    // Exponents of the first 17 known perfect numbers
    // NOTE: These values are ONLY used for unit test (they are never used in other code)
    final private static Integer[] KNOWN_PERFECT_NUMBER_EXPONENTS = {
            2, 3, 5, 7, 13, 17, 19, 31, 61, 89, 107, 127, 521, 607, 1279, 2203, 2281
    };

    // String value of the first 17 known perfect numbers
    // NOTE: These values are ONLY used for unit test (they are never used in other code)
    final private static String[] KNOWN_PERFECT_NUMBERS_VALUE_TEXT = {
            "6",
            "28",
            "496",
            "8128",
            "33550336",
            "8589869056",
            "137438691328",
            "2305843008139952128",
            "2658455991569831744654692615953842176",
            "191561942608236107294793378084303638130997321548169216",
            "13164036458569648337239753460458722910223472318386943117783728128",
            "14474011154664524427946373126085988481573677491474835889066354349131199152128",
            "23562723457267347065789548996709904988477547858392600710143027597506337283178622239730365539602600561360255566462503270175052892578043215543382498428777152427010394496918664028644534128033831439790236838624033171435922356643219703101720713163527487298747400647801939587165936401087419375649057918549492160555646976",
            "141053783706712069063207958086063189881486743514715667838838675999954867742652380114104193329037690251561950568709829327164087724366370087116731268159313652487450652439805877296207297446723295166658228846926807786652870188920867879451478364569313922060370695064736073572378695176473055266826253284886383715072974324463835300053138429460296575143368065570759537328128",
            "54162526284365847412654465374391316140856490539031695784603920818387206994158534859198999921056719921919057390080263646159280013827605439746262788903057303445505827028395139475207769044924431494861729435113126280837904930462740681717960465867348720992572190569465545299629919823431031092624244463547789635441481391719816441605586788092147886677321398756661624714551726964302217554281784254817319611951659855553573937788923405146222324506715979193757372820860878214322052227584537552897476256179395176624426314480313446935085203657584798247536021172880403783048602873621259313789994900336673941503747224966984028240806042108690077670395259231894666273615212775603535764707952250173858305171028603021234896647851363949928904973292145107505979911456221519899345764984291328",
            "1089258355057829337698225273522048981957108454302608067318906618508470155298616996291940961858901379546182685531220055762780759342407499066046704182083087124626926378164410931450968826355205573671671624202686633360807123109470452668371537599662797484934359039779954213666598820299501366380164619080260403235229556730554163992303009752651350320619930563673695280153023049498468696618144072021372831425963701460505606378119245841386552600145384072983309717141950085498085709671387054868320477972299055273914798446936214147860706887052107312380067072602317009422809314774791894700769891009818743169303028154303290071199392984292940283852217800166629229157110264080599294016452483028528153331119523441423159614934140265550242360007858215936798489500727196347516386044241721984706558329364277995903102292034620628080752342422906401283027034649671445569324281946859622177566643375489715678451311792675935981010355562887971948569016060035334607879359770371846507659970601616998311983878150420763306289490886429900481786499537645379839365212725494441511932772182768149943659849007457246983861558265144823191367758350341527780770221556945275566504831636564856831502556078058133043400055653540413313266034639355202834006126905491569560542489551023207382276137352665717018261519604817417112576526410535323991500058749996247580834453782528",
            "99497054337086473442435202604522816989643863571126408511774020575773849326355529178686629498151336416502516645641699516813140394897940636561646545947753232301453603583223268085613647233768081645727669037394385696522820301535888041815559513408036145123870584325525813950487109647770743827362571822870567643040184723115825645590386313377067112638149253171843914780065137373446222406322953569124771480101363180966448099882292453452395428270875732536311539266115116490704940164192417744919250000894727407937229829300578253427884494358459949535231819781361449649779252948099909821642207485514805768288115583409148969875790523961878753124972681179944234641016960011815788847436610192704551637034472552319820336532014561412028820492176940418377074274389149924303484945446105121267538061583299291707972378807395016030765440655601759109370564522647989156121804273012266011783451102230081380401951383582987149578229940818181514046314819313206321375973336785023565443101305633127610230549588655605951332351485641757542611227108073263889434409595976835137412187025349639504404061654653755349162680629290551644153382760681862294677414989047491922795707210920437811136712794483496437355980833463329592838140157803182055197821702739206310971006260383262542900044072533196137796552746439051760940430082375641150129817960183028081010978780902441733680977714813543438752546136375675139915776"
    };

    // The first 17 known perfect numbers used as a reference list.
    // NOTE: These values are ONLY used for unit test (they are never used in other code)
    final private static List<PerfectNumber> REFERENCE_LIST = new ArrayList<>();

    @BeforeAll
    static void beforeAll() {
        for (Integer exponent : Arrays.asList(KNOWN_PERFECT_NUMBER_EXPONENTS)) {
            REFERENCE_LIST.add(new PerfectNumber(exponent));
        }
    }

    @Test
    void testConstructor_GIVEN_knownPerfectNumberExponents_THEN_constructedPerfectNumbersMatchKnownValueTexts() {
        boolean allValid = true;
        for (int index = 0 ; index < KNOWN_PERFECT_NUMBERS_VALUE_TEXT.length ; ++index) {
            final PerfectNumber perfectNumber = REFERENCE_LIST.get(index);
            final BigInteger value = perfectNumber.getValue();
            final boolean isEqual = (value.compareTo(new BigInteger(KNOWN_PERFECT_NUMBERS_VALUE_TEXT[index])) == 0);
            allValid = allValid && isEqual && perfectNumber.isPerfect();
        }
        assertTrue(allValid);
    }

    @Test
    void testVerifyPerfectNumberByDefinition_GIVEN_zero_THEN_returnFalse() {
        final boolean isValid = PerfectNumber.verifyPerfectNumberByDefinition(BigInteger.ZERO);
        assertFalse(isValid);
    }

    @Test
    void testVerifyPerfectNumberByDefinition_GIVEN_one_THEN_returnFalse() {
        final boolean isValid = PerfectNumber.verifyPerfectNumberByDefinition(BigInteger.ONE);
        assertFalse(isValid);
    }

    @Test
    void testVerifyPerfectNumberByDefinition_GIVEN_squareNumber_THEN_returnFalse() {
        final boolean isValid = PerfectNumber.verifyPerfectNumberByDefinition(new BigInteger("256"));
        assertFalse(isValid);
    }

    @Test
    void testVerifyPerfectNumberByDefinition_GIVEN_perfectNumbers_THEN_allReturnTrue() {
        boolean allValid = true;
        for (PerfectNumber perfectNumber : REFERENCE_LIST.subList(0, 6)) {
            final BigInteger inputValue = perfectNumber.getValue();
            final boolean isValid = PerfectNumber.verifyPerfectNumberByDefinition(inputValue);
            allValid = allValid && isValid;
        }
        assertTrue(allValid);
    }

    @Test
    void testVerifyPerfectNumberByDefinition_GIVEN_nonPerfectNumbers_THEN_allReturnFalse() {
        boolean allInvalid = true;
        for (PerfectNumber perfectNumber : REFERENCE_LIST.subList(0, 6)) {
            final BigInteger inputValue = perfectNumber.getValue().subtract(BigInteger.ONE);
            final boolean isValid = PerfectNumber.verifyPerfectNumberByDefinition(inputValue);
            allInvalid = allInvalid && ! isValid;
        }
        assertTrue(allInvalid);
    }

    @Test
    void testVerifyPerfectNumberWithReferenceList_GIVEN_perfectNumbers_AND_referenceList_THEN_allReturnTrue() {
        boolean allValid = true;
        for (PerfectNumber perfectNumber : REFERENCE_LIST) {
            final BigInteger inputValue = perfectNumber.getValue();
            final boolean isValid = PerfectNumber.verifyPerfectNumberWithReferenceList(inputValue, REFERENCE_LIST);
            allValid = allValid && isValid;
        }
        assertTrue(allValid);
    }

    @Test
    void testVerifyPerfectNumberWithReferenceList_GIVEN_nonPerfectNumbers_AND_referenceList_THEN_allReturnFalse() {
        boolean allInvalid = true;
        for (PerfectNumber perfectNumber : REFERENCE_LIST) {
            final BigInteger inputValue = perfectNumber.getValue().subtract(BigInteger.ONE);
            final boolean isValid = PerfectNumber.verifyPerfectNumberWithReferenceList(inputValue, REFERENCE_LIST);
            allInvalid = allInvalid && ! isValid;
        }
        assertTrue(allInvalid);
    }

    @Test
    void testFindPerfectNumbersInRange_GIVEN_partialNumberRange_THEN_allFoundPerfectNumbersShouldBeInReferenceList() {
        final BigInteger lowLimit = new BigInteger("100");
        final BigInteger highLimit = new BigInteger("1000000000000");
        final List<PerfectNumber> allFoundPerfectNumbers = PerfectNumber.findPerfectNumbersInRange(lowLimit, highLimit);
        assertTrue(allFoundPerfectNumbers.size() <= REFERENCE_LIST.size());
        for (int index = 0 ; index < allFoundPerfectNumbers.size() ; ++index) {
            final PerfectNumber foundPerfectNumber = allFoundPerfectNumbers.get(index);
            assertTrue(REFERENCE_LIST.contains(foundPerfectNumber));
        }
    }


    @Test
    void testFindPerfectNumbersInRange_GIVEN_fullNumberRange_THEN_allFoundPerfectNumbersShouldBeInReferenceList() {
        final BigInteger lowLimit = BigInteger.ONE;
        final BigInteger highLimit = REFERENCE_LIST.get(REFERENCE_LIST.size() - 1).getValue();
        final List<PerfectNumber> allFoundPerfectNumbers = PerfectNumber.findPerfectNumbersInRange(lowLimit, highLimit);
        assertTrue(allFoundPerfectNumbers.size() <= REFERENCE_LIST.size());
        for (int index = 0 ; index < allFoundPerfectNumbers.size() ; ++index) {
            final PerfectNumber foundPerfectNumber = allFoundPerfectNumbers.get(index);
            final PerfectNumber referencedPerfectNumber = REFERENCE_LIST.get(index);
            assertEquals(referencedPerfectNumber, foundPerfectNumber);
        }
    }

    @Test
    void testFindAllPerfectNumbers_THEN_allFoundPerfectNumbersEqualsReferenceList() {
        final List<PerfectNumber> allFoundPerfectNumbers = PerfectNumber.findAllPerfectNumbers();
        assertTrue(allFoundPerfectNumbers.size() <= REFERENCE_LIST.size());
        for (int index = 0 ; index < allFoundPerfectNumbers.size() ; ++index) {
            final PerfectNumber foundPerfectNumber = allFoundPerfectNumbers.get(index);
            final PerfectNumber referencedPerfectNumber = REFERENCE_LIST.get(index);
            assertEquals(referencedPerfectNumber, foundPerfectNumber);
        }
    }
}