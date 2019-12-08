package io.revolut.moneytransfer.util;

import com.google.gson.Gson;

import io.revolut.moneytransfer.domain.ApiResponseBean;
import spark.Response;

public class JsonHelper {
    public static <T> T marshal(String jsonString, Class<T> classType) {
        return new Gson().fromJson(jsonString, classType);
    }
    
    public static String unmarshal (Object data){
        return new Gson().toJson(data);
    }
    
    public static String constructResponse(Response response, ApiResponseBean apiResponse) {
        response.type("application/json");
        response.status(apiResponse.getStatusCode());
        return (apiResponse.getApiResponseBody().isEmpty()) ? apiResponse.getErrorReason() : apiResponse.getApiResponseBody();
    }

}
