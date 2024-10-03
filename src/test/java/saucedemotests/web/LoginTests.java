package saucedemotests.web;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import saucedemotests.core.SauceDemoBaseWebTest;
import saucedemotests.enums.TestData;

public class LoginTests extends SauceDemoBaseWebTest   {


    @ParameterizedTest
    @EnumSource(TestData.class)
    public void userAuthenticated_when_validCredentialsProvided(TestData user) {
        loginPage.navigate();

        try {
            loginPage.submitLoginForm(user.getValue(), TestData.STANDARD_USER_PASSWORD.getValue());

            inventoryPage.waitForPageTitle();
            inventoryPage.assertNavigated();

        } catch (Exception e) {

            System.out.println("Login failed for user: " + user.getValue());
        }
    }
}
