package com.sky.utils;


import com.sky.constant.MessageConstant;
import com.sky.exception.OrderBusinessException;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@Slf4j
public class MapUtil {

    // 项目密钥
    private String ak;
    // 商家地址
    private String shopAddress;

    // 地理编码uri
    private String geocoding;
    // 路线规划uri
    private String driving;

    /**
     * 获取经纬度字符串
     *      从地理编码响应体中获取经纬度并拼接
     * @param address
     * @return
     */
    public String getLatlng(String address){

        // 获取地理编码响应体
        String geocodingBody = getGeocodingBody(address);

        // 创建Json对象并获取 location对象
        JSONObject json = new JSONObject(geocodingBody);
        if(json.getInt("status") != 0){
            throw new OrderBusinessException(MessageConstant.ADDRESS_PARSING_FAILED);
        }
        JSONObject loc = json.getJSONObject("result").getJSONObject("location");

        // 获取坐标属性
        BigDecimal lat = loc.getBigDecimal("lat");
        BigDecimal lng = loc.getBigDecimal("lng");

        return lat + "," + lng;
    }

    public Boolean isOutOfRange(String address){
        // 因为商家地址是固定的，所以这里直接获取商家坐标
        log.info("解析店铺地址坐标");
        String shopLatLng = getLatlng(shopAddress);
        // 获取用户坐标
        log.info("解析用户地址坐标");
        String userLatLng = getLatlng(address);

        // 获取路线规划响应体
        String drivingBody = getDrivingBody(shopLatLng, userLatLng);
        // 创建Json对象并获取 driving对象
        JSONObject json = new JSONObject(drivingBody);
        if(json.getInt("status") != 0){
            throw new OrderBusinessException(MessageConstant.DRIVING_PARSING_FAILED);
        }
        JSONArray jsonArray = json.getJSONObject("result").getJSONArray("routes");
        Integer distance = (Integer) ((JSONObject) jsonArray.get(0)).get("distance");

        // 配送举例超出 5km，则无法配送
        return distance > 5000;
    }

    /**
     * 获取路线规划响应体
     * @param origin: 店铺坐标
     * @param destination: 用户坐标
     * @return
     */
    private String getDrivingBody(String origin, String destination) {

        // 创建uri参数map
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("origin", origin);
        paramMap.put("destination", destination);
        paramMap.put("ak", ak);

        // 执行HttpClient请求，获取响应体
        String body = HttpClientUtil.doGet(driving, paramMap);

        return body;
    }

    /**
     * 获取地理编码响应体
     * @param address
     * @return
     */
    private String getGeocodingBody(String address) {
        // 创建uri参数map
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("address", address);
        paramMap.put("output", "json");
        paramMap.put("ak", ak);

        // 执行HttpClient请求，获取响应体
        String body = HttpClientUtil.doGet(geocoding, paramMap);

        return body;
    }
}
