package com.demo.controller;

import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.demo.dto.NCafe;
import com.demo.dto.NShopping;
import com.demo.dto.NaverCafeApi;
import com.demo.dto.NaverShoppingApi;
import com.demo.service.RecipeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class ShoppingController {
	
	@Value("${naver.client.id}")
    private String clientId;

    @Value("${naver.client.secret}")
    private String clientSecret;
    
    @Autowired
    private RecipeService recipeService;

    @GetMapping("/recipes")
    public String getRecipes(Model model) {
        Map<String, Object> recipes = recipeService.getRecipes();
        model.addAttribute("recipes", recipes);
        return "guide/campfood";
    }
    
    @GetMapping("/magazine")
    public String getMagazine() {
        return "guide/campmagazine";
    }
    
    @GetMapping("/enjoy")
    public String getEnjoy() {
        return "guide/campenjoy";
    }
    
    @GetMapping("/step")
   	public String gostep() {
   		 
   		return "guide/campingstep";
   	}
    
    
    @GetMapping("/shopping")
	public String goBookpage() {
		 
		return "Shopping/shoppingList";
	}
    
    @GetMapping("/api/chat")
    public String go_chat() {
        return "Chat";
    }
    
    @GetMapping("/detailrecipe")
    public String getDetailRecipes(@RequestParam("seq") int seq, Model model) {
        Map<String, Object> recipes = recipeService.getRecipes();
        List<Map<String, String>> filteredRecipes = filterRecipesBySeq(recipes, seq);
        if (filteredRecipes != null && !filteredRecipes.isEmpty()) {
            model.addAttribute("recipe", filteredRecipes.get(0));
        } else {
            model.addAttribute("recipe", null);
        }
        return "guide/fooddetail";
    }

    private List<Map<String, String>> filterRecipesBySeq(Map<String, Object> recipes, int seq) {
        if (recipes == null || !recipes.containsKey("COOKRCP01")) {
            return null;
        }
        Map<String, Object> cookRcp = (Map<String, Object>) recipes.get("COOKRCP01");
        List<Map<String, String>> allRecipes = (List<Map<String, String>>) cookRcp.get("row");
        return allRecipes.stream()
                .filter(recipe -> recipe.get("RCP_SEQ") != null && Integer.parseInt(recipe.get("RCP_SEQ")) == seq)
                .collect(Collectors.toList());
    }

	
	//네이버 쇼핑 api	
    @GetMapping("/shoppingsearch")
    public String list(@RequestParam("searchKeyword") String searchKeyword, Model model) {
        URI uri = UriComponentsBuilder
            .fromUriString("https://openapi.naver.com")
            .path("/v1/search/shop.json")
            .queryParam("query", searchKeyword + "캠핑")
            .queryParam("display", 10)
            .queryParam("start", 1)
            .queryParam("sort", "sim")
            .encode()
            .build()
            .toUri();

        RequestEntity<Void> req = RequestEntity
            .get(uri)
            .header("X-Naver-Client-Id", clientId)
            .header("X-Naver-Client-Secret", clientSecret)
            .build();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> resp = restTemplate.exchange(req, String.class);

        ObjectMapper om = new ObjectMapper();
        NaverShoppingApi resultVO = null;

        try {
            resultVO = om.readValue(resp.getBody(), NaverShoppingApi.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        List<NShopping> shopping = resultVO.getItems();
        model.addAttribute("shoppings", shopping);
        model.addAttribute("searchKeyword", searchKeyword);

        return "Shopping/shoppingResult :: #shopping_list";
    }



    // 네이버 카페글 api 
    @GetMapping("/cafesearch")
    public String cafelist(Model model) {
        URI uri = UriComponentsBuilder
            .fromUriString("https://openapi.naver.com")
            .path("/v1/search/cafearticle.json")
            .queryParam("query", "캠핑tip")
            .queryParam("display", 10)
            .queryParam("start", 1)
            .encode()
            .build()
            .toUri();

        RequestEntity<Void> req = RequestEntity
            .get(uri)
            .header("X-Naver-Client-Id", clientId)
            .header("X-Naver-Client-Secret", clientSecret)
            .build();

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> resp = restTemplate.exchange(req, String.class);

        ObjectMapper om = new ObjectMapper();
        NaverCafeApi resultVO = null;

        try {
            resultVO = om.readValue(resp.getBody(), NaverCafeApi.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        List<NCafe> cafes = resultVO.getItems();
        for (NCafe cafe : cafes) {
            cafe.setTitle(Jsoup.clean(cafe.getTitle(), Safelist.none()));
        }	
        model.addAttribute("cafes", cafes);

        return "guide/magazineResult :: #campingTip";
    }

    
    // 네이버 카페 글내용 크롤링해오기
    @GetMapping("/cafecontent")
    @ResponseBody
    public String getCafeContent(@RequestParam String url) {
        try {
            // ChromeDriver 경로
            System.setProperty("webdriver.chrome.driver",
                    "C:/Users/tiger/Downloads/chromedriver-win64/chromedriver.exe");

            
            ChromeOptions options = new ChromeOptions();
            options.addArguments("--headless"); // 헤드리스 모드

            WebDriver driver = new ChromeDriver(options);

            try {
                
                driver.get(url);

                // 30초 동안 대기
                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30)); 

                // iframe 내부로 이동
                driver.switchTo().frame("cafe_main");

                // JavaScriptExecutor를 사용하여 페이지를 맨 아래로 스크롤
                JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
                jsExecutor.executeScript("window.scrollTo(0, document.body.scrollHeight);");

                // 동적 콘텐츠 로드를 위해 3초 동안 대기
                Thread.sleep(3000);

                //CSS 셀렉터를 사용
                List<String> selectors = List.of(".se-main-container", ".se-component-content", ".se-text");

                
                WebElement mainContainer = null;

                // 셀렉터 리스트를 반복하며 콘텐츠를 찾기
                for (String selector : selectors) {
                    try {
                        // 지정된 셀렉터로 요소를 찾을 때까지 대기
                        mainContainer = wait
                                .until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(selector)));
                        if (mainContainer != null) {
                            System.out.println("Found element with selector: " + selector);
                            break;
                        }
                    } catch (Exception ignored) {
                        // 무시하고 다음 셀렉터 시도
                    }
                }


                if (mainContainer == null) {
                    String pageSource = driver.getPageSource();
                    return "No content found with the provided selectors. Page source: " + pageSource;
                }

                // 제목변환
                String content = mainContainer.getText().replace("\n", "<br>");
                return content;

            } catch (Exception e) {
                e.printStackTrace();
                return "Error retrieving content";
            } finally {
                driver.quit();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Error setting up ChromeDriver";
        }
    }
}

