/*
 * Copyright (c) 2010-2021 Haifeng Li. All rights reserved.
 *
 * Smile is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Smile is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Smile.  If not, see <https://www.gnu.org/licenses/>.
 */

package smile.classification;

import smile.datasets.BreastCancer;
import smile.datasets.ColonCancer;
import smile.datasets.Iris;
import smile.io.Read;
import smile.io.Write;
import smile.math.MathEx;
import smile.test.data.*;
import smile.validation.*;
import smile.validation.metric.Error;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Haifeng Li
 */
public class FLDTest {

    public FLDTest() {
    }

    @BeforeAll
    public static void setUpClass() throws Exception {
    }

    @AfterAll
    public static void tearDownClass() throws Exception {
    }

    @BeforeEach
    public void setUp() {
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testIris() throws Exception {
        System.out.println("Iris");
        var iris = new Iris();
        ClassificationMetrics metrics = LOOCV.classification(iris.x(), iris.y(), FLD::fit);

        System.out.println(metrics);
        assertEquals(0.98, metrics.accuracy(), 1E-4);
    }

    @Test
    public void testPenDigits() {
        System.out.println("Pen Digits");

        MathEx.setSeed(19650218); // to get repeatable results.
        ClassificationValidations<FLD> result = CrossValidation.classification(10, PenDigits.x, PenDigits.y,
                FLD::fit);

        System.out.println(result);
        assertEquals(0.8771, result.avg.accuracy(), 1E-4);
    }

    @Test
    public void testBreastCancer() throws Exception {
        System.out.println("Breast Cancer");

        MathEx.setSeed(19650218); // to get repeatable results.
        var cancer = new BreastCancer();
        ClassificationValidations<FLD> result = CrossValidation.classification(10, cancer.x(), cancer.y(), FLD::fit);

        System.out.println(result);
        assertEquals(0.9655, result.avg.accuracy(), 1E-4);
    }

    @Test
    public void testUSPS() throws Exception {
        System.out.println("USPS");

        ClassificationValidation<FLD> result = ClassificationValidation.of(USPS.x, USPS.y, USPS.testx, USPS.testy, FLD::fit);

        System.out.println(result);
        assertEquals(262, result.metrics.error());

        java.nio.file.Path temp = Write.object(result.model);
        FLD model = (FLD) Read.object(temp);

        int error = Error.of(USPS.testy, model.predict(USPS.testx));
        assertEquals(262, error);
    }

    @Test
    public void testColon() throws Exception {
        System.out.println("Colon");

        MathEx.setSeed(19650218); // to get repeatable results.
        var colon = ColonCancer.load();
        var result = CrossValidation.classification(5, colon.x(), colon.y(), FLD::fit);

        System.out.println(result);
        assertEquals(0.8524, result.avg.accuracy(), 1E-4);
    }
}