package com.coffee.roastnbrew.services;

import com.coffee.roastnbrew.dtos.SentimentAnalysisRequest;
import com.coffee.roastnbrew.exceptions.CoffeeException;
import com.coffee.roastnbrew.models.feedbacks.FeedbackContent;
import com.coffee.roastnbrew.utils.JSONUtils;
import com.coffee.roastnbrew.utils.RestUtils;
import org.jvnet.hk2.annotations.Service;

import javax.inject.Singleton;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Singleton
public class SentimentAnalysisService {
    private final String url = "https://api.monkeylearn.com/v3/classifiers/cl_pi3C7JiL/classify/";
    private static final String NEGATIVE = "Negative";
    private static final String POSITIVE = "Positive";
    private static final String NEUTRAL = "Neutral";

    public boolean isPositiveContent(FeedbackContent content) throws CoffeeException {
        List<String> texts = new ArrayList<>();
        texts.add(content.getStartText());
        texts.add(content.getContinueText());
        texts.add(content.getStopText());
        texts.add(content.getMessage());
        SentimentAnalysisRequest request = new SentimentAnalysisRequest(texts);
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Token d959e8806b6eb14f9f1b635b7caf45c75c531125");
        Response response = RestUtils.request(url, RestUtils.REQUEST_METHOD_POST, request, null, headers);
        return isPositiveClassification(response);
    }

    private boolean isPositiveClassification(Response response) {
        String responseBody = (String) response.getEntity();

        JsonArray responseArray = JSONUtils.jsonStringToJsonArray(responseBody);
        if (responseArray.isEmpty()) {
            return true;
        }

        for (int i = 0; i < responseArray.size(); i++) {
            JsonObject object = responseArray.getJsonObject(i);
            JsonArray classifications = object.getJsonArray("classifications");
            String tag = classifications.getJsonObject(0).getString("tag_name");
            if (NEGATIVE.equalsIgnoreCase(tag)) {
                return false;
            }
        }

        return true;

    }
}
