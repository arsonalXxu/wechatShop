package org.arsonal.wechatShop.service;

import org.arsonal.wechatShop.AuthController;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TelVerificationServiceTest {
    public static AuthController.TelAndCode VALID_PARAMETER =
            new AuthController.TelAndCode("13812345678", null);
    public static AuthController.TelAndCode VALID_PARAMETER_CODE =
            new AuthController.TelAndCode("13812345678", "000000");
    public static AuthController.TelAndCode EMPTY_TEL =
            new AuthController.TelAndCode(null, null);

    @Test
    public void returnTrueIfValid() {
        Assertions.assertTrue(new TelVerificationService().verifyTelParameters(VALID_PARAMETER));
    }

    @Test
    public void returnFalseIfNoTel() {
        Assertions.assertFalse(new TelVerificationService().verifyTelParameters(EMPTY_TEL));
    }

    @Test
    public void returnFalseNullParameter() {
        Assertions.assertFalse(new TelVerificationService().verifyTelParameters(null));
    }
}
