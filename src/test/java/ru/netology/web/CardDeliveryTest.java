package ru.netology.web;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;

class CardDeliveryTest {

    public String generateDate(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @Test
    public void shouldCheckCardDeliveryOrder() {
        String planningDate = generateDate(13);

        $x("//*[@placeholder=\"Город\"]").setValue("Москва");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(planningDate);
        $x("//*[@name=\"name\"]").setValue("Маслова-Маслова Ольга");
        $x("//*[@name=\"phone\"]").setValue("+79867281447");

        $("[data-test-id=\"agreement\"]").click();
        $(withText("Забронировать")).click();
        $(".notification__content")
                .shouldHave(Condition.text("Встреча успешно забронирована на " + planningDate), Duration.ofSeconds(15));
    }

    @Test
    public void shouldCheckInvalidCity() {
        String planningDate = generateDate(7);

        $x("//*[@placeholder=\"Город\"]").setValue("Шахунья");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(planningDate);
        $x("//*[@name=\"name\"]").setValue("Маслова-Маслова Ольга");
        $x("//*[@name=\"phone\"]").setValue("+79867281447");

        $("[data-test-id=\"agreement\"]").click();
        $(withText("Забронировать")).click();
        $("[data-test-id=\"notification\"]").should(Condition.not(Condition.visible));
        $(byText("Доставка в выбранный город недоступна")).shouldBe(Condition.visible);
    }

    @Test
    public void shouldCheckInvalidDate() {
        String planningDate = generateDate(2);

        $x("//*[@placeholder=\"Город\"]").setValue("Москва");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(planningDate);
        $x("//*[@name=\"name\"]").setValue("Маслова-Маслова Ольга");
        $x("//*[@name=\"phone\"]").setValue("+79867281447");

        $("[data-test-id=\"agreement\"]").click();
        $(withText("Забронировать")).click();
        $("[data-test-id=\"notification\"]").should(Condition.not(Condition.visible));
        $(byText("Заказ на выбранную дату невозможен")).shouldBe(Condition.visible);
    }

    @Test
    public void shouldCheckInvalidName() {
        String planningDate = generateDate(5);

        $x("//*[@placeholder=\"Город\"]").setValue("Москва");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(planningDate);
        $x("//*[@name=\"name\"]").setValue("Mas O2/");
        $x("//*[@name=\"phone\"]").setValue("+79867281447");

        $("[data-test-id=\"agreement\"]").click();
        $(withText("Забронировать")).click();
        $("[data-test-id=\"notification\"]").should(Condition.not(Condition.visible));
        $(withText("Имя и Фамилия указаные неверно")).shouldBe(Condition.visible);
    }

    @Test
    public void shouldCheckInvalidPhone() {
        String planningDate = generateDate(5);

        $x("//*[@placeholder=\"Город\"]").setValue("Москва");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(planningDate);
        $x("//*[@name=\"name\"]").setValue("Маслова-Маслова Ольга");
        $x("//*[@name=\"phone\"]").setValue("+7999867281447");

        $("[data-test-id=\"agreement\"]").click();
        $(withText("Забронировать")).click();
        $("[data-test-id=\"notification\"]").should(Condition.not(Condition.visible));
        $(withText("Телефон указан неверно")).shouldBe(Condition.visible);
    }

    @Test
    public void shouldCheckInvalidCheckbox() {
        String planningDate = generateDate(5);

        $x("//*[@placeholder=\"Город\"]").setValue("Москва");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id='date'] input").setValue(planningDate);
        $x("//*[@name=\"name\"]").setValue("Маслова-Маслова Ольга");
        $x("//*[@name=\"phone\"]").setValue("+79867281447");

        $("[data-test-id=\"agreement\"]");
        $(withText("Забронировать")).click();
        $("[data-test-id=\"notification\"]").should(Condition.not(Condition.visible));
        $(withText("Я соглашаюсь с условиями")).shouldBe(Condition.visible);
    }
}