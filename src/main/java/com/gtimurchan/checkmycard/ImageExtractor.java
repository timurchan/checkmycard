package com.gtimurchan.checkmycard;

import com.gtimurchan.checkmycard.webpagescraper.IWebPageScraper;
import com.gtimurchan.checkmycard.webpagescraper.WebPageScraper_ChromeDriver;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImageExtractor {

    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(ImageExtractor.class);

    public Collection<String> extractImageUrls(String url) throws IOException {
        Collection<String> imageUrls = new ArrayList<>();

//        IWebPageScraper pageScraper = new WebPageScraper_HtmlUnitDriver(url);
        IWebPageScraper pageScraper = new WebPageScraper_ChromeDriver();
        imageUrls = pageScraper.execute(url);


        LOG.debug("imageUrls {}", imageUrls);

        return imageUrls;
    }

    private String extractImageUrl(String html) {
        // Define regular expression pattern to extract image URLs
        Pattern pattern = Pattern.compile("<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>");
        Matcher matcher = pattern.matcher(html);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

//    imageUrls.add("/img/local-image.jpg");  // Локальное изображение
//    imageUrls.add("https://example.com/image1.jpg");
    private Collection<String> enrichWithCustomImage(Collection<String> imageUrls) {
        final int cycleNumber = 10;
        Random random = new Random();

        String custom_image_name = "ksenia_timur_green-2.jpg";
        String custom_image_path = "/img/" + custom_image_name;

        Collection<String> output = new ArrayList<>();

        int counter = 0;
        int randomNumber = random.nextInt(cycleNumber);
        System.out.println("randomNumber: " + randomNumber);

        for(String imageUrl : imageUrls) {
            if(counter == randomNumber) {
                output.add(custom_image_path);
            } else {
                output.add(imageUrl);
            }
            counter++;
            if(counter==cycleNumber) {
                counter=0;
                randomNumber = random.nextInt(cycleNumber);
                System.out.println("randomNumber: " + randomNumber);
            }
        }

        return output;
    }


    public Collection<String> execute(String inputUrl, String searchString) {
        try {
            String url = inputUrl + searchString;
            LOG.debug("Processed url: {}", url);
            System.out.println("Processed url: " + url);

            Collection<String> imageUrls = extractImageUrls(url);
//            Collection<String> imageUrls = emulateImageExtracting();
            System.out.println("imageUrls: " + imageUrls);

            imageUrls = enrichWithCustomImage(imageUrls);
            System.out.println("imageUrls: " + imageUrls);

            return imageUrls;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Collection<String> execute(String searchString) {
        return execute(Const.WEB_URL, searchString);
    }

    private Collection<String> emulateImageExtracting() throws  IOException {
        String[] urlsArray = {
                "https://basket-06.wbbasket.ru/vol1026/part102614/102614258/images/c516x688/1.jpg"
                , "https://basket-12.wbbasket.ru/vol1708/part170882/170882655/images/c516x688/1.jpg"
                , "https://basket-10.wbbasket.ru/vol1347/part134793/134793234/images/c516x688/1.jpg"
                , "https://basket-09.wbbasket.ru/vol1231/part123197/123197608/images/c516x688/1.jpg"
                , "https://basket-06.wbbasket.ru/vol1026/part102614/102614257/images/c516x688/1.jpg"
                , "https://basket-12.wbbasket.ru/vol1836/part183664/183664399/images/c516x688/1.jpg"
                , "https://basket-10.wbbasket.ru/vol1503/part150374/150374643/images/c516x688/1.jpg"
                , "https://basket-02.wbbasket.ru/vol255/part25582/25582261/images/c516x688/1.jpg"
                , "https://basket-12.wbbasket.ru/vol1709/part170915/170915545/images/c516x688/1.jpg"
                , "https://basket-12.wbbasket.ru/vol1836/part183650/183650716/images/c516x688/1.jpg"
                , "https://basket-06.wbbasket.ru/vol1026/part102614/102614259/images/c516x688/1.jpg"
                , "https://basket-05.wbbasket.ru/vol948/part94883/94883819/images/c516x688/1.jpg"
                , "https://basket-05.wbbasket.ru/vol761/part76173/76173902/images/c516x688/1.jpg"
                , "https://basket-08.wbbasket.ru/vol1126/part112616/112616991/images/c516x688/1.jpg"
                , "https://basket-01.wbbasket.ru/vol107/part10724/10724588/images/c516x688/1.jpg"
                , "https://basket-04.wbbasket.ru/vol537/part53785/53785371/images/c516x688/1.jpg"
                , "https://basket-05.wbbasket.ru/vol976/part97622/97622423/images/c516x688/1.jpg"
                , "https://basket-05.wbbasket.ru/vol732/part73293/73293426/images/c516x688/1.jpg"
                , "https://basket-01.wbbasket.ru/vol107/part10724/10724587/images/c516x688/1.jpg"
                , "https://basket-04.wbbasket.ru/vol438/part43845/43845421/images/c516x688/1.jpg"
                , "https://basket-05.wbbasket.ru/vol776/part77646/77646991/images/c516x688/1.jpg"
                , "https://basket-12.wbbasket.ru/vol1806/part180641/180641163/images/c516x688/1.jpg"
                , "https://basket-09.wbbasket.ru/vol1231/part123197/123197611/images/c516x688/1.jpg"
                , "https://basket-03.wbbasket.ru/vol349/part34984/34984441/images/c516x688/1.jpg"
                , "https://basket-12.wbbasket.ru/vol1836/part183658/183658282/images/c516x688/1.jpg"
                , "https://basket-12.wbbasket.ru/vol1671/part167197/167197725/images/c516x688/1.jpg"
                , "https://basket-03.wbbasket.ru/vol377/part37764/37764586/images/c516x688/1.jpg"
                , "https://basket-10.wbbasket.ru/vol1398/part139889/139889216/images/c516x688/1.jpg"
                , "https://basket-11.wbbasket.ru/vol1604/part160446/160446944/images/c516x688/1.jpg"
                , "https://basket-09.wbbasket.ru/vol1230/part123067/123067506/images/c516x688/1.jpg"
                , "https://basket-04.wbbasket.ru/vol617/part61752/61752635/images/c516x688/1.jpg"
                , "https://basket-04.wbbasket.ru/vol506/part50698/50698783/images/c516x688/1.jpg"
                , "https://basket-13.wbbasket.ru/vol1980/part198066/198066879/images/c516x688/1.jpg"
                , "https://basket-12.wbbasket.ru/vol1806/part180641/180641950/images/c516x688/1.jpg"
                , "https://basket-04.wbbasket.ru/vol506/part50698/50698782/images/c516x688/1.jpg"
                , "https://basket-10.wbbasket.ru/vol1369/part136906/136906342/images/c516x688/1.jpg"
                , "https://basket-08.wbbasket.ru/vol1145/part114597/114597829/images/c516x688/1.jpg"
                , "https://basket-03.wbbasket.ru/vol340/part34004/34004040/images/c516x688/1.jpg"
                , "https://basket-13.wbbasket.ru/vol1985/part198533/198533135/images/c516x688/1.jpg"
                , "https://basket-04.wbbasket.ru/vol670/part67094/67094985/images/c516x688/1.jpg"
                , "https://basket-12.wbbasket.ru/vol1774/part177456/177456890/images/c516x688/1.jpg"
                , "https://basket-10.wbbasket.ru/vol1392/part139202/139202223/images/c516x688/1.jpg"
                , "https://basket-01.wbbasket.ru/vol107/part10724/10724590/images/c516x688/1.jpg"
                , "https://basket-03.wbbasket.ru/vol358/part35884/35884969/images/c516x688/1.jpg"
                , "https://basket-04.wbbasket.ru/vol515/part51522/51522700/images/c516x688/1.jpg"
                , "https://basket-12.wbbasket.ru/vol1681/part168128/168128531/images/c516x688/1.jpg"
                , "https://basket-12.wbbasket.ru/vol1681/part168128/168128530/images/c516x688/1.jpg"
                , "https://basket-13.wbbasket.ru/vol1958/part195822/195822284/images/c516x688/1.jpg"
                , "https://basket-03.wbbasket.ru/vol390/part39072/39072147/images/c516x688/1.jpg"
                , "https://basket-01.wbbasket.ru/vol126/part12650/12650093/images/c516x688/1.jpg"
                , "https://basket-09.wbbasket.ru/vol1310/part131007/131007274/images/c516x688/1.jpg"
                , "https://basket-05.wbbasket.ru/vol881/part88121/88121434/images/c516x688/1.jpg"
                , "https://basket-10.wbbasket.ru/vol1460/part146016/146016479/images/c516x688/1.jpg"
                , "https://basket-09.wbbasket.ru/vol1253/part125300/125300199/images/c516x688/1.jpg"
                , "https://basket-05.wbbasket.ru/vol933/part93387/93387969/images/c516x688/1.jpg"
                , "https://basket-04.wbbasket.ru/vol575/part57575/57575056/images/c516x688/1.jpg"
                , "https://basket-10.wbbasket.ru/vol1330/part133056/133056678/images/c516x688/1.jpg"
                , "https://basket-09.wbbasket.ru/vol1231/part123197/123197609/images/c516x688/1.jpg"
                , "https://basket-03.wbbasket.ru/vol356/part35672/35672352/images/c516x688/1.jpg"
                , "https://basket-01.wbbasket.ru/vol133/part13325/13325986/images/c516x688/1.jpg"
                , "https://basket-04.wbbasket.ru/vol537/part53784/53784820/images/c516x688/1.jpg"
                , "https://basket-09.wbbasket.ru/vol1310/part131007/131007280/images/c516x688/1.jpg"
                , "https://basket-04.wbbasket.ru/vol617/part61752/61752637/images/c516x688/1.jpg"
                , "https://basket-10.wbbasket.ru/vol1586/part158681/158681896/images/c516x688/1.jpg"
                , "https://basket-10.wbbasket.ru/vol1330/part133030/133030456/images/c516x688/1.jpg"
                , "https://basket-04.wbbasket.ru/vol547/part54721/54721439/images/c516x688/1.jpg"
                , "https://basket-04.wbbasket.ru/vol482/part48273/48273404/images/c516x688/1.jpg"
                , "https://basket-07.wbbasket.ru/vol1100/part110063/110063262/images/c516x688/1.jpg"
                , "https://basket-10.wbbasket.ru/vol1586/part158689/158689255/images/c516x688/1.jpg"
                , "https://basket-04.wbbasket.ru/vol670/part67094/67094986/images/c516x688/1.jpg"
                , "https://basket-09.wbbasket.ru/vol1310/part131007/131007276/images/c516x688/1.jpg"
                , "https://basket-05.wbbasket.ru/vol982/part98270/98270102/images/c516x688/1.jpg"
                , "https://basket-05.wbbasket.ru/vol784/part78413/78413285/images/c516x688/1.jpg"
                , "https://basket-04.wbbasket.ru/vol540/part54019/54019647/images/c516x688/1.jpg"
                , "https://basket-03.wbbasket.ru/vol371/part37149/37149186/images/c516x688/1.jpg"
                , "https://basket-04.wbbasket.ru/vol703/part70311/70311187/images/c516x688/1.jpg"
                , "https://basket-06.wbbasket.ru/vol1038/part103880/103880662/images/c516x688/1.jpg"
                , "https://basket-07.wbbasket.ru/vol1070/part107009/107009041/images/c516x688/1.jpg"
                , "https://basket-04.wbbasket.ru/vol502/part50296/50296055/images/c516x688/1.jpg"
                , "https://basket-04.wbbasket.ru/vol479/part47905/47905569/images/c516x688/1.jpg"
                , "https://basket-04.wbbasket.ru/vol475/part47566/47566738/images/c516x688/1.jpg"
                , "https://basket-04.wbbasket.ru/vol704/part70474/70474897/images/c516x688/1.jpg"
                , "https://basket-03.wbbasket.ru/vol416/part41671/41671354/images/c516x688/1.jpg"
                , "https://basket-10.wbbasket.ru/vol1567/part156773/156773906/images/c516x688/1.jpg"
                , "https://basket-04.wbbasket.ru/vol532/part53247/53247143/images/c516x688/1.jpg"
                , "https://basket-12.wbbasket.ru/vol1753/part175382/175382262/images/c516x688/1.jpg"
                , "https://basket-04.wbbasket.ru/vol536/part53631/53631741/images/c516x688/1.jpg"
                , "https://basket-07.wbbasket.ru/vol1100/part110063/110063263/images/c516x688/1.jpg"
                , "https://basket-01.wbbasket.ru/vol41/part4138/4138055/images/c516x688/1.jpg"
                , "https://basket-14.wbbasket.ru/vol2184/part218479/218479992/images/c516x688/1.jpg"
                , "https://basket-06.wbbasket.ru/vol1025/part102547/102547939/images/c516x688/1.jpg"
                , "https://basket-10.wbbasket.ru/vol1475/part147504/147504421/images/c516x688/1.jpg"
                , "https://basket-04.wbbasket.ru/vol703/part70310/70310115/images/c516x688/1.jpg"
                , "https://basket-02.wbbasket.ru/vol247/part24777/24777992/images/c516x688/1.jpg"
                , "https://basket-09.wbbasket.ru/vol1182/part118261/118261402/images/c516x688/1.jpg"
                , "https://basket-12.wbbasket.ru/vol1753/part175383/175383079/images/c516x688/1.jpg"
                , "https://basket-13.wbbasket.ru/vol2009/part200959/200959791/images/c516x688/1.jpg"
                , "https://basket-03.wbbasket.ru/vol398/part39813/39813573/images/c516x688/1.jpg"
                , "https://basket-08.wbbasket.ru/vol1153/part115316/115316312/images/c516x688/1.jpg"
                , "https://basket-05.wbbasket.ru/vol930/part93076/93076079/images/c516x688/1.jpg"
                , "https://basket-02.wbbasket.ru/vol192/part19252/19252625/images/c516x688/1.jpg"
                , "https://basket-04.wbbasket.ru/vol562/part56236/56236410/images/c516x688/1.jpg"
                , "https://basket-05.wbbasket.ru/vol939/part93986/93986834/images/c516x688/1.jpg"
                , "https://basket-02.wbbasket.ru/vol211/part21154/21154521/images/c516x688/1.jpg"
                , "https://basket-10.wbbasket.ru/vol1416/part141663/141663193/images/c516x688/1.jpg"
                , "https://basket-10.wbbasket.ru/vol1488/part148825/148825454/images/c516x688/1.jpg"
                , "https://basket-01.wbbasket.ru/vol65/part6583/6583968/images/c516x688/1.jpg"
                , "https://basket-05.wbbasket.ru/vol935/part93550/93550775/images/c516x688/1.jpg"
                , "https://basket-12.wbbasket.ru/vol1666/part166670/166670400/images/c516x688/1.jpg"
                , "https://basket-10.wbbasket.ru/vol1565/part156598/156598758/images/c516x688/1.jpg"
                , "https://basket-13.wbbasket.ru/vol1995/part199543/199543967/images/c516x688/1.jpg"
                , "https://basket-01.wbbasket.ru/vol90/part9000/9000213/images/c516x688/1.jpg"
                , "https://basket-12.wbbasket.ru/vol1690/part169071/169071771/images/c516x688/1.jpg"
                , "https://basket-09.wbbasket.ru/vol1207/part120772/120772856/images/c516x688/1.jpg"
                , "https://basket-12.wbbasket.ru/vol1666/part166674/166674699/images/c516x688/1.jpg"
                , "https://basket-05.wbbasket.ru/vol856/part85697/85697332/images/c516x688/1.jpg"
                , "https://basket-12.wbbasket.ru/vol1750/part175088/175088486/images/c516x688/1.jpg"
                , "https://basket-10.wbbasket.ru/vol1430/part143087/143087058/images/c516x688/1.jpg"
                , "https://basket-12.wbbasket.ru/vol1674/part167479/167479534/images/c516x688/1.jpg"
                , "https://basket-05.wbbasket.ru/vol972/part97286/97286739/images/c516x688/1.jpg"
                , "https://basket-10.wbbasket.ru/vol1563/part156376/156376245/images/c516x688/1.jpg"
        };
        List<String> urlsList = new ArrayList<>(Arrays.asList(urlsArray));
        return urlsList;
    }
}