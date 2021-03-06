package cn.wensiqun.asmsupport.sample.client.json.demo;

import cn.wensiqun.asmsupport.sample.client.json.JSONPool;

import java.io.IOException;
import java.util.HashMap;

public class Runner {

    public static void main(String... args) throws IOException {
        JSONPool pool = new JSONPool();
        
        City city = new City();
        city.setConfidence(100);
        city.setName("Beijing");
        
        Country china = new Country();
        china.setConfidence(10);
        china.setIsoCode("CN");
        china.setInfos(new HashMap<String, Object>());
        china.getInfos().put("capital", city);
        china.getInfos().put("region-count", 32);

        System.out.println("Country info is          : " + pool.getJson(china.getInfos()));
        System.out.println("Country json is           : " + pool.getJson(china));
        
        Geo geo = new Geo();
        geo.setCity(city);
        geo.setCountry(china);
        
        Subdivision sub1 = new Subdivision();
        sub1.setConfidence(1);
        sub1.setIsoCode("JX");
        Subdivision sub2 = new Subdivision();
        sub2.setConfidence(2);
        sub2.setIsoCode("HN");
        geo.getSubdivisions().add(sub1);
        geo.getSubdivisions().add(sub2);

        System.out.println("Subdivisions json is      : " + pool.getJson(geo.getSubdivisions()));
        
        geo.setOtherSubdivisions(new Subdivision[]{sub2, sub1});

        System.out.println("OtherSubdivisions json is : " + pool.getJson(geo.getOtherSubdivisions()));
        System.out.println("Success generate json.demo.Subdivision.class Json Generator.");
        
        Country usa = new Country();
        usa.setConfidence(20);
        usa.setIsoCode("USA");
        geo.setRegisteredCountry(usa);

        System.out.println("Geo json is               : " + pool.getJson(geo));
        System.out.println("Success generate json.demo.Geo.class Json Generator.");
        
    }
    
}
